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

public class VendorDemoImagesAdapter extends RecyclerView.Adapter<VendorDemoImagesAdapter.VendorDemoImagesHolder> {

    Context context;
    ArrayList<BannerImages> arrayList;
    private OnDeleteImage onDeleteImage;
   

    private static final String TAG = "VendorDemoImagesAdapter";

    public VendorDemoImagesAdapter(Context context, ArrayList<BannerImages> arrayList,OnDeleteImage listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.onDeleteImage = listener;
    }
    

    @NonNull
    @Override
    public VendorDemoImagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner,parent,false);
        return new VendorDemoImagesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorDemoImagesHolder holder, int position) {
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


    }

    @Override
    public int getItemCount() {
        Log.e(TAG, "getItemCount: "+arrayList.size());
        return arrayList.size();
    }



    public class VendorDemoImagesHolder extends RecyclerView.ViewHolder {
        ImageView imageIV,clearImage;
        FontTextView bannerStatus;
        public VendorDemoImagesHolder(View itemView) {
            super(itemView);
            imageIV =(ImageView)itemView.findViewById(R.id.imageIV);
            clearImage=(ImageView)itemView.findViewById(R.id.clearImage);
            bannerStatus = (FontTextView)itemView.findViewById(R.id.bannerStatus);

            clearImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onDeleteImage.deleteImage(getAdapterPosition());
                }
            });
        }


    }
    public interface OnDeleteImage {
        void deleteImage(int position);
    }
}
