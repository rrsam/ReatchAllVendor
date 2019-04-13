package com.reatchall.charan.reatchallVendor.Vendor;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
import com.bumptech.glide.Glide;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.ConfirmationDialog;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.FetchPath;
import com.reatchall.charan.reatchallVendor.Utils.ImageCompression.Compressor;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.VendorBizBannersAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.VendorDemoImagesAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.VendorPromotionsVideoAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.ViewServicesAdaper;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.VendorBizBannersFragment;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BannerImages;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ImageUpload;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ScheduleValidity;
import com.reatchall.charan.reatchallVendor.Vendor.Models.VendorService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontCheckBox;
import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorServicesActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, ViewServicesAdaper.OnStatusCheckListener, ViewServicesAdaper.onDeleteViewListener {

    private static final String TAG = "VendorServicesActivity";
    FontTextView services,appointments,addService,txtSort,txtSelect,txtActions,subTitle;

    ImageView backArrow;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    Context context;
    BusinessDetails businessDashboard;
    FontTextView titleToolbar;
    PrefManager prefManager;
    ArrayList<VendorService> managersList = new ArrayList<>();
    RecyclerView servicesRcv;
    ViewServicesAdaper viewServicesAdaper;

    ArrayList<String> serviceIdList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_services);
        init();
    }





    private void init(){
        context = VendorServicesActivity.this;
        prefManager = new PrefManager(context);
        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        subTitle = (FontTextView)findViewById(R.id.subTitle);
        businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
        customProgressDialog = new CustomProgressDialog(context);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        services=(FontTextView)findViewById(R.id.services);
        appointments=(FontTextView)findViewById(R.id.appointments);
        addService=(FontTextView)findViewById(R.id.addService);
        servicesRcv=(RecyclerView) findViewById(R.id.servicesRcv);
        txtSort=(FontTextView)findViewById(R.id.txtSort);
        txtSelect=(FontTextView)findViewById(R.id.txtSelect);
        txtActions=(FontTextView)findViewById(R.id.txtActions);



        servicesRcv.setHasFixedSize(true);
        servicesRcv.setNestedScrollingEnabled(false);
        servicesRcv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        viewServicesAdaper=new ViewServicesAdaper(context,managersList,this,this);
        servicesRcv.setAdapter(viewServicesAdaper);




        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customProgressDialog.showDialog();
                getServices();

            }
        });

        addService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,VendorAddServiceActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                startActivity(intent);
            }
        });

        services.performClick();

        txtSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(VendorServicesActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.sort_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(VendorServicesActivity.this);
                popup.show();
            }
        });

        txtActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(VendorServicesActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.products_action_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(VendorServicesActivity.this);
                popup.show();
            }
        });

    }



    public void showAppmnts(String serviceId){
        Intent intent = new Intent(context,VendorServiceAppmntsActivity.class);
        intent.putExtra("businessDetails",businessDashboard);
        intent.putExtra("serviceId",serviceId);
        startActivity(intent);
    }

    private void getServices(){
        String url = Constants.BASE_URL+"vendor/get-all-services-by-business_id/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onResponse: "+response.toString() );
                try {
                    if(response.getBoolean("success")){
                        JSONArray servicesJsonArray = response.getJSONArray("msg");
                        if(servicesJsonArray.length()>0){
                            managersList = new ArrayList<>();
                            for(int i =0;i<servicesJsonArray.length();i++){
                                JSONObject serviceObj = servicesJsonArray.getJSONObject(i);
                                if(serviceObj.has("image")){
                                    JSONObject imgObj = serviceObj.getJSONObject("image");
                                    managersList.add(new VendorService(serviceObj.getString("_id"),serviceObj.getString("business_id"),
                                            serviceObj.getString("service_name"),imgObj.getString("url"),
                                            imgObj.getString("key")));
                                }else{
                                    managersList.add(new VendorService(serviceObj.getString("_id"),serviceObj.getString("business_id"),
                                            serviceObj.getString("service_name"),"", ""));
                                }

                            }
                            viewServicesAdaper = new ViewServicesAdaper(context,managersList,VendorServicesActivity.this,VendorServicesActivity.this);
                            servicesRcv.setAdapter(viewServicesAdaper);
                        }else{
                            Toast.makeText(context,"No services found",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context,"No services found",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Couldn't fetch services",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"GET_SERVICES");
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_menu_all:

                loadAllServices();
                return true;
            case R.id.sort_menu_active:
                loadActiveServices();
                return true;
            case R.id.sort_menu_inactive:

                loadInActiveServices();
                return true;
            case R.id.sort_menu_pending:

                loadPendingServices();
                return true;
            case R.id.products_menu_active:
                makeServicesactive();
                return true;
            case R.id.products_menu_deactive:
                makeServicesdeactive();
                return true;
            case R.id.products_menu_delete:
                makeServicesdelete();
                return true;
            default:
                return false;
        }
    }

    private void loadAllServices() {
        subTitle.setText("All Services");
        getServices();
    }

    private void loadActiveServices() {
        subTitle.setText("Active Services");
        String url = Constants.BASE_URL + "vendor/get-active-services-by-business_id/"+businessDashboard.getBusinessId();
        getTypeServices(url);
    }


    private void loadInActiveServices() {
        subTitle.setText("Inactive Services");
        String url = Constants.BASE_URL + "vendor/get-inactive-services-by-business_id/"+businessDashboard.getBusinessId();
        getTypeServices(url);
    }

    private void loadPendingServices() {
        subTitle.setText("Pending Services");
        String url = Constants.BASE_URL + "vendor/get-pending-services-by-business_id/"+businessDashboard.getBusinessId();
        getTypeServices(url);
    }

    private void makeServicesactive() {
        if(!serviceIdList.isEmpty()){
            JSONObject object = new JSONObject();
            try {
                object.put("business_id", businessDashboard.getBusinessId());
                object.put("service_id", new JSONArray(serviceIdList));
                String url = Constants.BASE_URL + "vendor/post-service-active";
                Log.e(TAG, "makeServicesactive: "+ object.toString() );
                fetchDifferentService(url,object);
                loadActiveServices();
            }catch (Exception e){

            }

        }else{
            Toast.makeText(context, "Select any Service...", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeServicesdeactive() {
        if(!serviceIdList.isEmpty()){
            JSONObject object = new JSONObject();
            try {
                object.put("business_id", businessDashboard.getBusinessId());
                object.put("service_id", new JSONArray(serviceIdList));
                String url = Constants.BASE_URL + "vendor/post-service-inactive";
                fetchDifferentService(url,object);
                loadInActiveServices();
            }catch (Exception e){

            }

        }else{
            Toast.makeText(context, "Select any Service...", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeServicesdelete() {
        if(!serviceIdList.isEmpty()){
            JSONObject object = new JSONObject();
            try {
                object.put("business_id", businessDashboard.getBusinessId());
                object.put("service_id", new JSONArray(serviceIdList));
                String url = Constants.BASE_URL + "vendor/remove-multiple-service";
                fetchDifferentService(url,object);
                loadAllServices();
            }catch (Exception e){

            }

        }else{
            Toast.makeText(context, "Select any Service...", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onCheckedChangedListener(boolean isChecked, VendorService mVendorService) {
        if (isChecked)
            serviceIdList.add(mVendorService.getServiceId());
        else
            serviceIdList.remove(mVendorService.getServiceId());

    }

    private void fetchDifferentService(String url,JSONObject object){
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onResponse: "+response.toString() );
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context, "Services Changed", Toast.LENGTH_LONG).show();
                        serviceIdList.clear();
                    }else{
                        Toast.makeText(context,"No services Changed",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Couldn't fetch services",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"GET_SERVICES");
    }

    private void getTypeServices(String url) {
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onResponse: type "+response.toString() );
                try {
                    if(response.getBoolean("success")){
                        JSONArray servicesJsonArray = response.getJSONArray("msg");
                        if(servicesJsonArray.length()>0){
                            managersList = new ArrayList<>();
                            for(int i =0;i<servicesJsonArray.length();i++){
                                JSONObject serviceObj = servicesJsonArray.getJSONObject(i);
                                if(serviceObj.has("image")){
                                    JSONObject imgObj = serviceObj.getJSONObject("image");
                                    managersList.add(new VendorService(serviceObj.getString("_id"),serviceObj.getString("business_id"),
                                            serviceObj.getString("service_name"),imgObj.getString("url"),
                                            imgObj.getString("key")));
                                }else{
                                    managersList.add(new VendorService(serviceObj.getString("_id"),serviceObj.getString("business_id"),
                                            serviceObj.getString("service_name"),"", ""));
                                }

                            }
                            viewServicesAdaper = new ViewServicesAdaper(context,managersList,VendorServicesActivity.this,VendorServicesActivity.this);
                            servicesRcv.setAdapter(viewServicesAdaper);
                        }else{
                            managersList.clear();
                            viewServicesAdaper.notifyDataSetChanged();
                            Toast.makeText(context,"No services found",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        managersList.clear();
                        viewServicesAdaper.notifyDataSetChanged();
                        Toast.makeText(context,"No services found",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Couldn't fetch services",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"GET_SERVICES");

    }

    @Override
    public void onDeleteViewClicked(int position) {
        deleteOfferDialog(position);
    }

    private void deleteOfferDialog(int position){
        ConfirmationDialog mAlert = new ConfirmationDialog(VendorServicesActivity.this);
        mAlert.setTitle("Delete Service");
        mAlert.setIcon(android.R.drawable.ic_dialog_alert);
        mAlert.setMessage("Are you sure you want to delete this Service? You can't undo this action.");
        mAlert.setPositveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
                deleteService(position);
                customProgressDialog.showDialog();

                //Do want you want
            }
        });

        mAlert.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
                //Do want you want
            }
        });

        mAlert.show();
    }

    private void deleteService(int positon){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("service_id",managersList.get(positon).getServiceId());
            String url = Constants.BASE_URL+"vendor/delete-service/"+managersList.get(positon).getServiceId();
            CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    customProgressDialog.hideDialog();
                    try {
                        if(response.getBoolean("success")){
                            Toast.makeText(VendorServicesActivity.this,"Success",Toast.LENGTH_LONG).show();
                            updateList(positon);
                        }else{
                            Toast.makeText(VendorServicesActivity.this,"Couldn't delete list. Please Try again!",Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    customProgressDialog.hideDialog();
                    Toast.makeText(VendorServicesActivity.this,"Couldn't delete list. Please Try again!",Toast.LENGTH_LONG).show();
                }
            });
            customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
            helper.addToRequestQueue(customJsonRequest,"DELETE_LIST");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateList(int i){
        managersList.remove(i);
        viewServicesAdaper.notifyItemRemoved(i);
    }
}
