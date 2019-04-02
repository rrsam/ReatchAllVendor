package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;
import java.util.Random;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class ImportDataParentAdapter extends RecyclerView.Adapter<ImportDataParentAdapter.ImportDataParentViewHolder> {


    Context context;
    ArrayList<String> arrayList;

    public ImportDataParentAdapter(Context context, ArrayList<String> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public ImportDataParentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_database_list,parent,false);
        return new ImportDataParentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImportDataParentViewHolder holder, int position) {

        holder.listTitle.setText(arrayList.get(position));

        Random random = new Random();

        int listNum = random.nextInt(5);
        if(listNum==0){
            listNum=listNum+2;
        }

        ArrayList<String> itemListArray = new ArrayList<>();

        for(int i=1;i<=listNum;i++){
            itemListArray.add("OrderedItem Name "+i);
        }

        holder.listTitleDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.listTitleDownload.setVisibility(View.GONE);
                holder.listTitleDownloadDone.setVisibility(View.VISIBLE);
            }
        });

        ImportDataListAdapter importDataListAdapter = new ImportDataListAdapter(context,itemListArray);
        holder.listItemRcv.setHasFixedSize(true);
        holder.listItemRcv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        holder.listItemRcv.setAdapter(importDataListAdapter);
        importDataListAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ImportDataParentViewHolder extends RecyclerView.ViewHolder {

        FontTextView listTitle;
        ImageView listTitleDownload,listTitleDownloadDone;
        RecyclerView listItemRcv;


        public ImportDataParentViewHolder(View itemView) {
            super(itemView);

            listItemRcv=(RecyclerView)itemView.findViewById(R.id.listDetailsRcv);
            listTitle=(FontTextView)itemView.findViewById(R.id.listTitle);
            listTitleDownload=(ImageView)itemView.findViewById(R.id.listTitleDownload);
            listTitleDownloadDone=(ImageView)itemView.findViewById(R.id.listTitleDownloadDone);
        }
    }
}
