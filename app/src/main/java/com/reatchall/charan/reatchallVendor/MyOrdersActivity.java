package com.reatchall.charan.reatchallVendor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.reatchall.charan.reatchallVendor.Intro.SplashActivity;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 19/02/18.
 */

public class MyOrdersActivity  extends AppCompatActivity{
    private static final String TAG = "MyOrdersActivity";

    ImageView emptyBox;
    FontTextView emptyTv,noOrdersTv;
    FontTextView currentOrders,previousOrders;
    View currentOrdersTab,previousOrdersTab;


    //NAV WIDGETS
    LinearLayout navHome,navProfileSettings,navPrivacySettings,navWishlist,navLogout,navRatings;
    LinearLayout navCash,navAddress,navFeedback,navInvite,navOrders;
    RelativeLayout navNotifications;
    FontTextView navNotficationBadge;
    private DrawerLayout mDrawerLayout;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;
    NavigationView navigationView;
    private View navHeader;
    ImageView drawer;
    private Context context = MyOrdersActivity.this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.navigation_view);
        navHeader = navigationView.getHeaderView(0);
        drawer=(ImageView)findViewById(R.id.navigation_drawer_person);
        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.RIGHT);
            }
        });
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setUpNavigationView();


        currentOrders=(FontTextView)findViewById(R.id.currentOrders);
        previousOrders=(FontTextView)findViewById(R.id.previousOrders);
        currentOrdersTab=(View)findViewById(R.id.currentOrdersTab);
        previousOrdersTab=(View)findViewById(R.id.previousOrdersTab);

        emptyBox=(ImageView)findViewById(R.id.emptyIV);
        emptyTv=(FontTextView)findViewById(R.id.emptyTV);
        noOrdersTv=(FontTextView)findViewById(R.id.noOrdersTV);

        currentOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currentOrders.setBackgroundColor(getResources().getColor(R.color.grey_light));
                previousOrders.setBackgroundColor(getResources().getColor(R.color.light_grey));

                currentOrdersTab.setVisibility(View.VISIBLE);
                previousOrdersTab.setVisibility(View.INVISIBLE);

                emptyBox.setVisibility(View.VISIBLE);
                emptyTv.setVisibility(View.GONE);
                noOrdersTv.setText("No Current Orders");

            }
        });

        previousOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                previousOrders.setBackgroundColor(getResources().getColor(R.color.grey_light));
                currentOrders.setBackgroundColor(getResources().getColor(R.color.light_grey));

                previousOrders.setVisibility(View.VISIBLE);
                currentOrdersTab.setVisibility(View.INVISIBLE);

                emptyBox.setVisibility(View.INVISIBLE);
                emptyTv.setVisibility(View.VISIBLE);
                noOrdersTv.setText("You haven't placed any orders yet");
            }
        });

        currentOrders.performClick();
    }


    private void setUpNavigationView() {

        navHome=(LinearLayout)navHeader.findViewById(R.id.navHome);
        navOrders=(LinearLayout)navHeader.findViewById(R.id.navMyOrders);
        navCash=(LinearLayout)navHeader.findViewById(R.id.navMyCash);
        navPrivacySettings=(LinearLayout)navHeader.findViewById(R.id.navPrivacySettings);

        navProfileSettings=(LinearLayout)navHeader.findViewById(R.id.navProfileSettings);
        navFeedback=(LinearLayout)navHeader.findViewById(R.id.navFeedback);
        navAddress=(LinearLayout)navHeader.findViewById(R.id.navAddress);
        navLogout=(LinearLayout)navHeader.findViewById(R.id.navLogout);

        navNotifications=(RelativeLayout) navHeader.findViewById(R.id.navNotifications);
        navWishlist=(LinearLayout)navHeader.findViewById(R.id.navWishlist);
        navRatings=(LinearLayout)navHeader.findViewById(R.id.navRatings);
        navInvite=(LinearLayout)navHeader.findViewById(R.id.navInviteEarn);

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
                startActivity(new Intent(context,HomeActivity.class));
            }
        });

        navOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
            }
        });

        navProfileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,ProfileSettingsActivity.class));
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
            }
        });

        navPrivacySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,PrivacySettingsActivity.class));
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
            }
        });

        navInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,InviteEarnActivity.class));
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
            }
        });

        navCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,MyCashActivity.class));
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
            }
        });

        navFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,FeedBackActivity.class));
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
            }
        });

        navAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,DeliveryAddressActivity.class));
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
            }
        });

        navWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,MyWishListActivity.class));
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
            }
        });

        navNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,NotificationsActivity.class));
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
            }
        });

        navRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context,RatingsReviewsActivity.class));
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
            }
        });

        navLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
                PrefManager.clearAll(context);
                Intent intent = new Intent(context, SplashActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
