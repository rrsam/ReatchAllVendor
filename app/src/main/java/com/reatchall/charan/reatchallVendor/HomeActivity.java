package com.reatchall.charan.reatchallVendor;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.reatchall.charan.reatchallVendor.Fragments.ParentFragment;
import com.reatchall.charan.reatchallVendor.Intro.SplashActivity;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;

import fr.arnaudguyon.smartfontslib.FontTextView;


/**
 * Created by NaNi on 06/02/18.
 */

public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";


    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_MY_ORDERS = "my_orders";
    private static final String TAG_MY_CASH = "my_cash";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_PROFILE_SETTINGS = "profile_settings";
    private static final String TAG_MY_WISHLIST="my_wishlist";
    private static final String TAG_DELIVERY_ADDRESS="delivery_address";
    private static final String TAG_PRIVACY_SETTINGS="privacy_settings";
    private static final String TAG_INVITE_EARN="invite_earn";
    private static final String TAG_FEEDBACK="feedback";
    private static final String TAG_TERMS_AND_CONDITIONS="terms_conditions";
    private static final String TAG_LOGOUT="logout";
    private static final String TAG_RATINGS="ratings";

    public static String CURRENT_TAG = TAG_HOME;

    private String[] mNavigationDrawerItemTitles;

    Toolbar toolbar;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    ImageView nearby,home,cart,search;
    private Handler mHandler;



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
    private Context context = HomeActivity.this;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mHandler = new Handler();
        mTitle = mDrawerTitle = getTitle();
        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);


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


        setFragment();

    }

    private void setFragment(){
        Fragment fragment = new ParentFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                android.R.anim.fade_out);
        fragmentTransaction.replace(R.id.content_frame, fragment, CURRENT_TAG);
        fragmentTransaction.commitAllowingStateLoss();
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
