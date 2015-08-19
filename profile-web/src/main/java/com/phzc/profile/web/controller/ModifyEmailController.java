package com.phzc.profile.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.console.base.session.impl.DefaultConsoleSession;
import com.phzc.ubs.common.facade.model.ModifyUserInfoRequest;
import com.phzc.ubs.common.facade.model.ModifyUserInfoResult;
import com.phzc.ubs.common.facade.model.UserInfoDto;
import com.phzc.ubs.common.facade.service.UserInfoFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ModifyEmailController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(ModifyEmailController.class);

    private static class ChangeEmailSteps {
        public static String CHANGE_EMAIL_STEP_KEY = "CHANGE_EMAIL_STEP_KEY";
        public static String CHANGE_EMAIL_STEP_ONE = "CHANGE_EMAIL_STEP_ONE";
        public static String CHANGE_EMAIL_STEP_TWO = "CHANGE_EMAIL_STEP_TWO";
        public static String CHANGE_EMAIL_STEP_THREE = "CHANGE_EMAIL_STEP_THREE";
    }

    @RequestMapping(value = "changeEmailOne.do", method = RequestMethod.GET)
    public String changeEmailOne(Model model, HttpServletRequest request) {
        model.addAttribute("config", applicationPropertyConfig);

        return "information/modifyEmailOne.ftl";
    }

    @RequestMapping(value = "verifyCurrentEmail.do", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject verifyCurrentEmail(HttpServletRequest request, HttpServletResponse response) {
        JSONObject jsonObject = new JSONObject();

        String currentEmail = request.getParameter("currentEmail");

        User user = null;
        UserInfoDto userInfoDto = null;
        try {
            user = userSessionFacade.getUser(request);
//            if (null == user) {
//                //TODO
//            }
//            if (null == user) {
//                response.sendRedirect(applicationPropertyConfig.getDomainPassport() + "/siteLogin/loginForm.do?toUrl=" + applicationPropertyConfig.getDomainProfile() + "/information/showBaseInfo.do");
//                return jsonObject;
//            }
            String custId = user.getCustId();
            String operId = user.getOperId();
//            String custId = "20150624100336";
//            String operId = "111";
            userInfoDto = queryUserInfoByCustIdFromUbs(custId, operId);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getLocalizedMessage());
        }
        if (null != userInfoDto) {
            if (userInfoDto.getUsrEmail().equals(currentEmail)) {
                jsonObject.put("message", "验证成功");
                jsonObject.put("success", true);
                DefaultConsoleSession.getInstance().setAttribute(request, ChangeEmailSteps.CHANGE_EMAIL_STEP_KEY, ChangeEmailSteps.CHANGE_EMAIL_STEP_ONE);
                return jsonObject;
            } else {
                jsonObject.put("message", "wrong");
                jsonObject.put("success", false);
                return jsonObject;
            }
        }
        jsonObject.put("message", "服务器异常，请稍后再试");
        jsonObject.put("success", false);
        return jsonObject;
    }

    @RequestMapping(value = "changeEmailTwo.do", method = RequestMethod.GET)
    public String changeEmailTwo(Model model, HttpServletRequest request) {
        //检查修改步骤
        String changeStep = DefaultConsoleSession.getInstance().getAttribute(request, ChangeEmailSteps.CHANGE_EMAIL_STEP_KEY);
        if (null == changeStep || !changeStep.equals(ChangeEmailSteps.CHANGE_EMAIL_STEP_ONE)) {
            return changeEmailOne(model, request);
        }

        model.addAttribute("config", applicationPropertyConfig);
        return "information/modifyEmailTwo.ftl";
    }

    @RequestMapping(value = "changeNewEmail.do", method = RequestMethod.POST)
    public
    @ResponseBody
    JSONObject changeNewEmail(HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();

        String newEmail = request.getParameter("newEmail");
        User user = null;
        try {
            user = userSessionFacade.getUser(request);
             String custId = user.getCustId();
        String operId = user.getOperId();
//            String custId = "20150624100336";
//            String operId = "111";
            ModifyUserInfoResult modifyUserInfoResult = modifyUsrEmail(custId, operId, newEmail);
            if (null == modifyUserInfoResult) {
                jsonObject.put("message", "服务器异常，请稍后再试！");
                jsonObject.put("success", false);
                return jsonObject;
            } else if ("000".equals(modifyUserInfoResult.getRespCode())) {
                jsonObject.put("message", "修改邮箱成功");
                jsonObject.put("success", true);
                DefaultConsoleSession.getInstance().setAttribute(request, ChangeEmailSteps.CHANGE_EMAIL_STEP_KEY, ChangeEmailSteps.CHANGE_EMAIL_STEP_TWO);
                return jsonObject;
            } else {
                jsonObject.put("message", modifyUserInfoResult.getRespDesc());
                jsonObject.put("success", false);
                return jsonObject;
            }
        } catch (Exception e) {
            logger.error("Get session user error: " + e.getLocalizedMessage());
        }
        jsonObject.put("message", "服务器异常，请稍后再试！");
        jsonObject.put("success", false);
        return jsonObject;
    }

    private ModifyUserInfoResult modifyUsrEmail(String custId, String operId, String newEmail) {
        try {
            UserInfoFacade userInfoFacade = getUserInfoWebServiceClient();
            ModifyUserInfoRequest modifyUserInfoRequest = new ModifyUserInfoRequest();
            modifyUserInfoRequest.setCustId(custId);
            modifyUserInfoRequest.setOperId(operId);
            modifyUserInfoRequest.setUsrEmail(newEmail);
            modifyUserInfoRequest.setConsumerId(ubsConsumerId);
            return userInfoFacade.modifyUserInfo(modifyUserInfoRequest);
        } catch (ServiceException e) {
            logger.error(e.getLocalizedMessage());
        }
        return null;
    }

    @RequestMapping(value = "changeEmailThree.do", method = RequestMethod.GET)
    public String changeEmailThree(Model model, HttpServletRequest request) {
        //检查修改步骤
        String changeStep = DefaultConsoleSession.getInstance().getAttribute(request, ChangeEmailSteps.CHANGE_EMAIL_STEP_KEY);
        if (null == changeStep || !changeStep.equals(ChangeEmailSteps.CHANGE_EMAIL_STEP_TWO)) {
            return changeEmailOne(model, request);
        } else {
            DefaultConsoleSession.getInstance().setAttribute(request, ChangeEmailSteps.CHANGE_EMAIL_STEP_KEY, ChangeEmailSteps.CHANGE_EMAIL_STEP_THREE);
        }
        model.addAttribute("config", applicationPropertyConfig);
        return "information/modifyEmailThree.ftl";
    }
}
