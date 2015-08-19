package com.phzc.profile.biz.model;

public class CertInfo {
	
	private String certNo;     //证件号
	private String certName;   //持件人姓名 
	private String certType;   //证件类型

	public String getCertNo() {
		return certNo;
	}
	public String getCertName() {
		return certName;
	}
	public String getCertType() {
		return certType;
	}
	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}
	public void setCertName(String certName) {
		this.certName = certName;
	}
	public void setCertType(String certType) {
		this.certType = certType;
	}
	
}