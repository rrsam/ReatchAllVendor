package com.reatchall.charan.reatchallVendor.Vendor;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorPrivacySettingsActivity extends AppCompatActivity {

    private static final String TAG = "VendorPrivacySettingsAc";
    ImageView backArrow;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    Context context;
    BusinessDetails businessDashboard;
    FontTextView titleToolbar;

    ImageView mobileUnCheckedIv,mobileCheckedIv,addressUnCheckedIv,addressCheckedIv;
    LinearLayout addressNumberLayout,mobileNumberLayout;
    boolean mobOrAdd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_privacy_settings);
        context = VendorPrivacySettingsActivity.this;
        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
        if(businessDashboard!=null){
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }
        customProgressDialog = new CustomProgressDialog(context);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        addressNumberLayout =(LinearLayout)findViewById(R.id.addressNumberLayout);
        mobileNumberLayout =(LinearLayout)findViewById(R.id.mobileNumberLayout);
        mobileUnCheckedIv =(ImageView) findViewById(R.id.mobileUnCheckedIv);
        mobileCheckedIv =(ImageView) findViewById(R.id.mobileCheckedIv);
        addressUnCheckedIv =(ImageView) findViewById(R.id.addressUnCheckedIv);
        addressCheckedIv =(ImageView) findViewById(R.id.addressCheckedIv);

        mobileCheckedIv.setVisibility(View.GONE);
        mobileUnCheckedIv.setVisibility(View.VISIBLE);
        addressCheckedIv.setVisibility(View.GONE);
        addressUnCheckedIv.setVisibility(View.VISIBLE);


        mobileNumberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobOrAdd=true;
                if(mobileCheckedIv.getVisibility()==View.GONE)
                    showPopup();
                else{
                    mobileCheckedIv.setVisibility(View.GONE);
                    mobileUnCheckedIv.setVisibility(View.VISIBLE);
                }
            }
        });

        addressNumberLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobOrAdd =false;
                if(addressCheckedIv.getVisibility()==View.GONE)
                    showPopup();
                else{
                    addressCheckedIv.setVisibility(View.GONE);
                    addressUnCheckedIv.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showPopup(){
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_hide_number_privacy);
        ImageView closePopup =(ImageView)dialog.findViewById(R.id.closePopup);
        FontTextView title = (FontTextView) dialog.findViewById(R.id.title);
        if(mobOrAdd)
            title.setText("Are you sure you want to hide your contact from customer ?");
        else
            title.setText("Are you sure you want to hide your address from customer ?");

        LinearLayout cancelPopup =(LinearLayout)dialog.findViewById(R.id.cancelPopup);
        LinearLayout proceedPopup =(LinearLayout)dialog.findViewById(R.id.proceedPopup);
        cancelPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        closePopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelPopup.performClick();
            }
        });

        proceedPopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mobOrAdd){
                    mobileCheckedIv.setVisibility(View.VISIBLE);
                    mobileUnCheckedIv.setVisibility(View.GONE);
                }else{
                    addressCheckedIv.setVisibility(View.VISIBLE);
                    addressUnCheckedIv.setVisibility(View.GONE);
                }
                dialog.dismiss();
            }
        });

        dialog.show();
    }


}
