package com.phzc.profile.web.controller.m;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.phzc.console.CertifyLevel;
import com.phzc.console.CertifyRequired;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.profile.web.controller.BaseYeepayController;
import com.phzc.profile.web.form.BankCardBindForm;
import com.phzc.ubs.common.facade.model.BankCardInfoDto;
import com.phzc.ubs.common.facade.model.IdInfoRequest;
import com.phzc.ubs.common.facade.model.IdInfoResult;
import com.phzc.ubs.common.facade.model.QueryBankCardRequest;
import com.phzc.ubs.common.facade.model.QueryBankCardResult;
import com.phzc.ubs.common.facade.model.UserInfoDto;
import com.phzc.ubs.common.facade.service.BankCardInfoFacade;
import com.phzc.ubs.common.facade.service.UserInfoFacade;

@Controller("mYeepayBindCardController")
@RequestMapping("/m/yeepay/*")
public class MYeepayBindCardController extends BaseYeepayController {

	private static final Logger logger = LoggerFactory.getLogger(MYeepayBindCardController.class);

	private static final String BIND_CARD_LEGAL_FLAG = "bindCardLegalFlag";
	private static final String BIND_CARD_FORM_KEY = "bindCardForm";
	
	@RequestMapping(value = "myCards.do", method = RequestMethod.GET)
	public String myCards(Model model, HttpServletRequest request) {
		model.addAttribute("config", applicationPropertyConfig);
		
		User user = userSessionFacade.getUser(request);
		
		BankCardInfoFacade cardInfoService = null;
		try {
			cardInfoService = (BankCardInfoFacade) serviceClientFactory
					.getWebServiceClient(BankCardInfoFacade.class, "bankCardInfoFacade", "UBS");
		} catch (ServiceException e) {
			model.addAttribute("msg", "系统异常");
			return "share/m/500.ftl";
		}

		QueryBankCardRequest qryCardReq = new QueryBankCardRequest();
		qryCardReq.setConsumerId(ubsConsumerId);
		qryCardReq.setCustId(user.getCustId());
		QueryBankCardResult queryBankCardResult = cardInfoService.queryBankCard(qryCardReq);
		if (null == queryBankCardResult || !"000".equals(queryBankCardResult.getRespCode())) {
			model.addAttribute("msg", (null == queryBankCardResult) ? "银行卡查询系统异常" : queryBankCardResult.getRespDesc());
			return "share/m/500.ftl";
		}
		
		BankCardInfoDto bankCardInfoDto = queryBankCardResult.getBankCardInfoList().get(0);
		String cardId = bankCardInfoDto.getCardId();
		model.addAttribute("config", applicationPropertyConfig);
		model.addAttribute("bankId", bankCardInfoDto.getBankId());
		model.addAttribute("bankName", bankCardInfoDto.getBankName());
		model.addAttribute("cardEndId", cardId.substring(cardId.length() - 4));
		
		return "card/m/yeepayBindSuccess.ftl";
	}

	@CertifyRequired(minLevel = CertifyLevel.TRADE_PWD)
	@RequestMapping(value = "bindStepOneForm.do", method = RequestMethod.GET)
	public String bindStepOneForm(HttpServletRequest request, HttpServletResponse response, Model model, 
			@RequestParam(required = false) String source) {
		model.addAttribute("config", applicationPropertyConfig);
		
		User user = userSessionFacade.getUser(request);
		
		UserInfoFacade userInfoFacade = null;
		try {
			userInfoFacade = (UserInfoFacade) serviceClientFactory
					.getWebServiceClient(UserInfoFacade.class, "userInfoFacade", "UBS");
		} catch (ServiceException e) {
			model.addAttribute("msg", "用户查询系统异常");
			return "share/m/500.ftl";
		}

		IdInfoRequest idInfoRequest = new IdInfoRequest();
		idInfoRequest.setCustId(user.getCustId());
		idInfoRequest.setOperId(user.getOperId());
		idInfoRequest.setConsumerId(ubsConsumerId);
		IdInfoResult idInfoResult = userInfoFacade.queryUserByCustId(idInfoRequest);
		UserInfoDto userInfoDto = null;
		if (null != idInfoResult && "000".equals(idInfoResult.getRespCode())) {
			userInfoDto = idInfoResult.getList().get(0);
		}
		
		if (null == userInfoDto) {
			model.addAttribute("msg", "用户查询系统异常");
			return "share/m/500.ftl";
		}
		
		BankCardInfoFacade cardInfoService = null;
		try {
			cardInfoService = (BankCardInfoFacade) serviceClientFactory
					.getWebServiceClient(BankCardInfoFacade.class, "bankCardInfoFacade", "UBS");
		} catch (ServiceException e) {
			model.addAttribute("msg", "银行卡查询系统异常");
			return "share/m/500.ftl";
		}

		QueryBankCardRequest qryCardReq = new QueryBankCardRequest();
		qryCardReq.setConsumerId(ubsConsumerId);
		qryCardReq.setCustId(userInfoDto.getCustId());
		QueryBankCardResult qryCardRslt = cardInfoService.queryBankCard(qryCardReq);
		if (null != qryCardRslt 
				&& qryCardRslt.getBankCardInfoList() != null 
				&& !qryCardRslt.getBankCardInfoList().isEmpty()) {
			return "redirect:" + applicationPropertyConfig.getDomainProfile() + "/m/yeepay/myCards.do";
		}

		model.addAttribute("realName", userInfoDto.getUsrName());
		if (!StringUtils.isEmpty(source)) {
			model.addAttribute("source", source);
		}
		
		consoleSession.setAttribute(request, BIND_CARD_LEGAL_FLAG, BIND_CARD_LEGAL_FLAG);
		return "card/m/yeepayBindStepOne.ftl";
	}

