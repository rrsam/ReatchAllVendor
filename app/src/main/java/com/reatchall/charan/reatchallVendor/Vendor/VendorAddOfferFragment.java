package com.reatchall.charan.reatchallVendor.Vendor;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.reatchall.charan.reatchallVendor.R;

import java.util.Arrays;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class VendorAddOfferFragment extends Fragment {

    private static final String TAG = "VendorAddOfferFragment";


    public VendorAddOfferFragment() {
        // Required empty public constructor
    }

    Spinner offerSpinner;
    Button submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendor_add_offer, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        offerSpinner=(Spinner)view.findViewById(R.id.offerSpinner);
        submit=(Button)view.findViewById(R.id.submitOffer);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        List<String> timingsList = Arrays.asList(getResources().getStringArray(R.array.offers_array));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, timingsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        offerSpinner.setAdapter(dataAdapter);
    }
}
