package com.phzc.profile.web.controller;

import javax.servlet.http.HttpServletRequest;

import com.phzc.console.base.exception.ErrorCode;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.console.base.remote.WebServiceClientFactory;
import com.phzc.console.base.session.UserSessionFacade;
import com.phzc.console.sms.SendMessage;
import com.phzc.console.sms.SmsConfig;
import com.phzc.console.util.ConsolePropertiesManager;
import com.phzc.profile.biz.service.UserAuthorizeService;
import com.phzc.profile.web.config.ApplicationPropertyConfig;
import com.phzc.profile.web.form.ModifyPwdForm;
import com.phzc.ubs.common.facade.model.IdInfoRequest;
import com.phzc.ubs.common.facade.model.IdInfoResult;
import com.phzc.ubs.common.facade.model.ModifyLoginPwdRequest;
import com.phzc.ubs.common.facade.model.ModifyTransPwdRequest;
import com.phzc.ubs.common.facade.model.PwdInfoResult;
import com.phzc.ubs.common.facade.model.SetTransPwdRequest;
import com.phzc.ubs.common.facade.model.UserInfoDto;
import com.phzc.ubs.common.facade.model.ValidTransPwdRequest;
import com.phzc.ubs.common.facade.service.PwdInfoFacade;
import com.phzc.ubs.common.facade.service.UserInfoFacade;

/**
 * Created by CAIMIN on 2015/7/24.
 */

@Controller
@RequestMapping("/modifyPwd/*")
public class ModifyPwdController  {
	private static final Logger logger = LoggerFactory.getLogger(ModifyPwdController.class);

	@Autowired
	private WebServiceClientFactory serviceClientFactory;

	@Autowired
	protected ApplicationPropertyConfig applicationPropertyConfig;

	@Autowired
	private UserSessionFacade userSessionFacade;

	@Autowired
	private UserAuthorizeService userAuthorizeService;

	// UBS安全码
	private String ubsConsumerId = ConsolePropertiesManager.getUbsConsumerCode();
	
	@Autowired
	private SendMessage sendMessage;
	
	// 修改密码页面
	@RequestMapping(value = "modifyPwd.do", method = RequestMethod.GET)
	public String modifyPwd(HttpServletRequest req, Model model) throws ServiceException {

		User user = userSessionFacade.getUser(req);

		model.addAttribute("config", applicationPropertyConfig);
		String custId = user.getCustId();

		UserInfoFacade ubsUserService = (UserInfoFacade) serviceClientFactory.getWebServiceClient(UserInfoFacade.class, "userInfoFacade", "UBS");

		IdInfoRequest usrBaseReq = new IdInfoRequest();
		usrBaseReq.setCustId(custId);
		usrBaseReq.setConsumerId(ubsConsumerId);
		IdInfoResult usrBaseRslt = ubsUserService.queryUserByCustId(usrBaseReq);
		UserInfoDto usrBaseInfo = null;
		if (null != usrBaseRslt) {
			usrBaseInfo = usrBaseRslt.getList().get(0);
		}
		
		if(usrBaseInfo.getIdChkStat().equals("S")){
			// 通过实名认证
			model.addAttribute("chkStat", "1");
		}else{
			// 没有通过实名认证
			model.addAttribute("chkStat", "0");
		}
		
		if (usrBaseInfo.getIsTransPwdSet().equals("0")) {
			// 没有设置过交易密码
			model.addAttribute("isTransPwdSet", "0");
		} else {
			// 设置过交易密码
			model.addAttribute("isTransPwdSet", "1");
		}

		return "pwd/modifyPwd.ftl";
	}

