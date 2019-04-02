package com.reatchall.charan.reatchallVendor.Vendor;

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
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.FetchPath;
import com.reatchall.charan.reatchallVendor.Utils.HomeDelPendingDialog;
import com.reatchall.charan.reatchallVendor.Utils.ImageCompression.Compressor;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ImageUpload;
import com.suke.widget.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontCheckBox;
import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

import static com.reatchall.charan.reatchallVendor.Registration.RegisterActivity.EmailValidator;
import static com.reatchall.charan.reatchallVendor.Registration.RegisterActivity.PhoneNumberValidator;

public class VendorNewHomeDelActivity extends AppCompatActivity {

    private static final String TAG = "VendorNewHomeDelActivit";
    ImageView backArrow;
    LinearLayout title;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    Context context;
    BusinessDetails businessDashboard;
    FontTextView titleToolbar;
    PrefManager prefManager;


    FontButton submit,continueTerms;

    FontCheckBox tAndCB,docCB;
    LinearLayout termsLayout,detailsLayout,onOffLayout;
    FontEditText gstNum,panNum,aadharNumber,bankName,accountHolderName,ifscCode,accountNum,cnfrmAccountNum;
    LinearLayout uploadRC,uploadLicense;

    LinearLayout addedRcLayout,addedLicenseLayout,gstLayout;
    ImageView rcIV,clearRc,licenseIV,clearLicense;
    FontTextView changeRcIv,changeLicenseIv;

    NestedScrollView nestedScrollView;
    SwitchButton homeDeliverySwitch;

