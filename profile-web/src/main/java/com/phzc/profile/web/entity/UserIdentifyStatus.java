package com.phzc.profile.web.entity;

public enum UserIdentifyStatus {
	SAVE("0", "未申请"), AUDIT("1", "待审核"), RETURN("2", "已通过"), AVAILABLE("3",
			"未通过"), REVIEW("4", "审核中"), CANCEL("9", "已撤销");

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

	UserIdentifyStatus(String code, String desc) {
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

		if (null == code)
			return null;
		for (UserIdentifyStatus c : UserIdentifyStatus.values()) {

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
		for (UserIdentifyStatus c : UserIdentifyStatus.values()) {

			if (desc.equals(c.getDesc())) {

				return c.code;
			}
		}
		return null;
	}
}
