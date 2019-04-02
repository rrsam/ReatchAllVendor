package com.reatchall.charan.reatchallVendor.Vendor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;

import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorBusinessProofsActivity extends AppCompatActivity {

    private static final String TAG = "VendorBusinessProofsAct";

    ImageView backArrow;
    FontTextView titleToolbar;
    BusinessDetails businessDashboard;

    FontEditText gstNum,tanNum,cinNum;
    FontTextView tanApply,cinApply,gstApply,otherUpload;
    LinearLayout tanCB,gstCB,cinCB;
    ImageView gstUnCheckedIV,tanUnCheckedIV,cinUnCheckedIV;
    ImageView gstCheckedIV,tanCheckedIV,cinCheckedIV;
    LinearLayout tanUpLayout,cinUpLayout,gstUpLayout;
    boolean tanUploaded=false,gstUploaded=false,cinUploaded=false;

    LinearLayout saveBusinessProofs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vendor_activity_business_proofs);


        backArrow=(ImageView)findViewById(R.id.back_arrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        titleToolbar = (FontTextView) findViewById(R.id.title_toolbar);
        businessDashboard = getIntent().getExtras().getParcelable("businessDetails");
        if (businessDashboard != null) {
            titleToolbar.setText(businessDashboard.getBusinessName().toString());
        }


        tanCB=(LinearLayout) findViewById(R.id.tanLayout);
        gstCB=(LinearLayout)findViewById(R.id.gstLayout);
        cinCB=(LinearLayout)findViewById(R.id.cinLayout);

        tanNum=(FontEditText)findViewById(R.id.tanNum);
        gstNum=(FontEditText)findViewById(R.id.gstNum);
        cinNum=(FontEditText)findViewById(R.id.cinNum);

        tanCheckedIV=(ImageView)findViewById(R.id.tanCheckedIV);
        gstCheckedIV=(ImageView)findViewById(R.id.gstCheckedIV);
        cinCheckedIV=(ImageView)findViewById(R.id.cinCheckedIV);

        tanUnCheckedIV=(ImageView)findViewById(R.id.tanUnCheckedIV);
        gstUnCheckedIV=(ImageView)findViewById(R.id.gstUnCheckedIV);
        cinUnCheckedIV=(ImageView)findViewById(R.id.cinUnCheckedIV);

        tanCheckedIV.setVisibility(View.GONE);
        gstCheckedIV.setVisibility(View.GONE);
        cinCheckedIV.setVisibility(View.GONE);
        tanUnCheckedIV.setVisibility(View.VISIBLE);
        gstUnCheckedIV.setVisibility(View.VISIBLE);
        cinUnCheckedIV.setVisibility(View.VISIBLE);

        otherUpload=(FontTextView)findViewById(R.id.otherUpload);
        gstApply=(FontTextView)findViewById(R.id.gstApply);
        tanApply=(FontTextView)findViewById(R.id.tanApply);
        cinApply=(FontTextView)findViewById(R.id.cinApply);

        gstApply.setVisibility(View.VISIBLE);
        tanApply.setVisibility(View.VISIBLE);
        cinApply.setVisibility(View.VISIBLE);

        tanUpLayout=(LinearLayout)findViewById(R.id.tanUploaded);
        gstUpLayout=(LinearLayout)findViewById(R.id.gstUploaded);
        cinUpLayout=(LinearLayout)findViewById(R.id.cinUploaded);

        tanUpLayout.setVisibility(View.GONE);
        gstUpLayout.setVisibility(View.GONE);
        cinUpLayout.setVisibility(View.GONE);

        saveBusinessProofs=(LinearLayout)findViewById(R.id.saveProofDetails);

        tanCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tanCheckedIV.getVisibility()==View.GONE){
                    tanCheckedIV.setVisibility(View.VISIBLE);
                    tanUnCheckedIV.setVisibility(View.GONE);
                    tanApply.setText("Upload Tan");
                }else{
                    tanCheckedIV.setVisibility(View.GONE);
                    tanUnCheckedIV.setVisibility(View.VISIBLE);
                    tanApply.setText("Apply Now");
                    tanNum.getText().clear();
                    tanUpLayout.setVisibility(View.GONE);

                }
            }
        });

        gstCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gstCheckedIV.getVisibility()==View.GONE){
                    gstCheckedIV.setVisibility(View.VISIBLE);
                    gstUnCheckedIV.setVisibility(View.GONE);
                    gstApply.setText("Upload Gst");
                }else{
                    gstCheckedIV.setVisibility(View.GONE);
                    gstUnCheckedIV.setVisibility(View.VISIBLE);
                    gstApply.setText("Apply Now");
                    gstNum.getText().clear();
                    gstUpLayout.setVisibility(View.GONE);

                }
            }
        });


        cinCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cinCheckedIV.getVisibility()==View.GONE){
                    cinCheckedIV.setVisibility(View.VISIBLE);
                    cinUnCheckedIV.setVisibility(View.GONE);
                    cinApply.setText("Upload Cin");
                }else{
                    cinCheckedIV.setVisibility(View.GONE);
                    cinUnCheckedIV.setVisibility(View.VISIBLE);
                    cinApply.setText("Apply Now");
                    cinNum.getText().clear();
                    cinUpLayout.setVisibility(View.GONE);

                }
            }
        });

        tanApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tanApply.getText().toString().contains("Apply")){
                    tanUploaded=false;
                }else{
                    tanUpLayout.setVisibility(View.VISIBLE);
                    tanUploaded=true;

                }
            }
        });

        gstApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gstApply.getText().toString().contains("Apply")){
                    gstUploaded=false;
                }else{
                    gstUpLayout.setVisibility(View.VISIBLE);
                    gstUploaded=true;

                }
            }
        });

        cinApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cinApply.getText().toString().contains("Apply")){
                    cinUploaded=false;
                }else{
                    cinUpLayout.setVisibility(View.VISIBLE);
                    cinUploaded=true;

                }
            }
        });


        saveBusinessProofs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(gstCheckedIV.getVisibility()== View.GONE || (gstCheckedIV.getVisibility()==View.VISIBLE && gstNum.getText().toString().length()!=0)){
                    if(tanCheckedIV.getVisibility()== View.GONE || (tanCheckedIV.getVisibility()==View.VISIBLE && tanNum.getText().toString().length()!=0)){
                        if(cinCheckedIV.getVisibility()== View.GONE || (cinCheckedIV.getVisibility()==View.VISIBLE && cinNum.getText().toString().length()!=0)){
                            if(gstCheckedIV.getVisibility()==View.VISIBLE && gstUploaded){
                                if(tanCheckedIV.getVisibility()==View.VISIBLE && tanUploaded){
                                    if(cinCheckedIV.getVisibility()==View.VISIBLE && cinUploaded){

                                        finish();



                                    }else{
                                        Toast.makeText(VendorBusinessProofsActivity.this,"Please upload CIN file",Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(VendorBusinessProofsActivity.this,"Please upload TAN file",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(VendorBusinessProofsActivity.this,"Please upload GST file",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(VendorBusinessProofsActivity.this,"CIN FIELD MISSING",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(VendorBusinessProofsActivity.this,"TAN FIELD MISSING",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(VendorBusinessProofsActivity.this,"GST FIELD MISSING",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
