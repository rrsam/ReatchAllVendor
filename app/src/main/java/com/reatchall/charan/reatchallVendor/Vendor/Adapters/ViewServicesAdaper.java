package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.NewProduct;
import com.reatchall.charan.reatchallVendor.Vendor.Models.VendorService;
import com.reatchall.charan.reatchallVendor.Vendor.VendorServicesActivity;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontCheckBox;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class ViewServicesAdaper extends RecyclerView.Adapter<ViewServicesAdaper.ViewServicesViewHolder> {

    Context context;
    ArrayList<VendorService> vendorServiceArrayList;
    OnStatusCheckListener mListener;
    onDeleteViewListener onDeleteViewListener;

    public ViewServicesAdaper(Context context, ArrayList<VendorService> vendorServiceArrayList,OnStatusCheckListener mListener,
                              onDeleteViewListener onDeleteViewListener) {
        this.context = context;
        this.vendorServiceArrayList = vendorServiceArrayList;
        this.mListener = mListener;
        this.onDeleteViewListener = onDeleteViewListener;
    }

    @NonNull
    @Override
    public ViewServicesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor_view_service,parent,false);
        return new ViewServicesViewHolder(view);
    }

    private static final String TAG = "ViewServicesAdaper";
    @Override
    public void onBindViewHolder(@NonNull ViewServicesViewHolder holder, int position) {
        Log.e(TAG, "onBindViewHolder: "+ vendorServiceArrayList.get(position).getImgUrl());
        if(!vendorServiceArrayList.get(position).getImgUrl().isEmpty()){
            try {
                Glide.with(context).load(new URL(vendorServiceArrayList.get(position).getImgUrl())).into(holder.serviceIv);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        holder.serviceName.setText(vendorServiceArrayList.get(position).getServiceName());
        holder.viewAppnmts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((VendorServicesActivity)context).showAppmnts(vendorServiceArrayList.get(position).getServiceId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return vendorServiceArrayList.size();
    }

    public class ViewServicesViewHolder extends RecyclerView.ViewHolder {
        ImageView serviceIv;
        FontTextView serviceName;
        FontCheckBox serviceSelect;
        RelativeLayout viewAppnmts;
        LinearLayout deleteLayout;
        public ViewServicesViewHolder(View itemView) {
            super(itemView);
            serviceIv =(ImageView)itemView.findViewById(R.id.serviceIv);
            serviceName =(FontTextView) itemView.findViewById(R.id.serviceName);
            serviceSelect =(FontCheckBox) itemView.findViewById(R.id.serviceSelect);
            viewAppnmts =(RelativeLayout) itemView.findViewById(R.id.viewAppnmts);
            deleteLayout =(LinearLayout) itemView.findViewById(R.id.removeLayout);
            serviceSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    mListener.onCheckedChangedListener(isChecked,vendorServiceArrayList.get(getAdapterPosition()));
                }
            });
            deleteLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteViewListener.onDeleteViewClicked(getAdapterPosition());
                }
            });

        }
    }

    public interface OnStatusCheckListener{
        void onCheckedChangedListener(boolean isChecked,VendorService mVendorService);
    }

    public interface onDeleteViewListener{
        void onDeleteViewClicked(int position);
    }
}
