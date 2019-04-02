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

public class VendorOrderAnalysisActivity extends AppCompatActivity {
    private static final String TAG = "VendorOrderAnalysisActi";
    ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;

    RelativeLayout ordersBreakdown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_order_analysis);

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


        ordersBreakdown=(RelativeLayout)findViewById(R.id.totalBreakdown);



        ordersBreakdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(VendorOrderAnalysisActivity.this,TotalEarningsBreakdownActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                intent.putExtra("toggle",2);
                startActivity(intent);
            }
        });
    }
}
