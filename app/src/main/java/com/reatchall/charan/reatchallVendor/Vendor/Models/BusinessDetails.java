package com.reatchall.charan.reatchallVendor.Vendor.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class BusinessDetails implements  Parcelable{

    private String businessId;
    private String businessName;
    private String planId;
    private boolean hide;
    private boolean delete;
    private boolean suspend;
    private boolean approved;
    private boolean featured;
    private boolean home_delivery;
    private boolean paused;
    private boolean timings_flag;
    private boolean service;

    private String vendorId;

    public BusinessDetails(String businessId, String businessName, String planId, boolean hide, boolean delete, boolean suspend, boolean approved, boolean featured, boolean home_delivery, boolean paused, boolean timings_flag, boolean service, String vendorId) {
        this.businessId = businessId;
        this.businessName = businessName;
        this.planId = planId;
        this.hide = hide;
        this.delete = delete;
        this.suspend = suspend;
        this.approved = approved;
        this.featured = featured;
        this.home_delivery = home_delivery;
        this.paused = paused;
        this.timings_flag = timings_flag;
        this.service = service;
        this.vendorId = vendorId;
    }

    public BusinessDetails(String businessId, String businessName, String planId, boolean hide, boolean delete, boolean suspend, boolean approved, boolean featured, boolean home_delivery, boolean paused, boolean timings_flag, boolean service) {
        this.businessId = businessId;
        this.businessName = businessName;
        this.planId = planId;
        this.hide = hide;
        this.delete = delete;
        this.suspend = suspend;
        this.approved = approved;
        this.featured = featured;
        this.home_delivery = home_delivery;
        this.paused = paused;
        this.timings_flag = timings_flag;
        this.service = service;
    }


    protected BusinessDetails(Parcel in) {
        businessId = in.readString();
        businessName = in.readString();
        planId = in.readString();
        hide = in.readByte() != 0;
        delete = in.readByte() != 0;
        suspend = in.readByte() != 0;
        approved = in.readByte() != 0;
        featured = in.readByte() != 0;
        home_delivery = in.readByte() != 0;
        paused = in.readByte() != 0;
        timings_flag = in.readByte() != 0;
        service = in.readByte() != 0;
        vendorId = in.readString();
    }

    public static final Creator<BusinessDetails> CREATOR = new Creator<BusinessDetails>() {
        @Override
        public BusinessDetails createFromParcel(Parcel in) {
            return new BusinessDetails(in);
        }

        @Override
        public BusinessDetails[] newArray(int size) {
            return new BusinessDetails[size];
        }
    };

    public boolean isService() {
        return service;
    }

    public void setService(boolean service) {
        this.service = service;
    }

    public BusinessDetails() {
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

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getPlanId() {
        return planId;
    }

    public void setPlanId(String planId) {
        this.planId = planId;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public boolean isSuspend() {
        return suspend;
    }

    public void setSuspend(boolean suspend) {
        this.suspend = suspend;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean isFeatured() {
        return featured;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isHome_delivery() {
        return home_delivery;
    }

    public void setHome_delivery(boolean home_delivery) {
        this.home_delivery = home_delivery;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isTimings_flag() {
        return timings_flag;
    }

    public void setTimings_flag(boolean timings_flag) {
        this.timings_flag = timings_flag;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(businessId);
        parcel.writeString(businessName);
        parcel.writeString(planId);
        parcel.writeByte((byte) (hide ? 1 : 0));
        parcel.writeByte((byte) (delete ? 1 : 0));
        parcel.writeByte((byte) (suspend ? 1 : 0));
        parcel.writeByte((byte) (approved ? 1 : 0));
        parcel.writeByte((byte) (featured ? 1 : 0));
        parcel.writeByte((byte) (home_delivery ? 1 : 0));
        parcel.writeByte((byte) (paused ? 1 : 0));
        parcel.writeByte((byte) (timings_flag ? 1 : 0));
        parcel.writeByte((byte) (service ? 1 : 0));
        parcel.writeString(vendorId);
    }
}
