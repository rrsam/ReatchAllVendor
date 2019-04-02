package com.reatchall.charan.reatchallVendor.Vendor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.Models.Appointment;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.VendorAppointmentAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorsAppoinmentListActivity extends AppCompatActivity {

    ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;
    private static final String TAG = "VendorsAppoinmentListActivity";

    ReatchAll helper;

    private ArrayList<Appointment> mList = new ArrayList<>();

    private RecyclerView rvAppointments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendors_appoinment_list);

        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        helper = ReatchAll.getInstance();
        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);

        businessDashboard = getIntent().getExtras().getParcelable("businessDetails");
        if(businessDashboard != null){

            Log.e(TAG, "onCreate:ID "+businessDashboard.getBusinessId() +"NAME"+businessDashboard.getBusinessName());
            titleToolbar.setText("Appointments");
        }

        rvAppointments=(RecyclerView) findViewById(R.id.rv_appointments);

        rvAppointments.setHasFixedSize(true);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        updateApoointments();

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void updateApoointments() {

        String url= Constants.BASE_URL + "vendor/get-appointments-by-business_id/"+ businessDashboard.getBusinessId();

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{
                    boolean status = response.getBoolean("success");
                    JSONArray msg = response.getJSONArray("msg");
                    int count = msg.length();
                    Log.e(TAG,String.valueOf(count));
                    if(count>0){
                        for (int i = 0; i < count; i++) {

                            JSONObject msgObj = msg.getJSONObject(i);
                            String bookingId = msgObj.getString("_id");
                            String serviceName = msgObj.getString("service_name");
                            String validity_fromdate = msgObj.getString("validity_fromdate");
                            String validity_todate = msgObj.getString("validity_todate");
                            JSONObject serviceObj =msgObj.getJSONObject("service");
                            String hours = serviceObj.getString("hours");
                            String minutes = serviceObj.getString("minutes");
                            String date = serviceObj.getString("date");
                            String createDate = serviceObj.getString("createDate");

                            JSONObject users = msgObj.getJSONObject("users");
                            String customerName = users.getString("firstname");
                            mList.add(new Appointment(bookingId,date.split("T")[0],customerName,serviceName,createDate));
                        }
                         rvAppointments.setAdapter(new VendorAppointmentAdapter(VendorsAppoinmentListActivity.this,mList));
                    }

                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest, businessDashboard.getBusinessId());
    }
}
