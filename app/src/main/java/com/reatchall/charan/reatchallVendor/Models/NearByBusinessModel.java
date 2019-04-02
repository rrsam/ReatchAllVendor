package com.reatchall.charan.reatchallVendor.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by NaNi on 13/03/18.
 */

public class NearByBusinessModel implements Parcelable{

    private String businessId;
    private String vendorId;
    private String businessName;
    private String latitude;
    private double longitude;
    private String businessStatus;


    public NearByBusinessModel(String businessId, String vendorId, String businessName, String latitude, double longitude, String businessStatus) {
        this.businessId = businessId;
        this.vendorId = vendorId;
        this.businessName = businessName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.businessStatus = businessStatus;
    }


    protected NearByBusinessModel(Parcel in) {
        businessId = in.readString();
        vendorId = in.readString();
        businessName = in.readString();
        latitude = in.readString();
        longitude = in.readDouble();
        businessStatus = in.readString();
    }

    public static final Creator<NearByBusinessModel> CREATOR = new Creator<NearByBusinessModel>() {
        @Override
        public NearByBusinessModel createFromParcel(Parcel in) {
            return new NearByBusinessModel(in);
        }

        @Override
        public NearByBusinessModel[] newArray(int size) {
            return new NearByBusinessModel[size];
        }
    };

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }


    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getBusinessStatus() {
        return businessStatus;
    }

    public void setBusinessStatus(String businessStatus) {
        this.businessStatus = businessStatus;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(businessId);
        parcel.writeString(vendorId);
        parcel.writeString(businessName);
        parcel.writeString(latitude);
        parcel.writeDouble(longitude);
        parcel.writeString(businessStatus);
    }
}
