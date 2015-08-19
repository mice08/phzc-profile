package com.phzc.profile.web.services;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.phzc.console.ConsoleConstants;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.session.impl.DefaultConsoleSession;
import com.phzc.console.util.CommonUtils;
import com.phzc.profile.web.constants.ProfileConstants;

/**
 * 
 * @author YOUGUOQIANG225
 *
 */
public class YeePayChannelClient extends PayChannelClient {
	
	private  Logger log = LoggerFactory.getLogger(getClass());
	

	public YeePayChannelClient(String from) {
		super();
		setSysId(from);
	}
	
	@Override
	String getPayChannel() {
		return ProfileConstants.YEE_PAY_CHANNEL;
	}

	@Override
	String getPgRetUrl() {
		return "";
	}

	@Override
	void setSmsSeqId(HttpServletRequest req, Map paramMap) {}
	
	@Override
	void setSmsCode(HttpServletRequest req, Map paramMap) {}
	
	@Override
	String getSmsSeqId(Map paramMap) {
		return "";
	}
	
	@Override
	String getSmsCode(Map paramMap){
		return "";
	}

	@Override
	public JSONObject sendSmsForRecharge(HttpServletRequest req)
			throws ServiceException, IOException {
		JSONObject jsonObj = new JSONObject();
		String type = "1";// 1 公共短信服务 
		String smsCode = CommonUtils.rands(6);
		DefaultConsoleSession.getInstance().setAttribute(req, ProfileConstants.RECHARGE_SMS_CODE, smsCode);
		//SmsConstants.CASH_WITHDRAWAL; 
		String randoms = getSmsMsg("RECHARGE", smsCode, getUser(req).getLoginId(), req.getParameter("transAmt"));//TODO SmsConstants.CASH_WITHDRAWAL
		log.info(randoms);
		sendMessage.send(randoms, null, type, getMobileNo(req));
		log.info("手机验证码：" + smsCode);
		jsonObj.put(ConsoleConstants.ERROR_CODE, ConsoleConstants.ERROR_CODE_000);
		return jsonObj;
	}

}
