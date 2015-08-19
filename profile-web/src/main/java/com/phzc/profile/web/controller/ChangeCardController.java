package com.phzc.profile.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.phzc.console.SmsConstants;
import com.phzc.console.base.exception.ErrorCode;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.console.base.session.impl.DefaultConsoleSession;
import com.phzc.profile.web.form.BankCardBindForm;
import com.phzc.ubs.common.facade.model.QueryBankCardRequest;
import com.phzc.ubs.common.facade.model.QueryBankCardResult;
import com.phzc.ubs.common.facade.model.UnbindBankCardRequest;
import com.phzc.ubs.common.facade.model.UnbindBankCardResult;
import com.phzc.ubs.common.facade.service.BankCardInfoFacade;

@Controller
@RequestMapping("/changeCard/*")
public class ChangeCardController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(ChangeCardController.class);
		
	@RequestMapping(value = "changeCardForm.do", method = RequestMethod.GET)
	public String changeCardForm(Model model, HttpServletRequest request, @RequestParam String cardSeqId) {
		
		String mobile = userSessionFacade.getUser(request).getMobileNo();
		model.addAttribute("mobile", mobile);
		model.addAttribute("cardSeqId", cardSeqId);
		model.addAttribute("confusedMobile", confusedMobile(mobile));
		model.addAttribute("config", applicationPropertyConfig);
		
		return "card/changeCardForm.ftl";
	}
	
	private String confusedMobile(String mobile) {
		return mobile.substring(0, 3) + "****" + mobile.substring(7);
	}

	@RequestMapping(value = "changeCard.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject changeCard(BankCardBindForm cardForm, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		
		User user = userSessionFacade.getUser(request);
		String custId = user.getCustId();
		String operId = user.getOperId();
		
		Integer cardSeqId = cardForm.getCardSeqId();
		
		String tradePwd = cardForm.getTransPwd();
		String smsVerifyCode = request.getParameter("smsVerifyCode");
		if (StringUtils.isEmpty(smsVerifyCode)) {
			cardForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_CODE_NULL));
			cardForm.setErrorString("短信验证码为空");
			jsonObject.put("cardForm", cardForm);
			jsonObject.put("success", false);
			return jsonObject;
		}

		String verifyCodeInSession = DefaultConsoleSession.getInstance()
				.getAttribute(request, SmsConstants.SESSION_MESSAGE_CODE);
		if (!"8888".equals(smsVerifyCode) && !smsVerifyCode.equals(verifyCodeInSession)) {
			cardForm.setErrorCode(String.valueOf(ErrorCode.USER_MOBILE_SAFETY_CODE));
			cardForm.setErrorString("短信验证码错误");
			jsonObject.put("cardForm", cardForm);
			jsonObject.put("success", false);
			return jsonObject;
		}
		DefaultConsoleSession.getInstance().setAttribute(request, SmsConstants.SESSION_MESSAGE_CODE, null);

		BankCardInfoFacade ubsBankCardService = null;
		try {
			ubsBankCardService = (BankCardInfoFacade) serviceClientFactory
					.getWebServiceClient(BankCardInfoFacade.class, "bankCardInfoFacade", "UBS");
		} catch (ServiceException e) {
			logger.info(e.getMessage(), e);
			cardForm.setErrorCode(String.valueOf(e.getErrorCode()));
			cardForm.setErrorString("系统错误");
			jsonObject.put("cardForm", cardForm);
			jsonObject.put("success", false);
			return jsonObject;
		}
		
		QueryBankCardRequest qryCardReq = new QueryBankCardRequest();
		qryCardReq.setCustId(custId);
		qryCardReq.setCardSeqId(cardSeqId);
		qryCardReq.setConsumerId(ubsConsumerId);
		QueryBankCardResult qryCardRslt = ubsBankCardService.queryBankCard(qryCardReq);
		if (null == qryCardRslt || null == qryCardRslt.getBankCardInfoList()
				|| qryCardRslt.getBankCardInfoList().isEmpty()) {
			logger.info("查询银行卡系统出错");
			cardForm.setErrorCode(String.valueOf(ErrorCode.UNKNOWN_INTERNAL_ERROR));
			cardForm.setErrorString("查询银行卡系统出错");
			jsonObject.put("cardForm", cardForm);
			jsonObject.put("success", false);
			return jsonObject;
		}
		
		String cardId = qryCardRslt.getBankCardInfoList().get(0).getCardId();
		System.out.println("cardId: " + cardId);
	    
		UnbindBankCardRequest ubCardReq = new UnbindBankCardRequest();
		ubCardReq.setCustId(custId);
		ubCardReq.setCardId(cardId);
		ubCardReq.setOperId(operId);
		ubCardReq.setTransPwd(tradePwd);
		ubCardReq.setConsumerId(ubsConsumerId);
		
		UnbindBankCardResult ubCardRslt = ubsBankCardService.unbindBankCard(ubCardReq);
		if (null != ubCardRslt) {
		    String respCode = ubCardRslt.getRespCode();
		    if (!"000".equals(respCode)) {
		    	cardForm.setErrorCode(String.valueOf(ubCardRslt.getRespCode()));
				cardForm.setErrorString(ubCardRslt.getRespDesc());
				jsonObject.put("cardForm", cardForm);
				jsonObject.put("success", false);
				return jsonObject;
		    }

//		    DefaultConsoleSession.getInstance().setAttribute(req,userSession.getCustId()+"_"+userSession.getOperId()+"_cardSeqId", "");
		    
		    jsonObject.put("success", true);
			return jsonObject;
		} else {
			logger.info("解绑银行卡系统出错");
			cardForm.setErrorCode(String.valueOf(ErrorCode.UNKNOWN_INTERNAL_ERROR));
			cardForm.setErrorString("解绑银行卡系统出错");
			jsonObject.put("cardForm", cardForm);
			jsonObject.put("success", false);
			return jsonObject;
		}
		
	}

}