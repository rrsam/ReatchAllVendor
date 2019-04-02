package com.reatchall.charan.reatchallVendor.Models;

public class PaymentModesModel {

    private String paymentModeId;
    private String paymentModeName;

    public PaymentModesModel(String paymentModeId, String paymentModeName) {
        this.paymentModeId = paymentModeId;
        this.paymentModeName = paymentModeName;
    }

    public PaymentModesModel() {
    }

    public String getPaymentModeId() {
        return paymentModeId;
    }

    public void setPaymentModeId(String paymentModeId) {
        this.paymentModeId = paymentModeId;
    }

    public String getPaymentModeName() {
        return paymentModeName;
    }

    public void setPaymentModeName(String paymentModeName) {
        this.paymentModeName = paymentModeName;
    }
}
