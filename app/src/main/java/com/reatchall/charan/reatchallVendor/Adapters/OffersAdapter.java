package com.reatchall.charan.reatchallVendor.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

/**
 * Created by NaNi on 08/02/18.
 */

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OffersViewHolder> {

    ArrayList<String> arrayList;
    Context context;

    public OffersAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @Override
    public OffersAdapter.OffersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offers, parent, false);

        return new OffersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(OffersAdapter.OffersViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class OffersViewHolder extends RecyclerView.ViewHolder {
        public OffersViewHolder(View itemView) {
            super(itemView);


        }
    }
}
