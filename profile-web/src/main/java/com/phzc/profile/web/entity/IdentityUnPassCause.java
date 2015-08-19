package com.phzc.profile.web.entity;

import java.util.HashMap;
import java.util.Map;

public enum IdentityUnPassCause {
	OTHER("0", "其他"), DATA_NOT_ACCURATE("1", "数据不准确"), PICTURE_NOT_CLEAR("2", "图片不清晰"), MATERIAL_WITH_PROBLEM("3",
			"经核查材料有问题");

	private static Map<String, String> causeMap = new HashMap<String, String>();

	static {
		causeMap.put("0", "其他");
		causeMap.put("1", "数据不准确");
		causeMap.put("2", "图片不清晰");
		causeMap.put("3", "经核查材料有问题");
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

	IdentityUnPassCause(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static Map<String, String> getCauseMap() {
		return causeMap;
	}

}
