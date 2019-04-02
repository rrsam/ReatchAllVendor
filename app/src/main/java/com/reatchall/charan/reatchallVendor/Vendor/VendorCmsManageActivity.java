package com.reatchall.charan.reatchallVendor.Vendor;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
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
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorCmsManageActivity extends Activity {

    private static final String TAG = "VendorCmsManageActivity";

    FontTextView aboutBusiness,aboutUs,profileContent,addPage;
    FontButton submit;
    FontEditText about;
    ImageView backArrow;
    int selectedTab = -1;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    Context context;
    BusinessDetails businessDashboard;
    FontTextView titleToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_cms_manage);
        context = VendorCmsManageActivity.this;
        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
        if(businessDashboard!=null){
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }
        customProgressDialog = new CustomProgressDialog(context);
        customProgressDialog.showDialog();
        getBusinessDetails();
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        about=(FontEditText)findViewById(R.id.about);
        submit=(FontButton)findViewById(R.id.update);
        aboutBusiness=(FontTextView)findViewById(R.id.aboutBusiness);
        aboutUs=(FontTextView)findViewById(R.id.aboutUs);
        profileContent=(FontTextView)findViewById(R.id.profileContent);
        addPage=(FontTextView)findViewById(R.id.addPage);

        aboutBusiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTab=0;
                aboutBusiness.setTextColor(getResources().getColor(R.color.black_variant_two));
                aboutBusiness.setBackground(getResources().getDrawable(R.drawable.address_tag_selected_bg));
                aboutUs.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                aboutUs.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                profileContent.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                profileContent.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                addPage.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                addPage.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                about.getText().clear();
                if(aboutBusinessData.trim().length()>0){
                    about.setText(Html.fromHtml(aboutBusinessData));
                }
                about.setHint("Write something about business");
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTab=1;
                aboutUs.setTextColor(getResources().getColor(R.color.black_variant_two));
                aboutUs.setBackground(getResources().getDrawable(R.drawable.address_tag_selected_bg));
                aboutBusiness.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                aboutBusiness.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                profileContent.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                profileContent.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                addPage.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                addPage.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                about.getText().clear();
                if(aboutUsData.trim().length()>0){
                    about.setText(Html.fromHtml(aboutUsData));
                }
                about.setHint("Write something about you");
            }
        });

        profileContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTab=2;
                profileContent.setTextColor(getResources().getColor(R.color.black_variant_two));
                profileContent.setBackground(getResources().getDrawable(R.drawable.address_tag_selected_bg));
                aboutUs.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                aboutUs.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                aboutBusiness.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                aboutBusiness.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                addPage.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                addPage.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                about.getText().clear();
                about.setHint("Write something about profile Content");

            }
        });

        addPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedTab=3;
                addPage.setTextColor(getResources().getColor(R.color.black_variant_two));
                addPage.setBackground(getResources().getDrawable(R.drawable.address_tag_selected_bg));
                aboutUs.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                aboutUs.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                profileContent.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                profileContent.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                aboutBusiness.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                aboutBusiness.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                about.getText().clear();
                about.setHint("Write something about your page");

            }
        });





        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedTab!=-1){
                    customProgressDialog.showDialog();
                    switch (selectedTab){
                       case 0: updateAboutBusiness();
                                break;
                       case 1: updateAboutUs();
                           break;
                       case 2: updateProfileContent();
                           break;
                       case 3: updateAddPage();
                           break;
                   }
                }
            }
        });
    }

    private void updateAboutBusiness(){

        String url = Constants.BASE_URL+"vendor/update-cms-about-business";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("business_id",businessDashboard.getBusinessId());
            jsonObject.put("about_business",about.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    customProgressDialog.hideDialog();
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Successfully updated!",Toast.LENGTH_LONG).show();
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
                Toast.makeText(context,"Please try again!",Toast.LENGTH_LONG).show();
                customProgressDialog.hideDialog();

            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"ABOUT_BUSINESS");
    }
    private void updateAboutUs(){
        String url = Constants.BASE_URL+"vendor/update-cms-about-contact";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("business_id",businessDashboard.getBusinessId());
            jsonObject.put("about_contact",about.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    customProgressDialog.hideDialog();
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Successfully updated!",Toast.LENGTH_LONG).show();
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
                Toast.makeText(context,"Please try again!",Toast.LENGTH_LONG).show();
                customProgressDialog.hideDialog();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"ABOUT_US");
    }
    private void updateProfileContent(){
        customProgressDialog.hideDialog();
        Toast.makeText(context,"Successfully updated!",Toast.LENGTH_LONG).show();
    }
    private void updateAddPage(){
        customProgressDialog.hideDialog();
        Toast.makeText(context,"Successfully updated!",Toast.LENGTH_LONG).show();
    }

    String aboutBusinessData="",aboutUsData="";
    private void getBusinessDetails(){
        String url = Constants.BASE_URL+"vendor/get-business-profile/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    customProgressDialog.hideDialog();
                    if(response.getBoolean("success")){
                        aboutBusinessData = response.getJSONObject("msg").getString("about_business");
                        aboutUsData=response.getJSONObject("msg").getJSONObject("contacts").getString("about_contact");
                        aboutBusiness.performClick();

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
}
