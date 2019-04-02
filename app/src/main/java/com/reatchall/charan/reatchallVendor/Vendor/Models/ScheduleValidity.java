package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class ScheduleValidity {

    private String _id;
    private String name;
    private String __v;

    public ScheduleValidity(String _id, String name, String __v) {
        this._id = _id;
        this.name = name;
        this.__v = __v;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String get__v() {
        return __v;
    }

    public void set__v(String __v) {
        this.__v = __v;
    }
}
