package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.ConfirmationDialog;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ListDetailsModel;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.ILoadProductsList;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IMyProducts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorMyProductsActivity extends AppCompatActivity implements ILoadProductsList,IMyProducts {

    private static final String TAG = "VendorMyProductsActivit";
    ImageView backArrow;
    FontTextView titleToolbar;

    RecyclerView recyclerView;
    ArrayList<ListDetailsModel> arrayList = new ArrayList<>();
    VendorMyProductsAdapter vendorMyProductsAdapter;
    BusinessDetails businessDashboard = null;
    String updateListNameString=null;

    int toggle;
    FontTextView subTitle;
    boolean listBoolean=true;

    ReatchAll helper = ReatchAll.getInstance();

    CustomProgressDialog customProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_my_products);

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

        customProgressDialog= new CustomProgressDialog(VendorMyProductsActivity.this);
        customProgressDialog.showDialog();
        loadListNew();
       // new LoadList().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadListNew();
    }

    @Override
    public void submitBusinessDetails(String listId) {
        Intent intent = new Intent(this,VendorProductListActivity.class);
        intent.putExtra("listId",listId);
        intent.putExtra("businessDetails",businessDashboard);
        startActivity(intent);
    }

    private void loadListNew(){
        String url = Constants.BASE_URL+"vendor_mobile/get-business-lists/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray msg = null;
                try {
                    msg = response.getJSONArray("msg");

                    arrayList.clear();

                    for(int i=0;i<msg.length();i++){

                        JSONObject list = msg.getJSONObject(i);
                        if(list.has("image")){
                            JSONObject imgObj = list.getJSONObject("image");
                            arrayList.add(new ListDetailsModel(list.getString("_id"),list.getString("list_name"),
                                    list.getString("vendor_id"),list.getString("business_id"),
                                    imgObj.getString("url"),imgObj.getString("key"),
                                    list.getBoolean("approved"), listBoolean));
                        }else{
                            arrayList.add(new ListDetailsModel(list.getString("_id"),list.getString("list_name"),
                                    list.getString("vendor_id"),list.getString("business_id"),
                                    "","",
                                    list.getBoolean("approved"), listBoolean));
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
                Toast.makeText(VendorMyProductsActivity.this,"Couldn't fetch the data",Toast.LENGTH_LONG).show();
            }
        });

        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,businessDashboard.getBusinessId()+"LISTS");
    }


    private void setUpRecyclerView(){
        recyclerView=(RecyclerView)findViewById(R.id.myProducts_rcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(VendorMyProductsActivity.this,LinearLayoutManager.VERTICAL, false));
        vendorMyProductsAdapter=new VendorMyProductsAdapter(arrayList,VendorMyProductsActivity.this,this,this);
        recyclerView.setAdapter(vendorMyProductsAdapter);
    }

    @Override
    public void saveAndLoad(String productName, ListDetailsModel listDetailsModel) {
        //updateListName(listDetailsModel,productName);
        Intent intent = new Intent(VendorMyProductsActivity.this,VendorEditListActivity.class);
        intent.putExtra("listDetails",listDetailsModel);
        intent.putExtra("businessDetails",businessDashboard);
        startActivity(intent);
    }

    @Override
    public void deleteAndLoad(final int i) {
       deleteOfferDialog(i);
    }


    private void deleteOfferDialog(int position){
        ConfirmationDialog mAlert = new ConfirmationDialog(VendorMyProductsActivity.this);
        mAlert.setTitle("Delete List");
        mAlert.setIcon(android.R.drawable.ic_dialog_alert);
        mAlert.setMessage("Are you sure you want to delete this list? You can't undo this action.");
        mAlert.setPositveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
                deleteList(position);
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

    private void updateList(int i){
        arrayList.remove(i);
        recyclerView=(RecyclerView)findViewById(R.id.myProducts_rcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(VendorMyProductsActivity.this, LinearLayoutManager.VERTICAL, false));
        vendorMyProductsAdapter=new VendorMyProductsAdapter(arrayList,VendorMyProductsActivity.this,this,this);
        recyclerView.setAdapter(vendorMyProductsAdapter);
        vendorMyProductsAdapter.notifyDataSetChanged();

    }

    private void deleteList(int i){
        String url = Constants.BASE_URL+"vendor/delete-list/"+arrayList.get(i).getListID();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(VendorMyProductsActivity.this,"Success",Toast.LENGTH_LONG).show();
                        updateList(i);
                    }else{
                        Toast.makeText(VendorMyProductsActivity.this,"Couldn't delete list. Please Try again!",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(VendorMyProductsActivity.this,"Couldn't delete list. Please Try again!",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"DELETE_LIST");
    }
}
