package com.reatchall.charan.reatchallVendor.Vendor.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class OfferDetails implements Parcelable {
    private String offerId;
    private String offerName;
    private String offerType;
    private boolean discountType;
    private String discountValue;
    private String startTime;
    private String endTime;
    private ArrayList<String> itemsArrayList;
    private boolean offerStatus;
    private String minimumAmount;
    private ArrayList<String> itemIdsArrayList;

    public OfferDetails(String offerId, String offerName, String offerType, boolean discountType, String discountValue, String startTime, String endTime, ArrayList<String> itemsArrayList, boolean offerStatus, String minimumAmount, ArrayList<String> itemIdsArrayList) {
        this.offerId = offerId;
        this.offerName = offerName;
        this.offerType = offerType;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.startTime = startTime;
        this.endTime = endTime;
        this.itemsArrayList = itemsArrayList;
        this.offerStatus = offerStatus;
        this.minimumAmount = minimumAmount;
        this.itemIdsArrayList = itemIdsArrayList;
    }


    protected OfferDetails(Parcel in) {
        offerId = in.readString();
        offerName = in.readString();
        offerType = in.readString();
        discountType = in.readByte() != 0;
        discountValue = in.readString();
        startTime = in.readString();
        endTime = in.readString();
        itemsArrayList = in.createStringArrayList();
        offerStatus = in.readByte() != 0;
        minimumAmount = in.readString();
        itemIdsArrayList = in.createStringArrayList();
    }

    public static final Creator<OfferDetails> CREATOR = new Creator<OfferDetails>() {
        @Override
        public OfferDetails createFromParcel(Parcel in) {
            return new OfferDetails(in);
        }

        @Override
        public OfferDetails[] newArray(int size) {
            return new OfferDetails[size];
        }
    };

    public ArrayList<String> getItemIdsArrayList() {
        return itemIdsArrayList;
    }

    public void setItemIdsArrayList(ArrayList<String> itemIdsArrayList) {
        this.itemIdsArrayList = itemIdsArrayList;
    }

    public String getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(String minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public boolean isOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(boolean offerStatus) {
        this.offerStatus = offerStatus;
    }

    public OfferDetails() {
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferType() {
        return offerType;
    }

    public void setOfferType(String offerType) {
        this.offerType = offerType;
    }

    public boolean isDiscountType() {
        return discountType;
    }

    public void setDiscountType(boolean discountType) {
        this.discountType = discountType;
    }

    public String getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(String discountValue) {
        this.discountValue = discountValue;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public ArrayList<String> getItemsArrayList() {
        return itemsArrayList;
    }

    public void setItemsArrayList(ArrayList<String> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(offerId);
        parcel.writeString(offerName);
        parcel.writeString(offerType);
        parcel.writeByte((byte) (discountType ? 1 : 0));
        parcel.writeString(discountValue);
        parcel.writeString(startTime);
        parcel.writeString(endTime);
        parcel.writeStringList(itemsArrayList);
        parcel.writeByte((byte) (offerStatus ? 1 : 0));
        parcel.writeString(minimumAmount);
        parcel.writeStringList(itemIdsArrayList);
    }
}
