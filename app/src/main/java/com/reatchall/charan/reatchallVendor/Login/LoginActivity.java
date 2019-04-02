package com.reatchall.charan.reatchallVendor.Login;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.HomeActivity;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Registration.HelpUserActivity;
import com.reatchall.charan.reatchallVendor.Registration.RegisterActivity;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.VendorHomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 06/02/18.
 */


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    private RelativeLayout logo_layout;
    private LinearLayout login_layout;

    private FontTextView newUser,forgot;

    private EditText username,password;
    private Button signin;
    FontTextView customer,business;
    boolean checkCusORBus;
    ReatchAll helper = ReatchAll.getInstance();
    FontTextView titleToolbar,help;
    CustomProgressDialog customProgressDialog;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        help=(FontTextView)findViewById(R.id.help);
        customProgressDialog= new CustomProgressDialog(LoginActivity.this);
        login_layout=(LinearLayout)findViewById(R.id.login_layout);
        logo_layout=(RelativeLayout)findViewById(R.id.login_logo);

        username=(EditText) findViewById(R.id.phoneNumber);
        password=(EditText)findViewById(R.id.password);
        signin=(Button)findViewById(R.id.signin);

        username.setImeOptions(EditorInfo.IME_ACTION_DONE);
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().contains("+91")) {
                    username.setText("+91" + s.toString());
                    username.setSelection(username.getText().toString().length());
                }
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    startActivity(new Intent(LoginActivity.this, HelpUserActivity.class));
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "onClick: "+username.getText().toString().substring(3,username.getText().toString().length()));

                if(username.getText().toString().length() != 0 && password.getText().toString().length() !=0){
                    if(PhoneNumberValidator(username.getText().toString().substring(3,username.getText().toString().length()))){
                        if(PasswordValidator(password.getText().toString())){

                            if(checkCusORBus){
                                customProgressDialog.showDialog();
                                authUserNew();
                            }else{
                                customProgressDialog.showDialog();

                                authVendorNew();
                            }

                        }else{
                            Snackbar snackbar = Snackbar
                                    .make(findViewById(R.id.login_layout), "PASSWORD LENGTH SHOULD BE ATLEAST 5", Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }

                    }else{
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.login_layout), "PHONE NUMBER NOT VALID", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                }else{
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.login_layout), "All inputs are required", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }


            }
        });
        customer=(FontTextView)findViewById(R.id.customer);
        business=(FontTextView)findViewById(R.id.business);
        login_layout.requestFocus();
        newUser=(FontTextView)findViewById(R.id.newUser);
        forgot=(FontTextView)findViewById(R.id.forgot);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkCusORBus){
                    PrefManager prefManager= new PrefManager(LoginActivity.this);
                    prefManager.setCusorbus(true);
                }else{
                    PrefManager prefManager= new PrefManager(LoginActivity.this);
                    prefManager.setCusorbus(false);
                }
                startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));

            }
        });

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customer.setBackgroundResource(R.color.primaryColor);
                business.setBackgroundResource(R.color.grey);
                titleToolbar.setText("Customer login");
                clearFields();
                checkCusORBus=true;

            }
        });
        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customer.setBackgroundResource(R.color.grey);

                business.setBackgroundResource(R.color.primaryColor);
                titleToolbar.setText("Business login");

                clearFields();
                checkCusORBus=false;

            }
        });
        customer.performClick();
    }

    private void clearFields(){
        password.getText().clear();
        username.getText().clear();

    }

    private void authUserNew(){
        String url="http://13.127.169.96:3000/user/authenticate";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("mobile", username.getText().toString().substring(3,username.getText().toString().length()));
            jsonBody.put("password", password.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.POST, url, jsonBody, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                customProgressDialog.hideDialog();

                try {
                    boolean success = response.getBoolean("success");

                    if (success){
                        JSONObject vendorString = response.getJSONObject("user");
                        PrefManager prefManager = new PrefManager(LoginActivity.this);
                        prefManager.setVendorId(vendorString.getString("id"),LoginActivity.this);
                        navigateToUserHome();
                    }else{
                        String msg = response.getString("msg");
                        Toast.makeText(LoginActivity.this,""+msg,Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();

                Toast.makeText(LoginActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"AUTHUSER");

    }

    private void authVendorNew(){
        String url="http://13.127.169.96:3000/vendor/authenticate";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("mobile", username.getText().toString().substring(3,username.getText().toString().length()));
            jsonBody.put("password", password.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.POST, url, jsonBody, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                customProgressDialog.hideDialog();

                try {
                    boolean success = response.getBoolean("success");

                    if (success){
                        JSONObject vendorString = response.getJSONObject("vendor");
                        PrefManager prefManager = new PrefManager(LoginActivity.this);
                        prefManager.setVendorId(vendorString.getString("id"),LoginActivity.this);
                        navigateToVendorHome();

                    }else{
                        String msg = response.getString("msg");
                        Toast.makeText(LoginActivity.this,""+msg,Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(LoginActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"AUTHVENDOR");

    }


    private void navigateToUserHome(){
        PrefManager.setManagerLoggedIn(LoginActivity.this);
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    private void navigateToVendorHome(){
        PrefManager.setVendorLoggedIn(LoginActivity.this);
        Intent intent = new Intent(LoginActivity.this, VendorHomeActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }



    @TargetApi(11)
    public  static boolean EmailValidator(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
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
            if(_Name.length()>=5)
                return true;
        }
        return false;
    }

    public  static boolean PasswordValidator(String Password) {
        if (Password.length() < 5)
            return false;
        else
            return true;
    }


}
