package com.reatchall.charan.reatchallVendor.Vendor.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.BusinessAllProductsActivity;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class ManageProductsFrag extends Fragment {



    public ManageProductsFrag() {
    }

    public static ManageProductsFrag newInstance() {
        ManageProductsFrag fragment = new ManageProductsFrag();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_manage_products, container, false);

        return view;
    }


}
