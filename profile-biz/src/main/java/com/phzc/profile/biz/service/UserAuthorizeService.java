package com.phzc.profile.biz.service;

import com.phzc.profile.api.vo.UserAuthorizeInfo;

public interface UserAuthorizeService {
	
	ServiceResponse verifyRealName(String realName, String certType, String certId);
	
	@Deprecated
	UserAuthorizeInfo authorizeUser(UserAuthorizeInfo userCertInfo);

}