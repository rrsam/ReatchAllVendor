package com.reatchall.charan.reatchallVendor.Vendor;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;

import fr.arnaudguyon.smartfontslib.FontTextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class VendorImportDatabaseFragment extends Fragment {


    private static final String TAG = "VendorImportDatabaseFra";
    FontTextView itemWise,listWise,wholeDatabase,recentlyUpdated;

    public VendorImportDatabaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendor_import_database, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemWise=(FontTextView)view.findViewById(R.id.itemWise);
        listWise=(FontTextView)view.findViewById(R.id.listWise);
        wholeDatabase=(FontTextView)view.findViewById(R.id.wholeDatabase);
        recentlyUpdated=(FontTextView)view.findViewById(R.id.recentlyUpdated);

        itemWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemWise.setBackgroundColor(getResources().getColor(R.color.grey));
                listWise.setBackgroundColor(Color.TRANSPARENT);
                wholeDatabase.setBackgroundColor(Color.TRANSPARENT);
                recentlyUpdated.setBackgroundColor(Color.TRANSPARENT);
            }
        });


        listWise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listWise.setBackgroundColor(getResources().getColor(R.color.grey));
                itemWise.setBackgroundColor(Color.TRANSPARENT);
                wholeDatabase.setBackgroundColor(Color.TRANSPARENT);
                recentlyUpdated.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        wholeDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wholeDatabase.setBackgroundColor(getResources().getColor(R.color.grey));
                listWise.setBackgroundColor(Color.TRANSPARENT);
                itemWise.setBackgroundColor(Color.TRANSPARENT);
                recentlyUpdated.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        recentlyUpdated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recentlyUpdated.setBackgroundColor(getResources().getColor(R.color.grey));
                listWise.setBackgroundColor(Color.TRANSPARENT);
                wholeDatabase.setBackgroundColor(Color.TRANSPARENT);
                itemWise.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        itemWise.performClick();

    }
}
