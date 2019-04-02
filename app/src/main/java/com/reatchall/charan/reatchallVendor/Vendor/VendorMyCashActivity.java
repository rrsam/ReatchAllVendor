package com.reatchall.charan.reatchallVendor.Vendor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorMyCashActivity extends AppCompatActivity {

    ImageView backArrow;

    private static final String TAG = "VendorMyCashActivity";
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;


    FontTextView currentCash,paymentHistory;
    View currentCashTab,paymentHistoryTab;


    RecyclerView recyclerView;
    ArrayList<String> arrayList;

    LinearLayout currentCashLayout;

    VendorCurrentCashAdapter vendorCurrentCashAdapter;
    VendorPaymentHistoryAdapter vendorPaymentHistoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_my_cash_new_two);
        backArrow = (ImageView) findViewById(R.id.back_arrow);
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

        currentCash=(FontTextView)findViewById(R.id.currentCash);
        paymentHistory=(FontTextView)findViewById(R.id.paymentHistory);

        currentCashTab=(View)findViewById(R.id.currentTab);
        paymentHistoryTab=(View)findViewById(R.id.paymentHistoryTab);


        currentCashLayout = (LinearLayout)findViewById(R.id.currentCashLayout);

        currentCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentCash.setBackgroundColor(getResources().getColor(R.color.grey_light));
                paymentHistory.setBackgroundColor(getResources().getColor(R.color.light_grey));

                currentCashTab.setVisibility(View.VISIBLE);
                paymentHistoryTab.setVisibility(View.INVISIBLE);

                currentCashLayout.setVisibility(View.VISIBLE);

                setupCurrentCashRecyclerView();
            }
        });



        paymentHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentHistory.setBackgroundColor(getResources().getColor(R.color.grey_light));
                currentCash.setBackgroundColor(getResources().getColor(R.color.light_grey));


                currentCashTab.setVisibility(View.INVISIBLE);
                paymentHistoryTab.setVisibility(View.VISIBLE);

                currentCashLayout.setVisibility(View.GONE);

                setupPaymentHistoryRecyclerView();

            }
        });

        currentCash.performClick();





    }


    private void setupCurrentCashRecyclerView(){
        recyclerView=(RecyclerView)findViewById(R.id.myCashRcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(VendorMyCashActivity.this,LinearLayoutManager.VERTICAL,false));
        arrayList = new ArrayList<>();
        for(int i=0;i<6;i++){
            arrayList.add("dsfs");
        }
        vendorCurrentCashAdapter = new VendorCurrentCashAdapter(VendorMyCashActivity.this,arrayList);
        recyclerView.setAdapter(vendorCurrentCashAdapter);

    }

    private  void setupPaymentHistoryRecyclerView(){
        recyclerView=(RecyclerView)findViewById(R.id.myCashRcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(VendorMyCashActivity.this,LinearLayoutManager.VERTICAL,false));
        arrayList = new ArrayList<>();
        for(int i=0;i<6;i++){
            arrayList.add("dsfs");
        }
        vendorPaymentHistoryAdapter = new VendorPaymentHistoryAdapter(VendorMyCashActivity.this,arrayList);
        recyclerView.setAdapter(vendorPaymentHistoryAdapter);
    }
}
