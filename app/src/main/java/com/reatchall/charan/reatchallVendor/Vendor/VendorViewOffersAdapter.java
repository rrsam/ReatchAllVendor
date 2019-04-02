package com.reatchall.charan.reatchallVendor.Vendor;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.ConfirmationDialog;
import com.reatchall.charan.reatchallVendor.Utils.Constants;
import com.reatchall.charan.reatchallVendor.Utils.CustomJsonRequest;
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Utils.StringConstants;
import com.reatchall.charan.reatchallVendor.Vendor.Models.OfferDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorViewOffersAdapter extends RecyclerView.Adapter<VendorViewOffersAdapter.ViewOffersViewHolder> {

    Context context;
    ArrayList<OfferDetails> arrayList;
    FragmentManager fragmentManager;
    PrefManager prefManager;
    ReatchAll helper = ReatchAll.getInstance();
    ViewOffersViewHolder[] holders;
    public VendorViewOffersAdapter(Context context, ArrayList<OfferDetails> arrayList, FragmentManager fragmentManager) {
        this.context = context;
        this.arrayList = arrayList;
        this.fragmentManager  = fragmentManager;
        holders = new ViewOffersViewHolder[arrayList.size()];
        prefManager = new PrefManager(context);
    }

    @Override
    public ViewOffersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor_view_offer,parent,false);
        return new ViewOffersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewOffersViewHolder holder, int position) {
        holders[position] = holder;
        holder.seeItemsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(arrayList.get(position).getItemsArrayList().size()>0){
                    OfferItemsPopupFragment dialog = new OfferItemsPopupFragment();
                    Bundle args = new Bundle();
                    args.putStringArrayList(StringConstants.OFFER_ITEMS_ARRAY, arrayList.get(position).getItemsArrayList());
                    dialog.setArguments(args);
                    dialog.show(fragmentManager, "dialog");
                }
            }
        });

        if(arrayList.get(position).getItemsArrayList().size()==0){
            holder.itemsFlagIv.setVisibility(View.INVISIBLE);
        }else{
            holder.itemsFlagIv.setVisibility(View.VISIBLE);
        }

        holder.offerName.setText(arrayList.get(position).getOfferName());
        holder.offerType.setText(arrayList.get(position).getOfferType());
        if(arrayList.get(position).isDiscountType()){
            holder.discountType.setText("Percentage");
        }else{
            holder.discountType.setText("Rupees");
        }

        holder.discountValue.setText(arrayList.get(position).getDiscountValue());
        if(!arrayList.get(position).isOfferStatus()){
            holder.offerStatus.setText("Visible");
            holder.hideOffer.setText("Hide");
        }else{
            holder.offerStatus.setText("Hidden");
            holder.hideOffer.setText("Show");
        }

        holder.startTime.setText(arrayList.get(position).getStartTime());
        holder.endTime.setText(arrayList.get(position).getEndTime());

        holder.editOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((VendorViewOffersActivity)context).editOffer(arrayList.get(position));
            }
        });

        holder.deleteOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteOfferDialog(arrayList.get(position).getOfferId(),position);
            }
        });

        holder.hideOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               hideShowOffer(arrayList.get(position).getOfferId(),position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewOffersViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout seeItemsLayout;
        ImageView itemsFlagIv;
        FontTextView offerName;
        FontTextView offerType;
        FontTextView discountType;
        FontTextView discountValue;
        FontTextView offerStatus;
        FontTextView startTime;
        FontTextView endTime;
        FontTextView editOffer;
        FontTextView deleteOffer;
        FontTextView hideOffer;
        public ViewOffersViewHolder(View itemView) {
            super(itemView);

            seeItemsLayout =(RelativeLayout)itemView.findViewById(R.id.seeItemsLayout);
            itemsFlagIv =(ImageView)itemView.findViewById(R.id.itemsFlagIv);
            offerName =(FontTextView)itemView.findViewById(R.id.offerName);
            offerType =(FontTextView)itemView.findViewById(R.id.offerType);
            discountType =(FontTextView)itemView.findViewById(R.id.discountType);
            discountValue =(FontTextView)itemView.findViewById(R.id.discountValue);
            offerStatus =(FontTextView)itemView.findViewById(R.id.offerStatus);
            startTime =(FontTextView)itemView.findViewById(R.id.startTime);
            endTime =(FontTextView)itemView.findViewById(R.id.endTime);
            editOffer =(FontTextView)itemView.findViewById(R.id.editOffer);
            deleteOffer =(FontTextView)itemView.findViewById(R.id.deleteOffer);
            hideOffer =(FontTextView)itemView.findViewById(R.id.hideOffer);
        }
    }

    private void deleteOfferDialog(String offerId,int position){
        ConfirmationDialog mAlert = new ConfirmationDialog(context);
        mAlert.setTitle("Delete Offer");
        mAlert.setIcon(android.R.drawable.ic_dialog_alert);
        mAlert.setMessage("Are you sure you want to delete this offer? You can't undo this action.");
        mAlert.setPositveButton("Yes", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
                ((VendorViewOffersActivity)context).deleteOffer(offerId,position);
                //Do want you want
            }
        });

        mAlert.setNegativeButton("No", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAlert.dismiss();
                //Do want you want
            }
        });

        mAlert.show();
    }

    private static final String TAG = "VendorViewOffersAdapter";

    private void hideShowOffer(String offerId,int position){
        String url = Constants.BASE_URL+"vendor/hide-offer";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
            jsonObject.put("_id",offerId);
            jsonObject.put("hide",!arrayList.get(position).isOfferStatus());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomJsonRequest customJsonRequest = new CustomJsonRequest(Request.Method.POST, url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e(TAG, "onResponse: "+response.toString());
                    if(response.getBoolean("success")){
                        Toast.makeText(context,"Success",Toast.LENGTH_LONG).show();
                        if(holders[position].offerStatus.getText().toString().equalsIgnoreCase("visible")){
                            holders[position].offerStatus.setText("Hidden");
                            holders[position].hideOffer.setText("Show");
                            arrayList.get(position).setOfferStatus(true);
                        }else{
                            holders[position].offerStatus.setText("Visible");
                            holders[position].hideOffer.setText("Hide");
                            arrayList.get(position).setOfferStatus(false);

                        }
                        //notifyItemChanged(position);
                    }else{
                        Toast.makeText(context,"Please try again!",Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Please try again!",Toast.LENGTH_LONG).show();

            }
        });
        customJsonRequest.setPriority(Request.Priority.HIGH);
        helper.addToRequestQueue(customJsonRequest,"HIDE_OFFER"+offerId);
    }
}
