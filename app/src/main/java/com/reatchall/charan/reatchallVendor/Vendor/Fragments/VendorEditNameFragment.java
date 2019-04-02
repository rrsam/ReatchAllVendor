package com.reatchall.charan.reatchallVendor.Vendor.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.reatchall.charan.reatchallVendor.Vendor.Models.VendorDetails;
import com.reatchall.charan.reatchallVendor.Vendor.VendorEditProfileActivity;

import org.json.JSONException;
import org.json.JSONObject;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontEditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class VendorEditNameFragment extends Fragment {

    View view;
    VendorDetails vendorDetails;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    PrefManager prefManager;
    Context context;

    FontEditText userName;
    FontButton editProfile;
    VendorEditProfileActivity vendorEditProfileActivity;

    public VendorEditNameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_vendor_edit_name, container, false);
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        vendorDetails = getArguments().getParcelable("vendorDetails");
        customProgressDialog = new CustomProgressDialog(context);
        prefManager = new PrefManager(context);
        vendorEditProfileActivity = (VendorEditProfileActivity)getActivity();
        vendorEditProfileActivity.highlightTab(1);

        userName =(FontEditText)view.findViewById(R.id.userName);
        editProfile =(FontButton) view.findViewById(R.id.editProfile);
        userName.setText(vendorDetails.getUserName());

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!userName.getText().toString().isEmpty()){
                    if(userName.getText().toString().equals(vendorDetails.getUserName())){
                        Toast.makeText(context,"No changes Found!",Toast.LENGTH_LONG).show();
                    }else{
                        customProgressDialog.showDialog();
                        update();
                    }

                }else{
                    Toast.makeText(context,"Username can't be empty",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void update(){
        String url = Constants.BASE_URL+"vendor/update-vendor-profile";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
            jsonObject.put("name",userName.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        vendorDetails.setUserName(userName.getText().toString());
                        vendorEditProfileActivity.updateVendorDetails(vendorDetails);
                        Toast.makeText(context,"Updated Successfully!",Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(context,"Please try again",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Please try again",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"UPDATENAME");
    }
}
