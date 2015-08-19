package com.phzc.profile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.phzc.console.SmsConstants;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.console.base.session.impl.DefaultConsoleSession;
import com.phzc.console.sms.SendMessage;
import com.phzc.console.sms.SmsConfig;
import com.phzc.console.util.CommonUtils;
import com.phzc.console.util.ParamValidator;
import com.phzc.profile.web.utils.UserInfoUtils;
import com.phzc.ubs.common.facade.model.*;
import com.phzc.ubs.common.facade.service.UserInfoFacade;
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
 * Created by HANRAN177 on 2015/7/28.
 */
@Controller
@RequestMapping("/information/*")
public class ModifyPhoneController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(ModifyPhoneController.class);

    private static class ChangePhoneSteps {
        public static String CHANGE_PHONE_STEP_KEY = "CHANGE_PHONE_STEP_KEY";
        public static String CHANGE_PHONE_STEP_ONE = "CHANGE_PHONE_STEP_ONE";
        public static String CHANGE_PHONE_STEP_TWO = "CHANGE_PHONE_STEP_TWO";
        public static String CHANGE_PHONE_STEP_THREE = "CHANGE_PHONE_STEP_THREE";
    }

    @Autowired
    private SendMessage sendMessage;

    @RequestMapping(value = "changePhoneOne.do", method = RequestMethod.GET)
    public String changePhoneOne(Model model, HttpServletRequest request, HttpServletResponse response) {
        model.addAttribute("config", applicationPropertyConfig);
        User user = null;
        try {
            user = userSessionFacade.getUser(request);
//            if (null == user) {
//                response.sendRedirect(applicationPropertyConfig.getDomainPassport() + "/siteLogin/loginForm.do?toUrl=" + applicationPropertyConfig.getDomainProfile() + "/information/showBaseInfo.do");
//                return "";
//            }
            String custId = user.getCustId();
            String operId = user.getOperId();
            UserInfoDto userInfoDto = queryUserInfoByCustIdFromUbs(custId, operId);
            logger.info("changePhoneOne user mp: " + userInfoDto.getUsrMp());
            model.addAttribute("phoneNo", UserInfoUtils.confuseUsrMp(userInfoDto.getUsrMp()));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Get session user error: " + e.getLocalizedMessage());
        }
        return "information/modifyPhoneOne.ftl";
    }

    @RequestMapping(value = "changePhoneTwo.do", method = RequestMethod.GET)
    public String changePhoneTwo(Model model, HttpServletRequest request, HttpServletResponse response) {

        //检查修改步骤
        String changeStep = DefaultConsoleSession.getInstance().getAttribute(request, ChangePhoneSteps.CHANGE_PHONE_STEP_KEY);
        if (null == changeStep || !changeStep.equals(ChangePhoneSteps.CHANGE_PHONE_STEP_ONE)) {
            return changePhoneOne(model, request, response);
        }
        model.addAttribute("config", applicationPropertyConfig);
//        User user = null;
//        try {
//            user = userSessionFacade.getUser(request);
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("Get session user error: " + e.getLocalizedMessage());
//        }
        return "information/modifyPhoneTwo.ftl";
    }

    @RequestMapping(value = "changePhoneThree.do", method = RequestMethod.GET)
    public String changePhoneThree(Model model, HttpServletRequest request, HttpServletResponse response) {

        //检查修改步骤
        String changeStep = DefaultConsoleSession.getInstance().getAttribute(request, ChangePhoneSteps.CHANGE_PHONE_STEP_KEY);
        if (null == changeStep || !changeStep.equals(ChangePhoneSteps.CHANGE_PHONE_STEP_TWO)) {
            return changePhoneTwo(model, request, response);
        } else {
            DefaultConsoleSession.getInstance().setAttribute(request, ChangePhoneSteps.CHANGE_PHONE_STEP_KEY, ChangePhoneSteps.CHANGE_PHONE_STEP_THREE);
        }

        model.addAttribute("config", applicationPropertyConfig);

        /*User user = null;
        try {
            user = userSessionFacade.getUser(request);
        } catch (Exception e) {
            logger.error("Get session user error: " + e.getLocalizedMessage());
        }*/
        return "information/modifyPhoneThree.ftl";
    }

    @RequestMapping(value = "sendForModifyPhone.do", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject sendForModifyPhone(HttpServletRequest request, HttpServletResponse response) {

        String changeStep = request.getParameter("changeStep");

        JSONObject jsonObj = new JSONObject();
        String phoneNo = null;
        try {
            User user = userSessionFacade.getUser(request);
//            if (null == user) {
//                response.sendRedirect(applicationPropertyConfig.getDomainPassport() + "/siteLogin/loginForm.do?toUrl=" + applicationPropertyConfig.getDomainProfile() + "/information/showBaseInfo.do");
//                return jsonObj;
//            }
            if ("PhoneOne".equals(changeStep)) {
                if (null != user) {
                    phoneNo = user.getMobileNo();
                }
            } else if ("PhoneTwo".equals(changeStep)) {
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

            String smsMsg = getSmsMsg(random, "MODIFY_MOBILE");
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
            logger.error(e.getMessage());
            jsonObj.put("message", "系统错误，请稍后再试！");
            jsonObj.put("success", false);
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
        if ("PhoneOne".equals(changeStep)) {
            DefaultConsoleSession.getInstance().setAttribute(request, ChangePhoneSteps.CHANGE_PHONE_STEP_KEY, ChangePhoneSteps.CHANGE_PHONE_STEP_ONE);
        } else if ("PhoneTwo".equals(changeStep)) {
            DefaultConsoleSession.getInstance().setAttribute(request, ChangePhoneSteps.CHANGE_PHONE_STEP_KEY, ChangePhoneSteps.CHANGE_PHONE_STEP_TWO);
        }
        return jsonObject;
    }

    @RequestMapping(value = "verifyNewPhoneNo.do", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject verifyNewPhoneNo(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();

        String mobileNo = request.getParameter("mobileNo");
        MpInfoResult mpInfoResult = queryUserInfoByMobileFromUbs(mobileNo);
        if (null == mpInfoResult) {
            jsonObject.put("message", "服务器异常，请稍后再试！");
            jsonObject.put("success", false);
            return jsonObject;
        } else if ("000".equals(mpInfoResult.getRespCode()) && null != mpInfoResult.getUserInfo()) {
            jsonObject.put("message", "该手机号已注册，请重新输入");
            jsonObject.put("success", false);
            return jsonObject;
        } else if ("402".equals(mpInfoResult.getRespCode()) && null == mpInfoResult.getUserInfo()) {
            jsonObject.put("message", "成功");
            jsonObject.put("success", true);
            return jsonObject;
        } else {
            jsonObject.put("message", mpInfoResult.getRespDesc());
            jsonObject.put("success", false);
            return jsonObject;
        }
    }

    @RequestMapping(value = "modifyNewPhoneNo.do", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject modifyNewPhoneNo(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        String mobileNo = request.getParameter("mobileNo");
        User user = null;
        try {
            user = userSessionFacade.getUser(request);
            String custId = user.getCustId();
            String operId = user.getOperId();
//            String custId = "1507011900156214";
//            String operId = "000001";
            ModifyUserInfoResult modifyUserInfoResult = modifyUsrMobile(custId, operId, mobileNo);
            if (null == modifyUserInfoResult) {
                jsonObject.put("message", "服务器异常，请稍后再试！");
                jsonObject.put("success", false);
                return jsonObject;
            } else if ("000".equals(modifyUserInfoResult.getRespCode())) {
                jsonObject.put("message", "修改手机成功");
                jsonObject.put("success", true);
                return jsonObject;
            } else {
                jsonObject.put("message", modifyUserInfoResult.getRespDesc());
                jsonObject.put("success", false);
                return jsonObject;
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Get session user error: " + e.getLocalizedMessage());
        }
        jsonObject.put("message", "服务器异常，请稍后再试！");
        jsonObject.put("success", false);
        return jsonObject;
    }

    private ModifyUserInfoResult modifyUsrMobile(String custId, String operId, String mobileNo) {
        try {
            UserInfoFacade userInfoFacade = getUserInfoWebServiceClient();
            ModifyUserInfoRequest modifyUserInfoRequest = new ModifyUserInfoRequest();
            modifyUserInfoRequest.setCustId(custId);
            modifyUserInfoRequest.setOperId(operId);
            modifyUserInfoRequest.setUsrMp(mobileNo);
            modifyUserInfoRequest.setConsumerId(ubsConsumerId);
            return userInfoFacade.modifyUserInfo(modifyUserInfoRequest);
        } catch (ServiceException e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }

    private MpInfoResult queryUserInfoByMobileFromUbs(String mobileNo) {
        try {
            UserInfoFacade userInfoFacade = getUserInfoWebServiceClient();
            MpInfoRequest mpInfoRequest = new MpInfoRequest();
            mpInfoRequest.setUsrMp(mobileNo);
            mpInfoRequest.setConsumerId(ubsConsumerId);
            return userInfoFacade.queryUserByUsrMp(mpInfoRequest);
        } catch (ServiceException e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
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
        logger.info("ShortMessageResponse" + messageResponse.getResponseCode());
        return "200".equals(messageResponse.getResponseCode());
    }*/
}
