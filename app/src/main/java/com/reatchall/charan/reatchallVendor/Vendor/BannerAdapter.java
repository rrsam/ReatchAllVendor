package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;

import java.util.List;

/**
 * Created by NaNi on 23/03/18.
 */

public class BannerAdapter extends PagerAdapter {

    private Context context;

    private List<String> data;
    private LayoutInflater mLayoutInflater;


    public BannerAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }
    @Override
    public int getItemPosition(final Object object) {
        return POSITION_NONE;
    }


    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final View view = mLayoutInflater.inflate(R.layout.item_banner, container,false);


        container.addView(view);
        return view;
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
