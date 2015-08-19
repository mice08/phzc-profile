package com.phzc.profile.biz.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.phzc.console.base.exception.DBException;
import com.phzc.profile.biz.dao.CertInfoMapper;
import com.phzc.profile.biz.model.CertInfo;
import com.phzc.profile.biz.service.CertInfoService;

@Service("certInfoService")
public class CertInfoServiceImpl implements CertInfoService {
	
	private static final Logger logger = LoggerFactory.getLogger(CertInfoServiceImpl.class);
	
	@Autowired
	private CertInfoMapper certInfoMapper;

	@Override
	public CertInfo findCertInfoByCertNo(String certNo) throws DBException {
		try {
			return certInfoMapper.getCertInfoByCertNo(certNo);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new DBException(e);
		}
	}

	@Override
	public boolean addCertInfo(CertInfo certInfo) {
		try {
			certInfoMapper.insertCertInfo(certInfo);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return false;
		}
	}

}