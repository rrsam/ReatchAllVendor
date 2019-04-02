package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class ImportDataListAdapter extends RecyclerView.Adapter<ImportDataListAdapter.ImportDataListViewHolder> {

    Context context;

    ArrayList<String> arrayList;

    public ImportDataListAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ImportDataListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_database_list_item,parent,false);

        return new ImportDataListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImportDataListViewHolder holder, int position) {

        holder.itemTitle.setText(arrayList.get(position));
        holder.itemDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.itemDownload.setVisibility(View.GONE);
                holder.itemDownloadDone.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ImportDataListViewHolder extends RecyclerView.ViewHolder {

        FontTextView itemTitle;
        ImageView itemDownload,itemDownloadDone;
        public ImportDataListViewHolder(View itemView) {
            super(itemView);
            itemTitle=(FontTextView)itemView.findViewById(R.id.itemTitle);
            itemDownload=(ImageView)itemView.findViewById(R.id.itemDownload);
            itemDownloadDone=(ImageView)itemView.findViewById(R.id.itemDownloaddone);
        }
    }
}
