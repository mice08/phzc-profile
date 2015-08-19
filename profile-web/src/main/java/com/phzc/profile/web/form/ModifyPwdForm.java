package com.phzc.profile.web.form;

public class ModifyPwdForm {

	private String custId; // 客户号

	private String orgTransPwd; // 原密码

	private String newTransPwd; // 新密码

	private String errorCode;

	private String errorString;

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getOrgTransPwd() {
		return orgTransPwd;
	}

	public void setOrgTransPwd(String orgTransPwd) {
		this.orgTransPwd = orgTransPwd;
	}

	public String getNewTransPwd() {
		return newTransPwd;
	}

	public void setNewTransPwd(String newTransPwd) {
		this.newTransPwd = newTransPwd;
	}

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

}