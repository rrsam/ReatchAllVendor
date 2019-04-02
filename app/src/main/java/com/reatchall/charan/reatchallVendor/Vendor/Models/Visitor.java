package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class Visitor {

    private String name;
    private String totaVisits;
    private String time;
    private String number;

    public Visitor(String name, String totaVisits, String time, String number) {
        this.name = name;
        this.totaVisits = totaVisits;
        this.time = time;
        this.number = number;
    }

    public Visitor() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotaVisits() {
        return totaVisits;
    }

    public void setTotaVisits(String totaVisits) {
        this.totaVisits = totaVisits;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
