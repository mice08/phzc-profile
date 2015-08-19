package com.phzc.profile.biz.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 国政通配置
 * @author YANGBO279
 *
 */
@Service
public class IDFiveProperties {
	
	@Value("${technoligy_org_code}")
	private String orgCode;
	@Value("${technoligy_chnl_id}")
	private String chnlId;
	@Value("${technoligy_auth_code}")
	private String authCode;
	@Value("${technoligy_post_url}")
	private String postUrl;
	@Value("${technoligy_user_name}")
	private String userName;
	@Value("${technoligy_user_password}")
	private String userPassword;
	@Value("${technoligy_check_code}")
	private String checkCode;
	@Value("${technoligy_jks_path}")
	private String jksPath;
	@Value("${technoligy_store_password}")
	private String storePassword;
	@Value("${technoligy_store_alias}")
	private String storeAlias;

	public String getOrgCode() {
		return orgCode;
	}

	public String getChnlId() {
		return chnlId;
	}

	public String getAuthCode() {
		return authCode;
	}

	public String getPostUrl() {
		return postUrl;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public String getJksPath() {
		return jksPath;
	}

	public String getStorePassword() {
		return storePassword;
	}

	public String getStoreAlias() {
		return storeAlias;
	}

}