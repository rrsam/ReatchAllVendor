package com.reatchall.charan.reatchallVendor.Vendor;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.R;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorAdvertisementsActivity extends AppCompatActivity {

    private static final String TAG = "VendorAdvertisementsAct";
    ImageView backArrow;

    FontTextView presentAd,pastAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_advertisements);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        presentAd=(FontTextView)findViewById(R.id.presentAd);
        pastAd=(FontTextView)findViewById(R.id.pastAd);

        presentAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                presentAd.setBackgroundColor(getResources().getColor(R.color.grey));
                pastAd.setBackgroundColor(Color.TRANSPARENT);

                Fragment fragment = new VendorBannerImagesFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.content_frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();

            }
        });

        pastAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pastAd.setBackgroundColor(getResources().getColor(R.color.grey));
                presentAd.setBackgroundColor(Color.TRANSPARENT);

                Fragment fragment = new VendorBannerImagesFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.content_frame, fragment, fragment.getTag());
                fragmentTransaction.commitAllowingStateLoss();

            }
        });


        presentAd.performClick();
    }
}
