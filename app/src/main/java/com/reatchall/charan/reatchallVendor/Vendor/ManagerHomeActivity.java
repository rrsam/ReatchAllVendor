package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.Intro.SplashActivity;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.ManagerBuzAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.ILoginManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class ManagerHomeActivity extends AppCompatActivity implements ILoginManager {

    private static final String TAG = "ManagerHomeActivity";

    Context context;
    ArrayList<BusinessDetails> businessDetailsArrayList;
    ManagerBuzAdapter managerBuzAdapter;
    RecyclerView recyclerView;
    FontTextView logOut;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home);
        context = ManagerHomeActivity.this;
        customProgressDialog = new CustomProgressDialog(context);
        prefManager = new PrefManager(context);
        logOut=(FontTextView)findViewById(R.id.logOut);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefManager.clearAll(context);
                Intent intent = new Intent(context, SplashActivity.class);
                startActivity(intent);
                finish();
            }
        });

        recyclerView =(RecyclerView)findViewById(R.id.businessRcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

        customProgressDialog.showDialog();
        getManagerBusinesses();
    }

    private void getManagerBusinesses(){
        String url = Constants.BASE_URL+"vendor/get-manager-businesses/"+prefManager.getManagerPhnNum(context);
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        JSONArray msg = response.getJSONArray("msg");
                        businessDetailsArrayList = new ArrayList<>();
                        for(int i =0 ;i<msg.length();i++){
                         BusinessDetails businessDetails = new BusinessDetails();
                         businessDetails.setBusinessId(msg.getJSONObject(i).getJSONObject("basic").getString("_id"));
                         businessDetails.setBusinessName(msg.getJSONObject(i).getJSONObject("basic").getString("name"));
                         businessDetails.setVendorId(msg.getJSONObject(i).getJSONObject("basic").getString("vendor_id"));
                         if(msg.getJSONObject(i).getJSONObject("business_type").getString("name").toLowerCase().contains("service")){
                             businessDetails.setService(true);
                         }else{
                             businessDetails.setService(false);
                         }
                         prefManager.setVendorId(businessDetails.getVendorId(),context);
                         businessDetailsArrayList.add(businessDetails);
                        }
                        managerBuzAdapter = new ManagerBuzAdapter(context,businessDetailsArrayList,ManagerHomeActivity.this);
                        recyclerView.setAdapter(managerBuzAdapter);
                    }else{
                        Toast.makeText(context,"couldn't fetch details",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"couldn't fetch details",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest);
    }

    @Override
    public void loginManager(int pos) {
        ManagerPasswordDialog managerPasswordDialog = new ManagerPasswordDialog(context);
        managerPasswordDialog.setPositveButton("Submit", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(managerPasswordDialog.getPassword().length()>=6){
                    managerPasswordDialog.dismiss();
                    customProgressDialog.showDialog();
                    managerLogin(managerPasswordDialog.getPassword(),pos);
                }else{
                    Toast.makeText(context,"Invalid password",Toast.LENGTH_LONG).show();
                }
            }
        });
        managerPasswordDialog.show();
    }

    private void managerLogin(String password, int pos){
        String url = Constants.BASE_URL+"vendor/manager-mobile-login";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile",prefManager.getManagerPhnNum(context));
            jsonObject.put("password",password);
            jsonObject.put("business_id",businessDetailsArrayList.get(pos).getBusinessId());
            Log.e(TAG, "managerLogin: "+jsonObject.toString() );
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        if(businessDetailsArrayList.get(pos).isService()){
                            Intent intent = new Intent(context,VendorDashBoardNewActivity.class);
                            intent.putExtra("businessDetails",businessDetailsArrayList.get(pos));
                            startActivity(intent);
                        }else{
                            Intent intent = new Intent(context,VendorDashBoardActivity.class);
                            intent.putExtra("businessDetails",businessDetailsArrayList.get(pos));
                            startActivity(intent);
                        }
                    }else{
                        Toast.makeText(context,response.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Unable to log you in.Please try again",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest);
    }

}
