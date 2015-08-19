package com.phzc.profile.web.controller.m;

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

import com.alibaba.fastjson.JSONObject;
import com.phzc.console.ConsoleConstants;
import com.phzc.console.SmsConstants;
import com.phzc.console.base.exception.ErrorCode;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.console.base.remote.WebServiceClientFactory;
import com.phzc.console.base.session.UserSessionFacade;
import com.phzc.console.base.session.impl.DefaultConsoleSession;
import com.phzc.console.sms.SendMessage;
import com.phzc.console.sms.SmsConfig;
import com.phzc.console.util.CommonUtils;
import com.phzc.console.util.ConsolePropertiesManager;
import com.phzc.console.util.ParamValidator;
import com.phzc.profile.biz.service.UserAuthorizeService;
import com.phzc.profile.web.config.ApplicationPropertyConfig;
import com.phzc.profile.web.controller.BaseController;
import com.phzc.profile.web.form.ModifyPwdForm;
import com.phzc.profile.web.utils.UserInfoUtils;
import com.phzc.ubs.common.facade.model.IdInfoRequest;
import com.phzc.ubs.common.facade.model.IdInfoResult;
import com.phzc.ubs.common.facade.model.ModifyLoginPwdRequest;
import com.phzc.ubs.common.facade.model.ModifyTransPwdRequest;
import com.phzc.ubs.common.facade.model.MpInfoRequest;
import com.phzc.ubs.common.facade.model.MpInfoResult;
import com.phzc.ubs.common.facade.model.PwdInfoResult;
import com.phzc.ubs.common.facade.model.ResetTransPwdRequest;
import com.phzc.ubs.common.facade.model.UserInfo;
import com.phzc.ubs.common.facade.model.UserInfoDto;
import com.phzc.ubs.common.facade.service.PwdInfoFacade;
import com.phzc.ubs.common.facade.service.UserInfoFacade;

/**
 * Created by CAIMIN on 2015/8/7.
 */

