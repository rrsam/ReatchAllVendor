package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IDeletePromoImage;

import java.util.ArrayList;

public class VendorPromotionsImageAdapter extends RecyclerView.Adapter<VendorPromotionsImageAdapter.VendorImagesViewHolder> {

    Context context;
    ArrayList<Bitmap> arrayList;
    IDeletePromoImage iDeletePromoImage;

    public VendorPromotionsImageAdapter(Context context, ArrayList<Bitmap> arrayList, IDeletePromoImage iDeletePromoImage) {
        this.context = context;
        this.arrayList = arrayList;
        this.iDeletePromoImage = iDeletePromoImage;
    }




    @Override
    public VendorImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_promo_images_list,parent,false);
        return new VendorImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VendorImagesViewHolder holder, int position) {
        holder.imageView.setImageBitmap(arrayList.get(position));
        holder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iDeletePromoImage.deleteImage(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VendorImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView  imageView,deleteImage;

        public VendorImagesViewHolder(View itemView) {
            super(itemView);
            imageView=(ImageView)itemView.findViewById(R.id.imageView);
            deleteImage=(ImageView)itemView.findViewById(R.id.deleteImage);
        }
    }
}
