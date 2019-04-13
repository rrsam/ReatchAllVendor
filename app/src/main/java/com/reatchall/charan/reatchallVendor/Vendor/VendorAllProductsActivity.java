package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.AllProductsPagerAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.BusinessAllProductsAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.NewProduct;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorAllProductsActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, BusinessAllProductsAdapter.OnItemClickListener, BusinessAllProductsAdapter.OncheckChangeListener {

    private static final String TAG = "VendorAllProductsActivity";
    ImageView backArrow;
    BusinessDetails businessDetails = null;
    CustomProgressDialog customProgressDialog;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private AllProductsPagerAdapter mPagerAdapter;
    private ConstraintLayout clManageLayout;

    FontTextView txtSort,txtSelect,txtActions;

    ReatchAll helper = ReatchAll.getInstance();

    ArrayList<String> itemsIdList = new ArrayList<>();
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_all_products);
        mContext = VendorAllProductsActivity.this;
        customProgressDialog=new CustomProgressDialog(VendorAllProductsActivity.this);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        businessDetails = getIntent().getExtras().getParcelable("businessDetails");
        tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        clManageLayout = (ConstraintLayout) findViewById(R.id.cl_products_manage);

        mPagerAdapter = new AllProductsPagerAdapter(this,getSupportFragmentManager(),businessDetails);
        viewPager.setAdapter(mPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        clManageLayout.setVisibility(View.GONE);
                        break;

                    case 1:
                        clManageLayout.setVisibility(View.GONE);
                        break;
                    case 2:
                        clManageLayout.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

                txtSort=(FontTextView)findViewById(R.id.txtSort);
        txtSelect=(FontTextView)findViewById(R.id.txtSelect);
        txtActions=(FontTextView)findViewById(R.id.txtActions);
        txtSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(VendorAllProductsActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.sort_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(VendorAllProductsActivity.this);
                popup.show();
            }
        });

        txtActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(VendorAllProductsActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.products_action_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(VendorAllProductsActivity.this);
                popup.show();
            }
        });

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_menu_all:

                return true;
            case R.id.sort_menu_active:
                viewPager.setCurrentItem(0);
                return true;
            case R.id.sort_menu_inactive:

                return true;
            case R.id.sort_menu_pending:
                viewPager.setCurrentItem(1);
                return true;
            case R.id.products_menu_active:
                makeproductsactive();
                return true;
            case R.id.products_menu_deactive:
                makeproductsdeactive();
                return true;
            case R.id.products_menu_delete:
                makeproductsdelete();
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onItemClicked(NewProduct mProduct) {
        Intent intent = new Intent(VendorAllProductsActivity.this,BusinessProductDetailsActivity.class);
        intent.putExtra("productDetails",mProduct);
        intent.putExtra("businessDetails",businessDetails);
        startActivity(intent);

    }

    @Override
    public void onCheckedChangedListener(boolean isChecked, NewProduct mProduct) {
        if (isChecked)
            itemsIdList.add(mProduct.getItemId());
        else
            itemsIdList.remove(mProduct.getItemId());
    }

    private void makeproductsactive() {
        if(!itemsIdList.isEmpty()){
            submitActiveProductsToServer(itemsIdList);
        }else{
            Toast.makeText(this, "Select any Product...", Toast.LENGTH_SHORT).show();
        }

    }

    private void submitActiveProductsToServer(ArrayList<String> itemsIdList) {
        JSONObject object = new JSONObject();
        try {
            object.put("business_id",businessDetails.getBusinessId());
            object.put("item_id",new JSONArray(itemsIdList));
            String url = Constants.BASE_URL+"vendor/post-item-active";
            CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        boolean success = response.getBoolean("success");
                        if(success){
                            viewPager.setCurrentItem(0);
                        }else{
                            Toast.makeText(mContext, "No Changes Done", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(TAG, "onErrorResponse: " + error.toString() );
                }
            });
            customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
            helper.addToRequestQueue(customJsonRequest,businessDetails.getBusinessId()+"ACTIVE");

        } catch (JSONException e) {


        }




    }

    private void makeproductsdeactive() {
        if(!itemsIdList.isEmpty()) {
            JSONObject object = new JSONObject();
            try {
                object.put("business_id", businessDetails.getBusinessId());
                object.put("item_id", new JSONArray(itemsIdList));
                String url = Constants.BASE_URL + "vendor/post-item-inactive";
                CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                viewPager.setCurrentItem(1);
                            } else {
                                Toast.makeText(mContext, "No Changes Done", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error.toString());
                    }
                });
                customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
                helper.addToRequestQueue(customJsonRequest, businessDetails.getBusinessId() + "ACTIVE");

            } catch (JSONException e) {


            }
        }else{
            Toast.makeText(mContext, "Select any Product...", Toast.LENGTH_SHORT).show();
        }

    }

    private void makeproductsdelete() {
        if(!itemsIdList.isEmpty()) {
            JSONObject object = new JSONObject();
            try {
                object.put("business_id",businessDetails.getBusinessId());
                object.put("item_id",new JSONArray(itemsIdList));
                String url = Constants.BASE_URL+"vendor/delete-multiple-items";
                CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if(success){
                                viewPager.setCurrentItem(2);
                            }else{
                                Toast.makeText(mContext, "No Changes Done", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "onErrorResponse: " + error.toString() );
                    }
                });
                customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
                helper.addToRequestQueue(customJsonRequest,businessDetails.getBusinessId()+"ACTIVE");

            } catch (JSONException e) {


            }
        }else{
            Toast.makeText(mContext, "Select any Product...", Toast.LENGTH_SHORT).show();
        }
    }
}