@Controller
@RequestMapping("/m/modifyPwd/*")
public class MModifyPwdController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(MModifyPwdController.class);

	@Autowired
	private UserAuthorizeService userAuthorizeService;

	@Autowired
	private SendMessage sendMessage;

	// 修改密码初始化页面
	@RequestMapping(value = "modifyPwdInit.do", method = RequestMethod.GET)
	public String modifyPwdInit(HttpServletRequest req, Model model) throws ServiceException {
		model.addAttribute("config", applicationPropertyConfig);
		
		User user = userSessionFacade.getUser(req);
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
		
		if (usrBaseInfo.getIsTransPwdSet().equals("0")) {
			// 没有设置过交易密码
			model.addAttribute("isTransPwdSet", "0");
		} else {
			// 设置过交易密码
			model.addAttribute("isTransPwdSet", "1");
		}
		
		return "pwd/m/modifyPwdInit_m.ftl";
	}

	// 跳转到具体修改页面
	@RequestMapping(value = "modifyPwd.do", method = RequestMethod.GET)
	public String modifyPwd(HttpServletRequest req, Model model, String modifyType) throws ServiceException {
		model.addAttribute("config", applicationPropertyConfig);
		model.addAttribute("modifyType", modifyType);
		return "pwd/m/modifyPwd_m.ftl";
	}

	// 修改密码成功跳转
	@RequestMapping(value = "modifySuccess.do", method = RequestMethod.GET)
	public String modifySuccess(HttpServletRequest req, Model model, String modifyType) throws ServiceException {
		model.addAttribute("config", applicationPropertyConfig);
		model.addAttribute("modifyType", modifyType);
		return "pwd/m/modifyPwdSuccess_m.ftl";
	}

	// 跳转到重置交易密码验证身份页面
	@RequestMapping(value = "resetVerify.do", method = RequestMethod.GET)
	public String resetVerify(HttpServletRequest req, Model model, String modifyType) throws ServiceException {
		model.addAttribute("config", applicationPropertyConfig);

		User user = userSessionFacade.getUser(req);
		String custId = user.getCustId();
		String operId = user.getOperId();
		UserInfoDto userInfoDto = queryUserInfoByCustIdFromUbs(custId, operId);
		logger.info("changePwdOne user mp: " + userInfoDto.getUsrMp());

		model.addAttribute("phoneNo", UserInfoUtils.confuseUsrMp(userInfoDto.getUsrMp()));

		return "pwd/m/resetVerifyForm_m.ftl";
	}

	// 跳转到重置交易密码页面
	@RequestMapping(value = "resetPwdForm.do", method = RequestMethod.GET)
	public String resetPwdForm(Model model) {
		model.addAttribute("config", applicationPropertyConfig);
		return "pwd/m/resetPwdForm_m.ftl";
	}

	// 重置交易密码成功跳转
	@RequestMapping(value = "resetSuccess.do", method = RequestMethod.GET)
	public String resetSuccess(HttpServletRequest req, Model model, String modifyType) throws ServiceException {
		model.addAttribute("config", applicationPropertyConfig);
		return "pwd/m/resetPwdSuccess_m.ftl";
	}

	// 修改密码(登录或交易)
	@RequestMapping(value = "changePwd.do", method = RequestMethod.POST)
	public @ResponseBody Object changePwd(HttpServletRequest req, Model model, ModifyPwdForm modForm) throws ServiceException {

		User user = userSessionFacade.getUser(req);
		model.addAttribute("config", applicationPropertyConfig);
		JSONObject jsonObject = new JSONObject();
		String custId = user.getCustId();
		String operId = user.getOperId();

		String modifyType = req.getParameter("modifyType");// 修改类型
		String orgPwd = req.getParameter("orgPwd");// 原密码
		String newPwd = req.getParameter("newPwd");// 新密码

		if (StringUtils.isBlank(operId)) {// 判断传入参数operId是否为空
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString("操作员Id不能为空");
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;
		}

		if (StringUtils.isBlank(orgPwd)) {// 判断原密码是否为空
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString("原密码不能为空");
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;
		}
		if (StringUtils.isBlank(newPwd)) {// 判断新密码是否为空
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString("请设置新密码");
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;
		}

		PwdInfoResult pwdResult = null;

		if (modifyType.equals("login")) {
			ModifyLoginPwdRequest modLoginPwdReq = new ModifyLoginPwdRequest();
			modLoginPwdReq.setCustId(custId);
			modLoginPwdReq.setConsumerId(ubsConsumerId);
			modLoginPwdReq.setOrgLoginPwd(orgPwd);
			modLoginPwdReq.setNewLoginPwd(newPwd);
			modLoginPwdReq.setOperId(operId);
			pwdResult = modifyLoginPwd(modLoginPwdReq);
		} else if (modifyType.equals("trans")) {

			ModifyTransPwdRequest modTransPwdReq = new ModifyTransPwdRequest();
			modTransPwdReq.setCustId(custId);
			modTransPwdReq.setConsumerId(ubsConsumerId);
			modTransPwdReq.setOrgTransPwd(orgPwd);
			modTransPwdReq.setNewTransPwd(newPwd);
			modTransPwdReq.setOperId(operId);
			pwdResult = modifyTransPwd(modTransPwdReq);

		} else {
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString("修改类型出错");
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;
		}

		if (pwdResult == null || !"000".equals(pwdResult.getRespCode())) {
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString(pwdResult.getRespDesc());
			logger.info("修改密码失败： " + pwdResult.getRespCode());
			logger.info("修改密码失败： " + pwdResult.getRespDesc());
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;

		} else {
			jsonObject.put("modForm", modForm);
			jsonObject.put("modifyType", modifyType);
			jsonObject.put("success", true);
			
			if(modifyType.equals("login")){
				//修改登录密码成功发送短信
				String phoneNo = user.getMobileNo();
	            String smsMsg = getSmsMsg("MODIFY_LOGIN_PWD_SUC");
	            logger.info("手机号： " + phoneNo + "，短信信息： " + smsMsg);
	            sendMessage.send(smsMsg, null, "1", phoneNo);
			}
			
			return jsonObject;
		}
	}

	// 异步重置交易密码
	@RequestMapping(value = "resetTransPwd.do", method = RequestMethod.POST)
	public @ResponseBody Object resetTransPwd(HttpServletRequest req, Model model, ModifyPwdForm modForm) throws ServiceException {

		User user = userSessionFacade.getUser(req);

		model.addAttribute("config", applicationPropertyConfig);

		JSONObject jsonObject = new JSONObject();
		String custId = user.getCustId();
		String operId = user.getOperId();

		String newPwd = req.getParameter("newPwd");// 重置交易密码

		ResetTransPwdRequest resetTransPwdReq = new ResetTransPwdRequest();
		resetTransPwdReq.setCustId(custId);
		resetTransPwdReq.setConsumerId(ubsConsumerId);
		resetTransPwdReq.setNewTransPwd(newPwd);
		resetTransPwdReq.setOperId(operId);

		// 返回参数
		PwdInfoResult pwdResult = resetTransPwd(resetTransPwdReq);

		if (pwdResult == null || !"000".equals(pwdResult.getRespCode())) {
			modForm.setErrorCode(String.valueOf(ErrorCode.USER_INVALID_PASSWORD));
			modForm.setErrorString(pwdResult.getRespDesc());
			logger.info("重置交易密码失败： " + pwdResult.getRespCode());
			logger.info("重置交易密码失败： " + pwdResult.getRespDesc());
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", false);
			return jsonObject;

		} else {
			jsonObject.put("modForm", modForm);
			jsonObject.put("success", true);
			
			//重置成功发送短信
			String phoneNo = user.getMobileNo();
            String smsMsg = getSmsMsg("FIND_TRANS_PWD_SUC");
            
            //如果用户姓名不为空，用用户姓名替换登录id
    		if (!StringUtils.isEmpty(user.getNick())) {
    			smsMsg = smsMsg.replace("#LOGINID#", user.getNick());
    		}else{
    			smsMsg = smsMsg.replace("#LOGINID#", user.getLoginId());
    		}
            logger.info("手机号： " + phoneNo + "，短信信息： " + smsMsg);
            sendMessage.send(smsMsg, null, "1", phoneNo);
			
			return jsonObject;
		}
	}

	// 修改登录密码
	private PwdInfoResult modifyLoginPwd(ModifyLoginPwdRequest modLoginPwdReq) throws ServiceException {
		// 调用facade修改登录密码接口
		PwdInfoFacade pwdInfoFacade = (PwdInfoFacade) serviceClientFactory.getWebServiceClient(PwdInfoFacade.class, "pwdInfoFacade", "UBS");
		return pwdInfoFacade.modifyLoginPwd(modLoginPwdReq);
	}

	// 修改交易密码
	private PwdInfoResult modifyTransPwd(ModifyTransPwdRequest modTransPwdReq) throws ServiceException {
		// 调用facade修改交易密码接口
		PwdInfoFacade pwdInfoFacade = (PwdInfoFacade) serviceClientFactory.getWebServiceClient(PwdInfoFacade.class, "pwdInfoFacade", "UBS");
		return pwdInfoFacade.modifyTransPwd(modTransPwdReq);
	}

	// 重置交易密码
	private PwdInfoResult resetTransPwd(ResetTransPwdRequest resetTransPwdReq) throws ServiceException {
		// 调用facade重置交易接口
		PwdInfoFacade pwdInfoFacade = (PwdInfoFacade) serviceClientFactory.getWebServiceClient(PwdInfoFacade.class, "pwdInfoFacade", "UBS");
		return pwdInfoFacade.resetTransPwd(resetTransPwdReq);
	}

	// 发送短信
	@RequestMapping(value = "sendForReset.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject sendForReset(@RequestParam String phoneNo, HttpServletRequest req) {
		JSONObject jsonObj = new JSONObject();

		User user = null;
		user = userSessionFacade.getUser(req);
		if (null != user) {
			phoneNo = user.getMobileNo();
		}

		try {
			if (StringUtils.isEmpty(phoneNo) || !ParamValidator.isMobile(phoneNo)) {
				jsonObj.put("message", "手机号输入不正确！");
				jsonObj.put("success", false);
				return jsonObj;
			}

			String random = CommonUtils.rands(6);
			DefaultConsoleSession.getInstance().setAttribute(req, SmsConstants.SESSION_MESSAGE_CODE, random);

			String smsMsg = getSmsMsg(random, "FIND_TRANS_PWD");

			// 如果用户姓名不为空，用用户姓名替换登录id
			if (!StringUtils.isEmpty(user.getNick())) {
				smsMsg = smsMsg.replace("#LOGINID#", user.getNick());
			} else {
				smsMsg = smsMsg.replace("#LOGINID#", user.getLoginId());
			}

			logger.info("手机号： " + phoneNo + "，验证信息： " + smsMsg + "，验证码: " + random);
			boolean success = sendMessage.send(smsMsg, null, "1", phoneNo);
			if (success) {
				jsonObj.put("message", "验证码发送成功！");
				jsonObj.put("success", true);
			} else {
				jsonObj.put("message", "验证码发送失败，请重试！");
				jsonObj.put("success", false);
			}
			return jsonObj;
		} catch (Exception e) {
			jsonObj.put("msg", "系统错误，请稍后再试！");
			jsonObj.put("success", false);
			return jsonObj;
		}
	}

	// 异步验证验证码
	@RequestMapping(value = "verifySmsCode.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject verifySmsCode(@RequestParam String mobile, @RequestParam String smsVerifyCode, HttpServletRequest request, HttpServletResponse response) {
		JSONObject jsonObj = new JSONObject();

		User user = null;
		user = userSessionFacade.getUser(request);
		if (null != user) {
			mobile = user.getMobileNo();
		}

		try {
			checkVerifyParams(mobile, smsVerifyCode, request);
		} catch (ServiceException e) {
			jsonObj.put("success", false);
			jsonObj.put("msg", e.getMessage());
			return jsonObj;
		}

		UserInfoFacade ubsUserInfoService = null;
		try {
			ubsUserInfoService = (UserInfoFacade) serviceClientFactory.getWebServiceClient(UserInfoFacade.class, "userInfoFacade", "UBS");
		} catch (ServiceException e) {
			jsonObj.put("success", false);
			jsonObj.put("msg", "系统错误");
			return jsonObj;
		}

		MpInfoRequest mpInfoRequest = new MpInfoRequest();
		mpInfoRequest.setConsumerId(ubsConsumerId);
		mpInfoRequest.setUsrMp(mobile);
		MpInfoResult mpInfoResult = ubsUserInfoService.queryUserByUsrMp(mpInfoRequest);
		if (null == mpInfoResult || !"000".equals(mpInfoResult.getRespCode())) {
			jsonObj.put("success", false);
			jsonObj.put("msg", (null == mpInfoResult) ? "用户查询系统错误" : mpInfoResult.getRespDesc());
			return jsonObj;
		}

		buildUserAndSetToSession(request, mpInfoResult);

		jsonObj.put("success", true);
		return jsonObj;
	}

	private String getSmsMsg(String random, String smsBiz) {
		SmsConfig smsCfg = SmsConfig.getInstance();
		smsCfg.load();
		String smsTemplate = smsCfg.getString(smsBiz);
		String randoms = smsTemplate.replace("#MSGCODE#", random);
		return randoms;
	}

	protected void checkVerifyParams(String mobile, String smsVerifyCode, HttpServletRequest request) throws ServiceException {
		ParamValidator.checkMobile(mobile);

		if (StringUtils.isEmpty(smsVerifyCode)) {
			throw new ServiceException(ErrorCode.NULL_INPUT, "短信验证码为空");
		}

		String smsVerifyCodeInSession = consoleSession.getAttribute(request, SmsConstants.SESSION_MESSAGE_CODE);
		consoleSession.deleteAttribute(request, SmsConstants.SESSION_MESSAGE_CODE);
		if (!smsVerifyCode.equals(smsVerifyCodeInSession) && !ConsoleConstants.DEFAULT_SMS_VERIFY_CODE.equals(smsVerifyCode)) {
			throw new ServiceException(ErrorCode.USER_INVALID_CODE_NULL, "短信验证码错误");
		}
	}

	protected void buildUserAndSetToSession(HttpServletRequest request, MpInfoResult mpInfoResult) {
		UserInfo userInfo = mpInfoResult.getUserInfo();
		User user = new User();
		user.setCustId(userInfo.getCustId());
		user.setOperId(userInfo.getOperId());
		user.setMobileNo(userInfo.getUsrMp());
		user.setLoginId(userInfo.getLoginId());
		user.setUserName(userInfo.getNickName());
		user.setNick(userInfo.getNickName());
		consoleSession.setAttribute(request, "userInfoForResetPwd", user);
	}
	
	private String getSmsMsg(String smsBiz) {
		SmsConfig smsCfg = SmsConfig.getInstance();
		smsCfg.load();
		String smsTemplate = smsCfg.getString(smsBiz);
		return smsTemplate;
	}

}
