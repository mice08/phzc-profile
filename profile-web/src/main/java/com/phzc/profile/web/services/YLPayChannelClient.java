package com.phzc.profile.web.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.session.impl.DefaultConsoleSession;
import com.phzc.console.util.Md5Utils;
import com.phzc.profile.api.utils.HttpClientPost;
import com.phzc.profile.web.constants.ProfileConstants;
import com.phzc.profile.web.dto.SmsResponseForYLDTO;

/**
 * 
 * @author YOUGUOQIANG225
 *
 */
public class YLPayChannelClient extends PayChannelClient{

	private  Logger log = LoggerFactory.getLogger(getClass());
	
	
	public YLPayChannelClient(String from) {
		super();
		setSysId(from);
	}

	@Override
	String getPayChannel() {
		return ProfileConstants.YL_PAY_CHANNEL;
	}

	@Override
	String getPgRetUrl() {
		return "";
	}
	
	@Override
	void setSmsSeqId(HttpServletRequest req, Map paramMap) {
		String smsSeqId = DefaultConsoleSession.getInstance().getAttribute(req, ProfileConstants.SEESION_KEY_SMS_SEQ);
		smsSeqId = smsSeqId == null ? "" : smsSeqId;
		paramMap.put("smsSeqId", smsSeqId);//易联发送短信接口,返回流水号
	}
	
	@Override
	void setSmsCode(HttpServletRequest req, Map paramMap) {
		paramMap.put("smsCode", req.getParameter("smsCode"));
	}
	
	@Override
	String getSmsSeqId(Map paramMap) {
		String smsSeqId = paramMap.get("smsSeqId") == null ? "" : paramMap.get("smsSeqId").toString();
		return smsSeqId;
	}

	@Override
	String getSmsCode(Map paramMap) {
		String smsCode = paramMap.get("smsCode") == null ? "" : paramMap.get("smsCode").toString();
		return smsCode;
	}
	
	
	public JSONObject sendSmsForRecharge(HttpServletRequest request) throws ServiceException, IOException {
		String url = getUrlForSms(request);
		log.info(url);
		SmsResponseForYLDTO smsResponseForYLDTO = null;
		try {
			String result = HttpClientPost.doPost("", url);
			if(!StringUtils.isBlank(result)) {
				smsResponseForYLDTO = JSON.parseObject(result,SmsResponseForYLDTO.class);
				if(ProfileConstants.RESPONSE_STATUS_SUCCESS.equals(smsResponseForYLDTO.getRespCode())
						&& smsResponseForYLDTO.getSmsSeqId() != null) {
					log.info("易联充值获取短信流水号成功! seqId:"+smsResponseForYLDTO.getSmsSeqId());
					DefaultConsoleSession.getInstance().setAttribute(request, ProfileConstants.SEESION_KEY_SMS_SEQ, smsResponseForYLDTO.getSmsSeqId());
				} else {
					log.info("易联充值未获取到短信流水号!");
				}
			} else {
				log.info("充值未获取到短信流水号!");
			}
		} catch (ClientProtocolException e) {
			log.error("充值获取短信流水号失败："+e.getMessage());
		} catch (IOException e) {
			log.error("易联充值获取短信流水号失败："+e.getMessage());
		}
		return null;
	}
	
	
	String getUrlForSms(HttpServletRequest request) {
		Map paramMap = new HashMap();
		paramMap.put("merId", ProfileConstants.DEFAULT_MERID);
		paramMap.put("cmdId", ProfileConstants.CMDID_SEND_SMS);
		paramMap.put("version", request.getParameter("version") == null ? ProfileConstants.REQ_VERSION : request.getParameter("version"));
		paramMap.put("custId", request.getParameter("custId") == null ? getUser(request).getCustId() : request.getParameter("custId"));
		paramMap.put("cardSeqId", getCardSeqId(request));
		paramMap.put("mobileNo", getMobileNo(request));
		paramMap.put("chkValue", getMd5ChkValForSmsYL(paramMap));
		
		return getTradeForRechargeBaseUrl() + getHttpClientUtil().getUriByParms(paramMap);
	}

	private String getMd5ChkValForSmsYL(Map map) {
		//{merId+cmdId+version+merPriv+custId+cardSeqId+mobileNo+reqExt+merKey}
		String chkVal = map.get("merId").toString()
				+map.get("cmdId").toString()
				+map.get("version").toString()
				//no need "merPriv"
				+map.get("custId").toString()
				+map.get("cardSeqId").toString()
				+map.get("mobileNo").toString()
				+ProfileConstants.MER_KEY;
		
		try {
			chkVal = Md5Utils.md5(chkVal, "utf-8");
		} catch (NoSuchAlgorithmException e) {
			log.error("getMd5ChkValForSmsYL exception:"+e.getMessage());
		} catch (UnsupportedEncodingException e) {
			log.error("getMd5ChkValForSmsYL exception:"+e.getMessage());
		}
		return chkVal;
	}

}
