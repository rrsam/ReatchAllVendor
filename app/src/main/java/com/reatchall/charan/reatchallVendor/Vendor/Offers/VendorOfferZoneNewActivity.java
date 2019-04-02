package com.reatchall.charan.reatchallVendor.Vendor.Offers;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.VendorBizProfilePicFragment;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.VendorBuzOffersFragment;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.VendorItemOffersFragment;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorOfferZoneNewActivity extends AppCompatActivity {

    private static final String TAG = "VendorOfferZoneNewActiv";
    FontTextView businessOffers,itemOffers;
    ImageView backArrow;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    PrefManager prefManager;
    Context context;
    BusinessDetails businessDashboard;
    FontTextView titleToolbar;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_offer_zone_new);
        context = VendorOfferZoneNewActivity.this;
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
        if(businessDashboard!=null){
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }
        customProgressDialog = new CustomProgressDialog(context);
        prefManager = new PrefManager(context);
        businessOffers=(FontTextView)findViewById(R.id.businessOffers);
        itemOffers=(FontTextView)findViewById(R.id.itemOffers);

        businessOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadBuzOffersFragment();
            }
        });

        itemOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadItemOffersFragment();
            }
        });

        businessOffers.performClick();
    }


    private void loadBuzOffersFragment(){
        mHandler = new Handler();
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = new VendorBuzOffersFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("businessDetails",businessDashboard);
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
    private void loadItemOffersFragment(){
        mHandler = new Handler();
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = new VendorItemOffersFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("businessDetails",businessDashboard);
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


    public void highlightTab(int position){
        switch (position){
            case 0: {
                businessOffers.setTextColor(getResources().getColor(R.color.black_variant_two));
                businessOffers.setBackground(getResources().getDrawable(R.drawable.address_tag_selected_bg));
                itemOffers.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                itemOffers.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                break;
            }
            case 1: {
                itemOffers.setTextColor(getResources().getColor(R.color.black_variant_two));
                itemOffers.setBackground(getResources().getDrawable(R.drawable.address_tag_selected_bg));
                businessOffers.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                businessOffers.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                break;
            }

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
