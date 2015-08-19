package com.phzc.profile.biz.service;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装service返回对象，RetType区分返回类型：
 * SUCCESS: 成功
 * BIZ_ERROR: 业务级异常，需要把信息反馈至用户
 * SYS_ERROR: 系统级异常，返回消息可统一设置
 * @author YANGBO279
 *
 */
public class ServiceResponse<T> {

	public enum RetType {
		SUCCESS,
		BIZ_ERROR,
		SYS_ERROR;
	}

	private RetType retType;
	private String msg;
	private T obj;
	private List<T> objs;
	
	public ServiceResponse() {
	}

	public ServiceResponse(RetType retType) {
		this.retType = retType;
	}

	public ServiceResponse(RetType retType, String msg) {
		this(retType);
		this.msg = msg;
	}

	public ServiceResponse(RetType retType, String msg, T obj) {
		this(retType, msg);
		this.obj = obj;
	}

	public ServiceResponse(RetType retType, String msg, List<T> objs) {
		this(retType, msg);
		this.objs = new ArrayList<T>();
		this.objs.addAll(objs);
	}

	public RetType getRetType() {
		return retType;
	}
	public void setRetType(RetType retType) {
		this.retType = retType;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public T getObj() {
		return obj;
	}
	public void setObj(T obj) {
		this.obj = obj;
	}
	public List<T> getObjs() {
		return objs;
	}
	public void setObjs(List<T> objs) {
		this.objs = objs;
	}

}