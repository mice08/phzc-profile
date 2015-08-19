package com.phzc.profile.web.controller;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.phzc.console.EnvEnum;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.profile.biz.service.ServiceResponse;
import com.phzc.profile.biz.service.ServiceResponse.RetType;
import com.phzc.profile.biz.service.UserAuthorizeService;
import com.phzc.profile.web.form.BankCardBindForm;
import com.phzc.profile.web.services.BindBankCardRequestBuilder;
import com.phzc.ubs.common.facade.model.BindBankCardRequest;
import com.phzc.ubs.common.facade.model.BindBankCardResult;
import com.phzc.ubs.common.facade.model.CardBinInfoDto;
import com.phzc.ubs.common.facade.model.IdInfoRequest;
import com.phzc.ubs.common.facade.model.IdInfoResult;
import com.phzc.ubs.common.facade.model.ModifyCertiInfoRequest;
import com.phzc.ubs.common.facade.model.ModifyCertiInfoResult;
import com.phzc.ubs.common.facade.model.OpenAccountInfoRequest;
import com.phzc.ubs.common.facade.model.OpenAccountInfoResult;
import com.phzc.ubs.common.facade.model.PwdInfoResult;
import com.phzc.ubs.common.facade.model.QueryCardBinInfoRequest;
import com.phzc.ubs.common.facade.model.QueryCardBinInfoResult;
import com.phzc.ubs.common.facade.model.SetTransPwdRequest;
import com.phzc.ubs.common.facade.service.AccountInfoFacade;
import com.phzc.ubs.common.facade.service.BankCardInfoFacade;
import com.phzc.ubs.common.facade.service.PwdInfoFacade;
import com.phzc.ubs.common.facade.service.UserInfoFacade;

