package com.phzc.profile.web.controller;

import java.util.ArrayList;
import java.util.List;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.phzc.console.base.pojo.User;
import com.phzc.profile.web.constants.ProfileConstants;
import com.phzc.profile.web.dto.RechargeRecordDTO;
import com.phzc.profile.web.dto.RechargeResponseDTO;
import com.phzc.profile.web.services.YeePayChannelClient;
import com.phzc.ubs.common.facade.model.AccountInfoDto;
import com.phzc.ubs.common.facade.model.CashLogDto;
import com.phzc.ubs.common.facade.model.WithdrawCashResult;

@Controller
@RequestMapping("/account/*")
public class RechargeAndDrawCashController extends AccountBaseController {
	private  Logger log = LoggerFactory.getLogger(getClass());

	
	@RequestMapping(value = "account.do", method = RequestMethod.GET)
	public  String getAccount(HttpServletRequest request , Model model) throws Exception {
		model.addAttribute("config", applicationPropertyConfig);

		payChannelClient = getInstance(request,ProfileConstants.FORM_PC);
		if(null == payChannelClient) {
			return "redirect:" + applicationPropertyConfig.getDomainProfile()+"/card/bindForm.do";
		}
		initServices();
		
		boolean isBinded =  payChannelClient.isBindedBankCard(request);
		if(!isBinded) {
			log.info("银行卡暂未绑定！");
			String baseUrl = applicationPropertyConfig.getDomainProfile();
			log.info(baseUrl);
			return "redirect:" + applicationPropertyConfig.getDomainProfile()+"/card/bindForm.do";//go to bind card page
		}
		
		//查询账户信息
		AccountInfoDto accountInfo = payChannelClient.getAccountInfo(request);
		request.setAttribute("acctInfo", accountInfo);
		

		//设置日期
		setDate(request);
		
		if(!StringUtils.isBlank(request.getParameter("action"))) {
			if(request.getParameter("action").equalsIgnoreCase("rechargeRecord")) {
				String result  = payChannelClient.getRechargeRecord(request);
				request.setAttribute("rechargeRecord", result);
			} else if(request.getParameter("action").equalsIgnoreCase("cashRecord")) {
				List<CashLogDto> cashRecord = getCashingRecordRequest(request);
				String cashResult = JSON.toJSONString(cashRecord);
				log.info(cashResult);
				request.setAttribute("cashRecord", cashResult);
			}
		}
		
		return "account/account.ftl";
	}
	
	
	@RequestMapping(value = "rechargeRecord.do", method = RequestMethod.GET)
	public  @ResponseBody RechargeRecordDTO getRechargeRecordRequest(HttpServletRequest request) throws Exception {
		
		payChannelClient = getInstance(request,ProfileConstants.FORM_PC);
		initServices();
		
		//设置日期
		setDate(request);
		
		//查询充值记录
		String result = payChannelClient.getRechargeRecord(request);
		log.info(result);

		RechargeRecordDTO rechargeRecordDTO = null;
		if(result != null) {
			rechargeRecordDTO = JSON.parseObject(result,RechargeRecordDTO.class);
			if(!ProfileConstants.RESPONSE_STATUS_SUCCESS.equals(rechargeRecordDTO.getRespCode())) {
				log.info("查询充值记录失败！ ");
			}
			int count = Integer.parseInt(rechargeRecordDTO.getTotalRecord());
			if(count > 0 && rechargeRecordDTO.getRechargeInfoList().size() > 0) {
				return rechargeRecordDTO;
			}
		} else {
			log.info("未查询到充值记录！");
		}
		return rechargeRecordDTO; //分页时使用
	}
	
