package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Fragments.VendorBizBannersFragment;
import com.reatchall.charan.reatchallVendor.Vendor.Models.BannerImages;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorBizBannersAdapter extends RecyclerView.Adapter<VendorBizBannersAdapter.VendorBizBannersViewHolder> {

    Context context;
    ArrayList<BannerImages> arrayList;
    VendorBizBannersFragment vendorBizBannersFragment;

    private static final String TAG = "VendorBizBannersAdapter";

    public VendorBizBannersAdapter(Context context, ArrayList<BannerImages> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public VendorBizBannersAdapter(Context context, ArrayList<BannerImages> arrayList, VendorBizBannersFragment vendorBizBannersFragment) {
        this.context = context;
        this.arrayList = arrayList;
        this.vendorBizBannersFragment = vendorBizBannersFragment;
    }

    @NonNull
    @Override
    public VendorBizBannersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner,parent,false);
        return new VendorBizBannersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorBizBannersViewHolder holder, int position) {
        if(arrayList.get(position).getBitmap()!=null)
        {
            Log.e(TAG, "onBindViewHolder: BITMAP" );
            holder.imageIV.setImageBitmap(arrayList.get(position).getBitmap());
            holder.bannerStatus.setText("To be Uploaded");

        }else{
            Log.e(TAG, "onBindViewHolder: URL" );
            try {
                Glide.with(context).load(new URL(arrayList.get(position).getImgUrl())).into(holder.imageIV);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if(arrayList.get(position).isApproved()){
                holder.bannerStatus.setText("Approved");
            }else{
                holder.bannerStatus.setText("Awaiting Approval");
            }
        }

        holder.clearImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vendorBizBannersFragment.deleteImage(position);
            }
        });




    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: "+arrayList.size());
        return arrayList.size();
    }

    public class VendorBizBannersViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIV,clearImage;
        FontTextView bannerStatus;
        public VendorBizBannersViewHolder(View itemView) {
            super(itemView);
            imageIV =(ImageView)itemView.findViewById(R.id.imageIV);
            clearImage=(ImageView)itemView.findViewById(R.id.clearImage);
            bannerStatus = (FontTextView)itemView.findViewById(R.id.bannerStatus);
        }
    }
}
