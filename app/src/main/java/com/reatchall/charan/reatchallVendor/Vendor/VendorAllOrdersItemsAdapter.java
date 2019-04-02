package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorAllOrdersItemsAdapter extends RecyclerView.Adapter<VendorAllOrdersItemsAdapter.VendorAllOrdersItemsViewHolder> {

    Context context;
    ArrayList<String> arrayList;

    public VendorAllOrdersItemsAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public VendorAllOrdersItemsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_item_all_orders_item,parent,false);
        return new VendorAllOrdersItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VendorAllOrdersItemsViewHolder holder, int position) {

        holder.itemName.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VendorAllOrdersItemsViewHolder extends RecyclerView.ViewHolder {
        FontTextView itemName;
        public VendorAllOrdersItemsViewHolder(View itemView) {
            super(itemView);
            itemName=(FontTextView)itemView.findViewById(R.id.itemName);
        }
    }
}
