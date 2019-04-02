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

public class MyWishListAdapter extends RecyclerView.Adapter<MyWishListAdapter.MyWishListViewHolder> {

    ArrayList<String> arrayList;
    private Context context;


    public MyWishListAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public MyWishListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_wishlist, parent, false);

        return new MyWishListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyWishListViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyWishListViewHolder extends RecyclerView.ViewHolder {
        public MyWishListViewHolder(View itemView) {
            super(itemView);

        }
    }
}
