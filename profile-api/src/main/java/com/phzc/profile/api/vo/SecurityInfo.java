package com.phzc.profile.api.vo;

public class SecurityInfo {
    private String signatureValue;
    private String userName;
    private String userPassword;
    public String getSignatureValue() {
        return signatureValue;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public void setSignatureValue(String signatureValue) {
        this.signatureValue = signatureValue;
    }
}