    //S3 bucket
    AmazonS3Client s3;
    BasicAWSCredentials credentials;
    TransferUtility transferUtility;
    TransferObserver observer;
    File selectedLicensePath;
    File selectedRcPath;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_new_home_del);
        setupAws();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            ActivityCompat.requestPermissions(VendorNewHomeDelActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
        context = VendorNewHomeDelActivity.this;
        prefManager = new PrefManager(context);
        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
        if(businessDashboard!=null){
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }
        customProgressDialog = new CustomProgressDialog(context);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(businessDashboard.isService()){
                    Intent intent = new Intent(context,VendorDashBoardNewActivity.class);
                    intent.putExtra("businessDetails",businessDashboard);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(context,VendorDashBoardActivity.class);
                    intent.putExtra("businessDetails",businessDashboard);
                    startActivity(intent);
                }
                finish();
            }
        });

        initViews();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageModelArrayList = new ArrayList<ImageUpload>();
                validate();
            }
        });

        continueTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tAndCB.isChecked() && docCB.isChecked()){
                    termsLayout.setVisibility(View.GONE);
                    detailsLayout.setVisibility(View.VISIBLE);
                    scrollToTOp();
                }else{
                    Toast.makeText(context,"You need to accept the terms and conditions to proceed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void scrollToTOp(){
        new Thread(new Runnable()
        {
            @Override
            public
            void run ()
            {
                // Determine where to set the scroll-to to by measuring the distance from the top of the scroll view
                // to the control to focus on by summing the "top" position of each view in the hierarchy.
                int yDistanceToControlsView = 0;
                View parentView = (View) gstLayout.getParent();
                while (true)
                {
                    if (parentView.equals(nestedScrollView))
                    {
                        break;
                    }
                    yDistanceToControlsView += parentView.getTop();
                    parentView = (View) parentView.getParent();
                }

                // Compute the final position value for the top and bottom of the control in the scroll view.
                final int topInScrollView = yDistanceToControlsView + gstLayout.getTop();
                final int bottomInScrollView = yDistanceToControlsView + gstLayout.getBottom();

                // Post the scroll action to happen on the scrollView with the UI thread.
                nestedScrollView.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int height =gstLayout.getHeight();
                        nestedScrollView.smoothScrollTo(0, ((topInScrollView + bottomInScrollView) / 2) - height);
                        gstLayout.requestFocus();
                    }
                });
            }
        }).start();
    }
    private void validate(){
        if(selectedLicensePath!=null){
            if(gstNum.getText().toString().length()>0){
                if(panNum.getText().toString().length()>0){
                    if(aadharNumber.getText().toString().length()>0){
                        if(bankName.getText().toString().length()>0){
                            if(accountHolderName.getText().toString().length()>0){
                                if(ifscCode.getText().toString().length()>0){
                                    if(accountNum.getText().toString().length()>0){
                                        if(accountNum.getText().toString().equals(cnfrmAccountNum.getText().toString())){
                                                    compressImage(selectedLicensePath);
                                        }else{
                                            Toast.makeText(context,"Account numbers not matching",Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        Toast.makeText(context,"Invalid account number",Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(context,"Invalid ifsc code",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(context,"Invalid account holder name",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(context,"Invalid bank name",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context,"Invalid aadhar number",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(context,"Invalid pan number",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(context,"Invalid gst number",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(context,"Please upload shop license images",Toast.LENGTH_LONG).show();
        }
    }
    private void setupAws(){
        credentials = new BasicAWSCredentials(Constants.S3_ACCESS_KEY_ID,Constants.S3_SECRET_ACCESS_KEY);
        s3 = new AmazonS3Client(credentials);
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));
        transferUtility = new TransferUtility(s3, this);
    }
    File compressedFile;
    private void compressImage(File selectedFilePath){
        try {
            compressedFile = new Compressor(VendorNewHomeDelActivity.this).compressToFile(selectedFilePath,gstNum.getText().toString().trim()+System.currentTimeMillis());
            if(compressedFile.exists()){
                Log.e(TAG, "compressImagesNupload: CMP EXISTS" );
                //compressedFilesList.add(cmpFile);
                uploadImageToAws();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    ArrayList<ImageUpload> imageModelArrayList = new ArrayList<ImageUpload>();
    private void uploadImageToAws(){
        //imgKey = businessDashboard.getBusinessId()+
        observer = transferUtility.upload(
                Constants.S3_BUCKET_CATEGORY,
                System.currentTimeMillis()+"",
                compressedFile
        );

        observer.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {

                if (state.COMPLETED.equals(observer.getState())) {

                    Log.e(TAG, "onStateChanged: UPLOAD COMPLETE" );
                    Log.e(TAG, "onStateChanged: https://"+Constants.S3_BUCKET_CATEGORY+".s3.amazonaws.com/"+observer.getKey());
                    String url = "https://"+Constants.S3_BUCKET_CATEGORY+".s3.amazonaws.com/"+observer.getKey();
                    //postListnew(url,observer.getKey());
                    imageModelArrayList.add(new ImageUpload(url,observer.getKey()));
                    if(selectedRcPath!=null && imageModelArrayList.size()==1){
                        compressImage(selectedRcPath);
                    }else{
                        customProgressDialog.showDialog();
                        requestHomeDelivery();
                    }
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
    private void initViews(){
        homeDeliverySwitch=(SwitchButton)findViewById(R.id.homeDeliverySwitch);
        homeDeliverySwitch.setChecked(false);
        nestedScrollView =(NestedScrollView) findViewById(R.id.nestedScrollView);
        submit =(FontButton) findViewById(R.id.submit);
        continueTerms =(FontButton) findViewById(R.id.continueTerms);
        tAndCB=(FontCheckBox)findViewById(R.id.tAndCB);
        docCB=(FontCheckBox)findViewById(R.id.docCB);
        termsLayout=(LinearLayout)findViewById(R.id.termsLayout);
        detailsLayout=(LinearLayout)findViewById(R.id.detailsLayout);
        gstLayout=(LinearLayout)findViewById(R.id.gstLayout);
        onOffLayout=(LinearLayout)findViewById(R.id.onOffLayout);
        uploadRC=(LinearLayout)findViewById(R.id.uploadRC);
        uploadLicense=(LinearLayout)findViewById(R.id.uploadLicense);
        title=(LinearLayout)findViewById(R.id.title);
        gstNum=(FontEditText)findViewById(R.id.gstNum);
        panNum=(FontEditText)findViewById(R.id.panNum);
        aadharNumber=(FontEditText)findViewById(R.id.aadharNumber);
        bankName=(FontEditText)findViewById(R.id.bankName);
        accountHolderName=(FontEditText)findViewById(R.id.accountHolderName);
        ifscCode=(FontEditText)findViewById(R.id.ifscCode);
        accountNum=(FontEditText)findViewById(R.id.accountNum);
        cnfrmAccountNum=(FontEditText)findViewById(R.id.cnfrmAccountNum);

        addedRcLayout =(LinearLayout)findViewById(R.id.addedRcLayout);
        addedLicenseLayout =(LinearLayout)findViewById(R.id.addedLicenseLayout);
        changeLicenseIv =(FontTextView) findViewById(R.id.changeLicenseIv);
        changeRcIv =(FontTextView) findViewById(R.id.changeRcIv);
        rcIV =(ImageView) findViewById(R.id.rcIV);
        clearRc =(ImageView) findViewById(R.id.clearRc);
        licenseIV =(ImageView) findViewById(R.id.licenseIV);
        clearLicense =(ImageView) findViewById(R.id.clearLicense);
        addedRcLayout.setVisibility(View.GONE);
        addedLicenseLayout.setVisibility(View.GONE);
        termsLayout.setVisibility(View.VISIBLE);
        detailsLayout.setVisibility(View.GONE);
        onOffLayout.setVisibility(View.GONE);

        uploadLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(getPickImageChooserIntent(), 201);
            }
        });

        uploadRC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(getPickImageChooserIntent(), 200);
            }
        });

        clearRc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadRC.setVisibility(View.VISIBLE);
                addedRcLayout.setVisibility(View.GONE);
                selectedRcPath = null;
            }
        });

        clearLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadLicense.setVisibility(View.VISIBLE);
                addedLicenseLayout.setVisibility(View.GONE);
                selectedLicensePath = null;
            }
        });

       /* homeDeliverySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.showDialog();
                setHomeDeliveryStatus();
            }
        });*/

        homeDeliverySwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                customProgressDialog.showDialog();
                setHomeDeliveryStatus();
            }
        });
        customProgressDialog.showDialog();
        getHomeDeliveryReqStatus();
    }

    private void getHomeDeliveryReqStatus(){
        String url = Constants.BASE_URL+"vendor/get-homedelivery-request/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        termsLayout.setVisibility(View.GONE);
                        detailsLayout.setVisibility(View.GONE);
                        onOffLayout.setVisibility(View.GONE);


                        if(response.getJSONObject("msg").getBoolean("approved")){
                            onOffLayout.setVisibility(View.VISIBLE);
                            if(businessDashboard.isHome_delivery()){
                                homeDeliverySwitch.setChecked(true);
                            }else{
                                homeDeliverySwitch.setChecked(false);
                            }

                        }else{
                            if(response.getJSONObject("msg").getBoolean("requested")){
                                popUP();
                            }
                        }
                    }else{
                        termsLayout.setVisibility(View.VISIBLE);
                        detailsLayout.setVisibility(View.GONE);
                        onOffLayout.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Couldn't get your home delivery status",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest);
    }
    private void setHomeDeliveryStatus(){
        String url = Constants.BASE_URL+"vendor/homedelivery-status";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("business_id",businessDashboard.getBusinessId());
            jsonObject.put("home_delivery",!businessDashboard.isHome_delivery());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: "+response );
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        businessDashboard.setHome_delivery(!businessDashboard.isHome_delivery());
                        Toast.makeText(context,"updated successfully!",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context,response.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Unable to update the status",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest);
    }
    private void popUP(){
        HomeDelPendingDialog homeDelPendingDialog = new HomeDelPendingDialog(context);
        homeDelPendingDialog.setTitle("Home Delivery Status");
        homeDelPendingDialog.setMessage("We have successfully received home delivery request from you.Please wait until you get an approval from our team");
        homeDelPendingDialog.setPositveButton("Ok", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeDelPendingDialog.dismiss();
                finish();
            }
        });
        homeDelPendingDialog.show();
    }
    private void requestHomeDelivery() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
            jsonObject.put("business_id",businessDashboard.getBusinessId());
            jsonObject.put("gst",gstNum.getText().toString());
            jsonObject.put("pan",panNum.getText().toString());
            jsonObject.put("aadhar_number",aadharNumber.getText().toString());
            jsonObject.put("bank_name",bankName.getText().toString());
            jsonObject.put("account_name",accountHolderName.getText().toString());
            jsonObject.put("account_number",accountNum.getText().toString());
            jsonObject.put("ifsc_code",ifscCode.getText().toString());
            jsonObject.put("requested",true);

            JSONObject licenseImg = new JSONObject();
            licenseImg.put("imageurl",imageModelArrayList.get(0).getUrl());
            licenseImg.put("key",imageModelArrayList.get(0).getKey());
            JSONObject rcImg = new JSONObject();
            if(imageModelArrayList.size()==2){
                rcImg.put("imageurl",imageModelArrayList.get(1).getUrl());
                rcImg.put("key",imageModelArrayList.get(1).getKey());
                jsonObject.put("registration_certificate",rcImg);
                jsonObject.put("registartion_flag",true);
            }else{
                jsonObject.put("registration_certificate",rcImg);
                jsonObject.put("registartion_flag",false);
            }

            jsonObject.put("shop_license",licenseImg);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = Constants.BASE_URL+"vendor/enable-homedelivery-mobile";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        detailsLayout.setVisibility(View.GONE);
                        popUP();
                        //Toast.makeText(context,"Thanks for your interest. Please wait till it gets approved.",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context,"Please try again",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Please try again",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"HOME_DEL_REQ");
    }
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
                        if (picUri != null) {
                            filePath  = FetchPath.getPath(this, picUri);
                        }
                        File imgFile = new  File(filePath);
                        selectedRcPath= imgFile;
                        if(imgFile.exists()){
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            rcIV.setImageBitmap(myBitmap);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Uri photoUri = data.getData();
                        if (photoUri != null) {
                            filePath  = FetchPath.getPath(this, photoUri);
                        }
                        File imgFile = new  File(filePath);
                        selectedRcPath= imgFile;
                        if(imgFile.exists()){
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            rcIV.setImageBitmap(myBitmap);
                        }
                    }
                } else {
                    Uri photoUri = data.getData();
                    if (photoUri != null) {
                        filePath  = FetchPath.getPath(this, photoUri);
                    }
                    File imgFile = new  File(filePath);
                    selectedRcPath= imgFile;
                    Log.e(TAG, "onActivityResult: SELECT");
                    bitmap = (Bitmap) data.getExtras().get("data");
                    myBitmap = bitmap;
                    rcIV.setImageBitmap(myBitmap);
                }
                addedRcLayout.setVisibility(View.VISIBLE);
                uploadRC.setVisibility(View.GONE);
            }
        }

        if(requestCode==201){
            Bitmap bitmap;
            if (resultCode == Activity.RESULT_OK) {
                if (getPickImageResultUri(data) != null) {
                    picUri = getPickImageResultUri(data);
                    Log.e(TAG, "onActivityResult: CAMERA" );
                    try {
                        myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), picUri);
                        myBitmap = rotateImageIfRequired(myBitmap, picUri);
                        myBitmap = getResizedBitmap(myBitmap, 500);
                        if (picUri != null) {
                            filePath  = FetchPath.getPath(this, picUri);
                        }
                        File imgFile = new  File(filePath);
                        selectedLicensePath= imgFile;
                        if(imgFile.exists()){
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            licenseIV.setImageBitmap(myBitmap);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        Uri photoUri = data.getData();
                        if (photoUri != null) {
                            filePath  = FetchPath.getPath(this, photoUri);
                        }
                        File imgFile = new  File(filePath);
                        selectedLicensePath= imgFile;
                        if(imgFile.exists()){
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            licenseIV.setImageBitmap(myBitmap);
                        }
                    }
                } else {
                    Uri photoUri = data.getData();
                    if (photoUri != null) {
                        filePath  = FetchPath.getPath(this, photoUri);
                    }
                    File imgFile = new  File(filePath);
                    selectedLicensePath= imgFile;
                    Log.e(TAG, "onActivityResult: SELECT");
                    bitmap = (Bitmap) data.getExtras().get("data");
                    myBitmap = bitmap;
                    licenseIV.setImageBitmap(myBitmap);
                }
                addedLicenseLayout.setVisibility(View.VISIBLE);
                uploadLicense.setVisibility(View.GONE);
            }
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
                ActivityCompat.requestPermissions(VendorNewHomeDelActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }

        }
    }


    @Override
    public void onBackPressed() {
        backArrow.performClick();
    }
}
