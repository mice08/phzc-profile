package com.phzc.profile.api.constants;

import java.util.HashMap;
import java.util.Map;

public enum CertChkStatEnum {
	S("S", "成功"), F("F", "失败"), P("P", "校验中");
	
	private static Map<String, String> statusMap = new HashMap<String, String>();
	
	static {
		statusMap.put("S", "成功");
		statusMap.put("F", "失败");
		statusMap.put("P", "校验中");
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

	CertChkStatEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static Map<String, String> getStatusMap() {
		return statusMap;
	}
}
