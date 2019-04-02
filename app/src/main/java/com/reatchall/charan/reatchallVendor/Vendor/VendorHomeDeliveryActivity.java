package com.reatchall.charan.reatchallVendor.Vendor;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorHomeDeliveryActivity extends AppCompatActivity {

    private static final String TAG = "VendorHomeDeliveryActiv";

    ImageView backArrow;

    FontTextView titleToolbar;
    BusinessDetails businessDashboard;

    FontButton enableHome,updateTimings;
    LinearLayout noHome,timingsLayout;
    FontTextView optOut;


    LinearLayout monDisabledLayout,tueDisabledLayout,wedDisabledLayout,thurDisabledLayout,friDisabledLayout,satDisabledLayout,sunDisabledLayout;
    LinearLayout monAbledLayout,tueAbledLayout,wedAbledLayout,thurAbledLayout,friAbledLayout,satAbledLayout,sunAbledLayout;

    ImageView monUnchecked,tueUnchecked,wedUnchecked,thurUnchecked,friUnchecked,satUnchecked,sunUnchecked;
    ImageView monChecked,tueChecked,wedChecked,thurChecked,friChecked,satChecked,sunChecked;

    Spinner monFromSpinner,tueFromSpinner,wedFromSpinner,thurFromSpinner,friFromSpinner,satFromSpinner,sunFromSpinner;
    Spinner monToSpinner,tueToSpinner,wedToSpinner,thurToSpinner,friToSpinner,satToSpinner,sunToSpinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_home_delivery);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titleToolbar = (FontTextView) findViewById(R.id.title_toolbar);
        businessDashboard = getIntent().getExtras().getParcelable("businessDetails");
        if (businessDashboard != null) {
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }

        enableHome=(FontButton)findViewById(R.id.enableHome);
        updateTimings=(FontButton)findViewById(R.id.updateTimings);
        noHome=(LinearLayout)findViewById(R.id.noHome);
        timingsLayout=(LinearLayout)findViewById(R.id.timingsLayout);
        optOut=(FontTextView)findViewById(R.id.optOut);
        noHome.setVisibility(View.VISIBLE);
        timingsLayout.setVisibility(View.GONE);

        enableHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noHome.setVisibility(View.GONE);
                timingsLayout.setVisibility(View.VISIBLE);
            }
        });

        updateTimings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        optOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noHome.setVisibility(View.VISIBLE);
                timingsLayout.setVisibility(View.GONE);
            }
        });

        initTimingsWidgets();
    }


    private void initTimingsWidgets() {
        monDisabledLayout=(LinearLayout)findViewById(R.id.monDisabledLayout);
        tueDisabledLayout=(LinearLayout)findViewById(R.id.tueDisabledLayout);
        wedDisabledLayout=(LinearLayout)findViewById(R.id.wedDisabledLayout);
        thurDisabledLayout=(LinearLayout)findViewById(R.id.thurDisabledLayout);
        friDisabledLayout=(LinearLayout)findViewById(R.id.friDisabledLayout);
        satDisabledLayout=(LinearLayout)findViewById(R.id.satDisabledLayout);
        sunDisabledLayout=(LinearLayout)findViewById(R.id.sunDisabledLayout);

        monAbledLayout=(LinearLayout)findViewById(R.id.monAbledLayout);
        tueAbledLayout=(LinearLayout)findViewById(R.id.tueAbledLayout);
        wedAbledLayout=(LinearLayout)findViewById(R.id.wedAbledLayout);
        thurAbledLayout=(LinearLayout)findViewById(R.id.thurAbledLayout);
        friAbledLayout=(LinearLayout)findViewById(R.id.friAbledLayout);
        satAbledLayout=(LinearLayout)findViewById(R.id.satAbledLayout);
        sunAbledLayout=(LinearLayout)findViewById(R.id.sunAbledLayout);


        monDisabledLayout.setVisibility(View.VISIBLE);
        tueDisabledLayout.setVisibility(View.VISIBLE);
        wedDisabledLayout.setVisibility(View.VISIBLE);
        thurDisabledLayout.setVisibility(View.VISIBLE);
        friDisabledLayout.setVisibility(View.VISIBLE);
        satDisabledLayout.setVisibility(View.VISIBLE);
        sunDisabledLayout.setVisibility(View.VISIBLE);

        monAbledLayout.setVisibility(View.GONE);
        tueAbledLayout.setVisibility(View.GONE);
        wedAbledLayout.setVisibility(View.GONE);
        thurAbledLayout.setVisibility(View.GONE);
        friAbledLayout.setVisibility(View.GONE);
        satAbledLayout.setVisibility(View.GONE);
        sunAbledLayout.setVisibility(View.GONE);

        monUnchecked=(ImageView)findViewById(R.id.monUnchecked);
        tueUnchecked=(ImageView)findViewById(R.id.tueUnchecked);
        wedUnchecked=(ImageView)findViewById(R.id.wedUnchecked);
        thurUnchecked=(ImageView)findViewById(R.id.thurUnchecked);
        friUnchecked=(ImageView)findViewById(R.id.friUnchecked);
        satUnchecked=(ImageView)findViewById(R.id.satUnchecked);
        sunUnchecked=(ImageView)findViewById(R.id.sunUnchecked);

        monChecked=(ImageView)findViewById(R.id.monChecked);
        tueChecked=(ImageView)findViewById(R.id.tueChecked);
        wedChecked=(ImageView)findViewById(R.id.wedChecked);
        thurChecked=(ImageView)findViewById(R.id.thurChecked);
        friChecked=(ImageView)findViewById(R.id.friChecked);
        satChecked=(ImageView)findViewById(R.id.satChecked);
        sunChecked=(ImageView)findViewById(R.id.sunChecked);


        monUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monDisabledLayout.setVisibility(View.GONE);
                monAbledLayout.setVisibility(View.VISIBLE);
            }
        });

        monChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monDisabledLayout.setVisibility(View.VISIBLE);
                monAbledLayout.setVisibility(View.GONE);
            }
        });

        //tue
        tueUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tueDisabledLayout.setVisibility(View.GONE);
                tueAbledLayout.setVisibility(View.VISIBLE);
            }
        });

        tueChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tueDisabledLayout.setVisibility(View.VISIBLE);
                tueAbledLayout.setVisibility(View.GONE);
            }
        });


        //wed
        wedUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wedDisabledLayout.setVisibility(View.GONE);
                wedAbledLayout.setVisibility(View.VISIBLE);
            }
        });

        wedChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wedDisabledLayout.setVisibility(View.VISIBLE);
                wedAbledLayout.setVisibility(View.GONE);
            }
        });

        //thur
        thurUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thurDisabledLayout.setVisibility(View.GONE);
                thurAbledLayout.setVisibility(View.VISIBLE);
            }
        });

        thurChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thurDisabledLayout.setVisibility(View.VISIBLE);
                thurAbledLayout.setVisibility(View.GONE);
            }
        });


        //fri
        friUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friDisabledLayout.setVisibility(View.GONE);
                friAbledLayout.setVisibility(View.VISIBLE);
            }
        });

        friChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friDisabledLayout.setVisibility(View.VISIBLE);
                friAbledLayout.setVisibility(View.GONE);
            }
        });

        //sat
        satUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                satDisabledLayout.setVisibility(View.GONE);
                satAbledLayout.setVisibility(View.VISIBLE);
            }
        });

        satChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                satDisabledLayout.setVisibility(View.VISIBLE);
                satAbledLayout.setVisibility(View.GONE);
            }
        });


        //sun
        sunUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sunDisabledLayout.setVisibility(View.GONE);
                sunAbledLayout.setVisibility(View.VISIBLE);
            }
        });

        sunChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sunDisabledLayout.setVisibility(View.VISIBLE);
                sunAbledLayout.setVisibility(View.GONE);
            }
        });


        monFromSpinner=(Spinner)findViewById(R.id.monFromSpinner);
        tueFromSpinner=(Spinner)findViewById(R.id.tueFromSpinner);
        wedFromSpinner=(Spinner)findViewById(R.id.wedFromSpinner);
        thurFromSpinner=(Spinner)findViewById(R.id.thurFromSpinner);
        friFromSpinner=(Spinner)findViewById(R.id.friFromSpinner);
        satFromSpinner=(Spinner)findViewById(R.id.satFromSpinner);
        sunFromSpinner=(Spinner)findViewById(R.id.sunFromSpinner);

        monToSpinner=(Spinner)findViewById(R.id.monToSpinner);
        tueToSpinner=(Spinner)findViewById(R.id.tueToSpinner);
        wedToSpinner=(Spinner)findViewById(R.id.wedToSpinner);
        thurToSpinner=(Spinner)findViewById(R.id.thurToSpinner);
        friToSpinner=(Spinner)findViewById(R.id.friToSpinner);
        satToSpinner=(Spinner)findViewById(R.id.satToSpinner);
        sunToSpinner=(Spinner)findViewById(R.id.sunToSpinner);

        initiateSpinners(monFromSpinner);
        initiateSpinners(tueFromSpinner);
        initiateSpinners(wedFromSpinner);
        initiateSpinners(thurFromSpinner);
        initiateSpinners(friFromSpinner);
        initiateSpinners(satFromSpinner);
        initiateSpinners(sunFromSpinner);

        initiateSpinners(monToSpinner);
        initiateSpinners(tueToSpinner);
        initiateSpinners(wedToSpinner);
        initiateSpinners(thurToSpinner);
        initiateSpinners(friToSpinner);
        initiateSpinners(satToSpinner);
        initiateSpinners(sunToSpinner);

    }


    private void initiateSpinners(Spinner spinner){

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.timings_array)){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont= Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(12);


                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(12);

                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

}
