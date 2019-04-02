package com.reatchall.charan.reatchallVendor.Vendor;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.MultiSpinnerFilter.KeyPairBoolData;
import com.reatchall.charan.reatchallVendor.Utils.MultiSpinnerFilter.MultiSpinnerSearch;
import com.reatchall.charan.reatchallVendor.Utils.MultiSpinnerFilter.SpinnerListener;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.AllProducts;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorAddOfferActivity extends AppCompatActivity {

    private static final String TAG = "VendorAddOfferActivity";
    public static ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;

    Spinner selectOfferSpinner,offerTypeSpinner,discountSpinner;
    FontEditText minimumAmount,discountValue,startDate,startTime,endDate,endTime;
    MultiSpinnerSearch selectItemsSpinner;
    FontTextView discountTV;
    LinearLayout selectItemsLayout,minimumAmountLayout;

    List<String> selectOffers,offersType,discountTypes;
    ArrayList<AllProducts> arrayList = new ArrayList<>();
    String[] productsStringArray;
    ArrayList<AllProducts> selectedProducts;
    boolean productsSelected=false;


    Calendar startCalendar,endCalendar;
    DatePickerDialog.OnDateSetListener startDateListener,endDateListener;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    TimePickerDialog.OnTimeSetListener startTimeListener,endTimeListener;

    ReatchAll helper = ReatchAll.getInstance();
    FontButton addOfferBtn;
    PrefManager prefManager;
    CustomProgressDialog customProgressDialog;
    ArrayList<String> selectOfferStringArray = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_add_offer);
        selectOfferStringArray.add("particular-item");
        selectOfferStringArray.add("special-offer");
        selectOfferStringArray.add("overall-purchase");
        customProgressDialog = new CustomProgressDialog(VendorAddOfferActivity.this);


        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });

        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        businessDashboard=getIntent().getExtras().getParcelable("businessDetails");
        if(businessDashboard!=null){
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }
        addOfferBtn=(FontButton)findViewById(R.id.addOffer);
        addOfferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFields();
            }
        });

        initWidgets();
    }

    private void formJSon(){
        prefManager= new PrefManager(VendorAddOfferActivity.this);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vendor_id",prefManager.getVendorId(VendorAddOfferActivity.this));
            jsonObject.put("business_id",businessDashboard.getBusinessId());
            jsonObject.put("select_offer",selectOfferStringArray.get(selectOfferSpinner.getSelectedItemPosition()));
            if(minimumAmountLayout.getVisibility()==View.VISIBLE){
                jsonObject.put("min_amount",Integer.parseInt(minimumAmount.getText().toString().trim()));
                jsonObject.put("amount_flag",true);
            }
            else{
                jsonObject.put("amount_flag",false);
                jsonObject.put("min_amount","");
            }
            jsonObject.put("offer_type",offerTypeSpinner.getSelectedItem().toString());
            if(discountSpinner.getSelectedItemPosition()==0)
                jsonObject.put("discount_percentage",false);
            else
                jsonObject.put("discount_percentage",true);

            jsonObject.put("discount",Integer.parseInt(discountValue.getText().toString().trim()));
            jsonObject.put("start_date",startDate.getText().toString());
            jsonObject.put("end_date",endDate.getText().toString());

            JSONObject startTimeJson = new JSONObject();
            String getfromdate = startTime.getText().toString().trim();
            String getfrom[] = getfromdate.split(":");
            int startHour,startMin;
            startHour= Integer.parseInt(getfrom[0].trim());
            startMin = Integer.parseInt(getfrom[1].trim());

            jsonObject.put("start_time",startTimeJson);

            startTimeJson.put("hour",startHour);
            startTimeJson.put("minute",startMin);
            JSONObject endTimeJson = new JSONObject();

            String getTodate = endTime.getText().toString().trim();
            String getTo[] = getTodate.split(":");
            int endHour,endMin;
            endHour= Integer.parseInt(getTo[0].trim());
            endMin = Integer.parseInt(getTo[1].trim());

            endTimeJson.put("hour",endHour);
            endTimeJson.put("minute",endMin);
            jsonObject.put("end_time",endTimeJson);

            JSONArray selectedItemsJsonArray = new JSONArray();
            if(selectedItemIds.size()>0){
                for(int i=0;i<selectedItemIds.size();i++){
                    JSONObject selJson = new JSONObject();
                    selJson.put("_id",selectedItemIds.get(i));
                    selJson.put("name",selectedItemNames.get(i));

                    selectedItemsJsonArray.put(selJson);
                }
                jsonObject.put("item",selectedItemsJsonArray);

            }

            customProgressDialog = new CustomProgressDialog(VendorAddOfferActivity.this);
            customProgressDialog.showDialog();
            addOffer(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void addOffer(JSONObject jsonObject){
        String url = Constants.BASE_URL+"vendor/add-offer";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(VendorAddOfferActivity.this,"Success!!",Toast.LENGTH_LONG).show();
                        finish();
                    }else{
                        Toast.makeText(VendorAddOfferActivity.this,"Please Try again!!",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onErrorResponse: "+error.getMessage());
                }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest);
    }

    private void validateFields(){
        if(selectOfferSpinner.getSelectedItemPosition()==2){
            if(minimumAmount.getText().length()!=0){
                if(discountValue.getText().length()!=0){
                    if(isDateAfter(startDate.getText().toString(),endDate.getText().toString())){
                                formJSon();
                    }else{
                        Toast.makeText(VendorAddOfferActivity.this,"Invalid Start date and End date",Toast.LENGTH_LONG).show();

                    }
                }else{
                    Toast.makeText(VendorAddOfferActivity.this,"Discount Value Can't be empty",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(VendorAddOfferActivity.this,"Minimum Amount Can't be empty",Toast.LENGTH_LONG).show();
            }
        }else{
            if(selectedItemIds.size()!=0){
                if(discountValue.getText().length()!=0){
                    if(isDateAfter(startDate.getText().toString(),endDate.getText().toString())){
                        formJSon();
                    }else{
                        Toast.makeText(VendorAddOfferActivity.this,"Invalid Start date and End date",Toast.LENGTH_LONG).show();

                    }
                }else{
                    Toast.makeText(VendorAddOfferActivity.this,"Discount Value Can't be empty",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(VendorAddOfferActivity.this,"Please select Items",Toast.LENGTH_LONG).show();

            }

        }
    }
    private void initWidgets(){
        selectOfferSpinner=(Spinner)findViewById(R.id.selectOfferSpinner);
        offerTypeSpinner=(Spinner)findViewById(R.id.offerTypeSpinner);
        discountSpinner=(Spinner)findViewById(R.id.discountSpinner);
        minimumAmount=(FontEditText)findViewById(R.id.minimumAmount);
        discountValue=(FontEditText)findViewById(R.id.discountValue);
        startDate=(FontEditText)findViewById(R.id.startDate);
        startTime=(FontEditText)findViewById(R.id.startTime);
        endDate=(FontEditText)findViewById(R.id.endDate);
        endTime=(FontEditText)findViewById(R.id.endTime);
        discountTV=(FontTextView) findViewById(R.id.discountTV);
        selectItemsSpinner =(MultiSpinnerSearch)findViewById(R.id.selectItemsSpinner);
        minimumAmountLayout=(LinearLayout)findViewById(R.id.minimumAmountLayout);
        selectItemsLayout=(LinearLayout)findViewById(R.id.selectItemsLayout);
        customProgressDialog.showDialog();

        getAllProducts();
        setupOfferSpinner();
        setupOfferTypeSpinner();
        setupDiscountsTypeSpinner();

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

        startTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                startCalendar.set(Calendar.HOUR,selectedHour);
                startCalendar.set(Calendar.MINUTE,selectedMinute);
                startTime.setText(String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute));
            }
        };

        endTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                endCalendar.set(Calendar.HOUR,selectedHour);
                endCalendar.set(Calendar.MINUTE,selectedMinute);
                endTime.setText( String.format("%02d", selectedHour) + ":" + String.format("%02d", selectedMinute));
            }
        };
        startDate.setOnClickListener(view -> {
            startCalendar = Calendar.getInstance();

            datePickerDialog = new DatePickerDialog(VendorAddOfferActivity.this, startDateListener, startCalendar
                    .get(Calendar.YEAR), startCalendar.get(Calendar.MONTH),
                    startCalendar.get(Calendar.DAY_OF_MONTH));

            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

            datePickerDialog.show();
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCalendar = Calendar.getInstance();
                timePickerDialog = new TimePickerDialog(VendorAddOfferActivity.this,startTimeListener,startCalendar.get(Calendar.HOUR_OF_DAY),startCalendar.get(Calendar.MINUTE),true);
                timePickerDialog.show();
            }
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
                datePickerDialog = new DatePickerDialog(VendorAddOfferActivity.this, endDateListener,endCalendar
                        .get(Calendar.YEAR), endCalendar.get(Calendar.MONTH),
                        endCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(startCalendar.getTimeInMillis()+86400000);
                datePickerDialog.show();
            }
        });


        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endCalendar = Calendar.getInstance();
                timePickerDialog = new TimePickerDialog(VendorAddOfferActivity.this,endTimeListener,endCalendar.get(Calendar.HOUR_OF_DAY),endCalendar.get(Calendar.MINUTE),true);
                timePickerDialog.show();
            }
        });
    }

    public static boolean isDateAfter(String startDate,String endDate) {
        try
        {
            String myFormatString = "yyyy-MM-dd"; // for example
            SimpleDateFormat df = new SimpleDateFormat(myFormatString);
            Date date1 = df.parse(endDate);
            Date startingDate = df.parse(startDate);

            if (date1.after(startingDate))
                return true;
            else
                return false;
        }
        catch (Exception e)
        {

            return false;
        }
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

    private void setupOfferSpinner(){
        selectOffers = Arrays.asList(getResources().getStringArray(R.array.offers_array));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, selectOffers){
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
                if(i!=2){
                    minimumAmountLayout.setVisibility(View.GONE);
                    selectItemsLayout.setVisibility(View.VISIBLE);
                }else{
                    minimumAmountLayout.setVisibility(View.VISIBLE);
                    selectItemsLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Toast.makeText(VendorAddOfferActivity.this,"PLEASE SELECT GENDER",Toast.LENGTH_LONG).show();
            }
        });

    }
    private void setupOfferTypeSpinner(){
        offersType = Arrays.asList(getResources().getStringArray(R.array.offer_type));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, offersType){
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

    ArrayList<String> selectedItemIds = new ArrayList<>();
    ArrayList<String> selectedItemNames = new ArrayList<>();
    private void getAllProducts(){
        String url = Constants.BASE_URL+"vendor/get-items-by-businessid/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(com.android.volley.Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                JSONArray msg = null;
                try {
                    msg = response.getJSONArray("msg");

                    arrayList.clear();
                    productsStringArray = new String[msg.length()];
                    for(int i=0;i<msg.length();i++){

                        JSONObject list = msg.getJSONObject(i);
                        if(list.has("list_id")){
                            if(list.getJSONArray("item_img").length()>0){
                                arrayList.add(new AllProducts(list.getString("_id"),list.getString("list_id"),list.getString("business_id"),
                                        list.getString("vendor_id"),list.getString("item_name"),list.getString("list_id"),
                                        list.getJSONArray("item_img").getJSONObject(0).getString("url")));
                            }else{
                                arrayList.add(new AllProducts(list.getString("_id"),list.getString("list_id"),list.getString("business_id"),
                                        list.getString("vendor_id"),list.getString("item_name"),list.getString("list_id"),
                                        ""));
                            }
                        }else{
                            if(list.getJSONArray("item_img").length()>0){
                                arrayList.add(new AllProducts(list.getString("_id"),"",list.getString("business_id"),
                                        list.getString("vendor_id"),list.getString("item_name"),"",
                                        list.getJSONArray("item_img").getJSONObject(0).getString("url")));
                            }else{
                                arrayList.add(new AllProducts(list.getString("_id"),"",list.getString("business_id"),
                                        list.getString("vendor_id"),list.getString("item_name"),"",
                                        ""));
                            }
                        }

                        productsStringArray[i] = list.getString("item_name");
                    }

                    final List<KeyPairBoolData> listArray = new ArrayList<KeyPairBoolData>();

                    for(int i=0; i<arrayList.size(); i++) {
                        KeyPairBoolData h = new KeyPairBoolData();
                        h.setId(i+1);
                        h.setActualId(arrayList.get(i).getProductId());
                        h.setName(arrayList.get(i).getProductName());
                        h.setSelected(false);
                        listArray.add(h);
                    }

/***
 * -1 is no by default selection
 * 0 to length will select corresponding values
 */
                    selectItemsSpinner.setItems(listArray, -1, new SpinnerListener() {

                        @Override
                        public void onItemsSelected(List<KeyPairBoolData> items) {
                            selectedItemIds = new ArrayList<>();
                            selectedItemNames = new ArrayList<>();
                            for (int i = 0; i < items.size(); i++) {
                                if (items.get(i).isSelected()) {
                                    selectedItemIds.add(items.get(i).getActualId());
                                    selectedItemNames.add(items.get(i).getName());
                                    Log.e(TAG, i + " : " + items.get(i).getName() + items.get(i).getActualId()+" : " + items.get(i).isSelected());
                                }
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                finish();
                Toast.makeText(VendorAddOfferActivity.this,"Couldn't fetch the data",Toast.LENGTH_LONG).show();
            }
        });

        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,businessDashboard.getBusinessId()+"LISTS");
    }
}
