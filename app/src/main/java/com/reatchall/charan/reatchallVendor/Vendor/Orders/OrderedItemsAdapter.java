package com.reatchall.charan.reatchallVendor.Vendor.Orders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.OrderedItem;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ItemOffer;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class OrderedItemsAdapter extends RecyclerView.Adapter<OrderedItemsAdapter.OrderedItemsViewHolder> {

    Context context;
    ArrayList<OrderedItem> orderedItemArrayList;

    public OrderedItemsAdapter(Context context, ArrayList<OrderedItem> orderedItemArrayList) {
        this.context = context;
        this.orderedItemArrayList = orderedItemArrayList;
    }

    @NonNull
    @Override
    public OrderedItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor_ordered_items,parent,false);
        return new OrderedItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderedItemsViewHolder holder, int i) {
        try {
            Glide.with(context).load(new URL(orderedItemArrayList.get(i).getImageUrl())).into(holder.itemImageView);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        holder.itemTitle.setText(orderedItemArrayList.get(i).getItemName());
        holder.itemType.setText(orderedItemArrayList.get(i).getItemBrand());
        if(orderedItemArrayList.get(i).isSingle()){
            holder.itemQuantity.setText(orderedItemArrayList.get(i).getQuantity()+" "+ orderedItemArrayList.get(i).getUnitName());
        }else{
            holder.itemQuantity.setText(orderedItemArrayList.get(i).getQuantity()+" "+ orderedItemArrayList.get(i).getUnitName()+
                    "(Pack of "+ orderedItemArrayList.get(i).getPack()+")");
        }

        if(orderedItemArrayList.get(i).getItemOffer()!=null){
            holder.offerLayout.setVisibility(View.VISIBLE);
            holder.divider.setVisibility(View.GONE);
            ItemOffer itemOffer = orderedItemArrayList.get(i).getItemOffer();
            if(itemOffer.isOfferType()){
                if(itemOffer.isRupees()){
                    holder.discountPercentage.setVisibility(View.GONE);
                    holder.clearanceTv.setVisibility(View.GONE);
                    holder.discountPriceLayout.setVisibility(View.VISIBLE);
                    holder.discountPrice.setVisibility(View.VISIBLE);
                    holder.actualPrice.setText(orderedItemArrayList.get(i).getPrice()*orderedItemArrayList.get(i).getOrderedQuantity()+"");
                    int finalPriceValue = (int) (orderedItemArrayList.get(i).getPrice() - itemOffer.getDiscountValue());
                    holder.finalPrice.setText(finalPriceValue*orderedItemArrayList.get(i).getOrderedQuantity()+"");
                    holder.discountPrice.setText(itemOffer.getDiscountValue() +" OFF");
                }else{
                    holder.discountPriceLayout.setVisibility(View.GONE);
                    holder.clearanceTv.setVisibility(View.GONE);
                    holder.actualPrice.setText(orderedItemArrayList.get(i).getPrice()*orderedItemArrayList.get(i).getOrderedQuantity()+"");
                    int finalPriceValue = (int) (orderedItemArrayList.get(i).getPrice() - (orderedItemArrayList.get(i).getPrice()*itemOffer.getDiscountValue())/100);
                    holder.finalPrice.setText(finalPriceValue*orderedItemArrayList.get(i).getOrderedQuantity()+"");
                    holder.discountPercentage.setText(itemOffer.getDiscountValue() +" % OFF");
                }
                    if(orderedItemArrayList.get(i).getOrderedQuantity()>1){
                        holder.itemOrderQuantity.setText(orderedItemArrayList.get(i).getOrderedQuantity()+" Items");
                    }else{
                        holder.itemOrderQuantity.setText(orderedItemArrayList.get(i).getOrderedQuantity()+" Item");
                    }
            }else{
                //clearance
                holder.discountPriceLayout.setVisibility(View.GONE);
                holder.discountPercentage.setVisibility(View.GONE);
                holder.clearanceTv.setVisibility(View.VISIBLE);
                holder.priceLayout.setVisibility(View.INVISIBLE);
                holder.finalPrice.setText(orderedItemArrayList.get(i).getPrice()*orderedItemArrayList.get(i).getOrderedQuantity()+ "");
                holder.clearanceTv.setText("Buy "+itemOffer.getBuyValue()+" & Get "+itemOffer.getGetValue()+" Free");

                holder.itemOrderQuantity.setText(orderedItemArrayList.get(i).getOrderedQuantity()*(itemOffer.getBuyValue()+itemOffer.getGetValue())+" Items( "+
                        itemOffer.getBuyValue()*orderedItemArrayList.get(i).getOrderedQuantity()+" Paid , "+itemOffer.getGetValue()*orderedItemArrayList.get(i).getOrderedQuantity()+" Free )");
            }
        }else {
            holder.offerLayout.setVisibility(View.GONE);
            holder.divider.setVisibility(View.GONE);
            holder.discountPercentage.setVisibility(View.INVISIBLE);
            holder.clearanceTv.setVisibility(View.INVISIBLE);
            holder.discountPriceLayout.setVisibility(View.INVISIBLE);
            holder.actualPrice.setVisibility(View.INVISIBLE);
            holder.priceLayout.setVisibility(View.INVISIBLE);
            holder.finalPrice.setText(orderedItemArrayList.get(i).getPrice()+ "");

            if(orderedItemArrayList.get(i).getOrderedQuantity()>1){
                holder.itemOrderQuantity.setText(orderedItemArrayList.get(i).getOrderedQuantity()+" Items");
            }else{
                holder.itemOrderQuantity.setText(orderedItemArrayList.get(i).getOrderedQuantity()+" Item");
            }
        }
    }

    @Override
    public int getItemCount() {
        return orderedItemArrayList.size();
    }

    public class OrderedItemsViewHolder extends RecyclerView.ViewHolder {
        ImageView itemImageView;
        FontTextView itemTitle,itemType,itemQuantity;
        LinearLayout finalPriceLayout,discountPriceLayout;
        RelativeLayout priceLayout;
        FontTextView finalPrice,actualPrice,discountPercentage,discountPrice,clearanceTv,itemOrderQuantity;
        LinearLayout offerLayout;
        View divider;

        public OrderedItemsViewHolder(View itemView) {
            super(itemView);
            itemImageView=(ImageView)itemView.findViewById(R.id.itemImageView);
            itemTitle=(FontTextView)itemView.findViewById(R.id.itemTitle);
            itemType=(FontTextView)itemView.findViewById(R.id.itemType);
            itemQuantity=(FontTextView)itemView.findViewById(R.id.itemQuantity);
            finalPriceLayout=(LinearLayout)itemView.findViewById(R.id.finalPriceLayout);
            discountPriceLayout=(LinearLayout)itemView.findViewById(R.id.discountPriceLayout);
            priceLayout=(RelativeLayout)itemView.findViewById(R.id.priceLayout);
            finalPrice=(FontTextView)itemView.findViewById(R.id.finalPrice);
            actualPrice=(FontTextView)itemView.findViewById(R.id.actualPriceNew);
            discountPercentage=(FontTextView)itemView.findViewById(R.id.discountPercentage);
            discountPrice=(FontTextView)itemView.findViewById(R.id.discountPrice);
            clearanceTv=(FontTextView)itemView.findViewById(R.id.clearanceTv);
            itemOrderQuantity=(FontTextView)itemView.findViewById(R.id.itemOrderQuantity);
            offerLayout=(LinearLayout)itemView. findViewById(R.id.offerLayout);
            divider=(View) itemView.findViewById(R.id.divider);
        }
    }
}
