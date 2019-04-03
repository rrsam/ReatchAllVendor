package com.reatchall.charan.reatchallVendor.Vendor;

import android.Manifest;
import android.animation.LayoutTransition;
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
import android.text.Editable;
import android.text.TextWatcher;
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
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ImageUpload;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ItemUnits;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ListDetailsModel;
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

public class VendorAddItemsActivity extends AppCompatActivity implements IDeletePromoImage {

    private static final String TAG = "VendorAddItemsActivity";
    ImageView backArrow;
    String listId=null,postItemString=null;
    BusinessDetails businessDashboard;
    FontTextView titleToolbar;


    FontEditText itemName,itemQuantity,itemPrice;
    FontEditText itemBrand,itemDesc,itemSpecs,discountValue;
    Spinner quantitySpinner,itemTypeSpinner,discountTypeSpinner,itemQualitySpinner;
    Button saveItem;
    RecyclerView itemImagesRcv;
    ArrayList<ItemUnits> itemUnitsArrayList;
    LinearLayout uploadImages;
    ReatchAll helper = ReatchAll.getInstance();
    FontTextView itemFinalPrice,discountTV;

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
        setContentView(R.layout.vendor_activity_add_items);
        ((ViewGroup) findViewById(R.id.parentLayout)).getLayoutTransition()
                .enableTransitionType(LayoutTransition.CHANGING);
        customProgressDialog=new CustomProgressDialog(VendorAddItemsActivity.this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            ActivityCompat.requestPermissions(VendorAddItemsActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
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
        itemFinalPrice=(FontTextView) findViewById(R.id.item_final_price);
        discountTV=(FontTextView) findViewById(R.id.discountTV);
        listSpinner =(Spinner)findViewById(R.id.listSpinner);
        discountTypeSpinner =(Spinner)findViewById(R.id.discountTypeSpinner);
        itemQualitySpinner =(Spinner)findViewById(R.id.itemQualitySpinner);
        addToListLayout=(LinearLayout)findViewById(R.id.addToListLayout);
        uploadImages=(LinearLayout) findViewById(R.id.uploadImages);
        itemQuantity=(FontEditText)findViewById(R.id.item_quantity);
        itemPrice=(FontEditText)findViewById(R.id.item_price);
        quantitySpinner=(Spinner)findViewById(R.id.quantitySpinner);
        itemTypeSpinner=(Spinner)findViewById(R.id.itemTypeSpinner);
        itemBrand=(FontEditText)findViewById(R.id.itemBrand);
        itemDesc=(FontEditText)findViewById(R.id.itemDesc);
        itemSpecs=(FontEditText)findViewById(R.id.itemSpecs);
        discountValue=(FontEditText)findViewById(R.id.discountValue);
        itemImagesRcv=(RecyclerView)findViewById(R.id.itemImagesRcv);
        itemImagesRcv.setNestedScrollingEnabled(false);
        itemImagesRcv.setLayoutManager(new LinearLayoutManager(VendorAddItemsActivity.this,LinearLayoutManager.HORIZONTAL,false));
        productImagesAdapter = new ProductImagesAdapter(VendorAddItemsActivity.this,arrayList,this);
        itemImagesRcv.setAdapter(productImagesAdapter);

        setupItemTypeSpinner();
        setupDiscountTypeSpinner();
        setupItemQualitySpinner();

        businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
        listId=getIntent().getExtras().getString("listId");

        if(listId==null){
            addToListLayout.setVisibility(View.VISIBLE);
            setupListSpinner();
        }else{
            addToListLayout.setVisibility(View.GONE);
        }


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

        itemPrice.setText(0+"");
        discountValue.setText(0+"");
        itemFinalPrice.setText(0+"");

        saveItem=(Button)findViewById(R.id.saveItem);
        saveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemName.getText().toString().length() !=0  ){
                    if(itemPrice.getText().toString().length() != 0){
                        if(itemBrand.getText().toString().length() != 0){
                            if(itemQuantity.getText().toString().length() != 0){
                                if(Integer.parseInt(itemPrice.getText().toString())>0){
                                    if(itemDesc.getText().toString().length() != 0){
                                        if(businessDashboard != null && listId != null){
                                            if(selectedImagesFiles.size()>=2){
                                                if(Integer.parseInt(itemQuantity.getText().toString())>0){
                                                    if(Integer.parseInt(itemFinalPrice.getText().toString())>0){
                                                        customProgressDialog.showDialog();
                                                        compressImagesNupload();
                                                    }else{
                                                        Toast.makeText(VendorAddItemsActivity.this,"Invalid final price",Toast.LENGTH_LONG).show();
                                                    }
                                                }else{
                                                    Toast.makeText(VendorAddItemsActivity.this,"Invalid item quantity",Toast.LENGTH_LONG).show();
                                                }
                                            }else{
                                                Toast.makeText(VendorAddItemsActivity.this,"Please select atleast 2 images",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    }else{
                                        Toast.makeText(VendorAddItemsActivity.this,"Item Description cannot be empty",Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(VendorAddItemsActivity.this,"Invalid item price",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(VendorAddItemsActivity.this,"Item Quantity cannot be empty",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(VendorAddItemsActivity.this,"Item Brand cannot be empty",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(VendorAddItemsActivity.this,"Item Price cannot be empty",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(VendorAddItemsActivity.this,"Item Name cannot be empty",Toast.LENGTH_LONG).show();
                }

            }
        });

        discountValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getFinalPrice();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    private void getFinalPrice(){
        if(Integer.parseInt(itemPrice.getText().toString().trim())>0){
            if(discountType){
                if(Integer.parseInt(discountValue.getText().toString().trim())>0)
                    itemFinalPrice.setText(Integer.parseInt(itemPrice.getText().toString().trim())-Integer.parseInt(discountValue.getText().toString().trim()));
            }else{
                if(Integer.parseInt(discountValue.getText().toString().trim())>0 && Integer.parseInt(discountValue.getText().toString().trim())<=100){
                    int finalPriceVal = Integer.parseInt(itemPrice.getText().toString().trim()) - (Integer.parseInt(itemPrice.getText().toString().trim())/Integer.parseInt(discountValue.getText().toString().trim()));
                    itemFinalPrice.setText(finalPriceVal+"");
                }
            }
        }
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

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(VendorAddItemsActivity.this, android.R.layout.simple_spinner_item, listNames){
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
                                Toast.makeText(VendorAddItemsActivity.this,"PLEASE SELECT A LIST",Toast.LENGTH_LONG).show();
                            }
                        });

                    }else{
                        Toast.makeText(VendorAddItemsActivity.this,response.getString("msg"),Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VendorAddItemsActivity.this,"Please try again!",Toast.LENGTH_LONG).show();

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

        arrayList.add(new ProductImages(myBitmap,""));
        if(selectedImagesFiles.size()<=7){
            selectedImagesFiles.add(imgFile);
            productImagesAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(VendorAddItemsActivity.this,"Max 8 Images",Toast.LENGTH_LONG).show();
        }
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
                ActivityCompat.requestPermissions(VendorAddItemsActivity.this,permissionsRequired,PERMISSION_CALLBACK_CONSTANT);
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
                Toast.makeText(VendorAddItemsActivity.this,"PLEASE SELECT GENDER",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setupItemTypeSpinner(){
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(VendorAddItemsActivity.this,"PLEASE SELECT GENDER",Toast.LENGTH_LONG).show();
            }
        });

    }
    private void setupItemQualitySpinner(){
        final List<String> discountTypes = Arrays.asList(getResources().getStringArray(R.array.item_quality_array));

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
        itemQualitySpinner.setAdapter(dataAdapter);
        itemQualitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(VendorAddItemsActivity.this,"PLEASE SELECT GENDER",Toast.LENGTH_LONG).show();
            }
        });

    }

    boolean discountType=true;
    private void setupDiscountTypeSpinner(){
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
        discountTypeSpinner.setAdapter(dataAdapter);
        discountTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                if(i==0){
                    discountType = true;
                    discountTV.setText("Dicount in rupees :");
                }else {
                    discountType = false;
                    discountTV.setText("Dicount in % :");
                }

                if(Integer.parseInt(discountValue.getText().toString().trim())>0){
                    getFinalPrice();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(VendorAddItemsActivity.this,"PLEASE SELECT GENDER",Toast.LENGTH_LONG).show();
            }
        });

    }




    private void formString(){
        PrefManager prefManager= new PrefManager(VendorAddItemsActivity.this);

        try {
            JSONObject postItemJson = new JSONObject();

            postItemJson.put("vendor_id",prefManager.getVendorId(VendorAddItemsActivity.this));
            postItemJson.put("business_id",businessDashboard.getBusinessId());
            if(listId == null){
                postItemJson.put("list_id","");

            }else{
                postItemJson.put("list_id",listId);

            }

            postItemJson.put("name",itemName.getText().toString());
            postItemJson.put("price",Integer.parseInt(itemFinalPrice.getText().toString().trim()));
            postItemJson.put("actual_price",Integer.parseInt(itemPrice.getText().toString().trim()));
            postItemJson.put("quantity",Integer.parseInt(itemQuantity.getText().toString().trim()));
            postItemJson.put("discount",Integer.parseInt(discountValue.getText().toString().trim()));
            postItemJson.put("units",selectedUnitId);
            postItemJson.put("rupees",discountType);
            postItemJson.put("quality",itemQualitySpinner.getSelectedItem().toString());
            postItemJson.put("product_type",itemTypeSpinner.getSelectedItem().toString());
            postItemJson.put("brand",itemBrand.getText().toString());
            postItemJson.put("description",itemDesc.getText().toString());
            postItemJson.put("specification",itemSpecs.getText().toString());

            JSONArray itemImagesJsonArray = new JSONArray();
            for(int i=0;i<imageUploadArrayList.size();i++){
                JSONObject itemImageObject = new JSONObject();
                itemImageObject.put("imageurl",imageUploadArrayList.get(i).getUrl());
                itemImageObject.put("key",imageUploadArrayList.get(i).getKey());
                itemImagesJsonArray.put(itemImageObject);
            }
            postItemJson.put("images",itemImagesJsonArray);
            postItemString=postItemJson.toString();

            Log.e(TAG, "formString: "+postItemString.toString() );
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

        for(int i =0;i<selectedImagesFiles.size();i++){
            try {
                File cmpFile = new Compressor(VendorAddItemsActivity.this).compressToFile(selectedImagesFiles.get(i),itemName.getText().toString().trim()+Calendar.getInstance().getTimeInMillis());
                if(cmpFile.exists()){
                    Log.e(TAG, "compressImagesNupload: CMP EXISTS" );
                    compressedFilesList.add(cmpFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(compressedFilesList.size()==selectedImagesFiles.size()){
            Log.e(TAG, "compressImagesNupload: EQUALS" );
            imageUploadArrayList = new ArrayList<>();
            uploadImageToAws(compressedFilesList.get(0),0);
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
        s3.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
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
        selectedImagesFiles.remove(imagePosition);
        //productImagesAdapter.notifyDataSetChanged();
        productImagesAdapter.notifyItemRemoved(imagePosition);
    }


    private void addItemToBusiness(JSONObject jsonObject){
        String url = Constants.BASE_URL+"vendor/post-item-mobile";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.POST, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(VendorAddItemsActivity.this,"OrderedItem has been added successfully!",Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onErrorResponse: "+error.getMessage() + error.getLocalizedMessage() );
                Toast.makeText(VendorAddItemsActivity.this,"Please try again!",Toast.LENGTH_LONG).show();

            }
        });
        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"ADD_ITEM");
    }



}
