package com.reatchall.charan.reatchallVendor.Vendor.CreateBusiness;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.reatchall.charan.reatchallVendor.Models.PaymentModesModel;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.MultiSelectSpinner;
import com.reatchall.charan.reatchallVendor.Utils.OnSpinerItemClick;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Utils.SpinnerDialog;
import com.reatchall.charan.reatchallVendor.Vendor.Maps.MarkLocationActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessController;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.Models.Categories;
import com.reatchall.charan.reatchallVendor.Vendor.Models.Cities;
import com.reatchall.charan.reatchallVendor.Vendor.Models.Districts;
import com.reatchall.charan.reatchallVendor.Vendor.Models.SellerType;
import com.reatchall.charan.reatchallVendor.Vendor.Models.States;
import com.reatchall.charan.reatchallVendor.Vendor.Models.VendorBusinessType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

import fr.arnaudguyon.smartfontslib.FontCheckBox;
import fr.arnaudguyon.smartfontslib.FontEditText;
import fr.arnaudguyon.smartfontslib.FontTextView;

import static com.reatchall.charan.reatchallVendor.Registration.RegisterActivity.EmailValidator;
import static com.reatchall.charan.reatchallVendor.Registration.RegisterActivity.PhoneNumberValidator;

public class VendorCreateBusinessActivity extends AppCompatActivity implements OnMapReadyCallback{

    private static final String TAG = "VendorCreateBusinessAct";

    ImageView backArrow;
    FontTextView titleToolbar;

    MultiSelectSpinner subCategorySpinner;
    Spinner businessTypeSpinner,sellerTypeSpinner,plansSpinner;
    FontTextView subCatTV,subCategoryImpTV;
    Spinner categorySpinner, planSpinner,statesSpinner;
    boolean categoryBoolean=false,statesBoolean =false,planBoolean=false,subCategoryBoolean=false,subCatZero=false;
    ArrayList<String> categoriesModelArrayList = new ArrayList<>();
    ArrayList<String> subCategoriesModelArrayList = new ArrayList<>();
    ArrayList<String> categoryIdsList = new ArrayList<>();
    ArrayList<String> subcategoryIdsList = new ArrayList<>();
    boolean modesPayment[] = new boolean[5];
    List<String> list;
    String subCategoryString = null;

    CheckBox serviceCB, productCB;

    CheckBox monCB, tueCB, wedCB, thurCB, friCB, satCB, sunCB;
    CheckBox selectAllCB;
    FontTextView monTv, tueTv, wedTv, thurTv, friTv, satTv, sunTv;
    FontTextView monTvNew, tueTvNew, wedTvNew, thurTvNew, friTvNew, satTvNew, sunTvNew;
    RelativeLayout monTimeLayout,tueTimeLayout,wedTimeLayout,thurTimeLayout,friTimeLayout,satTimeLayout,sunTimeLayout;
    ImageView editMon,editTue,editWed,editThur,editFri,editSat,editSun;
    String openMon, openTue, openWed, openThur, openFri, openSat, openSun,open,openAll;
    String closeMon, closeTue, closeWed, closeThur, closeFri, closeSat, closeSun,close,closeAll;
    boolean workingMon=false, workingTue=false, workingWed=false, workingThur=false, workingfri=false, workingSat=false, workingSun=false;

    boolean product = false, service = false;

    FontEditText businessName, registeredName,aboutBusiness,pinCode,doorNo,streetLocality,landmark,contactName, contactNumber, landline, whatsapp, emailBusiness, addressBusiness;
    FontEditText alternateNumber,aboutContactPerson,aadharNumber;
    FontEditText webLink,instaLink,twitterLink,fbLink,youtubeLink,linkedInLink,googleplus,instagram;
    RelativeLayout getCurrentlocation;
    LinearLayout saveWebSocial,saveTimings;

    FontTextView stateName, cityName,districtName;
    FontEditText countryName;
    LinearLayout saveControllerDetails,saveDetails,saveContactDetails,saveBusinessProofs;

    LocationManager locationManager;
    private double latitude, longitude;
    String addBusinessString="";

    LinearLayout managerLayout,createBusinessLayout, businessContactLayout, businessProofsLayout, businessWebSocialLayout,businessTimingsLayout;
    PrefManager prefManager;
    ProgressDialog dialog;

    ReatchAll helper = ReatchAll.getInstance();

    FontEditText gstNum,tanNum,cinNum,rnNum;
    FontTextView tanApply,cinApply,gstApply,rnApply,otherUpload;
    LinearLayout tanCB,gstCB,cinCB,rnCB;
    ImageView gstUnCheckedIV,tanUnCheckedIV,cinUnCheckedIV,rnUnCheckedIV;
    ImageView gstCheckedIV,tanCheckedIV,cinCheckedIV,rnCheckedIV;
    LinearLayout tanUpLayout,cinUpLayout,gstUpLayout,rnUpLayout;
    boolean tanUploaded=false,gstUploaded=false,cinUploaded=false,rnUploaded=false;


    //modesofPayment
    boolean cashB=false,paytmB=false,creditB=false,netB=false,othersB=false, oneB=false;
    LinearLayout cashCB,paytmCB,creditCB,netCB,othersCB;
    ImageView cashUnCheckedIV,creditUnCheckedIV,netBankUnCheckedIV,paytmUnCheckedIV,othersPaymentUnCheckedIV;
    ImageView cashCheckedIV,creditCheckedIV,netBankCheckedIV,paytmCheckedIV,othersPaymentCheckedIV;

    ScrollView scrollView;



    int openAllNew,closeAllNew;

    FontTextView allCheckTv;
    ImageView allCheckIV,monCheckIV,tueCheckIV,wedCheckIV,thurCheckIV,friCheckIV,satCheckIV,sunCheckIV;
    ImageView allUncheckIV,monUnCheckIV,tueUnCheckIV,wedUnCheckIV,thurUnCheckIV,friUnCheckIV,satUnCheckIV,sunUnCheckIV;

    Spinner monOpenSpinner,tueOpenSpinner,wedOpenSpinner,thurOpenSpinner,friOpenSpinner,satOpenSpinner,sunOpenSpinner;
    Spinner monCloseSpinner,tueCloseSpinner,wedCloseSpinner,thurCloseSpinner,friCloseSpinner,satCloseSpinner,sunCloseSpinner;

    List<String> plansList;


    //MARK LOCATION WIDGETS
    LinearLayout mapLayout;
    private GoogleMap mMap;
    private static final float DEFAULT_ZOOM = 18f;
    boolean mapReady=false;
    boolean coordsSet=false;


