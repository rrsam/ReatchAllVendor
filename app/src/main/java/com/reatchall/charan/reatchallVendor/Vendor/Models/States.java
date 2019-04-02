package com.reatchall.charan.reatchallVendor.Vendor.Models;

public class States {
    private String stateId;
    private String stateName;

    public States(String stateId, String stateName) {
        this.stateId = stateId;
        this.stateName = stateName;
    }

    public States() {
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }
}
