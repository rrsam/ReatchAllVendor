package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.ConfirmationDialog;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.BusinessAllProductsAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.AllProducts;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.NewProduct;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.ILoadProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class BusinessAllProductsActivity extends AppCompatActivity implements ILoadProducts {

    private static final String TAG = "BusinessAllProductsActi";
    ImageView backArrow;
    FontTextView titleToolbar;

    RecyclerView recyclerView;
    ArrayList<AllProducts> arrayList = new ArrayList<>();
    ArrayList<NewProduct> productsArrayList = new ArrayList<>();
    BusinessAllProductsAdapter businessAllProductsAdapter;
    BusinessDetails businessDashboard = null;
    String updateListNameString=null;

    int toggle;
    FontTextView subTitle;
    boolean listBoolean=false;

    ReatchAll helper = ReatchAll.getInstance();

    CustomProgressDialog customProgressDialog;
    LinearLayout saveControllerDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_all_products);

        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titleToolbar = (FontTextView) findViewById(R.id.title_toolbar);
        subTitle=(FontTextView)findViewById(R.id.subTitle);
        businessDashboard = getIntent().getExtras().getParcelable("businessDetails");
        toggle=getIntent().getExtras().getInt("toggle");

        if(toggle==2){
            subTitle.setText("My Lists");
            listBoolean=true;

        }else{
            listBoolean=false;
            subTitle.setText("My Products");
        }

        if (businessDashboard != null) {
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }

        if(businessDashboard != null){
            Log.e(TAG, "onCreate:ID "+businessDashboard.getBusinessId() +"NAME"+businessDashboard.getBusinessName());
        }

        customProgressDialog= new CustomProgressDialog(BusinessAllProductsActivity.this);
        customProgressDialog.showDialog();
        loadAllProducts();
    }


    private void loadAllProducts(){
        String url = Constants.BASE_URL+"vendor/get-items-by-businessid/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray msg = null;
                try {
                    msg = response.getJSONArray("msg");

                    productsArrayList.clear();

                    for(int i =0;i<msg.length();i++){
                        JSONObject list = msg.getJSONObject(i);
                        if(list.has("list_id")){
                            if(list.getJSONArray("images").length()>0){
                                if(list.getBoolean("single")){
                                    productsArrayList.add(new NewProduct(list.getString("_id"),list.getString("name"),list.getString("list_id"),
                                            list.getString("vendor_id"),list.getString("business_id"),list.getString("description"),
                                            list.getString("brand"),list.getString("units"),list.getInt("quantity"),
                                            list.getDouble("price"),list.getBoolean("single"),0,list.getJSONArray("images").getJSONObject(0).getString("url")));
                                }else{
                                    productsArrayList.add(new NewProduct(list.getString("_id"),list.getString("name"),list.getString("list_id"),
                                            list.getString("vendor_id"),list.getString("business_id"),list.getString("description"),
                                            list.getString("brand"),list.getString("units"),list.getInt("quantity"),
                                            list.getDouble("price"),list.getBoolean("single"),list.getInt("pack"),list.getJSONArray("images").getJSONObject(0).getString("url")));
                                }
                            }else{
                                if(list.getBoolean("single")){
                                    productsArrayList.add(new NewProduct(list.getString("_id"),list.getString("name"),list.getString("list_id"),
                                            list.getString("vendor_id"),list.getString("business_id"),list.getString("description"),
                                            list.getString("brand"),list.getString("units"),list.getInt("quantity"),
                                            list.getDouble("price"),list.getBoolean("single"),0,""));
                                }else{
                                    productsArrayList.add(new NewProduct(list.getString("_id"),list.getString("name"),list.getString("list_id"),
                                            list.getString("vendor_id"),list.getString("business_id"),list.getString("description"),
                                            list.getString("brand"),list.getString("units"),list.getInt("quantity"),
                                            list.getDouble("price"),list.getBoolean("single"),list.getInt("pack"),""));
                                }
                            }
                        }else{
                            if(list.getJSONArray("images").length()>0){
                                if(list.getBoolean("single")){
                                    productsArrayList.add(new NewProduct(list.getString("_id"),list.getString("name"),"",
                                            list.getString("vendor_id"),list.getString("business_id"),list.getString("description"),
                                            list.getString("brand"),list.getString("units"),list.getInt("quantity"),
                                            list.getDouble("price"),list.getBoolean("single"),0,list.getJSONArray("images").getJSONObject(0).getString("url")));
                                }else{
                                    productsArrayList.add(new NewProduct(list.getString("_id"),list.getString("name"),"",
                                            list.getString("vendor_id"),list.getString("business_id"),list.getString("description"),
                                            list.getString("brand"),list.getString("units"),list.getInt("quantity"),
                                            list.getDouble("price"),list.getBoolean("single"),list.getInt("pack"),list.getJSONArray("images").getJSONObject(0).getString("url")));
                                }
                            }else{
                                if(list.getBoolean("single")){
                                    productsArrayList.add(new NewProduct(list.getString("_id"),list.getString("name"),"",
                                            list.getString("vendor_id"),list.getString("business_id"),list.getString("description"),
                                            list.getString("brand"),list.getString("units"),list.getInt("quantity"),
                                            list.getDouble("price"),list.getBoolean("single"),0,""));
                                }else{
                                    productsArrayList.add(new NewProduct(list.getString("_id"),list.getString("name"),"",
                                            list.getString("vendor_id"),list.getString("business_id"),list.getString("description"),
                                            list.getString("brand"),list.getString("units"),list.getInt("quantity"),
                                            list.getDouble("price"),list.getBoolean("single"),list.getInt("pack"),""));
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                setUpRecyclerView();
                customProgressDialog.hideDialog();


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BusinessAllProductsActivity.this,"Couldn't fetch the data",Toast.LENGTH_LONG).show();
            }
        });

        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,businessDashboard.getBusinessId()+"LISTS");
    }


    private void setUpRecyclerView(){
        recyclerView=(RecyclerView)findViewById(R.id.myProducts_rcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(BusinessAllProductsActivity.this,LinearLayoutManager.VERTICAL, false));
        businessAllProductsAdapter=new BusinessAllProductsAdapter(BusinessAllProductsActivity.this,productsArrayList,BusinessAllProductsActivity.this);
        recyclerView.setAdapter(businessAllProductsAdapter);
    }

    @Override
    public void saveAndLoad(String productName, NewProduct allProducts) {
        loadAllProducts();
        Intent intent = new Intent(BusinessAllProductsActivity.this,VendorEditItemActivity.class);
        intent.putExtra("productDetails",allProducts);
        intent.putExtra("businessDetails",businessDashboard);
        startActivity(intent);
    }

    @Override
    public void deleteAndLoad(int i) {
        deleteOfferDialog(i);
    }

    private void deleteOfferDialog(int position){
        ConfirmationDialog mAlert = new ConfirmationDialog(BusinessAllProductsActivity.this);
        mAlert.setTitle("Delete List");
        mAlert.setIcon(android.R.drawable.ic_dialog_alert);
        mAlert.setMessage("Are you sure you want to delete this list? You can't undo this action.");
        mAlert.setPositveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
                deleteProduct(position);
                customProgressDialog.showDialog();

                //Do want you want
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
        String url = Constants.BASE_URL+"vendor/delete-item/"+arrayList.get(positon).getProductId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(BusinessAllProductsActivity.this,"Success",Toast.LENGTH_LONG).show();
                        updateList(positon);
                    }else{
                        Toast.makeText(BusinessAllProductsActivity.this,"Couldn't delete list. Please Try again!",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(BusinessAllProductsActivity.this,"Couldn't delete list. Please Try again!",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"DELETE_LIST");
    }

    private void updateList(int i){
        arrayList.remove(i);
        businessAllProductsAdapter.notifyItemRemoved(i);
    }
}
