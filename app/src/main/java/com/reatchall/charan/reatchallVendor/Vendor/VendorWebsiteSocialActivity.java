package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorWebsiteSocialActivity extends AppCompatActivity {

    private static final String TAG = "VendorWebsiteSocialActi";


    ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;

    LinearLayout submit;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    Context context;
    FontEditText webLink,twitterLink,fbLink,youtubeLink,linkedInLink,googleplus,instagram;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_website_social);
        context = VendorWebsiteSocialActivity.this;
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
            customProgressDialog.showDialog();
            getBusinessDetails();
        }

        submit=(LinearLayout)findViewById(R.id.saveWebDetails);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customProgressDialog.showDialog();
                updateBizLinks();
            }
        });

        webLink=(FontEditText)findViewById(R.id.website);
        instagram=(FontEditText)findViewById(R.id.instagram);
        fbLink=(FontEditText)findViewById(R.id.facebook);
        twitterLink=(FontEditText)findViewById(R.id.twitter);
        youtubeLink=(FontEditText)findViewById(R.id.youtubelink);
        linkedInLink=(FontEditText)findViewById(R.id.linkedIn);
        googleplus=(FontEditText)findViewById(R.id.googleplus);

    }

    private void getBusinessDetails(){
        String url = Constants.BASE_URL+"vendor/get-business-profile/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    customProgressDialog.hideDialog();
                    if(response.getBoolean("success")){
                        JSONObject links = response.getJSONObject("msg").getJSONObject("links");
                        if(links.has("website_url"))
                            webLink.setText(links.getString("website_url"));
                        if(links.has("facebook_url"))
                            fbLink.setText(links.getString("facebook_url"));
                        if(links.has("twitter_url"))
                            twitterLink.setText(links.getString("twitter_url"));
                        if(links.has("youtube_url"))
                            youtubeLink.setText(links.getString("youtube_url"));
                        if(links.has("linkedin_url"))
                            linkedInLink.setText(links.getString("linkedin_url"));
                        if(links.has("insta_url"))
                            instagram.setText(links.getString("insta_url"));
                        if(links.has("google_plus_url"))
                            googleplus.setText(links.getString("google_plus_url"));
                    }else{
                        Toast.makeText(context,"Unable to fetch details. Please try again!",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Unable to fetch details. Please try again!",Toast.LENGTH_LONG).show();
            }
        });

        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"GET_DETAILS");
    }

    private void updateBizLinks(){
        String url = Constants.BASE_URL+"vendor/update-business-links";
        JSONObject business = new JSONObject();
        try {
            business.put("business_id",businessDashboard.getBusinessId());
            business.put("website_url",webLink.getText().toString());
            business.put("youtube_url",youtubeLink.getText().toString());
            business.put("twitter_url",twitterLink.getText().toString());
            business.put("facebook_url",fbLink.getText().toString());
            business.put("linkedin_url",linkedInLink.getText().toString());
            business.put("google_plus_url",googleplus.getText().toString());
            business.put("insta_url",instagram.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, business, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Updated Successfully!",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(context,"Please try again!",Toast.LENGTH_LONG).show();
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
        helper.addToRequestQueue(customJsonRequest);
    }


}
