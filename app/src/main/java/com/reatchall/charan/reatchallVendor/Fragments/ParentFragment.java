package com.reatchall.charan.reatchallVendor.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.Adapters.ParentTabsPagerAdapter;
import com.reatchall.charan.reatchallVendor.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ParentFragment extends Fragment {

    private static final String TAG = "ParentFragment";
    TabLayout tabLayout;


    private int[] imageResId = {
            R.drawable.ic_drawer_logo,
            R.drawable.ic_location_white,
            R.drawable.ic_search_white,
            R.drawable.ic_cart_white};

    ParentTabsPagerAdapter parentTabsPagerAdapter;
    ViewPager viewPager;

    public ParentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parent, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager=(ViewPager)view.findViewById(R.id.viewpager);


        parentTabsPagerAdapter= new ParentTabsPagerAdapter(getChildFragmentManager(),getActivity());
        parentTabsPagerAdapter.addFragment(new HomeFragment());
        parentTabsPagerAdapter.addFragment(new NearByFragment());
        parentTabsPagerAdapter.addFragment(new SearchFragment());
        parentTabsPagerAdapter.addFragment(new CartFragment());
        viewPager.setAdapter(parentTabsPagerAdapter);
        tabLayout=(TabLayout) view.findViewById(R.id.tabsParent);
        tabLayout.setupWithViewPager(viewPager);
        for(int i=0;i<imageResId.length;i++){
            tabLayout.getTabAt(i).setIcon(imageResId[i]);

        }

    }
}
