package com.reatchall.charan.reatchallVendor.Vendor.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reatchall.charan.reatchallVendor.Models.Appointment;
import com.reatchall.charan.reatchallVendor.R;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontButton;
import fr.arnaudguyon.smartfontslib.FontTextView;

public class VendorAppointmentAdapter extends RecyclerView.Adapter<VendorAppointmentAdapter.VendorAppointmentHolder> {

    private Context mContext;
    private ArrayList<Appointment> mList;


    public VendorAppointmentAdapter(Context mContext, ArrayList<Appointment> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @NonNull
    @Override
    public VendorAppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.vendor_item_appointment,parent,false);
        return new VendorAppointmentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorAppointmentHolder holder, int position) {
        holder.txtBookingId.setText(mList.get(position).getBokingId());
        holder.txtScheduleDate.setText(mList.get(position).getBookingDate());
        holder.txtCustomerName.setText(mList.get(position).getCustomerName());
        holder.txtServiceName.setText(mList.get(position).getServiceName());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class VendorAppointmentHolder extends RecyclerView.ViewHolder {

        FontTextView txtBookingId,txtScheduleDate,txtCustomerName,txtServiceName;
        FontButton btnViewDetails;

        public VendorAppointmentHolder(View itemView) {
            super(itemView);
            txtBookingId = itemView.findViewById(R.id.item_appoint_bookingId);
            txtScheduleDate = itemView.findViewById(R.id.item_appoint_date);
            txtCustomerName = itemView.findViewById(R.id.item_appoint_cus_name);
            txtServiceName = itemView.findViewById(R.id.item_appoint_ser_name);
            btnViewDetails = itemView.findViewById(R.id.item_appoint_view_details);

        }
    }
}
