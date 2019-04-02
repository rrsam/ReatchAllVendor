package com.reatchall.charan.reatchallVendor.Vendor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.Models.UserReviews;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorRatingsActivity extends AppCompatActivity {

    ImageView backArrow;

    RecyclerView recyclerView;
    VendorRatingReviewsAdapter vendorRatingReviewsAdapter;
    ArrayList<UserReviews> arrayList;
    ArrayList<Integer> averageRatings;

    BusinessDetails businessDashboard;
    FontTextView titleToolbar;

    FontTextView overallRatings;

    private static final String TAG = "VendorRatingsActivity";

    ReatchAll helper = ReatchAll.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_ratings);
        backArrow = (ImageView) findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleToolbar = (FontTextView) findViewById(R.id.title_toolbar);
        overallRatings = (FontTextView) findViewById(R.id.overall_rating);

        businessDashboard = getIntent().getExtras().getParcelable("businessDetails");
        if (businessDashboard != null) {

            Log.e(TAG, "onCreate:ID " + businessDashboard.getBusinessId() + "NAME" + businessDashboard.getBusinessName());
            titleToolbar.setText(businessDashboard.getBusinessName());
        }


        recyclerView = (RecyclerView) findViewById(R.id.ratings_reviews_rcv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        arrayList = new ArrayList<>();
        averageRatings = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(VendorRatingsActivity.this, LinearLayoutManager.VERTICAL, false));
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getRatings();
    }

    private void getRatings() {
        String url = Constants.BASE_URL + "vendor/get-business-ratings/"+businessDashboard.getBusinessId();

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new com.android.volley.Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getBoolean("success")) {
                        JSONArray msgArray = response.getJSONArray("msg");
                        if(msgArray.length()>0){
                            for (int i = 0; i < msgArray.length(); i++) {
                                JSONObject ratingObject = msgArray.getJSONObject(i);
                                JSONObject userObject = ratingObject.getJSONObject("user");
                                arrayList.add(new UserReviews(userObject.getString("firstname"),String.valueOf(ratingObject.getInt("rating"))
                                        ,ratingObject.getString("review"),""));
                                averageRatings.add(ratingObject.getInt("rating"));
                            }
                            vendorRatingReviewsAdapter = new VendorRatingReviewsAdapter(arrayList, getApplicationContext());
                            recyclerView.setAdapter(vendorRatingReviewsAdapter);
                            calculateAvgRating(averageRatings);


                        }

                    } else {
                        Toast.makeText(VendorRatingsActivity.this, "Please try again!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        customJsonRequest.setPriority(com.android.volley.Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest, "POST LIST" + businessDashboard.getBusinessId());
    }

    private void calculateAvgRating(ArrayList<Integer> averageRatings) {
        int totalRatings = 0;
        float avgRating=0;
        for (int i =0;i< averageRatings.size();i++) {
            totalRatings = totalRatings + averageRatings.get(i);
        }
        avgRating = (float) totalRatings / averageRatings.size();

        if(avgRating == 5){
            overallRatings.setBackground(getDrawable(R.drawable.green_circle));
            overallRatings.setText(String.valueOf(avgRating));
        }else if(avgRating < 5 && avgRating > 2){
            overallRatings.setBackground(getDrawable(R.drawable.yellow_circle));
            overallRatings.setText(String.valueOf(avgRating));
        }else{
            overallRatings.setBackground(getDrawable(R.drawable.red_circle));
            overallRatings.setText(String.valueOf(avgRating));
        }
    }
}
