package com.reatchall.charan.reatchallVendor.Vendor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorOpenCloseStatusActivity extends AppCompatActivity {

    ImageView backArrow,statusOpen,statusClose;
    BusinessDetails businessDashboard = null;
    FontTextView titleToolbar,statusTV;


    LinearLayout openCloseLayout,closeLayout;
    Spinner hourSpinner;

    CustomProgressDialog customProgressDialog;
    ReatchAll helper = ReatchAll.getInstance();
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_open_close_status);
        context = VendorOpenCloseStatusActivity.this;
        customProgressDialog = new CustomProgressDialog(context);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        statusOpen=(ImageView)findViewById(R.id.statusOpen);
        statusClose=(ImageView)findViewById(R.id.statusClose);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titleToolbar = (FontTextView) findViewById(R.id.title_toolbar);
        statusTV = (FontTextView) findViewById(R.id.statusTV);
        businessDashboard = getIntent().getExtras().getParcelable("businessDetails");
        if (businessDashboard != null) {
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }
        setupHourSpinner();
        openCloseLayout=(LinearLayout) findViewById(R.id.openCloseLayout);
        closeLayout=(LinearLayout) findViewById(R.id.closeLayout);
        customProgressDialog.showDialog();
        getBusinessStatus();

        openCloseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.showDialog();
               updateStatus();
            }
        });
    }


    private static final String TAG = "VendorOpenCloseStatusAc";
    private void updateStatus(){
        String url = Constants.BASE_URL+"vendor/post-business-status";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("business_id",businessDashboard.getBusinessId());
            jsonObject.put("status",!open);
            if(open){
                jsonObject.put("time_period",noOfHours);
            }
            else{
                jsonObject.put("time_period","");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e(TAG, "updateStatus: "+jsonObject.toString() );

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onResponse: "+response.toString() );
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Updated successfully!",Toast.LENGTH_LONG).show();
                        open = !open;
                        if(open){
                            closeLayout.setVisibility(View.VISIBLE);
                            statusOpen.setVisibility(View.GONE);
                            statusClose.setVisibility(View.VISIBLE);
                            statusTV.setText("CLOSE");
                        }else{
                            closeLayout.setVisibility(View.GONE);
                            statusOpen.setVisibility(View.VISIBLE);
                            statusClose.setVisibility(View.GONE);
                            statusTV.setText("OPEN");
                        }
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
                Toast.makeText(context,"Please try again!",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"UPDATEBUSINESSSTATUS");
    }


    boolean open;
    private void getBusinessStatus(){
        String url = Constants.BASE_URL+"vendor/get-business-status/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        open = response.getJSONObject("msg").getBoolean("open");
                        if(open){
                            closeLayout.setVisibility(View.VISIBLE);
                            statusOpen.setVisibility(View.GONE);
                            statusClose.setVisibility(View.VISIBLE);
                            statusTV.setText("CLOSE");
                        }else{
                            closeLayout.setVisibility(View.GONE);
                            statusOpen.setVisibility(View.VISIBLE);
                            statusClose.setVisibility(View.GONE);
                            statusTV.setText("OPEN");
                        }
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
                Toast.makeText(context,"Couldn't get business status",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest);
    }



    int noOfHours =0;
    private void setupHourSpinner() {
        hourSpinner=(Spinner)findViewById(R.id.hourSpinner);
    final List<String> plansList = Arrays.asList(getResources().getStringArray(R.array.hoursArray));
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
        hourSpinner.setAdapter(dataAdapter);
        hourSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i){
                case 0: noOfHours =1;
                        break;
                case 1: noOfHours =3;
                    break;
                case 2: noOfHours =6;
                    break;
                case 3: noOfHours =12;
                    break;
                case 4: noOfHours =24;
                    break;
                case 5: noOfHours =48;
                    break;
            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });

}
}
