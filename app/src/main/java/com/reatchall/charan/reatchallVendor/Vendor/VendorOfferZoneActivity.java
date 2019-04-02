package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.ConfirmationDialog;
import com.reatchall.charan.reatchallVendor.Utils.OfferTypeDialog;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Offers.VendorAddBuzOfferActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Offers.VendorAddItemOfferActivity;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorOfferZoneActivity extends AppCompatActivity {

    private static final String TAG = "VendorOfferZoneActivity";

    public static ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;

    LinearLayout viewOffers,addOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_offer_zone);

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


        addOffer=(LinearLayout)findViewById(R.id.addOfferLayout);
        addOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OfferTypeDialog mAlert = new OfferTypeDialog(VendorOfferZoneActivity.this);
                mAlert.setPositveButton("Business Offer", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlert.dismiss();
                        Log.e(TAG, "onClick: BUZ OFFER" );
                        Intent intent = new Intent(VendorOfferZoneActivity.this,VendorAddBuzOfferActivity.class);
                        intent.putExtra("businessDetails",businessDashboard);
                        startActivity(intent);

                    }
                });

                mAlert.setNegativeButton("OrderedItem Offer", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAlert.dismiss();
                        Intent intent = new Intent(VendorOfferZoneActivity.this,VendorAddItemOfferActivity.class);
                        intent.putExtra("businessDetails",businessDashboard);
                        startActivity(intent);
                    }
                });
                mAlert.show();

            }
        });

        viewOffers=(LinearLayout)findViewById(R.id.viewOfferLayout);
        viewOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VendorOfferZoneActivity.this,VendorViewOffersActivity.class);
                intent.putExtra("businessDetails",businessDashboard);
                startActivity(intent);
            }
        });
    }


}
