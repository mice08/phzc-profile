package com.phzc.profile.web.form;

public class DiscussionForm {

    private Integer connectflag; //是否沟通

    private String  content; //吐槽内容
    
    private String mail;
    
    private String mobilePhone;
    
    private String custId;
    
    private String token;

	public Integer getConnectflag() {
		return connectflag;
	}

	public void setConnectflag(Integer connectflag) {
		this.connectflag = connectflag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}



	
	
}