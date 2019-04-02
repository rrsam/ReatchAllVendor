package com.reatchall.charan.reatchallVendor.Vendor.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class BuzOffers implements Parcelable {
    private String offerId;
    private String businessId;
    private String offerTypeId;
    private boolean exclusive;
    private boolean flat;
    private boolean rupees;
    private int discountValue;
    private int maxDiscount;
    private int minAmount;
    private String startDate;
    private String endDate;
    private boolean hidden;

    public BuzOffers(String offerId, String businessId, String offerTypeId, boolean exclusive, boolean flat, boolean rupees,
                     int discountValue, int maxDiscount, int minDiscount, String startDate, String endDate, boolean hidden) {
        this.offerId = offerId;
        this.businessId = businessId;
        this.offerTypeId = offerTypeId;
        this.exclusive = exclusive;
        this.flat = flat;
        this.rupees = rupees;
        this.discountValue = discountValue;
        this.maxDiscount = maxDiscount;
        this.minAmount = minDiscount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hidden = hidden;
    }

    public BuzOffers() {
    }

    protected BuzOffers(Parcel in) {
        offerId = in.readString();
        businessId = in.readString();
        offerTypeId = in.readString();
        exclusive = in.readByte() != 0;
        flat = in.readByte() != 0;
        rupees = in.readByte() != 0;
        discountValue = in.readInt();
        maxDiscount = in.readInt();
        minAmount = in.readInt();
        startDate = in.readString();
        endDate = in.readString();
        hidden = in.readByte() != 0;
    }

    public static final Creator<BuzOffers> CREATOR = new Creator<BuzOffers>() {
        @Override
        public BuzOffers createFromParcel(Parcel in) {
            return new BuzOffers(in);
        }

        @Override
        public BuzOffers[] newArray(int size) {
            return new BuzOffers[size];
        }
    };

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
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

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public boolean isFlat() {
        return flat;
    }

    public void setFlat(boolean flat) {
        this.flat = flat;
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

    public int getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(int maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(int minAmount) {
        this.minAmount = minAmount;
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

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(offerId);
        parcel.writeString(businessId);
        parcel.writeString(offerTypeId);
        parcel.writeByte((byte) (exclusive ? 1 : 0));
        parcel.writeByte((byte) (flat ? 1 : 0));
        parcel.writeByte((byte) (rupees ? 1 : 0));
        parcel.writeInt(discountValue);
        parcel.writeInt(maxDiscount);
        parcel.writeInt(minAmount);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeByte((byte) (hidden ? 1 : 0));
    }
}
