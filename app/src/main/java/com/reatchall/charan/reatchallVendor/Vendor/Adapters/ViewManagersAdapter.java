package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class ViewManagersAdapter extends RecyclerView.Adapter<ViewManagersAdapter.ViewManagersViewHolder> {

    Context context;
    ArrayList<String> arrayList;

    public ViewManagersAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewManagersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_offer_selected_items,parent,false);
        return  new ViewManagersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewManagersViewHolder holder, int position) {
        holder.itemName.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewManagersViewHolder extends RecyclerView.ViewHolder {
        FontTextView itemName;
        public ViewManagersViewHolder(View itemView) {
            super(itemView);
            itemName = (FontTextView)itemView.findViewById(R.id.itemName);
        }
    }
}
