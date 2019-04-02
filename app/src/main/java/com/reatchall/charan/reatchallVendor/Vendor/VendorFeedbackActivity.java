package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorFeedbackActivity extends AppCompatActivity {

    private static final String TAG = "VendorFeedbackActivity";

    ImageView backArrow;

    Button submit;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog ;
    Context context;
    PrefManager prefManager;
    FontEditText generalFeedback,appFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_feedback);
        context = VendorFeedbackActivity.this;
        prefManager = new PrefManager(context);
        customProgressDialog = new CustomProgressDialog(context);
        submit=(Button)findViewById(R.id.submitFeedback);
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
        generalFeedback = (FontEditText)findViewById(R.id.generalFeedback);
        appFeedback = (FontEditText)findViewById(R.id.appFeedback);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.showDialog();
                postFeedback();
            }
        });
    }

    private void postFeedback(){
        String url = Constants.BASE_URL+"vendor/post-vendor-feedback";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
            jsonObject.put("feedback",generalFeedback.getText().toString().trim());
            jsonObject.put("suggestion",appFeedback.getText().toString().trim());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if (response.getBoolean("success")) {
                        Toast.makeText(context,"Success!",Toast.LENGTH_LONG).show();

                        generalFeedback.getText().clear();
                        appFeedback.getText().clear();
                    } else {
                        Toast.makeText(context, "Please try again", Toast.LENGTH_LONG).show();
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
        helper.addToRequestQueue(customJsonRequest);
    }
}
