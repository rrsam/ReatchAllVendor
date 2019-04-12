package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.NewProduct;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.ILoadProducts;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontCheckBox;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorAllProductsAdapter extends RecyclerView.Adapter<VendorAllProductsAdapter.VendorAllProductsViewHolder>{

    Context context;
    ArrayList<NewProduct> allProductsArrayList;
    ILoadProducts iLoadProductsList;

    public VendorAllProductsAdapter(Context context, ArrayList<NewProduct> allProductsArrayList, ILoadProducts iLoadProductsList) {
        this.context = context;
        this.allProductsArrayList = allProductsArrayList;
        this.iLoadProductsList = iLoadProductsList;
    }

    @NonNull
    @Override
    public VendorAllProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_vendor_products,parent,false);
        return new VendorAllProductsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorAllProductsViewHolder holder, int position) {
        holder.productName.setText(allProductsArrayList.get(position).getItemName());
        if(allProductsArrayList.get(position).getItemBrand().isEmpty() || allProductsArrayList.get(position).getItemBrand()==null)
            holder.listName.setText("Miscellaneous");
        else
            holder.listName.setText(allProductsArrayList.get(position).getItemBrand());


        if(!allProductsArrayList.get(position).getItemImgUrl().isEmpty())
            Glide.with(context).load(allProductsArrayList.get(position).getItemImgUrl()).into(holder.productImage);


        holder.editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iLoadProductsList.saveAndLoad("",allProductsArrayList.get(position));
            }
        });

        holder.removeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iLoadProductsList.deleteAndLoad(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return allProductsArrayList.size();
    }

    public class VendorAllProductsViewHolder extends RecyclerView.ViewHolder {
        LinearLayout editLayout;
        LinearLayout removeLayout;
        ImageView productImage;
        FontTextView productName;
        FontTextView listName;

        public VendorAllProductsViewHolder(View itemView) {
            super(itemView);
            editLayout=(LinearLayout)itemView.findViewById(R.id.editLayout);
            removeLayout=(LinearLayout)itemView.findViewById(R.id.removeLayout);
            productImage=(ImageView)itemView.findViewById(R.id.productImage);
            productName=(FontTextView)itemView.findViewById(R.id.productName);
            listName=(FontTextView)itemView.findViewById(R.id.listName);
        }



    }

}