public class BaseCardController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(BaseCardController.class);
	
	protected static final int CERTIFY_NULL = 0; // 实名、交易密码和绑卡都未做
	protected static final int CERTIFY_NO_PWD_NO_CARD = 1; // 实名认证通过，未设置交易密码，未绑卡
	protected static final int CERTIFY_NO_CARD = 2; // 实名认证通过，已设置交易密码，未绑卡
	protected static final int CERTIFY_HAS_CARD = 3; // 已绑卡
	
	protected static final String CHINA_ID_CARD = "00";
	
	@Autowired
	protected UserAuthorizeService userAuthorizeService;
	
	@Autowired
	private BindBankCardRequestBuilder bindBankCardRequestBuilder;
	
	protected boolean needVerifyRealNameAndOpenAccount(String certStatus, String realName, String certId) {
		return String.valueOf(CERTIFY_NULL).equals(certStatus) 
				&& !StringUtils.isBlank(realName) && !StringUtils.isBlank(certId);
	}
	
	protected JSONObject verifyRealNameAndOpenAccount(BankCardBindForm cardForm,
			String custId, String realName, String certType, String certId) throws ServiceException {
		//严格进行身份证验证（local环境下不验证）
		if (!applicationPropertyConfig.getEnv().equalsIgnoreCase(EnvEnum.LOCAL.getEnvString())) {
			//国政通验证身份证
			JSONObject verifyRealNameResult = verifyRealName(realName, certType, certId);
			if (verifyRealNameResult != null) {
				return verifyRealNameResult;
			}
		}
		
		//更新用户实名信息
		JSONObject updateRealUserInfoResult = updateRealUserInfo(cardForm, custId, realName, certType, certId);
		if (updateRealUserInfoResult != null) {
			return updateRealUserInfoResult;
		}
		
		//3.开户
		JSONObject openAccountResult = openAccount(cardForm, custId);
		if (openAccountResult != null) {
			return openAccountResult;
		}
		
		return null;
	}

	private JSONObject verifyRealName(String realName, String certType, String certId) {
		ServiceResponse wsResponse = userAuthorizeService.verifyRealName(realName, certType, certId);
		if (RetType.BIZ_ERROR.equals(wsResponse.getRetType())) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("msg", wsResponse.getMsg());
			jsonObject.put("success", false);
			return jsonObject;
		} else if (RetType.SYS_ERROR.equals(wsResponse.getRetType())) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("msg", "很抱歉，您实名认证为通过，请联系客户邮箱");
			jsonObject.put("success", false);
			return jsonObject;
		}
		return null;
	}

	private JSONObject updateRealUserInfo(BankCardBindForm cardForm, String custId,
			String realName, String certType, String certId) throws ServiceException {
		String usrSex = "";
		String birthday = certId.substring(6, 10) + "-" + certId.substring(10, 12) + "-" + certId.substring(12, 14);
		String sexFlag = certId.substring(certId.length()-2, certId.length()-1);
		if ((Integer.valueOf(sexFlag) % 2) != 0) {
			usrSex = "M";
		} else {
			usrSex = "F";
		}
		
		UserInfoFacade ubsUserService = (UserInfoFacade) serviceClientFactory
				.getWebServiceClient(UserInfoFacade.class, "userInfoFacade", "UBS");
		ModifyCertiInfoRequest modifyCertiInfoRequest = new ModifyCertiInfoRequest();
		modifyCertiInfoRequest.setCustId(custId);
		modifyCertiInfoRequest.setRealName(realName);
		modifyCertiInfoRequest.setCertType(certType);
		modifyCertiInfoRequest.setCertNo(certId);
		modifyCertiInfoRequest.setBirthday(birthday);
		modifyCertiInfoRequest.setUsrSex(usrSex);
		modifyCertiInfoRequest.setConsumerId(ubsConsumerId);
		logger.info("ModifyCertiInfoRequest: " + JSON.toJSONString(modifyCertiInfoRequest));
		
		ModifyCertiInfoResult modifyCertiInfoResult = ubsUserService.modifyCertiInfo(modifyCertiInfoRequest);
		if (null == modifyCertiInfoResult || !"000".equals(modifyCertiInfoResult.getRespCode())) {
	        JSONObject jsonObj = new JSONObject();
	        jsonObj.put("msg", (null == modifyCertiInfoResult) ? "开户异常，请稍后重试" : modifyCertiInfoResult.getRespDesc());
	        jsonObj.put("success", false);
	        return jsonObj;
		}
		
		return null;
	}
	
	protected JSONObject openAccount(BankCardBindForm cardForm, String custId) throws ServiceException {
		AccountInfoFacade ubsAcctService = (AccountInfoFacade) serviceClientFactory.
				getWebServiceClient(AccountInfoFacade.class, "accountInfoFacade", "UBS");
		OpenAccountInfoRequest acctReq = new OpenAccountInfoRequest();
		acctReq.setCustId(custId);
		acctReq.setAcctType("BASEDA");
		acctReq.setDcFlag("D");
		acctReq.setAcctAlias("def");
		acctReq.setConsumerId(ubsConsumerId);
		logger.info("OpenAccountInfoRequest: " + JSON.toJSONString(acctReq));
		
		OpenAccountInfoResult openAccountInfoResult = ubsAcctService.openAccountInfo(acctReq);
		if (null == openAccountInfoResult || !"000".equals(openAccountInfoResult.getRespCode())) {
	        JSONObject jsonObj = new JSONObject();
	        jsonObj.put("msg", (null == openAccountInfoResult) ? "开通账户异常，请稍后重试" : openAccountInfoResult.getRespDesc());
	        jsonObj.put("success", false);
	        return jsonObj;
		}
		
		return null;
	}
	
	protected boolean needSetTransPwd(String certStatus, String transPwd) {
		return (String.valueOf(CERTIFY_NULL).equals(certStatus) 
				|| String.valueOf(CERTIFY_NO_PWD_NO_CARD).equals(certStatus)) 
				&& !StringUtils.isEmpty(transPwd);
	}

	protected JSONObject setTradePwd(String custId, String operId, String newTransPwd)
			throws ServiceException {
		PwdInfoFacade ubsPwdService = (PwdInfoFacade) serviceClientFactory
				.getWebServiceClient(PwdInfoFacade.class, "pwdInfoFacade", "UBS");
		SetTransPwdRequest transPwdReq = new SetTransPwdRequest();
		transPwdReq.setCustId(custId);
		transPwdReq.setNewTransPwd(newTransPwd);
		transPwdReq.setOperId(operId);
		transPwdReq.setConsumerId(ubsConsumerId);
		PwdInfoResult transPwdRslt = ubsPwdService.setTransPwd(transPwdReq);
		if (null == transPwdRslt || !"000".equals(transPwdRslt.getRespCode())) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("msg", (null == transPwdRslt) ? "设置交易密码系统异常" : transPwdRslt.getRespDesc());
			jsonObj.put("success", false);
		    return jsonObj;
		}
		
		return null;
	}
	
	protected IdInfoResult getRealUserInfo(String custId, String operId)
			throws ServiceException {
		UserInfoFacade ubsUserService = (UserInfoFacade) serviceClientFactory
				.getWebServiceClient(UserInfoFacade.class, "userInfoFacade", "UBS");
		IdInfoRequest usrBaseReq = new IdInfoRequest();
		usrBaseReq.setCustId(custId);
		usrBaseReq.setOperId(operId);
		usrBaseReq.setConsumerId(ubsConsumerId);
		IdInfoResult usrBaseRslt = ubsUserService.queryUserByCustId(usrBaseReq);
		return usrBaseRslt;
	}
	
	protected JSONObject bindCard(BankCardBindForm cardForm, String custId, String realName, String certId, String certType) {
		JSONObject jsonObj = new JSONObject();

		BankCardInfoFacade ubsBankCardService = null;
        try {
			ubsBankCardService = (BankCardInfoFacade) serviceClientFactory
					.getWebServiceClient(BankCardInfoFacade.class, "bankCardInfoFacade", "UBS");
		} catch (ServiceException e) {
			jsonObj.put("msg", e.getMessage());
			jsonObj.put("success", false);
			return jsonObj;
		}
        
        /*提交绑卡操作*/
		String cardId = cardForm.getCardId();
		String cardType = "P";
		String bankName = cardForm.getBankName();
		String telNo = cardForm.getMobile();
		
		BindBankCardRequest bindBankCardRequest = bindBankCardRequestBuilder.build()
				.withBizValidationInfo(ubsConsumerId)
				.withBankInfo(bankName, cardId, cardType, applicationPropertyConfig.getCardChannel())
				.withIdentificationInfo(custId, realName, certId, certType, telNo)
				.end();
		logger.info("BindBankCardRequest: " + JSON.toJSONString(bindBankCardRequest));
		
		BindBankCardResult bindBankCardResult = ubsBankCardService.bindBankCard(bindBankCardRequest);
		if (null == bindBankCardResult || !"000".equals(bindBankCardResult.getRespCode())) {
			jsonObj.put("msg", (null == bindBankCardResult) ? "银行卡绑定系统异常" : bindBankCardResult.getRespDesc());
			jsonObj.put("success", false);
			return jsonObj;
		}
		
		return null;
	}
	
	protected JSONObject queryBankInfoByCardId(String cardId) {
		JSONObject jsonObj = new JSONObject();

		BankCardInfoFacade ubsBandCardInfoService = null;
		try {
			ubsBandCardInfoService = (BankCardInfoFacade) serviceClientFactory
					.getWebServiceClient(BankCardInfoFacade.class, "bankCardInfoFacade", "UBS");
		} catch (ServiceException e) {
			jsonObj.put("success", false);
			jsonObj.put("msg", "系统异常");
			return jsonObj;
		}

		QueryCardBinInfoRequest binReq = new QueryCardBinInfoRequest();
		binReq.setCardId(cardId);
		binReq.setConsumerId(ubsConsumerId);
		QueryCardBinInfoResult binResult = ubsBandCardInfoService.queryeCardBinInfo(binReq);
		if (null == binResult || !"000".equals(binResult.getRespCode())) {
			jsonObj.put("success", false);
			jsonObj.put("msg", (null == binResult) ? "银行卡查询系统异常" : binResult.getRespDesc());
			return jsonObj;
		}

		CardBinInfoDto binInfo = binResult.getCardBinInfoDto();
		if(!"D".equals(binInfo.getCardType())){
			jsonObj.put("success", false);
			jsonObj.put("msg", "");
			return jsonObj;
		}

		jsonObj.put("success", true);
		jsonObj.put("msg", "银行卡信息查询成功");
		jsonObj.put("bankInfo", JSON.toJSON(binInfo));
		return jsonObj;
	}

}