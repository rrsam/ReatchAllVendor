package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class TaxesChargesModel {
    private String taxName;
    private String price;

    public TaxesChargesModel() {
    }

    public TaxesChargesModel(String taxName, String price) {
        this.taxName = taxName;
        this.price = price;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
