package com.reatchall.charan.reatchallVendor.Vendor;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.CreateBusiness.VendorCreateBusinessActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Models.AllProducts;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDashboard;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IDashboardInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import com.android.volley.Request;



/**
 * A simple {@link Fragment} subclass.
 */
public class VendorHomeFragment extends Fragment implements IDashboardInterface {


    public VendorHomeFragment() {
        // Required empty public constructor
    }

    private static final String TAG = "VendorHomeFragment";

    RecyclerView recyclerView;
    VendorHomeAdapterNew vendorHomeAdapter;
    ArrayList<BusinessDashboard> arrayList;
    ArrayList<BusinessDetails> businessDetails;
    NestedScrollView nestedScrollView;
    RelativeLayout noBusinessLayout;
    LinearLayout addBusinessLayout;
    ReatchAll helper = ReatchAll.getInstance();

    CustomProgressDialog customProgressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vendor_home_new, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nestedScrollView=(NestedScrollView)view.findViewById(R.id.scrollView);
        recyclerView=(RecyclerView)view.findViewById(R.id.vendor_home_rcv);
        noBusinessLayout=(RelativeLayout)view.findViewById(R.id.noBusinessLayout);
        addBusinessLayout=(LinearLayout)view.findViewById(R.id.addAnotherBusiness);

        addBusinessLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),VendorCreateBusinessActivity.class));
            }
        });

        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        customProgressDialog=new CustomProgressDialog(getActivity());
       // customProgressDialog.showDialog();
        getBusinessNew();


    }



    @Override
    public void passBusinessId(BusinessDetails businessDashboard) {
        if(businessDashboard.isService()){
            Intent intent = new Intent(getActivity(),VendorDashBoardNewActivity.class);
            intent.putExtra("businessDetails",businessDashboard);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getActivity(),VendorDashBoardActivity.class);
            intent.putExtra("businessDetails",businessDashboard);
            startActivity(intent);

           /* AllProducts allProducts = new AllProducts();
            allProducts.setProductId("5c08d185af5b401998b7457f");
            allProducts.setListId("5bf65a026f703b32bf391614");

            Intent intent = new Intent(getActivity(),VendorEditItemActivity.class);
            intent.putExtra("businessDetails",businessDashboard);
            intent.putExtra("productDetails",allProducts);
            startActivity(intent);*/
        }

    }


    private void getBusinessNew(){
        PrefManager prefManager = new PrefManager(getActivity());
            String id= prefManager.getVendorId(getActivity());
        Log.d(TAG, "getBusiness: "+id);
            String url = Constants.BASE_URL+"vendor/get-vendor-businesses/"+id;
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    if(success){
                        businessDetails=new ArrayList<>();

                        JSONArray Jarray = response.getJSONArray("msg");

                        for(int i=0;i<Jarray.length();i++){
                            JSONObject businessDetailsObj = new JSONObject(Jarray.get(i).toString());
                            //JSONObject business = businessDetails.getJSONObject("msg");
                            if(businessDetailsObj.getJSONObject("business_type").getString("name").contains("Service")){
                                Log.d(TAG, "doInBackground: BUSINESS NAME"+businessDetailsObj.getString("name")+ " "+businessDetailsObj.getBoolean("timings_flag") );
                                businessDetails.add(new BusinessDetails(businessDetailsObj.getString("_id"),businessDetailsObj.getString("name"),
                                        businessDetailsObj.getString("plan"),businessDetailsObj.getBoolean("hide"),
                                        businessDetailsObj.getBoolean("delete"),businessDetailsObj.getBoolean("suspend"),
                                        businessDetailsObj.getBoolean("approved"),businessDetailsObj.getBoolean("featured"),
                                        businessDetailsObj.getBoolean("home_delivery"),businessDetailsObj.getBoolean("paused"),
                                        businessDetailsObj.getBoolean("timings_flag"),true));
                            }else{
                                Log.d(TAG, "doInBackground: BUSINESS NAME"+businessDetailsObj.getString("name")+ " "+businessDetailsObj.getBoolean("timings_flag") );
                                businessDetails.add(new BusinessDetails(businessDetailsObj.getString("_id"),businessDetailsObj.getString("name"),
                                        businessDetailsObj.getString("plan"),businessDetailsObj.getBoolean("hide"),
                                        businessDetailsObj.getBoolean("delete"),businessDetailsObj.getBoolean("suspend"),
                                        businessDetailsObj.getBoolean("approved"),businessDetailsObj.getBoolean("featured"),
                                        businessDetailsObj.getBoolean("home_delivery"),businessDetailsObj.getBoolean("paused"),
                                        businessDetailsObj.getBoolean("timings_flag"),false));
                            }

                        }
                        //customProgressDialog.hideDialog();

                        setAdapter();
                    }else{
                        String msg = response.getString("msg");
                        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
                        nestedScrollView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        noBusinessLayout.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(),""+error.getMessage(),Toast.LENGTH_LONG).show();
                Log.d(TAG, "onErrorResponse: "+error );

            }
        });

        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"GET BUSINESSES LIST");
    }



    private void setAdapter(){

        if(businessDetails.size() > 0){
            Log.d(TAG, "setAdapter: "+businessDetails.size());
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            noBusinessLayout.setVisibility(View.GONE);
            nestedScrollView.setVisibility(View.VISIBLE);
            vendorHomeAdapter=new VendorHomeAdapterNew(businessDetails,getActivity(),this);
            recyclerView.setAdapter(vendorHomeAdapter);
            vendorHomeAdapter.notifyDataSetChanged();
        }else{
            nestedScrollView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            noBusinessLayout.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public void onResume() {
        super.onResume();
     //   customProgressDialog.showDialog();
        getBusinessNew();
       // customProgressDialog.hideDialog();
    }





}
