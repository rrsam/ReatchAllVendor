package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.CustomEntryEditText;
import com.reatchall.charan.reatchallVendor.Vendor.Models.Customer;

import java.util.ArrayList;
import java.util.Currency;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorCustomerAdapter extends RecyclerView.Adapter<VendorCustomerAdapter.VendorCustomerHolder> {

    private ArrayList<Customer> mList;
    private Context mContext;

    public VendorCustomerAdapter(ArrayList<Customer> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VendorCustomerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_vendor_customer, parent, false);
        return new VendorCustomerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorCustomerHolder holder, int position) {
        holder.txtName.setText(mList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class VendorCustomerHolder extends RecyclerView.ViewHolder {

        private FontTextView txtName;
        private ImageView ivMore;

        public VendorCustomerHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.item_customer_name);
            ivMore = itemView.findViewById(R.id.item_customer_more);
            ivMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
