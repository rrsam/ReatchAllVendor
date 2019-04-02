package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ProductImages;
import com.reatchall.charan.reatchallVendor.Vendor.interfaces.IDeletePromoImage;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class ProductImagesAdapter extends RecyclerView.Adapter<ProductImagesAdapter.ProductImagesViewHolder> {

    Context context;
    ArrayList<ProductImages> arrayList;
    IDeletePromoImage iDeletePromoImage;

    private static final String TAG = "ProductImagesAdapter";

    public ProductImagesAdapter(Context context, ArrayList<ProductImages> arrayList, IDeletePromoImage iDeletePromoImage) {
        this.context = context;
        this.arrayList = arrayList;
        this.iDeletePromoImage = iDeletePromoImage;
    }

    @Override
    public ProductImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor_product_image,parent,false);
        return new ProductImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductImagesViewHolder holder, int position) {

        if(arrayList.get(position).getBitmap()!=null)
        {
            Log.e(TAG, "onBindViewHolder: BITMAP" );
            holder.imageView.setImageBitmap(arrayList.get(position).getBitmap());

        }else{
            Log.e(TAG, "onBindViewHolder: URL" );
            try {
                Glide.with(context).load(new URL(arrayList.get(position).getImgUrl())).into(holder.imageView);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
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

    public class ProductImagesViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView,deleteImage;
        public ProductImagesViewHolder(View itemView) {
            super(itemView);
            imageView =(ImageView)itemView.findViewById(R.id.imageView);
            deleteImage =(ImageView)itemView.findViewById(R.id.deleteImage);
        }
    }
}
