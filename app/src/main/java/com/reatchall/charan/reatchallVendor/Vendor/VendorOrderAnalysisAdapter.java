package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 19/02/18.
 */

public class VendorOrderAnalysisAdapter extends RecyclerView.Adapter<VendorOrderAnalysisAdapter.VendorBalanceViewHolder> {

    ArrayList<String> arrayList;
    private Context context;
    private boolean hide;


    public VendorOrderAnalysisAdapter(ArrayList<String> arrayList, Context context,boolean hide) {
        this.arrayList = arrayList;
        this.context = context;
        this.hide=hide;
    }

    @Override
    public VendorBalanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_item_present_cash, parent, false);

        return new VendorBalanceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VendorBalanceViewHolder holder, int position) {

        if(hide){
            holder.priceValue.setVisibility(View.GONE);
            holder.priceSymbol.setVisibility(View.GONE);
        }else{
            holder.priceValue.setVisibility(View.VISIBLE);
            holder.priceSymbol.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VendorBalanceViewHolder extends RecyclerView.ViewHolder {

        FontTextView priceSymbol,priceValue;
        public VendorBalanceViewHolder(View itemView) {
            super(itemView);

            priceSymbol=(FontTextView)itemView.findViewById(R.id.order_price_symbol);
            priceValue=(FontTextView)itemView.findViewById(R.id.order_price);

        }
    }
}
