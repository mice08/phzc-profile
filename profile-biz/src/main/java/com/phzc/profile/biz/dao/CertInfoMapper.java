package com.phzc.profile.biz.dao;

import com.phzc.profile.biz.model.CertInfo;

public interface CertInfoMapper {

	CertInfo getCertInfoByCertNo(String certNo);
	void insertCertInfo(CertInfo certInfo);
	
}