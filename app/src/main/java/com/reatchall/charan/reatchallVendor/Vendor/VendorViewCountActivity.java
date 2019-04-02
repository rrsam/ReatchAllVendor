package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import org.json.JSONObject;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorViewCountActivity extends AppCompatActivity {

    private static final String TAG = "VendorViewCountActivity";
    FontTextView titleToolbar;
    BusinessDetails businessDashboard = null;
    ImageView backArrow;

    FontButton viewDetails;

    CustomProgressDialog customProgressDialog;

    ReatchAll helper;

    FontTextView todayTV,totalTV,lastMonthTV,lastWeekTV;
    FontTextView todayValue,totalValue,lastMonthValue,lastWeekValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_view_count);
        customProgressDialog = new CustomProgressDialog(this);
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
            titleToolbar.setText(businessDashboard.getBusinessName());
        }

        todayTV=(FontTextView)findViewById(R.id.todayTv);
        lastWeekTV=(FontTextView)findViewById(R.id.lastWeekTV);
        totalTV=(FontTextView)findViewById(R.id.totalTV);
        lastMonthTV=(FontTextView)findViewById(R.id.lastMonthTV);

        todayValue=(FontTextView)findViewById(R.id.todayValue);
        totalValue=(FontTextView)findViewById(R.id.totalValue);
        lastMonthValue=(FontTextView)findViewById(R.id.lastMonthValue);
        lastWeekValue=(FontTextView)findViewById(R.id.lastWeekValue);

        viewDetails = (FontButton) findViewById(R.id.today_new_count);


        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendorViewCountActivity.this, VendorViewDetailsActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                intent.putExtra("spinnerValue",1);
                startActivity(intent);
            }
        });

        todayTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(VendorViewCountActivity.this, VendorViewDetailsActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                intent.putExtra("spinnerValue",1);
                startActivity(intent);
            }
        });

        todayValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendorViewCountActivity.this, VendorViewDetailsActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                intent.putExtra("spinnerValue",1);
                startActivity(intent);
            }
        });

        lastWeekTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendorViewCountActivity.this, VendorViewDetailsActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                intent.putExtra("spinnerValue",2);
                startActivity(intent);
            }
        });
        lastMonthTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendorViewCountActivity.this, VendorViewDetailsActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                intent.putExtra("spinnerValue",3);
                startActivity(intent);
            }
        });

        totalTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendorViewCountActivity.this, VendorViewDetailsActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                intent.putExtra("spinnerValue",4);
                startActivity(intent);
            }
        });


        lastWeekValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendorViewCountActivity.this, VendorViewDetailsActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                intent.putExtra("spinnerValue",2);
                startActivity(intent);
            }
        });
        lastMonthValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendorViewCountActivity.this, VendorViewDetailsActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                intent.putExtra("spinnerValue",3);
                startActivity(intent);
            }
        });

        totalValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendorViewCountActivity.this, VendorViewDetailsActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                intent.putExtra("spinnerValue",4);
                startActivity(intent);
            }
        });

        customProgressDialog.showDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCount();
    }

    private void updateCount() {

        String url= Constants.BASE_URL + "vendor/get-total-business-visits/"+ businessDashboard.getBusinessId();

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try{
                    boolean status = response.getBoolean("success");
                    JSONObject msg = response.getJSONObject("msg");
                    if(status){
                        int today = msg.getInt("today");
                        int lastweek = msg.getInt("lastweek");
                        int lastmonth = msg.getInt("lastmonth");
                        int lastyear = msg.getInt("lastyear");
                        int total = msg.getInt("total");
                        todayValue.setText(String.valueOf(today));
                        totalValue.setText(String.valueOf(total));
                        lastWeekValue.setText(String.valueOf(lastweek));
                        lastMonthValue.setText(String.valueOf(lastmonth));
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
        helper.addToRequestQueue(customJsonRequest, businessDashboard.getBusinessId());
    }
}
