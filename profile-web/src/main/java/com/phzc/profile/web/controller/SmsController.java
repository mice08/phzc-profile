package com.phzc.profile.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.phzc.console.ConsoleConstants;
import com.phzc.console.SmsConstants;
import com.phzc.console.base.session.impl.DefaultConsoleSession;
import com.phzc.console.sms.SendMessage;
import com.phzc.console.sms.SmsConfig;
import com.phzc.console.util.CommonUtils;
import com.phzc.console.util.ParamValidator;

@Controller
@RequestMapping(value = "/sms/*")
public class SmsController {

	@Autowired
	private SendMessage sendMessage;

	@RequestMapping(value = "sendForBind.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject sendForBind(@RequestParam String mobile, HttpServletRequest req) {
		JSONObject jsonObj = new JSONObject();
		try {

			if (StringUtils.isEmpty(mobile) || !ParamValidator.isMobile(mobile)) {
				jsonObj.put("msg", "手机号输入不正确");
				return jsonObj;
			}

			String type = "1";
			String random = CommonUtils.rands(6);
			DefaultConsoleSession.getInstance().setAttribute(req, SmsConstants.SESSION_MESSAGE_CODE, random);

			String randoms = getSmsMsg(random, SmsConstants.BIZ_BIND_BANKCARD);
			System.out.println(randoms);
			sendMessage.send(randoms, null, type, mobile);
			System.out.println("手机验证码：" + random);

			jsonObj.put(ConsoleConstants.ERROR_CODE, ConsoleConstants.ERROR_CODE_000);
			return jsonObj;
		} catch (Exception e) {
			jsonObj.put("msg", "系统错误");
			return jsonObj;
		}
	}
	
	@RequestMapping(value = "sendForUnbind.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject sendForUnBind(@RequestParam String mobile, HttpServletRequest req) {
		JSONObject jsonObj = new JSONObject();
		try {

			if (StringUtils.isEmpty(mobile) || !ParamValidator.isMobile(mobile)) {
				jsonObj.put("msg", "手机号输入不正确");
				return jsonObj;
			}

			String type = "1";
			String random = CommonUtils.rands(6);
			DefaultConsoleSession.getInstance().setAttribute(req, SmsConstants.SESSION_MESSAGE_CODE, random);

			String randoms = getSmsMsg(random, SmsConstants.BIZ_UNBIND_BANKCARD);
			System.out.println(randoms);
			sendMessage.send(randoms, null, type, mobile);
			System.out.println("手机验证码：" + random);

			jsonObj.put(ConsoleConstants.ERROR_CODE, ConsoleConstants.ERROR_CODE_000);
			return jsonObj;
		} catch (Exception e) {
			jsonObj.put("msg", "系统错误");
			return jsonObj;
		}
	}

	private String getSmsMsg(String random, String smsBiz) {
		SmsConfig smsCfg = SmsConfig.getInstance();
		smsCfg.load();
		String smsTemplate = smsCfg.getString(smsBiz);
		String randoms = smsTemplate.replace("#MSGCODE#", random);
		return randoms;
	}

}