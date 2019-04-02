package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ListItemsModel;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 19/02/18.
 */

public class VendorMyProductsListAdapter extends RecyclerView.Adapter<VendorMyProductsListAdapter.VendorMyProductsListViewHolder> {

    ArrayList<ListItemsModel> arrayList;
    private Context context;


    public VendorMyProductsListAdapter(ArrayList<ListItemsModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public VendorMyProductsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_item_items_list, parent, false);
//        int height = parent.getMeasuredHeight() / 4;
//        itemView.setMinimumHeight(height);
        return new VendorMyProductsListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VendorMyProductsListViewHolder holder, int position) {

        holder.itemTitle.setText(arrayList.get(position).getItemName());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VendorMyProductsListViewHolder extends RecyclerView.ViewHolder {

        FontTextView itemTitle;
        public VendorMyProductsListViewHolder(View itemView) {
            super(itemView);
            itemTitle=(FontTextView)itemView.findViewById(R.id.title);

        }
    }
}
