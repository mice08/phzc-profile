package com.phzc.profile.biz.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phzc.profile.biz.dao.CertLogMapper;
import com.phzc.profile.biz.model.CertLog;
import com.phzc.profile.biz.service.CertLogService;

@Service("certLogService")
public class CertLogServiceImpl implements CertLogService {
	
	@Autowired
	private CertLogMapper certLogMapper;

	@Override
	public CertLog addCertLog(CertLog certLog) {
		certLogMapper.insertCertLog(certLog);
		return certLog;
	}

	@Override
	public void modifyCertLog(CertLog certLog) {
		certLogMapper.updateCertLog(certLog);
	}

}