package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;
import java.util.Random;

public class VendorAllOrdersParentAdapter extends RecyclerView.Adapter<VendorAllOrdersParentAdapter.VendorAllOrdersViewHolder> {

    Context context;
    ArrayList<String> arrayList;

    public VendorAllOrdersParentAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public VendorAllOrdersParentAdapter.VendorAllOrdersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_item_all_orders,parent,false);
        return new VendorAllOrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VendorAllOrdersParentAdapter.VendorAllOrdersViewHolder holder, int position) {

        Random random = new Random();

        int listNum = random.nextInt(5);
        if(listNum==0){
            listNum=listNum+2;
        }

        ArrayList<String> itemListArray = new ArrayList<>();

        for(int i=1;i<=listNum;i++){
            itemListArray.add("OrderedItem Name "+i);
        }

        VendorAllOrdersItemsAdapter vendorAllOrdersItemsAdapter = new VendorAllOrdersItemsAdapter(context,itemListArray);
        holder.itemsRcv.setHasFixedSize(true);
        holder.itemsRcv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.itemsRcv.setAdapter(vendorAllOrdersItemsAdapter);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VendorAllOrdersViewHolder extends RecyclerView.ViewHolder {
        RecyclerView itemsRcv;
        public VendorAllOrdersViewHolder(View itemView) {
            super(itemView);
            itemsRcv=(RecyclerView)itemView.findViewById(R.id.orderItemList);
        }
    }
}