	// 修改登录密码
	@RequestMapping(value = "modifyLoginPwd.do", method = RequestMethod.POST)
	public @ResponseBody Object modifyLoginPwd(HttpServletRequest req, Model model, ModifyPwdForm modForm) throws ServiceException {

		User user = userSessionFacade.getUser(req);

		model.addAttribute("config", applicationPropertyConfig);
		JSONObject jsonObject = new JSONObject();
		String custId = user.getCustId();
		String operId = user.getOperId();

		String orgLoginPwd = req.getParameter("orgLoginPwd");// 原登录密码

		String newLoginPwd = req.getParameter("newLoginPwd");// 新登录密码

		if (operId == null) {// 判断传入参数operId是否为空
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString("操作员Id不能为空");
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;
		}

		if (StringUtils.isBlank(orgLoginPwd)) {// 判断原密码是否为空
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString("原登录密码不能为空");
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;
		}
		if (StringUtils.isBlank(newLoginPwd)) {// 判断新密码是否为空
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString("请设置新的登录密码");
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;
		}
		if (StringUtils.isBlank(operId)) {
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString("操作员Id不能为空");
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;
		}

		// 调用facade修改登录密码接口
		PwdInfoFacade pwdInfoFacade = (PwdInfoFacade) serviceClientFactory.getWebServiceClient(PwdInfoFacade.class, "pwdInfoFacade", "UBS");

		ModifyLoginPwdRequest modLoginPwdReq = new ModifyLoginPwdRequest();
		modLoginPwdReq.setCustId(custId);
		modLoginPwdReq.setConsumerId(ubsConsumerId);
		modLoginPwdReq.setOrgLoginPwd(orgLoginPwd);
		modLoginPwdReq.setNewLoginPwd(newLoginPwd);
		modLoginPwdReq.setOperId(operId);

		// 返回参数
		PwdInfoResult pwdResult = pwdInfoFacade.modifyLoginPwd(modLoginPwdReq);

		if (pwdResult == null || !"000".equals(pwdResult.getRespCode())) {
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString(pwdResult.getRespDesc());
			logger.info("修改登录密码失败： " + pwdResult.getRespCode());
			logger.info("修改登录密码失败： " + pwdResult.getRespDesc());
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;

		} else {
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", true);
			
			//修改成功发送短信
			String phoneNo = user.getMobileNo();
            String smsMsg = getSmsMsg("MODIFY_LOGIN_PWD_SUC");
            logger.info("手机号： " + phoneNo + "，短信信息： " + smsMsg);
            sendMessage.send(smsMsg, null, "1", phoneNo);
			
			
			return jsonObject;
		}
	}

	// 修改交易密码
	@RequestMapping(value = "modifyTransPwd.do", method = RequestMethod.POST)
	public @ResponseBody Object modifyTransPwd(HttpServletRequest req, Model model, ModifyPwdForm modForm) throws ServiceException {

		User user = userSessionFacade.getUser(req);

		model.addAttribute("config", applicationPropertyConfig);

		JSONObject jsonObject = new JSONObject();
		String custId = user.getCustId();
		String operId = user.getOperId();

		String orgTransPwd = req.getParameter("orgTransPwd");// 原交易密码

		String newTransPwd = req.getParameter("newTransPwd");// 新交易密码

		if (operId == null) {// 判断传入参数operId是否为空
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString("操作员Id不能为空");
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;
		}

		if (StringUtils.isBlank(orgTransPwd)) {// 判断原密码是否为空
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString("原交易密码不能为空");
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;
		}
		if (StringUtils.isBlank(newTransPwd)) {// 判断新密码是否为空
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString("请设置新的交易密码");
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;
		}
		if (StringUtils.isBlank(operId)) {
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString("操作员Id不能为空");
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;
		}

		// 调用facade修改交易密码接口
		PwdInfoFacade pwdInfoFacade = (PwdInfoFacade) serviceClientFactory.getWebServiceClient(PwdInfoFacade.class, "pwdInfoFacade", "UBS");

		ModifyTransPwdRequest modTransPwdReq = new ModifyTransPwdRequest();
		modTransPwdReq.setCustId(custId);
		modTransPwdReq.setConsumerId(ubsConsumerId);
		modTransPwdReq.setOrgTransPwd(orgTransPwd);
		modTransPwdReq.setNewTransPwd(newTransPwd);
		modTransPwdReq.setOperId(operId);

		// 返回参数
		PwdInfoResult pwdResult = pwdInfoFacade.modifyTransPwd(modTransPwdReq);

		if (pwdResult == null || !"000".equals(pwdResult.getRespCode())) {
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString(pwdResult.getRespDesc());
			logger.info("修改交易密码失败： " + pwdResult.getRespCode());
			logger.info("修改交易密码失败： " + pwdResult.getRespDesc());
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;

		} else {
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", true);
			return jsonObject;
		}
	}

