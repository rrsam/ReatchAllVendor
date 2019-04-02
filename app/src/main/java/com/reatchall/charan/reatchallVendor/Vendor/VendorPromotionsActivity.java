package com.reatchall.charan.reatchallVendor.Vendor;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.FetchPath;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.VendorPromotionsImageAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.VendorPromotionsVideoAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.VendorBizBannersFragment;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.VendorBizPromoImagesFragment;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.VendorBizPromoVideosFragment;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.VendorBusinessLogoFragment;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IDeletePromoImage;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IDeletePromoVideo;
import com.volokh.danylo.video_player_manager.manager.PlayerItemChangeListener;
import com.volokh.danylo.video_player_manager.manager.SingleVideoPlayerManager;
import com.volokh.danylo.video_player_manager.manager.VideoPlayerManager;
import com.volokh.danylo.video_player_manager.meta.MetaData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.smartfontslib.FontTextView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class VendorPromotionsActivity extends AppCompatActivity {

    private static final String TAG = "VendorPromotionsActivit";
    ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;

    FontTextView banners, logo;
    Handler mHandler;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_promotions);
        mHandler = new Handler();
        context = VendorPromotionsActivity.this;
        backArrow = (ImageView) findViewById(R.id.back_arrow);
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


        banners = (FontTextView) findViewById(R.id.banners);
        logo = (FontTextView) findViewById(R.id.logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadBannersFragment();
            }
        });

        banners.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loadLogoFragment();

            }
        });

        logo.performClick();
    }

    public void highlightTab(int position){
        switch (position){
            case 0: {
                logo.setTextColor(getResources().getColor(R.color.black_variant_two));
                logo.setBackground(getResources().getDrawable(R.drawable.address_tag_selected_bg));
                banners.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                banners.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                break;
            }
            case 1: {
                banners.setTextColor(getResources().getColor(R.color.black_variant_two));
                banners.setBackground(getResources().getDrawable(R.drawable.address_tag_selected_bg));
                logo.setTextColor(getResources().getColor(R.color.address_tag_text_color));
                logo.setBackground(getResources().getDrawable(R.drawable.address_tag_un_selected_bg));
                break;
            }
        }
    }


    private void loadLogoFragment() {
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = new VendorBizPromoImagesFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("businessDetails", businessDashboard);
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.add(R.id.content_frame, fragment);
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

    private void loadBannersFragment() {
        mHandler = new Handler();
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = new VendorBizPromoVideosFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("businessDetails", businessDashboard);
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.add(R.id.content_frame, fragment);
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