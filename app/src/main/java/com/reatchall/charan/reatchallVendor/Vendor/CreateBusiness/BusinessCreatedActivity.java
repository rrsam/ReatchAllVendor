package com.reatchall.charan.reatchallVendor.Vendor.CreateBusiness;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.VendorDashBoardActivity;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class BusinessCreatedActivity extends AppCompatActivity {

    private static final String TAG = "BusinessCreatedActivity";

    FontTextView businessAdded, addProducts, gotoDashboard,home;

    BusinessDetails businessDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_business_created);


        businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
        if(businessDashboard != null){
            Log.e(TAG, "onCreate:   "+businessDashboard.getBusinessId() +" NAME "+businessDashboard.getBusinessName());
        }

        businessAdded=(FontTextView)findViewById(R.id.businessAdded);
        addProducts=(FontTextView)findViewById(R.id.addProducts);
        gotoDashboard=(FontTextView)findViewById(R.id.gotoDashboard);
        home=(FontTextView)findViewById(R.id.home);

        businessAdded.setText("Your Business \n"+businessDashboard.getBusinessName()+"\nhas been successfully created");

        addProducts=(FontTextView)findViewById(R.id.addProducts);
        gotoDashboard=(FontTextView)findViewById(R.id.gotoDashboard);
        home=(FontTextView)findViewById(R.id.home);

        addProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(BusinessCreatedActivity.this,VendorCreateBusinessActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                startActivity(intent);
                finish();*/
            }
        });

        gotoDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BusinessCreatedActivity.this,VendorDashBoardActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                startActivity(intent);
                finish();
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
