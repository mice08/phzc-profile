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
public class YeepayIFrameBindCardController extends BaseYeepayController {

	@RequestMapping(value = "iFrameBindStepOne.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject bindStepOne(BankCardBindForm cardForm, 
			HttpServletRequest request, HttpServletResponse response) {
		cardForm.setCertStatus(String.valueOf(CERTIFY_NO_CARD));
		return super.bindStepOne(cardForm, request, response);
	}

	@RequestMapping(value = "iFrameBindStepTwo.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject bindStepTwo(@RequestParam String smsVerifyCode, 
			HttpServletRequest request, HttpServletResponse response) {
		String certStatus = String.valueOf(CERTIFY_NO_CARD);
		return super.bindStepTwo(certStatus, smsVerifyCode, null, null, request, response);
	}

}