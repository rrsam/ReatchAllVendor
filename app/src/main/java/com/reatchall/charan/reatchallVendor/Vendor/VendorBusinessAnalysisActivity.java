package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorBusinessAnalysisActivity extends AppCompatActivity {

    private static final String TAG = "VendorBusinessAnalysisA";

    ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;

    RelativeLayout earningsBreakDown,ordersBreakdown,acceptedBreakdown,searchBreakdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_business_analysis_new);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleToolbar = (FontTextView) findViewById(R.id.title_toolbar);
        businessDashboard = getIntent().getExtras().getParcelable("businessDetails");
        if (businessDashboard != null) {
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }


        earningsBreakDown=(RelativeLayout)findViewById(R.id.earningsBreakdown);
        ordersBreakdown=(RelativeLayout)findViewById(R.id.totalBreakdown);
        acceptedBreakdown=(RelativeLayout)findViewById(R.id.acceptedBreakdown);
        searchBreakdown=(RelativeLayout)findViewById(R.id.searchBreakdown) ;

        earningsBreakDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(VendorBusinessAnalysisActivity.this,TotalEarningsBreakdownActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                intent.putExtra("toggle",1);
                startActivity(intent);
            }
        });

        ordersBreakdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(VendorBusinessAnalysisActivity.this,TotalEarningsBreakdownActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                intent.putExtra("toggle",2);
                startActivity(intent);
            }
        });

        acceptedBreakdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(VendorBusinessAnalysisActivity.this,TotalEarningsBreakdownActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                intent.putExtra("toggle",3);
                startActivity(intent);
            }
        });

        searchBreakdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(VendorBusinessAnalysisActivity.this,VendorSearchHistoryActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                intent.putExtra("toggle",3);
                startActivity(intent);
            }
        });


    }
}
