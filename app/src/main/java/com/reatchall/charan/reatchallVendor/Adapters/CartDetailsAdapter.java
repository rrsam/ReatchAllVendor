package com.reatchall.charan.reatchallVendor.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 08/02/18.
 */

public class CartDetailsAdapter extends RecyclerView.Adapter<CartDetailsAdapter.ViewBusinessProductsViewHolder> {

    ArrayList<String> arrayList;
    Context context;

    public CartDetailsAdapter(Context context, ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @Override
    public CartDetailsAdapter.ViewBusinessProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_details, parent, false);

        return new ViewBusinessProductsViewHolder(itemView);
    }
    int i=0;
    @Override
    public void onBindViewHolder(final CartDetailsAdapter.ViewBusinessProductsViewHolder holder, int position) {


        holder.title.setText(arrayList.get(position));



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewBusinessProductsViewHolder extends RecyclerView.ViewHolder {

        FontTextView title,add,minus,quantity;

        public ViewBusinessProductsViewHolder(View itemView) {
            super(itemView);

            title=(FontTextView)itemView.findViewById(R.id.itemName);


        }
    }
}
