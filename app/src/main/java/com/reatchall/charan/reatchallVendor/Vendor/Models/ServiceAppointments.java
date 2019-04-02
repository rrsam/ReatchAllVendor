package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class ServiceAppointments {
    private String appId;
    private String serviceId;
    private String userId;
    private String mobile;
    private String userName;
    private String gender;
    private String bookingDate;

    public ServiceAppointments(String appId, String serviceId,
                               String userId, String mobile,
                               String userName, String gender,
                               String bookingDate) {
        this.appId = appId;
        this.serviceId = serviceId;
        this.userId = userId;
        this.mobile = mobile;
        this.userName = userName;
        this.gender = gender;
        this.bookingDate = bookingDate;
    }

    public ServiceAppointments() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String isGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }
}
