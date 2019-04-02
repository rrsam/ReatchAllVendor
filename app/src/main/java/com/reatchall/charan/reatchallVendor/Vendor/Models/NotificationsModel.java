package com.reatchall.charan.reatchallVendor.Vendor.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationsModel  implements Parcelable{

    private String notificationTitle;
    private String notificationMsg;
    private int notificationType;


    public NotificationsModel() {
    }



    public NotificationsModel(String notificationTitle, String notificationMsg, int notificationType) {
        this.notificationTitle = notificationTitle;
        this.notificationMsg = notificationMsg;
        this.notificationType = notificationType;
    }

    protected NotificationsModel(Parcel in) {
        notificationTitle = in.readString();
        notificationMsg = in.readString();
        notificationType = in.readInt();
    }

    public static final Creator<NotificationsModel> CREATOR = new Creator<NotificationsModel>() {
        @Override
        public NotificationsModel createFromParcel(Parcel in) {
            return new NotificationsModel(in);
        }

        @Override
        public NotificationsModel[] newArray(int size) {
            return new NotificationsModel[size];
        }
    };

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationMsg() {
        return notificationMsg;
    }

    public void setNotificationMsg(String notificationMsg) {
        this.notificationMsg = notificationMsg;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(notificationTitle);
        parcel.writeString(notificationMsg);
        parcel.writeInt(notificationType);
    }
}
