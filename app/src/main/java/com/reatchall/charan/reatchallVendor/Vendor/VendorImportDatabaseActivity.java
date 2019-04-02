package com.reatchall.charan.reatchallVendor.Vendor;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VendorImportDatabaseActivity extends AppCompatActivity {

    private static final String TAG = "VendorImportDatabaseAct";
    ImageView backArrow;
    FontTextView titleToolbar;

    ArrayList<String> categoriesModelArrayList = new ArrayList<>();
    ArrayList<String> subCategoriesModelArrayList = new ArrayList<>();
    ArrayList<String> categoryIdsList = new ArrayList<>();
    ArrayList<String> subcategoryIdsList = new ArrayList<>();
    String subCategoryString = null;

    Spinner categorySpinner, subCategorySpinner;
    boolean categoryBoolean=false,subCategoryBoolean=false;

    LinearLayout linearLayout,databaseLayout;
    RecyclerView listRcv;

    FontTextView databaseTitle,subCatTV;
    ImageView databaseTitleDownload,databaseTitleDownloadDone;

    BusinessDetails businessDashboard;

    ImportDataParentAdapter importDataParentAdapter;

    ArrayList<String> arrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_import_database);
        linearLayout=(LinearLayout)findViewById(R.id.linearLayout);

        backArrow=(ImageView)findViewById(R.id.back_arrow);
        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);

        businessDashboard = getIntent().getExtras().getParcelable("businessDetails");
        if(businessDashboard != null){

            Log.e(TAG, "onCreate:ID "+businessDashboard.getBusinessId() +"NAME"+businessDashboard.getBusinessName());
            titleToolbar.setText(businessDashboard.getBusinessName());
        }


        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });


        categorySpinner=(Spinner)findViewById(R.id.categorySpinner);
        subCatTV=(FontTextView)findViewById(R.id.subCategoryTV);
        subCatTV.setVisibility(View.GONE);
        subCategorySpinner=(Spinner)findViewById(R.id.subCategorySpinner);



        new GetAllCategories().execute();

        databaseTitle=(FontTextView)findViewById(R.id.databaseTitle);
        databaseTitleDownload=(ImageView)findViewById(R.id.databaseTitleDownload);
        databaseTitleDownloadDone=(ImageView)findViewById(R.id.databaseTitleDownloadDone);
        databaseLayout=(LinearLayout)findViewById(R.id.databaseLayout);
        databaseLayout.setVisibility(View.GONE);

        listRcv=(RecyclerView)findViewById(R.id.listRcv);



    }




    private class GetAllCategories extends AsyncTask<String, String, String> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(VendorImportDatabaseActivity.this);
            dialog.setMessage("please wait.");
            dialog.show();
        }


        Response response;

        @Override
        protected String doInBackground(String... urlkk) {
            try {
                OkHttpClient client = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://13.127.169.96:3000/admin/get-all-categories")
                        .get()
                        .addHeader("cache-control", "no-cache")
                        .build();

                response = client.newCall(request).execute();

                if(response !=null){
                    if (response.isSuccessful()) {


                        Log.e(TAG, "onPostExecute: "+response.body() );
                        String jsonData = null;
                        try {
                            jsonData = response.body().string();
                            JSONObject Jobject = new JSONObject(jsonData);
                            JSONArray Jarray = Jobject.getJSONArray("msg");
                            categoriesModelArrayList= new ArrayList<>();
                            categoriesModelArrayList.add("Select a Category");

                            categoryIdsList= new ArrayList<>();
                            categoryIdsList.add("0");
                            for (int i = 0; i < Jarray.length(); i++) {
                                JSONObject object     = Jarray.getJSONObject(i);
                                Log.e(TAG, "onPostExecute: "+  object.getString("name"));
                                Log.e(TAG, "onPostExecute: "+   object.getString("_id"));

                                categoriesModelArrayList.add(object.getString("name"));
                                categoryIdsList.add(object.getString("_id"));

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    } else {
                        Log.e(TAG, "onPostExecute: NOT SUCCESSFULL");
                        new GetAllCategories().execute();

                    }
                }else{
                    Log.e(TAG, "onPostExecute: NULL " );
                    new GetAllCategories().execute();
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            dialog.dismiss();
            ArrayAdapter<String> dataAdapterCategories = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, categoriesModelArrayList) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);

                    Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                    ((TextView) v).setTypeface(externalFont);
                    ((TextView) v).setTextSize(14);


                    return v;
                }

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);

                    Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                    ((TextView) v).setTypeface(externalFont);
                    ((TextView) v).setTextSize(14);

                    return v;
                }
            };
            dataAdapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(dataAdapterCategories);
            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.e(TAG, "onItemSelected: ITEM"+adapterView.getItemAtPosition(i) );
                    Log.e(TAG, "onItemSelected: ITEM ID"+adapterView.getItemIdAtPosition(i) );

                    if(i!=0){
                        categoryBoolean=true;
                        subCategoryString=categoryIdsList.get(i);
                        if(subCategoryString != null){
                            Log.e(TAG, "onItemSelected: SUB CAT STRING NOT NULL" );
                            new GetSubCategories().execute();
                        }
                    }else{
                        categoryBoolean=false;
                        subCategoryBoolean=true;
                        subCategorySpinner.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    categoryBoolean=false;

                    Toast.makeText(getApplicationContext(),"PLEASE SELECT A CATEGORY",Toast.LENGTH_LONG).show();
                }
            });
        }

    }
    private class GetSubCategories extends AsyncTask<String,String,String>{
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(VendorImportDatabaseActivity.this);
            dialog.setMessage("please wait.");
            dialog.show();
        }

        Response response;
        @Override
        protected String doInBackground(String... strings) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://13.127.169.96:3000/vendor/get-sub-cats-of-cat/"+subCategoryString)
                    .get()
                    .build();

            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }


            if(response !=null){
                if (response.isSuccessful()) {


                    Log.e(TAG, "onPostExecute: "+response.body() );
                    String jsonData = null;
                    try {
                        jsonData = response.body().string();
                        JSONObject Jobject = new JSONObject(jsonData);
                        JSONArray Jarray = Jobject.getJSONArray("msg");


                        if(Jarray.length()!=0){
                            subCategoriesModelArrayList= new ArrayList<>();
                            subCategoriesModelArrayList.add("Select a Sub category");
                            subcategoryIdsList.add("0");

                        }else{
                            subCategoriesModelArrayList=null;
                        }
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject object     = Jarray.getJSONObject(i);
                            Log.e(TAG, "onPostExecute: "+  object.getString("name"));
                            Log.e(TAG, "onPostExecute: "+   object.getString("_id"));
                            subCategoriesModelArrayList.add(object.getString("name"));
                            subcategoryIdsList.add(object.getString("_id"));

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e(TAG, "onPostExecute: NOT SUCCESSFULL");
                    new GetAllCategories().execute();

                }
            }else{
                Log.e(TAG, "onPostExecute: NULL " );
                new GetSubCategories().execute();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(subCategoriesModelArrayList != null){

                dialog.dismiss();

                Log.e(TAG, "onPostExecute: "+subCategoriesModelArrayList.size() );

                ArrayAdapter<String> dataAdapterCategoriess = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, subCategoriesModelArrayList) {
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);

                        Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                        ((TextView) v).setTypeface(externalFont);
                        ((TextView) v).setTextSize(14);


                        return v;
                    }

                    @Override
                    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View v = super.getDropDownView(position, convertView, parent);

                        Typeface externalFont = Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                        ((TextView) v).setTypeface(externalFont);
                        ((TextView) v).setTextSize(14);

                        return v;
                    }
                };
                dataAdapterCategoriess.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                subCategorySpinner.setAdapter(dataAdapterCategoriess);
                subCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Log.e(TAG, "onItemSelected: ITEM"+adapterView.getItemAtPosition(i) );
                        Log.e(TAG, "onItemSelected: ITEM ID"+adapterView.getItemIdAtPosition(i) );

                        if(i!=0){
                            subCategoryBoolean=true;

                            databaseLayout.setVisibility(View.VISIBLE);
                            databaseTitle.setText(subCategorySpinner.getSelectedItem().toString().toUpperCase()+" DATABASE");
                            databaseTitleDownload.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    databaseTitleDownload.setVisibility(View.GONE);
                                    databaseTitleDownloadDone.setVisibility(View.VISIBLE);
                                }
                            });
                            loadDatabase();


                        }else{
                            subCategoryBoolean=false;
                            databaseLayout.setVisibility(View.GONE);
                            databaseTitleDownload.setVisibility(View.VISIBLE);
                            databaseTitleDownloadDone.setVisibility(View.GONE);

                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        subCategoryBoolean=false;
                        databaseLayout.setVisibility(View.VISIBLE);
                        databaseTitleDownload.setVisibility(View.VISIBLE);
                        databaseTitleDownloadDone.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"PLEASE SELECT A SUB CATEGORY",Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                dialog.dismiss();

            }

            if(subCategoriesModelArrayList == null){
                Log.e(TAG, "onPostExecute: NULL" );
                subCategorySpinner.setVisibility(View.GONE);
                subCatTV.setVisibility(View.GONE);
                subCategoryBoolean=true;


                databaseLayout.setVisibility(View.VISIBLE);
                databaseTitleDownload.setVisibility(View.VISIBLE);
                databaseTitleDownloadDone.setVisibility(View.GONE);

                databaseLayout.setVisibility(View.VISIBLE);
                databaseTitle.setText(categorySpinner.getSelectedItem().toString().toUpperCase()+" DATABASE");
                databaseTitleDownload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        databaseTitleDownload.setVisibility(View.GONE);
                        databaseTitleDownloadDone.setVisibility(View.VISIBLE);
                    }
                });
                loadDatabase();

            }else{
                if(subCategoriesModelArrayList.size()==0){
                    Log.e(TAG, "onPostExecute: ELSE IF");
                    subCategorySpinner.setVisibility(View.GONE);
                    subCatTV.setVisibility(View.GONE);

                    subCategoryBoolean=true;

                    databaseLayout.setVisibility(View.VISIBLE);
                    databaseTitleDownload.setVisibility(View.VISIBLE);
                    databaseTitleDownloadDone.setVisibility(View.GONE);

                    databaseLayout.setVisibility(View.VISIBLE);
                    databaseTitle.setText(categorySpinner.getSelectedItem().toString().toUpperCase()+" DATABASE");
                    databaseTitleDownload.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            databaseTitleDownload.setVisibility(View.GONE);
                            databaseTitleDownloadDone.setVisibility(View.VISIBLE);
                        }
                    });
                    loadDatabase();
                }else{
                    Log.e(TAG, "onPostExecute: ELSE ELSE" );
                    subCategoryBoolean=false;
                    subCatTV.setVisibility(View.VISIBLE);

                    subCategorySpinner.setVisibility(View.VISIBLE);
                }

            }

        }
    }


    private void loadDatabase(){
        for(int i=1;i<=9;i++){
            arrayList.add("LIST NAME "+i);

        }

        listRcv.setNestedScrollingEnabled(false);
        importDataParentAdapter=new ImportDataParentAdapter(VendorImportDatabaseActivity.this,arrayList);
        listRcv.setHasFixedSize(true);
        listRcv.setLayoutManager(new LinearLayoutManager(VendorImportDatabaseActivity.this, LinearLayoutManager.VERTICAL, false));
        listRcv.setAdapter(importDataParentAdapter);

    }



}
