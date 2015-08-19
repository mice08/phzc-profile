package com.phzc.profile.biz.dao;

import java.util.List;
import java.util.Map;

import com.phzc.console.base.exception.ServiceException;
import com.phzc.profile.biz.model.IdentityInfo;
import com.phzc.profile.biz.model.IdentityRecord;

public interface IdentityInfoMapper {

	/**
	 * 根据用户id获得认证信息列表
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public List<IdentityInfo> getIdentityListByUserId(String userId) throws ServiceException;
	/**
	 * 根据custid和identitytype获得认证信息
	 * @param paraMap
	 * @return
	 * @throws ServiceException
	 */
	public IdentityInfo getIdentityByIdentityType(Map<String,Object> paraMap)throws ServiceException;
	/**
	 * 新增认证信息
	 * @param identityInfo
	 * @return
	 * @throws ServiceException
	 */
	public int insertIdentityInfo(IdentityInfo identityInfo) throws ServiceException;
	/**
	 * 根据认证id获得认证信息
	 * @param identityId
	 * @return
	 */
	public IdentityRecord getIdentityByIdentityId(String identityId);
	/**
	 * 更新认证信息
	 * @param identityRecord
	 * @return
	 */
	public int updateIdentity(IdentityRecord identityRecord);
	
}