	@RequestMapping(value = "queryBankInfo.do", method = RequestMethod.GET)
	public @ResponseBody JSONObject queryBankInfoByCardId(@RequestParam String cardId) {
		return super.queryBankInfoByCardId(cardId);
	}

	@RequestMapping(value = "bindStepOne.do", method = RequestMethod.GET)
	public @ResponseBody JSONObject bindStepOneForM(BankCardBindForm cardForm, 
			HttpServletRequest request, HttpServletResponse response) {
		String bindCardLegalFlag = consoleSession.getAttribute(request, BIND_CARD_LEGAL_FLAG);
		if (StringUtils.isEmpty(bindCardLegalFlag)) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("success", false);
			jsonObj.put("msg", "非法请求");
			return jsonObj;
		}
		
		cardForm.setCertStatus(String.valueOf(CERTIFY_NO_CARD));
		consoleSession.setAttribute(request, BIND_CARD_FORM_KEY, cardForm);
		return super.bindStepOne(cardForm, request, response);
	}

	@RequestMapping(value = "bindStepTwoForm.do", method = RequestMethod.GET)
	public String bindStepTwoForm(HttpServletRequest request, HttpServletResponse response, Model model, 
			@RequestParam(required = false) String source) {
		model.addAttribute("config", applicationPropertyConfig);
		
		BankCardBindForm cardForm = (BankCardBindForm) consoleSession
				.getAttribute(request, BIND_CARD_FORM_KEY, BankCardBindForm.class);
		if (null == cardForm) {
			model.addAttribute("msg", "非法请求");
			return "share/m/500.ftl";
		}
		model.addAttribute("cardForm", cardForm);
		
		if (!StringUtils.isEmpty(source)) {
			model.addAttribute("source", source);
		}
		return "card/m/yeepayBindStepTwo.ftl";
	}

	@RequestMapping(value = "bindStepTwo.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject bindStepTwoForM(@RequestParam String smsVerifyCode, 
			HttpServletRequest request, HttpServletResponse response) {
		
		BankCardBindForm bankCardBindForm = (BankCardBindForm) consoleSession
				.getAttribute(request, BIND_CARD_FORM_KEY, BankCardBindForm.class);
		if (null == bankCardBindForm) {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("success", false);
			jsonObj.put("msg", "非法请求");
			return jsonObj;
		}
		consoleSession.deleteAttribute(request, BIND_CARD_FORM_KEY);
		
		String certStatus = String.valueOf(CERTIFY_NO_CARD);
		return super.bindStepTwo(certStatus, smsVerifyCode, null, null, request, response);
	}

	@RequestMapping(value = "bindSuccess.do", method = RequestMethod.GET)
	public String bindSuccess(HttpServletRequest request, HttpServletResponse response, Model model, 
			@RequestParam String bankId, @RequestParam String bankName, @RequestParam String cardId, 
			@RequestParam(required = false) String source) {
		model.addAttribute("config", applicationPropertyConfig);
		model.addAttribute("bankId", bankId);
		model.addAttribute("bankName", bankName);
		model.addAttribute("cardEndId", cardId.substring(cardId.length() - 4));
		if (!StringUtils.isEmpty(source)) {
			model.addAttribute("source", source);
		}
		return "card/m/yeepayBindSuccess.ftl";
	}

}