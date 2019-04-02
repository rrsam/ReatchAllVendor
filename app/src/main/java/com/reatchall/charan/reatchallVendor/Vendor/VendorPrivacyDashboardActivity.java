package com.reatchall.charan.reatchallVendor.Vendor;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDashboard;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorPrivacyDashboardActivity extends AppCompatActivity {

    private static final String TAG = "VendorPrivacyDashboardA";

    ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDashboard businessDashboard;

    LinearLayout hideNumberLayout,hideAddressLayout;
    ImageView emptyCheckNumber,savedCheckNumber;
    ImageView emptyCheckAddress,savedCheckAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_privacy_dashboard);

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

        hideNumberLayout=(LinearLayout)findViewById(R.id.numberLayout);
        hideAddressLayout=(LinearLayout)findViewById(R.id.addressLayout);

        emptyCheckNumber=(ImageView)findViewById(R.id.emptyCheckNumber);
        emptyCheckAddress=(ImageView)findViewById(R.id.emptyCheckAddress);
        savedCheckNumber=(ImageView)findViewById(R.id.savedCheckNumber);
        savedCheckAddress=(ImageView)findViewById(R.id.savedCheckAddress);

        emptyCheckNumber.setVisibility(View.VISIBLE);
        emptyCheckAddress.setVisibility(View.VISIBLE);

        savedCheckNumber.setVisibility(View.GONE);
        savedCheckAddress.setVisibility(View.GONE);



        hideNumberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emptyCheckNumber.getVisibility()==View.VISIBLE){
                    emptyCheckNumber.setVisibility(View.GONE);
                    savedCheckNumber.setVisibility(View.VISIBLE);

                    //popup

                    final Dialog dialog = new Dialog(VendorPrivacyDashboardActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.popup_hide_number_privacy);
                    dialog.setCancelable(false);
                    dialog.show();

                    ImageView closePopup = (ImageView)dialog.findViewById(R.id.closePopup);
                    LinearLayout cancelPopup = (LinearLayout) dialog.findViewById(R.id.cancelPopup);
                    LinearLayout proceedPopup = (LinearLayout) dialog.findViewById(R.id.proceedPopup);

                    closePopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            emptyCheckNumber.setVisibility(View.VISIBLE);
                            savedCheckNumber.setVisibility(View.GONE);
                        }
                    });

                    cancelPopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            emptyCheckNumber.setVisibility(View.VISIBLE);
                            savedCheckNumber.setVisibility(View.GONE);
                        }
                    });

                    proceedPopup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            emptyCheckNumber.setVisibility(View.GONE);
                            savedCheckNumber.setVisibility(View.VISIBLE);
                        }
                    });


                }else{
                    emptyCheckNumber.setVisibility(View.VISIBLE);
                    savedCheckNumber.setVisibility(View.GONE);
                }
            }
        });


        hideAddressLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(emptyCheckAddress.getVisibility()==View.VISIBLE){
                    emptyCheckAddress.setVisibility(View.GONE);
                    savedCheckAddress.setVisibility(View.VISIBLE);

                    //popup
                }else{
                    emptyCheckAddress.setVisibility(View.VISIBLE);
                    savedCheckAddress.setVisibility(View.GONE);
                }
            }
        });


    }
}
