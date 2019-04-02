package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class BusinessSellerType {
    private String id;
    private String sellerTypeName;
    private String businessType;

    public BusinessSellerType(String id, String sellerTypeName, String businessType) {
        this.id = id;
        this.sellerTypeName = sellerTypeName;
        this.businessType = businessType;
    }

    public BusinessSellerType() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSellerTypeName() {
        return sellerTypeName;
    }

    public void setSellerTypeName(String sellerTypeName) {
        this.sellerTypeName = sellerTypeName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }
}
