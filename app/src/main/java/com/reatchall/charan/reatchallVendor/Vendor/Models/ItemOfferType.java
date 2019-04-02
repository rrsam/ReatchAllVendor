package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class ItemOfferType {
    private String itemOfferTypeId;
    private String itemOfferType;
    private boolean exclusive;
    private boolean status;

    public ItemOfferType(String itemOfferTypeId, String itemOfferType, boolean exclusive, boolean status) {
        this.itemOfferTypeId = itemOfferTypeId;
        this.itemOfferType = itemOfferType;
        this.exclusive = exclusive;
        this.status = status;
    }

    public ItemOfferType() {
    }

    public String getItemOfferTypeId() {
        return itemOfferTypeId;
    }

    public void setItemOfferTypeId(String itemOfferTypeId) {
        this.itemOfferTypeId = itemOfferTypeId;
    }

    public String getItemOfferType() {
        return itemOfferType;
    }

    public void setItemOfferType(String itemOfferType) {
        this.itemOfferType = itemOfferType;
    }

    public boolean isExclusive() {
        return exclusive;
    }

    public void setExclusive(boolean exclusive) {
        this.exclusive = exclusive;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