	@RequestMapping(value = "cashRecord.do", method = RequestMethod.GET)
	public @ResponseBody List<CashLogDto> getCashingRecordRequest(HttpServletRequest request) throws Exception {
		payChannelClient = getInstance(request,ProfileConstants.FORM_PC);
		initServices();
		
		//设置日期
		setDate(request);
		
		//查询取现记录
		List<CashLogDto> cashingRecord = payChannelClient.getCashingRecord(request, get_startDate(), get_endDate());
		if(cashingRecord == null)
			cashingRecord = new ArrayList<CashLogDto>();
		return cashingRecord;
	}
	
	
	@RequestMapping(value = "initRecharge.do", method = RequestMethod.GET)
	public  String initRecharge(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception {
		model.addAttribute("config", applicationPropertyConfig);
		
		payChannelClient = getInstance(request,ProfileConstants.FORM_PC);
		if(null == payChannelClient) {
			return "redirect:" + applicationPropertyConfig.getDomainProfile()+"/card/bindForm.do";
		}
		initServices();
		
		//初始化银行卡信息
		payChannelClient.initBandCardInfo(request);
		
		return "account/recharge.ftl";
	}
	
	private  User getUser(HttpServletRequest request) {
		return userSessionFacade.getUser(request);
	}
	
	@RequestMapping(value = "initCash.do", method = RequestMethod.GET)
	public  String initCash(HttpServletRequest request,HttpServletResponse response,Model model) throws Exception {
		model.addAttribute("config", applicationPropertyConfig);
		
		payChannelClient = getInstance(request,ProfileConstants.FORM_PC);
		if(null == payChannelClient) {
			return "redirect:" + applicationPropertyConfig.getDomainProfile()+"/card/bindForm.do";
		}
		initServices();
		
		//初始化银行卡信息
		payChannelClient.initBandCardInfo(request);
		
		//查询账户信息, 需提供余额信息
		AccountInfoDto accountInfo = payChannelClient.getAccountInfo(request);
		request.setAttribute("acctInfo", accountInfo);
		return "account/cashing.ftl";
	}
	
	
	@RequestMapping(value = "sendSmsForCashing.do", method = RequestMethod.GET)
	public @ResponseBody JSONObject sendSmsForCashing(@RequestParam String mobileNo, HttpServletRequest req) throws Exception{
		payChannelClient = getInstance(req,ProfileConstants.FORM_PC);
		initServices();
		
		return payChannelClient.sendSmsForCashing(req);
	}
	
	
	
	@RequestMapping(value = "sendSmsForRecharge.do", method = RequestMethod.GET)
	public @ResponseBody JSONObject sendSmsForRecharge(HttpServletRequest request) throws Exception {
		payChannelClient.sendSmsForRecharge(request);
		return new JSONObject();
	}
	
	@RequestMapping(value = "recharge.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject rechargeRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		payChannelClient = getInstance(request,ProfileConstants.FORM_PC);
		initServices();
		
		User user = getUser(request);
		
		JSONObject jsonObj = new JSONObject();
		//校验短信验证码
		if(StringUtils.isBlank(request.getParameter("smsCode"))) {
			jsonObj.put("msg", "短信校验码不能为空！");
			jsonObj.put("result", false);
			jsonObj.put("responseDesc", "WrongOfSmsCode");
			return jsonObj;
		}else if(payChannelClient instanceof YeePayChannelClient &&  !request.getParameter("smsCode").equals(consoleSession.getAttribute(request, ProfileConstants.RECHARGE_SMS_CODE))) {
		    jsonObj.put("result", false);
			jsonObj.put("msg", "短信校验码不正确！");
			jsonObj.put("responseDesc", "smsCodeNotCorrect");
			return jsonObj;
		}
		
		//校验交易密码
		boolean isCorrectTradePswd = payChannelClient.isCorrectTradePassword(request, user);
		
		if(isCorrectTradePswd) {
			//调用充值服务
			RechargeResponseDTO rechargeResp = payChannelClient.recharge(request);
			if(rechargeResp != null)
				log.info("recharge responseCode："+rechargeResp.getRespCode()+"----msg:"+rechargeResp.getRespDesc());
			if(rechargeResp == null) {
				log.info("充值失败！");
			    jsonObj.put("msg", "充值失败，</br>请联系管理员或稍后再试！");
			    jsonObj.put("result", false);
			    jsonObj.put("responseDesc", "failed");
				return jsonObj;
			} else if(ProfileConstants.RESPONSE_STATUS_SUCCESS.equals(rechargeResp.getRespCode())) {
				jsonObj.put("msg", "恭喜您，充值成功！");
				jsonObj.put("responseDesc", "success");
			    jsonObj.put("result", true);
				return jsonObj;
			} else if(ProfileConstants.RESPONSE_STATUS_PROCESSING.equals(rechargeResp.getRespCode())) {
				jsonObj.put("msg", "银行交易处理中，请稍后查询！");
				jsonObj.put("responseDesc", "processing");
			    jsonObj.put("result", true);
			    return jsonObj;
			} else if(ProfileConstants.RESPONSE_STATUS_WRONG_SMS_CODE.equals(rechargeResp.getRespCode())){
		    	jsonObj.put("result", false);
				jsonObj.put("msg", "短信验证码不正确！");
				jsonObj.put("responseDesc", "wrongSmsCode");
				return jsonObj;
		    }else{
				jsonObj.put("msg", "充值失败，</br>请联系管理员或稍后再试！");
				jsonObj.put("responseDesc", "failed");
			    jsonObj.put("result", false);
			    return jsonObj;
		    }	
			
		} else {
			jsonObj.put("msg", "交易密码不匹配，请重新输入！");
			jsonObj.put("responseDesc", "wrongOfTransPwd");
		    jsonObj.put("result", false);
		    return jsonObj;
		}
		
	}
	
	@RequestMapping(value = "cashing.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject cashRequest(HttpServletRequest request,HttpServletResponse response) throws Exception {
		payChannelClient = getInstance(request,ProfileConstants.FORM_PC);
		initServices();
		
		User user = getUser(request);
		AccountInfoDto accountInfoDto = payChannelClient.getAccountInfo(request);//add in cache later TODO
		double avlBal = accountInfoDto == null ? 0.00 : Double.valueOf(accountInfoDto.getAvlBal());
		String avlBalReq = request.getParameter("amount");
		
		JSONObject jsonObj = new JSONObject();
		//校验提现金额
		if(Double.valueOf(avlBalReq) > avlBal){
			jsonObj.put("result", false);
			jsonObj.put("msg", "提现金额不能超过余额！");
			jsonObj.put("responseDesc", "moreThanBalance");
			return jsonObj;
			
        }
		//校验短信验证码
		if(StringUtils.isBlank(request.getParameter("smsCode").trim())){
		    jsonObj.put("result", false);
			jsonObj.put("msg", "短信校验码不能为空！");
			jsonObj.put("responseDesc", "smsCodeCanNotNull");
			return jsonObj;
		} else if(!request.getParameter("smsCode").equals(consoleSession.getAttribute(request, ProfileConstants.CASHING_SMS_CODE))) {
		    jsonObj.put("result", false);
			jsonObj.put("msg", "短信校验码不正确！");
			jsonObj.put("responseDesc", "smsCodeNotCorrect");
			return jsonObj;
		}
		
		
		//验证交易密码
		boolean isCorrectTradePwd = payChannelClient.isCorrectTradePassword(request, user);
		if(!isCorrectTradePwd) {
		    jsonObj.put("result", false);
			jsonObj.put("msg", "交易密码不匹配，请重新输入！");
			jsonObj.put("responseDesc", "tranPwdNotCorrect");
			return jsonObj;
		}
		
		//调用UBS账户取现服务
		WithdrawCashResult withdrawCashResult = payChannelClient.cashing(request, user, avlBalReq);
		if(withdrawCashResult!=null){
			log.info("cashing responseCode:"+withdrawCashResult.getRespCode()+"---msg:"+withdrawCashResult.getRespDesc());
		    if(ProfileConstants.RESPONSE_STATUS_SUCCESS.equals(withdrawCashResult.getRespCode())){
		        //获取当前时间+1天
		        //Calendar cal = Calendar.getInstance();
		        //cal.add(Calendar.DATE, 1); 款到达时间+1
		        jsonObj.put("result", true);
				jsonObj.put("msg", "恭喜您，提现成功！");
				jsonObj.put("responseDesc", "success");
				return jsonObj;
		    }else if("100".equals(withdrawCashResult.getRespCode())){                                  
                jsonObj.put("result", true);
				jsonObj.put("msg", "银行交易处理中，请稍后查询！");
				jsonObj.put("responseDesc", "processing");
				return jsonObj;
		    }else{
		        log.info("充值失败："+withdrawCashResult.getRespCode()+" "+withdrawCashResult.getRespDesc());
		        jsonObj.put("result", false);
				jsonObj.put("msg", "提现失败，</br>请联系管理员或稍后再试！");
				jsonObj.put("responseDesc", "failed");
				return jsonObj;
		    }			    
		}else{
			jsonObj.put("result", false);
			jsonObj.put("msg", "提现失败，</br>请联系管理员或稍后再试！");
			jsonObj.put("responseDesc", "failed");
			return jsonObj;
		}			
	}
	
}
