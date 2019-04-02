package com.reatchall.charan.reatchallVendor.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.R;

/**
 * Created by NaNi on 06/02/18.
 */

public class HomeFragmentAdapter extends RecyclerView.Adapter<HomeFragmentAdapter.HomeViewHolder> {

    private Context context;
    private Integer[] arrayList;


    public HomeFragmentAdapter(Context context, Integer[] arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public HomeFragmentAdapter.HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_rcv, parent, false);

        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeFragmentAdapter.HomeViewHolder holder, int position) {

        holder.list_image.setImageResource(arrayList[position]);


    }

    @Override
    public int getItemCount() {
        return arrayList.length;
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder {

        ImageView list_image;

        public HomeViewHolder(View itemView) {
            super(itemView);

            list_image=(ImageView)itemView.findViewById(R.id.list_image);
        }
    }
}
