package com.reatchall.charan.reatchallVendor.Vendor.Models;

import android.graphics.Bitmap;

public class ProductImages {
    private Bitmap bitmap;
    private String imgUrl;

    public ProductImages(Bitmap bitmap, String imgUrl) {
        this.bitmap = bitmap;
        this.imgUrl = imgUrl;
    }

    public ProductImages() {
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
