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
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.android.volley.AuthFailureError;
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
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.ProductImagesAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.AllProducts;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ImageUpload;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ItemUnits;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ListDetailsModel;
import com.reatchall.charan.reatchallVendor.Vendor.Models.NewProduct;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ProductImages;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IDeletePromoImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorEditItemActivity extends AppCompatActivity implements IDeletePromoImage {
    //productDetails
    private static final String TAG = "VendorEditItemActivity";

    ImageView backArrow;
    String listId=null,postItemString=null;
    BusinessDetails businessDashboard;
    FontTextView titleToolbar;


    FontEditText itemName,itemQuantity,itemPrice;
    FontEditText itemBrand,itemDesc;
    Spinner quantitySpinner,itemTypeSpinner;
    Button saveItem;
    RecyclerView itemImagesRcv;
    ArrayList<ItemUnits> itemUnitsArrayList;
    LinearLayout uploadImages;
    ReatchAll helper = ReatchAll.getInstance();
    NewProduct productDetails=null;

    boolean allgranted = false;

    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    //Add to list
    LinearLayout addToListLayout;
    Spinner listSpinner;
    CustomProgressDialog customProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_edit_item);
        customProgressDialog=new CustomProgressDialog(VendorEditItemActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            ActivityCompat.requestPermissions(VendorEditItemActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
        setupAws();
        getAllUnits();
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        itemName=(FontEditText)findViewById(R.id.itemName);
        listSpinner =(Spinner)findViewById(R.id.listSpinner);
        addToListLayout=(LinearLayout)findViewById(R.id.addToListLayout);
        //packLayout=(LinearLayout)findViewById(R.id.packLayout);
        //packLayout.setVisibility(View.GONE);
        uploadImages=(LinearLayout) findViewById(R.id.uploadImages);
        itemQuantity=(FontEditText)findViewById(R.id.item_quantity);
        itemPrice=(FontEditText)findViewById(R.id.item_price);
        quantitySpinner=(Spinner)findViewById(R.id.quantitySpinner);
        itemTypeSpinner=(Spinner)findViewById(R.id.itemTypeSpinner);
        itemBrand=(FontEditText)findViewById(R.id.itemBrand);
        //packOf=(FontEditText)findViewById(R.id.packOf);
        itemDesc=(FontEditText)findViewById(R.id.itemDesc);
        itemImagesRcv=(RecyclerView)findViewById(R.id.itemImagesRcv);
        itemImagesRcv.setHasFixedSize(true);
        itemImagesRcv.setNestedScrollingEnabled(false);
        itemImagesRcv.setLayoutManager(new LinearLayoutManager(VendorEditItemActivity.this,LinearLayoutManager.HORIZONTAL,false));
        productImagesAdapter = new ProductImagesAdapter(VendorEditItemActivity.this,arrayList,this);
        itemImagesRcv.setAdapter(productImagesAdapter);

        getAllUnits();
        setupDiscountSpinner();

        businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
        productDetails=getIntent().getExtras().getParcelable("productDetails");
        listId=productDetails.getListId();


        addToListLayout.setVisibility(View.VISIBLE);
        setupListSpinner();


        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
        if(businessDashboard!=null){
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }


        uploadImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(getPickImageChooserIntent(), 201);

            }
        });

        saveItem=(Button)findViewById(R.id.saveItem);
        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemName.getText().toString().length() !=0  ){
                    if(itemPrice.getText().toString().length() != 0){
                        if(itemBrand.getText().toString().length() != 0){
                            if(itemQuantity.getText().toString().length() != 0){
                                if(itemDesc.getText().toString().length() != 0){
                                    if(Integer.parseInt(itemPrice.getText().toString())>0){
                                        if(Integer.parseInt(itemQuantity.getText().toString())>0){
                                            if(!singleItem){
                                                    if(businessDashboard != null && listId != null){
                                                        if(arrayList.size()>=2){
                                                            if(selectedImagesFiles.size()>0){
                                                                customProgressDialog.showDialog();
                                                                compressImagesNupload();
                                                            }else{
                                                                customProgressDialog.showDialog();
                                                                formString();
                                                            }
                                                        }else{
                                                            Toast.makeText(VendorEditItemActivity.this,"upload atleast 2 images",Toast.LENGTH_LONG).show();
                                                        }
                                                    }

                                            }else{
                                                if(businessDashboard != null && listId != null){
                                                    if(arrayList.size()>=2){
                                                        if(selectedImagesFiles.size()>0){
                                                            customProgressDialog.showDialog();
                                                            compressImagesNupload();
                                                        }else{
                                                            customProgressDialog.showDialog();
                                                            formString();
                                                        }
                                                    }else{
                                                        Toast.makeText(VendorEditItemActivity.this,"upload atleast 2 images",Toast.LENGTH_LONG).show();
                                                    }
                                                }

                                            }
                                        }else{
                                            Toast.makeText(VendorEditItemActivity.this,"Invalid item quantity",Toast.LENGTH_LONG).show();
                                        }
                                    }else{
                                        Toast.makeText(VendorEditItemActivity.this,"Invalid item price",Toast.LENGTH_LONG).show();

                                    }
                                }else{
                                    Toast.makeText(VendorEditItemActivity.this,"OrderedItem Description cannot be empty",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(VendorEditItemActivity.this,"OrderedItem Quantity cannot be empty",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(VendorEditItemActivity.this,"OrderedItem brand cannot be empty",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(VendorEditItemActivity.this,"OrderedItem Price cannot be empty",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(VendorEditItemActivity.this,"OrderedItem Name cannot be empty",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void getItemDetails(){
        String url = Constants.BASE_URL+"vendor_mobile/get-item-details/"+productDetails.getItemId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        JSONObject itemObj = response.getJSONObject("msg");
                        itemName.setText(itemObj.getString("name"));
                        if(itemObj.has("list_id")){
                                for(int i =0;i<listDetailsArrayList.size();i++){
                                    Log.e(TAG, "onResponse: "+listDetailsArrayList.get(i).getListID() );
                                    if(itemObj.getString("list_id").equals(listDetailsArrayList.get(i).getListID())){
                                        Log.e(TAG, "onResponse: SEL"+listDetailsArrayList.get(i).getListID() );
                                        listSpinner.setSelection(i);
                                    }
                                }
                        }else{
                            listSpinner.setSelection(0);
                        }

                        itemQuantity.setText(itemObj.getString("quantity"));
                        itemDesc.setText(itemObj.getString("description"));
                        itemPrice.setText(itemObj.getString("price"));
                        itemBrand.setText(itemObj.getString("brand"));
                        if(itemObj.getBoolean("single")){
                            itemTypeSpinner.setSelection(0);
//                            packLayout.setVisibility(View.GONE);
                        }else{
                            itemTypeSpinner.setSelection(1);
                            //packLayout.setVisibility(View.VISIBLE);
                            //packOf.setText(itemObj.getString("pack"));
                        }

                        for(int i =0 ;i<itemUnitsArrayList.size();i++){
                            if(itemObj.getJSONObject("units").getString("_id").equals(itemUnitsArrayList.get(i).getUnitId())){
                                quantitySpinner.setSelection(i);
                            }
                        }

                        JSONArray imagesJsonArray = itemObj.getJSONArray("images");
                        for(int i =0;i<imagesJsonArray.length();i++){
                            arrayList.add(new ProductImages(null,imagesJsonArray.getJSONObject(i).getString("url")));
                            selectedImagesFiles.add(null);
                        }

                    }else{
                        Toast.makeText(VendorEditItemActivity.this,"Couldn't fetch details.Try again later",Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VendorEditItemActivity.this,"Couldn't fetch details.Try again later",Toast.LENGTH_LONG).show();
                finish();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"GET_PROD_DETAILS");
    }


    ArrayList<ListDetailsModel> listDetailsArrayList = new ArrayList<>();
    List<String> listNames = new ArrayList<>();
    private void setupListSpinner() {
        String url = Constants.BASE_URL+"vendor_mobile/get-business-lists/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if(response.getBoolean("success")){
                        JSONArray msg = null;
                        msg = response.getJSONArray("msg");

                        listDetailsArrayList.clear();
                        listDetailsArrayList.add(new ListDetailsModel("","None","","","","",false,true));
                        listNames.add("None");

                        for(int i=0;i<msg.length();i++){

                            JSONObject list = msg.getJSONObject(i);
                            if(list.has("image")){
                                JSONObject imgObj = list.getJSONObject("image");
                                listDetailsArrayList.add(new ListDetailsModel(list.getString("_id"),list.getString("list_name"),
                                        list.getString("vendor_id"),list.getString("business_id"),
                                        imgObj.getString("url"),imgObj.getString("key"),
                                        list.getBoolean("approved"), true));
                            }else{
                                listDetailsArrayList.add(new ListDetailsModel(list.getString("_id"),list.getString("list_name"),
                                        list.getString("vendor_id"),list.getString("business_id"),
                                        "","",
                                        list.getBoolean("approved"), true));
                            }
                            listNames.add(list.getString("list_name"));
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(VendorEditItemActivity.this, android.R.layout.simple_spinner_item, listNames){
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
                        listSpinner.setAdapter(dataAdapter);
                        listSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                                listId = listDetailsArrayList.get(i).getListID();

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                Toast.makeText(VendorEditItemActivity.this,"PLEASE SELECT A LIST",Toast.LENGTH_LONG).show();
                            }
                        });

                        Log.e(TAG, "onResponse: SIZE"+listDetailsArrayList.size() );
                        getItemDetails();

                    }else{
                        Toast.makeText(VendorEditItemActivity.this,response.getString("msg"),Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VendorEditItemActivity.this,"Please try again!",Toast.LENGTH_LONG).show();

            }
        });
        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest);
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
    ArrayList<ProductImages> arrayList = new ArrayList<>();
    ArrayList<File> selectedImagesFiles = new ArrayList<>();
    ProductImagesAdapter productImagesAdapter;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                filePath  = FetchPath.getPath(this, photoUri);
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
                filePath  = FetchPath.getPath(this, photoUri);
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

        if(arrayList.size()<=7){
            arrayList.add(new ProductImages(myBitmap,""));
        }else{
            Toast.makeText(VendorEditItemActivity.this,"Max 8 images",Toast.LENGTH_LONG).show();
        }
        selectedImagesFiles.add(imgFile);
        productImagesAdapter.notifyDataSetChanged();
        Log.e(TAG, "onActivityResult: "+arrayList.size() );
        Log.e(TAG, "onActivityResult: "+selectedImagesFiles.size() );

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
                ActivityCompat.requestPermissions(VendorEditItemActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
            }

        }
    }

    List<String> plansList;
    String selectedUnitId;
    private void getAllUnits(){
        String url = Constants.BASE_URL+"vendor/get-all-units";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        JSONArray unitsJsonArray = response.getJSONArray("msg");
                        itemUnitsArrayList = new ArrayList<>();
                        plansList = new ArrayList<>();
                        for(int i=0;i<unitsJsonArray.length();i++){
                            itemUnitsArrayList.add(new ItemUnits(unitsJsonArray.getJSONObject(i).getString("_id"),
                                    unitsJsonArray.getJSONObject(i).getString("name")));
                            plansList.add(unitsJsonArray.getJSONObject(i).getString("name"));
                        }
                        setupQuantitySpinner();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: "+error.getMessage());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("User-Agent", "one");
                params.put("header22", "two");

                return params;
            }
        };
        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"ITEM_UNITS");
    }

    private void setupQuantitySpinner(){

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, plansList){
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
        quantitySpinner.setAdapter(dataAdapter);
        quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                selectedUnitId = itemUnitsArrayList.get(i).getUnitId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(VendorEditItemActivity.this,"PLEASE SELECT GENDER",Toast.LENGTH_LONG).show();
            }
        });

    }

    boolean singleItem=false;
    private void setupDiscountSpinner(){
        final List<String> discountTypes = Arrays.asList(getResources().getStringArray(R.array.item_type));

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
        itemTypeSpinner.setAdapter(dataAdapter);
        itemTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );

                if(i!=0){
                    singleItem=false;
                    //packLayout.setVisibility(View.VISIBLE);
                }else{
                    singleItem=true;
                    //packLayout.setVisibility(View.GONE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(VendorEditItemActivity.this,"PLEASE SELECT GENDER",Toast.LENGTH_LONG).show();
            }
        });

    }




    private void formString(){
        PrefManager prefManager= new PrefManager(VendorEditItemActivity.this);

        try {
            JSONObject postItemJson = new JSONObject();

            postItemJson.put("vendor_id",prefManager.getVendorId(VendorEditItemActivity.this));
            postItemJson.put("business_id",businessDashboard.getBusinessId());
            postItemJson.put("item_id",productDetails.getItemId());
            if(listId == null){
                postItemJson.put("list_id","");

            }else{
                postItemJson.put("list_id",listId);

            }

            postItemJson.put("name",itemName.getText().toString());
            postItemJson.put("price",itemPrice.getText().toString());
            postItemJson.put("quantity",Integer.parseInt(itemQuantity.getText().toString()));
            postItemJson.put("units",selectedUnitId);
            postItemJson.put("brand",itemBrand.getText().toString());
            postItemJson.put("description",itemDesc.getText().toString());

            JSONArray itemImagesJsonArray = new JSONArray();
            for(int i=0;i<imageUploadArrayList.size();i++){
                JSONObject itemImageObject = new JSONObject();
                itemImageObject.put("imageurl",imageUploadArrayList.get(i).getUrl());
                itemImageObject.put("key",imageUploadArrayList.get(i).getKey());
                itemImagesJsonArray.put(itemImageObject);
            }
            Log.e(TAG, "formString: "+itemImagesJsonArray.length()+itemImagesJsonArray.toString() );
            postItemJson.put("images",itemImagesJsonArray);
            postItemJson.put("single",singleItem);
            if(singleItem)
                postItemJson.put("pack",0);
            else
                //postItemJson.put("pack",Integer.parseInt(packOf.getText().toString()));

            postItemString=postItemJson.toString();

            Log.e(TAG, "formString: "+postItemString.toString() );

            // new PostItem().execute();
            addItemToBusiness(postItemJson);
        } catch (JSONException e) {
            Log.e(TAG, "formString: "+e.getMessage() );
            e.printStackTrace();
        }

    }



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
                    File cmpFile = new Compressor(VendorEditItemActivity.this).compressToFile(selectedImagesFiles.get(i),itemName.getText().toString().trim()+Calendar.getInstance().getTimeInMillis());
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


    //S3 bucket
    AmazonS3Client s3;
    BasicAWSCredentials credentials;
    TransferUtility transferUtility;
    TransferObserver observer;

    private void setupAws(){
        credentials = new BasicAWSCredentials(Constants.S3_ACCESS_KEY_ID,Constants.S3_SECRET_ACCESS_KEY);
        s3 = new AmazonS3Client(credentials);
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));
        transferUtility = new TransferUtility(s3, this);
    }


    String imgKey;

    private  String getRandomString(final int sizeOfRandomString)
    {
        String ALLOWED_CHARACTERS =businessDashboard.getBusinessName()+"qwertyuiopasdfghjklzxcvbnm"+businessDashboard.getBusinessId()+Calendar.getInstance().getTimeInMillis();
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
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

    @Override
    public void deleteImage(int imagePosition) {
        arrayList.remove(imagePosition);
        if(selectedImagesFiles!=null){
            if(selectedImagesFiles.size()>0){
                selectedImagesFiles.remove(imagePosition);
            }
        }
        productImagesAdapter.notifyDataSetChanged();
        //productImagesAdapter.notifyItemRemoved(imagePosition);
    }


    private void addItemToBusiness(JSONObject jsonObject){
        String url = Constants.BASE_URL+"vendor/update-item-mobile";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.POST, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(VendorEditItemActivity.this,"OrderedItem has been updated successfully!",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Log.e(TAG, "onResponse: "+response.getString("msg") );
                        Toast.makeText(VendorEditItemActivity.this,response.getString("msg"),Toast.LENGTH_LONG).show();
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
                Toast.makeText(VendorEditItemActivity.this,"Please try again!",Toast.LENGTH_LONG).show();

            }
        });
        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"ADD_ITEM");
    }


}
