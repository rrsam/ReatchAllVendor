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

public class VendorOldOrdersAdapter extends RecyclerView.Adapter<VendorOldOrdersAdapter.VendorBalanceViewHolder> {

    ArrayList<String> arrayList;
    private Context context;


    public VendorOldOrdersAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public VendorBalanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_item_present_cash, parent, false);

        return new VendorBalanceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VendorBalanceViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VendorBalanceViewHolder extends RecyclerView.ViewHolder {
        public VendorBalanceViewHolder(View itemView) {
            super(itemView);

        }
    }
}
