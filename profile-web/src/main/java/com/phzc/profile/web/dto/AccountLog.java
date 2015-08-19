package com.phzc.profile.web.dto;

public class AccountLog {
	
	private String custId;
	private String operId;
	private String name;
	private String transStat;
	private String transAmt;
	private String acctTime;
	private String acctType;
	
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getOperId() {
		return operId;
	}
	public void setOperId(String operId) {
		this.operId = operId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTransStat() {
		return transStat;
	}
	public void setTransStat(String transStat) {
		this.transStat = transStat;
	}
	public String getTransAmt() {
		return transAmt;
	}
	public void setTransAmt(String transAmt) {
		this.transAmt = transAmt;
	}
	public String getAcctTime() {
		return acctTime;
	}
	public void setAcctTime(String acctTime) {
		this.acctTime = acctTime;
	}
	public String getAcctType() {
		return acctType;
	}
	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}
}
