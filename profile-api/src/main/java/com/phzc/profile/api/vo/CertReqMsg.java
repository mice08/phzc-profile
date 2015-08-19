package com.phzc.profile.api.vo;

public class CertReqMsg {
	private String busiData;
	private Header header;
	private SecurityInfo securityInfo;
	public String getBusiData() {
		return busiData;
	}
	public Header getHeader() {
		return header;
	}
	public SecurityInfo getSecurityInfo() {
		return securityInfo;
	}
	public void setBusiData(String busiData) {
		this.busiData = busiData;
	}
	public void setHeader(Header header) {
		this.header = header;
	}
	public void setSecurityInfo(SecurityInfo securityInfo) {
		this.securityInfo = securityInfo;
	}
	
	
}
