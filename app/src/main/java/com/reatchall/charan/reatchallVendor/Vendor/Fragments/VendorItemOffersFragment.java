package com.reatchall.charan.reatchallVendor.Vendor.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.ItemOffersAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ItemOffer;
import com.reatchall.charan.reatchallVendor.Vendor.Models.NewProduct;
import com.reatchall.charan.reatchallVendor.Vendor.Offers.VendorAddItemOfferActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Offers.VendorEditItemOfferActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Offers.VendorOfferZoneNewActivity;
import com.reatchall.charan.reatchallVendor.Vendor.VendorOfferZoneActivity;
import com.reatchall.charan.reatchallVendor.Vendor.VendorViewOffersActivity;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IOfferEditDelete;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class VendorItemOffersFragment extends Fragment implements IOfferEditDelete {

    private static final String TAG = "VendorItemOffersFragmen";
    Context context;
    ReatchAll helper = ReatchAll.getInstance();
    View view;
    CustomProgressDialog customProgressDialog;
    PrefManager prefManager;
    RecyclerView offersRcv;
    LinearLayout addNewOfferLayout;
    BusinessDetails businessDashboard;
    ArrayList<ItemOffer> itemOfferArrayList;
    ItemOffersAdapter itemOffersAdapter;


    public VendorItemOffersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      view =  inflater.inflate(R.layout.fragment_vendor_item_offers, container, false);
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
        vendorOfferZoneNewActivity.highlightTab(1);
        customProgressDialog = new CustomProgressDialog(context);
        businessDashboard = getArguments().getParcelable("businessDetails");
        prefManager = new PrefManager(context);
        offersRcv = (RecyclerView)view.findViewById(R.id.offersRcv);
        addNewOfferLayout = (LinearLayout) view.findViewById(R.id.addNewOfferLayout);

        addNewOfferLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,VendorAddItemOfferActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                startActivity(intent);
            }
        });

        offersRcv.setHasFixedSize(true);
        offersRcv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false));
        itemOfferArrayList = new ArrayList<>();
        itemOffersAdapter = new ItemOffersAdapter(context,itemOfferArrayList,getActivity().getFragmentManager(),VendorItemOffersFragment.this);
        offersRcv.setAdapter(itemOffersAdapter);
        customProgressDialog.showDialog();
        getItemOffers();
    }

    private void getItemOffers(){
        String url = Constants.BASE_URL+"vendor/get-all-itemoffers-by-busId/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: response"+ response.toString());
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        JSONArray msg = response.getJSONArray("msg");
                        itemOfferArrayList = new ArrayList<>();
                        for(int i=0;i<msg.length();i++){
                            JSONObject itemOffer = msg.getJSONObject(i);
                            if(itemOffer.getJSONArray("items").length()>0){
                                ArrayList<String> itemIds = new ArrayList<>();
                                for(int j=0;j<itemOffer.getJSONArray("item").length();j++){
                                    itemIds.add(itemOffer.getJSONArray("item").getString(j));
                                }
                                ArrayList<NewProduct> productsArrayList = new ArrayList<>();
                                for(int j=0;j<itemOffer.getJSONArray("items").length();j++){
                                    JSONObject list = itemOffer.getJSONArray("items").getJSONObject(j);
                                    if(list.has("list_id")){
                                        if(list.getJSONArray("images").length()>0){
                                            productsArrayList.add(new NewProduct(list.getString("_id"),list.getString("name"),list.getString("list_id"),
                                                    list.getString("vendor_id"),list.getString("business_id"),list.getString("description"),
                                                    list.getString("brand"),list.getString("units"),list.getInt("quantity"),
                                                    list.getDouble("price"),true,0,list.getJSONArray("images").getJSONObject(0).getString("url")));

                                        }else{
                                            productsArrayList.add(new NewProduct(list.getString("_id"),list.getString("name"),list.getString("list_id"),
                                                    list.getString("vendor_id"),list.getString("business_id"),list.getString("description"),
                                                    list.getString("brand"),list.getString("units"),list.getInt("quantity"),
                                                    list.getDouble("price"),true,0,""));

                                        }
                                    }else{
                                        if(list.getJSONArray("images").length()>0){
                                            productsArrayList.add(new NewProduct(list.getString("_id"),list.getString("name"),"",
                                                    list.getString("vendor_id"),list.getString("business_id"),list.getString("description"),
                                                    list.getString("brand"),list.getString("units"),list.getInt("quantity"),
                                                    list.getDouble("price"),true,0,list.getJSONArray("images").getJSONObject(0).getString("url")));

                                        }else{
                                            productsArrayList.add(new NewProduct(list.getString("_id"),list.getString("name"),"",
                                                    list.getString("vendor_id"),list.getString("business_id"),list.getString("description"),
                                                    list.getString("brand"),list.getString("units"),list.getInt("quantity"),
                                                    list.getDouble("price"),true,0,""));

                                        }
                                    }
                                }

                                itemOfferArrayList.add(new ItemOffer(itemOffer.getString("_id"),itemOffer.getString("vendor_id"),
                                        itemOffer.getString("business_id"),itemOffer.getString("offer_type"),itemOffer.getBoolean("offer"),
                                        itemOffer.getBoolean("rupees"),itemOffer.getInt("discount"),itemOffer.getInt("buy_value"),itemOffer.getInt("get_value"),
                                        itemOffer.getString("start_date"),itemOffer.getString("end_date"),itemOffer.getBoolean("delete"),
                                        itemOffer.getBoolean("hide"),itemIds,productsArrayList));
                            }
                        }
                        itemOffersAdapter = new ItemOffersAdapter(context,itemOfferArrayList,getActivity().getFragmentManager(),VendorItemOffersFragment.this);
                        offersRcv.setAdapter(itemOffersAdapter);
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
        Intent intent = new Intent(context,VendorEditItemOfferActivity.class);
        intent.putExtra("businessDetails",businessDashboard);
        intent.putExtra(StringConstants.ITEM_OFFER,itemOfferArrayList.get(position));
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
        String url = Constants.BASE_URL+"vendor/remove-offer-by-offerid";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("offer_id",itemOfferArrayList.get(pos).getOfferId());
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
                        itemOfferArrayList.remove(pos);
                        itemOffersAdapter = new ItemOffersAdapter(context,itemOfferArrayList,getActivity().getFragmentManager(),VendorItemOffersFragment.this);
                        offersRcv.setAdapter(itemOffersAdapter);
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
