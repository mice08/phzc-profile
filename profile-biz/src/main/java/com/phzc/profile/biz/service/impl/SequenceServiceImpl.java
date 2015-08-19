package com.phzc.profile.biz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phzc.profile.biz.dao.SequenceMapper;
import com.phzc.profile.biz.service.SequenceService;

@Service("sequenceService")
public class SequenceServiceImpl  implements SequenceService {
	@Autowired 
	SequenceMapper sequenceMapper;
	@Override
	public int getSequenceNextval(String seqName) {
		int i=sequenceMapper.getSequenceNextval(seqName);
			sequenceMapper.updateSequenceNextval(seqName);
		return i;
		
	}

//	@Override
//	public void updateSequenceNextval(String seqName) {
//		sequenceMapper.updateSequenceNextval(seqName);
//	}

	
	
	

}
