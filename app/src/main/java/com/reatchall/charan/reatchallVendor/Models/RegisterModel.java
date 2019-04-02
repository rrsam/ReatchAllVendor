package com.reatchall.charan.reatchallVendor.Models;

/**
 * Created by NaNi on 20/03/18.
 */

public class RegisterModel {

    private String name;
    private String email;
    private String mobile;
    private String password;
    private String gender;
    private String dateOfBirth;
    private boolean cusOrbus;
    private String OTP;

    public RegisterModel(String name, String email, String mobile, String password, String gender, String dateOfBirth, boolean cusOrbus,String OTP) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.password = password;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.cusOrbus = cusOrbus;
        this.OTP=OTP;
    }



    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isCusOrbus() {
        return cusOrbus;
    }

    public void setCusOrbus(boolean cusOrbus) {
        this.cusOrbus = cusOrbus;
    }
}
