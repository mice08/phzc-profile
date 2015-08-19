package com.phzc.profile.biz.service;

import com.phzc.console.base.exception.DBException;
import com.phzc.profile.biz.model.CertInfo;

public interface CertInfoService {
	
	CertInfo findCertInfoByCertNo(String certNo) throws DBException;
	
	boolean addCertInfo(CertInfo certInfo);

}