package com.phzc.profile.web.dto;

import java.io.Serializable;


public class OrderCancelResult implements Serializable {
    private String respCode;
    private String respDesc;
    private String merId;
    private String cmdId;
    private String merPriv;
    private String chkValue;
    private String orderId;
    private String operId;
    private String respExt;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public String getMerId() {
        return merId;
    }

    public void setMerId(String merId) {
        this.merId = merId;
    }

    public String getCmdId() {
        return cmdId;
    }

    public void setCmdId(String cmdId) {
        this.cmdId = cmdId;
    }

    public String getMerPriv() {
        return merPriv;
    }

    public void setMerPriv(String merPriv) {
        this.merPriv = merPriv;
    }

    public String getChkValue() {
        return chkValue;
    }

    public void setChkValue(String chkValue) {
        this.chkValue = chkValue;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOperId() {
        return operId;
    }

    public void setOperId(String operId) {
        this.operId = operId;
    }

    public String getRespExt() {
        return respExt;
    }

    public void setRespExt(String respExt) {
        this.respExt = respExt;
    }
}
