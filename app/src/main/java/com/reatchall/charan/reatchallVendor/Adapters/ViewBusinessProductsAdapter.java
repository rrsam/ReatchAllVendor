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

public class ViewBusinessProductsAdapter extends RecyclerView.Adapter<ViewBusinessProductsAdapter.ViewBusinessProductsViewHolder> {

    ArrayList<String> arrayList;
    Context context;

    public ViewBusinessProductsAdapter(Context context,ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @Override
    public ViewBusinessProductsAdapter.ViewBusinessProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_products_view_business, parent, false);

        return new ViewBusinessProductsViewHolder(itemView);
    }
    int i=0;
    @Override
    public void onBindViewHolder(final ViewBusinessProductsAdapter.ViewBusinessProductsViewHolder holder, int position) {


        holder.title.setText(arrayList.get(position));
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                holder.quantity.setText(""+i);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(i>0)
                i--;
                holder.quantity.setText(""+i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewBusinessProductsViewHolder extends RecyclerView.ViewHolder {

        FontTextView title,add,minus,quantity;

        public ViewBusinessProductsViewHolder(View itemView) {
            super(itemView);

            title=(FontTextView)itemView.findViewById(R.id.item_list_title);
            add=(FontTextView)itemView.findViewById(R.id.item_list_plus);
            minus=(FontTextView)itemView.findViewById(R.id.item_list_minus);
            quantity=(FontTextView)itemView.findViewById(R.id.item_list_quantity);

        }
    }
}
