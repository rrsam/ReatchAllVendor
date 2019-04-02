package com.reatchall.charan.reatchallVendor.Models;

public class AllStatesModel {
    private String stateId;
    private String stateName;

    public AllStatesModel(String stateId, String stateName) {
        this.stateId = stateId;
        this.stateName = stateName;
    }

    public AllStatesModel() {
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
