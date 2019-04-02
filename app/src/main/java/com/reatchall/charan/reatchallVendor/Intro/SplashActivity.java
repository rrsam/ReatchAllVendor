package com.reatchall.charan.reatchallVendor.Intro;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.reatchall.charan.reatchallVendor.HomeActivity;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Registration.ChooseUserActivity;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.StringConstants;
import com.reatchall.charan.reatchallVendor.Vendor.ManagerHomeActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Orders.VendorCurrentOrderActivity;
import com.reatchall.charan.reatchallVendor.Vendor.VendorHomeActivity;
import com.zl.reik.dilatingdotsprogressbar.DilatingDotsProgressBar;

import org.json.JSONObject;

import static com.reatchall.charan.reatchallVendor.Utils.StringConstants.BUZ_ID;
import static com.reatchall.charan.reatchallVendor.Utils.StringConstants.CURRENT_ORDER_ID;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";
    ImageView logo;
    TextView logoText,caption;

    private PrefManager prefManager;
    DilatingDotsProgressBar dilatingDotsProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_new);

        Log.e(TAG, "onCreate: "+FirebaseInstanceId.getInstance().getToken() );

        dilatingDotsProgressBar=(DilatingDotsProgressBar)findViewById(R.id.progress);
        dilatingDotsProgressBar.showNow();



        Checkout.preload(SplashActivity.this);

     // startPayment();
        onNewIntent(getIntent());
        //customNav();
    }


    private void customNav(){
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {


                prefManager = new PrefManager(SplashActivity.this);

                if(PrefManager.isManagerLoggedIn(SplashActivity.this) || PrefManager.isVendorLoggedIn(SplashActivity.this)){


                    if(PrefManager.isVendorLoggedIn(SplashActivity.this)){
                        Intent intent = new Intent(SplashActivity.this,VendorHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent = new Intent(SplashActivity.this,ManagerHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else{
                    prefManager.setCusorbus(false);
                    Intent intent = new Intent(SplashActivity.this, VideoScreenActivity.class);
                    intent.putExtra("VorU",1);
                    startActivity(intent);
                    finish();
                }


                //}


            }


        }, 2000L);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Bundle extras = intent.getExtras();
        String tabNumber;

        if(extras != null) {
            tabNumber = extras.getString("ACTION");
            Log.e("TEMP", " ACTION: " + tabNumber);
            if(tabNumber!=null && !tabNumber.equals("null")) {
                if (tabNumber.equals("NEW_ORDER")) {
                    Intent intent1 = new Intent(this, VendorCurrentOrderActivity.class);
                    intent1.putExtra(CURRENT_ORDER_ID, extras.getString(CURRENT_ORDER_ID));
                    intent1.putExtra(BUZ_ID, extras.getString(BUZ_ID));
                    startActivity(intent1);
                }
            }else{
                customNav();
            }
        } else {
            Log.e("TEMP", "Extras are NULL");
            customNav();
        }

    }

}
