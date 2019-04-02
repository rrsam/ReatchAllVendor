package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
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
import com.reatchall.charan.reatchallVendor.Utils.StringConstants;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.VendorItemOffersFragment;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ItemOffer;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ReviewItemOffer;
import com.reatchall.charan.reatchallVendor.Vendor.OfferItemsPopupFragment;
import com.reatchall.charan.reatchallVendor.Vendor.VendorViewOffersActivity;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IOfferEditDelete;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class ItemOffersAdapter extends RecyclerView.Adapter<ItemOffersAdapter.ItemOffersViewHolder> {
    private static final String TAG = "ItemOffersAdapter";

    Context context;
    ArrayList<ItemOffer> itemOfferArrayList;
    FragmentManager fragmentManager;
    IOfferEditDelete iOfferEditDelete;
    ItemOffersViewHolder[] itemOffersViewHolders;
    PrefManager prefManager;
    ReatchAll helper = ReatchAll.getInstance();

    public ItemOffersAdapter(Context context, ArrayList<ItemOffer> itemOfferArrayList, FragmentManager fragmentManager) {
        this.context = context;
        this.itemOfferArrayList = itemOfferArrayList;
        this.fragmentManager = fragmentManager;
        prefManager = new PrefManager(context);
        itemOffersViewHolders = new ItemOffersViewHolder[itemOfferArrayList.size()];
    }

    public ItemOffersAdapter(Context context, ArrayList<ItemOffer> itemOfferArrayList, FragmentManager fragmentManager, IOfferEditDelete iOfferEditDelete) {
        this.context = context;
        this.itemOfferArrayList = itemOfferArrayList;
        this.fragmentManager = fragmentManager;
        this.iOfferEditDelete = iOfferEditDelete;
        prefManager = new PrefManager(context);
        itemOffersViewHolders = new ItemOffersViewHolder[itemOfferArrayList.size()];
    }

    @NonNull
    @Override
    public ItemOffersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor_item_offers,parent,false);
        return new ItemOffersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemOffersViewHolder holder, int position) {
        itemOffersViewHolders[position] = holder;

        holder.seeItemsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(itemOfferArrayList.get(position).getItemsArrayList().size()>0){

                    ArrayList<ReviewItemOffer> reviewItemOffers = new ArrayList<>();
                    for(int i=0;i<itemOfferArrayList.get(position).getItemsArrayList().size();i++){
                                reviewItemOffers.add(new ReviewItemOffer(itemOfferArrayList.get(position).getItemsArrayList().get(i).getItemId(),
                                        itemOfferArrayList.get(position).getItemsArrayList().get(i).getItemName(), itemOfferArrayList.get(position).isRupees(),
                                        (int) itemOfferArrayList.get(position).getItemsArrayList().get(i).getPrice(),itemOfferArrayList.get(position).getDiscountValue()));

                    }

                    OfferItemsPopupFragment dialog = new OfferItemsPopupFragment();
                    Bundle args = new Bundle();
                    args.putParcelableArrayList(StringConstants.OFFER_ITEMS_ARRAY, reviewItemOffers);
                    if(itemOfferArrayList.get(position).isOfferType())
                        args.putBoolean("CLR", false);
                    else
                        args.putBoolean("CLR", true);

                    dialog.setArguments(args);
                    dialog.show(fragmentManager, "dialog");
                }
            }
        });

        if(itemOfferArrayList.get(position).getItemsArrayList().size()==0){
            holder.itemsFlagIv.setVisibility(View.INVISIBLE);
        }else{
            holder.itemsFlagIv.setVisibility(View.VISIBLE);
        }

        if(itemOfferArrayList.get(position).isOfferType()){
            holder.offerName.setText("Exclusive Offer");
            holder.offerType.setText("Exclusive Offer");
            holder.clearanceLayout.setVisibility(View.GONE);
            holder.exclusiveLayout.setVisibility(View.VISIBLE);
            if(itemOfferArrayList.get(position).isRupees())
                holder.discountType.setText("Rupees");
            else
                holder.discountType.setText("Percentage");
            holder.discountValue.setText(itemOfferArrayList.get(position).getDiscountValue()+"");
        }else{
            holder.offerName.setText("Clearance Offer");
            holder.offerType.setText("Clearance Offer");
            holder.clearanceLayout.setVisibility(View.VISIBLE);
            holder.exclusiveLayout.setVisibility(View.GONE);
            holder.buyValue.setText(itemOfferArrayList.get(position).getBuyValue()+"");
            holder.getValue.setText(itemOfferArrayList.get(position).getGetValue()+"");
        }

        if(!itemOfferArrayList.get(position).isHidden()){
            holder.offerStatus.setText("Visible");
            holder.hideOffer.setText("Hide");
        }else{
            holder.offerStatus.setText("Hidden");
            holder.hideOffer.setText("Show");
        }

        holder.startTime.setText(itemOfferArrayList.get(position).getStartDate().substring(0,10));
        holder.endTime.setText(itemOfferArrayList.get(position).getEndDate().substring(0,10));

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
                hideShowOffer(itemOfferArrayList.get(position).getOfferId(),position);
            }
        });


    }

    private void hideShowOffer(String offerId,int position){
        String url = Constants.BASE_URL+"vendor/hide-offer";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("vendor_id",prefManager.getVendorId(context));
            jsonObject.put("_id",offerId);
            jsonObject.put("hide",!itemOfferArrayList.get(position).isHidden());
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
                        if(itemOffersViewHolders[position].offerStatus.getText().toString().equalsIgnoreCase("visible")){
                            itemOffersViewHolders[position].offerStatus.setText("Hidden");
                            itemOffersViewHolders[position].hideOffer.setText("Show");
                            itemOfferArrayList.get(position).setHidden(true);
                        }else{
                            itemOffersViewHolders[position].offerStatus.setText("Visible");
                            itemOffersViewHolders[position].hideOffer.setText("Hide");
                            itemOfferArrayList.get(position).setHidden(false);

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
        return itemOfferArrayList.size();
    }

    public class ItemOffersViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout seeItemsLayout;
        ImageView itemsFlagIv;
        FontTextView offerName;
        FontTextView offerType;

        FontTextView discountType;
        FontTextView discountValue;
        LinearLayout exclusiveLayout;

        FontTextView buyValue;
        FontTextView getValue;
        LinearLayout clearanceLayout;


        FontTextView offerStatus;
        FontTextView startTime;
        FontTextView endTime;

        FontTextView editOffer;
        FontTextView deleteOffer;
        FontTextView hideOffer;
        public ItemOffersViewHolder(View itemView) {
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
            buyValue =(FontTextView)itemView.findViewById(R.id.buyValue);
            getValue =(FontTextView)itemView.findViewById(R.id.getValue);

            exclusiveLayout =(LinearLayout)itemView.findViewById(R.id.exclusiveLayout);
            clearanceLayout =(LinearLayout)itemView.findViewById(R.id.clearanceLayout);
        }
    }
}
