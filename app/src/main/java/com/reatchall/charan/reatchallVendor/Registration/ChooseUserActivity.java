package com.reatchall.charan.reatchallVendor.Registration;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.reatchall.charan.reatchallVendor.Intro.VideoScreenActivity;
import com.reatchall.charan.reatchallVendor.Login.ManagerLoginActivity;
import com.reatchall.charan.reatchallVendor.Login.VendorLoginActivity;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;

public class ChooseUserActivity extends AppCompatActivity {

    private static final String TAG = "ChooseUserActivity";

    LinearLayout userLogin, vendorLogin;
    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);
        prefManager = new PrefManager(ChooseUserActivity.this);
        userLogin=(LinearLayout)findViewById(R.id.userLogin);
        vendorLogin=(LinearLayout)findViewById(R.id.vendorLogin);

        vendorLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefManager.setCusorbus(false);
                Intent intent = new Intent(ChooseUserActivity.this, VendorLoginActivity.class);
                intent.putExtra("VorU",1);
                startActivity(intent);
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefManager.setCusorbus(true);

                Intent intent = new Intent(ChooseUserActivity.this, ManagerLoginActivity.class);
                intent.putExtra("VorU",0);
                startActivity(intent);
            }
        });
    }
}
