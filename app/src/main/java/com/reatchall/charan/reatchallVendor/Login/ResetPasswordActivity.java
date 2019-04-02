package com.reatchall.charan.reatchallVendor.Login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;

import org.json.JSONException;
import org.json.JSONObject;

public class ResetPasswordActivity extends AppCompatActivity {
    private static final String TAG = "ResetPasswordActivity";

    EditText password,cnfrmPass;
    Button reset;

    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;

    String phnNumber=null,otpValue=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        password=(EditText)findViewById(R.id.password);
        cnfrmPass=(EditText)findViewById(R.id.cnfrmPassword);
        reset=(Button)findViewById(R.id.submit);
        phnNumber=getIntent().getExtras().getString("PHN");
        if(phnNumber==null){
            Toast.makeText(ResetPasswordActivity.this,"ERROR",Toast.LENGTH_LONG).show();
        }
        otpValue=getIntent().getExtras().getString("OTP");
        if(otpValue==null){
            Toast.makeText(ResetPasswordActivity.this,"ERROR",Toast.LENGTH_LONG).show();

        }

        customProgressDialog= new CustomProgressDialog(ResetPasswordActivity.this);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(password.getText().toString().length() !=0 && cnfrmPass.getText().toString().length()!=0){
                    if(password.getText().toString().equalsIgnoreCase(cnfrmPass.getText().toString())){

                        formString();

                    }else{
                        Toast.makeText(ResetPasswordActivity.this,"Password's doesn't match",Toast.LENGTH_LONG).show();

                    }
                }else{
                    Toast.makeText(ResetPasswordActivity.this,"Password cannot be empty",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    JSONObject resetPass = null;
    private void formString(){
        try {

            resetPass = new JSONObject();

            resetPass.put("mobile",phnNumber);
            resetPass.put("pwd",password.getText().toString());
            resetPass.put("otp",otpValue);
            customProgressDialog.showDialog();
            resetPassword();

        }catch (Exception e){

        }

    }

    private void resetPassword(){
        PrefManager prefManager= new PrefManager(ResetPasswordActivity.this);
        String url=null;
        if(prefManager.isCusorbus()){
             url ="http://13.127.169.96:3000/user/reset-pwd";

        }else{
            url ="http://13.127.169.96:3000/vendor/reset-pwd";

        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.PUT, url, resetPass, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                customProgressDialog.hideDialog();

                try{
                    boolean success = response.getBoolean("success");
                    if(success){

                        Toast.makeText(ResetPasswordActivity.this,"PASSWORD UPDATED",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(ResetPasswordActivity.this,LoginActivity.class));
                        finish();
                    }else{
                        String msg = response.getString("msg");
                        Toast.makeText(ResetPasswordActivity.this,msg,Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(ResetPasswordActivity.this,""+error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"RESETPASSWORD");
    }
}