    //Manager Layout Widgets
    FontCheckBox myselfManagerCB,othersManagerCB;
    LinearLayout othersManagerLayout;
    Spinner controllerTypeSpinner,controllerGenderSpinner;
    FontEditText controllerUserName,controllerPassword,controllerMobile,controllerEmail,controllerName;
    boolean isManagerMyself = false,isManagerOthers = false;
    String businessCntlrTypeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_create_business);
        backArrow=(ImageView)findViewById(R.id.back_arrow);
        titleToolbar=(FontTextView)findViewById(R.id.title_toolbar);
        managerLayout =(LinearLayout)findViewById(R.id.managerLayout);
        createBusinessLayout=(LinearLayout)findViewById(R.id.createBusinessLayout);
        businessContactLayout=(LinearLayout)findViewById(R.id.businessContactDetails);
        businessProofsLayout=(LinearLayout)findViewById(R.id.businessProofsLayout);
        businessWebSocialLayout=(LinearLayout)findViewById(R.id.webSocialLayout);
        businessTimingsLayout=(LinearLayout)findViewById(R.id.businessTimingsLayoutNew);

        scrollView=(ScrollView)findViewById(R.id.scrollView);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customBackStack();
            }
        });

        prefManager = new PrefManager(VendorCreateBusinessActivity.this);
        Log.e(TAG, "onCreate: "+prefManager.getVendorId(VendorCreateBusinessActivity.this) );

        businessWebSocialLayout.setVisibility(View.GONE);
        businessContactLayout.setVisibility(View.GONE);
        businessProofsLayout.setVisibility(View.GONE);
        businessTimingsLayout.setVisibility(View.GONE);
        createBusinessLayout.setVisibility(View.GONE);
        managerLayout.setVisibility(View.VISIBLE);
        titleToolbar.setText("Create Business");

        saveControllerDetails = (LinearLayout)findViewById(R.id.saveControllerDetails);
        saveDetails=(LinearLayout) findViewById(R.id.saveDetails);
        saveContactDetails=(LinearLayout)findViewById(R.id.saveContactDetails);
        saveBusinessProofs=(LinearLayout)findViewById(R.id.saveProofDetails);
        saveWebSocial=(LinearLayout)findViewById(R.id.saveWebDetails);
        saveTimings=(LinearLayout)findViewById(R.id.submitNew);

        saveControllerDetails .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateControllerLayout();
            }
        });

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateCreateBusinessNnavigate();
            }
        });

        saveContactDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateBusinessContactsNnavigate();
            }
        });

        saveBusinessProofs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateBusinessProofsNnavigate();
            }
        });

        saveWebSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* titleToolbar.setText("Create Business");
                businessWebSocialLayout.setVisibility(View.GONE);
                businessContactLayout.setVisibility(View.GONE);
                businessProofsLayout.setVisibility(View.GONE);
                createBusinessLayout.setVisibility(View.GONE);
                businessTimingsLayout.setVisibility(View.VISIBLE);
                // Scroll the view so that the touched editText is near the top of the scroll view
                new Thread(new Runnable()
                {
                    @Override
                    public
                    void run ()
                    {
                        // Determine where to set the scroll-to to by measuring the distance from the top of the scroll view
                        // to the control to focus on by summing the "top" position of each view in the hierarchy.
                        int yDistanceToControlsView = 0;
                        View parentView = (View) selectAllCB.getParent();
                        while (true)
                        {
                            if (parentView.equals(scrollView))
                            {
                                break;
                            }
                            yDistanceToControlsView += parentView.getTop();
                            parentView = (View) parentView.getParent();
                        }

                        // Compute the final position value for the top and bottom of the control in the scroll view.
                        final int topInScrollView = yDistanceToControlsView + selectAllCB.getTop();
                        final int bottomInScrollView = yDistanceToControlsView + selectAllCB.getBottom();

                        // Post the scroll action to happen on the scrollView with the UI thread.
                        scrollView.post(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                int height =selectAllCB.getHeight();
                                scrollView.smoothScrollTo(0, ((topInScrollView + bottomInScrollView) / 2) - height);
                                selectAllCB.requestFocus();
                            }
                        });
                    }
                }).start();*/

                Log.e(TAG, "onClick: ");
                formJsonNew();

            }
        });

        saveTimings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e(TAG, "onClick: TIMINGS");
                retrieveTimesNew();
                if(workingMon || workingTue || workingWed || workingThur || workingfri || workingSat || workingSun){
                    Log.e(TAG, "onClick: IF YES" );
                    //formJSON();
                    formJSONnew();
                }
                else{
                    Toast.makeText(VendorCreateBusinessActivity.this,"Atleast 1 day should be a working day",Toast.LENGTH_LONG).show();
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    99);
        }


        initWidgets();
        saveControllerDetails.performClick();
    }

    private void showMap(Double latitude,Double longitude){
        moveCamera(new LatLng(latitude,longitude),DEFAULT_ZOOM,"Shop Location");
        mapLayout.setVisibility(View.VISIBLE);
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(VendorCreateBusinessActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
        mapReady=true;

    }

    MarkerOptions options;
    private void moveCamera(LatLng latLng, float zoom, String title){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
        if(options!=null){
            mMap.clear();
        }
        options = new MarkerOptions()
                .position(latLng)
                .title(title);
        mMap.addMarker(options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2)
        {
            Log.e(TAG, "onActivityResult: " );
            if(resultCode==RESULT_OK){
                Double lat=data.getDoubleExtra("latitude",0.0);
                Double longt=data.getDoubleExtra("longitude",0.0);
                Log.e(TAG, "onActivityResult: "+lat+"LONG"+longt );
                //startBtn.setText(latitude+"D"+longitude);

                latitude=lat;
                longitude=longt;
                coordsSet=true;
                if(mapReady){
                    Log.e(TAG, "onActivityResult: IFYES" );
                    showMap(lat,longt);
                }else{
                    Log.e(TAG, "onActivityResult: ELSE" );
                    initMap();
                    if(mapReady)
                        showMap(lat,longt);
                }
            }else{
                if(resultCode==RESULT_CANCELED)
                    Log.e(TAG, "onActivityResult: RES CANCE" );
                else{
                    Log.e(TAG, "onActivityResult: NOT OK" );
                    Double lat=data.getDoubleExtra("latitude",0.0);
                    Double longt=data.getDoubleExtra("longitude",0.0);
                    if(lat!=null && longt !=null){
                        Log.e(TAG, "onActivityResult: "+latitude+"LONG"+longitude );
                        //startBtn.setText(latitude+"D"+longitude);
                        latitude=lat;
                        longitude=longt;
                        coordsSet=true;
                        if(mapReady){
                            Log.e(TAG, "onActivityResult: IFYES" );
                            showMap(lat,longt);
                        }else{
                            Log.e(TAG, "onActivityResult: ELSE" );
                            initMap();
                            if(mapReady)
                                showMap(lat,longt);
                        }
                    }
                }
            }
        }
    }


    boolean tanChecked=false,gstChecked=false,cinChecked=false,rnChecked=false;
    private void initWidgets(){

        //Manager layout widgets
        othersManagerLayout = (LinearLayout)findViewById(R.id.othersManagerLayout);
        myselfManagerCB=(FontCheckBox)findViewById(R.id.myselfManagerCB);
        othersManagerCB=(FontCheckBox)findViewById(R.id.othersManagerCB);
        controllerTypeSpinner=(Spinner)findViewById(R.id.controllerTypeSpinner);
        controllerGenderSpinner=(Spinner)findViewById(R.id.controllerGenderSpinner);
        controllerUserName=(FontEditText)findViewById(R.id.controllerUserName);
        controllerPassword=(FontEditText)findViewById(R.id.controllerPassword);
        controllerMobile=(FontEditText)findViewById(R.id.controllerMobile);
        controllerEmail=(FontEditText)findViewById(R.id.controllerEmail);
        controllerName=(FontEditText)findViewById(R.id.controllerName);

        getBusinessControllersData();
        setupGenderSpinner();

        myselfManagerCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isManagerMyself){
                    myselfManagerCB.setChecked(false);
                    othersManagerCB.setChecked(true);
                    isManagerMyself=false;
                    isManagerOthers=true;
                }else{
                    myselfManagerCB.setChecked(true);
                    othersManagerCB.setChecked(false);
                    isManagerMyself=true;
                    isManagerOthers=false;
                    othersManagerLayout.setVisibility(View.GONE);
                }
            }
        });

        othersManagerCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isManagerOthers){
                    myselfManagerCB.setChecked(true);
                    othersManagerCB.setChecked(false);
                    isManagerMyself=true;
                    isManagerOthers=false;
                }else{
                    othersManagerLayout.setVisibility(View.VISIBLE);
                    myselfManagerCB.setChecked(false);
                    othersManagerCB.setChecked(true);
                    isManagerMyself=false;
                    isManagerOthers=true;
                }
            }
        });

        myselfManagerCB.performClick();

        controllerTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                businessCntlrTypeId=businessControllerArrayList.get(i).getControllerid();
                Log.e(TAG, "onItemSelected:BCID "+controllerTypeSpinner.getSelectedItem().toString()+" "+businessCntlrTypeId);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                businessCntlrTypeId=null;
            }
        });



        getCitiesData();
        getStatesData();

        businessTypeSpinner=(Spinner)findViewById(R.id.businessTypeSpinner);
        plansSpinner=(Spinner)findViewById(R.id.plansSpinner);
        sellerTypeSpinner=(Spinner)findViewById(R.id.sellerTypeSpinner);
        businessName = (FontEditText) findViewById(R.id.businessName);
        registeredName = (FontEditText) findViewById(R.id.registeredName);
        addressBusiness = (FontEditText) findViewById(R.id.addressBusiness);
        getCurrentlocation = (RelativeLayout) findViewById(R.id.getCurrentLocation);
        aboutBusiness=(FontEditText)findViewById(R.id.aboutBusiness);
        pinCode=(FontEditText)findViewById(R.id.pinCode);
        doorNo=(FontEditText)findViewById(R.id.doorNo);
        streetLocality=(FontEditText)findViewById(R.id.streetLocality);
        landmark=(FontEditText)findViewById(R.id.landmark);
        cityName=(FontTextView)findViewById(R.id.city);
        stateName=(FontTextView) findViewById(R.id.state);
        districtName=(FontTextView) findViewById(R.id.district);
        countryName=(FontEditText)findViewById(R.id.country);
        subCatTV=(FontTextView)findViewById(R.id.subCategoryTV);
        subCategoryImpTV=(FontTextView)findViewById(R.id.subCategoryImpTV);
        mapLayout=(LinearLayout)findViewById(R.id.mapLayout);
        mapLayout.setVisibility(View.GONE);
        getPaymentModesData();
        initMap();
        setupPlansSpinner();

        cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedStateId!=null)
                    citySpinnerDialog.showSpinerDialog();
                else
                    Toast.makeText(VendorCreateBusinessActivity.this,"Please select a state",Toast.LENGTH_LONG).show();
            }
        });

        stateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateSpinnerDialog.showSpinerDialog();
            }
        });

        districtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedStateId!=null)
                    districtSpinnerDialog.showSpinerDialog();
                else
                    Toast.makeText(VendorCreateBusinessActivity.this,"Please select a state",Toast.LENGTH_LONG).show();
            }
        });

        cashCB=(LinearLayout)findViewById(R.id.cashLayout);
        creditCB=(LinearLayout)findViewById(R.id.creditLayout);
        othersCB=(LinearLayout)findViewById(R.id.othersPaymentLayout);
        paytmCB=(LinearLayout)findViewById(R.id.paytmLayout);
        netCB=(LinearLayout)findViewById(R.id.netBankLayout);

        cashCheckedIV=(ImageView)findViewById(R.id.cashCheckedIV);
        creditCheckedIV=(ImageView)findViewById(R.id.creditCheckedIV);
        paytmCheckedIV=(ImageView)findViewById(R.id.paytmCheckedIV);
        othersPaymentCheckedIV=(ImageView)findViewById(R.id.othersPaymentCheckedIV);
        netBankCheckedIV=(ImageView)findViewById(R.id.netBankCheckedIV);

        cashUnCheckedIV=(ImageView)findViewById(R.id.cashUnCheckedIV);
        creditUnCheckedIV=(ImageView)findViewById(R.id.creditUnCheckedIV);
        paytmUnCheckedIV=(ImageView)findViewById(R.id.paytmUnCheckedIV);
        othersPaymentUnCheckedIV=(ImageView)findViewById(R.id.othersPaymentUnCheckedIV);
        netBankUnCheckedIV=(ImageView)findViewById(R.id.netBanUnCheckedIV);

        cashCheckedIV.setVisibility(View.GONE);
        creditCheckedIV.setVisibility(View.GONE);
        paytmCheckedIV.setVisibility(View.GONE);
        othersPaymentCheckedIV.setVisibility(View.GONE);
        netBankCheckedIV.setVisibility(View.GONE);

        setupPaymentModes();

        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        subCategorySpinner = (MultiSelectSpinner) findViewById(R.id.subCategorySpinner);
        planSpinner = (Spinner) findViewById(R.id.selectPlanSpinner);


        setupBusinessTypeSpinner();
        getCurrentLocationLatLong();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getCurrentlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent=new Intent(VendorCreateBusinessActivity.this,MarkLocationActivity.class);
                startActivityForResult(intent, 2);
            }
        });




        /*
        * BUSINESS CONTACTS
        * */
        contactName = (FontEditText) findViewById(R.id.contactPersonName);
        aadharNumber=(FontEditText)findViewById(R.id.aadharNumber);
        alternateNumber=(FontEditText)findViewById(R.id.alternateNumber);
        contactNumber = (FontEditText) findViewById(R.id.contactNumber);
        landline = (FontEditText) findViewById(R.id.landlineNumber);
        whatsapp = (FontEditText) findViewById(R.id.whatsappNum);
        emailBusiness = (FontEditText) findViewById(R.id.emailBusiness);
        aboutContactPerson=(FontEditText)findViewById(R.id.aboutPerson);


        /*
        * Business Proofs
        * */
        tanCB=(LinearLayout) findViewById(R.id.tanLayout);
        gstCB=(LinearLayout)findViewById(R.id.gstLayout);
        cinCB=(LinearLayout)findViewById(R.id.cinLayout);
        rnCB=(LinearLayout)findViewById(R.id.registrationNumberLayout);

        tanNum=(FontEditText)findViewById(R.id.tanNum);
        gstNum=(FontEditText)findViewById(R.id.gstNum);
        cinNum=(FontEditText)findViewById(R.id.cinNum);
        rnNum=(FontEditText)findViewById(R.id.rnNum);

        tanCheckedIV=(ImageView)findViewById(R.id.tanCheckedIV);
        gstCheckedIV=(ImageView)findViewById(R.id.gstCheckedIV);
        cinCheckedIV=(ImageView)findViewById(R.id.cinCheckedIV);
        rnCheckedIV=(ImageView)findViewById(R.id.rnCheckedIV);

        tanUnCheckedIV=(ImageView)findViewById(R.id.tanUnCheckedIV);
        gstUnCheckedIV=(ImageView)findViewById(R.id.gstUnCheckedIV);
        cinUnCheckedIV=(ImageView)findViewById(R.id.cinUnCheckedIV);
        rnUnCheckedIV=(ImageView)findViewById(R.id.rnUnCheckedIV);

        tanCheckedIV.setVisibility(View.GONE);
        gstCheckedIV.setVisibility(View.GONE);
        cinCheckedIV.setVisibility(View.GONE);
        rnCheckedIV.setVisibility(View.GONE);
        tanUnCheckedIV.setVisibility(View.VISIBLE);
        gstUnCheckedIV.setVisibility(View.VISIBLE);
        cinUnCheckedIV.setVisibility(View.VISIBLE);
        rnUnCheckedIV.setVisibility(View.VISIBLE);

        otherUpload=(FontTextView)findViewById(R.id.otherUpload);
        gstApply=(FontTextView)findViewById(R.id.gstApply);
        tanApply=(FontTextView)findViewById(R.id.tanApply);
        cinApply=(FontTextView)findViewById(R.id.cinApply);
        rnApply=(FontTextView)findViewById(R.id.rnApply);

        gstApply.setVisibility(View.VISIBLE);
        tanApply.setVisibility(View.VISIBLE);
        cinApply.setVisibility(View.VISIBLE);
        rnApply.setVisibility(View.VISIBLE);

        tanUpLayout=(LinearLayout)findViewById(R.id.tanUploaded);
        gstUpLayout=(LinearLayout)findViewById(R.id.gstUploaded);
        cinUpLayout=(LinearLayout)findViewById(R.id.cinUploaded);
        rnUpLayout=(LinearLayout)findViewById(R.id.rnUploaded);

        tanUpLayout.setVisibility(View.GONE);
        gstUpLayout.setVisibility(View.GONE);
        cinUpLayout.setVisibility(View.GONE);
        rnUpLayout.setVisibility(View.GONE);


        tanCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tanCheckedIV.getVisibility()==View.GONE){
                    tanCheckedIV.setVisibility(View.VISIBLE);
                    tanUnCheckedIV.setVisibility(View.GONE);
                    tanApply.setText("Upload Tan");
                    tanApply.setVisibility(View.GONE);
                    tanChecked=true;
                }else{
                    tanChecked=false;
                    tanCheckedIV.setVisibility(View.GONE);
                    tanUnCheckedIV.setVisibility(View.VISIBLE);
                    tanApply.setText("Upload Tan");
                    tanApply.setVisibility(View.VISIBLE);
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
                    gstApply.setVisibility(View.GONE);
                    gstChecked=true;
                }else{
                    gstChecked=false;
                    gstCheckedIV.setVisibility(View.GONE);
                    gstUnCheckedIV.setVisibility(View.VISIBLE);
                    gstApply.setText("Upload Gst");
                    gstApply.setVisibility(View.VISIBLE);
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
                    cinApply.setVisibility(View.GONE);
                    cinChecked=true;
                }else{
                    cinChecked=false;
                    cinCheckedIV.setVisibility(View.GONE);
                    cinUnCheckedIV.setVisibility(View.VISIBLE);
                    cinApply.setText("Upload Cin");
                    cinApply.setVisibility(View.VISIBLE);
                    cinNum.getText().clear();
                    cinUpLayout.setVisibility(View.GONE);

                }
            }
        });

        rnCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rnCheckedIV.getVisibility()==View.GONE){
                    rnCheckedIV.setVisibility(View.VISIBLE);
                    rnUnCheckedIV.setVisibility(View.GONE);
                    rnApply.setText("Upload Cin");
                    rnApply.setVisibility(View.GONE);
                    rnChecked=true;

                }else{
                    rnChecked=false;
                    rnCheckedIV.setVisibility(View.GONE);
                    rnUnCheckedIV.setVisibility(View.VISIBLE);
                    rnApply.setText("Upload Reg Num");
                    rnApply.setVisibility(View.VISIBLE);
                    rnNum.getText().clear();
                    rnUpLayout.setVisibility(View.GONE);
                }
            }
        });

        cinApply.setVisibility(View.GONE);
        rnApply.setVisibility(View.GONE);
        tanApply.setVisibility(View.GONE);
        gstApply.setVisibility(View.GONE);

        tanCB.performClick();
        rnCB.performClick();
        gstCB.performClick();
        cinCB.performClick();

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


        rnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rnApply.getText().toString().contains("Apply")){
                    rnUploaded=false;
                }else{
                    rnUpLayout.setVisibility(View.VISIBLE);
                    rnUploaded=true;

                }
            }
        });




        /*
        * WEB SOCIAL
        * */
        webLink=(FontEditText)findViewById(R.id.website);
        instagram=(FontEditText)findViewById(R.id.instagram);
        fbLink=(FontEditText)findViewById(R.id.facebook);
        twitterLink=(FontEditText)findViewById(R.id.twitter);
        youtubeLink=(FontEditText)findViewById(R.id.youtubelink);
        linkedInLink=(FontEditText)findViewById(R.id.linkedIn);
        googleplus=(FontEditText)findViewById(R.id.googleplus);


        /*
        * BusinessTimings
        * */
        monCB = (CheckBox) findViewById(R.id.mondayCB);
        tueCB = (CheckBox) findViewById(R.id.tuesdayCB);
        wedCB = (CheckBox) findViewById(R.id.wednesdayCB);
        thurCB = (CheckBox) findViewById(R.id.thursdayCB);
        friCB = (CheckBox) findViewById(R.id.fridayCB);
        satCB = (CheckBox) findViewById(R.id.saturdayCB);
        sunCB = (CheckBox) findViewById(R.id.sundayCB);

        selectAllCB=(CheckBox)findViewById(R.id.selectAllCB);

        monTv = (FontTextView) findViewById(R.id.mondayTV);
        tueTv = (FontTextView) findViewById(R.id.tuesdayTV);
        wedTv = (FontTextView) findViewById(R.id.wednesdayTV);
        thurTv = (FontTextView) findViewById(R.id.thursdayTV);
        friTv = (FontTextView) findViewById(R.id.fridayTV);
        satTv = (FontTextView) findViewById(R.id.saturdayTV);
        sunTv = (FontTextView) findViewById(R.id.sundayTV);

        monTvNew = (FontTextView) findViewById(R.id.mondayTVnew);
        tueTvNew = (FontTextView) findViewById(R.id.tuesdayTVnew);
        wedTvNew = (FontTextView) findViewById(R.id.wednesdayTVnew);
        thurTvNew = (FontTextView) findViewById(R.id.thursdayTVnew);
        friTvNew = (FontTextView) findViewById(R.id.fridayTVnew);
        satTvNew = (FontTextView) findViewById(R.id.saturdayTVnew);
        sunTvNew = (FontTextView) findViewById(R.id.sundayTVnew);

        editMon=(ImageView)findViewById(R.id.editMondayTime);
        editTue=(ImageView)findViewById(R.id.editTuesdayTime);
        editWed=(ImageView)findViewById(R.id.editWednesdayTime);
        editThur=(ImageView)findViewById(R.id.editThursdayTime);
        editFri=(ImageView)findViewById(R.id.editFridayTime);
        editSat=(ImageView)findViewById(R.id.editSaturdayTime);
        editSun=(ImageView)findViewById(R.id.editSundayTime);


        monTimeLayout=(RelativeLayout)findViewById(R.id.monTimeLayout);
        tueTimeLayout=(RelativeLayout)findViewById(R.id.tueTimeLayout);
        wedTimeLayout=(RelativeLayout)findViewById(R.id.wedTimeLayout);
        thurTimeLayout=(RelativeLayout)findViewById(R.id.thurTimeLayout);
        friTimeLayout=(RelativeLayout)findViewById(R.id.friTimeLayout);
        satTimeLayout=(RelativeLayout)findViewById(R.id.satTimeLayout);
        sunTimeLayout=(RelativeLayout)findViewById(R.id.sunTimeLayout);

        initTimingsWidgets();

        plansList = Arrays.asList(getResources().getStringArray(R.array.timings_array));
        initWidgetsTimings();
        setupTimingsClickListeners();

    }

    ArrayList<BusinessController> businessControllerArrayList;
    private void getBusinessControllersData(){
        businessControllerArrayList = new ArrayList<>();
        String url = Constants.BASE_URL+"admin/get-business-controllers";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    if(response.getBoolean("success")){
                        JSONArray data = response.getJSONArray("msg");
                        List<String> cntrlNamesList = new ArrayList<>();
                        for(int i = 0 ;i<data.length();i++){
                            JSONObject cntrlTypeObject = data.getJSONObject(i);
                            businessControllerArrayList.add(new BusinessController(cntrlTypeObject.getString("_id"),
                                        cntrlTypeObject.getString("name")));
                            cntrlNamesList.add(cntrlTypeObject.getString("name"));
                        }
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(VendorCreateBusinessActivity.this, android.R.layout.simple_spinner_item, cntrlNamesList);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        controllerTypeSpinner.setAdapter(dataAdapter);

                    }
                }catch (Exception e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"BUS_CNTRLR_DATA");
    }

    private void setupPaymentModes() {

        cashCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cashCheckedIV.getVisibility()==View.GONE){
                    cashCheckedIV.setVisibility(View.VISIBLE);
                    cashUnCheckedIV.setVisibility(View.GONE);

                    cashB=true;
                }else{
                    cashB=false;
                    cashCheckedIV.setVisibility(View.GONE);
                    cashUnCheckedIV.setVisibility(View.VISIBLE);
                }
            }
        });

        creditCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(creditCheckedIV.getVisibility()==View.GONE){
                    creditCheckedIV.setVisibility(View.VISIBLE);
                    creditUnCheckedIV.setVisibility(View.GONE);

                    creditB=true;
                }else{
                    creditB=false;
                    creditCheckedIV.setVisibility(View.GONE);
                    creditUnCheckedIV.setVisibility(View.VISIBLE);
                }
            }
        });

        paytmCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(paytmCheckedIV.getVisibility()==View.GONE){
                    paytmCheckedIV.setVisibility(View.VISIBLE);
                    paytmUnCheckedIV.setVisibility(View.GONE);

                    paytmB=true;
                }else{
                    paytmB=false;
                    paytmCheckedIV.setVisibility(View.GONE);
                    paytmUnCheckedIV.setVisibility(View.VISIBLE);
                }
            }
        });

        netCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(netBankCheckedIV.getVisibility()==View.GONE){
                    netBankCheckedIV.setVisibility(View.VISIBLE);
                    netBankUnCheckedIV.setVisibility(View.GONE);

                    netB=true;
                }else{
                    netB=false;
                    netBankCheckedIV.setVisibility(View.GONE);
                    netBankUnCheckedIV.setVisibility(View.VISIBLE);
                }
            }
        });

        othersCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(othersPaymentCheckedIV.getVisibility()==View.GONE){
                    othersPaymentCheckedIV.setVisibility(View.VISIBLE);
                    othersPaymentUnCheckedIV.setVisibility(View.GONE);

                    othersB=true;
                }else{
                    othersB=false;
                    othersPaymentCheckedIV.setVisibility(View.GONE);
                    othersPaymentUnCheckedIV.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    ArrayList<Cities> citiesArrayList = new ArrayList<>();
    ArrayList<String> cityNames;
    SpinnerDialog citySpinnerDialog;
    String selectedStateId,prevStateId,selectedCityStateId,selectedDistrictStateId;
    private void getCitiesData(){
        String url = Constants.BASE_URL+"admin/get-all-cities";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        citiesArrayList = new ArrayList<>();
                        cityNames= new ArrayList<>();
                        JSONArray citiesArray = response.getJSONArray("msg");
                        for(int i=0;i<citiesArray.length();i++){
                            JSONObject cityObject = citiesArray.getJSONObject(i);
                            cityNames.add(cityObject.getString("name"));
                            citiesArrayList.add(new Cities(cityObject.getString("_id"),cityObject.getString("state"),
                                    cityObject.getString("name"),cityObject.getString("id_code")));
                        }

                        citySpinnerDialog=new SpinnerDialog(VendorCreateBusinessActivity.this,cityNames,"Select a City",R.style.DialogAnimations_SmileWindow,"Close");// With 	Animation

                        citySpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, int position) {
                                selectedCityStateId = citiesArrayList.get(position).getCityState();
                               // Toast.makeText(VendorCreateBusinessActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                cityName.setText(item);
                            }
                        });
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
        helper.addToRequestQueue(customJsonRequest,"CITIES");
    }

    ArrayList<States> statesArrayList = new ArrayList<>();
    ArrayList<String> stateNames;
    SpinnerDialog stateSpinnerDialog;
    private void getStatesData(){
        String url = Constants.BASE_URL+"admin/get-all-states";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        statesArrayList = new ArrayList<>();
                        stateNames= new ArrayList<>();
                        JSONArray statesArray = response.getJSONArray("msg");
                        for(int i=0;i<statesArray.length();i++){
                            JSONObject statesObject = statesArray.getJSONObject(i);
                            stateNames.add(statesObject.getString("name"));
                            statesArrayList.add(new States(statesObject.getString("_id"),statesObject.getString("name")));
                        }
                        stateSpinnerDialog=new SpinnerDialog(VendorCreateBusinessActivity.this,stateNames,"Select a State",R.style.DialogAnimations_SmileWindow,"Close");// With 	Animation

                        stateSpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, int position) {
                                //Toast.makeText(VendorCreateBusinessActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                if(selectedStateId==null)
                                    prevStateId=statesArrayList.get(position).getStateId();
                                else
                                    prevStateId=selectedStateId;
                                selectedStateId=statesArrayList.get(position).getStateId();
                                getDistricts(selectedStateId);
                                stateName.setText(item);

                                if(!prevStateId.equals(selectedStateId)){
                                    cityName.setText("");
                                    districtName.setText("");
                                    districtSpinnerDialog.showSpinerDialog();
                                    citySpinnerDialog.showSpinerDialog();
                                }
                            }
                        });
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
        helper.addToRequestQueue(customJsonRequest,"STATES");
    }

    ArrayList<Districts> districtsArrayList;
    ArrayList<String> districtNames;
    SpinnerDialog districtSpinnerDialog;
    String selectedDistrictId;
    private void getDistricts(String stateId){
        String url = Constants.BASE_URL+"admin/get-district-by-state/"+stateId;
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        districtsArrayList = new ArrayList<>();
                        districtNames = new ArrayList<>();
                        JSONArray districtsArray = response.getJSONArray("msg");
                        for(int i=0;i<districtsArray.length();i++){
                            JSONObject districtsObject = districtsArray.getJSONObject(i);
                            districtNames.add(districtsObject.getString("name"));
                            districtsArrayList.add(new Districts(districtsObject.getString("_id"),districtsObject.getString("state"),
                                    districtsObject.getString("name")));
                        }

                        districtSpinnerDialog=new SpinnerDialog(VendorCreateBusinessActivity.this,districtNames,"Select a District",R.style.DialogAnimations_SmileWindow,"Close");// With 	Animation

                        districtSpinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                            @Override
                            public void onClick(String item, int position) {
                                //Toast.makeText(VendorCreateBusinessActivity.this, item + "  " + position+"", Toast.LENGTH_SHORT).show();
                                districtName.setText(item);
                                selectedDistrictId=districtsArrayList.get(position).getDistrictId();
                            }
                        });
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
        helper.addToRequestQueue(customJsonRequest,"DISTRICTS");
    }

    private void initTimingsWidgets(){
        selectAllCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectAllCB.isChecked()){
                    monCB.setChecked(true);
                    tueCB.setChecked(true);
                    wedCB.setChecked(true);
                    thurCB.setChecked(true);
                    friCB.setChecked(true);
                    satCB.setChecked(true);
                    sunCB.setChecked(true);


                    monTimeLayout.setVisibility(View.VISIBLE);
                    tueTimeLayout.setVisibility(View.VISIBLE);
                    wedTimeLayout.setVisibility(View.VISIBLE);
                    thurTimeLayout.setVisibility(View.VISIBLE);
                    friTimeLayout.setVisibility(View.VISIBLE);
                    satTimeLayout.setVisibility(View.VISIBLE);
                    sunTimeLayout.setVisibility(View.VISIBLE);

                    setAllDayTimings();


                }else{
                    monCB.setChecked(false);
                    tueCB.setChecked(false);
                    wedCB.setChecked(false);
                    thurCB.setChecked(false);
                    friCB.setChecked(false);
                    satCB.setChecked(false);
                    sunCB.setChecked(false);

                    monTimeLayout.setVisibility(View.GONE);
                    tueTimeLayout.setVisibility(View.GONE);
                    wedTimeLayout.setVisibility(View.GONE);
                    thurTimeLayout.setVisibility(View.GONE);
                    friTimeLayout.setVisibility(View.GONE);
                    satTimeLayout.setVisibility(View.GONE);
                    sunTimeLayout.setVisibility(View.GONE);
                }


            }
        });


        editMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIndividualTimings(monCB,monTvNew,"MONDAY");
            }
        });

        editTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIndividualTimings(tueCB,tueTvNew,"TUESDAY");
            }
        });

        editWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIndividualTimings(wedCB,wedTvNew,"WEDNESDAY");
            }
        });

        editThur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIndividualTimings(thurCB,thurTvNew,"THURSDAY");
            }
        });

        editFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIndividualTimings(friCB,friTvNew,"FRIDAY");
            }
        });

        editSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIndividualTimings(satCB,satTvNew,"SATURDAY");
            }
        });

        editSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setIndividualTimings(sunCB,sunTvNew,"SUNDAY");
            }
        });

        monCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(monCB.isChecked()){
                    monTimeLayout.setVisibility(View.VISIBLE);
                    workingMon=true;
                    monTv.setText("Monday");
                    if(!selectAllCB.isChecked()){
                        Log.e(TAG, "onCheckedChanged: MON");
                        editMon.performClick();
                    }
                }else{

                    workingMon=false;
                    monTv.setText(monTv.getText().toString()+"(closed)");
                    monTimeLayout.setVisibility(View.GONE);
                    selectAllCB.setChecked(false);
                }


            }
        });



        tueCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(tueCB.isChecked()){
                    tueTimeLayout.setVisibility(View.VISIBLE);
                    workingTue=true;
                    tueTv.setText("Tuesday");

                    if(!selectAllCB.isChecked()){
                        Log.e(TAG, "onCheckedChanged: TUE");
                        editTue.performClick();

                    }

                }else{
                    workingTue=false;
                    tueTv.setText(tueTv.getText().toString()+"(closed)");

                    tueTimeLayout.setVisibility(View.GONE);
                    selectAllCB.setChecked(false);

                }

            }
        });

        wedCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(wedCB.isChecked()){
                    wedTimeLayout.setVisibility(View.VISIBLE);
                    workingWed=true;
                    wedTv.setText("Wednesday");
                    if(!selectAllCB.isChecked()){
                        Log.e(TAG, "onCheckedChanged: WED");
                        editWed.performClick();

                    }

                }else{
                    wedTv.setText(wedTv.getText().toString()+"(closed)");
                    workingWed=false;
                    wedTimeLayout.setVisibility(View.GONE);
                    selectAllCB.setChecked(false);

                }


            }
        });

        thurCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(thurCB.isChecked()){
                    thurTimeLayout.setVisibility(View.VISIBLE);
                    workingThur=true;
                    thurTv.setText("Thursday");
                    if(!selectAllCB.isChecked()){
                        Log.e(TAG, "onCheckedChanged: THUR");
                        editThur.performClick();

                    }

                }else{
                    thurTv.setText(thurTv.getText().toString()+"(closed)");
                    workingThur=false;

                    thurTimeLayout.setVisibility(View.GONE);
                    selectAllCB.setChecked(false);

                }

            }
        });

        friCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(friCB.isChecked()){
                    friTimeLayout.setVisibility(View.VISIBLE);
                    workingfri=true;
                    friTv.setText("Friday");

                    if(!selectAllCB.isChecked()){
                        Log.e(TAG, "onCheckedChanged: FRI" );
                        editFri.performClick();

                    }

                }else{
                    friTv.setText(friTv.getText().toString()+"(closed)");
                    workingfri=false;
                    friTimeLayout.setVisibility(View.GONE);
                    selectAllCB.setChecked(false);

                }


            }
        });

        satCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(satCB.isChecked()){
                    satTimeLayout.setVisibility(View.VISIBLE);
                    workingSat=true;
                    satTv.setText("Saturday");
                    if(!selectAllCB.isChecked()){
                        Log.e(TAG, "onCheckedChanged: SAT" );
                        editSat.performClick();

                    }

                }else{
                    satTv.setText(satTv.getText().toString()+"(closed)");
                    workingSat=false;
                    satTimeLayout.setVisibility(View.GONE);
                    selectAllCB.setChecked(false);

                }


            }
        });

        sunCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(sunCB.isChecked()){
                    sunTimeLayout.setVisibility(View.VISIBLE);
                    workingSun=true;
                    sunTv.setText("Sunday");
                    if(!selectAllCB.isChecked()){
                        Log.e(TAG, "onCheckedChanged: SUNDAY" );
                        editSun.performClick();

                    }
                }else{
                    workingSun=false;
                    sunTv.setText(sunTv.getText().toString()+"(closed)");
                    sunTimeLayout.setVisibility(View.GONE);
                    selectAllCB.setChecked(false);

                }


            }
        });


        monCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(monCB.isChecked()){
                    checkAllSelected();
                }else{
                    selectAllCB.setChecked(false);
                }
            }
        });

        tueCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tueCB.isChecked()){
                    checkAllSelected();
                }else{
                    selectAllCB.setChecked(false);
                }
            }
        });

        wedCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(wedCB.isChecked()){
                    checkAllSelected();
                }else{
                    selectAllCB.setChecked(false);
                }
            }
        });

        thurCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(thurCB.isChecked()){
                    checkAllSelected();
                }else{
                    selectAllCB.setChecked(false);
                }
            }
        });

        friCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(friCB.isChecked()){
                    checkAllSelected();
                }else{
                    selectAllCB.setChecked(false);
                }
            }
        });

        satCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(satCB.isChecked()){
                    checkAllSelected();
                }else{
                    selectAllCB.setChecked(false);
                }
            }
        });

        sunCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sunCB.isChecked()){
                    checkAllSelected();
                }else{
                    selectAllCB.setChecked(false);
                }
            }
        });
    }

    private void retrieveTimes(){
        if(workingMon){
            StringTokenizer st = new StringTokenizer(monTvNew.getText().toString());
            openMon=st.nextToken("-");
            closeMon=st.nextToken();

            Log.e(TAG, "retrieveTimes: OPEN "+openMon+" CLOSE "+closeMon);
        }

        if(workingTue){
            StringTokenizer st = new StringTokenizer(tueTvNew.getText().toString());
            openTue=st.nextToken("-");
            closeTue=st.nextToken();

            Log.e(TAG, "retrieveTimes: OPEN "+openTue+" CLOSE "+closeTue);
        }
        if(workingWed){
            StringTokenizer st = new StringTokenizer(wedTvNew.getText().toString());
            openWed=st.nextToken("-");
            closeWed=st.nextToken();

            Log.e(TAG, "retrieveTimes: OPEN "+openWed+" CLOSE "+closeWed);
        }

        if(workingThur){
            StringTokenizer st = new StringTokenizer(thurTvNew.getText().toString());
            openThur=st.nextToken("-");
            closeThur=st.nextToken();

            Log.e(TAG, "retrieveTimes: OPEN "+openThur+" CLOSE "+closeThur);
        }

        if(workingfri){
            StringTokenizer st = new StringTokenizer(friTvNew.getText().toString());
            openFri=st.nextToken("-");
            closeFri=st.nextToken();

            Log.e(TAG, "retrieveTimes: OPEN "+openFri+" CLOSE "+closeFri);
        }

        if(workingSat){
            StringTokenizer st = new StringTokenizer(satTvNew.getText().toString());
            openSat=st.nextToken("-");
            closeSat=st.nextToken();

            Log.e(TAG, "retrieveTimes: OPEN "+openSat+" CLOSE "+closeSat);
        }

        if(workingSun){
            StringTokenizer st = new StringTokenizer(sunTvNew.getText().toString());
            openSun=st.nextToken("-");
            closeSun=st.nextToken();

            Log.e(TAG, "retrieveTimes: OPEN "+openSun+" CLOSE "+closeSun);
        }
    }

    private void checkAllSelected(){
        if(monCB.isChecked() && tueCB.isChecked() && wedCB.isChecked() && thurCB.isChecked() && friCB.isChecked() && satCB.isChecked() && sunCB.isChecked()){
            selectAllCB.setChecked(true);
        }else{
            selectAllCB.setChecked(false);
        }
    }
    private void setIndividualTimings(CheckBox CB, final FontTextView fontTextView,String day){
        if(CB.isChecked()){
            final Dialog dialog = new Dialog(VendorCreateBusinessActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.popup_timings);
            dialog.setCancelable(false);
            dialog.show();

            final Spinner fromSpinner =(Spinner)dialog.findViewById(R.id.selectFrom);
            final Spinner toSpinner =(Spinner)dialog.findViewById(R.id.selectTo);
            final FontTextView okButton=(FontTextView)dialog.findViewById(R.id.okPopUp);
              FontTextView fontTextView1 = (FontTextView)dialog.findViewById(R.id.day);
              fontTextView1.setVisibility(View.VISIBLE);
              fontTextView1.setText(day.toUpperCase().toString());
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fontTextView.setText(open+"-"+close);
                    dialog.dismiss();
                }
            });
            final List<String> timingsList = Arrays.asList(getResources().getStringArray(R.array.timings_array));
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(VendorCreateBusinessActivity.this, android.R.layout.simple_spinner_item, timingsList);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            ArrayAdapter<String> dataAdapterr = new ArrayAdapter<String>(VendorCreateBusinessActivity.this, android.R.layout.simple_spinner_item, timingsList);
            dataAdapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            fromSpinner.setAdapter(dataAdapter);
            toSpinner.setAdapter(dataAdapterr);

            fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                    open=adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    open=adapterView.getItemAtPosition(0).toString();

                    Toast.makeText(VendorCreateBusinessActivity.this,"PLEASE SELECT OPENING TIME",Toast.LENGTH_LONG).show();

                }
            });

            toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    close=adapterView.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    close=adapterView.getItemAtPosition(0).toString();

                    Toast.makeText(VendorCreateBusinessActivity.this,"PLEASE SELECT CLOSING TIME",Toast.LENGTH_LONG).show();

                }
            });
        }else{
            Toast.makeText(VendorCreateBusinessActivity.this,"Please check the box to edit",Toast.LENGTH_LONG).show();
        }
    }

    private void setAllDayTimings(){
        final Dialog dialog = new Dialog(VendorCreateBusinessActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_timings);
        dialog.setCancelable(false);
        dialog.show();

        final Spinner fromSpinner =(Spinner)dialog.findViewById(R.id.selectFrom);
        final Spinner toSpinner =(Spinner)dialog.findViewById(R.id.selectTo);
        final FontTextView okButton=(FontTextView)dialog.findViewById(R.id.okPopUp);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                monTvNew.setText(openAll+"-"+closeAll);
                tueTvNew.setText(openAll+"-"+closeAll);
                wedTvNew.setText(openAll+"-"+closeAll);
                thurTvNew.setText(openAll+"-"+closeAll);
                friTvNew.setText(openAll+"-"+closeAll);
                satTvNew.setText(openAll+"-"+closeAll);
                sunTvNew.setText(openAll+"-"+closeAll);

                dialog.dismiss();
            }
        });
        final List<String> timingsList = Arrays.asList(getResources().getStringArray(R.array.timings_array));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(VendorCreateBusinessActivity.this, android.R.layout.simple_spinner_item, timingsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> dataAdapterr = new ArrayAdapter<String>(VendorCreateBusinessActivity.this, android.R.layout.simple_spinner_item, timingsList);
        dataAdapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(dataAdapter);
        toSpinner.setAdapter(dataAdapterr);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                openAll=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                openAll=adapterView.getItemAtPosition(0).toString();

                Toast.makeText(VendorCreateBusinessActivity.this,"PLEASE SELECT OPENING TIME",Toast.LENGTH_LONG).show();

            }
        });

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                closeAll=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                closeAll=adapterView.getItemAtPosition(0).toString();

                Toast.makeText(VendorCreateBusinessActivity.this,"PLEASE SELECT CLOSING TIME",Toast.LENGTH_LONG).show();

            }
        });
    }


    private void setupTimingsClickListeners(){


        allUncheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAllDayTimingsNew();
                checkAllIV();
            }
        });

        allCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uncheckAllIV();
            }
        });

        monUnCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individualUncheckListener(monUnCheckIV,monCheckIV);

            }
        });

        monCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allUncheckIV.setVisibility(View.VISIBLE);
                allCheckIV.setVisibility(View.GONE);
                individualCheckListener(monUnCheckIV,monCheckIV);
                monOpenSpinner.setSelection(0);
                monCloseSpinner.setSelection(0);
            }
        });

        tueUnCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individualUncheckListener(tueUnCheckIV,tueCheckIV);

            }
        });

        tueCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allUncheckIV.setVisibility(View.VISIBLE);
                allCheckIV.setVisibility(View.GONE);
                individualCheckListener(tueUnCheckIV,tueCheckIV);
                monOpenSpinner.setSelection(0);
                monCloseSpinner.setSelection(0);
            }
        });

        wedUnCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individualUncheckListener(wedUnCheckIV,wedCheckIV);

            }
        });

        wedCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allUncheckIV.setVisibility(View.VISIBLE);
                allCheckIV.setVisibility(View.GONE);
                individualCheckListener(wedUnCheckIV,wedCheckIV);
                monOpenSpinner.setSelection(0);
                monCloseSpinner.setSelection(0);
            }
        });

        thurUnCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individualUncheckListener(thurUnCheckIV,thurCheckIV);

            }
        });

        thurCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allUncheckIV.setVisibility(View.VISIBLE);
                allCheckIV.setVisibility(View.GONE);
                individualCheckListener(thurUnCheckIV,thurCheckIV);
                monOpenSpinner.setSelection(0);
                monCloseSpinner.setSelection(0);
            }
        });

        friUnCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individualUncheckListener(friUnCheckIV,friCheckIV);
            }
        });

        friCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allUncheckIV.setVisibility(View.VISIBLE);
                allCheckIV.setVisibility(View.GONE);
                individualCheckListener(friUnCheckIV,friCheckIV);
                monOpenSpinner.setSelection(0);
                monCloseSpinner.setSelection(0);
            }
        });

        satUnCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individualUncheckListener(satUnCheckIV,satCheckIV);

            }
        });

        satCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allUncheckIV.setVisibility(View.VISIBLE);
                allCheckIV.setVisibility(View.GONE);
                individualCheckListener(satUnCheckIV,satCheckIV);
                monOpenSpinner.setSelection(0);
                monCloseSpinner.setSelection(0);
            }
        });

        sunUnCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                individualUncheckListener(sunUnCheckIV,sunCheckIV);

            }
        });

        sunCheckIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allUncheckIV.setVisibility(View.VISIBLE);
                allCheckIV.setVisibility(View.GONE);
                individualCheckListener(sunUnCheckIV,sunCheckIV);
                monOpenSpinner.setSelection(0);
                monCloseSpinner.setSelection(0);
            }
        });
    }

    private void individualUncheckListener(ImageView unCheck, ImageView Check){
        Check.setVisibility(View.VISIBLE);
        unCheck.setVisibility(View.GONE);
        checkAllVisible();
    }

    private void individualCheckListener(ImageView unCheck, ImageView Check){
        Check.setVisibility(View.GONE);
        unCheck.setVisibility(View.VISIBLE);
    }

    private void checkAllVisible(){
        if(monCheckIV.getVisibility()==View.VISIBLE &&
                tueCheckIV.getVisibility()==View.VISIBLE && friCheckIV.getVisibility()==View.VISIBLE &&
                wedCheckIV.getVisibility()==View.VISIBLE && satCheckIV.getVisibility()==View.VISIBLE &&
                thurCheckIV.getVisibility()==View.VISIBLE && sunCheckIV.getVisibility()==View.VISIBLE ){

            allUncheckIV.setVisibility(View.GONE);
            allCheckIV.setVisibility(View.VISIBLE);
        }else{
            allUncheckIV.setVisibility(View.VISIBLE);
            allCheckIV.setVisibility(View.GONE);
        }
    }

    private void checkAllIV(){
        allUncheckIV.setVisibility(View.GONE);
        monUnCheckIV.setVisibility(View.GONE);
        tueUnCheckIV.setVisibility(View.GONE);
        wedUnCheckIV.setVisibility(View.GONE);
        thurUnCheckIV.setVisibility(View.GONE);
        friUnCheckIV.setVisibility(View.GONE);
        satUnCheckIV.setVisibility(View.GONE);
        sunUnCheckIV.setVisibility(View.GONE);

        allCheckIV.setVisibility(View.VISIBLE);
        monCheckIV.setVisibility(View.VISIBLE);
        tueCheckIV.setVisibility(View.VISIBLE);
        wedCheckIV.setVisibility(View.VISIBLE);
        thurCheckIV.setVisibility(View.VISIBLE);
        friCheckIV.setVisibility(View.VISIBLE);
        satCheckIV.setVisibility(View.VISIBLE);
        sunCheckIV.setVisibility(View.VISIBLE);
    }

    private void uncheckAllIV(){
        allUncheckIV.setVisibility(View.VISIBLE);
        monUnCheckIV.setVisibility(View.VISIBLE);
        tueUnCheckIV.setVisibility(View.VISIBLE);
        wedUnCheckIV.setVisibility(View.VISIBLE);
        thurUnCheckIV.setVisibility(View.VISIBLE);
        friUnCheckIV.setVisibility(View.VISIBLE);
        satUnCheckIV.setVisibility(View.VISIBLE);
        sunUnCheckIV.setVisibility(View.VISIBLE);

        allCheckIV.setVisibility(View.GONE);
        monCheckIV.setVisibility(View.GONE);
        tueCheckIV.setVisibility(View.GONE);
        wedCheckIV.setVisibility(View.GONE);
        thurCheckIV.setVisibility(View.GONE);
        friCheckIV.setVisibility(View.GONE);
        satCheckIV.setVisibility(View.GONE);
        sunCheckIV.setVisibility(View.GONE);


        monOpenSpinner.setSelection(0);
        tueOpenSpinner.setSelection(0);
        wedOpenSpinner.setSelection(0);
        thurOpenSpinner.setSelection(0);
        friOpenSpinner.setSelection(0);
        satOpenSpinner.setSelection(0);
        sunOpenSpinner.setSelection(0);

        monCloseSpinner.setSelection(0);
        tueCloseSpinner.setSelection(0);
        wedCloseSpinner.setSelection(0);
        thurCloseSpinner.setSelection(0);
        friCloseSpinner.setSelection(0);
        satCloseSpinner.setSelection(0);
        sunCloseSpinner.setSelection(0);

    }

    private void initWidgetsTimings(){
        allCheckTv=(FontTextView)findViewById(R.id.allCheckTV);

        allCheckIV=(ImageView)findViewById(R.id.allcheckIV);
        allUncheckIV=(ImageView)findViewById(R.id.allUncheckIV);

        monCheckIV=(ImageView)findViewById(R.id.moncheckIV);
        tueCheckIV=(ImageView)findViewById(R.id.tuecheckIV);
        wedCheckIV=(ImageView)findViewById(R.id.wedcheckIV);
        thurCheckIV=(ImageView)findViewById(R.id.thurcheckIV);
        friCheckIV=(ImageView)findViewById(R.id.fricheckIV);
        satCheckIV=(ImageView)findViewById(R.id.satcheckIV);
        sunCheckIV=(ImageView)findViewById(R.id.suncheckIV);

        monUnCheckIV=(ImageView)findViewById(R.id.monUncheckIV);
        tueUnCheckIV=(ImageView)findViewById(R.id.tueUncheckIV);
        wedUnCheckIV=(ImageView)findViewById(R.id.wedUncheckIV);
        thurUnCheckIV=(ImageView)findViewById(R.id.thurUncheckIV);
        friUnCheckIV=(ImageView)findViewById(R.id.friUncheckIV);
        satUnCheckIV=(ImageView)findViewById(R.id.satUncheckIV);
        sunUnCheckIV=(ImageView)findViewById(R.id.sunUncheckIV);

        monOpenSpinner=(Spinner)findViewById(R.id.monOpenSpinner);
        tueOpenSpinner=(Spinner)findViewById(R.id.tueOpenSpinner);
        wedOpenSpinner=(Spinner)findViewById(R.id.wedOpenSpinner);
        thurOpenSpinner=(Spinner)findViewById(R.id.thurOpenSpinner);
        friOpenSpinner=(Spinner)findViewById(R.id.friOpenSpinner);
        satOpenSpinner=(Spinner)findViewById(R.id.satOpenSpinner);
        sunOpenSpinner=(Spinner)findViewById(R.id.sunOpenSpinner);

        monCloseSpinner=(Spinner)findViewById(R.id.monCloseSpinner);
        tueCloseSpinner=(Spinner)findViewById(R.id.tueCloseSpinner);
        wedCloseSpinner=(Spinner)findViewById(R.id.wedCloseSpinner);
        thurCloseSpinner=(Spinner)findViewById(R.id.thurCloseSpinner);
        friCloseSpinner=(Spinner)findViewById(R.id.friCloseSpinner);
        satCloseSpinner=(Spinner)findViewById(R.id.satCloseSpinner);
        sunCloseSpinner=(Spinner)findViewById(R.id.sunCloseSpinner);

        initiateSpinners(monOpenSpinner);
        initiateSpinners(tueOpenSpinner);
        initiateSpinners(wedOpenSpinner);
        initiateSpinners(thurOpenSpinner);
        initiateSpinners(friOpenSpinner);
        initiateSpinners(satOpenSpinner);
        initiateSpinners(sunOpenSpinner);

        initiateSpinners(monCloseSpinner);
        initiateSpinners(tueCloseSpinner);
        initiateSpinners(wedCloseSpinner);
        initiateSpinners(thurCloseSpinner);
        initiateSpinners(friCloseSpinner);
        initiateSpinners(satCloseSpinner);
        initiateSpinners(sunCloseSpinner);

    }

    private void initiateSpinners(Spinner spinner){

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, plansList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);

                Typeface externalFont= Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(12);


                return v;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v =super.getDropDownView(position, convertView, parent);

                Typeface externalFont=Typeface.createFromAsset(getAssets(), "fonts/Montserrat-Regular.ttf");
                ((TextView) v).setTypeface(externalFont);
                ((TextView) v).setTextSize(12);

                return v;
            }
        };
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void formJsonTimings(){
        try {
            JSONObject business = new JSONObject();
            JSONObject timings = new JSONObject();
            JSONObject days = new JSONObject();
            if (workingSun) {
                JSONObject sunTimings = new JSONObject();
                sunTimings.put("closing", sunCloseSpinner.getSelectedItem().toString());
                sunTimings.put("opening", sunOpenSpinner.getSelectedItem().toString());
                timings.put("sunday", sunTimings);
                days.put("sunday", workingSun);
            } else {
                days.put("sunday", false);
            }
            if (workingSat) {
                JSONObject satTimings = new JSONObject();
                satTimings.put("closing", satCloseSpinner.getSelectedItem().toString());
                satTimings.put("opening", satOpenSpinner.getSelectedItem().toString());
                timings.put("saturday", satTimings);
                days.put("saturday", workingSat);
            } else {
                days.put("saturday", false);
            }
            if (workingfri) {
                JSONObject friTimings = new JSONObject();
                friTimings.put("closing", friCloseSpinner.getSelectedItem().toString());
                friTimings.put("opening", friOpenSpinner.getSelectedItem().toString());
                timings.put("friday", friTimings);
                days.put("friday", workingfri);
            } else {
                days.put("friday", false);
            }
            if (workingThur) {
                JSONObject thurTimings = new JSONObject();
                thurTimings.put("closing", thurCloseSpinner.getSelectedItem().toString());
                thurTimings.put("opening", thurOpenSpinner.getSelectedItem().toString());
                timings.put("thursday", thurTimings);
                days.put("thursday", workingThur);
            } else {
                days.put("thursday", false);
            }
            if (workingWed) {
                JSONObject wedTimings = new JSONObject();
                wedTimings.put("closing", wedCloseSpinner.getSelectedItem().toString());
                wedTimings.put("opening", wedOpenSpinner.getSelectedItem().toString());
                timings.put("wednesday", wedTimings);
                days.put("wednesday", workingWed);
            } else {
                days.put("wednesday", false);
            }
            if (workingTue) {
                JSONObject tueTimings = new JSONObject();
                tueTimings.put("closing", tueCloseSpinner.getSelectedItem().toString());
                tueTimings.put("opening", tueOpenSpinner.getSelectedItem().toString());
                timings.put("tuesday", tueTimings);
                days.put("tuesday", workingTue);
            } else {
                days.put("tuesday", false);
            }
            if (workingMon) {
                JSONObject monTimings = new JSONObject();
                monTimings.put("closing", monCloseSpinner.getSelectedItem().toString());
                monTimings.put("opening", monOpenSpinner.getSelectedItem().toString());
                timings.put("monday", monTimings);
                days.put("monday", workingMon);
            } else {
                days.put("monday", false);
            }
            business.put("timings", timings);
            business.put("days", days);

            Log.e(TAG, "formJsonTimings: "+business.toString() );

        }catch (JSONException e){

        }

    }

    private void retrieveTimesNew(){

        if(allCheckIV.getVisibility()==View.VISIBLE){
            workingMon=true;
            workingTue=true;
            workingWed=true;
            workingThur=true;
            workingfri=true;
            workingSat=true;
            workingSun=true;

        }else{

            if(monCheckIV.getVisibility()==View.VISIBLE){
                workingMon=true;

            } else{
                workingMon=false;
            }

            if(tueCheckIV.getVisibility()==View.VISIBLE){
                workingTue=true;

            } else{
                workingTue=false;
            }

            if(wedCheckIV.getVisibility()==View.VISIBLE){
                workingWed=true;

            } else{
                workingWed=false;
            }

            if(thurCheckIV.getVisibility()==View.VISIBLE){
                workingThur=true;

            } else{
                workingThur=false;
            }

            if(friCheckIV.getVisibility()==View.VISIBLE){
                workingfri=true;

            } else{
                workingfri=false;
            }

            if(satCheckIV.getVisibility()==View.VISIBLE){
                workingSat=true;

            } else{
                workingSat=false;
            }

            if(sunCheckIV.getVisibility()==View.VISIBLE){
                workingSun =true;

            } else{
                workingSun=false;
            }

        }


    }

    private void setAllDayTimingsNew(){
        final Dialog dialog = new Dialog(VendorCreateBusinessActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_timings);
        dialog.setCancelable(false);
        dialog.show();

        final Spinner fromSpinner =(Spinner)dialog.findViewById(R.id.selectFrom);
        final Spinner toSpinner =(Spinner)dialog.findViewById(R.id.selectTo);
        final FontTextView okButton=(FontTextView)dialog.findViewById(R.id.okPopUp);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                monOpenSpinner.setSelection(openAllNew);
                tueOpenSpinner.setSelection(openAllNew);
                wedOpenSpinner.setSelection(openAllNew);
                thurOpenSpinner.setSelection(openAllNew);
                friOpenSpinner.setSelection(openAllNew);
                satOpenSpinner.setSelection(openAllNew);
                sunOpenSpinner.setSelection(openAllNew);

                monCloseSpinner.setSelection(closeAllNew);
                tueCloseSpinner.setSelection(closeAllNew);
                wedCloseSpinner.setSelection(closeAllNew);
                thurCloseSpinner.setSelection(closeAllNew);
                friCloseSpinner.setSelection(closeAllNew);
                satCloseSpinner.setSelection(closeAllNew);
                sunCloseSpinner.setSelection(closeAllNew);

                dialog.dismiss();
            }
        });
        final List<String> timingsList = Arrays.asList(getResources().getStringArray(R.array.timings_array));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(VendorCreateBusinessActivity.this, android.R.layout.simple_spinner_item, timingsList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> dataAdapterr = new ArrayAdapter<String>(VendorCreateBusinessActivity.this, android.R.layout.simple_spinner_item, timingsList);
        dataAdapterr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(dataAdapter);
        toSpinner.setAdapter(dataAdapterr);

        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                openAllNew=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                openAllNew=0;

                Toast.makeText(VendorCreateBusinessActivity.this,"PLEASE SELECT OPENING TIME",Toast.LENGTH_LONG).show();

            }
        });

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                closeAllNew=i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                closeAllNew=0;

                Toast.makeText(VendorCreateBusinessActivity.this,"PLEASE SELECT CLOSING TIME",Toast.LENGTH_LONG).show();

            }
        });
    }



    ArrayList<VendorBusinessType> vendorBusinessTypeArrayList;
    String businessTypeId;
    private void setupBusinessTypeSpinner(){
        vendorBusinessTypeArrayList = new ArrayList<>();
        List<String> businessTypes = new ArrayList<>();
        String url = Constants.BASE_URL+"admin/get-business-types";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(response.getBoolean("success")){
                        JSONArray businessTypeArrray = response.getJSONArray("msg");
                        for(int i =0;i <businessTypeArrray.length();i++){
                            JSONObject businessTypeObject = businessTypeArrray.getJSONObject(i);
                            vendorBusinessTypeArrayList.add(new VendorBusinessType(businessTypeObject.getString("_id"),
                                    businessTypeObject.getString("name")));
                            businessTypes.add(businessTypeObject.getString("name"));
                        }

                        final List<String> plansList = Arrays.asList(getResources().getStringArray(R.array.business_type_array));
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(VendorCreateBusinessActivity.this, android.R.layout.simple_spinner_item, businessTypes){
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
                        businessTypeSpinner.setAdapter(dataAdapter);
                        businessTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                businessTypeId = vendorBusinessTypeArrayList.get(i).getBusinessTypeId();
                                getSellerTypes(businessTypeId);
                                getAllCategories();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"BUSINESS_TYPES");
    }


    ArrayList<SellerType> sellerTypeArrayList;
    List<String> sellerTypeNames;
    String sellerTypeId;
    LinearLayout sellerTypeLayout;
    boolean sellerTypeSelected=false;
    String selectedSellerType;
    private void getSellerTypes(String businessTypeId){
        sellerTypeLayout=(LinearLayout)findViewById(R.id.sellerTypeLayout);
        String url = Constants.BASE_URL+"admin/get-seller-type/"+businessTypeId;
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        sellerTypeLayout.setVisibility(View.VISIBLE);
                        sellerTypeSelected=false;
                        JSONArray sellerTypeArray = response.getJSONArray("msg");
                        sellerTypeArrayList= new ArrayList<>();
                        sellerTypeNames= new ArrayList<>();
                        for(int i =0 ;i<sellerTypeArray.length();i++){
                            JSONObject sellerTypeObject = sellerTypeArray.getJSONObject(i);
                            sellerTypeArrayList.add(new SellerType(sellerTypeObject.getString("_id"),sellerTypeObject.getString("name"),
                                    sellerTypeObject.getString("business_type")));
                            sellerTypeNames.add(sellerTypeObject.getString("name"));
                        }

                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(VendorCreateBusinessActivity.this, android.R.layout.simple_spinner_item, sellerTypeNames){
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
                        sellerTypeSpinner.setAdapter(dataAdapter);
                        sellerTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                sellerTypeSelected=true;
                                selectedSellerType = sellerTypeArrayList.get(i).getSellerId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                sellerTypeSelected=false;
                            }
                        });

                    }else{
                        sellerTypeLayout.setVisibility(View.GONE);
                        sellerTypeSelected=true;
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
        helper.addToRequestQueue(customJsonRequest,"SellerTypes");
    }

    private void setupPlansSpinner(){
        List<String> planstype = new ArrayList<>();
        planstype.add("Select a business plan");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(VendorCreateBusinessActivity.this, android.R.layout.simple_spinner_item, planstype){
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
        plansSpinner.setAdapter(dataAdapter);
        plansSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    ArrayList<Categories> categoriesArrayList;
    String selectedCategoryId;

    private void getAllCategories(){
        categoriesArrayList = new ArrayList<>();
        String url=Constants.BASE_URL+"admin/get-categories-by-business-type/"+businessTypeId;
        Log.e(TAG, "getAllCategories: "+url);
        showDialog();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    hideDialog();

                    if(response.getBoolean("success")){
                        JSONArray Jarray = response.getJSONArray("msg");
                        categoriesModelArrayList= new ArrayList<>();

                        categoryIdsList= new ArrayList<>();
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject object     = Jarray.getJSONObject(i);

                            categoriesModelArrayList.add(object.getString("name"));
                            categoryIdsList.add(object.getString("_id"));
                            categoriesArrayList.add(new Categories(object.getString("_id"),object.getString("name"),object.getString("business_type"),
                                    "",""));
                        }
                    }
                }catch (JSONException e) {
                    e.printStackTrace();
                }

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

                            categoryBoolean=true;
                            subCategoryString=categoryIdsList.get(i);
                            selectedCategoryId=categoryIdsList.get(i);
                            if(subCategoryString != null){
                                getSubCategories();
                            }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        categoryBoolean=false;
                        selectedCategoryId=null;
                        Toast.makeText(getApplicationContext(),"PLEASE SELECT A CATEGORY",Toast.LENGTH_LONG).show();
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(VendorCreateBusinessActivity.this,"Couldn't fetch Categories",Toast.LENGTH_LONG).show();
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
        String url=Constants.BASE_URL+"admin/get-sub-cats-of-cat/"+subCategoryString;
        Log.e(TAG, "getSubCategories: "+url);
        showDialog();
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                hideDialog();
                try {
                    if(response.getBoolean("success")){
                        JSONArray Jarray = response.getJSONArray("msg");

                        if(Jarray.length()!=0){
                            subCatsStringArray = new String[Jarray.length()];
                            subCategoriesModelArrayList= new ArrayList<>();
                        }else{
                            subCategoriesModelArrayList=null;
                        }
                        for (int i = 0; i < Jarray.length(); i++) {
                            JSONObject object     = Jarray.getJSONObject(i);
                            Log.e(TAG, "onPostExecute: "+  object.getString("name"));
                            Log.e(TAG, "onPostExecute: "+   object.getString("_id"));
                            subCategoriesModelArrayList.add(object.getString("name"));
                            subCatsStringArray[i]= object.getString("name");
                            subcategoryIdsList.add(object.getString("_id"));
                        }

                        if(subCategoriesModelArrayList.size() ==0){
                            subCategorySpinner.setVisibility(View.GONE);
                            subCatTV.setVisibility(View.GONE);
                            subCategoryImpTV.setVisibility(View.GONE);
                            subCategoryBoolean=true;
                            subCatZero=true;
                        }else{
                            subCategoryBoolean=true;
                            subCategorySpinner.setItems(subCatsStringArray);
                            subCategorySpinner.setSelection(new int[]{0});
                            selectedSubCats= new ArrayList<>();
                            selectedSubCats.add(subcategoryIdsList.get(0));
                            subCategorySpinner.setListener(new MultiSelectSpinner.OnMultipleItemsSelectedListener() {
                                @Override
                                public void selectedIndices(List<Integer> indices) {
                                    selectedSubCats= new ArrayList<>();
                                    for(int i=0;i<indices.size();i++){
                                        selectedSubCats.add(subcategoryIdsList.get(indices.get(i)));
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
                            subCategoryImpTV.setVisibility(View.VISIBLE);
                            subCatTV.setVisibility(View.VISIBLE);
                        }
                    }else{
                        subCatTV.setVisibility(View.GONE);
                        subCategoryImpTV.setVisibility(View.GONE);
                        subCategorySpinner.setVisibility(View.GONE);
                        subCategoryBoolean=true;
                        subCatZero=true;
                    }


                }catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(VendorCreateBusinessActivity.this,"Couldn't fetch Sub Categories",Toast.LENGTH_LONG).show();
                Log.e(TAG, "onErrorResponse: "+error.toString());
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"GETALLSUBCATS");
    }

    String[] paymentModesStringArray;
    ArrayList<PaymentModesModel> paymentModesModelArrayList;
    ArrayList<PaymentModesModel> selectedPaymentModes;
    MultiSelectSpinner paymentModesSpinner;
    boolean paymentModesSelected= false;
    private void getPaymentModesData(){
        paymentModesModelArrayList = new ArrayList<>();
        paymentModesSpinner = (MultiSelectSpinner)findViewById(R.id.paymentModesSpinner);
        String url = Constants.BASE_URL+"admin/get-all-payment-modes";
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.getBoolean("success")){
                        JSONArray payArray = response.getJSONArray("msg");
                        paymentModesStringArray = new String[payArray.length()];
                        for(int i =0 ;i<payArray.length();i++){
                            JSONObject payObject = payArray.getJSONObject(i);
                            paymentModesModelArrayList.add(new PaymentModesModel(payObject.getString("_id"),payObject.getString("name")));
                            paymentModesStringArray[i] = payObject.getString("name");
                        }
                        paymentModesSpinner.setItems(paymentModesStringArray);
                        paymentModesSpinner.setSelection(new int[]{0});
                        selectedPaymentModes = new ArrayList<>();
                        selectedPaymentModes.add(new PaymentModesModel(paymentModesModelArrayList.get(0).getPaymentModeId(),paymentModesModelArrayList.get(0).getPaymentModeName()));

                        paymentModesSelected=true;
                        paymentModesSpinner.setListener(new MultiSelectSpinner.OnMultipleItemsSelectedListener() {
                            @Override
                            public void selectedIndices(List<Integer> indices) {
                                selectedPaymentModes = new ArrayList<>();
                                for(int i=0;i<indices.size();i++){
                                    Log.e(TAG, "selectedIndices:PAY " +indices.get(i));
                                    selectedPaymentModes.add(new PaymentModesModel(paymentModesModelArrayList.get(indices.get(i)).getPaymentModeId(),paymentModesModelArrayList.get(indices.get(i)).getPaymentModeName()));
                                }
                                if(indices.size()>0)
                                    paymentModesSelected=true;
                                else
                                    paymentModesSelected=false;
                            }

                            @Override
                            public void selectedStrings(List<String> strings) {
                                for(int i=0;i<strings.size();i++){
                                    Log.e(TAG, "selectedStrings: "+paymentModesStringArray[i]);
                                }
                            }
                        });
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
        helper.addToRequestQueue(customJsonRequest,"PAYMENT_MODES");
    }


    boolean exitScreen = false;
    private void customBackStack(){
        if(managerLayout.getVisibility()==View.VISIBLE){
            if(exitScreen)
                finish();
            else{
                exitScreen=true;
                Toast.makeText(VendorCreateBusinessActivity.this,"Click back again to exit",Toast.LENGTH_LONG).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exitScreen=false;
                    }
                },3000);
            }
        }

        if(createBusinessLayout.getVisibility()==View.VISIBLE){
            if(exitScreen)
                finish();
            else{
                exitScreen=true;
                Toast.makeText(VendorCreateBusinessActivity.this,"Click back again to exit",Toast.LENGTH_LONG).show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exitScreen=false;
                    }
                },3000);
            }
            titleToolbar.setText("Create Business");
        }

        /*if(createBusinessLayout.getVisibility()==View.VISIBLE){
            createBusinessLayout.setVisibility(View.GONE);
            managerLayout.setVisibility(View.VISIBLE);
            titleToolbar.setText("Create Business");
        }*/
        if(businessContactLayout.getVisibility()==View.VISIBLE){
            businessContactLayout.setVisibility(View.GONE);
            createBusinessLayout.setVisibility(View.VISIBLE);
            titleToolbar.setText("Create Business");
        }
        if(businessProofsLayout.getVisibility()==View.VISIBLE){
            businessProofsLayout.setVisibility(View.GONE);
            businessContactLayout.setVisibility(View.VISIBLE);
            titleToolbar.setText("Create Business");
        }
        if(businessWebSocialLayout.getVisibility()==View.VISIBLE){
            businessWebSocialLayout.setVisibility(View.GONE);
            businessProofsLayout.setVisibility(View.VISIBLE);
            titleToolbar.setText("Create Business");
        }
        if(businessTimingsLayout.getVisibility()==View.VISIBLE){
            businessTimingsLayout.setVisibility(View.GONE);
            businessWebSocialLayout.setVisibility(View.VISIBLE);
            titleToolbar.setText("Create Business");
        }
    }

    private void getCurrentLocationLatLong(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Log.e(TAG, "onPermissionsChecked: " );
                            @SuppressLint("MissingPermission")
                            Location location = getLastKnownLocation();
                            if(location != null){
                                latitude=location.getLatitude();
                                longitude=location.getLongitude();
                                // setAddressText();
                            }else{
                                Log.e(TAG, "onPermissionsChecked: NULL" );
                                // Toast.makeText(VendorBusinessProfileActivity.this,"Coudn't detect your location",Toast.LENGTH_LONG).show();
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()){
                            getCurrentLocation();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();

    }

    private void getCurrentLocation(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Log.e(TAG, "onPermissionsChecked:");
                            @SuppressLint("MissingPermission")
                            Location location = getLastKnownLocation();
                            if(location != null){
                                latitude=location.getLatitude();
                                longitude=location.getLongitude();
                                setAddressText();
                            }else{
                                Log.e(TAG, "onPermissionsChecked: NULL");
                                Toast.makeText(VendorCreateBusinessActivity.this,"Coudn't detect your location",Toast.LENGTH_LONG).show();
                            }
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            getCurrentLocation();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();

    }

    private Location getLastKnownLocation() {
        locationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission")
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private void setAddressText(){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(addresses != null && addresses.size() != 0){
                Log.e(TAG, "onCreate: "+addresses.get(0) );
                addressBusiness.setText(addresses.get(0).getAddressLine(0));
            }else{
                Toast.makeText(getApplicationContext(),"Couldn't Detect your Location",Toast.LENGTH_LONG).show();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showDialog(){
        dialog = new ProgressDialog(VendorCreateBusinessActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("please wait...");
        dialog.show();
    }

    private void hideDialog(){
        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        customBackStack();
    }


    boolean controllerGender;
    private void setupGenderSpinner(){

        final List<String> plansList = Arrays.asList(getResources().getStringArray(R.array.genderArray));
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, plansList){
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
        controllerGenderSpinner.setAdapter(dataAdapter);
        controllerGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e(TAG, "onItemSelected: "+adapterView.getItemAtPosition(i) );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(VendorCreateBusinessActivity.this,"PLEASE SELECT GENDER",Toast.LENGTH_LONG).show();
            }
        });

    }

    /*
    * ON CLICK SAVE CONTROLLER DETAILS
    * */
    private void validateControllerLayout(){

        if(isManagerMyself){
            managerLayout.setVisibility(View.GONE);
            businessWebSocialLayout.setVisibility(View.GONE);
            businessContactLayout.setVisibility(View.GONE);
            businessProofsLayout.setVisibility(View.GONE);
            createBusinessLayout.setVisibility(View.VISIBLE);
            businessTimingsLayout.setVisibility(View.GONE);

            titleToolbar.setText("Create Business");
            // Scroll the view so that the touched editText is near the top of the scroll view
            new Thread(new Runnable()
            {
                @Override
                public
                void run ()
                {
                    // Determine where to set the scroll-to to by measuring the distance from the top of the scroll view
                    // to the control to focus on by summing the "top" position of each view in the hierarchy.
                    int yDistanceToControlsView = 0;
                    View parentView = (View) businessTypeSpinner.getParent();
                    while (true)
                    {
                        if (parentView.equals(scrollView))
                        {
                            break;
                        }
                        yDistanceToControlsView += parentView.getTop();
                        parentView = (View) parentView.getParent();
                    }

                    // Compute the final position value for the top and bottom of the control in the scroll view.
                    final int topInScrollView = yDistanceToControlsView + businessTypeSpinner.getTop();
                    final int bottomInScrollView = yDistanceToControlsView + businessTypeSpinner.getBottom();

                    // Post the scroll action to happen on the scrollView with the UI thread.
                    scrollView.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            int height =businessTypeSpinner.getHeight();
                            scrollView.smoothScrollTo(0, ((topInScrollView + bottomInScrollView) / 2) - height);
                            businessTypeSpinner.requestFocus();
                        }
                    });
                }
            }).start();
        }

        if(isManagerOthers){
            if(businessCntlrTypeId!=null){
                if(!businessCntlrTypeId.isEmpty()){
                    if(!controllerUserName.getText().toString().isEmpty()){
                        if(!controllerPassword.getText().toString().isEmpty()){
                            if(!controllerMobile.getText().toString().isEmpty()){
                                if(!controllerEmail.getText().toString().isEmpty()){
                                    if(!controllerName.getText().toString().isEmpty()){
                                        managerLayout.setVisibility(View.GONE);
                                        businessWebSocialLayout.setVisibility(View.GONE);
                                        businessContactLayout.setVisibility(View.GONE);
                                        businessProofsLayout.setVisibility(View.GONE);
                                        createBusinessLayout.setVisibility(View.VISIBLE);
                                        businessTimingsLayout.setVisibility(View.GONE);

                                        titleToolbar.setText("Create Business");
                                        // Scroll the view so that the touched editText is near the top of the scroll view
                                        new Thread(new Runnable()
                                        {
                                            @Override
                                            public
                                            void run ()
                                            {
                                                // Determine where to set the scroll-to to by measuring the distance from the top of the scroll view
                                                // to the control to focus on by summing the "top" position of each view in the hierarchy.
                                                int yDistanceToControlsView = 0;
                                                View parentView = (View) businessTypeSpinner.getParent();
                                                while (true)
                                                {
                                                    if (parentView.equals(scrollView))
                                                    {
                                                        break;
                                                    }
                                                    yDistanceToControlsView += parentView.getTop();
                                                    parentView = (View) parentView.getParent();
                                                }

                                                // Compute the final position value for the top and bottom of the control in the scroll view.
                                                final int topInScrollView = yDistanceToControlsView + businessTypeSpinner.getTop();
                                                final int bottomInScrollView = yDistanceToControlsView + businessTypeSpinner.getBottom();

                                                // Post the scroll action to happen on the scrollView with the UI thread.
                                                scrollView.post(new Runnable()
                                                {
                                                    @Override
                                                    public void run()
                                                    {
                                                        int height =businessTypeSpinner.getHeight();
                                                        scrollView.smoothScrollTo(0, ((topInScrollView + bottomInScrollView) / 2) - height);
                                                        businessTypeSpinner.requestFocus();
                                                    }
                                                });
                                            }
                                        }).start();
                                    }else{
                                        Toast.makeText(VendorCreateBusinessActivity.this,"Controller Name can't be empty",Toast.LENGTH_LONG).show();
                                    }
                                }else{
                                    Toast.makeText(VendorCreateBusinessActivity.this,"Controller Email can't be empty",Toast.LENGTH_LONG).show();
                                }
                            }else{
                                Toast.makeText(VendorCreateBusinessActivity.this,"Controller Mobile number can't be empty",Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(VendorCreateBusinessActivity.this,"Controller Password can't be empty",Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(VendorCreateBusinessActivity.this,"Controller Username can't be empty",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(VendorCreateBusinessActivity.this,"Please select controller type",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(VendorCreateBusinessActivity.this,"Please select Controller type",Toast.LENGTH_LONG).show();
            }
        }
    }

    /*ON CLICK CREATE BUSINESS
    * */
    private void validateCreateBusinessNnavigate(){
        if(validateBusinessType()){
            if(businessName.getText().toString().length()!=0){
                if(registeredName.getText().toString().length()!=0) {
                    if (categoryBoolean) {
                        if (subCategoryBoolean) {
                            if (paymentModesSelected) {
                                if (addressBusiness.getText().toString().length() != 0) {
                                    if (coordsSet) {
                                        if (doorNo.getText().toString().length() != 0) {
                                            if (streetLocality.getText().toString().length() != 0) {
                                                if (landmark.getText().toString().length() != 0) {
                                                    if (stateName.getText().toString().length() != 0) {
                                                        if (countryName.getText().toString().length() != 0) {
                                                            if (cityName.getText().toString().length() != 0) {
                                                                if (districtName.getText().toString().length() != 0) {
                                                                    if (pinCode.getText().toString().length() != 0) {
                                                                        if (pinCode.getText().toString().length() == 6) {
                                                                            managerLayout.setVisibility(View.GONE);
                                                                            businessWebSocialLayout.setVisibility(View.GONE);
                                                                            businessContactLayout.setVisibility(View.VISIBLE);
                                                                            businessProofsLayout.setVisibility(View.GONE);
                                                                            createBusinessLayout.setVisibility(View.GONE);
                                                                            businessTimingsLayout.setVisibility(View.GONE);

                                                                            titleToolbar.setText("Create Business");
                                                                            // Scroll the view so that the touched editText is near the top of the scroll view
                                                                            new Thread(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    // Determine where to set the scroll-to to by measuring the distance from the top of the scroll view
                                                                                    // to the control to focus on by summing the "top" position of each view in the hierarchy.
                                                                                    int yDistanceToControlsView = 0;
                                                                                    View parentView = (View) contactName.getParent();
                                                                                    while (true) {
                                                                                        if (parentView.equals(scrollView)) {
                                                                                            break;
                                                                                        }
                                                                                        yDistanceToControlsView += parentView.getTop();
                                                                                        parentView = (View) parentView.getParent();
                                                                                    }

                                                                                    // Compute the final position value for the top and bottom of the control in the scroll view.
                                                                                    final int topInScrollView = yDistanceToControlsView + contactName.getTop();
                                                                                    final int bottomInScrollView = yDistanceToControlsView + contactName.getBottom();

                                                                                    // Post the scroll action to happen on the scrollView with the UI thread.
                                                                                    scrollView.post(new Runnable() {
                                                                                        @Override
                                                                                        public void run() {
                                                                                            int height = contactName.getHeight();
                                                                                            scrollView.smoothScrollTo(0, ((topInScrollView + bottomInScrollView) / 2) - height);
                                                                                            contactName.requestFocus();
                                                                                        }
                                                                                    });
                                                                                }
                                                                            }).start();
                                                                        } else {
                                                                            Toast.makeText(VendorCreateBusinessActivity.this, "Pincode isn't valid", Toast.LENGTH_LONG).show();
                                                                        }
                                                                    } else {
                                                                        Toast.makeText(VendorCreateBusinessActivity.this, "Pincode cannot be empty", Toast.LENGTH_LONG).show();
                                                                    }
                                                                } else {
                                                                    Toast.makeText(VendorCreateBusinessActivity.this, "District cannot be empty", Toast.LENGTH_LONG).show();
                                                                }
                                                            } else {
                                                                Toast.makeText(VendorCreateBusinessActivity.this, "city field is empty", Toast.LENGTH_LONG).show();
                                                            }
                                                        } else {
                                                            Toast.makeText(VendorCreateBusinessActivity.this, "Country field is empty", Toast.LENGTH_LONG).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(VendorCreateBusinessActivity.this, "Please select a STATE", Toast.LENGTH_LONG).show();

                                                    }
                                                } else {
                                                    Toast.makeText(VendorCreateBusinessActivity.this, "Landmark cannot be empty", Toast.LENGTH_LONG).show();

                                                }
                                            } else {
                                                Toast.makeText(VendorCreateBusinessActivity.this, "Street or Area cannot be empty", Toast.LENGTH_LONG).show();

                                            }
                                        } else {
                                            Toast.makeText(VendorCreateBusinessActivity.this, "Door Number cannot be empty", Toast.LENGTH_LONG).show();

                                        }
                                    } else {
                                        Toast.makeText(VendorCreateBusinessActivity.this, "Please mark location on map", Toast.LENGTH_LONG).show();

                                    }
                                } else {
                                    Toast.makeText(VendorCreateBusinessActivity.this, "Address cannot be empty", Toast.LENGTH_LONG).show();

                                }
                            } else {
                                Toast.makeText(VendorCreateBusinessActivity.this, "Select atleast one modes of payment", Toast.LENGTH_LONG).show();

                            }
                        } else {
                            Toast.makeText(VendorCreateBusinessActivity.this, "Please select a Sub Category", Toast.LENGTH_LONG).show();

                        }
                    } else {
                        Toast.makeText(VendorCreateBusinessActivity.this, "Please select a Category", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(VendorCreateBusinessActivity.this,"Registered Name cannot be empty",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(VendorCreateBusinessActivity.this,"Business Name cannot be empty",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(VendorCreateBusinessActivity.this,"Please select a Business Type",Toast.LENGTH_LONG).show();
        }

    }

    /*ON CLICK BUSINESS CONTACTS
    * */
    private void validateBusinessContactsNnavigate(){
        if(contactName.getText().toString().length()!=0){
            if(contactNumber.getText().toString().length()!=0 && PhoneNumberValidator(contactNumber.getText().toString())){
                if(emailBusiness.getText().toString().length() !=0 && EmailValidator(emailBusiness.getText().toString())){

//                        scrollView.fullScroll(ScrollView.FOCUS_UP);
//                        scrollView.setSmoothScrollingEnabled(true);
//                        scrollView.smoothScrollTo(0,contactName.getTop());
//                        contactName.requestFocus();

                        businessWebSocialLayout.setVisibility(View.GONE);
                        businessContactLayout.setVisibility(View.GONE);
                        businessProofsLayout.setVisibility(View.VISIBLE);
                        createBusinessLayout.setVisibility(View.GONE);
                        businessTimingsLayout.setVisibility(View.GONE);
                        titleToolbar.setText("Create Business");

                        // Scroll the view so that the touched editText is near the top of the scroll view
                        new Thread(new Runnable(){
                            @Override
                            public
                            void run ()
                            {


                                // Determine where to set the scroll-to to by measuring the distance from the top of the scroll view
                                // to the control to focus on by summing the "top" position of each view in the hierarchy.
                                int yDistanceToControlsView = 0;
                                View parentView = (View) gstNum.getParent();
                                while (true)
                                {
                                    if (parentView.equals(scrollView))
                                    {
                                        break;
                                    }
                                    yDistanceToControlsView += parentView.getTop();
                                    parentView = (View) parentView.getParent();
                                }

                                // Compute the final position value for the top and bottom of the control in the scroll view.
                                final int topInScrollView = yDistanceToControlsView + gstNum.getTop();
                                final int bottomInScrollView = yDistanceToControlsView + gstNum.getBottom();

                                // Post the scroll action to happen on the scrollView with the UI thread.
                                scrollView.post(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        int height =gstNum.getHeight();
                                        scrollView.smoothScrollTo(0, ((topInScrollView + bottomInScrollView) / 2) - height);
                                        gstNum.requestFocus();
                                    }
                                });
                            }
                        }).start();

                }else{
                    Toast.makeText(VendorCreateBusinessActivity.this,"Email address not valid",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(VendorCreateBusinessActivity.this,"Contact Number not valid",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(VendorCreateBusinessActivity.this,"Contact person name cannot be empty",Toast.LENGTH_LONG).show();
        }
    }

    /*
    * ON CLICK BUSINESS PROOFS
    * */
    private void validateBusinessProofsNnavigate(){
        if(gstCheckedIV.getVisibility()== View.GONE || (gstCheckedIV.getVisibility()==View.VISIBLE && gstNum.getText().toString().length()!=0)){
            if(tanCheckedIV.getVisibility()== View.GONE || (tanCheckedIV.getVisibility()==View.VISIBLE && tanNum.getText().toString().length()!=0)){
                if(cinCheckedIV.getVisibility()== View.GONE || (cinCheckedIV.getVisibility()==View.VISIBLE && cinNum.getText().toString().length()!=0)){
                if(rnCheckedIV.getVisibility()== View.GONE || (rnCheckedIV.getVisibility()==View.VISIBLE && rnNum.getText().toString().length()!=0)){

                                    businessWebSocialLayout.setVisibility(View.VISIBLE);
                                    businessContactLayout.setVisibility(View.GONE);
                                    businessProofsLayout.setVisibility(View.GONE);
                                    createBusinessLayout.setVisibility(View.GONE);
                                    businessTimingsLayout.setVisibility(View.GONE);
                                    titleToolbar.setText("Create Business");
                                    // Scroll the view so that the touched editText is near the top of the scroll view
                                    new Thread(new Runnable()
                                    {
                                        @Override
                                        public
                                        void run ()
                                        {
                                            // Determine where to set the scroll-to to by measuring the distance from the top of the scroll view
                                            // to the control to focus on by summing the "top" position of each view in the hierarchy.
                                            int yDistanceToControlsView = 0;
                                            View parentView = (View) webLink.getParent();
                                            while (true)
                                            {
                                                if (parentView.equals(scrollView))
                                                {
                                                    break;
                                                }
                                                yDistanceToControlsView += parentView.getTop();
                                                parentView = (View) parentView.getParent();
                                            }

                                            // Compute the final position value for the top and bottom of the control in the scroll view.
                                            final int topInScrollView = yDistanceToControlsView + webLink.getTop();
                                            final int bottomInScrollView = yDistanceToControlsView + webLink.getBottom();

                                            // Post the scroll action to happen on the scrollView with the UI thread.
                                            scrollView.post(new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    int height =webLink.getHeight();
                                                    scrollView.smoothScrollTo(0, ((topInScrollView + bottomInScrollView) / 2) - height);
                                                    webLink.requestFocus();
                                                }
                                            });
                                        }
                                    }).start();

                }else{
                    Toast.makeText(VendorCreateBusinessActivity.this,"Registration number cannot be empty if checked true",Toast.LENGTH_LONG).show();
                }

                }else{
                    Toast.makeText(VendorCreateBusinessActivity.this,"CIN cannot be empty if checked true",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(VendorCreateBusinessActivity.this,"TAN cannot be empty if checked true",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(VendorCreateBusinessActivity.this,"GST cannot be empty if checked true",Toast.LENGTH_LONG).show();
        }

    }

    /*
    * VALIDATIONS CREATE BUSINESS
    * */
    private boolean validateBusinessType(){
        /*if(businessTypeSpinner.getSelectedItemPosition()!=0){
            Log.e(TAG, "validateBusinessType: "+businessTypeSpinner.getSelectedItemPosition() +businessTypeSpinner.getSelectedItem().toString());
            return true;
        }else
            Log.e(TAG, "validateBusinessType:ZERO "+businessTypeSpinner.getSelectedItemPosition()+businessTypeSpinner.getSelectedItem().toString() );
*/
        return true;
    }

    private boolean validateModesOfPayments(){
        if(cashB || creditB || paytmB || netB || othersB ){
            return  true;
        }
        return false;
    }


     /*
     * FORMING BUSINESS JSON OBJECT
     * */
     private void formJSON(){

         try{
             JSONObject addBusiness = new JSONObject();
             JSONObject business = new JSONObject();
             JSONObject timings = new JSONObject();
             JSONObject days = new JSONObject();
             if(workingSun){
                 JSONObject sunTimings = new JSONObject();
                 sunTimings.put("close",sunCloseSpinner.getSelectedItem().toString());
                 sunTimings.put("open",sunOpenSpinner.getSelectedItem().toString());
                 timings.put("sunday",sunTimings);
                 days.put("sunday",workingSun);
             }else{
                 days.put("sunday",false);
             }
             if(workingSat){
                 JSONObject satTimings = new JSONObject();
                 satTimings.put("close",satCloseSpinner.getSelectedItem().toString());
                 satTimings.put("open",satOpenSpinner.getSelectedItem().toString());
                 timings.put("saturday",satTimings);
                 days.put("saturday",workingSat);
             }else{
                 days.put("saturday",false);
             }
             if(workingfri){
                 JSONObject friTimings = new JSONObject();
                 friTimings.put("close",friCloseSpinner.getSelectedItem().toString());
                 friTimings.put("open",friOpenSpinner.getSelectedItem().toString());
                 timings.put("friday",friTimings);
                 days.put("friday",workingfri);
             }else{
                 days.put("friday",false);
             }
             if(workingThur){
                 JSONObject thurTimings = new JSONObject();
                 thurTimings.put("close",thurCloseSpinner.getSelectedItem().toString());
                 thurTimings.put("open",thurOpenSpinner.getSelectedItem().toString());
                 timings.put("thursday",thurTimings);
                 days.put("thursday",workingThur);
             }else{
                 days.put("thursday",false);
             }
             if(workingWed){
                 JSONObject wedTimings = new JSONObject();
                 wedTimings.put("close",wedCloseSpinner.getSelectedItem().toString());
                 wedTimings.put("open",wedOpenSpinner.getSelectedItem().toString());
                 timings.put("wednesday",wedTimings);
                 days.put("wednesday",workingWed);
             }else{
                 days.put("wednesday",false);
             }
             if(workingTue){
                 JSONObject tueTimings = new JSONObject();
                 tueTimings.put("close",tueCloseSpinner.getSelectedItem().toString());
                 tueTimings.put("open",tueOpenSpinner.getSelectedItem().toString());
                 timings.put("tuesday",tueTimings);
                 days.put("tuesday",workingTue);
             }else{
                 days.put("tuesday",false);
             }
             if(workingMon){
                 JSONObject monTimings = new JSONObject();
                 monTimings.put("close",monCloseSpinner.getSelectedItem().toString());
                 monTimings.put("open",monOpenSpinner.getSelectedItem().toString());
                 timings.put("monday",monTimings);
                 days.put("monday",workingMon);
             }else{
                 days.put("monday",false);
             }
             business.put("timings",timings);
             business.put("days",days);


             //FIRST
             business.put("plan","Free");
             business.put("category",subCategoryString);
             if(!subCatZero){
                 business.put("sub_categories",subcategoryIdsList.get((int)subCategorySpinner.getSelectedItemId()));
             }
             JSONObject payments = new JSONObject();
             /*for(int i=0;i<modesPayment.length;i++){
                 payments.put(list.get(i),modesPayment[i]);
             }*/
             if(cashB){
                 payments.put("Cash",true);

             }else{
                 payments.put("Cash",false);

             }

             if(paytmB){
                 payments.put("Paytm",true);

             }else{
                 payments.put("Paytm",false);

             }

             if(netB){
                 payments.put("Net Banking",true);

             }else{
                 payments.put("Net Banking",false);

             }

             if(othersB){
                 payments.put("Others",true);

             }else{
                 payments.put("Others",false);

             }

             if(creditB){
                 payments.put("Credit/Debit Cards",true);

             }else{
                 payments.put("Credit/Debit Cards",false);

             }
             Log.e(TAG, "formJsonString: "+payments.toString());
             business.put("modes_of_payment",payments);
             JSONObject coards = new JSONObject();
             coards.put("lat",latitude);
             coards.put("long",longitude);
             business.put("location",coards);
             business.put("business_name",businessName.getText().toString());
             business.put("pincode",pinCode.getText().toString());
             business.put("state",stateName.getText().toString());
             business.put("city",cityName.getText().toString());
             business.put("country",countryName.getText().toString());
             business.put("registered_name",registeredName.getText().toString());
             business.put("about_us",aboutBusiness.getText().toString());
             business.put("address",addressBusiness.getText().toString());
             if(businessTypeSpinner.getSelectedItemPosition()!=0)
                 business.put("business_type",businessTypeSpinner.getSelectedItem().toString());



             //SECOND
             business.put("contact_email",emailBusiness.getText().toString());
             business.put("contact_person_name",contactName.getText().toString());
             business.put("contact_person_mobile",contactNumber.getText().toString());
             business.put("contact_aadhaar_number",aadharNumber.getText().toString());
             business.put("alternate_number",alternateNumber.getText().toString());
             business.put("office_number",landline.getText().toString());
             business.put("whatsapp_number",whatsapp.getText().toString());
             business.put("about_contact_person",aboutContactPerson.getText().toString());
             business.put("home_delivery_availability",true);
             business.put("plan","FREE");


             //THIRD
             business.put("tan",tanNum.getText().toString());
             business.put("gstin",gstNum.getText().toString());
             business.put("cin",cinNum.getText().toString());
             if(tanCheckedIV.getVisibility()==View.VISIBLE){
                 business.put("tan_flag",true);
             }else{
                 business.put("tan_flag",false);
             }
             if(gstCheckedIV.getVisibility()==View.VISIBLE){
                 business.put("gstin_flag",true);
             }else{
                 business.put("gstin_flag",false);
             }
             if(cinCheckedIV.getVisibility()==View.VISIBLE){
                 business.put("cin_flag",true);
             }else{
                 business.put("cin_flag",false);
             }


             //FOURTH
             business.put("website_url",webLink.getText().toString());
             business.put("youtube_url",youtubeLink.getText().toString());
             business.put("twitter_url",twitterLink.getText().toString());
             business.put("facebook_url",fbLink.getText().toString());
             business.put("linkedin_url",linkedInLink.getText().toString());
             business.put("google_plus_url",googleplus.getText().toString());



             addBusiness.put("business",business);
             PrefManager prefManager = new PrefManager(VendorCreateBusinessActivity.this);
             addBusiness.put("vendor_id",prefManager.getVendorId(VendorCreateBusinessActivity.this));
             addBusiness.put("plan","Free");
             if(businessTypeSpinner.getSelectedItemPosition()!=0)
                 addBusiness.put("type",businessTypeSpinner.getSelectedItem().toString());


             Log.e(TAG, "formJsonString: \n"+addBusiness.toString());
             addBusinessString = addBusiness.toString();
             showDialog();
             addBusinessToVendor(addBusiness);

         }catch (Exception e){
             Log.e(TAG, "formJSON: "+e.getMessage() );
         }
     }



     private void formJsonNew(){
         try{
             JSONObject business = new JSONObject();
             //manager layout
             if(isManagerMyself){
                 business.put("business_manager",false);
                 business.put("controller_type","");
                 business.put("user_name","");
                 business.put("password","");
                 business.put("controller_mobile","");
                 business.put("controller_name","");
                 business.put("gender","");
                 business.put("manager_email","");
             }else{
                 business.put("business_manager",true);
                 business.put("controller_type",businessCntlrTypeId);
                 business.put("user_name",controllerUserName.getText().toString().trim());
                 business.put("password",controllerPassword.getText().toString().trim());
                 business.put("controller_mobile",controllerMobile.getText().toString().trim());
                 business.put("controller_name",controllerName.getText().toString().trim());
                 business.put("gender",controllerGenderSpinner.getSelectedItem());
                 business.put("manager_email",controllerEmail.getText().toString().trim());
             }

             PrefManager prefManager = new PrefManager(VendorCreateBusinessActivity.this);
             business.put("vendor_id",prefManager.getVendorId(VendorCreateBusinessActivity.this));

             //FIRST
             business.put("business_type",businessTypeId);
             if(businessTypeSpinner.getSelectedItemPosition()==0){
                 business.put("nature_business",true);
             }else{
                 business.put("nature_business",false);
             }
             business.put("seller_type",selectedSellerType);
             JSONObject coards = new JSONObject();
             coards.put("lat",latitude);
             coards.put("lng",longitude);
             business.put("location",coards);
             business.put("business_name",businessName.getText().toString());
             business.put("registered_name",registeredName.getText().toString());
             business.put("category",subCategoryString);

             JSONArray subcategoriesArray = new JSONArray();
             for(int i =0;i<selectedSubCats.size();i++){
                 subcategoriesArray.put(selectedSubCats.get(i));
             }
             business.put("sub_categories",subcategoriesArray);
             business.put("about_business",aboutBusiness.getText().toString());
             business.put("about_us",aboutBusiness.getText().toString());

             JSONArray payments = new JSONArray();
             for(int i=0;i<selectedPaymentModes.size();i++){
                 JSONObject selectedPayMode = new JSONObject();
                 selectedPayMode.put("id",selectedPaymentModes.get(i).getPaymentModeId());
                 selectedPayMode.put("itemName",selectedPaymentModes.get(i).getPaymentModeName());
                 payments.put(selectedPayMode);
             }
             business.put("modes_of_payment",payments);
             Log.e(TAG, "formJsonString: "+payments.toString());


             business.put("address",addressBusiness.getText().toString());
             business.put("door_no",doorNo.getText().toString());
             business.put("street",streetLocality.getText().toString());
             business.put("landmark",landmark.getText().toString());
             business.put("city",cityName.getText().toString());
             business.put("country",countryName.getText().toString());
             business.put("state",selectedStateId);
             business.put("district",selectedDistrictId);
             business.put("pincode",pinCode.getText().toString());


             //SECOND
             business.put("contact_email",emailBusiness.getText().toString());
             business.put("contact_person_name",contactName.getText().toString());
             business.put("contact_person_mobile",contactNumber.getText().toString());
             business.put("landline_number",aadharNumber.getText().toString());
             business.put("alternate_number",alternateNumber.getText().toString());
             business.put("office_number",landline.getText().toString());
             business.put("whatsapp_number",whatsapp.getText().toString());
             business.put("about_contact",aboutContactPerson.getText().toString());
             //business.put("home_delivery_availability",true);


             //THIRD
             business.put("tan_number",tanNum.getText().toString());
             business.put("gst_number",gstNum.getText().toString());
             business.put("cin_number",cinNum.getText().toString());
             business.put("registration_number",rnNum.getText().toString());
             business.put("gst_file","");
             business.put("tan_file","");
             business.put("cin_file","");
             business.put("registration_file","");
             if(tanCheckedIV.getVisibility()==View.VISIBLE){
                 business.put("tan_flag",true);
             }else{
                 business.put("tan_flag",false);
             }
             if(gstCheckedIV.getVisibility()==View.VISIBLE){
                 business.put("gstin_flag",true);
             }else{
                 business.put("gstin_flag",false);
             }
             if(cinCheckedIV.getVisibility()==View.VISIBLE){
                 business.put("cin_flag",true);
             }else{
                 business.put("cin_flag",false);
             }

             if(rnCheckedIV.getVisibility()==View.VISIBLE){
                 business.put("registration_flag",true);
             }else{
                 business.put("registration_flag",false);
             }


             //FOURTH
             business.put("website_url",webLink.getText().toString());
             business.put("youtube_url",youtubeLink.getText().toString());
             business.put("twitter_url",twitterLink.getText().toString());
             business.put("facebook_url",fbLink.getText().toString());
             business.put("linkedin_url",linkedInLink.getText().toString());
             business.put("google_plus_url",googleplus.getText().toString());
             business.put("insta_url",instagram.getText().toString());




            // business.put("plan","Free");


             Log.e(TAG, "formJsonString: \n"+business.toString());
             addBusinessString = business.toString();
             showDialog();
             addBusinessToVendor(business);

         }catch (Exception e){
             Log.e(TAG, "formJSON: "+e.getMessage() );
         }
     }

     private void addBusinessToendorNew(JSONObject business){
         String url = Constants.BASE_URL+"vendor/post-business";
         StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
             @Override
             public void onResponse(String res) {

                 /*try {
                     //JSONObject response  = (JSONObject)res;
                     boolean success = response.getBoolean("success");
                     if(success){
                         hideDialog();
                         Toast.makeText(VendorCreateBusinessActivity.this,"Business Created Successfully",Toast.LENGTH_LONG).show();

                         JSONObject msg = response.getJSONObject("msg");
                         String bID= msg.getString("_id");

                         BusinessDashboard businessDashboard = new BusinessDashboard();
                         businessDashboard.setBusinessId(bID);
                         businessDashboard.setBusinessName(businessName.getText().toString());

                         Intent intent = new Intent(VendorCreateBusinessActivity.this,BusinessCreatedActivity.class);
                         intent.putExtra("businessDashboard",businessDashboard);
                         startActivity(intent);
                         finish();
                     }else{
                         hideDialog();
                         String msg = response.getString("msg");
                         Log.e(TAG, "onResponse: "+msg );
                         Toast.makeText(VendorCreateBusinessActivity.this,""+msg,Toast.LENGTH_LONG).show();

                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }*/
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {

             }
         }){

             @Override
             public String getBodyContentType() {
                 return "application/json; charset=utf-8";
             }


             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String, String> postMap = new HashMap<>();
                 try {
                     postMap.put("business_manager", business.getBoolean("business_manager")+"");
                     postMap.put("controller_type", business.getString("controller_type"));
                     postMap.put("user_name", business.getString("user_name"));
                     postMap.put("password", business.getString("password"));
                     postMap.put("controller_mobile", business.getString("controller_mobile"));
                     postMap.put("controller_name", business.getString("controller_name"));
                     postMap.put("gender", business.getString("gender"));
                     postMap.put("manager_email", business.getString("manager_email"));
                     postMap.put("vendor_id", business.getString("vendor_id"));
                     postMap.put("business_type", business.getString("business_type"));
                     postMap.put("seller_type", business.getString("seller_type"));
                     postMap.put("location", business.getJSONObject("location").toString());
                     postMap.put("business_name", business.getString("business_name"));
                     postMap.put("registered_name", business.getString("registered_name"));
                     postMap.put("category", business.getString("category"));
                     postMap.put("sub_categories", business.getJSONArray("sub_categories").toString());
                     postMap.put("modes_of_payment", business.getJSONArray("modes_of_payment").toString());
                     postMap.put("about_business", business.getString("about_business"));
                     postMap.put("about_us", business.getString("about_us"));
                     postMap.put("address", business.getString("address"));
                     postMap.put("door_no", business.getString("door_no"));
                     postMap.put("street", business.getString("street"));
                     postMap.put("landmark", business.getString("landmark"));
                     postMap.put("city", business.getString("city"));
                     postMap.put("country", business.getString("country"));
                     postMap.put("state", business.getString("state"));
                     postMap.put("district", business.getString("district"));
                     postMap.put("pincode", business.getString("pincode"));
                     postMap.put("contact_email", business.getString("contact_email"));
                     postMap.put("contact_person_name", business.getString("contact_person_name"));
                     postMap.put("contact_person_mobile", business.getString("contact_person_mobile"));
                     postMap.put("landline_number", business.getString("landline_number"));
                     postMap.put("alternate_number", business.getString("alternate_number"));
                     postMap.put("office_number", business.getString("office_number"));
                     postMap.put("whatsapp_number", business.getString("whatsapp_number"));
                     postMap.put("about_contact", business.getString("about_contact"));
                     postMap.put("tan_number", business.getString("tan_number"));
                     postMap.put("gst_number", business.getString("gst_number"));
                     postMap.put("cin_number", business.getString("cin_number"));
                     postMap.put("registration_number", business.getString("registration_number"));
                     postMap.put("tan_flag", business.getBoolean("tan_flag")+"");
                     postMap.put("gstin_flag", business.getBoolean("gstin_flag")+"");
                     postMap.put("cin_flag", business.getBoolean("cin_flag")+"");
                     postMap.put("registration_flag", business.getBoolean("registration_flag")+"");
                     postMap.put("website_url", business.getString("website_url"));
                     postMap.put("youtube_url", business.getString("youtube_url"));
                     postMap.put("twitter_url", business.getString("twitter_url"));
                     postMap.put("facebook_url", business.getString("facebook_url"));
                     postMap.put("linkedin_url", business.getString("linkedin_url"));
                     postMap.put("google_plus_url", business.getString("google_plus_url"));
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
                 return postMap;
             }
         };

     }

     private void formJSONnew(){
         try{
             JSONObject business = new JSONObject();
             JSONObject timings = new JSONObject();
             JSONObject days = new JSONObject();
             if(workingSun){
                 JSONObject sunTimings = new JSONObject();
                 sunTimings.put("close",sunCloseSpinner.getSelectedItem().toString());
                 sunTimings.put("open",sunOpenSpinner.getSelectedItem().toString());
                 sunTimings.put("isWorkingDay",workingSun);
                 timings.put("sunday",sunTimings);
                 days.put("sunday",workingSun);
             }else{
                 JSONObject sunTimings = new JSONObject();
                 sunTimings.put("close","");
                 sunTimings.put("open","");
                 sunTimings.put("isWorkingDay",workingSun);
                 timings.put("sunday",sunTimings);
                 days.put("sunday",false);
             }
             if(workingSat){
                 JSONObject satTimings = new JSONObject();
                 satTimings.put("close",satCloseSpinner.getSelectedItem().toString());
                 satTimings.put("open",satOpenSpinner.getSelectedItem().toString());
                 satTimings.put("isWorkingDay",workingSat);
                 timings.put("saturday",satTimings);
                 days.put("saturday",workingSat);
             }else{
                 JSONObject satTimings = new JSONObject();
                 satTimings.put("close","");
                 satTimings.put("open","");
                 satTimings.put("isWorkingDay",workingSat);
                 timings.put("saturday",satTimings);
                 days.put("saturday",false);
             }
             if(workingfri){
                 JSONObject friTimings = new JSONObject();
                 friTimings.put("close",friCloseSpinner.getSelectedItem().toString());
                 friTimings.put("open",friOpenSpinner.getSelectedItem().toString());
                 friTimings.put("isWorkingDay",workingfri);
                 timings.put("friday",friTimings);
                 days.put("friday",workingfri);
             }else{
                 JSONObject friTimings = new JSONObject();
                 friTimings.put("close","");
                 friTimings.put("open","");
                 friTimings.put("isWorkingDay",workingfri);
                 timings.put("friday",friTimings);
                 days.put("friday",false);
             }
             if(workingThur){
                 JSONObject thurTimings = new JSONObject();
                 thurTimings.put("close",thurCloseSpinner.getSelectedItem().toString());
                 thurTimings.put("open",thurOpenSpinner.getSelectedItem().toString());
                 thurTimings.put("isWorkingDay",workingThur);
                 timings.put("thursday",thurTimings);
                 days.put("thursday",workingThur);
             }else{
                 JSONObject thurTimings = new JSONObject();
                 thurTimings.put("close","");
                 thurTimings.put("open","");
                 thurTimings.put("isWorkingDay",workingThur);
                 timings.put("thursday",thurTimings);
                 days.put("thursday",false);
             }
             if(workingWed){
                 JSONObject wedTimings = new JSONObject();
                 wedTimings.put("close",wedCloseSpinner.getSelectedItem().toString());
                 wedTimings.put("open",wedOpenSpinner.getSelectedItem().toString());
                 wedTimings.put("isWorkingDay",workingWed);
                 timings.put("wednesday",wedTimings);
                 days.put("wednesday",workingWed);
             }else{
                 JSONObject wedTimings = new JSONObject();
                 wedTimings.put("close","");
                 wedTimings.put("open","");
                 wedTimings.put("isWorkingDay",workingWed);
                 timings.put("wednesday",wedTimings);
                 days.put("wednesday",false);
             }
             if(workingTue){
                 JSONObject tueTimings = new JSONObject();
                 tueTimings.put("close",tueCloseSpinner.getSelectedItem().toString());
                 tueTimings.put("open",tueOpenSpinner.getSelectedItem().toString());
                 tueTimings.put("isWorkingDay",workingTue);
                 timings.put("tuesday",tueTimings);
                 days.put("tuesday",workingTue);
             }else{
                 JSONObject tueTimings = new JSONObject();
                 tueTimings.put("close","");
                 tueTimings.put("open","");
                 tueTimings.put("isWorkingDay",workingTue);
                 timings.put("tuesday",tueTimings);
                 days.put("tuesday",false);
             }
             if(workingMon){
                 JSONObject monTimings = new JSONObject();
                 monTimings.put("close",monCloseSpinner.getSelectedItem().toString());
                 monTimings.put("open",monOpenSpinner.getSelectedItem().toString());
                 monTimings.put("isWorkingDay",workingMon);
                 timings.put("monday",monTimings);
                 days.put("monday",workingMon);
             }else{
                 JSONObject monTimings = new JSONObject();
                 monTimings.put("close","");
                 monTimings.put("open","");
                 monTimings.put("isWorkingDay",workingMon);
                 timings.put("monday",monTimings);
                 days.put("monday",false);
             }
             business.put("timings",timings);
             business.put("working_days",days);


             //FIRST
             business.put("business_plan","Free");
             business.put("category",subCategoryString);
             if(!subCatZero){
                 business.put("sub_categories",subcategoryIdsList.get((int)subCategorySpinner.getSelectedItemId()));
             }
             JSONObject payments = new JSONObject();
             /*for(int i=0;i<modesPayment.length;i++){
                 payments.put(list.get(i),modesPayment[i]);
             }*/
             if(cashB){
                 payments.put("Cash",true);

             }else{
                 payments.put("Cash",false);

             }

             if(paytmB){
                 payments.put("Paytm",true);

             }else{
                 payments.put("Paytm",false);

             }

             if(netB){
                 payments.put("Net Banking",true);

             }else{
                 payments.put("Net Banking",false);

             }

             if(othersB){
                 payments.put("Others",true);

             }else{
                 payments.put("Others",false);

             }

             if(creditB){
                 payments.put("Credit/Debit Cards",true);

             }else{
                 payments.put("Credit/Debit Cards",false);

             }
             Log.e(TAG, "formJsonString: "+payments.toString());
             business.put("modes_of_payment",payments);
             JSONObject coards = new JSONObject();
             coards.put("lat",latitude);
             coards.put("long",longitude);
             business.put("location",coards);
             business.put("business_name",businessName.getText().toString());
             business.put("pincode",pinCode.getText().toString());
             business.put("state",stateName.getText().toString());
             business.put("city",cityName.getText().toString());
             business.put("country",countryName.getText().toString());
             business.put("registered_name",registeredName.getText().toString());
             business.put("about_us",aboutBusiness.getText().toString());
             business.put("address",addressBusiness.getText().toString());
             if(businessTypeSpinner.getSelectedItemPosition()!=0)
                 business.put("business_type",businessTypeSpinner.getSelectedItem().toString());



             //SECOND
             business.put("contact_email",emailBusiness.getText().toString());
             business.put("contact_person_name",contactName.getText().toString());
             business.put("contact_person_mobile",contactNumber.getText().toString());
             business.put("contact_aadhaar_number",aadharNumber.getText().toString());
             business.put("alternate_number",alternateNumber.getText().toString());
             business.put("office_number",landline.getText().toString());
             business.put("whatsapp_number",whatsapp.getText().toString());
             business.put("about_contact_person",aboutContactPerson.getText().toString());
             business.put("home_delivery_availability",true);


             //THIRD
             business.put("tan",tanNum.getText().toString());
             business.put("gstin",gstNum.getText().toString());
             business.put("cin",cinNum.getText().toString());
             if(tanCheckedIV.getVisibility()==View.VISIBLE){
                 business.put("tan_flag",true);
             }else{
                 business.put("tan_flag",false);
             }
             if(gstCheckedIV.getVisibility()==View.VISIBLE){
                 business.put("gstin_flag",true);
             }else{
                 business.put("gstin_flag",false);
             }
             if(cinCheckedIV.getVisibility()==View.VISIBLE){
                 business.put("cin_flag",true);
             }else{
                 business.put("cin_flag",false);
             }


             //FOURTH
             business.put("website_url",webLink.getText().toString());
             business.put("youtube_url",youtubeLink.getText().toString());
             business.put("twitter_url",twitterLink.getText().toString());
             business.put("facebook_url",fbLink.getText().toString());
             business.put("linkedin_url",linkedInLink.getText().toString());
             business.put("google_plus_url",googleplus.getText().toString());



             PrefManager prefManager = new PrefManager(VendorCreateBusinessActivity.this);
             business.put("vendor_id",prefManager.getVendorId(VendorCreateBusinessActivity.this));
            // business.put("plan","Free");


             Log.e(TAG, "formJsonString: \n"+business.toString());
             addBusinessString = business.toString();
             showDialog();
             addBusinessToVendor(business);

         }catch (Exception e){
             Log.e(TAG, "formJSON: "+e.getMessage() );
         }
     }

     private void addBusinessToVendor(JSONObject jsonObject){
        String url = Constants.BASE_URL+"vendor/post-business-mobile";

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    boolean success = response.getBoolean("success");
                    if(success){
                        hideDialog();
                        Toast.makeText(VendorCreateBusinessActivity.this,"Business Created Successfully",Toast.LENGTH_LONG).show();

                        JSONObject msg = response.getJSONObject("msg");
                        String bID= msg.getString("_id");

                        BusinessDetails businessDashboard = new BusinessDetails();
                        businessDashboard.setBusinessId(bID);
                        businessDashboard.setBusinessName(businessName.getText().toString());
                        if(businessTypeSpinner.getSelectedItemPosition()==0){
                            businessDashboard.setService(false);
                        }else{
                            businessDashboard.setService(true);
                        }

                        Intent intent = new Intent(VendorCreateBusinessActivity.this,BusinessCreatedActivity.class);
                        intent.putExtra("businessDetails",businessDashboard);
                        startActivity(intent);
                        finish();
                    }else{
                        hideDialog();
                        String msg = response.getString("msg");
                        Log.e(TAG, "onResponse: "+msg );
                        Toast.makeText(VendorCreateBusinessActivity.this,""+msg,Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideDialog();
                Toast.makeText(VendorCreateBusinessActivity.this,"Business not created.Please Try Again",Toast.LENGTH_LONG).show();

                Log.e(TAG, "onErrorResponse: "+error.getMessage());

            }
        });

        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"ADDBUSINESSTOVENDOR");
     }


}
