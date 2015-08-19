package com.phzc.profile.biz.model;

import java.sql.Timestamp;
/**
 * 认证字段
 * @author ZHAIRONGYE
 *
 */
public class IdentityInfo  {
	/**
	 * 申请编号也是主键
	 */
	private String idSid;
	private String custId;
	/**
	 * 认证类型
	 */
	private String idType;
	private String usrType;
	private String assetSal;
	private String assetTot;
	private String assetOther;
	private String orgName;
	private String usrName;
	private String certId;
	private String usrMp;
	private String usrEmail;
	private Timestamp auditTime;
	private Timestamp identityTime;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 
	 * @return
	 */
	private String comments;
	/**
	 * 审核状态
	 */
	private String identityStat;
	
	private String radioCons;
	private String errorCode;
	private String errorString;
	private String isAgree;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorString() {
		return errorString;
	}

	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}

	

	public String getIdSid() {
		return idSid;
	}

	public void setIdSid(String idSid) {
		this.idSid = idSid;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getIdType() {
		return idType;
	}

	public void setIdType(String idType) {
		this.idType = idType;
	}

	public String getUsrType() {
		return usrType;
	}

	public void setUsrType(String usrType) {
		this.usrType = usrType;
	}

	public String getAssetSal() {
		return assetSal;
	}

	public void setAssetSal(String assetSal) {
		this.assetSal = assetSal;
	}

	public String getAssetTot() {
		return assetTot;
	}

	public void setAssetTot(String assetTot) {
		this.assetTot = assetTot;
	}

	public String getAssetOther() {
		return assetOther;
	}

	public void setAssetOther(String assetOther) {
		this.assetOther = assetOther;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getUsrName() {
		return usrName;
	}

	public void setUsrName(String usrName) {
		this.usrName = usrName;
	}

	public String getCertId() {
		return certId;
	}

	public void setCertId(String certId) {
		this.certId = certId;
	}

	public String getUsrMp() {
		return usrMp;
	}

	public void setUsrMp(String usrMp) {
		this.usrMp = usrMp;
	}

	public String getUsrEmail() {
		return usrEmail;
	}

	public void setUsrEmail(String usrEmail) {
		this.usrEmail = usrEmail;
	}

	public Timestamp getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Timestamp auditTime) {
		this.auditTime = auditTime;
	}

	public Timestamp getIdentityTime() {
		return identityTime;
	}

	public void setIdentityTime(Timestamp identityTime) {
		this.identityTime = identityTime;
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

	public String getIdentityStat() {
		return identityStat;
	}

	public void setIdentityStat(String identityStat) {
		this.identityStat = identityStat;
	}

	public String getRadioCons() {
		return radioCons;
	}

	public void setRadioCons(String radioCons) {
		this.radioCons = radioCons;
	}

	public String getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(String isAgree) {
		this.isAgree = isAgree;
	}
	
	
}
	
	