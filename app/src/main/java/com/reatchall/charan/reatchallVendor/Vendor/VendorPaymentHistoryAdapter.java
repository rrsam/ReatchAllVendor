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

public class VendorPaymentHistoryAdapter extends RecyclerView.Adapter<VendorPaymentHistoryAdapter.VendorPaymentHistoryViewHolder> {

    private Context context;
    ArrayList<String> arrayList;


    public VendorPaymentHistoryAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public VendorPaymentHistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vendor_payment_history, parent, false);

        return new VendorPaymentHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VendorPaymentHistoryViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VendorPaymentHistoryViewHolder extends RecyclerView.ViewHolder {
        public VendorPaymentHistoryViewHolder(View itemView) {
            super(itemView);

        }
    }
}
