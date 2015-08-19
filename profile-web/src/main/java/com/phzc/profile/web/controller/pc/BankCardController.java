package com.phzc.profile.web.controller.pc;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.phzc.console.ConsoleConstants;
import com.phzc.console.SmsConstants;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.profile.biz.conf.IDFiveProperties;
import com.phzc.profile.web.CardChannel;
import com.phzc.profile.web.controller.BaseCardController;
import com.phzc.profile.web.form.BankCardBindForm;
import com.phzc.ubs.common.facade.model.BankCardInfoDto;
import com.phzc.ubs.common.facade.model.IdInfoRequest;
import com.phzc.ubs.common.facade.model.IdInfoResult;
import com.phzc.ubs.common.facade.model.QueryBankCardRequest;
import com.phzc.ubs.common.facade.model.QueryBankCardResult;
import com.phzc.ubs.common.facade.model.UserInfoDto;
import com.phzc.ubs.common.facade.service.BankCardInfoFacade;
import com.phzc.ubs.common.facade.service.UserInfoFacade;

@Controller
@RequestMapping("/card/*")
public class BankCardController extends BaseCardController {

	private static final Logger logger = LoggerFactory.getLogger(BankCardController.class);
	
	@Autowired
	private IDFiveProperties idFiveProperties;

	@RequestMapping(value = "user", method = RequestMethod.GET)
	public @ResponseBody JSONObject getUser(@RequestParam(required = false) String custId) throws ServiceException {
		UserInfoFacade ubsUserService = (UserInfoFacade) serviceClientFactory
					.getWebServiceClient(UserInfoFacade.class, "userInfoFacade", "UBS");
		
		IdInfoRequest usrBaseReq = new IdInfoRequest();
		usrBaseReq.setCustId(StringUtils.isEmpty(custId) ? "1507181444089953" : custId);
		usrBaseReq.setConsumerId(ubsConsumerId);
		IdInfoResult usrBaseRslt = ubsUserService.queryUserByCustId(usrBaseReq);
		UserInfoDto usrBaseInfo = null;
		if (null != usrBaseRslt) {
			usrBaseInfo = usrBaseRslt.getList().get(0);
		}
		
		logger.info("authcode: " + idFiveProperties.getAuthCode());
		
		return JSONObject.parseObject(JSON.toJSONString(usrBaseInfo));
	}

	@RequestMapping(value = "getBind.do", method = RequestMethod.GET)
	public @ResponseBody Object getBind(HttpServletRequest request) throws ServiceException {
		BankCardInfoFacade cardInfoService = (BankCardInfoFacade) serviceClientFactory
				.getWebServiceClient(BankCardInfoFacade.class, "bankCardInfoFacade", "UBS");
		User user = userSessionFacade.getUser(request);
		
		QueryBankCardRequest qryCardReq = new QueryBankCardRequest();
		qryCardReq.setCustId(user.getCustId());
		qryCardReq.setConsumerId(ubsConsumerId);
		QueryBankCardResult qryCardRslt = cardInfoService.queryBankCard(qryCardReq);
		
		if (qryCardRslt.getBankCardInfoList() == null) {
			return null;
		} else {
			return JSONArray.parseObject(JSONArray.toJSONString(qryCardRslt.getBankCardInfoList()));
		}
	}
	
