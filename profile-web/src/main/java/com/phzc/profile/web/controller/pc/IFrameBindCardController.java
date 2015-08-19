package com.phzc.profile.web.controller.pc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.phzc.console.ConsoleConstants;
import com.phzc.console.SmsConstants;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.profile.web.CardChannel;
import com.phzc.profile.web.controller.BaseCardController;
import com.phzc.profile.web.form.BankCardBindForm;
import com.phzc.ubs.common.facade.model.IdInfoRequest;
import com.phzc.ubs.common.facade.model.IdInfoResult;
import com.phzc.ubs.common.facade.model.UserInfoDto;
import com.phzc.ubs.common.facade.service.UserInfoFacade;

@Controller
@RequestMapping("/card/*")
public class IFrameBindCardController extends BaseCardController {
	
	private static final Logger logger = LoggerFactory.getLogger(IFrameBindCardController.class);
	
	@RequestMapping(value = "iFrameBindForm.do", method = RequestMethod.GET)
	public String iFrameBindForm(Model model, HttpServletRequest request, HttpServletResponse response, 
			@RequestParam(required = false) String successFunction, @RequestParam(required = false) String failureFunction) {
		model.addAttribute("config", applicationPropertyConfig);
		model.addAttribute("successFunction", successFunction);
		model.addAttribute("failureFunction", failureFunction);
		
		User user = userSessionFacade.getUser(request);
		
		logger.info("custId: " + user.getCustId());
		logger.info("nickName: " + user.getNick());
		
		UserInfoFacade userInfoFacade = null;
		try {
			userInfoFacade = this.getUserInfoWebServiceClient();
		} catch (ServiceException e) {
			model.addAttribute("msg", "系统异常");
			return "card/iFrameBindCard.ftl";
		}
		
		IdInfoRequest idInfoRequest = new IdInfoRequest();
		idInfoRequest.setConsumerId(ubsConsumerId);
		idInfoRequest.setCustId(user.getCustId());
		idInfoRequest.setOperId(user.getOperId());
		IdInfoResult idInfoResult = userInfoFacade.queryUserByCustId(idInfoRequest);
		if (null == idInfoResult || !"000".equals(idInfoResult.getRespCode())) {
			model.addAttribute("msg", (null == idInfoResult) ? "用户查询系统异常" : idInfoResult.getRespDesc());
		} else {
			UserInfoDto userInfoDto = idInfoResult.getList().get(0);
			model.addAttribute("userInfo", userInfoDto);
		}
		
		if (CardChannel.EASY_LINK.getChannelNo().equals(applicationPropertyConfig.getCardChannel())) {
			return "card/iFrameBindCard.ftl";
		} else if (CardChannel.YEE_PAY.getChannelNo().equals(applicationPropertyConfig.getCardChannel())) {
			return "card/yeepayIframeBindCard.ftl";
		} else {
			throw new RuntimeException("Not support current card channel: " + applicationPropertyConfig.getCardChannel());
		}
	}

	@RequestMapping(value = "iFrameBind.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject iFrameBind(HttpServletRequest request, HttpServletResponse response, 
			BankCardBindForm cardForm) {
		JSONObject jsonObj = new JSONObject();
		
		String smsVerifyCode = request.getParameter("smsVerifyCode");
		if (StringUtils.isEmpty(smsVerifyCode)) {
			jsonObj.put("msg", "短信验证码为空");
			jsonObj.put("success", false);
			return jsonObj;
		}

		String verifyCodeInSession = consoleSession.getAttribute(request, SmsConstants.SESSION_MESSAGE_CODE);
		consoleSession.deleteAttribute(request, SmsConstants.SESSION_MESSAGE_CODE);
		if (!ConsoleConstants.DEFAULT_SMS_VERIFY_CODE.equals(smsVerifyCode) 
				&& !smsVerifyCode.equals(verifyCodeInSession)) {
			jsonObj.put("msg", "短信验证码错误");
			jsonObj.put("success", false);
			return jsonObj;
		}
		
		UserInfoFacade userInfoFacade = null;
		try {
			userInfoFacade = this.getUserInfoWebServiceClient();
		} catch (ServiceException e) {
			jsonObj.put("msg", e.getMessage());
			jsonObj.put("success", false);
			return jsonObj;
		}
		
		User user = userSessionFacade.getUser(request);
		
		IdInfoRequest idInfoRequest = new IdInfoRequest();
		idInfoRequest.setConsumerId(ubsConsumerId);
		idInfoRequest.setCustId(user.getCustId());
		idInfoRequest.setOperId(user.getOperId());
		IdInfoResult idInfoResult = userInfoFacade.queryUserByCustId(idInfoRequest);
		if (null == idInfoResult || !"000".equals(idInfoResult.getRespCode())) {
			jsonObj.put("msg", (null == idInfoResult)? "用户查询系统异常" : idInfoResult.getRespDesc());
			jsonObj.put("success", false);
			return jsonObj;
		}
		
		UserInfoDto userinfoDto = idInfoResult.getList().get(0);
		logger.info("用户查询成功： " + userinfoDto.getUsrName());
		
		JSONObject bindCardResult = bindCard(cardForm, user.getCustId(), userinfoDto.getUsrName(), userinfoDto.getCertId(), CHINA_ID_CARD);
        if (bindCardResult != null) {
        	return bindCardResult;
        }
		
		jsonObj.put("success", true);
		return jsonObj;
	}

}