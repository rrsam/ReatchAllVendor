package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorBusinessSettingsActivity extends AppCompatActivity {

    private static final String TAG = "VendorBusinessSettingsA";

    ImageView backArrow;
    Context context;
    FontTextView titleToolbar,selectAllTv;
    BusinessDetails businessDashboard;

    LinearLayout selectAllLayout,monDisabledLayout,tueDisabledLayout,wedDisabledLayout,thurDisabledLayout,friDisabledLayout,satDisabledLayout,sunDisabledLayout;
    LinearLayout monAbledLayout,tueAbledLayout,wedAbledLayout,thurAbledLayout,friAbledLayout,satAbledLayout,sunAbledLayout;

    ImageView monUnchecked,tueUnchecked,wedUnchecked,thurUnchecked,friUnchecked,satUnchecked,sunUnchecked;
    ImageView monChecked,tueChecked,wedChecked,thurChecked,friChecked,satChecked,sunChecked;
    ImageView allUnchecked;
    Spinner monFromSpinner,tueFromSpinner,wedFromSpinner,thurFromSpinner,friFromSpinner,satFromSpinner,sunFromSpinner;
    Spinner monToSpinner,tueToSpinner,wedToSpinner,thurToSpinner,friToSpinner,satToSpinner,sunToSpinner;


    ReatchAll helper = ReatchAll.getInstance();

    CustomProgressDialog customProgressDialog;

    //JSON
    JSONObject sunJSON,monJSON,tueJSON,wedJSON,thurJSON,friJSON,satJSON;
    boolean monWrk=false,tueWrk=false,wedWrk=false,thurWrk=false,friWrk=false,satWrk=false,sunWrk=false;
    String openMon, openTue, openWed, openThur, openFri, openSat, openSun,open,openAll;
    String closeMon, closeTue, closeWed, closeThur, closeFri, closeSat, closeSun,close,closeAll;
    ArrayList<String> timesList = new ArrayList<>();


    FontButton updateTimings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_business_settings);
        context = VendorBusinessSettingsActivity.this;
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titleToolbar = (FontTextView) findViewById(R.id.title_toolbar);
        selectAllTv = (FontTextView) findViewById(R.id.selectAllTv);
        updateTimings = (FontButton) findViewById(R.id.updateTimings);
        businessDashboard = getIntent().getExtras().getParcelable("businessDetails");
        if (businessDashboard != null) {
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }
        customProgressDialog=new CustomProgressDialog(this);
        int length= getResources().getStringArray(R.array.timings_array).length;
        for(int i=0;i<length;i++){
            timesList.add(i,getResources().getStringArray(R.array.timings_array)[i]);
        }
        initTimingsWidgets();
        customProgressDialog.showDialog();
        getTimings();

        updateTimings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    validateTimings();
            }
        });
    }

    private void initTimingsWidgets() {
        selectAllLayout=(LinearLayout)findViewById(R.id.selectAllLayout);
        monDisabledLayout=(LinearLayout)findViewById(R.id.monDisabledLayout);
        tueDisabledLayout=(LinearLayout)findViewById(R.id.tueDisabledLayout);
        wedDisabledLayout=(LinearLayout)findViewById(R.id.wedDisabledLayout);
        thurDisabledLayout=(LinearLayout)findViewById(R.id.thurDisabledLayout);
        friDisabledLayout=(LinearLayout)findViewById(R.id.friDisabledLayout);
        satDisabledLayout=(LinearLayout)findViewById(R.id.satDisabledLayout);
        sunDisabledLayout=(LinearLayout)findViewById(R.id.sunDisabledLayout);

        monAbledLayout=(LinearLayout)findViewById(R.id.monAbledLayout);
        tueAbledLayout=(LinearLayout)findViewById(R.id.tueAbledLayout);
        wedAbledLayout=(LinearLayout)findViewById(R.id.wedAbledLayout);
        thurAbledLayout=(LinearLayout)findViewById(R.id.thurAbledLayout);
        friAbledLayout=(LinearLayout)findViewById(R.id.friAbledLayout);
        satAbledLayout=(LinearLayout)findViewById(R.id.satAbledLayout);
        sunAbledLayout=(LinearLayout)findViewById(R.id.sunAbledLayout);


        monDisabledLayout.setVisibility(View.VISIBLE);
        tueDisabledLayout.setVisibility(View.VISIBLE);
        wedDisabledLayout.setVisibility(View.VISIBLE);
        thurDisabledLayout.setVisibility(View.VISIBLE);
        friDisabledLayout.setVisibility(View.VISIBLE);
        satDisabledLayout.setVisibility(View.VISIBLE);
        sunDisabledLayout.setVisibility(View.VISIBLE);

        monAbledLayout.setVisibility(View.GONE);
        tueAbledLayout.setVisibility(View.GONE);
        wedAbledLayout.setVisibility(View.GONE);
        thurAbledLayout.setVisibility(View.GONE);
        friAbledLayout.setVisibility(View.GONE);
        satAbledLayout.setVisibility(View.GONE);
        sunAbledLayout.setVisibility(View.GONE);

        allUnchecked=(ImageView)findViewById(R.id.allUnchecked);
        monUnchecked=(ImageView)findViewById(R.id.monUnchecked);
        tueUnchecked=(ImageView)findViewById(R.id.tueUnchecked);
        wedUnchecked=(ImageView)findViewById(R.id.wedUnchecked);
        thurUnchecked=(ImageView)findViewById(R.id.thurUnchecked);
        friUnchecked=(ImageView)findViewById(R.id.friUnchecked);
        satUnchecked=(ImageView)findViewById(R.id.satUnchecked);
        sunUnchecked=(ImageView)findViewById(R.id.sunUnchecked);

        monChecked=(ImageView)findViewById(R.id.monChecked);
        tueChecked=(ImageView)findViewById(R.id.tueChecked);
        wedChecked=(ImageView)findViewById(R.id.wedChecked);
        thurChecked=(ImageView)findViewById(R.id.thurChecked);
        friChecked=(ImageView)findViewById(R.id.friChecked);
        satChecked=(ImageView)findViewById(R.id.satChecked);
        sunChecked=(ImageView)findViewById(R.id.sunChecked);




        monUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monDisabledLayout.setVisibility(View.GONE);
                monAbledLayout.setVisibility(View.VISIBLE);
                monWrk = true;
            }
        });

        monChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                monDisabledLayout.setVisibility(View.VISIBLE);
                monAbledLayout.setVisibility(View.GONE);
                monWrk = false;

            }
        });

        //tue
        tueUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tueDisabledLayout.setVisibility(View.GONE);
                tueAbledLayout.setVisibility(View.VISIBLE);
                tueWrk =true;
            }
        });

        tueChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tueDisabledLayout.setVisibility(View.VISIBLE);
                tueAbledLayout.setVisibility(View.GONE);
                tueWrk=false;
            }
        });


        //wed
        wedUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wedDisabledLayout.setVisibility(View.GONE);
                wedAbledLayout.setVisibility(View.VISIBLE);
                wedWrk =true;
            }
        });

        wedChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wedDisabledLayout.setVisibility(View.VISIBLE);
                wedAbledLayout.setVisibility(View.GONE);
                wedWrk =false;

            }
        });

        //thur
        thurUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thurDisabledLayout.setVisibility(View.GONE);
                thurAbledLayout.setVisibility(View.VISIBLE);
                thurWrk = true;
            }
        });

        thurChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thurDisabledLayout.setVisibility(View.VISIBLE);
                thurAbledLayout.setVisibility(View.GONE);
                thurWrk = false;

            }
        });


        //fri
        friUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friDisabledLayout.setVisibility(View.GONE);
                friAbledLayout.setVisibility(View.VISIBLE);
                friWrk = true;
            }
        });

        friChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friDisabledLayout.setVisibility(View.VISIBLE);
                friAbledLayout.setVisibility(View.GONE);
                friWrk = false;
            }
        });

        //sat
        satUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                satDisabledLayout.setVisibility(View.GONE);
                satAbledLayout.setVisibility(View.VISIBLE);
                satWrk = true;
            }
        });

        satChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                satDisabledLayout.setVisibility(View.VISIBLE);
                satAbledLayout.setVisibility(View.GONE);
                satWrk=false;
            }
        });


        //sun
        sunUnchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sunDisabledLayout.setVisibility(View.GONE);
                sunAbledLayout.setVisibility(View.VISIBLE);
                sunWrk = true;
            }
        });

        sunChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sunDisabledLayout.setVisibility(View.VISIBLE);
                sunAbledLayout.setVisibility(View.GONE);
                sunWrk= false;
            }
        });


        monFromSpinner=(Spinner)findViewById(R.id.monFromSpinner);
        tueFromSpinner=(Spinner)findViewById(R.id.tueFromSpinner);
        wedFromSpinner=(Spinner)findViewById(R.id.wedFromSpinner);
        thurFromSpinner=(Spinner)findViewById(R.id.thurFromSpinner);
        friFromSpinner=(Spinner)findViewById(R.id.friFromSpinner);
        satFromSpinner=(Spinner)findViewById(R.id.satFromSpinner);
        sunFromSpinner=(Spinner)findViewById(R.id.sunFromSpinner);

        monToSpinner=(Spinner)findViewById(R.id.monToSpinner);
        tueToSpinner=(Spinner)findViewById(R.id.tueToSpinner);
        wedToSpinner=(Spinner)findViewById(R.id.wedToSpinner);
        thurToSpinner=(Spinner)findViewById(R.id.thurToSpinner);
        friToSpinner=(Spinner)findViewById(R.id.friToSpinner);
        satToSpinner=(Spinner)findViewById(R.id.satToSpinner);
        sunToSpinner=(Spinner)findViewById(R.id.sunToSpinner);

        initiateSpinners(monFromSpinner);
        initiateSpinners(tueFromSpinner);
        initiateSpinners(wedFromSpinner);
        initiateSpinners(thurFromSpinner);
        initiateSpinners(friFromSpinner);
        initiateSpinners(satFromSpinner);
        initiateSpinners(sunFromSpinner);

        initiateSpinners(monToSpinner);
        initiateSpinners(tueToSpinner);
        initiateSpinners(wedToSpinner);
        initiateSpinners(thurToSpinner);
        initiateSpinners(friToSpinner);
        initiateSpinners(satToSpinner);
        initiateSpinners(sunToSpinner);

    }

    private void initiateSpinners(Spinner spinner){

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.timings_array)){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont= Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(12);


                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(12);

                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setSpinnerValue(Spinner spinner, String value){
    }

    private void getTimings(){
        String url =Constants.BASE_URL+"vendor/get-business-by-id/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onResponse: " );
                try {
                    if(response.getBoolean("success")){
                        JSONObject msg = response.getJSONObject("msg");
                        JSONObject timings = msg.getJSONObject("business_timings");

                        if(timings.getBoolean("regular_mode")){
                            JSONObject timingDetails = timings.getJSONObject("timings");

                            JSONObject monObj = timingDetails.getJSONObject("monday");
                            JSONObject tueObj = timingDetails.getJSONObject("tuesday");
                            JSONObject wedObj = timingDetails.getJSONObject("wednesday");
                            JSONObject thurObj = timingDetails.getJSONObject("thursday");
                            JSONObject friObj = timingDetails.getJSONObject("friday");
                            JSONObject satObj = timingDetails.getJSONObject("saturday");
                            JSONObject sunObj = timingDetails.getJSONObject("sunday");

                            if(monObj.getBoolean("isWorkingDay")){
                                monUnchecked.performClick();
                                monFromSpinner.setSelection(returnPosition(monObj.getString("open")));
                                monToSpinner.setSelection(returnPosition(monObj.getString("close")));
                            }
                            if(tueObj.getBoolean("isWorkingDay")){
                                tueUnchecked.performClick();
                                tueFromSpinner.setSelection(returnPosition(monObj.getString("open")));
                                tueToSpinner.setSelection(returnPosition(monObj.getString("close")));
                            }
                            if(wedObj.getBoolean("isWorkingDay")){
                                wedUnchecked.performClick();
                                wedFromSpinner.setSelection(returnPosition(monObj.getString("open")));
                                wedToSpinner.setSelection(returnPosition(monObj.getString("close")));
                            }
                            if(thurObj.getBoolean("isWorkingDay")){
                                thurUnchecked.performClick();
                                thurFromSpinner.setSelection(returnPosition(monObj.getString("open")));
                                thurToSpinner.setSelection(returnPosition(monObj.getString("close")));
                            }
                            if(friObj.getBoolean("isWorkingDay")){
                                friUnchecked.performClick();
                                friFromSpinner.setSelection(returnPosition(monObj.getString("open")));
                                friToSpinner.setSelection(returnPosition(monObj.getString("close")));
                            }
                            if(satObj.getBoolean("isWorkingDay")){
                                satUnchecked.performClick();
                                satFromSpinner.setSelection(returnPosition(monObj.getString("open")));
                                satToSpinner.setSelection(returnPosition(monObj.getString("close")));
                            }
                            if(sunObj.getBoolean("isWorkingDay")){
                                sunUnchecked.performClick();
                                sunFromSpinner.setSelection(returnPosition(monObj.getString("open")));
                                sunToSpinner.setSelection(returnPosition(monObj.getString("close")));
                            }
                        }


                    }else{
                        String msg = response.getString("msg");
                        Toast.makeText(VendorBusinessSettingsActivity.this,msg,Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(VendorBusinessSettingsActivity.this,"Couldn't fetch timings"+error.getMessage(),Toast.LENGTH_LONG).show();
                Log.e(TAG, "onErrorResponse: "+error );
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"BUSINESS TIMINGS"+businessDashboard.getBusinessId());
    }

    private int returnPosition(String time){
        int pos =-1;
        for(int i=0;i<timesList.size();i++){
            if(time.equals(timesList.get(i))){
                pos=i;
                break;
            }
        }
        return pos;
    }

    private void validateTimings(){
        if(monWrk || tueWrk || wedWrk || thurWrk || friWrk || satWrk || sunWrk){
            if(monWrk){
                if(monFromSpinner.getSelectedItemPosition()>=monToSpinner.getSelectedItemPosition()){
                    Toast.makeText(context,"Invalid monday timings",Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if(tueWrk){
                if(tueFromSpinner.getSelectedItemPosition()>=tueToSpinner.getSelectedItemPosition()){
                    Toast.makeText(context,"Invalid tuesday timings",Toast.LENGTH_LONG).show();
                    return;
                }
            }

            if(wedWrk){
                if(wedFromSpinner.getSelectedItemPosition()>=wedToSpinner.getSelectedItemPosition()){
                    Toast.makeText(context,"Invalid wednesday timings",Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if(thurWrk){
                if(thurFromSpinner.getSelectedItemPosition()>=thurToSpinner.getSelectedItemPosition()){
                    Toast.makeText(context,"Invalid thursday timings",Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if(friWrk){
                if(friFromSpinner.getSelectedItemPosition()>=friToSpinner.getSelectedItemPosition()){
                    Toast.makeText(context,"Invalid friday timings",Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if(satWrk){
                if(satFromSpinner.getSelectedItemPosition()>=satToSpinner.getSelectedItemPosition()){
                    Toast.makeText(context,"Invalid saturday timings",Toast.LENGTH_LONG).show();
                    return;
                }
            }
            if(sunWrk){
                if(sunFromSpinner.getSelectedItemPosition()>=sunToSpinner.getSelectedItemPosition()){
                    Toast.makeText(context,"Invalid sunday timings",Toast.LENGTH_LONG).show();
                    return;
                }
            }

            formString();

        }else{
            Toast.makeText(context,"Select atleast one day to set timings",Toast.LENGTH_LONG).show();
        }
    }

    private void formString(){
        JSONObject jsonObject = new JSONObject();
        PrefManager prefManager = new PrefManager(context);

        try {
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
            jsonObject.put("business_id",businessDashboard.getBusinessId());
            jsonObject.put("switch_mode",false);

            JSONObject timings = new JSONObject();

            JSONObject monObj = new JSONObject();
            if(monWrk){
                monObj.put("open",monFromSpinner.getSelectedItem().toString());
                monObj.put("close",monToSpinner.getSelectedItem().toString());
                monObj.put("isWorkingDay",true);
            }else{
                monObj.put("open","");
                monObj.put("close","");
                monObj.put("isWorkingDay",false);
            }
            timings.put("monday",monObj);

            JSONObject tueObj = new JSONObject();
            if(tueWrk){
                tueObj.put("open",tueFromSpinner.getSelectedItem().toString());
                tueObj.put("close",tueToSpinner.getSelectedItem().toString());
                tueObj.put("isWorkingDay",true);
            }else{
                tueObj.put("open","");
                tueObj.put("close","");
                tueObj.put("isWorkingDay",false);
            }
            timings.put("tuesday",tueObj);


            JSONObject wedObj = new JSONObject();
            if(wedWrk){
                wedObj.put("open",wedFromSpinner.getSelectedItem().toString());
                wedObj.put("close",wedToSpinner.getSelectedItem().toString());
                wedObj.put("isWorkingDay",true);
            }else{
                wedObj.put("open","");
                wedObj.put("close","");
                wedObj.put("isWorkingDay",false);
            }
            timings.put("wednesday",wedObj);

            JSONObject thurObj = new JSONObject();
            if(thurWrk){
                thurObj.put("open",thurFromSpinner.getSelectedItem().toString());
                thurObj.put("close",thurToSpinner.getSelectedItem().toString());
                thurObj.put("isWorkingDay",true);
            }else{
                thurObj.put("open","");
                thurObj.put("close","");
                thurObj.put("isWorkingDay",false);
            }
            timings.put("thursday",thurObj);

            JSONObject friObj = new JSONObject();
            if(friWrk){
                friObj.put("open",friFromSpinner.getSelectedItem().toString());
                friObj.put("close",friToSpinner.getSelectedItem().toString());
                friObj.put("isWorkingDay",true);
            }else{
                friObj.put("open","");
                friObj.put("close","");
                friObj.put("isWorkingDay",false);
            }
            timings.put("friday",friObj);

            JSONObject satObj = new JSONObject();
            if(satWrk){
                satObj.put("open",satFromSpinner.getSelectedItem().toString());
                satObj.put("close",satToSpinner.getSelectedItem().toString());
                satObj.put("isWorkingDay",true);
            }else{
                satObj.put("open","");
                satObj.put("close","");
                satObj.put("isWorkingDay",false);
            }
            timings.put("saturday",satObj);
            JSONObject sunObj = new JSONObject();
            if(sunWrk){
                sunObj.put("open",sunFromSpinner.getSelectedItem().toString());
                sunObj.put("close",sunToSpinner.getSelectedItem().toString());
                sunObj.put("isWorkingDay",true);
            }else{
                sunObj.put("open","");
                sunObj.put("close","");
                sunObj.put("isWorkingDay",false);
            }
            timings.put("sunday",sunObj);

            jsonObject.put("timings",timings);
            Log.e(TAG, "formString: "+jsonObject.toString());
            customProgressDialog.showDialog();
            updateTimings(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateTimings(JSONObject jsonObject){
        Log.e(TAG, "updateTimings: "+ jsonObject.toString());
        String url = Constants.BASE_URL+"vendor/post-business-timings";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: " +response.toString() );
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Updated successfully!",Toast.LENGTH_LONG).show();

                    }else{
                        Toast.makeText(context,response.getString("msg"),Toast.LENGTH_LONG).show();
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
        helper.addToRequestQueue(customJsonRequest,"UPDTETIMINGS");
    }
}
