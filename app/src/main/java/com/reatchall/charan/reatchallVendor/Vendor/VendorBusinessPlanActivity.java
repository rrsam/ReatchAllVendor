package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Plans.ComparePlansActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Plans.FreeBusinessPlansActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Plans.GoldBusinessPlansActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Plans.PlatinumBusinessPlansActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Plans.SilverBusinessPlansActivity;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorBusinessPlanActivity extends AppCompatActivity {
    private static final String TAG = "VendorBusinessPlanActiv";
    ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;

    LinearLayout silverLayout,goldLayout,platinumLayout,freeLayout;
    ImageView silverIV,goldIV,platinumIV,freeIV;

    boolean silverB=false,goldB=false,platB=false,freeB=false;

    FontButton comparePackages,viewDetails;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_business_plan);
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

        silverIV=(ImageView)findViewById(R.id.silverIV);
        goldIV=(ImageView)findViewById(R.id.goldIV);
        platinumIV=(ImageView)findViewById(R.id.platinumIV);
        freeIV=(ImageView)findViewById(R.id.freeIV);

        silverIV.setVisibility(View.INVISIBLE);
        goldIV.setVisibility(View.INVISIBLE);
        platinumIV.setVisibility(View.INVISIBLE);
        freeIV.setVisibility(View.INVISIBLE);

        silverLayout=(LinearLayout)findViewById(R.id.silverLayout);
        goldLayout=(LinearLayout)findViewById(R.id.goldLayout);
        platinumLayout=(LinearLayout)findViewById(R.id.platinumLayout);
        freeLayout=(LinearLayout)findViewById(R.id.freeLayout);



        freeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                freeLayout.setBackground(getResources().getDrawable(R.drawable.plan_selected_bg));
                goldLayout.setBackgroundColor(getResources().getColor(R.color.dark_orange));
                platinumLayout.setBackgroundColor(getResources().getColor(R.color.dark_orange));
                silverLayout.setBackgroundColor(getResources().getColor(R.color.dark_orange));

                silverIV.setVisibility(View.INVISIBLE);
                goldIV.setVisibility(View.INVISIBLE);
                platinumIV.setVisibility(View.INVISIBLE);
                freeIV.setVisibility(View.VISIBLE);


                freeB=true;
                silverB=false;
                goldB=false;
                platB=false;
            }
        });

        silverLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                silverLayout.setBackground(getResources().getDrawable(R.drawable.plan_selected_bg));
                goldLayout.setBackgroundColor(getResources().getColor(R.color.dark_orange));
                platinumLayout.setBackgroundColor(getResources().getColor(R.color.dark_orange));
                freeLayout.setBackgroundColor(getResources().getColor(R.color.dark_orange));

                silverIV.setVisibility(View.VISIBLE);
                goldIV.setVisibility(View.INVISIBLE);
                platinumIV.setVisibility(View.INVISIBLE);
                freeIV.setVisibility(View.INVISIBLE);


                freeB=false;
                silverB=true;
                goldB=false;
                platB=false;
            }
        });

        goldLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goldLayout.setBackground(getResources().getDrawable(R.drawable.plan_selected_bg));
                silverLayout.setBackgroundColor(getResources().getColor(R.color.dark_orange));
                platinumLayout.setBackgroundColor(getResources().getColor(R.color.dark_orange));
                freeLayout.setBackgroundColor(getResources().getColor(R.color.dark_orange));


                silverIV.setVisibility(View.INVISIBLE);
                goldIV.setVisibility(View.VISIBLE);
                platinumIV.setVisibility(View.INVISIBLE);
                freeIV.setVisibility(View.INVISIBLE);

                freeB=false;
                silverB=false;
                goldB=true;
                platB=false;
            }
        });

        platinumLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                platinumLayout.setBackground(getResources().getDrawable(R.drawable.plan_selected_bg));
                goldLayout.setBackgroundColor(getResources().getColor(R.color.dark_orange));
                silverLayout.setBackgroundColor(getResources().getColor(R.color.dark_orange));
                freeLayout.setBackgroundColor(getResources().getColor(R.color.dark_orange));


                silverIV.setVisibility(View.INVISIBLE);
                goldIV.setVisibility(View.INVISIBLE);
                platinumIV.setVisibility(View.VISIBLE);
                freeIV.setVisibility(View.INVISIBLE);

                freeB=false;
                silverB=false;
                goldB=false;
                platB=true;
            }
        });


        comparePackages=(FontButton)findViewById(R.id.comparePlans);
        viewDetails=(FontButton)findViewById(R.id.viewDetails);

        comparePackages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(VendorBusinessPlanActivity.this, ComparePlansActivity.class);
                    intent.putExtra("businessDetails",businessDashboard);
                    startActivity(intent);
            }
        });


        freeLayout.performClick();

        viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(silverB){
                        Intent intent = new Intent(VendorBusinessPlanActivity.this, SilverBusinessPlansActivity.class);
                        intent.putExtra("businessDetails",businessDashboard);
                        startActivity(intent);
                }

                if(goldB){
                    Intent intent = new Intent(VendorBusinessPlanActivity.this, GoldBusinessPlansActivity.class);
                    intent.putExtra("businessDetails",businessDashboard);
                    startActivity(intent);

                }

                if(platB){
                    Intent intent = new Intent(VendorBusinessPlanActivity.this, PlatinumBusinessPlansActivity.class);
                    intent.putExtra("businessDetails",businessDashboard);
                    startActivity(intent);

                }

                if(freeB){
                    Intent intent = new Intent(VendorBusinessPlanActivity.this, FreeBusinessPlansActivity.class);
                    intent.putExtra("businessDetails",businessDashboard);
                    startActivity(intent);

                }
            }
        });

    }
}
