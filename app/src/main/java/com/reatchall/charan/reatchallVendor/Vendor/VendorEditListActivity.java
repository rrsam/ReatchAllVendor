package com.reatchall.charan.reatchallVendor.Vendor;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.reatchall.charan.reatchallVendor.Vendor.Models.ListDetailsModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorEditListActivity extends AppCompatActivity {

    private static final String TAG = "VendorEditListActivity";
    ImageView backArrow;
    BusinessDetails businessDashboard = null;
    ListDetailsModel listDetailsModel = null;
    FontTextView titleToolbar;
    String listId=null;
    FontEditText listName;
    Button postListBtn;
    ReatchAll helper = ReatchAll.getInstance();

    CustomProgressDialog customProgressDialog;
    FontTextView yourListsTV,changeImageBtn;
    LinearLayout addImageLayout,addedImageLayout;
    ImageView imageIV;


    Bitmap myBitmap;
    Uri picUri;
    byte[] selectedImageByteData;

    String filePath;
    boolean allgranted = false;

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    //S3 bucket
    AmazonS3Client s3;
    BasicAWSCredentials credentials;
    TransferUtility transferUtility;
    TransferObserver observer;
    File selectedFilePath;

    boolean imageChangeClicked = false;
    boolean deletePresentImage = false;

    ImageView clearImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_edit_list);
        setupAws();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            ActivityCompat.requestPermissions(VendorEditListActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);

        customProgressDialog=new CustomProgressDialog(VendorEditListActivity.this);

        backArrow=(ImageView)findViewById(R.id.back_arrow);
        listName=(FontEditText)findViewById(R.id.listName);
        postListBtn=(Button)findViewById(R.id.createList);
        addImageLayout=(LinearLayout)findViewById(R.id.addImageLayout);
        addedImageLayout=(LinearLayout)findViewById(R.id.addedImageLayout);
        addedImageLayout.setVisibility(View.GONE);
        imageIV=(ImageView)findViewById(R.id.imageIV);
        clearImage=(ImageView)findViewById(R.id.clearImage);
        changeImageBtn=(FontTextView)findViewById(R.id.changeImageBtn);

        yourListsTV=(FontTextView)findViewById(R.id.yourListsTV);
        postListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listName.getText().toString().length()>0){
                    customProgressDialog.showDialog();
                    // postListnew();
                    //addList();

                   /* if(selectedFilePath!=null){
                        uploadImageToAws();
                    }
                    else{
                        postListnew(null,null);
                    }*/
                   updateList();
                }
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        businessDashboard = getIntent().getExtras().getParcelable("businessDetails");
        listDetailsModel = getIntent().getExtras().getParcelable("listDetails");
        if(businessDashboard != null){
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
            Log.e(TAG, "onCreate:ID "+businessDashboard.getBusinessId() +"NAME"+businessDashboard.getBusinessName());
        }

        if(listDetailsModel!=null){
            listName.setText(listDetailsModel.getListName());
            if(listDetailsModel.getImgUrl().isEmpty()){
                addedImageLayout.setVisibility(View.GONE);
                addImageLayout.setVisibility(View.VISIBLE);
            }else{
                addedImageLayout.setVisibility(View.VISIBLE);
                addImageLayout.setVisibility(View.GONE);
                Glide.with(VendorEditListActivity.this).load(listDetailsModel.getImgUrl()).into(imageIV);
            }
        }

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
                deletePresentImage = true;
                selectedFilePath=null;
            }
        });
    }

    private void setupAws(){
        credentials = new BasicAWSCredentials(Constants.S3_ACCESS_KEY_ID,Constants.S3_SECRET_ACCESS_KEY);
        s3 = new AmazonS3Client(credentials);
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));
        transferUtility = new TransferUtility(s3, this);
    }

    String imgKey;
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

                if (TransferState.COMPLETED.equals(observer.getState())) {

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
        String url=Constants.BASE_URL+"vendor_mobile/edit-list-mobile";

        JSONObject postListJson = new JSONObject();
        PrefManager prefManager= new PrefManager(VendorEditListActivity.this);

        try {
            postListJson.put("list_id",listDetailsModel.getListID());
            if(imgUrl!=null){
                postListJson.put("imageurl",imgUrl);
                postListJson.put("key",key);
            }else{
                postListJson.put("imageurl","");
                postListJson.put("key","");
            }
            postListJson.put("list_name",listName.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.POST, url, postListJson, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(VendorEditListActivity.this,"List updated Successfully",Toast.LENGTH_LONG).show();
                        finish();

                    }else{
                        Toast.makeText(VendorEditListActivity.this,"List Not updated. Please try again",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"POST LIST"+businessDashboard.getBusinessId());
    }


    private void updateList(){
        if(selectedFilePath==null){
            if(listDetailsModel.getImgUrl().isEmpty()){
                postListnew(null,null);
            }else{
                if(deletePresentImage){
                    Log.e(TAG, "updateList:REMOVE IMAGE " );
                    postListnew(null,null);
                }else{
                    postListnew(listDetailsModel.getImgUrl(),listDetailsModel.getImgKey());
                }
            }
        }else{
            compressImage();
        }
    }

    File compressedFile;
    private void compressImage(){
        try {
            compressedFile = new Compressor(VendorEditListActivity.this).compressToFile(selectedFilePath,listName.getText().toString().trim()+System.currentTimeMillis());
            if(compressedFile.exists()){
                Log.e(TAG, "compressImagesNupload: CMP EXISTS" );
                //compressedFilesList.add(cmpFile);
                uploadImageToAws();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                ActivityCompat.requestPermissions(VendorEditListActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }

        }
    }

    /**
     * Create a chooser intent to select the source to get image from.<br/>
     * The source can be camera's (ACTION_IMAGE_CAPTURE) or gallery's (ACTION_GET_CONTENT).<br/>
     * All possible sources are added to the intent chooser.
     */
    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = getPackageManager();

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
        File getImage = getExternalCacheDir();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==200){
            Bitmap bitmap;
            if (resultCode == Activity.RESULT_OK) {
                if (getPickImageResultUri(data) != null) {
                    picUri = getPickImageResultUri(data);
                    Log.e(TAG, "onActivityResult: CAMERA" );
                    try {
                        myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                        myBitmap = rotateImageIfRequired(myBitmap, picUri);
                        myBitmap = getResizedBitmap(myBitmap, 500);
                        imageIV.setImageBitmap(myBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Uri photoUri = data.getData();
                        if (photoUri != null) {
                            filePath  = FetchPath.getPath(this, photoUri);
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
                        filePath  = FetchPath.getPath(this, photoUri);
                    }
                    File imgFile = new  File(filePath);
                    selectedFilePath= imgFile;
                    Log.e(TAG, "onActivityResult: SELECT");
                    bitmap = (Bitmap) data.getExtras().get("data");
                    myBitmap = bitmap;
                    imageIV.setImageBitmap(myBitmap);
                }
                imageChangeClicked = true;
                deletePresentImage = true;
                addedImageLayout.setVisibility(View.VISIBLE);
                addImageLayout.setVisibility(View.GONE);
            }


        }





    }
}
