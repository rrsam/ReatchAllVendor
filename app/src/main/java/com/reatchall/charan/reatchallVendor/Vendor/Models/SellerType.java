package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class SellerType {
    private String sellerId;
    private String sellerName;
    private String businessTypeId;

    public SellerType(String sellerId, String sellerName, String businessTypeId) {
        this.sellerId = sellerId;
        this.sellerName = sellerName;
        this.businessTypeId = businessTypeId;
    }

    public SellerType() {
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(String businessTypeId) {
        this.businessTypeId = businessTypeId;
    }
}
