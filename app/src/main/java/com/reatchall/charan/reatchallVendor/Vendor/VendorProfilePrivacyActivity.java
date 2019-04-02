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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.reatchall.charan.reatchallVendor.Intro.SplashActivity;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.VendorDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorProfilePrivacyActivity extends AppCompatActivity {
    private static final String TAG = "VendorProfilePrivacyAct";

    Button editProfile;

    //NAV WIDGETS
    LinearLayout navHome,navProfileSettings,navPrivacySettings,navLogout;
    private DrawerLayout mDrawerLayout;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    NavigationView navigationView;
    private View navHeader;
    ImageView drawer;
    private Context context = VendorProfilePrivacyActivity.this;

    FontEditText userName,phoneNumber,email;

    VendorDetails vendorDetails= new VendorDetails();
    FontTextView userNameNav;
    ReatchAll helper = ReatchAll.getInstance();
    PrefManager prefManager;
    CustomProgressDialog customProgressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        customProgressDialog = new CustomProgressDialog(context);
        prefManager = new PrefManager(context);
        setContentView(R.layout.activity_vendor_profile_privacy);
        userName=(FontEditText)findViewById(R.id.userName);
        phoneNumber=(FontEditText)findViewById(R.id.phoneNumber);
        email=(FontEditText)findViewById(R.id.email);

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
               /* Intent intent = new Intent(context,VendorEditProfileActivity.class);
                intent.putExtra("vendorDetails",vendorDetails);
                startActivity(intent);*/

               if(phoneNumber.getText().toString().equalsIgnoreCase(email.getText().toString())){
                   customProgressDialog.showDialog();
                   updatePassword();
               }else{
                   Toast.makeText(context,"Password's didn't match",Toast.LENGTH_LONG).show();
               }
            }
        });
    }

    private void updatePassword(){
        String url = Constants.BASE_URL+"vendor/change-password";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
            jsonObject.put("old_password",userName.getText().toString());
            jsonObject.put("new_password",phoneNumber.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Updated Successfully!",Toast.LENGTH_LONG).show();
                        userName.getText().clear();
                        phoneNumber.getText().clear();
                        email.getText().clear();
                    }else{
                        Toast.makeText(context,"Please try again",Toast.LENGTH_LONG).show();
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
        helper.addToRequestQueue(customJsonRequest,"UPDATENAME");
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
                startActivity(new Intent(context,VendorProfileSettingsActivity.class));
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
            }
        });

        navPrivacySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    }

}
