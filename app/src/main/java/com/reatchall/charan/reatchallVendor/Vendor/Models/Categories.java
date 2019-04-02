package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class Categories {
    private String categoryId;
    private String categoryName;
    private String businessType;
    private String imageUrl;
    private String key;

    public Categories() {
    }

    public Categories(String categoryId, String categoryName, String businessType, String imageUrl, String key) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.businessType = businessType;
        this.imageUrl = imageUrl;
        this.key = key;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
