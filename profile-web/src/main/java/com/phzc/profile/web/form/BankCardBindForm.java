package com.phzc.profile.web.form;

public class BankCardBindForm {

    private String  custId;   //客户号

    private Integer cardSeqId; //卡序列号

    private String  cardId; //卡号
    
    private String bankId; //银行简称

    private String bankName;  //银行名称
    
    private String realName;  //用户真实姓名
    
    private String certId;  //用户身份证号码
    
    private String transPwd; //支付密码
    
    private String confirmTransPwd; //确认输入的支付密码

    private String mobile; //银行预留手机号
    
    private String telCode; //验证码
    
    private String certStatus;//验证标志
    
    private String errorCode;
    
    private String errorString;
    
    private String fromPath;
    
    private String preStep;
    
    public String getPreStep() {
		return preStep;
	}

	public void setPreStep(String preStep) {
		this.preStep = preStep;
	}

	public String getFromPath() {
		return fromPath;
	}

	public void setFromPath(String fromPath) {
		this.fromPath = fromPath;
	}

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getConfirmTransPwd() {
        return confirmTransPwd;
    }

    public void setConfirmTransPwd(String confirmTransPwd) {
        this.confirmTransPwd = confirmTransPwd;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public Integer getCardSeqId() {
        return cardSeqId;
    }

    public void setCardSeqId(Integer cardSeqId) {
        this.cardSeqId = cardSeqId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public String getTelCode() {
        return telCode;
    }

    public void setTelCode(String telCode) {
        this.telCode = telCode;
    }

    public String getTransPwd() {
        return transPwd;
    }

    public void setTransPwd(String transPwd) {
        this.transPwd = transPwd;
    }

    public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

	public String getCertStatus() {
		return certStatus;
	}

	public void setCertStatus(String certStatus) {
		this.certStatus = certStatus;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}
    
}