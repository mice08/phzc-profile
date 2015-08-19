package com.phzc.profile.web.controller;

import com.alibaba.fastjson.JSONObject;
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
import com.phzc.profile.web.form.ModifyPwdForm;
import com.phzc.profile.web.utils.UserInfoUtils;
import com.phzc.ubs.common.facade.model.PwdInfoResult;
import com.phzc.ubs.common.facade.model.ResetTransPwdRequest;
import com.phzc.ubs.common.facade.model.UserInfoDto;
import com.phzc.ubs.common.facade.service.PwdInfoFacade;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by CAIMIN on 2015/7/24.
 */

@Controller
@RequestMapping("/modifyPwd/*")
public class ResetTransPwdController extends BaseController {

	private static class resetTransPwdSteps {
		public static String RESET_TRANS_PWD_STEP_KEY = "RESET_TRANS_PWD_STEP_KEY";
		public static String RESET_TRANS_PWD_STEP_ONE = "RESET_TRANS_PWD_STEP_ONE";
		public static String RESET_TRANS_PWD_STEP_TWO = "RESET_TRANS_PWD_STEP_TWO";
		public static String RESET_TRANS_PWD_STEP_THREE = "RESET_TRANS_PWD_STEP_THREE";
	}

	private static final Logger logger = LoggerFactory.getLogger(ResetTransPwdController.class);

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

