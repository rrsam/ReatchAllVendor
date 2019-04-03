package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.VendorCustomerAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.Customer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorCustomerActivity extends AppCompatActivity {

    ImageView backArrow;
    FontTextView titleToolbar;
    private static final String TAG = "VendorCustomerActivity";

    private FontTextView txtMyListCustomer,txtAddCustomer,txtSort,txtSelect,txtActions;

    private PrefManager prefManager;

    Context context;
    CustomProgressDialog customProgressDialog;
    ReatchAll helper = ReatchAll.getInstance();

    private ArrayList<Customer> mCustomerList;

    private RecyclerView rvCustomers;

    private VendorCustomerAdapter vendorCustomerAdapter;


    BusinessDetails businessDetails = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_customer);
        context = VendorCustomerActivity.this;
        customProgressDialog = new CustomProgressDialog(context);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        prefManager = new PrefManager(context);
        businessDetails = getIntent().getExtras().getParcelable("businessDetails");
        if(businessDetails != null){
            Log.e(TAG, "onCreate:ID "+businessDetails.getBusinessId() +"NAME"+businessDetails.getBusinessName());
        }

        init();
        rvCustomers.setHasFixedSize(true);
        rvCustomers.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));

    }

    private void init() {

        txtMyListCustomer = findViewById(R.id.mycustomer_list);
        txtAddCustomer = findViewById(R.id.add_customer);
        rvCustomers = findViewById(R.id.rv_customers);

        txtSort=(FontTextView)findViewById(R.id.txtSort);
        txtSelect=(FontTextView)findViewById(R.id.txtSelect);
        txtActions=(FontTextView)findViewById(R.id.txtActions);

        txtAddCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,VendorAddCustomerActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);

            }
        });

        txtSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(VendorCustomerActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.sort_menu, popup.getMenu());
                popup.show();
            }
        });

        txtActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(VendorCustomerActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.customer_menu, popup.getMenu());
                popup.show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchCustomers();
    }

    private void fetchCustomers() {

        String url = Constants.BASE_URL + "vendor/get-all-customers-by-vendorId/"+ prefManager.getVendorId(context);

        CustomJsonRequest customJsonRequest =  new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    boolean success = response.getBoolean("success");
                    if(success){

                        JSONArray jsonArray = response.getJSONArray("msg");
                        mCustomerList =new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String _id = object.getString("_id");
                            String gender = object.getString("gender");
                            String email = object.getString("email");
                            String mobile = object.getString("mobile");
                            String name = object.getString("name");
                            String vendor_id = object.getString("vendor_id");
                            String address = object.getString("address");
                            String created_date = object.getString("created_date");
                            String __v = object.getString("__v");
                            mCustomerList.add(new Customer(_id,gender,email,mobile,name,vendor_id,address,created_date,__v));

                        }
                        vendorCustomerAdapter = new VendorCustomerAdapter(mCustomerList,context);
                        rvCustomers.setAdapter(vendorCustomerAdapter);
                    }

                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"GET_CUSTOMERS");
    }
}
