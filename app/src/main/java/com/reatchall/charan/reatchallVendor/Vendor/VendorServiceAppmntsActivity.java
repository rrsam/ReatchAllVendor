package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
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
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.ViewAppointmentsAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ServiceAppointments;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorServiceAppmntsActivity extends AppCompatActivity {

    private static final String TAG = "VendorServiceAppmntsAct";

    ImageView backArrow;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    Context context;
    BusinessDetails businessDashboard;
    FontTextView titleToolbar;
    PrefManager prefManager;
    ArrayList<ServiceAppointments> managersList = new ArrayList<>();
    RecyclerView servicesRcv;
    ViewAppointmentsAdapter viewAppointmentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_service_appmnts);
        context = VendorServiceAppmntsActivity.this;
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
                finish();
            }
        });

        servicesRcv=(RecyclerView) findViewById(R.id.servicesRcv);
        servicesRcv.setHasFixedSize(true);
        servicesRcv.setNestedScrollingEnabled(false);
        servicesRcv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        viewAppointmentsAdapter = new ViewAppointmentsAdapter(context,managersList);
        servicesRcv.setAdapter(viewAppointmentsAdapter);
        customProgressDialog.showDialog();
        getAppointments();
    }

    private void getAppointments() {
        String url = Constants.BASE_URL+"vendor/get-appointments/"+getIntent().getExtras().getString("serviceId");
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        JSONArray appArray = response.getJSONArray("msg");
                        if(appArray.length()>0){
                            managersList = new ArrayList<>();
                            for(int i =0;i<appArray.length();i++){
                                JSONObject appObj = appArray.getJSONObject(i);
                                String date = appObj.getString("date").substring(0,10);
                                date = date +" "+appObj.getString("hours") +":"+appObj.getString("minutes");
                                managersList.add(new ServiceAppointments(appObj.getString("_id"),appObj.getString("service"),
                                        appObj.getString("user"), appObj.getString("mobile"),
                                        appObj.getString("name"), appObj.getString("gender"),date));
                            }
                            viewAppointmentsAdapter = new ViewAppointmentsAdapter(context,managersList);
                            servicesRcv.setAdapter(viewAppointmentsAdapter);
                        }else{
                            Toast.makeText(context,"No appointments found!",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context,"No appointments found!",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"No appointments found!",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest);
    }
}
