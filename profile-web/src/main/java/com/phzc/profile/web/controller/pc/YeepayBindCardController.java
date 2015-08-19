package com.phzc.profile.web.controller.pc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.phzc.profile.web.controller.BaseYeepayController;
import com.phzc.profile.web.form.BankCardBindForm;

@Controller
@RequestMapping("/yeepay/*")
public class YeepayBindCardController extends BaseYeepayController {
	
	@RequestMapping(value = "bindStepOne.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject bindStepOne(BankCardBindForm cardForm, 
			HttpServletRequest request, HttpServletResponse response) {
		return super.bindStepOne(cardForm, request, response);
	}
	
	@RequestMapping(value = "bindStepTwo.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject bindStepTwo(@RequestParam String certStatus, @RequestParam String smsVerifyCode, 
			@RequestParam(required = false) String transPwd, @RequestParam(required = false) String confirmTransPwd,  
			HttpServletRequest request, HttpServletResponse response) {
		return super.bindStepTwo(certStatus, smsVerifyCode, transPwd, confirmTransPwd, request, response);
	}

}