package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ListItemsModel;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontEditText;

/**
 * Created by NaNi on 19/02/18.
 */

public class VendorMyProductsListNewAdapter extends RecyclerView.Adapter<VendorMyProductsListNewAdapter.VendorMyProductsListNewViewHolder> {

    ArrayList<ListItemsModel> arrayList;
    private Context context;


    public VendorMyProductsListNewAdapter(ArrayList<ListItemsModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public VendorMyProductsListNewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vendor_item_list_product, parent, false);
//        int height = parent.getMeasuredHeight() / 4;
//        itemView.setMinimumHeight(height);
        return new VendorMyProductsListNewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VendorMyProductsListNewViewHolder holder, int position) {

        holder.productName.setText(arrayList.get(position).getItemName());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VendorMyProductsListNewViewHolder extends RecyclerView.ViewHolder {

        FontEditText productName;
        ImageView editProduct,deleteProduct,saveProductName;
        RelativeLayout relativeLayout;
        public VendorMyProductsListNewViewHolder(View itemView) {
            super(itemView);
            productName=(FontEditText)itemView.findViewById(R.id.productName);
            editProduct=(ImageView)itemView.findViewById(R.id.editProduct);
            deleteProduct=(ImageView)itemView.findViewById(R.id.deleteProduct);
            saveProductName=(ImageView)itemView.findViewById(R.id.saveProductName);
            relativeLayout=(RelativeLayout)itemView.findViewById(R.id.relativeLayout);

        }
    }
}
