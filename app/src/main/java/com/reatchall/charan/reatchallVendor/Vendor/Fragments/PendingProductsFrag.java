package com.reatchall.charan.reatchallVendor.Vendor.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.ConfirmationDialog;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.VendorAllProductsAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.BusinessProductDetailsActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.NewProduct;
import com.reatchall.charan.reatchallVendor.Vendor.VendorEditItemActivity;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.ILoadProducts;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.ILoadProductsList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class PendingProductsFrag extends Fragment implements ILoadProducts, VendorAllProductsAdapter.OnItemClickListener {

    RecyclerView rvProducts;
    private static final String BUSSINESS_ID = "param1";

    private BusinessDetails businessDetails;
    ArrayList<NewProduct> productsArrayList;
    ReatchAll helper = ReatchAll.getInstance();
    private VendorAllProductsAdapter vendorAllProductsAdapter;


    public PendingProductsFrag() {
        // Required empty public constructor
    }

    public static PendingProductsFrag newInstance(BusinessDetails businessDetails) {
        PendingProductsFrag fragment = new PendingProductsFrag();
        Bundle args = new Bundle();
        args.putParcelable(BUSSINESS_ID,businessDetails);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        businessDetails = getArguments().getParcelable(BUSSINESS_ID);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.frag_pending_products, container, false);
        rvProducts = view.findViewById(R.id.rv_pending_prod);

        rvProducts.setHasFixedSize(true);
        rvProducts.setNestedScrollingEnabled(false);
        rvProducts.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL, false));
        loadPendingProducts();
        return view;
    }

    private void loadPendingProducts(){
        String url = Constants.BASE_URL+"vendor/get-pending-items-by-business_id/"+businessDetails.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray msg = null;
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        msg = response.getJSONArray("msg");
                        productsArrayList = new ArrayList<>();

                        for (int i = 0; i < msg.length(); i++) {
                            JSONObject list = msg.getJSONObject(i);
                            if (list.has("list_id")) {
                                if (list.getJSONArray("images").length() > 0) {
                                    productsArrayList.add(new NewProduct(list.getString("_id"), list.getString("name"), list.getString("list_id"),
                                            list.getString("vendor_id"), list.getString("business_id"), list.getString("description"),
                                            list.getString("brand"), list.getString("units"), list.getInt("quantity"),
                                            list.getDouble("price"), true, 0, list.getJSONArray("images").getJSONObject(0).getString("url")));

                                } else {
                                    productsArrayList.add(new NewProduct(list.getString("_id"), list.getString("name"), list.getString("list_id"),
                                            list.getString("vendor_id"), list.getString("business_id"), list.getString("description"),
                                            list.getString("brand"), list.getString("units"), list.getInt("quantity"),
                                            list.getDouble("price"), true, 0, ""));

                                }
                            } else {
                                if (list.getJSONArray("images").length() > 0) {
                                    productsArrayList.add(new NewProduct(list.getString("_id"), list.getString("name"), "",
                                            list.getString("vendor_id"), list.getString("business_id"), list.getString("description"),
                                            list.getString("brand"), list.getString("units"), list.getInt("quantity"),
                                            list.getDouble("price"), true, 0, list.getJSONArray("images").getJSONObject(0).getString("url")));

                                } else {
                                    productsArrayList.add(new NewProduct(list.getString("_id"), list.getString("name"), "",
                                            list.getString("vendor_id"), list.getString("business_id"), list.getString("description"),
                                            list.getString("brand"), list.getString("units"), list.getInt("quantity"),
                                            list.getDouble("price"), true, 0, ""));

                                }
                            }
                        }
                        vendorAllProductsAdapter = new VendorAllProductsAdapter(getActivity(),productsArrayList,PendingProductsFrag.this,PendingProductsFrag.this);
                        rvProducts.setAdapter(vendorAllProductsAdapter);
                    }else{

                        Toast.makeText(getActivity(), "No List", Toast.LENGTH_SHORT).show();
                    }

                } catch(JSONException e){
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Couldn't fetch the data",Toast.LENGTH_LONG).show();
            }
        });

        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,businessDetails.getBusinessId()+"LISTS");
    }

    @Override
    public void saveAndLoad(String productName, NewProduct allProducts) {
        Intent intent = new Intent(getActivity(), VendorEditItemActivity.class);
        intent.putExtra("productDetails",allProducts);
        intent.putExtra("businessDetails",businessDetails);
        startActivity(intent);
    }

    @Override
    public void deleteAndLoad(int i) {
        deleteOfferDialog(i);
    }

    private void deleteOfferDialog(int position){
        ConfirmationDialog mAlert = new ConfirmationDialog(getActivity());
        mAlert.setTitle("Delete List");
        mAlert.setIcon(android.R.drawable.ic_dialog_alert);
        mAlert.setMessage("Are you sure you want to delete this list? You can't undo this action.");
        mAlert.setPositveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
                deleteProduct(position);

            }
        });

        mAlert.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
                //Do want you want
            }
        });

        mAlert.show();
    }

    private void deleteProduct(int positon){
        String url = Constants.BASE_URL+"vendor/delete-item/"+productsArrayList.get(positon).getItemId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(getActivity(),"Success",Toast.LENGTH_LONG).show();
                        updateList(positon);
                    }else{
                        Toast.makeText(getActivity(),"Couldn't delete list. Please Try again!",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Couldn't delete list. Please Try again!",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"DELETE_LIST");
    }

    private void updateList(int i){
        productsArrayList.remove(i);
        vendorAllProductsAdapter.notifyItemRemoved(i);
    }

    @Override
    public void onItemClicked(NewProduct mProduct) {
        Intent intent = new Intent(getActivity(), BusinessProductDetailsActivity.class);
        intent.putExtra("productDetails",mProduct);
        intent.putExtra("businessDetails",businessDetails);
        startActivity(intent);
    }
}
