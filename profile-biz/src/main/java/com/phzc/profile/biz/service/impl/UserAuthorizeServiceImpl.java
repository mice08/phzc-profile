package com.phzc.profile.biz.service.impl;

import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.phzc.console.base.exception.DBException;
import com.phzc.console.base.exception.ErrorCodeEnum;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.util.VerificationUtil;
import com.phzc.profile.api.constants.CertChkStatEnum;
import com.phzc.profile.api.utils.HttpClientPost;
import com.phzc.profile.api.vo.BusiData;
import com.phzc.profile.api.vo.CertReqMsg;
import com.phzc.profile.api.vo.Header;
import com.phzc.profile.api.vo.SecurityInfo;
import com.phzc.profile.api.vo.UserAuthorizeInfo;
import com.phzc.profile.biz.conf.IDFiveProperties;
import com.phzc.profile.biz.model.CertInfo;
import com.phzc.profile.biz.model.CertLog;
import com.phzc.profile.biz.service.CertInfoService;
import com.phzc.profile.biz.service.CertLogService;
import com.phzc.profile.biz.service.UserAuthorizeService;
import com.phzc.profile.biz.service.ServiceResponse;
import com.phzc.profile.biz.service.ServiceResponse.RetType;
import com.phzc.profile.biz.utils.DataSecurityUtil;

@Service("userAuthorizeService")
public class UserAuthorizeServiceImpl implements UserAuthorizeService {
	
	private static final Logger logger = LoggerFactory.getLogger(UserAuthorizeServiceImpl.class);
	
	private static final String CHINA_ID_CARD = "00";
	
	@Autowired
	private CertInfoService certInfoService;
	
	@Autowired
	private CertLogService certLogService;
	
	@Autowired
	private IDFiveProperties idFiveProperties;
	
	@Override
	public ServiceResponse verifyRealName(String realName, String certType, String certId) {
		if (!CHINA_ID_CARD.equals(certType)) {
			throw new RuntimeException("Not support this type of certificate: " + certType);
		}

		UserAuthorizeInfo userAuthrizeInfo = new UserAuthorizeInfo();
		userAuthrizeInfo.setCertId(certId);
		userAuthrizeInfo.setCertName(realName);
		this.authorizeUser(userAuthrizeInfo);
		String respCode = userAuthrizeInfo.getRespCode();
		String respDesc = userAuthrizeInfo.getRespDesc();

		ServiceResponse wsResponse = new ServiceResponse();
		if (!"0".equals(respCode)) {
			logger.info("实名认证失败：" + respDesc);
			String msg = respDesc;
			if (String.valueOf(ErrorCodeEnum.CERT_CHECK_FAIL.getErrorCode()).equals(respCode)
					|| String.valueOf(ErrorCodeEnum.CERT_CHECK_MANY.getErrorCode()).equals(respCode)
					|| String.valueOf(ErrorCodeEnum.CERT_CHECK_MMANY.getErrorCode()).equals(respCode)
					|| String.valueOf(ErrorCodeEnum.CERT_CHECK_DIFFER.getErrorCode()).equals(respCode)) {
				wsResponse.setRetType(RetType.BIZ_ERROR);
				wsResponse.setMsg(msg);
			} else {
				wsResponse.setRetType(RetType.SYS_ERROR);
			}
		} else {
			wsResponse.setRetType(RetType.SUCCESS);
		}

		return wsResponse;
	}

	@Override
	@Transactional
	public UserAuthorizeInfo authorizeUser(UserAuthorizeInfo userAuthorizeInfo) {
		
		if (!checkParam(userAuthorizeInfo)) {
			logger.info("Identity card number format error: " + userAuthorizeInfo.getCertId());
			return userAuthorizeInfo.assembleAuthorizeInfo(ErrorCodeEnum.PARAM_FAIL);
		}
		
		String certId = userAuthorizeInfo.getCertId();
		String certName = userAuthorizeInfo.getCertName();
		
		CertInfo certInfo = null;
		try {
			certInfo = certInfoService.findCertInfoByCertNo(certId);
		} catch (DBException e) {
			logger.error("[" + userAuthorizeInfo.getCertId() + "]" + e.getMessage(), e);
			return userAuthorizeInfo.assembleAuthorizeInfo(ErrorCodeEnum.EXCEPTION);
		}
		
		if (null != certInfo) {
			if (StringUtils.equals(certName, certInfo.getCertName())) {
				logger.info("Identity card number and name repeated verification: " + userAuthorizeInfo.getCertId());
				return userAuthorizeInfo.assembleAuthorizeInfo(ErrorCodeEnum.CERT_CHECK_MMANY);
			}
			logger.info("Duplicate verification of different names and identity cards: " + userAuthorizeInfo.getCertId());
			return userAuthorizeInfo.assembleAuthorizeInfo(ErrorCodeEnum.CERT_CHECK_MANY);
		}
		
		// TODO 记录流水信息
		CertLog certLog = new CertLog();
		certLog.setCertName(certName);
		certLog.setCertNo(certId);
		certLog.setCertType(CHINA_ID_CARD);
		certLog.setChkStat(CertChkStatEnum.P.getCode());
		certLogService.addCertLog(certLog);
		
		String res = gotResult(userAuthorizeInfo, String.valueOf(certLog.getChkSeqId()));
		if ("00".equals(res)) {
			certInfo = new CertInfo();
			certInfo.setCertNo(certId);
			certInfo.setCertName(certName);
			certInfo.setCertType(CHINA_ID_CARD);
			
			boolean addSuccess = certInfoService.addCertInfo(certInfo);
			if (addSuccess) {
				userAuthorizeInfo.assembleAuthorizeInfo(ErrorCodeEnum.SUCCESS);
				certLog.setChkStat(CertChkStatEnum.S.getCode());
			} else {
				certLog.setChkStat(CertChkStatEnum.F.getCode());
				userAuthorizeInfo.assembleAuthorizeInfo(ErrorCodeEnum.EXCEPTION);
			}
			
		} else if ("01".equals(res)) {
		 	certLog.setChkStat(CertChkStatEnum.F.getCode());
			userAuthorizeInfo.assembleAuthorizeInfo(ErrorCodeEnum.CERT_CHECK_DIFFER);
		} else {
			certLog.setChkStat(CertChkStatEnum.F.getCode());
			userAuthorizeInfo.assembleAuthorizeInfo(ErrorCodeEnum.CERT_CHECK_FAIL);
		}
		
		// update cert log
		certLogService.modifyCertLog(certLog);
		
		logger.info("Result of ID5: " + userAuthorizeInfo.getRespDesc());
		
		return userAuthorizeInfo;
	}
	
