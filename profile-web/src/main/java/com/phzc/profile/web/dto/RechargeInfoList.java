package com.phzc.profile.web.dto;


public class RechargeInfoList {

//	{"merId":"site","cmdId":"queryRechargeLog","respCode":"000","respDesc":"根据custId查询充值记录信息成功！",
//	"merPriv":"","chkValue":"bba7ffc22a4e8532a1a96aa437754baf","respExt":"",
//	"custId":"20150624100336","totalRecord":"7","totalPage":"1",
//	"currentPage":"1","rechargeInfoList":[{"custId":"20150624100336",
//	"operId":"111","acctDate":"20150724","sysSeqId":"183825065112","payChnl":"13",
//	"transStat":"P","transAmt":"1000.00","feeAmt":null},
	
	
	private String custId;
	private String operId;
	private String acctDate;
	private String sysSeqId;
	private String payChnl;
	private String transStat;
	private String transAmt;
	private String feeAmt;
	private String acctTime;
	
	
	
	public String getAcctTime() {
		return acctTime;
	}

	public void setAcctTime(String acctTime) {
		this.acctTime = acctTime;
	}

	public RechargeInfoList() {}

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

	public String getAcctDate() {
		return acctDate;
	}

	public void setAcctDate(String acctDate) {
		this.acctDate = acctDate;
	}

	public String getSysSeqId() {
		return sysSeqId;
	}

	public void setSysSeqId(String sysSeqId) {
		this.sysSeqId = sysSeqId;
	}

	public String getPayChnl() {
		return payChnl;
	}

	public void setPayChnl(String payChnl) {
		this.payChnl = payChnl;
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

	public String getFeeAmt() {
		return feeAmt;
	}

	public void setFeeAmt(String feeAmt) {
		this.feeAmt = feeAmt;
	}

}
