package com.reatchall.charan.reatchallVendor.Registration;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.arnaudguyon.smartfontslib.FontTextView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by NaNi on 06/02/18.
 */

public class RegisterActivity extends AppCompatActivity  implements GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "RegisterActivity";
    Dialog dialog;

    private RelativeLayout logo_layout;
    private LinearLayout login_layout,cusBus;
    FontTextView customer,business,login;

    EditText userName,emailId,phnNumber,passWord,cnfrmPassword,dateBirth;
    RelativeLayout maleRl,femaleRl;
    CheckBox maleCB,femaleCB;

    private Button submit;
    LoginButton loginButton;


    private boolean checkCusORBus=true;


     Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    //GOOGLE
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 007;

    //FACEBOOK
    CallbackManager callbackManager;
    String fID;

    String year=null;


    AppCompatButton googleSignUp, facebookSignUp;
    ScrollView scrollView;

    ReatchAll helper = ReatchAll.getInstance();

    FontTextView titleToolbar,help;

    CustomProgressDialog customProgressDialog;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        if(Build.VERSION.SDK_INT < 23){
            //your code here
        }else {
            requestContactPermission();
        }
        customProgressDialog= new CustomProgressDialog(RegisterActivity.this);
        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        help=(FontTextView)findViewById(R.id.help);
        login_layout=(LinearLayout)findViewById(R.id.login_layout);
        cusBus=(LinearLayout)findViewById(R.id.customer_business);
        logo_layout=(RelativeLayout)findViewById(R.id.register_logo);
        customer=(FontTextView)findViewById(R.id.customer);
        business=(FontTextView)findViewById(R.id.business);
        login=(FontTextView)findViewById(R.id.login);
        submit=(Button)findViewById(R.id.submit);

        userName=(EditText)findViewById(R.id.username);
        emailId=(EditText)findViewById(R.id.email);
        phnNumber=(EditText)findViewById(R.id.phoneNumber);
        passWord=(EditText)findViewById(R.id.password);
        cnfrmPassword=(EditText)findViewById(R.id.cnfrmPassword);
        dateBirth=(EditText)findViewById(R.id.date_of_birth);

        googleSignUp=(AppCompatButton)findViewById(R.id.google_signin);
        facebookSignUp=(AppCompatButton)findViewById(R.id.fb_signin);
        loginButton=(LoginButton)findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        setupGender();
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, HelpUserActivity.class));
            }
        });

        List< String > permissionNeeds = Arrays.asList("user_photos", "email",
                "user_birthday", "public_profile");
        loginButton.setReadPermissions(permissionNeeds);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                    getUserDetails(loginResult);
            }

            @Override
            public void onCancel() {
                    Toast.makeText(RegisterActivity.this,"Cancelled",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(RegisterActivity.this,"Error",Toast.LENGTH_LONG).show();

            }
        });


        googleSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGOOGLE();
            }
        });

        facebookSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginButton.performClick();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customer.setBackgroundResource(R.color.primaryColor);
                business.setBackgroundResource(R.color.grey);

                checkCusORBus=true;
                clearFields();
                titleToolbar.setText("Customer Signup");

            }
        });
        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customer.setBackgroundResource(R.color.grey);

                business.setBackgroundResource(R.color.primaryColor);

                checkCusORBus=false;
                clearFields();
                titleToolbar.setText("Business Signup");


            }
        });

        customer.performClick();
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

        dateBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegisterActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        scrollView=(ScrollView)findViewById(R.id.scrollView);


        setupRegistration();

        //GOOGLE SIGNIN
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addApi(Plus.API)
                .build();


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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "onClick: "+phnNumber.getText().toString().substring(3,phnNumber.getText().toString().length()));

                if(emailId.getText().toString().length() == 0 &&
                        phnNumber.getText().toString().length() ==0 &&
                        userName.getText().toString().length() ==0 &&
                        dateBirth.getText().toString().length() ==0 &&
                        passWord.getText().toString().length() ==0 &&
                        cnfrmPassword.getText().toString().length() ==0 && !maleCB.isChecked() && !femaleCB.isChecked()){

                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.login_layout), "All inputs are required", Snackbar.LENGTH_LONG);
                        snackbar.show();

                }else{
                    if(emailId.getText().toString().length() != 0){
                        if(phnNumber.getText().toString().length() !=0){
                            if(EmailValidator(emailId.getText().toString())){
                                if(PhoneNumberValidator(phnNumber.getText().toString().substring(3,phnNumber.getText().toString().length()))){
                                    if(NameValidator(userName.getText().toString())){
                                        if(PasswordValidator(passWord.getText().toString())){
                                            if(dateBirth.getText().toString().length()!=0){
                                                if(passWord.getText().toString().equalsIgnoreCase(cnfrmPassword.getText().toString())){
                                                    if(maleCB.isChecked() || femaleCB.isChecked()){
                                                        if(checkCusORBus){
                                                            customProgressDialog.showDialog();
                                                            registerUserNew();
                                                        }else{
                                                            customProgressDialog.showDialog();
                                                            registerVendorNew();
                                                        }
                                                    }else{
                                                        Snackbar snackbar = Snackbar
                                                                .make(findViewById(R.id.login_layout), "Please select your gender", Snackbar.LENGTH_LONG);
                                                        snackbar.show();
                                                    }

                                                }else{
                                                    Snackbar snackbar = Snackbar
                                                            .make(findViewById(R.id.login_layout), "Passwords don't match", Snackbar.LENGTH_LONG);
                                                    snackbar.show();
                                                }

                                            }else{
                                                Snackbar snackbar = Snackbar
                                                        .make(findViewById(R.id.login_layout), "Please fill the date of birth", Snackbar.LENGTH_LONG);
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

    private void clearFields(){
        emailId.getText().clear();
        phnNumber.getText().clear();
        userName.getText().clear();
        passWord.getText().clear();
        cnfrmPassword.getText().clear();
        dateBirth.getText().clear();
        if(maleCB.isChecked()){
            maleCB.setChecked(false);
        }
        if(femaleCB.isChecked()){
            femaleCB.setChecked(false);
        }
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        year = sdf.format(myCalendar.getTime());
        year=year.substring(6,year.length());
        Log.e(TAG, "updateLabel: YEAR"+year );
        long yr = Long.parseLong(year);
        if(yr <= 2000){
            dateBirth.setText(sdf.format(myCalendar.getTime()));
        }else{
            Toast.makeText(RegisterActivity.this,"You should be atleast 18 years above",Toast.LENGTH_LONG).show();
            dateBirth.performClick();
        }
    }

    private void requestContactPermission() {

        int hasContactPermission =ActivityCompat.checkSelfPermission(RegisterActivity.this,Manifest.permission.RECEIVE_SMS);

        if(hasContactPermission != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(RegisterActivity.this, new String[]   {Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_ASK_PERMISSIONS);
        }else {
            //Toast.makeText(AddContactsActivity.this, "Contact Permission is already granted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                // Check if the only required permission has been granted
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i("Permission", "Contact permission has now been granted. Showing result.");
                   // Toast.makeText(this,"Contact Permission is Granted",Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("Permission", "Contact permission was NOT granted.");
                    requestContactPermission();
                }
                break;
        }
    }

    private void setupGender(){
        maleCB=(CheckBox)findViewById(R.id.maleCB);
        femaleCB=(CheckBox)findViewById(R.id.femaleCB);
        maleRl=(RelativeLayout)findViewById(R.id.maleRl);
        femaleRl=(RelativeLayout)findViewById(R.id.femaleRl);


        femaleCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (maleCB.isChecked()) {
                    maleCB.setChecked(false);
                }

            }
        });


        maleCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (femaleCB.isChecked()) {
                    femaleCB.setChecked(false);
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {



    }

    private void registerUserNew(){
        String url ="http://13.127.169.96:3000/user/register";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", userName.getText().toString());
            jsonBody.put("email", emailId.getText().toString());
            jsonBody.put("mobile", phnNumber.getText().toString().substring(3,phnNumber.getText().toString().length()));
            jsonBody.put("password", passWord.getText().toString());

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
                        Toast.makeText(RegisterActivity.this,"Success",Toast.LENGTH_LONG).show();
                        navigateOTP();
                    }else{
                        Toast.makeText(RegisterActivity.this,obj.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(RegisterActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        helper.addToRequestQueue(jsonObjectRequest,"REGISTERUSERNEW");
    }

    private void registerVendorNew(){
        String url ="http://13.127.169.96:3000/vendor/register";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("name", userName.getText().toString());
            jsonBody.put("email", emailId.getText().toString());
            jsonBody.put("mobile", phnNumber.getText().toString().substring(3,phnNumber.getText().toString().length()));
            jsonBody.put("password", passWord.getText().toString());

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
                        Toast.makeText(RegisterActivity.this,"Success",Toast.LENGTH_LONG).show();
                        navigateOTP();
                    }else{
                        Toast.makeText(RegisterActivity.this,obj.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(RegisterActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

        helper.addToRequestQueue(jsonObjectRequest,"REGISTERVENDORNEW");
    }


    private void navigateOTP(){
        Intent intent = new Intent(RegisterActivity.this, EnterOtpActivity.class);
        intent.putExtra("PHN",phnNumber.getText().toString());
        startActivity(intent);
    }

    private class RegisterUser extends AsyncTask<String, String, String> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setMessage("please wait.");
            dialog.show();
        }

        Response response;
        @Override
        protected String doInBackground(String... urlkk) {
            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\"name\":\""+userName.getText().toString()+"\"," +
                        "\"email\":\""+emailId.getText().toString()+"\"," +
                        "\"mobile\":"+phnNumber.getText()+"," +
                        "\"password\":\""+passWord.getText().toString()+"\"}");
                Request request = new Request.Builder()
                        .url("http://13.127.169.96:3000/user/register")
                        .post(body)
                        .addHeader("content-type", "application/json")
                        .build();
                 response = client.newCall(request).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(response != null){
                if(response.isSuccessful()){

                        Intent intent = new Intent(RegisterActivity.this, EnterOtpActivity.class);
                        intent.putExtra("PHN",phnNumber.getText().toString());
                        startActivity(intent);

                }else{

                    Log.e(TAG, "registerUser: "+response.body().toString() );
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.login_layout), "ACCOUNT ALREADY EXISTS", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }else{
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.login_layout), "ACCOUNT ALREADY EXISTS", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }

    }
    private class RegisterVendor extends AsyncTask<String, String, String> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(RegisterActivity.this);
            dialog.setMessage("please wait.");
            dialog.show();
        }


        Response response;

        @Override
        protected String doInBackground(String... urlkk) {
            try {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/json");
                RequestBody body = RequestBody.create(mediaType, "{\"name\":\""+userName.getText().toString()+"\"," +
                        "\"email\":\""+emailId.getText().toString()+"\"," +
                        "\"mobile\":"+phnNumber.getText()+"," +
                        "\"password\":\""+passWord.getText().toString()+"\"}");
                Request request = new Request.Builder()
                        .url("http://13.127.169.96:3000/vendor/register")
                        .post(body)
                        .addHeader("content-type", "application/json")
                        .build();
                response = client.newCall(request).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            if(response != null){
                if(response.isSuccessful()){
                    Intent intent = new Intent(RegisterActivity.this, EnterOtpActivity.class);
                    intent.putExtra("PHN",phnNumber.getText().toString());
                    startActivity(intent);
                }else{

                    Log.e(TAG, "registerVENDOR: "+response.body().toString() );
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.login_layout), "ACCOUNT ALREADY EXISTS", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }else{
                Snackbar snackbar = Snackbar
                        .make(findViewById(R.id.login_layout), "ACCOUNT ALREADY EXISTS", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

        }

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
            return false;
        } else {
            return Patterns.PHONE.matcher(target).matches();
        }
    }

    public  static boolean NameValidator(String _Name) {

            if(_Name.length()>=0)
            return true;
        return false;
    }

    public  static boolean PasswordValidator(String Password) {
        if (Password.length() < 5)
            return false;
        else
            return true;
    }



    //GOOGLE SIGNIN
    private void signInGOOGLE() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();


            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            userName.setText(personName);
            emailId.setText(email);
            Person person  = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            if(person != null){
                Log.i(TAG, "Display Name: " + person.getDisplayName());
                Log.i(TAG, "Gender: " + person.getGender());
                Log.i(TAG, "AboutMe: " + person.getAboutMe());
                Log.i(TAG, "Birthday: " + person.getBirthday());
                Log.i(TAG, "Current Location: " + person.getCurrentLocation());
                Log.i(TAG, "Language: " + person.getLanguage());
            }else{
                Log.e(TAG, "handleSignInResult: NULL");
            }


            scrollView.smoothScrollTo(0,0);

        } else {
            // Signed out, show unauthenticated UI.

            Toast.makeText(RegisterActivity.this,"Couldn't register with GOOGLE",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        /*OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    handleSignInResult(googleSignInResult);
                }
            });
        }*/
    }

    protected void getUserDetails(LoginResult loginResult) {

        GraphRequest data_request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(
                            JSONObject object,
                            GraphResponse response) {

                        Log.i("LoginActivity",
                                response.toString());
                        try {
                            fID = object.getString("id");
                            try {
                                URL profile_pic = new URL(
                                        "http://graph.facebook.com/" + fID + "/picture?type=large");
                                Log.i("profile_pic",
                                        profile_pic + "");

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            String name = object.getString("name");
                            String email = object.getString("email");
                            String gender = object.getString("gender");
                            String birthday = object.getString("birthday");

                            if(name != null && email !=null && gender != null && birthday != null){
                                userName.setText(name);
                                emailId.setText(email);
                                if(gender.equalsIgnoreCase("male")){
                                    maleCB.setChecked(true);
                                }else{
                                    femaleCB.setChecked(true);
                                }
                                dateBirth.setText(birthday);
                            }

                            Log.e(TAG, "onCompleted: FB"+name+" "+email+" "+birthday+" "+gender);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                });
        Bundle permission_param = new Bundle();
        permission_param.putString("fields", "id,name,email,picture.width(120).height(120),gender,birthday");
        data_request.setParameters(permission_param);
        data_request.executeAsync();

    }

    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }
}
