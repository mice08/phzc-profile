package com.phzc.profile.biz.service;

import java.util.List;

import com.phzc.console.base.exception.ServiceException;
import com.phzc.profile.biz.model.IdentityAttachInfoRecord;

public interface IdentityAttachService {

	/**
	 * 增加资料信息
	 * 
	 * @param identityAttachInfoRecord
	 * @throws ServiceException
	 */
	public void insertIndentityAttachInfo(
			IdentityAttachInfoRecord identityAttachInfoRecord)
			throws ServiceException;

	/**
	 * 更新资料信息
	 * 
	 * @param identityAttachInfoRecord
	 * @return
	 * @throws ServiceException
	 */
	public int updateIndentityAttachInfo(
			IdentityAttachInfoRecord identityAttachInfoRecord)
			throws ServiceException;
	/**
	 * 根据custid获得可以删除的图片
	 * @param custId
	 * @param fileStat
	 * @return
	 */
	public List<IdentityAttachInfoRecord> getIdentityFilepath(IdentityAttachInfoRecord identityAttachInfoRecord);
	
	/**
	 * 根据用户的cust_id 和 type类型获得信息
	 * @param identityAttachInfoRecord
	 * @return
	 */
	public IdentityAttachInfoRecord getIndentityAttachInfoByCustIdType(IdentityAttachInfoRecord identityAttachInfoRecord);
}
