package com.reatchall.charan.reatchallVendor.Vendor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDashboard;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.Visitor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorViewDetailsActivity extends AppCompatActivity implements VisitorsAdapter.PhoneClickListener {

    private static final String TAG = "VendorViewDetailsActivi";
    ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDetails = null;
    int spinnerValue;
    ReatchAll helper;

    Spinner viewsSpinner;
    RecyclerView viewsRcv;

    VisitorsAdapter visitorsAdapter;
    ArrayList<Visitor> arrayList ;

    private String url;
    private CustomProgressDialog customProgressDialog;

    String[] permissionsRequired = new String[]{Manifest.permission.CALL_PHONE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_view_details);
        helper = ReatchAll.getInstance();
        customProgressDialog = new CustomProgressDialog(this);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        viewsSpinner=(Spinner)findViewById(R.id.viewsSpinner);
        setupViewsSpinner();

        arrayList=new ArrayList<>();
        businessDetails=getIntent().getExtras().getParcelable("businessDetails");
        spinnerValue=getIntent().getExtras().getInt("spinnerValue");
        if(spinnerValue!=0){
            if(spinnerValue==1) {
                viewsSpinner.setSelection(0);
            }
            if(spinnerValue==2) {
                viewsSpinner.setSelection(1);
            }
            if(spinnerValue==3) {
                viewsSpinner.setSelection(2);
            }
            if(spinnerValue==4) {
                viewsSpinner.setSelection(3);
            }

        }
        if(businessDetails != null) {
            titleToolbar.setText(businessDetails.getBusinessName());
            Log.e(TAG, "onCreate:ID "+businessDetails.getVendorId() +"NAME"+businessDetails.getBusinessName());
        }

        viewsRcv=(RecyclerView)findViewById(R.id.viewsRcv);
        viewsRcv.setNestedScrollingEnabled(false);
        viewsRcv.setHasFixedSize(true);



        visitorsAdapter=new VisitorsAdapter(VendorViewDetailsActivity.this,arrayList,this);
        viewsRcv.setLayoutManager(new LinearLayoutManager(VendorViewDetailsActivity.this, LinearLayoutManager.VERTICAL, false));
        viewsRcv.setAdapter(visitorsAdapter);

        if(url!=null){
            getVisits(url,viewsSpinner.getSelectedItemPosition());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            ActivityCompat.requestPermissions(VendorViewDetailsActivity.this, permissionsRequired,1001);



    }

    private void getVisits(String url,int position) {
        customProgressDialog.showDialog();
        CustomJsonRequest jsonRequest = new CustomJsonRequest(Request.Method.GET, url + businessDetails.getBusinessId(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onResponse: "+response.toString());
                try{
                    boolean status = response.getBoolean("success");
                    if(status){
                        arrayList.clear();
                        JSONObject msg = response.getJSONObject("msg");
                        JSONArray usersArray =msg.getJSONArray("users");

                        for (int i =0;i<usersArray.length();i++){
                            JSONObject userObj = usersArray.getJSONObject(i);
                            Visitor mVisitor = new Visitor();
                            mVisitor.setName(userObj.getJSONObject("user").getString("firstname"));
                            mVisitor.setNumber(userObj.getJSONObject("user").getString("mobile"));
                            mVisitor.setTotaVisits(userObj.getString("visits"));
                            mVisitor.setTime(userObj.getString("last_visited_time"));
                            arrayList.add(mVisitor);
                        }
                        Log.e(TAG, "onResponse: "+ arrayList.size());
                        visitorsAdapter.notifyDataSetChanged();

                    } else {
                        switch (position){
                            case 0:
                                Toast.makeText(VendorViewDetailsActivity.this, "No Visits Today", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(VendorViewDetailsActivity.this, "No Last Week Visits", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Toast.makeText(VendorViewDetailsActivity.this, "No Last Month Visits", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(VendorViewDetailsActivity.this, "No Last Year Visits", Toast.LENGTH_SHORT).show();
                                break;
                        }

                        int count = arrayList.size();
                        arrayList.clear();
                        visitorsAdapter.notifyItemRangeRemoved(0,count);
                    }

                }catch (Exception e){

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(helper, "Please try again", Toast.LENGTH_SHORT).show();
            }
        });
        jsonRequest.setPriority(Request.Priority.IMMEDIATE);
        helper.addToRequestQueue(jsonRequest,"VISITS");

    }


    private void setupViewsSpinner(){
        final List<String> plansList = Arrays.asList(getResources().getStringArray(R.array.viewsArray));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, plansList){
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
        viewsSpinner.setAdapter(dataAdapter);
        viewsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                  switch (i){
                      case 0:
                          url = Constants.BASE_URL+ "vendor/get-today-business-visits/";
                          break;
                      case 1:
                          url = Constants.BASE_URL+ "vendor/get-week-business-visits/";
                          break;
                      case 2:
                          url = Constants.BASE_URL+ "vendor/get-month-business-visits/";
                          break;
                      case 3:
                          url = Constants.BASE_URL+ "vendor/get-year-business-visits/";
                          break;
                  }
                getVisits(url,i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onPhoneClicked(String phonenumber) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            String dial = "tel:" + phonenumber;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        } else {
            Toast.makeText(VendorViewDetailsActivity.this, "Permission Call Phone denied", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case 1001 :
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                }else{
                    ActivityCompat.requestPermissions(VendorViewDetailsActivity.this,permissionsRequired,1001);
                }
        }
    }
}
