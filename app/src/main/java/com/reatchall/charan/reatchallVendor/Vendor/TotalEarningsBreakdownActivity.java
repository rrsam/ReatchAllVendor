package com.reatchall.charan.reatchallVendor.Vendor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDashboard;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class TotalEarningsBreakdownActivity extends AppCompatActivity {

    private static final String TAG = "TotalEarningsBreakdownA";
    ImageView backArrow;
    FontTextView titleToolbar,subTitle;
    BusinessDashboard businessDashboard;

    int toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_earnings_breakdown);

        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleToolbar = (FontTextView) findViewById(R.id.title_toolbar);
        subTitle = (FontTextView) findViewById(R.id.subTitle);
        businessDashboard = getIntent().getExtras().getParcelable("businessDetails");
        toggle=getIntent().getExtras().getInt("toggle");
        if(toggle==1){
            subTitle.setText("Total Earnings Breakdown");
        }
        if(toggle==2) {
            subTitle.setText("Total Orders Breakdown");

        }
        if(toggle==3){
            subTitle.setText("Accepted Orders Breakdown");

        }
        if (businessDashboard != null) {
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }
    }
}
