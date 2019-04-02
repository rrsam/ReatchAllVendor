package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

/**
 * Created by NaNi on 19/02/18.
 */

public class VendorCurrentsOrderAdapter extends RecyclerView.Adapter<VendorCurrentsOrderAdapter.VendorCurrentsOrderViewHolder> {

    ArrayList<String> arrayList;
    private Context context;


    public VendorCurrentsOrderAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public VendorCurrentsOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_item_current_orders, parent, false);

        return new VendorCurrentsOrderViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VendorCurrentsOrderViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VendorCurrentsOrderViewHolder extends RecyclerView.ViewHolder {
        public VendorCurrentsOrderViewHolder(View itemView) {
            super(itemView);

        }
    }
}
