package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class BusinessController {
    private String controllerid;
    private String controllerName;

    public BusinessController() {
    }

    public BusinessController(String controllerid, String controllerName) {
        this.controllerid = controllerid;
        this.controllerName = controllerName;
    }

    public String getControllerid() {
        return controllerid;
    }

    public void setControllerid(String controllerid) {
        this.controllerid = controllerid;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }
}
