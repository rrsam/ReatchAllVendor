package com.reatchall.charan.reatchallVendor.Vendor.Orders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.TaxesChargesModel;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class TaxesChargesAdapter extends RecyclerView.Adapter<TaxesChargesAdapter.TaxesChargesViewHolder>{

    Context context;
    ArrayList<TaxesChargesModel> taxesChargesModels;

    public TaxesChargesAdapter(Context context, ArrayList<TaxesChargesModel> taxesChargesModels) {
        this.context = context;
        this.taxesChargesModels = taxesChargesModels;
    }

    @NonNull
    @Override
    public TaxesChargesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_taxes_charges,parent,false);
        return new TaxesChargesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaxesChargesViewHolder holder, int position) {
        holder.taxName.setText(taxesChargesModels.get(position).getTaxName());
        holder.taxCharge.setText(taxesChargesModels.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return taxesChargesModels.size();
    }

    public class TaxesChargesViewHolder extends RecyclerView.ViewHolder {
        FontTextView taxName,taxCharge;
        public TaxesChargesViewHolder(View itemView) {
            super(itemView);
            taxName=(FontTextView)itemView.findViewById(R.id.taxName);
            taxCharge=(FontTextView)itemView.findViewById(R.id.taxCharge);
        }
    }
}
