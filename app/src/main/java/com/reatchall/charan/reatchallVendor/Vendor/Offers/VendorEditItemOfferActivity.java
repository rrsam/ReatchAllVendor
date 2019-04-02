package com.reatchall.charan.reatchallVendor.Vendor.Offers;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
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
import com.reatchall.charan.reatchallVendor.Utils.MultiSpinnerFilter.KeyPairBoolData;
import com.reatchall.charan.reatchallVendor.Utils.MultiSpinnerFilter.MultiSpinnerSearch;
import com.reatchall.charan.reatchallVendor.Utils.MultiSpinnerFilter.SpinnerListener;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Utils.StringConstants;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ItemOffer;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ItemOfferType;
import com.reatchall.charan.reatchallVendor.Vendor.Models.NewProduct;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ReviewItemOffer;

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

public class VendorEditItemOfferActivity extends AppCompatActivity implements ReviewOfferItemsPopupFragment.ReviewListener {
    private static final String TAG = "VendorAddItemOfferActiv";
    Context context;
    CustomProgressDialog customProgressDialog;
    PrefManager prefManager;
    ReatchAll helper = ReatchAll.getInstance();

    public static ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;
    ItemOffer itemOffer;

    Spinner offerTypeSpinner,discountSpinner;
    MultiSpinnerSearch selectItemsSpinner;
    FontEditText discountValue,buyValue,getValue;
    FontTextView startDate,endDate,discountTV;
    FontButton addOffer;
    LinearLayout exclusiveLayout,clearanceLayout;

