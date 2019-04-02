package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ReviewItemOffer;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class OfferItemsReviewAdapter extends RecyclerView.Adapter<OfferItemsReviewAdapter.OfferItemsReviewViewHolder> {
    Context context;
    ArrayList<ReviewItemOffer> reviewItemOffers;
    boolean clearance;
    boolean confirm = true;

    public OfferItemsReviewAdapter(Context context, ArrayList<ReviewItemOffer> reviewItemOffers, boolean clearance) {
        this.context = context;
        this.reviewItemOffers = reviewItemOffers;
        this.clearance = clearance;
    }

    @NonNull
    @Override
    public OfferItemsReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review_offer_items,parent,false);
        return new OfferItemsReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OfferItemsReviewViewHolder holder, int position) {

        if(clearance){
            holder.itemName.setText(reviewItemOffers.get(position).getItemName());
            holder.itemPrice.setVisibility(View.GONE);
            holder.finalPrice.setVisibility(View.GONE);
        }else{
            if(reviewItemOffers.get(position).isRupees()){
                holder.itemName.setText(reviewItemOffers.get(position).getItemName());
                holder.itemPrice.setText(reviewItemOffers.get(position).getPrice()+"");
                holder.finalPrice.setText((reviewItemOffers.get(position).getPrice()-reviewItemOffers.get(position).getDiscountPrice())+"");

                if(reviewItemOffers.get(position).getPrice()-reviewItemOffers.get(position).getDiscountPrice()<=0){
                    holder.itemPrice.setTextColor(context.getResources().getColor(R.color.red_new));
                    holder.itemName.setTextColor(context.getResources().getColor(R.color.red_new));
                    holder.finalPrice.setTextColor(context.getResources().getColor(R.color.red_new));
                    confirm = false;
                }
            }else{
                holder.itemName.setText(reviewItemOffers.get(position).getItemName());
                holder.itemPrice.setText(reviewItemOffers.get(position).getPrice()+"");
                holder.finalPrice.setText((reviewItemOffers.get(position).getPrice()-(reviewItemOffers.get(position).getPrice()*reviewItemOffers.get(position).getDiscountPrice()/100))+"");
                confirm = true;
            }
        }



    }

    @Override
    public int getItemCount() {
        return reviewItemOffers.size();
    }

    public class OfferItemsReviewViewHolder extends RecyclerView.ViewHolder {
        FontTextView itemName,itemPrice,finalPrice;
        public OfferItemsReviewViewHolder(View itemView) {
            super(itemView);
            itemName =(FontTextView)itemView.findViewById(R.id.itemName);
            itemPrice =(FontTextView)itemView.findViewById(R.id.itemPrice);
            finalPrice =(FontTextView)itemView.findViewById(R.id.finalPrice);
        }
    }
}
