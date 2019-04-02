package com.reatchall.charan.reatchallVendor.Login;

import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class ForgotPasswordNewActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordNewActivi";

    int vORu;
    boolean vendor=false,user=false;
    ImageView backArrow;
    FontEditText phnNumber,otpValue,passWrd,cnfrmPassWrd;

    FontTextView getOtp;
    FontButton resetBtn;

    ReatchAll helper = ReatchAll.getInstance();

    CustomProgressDialog customProgressDialog;


    //BOOLEANS
    boolean otpSent=false;
    boolean otpTrue=false;

    boolean success=false,verifyOTP=false;
    String otpValueString=null,verifyOtpString=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password_new);
        backArrow= findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        customProgressDialog = new CustomProgressDialog(ForgotPasswordNewActivity.this);

        vORu=getIntent().getExtras().getInt("VorU");
        if(vORu==1){

            user=true;
        }else{

           vendor=true;
        }


        phnNumber= findViewById(R.id.phoneNumber);
        otpValue= findViewById(R.id.otp);
        passWrd= findViewById(R.id.password);
        cnfrmPassWrd= findViewById(R.id.cnfrmPassword);

        getOtp= findViewById(R.id.getOTP);
        resetBtn= findViewById(R.id.resetPassword);

        phnNumber.setImeOptions(EditorInfo.IME_ACTION_DONE);
        phnNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().contains("+91")) {
                    phnNumber.setText("+91" + s.toString());
                    phnNumber.setSelection(phnNumber.getText().toString().length());
                }
            }
        });


        getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(phnNumber.getText().toString().length()!=0){

                    if(PhoneNumberValidator(phnNumber.getText().toString().substring(3))){


                        customProgressDialog.showDialog();

                        sendOtp();

                    }else{
                        Toast.makeText(ForgotPasswordNewActivity.this,"Phone Number not valid",Toast.LENGTH_LONG).show();
                    }

                }else{
                    Toast.makeText(ForgotPasswordNewActivity.this,"Phone Number is empty",Toast.LENGTH_LONG).show();
                }

            }
        });


        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(passWrd.getText().toString().length() !=0 && cnfrmPassWrd.getText().toString().length()!=0){
                    if(passWrd.getText().toString().equals(cnfrmPassWrd.getText().toString())){

                        customProgressDialog.showDialog();
                        verifyOtp();

                    }else{
                        Toast.makeText(ForgotPasswordNewActivity.this,"Password's doesn't match",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(ForgotPasswordNewActivity.this,"Password cannot be empty",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void verifyOtp(){
        String url = Constants.BASE_URL+"vendor/verify-otp";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile",phnNumber.getText().toString().substring(3));
            jsonObject.put("otp",otpValue.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "verifyOtp: login"+jsonObject.toString() );
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onResponse: "+response.toString());
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(ForgotPasswordNewActivity.this,"OTP verified!",Toast.LENGTH_LONG).show();
                        formString();
                    }else{
                        Toast.makeText(ForgotPasswordNewActivity.this,"Please Try again!",Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onErrorResponse: "+error.getMessage() );
                Toast.makeText(ForgotPasswordNewActivity.this,"Please Try again!",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"VERIFY_OTP");
    }


    private void sendOtp(){

        String url = Constants.BASE_URL+ "vendor/generate-otp";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile",phnNumber.getText().toString().substring(3));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new com.android.volley.Response.Listener<JSONObject>() {
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
                        Toast.makeText(ForgotPasswordNewActivity.this,"OTP SENT",Toast.LENGTH_LONG).show();
                        getOtp.setText("RESEND OTP");


                    }else{
                        String msg = null;
                        msg = response.getString("msg");
                        getOtp.setText("GET OTP");
                        Toast.makeText(ForgotPasswordNewActivity.this,""+msg,Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();

                Toast.makeText(ForgotPasswordNewActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });

        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"SENDOTP");

    }



    JSONObject resetPass = null;
    private void formString(){
        try {

            resetPass = new JSONObject();

            resetPass.put("mobile",phnNumber.getText().toString().substring(3));
            resetPass.put("password",passWrd.getText().toString());
            customProgressDialog.showDialog();
            resetPassword();

        }catch (Exception e){

        }

    }



    private void resetPassword(){
        String url=null;
        if(user){
            url =Constants.BASE_URL+ "user/reset-pwd";

        }else{
            url =Constants.BASE_URL+"vendor/reset-password";

        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, resetPass, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                customProgressDialog.hideDialog();

                try{
                    boolean success = response.getBoolean("success");
                    if(success){

                        Toast.makeText(ForgotPasswordNewActivity.this,"PASSWORD UPDATED",Toast.LENGTH_LONG).show();
                        if(vendor){
                            startActivity(new Intent(ForgotPasswordNewActivity.this,VendorLoginActivity.class));
                            finish();
                        }else{
                            startActivity(new Intent(ForgotPasswordNewActivity.this,UserLoginActivity.class));
                            finish();
                        }

                    }else{
                        String msg = response.getString("msg");
                        Toast.makeText(ForgotPasswordNewActivity.this,msg,Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(ForgotPasswordNewActivity.this,""+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"RESETPASSWORD");
    }


    @TargetApi(11)
    public  static boolean PhoneNumberValidator(CharSequence target) {
        if (target == null || target.length() != 10) {
            if(target ==null){
                Log.e(TAG, "PhoneNumberValidator: TARGET NULL");
                return false;

            }else{

                Log.e(TAG, "PhoneNumberValidator: LENGHT NOT 10"+target.length() );
                return false;
            }

        } else {
            return Patterns.PHONE.matcher(target).matches();
        }
    }

    public  static boolean NameValidator(String _Name) {
        Pattern pattern;
        Matcher matcher;
        final String Name = "^[a-zA-Z\\\\s]+";
        pattern = Pattern.compile(Name);
        matcher = pattern.matcher(_Name);
        if(matcher.matches()){
            return _Name.length() >= 5;
        }
        return false;
    }

    public  static boolean PasswordValidator(String Password) {
        return Password.length() >= 5;
    }
}
