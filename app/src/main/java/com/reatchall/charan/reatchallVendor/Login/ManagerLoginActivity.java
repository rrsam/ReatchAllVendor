package com.reatchall.charan.reatchallVendor.Login;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.ManagerHomeActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.VendorDashBoardActivity;
import com.reatchall.charan.reatchallVendor.Vendor.VendorDashBoardNewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontCheckBox;
import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

import static com.reatchall.charan.reatchallVendor.Login.UserLoginActivity.PhoneNumberValidator;

public class ManagerLoginActivity extends AppCompatActivity {
    private static final String TAG = "ManagerLoginActivity";

    ImageView backArrow;
    FontEditText phoneNumber ,passwordOrOtp;
    LinearLayout passwordLayout,parentLayout;
    FontTextView passwordOrOtpTv;
    FontButton loginBtn;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    PrefManager prefManager;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_manager_login);
        context = ManagerLoginActivity.this;
        prefManager = new PrefManager(context);
        customProgressDialog = new CustomProgressDialog(context);
        backArrow=(ImageView) findViewById(R.id.back_arrow);
        phoneNumber=(FontEditText) findViewById(R.id.phoneNumber);
        passwordOrOtp=(FontEditText) findViewById(R.id.passwordOrOtp);
        passwordOrOtpTv=(FontTextView) findViewById(R.id.passwordOrOtpTv);
        passwordLayout=(LinearLayout) findViewById(R.id.passwordLayout);
        parentLayout=(LinearLayout) findViewById(R.id.parentLayout);
        loginBtn=(FontButton) findViewById(R.id.loginBtn);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ((ViewGroup) findViewById(R.id.parentLayout)).getLayoutTransition()
                    .enableTransitionType(LayoutTransition.CHANGING);
        }

        loginBtn.setText("Continue");
        passwordLayout.setVisibility(View.GONE);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passwordLayout.getVisibility()==View.GONE){
                    if(PhoneNumberValidator(phoneNumber.getText().toString())){
                        customProgressDialog.showDialog();
                        getManagerBusinesses();
                    }else{
                        Toast.makeText(context,"Invalid mobile number",Toast.LENGTH_LONG).show();
                    }
                }

                if(passwordLayout.getVisibility()==View.VISIBLE){
                    if(loginBtn.getText().toString().equalsIgnoreCase("login")){
                        if(passwordOrOtp.getText().toString().length()>=6){
                            //login
                            customProgressDialog.showDialog();
                            managerLogin();

                        }else{
                            Toast.makeText(context,"Invalid password!",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        if(passwordOrOtp.getText().toString().length()==6){
                            //verifyOtp
                            customProgressDialog.showDialog();
                            verifyOtp();
                        }else{
                            Toast.makeText(context,"Invalid Otp!",Toast.LENGTH_LONG).show();
                        }
                    }
                }

            }
        });
    }

    private void managerLogin(){
     String url = Constants.BASE_URL+"vendor/manager-mobile-login";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile",phoneNumber.getText().toString());
            jsonObject.put("password",passwordOrOtp.getText().toString());
            jsonObject.put("business_id",singleBizId);
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
                        customProgressDialog.showDialog();
                        getBusinessDetails();
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

    private void getBusinessDetails(){
        String url = Constants.BASE_URL+"vendor/get-manager-businesses/"+phoneNumber.getText().toString();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        //navigate to dashboard
                        BusinessDetails businessDetails = new BusinessDetails();
                        businessDetails.setBusinessId(response.getJSONArray("msg").getJSONObject(0).getJSONObject("basic").getString("_id"));
                        businessDetails.setBusinessName(response.getJSONArray("msg").getJSONObject(0).getJSONObject("basic").getString("name"));
                        businessDetails.setVendorId(response.getJSONArray("msg").getJSONObject(0).getJSONObject("basic").getString("vendor_id"));

                        if(response.getJSONArray("msg").getJSONObject(0).getJSONObject("business_type").getString("name").toLowerCase().
                                contains("service")){
                            businessDetails.setService(true);
                        }else{
                            businessDetails.setService(false);
                        }

                        PrefManager.setManagerLoggedIn(context);
                        prefManager.setVendorId(businessDetails.getVendorId(),context);
                        prefManager.setManagerPhnNum(phoneNumber.getText().toString(),context);

                        if(businessDetails.isService()){
                            Intent intent = new Intent(context,VendorDashBoardNewActivity.class);
                            intent.putExtra("businessDetails",businessDetails);
                            startActivity(intent);
                            finish();
                        }else{
                            Intent intent = new Intent(context,VendorDashBoardActivity.class);
                            intent.putExtra("businessDetails",businessDetails);
                            startActivity(intent);
                            finish();
                        }
                    }else{
                        Toast.makeText(context,"Unable to fetch details",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Unable to fetch details",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest);
    }

    private void verifyOtp(){
        String url = Constants.BASE_URL+"vendor/verify-otp";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile",phoneNumber.getText().toString());
            jsonObject.put("otp",passwordOrOtp.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        //navigateToManagerHome
                        PrefManager.setManagerLoggedIn(context);
                        prefManager.setManagerPhnNum(phoneNumber.getText().toString(),context);
                        startActivity(new Intent(context,ManagerHomeActivity.class));
                        finish();
                    }else{
                        Toast.makeText(context,"Unable to verify otp",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Unable to verify otp",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest);
    }

    private void sendOtp(){
        String url = Constants.BASE_URL+"vendor/manager-login-otp";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile",phoneNumber.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Otp has been sent!",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context,"Unable to send otp",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Unable to send otp",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest);
    }

    String singleBizId = "";
    private void getManagerBusinesses(){
        String url = Constants.BASE_URL+"vendor/get-manager-business-count/"+phoneNumber.getText().toString();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        JSONArray msg = response.getJSONArray("msg");
                        if(msg.length()==0){
                            Toast.makeText(context,"No managers found",Toast.LENGTH_LONG).show();
                        }
                        if(msg.length()==1){
                            passwordLayout.setVisibility(View.VISIBLE);
                            passwordOrOtpTv.setText("Password :");
                            loginBtn.setText("LOGIN");
                            singleBizId = msg.getJSONObject(0).getString("business_id");
                            passwordOrOtp.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }
                        if(msg.length()>1){
                            passwordLayout.setVisibility(View.VISIBLE);
                            passwordOrOtpTv.setText("Otp :");
                            loginBtn.setText("VERIFY");
                            passwordOrOtp.setInputType(InputType.TYPE_NUMBER_VARIATION_NORMAL);
                            customProgressDialog.showDialog();
                            sendOtp();
                        }
                    }else{
                        Toast.makeText(context,"No managers found",Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        if(passwordLayout.getVisibility()==View.GONE){
            super.onBackPressed();
        }else{
            passwordLayout.setVisibility(View.GONE);
        }
    }
}
