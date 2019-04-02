package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class BuzOfferType {
    private String offerName;
    private String offerId;
    private boolean overall;


    public BuzOfferType(String offerName, String offerId, boolean overall) {
        this.offerName = offerName;
        this.offerId = offerId;
        this.overall = overall;
    }

    public String getOfferName() {
        return offerName;
    }

    public void setOfferName(String offerName) {
        this.offerName = offerName;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    public boolean isOverall() {
        return overall;
    }

    public void setOverall(boolean overall) {
        this.overall = overall;
    }
}
