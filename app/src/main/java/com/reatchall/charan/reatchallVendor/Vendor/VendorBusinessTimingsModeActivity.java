package com.reatchall.charan.reatchallVendor.Vendor;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.suke.widget.SwitchButton;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorBusinessTimingsModeActivity extends AppCompatActivity {
    private static final String TAG = "VendorBusinessTimingsMo";
    ImageView back_arrow;
    FontTextView businessName;
    BusinessDetails businessDetails = null;
    SwitchButton dynamicSwitch,regularSwitch;
    ReatchAll helper = ReatchAll.getInstance();

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_business_timings_mode);
        back_arrow=(ImageView)findViewById(R.id.back_arrow);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        businessName=(FontTextView)findViewById(R.id.businessName);
        businessDetails = getIntent().getExtras().getParcelable("businessDetails");
        if(businessDetails != null){

            Log.e(TAG, "onCreate:ID "+businessDetails.getBusinessId() +"NAME"+businessDetails.getBusinessName());
            businessName.setText(businessDetails.getBusinessName()+" - Dashboard");
        }

        dynamicSwitch=(SwitchButton)findViewById(R.id.dynamicSwitch);
        regularSwitch=(SwitchButton)findViewById(R.id.regularSwitch);
        dynamicSwitch.setChecked(false);
        regularSwitch.setChecked(false);
        regularSwitch.setClickable(false);

        dynamicSwitch.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    showDialog();
                    changeTimingsMode();
                }
            }
        });
    }

    private void showDialog(){
        dialog = new ProgressDialog(VendorBusinessTimingsModeActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("please wait...");
        dialog.show();
    }

    private void hideDialog(){
        dialog.dismiss();
    }

    private void changeTimingsMode(){
        String url = Constants.BASE_URL+"vendor/post-business-timings";
        JSONObject timingsMode = new JSONObject();
        PrefManager prefManager = new PrefManager(VendorBusinessTimingsModeActivity.this);
        String id= prefManager.getVendorId(VendorBusinessTimingsModeActivity.this);
        try {
            timingsMode.put("business_id",businessDetails.getBusinessId());
            timingsMode.put("vendor_id",id);
            timingsMode.put("switch_mode",true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, timingsMode, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(VendorBusinessTimingsModeActivity.this,"Successfull!!!",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(VendorBusinessTimingsModeActivity.this,"Please Try again after some time",Toast.LENGTH_LONG).show();
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(VendorBusinessTimingsModeActivity.this,"Please Try again after some time",Toast.LENGTH_LONG).show();
                finish();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"TIMINGS_MODE");

    }
}
