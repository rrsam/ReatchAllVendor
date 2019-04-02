package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.reatchall.charan.reatchallVendor.Intro.SplashActivity;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.VendorDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorProfileSettingsActivity extends AppCompatActivity {

    private static final String TAG = "VendorProfileSettingsAc";
    Button editProfile;

    //NAV WIDGETS
    LinearLayout navHome,navProfileSettings,navPrivacySettings,navLogout;
    private DrawerLayout mDrawerLayout;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    NavigationView navigationView;
    private View navHeader;
    ImageView drawer;
    private Context context = VendorProfileSettingsActivity.this;

    FontTextView userName,phoneNumber,email,gender,dateOfBirth;

    VendorDetails vendorDetails= new VendorDetails();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_profile_settings);

        userName=(FontTextView)findViewById(R.id.userName);
        phoneNumber=(FontTextView)findViewById(R.id.phoneNumber);
        email=(FontTextView)findViewById(R.id.email);
        gender=(FontTextView)findViewById(R.id.gender);
        dateOfBirth=(FontTextView)findViewById(R.id.dateOfBirth);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.navigation_view);
        navHeader = navigationView.getHeaderView(0);
        drawer=(ImageView)findViewById(R.id.navigation_drawer_person);
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setUpNavigationView();

        editProfile=(Button)findViewById(R.id.editProfile);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,VendorEditProfileActivity.class);
                intent.putExtra("vendorDetails",vendorDetails);
                startActivity(intent);
            }
        });
    }


    private void setUpNavigationView() {
        navHome=(LinearLayout)navHeader.findViewById(R.id.navHome);
        navPrivacySettings=(LinearLayout)navHeader.findViewById(R.id.navPrivacySettings);
        navProfileSettings=(LinearLayout)navHeader.findViewById(R.id.navProfileSettings);
        navLogout=(LinearLayout)navHeader.findViewById(R.id.navLogout);

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,VendorHomeActivity.class));
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
            }
        });

        navProfileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
            }
        });

        navPrivacySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,VendorProfilePrivacyActivity.class));
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
            }
        });

        navLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
                PrefManager.clearAll(context);
                Intent intent = new Intent(context, SplashActivity.class);
                startActivity(intent);
                finish();
            }
        });
        userNameNav=(FontTextView) navHeader.findViewById(R.id.userName);
        getVendorDetails();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getVendorDetails();
    }

    FontTextView userNameNav;
    ReatchAll helper = ReatchAll.getInstance();
    PrefManager prefManager;
    private void getVendorDetails(){
        prefManager = new PrefManager(context);
        String url = Constants.BASE_URL+"vendor/get-vendor-by-id/"+prefManager.getVendorId(context);
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        JSONObject msg = response.getJSONObject("msg");
                        userNameNav.setText(msg.getString("name"));
                        userName.setText(msg.getString("name"));
                        phoneNumber.setText(msg.getString("mobile"));
                        email.setText(msg.getString("email"));
                        gender.setText(msg.getString("gender"));
                        dateOfBirth.setText(msg.getString("dob").toString().substring(0,10));
                        vendorDetails.setUserName(userName.getText().toString());
                        vendorDetails.setPhoneNumber(phoneNumber.getText().toString());
                        vendorDetails.setEmail(email.getText().toString());
                        vendorDetails.setGender(gender.getText().toString());
                        vendorDetails.setDateOfBirth(dateOfBirth.getText().toString());
                    }else{
                        userNameNav.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"VENDOR_DETAILS");
    }



}
