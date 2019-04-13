package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.ActiveProductsFrag;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.ManageProductsFrag;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.PendingProductsFrag;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

public class AllProductsPagerAdapter extends FragmentStatePagerAdapter {

    private Context mContext;
    private BusinessDetails businessDetails;

    public AllProductsPagerAdapter(Context context, FragmentManager fm,BusinessDetails businessDetails) {
        super(fm);
        mContext = context;
        this.businessDetails = businessDetails;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return ActiveProductsFrag.newInstance(businessDetails);
        } else if (position == 1){
            return PendingProductsFrag.newInstance(businessDetails);
        } else {
            return ManageProductsFrag.newInstance(businessDetails);
        }

    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.active);
            case 1:
                return mContext.getString(R.string.pending);
            case 2:
                return mContext.getString(R.string.manage);

            default:
                return null;
        }
    }
}
