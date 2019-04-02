package com.reatchall.charan.reatchallVendor.Registration;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
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
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontEditText;

import static com.reatchall.charan.reatchallVendor.Registration.RegisterActivity.NameValidator;
import static com.reatchall.charan.reatchallVendor.Registration.RegisterActivity.PhoneNumberValidator;

public class VendorRegistrationActivity extends AppCompatActivity {

    private static final String TAG = "VendorRegistrationActiv";

    ImageView backArrow;

    FontEditText userFirstName,userLastName,phnNumber,passWrd,cnfrmPassWrd,email,dob;
    Spinner genderSpinner;

    FontButton registerBtn;

    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;

    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    String year=null;
    DatePickerDialog datePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_vendor_registration);

        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        customProgressDialog= new CustomProgressDialog(VendorRegistrationActivity.this);


        userFirstName=(FontEditText)findViewById(R.id.userFirstName);
        userLastName=(FontEditText)findViewById(R.id.userLastName);
        phnNumber=(FontEditText)findViewById(R.id.phoneNumber);
        passWrd=(FontEditText)findViewById(R.id.password);
        cnfrmPassWrd=(FontEditText)findViewById(R.id.cnfrmPassword);
        email=(FontEditText)findViewById(R.id.email);
        dob=(FontEditText)findViewById(R.id.dob);
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

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        datePickerDialog = new DatePickerDialog(VendorRegistrationActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH));

        //datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        year = sdf.format(myCalendar.getTime());
        year=year.substring(0,4);
        Log.e(TAG, "updateLabel: YEAR"+year);
        long yr = Long.parseLong(year);
        if(yr <= 2000){
            dob.setText(sdf.format(myCalendar.getTime()));
        }else{
            Toast.makeText(VendorRegistrationActivity.this,"You should be atleast 18 years above to register",Toast.LENGTH_LONG).show();
            dob.performClick();
        }
    }



    private void setupRegistration(){
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "onClick: "+phnNumber.getText().toString().substring(3,phnNumber.getText().toString().length()));

                if(email.getText().toString().length() == 0 &&
                        phnNumber.getText().toString().length() ==0 &&
                        userFirstName.getText().toString().length() ==0 &&
                        userLastName.getText().toString().length() ==0 &&
                        passWrd.getText().toString().length() ==0 &&
                        dob.getText().toString().length() ==0 &&
                        cnfrmPassWrd.getText().toString().length() ==0){

                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.login_layout), "All inputs are required", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }else{
                    if(email.getText().toString().length() != 0){
                        if(phnNumber.getText().toString().length() !=0){
                            if(EmailValidator(email.getText().toString())){
                                if(PhoneNumberValidator(phnNumber.getText().toString().substring(3,phnNumber.getText().toString().length()))){
                                    if(NameValidator(userFirstName.getText().toString())){
                                            if(PasswordValidator(passWrd.getText().toString())){
                                                if(passWrd.getText().toString().equalsIgnoreCase(cnfrmPassWrd.getText().toString())){
                                                    if(dob.getText().toString().length() !=0){
                                                        customProgressDialog.showDialog();
                                                        registerVendorNew();
                                                    }else{
                                                        Snackbar snackbar = Snackbar
                                                                .make(findViewById(R.id.login_layout), "Please Enter your date of birth", Snackbar.LENGTH_LONG);
                                                        snackbar.show();
                                                    }
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
                                                .make(findViewById(R.id.login_layout), "PLEASE ENTER YOUR FIRST NAME", Snackbar.LENGTH_LONG);
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


    private void registerVendorNew(){
        String url =Constants.BASE_URL+"vendor/register";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", userFirstName.getText().toString());
            jsonBody.put("referral_code", userLastName.getText().toString());
            jsonBody.put("email", email.getText().toString());
            jsonBody.put("mobile", phnNumber.getText().toString().substring(3,phnNumber.getText().toString().length()));
            jsonBody.put("password", passWrd.getText().toString());
            jsonBody.put("dob", dob.getText().toString());
            jsonBody.put("gender", genderSpinner.getSelectedItem().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String requestBody = jsonBody.toString();

        CustomJsonRequest jsonObjectRequest = new CustomJsonRequest(Request.Method.POST,url, jsonBody, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject obj) {

                try {
                    customProgressDialog.hideDialog();

                    boolean success = obj.getBoolean("success");
                    if(success){
                        Toast.makeText(VendorRegistrationActivity.this,"Registered Successfully!!",Toast.LENGTH_LONG).show();
                        navigateOTP();
                    }else{
                        Toast.makeText(VendorRegistrationActivity.this,obj.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(VendorRegistrationActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        jsonObjectRequest.setPriority(Request.Priority.HIGH);

        helper.addToRequestQueue(jsonObjectRequest,"REGISTERVENDORNEW");
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
        Intent intent = new Intent(VendorRegistrationActivity.this, EnterOtpActivity.class);
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
                Toast.makeText(VendorRegistrationActivity.this,"PLEASE SELECT GENDER",Toast.LENGTH_LONG).show();
            }
        });

    }
}
