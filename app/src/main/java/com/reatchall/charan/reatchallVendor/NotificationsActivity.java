package com.reatchall.charan.reatchallVendor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.reatchall.charan.reatchallVendor.Adapters.NotificationsAdapter;
import com.reatchall.charan.reatchallVendor.Intro.SplashActivity;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Vendor.Models.NotificationsModel;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 20/02/18.
 */

public class NotificationsActivity extends AppCompatActivity {

    private static final String TAG = "NotificationsActivity";
    RecyclerView recyclerView;
    ArrayList<NotificationsModel> arrayList;
    NotificationsAdapter notificationsAdapter;

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
    private Context context = NotificationsActivity.this;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

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



        recyclerView=(RecyclerView)findViewById(R.id.notifications_rcv);
        recyclerView.setHasFixedSize(true);
        arrayList=new ArrayList<>();

        arrayList.add(new NotificationsModel("New Order !","You Received a new Order \n click here to accept",0));
        arrayList.add(new NotificationsModel("Order Cancelled !","Order has been cancelled as you failed to respond within 10 mins",1));
        arrayList.add(new NotificationsModel("Order Delivered !","Your Order #WR21311 has been delivered to the customer successfully !",2));
        arrayList.add(new NotificationsModel("Order in transit","Your Order #WR21313 is on it's way to the customer and will be delivered in 1 hr",3));
        arrayList.add(new NotificationsModel("New Order !","You Received a new Order \n click here to accept",0));
        arrayList.add(new NotificationsModel("Order Cancelled !","Order has been cancelled as you failed to respond within 10 mins",1));
        arrayList.add(new NotificationsModel("Order Delivered !","Your Order #WR21311 has been delivered to the customer successfully !",2));
        arrayList.add(new NotificationsModel("Order in transit","Your Order #WR21313 is on it's way to the customer and will be delivered in 1 hr",3));
        arrayList.add(new NotificationsModel("New Order !","You Received a new Order \n click here to accept",0));
        arrayList.add(new NotificationsModel("Order Cancelled !","Order has been cancelled as you failed to respond within 10 mins",1));
        arrayList.add(new NotificationsModel("Order Delivered !","Your Order #WR21311 has been delivered to the customer successfully !",2));
        arrayList.add(new NotificationsModel("Order in transit","Your Order #WR21313 is on it's way to the customer and will be delivered in 1 hr",3));


        recyclerView.setLayoutManager(new LinearLayoutManager(NotificationsActivity.this, LinearLayoutManager.VERTICAL, false));
        notificationsAdapter=new NotificationsAdapter(arrayList,getApplicationContext());
        recyclerView.setAdapter(notificationsAdapter);


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

        navNotifications=(RelativeLayout)navHeader.findViewById(R.id.navNotifications);
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
                startActivity(new Intent(context,MyOrdersActivity.class));
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
                mDrawerLayout.closeDrawer(Gravity.RIGHT,true);
                startActivity(new Intent(context,MyCashActivity.class));

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