	@RequestMapping(value = "myCards.do", method = RequestMethod.GET)
	public String myCards(Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		model.addAttribute("config", applicationPropertyConfig);
		
		User user = userSessionFacade.getUser(request);
		
		// 目前只支持绑定一张卡，已绑卡直接返回，提示用户
		BankCardInfoFacade cardInfoService = null;
		try {
			cardInfoService = (BankCardInfoFacade) serviceClientFactory
					.getWebServiceClient(BankCardInfoFacade.class, "bankCardInfoFacade", "UBS");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		
		QueryBankCardRequest qryCardReq = new QueryBankCardRequest();
		qryCardReq.setCustId(user.getCustId());
		qryCardReq.setConsumerId(ubsConsumerId);
		QueryBankCardResult qryCardRslt = cardInfoService.queryBankCard(qryCardReq);
		if (qryCardRslt != null && qryCardRslt.getBankCardInfoList() != null) {
			List<BankCardInfoDto> bankCardInfos = qryCardRslt.getBankCardInfoList();
			for (BankCardInfoDto bankCardInfo : qryCardRslt.getBankCardInfoList()) {
				bankCardInfo.setCardId(confuseCardId(bankCardInfo));
			}
			
			model.addAttribute("cards", bankCardInfos);
		}
		
		String msg = (String) request.getParameter("msg");
		if (!StringUtils.isEmpty(msg)) {
			model.addAttribute("msg", URLEncoder.encode(msg, "UTF-8"));
		}

		return "card/myCards.ftl";
	}

	private String confuseCardId(BankCardInfoDto bankCardInfo) {
		String cardId = bankCardInfo.getCardId();
		int len = cardId.length();
		String subStr = " *** *** ";
		String encodedCardId = cardId.substring(0, 4) + subStr + cardId.substring(len - 4, len);
		return encodedCardId;
	}

	@RequestMapping(value = "bindForm.do", method = RequestMethod.GET)
	public String bindForm(Model model, HttpServletRequest request) {
		model.addAttribute("config", applicationPropertyConfig);

		int certStatus = CERTIFY_NULL;
		
		User user = userSessionFacade.getUser(request);

		// 目前只支持绑定一张卡，已绑卡直接返回，提示用户
		BankCardInfoFacade cardInfoService = null;
		try {
			cardInfoService = (BankCardInfoFacade) serviceClientFactory
					.getWebServiceClient(BankCardInfoFacade.class, "bankCardInfoFacade", "UBS");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		QueryBankCardRequest qryCardReq = new QueryBankCardRequest();
		qryCardReq.setCustId(user.getCustId());
		qryCardReq.setConsumerId(ubsConsumerId);
		QueryBankCardResult qryCardRslt = cardInfoService.queryBankCard(qryCardReq);
		if (null != qryCardRslt 
				&& qryCardRslt.getBankCardInfoList() != null 
				&& !qryCardRslt.getBankCardInfoList().isEmpty()) {
			certStatus = CERTIFY_HAS_CARD;
			model.addAttribute("certStatus", certStatus);
			model.addAttribute("msg", "您已经绑定过银行卡");
			return "redirect:myCards.do";
		}

		// 查询用户实名信息状况，交易密码是否设置
		UserInfoFacade ubsUserService = null;
		try {
			ubsUserService = (UserInfoFacade) serviceClientFactory
					.getWebServiceClient(UserInfoFacade.class, "userInfoFacade", "UBS");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		IdInfoRequest usrBaseReq = new IdInfoRequest();
		usrBaseReq.setCustId(user.getCustId());
		usrBaseReq.setOperId(user.getOperId());
		usrBaseReq.setConsumerId(ubsConsumerId);
		IdInfoResult usrBaseRslt = ubsUserService.queryUserByCustId(usrBaseReq);
		UserInfoDto usrBaseInfo = null;
		if (null != usrBaseRslt && "000".equals(usrBaseRslt.getRespCode())) {
			usrBaseInfo = usrBaseRslt.getList().get(0);
		}

		if (null == usrBaseInfo) {
			certStatus = CERTIFY_NULL;
			model.addAttribute("msg", usrBaseRslt.getRespDesc());
		} else {
			// 新增实名认证状态位，根据此字段来查询是否通过实名认证
			String realName = usrBaseInfo.getUsrName();
			String certId = usrBaseInfo.getCertId();
			if (StringUtils.isBlank(realName) && StringUtils.isBlank(certId)) {
				certStatus = CERTIFY_NULL;
			} else {
				realName = confuseRealName(realName);
				certId = confuseCertId(certId);

				String transPwdSet = usrBaseInfo.getIsTransPwdSet();
				if ("0".equals(transPwdSet)) {
					certStatus = CERTIFY_NO_PWD_NO_CARD;
				} else {
					certStatus = CERTIFY_NO_CARD;
				}
				model.addAttribute("realName", realName);
				model.addAttribute("certId", certId);
			}
		}

		model.addAttribute("certStatus", certStatus);
		
		if (CardChannel.EASY_LINK.getChannelNo().equals(applicationPropertyConfig.getCardChannel())) {
			return "card/bindCard.ftl";
		} else if (CardChannel.YEE_PAY.getChannelNo().equals(applicationPropertyConfig.getCardChannel())) {
			return "card/yeepayBindCard.ftl";
		} else {
			throw new RuntimeException("Not support current card channel: " + applicationPropertyConfig.getCardChannel());
		}

	}

	private String confuseRealName(String realName) {
		realName = "**" + realName.substring(realName.length() - 1, realName.length());
		return realName;
	}

	private String confuseCertId(String certId) {
		certId = certId.substring(0, 1) + "****************"
				+ certId.substring(certId.length() - 1, certId.length());
		return certId;
	}

	@RequestMapping(value = "bind", method = RequestMethod.POST)
	public @ResponseBody Object bind(HttpServletRequest request, HttpServletResponse response, BankCardBindForm cardForm) {
		JSONObject jsonObject = new JSONObject();

		User user = userSessionFacade.getUser(request);
		String custId = user.getCustId();
		String operId = user.getOperId();

		/*提交绑卡操作*/
		String realName = cardForm.getRealName();
		String certType = "00";
		String certId = cardForm.getCertId();
		String transPwd = cardForm.getTransPwd();
		String certStatus = cardForm.getCertStatus();
		
		String smsVerifyCode = request.getParameter("smsVerifyCode");
		if (StringUtils.isEmpty(smsVerifyCode)) {
			jsonObject.put("msg", "短信验证码为空");
			jsonObject.put("success", false);
			return jsonObject;
		}

		String verifyCodeInSession = consoleSession.getAttribute(request, SmsConstants.SESSION_MESSAGE_CODE);
		consoleSession.deleteAttribute(request, SmsConstants.SESSION_MESSAGE_CODE);
		if (!ConsoleConstants.DEFAULT_SMS_VERIFY_CODE.equals(smsVerifyCode) 
				&& !smsVerifyCode.equals(verifyCodeInSession)) {
			jsonObject.put("msg", "短信验证码错误");
			jsonObject.put("success", false);
			return jsonObject;
		}

		//2-3. 实名认证&开户
		if(needVerifyRealNameAndOpenAccount(certStatus, realName, certId)) {
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
		
		//4. 开户完成设置交易密码
		if (needSetTransPwd(certStatus, transPwd)) {
			logger.info("开户完成设置交易密码, certStatus: " + certStatus);
			try {
				JSONObject setTradePwdResult = setTradePwd(custId, operId, transPwd);
				if (setTradePwdResult != null) {
					return setTradePwdResult;
				}
			} catch (ServiceException e) {
				jsonObject.put("msg", e.getMessage());
				jsonObject.put("success", false);
				return jsonObject;
			}
		}

		//5. 绑卡
		logger.info("绑卡, certStatus: " + certStatus);
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
		
		JSONObject bindCardResult = bindCard(cardForm, custId, realName, certId, certType);
        if (bindCardResult != null) {
        	return bindCardResult;
        }
		
		jsonObject.put("success", true);
		return jsonObject;
	}

	@RequestMapping(value = "submitSuccess.do", method = RequestMethod.GET)
    public String submitSuccess(Model model) {
		model.addAttribute("config", applicationPropertyConfig);
    	return "card/submitSuccess.ftl";
    }

	@RequestMapping(value = "queryBankInfo.do", method = RequestMethod.GET)
	public @ResponseBody JSONObject queryBankInfoByCardId(@RequestParam String cardId) {
		return super.queryBankInfoByCardId(cardId);
	}

}