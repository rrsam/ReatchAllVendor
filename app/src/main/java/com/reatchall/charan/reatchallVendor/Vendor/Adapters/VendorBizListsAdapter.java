package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.reatchall.charan.reatchallVendor.Vendor.VendorMyProductsListNewAdapter;
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

public class VendorBizListsAdapter extends RecyclerView.Adapter<VendorBizListsAdapter.VendorBizListsViewHolder> {


    private static final String TAG = "VendorMyProductsAdapter";

    ArrayList<ListDetailsModel> arrayList;
    private Context context;
    ILoadProductsList iLoadProductsList;
    IMyProducts iMyProducts;

    ReatchAll helper;
    CustomProgressDialog customProgressDialog;


    public VendorBizListsAdapter(ArrayList<ListDetailsModel> arrayList, Context context, ILoadProductsList iLoadProductsList, IMyProducts iMyProducts) {
        this.arrayList = arrayList;
        this.context = context;
        this.iLoadProductsList=iLoadProductsList;
        this.iMyProducts=iMyProducts;

        helper = ReatchAll.getInstance();
        customProgressDialog=new CustomProgressDialog(context);
    }

    @Override
    public VendorBizListsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vendor_view_lists, parent, false);

        return new VendorBizListsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VendorBizListsViewHolder holder, final int position) {


        if(!arrayList.get(position).isList()){
            holder.productName.setText(arrayList.get(position).getListName());
            holder.productName.setFocusable(false);
            holder.productName.setFocusableInTouchMode(false);
        }else{
            holder.productName.setText(arrayList.get(position).getListName());
            holder.productName.setFocusable(false);
            holder.productName.setFocusableInTouchMode(false);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public class VendorBizListsViewHolder extends RecyclerView.ViewHolder {

        FontEditText productName;
        public VendorBizListsViewHolder(View itemView) {
            super(itemView);
            productName=(FontEditText)itemView.findViewById(R.id.productName);
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
