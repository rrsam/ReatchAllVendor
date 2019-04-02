package com.reatchall.charan.reatchallVendor.Vendor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ListItemsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VendorProductListActivity extends AppCompatActivity {

    private static final String TAG = "VendorProductListActivi";
    ImageView backArrow,addItem;

    RecyclerView recyclerView;
    ArrayList<ListItemsModel> arrayList = new ArrayList<>();
    VendorMyProductsListAdapter vendorMyProductsListAdapter;
    String listID = null;
    BusinessDetails businessDashboard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_product_list);
        addItem=(ImageView)findViewById(R.id.addItem);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        listID=getIntent().getExtras().getString("listId");
        businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
        if(listID != null){
            Log.e(TAG, "onClick: NOT NULL" );

            if(listID.equalsIgnoreCase("abc")){
                Log.e(TAG, "onClick: ABC");

                new LoadAllBusinessProducts().execute();
            }else{
                Log.e(TAG, "onClick: "+listID );

                new LoadListProducts().execute();
            }
        }

        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendorProductListActivity.this,VendorAddItemsActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                if(listID != null){
                    Log.e(TAG, "onClick: NOT NULL" );
                    if(!listID.equalsIgnoreCase("abc")){
                       intent.putExtra("listId",listID);
                        Log.e(TAG, "onClick: "+listID );
                    }
                }

                startActivity(intent);
            }
        });

    }


    public class LoadAllBusinessProducts extends  AsyncTask<String,String,String>{
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(VendorProductListActivity.this);
            dialog.setMessage("please wait.");
            dialog.show();
        }


        Response response;
        @Override
        protected String doInBackground(String... strings) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://13.127.169.96:3000/vendor/get-items-of-business/"+businessDashboard.getBusinessId())
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .addHeader("postman-token", "fd918bac-0600-1e29-03aa-5299c3f07c15")
                    .build();

            try {
                response = client.newCall(request).execute();

                if(response != null){
                    if(response.isSuccessful()){
                        Log.e(TAG, "doInBackground: ");

                        Log.e(TAG, "onPostExecute: "+response.body() );
                        String jsonData = null;
                        try {
                            jsonData = response.body().string();
                            JSONObject Jobject = new JSONObject(jsonData);
                            JSONArray msg = Jobject.getJSONArray("msg");

                            arrayList.clear();

                            for(int i=0;i<msg.length();i++){

                                JSONObject list = msg.getJSONObject(i);
                                JSONObject itemData = list.getJSONObject("item_data");

                                arrayList.add(new ListItemsModel("",itemData.getString("item_name")));

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(arrayList.size() != 0){
                setupRecyclerView();
            }else{
                Toast.makeText(VendorProductListActivity.this,"Please try again!",Toast.LENGTH_LONG).show();
            }
        }
    }


    public class LoadListProducts extends AsyncTask<String,String,String>{
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(VendorProductListActivity.this);
            dialog.setMessage("please wait.");
            dialog.show();
        }



        Response response;
        @Override
        protected String doInBackground(String... strings) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://13.127.169.96:3000/vendor/get-items-of-list/"+listID)
                    .get()
                    .build();
            try {
                response = client.newCall(request).execute();

                if(response != null){
                    if(response.isSuccessful()){
                        Log.e(TAG, "doInBackground: ");

                        Log.e(TAG, "onPostExecute: "+response.body() );
                        String jsonData = null;
                        try {
                            jsonData = response.body().string();
                            JSONObject Jobject = new JSONObject(jsonData);
                            JSONArray msg = Jobject.getJSONArray("msg");

                            arrayList.clear();

                            for(int i=0;i<msg.length();i++){

                                JSONObject list = msg.getJSONObject(i);
                                JSONObject itemData = list.getJSONObject("item_data");

                                arrayList.add(new ListItemsModel("",itemData.getString("item_name")));

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if(arrayList.size() != 0){
                setupRecyclerView();
            }else{
                Toast.makeText(VendorProductListActivity.this,"No items found!",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setupRecyclerView(){
        recyclerView=(RecyclerView)findViewById(R.id.myProductsList_rcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(VendorProductListActivity.this,4,LinearLayoutManager.VERTICAL,false));

        vendorMyProductsListAdapter = new VendorMyProductsListAdapter(arrayList,getApplicationContext());
        recyclerView.setAdapter(vendorMyProductsListAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(listID != null){
            Log.e(TAG, "onClick: NOT NULL" );

            if(listID.equalsIgnoreCase("abc")){
                Log.e(TAG, "onClick: ABC");

                new LoadAllBusinessProducts().execute();
            }else{
                Log.e(TAG, "onClick: "+listID );

                new LoadListProducts().execute();
            }
        }
    }
}
