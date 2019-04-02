package com.reatchall.charan.reatchallVendor.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.Adapters.ViewBusinessProductsAdapterNew;
import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by NaNi on 08/02/18.
 */

public class ViewBusinessProductsFragment extends Fragment {

    private static final String TAG = "ViewBusinessProductsFra";

    RecyclerView recyclerView;

    ArrayList<String> categoryList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_view_business_products, container, false);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView=(RecyclerView)view.findViewById(R.id.category_rcv);
        categoryList = new ArrayList<>();
        Random ran = new Random();
        int x = ran.nextInt(6)+1;

       recyclerView.setHasFixedSize(true);
       recyclerView.setNestedScrollingEnabled(false);
       recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),4,1,false));

        for(int i=1;i<=20;i++){
            categoryList.add("Category "+i);
        }


        ViewBusinessProductsAdapterNew viewBusinessProductsAdapterNew = new ViewBusinessProductsAdapterNew(getActivity(),categoryList);

        recyclerView.setAdapter(viewBusinessProductsAdapterNew);
    }
}
