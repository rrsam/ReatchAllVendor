package com.reatchall.charan.reatchallVendor.Registration;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontEditText;

import static com.reatchall.charan.reatchallVendor.Registration.RegisterActivity.NameValidator;
import static com.reatchall.charan.reatchallVendor.Registration.RegisterActivity.PhoneNumberValidator;

public class UserRegistrationActivity extends AppCompatActivity {

    private static final String TAG = "UserRegistrationActivit";

    ImageView backArrow;

    FontEditText userName,phnNumber,passWrd,cnfrmPassWrd,email;
    Spinner genderSpinner;

    FontButton registerBtn;

    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        customProgressDialog= new CustomProgressDialog(UserRegistrationActivity.this);

        userName=(FontEditText)findViewById(R.id.userName);
        phnNumber=(FontEditText)findViewById(R.id.phoneNumber);
        passWrd=(FontEditText)findViewById(R.id.password);
        cnfrmPassWrd=(FontEditText)findViewById(R.id.cnfrmPassword);
        email=(FontEditText)findViewById(R.id.email);
        genderSpinner=(Spinner)findViewById(R.id.genderSpinner);

        setupGenderSpinner();

        registerBtn=(FontButton)findViewById(R.id.registerBtn);

        setupRegistration();

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

    }

    private void setupRegistration(){
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "onClick: "+phnNumber.getText().toString().substring(3,phnNumber.getText().toString().length()));

                if(email.getText().toString().length() == 0 &&
                        phnNumber.getText().toString().length() ==0 &&
                        userName.getText().toString().length() ==0 &&
                        passWrd.getText().toString().length() ==0 &&
                        cnfrmPassWrd.getText().toString().length() ==0){

                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.login_layout), "All inputs are required", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }else{
                    if(email.getText().toString().length() != 0){
                        if(phnNumber.getText().toString().length() !=0){
                            if(EmailValidator(email.getText().toString())){
                                if(PhoneNumberValidator(phnNumber.getText().toString().substring(3,phnNumber.getText().toString().length()))){
                                    if(NameValidator(userName.getText().toString())){
                                        if(PasswordValidator(passWrd.getText().toString())){
                                                if(passWrd.getText().toString().equalsIgnoreCase(cnfrmPassWrd.getText().toString())){

                                                    customProgressDialog.showDialog();
                                                    registerUser();


                                                }else{
                                                    Snackbar snackbar = Snackbar
                                                            .make(findViewById(R.id.login_layout), "Passwords don't match", Snackbar.LENGTH_LONG);
                                                    snackbar.show();
                                                }


                                        }else{
                                            Snackbar snackbar = Snackbar
                                                    .make(findViewById(R.id.login_layout), "PASSWORD LENGTH SHOULD BE ATLEAST 5", Snackbar.LENGTH_LONG);
                                            snackbar.show();

                                        }
                                    }else{
                                        Snackbar snackbar = Snackbar
                                                .make(findViewById(R.id.login_layout), "PLEASE ENTER YOUR NAME", Snackbar.LENGTH_LONG);
                                        snackbar.show();

                                    }
                                }else{
                                    Snackbar snackbar = Snackbar
                                            .make(findViewById(R.id.login_layout), "PHONE NUMBER NOT VALID", Snackbar.LENGTH_LONG);
                                    snackbar.show();

                                }
                            }else{
                                Snackbar snackbar = Snackbar
                                        .make(findViewById(R.id.login_layout), "EMAIL ADDRESS NOT VALID", Snackbar.LENGTH_LONG);
                                snackbar.show();

                            }
                        }else{
                            Snackbar snackbar = Snackbar
                                    .make(findViewById(R.id.login_layout), "PHONE NUMBER EMPTY", Snackbar.LENGTH_LONG);
                            snackbar.show();

                        }
                    }else{
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.login_layout), "EMAIL ADDRESS EMPTY", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                }
            }
        });
    }


    private void registerUser(){
        String url ="http://13.127.169.96:3000/user/register";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", userName.getText().toString());
            jsonBody.put("email", email.getText().toString());
            jsonBody.put("mobile", phnNumber.getText().toString().substring(3,phnNumber.getText().toString().length()));
            jsonBody.put("password", passWrd.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, jsonBody, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {
                try {
                    customProgressDialog.hideDialog();

                    boolean success = obj.getBoolean("success");
                    if(success){
                        Toast.makeText(UserRegistrationActivity.this,"Success",Toast.LENGTH_LONG).show();
                        navigateOTP();
                    }else{
                        Toast.makeText(UserRegistrationActivity.this,obj.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(UserRegistrationActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        helper.addToRequestQueue(jsonObjectRequest,"REGISTERUSERNEW");
    }


    @TargetApi(11)
    public  static boolean EmailValidator(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public  static boolean PasswordValidator(String Password) {
        if (Password.length() < 5)
            return false;
        else
            return true;
    }


    private void navigateOTP(){
        Intent intent = new Intent(UserRegistrationActivity.this, EnterOtpActivity.class);
        intent.putExtra("PHN",phnNumber.getText().toString());
        startActivity(intent);
    }

    private void setupGenderSpinner(){

        final List<String> plansList = Arrays.asList(getResources().getStringArray(R.array.genderArray));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, plansList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont= Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(14);


                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(14);

                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(dataAdapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(UserRegistrationActivity.this,"PLEASE SELECT GENDER",Toast.LENGTH_LONG).show();
            }
        });

    }

}
