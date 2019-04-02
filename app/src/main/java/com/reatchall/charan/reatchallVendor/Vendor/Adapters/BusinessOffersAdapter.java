package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
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
import com.reatchall.charan.reatchallVendor.Utils.PrefManager;
import com.reatchall.charan.reatchallVendor.Utils.ReatchAll;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BuzOffers;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IOfferEditDelete;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class BusinessOffersAdapter extends RecyclerView.Adapter<BusinessOffersAdapter.BuzOffersViewHolder> {
    private static final String TAG = "BusinessOffersAdapter";
    Context context;
    ArrayList<BuzOffers> buzOffersArrayList;
    IOfferEditDelete iOfferEditDelete;
    ReatchAll helper = ReatchAll.getInstance();
    PrefManager prefManager;
    BuzOffersViewHolder[] buzOffersViewHolders;

    public BusinessOffersAdapter(Context context, ArrayList<BuzOffers> buzOffersArrayList, IOfferEditDelete iOfferEditDelete) {
        this.context = context;
        this.buzOffersArrayList = buzOffersArrayList;
        this.iOfferEditDelete = iOfferEditDelete;
        buzOffersViewHolders = new BuzOffersViewHolder[buzOffersArrayList.size()];
    }

    @NonNull
    @Override
    public BuzOffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor_buz_offer,parent,false);
        return new BuzOffersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuzOffersViewHolder holder, int position) {
        buzOffersViewHolders[position] = holder;
        if(buzOffersArrayList.get(position).isExclusive()){
            holder.minAmountLayout.setVisibility(View.GONE);
            holder.offerName.setText("Special Offer");
        }else{
            holder.minAmountLayout.setVisibility(View.VISIBLE);
            holder.minAmount.setText(buzOffersArrayList.get(position).getMinAmount()+"");
            holder.offerName.setText("Overall purchase ");
        }

        if(buzOffersArrayList.get(position).isFlat()){
            holder.offerType.setText("Flat");
            holder.maxAmountLayout.setVisibility(View.GONE);
        }else{
            holder.offerType.setText("Upto");
            holder.maxAmountLayout.setVisibility(View.VISIBLE);
            holder.maxAmount.setText(buzOffersArrayList.get(position).getMaxDiscount()+"");
        }

        if(buzOffersArrayList.get(position).isRupees())
            holder.discountType.setText("Rupees");
        else
            holder.discountType.setText("Percentage");

        holder.discountValue.setText(buzOffersArrayList.get(position).getDiscountValue()+"");


        if(buzOffersArrayList.get(position).isHidden()){
            holder.offerStatus.setText("Visible");
            holder.hideOffer.setText("Hide");
        }else{
            holder.offerStatus.setText("Hidden");
            holder.hideOffer.setText("Show");
        }


        holder.startTime.setText(buzOffersArrayList.get(position).getStartDate().substring(0,10));
        holder.endTime.setText(buzOffersArrayList.get(position).getEndDate().substring(0,10));

        holder.editOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOfferEditDelete.editOffer(position);
            }
        });

        holder.deleteOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iOfferEditDelete.deleteOffer(position);
            }
        });

        holder.hideOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideShowOffer(buzOffersArrayList.get(position).getOfferId(),position);
            }
        });
    }

    private void hideShowOffer(String offerId,int position){
        String url = Constants.BASE_URL+"vendor/hide-offer";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
            jsonObject.put("_id",offerId);
            jsonObject.put("hide",!buzOffersArrayList.get(position).isHidden());
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
                        if(buzOffersViewHolders[position].offerStatus.getText().toString().equalsIgnoreCase("visible")){
                            buzOffersViewHolders[position].offerStatus.setText("Hidden");
                            buzOffersViewHolders[position].hideOffer.setText("Show");
                            buzOffersArrayList.get(position).setHidden(true);
                        }else{
                            buzOffersViewHolders[position].offerStatus.setText("Visible");
                            buzOffersViewHolders[position].hideOffer.setText("Hide");
                            buzOffersArrayList.get(position).setHidden(false);

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

    @Override
    public int getItemCount() {
        return buzOffersArrayList.size();
    }

    public class BuzOffersViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout seeItemsLayout;
        ImageView itemsFlagIv;
        FontTextView offerName;
        FontTextView offerType;

        FontTextView discountType;
        FontTextView discountValue;

        FontTextView minAmount;
        FontTextView maxAmount;
        LinearLayout minAmountLayout;
        LinearLayout maxAmountLayout;


        FontTextView offerStatus;
        FontTextView startTime;
        FontTextView endTime;

        FontTextView editOffer;
        FontTextView deleteOffer;
        FontTextView hideOffer;
        public BuzOffersViewHolder(View itemView) {
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
            minAmount =(FontTextView)itemView.findViewById(R.id.minAmount);
            maxAmount =(FontTextView)itemView.findViewById(R.id.maxAmount);

            minAmountLayout =(LinearLayout)itemView.findViewById(R.id.minAmountLayout);
            maxAmountLayout =(LinearLayout)itemView.findViewById(R.id.maxAmountLayout);
        }
    }
}
