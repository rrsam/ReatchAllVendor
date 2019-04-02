package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ListDetailsModel;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ListItemsModel;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.ILoadProductsList;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IMyProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontEditText;

/**
 * Created by NaNi on 19/02/18.
 */

public class VendorMyProductsAdapter extends RecyclerView.Adapter<VendorMyProductsAdapter.VendorMyProductsViewHolder> {


    private static final String TAG = "VendorMyProductsAdapter";

    ArrayList<ListDetailsModel> arrayList;
    private Context context;
    ILoadProductsList iLoadProductsList;
    IMyProducts iMyProducts;

    ReatchAll helper;
    CustomProgressDialog customProgressDialog;


    public VendorMyProductsAdapter(ArrayList<ListDetailsModel> arrayList, Context context, ILoadProductsList iLoadProductsList, IMyProducts iMyProducts) {
        this.arrayList = arrayList;
        this.context = context;
        this.iLoadProductsList=iLoadProductsList;
        this.iMyProducts=iMyProducts;

        helper = ReatchAll.getInstance();
        customProgressDialog=new CustomProgressDialog(context);
    }

    @Override
    public VendorMyProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_item_my_products, parent, false);

        return new VendorMyProductsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VendorMyProductsViewHolder holder, final int position) {


        if(!arrayList.get(position).isList()){
            holder.productName.setText(arrayList.get(position).getListName());
            holder.productName.setFocusable(false);
            holder.productName.setFocusableInTouchMode(false);
            holder.editProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.productName.setFocusable(true);
                    holder.productName.setFocusableInTouchMode(true);
                    holder.productName.requestFocus();
                    holder.productName.setSelection(holder.productName.getText().length());
                    holder.editProduct.setVisibility(View.GONE);
                    holder.saveProductName.setVisibility(View.VISIBLE);
                }
            });

            holder.saveProductName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.productName.setFocusableInTouchMode(false);
                    holder.productName.setFocusable(false);
                    holder.saveProductName.setVisibility(View.GONE);
                    holder.editProduct.setVisibility(View.VISIBLE);
                    Toast.makeText(context,"List Name Saved",Toast.LENGTH_LONG).show();
                    iLoadProductsList.saveAndLoad(holder.productName.getText().toString(),arrayList.get(position));
                }
            });

            holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iLoadProductsList.deleteAndLoad(position);
                }
            });

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // iMyProducts.submitBusinessDetails(arrayList.get(position).getListID());
                }
            });

            holder.listProductsRcv.setHasFixedSize(true);
            holder.listProductsRcv.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));

            ArrayList<ListItemsModel> arrayListNew = new ArrayList<>();
            VendorMyProductsListNewAdapter vendorMyProductsListNewAdapter = new VendorMyProductsListNewAdapter(arrayListNew,context);
            holder.listProductsRcv.setAdapter(vendorMyProductsListNewAdapter);

            holder.toggleIVOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.toggleIVClose.setVisibility(View.VISIBLE);
                    holder.listProductsRcv.setVisibility(View.VISIBLE);
                    holder.toggleIVOpen.setVisibility(View.GONE);
                }
            });

            holder.toggleIVClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.toggleIVClose.setVisibility(View.GONE);
                    holder.listProductsRcv.setVisibility(View.GONE);
                    holder.toggleIVOpen.setVisibility(View.VISIBLE);
                }
            });

            holder.toggleIVOpen.setVisibility(View.VISIBLE);
            holder.toggleIVClose.setVisibility(View.GONE);
            holder.listProductsRcv.setVisibility(View.GONE);
            getListProducts(arrayList.get(position).getListID(),arrayList.get(position).getListName(),holder.listProductsRcv,vendorMyProductsListNewAdapter,arrayListNew);

        }else{
            holder.productName.setText(arrayList.get(position).getListName());
            holder.productName.setFocusable(false);
            holder.productName.setFocusableInTouchMode(false);
            holder.editProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*holder.productName.setFocusable(true);
                    holder.productName.setFocusableInTouchMode(true);
                    holder.productName.requestFocus();
                    holder.productName.setSelection(holder.productName.getText().length());
                    holder.editProduct.setVisibility(View.GONE);
                    holder.saveProductName.setVisibility(View.VISIBLE);*/
                    iLoadProductsList.saveAndLoad(holder.productName.getText().toString(),arrayList.get(position));
                }
            });

            holder.saveProductName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.productName.setFocusableInTouchMode(false);
                    holder.productName.setFocusable(false);
                    holder.saveProductName.setVisibility(View.GONE);
                    holder.editProduct.setVisibility(View.VISIBLE);
                    Toast.makeText(context,"List Name Saved",Toast.LENGTH_LONG).show();
                    iLoadProductsList.saveAndLoad(holder.productName.getText().toString(),arrayList.get(position));
                }
            });

            holder.deleteProduct.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iLoadProductsList.deleteAndLoad(position);
                }
            });

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // iMyProducts.submitBusinessDetails(arrayList.get(position).getListID());
                }
            });
            holder.listProductsRcv.setVisibility(View.GONE);
            holder.toggleIVOpen.setVisibility(View.GONE);
            holder.toggleIVClose.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class VendorMyProductsViewHolder extends RecyclerView.ViewHolder {

        FontEditText productName;
        ImageView editProduct,deleteProduct,saveProductName,toggleIVOpen,toggleIVClose;
        RelativeLayout relativeLayout;

        RecyclerView listProductsRcv;

        public VendorMyProductsViewHolder(View itemView) {
            super(itemView);
            productName=(FontEditText)itemView.findViewById(R.id.productName);
            editProduct=(ImageView)itemView.findViewById(R.id.editProduct);
            deleteProduct=(ImageView)itemView.findViewById(R.id.deleteProduct);
            toggleIVOpen=(ImageView)itemView.findViewById(R.id.toggleIVOpen);
            toggleIVClose=(ImageView)itemView.findViewById(R.id.toggleIVClose);
            saveProductName=(ImageView)itemView.findViewById(R.id.saveProductName);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.relativeLayout);
            listProductsRcv=(RecyclerView)itemView.findViewById(R.id.listItemsRcv);
        }
    }


    private void getListProducts(String listID, String listName,RecyclerView recyclerView, final VendorMyProductsListNewAdapter vendorMyProductsListNewAdapter,ArrayList<ListItemsModel> arrayList){
        String url =Constants.BASE_URL+ "vendor/get-items-by-listid/"+listID;
        Log.e(TAG, "getListProducts: " );
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    JSONArray msg = response.getJSONArray("msg");

                    if(msg!=null){
                        if(msg.length()!=0){
                            for(int i=0;i<msg.length();i++){

                                JSONObject list = msg.getJSONObject(i);

                                arrayList.add(new ListItemsModel("",list.getString("item_name")));

                                Log.e(TAG, "onResponse: "+"LIST NAME "+ listName+"PRO NAME "+list.getString("item_name"));

                            }

                            vendorMyProductsListNewAdapter.notifyDataSetChanged();

                        }else{
                            Log.e(TAG, "onResponse: ZERO "+listName );
                        }
                    }else{
                        Log.e(TAG, "onResponse: NULL"+listName);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,listID);


    }
}
