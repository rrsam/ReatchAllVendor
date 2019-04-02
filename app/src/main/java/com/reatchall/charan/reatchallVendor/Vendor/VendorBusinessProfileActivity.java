package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.MultiSelectSpinner;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.Categories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorBusinessProfileActivity extends AppCompatActivity {

    private static final String TAG = "VendorBusinessProfileAc";

    ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;
    Context context;
    CustomProgressDialog customProgressDialog;
    ReatchAll helper = ReatchAll.getInstance();

    FontEditText businessName,registeredName,contactPersonName,officeNumber,whatsappNum,emailBusiness;
    FontTextView businessTypeTv,categoryTv;
    MultiSelectSpinner subCategorySpinner;
    LinearLayout saveDetails,subCatLayout;

    boolean subCategoryBoolean=false;
    String businessNameData,registeredNameData,contactPersonNameData,officeNumberData,
            whatsAppNumData,businessEmailData,businessTypeName,businessTypeId,categoryNameData,categoryIdData;
    ArrayList<String> categoriesModelArrayList = new ArrayList<>();
    ArrayList<String> subCategoriesModelArrayList = new ArrayList<>();
    ArrayList<String> categoryIdsList = new ArrayList<>();
    ArrayList<String> subcategoryIdsList = new ArrayList<>();
    ArrayList<String> subcategoryIdsListAll = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_business_profile);
        context = VendorBusinessProfileActivity.this;
        customProgressDialog = new CustomProgressDialog(context);
        backArrow = (ImageView) findViewById(R.id.back_arrow);
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
        initViews();
        customProgressDialog.showDialog();
        getBusinessDetails();
    }

    private void initViews(){
        businessName=(FontEditText)findViewById(R.id.businessName);
        registeredName=(FontEditText)findViewById(R.id.registeredName);
        contactPersonName=(FontEditText)findViewById(R.id.contactPersonName);
        officeNumber=(FontEditText)findViewById(R.id.officeNumber);
        whatsappNum=(FontEditText)findViewById(R.id.whatsappNum);
        emailBusiness=(FontEditText)findViewById(R.id.emailBusiness);
        businessTypeTv=(FontTextView) findViewById(R.id.businessTypeTv);
        categoryTv=(FontTextView) findViewById(R.id.categoryTv);
        subCategorySpinner=(MultiSelectSpinner) findViewById(R.id.subCategorySpinner);
        saveDetails=(LinearLayout) findViewById(R.id.saveDetails);
        subCatLayout=(LinearLayout) findViewById(R.id.subCatLayout);

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateFields();
            }
        });
    }

    private void parseBusinessDetails(JSONObject response){
        Log.e(TAG, "parseBusinessDetails: " +response.toString());
        try{
            JSONObject parent = response.getJSONObject("msg");
            businessNameData = parent.getString("name");
            registeredNameData= parent.getString("registered_name");
            contactPersonNameData = parent.getJSONObject("contacts").getString("contact_person_name");
            officeNumberData=parent.getJSONObject("contacts").getString("office_number");
            whatsAppNumData=parent.getJSONObject("contacts").getString("whatsapp_number");
            businessEmailData=parent.getJSONObject("contacts").getString("email");
            businessTypeName =parent.getJSONObject("business_type").getString("name");
            businessTypeId =parent.getJSONObject("business_type").getString("_id");
            categoryNameData=parent.getJSONObject("category_name").getString("name");
            categoryIdData = parent.getString("category");
            subCategoriesModelArrayList = new ArrayList<>();
            subcategoryIdsList = new ArrayList<>();
            JSONArray jsonArray1 = parent.getJSONArray("sub_category");
            JSONArray jsonArray2 = parent.getJSONArray("subcategory_name");

            for(int i =0;i<jsonArray1.length();i++){
                subcategoryIdsList.add(jsonArray1.getString(i));
                subCategoriesModelArrayList.add(jsonArray2.getJSONObject(i).getString("name"));
            }
            selectedCategoryId = categoryIdData;

            businessName.setText(businessNameData);
            registeredName.setText(registeredNameData);
            businessTypeTv.setText(businessTypeName);
            categoryTv.setText(categoryNameData);
            contactPersonName.setText(contactPersonNameData);
            officeNumber.setText(officeNumberData);
            whatsappNum.setText(whatsAppNumData);
            emailBusiness.setText(businessEmailData);
            customProgressDialog.showDialog();
            getSubCategories();
        }catch (JSONException e){
            Log.e(TAG, "parseBusinessDetails: "+e.getMessage() );
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

                        parseBusinessDetails(response);

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
        helper.addToRequestQueue(customJsonRequest,"GET_BUSINESS_DETAILS");
    }

    ArrayList<Categories> categoriesArrayList;
    String selectedCategoryId;

    private void getAllCategories(String businessTypeId){
        categoriesArrayList = new ArrayList<>();
        String url=Constants.BASE_URL+"admin/get-categories-by-business-type/"+businessTypeId;
        Log.e(TAG, "getAllCategories: "+url);
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    if (response.getBoolean("success")) {
                        JSONArray Jarray = response.getJSONArray("msg");
                        categoriesModelArrayList = new ArrayList<>();

                        categoryIdsList = new ArrayList<>();
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject object = Jarray.getJSONObject(i);
//                            JSONObject image = object.getJSONObject("image");
                            Log.e(TAG, "onPostExecute: " + object.getString("name"));
                            Log.e(TAG, "onPostExecute: " + object.getString("_id"));

                            categoriesModelArrayList.add(object.getString("name"));
                            categoryIdsList.add(object.getString("_id"));
                            categoriesArrayList.add(new Categories(object.getString("_id"), object.getString("name"), object.getString("business_type"),
                                    "", ""));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Couldn't fetch Categories",Toast.LENGTH_LONG).show();
                Log.e(TAG, "onErrorResponse: "+error.toString());
            }
        });

        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"GETALLCATS");
    }

    String[] subCatsStringArray;
    ArrayList<String> selectedSubCats;
    private void getSubCategories(){
        selectedSubCats = new ArrayList<>();
        String url=Constants.BASE_URL+"admin/get-sub-cats-of-cat/"+selectedCategoryId;
        Log.e(TAG, "getSubCategories: "+url);
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        JSONArray Jarray = response.getJSONArray("msg");

                        if(Jarray.length()!=0){
                            subCatsStringArray = new String[Jarray.length()];
                            subCategoriesModelArrayList= new ArrayList<>();
                            subcategoryIdsListAll= new ArrayList<>();
                        }else{
                            subCategoriesModelArrayList=null;
                        }
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject object     = Jarray.getJSONObject(i);
                            Log.e(TAG, "onPostExecute: "+  object.getString("name"));
                            Log.e(TAG, "onPostExecute: "+   object.getString("_id"));
                            subCategoriesModelArrayList.add(object.getString("name"));
                            subCatsStringArray[i]= object.getString("name");
                            subcategoryIdsListAll.add(object.getString("_id"));
                        }

                        if(subCategoriesModelArrayList.size() ==0){
                            subCategorySpinner.setVisibility(View.GONE);
                            subCatLayout.setVisibility(View.GONE);
                        }else{
                            int[] alreadySelectedItems = new int[subcategoryIdsList.size()];
                            subCategorySpinner.setItems(subCatsStringArray);
                            for(int k =0; k<subcategoryIdsList.size();k++){
                                for(int j=0;j<subcategoryIdsListAll.size();j++){
                                    if(subcategoryIdsList.get(k).equals(subcategoryIdsListAll.get(j))){
                                        alreadySelectedItems[k] = j;
                                        selectedSubCats.add(subcategoryIdsListAll.get(j));
                                    }
                                }
                            }
                            subCategorySpinner.setSelection(alreadySelectedItems);
                            selectedSubCats= new ArrayList<>();
                            subCategorySpinner.setListener(new MultiSelectSpinner.OnMultipleItemsSelectedListener() {
                                @Override
                                public void selectedIndices(List<Integer> indices) {
                                    selectedSubCats= new ArrayList<>();
                                    for(int i=0;i<indices.size();i++){
                                        selectedSubCats.add(subcategoryIdsListAll.get(indices.get(i)));
                                    }
                                    if(indices.size()==0){
                                        subCategoryBoolean=false;
                                    }else{
                                        subCategoryBoolean=true;
                                    }
                                }

                                @Override
                                public void selectedStrings(List<String> strings) {
                                    for(int i=0;i<strings.size();i++){
                                        Log.e(TAG, "selectedStrings: "+subCategoriesModelArrayList.get(i) );
                                    }
                                }
                            });
                            subCategorySpinner.setVisibility(View.VISIBLE);
                            subCatLayout.setVisibility(View.VISIBLE);
                        }
                    }else{
                        subCatLayout.setVisibility(View.GONE);
                        subCategorySpinner.setVisibility(View.GONE);
                        subCategoryBoolean=true;
                    }


                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Toast.makeText(context,"Couldn't fetch Sub Categories",Toast.LENGTH_LONG).show();
                Log.e(TAG, "onErrorResponse: "+error.toString());
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"GETALLSUBCATS");
    }

    private void validateFields(){
        if(businessName.getText().toString().length()>0){
            if(registeredName.getText().toString().length()>0){
                if(contactPersonName.getText().toString().length()>0){
                    if(emailBusiness.getText().toString().length()>0){
                        if(selectedSubCats.size()>0){
                            formJson();
                        }else{
                            Toast.makeText(context,"Please select atleast one Sub Category",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(context,"Email can't be empty",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(context,"Contact person Name can't be empty",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(context,"Registered Name can't be empty",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(context,"Business Name can't be empty",Toast.LENGTH_LONG).show();
        }
    }

    PrefManager prefManager ;
    private void formJson(){
        prefManager = new PrefManager(context);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
            jsonObject.put("business_id",businessDashboard.getBusinessId());
            jsonObject.put("business_name",businessName.getText().toString());
            jsonObject.put("registered_name",registeredName.getText().toString());
            jsonObject.put("business_type",businessTypeId);
            jsonObject.put("category",categoryIdData);
            jsonObject.put("contact_person_name",contactPersonName.getText().toString());
            jsonObject.put("office_number",officeNumber.getText().toString());
            jsonObject.put("whatsapp_number",whatsappNum.getText().toString());
            jsonObject.put("contact_email",emailBusiness.getText().toString());

            JSONArray subJSONArray = new JSONArray();
            for(int i =0;i<selectedSubCats.size();i++){
                subJSONArray.put(selectedSubCats.get(i));
            }
            jsonObject.put("sub_categories",subJSONArray);
            customProgressDialog.showDialog();
            updateBusinessProfile(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateBusinessProfile(JSONObject jsonObject){
        String url = Constants.BASE_URL+"vendor/update-business-profile";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                customProgressDialog.hideDialog();
                try {
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Updated Successfully",Toast.LENGTH_LONG).show();
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
        helper.addToRequestQueue(customJsonRequest,"UPDATE_BUSINESS_PROFILE");
    }
}
