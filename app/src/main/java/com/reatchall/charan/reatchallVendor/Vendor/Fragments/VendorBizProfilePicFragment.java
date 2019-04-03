package com.reatchall.charan.reatchallVendor.Vendor.Fragments;


import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.FetchPath;
import com.reatchall.charan.reatchallVendor.Utils.ImageCompression.Compressor;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.VendorBusinessSettingsNewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorBizProfilePicFragment extends Fragment {

    private static final String TAG = "VendorBizProfilePicFrag";
    View view;

    LinearLayout addImageLayout,addedImageLayout;
    ImageView imageIV,clearImage;
    FontTextView changeImageBtn;
    FontButton saveLogo;
    BusinessDetails businessDashboard;
    ReatchAll helper = ReatchAll.getInstance();
    Context context;
    CustomProgressDialog customProgressDialog;
    PrefManager prefManager;


    Bitmap myBitmap;
    Uri picUri;

    String filePath;
    boolean allgranted = false;

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //S3 bucket
    AmazonS3Client s3;
    BasicAWSCredentials credentials;
    TransferUtility transferUtility;
    TransferObserver observer;
    File selectedFilePath;

    public VendorBizProfilePicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_vendor_biz_profile_pic, container, false);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        businessDashboard = getArguments().getParcelable("businessDetails");
        context = getActivity();
        VendorBusinessSettingsNewActivity vendorBusinessSettingsNewActivity = (VendorBusinessSettingsNewActivity)context;
        vendorBusinessSettingsNewActivity.highlightTab(2);
        customProgressDialog = new CustomProgressDialog(context);
        prefManager = new PrefManager(context);
        addImageLayout =(LinearLayout)view.findViewById(R.id.addImageLayout);
        addedImageLayout=(LinearLayout)view.findViewById(R.id.addedImageLayout);
        imageIV=(ImageView)view.findViewById(R.id.imageIV);
        clearImage=(ImageView)view.findViewById(R.id.clearImage);
        changeImageBtn=(FontTextView) view.findViewById(R.id.changeImageBtn);
        saveLogo=(FontButton) view.findViewById(R.id.saveLogo);
        addedImageLayout.setVisibility(View.GONE);

        if(getActivity()!=null)
        customProgressDialog.showDialog();
        getImages();

        setupAws();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            ActivityCompat.requestPermissions(getActivity(),permissionsRequired,PERMISSION_CALLBACK_CONSTANT);

        addImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(getPickImageChooserIntent(), 200);

            }
        });

        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImageLayout.performClick();
            }
        });

        clearImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addImageLayout.setVisibility(View.VISIBLE);
                addedImageLayout.setVisibility(View.GONE);
                selectedFilePath=null;
            }
        });

        saveLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedFilePath!=null){
                    customProgressDialog.showDialog();
                    compressImage();
                }else{
                    if(addedImageLayout.getVisibility()==View.VISIBLE){
                        Toast.makeText(context,"Saved!",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context,"Please select an image to upload",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void getImages(){
        String url = Constants.BASE_URL+"vendor/get-business-by-id/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        JSONObject basicDetails = response.getJSONObject("msg").getJSONObject("basic_details");
                        if(basicDetails.has("temp_logo")){
                            addImageLayout.setVisibility(View.GONE);
                            addedImageLayout.setVisibility(View.VISIBLE);
                            Glide.with(context).load(new URL(basicDetails.getJSONObject("temp_profile").getString("url"))).into(imageIV);
                        }
                    }else{
                        Toast.makeText(context,"Couldn't fetch images",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onErrorResponse: "+error.getMessage() );
                Toast.makeText(context,"Couldn't fetch images",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"GET_BIZ_LOGOS");
    }

    private void setupAws(){
        credentials = new BasicAWSCredentials(Constants.S3_ACCESS_KEY_ID,Constants.S3_SECRET_ACCESS_KEY);
        s3 = new AmazonS3Client(credentials);
        s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
        transferUtility = new TransferUtility(s3, context);
    }

    String imgKey;
    File compressedFile;
    private void compressImage(){
        try {
            compressedFile = new Compressor(context).compressToFile(selectedFilePath,businessDashboard.getBusinessName()+System.currentTimeMillis());
            if(compressedFile.exists()){
                Log.e(TAG, "compressImagesNupload: CMP EXISTS" );
                //compressedFilesList.add(cmpFile);
                uploadImageToAws();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadImageToAws(){
        //imgKey = businessDashboard.getBusinessId()+
        imgKey = System.currentTimeMillis()+"";
        Log.e(TAG, "uploadImageToAws: "+imgKey );
        observer = transferUtility.upload(
                Constants.S3_BUCKET_CATEGORY,
                imgKey,
                compressedFile
        );

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

                if (state.COMPLETED.equals(observer.getState())) {

                    Log.e(TAG, "onStateChanged: UPLOAD COMPLETE" );
                    Log.e(TAG, "onStateChanged: https://"+Constants.S3_BUCKET_CATEGORY+".s3.amazonaws.com/"+observer.getKey());
                    String url = "https://"+Constants.S3_BUCKET_CATEGORY+".s3.amazonaws.com/"+observer.getKey();
                    postListnew(url,observer.getKey());
                    //Toast.makeText(VendorCreateListActivity.this, "File Upload Complete", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {



                long _bytesCurrent = bytesCurrent;
                long _bytesTotal = bytesTotal;

                float percentage =  ((float)_bytesCurrent /(float)_bytesTotal * 100);
                Log.e("percentage","" +percentage);
                /*pb.setProgress((int) percentage);
                _status.setText(percentage + "%");*/
            }

            @Override
            public void onError(int id, Exception ex) {

                Log.e(TAG, "onStateChanged: UPLOAD"+ex.getMessage());

                //Toast.makeText(MainActivity.this, "" + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postListnew(String imgUrl,String key){
        String url=Constants.BASE_URL+"vendor_mobile/upload-business-profile-image-mobile";

        JSONObject postListJson = new JSONObject();

        try {
            postListJson.put("vendor_id",prefManager.getVendorId(context));
            postListJson.put("business_id",businessDashboard.getBusinessId());
            JSONObject businessProfile = new JSONObject();
            if(imgUrl!=null){
                businessProfile.put("imageurl",imgUrl);
                businessProfile.put("key",key);
            }else{
                businessProfile.put("imageurl","");
                businessProfile.put("key","");
            }
            postListJson.put("profile_image",businessProfile);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.POST, url, postListJson, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onResponse: "+response.toString() );
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(context,"Error",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onErrorResponse: "+error.getMessage() );
            }
        });
        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"UPLOADLOGO"+businessDashboard.getBusinessId());
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PERMISSION_CALLBACK_CONSTANT){
            //check if all permissions are granted
            for(int i=0;i<grantResults.length;i++){
                if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if(!allgranted){
                ActivityCompat.requestPermissions(getActivity(),permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }

        }
    }


    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                mainIntent = intent;
                break;
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }


    /**
     * Get URI to image received from capture by camera.
     */
    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        File getImage = context.getExternalCacheDir();
        if (getImage != null) {
            outputFileUri = Uri.fromFile(new File(getImage.getPath(), "profile.png"));
        }
        return outputFileUri;
    }

    private static Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    /**
     * Get the URI of the selected image from {@link #getPickImageChooserIntent()}.<br/>
     * Will return the correct URI for camera and gallery image.
     *
     * @param data the returned data of the activity result
     */
    public Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }
        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==200){
            Bitmap bitmap;
            if (resultCode == Activity.RESULT_OK) {
                if (getPickImageResultUri(data) != null) {
                    picUri = getPickImageResultUri(data);
                    Log.e(TAG, "onActivityResult: CAMERA" );
                    try {
                        myBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), picUri);
                        myBitmap = rotateImageIfRequired(myBitmap, picUri);
                        myBitmap = getResizedBitmap(myBitmap, 500);
                        imageIV.setImageBitmap(myBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Uri photoUri = data.getData();
                        if (photoUri != null) {
                            filePath  = FetchPath.getPath(context, photoUri);
                        }
                        File imgFile = new  File(filePath);
                        selectedFilePath= imgFile;
                        if(imgFile.exists()){
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            imageIV.setImageBitmap(myBitmap);
                        }
                    }
                } else {
                    Uri photoUri = data.getData();
                    if (photoUri != null) {
                        filePath  = FetchPath.getPath(context, photoUri);
                    }
                    File imgFile = new  File(filePath);
                    selectedFilePath= imgFile;
                    Log.e(TAG, "onActivityResult: SELECT");
                    bitmap = (Bitmap) data.getExtras().get("data");
                    myBitmap = bitmap;
                    imageIV.setImageBitmap(myBitmap);
                }
                addedImageLayout.setVisibility(View.VISIBLE);
                addImageLayout.setVisibility(View.GONE);
            }
        }
    }



}
