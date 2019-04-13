package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.OnSpinerItemClick;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Utils.SpinnerDialog;
import com.reatchall.charan.reatchallVendor.Vendor.Maps.MarkLocationActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.Districts;
import com.reatchall.charan.reatchallVendor.Vendor.Models.States;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorUpdateAddressActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = "VendorUpdateAddressActi";
    Context context;
    ReatchAll helper = ReatchAll.getInstance();
    CustomProgressDialog customProgressDialog;
    ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;

    FontEditText pinCode,doorNo,streetLocality,landmark,cityName,countryName,addressBusiness;
    FontTextView stateName,districtName;
    RelativeLayout getCurrentlocation;
    LinearLayout updateAddress;
    //MARK LOCATION WIDGETS
    LinearLayout mapLayout;
    private GoogleMap mMap;
    private static final float DEFAULT_ZOOM = 18f;
    boolean mapReady=false;
    boolean coordsSet=false;
    LocationManager locationManager;
    private double latitude, longitude;
    String selectedStateId,prevStateId,selectedCityStateId,selectedDistrictStateId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_update_address);
        context= VendorUpdateAddressActivity.this;
        customProgressDialog = new CustomProgressDialog(context);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titleToolbar = (FontTextView) findViewById(R.id.title_toolbar);
        businessDashboard = getIntent().getExtras().getParcelable("businessDetails");
        if (businessDashboard != null) {
            titleToolbar.setText("Update Address");
        }

        initViews();
        customProgressDialog.showDialog();
        updateAddress=(LinearLayout)findViewById(R.id.updateAddress);
        updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFields();
            }
        });
    }

    private void formJSOn(){
        JSONObject business = new JSONObject();
        JSONObject coards = new JSONObject();
        try {
            business.put("business_id",businessDashboard.getBusinessId());
            coards.put("lat",latitude);
            coards.put("lng",longitude);
            business.put("location",coards);
            business.put("address",addressBusiness.getText().toString());
            business.put("door_no",doorNo.getText().toString());
            business.put("street",streetLocality.getText().toString());
            business.put("landmark",landmark.getText().toString());
            business.put("city",cityName.getText().toString());
            business.put("country",countryName.getText().toString());
            business.put("state",selectedStateId);
            business.put("district",selectedDistrictId);
            business.put("pincode",pinCode.getText().toString());
            customProgressDialog.showDialog();
            updateBizAddress(business);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void updateBizAddress(JSONObject business) {
        String url = Constants.BASE_URL+"vendor/update-business-profile-address";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, business, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Successfully updated!",Toast.LENGTH_LONG).show();
                        finish();
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
        helper.addToRequestQueue(customJsonRequest,"UPDATE_BIZ_ADDRESS");
    }

    private void validateFields(){
        if (addressBusiness.getText().toString().length() != 0) {
            if (coordsSet) {
                if (doorNo.getText().toString().length() != 0) {
                    if (streetLocality.getText().toString().length() != 0) {
                        if (landmark.getText().toString().length() != 0) {
                            if (stateName.getText().toString().length() != 0) {
                                if (countryName.getText().toString().length() != 0) {
                                    if (cityName.getText().toString().length() != 0) {
                                        if (districtName.getText().toString().length() != 0) {
                                            if (pinCode.getText().toString().length() != 0) {
                                                if (pinCode.getText().toString().length() == 6) {
                                                   formJSOn();
                                                } else {
                                                    Toast.makeText(context, "Pincode isn't valid", Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(context, "Pincode cannot be empty", Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(context, "District cannot be empty", Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(context, "city field is empty", Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(context, "Country field is empty", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(context, "Please select a STATE", Toast.LENGTH_LONG).show();

                            }
                        } else {
                            Toast.makeText(context, "Landmark cannot be empty", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(context, "Street or Area cannot be empty", Toast.LENGTH_LONG).show();

                    }
                } else {
                    Toast.makeText(context, "Door Number cannot be empty", Toast.LENGTH_LONG).show();

                }
            } else {
                Toast.makeText(context, "Please mark location on map", Toast.LENGTH_LONG).show();

            }
        } else {
            Toast.makeText(context, "Address cannot be empty", Toast.LENGTH_LONG).show();

        }
    }

    private void getBusinessDetails(){
        String url = Constants.BASE_URL+"vendor/get-business-profile/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    customProgressDialog.hideDialog();
                    if(response.getBoolean("success")){
                        JSONObject parent = response.getJSONObject("msg");
                       latitude = parent.getJSONObject("location").getDouble("lat");
                       longitude = parent.getJSONObject("location").getDouble("lng");
                       addressBusiness.setText(parent.getString("address"));
                       landmark.setText(parent.getString("landmark"));
                       streetLocality.setText(parent.getString("street"));
                       selectedDistrictStateId = parent.getJSONArray("district").getJSONObject(0).getString("_id");
                       selectedStateId = parent.getJSONArray("state").getJSONObject(0).getString("_id");
                       districtName.setText(parent.getJSONArray("district").getJSONObject(0).getString("name"));
                       stateName.setText(parent.getJSONArray("state").getJSONObject(0).getString("name"));
                       doorNo.setText(parent.getString("door_no"));
                       cityName.setText(parent.getString("city"));
                       pinCode.setText(parent.getString("pincode"));
                       countryName.setText(parent.getString("country"));

                       if(latitude!=0){
                           showMap(latitude,longitude);
                       }
                    }else{
                        Toast.makeText(context,"Unable to fetch details. Please try again!",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Unable to fetch details. Please try again!",Toast.LENGTH_LONG).show();
            }
        });

        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"GET_DETAILS");
    }

    private void initViews(){
        getStatesData();

        addressBusiness = (FontEditText) findViewById(R.id.addressBusiness);
        getCurrentlocation = (RelativeLayout) findViewById(R.id.getCurrentLocation);
        pinCode=(FontEditText)findViewById(R.id.pinCode);
        doorNo=(FontEditText)findViewById(R.id.doorNo);
        streetLocality=(FontEditText)findViewById(R.id.streetLocality);
        landmark=(FontEditText)findViewById(R.id.landmark);
        cityName=(FontEditText) findViewById(R.id.cityName);
        stateName=(FontTextView) findViewById(R.id.stateName);
        districtName=(FontTextView) findViewById(R.id.districtName);
        countryName=(FontEditText)findViewById(R.id.countryName);
        mapLayout=(LinearLayout)findViewById(R.id.mapLayout);
        pinCode=(FontEditText)findViewById(R.id.pincode);
        mapLayout.setVisibility(View.GONE);
        initMap();


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getCurrentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // getCurrentLocation();

                Intent intent=new Intent(VendorUpdateAddressActivity.this,MarkLocationActivity.class);
                startActivityForResult(intent, 2);
            }
        });


        stateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateSpinnerDialog.showSpinerDialog();
            }
        });

        districtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedStateId!=null)
                    districtSpinnerDialog.showSpinerDialog();
                else
                    Toast.makeText(context,"Please select a state",Toast.LENGTH_LONG).show();
            }
        });

        getBusinessDetails();

    }

    ArrayList<States> statesArrayList = new ArrayList<>();
    ArrayList<String> stateNames;
    SpinnerDialog stateSpinnerDialog;
    private void getStatesData(){
        String url = Constants.BASE_URL+"admin/get-all-states";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        statesArrayList = new ArrayList<>();
                        stateNames= new ArrayList<>();
                        JSONArray statesArray = response.getJSONArray("msg");
                        for(int i=0;i<statesArray.length();i++){
                            JSONObject statesObject = statesArray.getJSONObject(i);
                            stateNames.add(statesObject.getString("name"));
                            statesArrayList.add(new States(statesObject.getString("_id"),statesObject.getString("name")));
                        }
                        stateSpinnerDialog=new SpinnerDialog(VendorUpdateAddressActivity.this,stateNames,"Select a State",R.style.DialogAnimations_SmileWindow,"Close");// With 	Animation

                        stateSpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, int position) {
                                //Toast.makeText(VendorCreateBusinessActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                if(selectedStateId==null)
                                    prevStateId=statesArrayList.get(position).getStateId();
                                else
                                    prevStateId=selectedStateId;
                                selectedStateId=statesArrayList.get(position).getStateId();
                                getDistricts(selectedStateId);
                                stateName.setText(item);

                                if(!prevStateId.equals(selectedStateId)){
                                    cityName.setText("");
                                    districtName.setText("");
                                   // districtSpinnerDialog.showSpinerDialog();
                                }
                            }
                        });
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
        helper.addToRequestQueue(customJsonRequest,"STATES");
    }

    ArrayList<Districts> districtsArrayList;
    ArrayList<String> districtNames;
    SpinnerDialog districtSpinnerDialog;
    String selectedDistrictId;
    private void getDistricts(String stateId){
        String url = Constants.BASE_URL+"admin/get-district-by-state/"+stateId;
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        districtsArrayList = new ArrayList<>();
                        districtNames = new ArrayList<>();
                        JSONArray districtsArray = response.getJSONArray("msg");
                        for(int i=0;i<districtsArray.length();i++){
                            JSONObject districtsObject = districtsArray.getJSONObject(i);
                            districtNames.add(districtsObject.getString("name"));
                            districtsArrayList.add(new Districts(districtsObject.getString("_id"),districtsObject.getString("state"),
                                    districtsObject.getString("name")));
                        }

                        districtSpinnerDialog=new SpinnerDialog(VendorUpdateAddressActivity .this,districtNames,"Select a District",R.style.DialogAnimations_SmileWindow,"Close");// With 	Animation

                        districtSpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, int position) {
                                //Toast.makeText(VendorCreateBusinessActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                districtName.setText(item);
                                selectedDistrictId=districtsArrayList.get(position).getDistrictId();
                            }
                        });
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
        helper.addToRequestQueue(customJsonRequest,"DISTRICTS");
    }


    private void showMap(Double latitude,Double longitude){
        moveCamera(new LatLng(latitude,longitude),DEFAULT_ZOOM,"Shop Location");
        mapLayout.setVisibility(View.VISIBLE);
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(VendorUpdateAddressActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
        mapReady=true;

    }

    MarkerOptions options;
    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if(options!=null){
            mMap.clear();
        }
        options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2)
        {
            Log.e(TAG, "onActivityResult: " );
            if(resultCode==RESULT_OK){
                Double lat=data.getDoubleExtra("latitude",0.0);
                Double longt=data.getDoubleExtra("longitude",0.0);
                Log.e(TAG, "onActivityResult: "+lat+"LONG"+longt );
                //startBtn.setText(latitude+"D"+longitude);

                latitude=lat;
                longitude=longt;
                coordsSet=true;
                if(mapReady){
                    Log.e(TAG, "onActivityResult: IFYES" );
                    showMap(lat,longt);
                }else{
                    Log.e(TAG, "onActivityResult: ELSE" );
                    initMap();
                    if(mapReady)
                        showMap(lat,longt);
                }
            }else{
                if(resultCode==RESULT_CANCELED)
                    Log.e(TAG, "onActivityResult: RES CANCE" );
                else{
                    Log.e(TAG, "onActivityResult: NOT OK" );
                    Double lat=data.getDoubleExtra("latitude",0.0);
                    Double longt=data.getDoubleExtra("longitude",0.0);
                    if(lat!=null && longt !=null){
                        Log.e(TAG, "onActivityResult: "+latitude+"LONG"+longitude );
                        //startBtn.setText(latitude+"D"+longitude);
                        latitude=lat;
                        longitude=longt;
                        coordsSet=true;
                        if(mapReady){
                            Log.e(TAG, "onActivityResult: IFYES" );
                            showMap(lat,longt);
                        }else{
                            Log.e(TAG, "onActivityResult: ELSE" );
                            initMap();
                            if(mapReady)
                                showMap(lat,longt);
                        }
                    }
                }
            }
        }
    }

}
