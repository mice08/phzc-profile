package com.phzc.profile.biz.model;

import java.util.Date;

public class CertLog {
	
	private Integer chkSeqId = null;
	private String certNo;     //证件号
	private String certName;   //持件人姓名 
	private String certType;   //证件类型
	private String chkStat;
	private Date openDatetime;
	private Date updDatetime;
	
	public Integer getChkSeqId() {
		return chkSeqId;
	}
	public void setChkSeqId(Integer chkSeqId) {
		this.chkSeqId = chkSeqId;
	}
	public String getCertNo() {
		return certNo;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public String getCertName() {
		return certName;
	}
	public void setCertName(String certName) {
		this.certName = certName;
	}
	public String getCertType() {
		return certType;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}
	public String getChkStat() {
		return chkStat;
	}
	public void setChkStat(String chkStat) {
		this.chkStat = chkStat;
	}
	public Date getOpenDatetime() {
		return openDatetime;
	}
	public void setOpenDatetime(Date openDatetime) {
		this.openDatetime = openDatetime;
	}
	public Date getUpdDatetime() {
		return updDatetime;
	}
	public void setUpdDatetime(Date updDatetime) {
		this.updDatetime = updDatetime;
	}

}