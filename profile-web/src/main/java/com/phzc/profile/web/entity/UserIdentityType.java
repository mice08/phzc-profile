package com.phzc.profile.web.entity;

/**
 * 认证类型
 * @author devuser
 *
 */
public enum UserIdentityType {
	NORMAL("0", "普通"), NEW_THREE("1", "新三板"),SPEC_QUALI_INVESTORS("2","特定合格投资人");

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	private String code;
	private String desc;

	UserIdentityType(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	
	/**
	 * 根据code值获取value
	 * 
	 * @param code
	 * @return
	 */
	public static String getName(String code) {

		if(null == code) return null;
		for (UserIdentityType c : UserIdentityType.values()) {

			if (code.equals(c.getCode())) {

				return c.desc;
			}
		}
		return null;
	}

	/**
	 * 根据desc获取code
	 * 
	 * @param index
	 * @return
	 */
	public static String getId(String desc) {

		if (null == desc) {
			return null;
		}
		for (UserIdentityType c : UserIdentityType.values()) {

			if (desc.equals(c.getDesc())) {

				return c.code;
			}
		}
		return null;
	}
}
