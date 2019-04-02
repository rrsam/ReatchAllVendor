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

public class VendorOfferAdapter extends RecyclerView.Adapter<VendorOfferAdapter.VendorOfferViewHolder> {

    ArrayList<String> arrayList;
    private Context context;


    public VendorOfferAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public VendorOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_item_offers, parent, false);

        return new VendorOfferViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VendorOfferViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VendorOfferViewHolder extends RecyclerView.ViewHolder {
        public VendorOfferViewHolder(View itemView) {
            super(itemView);

        }
    }
}
