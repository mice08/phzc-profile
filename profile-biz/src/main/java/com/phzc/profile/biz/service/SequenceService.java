package com.phzc.profile.biz.service;


public interface SequenceService {
	/**
	 * 根据 序列名获得序列值
	 * @param seqName
	 * @return
	 */
	public int getSequenceNextval(String seqName);
//	/**
//	 * 根据 序列名序列值增1
//	 * @param seqName
//	 * @return
//	 */
//	public void updateSequenceNextval(String seqName);

}