package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.OfferDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorViewOffersActivity extends AppCompatActivity {

    private static final String TAG = "VendorViewOffersActivit";

    ImageView backArrow;


    RecyclerView recyclerView;
    ArrayList<OfferDetails> arrayList;
    VendorViewOffersAdapter vendorViewOffersAdapter;

    BusinessDetails businessDashboard;
    FontTextView titleToolbar;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    Context context;

    PrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_view_offers);
        context = VendorViewOffersActivity.this;
        prefManager = new PrefManager(context);
        customProgressDialog = new CustomProgressDialog(VendorViewOffersActivity.this);
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
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }
        recyclerView = (RecyclerView)findViewById(R.id.viewOffersRcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(VendorViewOffersActivity.this, LinearLayoutManager.VERTICAL, false));


        arrayList = new ArrayList<>();
        customProgressDialog.showDialog();
        getAllOffers();



    }

    public void editOffer(OfferDetails offerId){
        Intent intent = new Intent(VendorViewOffersActivity.this,VendorEditOfferActivity.class);
        intent.putExtra("businessDetails",businessDashboard);
        intent.putExtra("offerDetails",offerId);
        startActivity(intent);
    }

    public void deleteOffer(String offerId,int position){
        String url = Constants.BASE_URL+"vendor/delete-offer";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
            jsonObject.put("_id",offerId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Deleted Successfully!",Toast.LENGTH_LONG).show();
                        arrayList.remove(position);
                        vendorViewOffersAdapter.notifyItemRemoved(position);
                    }else{
                        Toast.makeText(context,"Please try again!",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Please try again!",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"DELETE_OFFER"+offerId);
    }



    private void getAllOffers(){
        String url = Constants.BASE_URL+"vendor/get-offers-by-businessId/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        JSONArray parent = response.getJSONArray("msg");
                        if(parent.length()>0){
                            arrayList = new ArrayList<>();
                            for(int i=0 ; i<parent.length();i++){
                                JSONObject child = parent.getJSONObject(i);
                                ArrayList<String> itemsArray = new ArrayList<>();
                                ArrayList<String> itemIdsArray = new ArrayList<>();
                                String startTime="",endTime="";
                                startTime = formatTime(child.getString("start_date"),child.getJSONObject("start_time").getInt("hour"),child.getJSONObject("start_time").getInt("minute"));
                                endTime = formatTime(child.getString("end_date"),child.getJSONObject("end_time").getInt("hour"),child.getJSONObject("end_time").getInt("minute"));

                                JSONArray itemsJsonArray = child.getJSONArray("items");
                                if(itemsJsonArray.length()>0){
                                    for(int j=0;j<itemsJsonArray.length();j++){
                                        itemsArray.add(itemsJsonArray.getJSONObject(j).getString("item_name"));
                                        itemIdsArray.add(itemsJsonArray.getJSONObject(j).getString("_id"));
                                    }
                                }

                                if(child.has("min_amount")){
                                    if(child.getString("min_amount")!=null){
                                        arrayList.add(new OfferDetails(child.getString("_id"),child.getString("select_offer"),
                                                child.getString("offer_type"),child.getBoolean("discount_percentage"),
                                                child.getString("discount"),startTime,endTime,
                                                itemsArray,child.getBoolean("hide"),child.getString("min_amount"),itemIdsArray));
                                    }
                                }else{
                                    arrayList.add(new OfferDetails(child.getString("_id"),child.getString("select_offer"),
                                            child.getString("offer_type"),child.getBoolean("discount_percentage"),
                                            child.getString("discount"),startTime,endTime,
                                            itemsArray,child.getBoolean("hide"),null,itemIdsArray));
                                }

                            }

                            vendorViewOffersAdapter=new VendorViewOffersAdapter(VendorViewOffersActivity.this,arrayList,VendorViewOffersActivity.this.getFragmentManager());
                            recyclerView.setAdapter(vendorViewOffersAdapter);

                        }else{
                            Toast.makeText(VendorViewOffersActivity.this,"No Offers",Toast.LENGTH_LONG).show();

                        }
                    }else{
                        Toast.makeText(VendorViewOffersActivity.this,"Please Try Again",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e(TAG, "onResponse: "+e.getMessage() );
                    Toast.makeText(VendorViewOffersActivity.this,"Please Try Again",Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(VendorViewOffersActivity.this,"Please Try Again",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"GET_OFFERS");
    }

    private String formatTime(String date, int hour,int minutes){
        String result ="";
        result = date.substring(0,10);
        result = result +" , " + String.format("%02d", hour) + ":" + String.format("%02d", minutes);
        return  result;
    }
}
