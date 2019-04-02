package com.reatchall.charan.reatchallVendor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.ViewProductsTypesActivity;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 08/02/18.
 */

public class ViewBusinessProductsAdapterNew extends RecyclerView.Adapter<ViewBusinessProductsAdapterNew.ViewBusinessProductsNewViewHolder> {

    ArrayList<String> arrayList;
    Context context;

    public ViewBusinessProductsAdapterNew(Context context, ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @Override
    public ViewBusinessProductsAdapterNew.ViewBusinessProductsNewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_products_view_business_new, parent, false);

        return new ViewBusinessProductsNewViewHolder(itemView);
    }
    int i=0;
    @Override
    public void onBindViewHolder(final ViewBusinessProductsAdapterNew.ViewBusinessProductsNewViewHolder holder, int position) {




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewBusinessProductsNewViewHolder extends RecyclerView.ViewHolder {

        FontTextView title,add,minus,quantity;

        public ViewBusinessProductsNewViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, ViewProductsTypesActivity.class));
                }
            });

        }
    }
}
