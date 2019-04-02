package com.reatchall.charan.reatchallVendor.Models;

public class AddressModel {

    private String addressTitle;
    private String addressDetails;
    private boolean defaultAddress;

    public AddressModel(String addressTitle, String addressDetails, boolean defaultAddress) {
        this.addressTitle = addressTitle;
        this.addressDetails = addressDetails;
        this.defaultAddress = defaultAddress;
    }

    public AddressModel() {
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public void setAddressTitle(String addressTitle) {
        this.addressTitle = addressTitle;
    }

    public String getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(String addressDetails) {
        this.addressDetails = addressDetails;
    }

    public boolean isDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(boolean defaultAddress) {
        this.defaultAddress = defaultAddress;
    }
}

