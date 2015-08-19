package com.phzc.profile.api.vo;

public class BusiData {
	private String name;
	private String idNo;
	private String refId; //用于唯一识别本批次查询中应流水号
	public String getName() {
		return name;
	}
	public String getIdNo() {
		return idNo;
	}
	public String getRefId() {
		return refId;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	
	
	
}
