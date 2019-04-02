package com.reatchall.charan.reatchallVendor.Vendor.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewItemOffer implements Parcelable {
    private String itemId;
    private String itemName;
    private boolean rupees;
    private int price;
    private int discountPrice;

    public ReviewItemOffer(String itemId, String itemName, boolean rupees, int price, int discountPrice) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.rupees = rupees;
        this.price = price;
        this.discountPrice = discountPrice;
    }

    public ReviewItemOffer() {
    }

    protected ReviewItemOffer(Parcel in) {
        itemId = in.readString();
        itemName = in.readString();
        rupees = in.readByte() != 0;
        price = in.readInt();
        discountPrice = in.readInt();
    }

    public static final Creator<ReviewItemOffer> CREATOR = new Creator<ReviewItemOffer>() {
        @Override
        public ReviewItemOffer createFromParcel(Parcel in) {
            return new ReviewItemOffer(in);
        }

        @Override
        public ReviewItemOffer[] newArray(int size) {
            return new ReviewItemOffer[size];
        }
    };

    public boolean isRupees() {
        return rupees;
    }

    public void setRupees(boolean rupees) {
        this.rupees = rupees;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemId);
        parcel.writeString(itemName);
        parcel.writeByte((byte) (rupees ? 1 : 0));
        parcel.writeInt(price);
        parcel.writeInt(discountPrice);
    }
}
