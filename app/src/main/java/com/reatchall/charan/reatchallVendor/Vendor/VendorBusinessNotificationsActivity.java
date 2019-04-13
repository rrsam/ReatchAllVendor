package com.reatchall.charan.reatchallVendor.Vendor;

import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.Adapters.NotificationsAdapter;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.NotificationsModel;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorBusinessNotificationsActivity extends AppCompatActivity {
    private static final String TAG = "VendorBusinessNotificat";
    ImageView backArrow;
    FontTextView orders,payment,general;


    RecyclerView recyclerView;
    ArrayList<NotificationsModel> arrayList;
    NotificationsAdapter notificationsAdapter;
    NestedScrollView nestedScrollView;

    BusinessDetails businessDashboard;
    FontTextView titleToolbar;

    View ordersTab, paymentsTab, generalTab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_fragment_notifications_new);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
        if(businessDashboard!=null){
            titleToolbar.setText("Notifications");
        }

        orders=(FontTextView)findViewById(R.id.ordersNotifications);
        payment=(FontTextView)findViewById(R.id.paymentNotifications);
        general=(FontTextView)findViewById(R.id.generalNotifications);

        ordersTab=(View)findViewById(R.id.ordersTab);
        paymentsTab=(View)findViewById(R.id.paymentsTab);
        generalTab=(View)findViewById(R.id.generalTab);



        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orders.setBackgroundColor(getResources().getColor(R.color.grey_light));
                payment.setBackgroundColor(getResources().getColor(R.color.light_grey));
                general.setBackgroundColor(getResources().getColor(R.color.light_grey));

                ordersTab.setVisibility(View.VISIBLE);
                paymentsTab.setVisibility(View.INVISIBLE);
                generalTab.setVisibility(View.INVISIBLE);
            }
        });



        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payment.setBackgroundColor(getResources().getColor(R.color.grey_light));
                orders.setBackgroundColor(getResources().getColor(R.color.light_grey));
                general.setBackgroundColor(getResources().getColor(R.color.light_grey));


                ordersTab.setVisibility(View.INVISIBLE);
                paymentsTab.setVisibility(View.VISIBLE);
                generalTab.setVisibility(View.INVISIBLE);
            }
        });

        general.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                general.setBackgroundColor(getResources().getColor(R.color.grey_light));
                payment.setBackgroundColor(getResources().getColor(R.color.light_grey));
                orders.setBackgroundColor(getResources().getColor(R.color.light_grey));

                ordersTab.setVisibility(View.INVISIBLE);
                paymentsTab.setVisibility(View.INVISIBLE);
                generalTab.setVisibility(View.VISIBLE);

            }
        });

        orders.performClick();

        recyclerView=(RecyclerView)findViewById(R.id.notifications_rcv);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        arrayList=new ArrayList<>();

        arrayList.add(new NotificationsModel("New Order !","You Received a new Order \n click here to accept",0));
        arrayList.add(new NotificationsModel("Order Cancelled !","Order has been cancelled as you failed to respond within 10 mins",1));
        arrayList.add(new NotificationsModel("Order Delivered !","Your Order #WR21311 has been delivered to the customer successfully !",2));
        arrayList.add(new NotificationsModel("Order in transit","Your Order #WR21313 is on it's way to the customer and will be delivered in 1 hr",3));
        arrayList.add(new NotificationsModel("New Order !","You Received a new Order \n click here to accept",0));
        arrayList.add(new NotificationsModel("Order Cancelled !","Order has been cancelled as you failed to respond within 10 mins",1));
        arrayList.add(new NotificationsModel("Order Delivered !","Your Order #WR21311 has been delivered to the customer successfully !",2));
        arrayList.add(new NotificationsModel("Order in transit","Your Order #WR21313 is on it's way to the customer and will be delivered in 1 hr",3));
        arrayList.add(new NotificationsModel("New Order !","You Received a new Order \n click here to accept",0));
        arrayList.add(new NotificationsModel("Order Cancelled !","Order has been cancelled as you failed to respond within 10 mins",1));
        arrayList.add(new NotificationsModel("Order Delivered !","Your Order #WR21311 has been delivered to the customer successfully !",2));
        arrayList.add(new NotificationsModel("Order in transit","Your Order #WR21313 is on it's way to the customer and will be delivered in 1 hr",3));


        recyclerView.setLayoutManager(new LinearLayoutManager(VendorBusinessNotificationsActivity.this, LinearLayoutManager.VERTICAL, false));
        notificationsAdapter=new NotificationsAdapter(arrayList,VendorBusinessNotificationsActivity.this);
        recyclerView.setAdapter(notificationsAdapter);
    }
}
