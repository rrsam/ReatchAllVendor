package com.reatchall.charan.reatchallVendor.Vendor.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class VendorService implements Parcelable {
    private String serviceId;
    private String businessId;
    private String serviceName;
    private String imgUrl;
    private String imgKey;

    public VendorService(String serviceId, String businessId, String serviceName, String imgUrl, String imgKey) {
        this.serviceId = serviceId;
        this.businessId = businessId;
        this.serviceName = serviceName;
        this.imgUrl = imgUrl;
        this.imgKey = imgKey;
    }

    public VendorService() {
    }

    protected VendorService(Parcel in) {
        serviceId = in.readString();
        businessId = in.readString();
        serviceName = in.readString();
        imgUrl = in.readString();
        imgKey = in.readString();
    }

    public static final Creator<VendorService> CREATOR = new Creator<VendorService>() {
        @Override
        public VendorService createFromParcel(Parcel in) {
            return new VendorService(in);
        }

        @Override
        public VendorService[] newArray(int size) {
            return new VendorService[size];
        }
    };

    public String getImgKey() {
        return imgKey;
    }

    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(serviceId);
        parcel.writeString(businessId);
        parcel.writeString(serviceName);
        parcel.writeString(imgUrl);
        parcel.writeString(imgKey);
    }
}
