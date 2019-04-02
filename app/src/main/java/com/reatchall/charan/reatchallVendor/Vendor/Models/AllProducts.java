package com.reatchall.charan.reatchallVendor.Vendor.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class AllProducts implements Parcelable {

    private String productId;
    private String listId;
    private String businessId;
    private String vendorId;
    private String productName;
    private String listName;
    private String imageUrl;

    public AllProducts(String productId, String listId, String businessId, String vendorId, String productName, String listName, String imageUrl) {
        this.productId = productId;
        this.listId = listId;
        this.businessId = businessId;
        this.vendorId = vendorId;
        this.productName = productName;
        this.listName = listName;
        this.imageUrl = imageUrl;
    }

    public AllProducts() {
    }

    protected AllProducts(Parcel in) {
        productId = in.readString();
        listId = in.readString();
        businessId = in.readString();
        vendorId = in.readString();
        productName = in.readString();
        listName = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<AllProducts> CREATOR = new Creator<AllProducts>() {
        @Override
        public AllProducts createFromParcel(Parcel in) {
            return new AllProducts(in);
        }

        @Override
        public AllProducts[] newArray(int size) {
            return new AllProducts[size];
        }
    };

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productId);
        parcel.writeString(listId);
        parcel.writeString(businessId);
        parcel.writeString(vendorId);
        parcel.writeString(productName);
        parcel.writeString(listName);
        parcel.writeString(imageUrl);
    }
}

