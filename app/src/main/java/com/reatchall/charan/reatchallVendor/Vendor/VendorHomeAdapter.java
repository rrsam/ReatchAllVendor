package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.CustomProgressDialog;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDashboard;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IDashboardInterface;
import com.suke.widget.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 19/02/18.
 */

public class VendorHomeAdapter extends RecyclerView.Adapter<VendorHomeAdapter.VendorHomeViewHolder> {

    ArrayList<BusinessDashboard> arrayList;
    private Context context;
    private IDashboardInterface iDashboardInterface;
    ReatchAll helper;
    private static final String TAG = "VendorHomeAdapter";
    CustomProgressDialog customProgressDialog;


    public VendorHomeAdapter(ArrayList<BusinessDashboard> arrayList, Context context,IDashboardInterface iDashboardInterface) {
        this.arrayList = arrayList;
        this.context = context;
        this.iDashboardInterface=iDashboardInterface;
        helper = ReatchAll.getInstance();
        customProgressDialog=new CustomProgressDialog(context);
    }

    @Override
    public VendorHomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_item_home_fragment, parent, false);

        return new VendorHomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VendorHomeViewHolder holder, final int position) {


        holder.businessName.setText(arrayList.get(position).getBusinessName());

        holder.viewDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //iDashboardInterface.passBusinessId(arrayList.get(position));
            }
        });



        holder.switchFake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e(TAG, "onClick: CLICKED" );
                if(holder.switchButton.isChecked()){
                    customProgressDialog.showDialog();
                    changeBusinessStatus(arrayList.get(position).getBusinessId(),"OPEN",holder.switchButton,holder.openCloseStatus);
                }else{
                    customProgressDialog.showDialog();
                    changeBusinessStatus(arrayList.get(position).getBusinessId(),"CLOSED",holder.switchButton,holder.openCloseStatus);

                }
            }
        });

        final GestureDetector gd = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);

            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.e(TAG, "onClick: SINGLE TAP" );
                if(holder.switchButton.isChecked()){
                    customProgressDialog.showDialog();
                    changeBusinessStatus(arrayList.get(position).getBusinessId(),"CLOSED",holder.switchButton,holder.openCloseStatus);
                }else{
                    customProgressDialog.showDialog();
                    changeBusinessStatus(arrayList.get(position).getBusinessId(),"OPEN",holder.switchButton,holder.openCloseStatus);

                }
                return super.onSingleTapConfirmed(e);
            }
        });

        holder.switchButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                Log.e("MOTHH", view.getId() + "");
                return gd.onTouchEvent(event);
            }
        });
        getBusinessStatus(arrayList.get(position).getBusinessId(),holder.switchButton,holder.openCloseStatus);
    }


    private void getBusinessStatus(String businessId, final SwitchButton switchButton, final FontTextView fontTextView){
        Log.e(TAG, "getBusinessStatus: "+businessId);
        String url= Constants.BASE_URL + "vendor/get-business-status/"+businessId;
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try{

                    boolean success = response.getBoolean("success");
                    if(success){
                        JSONArray msg = response.getJSONArray("msg");
                        if(msg.length()!=0){
                            JSONObject msgvalue = msg.getJSONObject(0);
                            String status = msgvalue.getString("status");
                            Log.e(TAG, "onResponse: "+status );
                            if(status.contains("open") || status.equalsIgnoreCase("open") || status.contains("OPEN")){
                                switchButton.setChecked(true);
                                fontTextView.setText("OPEN");
                            }else{
                                switchButton.setChecked(false);
                                fontTextView.setText("CLOSED");
                            }
                        }else{
                            Log.e(TAG, "onResponse: NULL" );
                            fontTextView.setText("OPEN");
                            switchButton.setChecked(true);
                        }
                    }else{
                    }
                }catch (JSONException e){

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e(TAG, "onErrorResponse: "+error.getMessage() );
            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,businessId);
    }

    JSONObject json;
    private void formString(String bID,String status){
        json = new JSONObject();
        try {
            json.put("b_id",bID);
            json.put("status",status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void changeBusinessStatus(String businessId, final String status, final SwitchButton switchButton, final FontTextView fontTextView){

        String url= Constants.BASE_URL + "vendor/post-business-status";
        formString(businessId,status);
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: "+ response.toString());
                try{
                    customProgressDialog.hideDialog();

                    boolean success = response.getBoolean("success");
                    if(success){

                        Toast.makeText(context,"Status Changed to "+status,Toast.LENGTH_LONG).show();
                        Toast.makeText(context,"Status will be back to normal in 12 Hrs",Toast.LENGTH_LONG).show();
                        fontTextView.setText(status);


                    }else{
                        Toast.makeText(context,"Failed to Change Status",Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    Log.e(TAG, "onResponse: "+e.getLocalizedMessage() );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                customProgressDialog.hideDialog();
                Log.e(TAG, "onErrorResponse: "+error.getMessage() );

            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"BID"+businessId);
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VendorHomeViewHolder extends RecyclerView.ViewHolder {

        FontTextView businessName;
        FontTextView viewDashboard;
        SwitchButton switchButton;
        FontTextView openCloseStatus;
        RelativeLayout switchLayout;
        Button switchFake;
        public VendorHomeViewHolder(View itemView) {
            super(itemView);
            businessName=(FontTextView)itemView.findViewById(R.id.businessName);
            viewDashboard=(FontTextView)itemView.findViewById(R.id.viewDashboard);
            openCloseStatus=(FontTextView)itemView.findViewById(R.id.open_or_close);
            switchButton=(SwitchButton)itemView.findViewById(R.id.switch_button);
            switchLayout=(RelativeLayout) itemView.findViewById(R.id.switchLayout);
            switchFake=(Button)itemView.findViewById(R.id.switchFake);

        }
    }
}
