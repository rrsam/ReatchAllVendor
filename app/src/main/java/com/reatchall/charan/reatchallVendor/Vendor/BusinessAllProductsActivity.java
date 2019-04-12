package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
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

public class BusinessAllProductsActivity extends AppCompatActivity implements ILoadProducts, PopupMenu.OnMenuItemClickListener, BusinessAllProductsAdapter.OncheckChangeListener, BusinessAllProductsAdapter.OnItemClickListener {

    private static final String TAG = "BusinessAllProductsActi";
    ImageView backArrow;
    FontTextView titleToolbar;

    RecyclerView recyclerView;
    ArrayList<AllProducts> arrayList = new ArrayList<>();
    ArrayList<NewProduct> productsArrayList;
    BusinessAllProductsAdapter businessAllProductsAdapter;
    BusinessDetails businessDetails = null;

    int toggle;
    FontTextView subTitle,txtSort,txtSelect,txtActions,addBtn;
    boolean listBoolean=false,allData = false;

    ReatchAll helper = ReatchAll.getInstance();

    CustomProgressDialog customProgressDialog;
    LinearLayout saveControllerDetails;
    ArrayList<String> itemsIdList = new ArrayList<>();
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_all_products);
        mContext = BusinessAllProductsActivity.this;
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titleToolbar = (FontTextView) findViewById(R.id.title_toolbar);
        subTitle=(FontTextView)findViewById(R.id.subTitle);
        txtSort=(FontTextView)findViewById(R.id.txtSort);
        txtSelect=(FontTextView)findViewById(R.id.txtSelect);
        txtActions=(FontTextView)findViewById(R.id.txtActions);

        addBtn=(FontTextView)findViewById(R.id.add_btn);

        businessDetails = getIntent().getExtras().getParcelable("businessDetails");
        toggle=getIntent().getExtras().getInt("toggle");



        if(toggle==2){
            subTitle.setText("My Lists");
            listBoolean=true;

        }else{
            listBoolean=false;
            subTitle.setText("My Products");
        }

        if (businessDetails != null) {
            titleToolbar.setText(businessDetails.getBusinessName().toString());
        }

        if(businessDetails != null){
            Log.e(TAG, "onCreate:ID "+businessDetails.getBusinessId() +"NAME"+businessDetails.getBusinessName());
        }

        recyclerView=(RecyclerView)findViewById(R.id.myProducts_rcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(BusinessAllProductsActivity.this,LinearLayoutManager.VERTICAL, false));


        customProgressDialog= new CustomProgressDialog(BusinessAllProductsActivity.this);
        loadAllProducts();
        txtSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(BusinessAllProductsActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.sort_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(BusinessAllProductsActivity.this);
                popup.show();
            }
        });

        txtActions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(BusinessAllProductsActivity.this, v);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.products_action_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(BusinessAllProductsActivity.this);
                popup.show();
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VendorAddItemsActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });


    }


    private void setUpRecyclerView(){
        businessAllProductsAdapter=new BusinessAllProductsAdapter(BusinessAllProductsActivity.this,productsArrayList,BusinessAllProductsActivity.this,
                BusinessAllProductsActivity.this,BusinessAllProductsActivity.this);
        recyclerView.setAdapter(businessAllProductsAdapter);
    }

    @Override
    public void saveAndLoad(String productName, NewProduct allProducts) {
        loadAllProducts();
        Intent intent = new Intent(BusinessAllProductsActivity.this,VendorEditItemActivity.class);
        intent.putExtra("productDetails",allProducts);
        intent.putExtra("businessDetails",businessDetails);
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
        String url = Constants.BASE_URL+"vendor/delete-item/"+productsArrayList.get(positon).getItemId();
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


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_menu_all:
                subTitle.setText("All Products");
                loadAllProducts();
                return true;
            case R.id.sort_menu_active:
                subTitle.setText("Active Products");
                loadActiveProducts();
                return true;
            case R.id.sort_menu_inactive:
                subTitle.setText("Inactive Products");
                loadInActiveProducts();
                return true;
            case R.id.sort_menu_pending:
                subTitle.setText("Pending Products");
                loadPendingProducts();
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




    private void loadAllProducts(){
        customProgressDialog.showDialog();
        String url = Constants.BASE_URL+"vendor/get-items-by-businessid/"+businessDetails.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                JSONArray msg = null;
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                    msg = response.getJSONArray("msg");

                    productsArrayList  = new ArrayList<>();

                    for(int i =0;i<msg.length();i++){
                        JSONObject list = msg.getJSONObject(i);
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
                    setUpRecyclerView();

                }else{
                        productsArrayList.clear();
                        businessAllProductsAdapter.notifyDataSetChanged();
                    Toast.makeText(BusinessAllProductsActivity.this, "No List", Toast.LENGTH_SHORT).show();
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                customProgressDialog.hideDialog();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(BusinessAllProductsActivity.this,"Couldn't fetch the data",Toast.LENGTH_LONG).show();
            }
        });

        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,businessDetails.getBusinessId()+"LISTS");
    }

    private void loadActiveProducts(){
        customProgressDialog.showDialog();
        String url = Constants.BASE_URL+"vendor/get-active-items-by-business_id/"+businessDetails.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: " +response.toString() );
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
                                    if (list.getBoolean("single")) {
                                        productsArrayList.add(new NewProduct(list.getString("_id"), list.getString("name"), list.getString("list_id"),
                                                list.getString("vendor_id"), list.getString("business_id"), list.getString("description"),
                                                list.getString("brand"), list.getString("units"), list.getInt("quantity"),
                                                list.getDouble("price"), list.getBoolean("single"), 0, list.getJSONArray("images").getJSONObject(0).getString("url")));
                                    } else {
                                        productsArrayList.add(new NewProduct(list.getString("_id"), list.getString("name"), list.getString("list_id"),
                                                list.getString("vendor_id"), list.getString("business_id"), list.getString("description"),
                                                list.getString("brand"), list.getString("units"), list.getInt("quantity"),
                                                list.getDouble("price"), list.getBoolean("single"), list.getInt("pack"), list.getJSONArray("images").getJSONObject(0).getString("url")));
                                    }
                                } else {
                                    if (list.getBoolean("single")) {
                                        productsArrayList.add(new NewProduct(list.getString("_id"), list.getString("name"), list.getString("list_id"),
                                                list.getString("vendor_id"), list.getString("business_id"), list.getString("description"),
                                                list.getString("brand"), list.getString("units"), list.getInt("quantity"),
                                                list.getDouble("price"), list.getBoolean("single"), 0, ""));
                                    } else {
                                        productsArrayList.add(new NewProduct(list.getString("_id"), list.getString("name"), list.getString("list_id"),
                                                list.getString("vendor_id"), list.getString("business_id"), list.getString("description"),
                                                list.getString("brand"), list.getString("units"), list.getInt("quantity"),
                                                list.getDouble("price"), list.getBoolean("single"), list.getInt("pack"), ""));
                                    }
                                }
                            } else {
                                if (list.getJSONArray("images").length() > 0) {
                                    if (list.getBoolean("single")) {
                                        productsArrayList.add(new NewProduct(list.getString("_id"), list.getString("name"), "",
                                                list.getString("vendor_id"), list.getString("business_id"), list.getString("description"),
                                                list.getString("brand"), list.getString("units"), list.getInt("quantity"),
                                                list.getDouble("price"), list.getBoolean("single"), 0, list.getJSONArray("images").getJSONObject(0).getString("url")));
                                    } else {
                                        productsArrayList.add(new NewProduct(list.getString("_id"), list.getString("name"), "",
                                                list.getString("vendor_id"), list.getString("business_id"), list.getString("description"),
                                                list.getString("brand"), list.getString("units"), list.getInt("quantity"),
                                                list.getDouble("price"), list.getBoolean("single"), list.getInt("pack"), list.getJSONArray("images").getJSONObject(0).getString("url")));
                                    }
                                } else {
                                    if (list.getBoolean("single")) {
                                        productsArrayList.add(new NewProduct(list.getString("_id"), list.getString("name"), "",
                                                list.getString("vendor_id"), list.getString("business_id"), list.getString("description"),
                                                list.getString("brand"), list.getString("units"), list.getInt("quantity"),
                                                list.getDouble("price"), list.getBoolean("single"), 0, ""));
                                    } else {
                                        productsArrayList.add(new NewProduct(list.getString("_id"), list.getString("name"), "",
                                                list.getString("vendor_id"), list.getString("business_id"), list.getString("description"),
                                                list.getString("brand"), list.getString("units"), list.getInt("quantity"),
                                                list.getDouble("price"), list.getBoolean("single"), list.getInt("pack"), ""));
                                    }
                                }
                            }
                        }
                        setUpRecyclerView();
                    }else{

                        productsArrayList.clear();
                        businessAllProductsAdapter.notifyDataSetChanged();
                        Toast.makeText(BusinessAllProductsActivity.this, "No List", Toast.LENGTH_SHORT).show();
                    }

                } catch(JSONException e){
                        e.printStackTrace();
                    }

                customProgressDialog.hideDialog();

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BusinessAllProductsActivity.this,"Couldn't fetch the data",Toast.LENGTH_LONG).show();
                customProgressDialog.hideDialog();
            }
        });

        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,businessDetails.getBusinessId()+"LISTS");
    }

    private void loadInActiveProducts(){
        customProgressDialog.showDialog();
        String url = Constants.BASE_URL+"vendor/get-inactive-items-by-business_id/"+businessDetails.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray msg = null;
                try {
                    boolean success = response.getBoolean("success");
                    if (success) {
                        productsArrayList  = new ArrayList<>();
                        msg = response.getJSONArray("msg");
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

                        setUpRecyclerView();
                    }else{
                        productsArrayList.clear();
                        businessAllProductsAdapter.notifyDataSetChanged();
                        Toast.makeText(BusinessAllProductsActivity.this, "No List", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                customProgressDialog.hideDialog();


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BusinessAllProductsActivity.this,"Couldn't fetch the data",Toast.LENGTH_LONG).show();
                customProgressDialog.hideDialog();
            }
        });

        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,businessDetails.getBusinessId()+"LISTS");
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
                        productsArrayList  = new ArrayList<>();
                        msg = response.getJSONArray("msg");

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
                        setUpRecyclerView();

                    }else{
                        productsArrayList.clear();
                        businessAllProductsAdapter.notifyDataSetChanged();
                        Toast.makeText(BusinessAllProductsActivity.this, "No List", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                customProgressDialog.hideDialog();


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(BusinessAllProductsActivity.this,"Couldn't fetch the data",Toast.LENGTH_LONG).show();
                customProgressDialog.hideDialog();
            }
        });

        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,businessDetails.getBusinessId()+"LISTS");
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
            Toast.makeText(mContext, "Select any Product...", Toast.LENGTH_SHORT).show();
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
                            loadActiveProducts();
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
                                loadInActiveProducts();
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
                                loadAllProducts();
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

    @Override
    public void onItemClicked(NewProduct productDetails) {
        Intent intent = new Intent(BusinessAllProductsActivity.this,BusinessProductDetailsActivity.class);
        intent.putExtra("productDetails",productDetails);
        intent.putExtra("businessDetails",businessDetails);
        startActivity(intent);


    }
}
