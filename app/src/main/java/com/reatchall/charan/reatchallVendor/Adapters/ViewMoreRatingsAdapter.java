package com.reatchall.charan.reatchallVendor.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

/**
 * Created by NaNi on 20/02/18.
 */

public class ViewMoreRatingsAdapter extends RecyclerView.Adapter<ViewMoreRatingsAdapter.ViewMoreRatingsViewHolder> {

    ArrayList<String> arrayList;
    private Context context;


    public ViewMoreRatingsAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public ViewMoreRatingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_more_ratings, parent, false);

        return new ViewMoreRatingsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewMoreRatingsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewMoreRatingsViewHolder extends RecyclerView.ViewHolder {
        public ViewMoreRatingsViewHolder(View itemView) {
            super(itemView);
        }
    }
}
