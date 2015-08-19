package com.phzc.profile.api.vo;

import com.phzc.console.base.exception.ErrorCodeEnum;

public class UserAuthorizeInfo {

	private String certId;
	private String certName;
	private String respCode;
	private String respDesc;
	
	public UserAuthorizeInfo(){}
	public UserAuthorizeInfo(String certId,String certName,String respCode,String respDesc){
		this.certId = certId;
		this.certName = certName;
		this.respCode = respCode;
		this.respDesc = respDesc;
	}
	
	public UserAuthorizeInfo assembleAuthorizeInfo(ErrorCodeEnum errorCodeEnum) {
		this.respCode = String.valueOf(errorCodeEnum.getErrorCode());
		this.respDesc = String.valueOf(errorCodeEnum.getErrorDesc());
		return this;
	}

	public String getCertId() {
		return certId;
	}
	public String getCertName() {
		return certName;
	}
	public String getRespCode() {
		return respCode;
	}
	public String getRespDesc() {
		return respDesc;
	}
	public void setCertId(String certId) {
		this.certId = certId;
	}
	public void setCertName(String certName) {
		this.certName = certName;
	}
	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}
	public void setRespDesc(String respDesc) {
		this.respDesc = respDesc;
	}
	
}
