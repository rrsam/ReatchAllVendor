package com.reatchall.charan.reatchallVendor.Vendor.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderedItem implements Parcelable {
    private String itemId;
    private String businessId;
    private String vendorId;
    private String itemName;
    private String itemBrand;
    private String imageUrl;
    private int price;
    private int quantity;
    private String unitId;
    private String unitName;
    private boolean single;
    private int pack;
    private String desc;
    private ItemOffer itemOffer;
    private int orderedQuantity;

    public OrderedItem(String itemId, String businessId, String vendorId, String itemName, String itemBrand, String imageUrl, int price, int quantity,
                       String unitId, String unitName, boolean single, int pack, String desc, ItemOffer itemOffer,int orderedQuantity) {
        this.itemId = itemId;
        this.businessId = businessId;
        this.vendorId = vendorId;
        this.itemName = itemName;
        this.itemBrand = itemBrand;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
        this.unitId = unitId;
        this.unitName = unitName;
        this.single = single;
        this.pack = pack;
        this.desc = desc;
        this.itemOffer = itemOffer;
        this.orderedQuantity = orderedQuantity;
    }

    public OrderedItem() {
    }


    protected OrderedItem(Parcel in) {
        itemId = in.readString();
        businessId = in.readString();
        vendorId = in.readString();
        itemName = in.readString();
        itemBrand = in.readString();
        imageUrl = in.readString();
        price = in.readInt();
        quantity = in.readInt();
        unitId = in.readString();
        unitName = in.readString();
        single = in.readByte() != 0;
        pack = in.readInt();
        desc = in.readString();
        itemOffer = in.readParcelable(ItemOffer.class.getClassLoader());
        orderedQuantity = in.readInt();
    }

    public static final Creator<OrderedItem> CREATOR = new Creator<OrderedItem>() {
        @Override
        public OrderedItem createFromParcel(Parcel in) {
            return new OrderedItem(in);
        }

        @Override
        public OrderedItem[] newArray(int size) {
            return new OrderedItem[size];
        }
    };

    public int getOrderedQuantity() {
        return orderedQuantity;
    }

    public void setOrderedQuantity(int orderedQuantity) {
        this.orderedQuantity = orderedQuantity;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
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

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ItemOffer getItemOffer() {
        return itemOffer;
    }

    public void setItemOffer(ItemOffer itemOffer) {
        this.itemOffer = itemOffer;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(itemId);
        parcel.writeString(businessId);
        parcel.writeString(vendorId);
        parcel.writeString(itemName);
        parcel.writeString(itemBrand);
        parcel.writeString(imageUrl);
        parcel.writeInt(price);
        parcel.writeInt(quantity);
        parcel.writeString(unitId);
        parcel.writeString(unitName);
        parcel.writeByte((byte) (single ? 1 : 0));
        parcel.writeInt(pack);
        parcel.writeString(desc);
        parcel.writeParcelable(itemOffer, i);
        parcel.writeInt(orderedQuantity);
    }
}
