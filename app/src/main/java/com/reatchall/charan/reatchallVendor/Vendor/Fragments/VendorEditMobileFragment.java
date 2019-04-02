package com.reatchall.charan.reatchallVendor.Vendor.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import static com.reatchall.charan.reatchallVendor.Registration.RegisterActivity.PhoneNumberValidator;


/**
 * A simple {@link Fragment} subclass.
 */
public class VendorEditMobileFragment extends Fragment {
    View view;
    VendorDetails vendorDetails;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    PrefManager prefManager;
    Context context;

    FontEditText userName,otp;
    FontButton editProfile;
    LinearLayout otpLayout;
    VendorEditProfileActivity vendorEditProfileActivity;
    public VendorEditMobileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_vendor_edit_mobile, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        vendorDetails = getArguments().getParcelable("vendorDetails");
        customProgressDialog = new CustomProgressDialog(context);
        prefManager = new PrefManager(context);
        vendorEditProfileActivity = (VendorEditProfileActivity)getActivity();
        vendorEditProfileActivity.highlightTab(2);
        userName =(FontEditText)view.findViewById(R.id.userName);
        otp =(FontEditText)view.findViewById(R.id.otp);
        otpLayout =(LinearLayout) view.findViewById(R.id.otpLayout);
        editProfile =(FontButton) view.findViewById(R.id.editProfile);
        userName.setText(vendorDetails.getPhoneNumber());

        otpLayout.setVisibility(View.GONE);
        otp.getText().clear();
        editProfile.setText("SAVE");

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editProfile.getText().toString().equalsIgnoreCase("SAVE")){
                    if(!userName.getText().toString().isEmpty()){
                        if(PhoneNumberValidator(userName.getText().toString())){
                            if(userName.getText().toString().equals(vendorDetails.getPhoneNumber())){
                                Toast.makeText(context,"No changes Found!",Toast.LENGTH_LONG).show();
                            }else{
                                customProgressDialog.showDialog();
                                Toast.makeText(context,"Otp sent to "+vendorDetails.getPhoneNumber(),Toast.LENGTH_LONG).show();
                                sendOtp();
                            }
                        }else{
                            Toast.makeText(context,"Invalid Mobile number",Toast.LENGTH_LONG).show();

                        }
                    }else{
                        Toast.makeText(context,"Mobile number can't be empty",Toast.LENGTH_LONG).show();
                    }
                }else{
                    if(!otp.getText().toString().isEmpty()){
                        customProgressDialog.showDialog();
                        update();
                    }else{
                        Toast.makeText(context,"Invalid Otp",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    private void update(){
        String url = Constants.BASE_URL+"vendor/update-vendor-mobile";

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
            jsonObject.put("new_mobile",userName.getText().toString());
            jsonObject.put("otp",otp.getText().toString());
            jsonObject.put("mobile",vendorDetails.getPhoneNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Updated Successfully!",Toast.LENGTH_LONG).show();
                        vendorDetails.setPhoneNumber(userName.getText().toString());
                        vendorEditProfileActivity.updateVendorDetails(vendorDetails);
                        otpLayout.setVisibility(View.GONE);
                        otp.getText().clear();
                        editProfile.setText("SAVE");
                        userName.setFocusable(true);
                        userName.setFocusableInTouchMode(true);
                        userName.setClickable(false);
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

    private static final String TAG = "VendorEditMobileFragmen";
    private void sendOtp(){
        String url = Constants.BASE_URL+"vendor/vendor-update-otp";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
            jsonObject.put("mobile",vendorDetails.getPhoneNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onResponse: "+response.toString() );
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Please Enter the Otp",Toast.LENGTH_LONG).show();
                        userName.setFocusable(false);
                        userName.setFocusableInTouchMode(false);
                        userName.setClickable(true);
                        otpLayout.setVisibility(View.VISIBLE);
                        otp.getText().clear();
                        editProfile.setText("SUBMIT");
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
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Please try again!",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"UPDATEOTP");
    }
}
