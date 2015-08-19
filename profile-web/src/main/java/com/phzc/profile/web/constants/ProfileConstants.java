package com.phzc.profile.web.constants;


import com.phzc.console.util.ConsolePropertiesManager;

public class ProfileConstants {
	
	public static final String RESPONSE_STATUS_SUCCESS = "000";
	public static final String RESPONSE_STATUS_PROCESSING = "108";
	public static final String RESPONSE_STATUS_WRONG_SMS_CODE = "601";
	
	public static final String SEESION_KEY_CARD="cardmap";
	public static final String SEESION_KEY_SMS_SEQ="smsSeq";
	
	
	// UBS安全码
	public String consumerId = ConsolePropertiesManager.getUbsConsumerCode();
	public static final String FORM_PC = "60012";
	public static final String FORM_WX = "60025";
	public static final String DEPARTMENT_CODE = "phzc";
	public static final String SYSTEM_ID = "zc";
	

	public static final String CMDID_RECHARGE = "recharge";
	public static final String CMDID_SEND_SMS = "sendMessage2EL";
	public static final String CMDID_QUERY_RECHARGE_LOG = "queryRechargeLog";
	public static final String REQ_VERSION = "10"; //default 10
	public static final String DEFAULT_ACCOUNT_TYPE = "BASEDA";
	public static final String DEFAULT_BUSINESS_TYPE = "BASEDA";
	public static final String MER_KEY = "TS2015";
	public static final String YL_PAY_CHANNEL = "13";
	public static final String YEE_PAY_CHANNEL = "14";
	public static final String CASHING_SMS_CODE = "CASHING_SMS_CODE";
	public static final String RECHARGE_SMS_CODE = "RECHARGE_SMS_CODE";
	public static final String DEFAULT_PAGE_SIZE = "20";
	public static final String DEFAULT_MERID = "site";

}
