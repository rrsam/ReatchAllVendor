package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Offers.VendorOfferZoneNewActivity;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorDashBoardNewActivity extends AppCompatActivity {

    ImageView backArrow;
    FontTextView titleToolbar;
    private static final String TAG = "VendorDashBoardNewActivity";

    LinearLayout myCash,openCloseStatus,offerZone,viewCount,orders,ratings,businessProfile,businessPlan,privacySettings,createList;

    LinearLayout myProducts,productsList,addItem,businessAnalysis,orderAnalysis,businessSettings,businessTimings;

    LinearLayout notifications,importDatabase,cmsManage;
    LinearLayout feedBack,contactUs,webSocial,businessProofs,ads;
    LinearLayout businessPromotions,homeDelivery;
    BusinessDetails businessDetails = null;

    private PrefManager prefManager;

    LinearLayout addManager,accessManagement,changeAddress,services,changeImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_dash_board_new);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        prefManager = new PrefManager(this);
        businessDetails = getIntent().getExtras().getParcelable("businessDetails");
        if(businessDetails != null){

            Log.e(TAG, "onCreate:ID "+businessDetails.getBusinessId() +"NAME"+businessDetails.getBusinessName());
            titleToolbar.setText(businessDetails.getBusinessName());
        }

        Log.e(TAG, "onCreate: vendorId "+ prefManager.getVendorId(VendorDashBoardNewActivity.this));

        services=(LinearLayout) findViewById(R.id.services);
        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorServicesActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        accessManagement=(LinearLayout) findViewById(R.id.accessManagement);
        accessManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorAccessManagementActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        changeAddress=(LinearLayout) findViewById(R.id.changeAddress);
        changeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorUpdateAddressActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        addManager=(LinearLayout) findViewById(R.id.addManager);
        addManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorAddManagerActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        notifications=(LinearLayout) findViewById(R.id.notifications);
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorBusinessNotificationsActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });


        importDatabase=(LinearLayout) findViewById(R.id.importDatabase);
        importDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), VendorImportDatabaseActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        myCash=(LinearLayout)findViewById(R.id.myCash);
        myCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorMyCashActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        openCloseStatus=(LinearLayout)findViewById(R.id.openCloseStatus);
        openCloseStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorOpenCloseStatusActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        offerZone=(LinearLayout)findViewById(R.id.offerZone);
        offerZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorOfferZoneNewActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });


        viewCount=(LinearLayout)findViewById(R.id.viewCount);
        viewCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorViewCountActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        orders=(LinearLayout)findViewById(R.id.orders);
        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorsAppoinmentListActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        ratings=(LinearLayout)findViewById(R.id.ratingsReviews);
        ratings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorRatingsActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });
        businessProfile=(LinearLayout)findViewById(R.id.businessProfile);
        businessProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorBusinessProfileActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);

            }
        });

        businessPlan=(LinearLayout)findViewById(R.id.businessPlans);
        businessPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorBusinessPlanActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        privacySettings=(LinearLayout)findViewById(R.id.privacySettings);
        privacySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorPrivacySettingsActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);

            }
        });


        createList=(LinearLayout)findViewById(R.id.createList);
        createList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorCreateListActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        addItem=(LinearLayout)findViewById(R.id.myCustomers);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorCustomerActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        myProducts=(LinearLayout)findViewById(R.id.myProducts);
        myProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BusinessAllProductsActivity.class);
                //Intent intent = new Intent(getApplicationContext(), VendorMyProductsActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                intent.putExtra("toggle",1);
                startActivity(intent);
            }
        });

        productsList=(LinearLayout)findViewById(R.id.productList);
        productsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(getApplicationContext(), BusinessAllProductsActivity.class);
                Intent intent = new Intent(getApplicationContext(), VendorMyProductsActivity.class);
                intent.putExtra("listId","abc");
                intent.putExtra("toggle",2);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        businessAnalysis=(LinearLayout)findViewById(R.id.businessAnalysis);
        businessAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorBusinessAnalysisActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });


        orderAnalysis=(LinearLayout)findViewById(R.id.orderAnalysis);
        orderAnalysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorOrderAnalysisActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });


        businessSettings=(LinearLayout)findViewById(R.id.businessSettings);
        businessSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorBusinessSettingsNewActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        businessTimings=(LinearLayout) findViewById(R.id.businessTimings);
        businessTimings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorBusinessSettingsActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });


        homeDelivery=(LinearLayout) findViewById(R.id.homeDelivery);
        homeDelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorNewHomeDelActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
                finish();
            }
        });

        businessPromotions=(LinearLayout) findViewById(R.id.myPromotions);
        businessPromotions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorPromotionsActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        businessProofs=(LinearLayout) findViewById(R.id.businessProofs);
        businessProofs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorBusinessProofsActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        ads=(LinearLayout) findViewById(R.id.advertisements);
        ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), VendorAdvertisementsActivity.class));
            }
        });


        webSocial=(LinearLayout) findViewById(R.id.web_social);
        webSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorWebsiteSocialActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        contactUs=(LinearLayout) findViewById(R.id.contact_us);
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorContactUsActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        cmsManage=(LinearLayout) findViewById(R.id.cmsManage);
        cmsManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorCmsManageActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

        feedBack=(LinearLayout) findViewById(R.id.feedback);
        feedBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), VendorFeedbackActivity.class);
                intent.putExtra("businessDetails",businessDetails);
                startActivity(intent);
            }
        });

    }
}