    Calendar startCalendar,endCalendar;
    DatePickerDialog.OnDateSetListener startDateListener,endDateListener;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    List<String> discountTypes;
    ArrayList<ItemOfferType> itemOfferTypeArrayList;
    ArrayList<NewProduct> productsArrayList;
    ArrayList<String> selectedItemIds = new ArrayList<>();
    ArrayList<String> selectedItemNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_add_item_offer);
        context = VendorEditItemOfferActivity.this;
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
        itemOffer=getIntent().getExtras().getParcelable(StringConstants.ITEM_OFFER);
        if(businessDashboard!=null){
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }

           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                ((ViewGroup) findViewById(R.id.parentLayoutNew)).getLayoutTransition()
                        .enableTransitionType(LayoutTransition.CHANGING);
            }*/

       initViews();
    }

    private void initViews(){
        offerTypeSpinner=(Spinner)findViewById(R.id.offerTypeSpinner);
        discountSpinner=(Spinner)findViewById(R.id.discountSpinner);
        selectItemsSpinner=(MultiSpinnerSearch)findViewById(R.id.selectItemsSpinner);
        discountValue=(FontEditText)findViewById(R.id.discountValue);
        buyValue=(FontEditText)findViewById(R.id.buyValue);
        getValue=(FontEditText)findViewById(R.id.getValue);
        startDate=(FontTextView)findViewById(R.id.startDate);
        endDate=(FontTextView)findViewById(R.id.endDate);
        discountTV=(FontTextView)findViewById(R.id.discountTV);
        addOffer=(FontButton) findViewById(R.id.addOffer);
        exclusiveLayout=(LinearLayout) findViewById(R.id.exclusiveLayout);
        clearanceLayout=(LinearLayout) findViewById(R.id.clearanceLayout);

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
        setupOfferTypeSpinner();
    }

    private void reviewItems(){
        boolean negValue = false;
        ArrayList<ReviewItemOffer> reviewItemOffers = new ArrayList<>();
        for(int i=0;i<selectedItemIds.size();i++){
            for(int j=0;j<productsArrayList.size();j++){
                if(selectedItemIds.get(i).equals(productsArrayList.get(j).getItemId())){
                    if(discountSpinner.getSelectedItemPosition()==0){
                        reviewItemOffers.add(new ReviewItemOffer(productsArrayList.get(j).getItemId(),productsArrayList.get(j).getItemName(),
                                true,(int) productsArrayList.get(j).getPrice(),Integer.parseInt(discountValue.getText().toString())));
                        if(((int) productsArrayList.get(j).getPrice()-Integer.parseInt(discountValue.getText().toString()))<0){
                            negValue=true;
                        }
                    }else{
                        reviewItemOffers.add(new ReviewItemOffer(productsArrayList.get(j).getItemId(),productsArrayList.get(j).getItemName(),
                                false,(int) productsArrayList.get(j).getPrice(),Integer.parseInt(discountValue.getText().toString())));
                        if(((int) productsArrayList.get(j).getPrice()-Integer.parseInt(discountValue.getText().toString()))<0){
                            negValue=true;
                        }
                    }

                }
            }
        }

        boolean finalNegValue = negValue;

        /*ConfirmItemOffer confirmItemOffer = new ConfirmItemOffer(context);
        confirmItemOffer.setPositveButton("Edit", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmItemOffer.dismiss();
                selectItemsSpinner.performClick();
            }
        });

        confirmItemOffer.setNegativeButton("Confirm", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(finalNegValue){
                    Toast.makeText(context,"Please remove items with negative price value",Toast.LENGTH_LONG).show();
                }else{
                    confirmItemOffer.dismiss();
                    customProgressDialog.showDialog();
                    addItemOffer();
                }
            }
        });

        confirmItemOffer.setupRcv(reviewItemOffers);
        confirmItemOffer.show();*/

        ReviewOfferItemsPopupFragment dialog = new ReviewOfferItemsPopupFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(StringConstants.OFFER_ITEMS_ARRAY, reviewItemOffers);
        args.putBoolean("NEG", negValue);
        dialog.setArguments(args);
        dialog.show(VendorEditItemOfferActivity.this.getFragmentManager(), "dialog");

    }

    private void validate(){
        if(itemOfferTypeArrayList.get(offerTypeSpinner.getSelectedItemPosition()).isExclusive()){
            if(selectedItemIds.size()!=0){
               if(!discountValue.getText().toString().isEmpty()){
                   if(!startDate.getText().toString().isEmpty()){
                       if(!endDate.getText().toString().isEmpty()){
                           if(discountSpinner.getSelectedItemPosition()==0){
                               reviewItems();
                           }else{
                               if(Integer.parseInt(discountValue.getText().toString())<100) {
                                   reviewItems();
                               }else{
                                   Toast.makeText(context,"Discount percentage should be less than 100 percent",Toast.LENGTH_LONG).show();
                               }
                           }
                       }else{
                           Toast.makeText(context,"Please select start date",Toast.LENGTH_LONG).show();
                       }
                   }else{
                       Toast.makeText(context,"Please select start date",Toast.LENGTH_LONG).show();
                   }
               }else{
                   Toast.makeText(context,"Please enter discount value",Toast.LENGTH_LONG).show();
               }
            }else{
                Toast.makeText(context,"Please select atleast one item",Toast.LENGTH_LONG).show();
            }
        }else{
            if(selectedItemIds.size()!=0){
                if(!buyValue.getText().toString().isEmpty()){
                    if(!startDate.getText().toString().isEmpty()){
                        if(!endDate.getText().toString().isEmpty()){
                            if(!buyValue.getText().toString().isEmpty()){
                                customProgressDialog.showDialog();
                                addItemOffer();
                            }else {
                                Toast.makeText(context,"Please enter get value",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(context,"Please select start date",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context,"Please select start date",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(context,"Please enter buy value",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(context,"Please select atleast one item",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void addItemOffer(){
        String url = Constants.BASE_URL+"vendor/edit-itemoffer";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
            jsonObject.put("business_id",businessDashboard.getBusinessId());
            jsonObject.put("offer_id",itemOffer.getOfferId());
            jsonObject.put("offer_type",itemOfferTypeArrayList.get(offerTypeSpinner.getSelectedItemPosition()).getItemOfferTypeId());
            if(itemOfferTypeArrayList.get(offerTypeSpinner.getSelectedItemPosition()).isExclusive()){
                jsonObject.put("offer",true);
                if(discountSpinner.getSelectedItemPosition()==0){
                    jsonObject.put("rupees",true);
                }else{
                    jsonObject.put("rupees",false);
                }
                jsonObject.put("discount",Integer.parseInt(discountValue.getText().toString()));
                jsonObject.put("buy_value",0);
                jsonObject.put("get_value",0);

            }
            else{
                jsonObject.put("offer",false);
                if(discountSpinner.getSelectedItemPosition()==0){
                    jsonObject.put("rupees",true);
                }else{
                    jsonObject.put("rupees",false);
                }
                jsonObject.put("discount",0);
                jsonObject.put("buy_value",Integer.parseInt(buyValue.getText().toString()));
                jsonObject.put("get_value",Integer.parseInt(getValue.getText().toString()));
            }

            JSONArray selectedItemsArray = new JSONArray();
            for(int i =0;i<selectedItemIds.size();i++){
                selectedItemsArray.put(selectedItemIds.get(i));
            }
            jsonObject.put("item",selectedItemsArray);
            jsonObject.put("start_date",startDate.getText().toString());
            jsonObject.put("end_date",endDate.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Offer added successfully",Toast.LENGTH_LONG).show();
                        finish();
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
        helper.addToRequestQueue(customJsonRequest,"ADD_ITEM_OFFER");
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

    List<String> itemOfferTypes ;
    private void setupOfferTypeSpinner(){
        String url = Constants.BASE_URL+"admin/get-all-item_offers";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    Log.e(TAG, "onResponse: "+response.toString() );
                    if(response.getBoolean("success")){
                        JSONArray msg = response.getJSONArray("msg");
                        itemOfferTypeArrayList = new ArrayList<>();
                        itemOfferTypes = new ArrayList<>();
                        for(int i=0;i<msg.length();i++){
                            itemOfferTypeArrayList.add(new ItemOfferType(msg.getJSONObject(i).getString("_id"),
                                    msg.getJSONObject(i).getString("name"),msg.getJSONObject(i).getBoolean("type"),
                                    msg.getJSONObject(i).getBoolean("status")));
                            itemOfferTypes.add(msg.getJSONObject(i).getString("name"));
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, itemOfferTypes){
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
                                if(itemOfferTypeArrayList.get(i).isExclusive()){
                                    exclusiveLayout.setVisibility(View.VISIBLE);
                                    setupDiscountsTypeSpinner();
                                    clearanceLayout.setVisibility(View.GONE);
                                }else{
                                    exclusiveLayout.setVisibility(View.GONE);
                                    clearanceLayout.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                //Toast.makeText(VendorAddOfferActivity.this,"PLEASE SELECT GENDER",Toast.LENGTH_LONG).show();
                            }
                        });


                    }
                    customProgressDialog.showDialog();
                    getAllProducts();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onErrorResponse: " );
                customProgressDialog.showDialog();
                getAllProducts();
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"ITEM_OFFER_TYPES");
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
    private void getAllProducts(){
        String url = Constants.BASE_URL+"vendor/get-items-by-businessid/"+businessDashboard.getBusinessId();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                selectItemsSpinner.setClickable(true);

                try {
                    if(response.getBoolean("success")){
                        selectItemsSpinner.setClickable(true);
                        productsArrayList = new ArrayList<>();
                        JSONArray msg = response.getJSONArray("msg");
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

                        final List<KeyPairBoolData> listArray = new ArrayList<KeyPairBoolData>();
                       /* for(int i=0; i<productsArrayList.size(); i++) {
                            KeyPairBoolData h = new KeyPairBoolData();
                            h.setId(i+1);
                            h.setActualId(productsArrayList.get(i).getItemId());
                            h.setName(productsArrayList.get(i).getItemName());
                            h.setSelected(false);
                            listArray.add(h);
                        }*/

                        for(int i=0; i<productsArrayList.size(); i++) {
                            KeyPairBoolData h = new KeyPairBoolData();
                            h.setId(i+1);
                            h.setActualId(productsArrayList.get(i).getItemId());
                            h.setName(productsArrayList.get(i).getItemName());
                            for(int j=0;j<itemOffer.getItemIds().size();j++){
                                h.setSelected(false);
                                if(productsArrayList.get(i).getItemId().equals(itemOffer.getItemIds().get(j))){
                                    h.setSelected(true);
                                    break;
                                }
                            }
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


                        setupfields();

                    }else{
                        selectItemsSpinner.setClickable(false);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                selectItemsSpinner.setClickable(false);
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"GET_ALL_PRODS");
    }

    private void setupfields(){
        if(itemOffer.isOfferType()){
            offerTypeSpinner.setSelection(0);
            exclusiveLayout.setVisibility(View.VISIBLE);
            clearanceLayout.setVisibility(View.GONE);
            if(itemOffer.isRupees()){
                discountSpinner.setSelection(0);
                discountTV.setText("Discount(in Rupees)");
            }else{
                discountSpinner.setSelection(1);
                discountTV.setText("Discount(in %)");
            }
            discountValue.setText(itemOffer.getDiscountValue()+"");

        }else{
            offerTypeSpinner.setSelection(1);
            exclusiveLayout.setVisibility(View.GONE);
            clearanceLayout.setVisibility(View.VISIBLE);
            buyValue.setText(itemOffer.getBuyValue()+"");
            getValue.setText(itemOffer.getGetValue()+"");
        }

        startDate.setText(itemOffer.getStartDate().substring(0,10));
        endDate.setText(itemOffer.getEndDate().substring(0,10));
    }

    @Override
    public void onEdit() {
        selectItemsSpinner.performClick();
    }

    @Override
    public void onConfirm() {
        customProgressDialog.showDialog();
        addItemOffer();
    }
}
