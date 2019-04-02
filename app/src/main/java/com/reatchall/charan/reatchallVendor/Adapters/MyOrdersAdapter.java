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
 * Created by NaNi on 19/02/18.
 */

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyOrdersViewHolder> {

    ArrayList<String> arrayList;
    private Context context;
    private boolean  CurrentOrOld;


    public MyOrdersAdapter(ArrayList<String> arrayList, Context context, boolean currentOrOld) {
        this.arrayList = arrayList;
        this.context = context;
        CurrentOrOld = currentOrOld;
    }

    @Override
    public MyOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_current_old_orders, parent, false);

        return new MyOrdersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyOrdersViewHolder holder, int position) {

        if(CurrentOrOld)
        holder.orderTracker.setVisibility(View.GONE);
        else{
            holder.orderTracker.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyOrdersViewHolder extends RecyclerView.ViewHolder {
        FontTextView orderTracker;
        public MyOrdersViewHolder(View itemView) {
            super(itemView);
            orderTracker=(FontTextView)itemView.findViewById(R.id.order_tracking);
        }
    }
}
