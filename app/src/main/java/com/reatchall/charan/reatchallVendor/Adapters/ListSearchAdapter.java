package com.reatchall.charan.reatchallVendor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.ViewBusinessActivity;
import com.reatchall.charan.reatchallVendor.ViewProductDetailsActivity;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 08/02/18.
 */

public class ListSearchAdapter extends RecyclerView.Adapter<ListSearchAdapter.ListSearchViewHolder> {

    ArrayList<String> arrayList;
    Context context;

    public ListSearchAdapter(ArrayList<String> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @Override
    public ListSearchAdapter.ListSearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_fragment_list, parent, false);

        return new ListSearchViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListSearchAdapter.ListSearchViewHolder holder, int position) {
        Spannable wordtoSpan = new SpannableString("1000");
        wordtoSpan.setSpan(new StrikethroughSpan(), 0, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.realPrice.setText(wordtoSpan);
        holder.viewVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=  new Intent(context, ViewBusinessActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ListSearchViewHolder extends RecyclerView.ViewHolder {

        FontTextView realPrice,viewVendor;
        public ListSearchViewHolder(View itemView) {
            super(itemView);
            realPrice=(FontTextView)itemView.findViewById(R.id.realPrice);
            viewVendor=(FontTextView)itemView.findViewById(R.id.viewVendor);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=  new Intent(context, ViewProductDetailsActivity.class);
                    context.startActivity(intent);
                }
            });
        }
    }
}
