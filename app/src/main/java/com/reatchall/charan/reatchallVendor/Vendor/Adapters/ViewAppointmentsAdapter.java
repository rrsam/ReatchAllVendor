package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.ServiceAppointments;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

public class ViewAppointmentsAdapter extends RecyclerView.Adapter<ViewAppointmentsAdapter.ViewAppointmentsViewHolder> {
    Context context;
    ArrayList<ServiceAppointments> serviceAppointmentsArrayList;

    public ViewAppointmentsAdapter(Context context, ArrayList<ServiceAppointments> serviceAppointmentsArrayList) {
        this.context = context;
        this.serviceAppointmentsArrayList = serviceAppointmentsArrayList;
    }

    @NonNull
    @Override
    public ViewAppointmentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor_appmnts,parent,false);
        return new ViewAppointmentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAppointmentsViewHolder holder, int position) {
        holder.userName.setText(serviceAppointmentsArrayList.get(position).getUserName());
        holder.mobileNumber.setText(serviceAppointmentsArrayList.get(position).getMobile());
        holder.gender.setText(serviceAppointmentsArrayList.get(position).isGender());
        holder.bookingTime.setText(serviceAppointmentsArrayList.get(position).getBookingDate());
    }

    @Override
    public int getItemCount() {
        return serviceAppointmentsArrayList.size();
    }

    public class ViewAppointmentsViewHolder extends RecyclerView.ViewHolder {
        FontTextView userName,mobileNumber,gender,bookingTime;
        public ViewAppointmentsViewHolder(View itemView) {
            super(itemView);
            userName =(FontTextView)itemView.findViewById(R.id.userName);
            mobileNumber =(FontTextView)itemView.findViewById(R.id.mobileNumber);
            gender =(FontTextView)itemView.findViewById(R.id.gender);
            bookingTime =(FontTextView)itemView.findViewById(R.id.bookingTime);
        }
    }
}
