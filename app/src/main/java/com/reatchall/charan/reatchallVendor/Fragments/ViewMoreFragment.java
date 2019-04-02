package com.reatchall.charan.reatchallVendor.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.ViewMoreAboutUsActivity;
import com.reatchall.charan.reatchallVendor.ViewMoreContactUsActivity;
import com.reatchall.charan.reatchallVendor.ViewMoreRatingsActivity;
import com.reatchall.charan.reatchallVendor.ViewMoreRemarksActivity;
import com.reatchall.charan.reatchallVendor.ViewMoreTimingsActivity;
import com.reatchall.charan.reatchallVendor.ViewMoreWebSocialActivity;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 09/02/18.
 */

public class ViewMoreFragment extends Fragment {


    FontTextView about,ratings,remarks,webSocial,timings,contact;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_view_business_view_more, container, false);

        return rootView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        about=(FontTextView)view.findViewById(R.id.about_us);
        ratings=(FontTextView)view.findViewById(R.id.ratings);
        remarks=(FontTextView)view.findViewById(R.id.remarks);
        timings=(FontTextView)view.findViewById(R.id.timings);
        webSocial=(FontTextView)view.findViewById(R.id.web_social);
        contact=(FontTextView)view.findViewById(R.id.contact_us);

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            startActivity(new Intent(getActivity(), ViewMoreAboutUsActivity.class));

            }
        });
        ratings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ViewMoreRatingsActivity.class));
            }
        });
        remarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ViewMoreRemarksActivity.class));

            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ViewMoreContactUsActivity.class));

            }
        });
        webSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ViewMoreWebSocialActivity.class));

            }
        });
        timings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), ViewMoreTimingsActivity.class));

            }
        });

    }
}
