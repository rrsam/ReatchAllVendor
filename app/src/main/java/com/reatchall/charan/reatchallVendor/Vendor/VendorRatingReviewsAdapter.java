package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import com.reatchall.charan.reatchallVendor.Models.UserReviews;
import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 19/02/18.
 */

public class VendorRatingReviewsAdapter extends RecyclerView.Adapter<VendorRatingReviewsAdapter.RatingReviewsViewHolder> {

    ArrayList<UserReviews> arrayList;
    private Context context;


    public VendorRatingReviewsAdapter(ArrayList<UserReviews> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public RatingReviewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_item_ratings, parent, false);

        return new RatingReviewsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RatingReviewsViewHolder holder, int position) {
        holder.txtName.setText(arrayList.get(position).getUserName());
        holder.txtReview.setText(arrayList.get(position).getReview());
        holder.mRatingBar.setRating(Float.parseFloat(arrayList.get(position).getRating()));

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class RatingReviewsViewHolder extends RecyclerView.ViewHolder {


        FontTextView txtName,txtReview;
        RatingBar mRatingBar;


        public RatingReviewsViewHolder(View itemView) {
            super(itemView);
            txtName = (FontTextView) itemView.findViewById(R.id.item_ratings_username);
            txtReview = (FontTextView) itemView.findViewById(R.id.item_rating_review);
            mRatingBar = (RatingBar) itemView.findViewById(R.id.item_rating_bar);

        }
    }
}