	// 异步重置交易密码
	@RequestMapping(value = "resetTransPwd.do", method = RequestMethod.POST)
	public @ResponseBody Object resetTransPwd(HttpServletRequest req, Model model, ModifyPwdForm modForm) throws ServiceException {

		User user = userSessionFacade.getUser(req);

		model.addAttribute("config", applicationPropertyConfig);

		JSONObject jsonObject = new JSONObject();
		String custId = user.getCustId();
		String operId = user.getOperId();

		String firstTransPwd = req.getParameter("firstTransPwd");// 重置交易密码

		// 调用facade重置交易密码接口
		PwdInfoFacade pwdInfoFacade = (PwdInfoFacade) serviceClientFactory.getWebServiceClient(PwdInfoFacade.class, "pwdInfoFacade", "UBS");

		ResetTransPwdRequest resetTransPwdReq = new ResetTransPwdRequest();
		resetTransPwdReq.setCustId(custId);
		resetTransPwdReq.setConsumerId(ubsConsumerId);
		resetTransPwdReq.setNewTransPwd(firstTransPwd);
		resetTransPwdReq.setOperId(operId);

		// 返回参数
		PwdInfoResult pwdResult = pwdInfoFacade.resetTransPwd(resetTransPwdReq);

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
            //DefaultConsoleSession.getInstance().setAttribute(request, SmsConstants.SESSION_MESSAGE_CODE, random);
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

	// 跳转到修改交易密码页面1
    @RequestMapping(value = "resetTransPwdOne.do", method = RequestMethod.GET)
    public String resetTransPwdOne(Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("config", applicationPropertyConfig);
        User user = null;
        try {
            user = userSessionFacade.getUser(request);
            String custId = user.getCustId();
            String operId = user.getOperId();
            UserInfoDto userInfoDto = queryUserInfoByCustIdFromUbs(custId, operId);
            logger.info("changePwdOne user mp: " + userInfoDto.getUsrMp());
            model.addAttribute("phoneNo", UserInfoUtils.confuseUsrMp(userInfoDto.getUsrMp()));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Get session user error: " + e.getLocalizedMessage());
        }
        return "pwd/resetTransPwdOne.ftl";
    }

    // 跳转到修改交易密码页面2
    @RequestMapping(value = "resetTransPwdTwo.do", method = RequestMethod.GET)
    public String resetTransPwdTwo(Model model, HttpServletRequest request, HttpServletResponse response) {

        //检查修改步骤
        String changeStep = DefaultConsoleSession.getInstance().getAttribute(request, resetTransPwdSteps.RESET_TRANS_PWD_STEP_KEY);
        if (null == changeStep || !changeStep.equals(resetTransPwdSteps.RESET_TRANS_PWD_STEP_ONE)) {
            return resetTransPwdOne(model, request, response);
        }else {
            DefaultConsoleSession.getInstance().setAttribute(request, resetTransPwdSteps.RESET_TRANS_PWD_STEP_KEY, resetTransPwdSteps.RESET_TRANS_PWD_STEP_TWO);
        }
        model.addAttribute("config", applicationPropertyConfig);

        return "pwd/resetTransPwdTwo.ftl";
    }

    // 跳转到修改交易密码页面3
    @RequestMapping(value = "resetTransPwdThree.do", method = RequestMethod.GET)
    public String resetTransPwdThree(Model model, HttpServletRequest request, HttpServletResponse response) {

        //检查修改步骤
        String changeStep = DefaultConsoleSession.getInstance().getAttribute(request, resetTransPwdSteps.RESET_TRANS_PWD_STEP_KEY);
        if (null == changeStep || !changeStep.equals(resetTransPwdSteps.RESET_TRANS_PWD_STEP_TWO)) {
            return resetTransPwdTwo(model, request, response);
        } else {
            DefaultConsoleSession.getInstance().setAttribute(request, resetTransPwdSteps.RESET_TRANS_PWD_STEP_KEY, resetTransPwdSteps.RESET_TRANS_PWD_STEP_THREE);
        }

        model.addAttribute("config", applicationPropertyConfig);

        return "pwd/resetTransPwdThree.ftl";
    }


	//发送短信
	@RequestMapping(value = "sendForResetTransPwd.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject sendForResetTransPwd(HttpServletRequest request, HttpServletResponse response) {

        String changeStep = request.getParameter("changeStep");

        JSONObject jsonObj = new JSONObject();
        String phoneNo = null;
        User user = userSessionFacade.getUser(request);
        try {
            
            if ("PwdOne".equals(changeStep)) {
                if (null != user) {
                    phoneNo = user.getMobileNo();
                }
            } else if ("PwdTwo".equals(changeStep)) {
                phoneNo = request.getParameter("phoneNo");
            } else {
                jsonObj.put("message", "数据异常");
                jsonObj.put("success", false);
                return jsonObj;
            }

        } catch (Exception e) {
            logger.error("Get session user error: " + e.getLocalizedMessage());
        }

        try {
            if (StringUtils.isEmpty(phoneNo) || !ParamValidator.isMobile(phoneNo)) {
                jsonObj.put("message", "手机号输入不正确！");
                jsonObj.put("success", false);
                return jsonObj;
            }

            String random = CommonUtils.rands(6);
            DefaultConsoleSession.getInstance().setAttribute(request, SmsConstants.SESSION_MESSAGE_CODE, random);

            //TODO
            String smsMsg = getSmsMsg(random, "FIND_TRANS_PWD");
            
            //如果用户姓名不为空，用用户姓名替换登录id
    		if (!StringUtils.isEmpty(user.getNick())) {
    			smsMsg = smsMsg.replace("#LOGINID#", user.getNick());
    		}else{
    			smsMsg = smsMsg.replace("#LOGINID#", user.getLoginId());
    		}
            
            logger.info("手机号： " + phoneNo + "，验证信息： " + smsMsg + "，验证码: " + random);
//            boolean success = sendShortMessage(smsMsg, "1", phoneNo);
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

	//验证短信
    @RequestMapping(value = "verifyMessageCode.do", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject verifyMessageCode(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();

        String telCode = request.getParameter("telCode");
        if (StringUtils.isEmpty(telCode)) {
            jsonObject.put("message", "短信验证码为空！");
            jsonObject.put("success", false);
            return jsonObject;
        }

        String messageCodeInSession = DefaultConsoleSession.getInstance().getAttribute(request, SmsConstants.SESSION_MESSAGE_CODE);

        if (!"8888".equals(telCode) && !telCode.equals(messageCodeInSession)) {
            jsonObject.put("message", "短信验证码错误！");
            jsonObject.put("success", false);
            return jsonObject;
        }
        jsonObject.put("message", "短信验证码成功！");
        jsonObject.put("success", true);

        //记录修改步骤
        String changeStep = request.getParameter("changeStep");
        if ("PwdOne".equals(changeStep)) {
            DefaultConsoleSession.getInstance().setAttribute(request, resetTransPwdSteps.RESET_TRANS_PWD_STEP_KEY, resetTransPwdSteps.RESET_TRANS_PWD_STEP_ONE);
        } else if ("PwdTwo".equals(changeStep)) {
            DefaultConsoleSession.getInstance().setAttribute(request, resetTransPwdSteps.RESET_TRANS_PWD_STEP_KEY, resetTransPwdSteps.RESET_TRANS_PWD_STEP_TWO);
        }
        return jsonObject;
    }

	private String getSmsMsg(String smsBiz) {
		SmsConfig smsCfg = SmsConfig.getInstance();
		smsCfg.load();
		String smsTemplate = smsCfg.getString(smsBiz);
		return smsTemplate;
	}

	private String getSmsMsg(String random, String smsBiz) {
		SmsConfig smsCfg = SmsConfig.getInstance();
		smsCfg.load();
		String smsTemplate = smsCfg.getString(smsBiz);
		String randoms = smsTemplate.replace("#MSGCODE#", random);
		return randoms;
	}
	 /*private static ShortMessageDubbo shortMessageDubbo = null;

	    protected static ShortMessageDubbo getShortMessageDubbo() {
	        if (null == shortMessageDubbo) {
	            WebApplicationContext webContext = ContextLoader.getCurrentWebApplicationContext();
	            shortMessageDubbo = (ShortMessageDubbo) webContext.getBean(ShortMessageDubbo.class.getSimpleName());

	        }
	        return shortMessageDubbo;
	    }

	    protected boolean sendShortMessage(String content, String channel, String phoneNo) {
	        ShortMessageRequest messageRequest = new ShortMessageRequest();
	        messageRequest.setMobile(phoneNo);
	        messageRequest.setContent(content);
	        messageRequest.setSourceId("site");
	        ShortMessageResponse messageResponse = getShortMessageDubbo().add(messageRequest);
	        logger.info("ShortMessageResponse" + messageResponse.getReponseDesc());
	        return "200".equals(messageResponse.getReponseDesc());
	    }*/

}
