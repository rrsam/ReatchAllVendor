package com.reatchall.charan.reatchallVendor.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;
import java.util.Random;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 08/02/18.
 */

public class ViewBusinessCategoriesAdapter extends RecyclerView.Adapter<ViewBusinessCategoriesAdapter.ViewBusinessCategoriesViewHolder> {

    ArrayList<String> arrayList;
    Context context;

    public ViewBusinessCategoriesAdapter(Context context, ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @Override
    public ViewBusinessCategoriesAdapter.ViewBusinessCategoriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_list, parent, false);

        return new ViewBusinessCategoriesViewHolder(itemView);
    }

    ArrayList<String> itemList;
    @Override
    public void onBindViewHolder(final ViewBusinessCategoriesAdapter.ViewBusinessCategoriesViewHolder holder, int position) {

      itemList = new ArrayList<>();
        holder.title.setText(arrayList.get(position));
        holder.recyclerView.setVisibility(View.GONE);
        Random ran = new Random();
        int x = ran.nextInt(6)+1;
        for(int i=1;i<=x;i++){
            itemList.add("OrderedItem "+i);
        }
        holder.dropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.recyclerView.getVisibility()==View.GONE){
                    holder.recyclerView.setVisibility(View.VISIBLE);
                    holder.recyclerView.setHasFixedSize(true);
                    holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));



                    ViewBusinessProductsAdapter viewBusinessProductsAdapter = new ViewBusinessProductsAdapter(context,itemList);

                    holder.recyclerView.setAdapter(viewBusinessProductsAdapter);
                }else{
                    holder.recyclerView.setVisibility(View.GONE);
                }


            }
        });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewBusinessCategoriesViewHolder extends RecyclerView.ViewHolder {

        FontTextView title;
        RecyclerView recyclerView;
        ImageView dropDown;

        public ViewBusinessCategoriesViewHolder(View itemView) {
            super(itemView);

            title=(FontTextView)itemView.findViewById(R.id.category_item);
            recyclerView=(RecyclerView)itemView.findViewById(R.id.products_rcv);
            dropDown=(ImageView)itemView.findViewById(R.id.expand);

        }
    }
}
