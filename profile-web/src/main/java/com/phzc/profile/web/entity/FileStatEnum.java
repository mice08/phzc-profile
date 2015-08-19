package com.phzc.profile.web.entity;

import java.util.HashMap;
import java.util.Map;

public enum FileStatEnum {
N("N", "正常"), C("C", "删除");
	
	private static Map<String, String> statusMap = new HashMap<String, String>();
	
	static {
		statusMap.put("N", "正常");
		statusMap.put("C", "删除");
	}
	
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

	FileStatEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static Map<String, String> getStatusMap() {
		return statusMap;
	}
}
