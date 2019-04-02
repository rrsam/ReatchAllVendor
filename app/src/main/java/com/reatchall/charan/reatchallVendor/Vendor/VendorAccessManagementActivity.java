package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.ViewManagersAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorAccessManagementActivity extends AppCompatActivity {
    private static final String TAG = "VendorAccessManagementA";
    FontTextView managers,assignLinks,addManager;

    ImageView backArrow;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    Context context;
    BusinessDetails businessDashboard;
    FontTextView titleToolbar;
    PrefManager prefManager;
    ArrayList<String> managersList = new ArrayList<>();
    RecyclerView managersRcv;
    ViewManagersAdapter viewManagersAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_access_management);
        init();
    }

    private void init(){
        context = VendorAccessManagementActivity.this;
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

        managers=(FontTextView)findViewById(R.id.managers);
        assignLinks=(FontTextView)findViewById(R.id.assignLinks);
        addManager=(FontTextView)findViewById(R.id.addManager);
        managersRcv=(RecyclerView) findViewById(R.id.managersRcv);
        managersRcv.setHasFixedSize(true);
        managersRcv.setNestedScrollingEnabled(false);
        managersRcv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        viewManagersAdapter=new ViewManagersAdapter(context,managersList);
        managersRcv.setAdapter(viewManagersAdapter);

        getBizManagers();

        assignLinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,VendorAssignLinksActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                startActivity(intent);
            }
        });

        addManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,VendorAddManagerActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                startActivity(intent);
            }
        });
    }

    private void getBizManagers(){
        String url = Constants.BASE_URL+"vendor/get-business-managers";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("business_id",businessDashboard.getBusinessId());
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: response"+ response.toString() );
                try {
                    if(response.getBoolean("success")){
                        JSONArray managersArray = response.getJSONArray("msg");
                        managersList = new ArrayList<>();
                        for(int i=0;i<managersArray.length();i++){
                            managersList.add(managersArray.getJSONObject(i).getString("name"));
                        }
                        viewManagersAdapter=new ViewManagersAdapter(context,managersList);
                        managersRcv.setAdapter(viewManagersAdapter);
                    }else{
                        Toast.makeText(context,"Couldn't fetch details",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Couldn't fetch details",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"BIZ_MANAGERS");
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }
}
