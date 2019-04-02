package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorAssignLinksAdapter extends RecyclerView.Adapter<VendorAssignLinksAdapter.VendorAssignLinksViewHolder> {

    Context context;
    ArrayList<String> strings;
    ArrayList<Boolean> booleans;

    public VendorAssignLinksAdapter(Context context, ArrayList<String> strings, ArrayList<Boolean> booleans) {
        this.context = context;
        this.strings = strings;
        this.booleans = booleans;
    }

    @NonNull
    @Override
    public VendorAssignLinksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor_assign_links,parent,false);
        return new VendorAssignLinksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorAssignLinksViewHolder holder, int position) {
        if(booleans.get(position)){
            holder.unCheckedIv.setVisibility(View.GONE);
            holder.checkedIv.setVisibility(View.VISIBLE);
        }else{
            holder.unCheckedIv.setVisibility(View.VISIBLE);
            holder.checkedIv.setVisibility(View.GONE);
        }

        holder.linkName.setText(strings.get(position));

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(booleans.get(position)){
                    booleans.set(position,false);
                    notifyItemChanged(position);
                }else{
                    booleans.set(position,true);
                    notifyItemChanged(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class VendorAssignLinksViewHolder extends RecyclerView.ViewHolder {
        ImageView unCheckedIv,checkedIv;
        FontTextView linkName;
        LinearLayout parentLayout;
        public VendorAssignLinksViewHolder(View itemView) {
            super(itemView);
            unCheckedIv =(ImageView)itemView.findViewById(R.id.unCheckedIv);
            checkedIv =(ImageView)itemView.findViewById(R.id.checkedIv);
            linkName =(FontTextView)itemView.findViewById(R.id.linkName);
            parentLayout =(LinearLayout)itemView.findViewById(R.id.parentLayout);
        }
    }
}
