package com.reatchall.charan.reatchallVendor.Vendor.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by NaNi on 08/03/18.
 */

public class ListDetailsModel implements Parcelable {

    private String listID;
    private String listName;
    private String vendorId;
    private String businessId;
    private String imgUrl;
    private String imgKey;
    private boolean approved;
    private  boolean list;


    public ListDetailsModel(String listID, String listName, String vendorId, String businessId,
                            String imgUrl, String imgKey, boolean approved, boolean list) {
        this.listID = listID;
        this.listName = listName;
        this.vendorId = vendorId;
        this.businessId = businessId;
        this.imgUrl = imgUrl;
        this.imgKey = imgKey;
        this.approved = approved;
        this.list = list;
    }

    public ListDetailsModel() {
    }

    protected ListDetailsModel(Parcel in) {
        listID = in.readString();
        listName = in.readString();
        vendorId = in.readString();
        businessId = in.readString();
        imgUrl = in.readString();
        imgKey = in.readString();
        approved = in.readByte() != 0;
        list = in.readByte() != 0;
    }

    public static final Creator<ListDetailsModel> CREATOR = new Creator<ListDetailsModel>() {
        @Override
        public ListDetailsModel createFromParcel(Parcel in) {
            return new ListDetailsModel(in);
        }

        @Override
        public ListDetailsModel[] newArray(int size) {
            return new ListDetailsModel[size];
        }
    };

    public String getListID() {
        return listID;
    }

    public void setListID(String listID) {
        this.listID = listID;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isList() {
        return list;
    }

    public void setList(boolean list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(listID);
        parcel.writeString(listName);
        parcel.writeString(vendorId);
        parcel.writeString(businessId);
        parcel.writeString(imgUrl);
        parcel.writeString(imgKey);
        parcel.writeByte((byte) (approved ? 1 : 0));
        parcel.writeByte((byte) (list ? 1 : 0));
    }
}
