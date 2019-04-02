package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class ItemUnits {
    private String unitId;
    private String unitName;

    public ItemUnits(String unitId, String unitName) {
        this.unitId = unitId;
        this.unitName = unitName;
    }

    public ItemUnits() {
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }
}
