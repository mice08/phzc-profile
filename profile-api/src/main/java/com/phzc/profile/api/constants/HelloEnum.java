package com.phzc.profile.api.constants;

public enum HelloEnum {
	FILE(0, "配置文件"), ITEM(1, "配置项");
	private int type = 0;
	private String modelName = null;

	private HelloEnum(int type, String modelName) {
		this.type = type;
		this.modelName = modelName;
	}

	public static HelloEnum getByType(int type) {

		int index = 0;
		for (HelloEnum disConfigTypeEnum : HelloEnum.values()) {

			if (type == index) {
				return disConfigTypeEnum;
			}

			index++;
		}

		return null;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
}
