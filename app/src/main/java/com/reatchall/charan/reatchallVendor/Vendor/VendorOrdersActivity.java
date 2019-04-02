package com.reatchall.charan.reatchallVendor.Vendor;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorOrdersActivity extends AppCompatActivity {

    ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;
    private static final String TAG = "VendorOrdersActivity";

    Spinner ordersSpinner;
    RecyclerView ordersRcv;

    VendorAllOrdersParentAdapter vendorAllOrdersParentAdapter;
    ArrayList<String> arrayList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_orders);

        backArrow=(ImageView)findViewById(R.id.back_arrow);
        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
        if(businessDashboard != null){
            titleToolbar.setText(businessDashboard.getBusinessName());
        }


        for(int i=1;i<=9;i++){
            arrayList.add(""+i);
        }

        vendorAllOrdersParentAdapter= new VendorAllOrdersParentAdapter(VendorOrdersActivity.this,arrayList);


        ordersRcv=(RecyclerView)findViewById(R.id.ordersRcv);
        ordersRcv.setNestedScrollingEnabled(false);
        ordersSpinner=(Spinner)findViewById(R.id.orderSpinner);

        ordersRcv.setHasFixedSize(true);
        ordersRcv.setLayoutManager(new LinearLayoutManager(VendorOrdersActivity.this, LinearLayoutManager.VERTICAL, false));
        ordersRcv.setAdapter(vendorAllOrdersParentAdapter);


        setupOrdersSpinner();


    }

    private void setupOrdersSpinner() {
        final List<String> plansList = Arrays.asList(getResources().getStringArray(R.array.ordersArray));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, plansList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont= Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(14);


                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(14);

                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ordersSpinner.setAdapter(dataAdapter);
        ordersSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {



            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
}
