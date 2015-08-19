package com.phzc.profile.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.phzc.console.base.exception.ErrorCode;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.profile.web.form.BankCardBindForm;
import com.phzc.ubs.common.facade.model.IdInfoResult;
import com.phzc.ubs.common.facade.model.QueryAcctInfoByPkRequest;
import com.phzc.ubs.common.facade.model.QueryAcctInfoByPkResult;
import com.phzc.ubs.common.facade.model.UserInfoDto;
import com.phzc.ubs.common.facade.model.YBbindCardConfirmRequest;
import com.phzc.ubs.common.facade.model.YBbindCardConfirmResult;
import com.phzc.ubs.common.facade.service.AccountInfoFacade;
import com.phzc.ubs.common.facade.service.BankCardInfoFacade;

public class BaseYeepayController extends BaseCardController {
	
	private static final Logger logger = LoggerFactory.getLogger(BaseYeepayController.class);
	
	protected JSONObject bindStepOne(BankCardBindForm cardForm, 
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		
		User user = userSessionFacade.getUser(request);
		String custId = user.getCustId();
		String operId = user.getOperId();

		String realName = cardForm.getRealName();
		String certType = CHINA_ID_CARD;
		String certId = cardForm.getCertId();
		String certStatus = cardForm.getCertStatus();
        
        if (needVerifyRealNameAndOpenAccount(certStatus, realName, certId)) {
        	logger.info("实名开户, certStatus: " + certStatus);
			try {
				JSONObject verifyRealNameAndOpenAccountResult = verifyRealNameAndOpenAccount(cardForm, custId,
						realName, certType, certId);
				if (verifyRealNameAndOpenAccountResult != null) {
					return verifyRealNameAndOpenAccountResult;
				}
			} catch (ServiceException e) {
				jsonObject.put("msg", e.getMessage());
				jsonObject.put("success", false);
				return jsonObject;
			}
        }
        
        // 除实名认证外，其他阶段都需要再从UBS获取用户实名信息
        if (!String.valueOf(CERTIFY_NULL).equals(certStatus)) {
		    IdInfoResult idInfoResult = null;
			try {
				idInfoResult = getRealUserInfo(custId, operId);
			} catch (ServiceException e) {
				jsonObject.put("msg", e.getMessage());
				jsonObject.put("success", false);
				return jsonObject;
			}
			
			if (null == idInfoResult || !"000".equals(idInfoResult.getRespCode())) {
				jsonObject.put("msg", (null == idInfoResult) ? "用户查询系统异常" : idInfoResult.getRespDesc());
				jsonObject.put("success", false);
				return jsonObject;
			}
			
			UserInfoDto usrBaseInfo = idInfoResult.getList().get(0);
            realName = usrBaseInfo.getUsrName();
            certId = usrBaseInfo.getCertId();
            certType = usrBaseInfo.getCertType();
		}

        AccountInfoFacade accountInfoFacade = null;
		try {
			accountInfoFacade = (AccountInfoFacade) serviceClientFactory.
					getWebServiceClient(AccountInfoFacade.class, "accountInfoFacade", "UBS");
	        QueryAcctInfoByPkRequest queryAcctInfoByPkRequest = new QueryAcctInfoByPkRequest();
	        queryAcctInfoByPkRequest.setConsumerId(ubsConsumerId);
	        queryAcctInfoByPkRequest.setCustId(custId);
	        queryAcctInfoByPkRequest.setSubAcctId("BASEDA");
	        QueryAcctInfoByPkResult queryAcctInfoByPkResult = accountInfoFacade.queryAcctInfoByCustIdAndSubAcctId(queryAcctInfoByPkRequest);
	        if (null != queryAcctInfoByPkResult && null == queryAcctInfoByPkResult.getAccountInfoDto()) {
	        	// need open account
	        	JSONObject openAccountResultJSON = this.openAccount(cardForm, custId);
	        	if (openAccountResultJSON != null) {
	        		return openAccountResultJSON;
	        	}
	        }
		} catch (ServiceException e) {
			jsonObject.put("msg", e.getMessage());
			jsonObject.put("success", false);
			return jsonObject;
		}

        //绑卡鉴权第一次
        JSONObject bindCardResult = bindCard(cardForm, custId, realName, certId, certType);
        if (bindCardResult != null) {
        	return bindCardResult;
        }
		
		jsonObject.put("success", true);
		return jsonObject;
	}

	protected JSONObject bindStepTwo(String certStatus, String smsVerifyCode, 
			String transPwd, String confirmTransPwd,  
			HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();
		
		try {
			this.checkParams(smsVerifyCode, transPwd, confirmTransPwd);
		} catch (ServiceException e) {
			jsonObj.put("msg", e.getMessage());
			jsonObj.put("success", false);
			return jsonObj;
		}
		
		User user = userSessionFacade.getUser(request);
		
		if (needSetTransPwd(certStatus, transPwd)) {
			logger.info("开户完成设置交易密码, certStatus: " + certStatus);
			try {
				JSONObject setTradePwdResult = setTradePwd(user.getCustId(), user.getOperId(), transPwd);
				if (setTradePwdResult != null) {
					return setTradePwdResult;
				}
			} catch (ServiceException e) {
				jsonObj.put("msg", e.getMessage());
				jsonObj.put("success", false);
				return jsonObj;
			}
		}
		
		BankCardInfoFacade ubsBankCardService = null;
        try {
			ubsBankCardService = (BankCardInfoFacade) serviceClientFactory
					.getWebServiceClient(BankCardInfoFacade.class, "bankCardInfoFacade", "UBS");
		} catch (ServiceException e) {
			jsonObj.put("msg", e.getMessage());
			jsonObj.put("success", false);
			return jsonObj;
		}
        
        YBbindCardConfirmRequest ybBindCardConfirmRequest = new YBbindCardConfirmRequest();
        ybBindCardConfirmRequest.setConsumerId(ubsConsumerId);
        ybBindCardConfirmRequest.setCustId(user.getCustId());
        ybBindCardConfirmRequest.setSmsCode(smsVerifyCode);
        logger.info("YBbindCardConfirmRequest: " + JSON.toJSONString(ybBindCardConfirmRequest));
        
		YBbindCardConfirmResult ybBindCardConfirmResult = ubsBankCardService.YBbindCardConfirm(ybBindCardConfirmRequest);
		if (null == ybBindCardConfirmResult || !"000".equals(ybBindCardConfirmResult.getRespCode())) {
			jsonObj.put("msg", (null == ybBindCardConfirmResult) ? "易宝绑卡系统异常" : ybBindCardConfirmResult.getRespDesc());
			jsonObj.put("success", false);
			return jsonObj;
		}
        
        jsonObj.put("success", true);
		return jsonObj;
	}
	
	private void checkParams(String smsVerifyCode, String transPwd, String confirmTransPwd) 
			throws ServiceException {
		if (StringUtils.isEmpty(smsVerifyCode)) {
			throw new ServiceException(ErrorCode.NULL_INPUT, "短信验证码为空");
		}
		
		if (!StringUtils.isEmpty(transPwd) && !StringUtils.isEmpty(confirmTransPwd) 
				&& !transPwd.equals(confirmTransPwd)) {
			throw new ServiceException(ErrorCode.PASSWORD_IS_NOT_SAME, "两次输入的交易密码不一致");
		}
	}

}