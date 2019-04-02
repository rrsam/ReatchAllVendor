package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class Cities {
    private String cityId;
    private String cityStateId;
    private String cityName;
    private String cityIdCode;

    public Cities(String cityId, String cityStateId, String cityName, String cityIdCode) {
        this.cityId = cityId;
        this.cityStateId = cityStateId;
        this.cityName = cityName;
        this.cityIdCode = cityIdCode;
    }

    public Cities() {
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityState() {
        return cityStateId;
    }

    public void setCityState(String cityStateId) {
        this.cityStateId = cityStateId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityIdCode() {
        return cityIdCode;
    }

    public void setCityIdCode(String cityIdCode) {
        this.cityIdCode = cityIdCode;
    }
}
