package com.reatchall.charan.reatchallVendor.Intro;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.reatchall.charan.reatchallVendor.Login.UserLoginActivity;
import com.reatchall.charan.reatchallVendor.Login.VendorLoginActivity;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Registration.ChooseUserActivity;

import org.json.JSONObject;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VideoScreenActivity extends AppCompatActivity implements PaymentResultListener {

    private static final String TAG = "VideoScreenActivity";

    FontTextView titleToolbar;
    RelativeLayout loginLayout;

    FontTextView hindi,telugu,english;


    int vORu=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_screen);

        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        loginLayout=(RelativeLayout)findViewById(R.id.loginLayout);
        vORu=getIntent().getExtras().getInt("VorU");
        if(vORu==0){
            titleToolbar.setText("Reatchall User");
        }else{
            titleToolbar.setText("Reatchall Vendor");
        }

        loginLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vORu==0){
                    startActivity(new Intent(VideoScreenActivity.this, UserLoginActivity.class));

                }else{
                        startActivity(new Intent(VideoScreenActivity.this, ChooseUserActivity.class));
                }

               //startPayment();
            }
        });



        hindi=(FontTextView)findViewById(R.id.hindi);
        english=(FontTextView)findViewById(R.id.english);
        telugu=(FontTextView)findViewById(R.id.telugu);

        hindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hindi.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                telugu.setBackgroundColor(getResources().getColor(R.color.blue_dark));
                english.setBackgroundColor(getResources().getColor(R.color.blue_dark));
            }
        });

        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                english.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                telugu.setBackgroundColor(getResources().getColor(R.color.blue_dark));
                hindi.setBackgroundColor(getResources().getColor(R.color.blue_dark));
            }
        });

        telugu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                telugu.setBackgroundColor(getResources().getColor(R.color.primaryColor));
                hindi.setBackgroundColor(getResources().getColor(R.color.blue_dark));
                english.setBackgroundColor(getResources().getColor(R.color.blue_dark));
            }
        });

        hindi.performClick();
    }

    private void startPayment(){
        Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.logonew);
        final Activity activity = VideoScreenActivity.this;
        try {
            JSONObject options = new JSONObject();
            options.put("name", "Reatchall");
            options.put("description", "FMS");
            options.put("currency", "INR");
            options.put("amount", "500");
            checkout.setFullScreenDisable(true);
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e(TAG, "onPaymentSuccess: "+s);
    }

    @Override
    public void onPaymentError(int i, String s) {
        if(i==Checkout.NETWORK_ERROR){
            Log.e(TAG, "onPaymentError: NETWORK ERROR" );
        }
        if(i==Checkout.INVALID_OPTIONS){
            Log.e(TAG, "onPaymentError: INVALID OPTIONS" );
        }
        if(i==Checkout.PAYMENT_CANCELED){
            Log.e(TAG, "onPaymentError: PAYMENT CANCELLED BY USER" );
        }
        if(i==Checkout.TLS_ERROR){
            Log.e(TAG, "onPaymentError: TLS ERROR" );
        }
    }
}
