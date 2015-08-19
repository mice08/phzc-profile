package com.phzc.profile.biz.model;


public class IdentityRecord extends IdentityInfo{
	private String cardImg;
	private String remark;
	private String comments;
	private Integer investorType;	
	private String bizentitycertImg;
	private String taxregistcertImg;
	
	public String getCardImg() {
		return cardImg;
	}
	public void setCardImg(String cardImg) {
		this.cardImg = cardImg;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Integer getInvestorType() {
		return investorType;
	}
	public void setInvestorType(Integer investorType) {
		this.investorType = investorType;
	}
	public String getBizentitycertImg() {
		return bizentitycertImg;
	}
	public void setBizentitycertImg(String bizentitycertImg) {
		this.bizentitycertImg = bizentitycertImg;
	}
	public String getTaxregistcertImg() {
		return taxregistcertImg;
	}
	public void setTaxregistcertImg(String taxregistcertImg) {
		this.taxregistcertImg = taxregistcertImg;
	}
	
}
