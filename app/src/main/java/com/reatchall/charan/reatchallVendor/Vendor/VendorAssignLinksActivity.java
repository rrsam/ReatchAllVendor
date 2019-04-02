package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.VendorAssignLinksAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.ViewManagersAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorAssignLinksActivity extends AppCompatActivity {

    private static final String TAG = "VendorAssignLinksActivi";

    ImageView backArrow;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    Context context;
    BusinessDetails businessDetails;
    FontTextView titleToolbar;
    PrefManager prefManager;
    ArrayList<String> managersList = new ArrayList<>();
    ArrayList<Boolean> booleans = new ArrayList<>();
    RecyclerView assignLinks;
    VendorAssignLinksAdapter vendorAssignLinksAdapter;

    LinearLayout parentLayout;
    FontTextView selectAllTv;
    ImageView selectAllUnchecked,selectAllChecked;

    private Spinner managerSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_assign_links);
        init();
    }

    private void init(){
        context = VendorAssignLinksActivity.this;
        prefManager = new PrefManager(context);
        titleToolbar= findViewById(R.id.title_toolbar);
        businessDetails=getIntent().getExtras().getParcelable("businessDetails");
        if(businessDetails!=null){
            titleToolbar.setText(businessDetails.getBusinessName());
        }
        customProgressDialog = new CustomProgressDialog(context);
        backArrow= findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        parentLayout = findViewById(R.id.parentLayout);
        selectAllTv = findViewById(R.id.selectAllTv);
        selectAllUnchecked = findViewById(R.id.selectAllUnchecked);
        selectAllChecked = findViewById(R.id.selectAllChecked);
        managerSpinner =findViewById(R.id.managerSpinner);
        fetchManagersFromServer();

        assignLinks= findViewById(R.id.assignLinks);
        assignLinks.setHasFixedSize(true);
        assignLinks.setNestedScrollingEnabled(false);
        assignLinks.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));

        managersList.add("Dashboard Home");
        booleans.add(false);
        managersList.add("Visitor's Analysis");
        booleans.add(false);
        managersList.add("Orders");
        booleans.add(false);
        managersList.add("Cash");
        booleans.add(false);
        managersList.add("Notifications");
        booleans.add(false);
        managersList.add("Offer Zone");
        booleans.add(false);
        managersList.add("Business Analysis");
        booleans.add(false);
        managersList.add("CMS Management");
        booleans.add(false);
        managersList.add("Business Profile");
        booleans.add(false);
        managersList.add("Business Settings");
        booleans.add(false);
        managersList.add("Privacy Settings");
        booleans.add(false);
        managersList.add("Access Management");
        booleans.add(false);
        managersList.add("Business Plans");
        booleans.add(false);
        managersList.add("Business Timings");
        booleans.add(false);
        managersList.add("Home Delivery Options");
        booleans.add(false);
        managersList.add("My promotions");
        booleans.add(false);
        managersList.add("Bank Details");
        booleans.add(false);
        managersList.add("My Documents");
        booleans.add(false);
        managersList.add("Update Aadhaar");
        booleans.add(false);
        managersList.add("Social Links");
        booleans.add(false);
        managersList.add("Advertisements");
        booleans.add(false);
        managersList.add("Feedback");
        booleans.add(false);
        managersList.add("Help/Contact Us");
        booleans.add(false);
        managersList.add("Add OrderedItem");
        booleans.add(false);
        managersList.add("Add List");
        booleans.add(false);
        managersList.add("Manage Sales");
        booleans.add(false);
        managersList.add("Notification Settings");
        booleans.add(false);
        managersList.add("Add Manager");
        booleans.add(false);

        selectAllTv.setText("Select All Links");
        selectAllUnchecked.setVisibility(View.VISIBLE);
        selectAllChecked.setVisibility(View.GONE);
        vendorAssignLinksAdapter=new VendorAssignLinksAdapter(context,managersList,booleans);
        assignLinks.setAdapter(vendorAssignLinksAdapter);

        parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectAllChecked.getVisibility()==View.VISIBLE){
                    selectAllChecked.setVisibility(View.GONE);
                    selectAllUnchecked.setVisibility(View.VISIBLE);
                    selectAllTv.setText("Select All Links");
                    for(int i=0;i<booleans.size();i++){
                        booleans.set(i,false);
                    }
                    vendorAssignLinksAdapter.notifyDataSetChanged();
                }else{
                    selectAllChecked.setVisibility(View.VISIBLE);
                    selectAllUnchecked.setVisibility(View.GONE);
                    selectAllTv.setText("UnSelect");
                    for(int i=0;i<booleans.size();i++){
                        booleans.set(i,true);
                    }
                    vendorAssignLinksAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void fetchManagersFromServer() {
        String url = Constants.BASE_URL+"vendor/get-business-managers";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("business_id",businessDetails.getBusinessId());
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: response"+ response.toString() );
                try {
                    if(response.getBoolean("success")){
                        JSONArray managersArray = response.getJSONArray("msg");
                        managersList = new ArrayList<>();
                        for(int i=0;i<managersArray.length();i++){
                            managersList.add(managersArray.getJSONObject(i).getString("name"));
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, managersList){
                            @NonNull
                            @Override
                            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                View v = super.getView(position, convertView, parent);

                                Typeface externalFont= Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                                ((TextView) v).setTypeface(externalFont);
                                ((TextView) v).setTextSize(14);


                                return v;
                            }

                            @Override
                            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                                View v =super.getDropDownView(position, convertView, parent);

                                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                                ((TextView) v).setTypeface(externalFont);
                                ((TextView) v).setTextSize(14);

                                return v;
                            }
                        };
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        managerSpinner.setAdapter(dataAdapter);


                    }else{
                        Toast.makeText(context,"Couldn't fetch details",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Couldn't fetch details",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"BIZ_MANAGERS");
    }
}
