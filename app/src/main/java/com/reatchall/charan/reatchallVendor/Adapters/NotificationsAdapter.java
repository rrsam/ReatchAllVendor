package com.reatchall.charan.reatchallVendor.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reatchall.charan.reatchallVendor.R;
import com.reatchall.charan.reatchallVendor.Vendor.Models.NotificationsModel;

import java.util.ArrayList;

import fr.arnaudguyon.smartfontslib.FontTextView;

/**
 * Created by NaNi on 19/02/18.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder> {

    ArrayList<NotificationsModel> arrayList;
    private Context context;


    public NotificationsAdapter(ArrayList<NotificationsModel> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notifications, parent, false);

        return new NotificationsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationsViewHolder holder, int position) {

        holder.notificationMsg.setText(arrayList.get(position).getNotificationMsg());

        switch (arrayList.get(position).getNotificationType()){
            case 0: holder.notificationLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_new_order));
                    break;
            case 1: holder.notificationLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_order_cancelled));
                break;
            case 2: holder.notificationLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_order_delivered));
                break;
            case 3: holder.notificationLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_in_transit));
                break;

                default:holder.notificationLogo.setImageDrawable(context.getResources().getDrawable(R.drawable.icon_new_order));
                    break;

        }

        holder.notificationTitle.setText(arrayList.get(position).getNotificationTitle());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class NotificationsViewHolder extends RecyclerView.ViewHolder {

        FontTextView notificationMsg,notificationTitle;
        ImageView notificationLogo;

        public NotificationsViewHolder(View itemView) {
            super(itemView);

            notificationMsg=(FontTextView)itemView.findViewById(R.id.notification_msg);
            notificationTitle=(FontTextView)itemView.findViewById(R.id.notification_title);

            notificationLogo=(ImageView)itemView.findViewById(R.id.notificationLogo);

        }
    }
}
