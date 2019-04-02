package com.reatchall.charan.reatchallVendor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.reatchall.charan.reatchallVendor.Models.NearByBusinessModel;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.ViewBusinessActivity;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 08/02/18.
 */

public class GridNearByAdapter extends RecyclerView.Adapter<GridNearByAdapter.GridViewHolder> {

    ArrayList<NearByBusinessModel> arrayList;
    ArrayList<String> arrayListnew;
    private Context context;

    public GridNearByAdapter(ArrayList<NearByBusinessModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    public GridNearByAdapter(ArrayList<String> arrayList, Context context,String ed) {
        this.arrayListnew = arrayList;
        this.context = context;
    }


    @Override
    public GridNearByAdapter.GridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_near_by_grid, parent, false);

        return new GridViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GridNearByAdapter.GridViewHolder holder, final int position) {
        if(arrayList != null){
            holder.title.setText(arrayList.get(position).getBusinessName());
            holder.distance.setText(arrayList.get(position).getLatitude()+" km");

            holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=  new Intent(context, ViewBusinessActivity.class);
                    intent.putExtra(context.getResources().getString(R.string.businessDetails),arrayList.get(position));
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
       if(arrayList == null){
           return arrayListnew.size();
       }
        return   arrayList.size();
    }

    public class GridViewHolder extends RecyclerView.ViewHolder {

        FontTextView title,distance;
        LinearLayout linearLayout;
        public GridViewHolder(View itemView) {
            super(itemView);

            title=(FontTextView)itemView.findViewById(R.id.title);
            distance=(FontTextView)itemView.findViewById(R.id.distance);
            linearLayout=(LinearLayout)itemView.findViewById(R.id.linearLayout);

        }
    }





}
