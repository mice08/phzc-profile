package com.phzc.profile.biz.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phzc.console.base.exception.ServiceException;
import com.phzc.profile.biz.dao.IdentityInfoMapper;
import com.phzc.profile.biz.model.IdentityInfo;
import com.phzc.profile.biz.model.IdentityRecord;
import com.phzc.profile.biz.service.IdentityInfoService;

@Service("identityInfoService")
public class IdentityInfoServiceImpl  implements IdentityInfoService {

	@Autowired 
	IdentityInfoMapper identityInfoMapper;
	
	public List<IdentityInfo> getIdentityListByUserId(String userId)
			throws ServiceException {
		
		return identityInfoMapper.getIdentityListByUserId(userId);
		
	}
	public int insertIdentityInfo(IdentityInfo identityInfo) throws ServiceException{
		
		int i=identityInfoMapper.insertIdentityInfo(identityInfo);
		
		return i;
	}
	@Override
	public IdentityRecord getIdentityByIdentityId(String identityId) {
		
		return identityInfoMapper.getIdentityByIdentityId(identityId);
	}
	@Override
	public int updateIdentity(IdentityRecord identityRecord) {
		int i=identityInfoMapper.updateIdentity(identityRecord);
		return i;
	}
	public IdentityInfo getIdentityByIdentityType(Map<String,Object> paraMap)throws ServiceException{
		return identityInfoMapper.getIdentityByIdentityType(paraMap);
	}
	

}
