package com.phzc.profile.web.dto;

public class SmsResponseForYLDTO {

//	{"merId":"site","cmdId":"sendMessage2EL",
//		"respCode":"000","respDesc":"成功","merPriv":"",
//		"chkValue":"9a223ff193de79cea1f5934904c4b1bc",
//		"respExt":"","mobileNo":"18221932340",
//		"smsSeqId":"2015072911574826101913"}
	public SmsResponseForYLDTO() {}
	
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
	public String getRespExt() {
		return respExt;
	}
	public void setRespExt(String respExt) {
		this.respExt = respExt;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getSmsSeqId() {
		return smsSeqId;
	}
	public void setSmsSeqId(String smsSeqId) {
		this.smsSeqId = smsSeqId;
	}
	
	private String merId;
	private String cmdId;
	private String respCode;
	private String respDesc;
	private String merPriv;
	private String chkValue;
	private String respExt;
	private String mobileNo;
	private String smsSeqId;
}
