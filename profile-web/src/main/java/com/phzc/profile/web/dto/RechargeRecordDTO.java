package com.phzc.profile.web.dto;

import java.util.ArrayList;
import java.util.List;

public class RechargeRecordDTO {

//	{"merId":"site","cmdId":"queryRechargeLog","respCode":"000","respDesc":"根据custId查询充值记录信息成功！",
//	"merPriv":"","chkValue":"bba7ffc22a4e8532a1a96aa437754baf","respExt":"",
//	"custId":"20150624100336","totalRecord":"7","totalPage":"1",
//	"currentPage":"1","rechargeInfoList":[{"custId":"20150624100336",
//	"operId":"111","acctDate":"20150724","sysSeqId":"183825065112","payChnl":"13",
//	"transStat":"P","transAmt":"1000.00","feeAmt":null},
//	
	private String merId;
	private String cmdId;
	private String respCode;
	private String respDesc;
	private String merPriv;
	private String chkValue;
	private String respExt;
	private String custId;
	private String totalRecord;
	private String totalPage;
	
	
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getTotalRecord() {
		return totalRecord;
	}
	public void setTotalRecord(String totalRecord) {
		this.totalRecord = totalRecord;
	}
	public String getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}
	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}



	private String currentPage;
	
	
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

	
	
	private List<RechargeInfoList> rechargeInfoList = new ArrayList<RechargeInfoList>();


	public List<RechargeInfoList> getRechargeInfoList() {
		return rechargeInfoList;
	}
	public void setRechargeInfoList(List<RechargeInfoList> rechargeInfoList) {
		this.rechargeInfoList = rechargeInfoList;
	}
	
}
