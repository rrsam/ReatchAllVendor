package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class Customer {

    private String _id;
    private String gender;
    private String email;
    private String mobile;
    private String name;
    private String vendor_id;
    private String address;
    private String created_date;
    private String __v;

    public Customer(String _id, String gender, String email, String mobile, String name, String vendor_id, String address, String created_date, String __v) {
        this._id = _id;
        this.gender = gender;
        this.email = email;
        this.mobile = mobile;
        this.name = name;
        this.vendor_id = vendor_id;
        this.address = address;
        this.created_date = created_date;
        this.__v = __v;
    }

    public Customer() {

    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public void setVendor_id(String vendor_id) {
        this.vendor_id = vendor_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }
}
