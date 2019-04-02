package com.reatchall.charan.reatchallVendor.Vendor.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by NaNi on 06/03/18.
 */

public class BusinessDashboard implements Parcelable {

    private String businessName;
    private String businessId;

    public BusinessDashboard(String businessName, String businessId) {
        this.businessName = businessName;
        this.businessId = businessId;
    }

    public BusinessDashboard() {
    }

    protected BusinessDashboard(Parcel in) {
        businessName = in.readString();
        businessId = in.readString();
    }

    public static final Creator<BusinessDashboard> CREATOR = new Creator<BusinessDashboard>() {
        @Override
        public BusinessDashboard createFromParcel(Parcel in) {
            return new BusinessDashboard(in);
        }

        @Override
        public BusinessDashboard[] newArray(int size) {
            return new BusinessDashboard[size];
        }
    };

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(businessName);
        parcel.writeString(businessId);
    }
}
