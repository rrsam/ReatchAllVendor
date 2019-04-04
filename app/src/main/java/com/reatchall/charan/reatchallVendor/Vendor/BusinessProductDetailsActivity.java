package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Adapters.BusinessAllProductsAdapter;
import com.reatchall.charan.reatchallVendor.Vendor.Models.AllProducts;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.NewProduct;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;


public class BusinessProductDetailsActivity extends AppCompatActivity {


    private static final String TAG = "BusinessProductDetailsActivity";
    ImageView backArrow;
    FontTextView titleToolbar,subTitle;
    ReatchAll helper = ReatchAll.getInstance();

    CustomProgressDialog customProgressDialog;
    BusinessDetails businessDetails = null;
    private Context mContext;
    private NewProduct productDetails;

    FontTextView productName,productBrand,productQuality,productQuantity,productType,productPrice,
                productDiscount,productFinalPrice,productDescripiton,productSpecification;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_product_details);
        mContext = BusinessProductDetailsActivity.this;
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        titleToolbar = (FontTextView) findViewById(R.id.title_toolbar);
        subTitle=(FontTextView)findViewById(R.id.subTitle);

        businessDetails = getIntent().getExtras().getParcelable("businessDetails");
        productDetails = getIntent().getExtras().getParcelable("productDetails");
        if(productDetails!=null) {
            titleToolbar.setText(productDetails.getItemName());
            subTitle.setText(productDetails.getItemName());
        }

        init();
    }


    public void init(){
        productName = findViewById(R.id.product_details_name);
        productBrand = findViewById(R.id.product_details_brand);
        productQuality = findViewById(R.id.product_details_quality);
        productQuantity = findViewById(R.id.product_details_quantity);
        productType = findViewById(R.id.product_details_prod_type);
        productPrice = findViewById(R.id.product_details_act_price);
        productDiscount = findViewById(R.id.product_details_disc);
        productFinalPrice = findViewById(R.id.product_details_final_pri);
        productDescripiton = findViewById(R.id.product_details_desc);
        productSpecification = findViewById(R.id.product_details_spec);

        getItemDetails(productDetails.getItemId());

    }

    private void getItemDetails(String itemId) {
        String url  = Constants.BASE_URL + "vendor/get-item-details/"+itemId;

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    boolean success = response.getBoolean("success");
                    if(success){
                        String key;
                        JSONObject singleObject = response.getJSONObject("msg");
                        productName.setText(singleObject.getString("name"));
                        productBrand.setText(singleObject.getString("brand"));
                        productQuality.setText(singleObject.getString("quality"));
                        String quantity = String.valueOf(singleObject.getString("quantity"))+ " " + singleObject.getJSONObject("units").getString("name");
                        productQuantity.setText(quantity);
                        productType.setText(singleObject.getString("product_type"));
                        boolean rupee = singleObject.getBoolean("rupees");
                        int discountValue = singleObject.getInt("discount");
                        int actual_price = singleObject.getInt("actual_price");
                        productPrice.setText("Rs " + String.valueOf(actual_price));
                        if(rupee){
                            productDiscount.setText("Rs " + String.valueOf(String.valueOf(discountValue)));
                            productFinalPrice.setText("Rs " + String.valueOf(actual_price-discountValue));
                        }else{
                            productDiscount.setText(String.valueOf(String.valueOf(discountValue))+ "%" );
                            productFinalPrice.setText("Rs " + String.valueOf(actual_price-(actual_price/discountValue)));
                        }
                        productDescripiton.setText(singleObject.getString("description"));
                        productSpecification.setText(singleObject.getString("specification"));

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
        helper.addToRequestQueue(customJsonRequest,"PROD_DETAILS");


    }
}
