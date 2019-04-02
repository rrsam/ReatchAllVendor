package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class ImageUpload {
    private String url;
    private String key;

    public ImageUpload(String url, String key) {
        this.url = url;
        this.key = key;
    }

    public ImageUpload() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
