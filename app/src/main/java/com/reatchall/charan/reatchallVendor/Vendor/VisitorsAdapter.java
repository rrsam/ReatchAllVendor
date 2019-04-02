package com.reatchall.charan.reatchallVendor.Vendor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.Visitor;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class VisitorsAdapter extends RecyclerView.Adapter<VisitorsAdapter.VisitorsViewHolder> {

    Context context;
    ArrayList<Visitor> arrayList;
    private PhoneClickListener mListener;


    public VisitorsAdapter(Context context, ArrayList<Visitor> arrayList, PhoneClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.mListener = listener;
    }

    @Override
    public VisitorsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_views_visitor,parent,false);
        return new VisitorsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VisitorsViewHolder holder, int position) {
        holder.txtName.setText("Name : "+ arrayList.get(position).getName());
        holder.txtTime.setText("Visited Time : "+arrayList.get(position).getTime());
        holder.txtTotalVisits.setText("Total Visits : "+arrayList.get(position).getTotaVisits() );
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class VisitorsViewHolder extends RecyclerView.ViewHolder {

        private FontTextView txtName;
        private FontTextView txtTime;
        private FontTextView txtTotalVisits;
        private ImageView ivCall;


        public VisitorsViewHolder(View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.visitorName);
            txtTime = itemView.findViewById(R.id.visitedTime);
            txtTotalVisits = itemView.findViewById(R.id.totalVisits);
            ivCall = itemView.findViewById(R.id.visitorCall);

            ivCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onPhoneClicked(arrayList.get(getAdapterPosition()).getNumber());
                }
            });
        }
    }

    public interface PhoneClickListener{
        void onPhoneClicked(String phonenumber);
    }
}
