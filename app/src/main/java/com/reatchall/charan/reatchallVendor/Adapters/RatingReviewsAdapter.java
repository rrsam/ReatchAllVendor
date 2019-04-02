package com.reatchall.charan.reatchallVendor.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

/**
 * Created by NaNi on 19/02/18.
 */

public class RatingReviewsAdapter extends RecyclerView.Adapter<RatingReviewsAdapter.RatingReviewsViewHolder> {

    ArrayList<String> arrayList;
    private Context context;


    public RatingReviewsAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public RatingReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_rating_reviews, parent, false);

        return new RatingReviewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RatingReviewsViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RatingReviewsViewHolder extends RecyclerView.ViewHolder {
        public RatingReviewsViewHolder(View itemView) {
            super(itemView);

        }
    }
}
