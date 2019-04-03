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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.FetchPath;
import com.reatchall.charan.reatchallVendor.Utils.ImageCompression.Compressor;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.VendorBizBannersAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BannerImages;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ImageUpload;
import com.reatchall.charan.reatchallVendor.Vendor.VendorBusinessSettingsNewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorBizBannersFragment extends Fragment {

    private static final String TAG = "VendorBizBannersFragmen";
    View view;
    BusinessDetails businessDashboard;
    ReatchAll helper = ReatchAll.getInstance();
    Context context;
    CustomProgressDialog customProgressDialog;
    PrefManager prefManager;

    boolean allgranted = false;

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    AmazonS3Client s3;
    BasicAWSCredentials credentials;
    TransferUtility transferUtility;
    TransferObserver observer;
    File selectedFilePath;

    RecyclerView itemImagesRcv;
    LinearLayout uploadImages;

    FontTextView saveLogo;

    public VendorBizBannersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_vendor_biz_banners, container, false);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        businessDashboard = getArguments().getParcelable("businessDetails");
        context = getActivity();
        VendorBusinessSettingsNewActivity vendorBusinessSettingsNewActivity = (VendorBusinessSettingsNewActivity)context;
        vendorBusinessSettingsNewActivity.highlightTab(1);
        customProgressDialog = new CustomProgressDialog(context);
        prefManager = new PrefManager(context);
        itemImagesRcv=(RecyclerView)view.findViewById(R.id.itemImagesRcv);
        itemImagesRcv.setNestedScrollingEnabled(false);
        itemImagesRcv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true));
        vendorBizBannersAdapter = new VendorBizBannersAdapter(context,arrayList,VendorBizBannersFragment.this);
        itemImagesRcv.setAdapter(vendorBizBannersAdapter);
        saveLogo = (FontTextView)view.findViewById(R.id.saveLogo);

        customProgressDialog.showDialog();
        getImages();

        setupAws();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            ActivityCompat.requestPermissions(getActivity(),permissionsRequired,PERMISSION_CALLBACK_CONSTANT);


        saveLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedImagesFiles.size()>0){
                    customProgressDialog.showDialog();
                    compressImagesNupload();
                }else{
                    customProgressDialog.showDialog();
                    formString();
                }
            }
        });

        uploadImages=(LinearLayout)view.findViewById(R.id.uploadImages);
        uploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(getPickImageChooserIntent(), 201);

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
                        arrayList = new ArrayList<>();
                        JSONObject basicDetails = response.getJSONObject("msg").getJSONObject("banners");
                        if(basicDetails.has("temp_banners")){
                            //Glide.with(context).load(new URL(basicDetails.getJSONObject("temp_logo").getString("url"))).into();
                            JSONArray tempBanners = basicDetails.getJSONArray("temp_banners");
                            for(int i =0;i<tempBanners.length();i++){
                                arrayList.add(new BannerImages(null,tempBanners.getJSONObject(i).getString("url"),false));
                                selectedImagesFiles.add(null);
                            }
                        }

                        if(basicDetails.has("banners")){
                            //Glide.with(context).load(new URL(basicDetails.getJSONObject("temp_logo").getString("url"))).into();
                            JSONArray tempBanners = basicDetails.getJSONArray("banners");
                            for(int i =0;i<tempBanners.length();i++){
                                arrayList.add(new BannerImages(null,tempBanners.getJSONObject(i).getString("url"),true));
                                selectedImagesFiles.add(null);
                            }
                        }

                        vendorBizBannersAdapter = new VendorBizBannersAdapter(context,arrayList,VendorBizBannersFragment.this);
                        itemImagesRcv.setAdapter(vendorBizBannersAdapter);
                        vendorBizBannersAdapter.notifyDataSetChanged();
                    }else{
                        Toast.makeText(context,"Couldn't fetch images",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
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

    public void deleteImage(int imagePosition) {
        arrayList.remove(imagePosition);
        if(selectedImagesFiles!=null){
            if(selectedImagesFiles.size()>0){
                selectedImagesFiles.remove(imagePosition);
            }
        }
        vendorBizBannersAdapter.notifyDataSetChanged();
        //productImagesAdapter.notifyItemRemoved(imagePosition);
    }


    private void setupAws(){
        credentials = new BasicAWSCredentials(Constants.S3_ACCESS_KEY_ID,Constants.S3_SECRET_ACCESS_KEY);
        s3 = new AmazonS3Client(credentials);
        s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
        transferUtility = new TransferUtility(s3, context);
    }

    String imgKey;
    File compressedFile;
    ArrayList<File> compressedFilesList ;
    ArrayList<ImageUpload> imageUploadArrayList = new ArrayList<>();

    private void compressImagesNupload(){
        compressedFilesList = new ArrayList<>();
        imageUploadArrayList = new ArrayList<>();

        Log.e(TAG, "compressImagesNupload: "+selectedImagesFiles.size());
        for(int i =0;i<selectedImagesFiles.size();i++){
            try {
                if(selectedImagesFiles.get(i)==null){
                    String getfromdate = arrayList.get(i).getImgUrl().trim();
                    String getfrom[] = getfromdate.split("https://categorybucket.s3.amazonaws.com/");
                    imageUploadArrayList.add(new ImageUpload(arrayList.get(i).getImgUrl(),getfrom[1]));
                    compressedFilesList.add(null);
                }else{
                    File cmpFile = new Compressor(context).compressToFile(selectedImagesFiles.get(i),businessDashboard.getBusinessName().trim()+Calendar.getInstance().getTimeInMillis());
                    if(cmpFile.exists()){
                        Log.e(TAG, "compressImagesNupload: CMP EXISTS" );
                        compressedFilesList.add(cmpFile);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(compressedFilesList.size()==selectedImagesFiles.size()){
            Log.e(TAG, "compressImagesNupload: EQUALS" );
            findUploadFiles( 0);
        }
    }

    private void findUploadFiles(int i){
        Log.e(TAG, "findUploadFiles: "+i );
        if(i<compressedFilesList.size()){
            if(compressedFilesList.get(i)!=null)
            {
                uploadImageToAws(compressedFilesList.get(i),i);
            } else{
                Log.e(TAG, "findUploadFiles: NULL" +i);
                findUploadFiles(i+1);
            }
        }else{
            formString();
        }

    }

    private void uploadImageToAws(File selectedFilePath,int filePosition){
        // imgKey = getRandomString(13);
        imgKey = System.currentTimeMillis()+"";
        Log.e(TAG, "uploadImageToAws: "+imgKey );
        observer = transferUtility.upload(
                Constants.S3_BUCKET_CATEGORY,
                imgKey,
                selectedFilePath
        );

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

                if (state.COMPLETED.equals(observer.getState())) {

                    Log.e(TAG, "onStateChanged: UPLOAD COMPLETE" + selectedFilePath.getAbsolutePath() );
                    Log.e(TAG, "onStateChanged: https://"+Constants.S3_BUCKET_CATEGORY+".s3.amazonaws.com/"+observer.getKey());
                    String url = "https://"+Constants.S3_BUCKET_CATEGORY+".s3.amazonaws.com/"+observer.getKey();
                    imageUploadArrayList.add(new ImageUpload(url,observer.getKey()));
                    if(filePosition<compressedFilesList.size()-1){
                        uploadImageToAws(compressedFilesList.get(filePosition+1),filePosition+1);
                    }else{
                        Log.e(TAG, "onStateChanged: FORM STRING" );
                        formString();
                    }
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

    private void formString(){

        try {
            JSONObject postItemJson = new JSONObject();

            postItemJson.put("business_id",businessDashboard.getBusinessId());

            JSONArray itemImagesJsonArray = new JSONArray();
            for(int i=0;i<imageUploadArrayList.size();i++){
                JSONObject itemImageObject = new JSONObject();
                itemImageObject.put("imageurl",imageUploadArrayList.get(i).getUrl());
                itemImageObject.put("key",imageUploadArrayList.get(i).getKey());
                itemImagesJsonArray.put(itemImageObject);
            }
            Log.e(TAG, "formString: "+itemImagesJsonArray.length()+itemImagesJsonArray.toString() );
            postItemJson.put("banner_images",itemImagesJsonArray);


            addItemToBusiness(postItemJson);
        } catch (JSONException e) {
            Log.e(TAG, "formString: "+e.getMessage() );
            e.printStackTrace();
        }

    }

    private void addItemToBusiness(JSONObject jsonObject){
        String url=Constants.BASE_URL+"vendor_mobile/upload-banner-images-mobile";



        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.POST, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
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

    private static Bitmap rotateImageIfRequired(Bitmap img, File selectedImage){

        ExifInterface ei = null;
        try {
            ei = new ExifInterface(selectedImage.getAbsolutePath());
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
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

    Bitmap myBitmap;
    Uri picUri;

    String filePath;
    ArrayList<BannerImages> arrayList = new ArrayList<>();
    ArrayList<File> selectedImagesFiles = new ArrayList<>();
    VendorBizBannersAdapter vendorBizBannersAdapter;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==201){
            if (resultCode == Activity.RESULT_OK) {
                processSelectedImage(data);
            }
        }
    }

    File imgFile;

    private void processSelectedImage(Intent data){
        if(getPickImageResultUri(data)!=null){
            Log.e(TAG, "processSelectedImage: CAMERA" );

            Uri photoUri = getPickImageResultUri(data);
            if (photoUri != null) {
                filePath  = FetchPath.getPath(context, photoUri);
            }
            imgFile = new  File(filePath);
            if(imgFile.exists()){
                Log.e(TAG, "onActivityResult: EXISTS" );
                Log.e(TAG, "onActivityResult: "+filePath );
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = rotateImageIfRequired(myBitmap, imgFile);
                myBitmap = getResizedBitmap(myBitmap, 500);
            }
        }else{
            Log.e(TAG, "processSelectedImage: PICKER" );
            Uri photoUri = data.getData();
            if (photoUri != null) {
                filePath  = FetchPath.getPath(context, photoUri);
            }
            imgFile = new  File(filePath);
            if(imgFile.exists()){
                Log.e(TAG, "onActivityResult: EXISTS" );
                Log.e(TAG, "onActivityResult: "+filePath );
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = rotateImageIfRequired(myBitmap, imgFile);
                myBitmap = getResizedBitmap(myBitmap, 500);
            }
        }

        arrayList.add(new BannerImages(myBitmap,"",false));
        selectedImagesFiles.add(imgFile);
        vendorBizBannersAdapter.notifyDataSetChanged();
        Log.e(TAG, "onActivityResult: "+arrayList.size() );
        Log.e(TAG, "onActivityResult: "+selectedImagesFiles.size() );

    }


}
