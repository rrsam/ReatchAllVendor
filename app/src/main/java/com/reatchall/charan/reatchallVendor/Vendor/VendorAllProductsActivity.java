package com.reatchall.charan.reatchallVendor.Vendor;

import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.AllProductsPagerAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorAllProductsActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private static final String TAG = "VendorAllProductsActivity";
    ImageView backArrow;
    BusinessDetails businessDetails = null;
    CustomProgressDialog customProgressDialog;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AllProductsPagerAdapter mPagerAdapter;
    private ConstraintLayout clManageLayout;

    FontTextView txtSort,txtSelect,txtActions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_all_products);

        customProgressDialog=new CustomProgressDialog(VendorAllProductsActivity.this);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        businessDetails = getIntent().getExtras().getParcelable("businessDetails");
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        clManageLayout = (ConstraintLayout) findViewById(R.id.cl_products_manage);

        mPagerAdapter = new AllProductsPagerAdapter(this,getSupportFragmentManager(),businessDetails);
        viewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        clManageLayout.setVisibility(View.GONE);
                        break;

                    case 1:
                        clManageLayout.setVisibility(View.GONE);
                        break;
                    case 2:
                        clManageLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

                txtSort=(FontTextView)findViewById(R.id.txtSort);
        txtSelect=(FontTextView)findViewById(R.id.txtSelect);
        txtActions=(FontTextView)findViewById(R.id.txtActions);
        txtSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(VendorAllProductsActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.sort_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(VendorAllProductsActivity.this);
                popup.show();
            }
        });

        txtActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(VendorAllProductsActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.products_action_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(VendorAllProductsActivity.this);
                popup.show();
            }
        });

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }
}
