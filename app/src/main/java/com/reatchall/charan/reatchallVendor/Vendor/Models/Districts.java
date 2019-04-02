package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class Districts {
    private String districtId;
    private String stateId;
    private String districtName;

    public Districts(String districtId, String stateId, String districtName) {
        this.districtId = districtId;
        this.stateId = stateId;
        this.districtName = districtName;
    }

    public Districts() {
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}
