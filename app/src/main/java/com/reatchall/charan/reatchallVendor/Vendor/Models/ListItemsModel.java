package com.reatchall.charan.reatchallVendor.Vendor.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by NaNi on 08/03/18.
 */

public class ListItemsModel implements Parcelable{

    private  String imgUrl;
    private  String itemName;

    public ListItemsModel(String imgUrl, String itemName) {
        this.imgUrl = imgUrl;
        this.itemName = itemName;
    }


    protected ListItemsModel(Parcel in) {
        imgUrl = in.readString();
        itemName = in.readString();
    }

    public static final Creator<ListItemsModel> CREATOR = new Creator<ListItemsModel>() {
        @Override
        public ListItemsModel createFromParcel(Parcel in) {
            return new ListItemsModel(in);
        }

        @Override
        public ListItemsModel[] newArray(int size) {
            return new ListItemsModel[size];
        }
    };

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(imgUrl);
        parcel.writeString(itemName);
    }
}
