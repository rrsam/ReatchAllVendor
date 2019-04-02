package com.reatchall.charan.reatchallVendor.Cabs;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.reatchall.charan.reatchallVendor.R;

public class CabsHomeActivity extends AppCompatActivity implements GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMarkerDragListener,
        SeekBar.OnSeekBarChangeListener,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    private static final String TAG = "CabsHomeActivity";

    //vars
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;

    ImageView auto,car,bike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cabs_activity_home);

        auto=(ImageView)findViewById(R.id.autoTab);
        car=(ImageView)findViewById(R.id.carTab);
        bike=(ImageView)findViewById(R.id.bikeTab);

        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auto.setBackgroundColor(getResources().getColor(R.color.grey));
                car.setBackgroundColor(Color.TRANSPARENT);
                bike.setBackgroundColor(Color.TRANSPARENT);

                auto.setImageDrawable(getResources().getDrawable(R.drawable.autonew));
                car.setImageDrawable(getResources().getDrawable(R.drawable.car));
                bike.setImageDrawable(getResources().getDrawable(R.drawable.vespablack));
            }
        });

        car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                car.setBackgroundColor(getResources().getColor(R.color.grey));
                auto.setBackgroundColor(Color.TRANSPARENT);
                bike.setBackgroundColor(Color.TRANSPARENT);

                auto.setImageDrawable(getResources().getDrawable(R.drawable.autoblack));
                car.setImageDrawable(getResources().getDrawable(R.drawable.carcolor));
                bike.setImageDrawable(getResources().getDrawable(R.drawable.vespablack));
            }
        });

        bike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bike.setBackgroundColor(getResources().getColor(R.color.grey));
                car.setBackgroundColor(Color.TRANSPARENT);
                auto.setBackgroundColor(Color.TRANSPARENT);
                auto.setImageDrawable(getResources().getDrawable(R.drawable.autoblack));
                car.setImageDrawable(getResources().getDrawable(R.drawable.car));
                bike.setImageDrawable(getResources().getDrawable(R.drawable.vespa));
            }
        });

        if(!mLocationPermissionsGranted){
            Log.e(TAG, "onViewCreated: "+mLocationPermissionsGranted );
            initMap();
        }else{
            Log.e(TAG, "onViewCreated: "+mLocationPermissionsGranted );

            getLocationPermission();

        }

        auto.performClick();
    }

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};

        if(ContextCompat.checkSelfPermission(this,
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            mLocationPermissionsGranted = true;

        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.e(TAG, "onMapReady: "+"MAP READY" );
        mMap = googleMap;
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(CabsHomeActivity.this,R.raw.custom_map);
        mMap.setMapStyle(style);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);
    }
}
