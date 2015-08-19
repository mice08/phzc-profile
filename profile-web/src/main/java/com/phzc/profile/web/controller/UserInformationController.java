package com.phzc.profile.web.controller;

import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.profile.web.form.UserInfoVo;
import com.phzc.profile.web.utils.UserInfoUtils;
import com.phzc.ubs.common.facade.model.UserInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by HANRAN177 on 2015/7/28.
 */
@Controller
@RequestMapping("/information/*")
public class UserInformationController extends BaseController {
    private static Logger logger = LoggerFactory.getLogger(UserInformationController.class);

    @RequestMapping(value = "showBaseInfo.do", method = RequestMethod.GET)
    public String showBaseInfo(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

        logger.info("UserInformationController - showBaseInfo");
        model.addAttribute("config", applicationPropertyConfig);
        User user = null;
        try {
            user = userSessionFacade.getUser(request);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Get session user error: " + e.getLocalizedMessage());
        }
//        if (null == user) {
//            response.sendRedirect(applicationPropertyConfig.getDomainPassport() + "/siteLogin/loginForm.do?toUrl=" + applicationPropertyConfig.getDomainProfile() + "/information/showBaseInfo.do");
//            return "information/baseInfo.ftl";
////        }
        logger.info("UserInformationController - showBaseInfo - start query user form UBS");

        String custId = user.getCustId();
        String operId = user.getOperId();
//        String custId = "1507011900156214";
//        String operId = "000001";
        UserInfoVo userInfoVo = new UserInfoVo();
        UserInfoDto userInfoDto = null;
        try {
            userInfoDto = queryUserInfoByCustIdFromUbs(custId, operId);
        } catch (ServiceException e) {
            logger.error(e.getLocalizedMessage());
        }
        logger.info("UserInformationController - showBaseInfo - end query user form UBS");
        if (null != userInfoDto) {
            BeanUtils.copyProperties(userInfoDto, userInfoVo);
            confuseUsrnfo(userInfoVo);
        }
        model.addAttribute("info", userInfoVo);
        logger.info("UserInformationController - showBaseInfo - finished");
        return "information/baseInfo.ftl";
    }


    private void confuseUsrnfo(UserInfoVo userInfoVo) {
        userInfoVo.setUsrName(UserInfoUtils.confuseUsrName(userInfoVo.getUsrName()));
        userInfoVo.setCertId(UserInfoUtils.confuseCertId(userInfoVo.getCertId()));
        userInfoVo.setUsrMp(UserInfoUtils.confuseUsrMp(userInfoVo.getUsrMp()));
        userInfoVo.setUsrEmail(UserInfoUtils.confuseUsrEmail(userInfoVo.getUsrEmail()));
        userInfoVo.setUsrSex(UserInfoUtils.convertUsrSex(userInfoVo.getUsrSex()));
    }
}
