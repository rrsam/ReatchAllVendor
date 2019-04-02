package com.reatchall.charan.reatchallVendor.Vendor;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.reatchall.charan.reatchallVendor.Cabs.CabsHomeActivity;
import com.reatchall.charan.reatchallVendor.Intro.SplashActivity;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorHomeActivity extends Activity{

    private static final String TAG = "VendorHomeActivity";

    private DrawerLayout mDrawerLayout;
    Toolbar toolbar;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    ImageView drawer;
    NavigationView navigationView;
    private View navHeader;
    int min = 1;
    int max = 3;
    int numOfBusiness=0;
    int businessDetailsArray[];

    FontTextView bookCab;

    LinearLayout navHome,navProfileSettings,navPrivacySettings,navLogout;
    Context context=VendorHomeActivity.this;

    FontTextView userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_home);

        bookCab=(FontTextView)findViewById(R.id.bookCab);
        bookCab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VendorHomeActivity.this,CabsHomeActivity.class));
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.navigation_view);
        navHeader = navigationView.getHeaderView(0);
        drawer=(ImageView)findViewById(R.id.person_toolbar);
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setUpNavigationView();
        setFragment();
    }


    private void setFragment(){
        Fragment fragment = new VendorHomeFragment();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment, fragment.getTag());
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void setUpNavigationView() {
        navHome=(LinearLayout)navHeader.findViewById(R.id.navHome);
        navPrivacySettings=(LinearLayout)navHeader.findViewById(R.id.navPrivacySettings);
        navProfileSettings=(LinearLayout)navHeader.findViewById(R.id.navProfileSettings);
        navLogout=(LinearLayout)navHeader.findViewById(R.id.navLogout);

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        userName=(FontTextView) navHeader.findViewById(R.id.userName);
        getVendorDetails();
    }



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
                        userName.setText(msg.getString("name"));
                    }else{
                        userName.setText("");
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
