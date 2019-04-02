package com.reatchall.charan.reatchallVendor.Vendor.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ItemOffer implements Parcelable {
    private String offerId;
    private String vendorId;
    private String businessId;
    private String offerTypeId;
    private boolean offerType;
    private boolean rupees;
    private int discountValue;
    private int buyValue;
    private int getValue;
    private String startDate;
    private String endDate;
    private boolean deleted;
    private boolean hidden;
    private ArrayList<String> itemIds;
    private ArrayList<NewProduct> itemsArrayList;

    public ItemOffer(String offerId, String vendorId, String businessId, String offerTypeId, boolean offerType,
                     boolean rupees, int discountValue, int buyValue, int getValue, String startDate, String endDate,
                     boolean deleted, boolean hidden, ArrayList<String> itemIds, ArrayList<NewProduct> itemsArrayList) {
        this.offerId = offerId;
        this.vendorId = vendorId;
        this.businessId = businessId;
        this.offerTypeId = offerTypeId;
        this.offerType = offerType;
        this.rupees = rupees;
        this.discountValue = discountValue;
        this.buyValue = buyValue;
        this.getValue = getValue;
        this.startDate = startDate;
        this.endDate = endDate;
        this.deleted = deleted;
        this.hidden = hidden;
        this.itemIds = itemIds;
        this.itemsArrayList = itemsArrayList;
    }

    public ItemOffer() {
    }

    protected ItemOffer(Parcel in) {
        offerId = in.readString();
        vendorId = in.readString();
        businessId = in.readString();
        offerTypeId = in.readString();
        offerType = in.readByte() != 0;
        rupees = in.readByte() != 0;
        discountValue = in.readInt();
        buyValue = in.readInt();
        getValue = in.readInt();
        startDate = in.readString();
        endDate = in.readString();
        deleted = in.readByte() != 0;
        hidden = in.readByte() != 0;
        itemIds = in.createStringArrayList();
    }

    public static final Creator<ItemOffer> CREATOR = new Creator<ItemOffer>() {
        @Override
        public ItemOffer createFromParcel(Parcel in) {
            return new ItemOffer(in);
        }

        @Override
        public ItemOffer[] newArray(int size) {
            return new ItemOffer[size];
        }
    };

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
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

    public String getOfferTypeId() {
        return offerTypeId;
    }

    public void setOfferTypeId(String offerTypeId) {
        this.offerTypeId = offerTypeId;
    }

    public boolean isOfferType() {
        return offerType;
    }

    public void setOfferType(boolean offerType) {
        this.offerType = offerType;
    }

    public boolean isRupees() {
        return rupees;
    }

    public void setRupees(boolean rupees) {
        this.rupees = rupees;
    }

    public int getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(int discountValue) {
        this.discountValue = discountValue;
    }

    public int getBuyValue() {
        return buyValue;
    }

    public void setBuyValue(int buyValue) {
        this.buyValue = buyValue;
    }

    public int getGetValue() {
        return getValue;
    }

    public void setGetValue(int getValue) {
        this.getValue = getValue;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public ArrayList<String> getItemIds() {
        return itemIds;
    }

    public void setItemIds(ArrayList<String> itemIds) {
        this.itemIds = itemIds;
    }

    public ArrayList<NewProduct> getItemsArrayList() {
        return itemsArrayList;
    }

    public void setItemsArrayList(ArrayList<NewProduct> itemsArrayList) {
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(offerId);
        parcel.writeString(vendorId);
        parcel.writeString(businessId);
        parcel.writeString(offerTypeId);
        parcel.writeByte((byte) (offerType ? 1 : 0));
        parcel.writeByte((byte) (rupees ? 1 : 0));
        parcel.writeInt(discountValue);
        parcel.writeInt(buyValue);
        parcel.writeInt(getValue);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeByte((byte) (deleted ? 1 : 0));
        parcel.writeByte((byte) (hidden ? 1 : 0));
        parcel.writeStringList(itemIds);
    }
}
