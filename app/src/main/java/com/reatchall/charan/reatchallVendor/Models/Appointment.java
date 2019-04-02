package com.reatchall.charan.reatchallVendor.Models;

public class Appointment {

    private String bokingId;
    private String bookingDate;
    private String customerName;
    private String serviceName;
    private String timeDate;

    public Appointment(String bokingId, String bookingDate, String customerName, String serviceName, String timeDate) {
        this.bokingId = bokingId;
        this.bookingDate = bookingDate;
        this.customerName = customerName;
        this.serviceName = serviceName;
        this.timeDate = timeDate;
    }

    public String getBokingId() {
        return bokingId;
    }

    public void setBokingId(String bokingId) {
        this.bokingId = bokingId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getTimeDate() {
        return timeDate;
    }

    public void setTimeDate(String timeDate) {
        this.timeDate = timeDate;
    }
}
