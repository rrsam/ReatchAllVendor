package com.reatchall.charan.reatchallVendor.Vendor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorContactUsActivity extends AppCompatActivity {

    private static final String TAG = "VendorContactUsActivity";


    ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_contact_us);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
       businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
       if(businessDashboard!=null){
           titleToolbar.setText(businessDashboard.getBusinessName().toString());
       }
    }
}
