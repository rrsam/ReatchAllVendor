package com.reatchall.charan.reatchallVendor.Vendor;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.R;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontEditText;

public class VendorBusinessEditProfileActivity extends Activity {

    private static final String TAG = "VendorBusinessEditProfi";

    ImageView editBusinessName,editContactPersonName,editAddress,editEmail,editWhatsapp,editContactNumber,editOfflineNumber;

    FontEditText businessName,contactPersonName,address,email,whatsapp,contactNumber,offlineNumber;

    FontButton saveDetails;

    ImageView backArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_business_edit_profile);

        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editBusinessName=(ImageView)findViewById(R.id.editBusinessName);
        editContactPersonName=(ImageView)findViewById(R.id.editContactPersonName);
        editAddress=(ImageView)findViewById(R.id.editaddressBusiness);
        editEmail=(ImageView)findViewById(R.id.editemailBusiness);
        editWhatsapp=(ImageView)findViewById(R.id.editwhatsappNum);
        editContactNumber=(ImageView)findViewById(R.id.editcontactNumber);
        editOfflineNumber=(ImageView)findViewById(R.id.editlandlineNumber);

        saveDetails=(FontButton)findViewById(R.id.saveDetails);
        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        businessName=(FontEditText)findViewById(R.id.businessName);
        contactPersonName=(FontEditText)findViewById(R.id.contactPersonName);
        address=(FontEditText)findViewById(R.id.addressBusiness);
        email=(FontEditText)findViewById(R.id.emailBusiness);
        whatsapp=(FontEditText)findViewById(R.id.whatsappNum);
        contactNumber=(FontEditText)findViewById(R.id.contactNumber);
        offlineNumber=(FontEditText)findViewById(R.id.landlineNumber);


        editBusinessName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                businessName.setFocusable(true);
                businessName.setFocusableInTouchMode(true);
                businessName.requestFocus();
                businessName.setSelection(businessName.getText().toString().length());
            }
        });

        editContactPersonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactPersonName.setFocusable(true);
                contactPersonName.setFocusableInTouchMode(true);
                contactPersonName.requestFocus();
                contactPersonName.setSelection(contactPersonName.getText().toString().length());
            }
        });


        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                address.setFocusable(true);
                address.setFocusableInTouchMode(true);
                address.requestFocus();
                address.setSelection(address.getText().toString().length());
            }
        });

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.setFocusable(true);
                email.setFocusableInTouchMode(true);
                email.requestFocus();
                email.setSelection(email.getText().toString().length());
            }
        });

        editContactPersonName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactPersonName.setFocusable(true);
                contactPersonName.setFocusableInTouchMode(true);
                contactPersonName.requestFocus();
                contactPersonName.setSelection(contactPersonName.getText().toString().length());
            }
        });

        editContactNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactNumber.setFocusable(true);
                contactNumber.setFocusableInTouchMode(true);
                contactNumber.requestFocus();
                contactNumber.setSelection(contactNumber.getText().toString().length());
            }
        });
        editOfflineNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offlineNumber.setFocusable(true);
                offlineNumber.setFocusableInTouchMode(true);
                offlineNumber.requestFocus();
                offlineNumber.setSelection(offlineNumber.getText().toString().length());
            }
        });
    }
}
