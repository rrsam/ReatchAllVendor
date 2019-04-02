package com.reatchall.charan.reatchallVendor.Adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;
import java.util.Random;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 08/02/18.
 */

public class CartBusinessAdapter extends RecyclerView.Adapter<CartBusinessAdapter.CartBusinessViewHolder> {


    Context context;
    ArrayList<String> arrayList;

    public CartBusinessAdapter(Context context, ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @Override
    public CartBusinessAdapter.CartBusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart_business, parent, false);

        return new CartBusinessViewHolder(itemView);
    }

    ArrayList<String> itemList;
    @Override
    public void onBindViewHolder(final CartBusinessAdapter.CartBusinessViewHolder holder, int position) {

      itemList = new ArrayList<>();
        Random ran = new Random();
        int x = ran.nextInt(6)+1;
        for(int i=1;i<=x;i++){
            itemList.add("OrderedItem "+i);
        }
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        CartDetailsAdapter cartDetailsAdapter = new CartDetailsAdapter(context,itemList);
        holder.recyclerView.setAdapter(cartDetailsAdapter);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class CartBusinessViewHolder extends RecyclerView.ViewHolder {

        FontTextView title;
        RecyclerView recyclerView;

        public CartBusinessViewHolder(View itemView) {
            super(itemView);

            recyclerView=(RecyclerView)itemView.findViewById(R.id.cart_details_rcv);

        }
    }
}
