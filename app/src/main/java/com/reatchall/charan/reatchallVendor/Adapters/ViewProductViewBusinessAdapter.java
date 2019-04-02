package com.reatchall.charan.reatchallVendor.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 08/02/18.
 */

public class ViewProductViewBusinessAdapter extends RecyclerView.Adapter<ViewProductViewBusinessAdapter.ViewProductViewBusinessViewHolder> {

    ArrayList<String> arrayList;
    Context context;

    public ViewProductViewBusinessAdapter(Context context, ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        this.context = context;
    }


    @Override
    public ViewProductViewBusinessAdapter.ViewProductViewBusinessViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_product_view_more, parent, false);

        return new ViewProductViewBusinessViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final ViewProductViewBusinessAdapter.ViewProductViewBusinessViewHolder holder, int position) {


        Spannable wordtoSpan = new SpannableString("1000");
        wordtoSpan.setSpan(new StrikethroughSpan(), 0, wordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.realPrice.setText(wordtoSpan);


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewProductViewBusinessViewHolder extends RecyclerView.ViewHolder {

        FontTextView realPrice;

        public ViewProductViewBusinessViewHolder(View itemView) {
            super(itemView);

            realPrice=(FontTextView)itemView.findViewById(R.id.realPrice);



        }
    }
}