	private String gotResult(UserAuthorizeInfo userCertInfo, String reqId) {

		try {
			Header header = new Header();
			CertReqMsg reqMsg = new CertReqMsg();
			
			String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis()));
			
			logger.info(" before Get configuration information");
			header.setOrgCode(idFiveProperties.getOrgCode());
			header.setChnlId(idFiveProperties.getChnlId());
			header.setAuthCode(idFiveProperties.getAuthCode());
			logger.info(" after Get configuration information");
			header.setAuthDate(date);
			header.setTransDate(date);
			header.setTransNo(reqId);
			logger.info("header: " + JSON.toJSONString(header));
			BusiData busiData = new BusiData();
			busiData.setIdNo(userCertInfo.getCertId());
			busiData.setName(userCertInfo.getCertName());
			busiData.setRefId(reqId);
			logger.info("busiData: " + JSON.toJSONString(busiData));
			String busIDataJson = JSONObject.toJSONString(busiData);
			String encBusiData = DataSecurityUtil.encrypt(busIDataJson.getBytes("utf-8"), idFiveProperties.getCheckCode());// 对json加密

			String sigValue = DataSecurityUtil.signData(encBusiData, idFiveProperties);// 将busiData节点做加密
			SecurityInfo securityInfo = new SecurityInfo();
			securityInfo.setSignatureValue(sigValue);
			securityInfo.setUserPassword(DataSecurityUtil.digest(idFiveProperties.getUserPassword().getBytes()));
			securityInfo.setUserName(idFiveProperties.getUserName());

			reqMsg.setHeader(header);
			reqMsg.setBusiData(encBusiData);
			reqMsg.setSecurityInfo(securityInfo);

			String sendMessage = JSONObject.toJSONString(reqMsg);//SiteJsonUtils.writeEntiry2JSON(reqMsg);
			logger.info("Before calling the state");
			String res = HttpClientPost.doPost(sendMessage, idFiveProperties.getPostUrl());
			logger.info("Return message" + res);
			String result = resolve(res);
			return result;	
		} catch (ClientProtocolException e) {
			logger.info("Client call exception",e);
		} catch (ServiceException e) {
			logger.info("Service exception",e);
		} catch (IOException e) {
			logger.info("Read file flow error",e);
		} catch (Exception e) {  
			logger.info("配置文件site.property读取字段有错或其他错",e);
		}
		return null;
	}
	
	private boolean checkParam(UserAuthorizeInfo userCertInfo) {
		if (StringUtils.isBlank(userCertInfo.getCertId())) {
			return false;
		}
		if (StringUtils.isBlank(userCertInfo.getCertName())) {
			return false;
		}
		if (!VerificationUtil.verify(userCertInfo.getCertId())) {
			return false;
		}
		return true;
	}
	
	/**
	 * 解析返回数据
	 * 
	 * @param res
	 * @return result
	 * @throws Exception
	 */
	public String resolve(String res) throws Exception {
		String result = "04";// 返回数据，00一致，01不一致，02无此号，04数据异常
		JSONObject json = JSONObject.parseObject(res);
		String header = json.getString("header");
		JSONObject headerJson = JSONObject.parseObject(header);
		String rtCode = headerJson.getString("rtCode");
		if (null != rtCode && "E000000".equals(rtCode)) {// 返回成功开始解析
			String busiData = json.getString("busiData");
			if (null != busiData) {
				String busiDataResolve = DataSecurityUtil.decrypt(busiData, idFiveProperties.getCheckCode());
				JSONObject busiDataJson = JSONObject.parseObject(busiDataResolve);
				logger.info("success:" + busiDataResolve);
				String compStatus = busiDataJson.getString("compStatus");
				if (null != compStatus) {
					if ("3".equals(compStatus)) {
						result = "00";
					} else if ("2".equals(compStatus)) {
						result = "01";
					} else if ("1".equals(compStatus)) {
						result = "02";
					}
				}
			}
		}
		return result;
	}

}