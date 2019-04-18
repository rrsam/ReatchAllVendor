package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.reatchall.charan.reatchallVendor.Vendor.CreateBusiness.VendorCreateBusinessActivity;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BusinessDetails;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IDashboardInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorHomeAdapterNew extends RecyclerView.Adapter<VendorHomeAdapterNew.VendorHomeNewViewHolder> {


    ArrayList<BusinessDetails> arrayList;
    private Context context;
    private IDashboardInterface iDashboardInterface;
    ReatchAll helper;
    private static final String TAG = "VendorHomeAdapterNew";
    CustomProgressDialog customProgressDialog;


    public VendorHomeAdapterNew(ArrayList<BusinessDetails> arrayList, Context context,IDashboardInterface iDashboardInterface) {
        this.arrayList = arrayList;
        this.context = context;
        this.iDashboardInterface=iDashboardInterface;
        helper = ReatchAll.getInstance();
        customProgressDialog=new CustomProgressDialog(context);
    }


    @Override
    public VendorHomeAdapterNew.VendorHomeNewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor_home_business_list,parent,false);
        return new VendorHomeNewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VendorHomeAdapterNew.VendorHomeNewViewHolder holder, final int position) {

        getBusinessStatus(arrayList.get(position).getBusinessId(),holder.switchOn,holder.switchOff,holder.statusTV,holder.openCloseLayout);

        if(position==arrayList.size()-1){
            holder.addBusinessLayout.setVisibility(View.VISIBLE);
        }else{
            holder.addBusinessLayout.setVisibility(View.GONE);
        }

        if(holder.addBusinessLayout.getVisibility()==View.VISIBLE){
            holder.addBusinessLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context,VendorCreateBusinessActivity.class));
                }
            });
        }


        holder.viewDashboardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrayList.get(position).isTimings_flag()){
                    if(arrayList.get(position).isApproved()){
                        iDashboardInterface.passBusinessId(arrayList.get(position));
                    }else{
                        Toast.makeText(context,"Your business isn't approved yet.Please contact reatchall customer care",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Intent intent = new Intent(context,VendorBusinessTimingsModeActivity.class);
                    intent.putExtra("businessDetails",arrayList.get(position));
                    context.startActivity(intent);
                }
            }
        });

        holder.businessName.setText(arrayList.get(position).getBusinessName());

        holder.statusLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.statusTV.getText().toString().contains("OPEN") || holder.statusTV.getText().toString().equalsIgnoreCase("open")){
                    customProgressDialog.showDialog();
                    changeBusinessStatus(arrayList.get(position).getBusinessId(),"CLOSED",holder.switchOn,holder.switchOff,holder.statusTV,holder.openCloseLayout);
                }else{
                    customProgressDialog.showDialog();
                    changeBusinessStatus(arrayList.get(position).getBusinessId(),"OPEN",holder.switchOn,holder.switchOff,holder.statusTV,holder.openCloseLayout);
                }
            }
        });

    }

    private void getBusinessStatus(String businessId, final ImageView open, final ImageView closed, final FontTextView statusTV,LinearLayout openCloseLayout){
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

                                open.setVisibility(View.VISIBLE);
                                closed.setVisibility(View.GONE);
                                statusTV.setText("Open");
                                openCloseLayout.setBackground(context.getResources().getDrawable(R.drawable.orange_rounded));

                            }else{
                                open.setVisibility(View.GONE);
                                closed.setVisibility(View.VISIBLE);
                                statusTV.setText("Closed");
                                openCloseLayout.setBackground(context.getResources().getDrawable(R.drawable.red_ten_rounded_bg));

                            }
                        }else{
                            Log.e(TAG, "onResponse: NULL" );
                            open.setVisibility(View.VISIBLE);
                            closed.setVisibility(View.GONE);
                            statusTV.setText("Open");
                            openCloseLayout.setBackground(context.getResources().getDrawable(R.drawable.orange_rounded));

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

    private void changeBusinessStatus(String businessId, final String status, final ImageView open, final ImageView closed,final FontTextView fontTextView,LinearLayout openCloseLayout){

        String url= Constants.BASE_URL+ "vendor/post-business-status";
        formString(businessId,status);
        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e(TAG, "onResponse: status "+response);
                try{
                    customProgressDialog.hideDialog();

                    boolean success = response.getBoolean("success");
                    if(success){

                        Toast.makeText(context,"Status Changed to "+status,Toast.LENGTH_LONG).show();
                        Toast.makeText(context,"Status will be back to normal in 12 Hrs",Toast.LENGTH_LONG).show();

                        if(status.contains("open") || status.equalsIgnoreCase("open") || status.contains("OPEN")){

                            open.setVisibility(View.VISIBLE);
                            closed.setVisibility(View.GONE);
                            fontTextView.setText("Open");
                            openCloseLayout.setBackground(context.getResources().getDrawable(R.drawable.orange_rounded));


                        }else{
                            open.setVisibility(View.GONE);
                            closed.setVisibility(View.VISIBLE);
                            fontTextView.setText("Closed");
                            openCloseLayout.setBackground(context.getResources().getDrawable(R.drawable.red_ten_rounded_bg));

                        }


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

    public class VendorHomeNewViewHolder extends RecyclerView.ViewHolder {

        FontTextView businessName,statusTV;
        ImageView switchOn,switchOff;
        RelativeLayout viewDashboardLayout;
        LinearLayout addBusinessLayout,statusLayout,openCloseLayout;

        public VendorHomeNewViewHolder(View itemView) {
            super(itemView);

            businessName=(FontTextView)itemView.findViewById(R.id.businessName);
            statusTV=(FontTextView)itemView.findViewById(R.id.statusTV);
            switchOn=(ImageView)itemView.findViewById(R.id.openIV);
            switchOff=(ImageView)itemView.findViewById(R.id.closedIV);
            viewDashboardLayout=(RelativeLayout)itemView.findViewById(R.id.viewDashboard);
            addBusinessLayout=(LinearLayout)itemView.findViewById(R.id.addLayout);
            statusLayout=(LinearLayout)itemView.findViewById(R.id.statusLayout);
            openCloseLayout=(LinearLayout)itemView.findViewById(R.id.openCloseLayout);
        }
    }
}
