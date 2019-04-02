package com.reatchall.charan.reatchallVendor.Vendor.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class NewProduct implements Parcelable {
    private String itemId;
    private String itemName;
    private String listId;
    private String vendorId;
    private String businessId;
    private String description;
    private String itemBrand;
    private String unitId;
    private int itemQuantity;
    private double price;
    private boolean single;
    private int pack;
    private String itemImgUrl;

    public NewProduct(String itemId, String itemName, String listId, String vendorId, String businessId, String description,
                      String itemBrand, String unitId, int itemQuantity, double price, boolean single, int pack, String itemImgUrl) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.listId = listId;
        this.vendorId = vendorId;
        this.businessId = businessId;
        this.description = description;
        this.itemBrand = itemBrand;
        this.unitId = unitId;
        this.itemQuantity = itemQuantity;
        this.price = price;
        this.single = single;
        this.pack = pack;
        this.itemImgUrl = itemImgUrl;
    }

    public NewProduct(String itemId, String itemName, String listId, String vendorId, String businessId, String description, String itemBrand,
                      String unitId, int itemQuantity, double price, boolean single, int pack) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.listId = listId;
        this.vendorId = vendorId;
        this.businessId = businessId;
        this.description = description;
        this.itemBrand = itemBrand;
        this.unitId = unitId;
        this.itemQuantity = itemQuantity;
        this.price = price;
        this.single = single;
        this.pack = pack;
    }

    public NewProduct() {
    }

    protected NewProduct(Parcel in) {
        itemId = in.readString();
        itemName = in.readString();
        listId = in.readString();
        vendorId = in.readString();
        businessId = in.readString();
        description = in.readString();
        itemBrand = in.readString();
        unitId = in.readString();
        itemQuantity = in.readInt();
        price = in.readDouble();
        single = in.readByte() != 0;
        pack = in.readInt();
        itemImgUrl = in.readString();
    }

    public static final Creator<NewProduct> CREATOR = new Creator<NewProduct>() {
        @Override
        public NewProduct createFromParcel(Parcel in) {
            return new NewProduct(in);
        }

        @Override
        public NewProduct[] newArray(int size) {
            return new NewProduct[size];
        }
    };

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getItemImgUrl() {
        return itemImgUrl;
    }

    public void setItemImgUrl(String itemImgUrl) {
        this.itemImgUrl = itemImgUrl;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }


    public int getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(int itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }

    public int getPack() {
        return pack;
    }

    public void setPack(int pack) {
        this.pack = pack;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemId);
        parcel.writeString(itemName);
        parcel.writeString(listId);
        parcel.writeString(vendorId);
        parcel.writeString(businessId);
        parcel.writeString(description);
        parcel.writeString(itemBrand);
        parcel.writeString(unitId);
        parcel.writeInt(itemQuantity);
        parcel.writeDouble(price);
        parcel.writeByte((byte) (single ? 1 : 0));
        parcel.writeInt(pack);
        parcel.writeString(itemImgUrl);
    }
}
