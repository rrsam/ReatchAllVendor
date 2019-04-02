package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class VendorBusinessType {
    private String businessTypeId;
    private String businessTypeName;

    public VendorBusinessType(String businessTypeId, String businessTypeName) {
        this.businessTypeId = businessTypeId;
        this.businessTypeName = businessTypeName;
    }

    public VendorBusinessType() {
    }

    public String getBusinessTypeId() {
        return businessTypeId;
    }

    public void setBusinessTypeId(String businessTypeId) {
        this.businessTypeId = businessTypeId;
    }

    public String getBusinessTypeName() {
        return businessTypeName;
    }

    public void setBusinessTypeName(String businessTypeName) {
        this.businessTypeName = businessTypeName;
    }
}
