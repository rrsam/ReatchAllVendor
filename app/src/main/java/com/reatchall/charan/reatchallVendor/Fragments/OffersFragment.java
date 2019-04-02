package com.reatchall.charan.reatchallVendor.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.Adapters.OffersAdapter;
import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

/**
 * Created by NaNi on 09/02/18.
 */

public class OffersFragment extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> offersList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_view_business_offers, container, false);

        return rootView;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        offersList=new ArrayList<>();
        recyclerView=(RecyclerView)view.findViewById(R.id.offers_rcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        for(int i=1;i<=10;i++){
            offersList.add("Category "+i);
        }


        OffersAdapter offersAdapter = new OffersAdapter(offersList,getActivity());

        recyclerView.setAdapter(offersAdapter);


    }
}
