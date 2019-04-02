package com.reatchall.charan.reatchallVendor.Vendor.Models;

import android.graphics.Bitmap;

public class BannerImages {
    private Bitmap bitmap;
    private String imgUrl;
    private boolean approved;

    public BannerImages(Bitmap bitmap, String imgUrl, boolean approved) {
        this.bitmap = bitmap;
        this.imgUrl = imgUrl;
        this.approved = approved;
    }

    public BannerImages() {
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

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
