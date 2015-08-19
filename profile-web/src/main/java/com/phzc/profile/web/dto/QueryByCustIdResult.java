package com.phzc.profile.web.dto;

import java.io.Serializable;
import java.util.List;

public class QueryByCustIdResult implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = -1950173585529947765L;

	private String custId;//用户客户号
	
	private String totalRecord;//总记录数
	
	private OrderInfo orderInfo;//订单详情信息列表
	
	private String merId;//商户号
	
    private String cmdId;//请求方法名
    
    private String respCode;//返回码
    
    private String respDesc;//返回内容
    
    private String merPriv;//私有域
    
    private String chkValue;//加签
    
    private String respExt;//返回扩展字段

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

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
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
    
}
