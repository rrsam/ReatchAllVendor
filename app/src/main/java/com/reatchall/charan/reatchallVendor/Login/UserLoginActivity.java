package com.reatchall.charan.reatchallVendor.Login;

import android.annotation.TargetApi;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.HomeActivity;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Registration.UserRegistrationActivity;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontEditText;

public class UserLoginActivity extends AppCompatActivity {

    private static final String TAG = "UserLoginActivity";

    ImageView backArrow;
    FontEditText phnNumber ,passWrd;
    FontButton loginBtn,registerBtn,forgotBtn;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        customProgressDialog= new CustomProgressDialog(UserLoginActivity.this);

        phnNumber = (FontEditText)findViewById(R.id.phoneNumber);
        passWrd=(FontEditText)findViewById(R.id.password);
        loginBtn=(FontButton)findViewById(R.id.loginBtn);
        registerBtn=(FontButton)findViewById(R.id.registerBtn);
        forgotBtn=(FontButton)findViewById(R.id.forgotPassword);

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

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(phnNumber.getText().toString().length() != 0 && passWrd.getText().toString().length() !=0){
                    if(PhoneNumberValidator(phnNumber.getText().toString().substring(3,phnNumber.getText().toString().length()))){
                        if(PasswordValidator(passWrd.getText().toString())){


                                customProgressDialog.showDialog();
                                authUserNew();

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


        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserLoginActivity.this, ForgotPasswordNewActivity.class);
                intent.putExtra("VorU",1);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UserLoginActivity.this, UserRegistrationActivity.class));
            }
        });
    }

    private void authUserNew(){
        String url="http://13.127.169.96:3000/user/authenticate";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("mobile", phnNumber.getText().toString().substring(3,phnNumber.getText().toString().length()));
            jsonBody.put("password", passWrd.getText().toString());

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
                        PrefManager prefManager = new PrefManager(UserLoginActivity.this);
                        prefManager.setManagerId(vendorString.getString("id"),UserLoginActivity.this);
                        navigateToUserHome();
                    }else{
                        String msg = response.getString("msg");
                        Toast.makeText(UserLoginActivity.this,""+msg,Toast.LENGTH_LONG).show();


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();

                Toast.makeText(UserLoginActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"AUTHUSER");

    }

    private void navigateToUserHome(){
        PrefManager.setManagerLoggedIn(UserLoginActivity.this);
        Intent intent = new Intent(UserLoginActivity.this, HomeActivity.class);
        startActivity(intent);
        UserLoginActivity.this.finish();
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
