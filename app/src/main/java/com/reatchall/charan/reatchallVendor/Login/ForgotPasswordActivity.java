package com.reatchall.charan.reatchallVendor.Login;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Registration.EnterOtpActivity;
import com.reatchall.charan.reatchallVendor.Registration.HelpUserActivity;
import com.reatchall.charan.reatchallVendor.Registration.RegisterActivity;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;

import fr.arnaudguyon.smartfontslib.FontTextView;

import static com.reatchall.charan.reatchallVendor.Login.LoginActivity.PhoneNumberValidator;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";
    FontTextView customer,business;
    boolean checkCusORBus;
    ReatchAll helper = ReatchAll.getInstance();

    private EditText username;
    private FontTextView newUser;

    private Button forgotPass;

    ImageView backArrow;

    PrefManager prefManager;

    FontTextView titleToolbar,help;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        prefManager= new PrefManager(ForgotPasswordActivity.this);
        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        help=(FontTextView)findViewById(R.id.help);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgotPasswordActivity.this, HelpUserActivity.class));
            }
        });


        username=(EditText) findViewById(R.id.phoneNumber);
        forgotPass=(Button)findViewById(R.id.forgot);

        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(username.getText().toString().length() !=0){
                    if(PhoneNumberValidator(username.getText())){

                        if(checkCusORBus){
                            prefManager.setCusorbus(true);
                        }else{
                            prefManager.setCusorbus(false);
                        }

                        prefManager.setForgotPass(true);
                        Intent intent = new Intent(ForgotPasswordActivity.this, EnterOtpActivity.class);
                        intent.putExtra("PHN",username.getText().toString());
                        startActivity(intent);

                    }else{
                        Snackbar snackbar = Snackbar
                                .make(findViewById(R.id.login_layout), "PHONE NUMBER NOT VALID", Snackbar.LENGTH_LONG);
                        snackbar.show();

                    }
                }else{
                    Snackbar snackbar = Snackbar
                            .make(findViewById(R.id.login_layout), "PHONE NUMBER EMPTY", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }


            }
        });
        customer=(FontTextView)findViewById(R.id.customer);
        business=(FontTextView)findViewById(R.id.business);
        newUser=(FontTextView)findViewById(R.id.newUser);

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ForgotPasswordActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });



        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customer.setBackgroundResource(R.color.primaryColor);
                business.setBackgroundResource(R.color.grey);
                username.getText().clear();
                checkCusORBus=true;

            }
        });
        business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customer.setBackgroundResource(R.color.grey);
                business.setBackgroundResource(R.color.primaryColor);
                username.getText().clear();
                checkCusORBus=false;

            }
        });
        customer.performClick();
    }
}
