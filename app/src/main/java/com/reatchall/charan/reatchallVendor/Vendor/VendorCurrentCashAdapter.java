package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

public class VendorCurrentCashAdapter extends RecyclerView.Adapter<VendorCurrentCashAdapter.CurrentCashViewHolder>{

    Context context;
    ArrayList<String> arrayList;

    public VendorCurrentCashAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public VendorCurrentCashAdapter.CurrentCashViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor_current_cash,parent,false);
        return new CurrentCashViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VendorCurrentCashAdapter.CurrentCashViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CurrentCashViewHolder extends RecyclerView.ViewHolder {
        public CurrentCashViewHolder(View itemView) {
            super(itemView);
        }
    }
}
