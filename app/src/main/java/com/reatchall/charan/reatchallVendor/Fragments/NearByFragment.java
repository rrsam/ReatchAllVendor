package com.reatchall.charan.reatchallVendor.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.reatchall.charan.reatchallVendor.Adapters.GridNearByAdapter;
import com.reatchall.charan.reatchallVendor.Adapters.ListNearByAdapter;
import com.reatchall.charan.reatchallVendor.Models.NearByBusinessModel;
import com.reatchall.charan.reatchallVendor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by NaNi on 08/02/18.
 */

public class NearByFragment extends Fragment {


    private static final String TAG = "NearByFragment";
    ImageView grid,list;

    RecyclerView recyclerViewGrid,recyclerViewList;

    ArrayList<String> arrayList= new ArrayList<>();
    ArrayList<NearByBusinessModel> nearByBusinessModelArrayList;

    GridNearByAdapter gridNearByAdapter;
    ListNearByAdapter listNearByAdapter;
    boolean checkGrid=true,checkList=true;


    LocationManager locationManager;
    private double latitude, longitude;
    String latlonhg=null;

    public NearByFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nearby, container, false);

        return rootView;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        grid=(ImageView)view.findViewById(R.id.list_grid);
        list=(ImageView)view.findViewById(R.id.list_icon);
        recyclerViewGrid=(RecyclerView)view.findViewById(R.id.near_by_rcv_grid);
        recyclerViewList=(RecyclerView)view.findViewById(R.id.near_by_rcv_list);

        recyclerViewGrid.setHasFixedSize(true);
        recyclerViewList.setHasFixedSize(true);


        nearByBusinessModelArrayList = new ArrayList<>();

        for(int i=0;i<10;i++){
            nearByBusinessModelArrayList.add(new NearByBusinessModel("","","","",0,""));
        }


        getCurrentLocationLatLong();

        /*while(latlonhg == null){

        }*/

       // new GetNearByBusiness().execute();

        grid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    recyclerViewGrid.setVisibility(View.VISIBLE);
                    recyclerViewList.setVisibility(View.GONE);



                grid.setImageResource(R.drawable.ic_grid_white);
                list.setImageResource(R.drawable.ic_list_grey);
            }
        });


        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recyclerViewGrid.setVisibility(View.GONE);
                recyclerViewList.setVisibility(View.VISIBLE);
                list.setImageResource(R.drawable.ic_list_white);
                grid.setImageResource(R.drawable.ic_grid_grey);


            }
        });

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerViewGrid.setLayoutManager(mLayoutManager);
        recyclerViewGrid.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));

        gridNearByAdapter=new GridNearByAdapter(nearByBusinessModelArrayList,getActivity());
        recyclerViewGrid.setAdapter(gridNearByAdapter);




        RecyclerView.LayoutManager mLayoutManagerList = new GridLayoutManager(getActivity(), 1);
        recyclerViewList.setLayoutManager(mLayoutManagerList);
        recyclerViewList.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));

        listNearByAdapter=new ListNearByAdapter(nearByBusinessModelArrayList,getActivity());
        recyclerViewList.setAdapter(listNearByAdapter);

        grid.performClick();

    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    public  class GetNearByBusiness extends AsyncTask<String,String,String> {
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("please wait.");
            dialog.show();
        }

        Response response;
        @Override
        protected String doInBackground(String... strings) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url("http://13.127.169.96:3000/user/get-all-businesses")
                    .get()
                    .addHeader("cache-control", "no-cache")
                    .build();

            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(response != null && response.isSuccessful()){
                nearByBusinessModelArrayList = new ArrayList<>();

                try {
                    String res = response.body().string();
                    JSONObject business = new JSONObject(res);
                    JSONArray msg = business.getJSONArray("msg");
                    int k=1;
                    for(int i=0;i<msg.length();i++){

                        JSONObject businessDetails = msg.getJSONObject(i);
                        JSONObject businessObject = businessDetails.getJSONObject("business");
                        double latitud=0,longitud=0;
                        JSONObject coords = businessObject.getJSONObject("coords");
                        latitud=Double.parseDouble(coords.getString("lat"));
                        longitud=Double.parseDouble(coords.getString("long"));
                        nearByBusinessModelArrayList.add(new NearByBusinessModel(businessDetails.getString("_id"),businessDetails.getString("vendor_id"),businessObject.getString("name"),distanceNew(latitud,longitud),longitude,""));

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{

            }


            return null;
        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(response != null && response.isSuccessful()){
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                recyclerViewGrid.setLayoutManager(mLayoutManager);
                recyclerViewGrid.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));

                gridNearByAdapter=new GridNearByAdapter(nearByBusinessModelArrayList,getActivity());
                recyclerViewGrid.setAdapter(gridNearByAdapter);




                RecyclerView.LayoutManager mLayoutManagerList = new GridLayoutManager(getActivity(), 1);
                recyclerViewList.setLayoutManager(mLayoutManagerList);
                recyclerViewList.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));

                listNearByAdapter=new ListNearByAdapter(nearByBusinessModelArrayList,getActivity());
                recyclerViewList.setAdapter(listNearByAdapter);
                dialog.dismiss();
            }else{
                dialog.dismiss();
                Toast.makeText(getActivity(),"PLEASE TRY AGAIN",Toast.LENGTH_LONG).show();
            }

        }

    }





    private void getCurrentLocationLatLong(){
        Dexter.withActivity(getActivity())
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
                                latlonhg=latitude+longitude+"";
                                // setAddressText();
                            }else{
                                Log.e(TAG, "onPermissionsChecked: NULL" );
                                // Toast.makeText(VendorBusinessProfileActivity.this,"Coudn't detect your location",Toast.LENGTH_LONG).show();
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
    private void getCurrentLocation(){
        Dexter.withActivity(getActivity())
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
                            }else{
                                Log.e(TAG, "onPermissionsChecked: NULL" );
                                Toast.makeText(getActivity(),"Coudn't detect your location",Toast.LENGTH_LONG).show();
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
        locationManager = (LocationManager)getActivity().getSystemService(LOCATION_SERVICE);
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


    private double getDistance(double lat,double longt){
        Location startPoint=new Location("locationA");
        startPoint.setLatitude(latitude);
        startPoint.setLongitude(longitude);

        Log.e(TAG, "getDistance: USER LOCATION"+latitude+"LONG"+longitude);
        Log.e(TAG, "getDistance: SHOP LOCATION"+lat+"LONG"+longt);

        Location endPoint=new Location("locationA");
        endPoint.setLatitude(lat);
        endPoint.setLongitude(longt);

        double distance=startPoint.distanceTo(endPoint);
        Log.e(TAG, "getDistance: "+distance );

        return distance/1000;
    }


    public  float distance(float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        float lat1= (float) latitude;
        float lng1 =(float)longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);
        dist=dist/1000;
        return dist;
    }


    private String distanceNew(double lat2, double lon2) {
        double theta = longitude - lon2;
        double dist = Math.sin(deg2rad(latitude)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(latitude)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        DecimalFormat decimalFormat = new DecimalFormat(".#");
        String result = decimalFormat.format(dist);
        return (result);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
