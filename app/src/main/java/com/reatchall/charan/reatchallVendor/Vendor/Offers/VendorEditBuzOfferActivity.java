package com.reatchall.charan.reatchallVendor.Vendor.Offers;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
import com.reatchall.charan.reatchallVendor.Utils.StringConstants;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BuzOfferType;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BuzOffers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorEditBuzOfferActivity extends AppCompatActivity {
    private static final String TAG = "VendorAddBuzOfferActivi";


    Context context;
    CustomProgressDialog customProgressDialog;
    PrefManager prefManager;
    ReatchAll helper = ReatchAll.getInstance();

    public static ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;
    BuzOffers buzOffers;

    Spinner offerTypeSpinner,discountSpinner,selectOfferSpinner;

    FontEditText discountValue,minAmountValue,maxDiscountValue;
    FontTextView startDate,endDate,discountTV,maxDiscountTV;
    FontButton addOffer;
    LinearLayout maxDiscountLayout,discountTypeLayout,minimumAmountLayout;

    Calendar startCalendar,endCalendar;
    DatePickerDialog.OnDateSetListener startDateListener,endDateListener;
    DatePickerDialog datePickerDialog;

    List<String> discountTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_add_buz_offer);
        context = VendorEditBuzOfferActivity.this;
        customProgressDialog = new CustomProgressDialog(context);
        prefManager = new PrefManager(context);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
        buzOffers=getIntent().getExtras().getParcelable(StringConstants.BUZ_OFFER);
        if(businessDashboard!=null){
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }

        if(buzOffers!=null){
            Log.e(TAG, "onCreate: "+buzOffers.isExclusive() );
        }else{
            Log.e(TAG, "onCreate: NULL" );
        }

        initViews();
    }

    private void initViews(){
        offerTypeSpinner=(Spinner)findViewById(R.id.offerTypeSpinner);
        discountSpinner=(Spinner)findViewById(R.id.discountSpinner);
        selectOfferSpinner=(Spinner)findViewById(R.id.selectOfferSpinner);
        discountValue=(FontEditText)findViewById(R.id.discountValue);
        minAmountValue=(FontEditText)findViewById(R.id.minAmountValue);
        maxDiscountValue=(FontEditText)findViewById(R.id.maxDiscountValue);
        startDate=(FontTextView)findViewById(R.id.startDate);
        endDate=(FontTextView)findViewById(R.id.endDate);
        discountTV=(FontTextView)findViewById(R.id.discountTV);
        maxDiscountTV=(FontTextView)findViewById(R.id.maxDiscountTV);
        addOffer=(FontButton) findViewById(R.id.addOffer);
        maxDiscountLayout=(LinearLayout) findViewById(R.id.maxDiscountLayout);
        discountTypeLayout=(LinearLayout) findViewById(R.id.discountTypeLayout);
        minimumAmountLayout=(LinearLayout) findViewById(R.id.minimumAmountLayout);

        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                startCalendar.set(Calendar.YEAR, year);
                startCalendar.set(Calendar.MONTH, monthOfYear);
                startCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateStartDate();
            }
        };

        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                endCalendar.set(Calendar.YEAR, year);
                endCalendar.set(Calendar.MONTH, monthOfYear);
                endCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateEndDate();
            }
        };


        startDate.setOnClickListener(view -> {
            startCalendar = Calendar.getInstance();

            datePickerDialog = new DatePickerDialog(context, startDateListener, startCalendar
                    .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                    startCalendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

            datePickerDialog.show();
        });

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getfromdate = startDate.getText().toString().trim();
                String getfrom[] = getfromdate.split("-");
                int year,month,day;
                year= Integer.parseInt(getfrom[0]);
                month = Integer.parseInt(getfrom[1]);
                day = Integer.parseInt(getfrom[2]);

                Log.e(TAG, "onClick: "+year + month +day);

                endCalendar = Calendar.getInstance();

                //endCalendar.set(year,month,day+1);
                endCalendar.setTimeInMillis(startCalendar.getTimeInMillis());
                Log.e(TAG, "onClick: "+endCalendar.getTime().toString());
                datePickerDialog = new DatePickerDialog(context, endDateListener,endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(startCalendar.getTimeInMillis()+86400000);
                datePickerDialog.show();
            }
        });

        addOffer.setText("UPDATE");


        addOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        customProgressDialog.showDialog();
        setupSelectOfferSpinner();
        setupOfferTypeSpinner();
        setupDiscountsTypeSpinner();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setupOffer();
            }
        },1000);
    }

    private void setupOffer(){


        if(buzOffers.isExclusive()){
            minimumAmountLayout.setVisibility(View.GONE);
            selectOfferSpinner.setSelection(1);
            if(buzOffers.isFlat()){
                Log.e(TAG, "setupOffer:FLAT EXC " );
                offerTypeSpinner.setSelection(0);
                maxDiscountLayout.setVisibility(View.GONE);
                if(buzOffers.isRupees()){
                    Log.e(TAG, "setupOffer: FLAT EXC RUPE" );
                    discountTV.setText("Discount(in rupees)");
                    discountSpinner.setSelection(0);
                }else{
                    Log.e(TAG, "setupOffer: FLAT EXC PER" );
                    discountTV.setText("Discount(in %)");
                    discountSpinner.setSelection(1);
                }

            }else{
                Log.e(TAG, "setupOffer: UPTOEXC" );
                offerTypeSpinner.setSelection(1);
                maxDiscountLayout.setVisibility(View.VISIBLE);
                discountSpinner.setSelection(1);
                discountTV.setText("Discount(in %)");
                maxDiscountValue.setText(buzOffers.getMaxDiscount()+"");
            }
            discountValue.setText(buzOffers.getDiscountValue()+"");
        }

        if(!buzOffers.isExclusive()){
            selectOfferSpinner.setSelection(0);
            minimumAmountLayout.setVisibility(View.VISIBLE);
            if(buzOffers.isFlat()){
                Log.e(TAG, "setupOffer:FLAT OVR " );
                offerTypeSpinner.setSelection(0);
                maxDiscountLayout.setVisibility(View.GONE);
                if(buzOffers.isRupees()){
                    Log.e(TAG, "setupOffer: FLAT OVR RUPE" );
                    discountTV.setText("Discount(in rupees)");
                    discountSpinner.setSelection(0);
                }else{
                    Log.e(TAG, "setupOffer: FLAT OVR PER" );
                    discountTV.setText("Discount(in %)");
                    discountSpinner.setSelection(1);
                }
            }else{
                Log.e(TAG, "setupOffer: UPTOOVR" );

                offerTypeSpinner.setSelection(1);
                maxDiscountLayout.setVisibility(View.VISIBLE);
                discountSpinner.setSelection(1);
                discountTV.setText("Discount(in %)");
                maxDiscountValue.setText(buzOffers.getMaxDiscount()+"");
            }
            discountValue.setText(buzOffers.getDiscountValue()+"");
            minAmountValue.setText(buzOffers.getMinAmount()+"");
        }

        startDate.setText(buzOffers.getStartDate().substring(0,10));
        endDate.setText(buzOffers.getEndDate().substring(0,10));
        customProgressDialog.hideDialog();
    }

    private void validate(){
        if(buzOfferTypes.get(selectOfferSpinner.getSelectedItemPosition()).isOverall()){
            if(!minAmountValue.getText().toString().isEmpty() && Integer.parseInt(discountValue.getText().toString())>0){
                if(offerTypeSpinner.getSelectedItemPosition()==0){
                    if(discountSpinner.getSelectedItemPosition()==0){
                        if(!discountValue.getText().toString().isEmpty() && Integer.parseInt(discountValue.getText().toString())>0){
                            if(!startDate.getText().toString().isEmpty()){
                                if(!endDate.getText().toString().isEmpty()){
                                    //addOffer
                                    formString();
                                }else{
                                    Toast.makeText(context,"Invalid end date ",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(context,"Invalid start date ",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(context,"Invalid discount value",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        if(!discountValue.getText().toString().isEmpty() && Integer.parseInt(discountValue.getText().toString())>0 &&
                                Integer.parseInt(discountValue.getText().toString())<100){
                            if(!startDate.getText().toString().isEmpty()){
                                if(!endDate.getText().toString().isEmpty()){
                                    //addOffer
                                    formString();
                                }else{
                                    Toast.makeText(context,"Invalid end date ",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(context,"Invalid start date ",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(context,"Invalid discount value",Toast.LENGTH_LONG).show();
                        }
                    }

                }else{
                    if(!maxDiscountValue.getText().toString().isEmpty() && Integer.parseInt(maxDiscountValue.getText().toString())>0){
                        if(!discountValue.getText().toString().isEmpty() && Integer.parseInt(discountValue.getText().toString())>0 &&
                                Integer.parseInt(discountValue.getText().toString())<100){
                            if(!startDate.getText().toString().isEmpty()){
                                if(!endDate.getText().toString().isEmpty()){
                                    //addOffer
                                    formString();

                                }else{
                                    Toast.makeText(context,"Invalid end date ",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(context,"Invalid start date ",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(context,"Invalid discount value",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context,"Invalid Max Discount value",Toast.LENGTH_LONG).show();
                    }
                }
            }else{
                Toast.makeText(context,"Invalid minimum value",Toast.LENGTH_LONG).show();
            }

        }else{
            //exclusive
            if(offerTypeSpinner.getSelectedItemPosition()==0){
                if(discountSpinner.getSelectedItemPosition()==0){
                    if(!discountValue.getText().toString().isEmpty() && Integer.parseInt(discountValue.getText().toString())>0){
                        if(!startDate.getText().toString().isEmpty()){
                            if(!endDate.getText().toString().isEmpty()){
                                //addOffer
                                formString();

                            }else{
                                Toast.makeText(context,"Invalid end date ",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(context,"Invalid start date ",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context,"Invalid discount value",Toast.LENGTH_LONG).show();
                    }
                }else{
                    if(!discountValue.getText().toString().isEmpty() && Integer.parseInt(discountValue.getText().toString())>0 &&
                            Integer.parseInt(discountValue.getText().toString())<100){
                        if(!startDate.getText().toString().isEmpty()){
                            if(!endDate.getText().toString().isEmpty()){
                                //addOffer
                                formString();

                            }else{
                                Toast.makeText(context,"Invalid end date ",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(context,"Invalid start date ",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context,"Invalid discount value",Toast.LENGTH_LONG).show();
                    }
                }

            }else{
                if(!maxDiscountValue.getText().toString().isEmpty() && Integer.parseInt(maxDiscountValue.getText().toString())>0){
                    if(!discountValue.getText().toString().isEmpty() && Integer.parseInt(discountValue.getText().toString())>0 &&
                            Integer.parseInt(discountValue.getText().toString())<100){
                        if(!startDate.getText().toString().isEmpty()){
                            if(!endDate.getText().toString().isEmpty()){
                                //addOffer
                                formString();

                            }else{
                                Toast.makeText(context,"Invalid end date ",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(context,"Invalid start date ",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context,"Invalid discount value",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(context,"Invalid Max Discount value",Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private void formString(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
            jsonObject.put("business_id",businessDashboard.getBusinessId());
            jsonObject.put("offer_id",buzOffers.getOfferId());
            jsonObject.put("offer_type",buzOfferTypes.get(selectOfferSpinner.getSelectedItemPosition()).getOfferId());
            if(buzOfferTypes.get(selectOfferSpinner.getSelectedItemPosition()).isOverall()){
                jsonObject.put("exclusive",false);
                jsonObject.put("minAmount",Integer.parseInt(minAmountValue.getText().toString()));

            }else{
                jsonObject.put("exclusive",true);
                jsonObject.put("minAmount",0);
            }

            if(offerTypeSpinner.getSelectedItemPosition()==0){
                jsonObject.put("flat",true);
                if(discountSpinner.getSelectedItemPosition()==0){
                    jsonObject.put("rupees",true);
                }else{
                    jsonObject.put("rupees",false);
                }
                jsonObject.put("discount",Integer.parseInt(discountValue.getText().toString()));
                jsonObject.put("maxDiscount",0);

            }else{
                jsonObject.put("flat",false);
                jsonObject.put("rupees",false);
                jsonObject.put("discount",Integer.parseInt(discountValue.getText().toString()));
                jsonObject.put("maxDiscount",Integer.parseInt(maxDiscountValue.getText().toString()));
            }

            jsonObject.put("startDate",startDate.getText().toString());
            jsonObject.put("endDate",endDate.getText().toString());

            customProgressDialog.showDialog();
            addBuzOffer(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void addBuzOffer(JSONObject jsonObject){
        String url = Constants.BASE_URL+"vendor/edit-businessoffer";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Offer added successfully",Toast.LENGTH_LONG).show();
                        finish();
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
                Toast.makeText(context,"Please try again!",Toast.LENGTH_LONG).show();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest);
    }

    private void updateStartDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        startDate.setText(sdf.format(startCalendar.getTime()));
    }

    private void updateEndDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        endDate.setText(sdf.format(endCalendar.getTime()));
    }

    List<String> offerTypes;
    private void setupOfferTypeSpinner(){
        offerTypes = Arrays.asList(getResources().getStringArray(R.array.offer_type));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, offerTypes){
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
        offerTypeSpinner.setAdapter(dataAdapter);
        offerTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                if(i==0){
                    discountTypeLayout.setVisibility(View.VISIBLE);
                    maxDiscountLayout.setVisibility(View.GONE);
                }else{
                    discountTypeLayout.setVisibility(View.GONE);
                    discountSpinner.setSelection(1);
                    discountTV.setText("Discount (In %) :");
                    maxDiscountLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Toast.makeText(VendorAddOfferActivity.this,"PLEASE SELECT GENDER",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupDiscountsTypeSpinner(){
        discountTypes = Arrays.asList(getResources().getStringArray(R.array.discounts_type));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, discountTypes){
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
        discountSpinner.setAdapter(dataAdapter);
        discountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                if(i==0){
                    discountTV.setText("Discount (In Rupees) :");
                }else{
                    discountTV.setText("Discount (In %) :");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Toast.makeText(VendorAddOfferActivity.this,"PLEASE SELECT GENDER",Toast.LENGTH_LONG).show();
            }
        });
    }


    ArrayList<BuzOfferType> buzOfferTypes;
    List<String> buzOfferNames;
    private void setupSelectOfferSpinner(){
        String url = Constants.BASE_URL+"admin/get-all-business_offers";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                buzOfferTypes = new ArrayList<>();
                buzOfferNames = new ArrayList<>();
                try {
                    if(response.getBoolean("success")){
                        JSONArray msg = response.getJSONArray("msg");
                        for(int i=0;i<msg.length();i++){
                            buzOfferTypes.add(new BuzOfferType(msg.getJSONObject(i).getString("name"),msg.getJSONObject(i).getString("_id")
                                    ,msg.getJSONObject(i).getBoolean("type")));
                            buzOfferNames.add(msg.getJSONObject(i).getString("name"));
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, buzOfferNames){
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
                        selectOfferSpinner.setAdapter(dataAdapter);
                        selectOfferSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
                                if(buzOfferTypes.get(i).isOverall()){
                                    minimumAmountLayout.setVisibility(View.VISIBLE);
                                }else{
                                    minimumAmountLayout.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                //Toast.makeText(VendorAddOfferActivity.this,"PLEASE SELECT GENDER",Toast.LENGTH_LONG).show();
                            }
                        });

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
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest);
    }

}
