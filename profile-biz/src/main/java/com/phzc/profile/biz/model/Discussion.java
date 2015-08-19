package com.phzc.profile.biz.model;

import java.sql.Timestamp;

/**
 * Created by ZHUCHENGYUAN646 on 2015/7/30.
 */
public class Discussion {
    private String disSeq;
    private String disUsername;
    private String disMobilephone;
    private String disMail;
    private Integer disDealstate;
    private Integer disConnectflag;
    private String disTopic;
    private String disUrl;
    private String disContent;
    private String disReply;
    private Timestamp disCreatetime;
    private Timestamp disUpdatetime;
    private String custId;

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getDisSeq() {
        return disSeq;
    }

    public void setDisSeq(String disSeq) {
        this.disSeq = disSeq;
    }

    public String getDisUsername() {
        return disUsername;
    }

    public void setDisUsername(String disUsername) {
        this.disUsername = disUsername;
    }

    public String getDisMobilephone() {
        return disMobilephone;
    }

    public void setDisMobilephone(String disMobilephone) {
        this.disMobilephone = disMobilephone;
    }

    public String getDisMail() {
        return disMail;
    }

    public void setDisMail(String disMail) {
        this.disMail = disMail;
    }

    public Integer getDisDealstate() {
        return disDealstate;
    }

    public void setDisDealstate(Integer disDealstate) {
        this.disDealstate = disDealstate;
    }

    public Integer getDisConnectflag() {
        return disConnectflag;
    }

    public void setDisConnectflag(Integer disConnectflag) {
        this.disConnectflag = disConnectflag;
    }

    public String getDisTopic() {
        return disTopic;
    }

    public void setDisTopic(String disTopic) {
        this.disTopic = disTopic;
    }

    public String getDisUrl() {
        return disUrl;
    }

    public void setDisUrl(String disUrl) {
        this.disUrl = disUrl;
    }

    public String getDisContent() {
        return disContent;
    }

    public void setDisContent(String disContent) {
        this.disContent = disContent;
    }

    public String getDisReply() {
        return disReply;
    }

    public void setDisReply(String disReply) {
        this.disReply = disReply;
    }

    public Timestamp getDisCreatetime() {
        return disCreatetime;
    }

    public void setDisCreatetime(Timestamp disCreatetime) {
        this.disCreatetime = disCreatetime;
    }

    public Timestamp getDisUpdatetime() {
        return disUpdatetime;
    }

    public void setDisUpdatetime(Timestamp disUpdatetime) {
        this.disUpdatetime = disUpdatetime;
    }
}
