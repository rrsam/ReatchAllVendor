package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.NewProduct;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class ViewOfferSelectedItemsAdapter extends RecyclerView.Adapter<ViewOfferSelectedItemsAdapter.ViewOfferSelectedItemsViewHolder> {

    Context context;
    ArrayList<NewProduct> arrayList;
    private static final String TAG = "ViewOfferSelectedItemsA";

    public ViewOfferSelectedItemsAdapter(Context context, ArrayList<NewProduct> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewOfferSelectedItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_offer_selected_items,parent,false);
        return new ViewOfferSelectedItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewOfferSelectedItemsViewHolder holder, int position) {
        holder.itemName.setText(arrayList.get(position).getItemName());
        if(position==(arrayList.size()-1)){
            holder.border.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: "+arrayList.size() );
        return arrayList.size();
    }

    public class ViewOfferSelectedItemsViewHolder extends RecyclerView.ViewHolder {
        FontTextView  itemName;
        View border;
        public ViewOfferSelectedItemsViewHolder(View itemView) {
            super(itemView);
            itemName = (FontTextView)itemView.findViewById(R.id.itemName);
            border = (View) itemView.findViewById(R.id.border);
        }
    }
}
