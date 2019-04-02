package com.reatchall.charan.reatchallVendor.Login;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Registration.VendorRegistrationActivity;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.VendorHomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontCheckBox;
import fr.arnaudguyon.smartfontslib.FontEditText;

import static com.reatchall.charan.reatchallVendor.Login.UserLoginActivity.PasswordValidator;
import static com.reatchall.charan.reatchallVendor.Login.UserLoginActivity.PhoneNumberValidator;



public class VendorLoginActivity extends AppCompatActivity {

    private static final String TAG = "VendorLoginActivity";

    ImageView backArrow;
    FontEditText phnNumber ,passWrd;
    FontButton loginBtn,registerBtn,forgotBtn;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    FontCheckBox ownerCB,managerCB;
    boolean isOwner=false , isManager = false;
    CoordinatorLayout clLogin;

    String newToken ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vendor_login);

        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
            newToken = instanceIdResult.getToken();
            Log.e("newToken", newToken);
        });

        backArrow=(ImageView)findViewById(R.id.back_arrow);
        ownerCB=(FontCheckBox)findViewById(R.id.ownerCB);
        managerCB=(FontCheckBox)findViewById(R.id.managerCB);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        customProgressDialog= new CustomProgressDialog(VendorLoginActivity.this);

        phnNumber = (FontEditText)findViewById(R.id.phoneNumber);
        passWrd=(FontEditText)findViewById(R.id.password);
        loginBtn=(FontButton)findViewById(R.id.loginBtn);
        registerBtn=(FontButton)findViewById(R.id.registerBtn);
        forgotBtn=(FontButton)findViewById(R.id.forgotPassword);
        clLogin = (CoordinatorLayout)findViewById(R.id.clLogin);

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
                            authVendorNew();

                        }else{
                            Snackbar snackbar = Snackbar
                                    .make(clLogin, "PASSWORD LENGTH SHOULD BE ATLEAST 5", Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }

                    }else{
                        Snackbar snackbar = Snackbar
                                .make(clLogin, "PHONE NUMBER NOT VALID", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                }else{
                    Snackbar snackbar = Snackbar
                            .make(clLogin, "All inputs are required", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }



            }
        });


        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(VendorLoginActivity.this, ForgotPasswordNewActivity.class);
                intent.putExtra("VorU",2);
                startActivity(intent);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VendorLoginActivity.this, VendorRegistrationActivity.class));
            }
        });

        ownerCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: "+ownerCB.isChecked() );
                if(isOwner){
                    Log.e(TAG, "onClick: OC" );
                    ownerCB.setChecked(false);
                    managerCB.setChecked(true);
                    isOwner = false;
                    isManager=true;
                }else{
                    Log.e(TAG, "onClick: ONC" );
                    ownerCB.setChecked(true);
                    managerCB.setChecked(false);
                    isOwner=true;
                    isManager=false;
                }
            }
        });

        managerCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isManager){
                    Log.e(TAG, "onClick: MC" );
                    managerCB.setChecked(false);
                    ownerCB.setChecked(true);
                    isOwner = true;
                    isManager=false;
                }else{
                    Log.e(TAG, "onClick: MNC" );
                    managerCB.setChecked(true);
                    ownerCB.setChecked(false);
                    isOwner=false;
                    isManager=true;
                }
            }
        });

        ownerCB.setChecked(false);
        managerCB.setChecked(false);

        ownerCB.performClick();

    }


    private void authVendorNew(){
        String url= Constants.BASE_URL+"vendor/login";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("username", phnNumber.getText().toString().substring(3,phnNumber.getText().toString().length()));
            jsonBody.put("password", passWrd.getText().toString());
            jsonBody.put("token",newToken);
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
                        PrefManager.setVendorLoggedIn(VendorLoginActivity.this);
                        PrefManager prefManager = new PrefManager(VendorLoginActivity.this);
                        prefManager.setVendorId(vendorString.getString("_id"),VendorLoginActivity.this);
                        navigateToVendorHome();

                    }else{
                        String msg = response.getString("msg");
                        Toast.makeText(VendorLoginActivity.this,""+msg,Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(VendorLoginActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"AUTHVENDOR");
    }


    private void navigateToVendorHome(){
        PrefManager.setVendorLoggedIn(VendorLoginActivity.this);
        Intent intent = new Intent(VendorLoginActivity.this, VendorHomeActivity.class);
        startActivity(intent);
        VendorLoginActivity.this.finish();
    }

}
