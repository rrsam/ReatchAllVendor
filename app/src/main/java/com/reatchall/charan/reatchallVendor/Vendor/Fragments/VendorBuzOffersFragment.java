package com.reatchall.charan.reatchallVendor.Vendor.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.ConfirmationDialog;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Utils.StringConstants;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.BusinessOffersAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.ItemOffersAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BuzOffers;
import com.reatchall.charan.reatchallVendor.Vendor.Offers.VendorAddBuzOfferActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Offers.VendorAddItemOfferActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Offers.VendorEditBuzOfferActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Offers.VendorOfferZoneNewActivity;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IOfferEditDelete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorBuzOffersFragment extends Fragment implements IOfferEditDelete {
    private static final String TAG = "VendorBuzOffersFragment";

    Context context;
    ReatchAll helper = ReatchAll.getInstance();
    View view;
    CustomProgressDialog customProgressDialog;
    PrefManager prefManager;
    RecyclerView offersRcv;
    LinearLayout addNewOfferLayout;
    BusinessDetails businessDashboard;
    ArrayList<BuzOffers> buzOffersArrayList;
    BusinessOffersAdapter businessOffersAdapter;

    public VendorBuzOffersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vendor_buz_offers, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


    @Override
    public void onResume() {
        super.onResume();
        context = getActivity();
        VendorOfferZoneNewActivity vendorOfferZoneNewActivity =(VendorOfferZoneNewActivity)context;
        vendorOfferZoneNewActivity.highlightTab(0);
        customProgressDialog = new CustomProgressDialog(context);
        businessDashboard = getArguments().getParcelable("businessDetails");
        prefManager = new PrefManager(context);
        offersRcv = (RecyclerView)view.findViewById(R.id.offersRcv);
        addNewOfferLayout = (LinearLayout) view.findViewById(R.id.addNewOfferLayout);

        addNewOfferLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,VendorAddBuzOfferActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                startActivity(intent);
            }
        });

        offersRcv.setHasFixedSize(true);
        offersRcv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        buzOffersArrayList = new ArrayList<>();
        businessOffersAdapter = new BusinessOffersAdapter(context,buzOffersArrayList,VendorBuzOffersFragment.this);
        offersRcv.setAdapter(businessOffersAdapter);
        customProgressDialog.showDialog();
        getBuzOffers();
    }

    private void getBuzOffers(){
        String url = Constants.BASE_URL+"vendor/get-all-businessoffers-by-busId/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        JSONArray msg = response.getJSONArray("msg");
                        buzOffersArrayList = new ArrayList<>();
                        for(int i=0;i<msg.length();i++){
                            JSONObject buzOffer = msg.getJSONObject(i);
                            buzOffersArrayList.add(new BuzOffers(buzOffer.getString("_id"),buzOffer.getString("business_id"),
                                    buzOffer.getString("offer_type"),buzOffer.getBoolean("exclusive"),buzOffer.getBoolean("flat")
                                    ,buzOffer.getBoolean("rupees"),buzOffer.getInt("discount"),buzOffer.getInt("maxDiscount"),
                                    buzOffer.getInt("minAmount"), buzOffer.getString("startDate"), buzOffer.getString("endDate"),buzOffer.getBoolean("status")));
                        }
                        businessOffersAdapter = new BusinessOffersAdapter(context,buzOffersArrayList,VendorBuzOffersFragment.this);
                        offersRcv.setAdapter(businessOffersAdapter);
                    }else{
                        Toast.makeText(context,"No offers found",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Couldn't fetch data",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest);
    }

    @Override
    public void editOffer(int position) {
        Intent intent = new Intent(context,VendorEditBuzOfferActivity.class);
        intent.putExtra("businessDetails",businessDashboard);
        intent.putExtra(StringConstants.BUZ_OFFER,buzOffersArrayList.get(position));
        startActivity(intent);
    }

    @Override
    public void deleteOffer(int position) {
        ConfirmationDialog mAlert = new ConfirmationDialog(context);
        mAlert.setTitle("Delete Offer");
        mAlert.setIcon(android.R.drawable.ic_dialog_alert);
        mAlert.setMessage("Are you sure you want to delete this offer? You can't undo this action.");
        mAlert.setPositveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
                customProgressDialog.showDialog();
                deleteItemOffer(position);
            }
        });
        mAlert.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
            }
        });
        mAlert.show();
    }

    private void deleteItemOffer(int pos){
        String url = Constants.BASE_URL+"vendor/remove-business-offer";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("offer_id",buzOffersArrayList.get(pos).getOfferId());
            jsonObject.put("business_id",businessDashboard.getBusinessId());
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        buzOffersArrayList.remove(pos);
                        businessOffersAdapter = new BusinessOffersAdapter(context,buzOffersArrayList,VendorBuzOffersFragment.this);
                        offersRcv.setAdapter(businessOffersAdapter);
                        Toast.makeText(context,"Deleted successfully",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context,response.getString("msg"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Please try again",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest);
    }
}
