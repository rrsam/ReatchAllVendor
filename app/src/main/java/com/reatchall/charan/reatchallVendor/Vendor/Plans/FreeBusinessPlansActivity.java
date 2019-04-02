package com.reatchall.charan.reatchallVendor.Vendor.Plans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDashboard;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class FreeBusinessPlansActivity extends AppCompatActivity {
    private static final String TAG = "FreeBusinessPlansActivi";
    ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;

    FontButton comparePackages,checkOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_business_plans);

        backArrow=(ImageView) findViewById(R.id.back_arrow);
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

        comparePackages=(FontButton)findViewById(R.id.comparePlans);
        checkOut=(FontButton)findViewById(R.id.checkOut);

        comparePackages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(FreeBusinessPlansActivity.this, ComparePlansActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                startActivity(intent);
            }
        });
    }
}
