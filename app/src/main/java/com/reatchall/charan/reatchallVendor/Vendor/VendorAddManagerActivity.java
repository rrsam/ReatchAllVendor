package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessController;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorAddManagerActivity extends AppCompatActivity {
    private static final String TAG = "VendorAddManagerActivit";

    Spinner controllerTypeSpinner,controllerGenderSpinner;
    FontEditText controllerConfirmPwd,controllerPassword,controllerMobile,controllerEmail,controllerName;
    String businessCntlrTypeId;
    LinearLayout saveControllerDetails;


    ImageView backArrow;
    FontTextView titleToolbar;
    Context context;
    CustomProgressDialog customProgressDialog;
    ReatchAll helper = ReatchAll.getInstance();
    BusinessDetails businessDashboard;
    private JSONObject mAssignObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_add_manager);
        context = VendorAddManagerActivity.this;
        customProgressDialog = new CustomProgressDialog(context);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titleToolbar = (FontTextView) findViewById(R.id.title_toolbar);
        businessDashboard = getIntent().getExtras().getParcelable("businessDetails");
        if (businessDashboard != null) {
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }
        initViews();
    }

    private void initViews(){
        controllerTypeSpinner=(Spinner)findViewById(R.id.controllerTypeSpinner);
        controllerGenderSpinner=(Spinner)findViewById(R.id.controllerGenderSpinner);
        controllerConfirmPwd=(FontEditText)findViewById(R.id.controllerConfirmPassword);
        controllerPassword=(FontEditText)findViewById(R.id.controllerPassword);
        controllerMobile=(FontEditText)findViewById(R.id.controllerMobile);
        controllerEmail=(FontEditText)findViewById(R.id.controllerEmail);
        controllerName=(FontEditText)findViewById(R.id.controllerName);
        saveControllerDetails =(LinearLayout)findViewById(R.id.saveControllerDetails);
        saveControllerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validateControllerLayout();
            }
        });
        customProgressDialog.showDialog();
        getBusinessControllersData();
        setupGenderSpinner();


        controllerTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                businessCntlrTypeId=businessControllerArrayList.get(i).getControllerid();
                Log.e(TAG, "onItemSelected:BCID "+controllerTypeSpinner.getSelectedItem().toString()+" "+businessCntlrTypeId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                businessCntlrTypeId=null;
            }
        });

    }

    private void fetchManagerAssignLinks() {
        String url = Constants.BASE_URL+"admin/get-vendor-links";

        CustomJsonRequest customJsonRequest= new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    mAssignObject = new JSONObject();
                    if(success){
                        String key;
                        JSONArray jsonArray = response.getJSONArray("msg");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            String _id = obj.getString("_id");
                            String name = obj.getString("name");
                            String __v = obj.getString("__v");
                            String createddate = obj.getString("createddate");
                            mAssignObject.put(name,false);
                        }
                    }

                } catch (JSONException e) {


                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"BUS_CNTRLR_DATA");

    }

    ArrayList<BusinessController> businessControllerArrayList;
    private void getBusinessControllersData(){
        businessControllerArrayList = new ArrayList<>();
        String url = Constants.BASE_URL+"admin/get-business-controllers";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    customProgressDialog.hideDialog();
                    if(response.getBoolean("success")){
                        JSONArray data = response.getJSONArray("msg");
                        List<String> cntrlNamesList = new ArrayList<>();
                        for(int i = 0 ;i<data.length();i++){
                            JSONObject cntrlTypeObject = data.getJSONObject(i);
                            businessControllerArrayList.add(new BusinessController(cntrlTypeObject.getString("_id"),
                                    cntrlTypeObject.getString("name")));
                            cntrlNamesList.add(cntrlTypeObject.getString("name"));
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, cntrlNamesList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        controllerTypeSpinner.setAdapter(dataAdapter);

                    }
                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();

            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"BUS_CNTRLR_DATA");
    }

    private void setupGenderSpinner(){

        final List<String> plansList = Arrays.asList(getResources().getStringArray(R.array.genderArray));
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
        controllerGenderSpinner.setAdapter(dataAdapter);
        controllerGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(context,"PLEASE SELECT GENDER",Toast.LENGTH_LONG).show();
            }
        });

    }

    private void validateControllerLayout(){

            if(businessCntlrTypeId!=null){
                if(!businessCntlrTypeId.isEmpty()){
                    if(!controllerPassword.getText().toString().isEmpty()){
                        if(!controllerMobile.getText().toString().isEmpty()){
                            if(!controllerEmail.getText().toString().isEmpty()){
                                if(!controllerName.getText().toString().isEmpty()){
                                    if(controllerPassword.getText().toString().equals(controllerConfirmPwd.getText().toString())) {
                                        fetchManagerAssignLinks();
                                        customProgressDialog.showDialog();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                formJson();
                                            }
                                        },2000);

                                    }else{
                                        Toast.makeText(context,"Password not matching",Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(context,"Controller Name can't be empty",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(context,"Controller Email can't be empty",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(context,"Controller Mobile number can't be empty",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context,"Controller Password can't be empty",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(context,"Please select controller type",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(context,"Please select Controller type",Toast.LENGTH_LONG).show();
            }
    }

    private void formJson(){
        JSONObject business = new JSONObject();
        try {
            business.put("controller_name",controllerName.getText().toString().trim());
            business.put("manager_email",controllerEmail.getText().toString().trim());
            business.put("business_id",businessDashboard.getBusinessId());
            business.put("controller_type",businessCntlrTypeId);
            business.put("password",controllerPassword.getText().toString().trim());
            business.put("controller_mobile",controllerMobile.getText().toString().trim());
            business.put("gender",controllerGenderSpinner.getSelectedItem());
            business.put("permissions",mAssignObject);
            PrefManager prefManager = new PrefManager(context);
            business.put("vendor_id",prefManager.getVendorId(context));
            Log.e(TAG, "formJson: "+ business.toString() );
            addManager(business);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addManager(JSONObject business) {
        String url = Constants.BASE_URL+"vendor/add-manager";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, business, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(context,response.getString("msg").toString(),Toast.LENGTH_LONG).show();
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
        helper.addToRequestQueue(customJsonRequest,"ADD_MANAGER");
    }
}
