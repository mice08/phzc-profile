package com.phzc.profile.web.dto;

import java.io.Serializable;

public class OrderPaybackInfo implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 8434481568098504021L;
	
	private String payBackId;
	private String orderId;
	private String custId;
	private String addressId;
	private String goodsId;
	private String goodsDetail;
	private String email;
	private String status;
	private String reciveDate;
	private String sendDate;
	
	public String getPayBackId() {
		return payBackId;
	}
	public void setPayBackId(String payBackId) {
		this.payBackId = payBackId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getCustId() {
		return custId;
	}
	public void setCustId(String custId) {
		this.custId = custId;
	}
	public String getAddressId() {
		return addressId;
	}
	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsDetail() {
		return goodsDetail;
	}
	public void setGoodsDetail(String goodsDetail) {
		this.goodsDetail = goodsDetail;
	}
	public String getReciveDate() {
		return reciveDate;
	}
	public void setReciveDate(String reciveDate) {
		this.reciveDate = reciveDate;
	}
	public String getSendDate() {
		return sendDate;
	}
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
}
