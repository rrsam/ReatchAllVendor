package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.VendorBizBannersFragment;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.VendorBizProfilePicFragment;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.VendorBusinessLogoFragment;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.VendorEditEmailFragment;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.VendorEditMobileFragment;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.VendorEditNameFragment;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.VendorDetails;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorEditProfileActivity extends AppCompatActivity {
    private static final String TAG = "VendorEditProfileActivi";
    ImageView backArrow;
    FontTextView titleToolbar;
    VendorDetails vendorDetails;

    FontTextView profilePic,banners,logo;
    Handler mHandler;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_edit_profile);
        mHandler = new Handler();
        context = VendorEditProfileActivity.this;
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleToolbar = (FontTextView) findViewById(R.id.title_toolbar);
        vendorDetails = getIntent().getExtras().getParcelable("vendorDetails");
       /* if (vendorDetails != null) {
            titleToolbar.setText(vendorDetails.getBusinessName().toString());
        }*/


        profilePic=(FontTextView)findViewById(R.id.profilePic);
        banners=(FontTextView)findViewById(R.id.banners);
        logo=(FontTextView)findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadLogoFragment();
            }
        });

        banners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadBannersFragment();
            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadProfilePicFragment();
            }
        });

        logo.performClick();
    }

    public void highlightTab(int position){
        switch (position){
            case 1: {
                logo.setTextColor(getResources().getColor(R.color.black_variant_two));
                logo.setBackground(getResources().getDrawable(R.drawable.address_tag_selected_bg));
                banners.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                banners.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                profilePic.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                profilePic.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                break;
            }
            case 2: {
                banners.setTextColor(getResources().getColor(R.color.black_variant_two));
                banners.setBackground(getResources().getDrawable(R.drawable.address_tag_selected_bg));
                logo.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                logo.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                profilePic.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                profilePic.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                break;
            }
            case 3: {
                profilePic.setTextColor(getResources().getColor(R.color.black_variant_two));
                profilePic.setBackground(getResources().getDrawable(R.drawable.address_tag_selected_bg));
                logo.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                logo.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                banners.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                banners.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                break;
            }
        }
    }

    public void updateVendorDetails(VendorDetails vendorDetails){
        this.vendorDetails = vendorDetails;
    }

    private void loadLogoFragment(){
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = new VendorEditNameFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("vendorDetails",vendorDetails);
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.add(R.id.content_frame,fragment);
                fragmentTransaction.replace(R.id.content_frame, fragment, fragment.getTag());
                fragmentTransaction.addToBackStack(fragment.getTag());
                fragmentTransaction.commit();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
    }

    private void loadBannersFragment(){
        mHandler = new Handler();
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = new VendorEditMobileFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("vendorDetails",vendorDetails);
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.add(R.id.content_frame,fragment);
                fragmentTransaction.replace(R.id.content_frame, fragment, fragment.getTag());
                fragmentTransaction.addToBackStack(fragment.getTag());
                fragmentTransaction.commit();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
    }

    private void loadProfilePicFragment(){
        mHandler = new Handler();
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = new VendorEditEmailFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("vendorDetails",vendorDetails);
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.add(R.id.content_frame,fragment);
                fragmentTransaction.replace(R.id.content_frame, fragment, fragment.getTag());
                fragmentTransaction.addToBackStack(fragment.getTag());
                fragmentTransaction.commit();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
    }

    @Override
    public void onBackPressed() {
        int fragments = getSupportFragmentManager().getBackStackEntryCount();
        if (fragments == 1) {
            finish();
        } else {
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                getFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }
}
