package com.phzc.profile.api.constants;

public interface ProfileConstants {
	
	//sequence
	public static String prdtOrderId = "prdtOrderId";
	public static String payOrderId = "payOrderId";
	public static String appointOrderId = "appointOrderId";
	public static String refundOrderId = "refundOrderId";

	/**
	 * 状态 0：不可用， 1：可用
	 */
	final static int STATUS_DELETE = 0; 
	/**
	 * 状态 0：不可用， 1：可用
	 */
	final static int STATUS_AVAILABLE = 1; 
	/**
	 * 认证类型 0：个人投资人，1：机构投资人，2：TG
	 */
	final static int IDENTITY_TYPE_PER = 0;
	/**
	 * 认证类型 0：个人投资人，1：机构投资人，2：TG
	 */
	final static int IDENTITY_TYPE_ORG = 1;
	/**
	 * 认证类型 0：个人投资人，1：机构投资人，2：TG
	 */
	final static int IDENTITY_TYPE_TG = 2;
	/**
	 * 用户来源类型 1：众筹直接用户，2：第三方用户
	 */
	final static int USER_TYPE_OWN = 1;
	/**
	 * 用户来源类型 1：众筹直接用户，2：第三方用户
	 */
	final static int USER_TYPE_THIRD = 2;  
	/**
	 * 认证状态   0： 未申请，1：待审核， 2：已通过  ，3：审核未通过 
	 */
	final static int IDENTITY_FLAG_NO = 0; 
	/**
	 * 认证状态   0： 未申请，1：待审核， 2：已通过  ，3：审核未通过 
	 */
	final static int IDENTITY_FLAG_ING = 1; 
	/**
	 * 认证状态   0： 未申请，1：待审核， 2：已通过  ，3：审核未通过 
	 */
	final static int IDENTITY_FLAG_OK = 2;
	/**
	 * 认证状态   0： 未申请，1：待审核， 2：已通过  ，3：审核未通过 
	 */
	final static int IDENTITY_FLAG_UN_PASS = 3;
	/**
	 * email认证结果  0:未认证通过 ,1:认证通过
	 */
	final static int EMAIL_FLAG_NO = 0; 
	/**
	 * email认证结果  0:未认证通过 ,1:认证通过
	 */
	final static int EMAIL_FLAG_OK = 1; 
	

}