	// 设置交易密码
	@RequestMapping(value = "setTransPwd.do", method = RequestMethod.POST)
	public @ResponseBody Object setTransPwd(HttpServletRequest req, Model model, ModifyPwdForm modForm) throws ServiceException {

		User user = userSessionFacade.getUser(req);

		model.addAttribute("config", applicationPropertyConfig);

		JSONObject jsonObject = new JSONObject();
		String custId = user.getCustId();
		String operId = user.getOperId();

		String firstTransPwd = req.getParameter("firstTransPwd");// 新交易密码

		if (operId == null) {// 判断传入参数operId是否为空
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString("操作员Id不能为空");
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;
		}

		if (StringUtils.isBlank(firstTransPwd)) {// 判断新密码是否为空
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString("请设置新的交易密码");
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;
		}
		if (StringUtils.isBlank(operId)) {
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString("操作员Id不能为空");
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;
		}

		// 调用facade设置交易密码接口
		PwdInfoFacade pwdInfoFacade = (PwdInfoFacade) serviceClientFactory.getWebServiceClient(PwdInfoFacade.class, "pwdInfoFacade", "UBS");

		SetTransPwdRequest setTransPwdReq = new SetTransPwdRequest();
		setTransPwdReq.setCustId(custId);
		setTransPwdReq.setConsumerId(ubsConsumerId);
		setTransPwdReq.setNewTransPwd(firstTransPwd);
		setTransPwdReq.setOperId(operId);

		// 返回参数
		PwdInfoResult pwdResult = pwdInfoFacade.setTransPwd(setTransPwdReq);

		if (pwdResult == null || !"000".equals(pwdResult.getRespCode())) {
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString(pwdResult.getRespDesc());
			logger.info("设置交易密码失败： " + pwdResult.getRespCode());
			logger.info("设置交易密码失败： " + pwdResult.getRespDesc());
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;

		} else {
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", true);
			return jsonObject;
		}
	}

	// 异步验证交易密码
	@RequestMapping(value = "validTransPwd.do", method = RequestMethod.POST)
	public @ResponseBody Object validTransPwd(HttpServletRequest req, Model model, ModifyPwdForm modForm) throws ServiceException {

		User user = userSessionFacade.getUser(req);

		model.addAttribute("config", applicationPropertyConfig);

		JSONObject jsonObject = new JSONObject();
		String custId = user.getCustId();
		String operId = user.getOperId();

		String orgTransPwd = req.getParameter("orgTransPwd");// 原交易密码

		// 调用facade设置交易密码接口
		PwdInfoFacade pwdInfoFacade = (PwdInfoFacade) serviceClientFactory.getWebServiceClient(PwdInfoFacade.class, "pwdInfoFacade", "UBS");

		ValidTransPwdRequest validTransPwdReq = new ValidTransPwdRequest();
		validTransPwdReq.setCustId(custId);
		validTransPwdReq.setConsumerId(ubsConsumerId);
		validTransPwdReq.setTransPwd(orgTransPwd);
		validTransPwdReq.setOperId(operId);

		// 返回参数
		PwdInfoResult pwdResult = pwdInfoFacade.validTransPwd(validTransPwdReq);

		if (pwdResult == null || !"000".equals(pwdResult.getRespCode())) {
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString(pwdResult.getRespDesc());
			logger.info("验证交易密码失败： " + pwdResult.getRespCode());
			logger.info("验证交易密码失败： " + pwdResult.getRespDesc());
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;

		} else {
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", true);
			return jsonObject;
		}
	}
	
	private String getSmsMsg(String smsBiz) {
		SmsConfig smsCfg = SmsConfig.getInstance();
		smsCfg.load();
		String smsTemplate = smsCfg.getString(smsBiz);
		return smsTemplate;
	}
	

}