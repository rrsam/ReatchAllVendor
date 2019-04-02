package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Utils.SquareImageView;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IDeleteBannerImage;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorBannerImagesAdapter extends RecyclerView.Adapter<VendorBannerImagesAdapter.BannerImagesViewHolder> {


    Context context;
    ArrayList<Bitmap> arrayList;
    IDeleteBannerImage iDeleteBannerImage;

    public VendorBannerImagesAdapter(Context context, ArrayList<Bitmap> arrayList,IDeleteBannerImage iDeleteBannerImage) {
        this.context = context;
        this.arrayList = arrayList;
        this.iDeleteBannerImage=iDeleteBannerImage;
    }

    @Override
    public BannerImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor_banner_image,parent,false);
        return new BannerImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BannerImagesViewHolder holder, int position) {
        holder.bannerIV.setImageBitmap(arrayList.get(position));
        holder.bannerDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iDeleteBannerImage.deleteAndLoad(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class BannerImagesViewHolder extends RecyclerView.ViewHolder {

        SquareImageView bannerIV;
        FontTextView bannerDeleteBtn;

        public BannerImagesViewHolder(View itemView) {
            super(itemView);
            bannerIV=(SquareImageView)itemView.findViewById(R.id.bannerIV);
            bannerDeleteBtn=(FontTextView)itemView.findViewById(R.id.bannerDeleteBtn);
        }
    }
}
