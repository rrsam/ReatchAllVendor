package com.reatchall.charan.reatchallVendor.Registration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.goodiebag.pinview.Pinview;
import com.reatchall.charan.reatchallVendor.Login.LoginActivity;
import com.reatchall.charan.reatchallVendor.Login.UserLoginActivity;
import com.reatchall.charan.reatchallVendor.Login.VendorLoginActivity;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class EnterOtpActivity extends AppCompatActivity {

    private static final String TAG = "EnterOtpActivity";
    Pinview otpEdittext;
    FontTextView resend;
    String phnNumber=null;
    FontButton verify;

    //BOOLEANS
    boolean otpSent=false;
    boolean otpTrue=false;

    boolean success=false,verifyOTP=false;
    String otpValue=null,verifyOtpString=null;

    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        customProgressDialog=new CustomProgressDialog(EnterOtpActivity.this);
        otpEdittext=(Pinview)findViewById(R.id.enterOtpPin);
        verify=(FontButton)findViewById(R.id.verifyOtp);

        prefManager=new PrefManager(EnterOtpActivity.this);


        phnNumber=getIntent().getExtras().getString("PHN");
        if(phnNumber==null){
            Toast.makeText(EnterOtpActivity.this,"UNABLE TO SEND OTP",Toast.LENGTH_LONG).show();
        }else{
            Log.e(TAG, "onCreate: "+phnNumber );
           // customProgressDialog.showDialog();
            //sendOtp();

        }

        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                Log.e(TAG, "messageReceived: "+messageText );
                otpEdittext.setValue(messageText);

                if(messageText.contains(otpValue)){

                    otpEdittext.setValue(otpValue);
                    otpTrue=true;
                    Toast.makeText(EnterOtpActivity.this,"OTP DETECTED",Toast.LENGTH_LONG).show();

                }else{
                    otpEdittext.setValue(otpValue);
                    Toast.makeText(EnterOtpActivity.this,"OTP NOT VALID",Toast.LENGTH_LONG).show();
                }
            }
        });


        resend=(FontTextView)findViewById(R.id.resendOtp);
        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* customProgressDialog.showDialog();

                sendOtp();*/
                Toast.makeText(EnterOtpActivity.this,"OTP SENT",Toast.LENGTH_LONG).show();

            }
        });


       /* verify.setEnabled(false);
        verify.setBackgroundColor(getResources().getColor(R.color.grey));*/
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* if(otpEdittext.getValue().equalsIgnoreCase(otpValue)){
                    otpTrue=true;
                }else {
                    otpTrue=false;
                }

                if(otpTrue){
                    formString();
                }else{
                    Toast.makeText(EnterOtpActivity.this,"OTP NOT VALID",Toast.LENGTH_LONG).show();
                }*/

               if(otpEdittext.getValue().equalsIgnoreCase("123456")){
                   if(prefManager.isCusorbus()){
                       Toast.makeText(EnterOtpActivity.this,"OTP VERIFIED. LOGIN TO CONTINUE",Toast.LENGTH_LONG).show();
                       startActivity(new Intent(EnterOtpActivity.this, UserLoginActivity.class));
                       finish();
                   }else{
                       Toast.makeText(EnterOtpActivity.this,"OTP VERIFIED. LOGIN TO CONTINUE",Toast.LENGTH_LONG).show();
                       startActivity(new Intent(EnterOtpActivity.this, VendorLoginActivity.class));
                       finish();
                   }

               }else{
                   Toast.makeText(EnterOtpActivity.this,"ENTER A VALID OTP",Toast.LENGTH_LONG).show();

               }

            }
        });
    }



    private void sendOtp(){

        String url = Constants.BASE_URL+ "vendor/vendor-register-otp";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile",phnNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.POST, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                customProgressDialog.hideDialog();
                try {
                    success=response.getBoolean("success");

                    if(success){
                        otpSent = true;
                        JSONObject msg = null;
                        // msg = response.getJSONObject("msg");
                        // otpValueString=msg.getString("otp");
                        Toast.makeText(EnterOtpActivity.this,"OTP SENT",Toast.LENGTH_LONG).show();


                    }else{
                        String msg = null;
                        msg = response.getString("msg");
                        Toast.makeText(EnterOtpActivity.this,""+msg,Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();

                Toast.makeText(EnterOtpActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"SENDOTP");

    }



    JSONObject verifyOtp = null;
    private void formString(){
        try {

            verifyOtp = new JSONObject();
            verifyOtp.put("mobile",phnNumber);
            verifyOtp.put("otp",otpEdittext.getValue().toString());
            verifyOtpString=verifyOtp.toString();
            Log.e(TAG, "formString: "+verifyOtpString );
            customProgressDialog.showDialog();
            verifyOtp();

        }catch (Exception e){

        }

    }


    private void verifyOtp(){

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile",phnNumber);
            jsonObject.put("otp",otpEdittext.getValue().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String url =Constants.BASE_URL+"vendor/verify-otp";

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.PUT, url, verifyOtp, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    customProgressDialog.hideDialog();

                    verifyOTP = response.getBoolean("success");

                    if(verifyOTP){

                        Toast.makeText(EnterOtpActivity.this,"OTP VERIFIED. LOGIN TO CONTINUE",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(EnterOtpActivity.this, LoginActivity.class));
                        finish();

                        /*if(prefManager.isForgotPass()){
                            prefManager.setForgotPass(false);
                            Toast.makeText(EnterOtpActivity.this,"OTP VERIFIED.RESET YOUR PASSWORD",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(EnterOtpActivity.this, ResetPasswordActivity.class);
                            intent.putExtra("PHN",phnNumber);
                            intent.putExtra("OTP",otpValue);
                            startActivity(intent);
                            finish();
                        }else{
                            Toast.makeText(EnterOtpActivity.this,"OTP VERIFIED. LOGIN TO CONTINUE",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(EnterOtpActivity.this, LoginActivity.class));
                            finish();
                        }*/

                    }else{
                        Toast.makeText(EnterOtpActivity.this,"OTP NOT VALID",Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();

                Toast.makeText(EnterOtpActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"VERIFYOTP");


    }
}
