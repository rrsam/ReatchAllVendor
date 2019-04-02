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
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorAddCustomerActivity extends AppCompatActivity {

    private static final String TAG = "VendorAddCustomerActivity";
    public ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;

    PrefManager prefManager;
    Context context;
    ReatchAll helper = ReatchAll.getInstance();

    CustomProgressDialog customProgressDialog;

    Spinner customerGenderSpinner;

    FontEditText customerMobile,customerEmail,customerName,customerAddress;

    FontButton saveCustomerDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_add_customer);
        customProgressDialog = new CustomProgressDialog(VendorAddCustomerActivity.this);
        context = VendorAddCustomerActivity.this;
        prefManager = new PrefManager(this);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
        if(businessDashboard!=null){
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }
        initViews();

    }

    private void initViews(){
        customerGenderSpinner=(Spinner)findViewById(R.id.customerGenderSpinner);
        customerAddress=(FontEditText)findViewById(R.id.customerAddress);
        customerMobile=(FontEditText)findViewById(R.id.customerMobile);
        customerEmail=(FontEditText)findViewById(R.id.customerEmail);
        customerName=(FontEditText)findViewById(R.id.customerName);
        saveCustomerDetails=(FontButton) findViewById(R.id.addCustomerBtn);

        setupGenderSpinner();

        saveCustomerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateCustomerLayout();
            }
        });

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
        customerGenderSpinner.setAdapter(dataAdapter);
        customerGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    private void validateCustomerLayout(){

        if(!customerAddress.getText().toString().isEmpty()){
            if(!customerMobile.getText().toString().isEmpty()){
                if(!customerEmail.getText().toString().isEmpty()){
                    if(!customerName.getText().toString().isEmpty()){
                        customProgressDialog.showDialog();
                        formJson();
                    }else{
                        Toast.makeText(context,"Customer Name can't be empty",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(context,"Customer Email can't be empty",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(context,"Customer Mobile number can't be empty",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(context,"Customer Address can't be empty",Toast.LENGTH_LONG).show();
        }
    }

    private void formJson(){
        JSONObject customerObj = new JSONObject();
        try {
            customerObj.put("name",customerName.getText().toString().trim());
            customerObj.put("email",customerEmail.getText().toString().trim());
            customerObj.put("mobile",customerMobile.getText().toString().trim());
            customerObj.put("address",customerAddress.getText().toString().trim());
            int item = customerGenderSpinner.getSelectedItemPosition();
            if(item == 0)
                customerObj.put("gender","male");
            else
                customerObj.put("gender","female");
            customerObj.put("vendor_id",prefManager.getVendorId(context));

            addCustomer(customerObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addCustomer(JSONObject customerObj) {

        String url = Constants.BASE_URL+"vendor/add-customer";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, customerObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: " + response.toString());
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
        helper.addToRequestQueue(customJsonRequest,"ADD_CUSTOMER");
    }
}
