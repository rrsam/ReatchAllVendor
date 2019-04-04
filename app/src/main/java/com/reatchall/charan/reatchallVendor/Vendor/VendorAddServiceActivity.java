package com.reatchall.charan.reatchallVendor.Vendor;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

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
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.VendorDemoImagesAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BannerImages;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ImageUpload;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ScheduleValidity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontCheckBox;
import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorAddServiceActivity extends AppCompatActivity  implements VendorDemoImagesAdapter.OnDeleteImage{

    private static final String TAG = "VendorAddServiceActivity";

    ImageView backArrow;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    Context context;
    BusinessDetails businessDashboard;
    PrefManager prefManager;


    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    FontEditText serviceName,servicePrice,discountValue,moreInfo,serviceDescripiton;
    Spinner discountSpinner;
    FontTextView titleToolbar,serviceFinalPrice;
    RadioGroup rgDemoLayout;
    FontButton addServiceBtn;
    VideoView videoThumbnail;

    LinearLayout imageDemoLayout,videoDemoLayout;

    LinearLayout addImageLayout,addedImageLayout;
    FontTextView changeImageBtn;
    ImageView imageIV;

    Bitmap myBitmap;
    Uri picUri;

    String filePath;
    boolean allgranted = false;

    //S3 bucket
    AmazonS3Client s3;
    BasicAWSCredentials credentials;
    TransferUtility transferUtility;
    TransferObserver observer,observer1;
    File selectedFilePath;
    ImageView clearImage;

    List<String> validityList;
    private String selectedVideoPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_add_service);
        context = VendorAddServiceActivity.this;
        prefManager = new PrefManager(context);
        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
        if(businessDashboard!=null){
            titleToolbar.setText("Add Service");
        }
        customProgressDialog = new CustomProgressDialog(context);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        initAddServiceWidgets();

        scheduleViews();
        setupDemoLayout();
    }

    private void initAddServiceWidgets() {
        serviceName =(FontEditText)findViewById(R.id.serviceNamen);
        servicePrice =(FontEditText)findViewById(R.id.servicePrice);
        serviceFinalPrice = (FontTextView) findViewById(R.id.service_final_price);
        discountValue =(FontEditText)findViewById(R.id.discountValue);
        serviceDescripiton =(FontEditText) findViewById(R.id.serviceDesc);
        moreInfo =(FontEditText)findViewById(R.id.moreInfo);
        discountSpinner =(Spinner) findViewById(R.id.discountSpinner);
        addServiceBtn=(FontButton)findViewById(R.id.addServiceBtn);
        itemImagesRcv=(RecyclerView)findViewById(R.id.itemImagesRcv);

        rgDemoLayout = (RadioGroup) findViewById(R.id.rgServiceDemo);
        imageDemoLayout = (LinearLayout) findViewById(R.id.demoImagesLayout);
        videoDemoLayout = (LinearLayout) findViewById(R.id.demoVideoLayout);

        videoThumbnail = (VideoView) findViewById(R.id.itemVideoThumbnail);

        setupDiscountSpinner();

        itemImagesRcv.setNestedScrollingEnabled(false);
        itemImagesRcv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,true));
        vendorDemoImagesAdapter = new VendorDemoImagesAdapter(context,arrayDemoList, this);
        itemImagesRcv.setAdapter(vendorDemoImagesAdapter);



        setupAws();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            ActivityCompat.requestPermissions(VendorAddServiceActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);

        addImageLayout=(LinearLayout)findViewById(R.id.addImageLayout);
        addedImageLayout=(LinearLayout)findViewById(R.id.addedImageLayout);
        addedImageLayout.setVisibility(View.GONE);
        imageIV=(ImageView)findViewById(R.id.imageIV);
        clearImage=(ImageView)findViewById(R.id.clearImage);
        changeImageBtn=(FontTextView)findViewById(R.id.changeImageBtn);

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


        discountValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getFinalPrice();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddServiceButtonClicked();
            }


        });

    }

    private void onAddServiceButtonClicked() {

        if(!serviceName.getText().toString().isEmpty()) {
            if (!serviceDescripiton.getText().toString().isEmpty()){
                if (!servicePrice.getText().toString().isEmpty()) {
                    if (!discountValue.getText().toString().isEmpty()) {
                        int radioId = rgDemoLayout.getCheckedRadioButtonId();
                        if (radioId == R.id.rb_image) {
                            //customProgressDialog.showDialog();
                            if (selectedImagesFiles.size() > 0) {
                                proceedWithDemoImages();
                            } else {
                                Toast.makeText(context, "Select atleast 1 Image", Toast.LENGTH_LONG).show();
                            }
                        } else if (radioId == R.id.rb_video) {
                            if(selectedVideoPath !=null) {
                                proceedWithDemoVideo();
                            }else{
                                Toast.makeText(helper, "Select Video", Toast.LENGTH_LONG).show();
                            }

                        } else if (radioId == R.id.rb_none) {
                            proceedWithNoDemo();
                        }

                    } else {
                        Toast.makeText(context, "Discount value can't be empty", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "Service Price can't be empty", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(helper, "Service Description Can't be empty ", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context,"Service name can't be empty",Toast.LENGTH_LONG).show();
        }


    }



    String imgKeyImage,imgKeyVideo;
    ArrayList<File> compressedFilesList ;
    ArrayList<ImageUpload> imageUploadArrayList = new ArrayList<>();

    private void proceedWithDemoImages() {

        compressedFilesList = new ArrayList<>();
        imageUploadArrayList = new ArrayList<>();

        Log.e(TAG, "compressImagesNupload: "+selectedImagesFiles.size());
        for(int i =0;i<selectedImagesFiles.size();i++){
            try {
                if(selectedImagesFiles.get(i)==null){
                    String getfromdate = arrayDemoList.get(i).getImgUrl().trim();
                    String getfrom[] = getfromdate.split("https://categorybucket.s3.amazonaws.com/");
                    imageUploadArrayList.add(new ImageUpload(arrayDemoList.get(i).getImgUrl(),getfrom[1]));
                    compressedFilesList.add(null);
                }else{
                    File cmpFile = new Compressor(context).compressToFile(selectedImagesFiles.get(i),businessDashboard.getBusinessName().trim()+ Calendar.getInstance().getTimeInMillis());
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
                uploadDemoImageToAws(compressedFilesList.get(i),i);
            } else{
                Log.e(TAG, "findUploadFiles: NULL" +i);
                findUploadFiles(i+1);
            }
        }
    }

    private void uploadDemoImageToAws(File selectedFilePath1,int filePosition){
        imgKeyImage = System.currentTimeMillis()+"";
        Log.e(TAG, "uploadImageToAws: "+imgKeyImage );
        observer1 = transferUtility.upload(
                Constants.S3_BUCKET_CATEGORY,
                imgKeyImage,
                selectedFilePath1
        );

        observer1.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

                if (TransferState.COMPLETED.equals(observer1.getState())) {

                    Log.e(TAG, "onStateChanged: UPLOAD COMPLETE" + selectedFilePath1.getAbsolutePath() );
                    Log.e(TAG, "onStateChanged: https://"+Constants.S3_BUCKET_CATEGORY+".s3.amazonaws.com/"+observer1.getKey());
                    String url = "https://"+Constants.S3_BUCKET_CATEGORY+".s3.amazonaws.com/"+observer1.getKey();
                    imageUploadArrayList.add(new ImageUpload(url,observer1.getKey()));
                    if(filePosition<compressedFilesList.size()-1){
                        uploadDemoImageToAws(compressedFilesList.get(filePosition+1),filePosition+1);
                    }else{
                        Log.e(TAG, "onStateChanged: FORM STRING" );
                        if(selectedFilePath!=null){
                            compressImage();
                        }
                        else{
                            postListnew(null,null);
                        }
                    }
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

                long _bytesCurrent = bytesCurrent;
                long _bytesTotal = bytesTotal;

                float percentage =  ((float)_bytesCurrent /(float)_bytesTotal * 100);
                Log.e("percentage","" +percentage);
            }

            @Override
            public void onError(int id, Exception ex) {

                Log.e(TAG, "onStateChanged: UPLOAD"+ex.getMessage());

            }
        });
    }


    private void proceedWithDemoVideo() {

        imgKeyVideo = System.currentTimeMillis()+"";
        Log.e(TAG, "uploadImageToAws: "+imgKeyVideo );
        observer1 = transferUtility.upload(
                Constants.S3_BUCKET_CATEGORY,
                imgKeyVideo,
                new File(selectedVideoPath)
        );

        observer1.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                String url = "https://"+Constants.S3_BUCKET_CATEGORY+".s3.amazonaws.com/"+observer1.getKey();
                imageUploadArrayList.add(new ImageUpload(url,observer1.getKey()));
                if(selectedFilePath!=null){
                    compressImage();
                }
                else{
                    postListnew(null,null);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onError(int id, Exception ex) {

            }
        });

    }

    private void proceedWithNoDemo() {

        if(selectedFilePath!=null){
            compressImage();
        }
        else{
            postListnew(null,null);
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


    public Intent getPickVideoChooserIntent(){
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return intent;
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


    private void setupAws(){
        credentials = new BasicAWSCredentials(Constants.S3_ACCESS_KEY_ID,Constants.S3_SECRET_ACCESS_KEY);
        s3 = new AmazonS3Client(credentials);
        s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
        transferUtility = new TransferUtility(s3, this);
    }

    String imgKey;
    File compressedFile;
    private void compressImage(){
        try {
            compressedFile = new Compressor(VendorAddServiceActivity.this).compressToFile(selectedFilePath,serviceName.getText().toString().trim()+System.currentTimeMillis());
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
        String url= Constants.BASE_URL+ "vendor/post-service-mobile";

        JSONObject postListJson = new JSONObject();
        PrefManager prefManager= new PrefManager(context);

        try {
            postListJson.put("vendor_id",prefManager.getVendorId(context));
            postListJson.put("business_id",businessDashboard.getBusinessId());
            JSONObject imageObj = new JSONObject();
            if(imgUrl!=null){
                imageObj.put("imageurl",imgUrl);
                imageObj.put("key",key);
            }else{
                imageObj.put("imageurl","");
                imageObj.put("key","");
            }
            postListJson.put("image",imageObj);
            postListJson.put("service_name",serviceName.getText().toString());
            postListJson.put("service_cost",Integer.parseInt(servicePrice.getText().toString()));
            postListJson.put("service_description",serviceDescripiton.getText().toString());
            postListJson.put("moreinfo_service",moreInfo.getText().toString());
            postListJson.put("service_availability",getScheduleValidityObj());
            postListJson.put("service_validity",mScheduleValidity.get(serviceValiditySpinner.getSelectedItemPosition()).get_id());
            postListJson.put("final_price",Integer.parseInt(serviceFinalPrice.getText().toString()));

            if(discountSpinner.getSelectedItemPosition()==0){
                postListJson.put("rupees",true);
                postListJson.put("discount",discountValue.getText().toString());

            }else{
                postListJson.put("discount",discountValue.getText().toString());
                postListJson.put("rupees",false);
            }
            if(rgDemoLayout.getCheckedRadioButtonId() ==R.id.rb_image){
                postListJson.put("service_demo",true);

                JSONArray itemImagesJsonArray = new JSONArray();

                for(int i=0;i<imageUploadArrayList.size();i++){
                    JSONObject itemImageObject = new JSONObject();
                    itemImageObject.put("url",imageUploadArrayList.get(i).getUrl());
                    itemImageObject.put("key",imageUploadArrayList.get(i).getKey());
                    itemImagesJsonArray.put(itemImageObject);

                }

                postListJson.put("adv_images",itemImagesJsonArray);
            }else if(rgDemoLayout.getCheckedRadioButtonId() == R.id.rb_video){
                postListJson.put("service_demo",false);
                JSONObject itemImageObject = new JSONObject();
                itemImageObject.put("url",imageUploadArrayList.get(0).getUrl());
                itemImageObject.put("key",imageUploadArrayList.get(0).getKey());
                postListJson.put("demo_video",itemImageObject);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "postListnew: aws object"+ postListJson.toString());

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.POST, url, postListJson, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: response"+ response.toString());
                //customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        serviceName.getText().clear();
                        servicePrice.getText().clear();
                        serviceDescripiton.getText().clear();
                        discountSpinner.setSelection(0);
                        discountValue.getText().clear();
                        serviceFinalPrice.setText("");
                        moreInfo.getText().clear();
                        serviceValiditySpinner.setSelection(0);
                        Toast.makeText(context,"Service has been added Successfully!",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(context,"Please try again!",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Please try again!",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"POST LIST"+businessDashboard.getBusinessId());
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
                ActivityCompat.requestPermissions(VendorAddServiceActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }

        }
    }


    boolean discountPercentage=true;
    private void setupDiscountSpinner(){
        final List<String> discountTypes = Arrays.asList(getResources().getStringArray(R.array.discounts_type));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, discountTypes){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont= Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(14);


                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(14);

                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        discountSpinner.setAdapter(dataAdapter);
        discountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );

                if(i==0){
                    discountPercentage=true;
                }else{
                    discountPercentage=false;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }


    private void getFinalPrice(){
        if(discountValue.getText().toString().trim().length()>0) {
            if (Integer.parseInt(servicePrice.getText().toString().trim()) > 0) {
                if (discountPercentage) {
                    if (Integer.parseInt(discountValue.getText().toString().trim()) > 0)
                        serviceFinalPrice.setText(String.valueOf(Integer.parseInt(servicePrice.getText().toString().trim()) - Integer.parseInt(discountValue.getText().toString().trim())));
                } else {
                    if (Integer.parseInt(discountValue.getText().toString().trim()) > 0 && Integer.parseInt(discountValue.getText().toString().trim()) <= 100) {
                        int finalPriceVal = Integer.parseInt(servicePrice.getText().toString().trim()) - (Integer.parseInt(servicePrice.getText().toString().trim()) / Integer.parseInt(discountValue.getText().toString().trim()));
                        serviceFinalPrice.setText(String.valueOf(finalPriceVal));
                    }
                }
            }
        }else{
            serviceFinalPrice.setText("0");
        }
    }

    ArrayList<ScheduleValidity> mScheduleValidity;

    LinearLayout allDaysTimingLayout,mondayTimingLayout,tuesdayTimingLayout,wednesdayTimingLayout,thursdayTimingLayout,fridayTimingLayout,saturdayTimingLayout,sundayTimingLayout;
    FontCheckBox chckAllDays,chckMonday,chckTuesday,chckWednesday,chckThursday,chckFriday,chckSaturday,chckSunday;

    Spinner allDaysStartTimeSpinner,allDaysEndTimeSpinner,mondayStartTimeSpinner,mondayEndTimeSpinner;
    Spinner tuesdayStartTimeSpinner,tuesdayEndTimeSpinner,wednesdayStartTimeSpinner,wednesdayEndTimeSpinner,thursdayStartTimeSpinner,thursdayEndTimeSpinner;
    Spinner fridayStartTimeSpinner,fridayEndTimeSpinner,saturdayStartTimeSpinner,saturdayEndTimeSpinner,sundayStartTimeSpinner,sundayEndTimeSpinner;
    Spinner serviceValiditySpinner;

    String allDaysStartTime,allDaysEndTime,mondayStartTime,mondayEndTime,tuesdayStartTime,tuesdayEndTime,wedStartTime,wedEndTime;
    String thursdayStartTime,thursdayEndTime,fridayStartTime,fridayEndTime,saturdayStartTime,saturdayEndTime,sundayStartTime,sundayEndTime;

    private void scheduleViews() {

        allDaysTimingLayout = (LinearLayout) findViewById(R.id.allDaysTimingsLayout);
        chckAllDays = (FontCheckBox) findViewById(R.id.allDaysCheckBox);
        allDaysStartTimeSpinner = (Spinner) findViewById(R.id.allDaysStartTime);
        allDaysEndTimeSpinner = (Spinner) findViewById(R.id.allDaysEndTime);

        mondayTimingLayout = (LinearLayout) findViewById(R.id.mondayTimingsLayout);
        chckMonday = (FontCheckBox) findViewById(R.id.mondayCheckBox);
        mondayStartTimeSpinner = (Spinner) findViewById(R.id.mondayStartTime);
        mondayEndTimeSpinner = (Spinner) findViewById(R.id.mondayEndTime);

        tuesdayTimingLayout = (LinearLayout) findViewById(R.id.tuesdayTimingsLayout);
        chckTuesday = (FontCheckBox) findViewById(R.id.tuesdayCheckBox);
        tuesdayStartTimeSpinner = (Spinner) findViewById(R.id.tuesdayStartTime);
        tuesdayEndTimeSpinner = (Spinner) findViewById(R.id.tuesdayEndTime);

        wednesdayTimingLayout = (LinearLayout) findViewById(R.id.wednesdayTimingsLayout);
        chckWednesday = (FontCheckBox) findViewById(R.id.wednesdayCheckBox);
        wednesdayStartTimeSpinner = (Spinner) findViewById(R.id.wednesdayStartTime);
        wednesdayEndTimeSpinner = (Spinner) findViewById(R.id.wednesdayEndTime);

        thursdayTimingLayout = (LinearLayout) findViewById(R.id.thursdayTimingsLayout);
        chckThursday = (FontCheckBox) findViewById(R.id.thursdayCheckBox);
        thursdayStartTimeSpinner = (Spinner) findViewById(R.id.thursdayStartTime);
        thursdayEndTimeSpinner = (Spinner) findViewById(R.id.thursdayEndTime);

        fridayTimingLayout = (LinearLayout) findViewById(R.id.fridayTimingsLayout);
        chckFriday = (FontCheckBox) findViewById(R.id.fridayCheckBox);
        fridayStartTimeSpinner = (Spinner) findViewById(R.id.fridayStartTime);
        fridayEndTimeSpinner = (Spinner) findViewById(R.id.fridayEndTime);

        saturdayTimingLayout = (LinearLayout) findViewById(R.id.saturdayTimingsLayout);
        chckSaturday = (FontCheckBox) findViewById(R.id.saturdayCheckBox);
        saturdayStartTimeSpinner = (Spinner) findViewById(R.id.saturdayStartTime);
        saturdayEndTimeSpinner = (Spinner) findViewById(R.id.saturdayEndTime);

        sundayTimingLayout = (LinearLayout) findViewById(R.id.sundayTimingsLayout);
        chckSunday = (FontCheckBox) findViewById(R.id.sundayCheckBox);
        sundayStartTimeSpinner = (Spinner) findViewById(R.id.sundayStartTime);
        sundayEndTimeSpinner = (Spinner) findViewById(R.id.sundayEndTime);

        serviceValiditySpinner = (Spinner) findViewById(R.id.serviceValidity);

        final List<String> timingsList = Arrays.asList(getResources().getStringArray(R.array.timings_array));


        chckAllDays.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                allDaysTimingLayout.setVisibility(View.VISIBLE);
                chckMonday.setChecked(false);
                chckTuesday.setChecked(false);
                chckWednesday.setChecked(false);
                chckThursday.setChecked(false);
                chckFriday.setChecked(false);
                chckSaturday.setChecked(false);
                chckSunday.setChecked(false);
            }else{
                allDaysTimingLayout.setVisibility(View.GONE);
                allDaysStartTime = "00.00";
                allDaysEndTime = "00.00";
            }
        });
        chckMonday.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(isChecked){
                mondayTimingLayout.setVisibility(View.VISIBLE);
                chckAllDays.setChecked(false);
            }else{
                mondayTimingLayout.setVisibility(View.GONE);
            }
        }));
        chckTuesday.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(isChecked){
                tuesdayTimingLayout.setVisibility(View.VISIBLE);
                chckAllDays.setChecked(false);
            }else{
                tuesdayTimingLayout.setVisibility(View.GONE);
            }
        }));
        chckWednesday.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(isChecked){
                wednesdayTimingLayout.setVisibility(View.VISIBLE);
                chckAllDays.setChecked(false);
            }else{
                wednesdayTimingLayout.setVisibility(View.GONE);
            }
        }));
        chckThursday.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(isChecked){
                thursdayTimingLayout.setVisibility(View.VISIBLE);
                chckAllDays.setChecked(false);
            }else{
                thursdayTimingLayout.setVisibility(View.GONE);
            }
        }));
        chckFriday.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(isChecked){
                fridayTimingLayout.setVisibility(View.VISIBLE);
                chckAllDays.setChecked(false);
            }else{
                fridayTimingLayout.setVisibility(View.GONE);
            }
        }));
        chckSaturday.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(isChecked){
                saturdayTimingLayout.setVisibility(View.VISIBLE);
                chckAllDays.setChecked(false);
            }else{
                saturdayTimingLayout.setVisibility(View.GONE);
            }
        }));
        chckSunday.setOnCheckedChangeListener(((buttonView, isChecked) -> {
            if(isChecked){
                sundayTimingLayout.setVisibility(View.VISIBLE);
                chckAllDays.setChecked(false);
            }else{
                sundayTimingLayout.setVisibility(View.GONE);
            }
        }));

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, timingsList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont= Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(14);


                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(14);

                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        allDaysStartTimeSpinner.setAdapter(dataAdapter);
        allDaysEndTimeSpinner.setAdapter(dataAdapter);

        mondayStartTimeSpinner.setAdapter(dataAdapter);
        mondayEndTimeSpinner.setAdapter(dataAdapter);

        tuesdayStartTimeSpinner.setAdapter(dataAdapter);
        tuesdayEndTimeSpinner.setAdapter(dataAdapter);

        wednesdayStartTimeSpinner.setAdapter(dataAdapter);
        wednesdayEndTimeSpinner.setAdapter(dataAdapter);

        thursdayStartTimeSpinner.setAdapter(dataAdapter);
        thursdayEndTimeSpinner.setAdapter(dataAdapter);

        fridayStartTimeSpinner.setAdapter(dataAdapter);
        fridayEndTimeSpinner.setAdapter(dataAdapter);

        saturdayStartTimeSpinner.setAdapter(dataAdapter);
        saturdayEndTimeSpinner.setAdapter(dataAdapter);

        sundayStartTimeSpinner.setAdapter(dataAdapter);
        saturdayEndTimeSpinner.setAdapter(dataAdapter);

        String url = Constants.BASE_URL+ "admin/get-all-service-validities";

        CustomJsonRequest jsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        JSONArray jsonArray =response.getJSONArray("msg");

                        validityList = new ArrayList<>();
                        mScheduleValidity = new ArrayList<>();

                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object =jsonArray.getJSONObject(i);
                            String id = object.getString("_id");
                            String name = object.getString("name");
                            String v = object.getString("__v");
                            mScheduleValidity.add(new ScheduleValidity(id,name,v));
                            validityList.add(name);
                        }

                        ArrayAdapter<String> dataValidityAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,validityList){
                            @NonNull
                            @Override
                            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                View v = super.getView(position, convertView, parent);

                                Typeface externalFont= Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                                ((TextView) v).setTypeface(externalFont);
                                ((TextView) v).setTextSize(14);


                                return v;
                            }

                            @Override
                            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                View v =super.getDropDownView(position, convertView, parent);

                                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                                ((TextView) v).setTypeface(externalFont);
                                ((TextView) v).setTextSize(14);

                                return v;
                            }
                        };
                        dataValidityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        serviceValiditySpinner.setAdapter(dataValidityAdapter);
                        serviceValiditySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        jsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(jsonRequest,"GET_SC_VAL");





        onScheduleItemSelected();


    }

    private void onScheduleItemSelected() {

        allDaysStartTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                allDaysStartTime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        allDaysEndTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                allDaysEndTime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mondayStartTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                mondayStartTime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        mondayEndTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                mondayEndTime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        tuesdayStartTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                tuesdayStartTime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        tuesdayEndTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                tuesdayEndTime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        wednesdayStartTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                wedStartTime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        wednesdayEndTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                wedEndTime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        thursdayStartTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                thursdayStartTime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        thursdayEndTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                thursdayEndTime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        fridayStartTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                fridayStartTime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        fridayEndTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                fridayEndTime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        saturdayStartTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                saturdayStartTime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        saturdayEndTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                saturdayEndTime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        sundayStartTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                sundayStartTime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        sundayEndTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                sundayEndTime = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        serviceValiditySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: serviceValidity "+ mScheduleValidity.get(i).getName() );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }



    public JSONObject getScheduleValidityObj() throws JSONException {
        JSONObject  jsonObject = new JSONObject();
        JSONObject mondayObject = new JSONObject();
        JSONObject tuesdayObject = new JSONObject();
        JSONObject wednesdayObject = new JSONObject();
        JSONObject thursdayObject = new JSONObject();
        JSONObject fridayObject = new JSONObject();
        JSONObject saturdayObject = new JSONObject();
        JSONObject sundayObject = new JSONObject();


        if(chckAllDays.isChecked()){
            mondayObject.put("isWorking",true);
            mondayObject.put("open",allDaysStartTime);
            mondayObject.put("close",allDaysEndTime);

            tuesdayObject.put("isWorking",true);
            tuesdayObject.put("open",allDaysStartTime);
            tuesdayObject.put("close",allDaysEndTime);

            wednesdayObject.put("isWorking",true);
            wednesdayObject.put("open",allDaysStartTime);
            wednesdayObject.put("close",allDaysEndTime);

            thursdayObject.put("isWorking",true);
            thursdayObject.put("open",allDaysStartTime);
            thursdayObject.put("close",allDaysEndTime);

            fridayObject.put("isWorking",true);
            fridayObject.put("open",allDaysStartTime);
            fridayObject.put("close",allDaysEndTime);

            saturdayObject.put("isWorking",true);
            saturdayObject.put("open",allDaysStartTime);
            saturdayObject.put("close",allDaysEndTime);

            sundayObject.put("isWorking",true);
            sundayObject.put("open",allDaysStartTime);
            sundayObject.put("close",allDaysEndTime);
        }else{
            if(chckMonday.isChecked()){
                mondayObject.put("isWorking",true);
                mondayObject.put("open",mondayStartTime);
                mondayObject.put("close",mondayEndTime);
            }else{
                mondayObject.put("isWorking",false);
                mondayObject.put("open","00:00");
                mondayObject.put("close","00:00");
            }
            if(chckTuesday.isChecked()){
                tuesdayObject.put("isWorking",true);
                tuesdayObject.put("open",tuesdayStartTime);
                tuesdayObject.put("close",tuesdayEndTime);
            }else{
                tuesdayObject.put("isWorking",false);
                tuesdayObject.put("open","00:00");
                tuesdayObject.put("close","00:00");
            }
            if(chckWednesday.isChecked()){
                wednesdayObject.put("isWorking",true);
                wednesdayObject.put("open",wedStartTime);
                wednesdayObject.put("close",wedEndTime);
            }else{
                wednesdayObject.put("isWorking",false);
                wednesdayObject.put("open","00:00");
                wednesdayObject.put("close","00:00");
            }
            if(chckThursday.isChecked()){
                thursdayObject.put("isWorking",true);
                thursdayObject.put("open",thursdayStartTime);
                thursdayObject.put("close",thursdayEndTime);
            }else{
                thursdayObject.put("isWorking",false);
                thursdayObject.put("open","00:00");
                thursdayObject.put("close","00:00");
            }
            if(chckFriday.isChecked()){
                fridayObject.put("isWorking",true);
                fridayObject.put("open",fridayStartTime);
                fridayObject.put("close",fridayEndTime);
            }else{
                fridayObject.put("isWorking",false);
                fridayObject.put("open","00:00");
                fridayObject.put("close","00:00");
            }
            if(chckSaturday.isChecked()){
                saturdayObject.put("isWorking",true);
                saturdayObject.put("open",saturdayStartTime);
                saturdayObject.put("close",saturdayEndTime);
            }else{
                saturdayObject.put("isWorking",false);
                saturdayObject.put("open","00:00");
                saturdayObject.put("close","00:00");
            }
            if(chckSunday.isChecked()){
                sundayObject.put("isWorking",true);
                sundayObject.put("open",sundayStartTime);
                sundayObject.put("close",sundayEndTime);
            }else{
                sundayObject.put("isWorking",false);
                sundayObject.put("open","00:00");
                sundayObject.put("close","00:00");
            }
        }

        jsonObject.put("monday",mondayObject);
        jsonObject.put("tuesday",tuesdayObject);
        jsonObject.put("wednesday",wednesdayObject);
        jsonObject.put("thursday",thursdayObject);
        jsonObject.put("friday",fridayObject);
        jsonObject.put("saturday",saturdayObject);
        jsonObject.put("sunday",sundayObject);
        return jsonObject;
    }



    private void setupDemoLayout() {

        rgDemoLayout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {


                if(i == R.id.rb_image){
                    imageDemoLayout.setVisibility(View.VISIBLE);
                    videoDemoLayout.setVisibility(View.GONE);
                }else if(i == R.id.rb_video){

                    videoDemoLayout.setVisibility(View.VISIBLE);
                    imageDemoLayout.setVisibility(View.GONE);
                }else if(i == R.id.rb_none){
                    videoDemoLayout.setVisibility(View.GONE);
                    imageDemoLayout.setVisibility(View.GONE);
                }

            }
        });

        imageDemoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(getPickImageChooserIntent(), 201);

            }
        });

        videoDemoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(getPickVideoChooserIntent(), 202);

            }
        });
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
                addedImageLayout.setVisibility(View.VISIBLE);
                addImageLayout.setVisibility(View.GONE);
            }


        } else if(requestCode == 201){
            if (resultCode == Activity.RESULT_OK) {
                processSelectedImage(data);
            }
        } else if(requestCode ==202){
            if (resultCode == Activity.RESULT_OK) {
                processSelectedVideo(data);
            }
        }
    }



    String filePath1;
    ArrayList<BannerImages> arrayDemoList = new ArrayList<>();
    ArrayList<File> selectedImagesFiles = new ArrayList<>();
    VendorDemoImagesAdapter vendorDemoImagesAdapter;
    RecyclerView itemImagesRcv;

    File imgFile;
    private void processSelectedImage(Intent data){
        if(getPickImageResultUri(data)!=null){
            Log.e(TAG, "processSelectedImage: CAMERA" );

            Uri photoUri = getPickImageResultUri(data);
            if (photoUri != null) {
                filePath1  = FetchPath.getPath(context, photoUri);
            }
            imgFile = new  File(filePath1);
            if(imgFile.exists()){
                Log.e(TAG, "onActivityResult: EXISTS" );
                Log.e(TAG, "onActivityResult: "+filePath1 );
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = rotateImageIfRequired(myBitmap, imgFile);
                myBitmap = getResizedBitmap(myBitmap, 500);
            }
        }else{
            Log.e(TAG, "processSelectedImage: PICKER" );
            Uri photoUri = data.getData();
            if (photoUri != null) {
                filePath1  = FetchPath.getPath(context, photoUri);
            }
            imgFile = new  File(filePath1);
            if(imgFile.exists()){
                Log.e(TAG, "onActivityResult: EXISTS" );
                Log.e(TAG, "onActivityResult: "+filePath1 );
                myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                myBitmap = rotateImageIfRequired(myBitmap, imgFile);
                myBitmap = getResizedBitmap(myBitmap, 500);
            }
        }

        arrayDemoList.add(new BannerImages(myBitmap,"",false));
        selectedImagesFiles.add(imgFile);
        vendorDemoImagesAdapter.notifyDataSetChanged();
        Log.e(TAG, "onActivityResult: "+arrayDemoList.size() );
        Log.e(TAG, "onActivityResult: "+selectedImagesFiles.size() );

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

    @Override
    public void deleteImage(int imagePosition) {
        arrayDemoList.remove(imagePosition);
        if(selectedImagesFiles!=null){
            if(selectedImagesFiles.size()>0){
                selectedImagesFiles.remove(imagePosition);
            }
        }
        vendorDemoImagesAdapter.notifyDataSetChanged();
    }



    private void processSelectedVideo(Intent data) {
        Uri selectedImageUri = data.getData();
        selectedVideoPath = getPath(context,selectedImageUri);
        if (selectedVideoPath != null) {
            Log.e(TAG, "processSelectedVideo: path" +selectedVideoPath );

            videoThumbnail.setVideoPath(selectedVideoPath);


        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }


            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }/**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
}
