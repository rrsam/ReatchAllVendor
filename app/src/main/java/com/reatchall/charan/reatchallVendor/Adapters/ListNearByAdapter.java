package com.reatchall.charan.reatchallVendor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.Models.NearByBusinessModel;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.ViewBusinessActivity;

import java.util.ArrayList;

/**
 * Created by NaNi on 08/02/18.
 */

public class ListNearByAdapter extends RecyclerView.Adapter<ListNearByAdapter.ListViewHolder> {

    ArrayList<NearByBusinessModel> arrayList;
    Context context;

    public ListNearByAdapter(ArrayList<NearByBusinessModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @Override
    public ListNearByAdapter.ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_near_by_list, parent, false);

        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListNearByAdapter.ListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        public ListViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=  new Intent(context, ViewBusinessActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
