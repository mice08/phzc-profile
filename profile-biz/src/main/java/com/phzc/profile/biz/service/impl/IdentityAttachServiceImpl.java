package com.phzc.profile.biz.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phzc.console.base.exception.ServiceException;
import com.phzc.profile.biz.dao.IdentityAttachInfoMapper;
import com.phzc.profile.biz.model.IdentityAttachInfoRecord;
import com.phzc.profile.biz.service.IdentityAttachService;

@Service("identityAttachService")
public class IdentityAttachServiceImpl  implements IdentityAttachService {
	@Autowired  
	IdentityAttachInfoMapper identityAttachInfoMapper;
	@Override
	public void insertIndentityAttachInfo(
			IdentityAttachInfoRecord identityAttachInfoRecord)
			throws ServiceException {
		identityAttachInfoMapper.insertIndentityAttachInfo(identityAttachInfoRecord);
		// TODO Auto-generated method stub
		
	}

	@Override
	public int updateIndentityAttachInfo(
			IdentityAttachInfoRecord identityAttachInfoRecord)
			throws ServiceException {
		 int i=identityAttachInfoMapper.updateIndentityAttachInfo(identityAttachInfoRecord);
		return i;
	}

	@Override
	public List<IdentityAttachInfoRecord> getIdentityFilepath(IdentityAttachInfoRecord identityAttachInfoRecord){
		
		return identityAttachInfoMapper.getIdentityFilepath(identityAttachInfoRecord);
	}

	@Override
	public IdentityAttachInfoRecord getIndentityAttachInfoByCustIdType(
			IdentityAttachInfoRecord identityAttachInfoRecord) {
		// TODO Auto-generated method stub
		return identityAttachInfoMapper.getIndentityAttachInfoByCustIdType(identityAttachInfoRecord);
	}

	
	
	

}
