package com.reatchall.charan.reatchallVendor.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.Interfaces.IAddressDefault;
import com.reatchall.charan.reatchallVendor.Models.AddressModel;
import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 19/02/18.
 */

public class DeliveryAddressAdapter extends RecyclerView.Adapter<DeliveryAddressAdapter.DeliveryAddressViewHolder> {

    ArrayList<AddressModel> arrayList;
    private Context context;
    IAddressDefault iAddressDefault;


    public DeliveryAddressAdapter(ArrayList<AddressModel> arrayList, Context context, IAddressDefault iAddressDefault) {
        this.arrayList = arrayList;
        this.context = context;
        this.iAddressDefault = iAddressDefault;
    }

    @Override
    public DeliveryAddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_delivery_address_new, parent, false);

        return new DeliveryAddressViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DeliveryAddressViewHolder holder, int position) {

        if(arrayList.get(position).isDefaultAddress()){
            holder.addressDefault.setBackground(context.getResources().getDrawable(R.drawable.red_rounded_bg));
            holder.addressDefault.setText("DEFAULT");
        }else{
            holder.addressDefault.setBackground(context.getResources().getDrawable(R.drawable.orange_rounded));
            holder.addressDefault.setText("SET AS DEFAULT");
        }

        holder.addressDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int fromPos =0;
                for(int i=0;i<arrayList.size();i++){
                    if(arrayList.get(i).isDefaultAddress()){
                        fromPos=i;
                    }
                }

                iAddressDefault.setDafault(position,fromPos);
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class DeliveryAddressViewHolder extends RecyclerView.ViewHolder {
        FontTextView addressDefault;
        public DeliveryAddressViewHolder(View itemView) {
            super(itemView);
            addressDefault=(FontTextView)itemView.findViewById(R.id.addressDefault);
        }
    }
}
