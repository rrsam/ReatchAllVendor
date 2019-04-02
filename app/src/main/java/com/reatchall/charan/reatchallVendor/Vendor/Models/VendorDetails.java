package com.reatchall.charan.reatchallVendor.Vendor.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class VendorDetails implements Parcelable {
    private String userName;
    private String email;
    private String phoneNumber;
    private String gender;
    private String dateOfBirth;

    public VendorDetails(String userName, String email, String phoneNumber, String gender, String dateOfBirth) {
        this.userName = userName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }

    public VendorDetails() {
    }

    protected VendorDetails(Parcel in) {
        userName = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        gender = in.readString();
        dateOfBirth = in.readString();
    }

    public static final Creator<VendorDetails> CREATOR = new Creator<VendorDetails>() {
        @Override
        public VendorDetails createFromParcel(Parcel in) {
            return new VendorDetails(in);
        }

        @Override
        public VendorDetails[] newArray(int size) {
            return new VendorDetails[size];
        }
    };

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeString(email);
        parcel.writeString(phoneNumber);
        parcel.writeString(gender);
        parcel.writeString(dateOfBirth);
    }
}

