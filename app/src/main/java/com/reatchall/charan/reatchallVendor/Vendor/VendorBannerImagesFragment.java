package com.reatchall.charan.reatchallVendor.Vendor;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.infinitecycleviewpager.HorizontalInfiniteCycleViewPager;
import com.gigamole.infinitecycleviewpager.OnInfiniteCyclePageTransformListener;
import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class VendorBannerImagesFragment extends Fragment {

    private static final String TAG = "VendorBannerImagesFragm";

    ArrayList<String> arrayList= new ArrayList<>();

    BannerAdapter bannerAdapter;

    public VendorBannerImagesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendor_banner_images, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        arrayList.add("Let's get loco");
        arrayList.add("Carnivale");
        arrayList.add("Citadel 2018");
        arrayList.add("Black parade");
        arrayList.add("jungle therapy");
        arrayList.add("Let's get loco");
        arrayList.add("Carnivale");
        arrayList.add("Citadel 2018");
        arrayList.add("Black parade");
        arrayList.add("jungle therapy");


        final HorizontalInfiniteCycleViewPager verticalInfiniteCycleViewPager =
                (HorizontalInfiniteCycleViewPager) view.findViewById(R.id.vicvp);

        bannerAdapter = new BannerAdapter(getActivity(),arrayList);
        verticalInfiniteCycleViewPager.setAdapter(bannerAdapter);
        verticalInfiniteCycleViewPager.notifyDataSetChanged();
        verticalInfiniteCycleViewPager.setOnInfiniteCyclePageTransformListener(new OnInfiniteCyclePageTransformListener() {
            @Override
            public void onPreTransform(View page, float position) {

            }

            @Override
            public void onPostTransform(View page, float position) {
                Log.e(TAG, "onPostTransform: "+verticalInfiniteCycleViewPager.getRealItem()+"POSITION"+position );
            }
        });
    }
}
