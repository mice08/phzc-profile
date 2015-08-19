package com.phzc.profile.web.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.remote.WebServiceClientFactory;
import com.phzc.console.base.session.ConsoleSession;
import com.phzc.console.base.session.UserSessionFacade;
import com.phzc.console.base.session.impl.DefaultConsoleSession;
import com.phzc.console.util.ConsolePropertiesManager;
import com.phzc.pms.facade.service.base.UserRelationFacade;
import com.phzc.pms.facade.service.business.UserProductFacade;
import com.phzc.profile.web.config.ApplicationPropertyConfig;
import com.phzc.profile.web.services.ServiceClient;
import com.phzc.profile.web.utils.DateUtils;
import com.phzc.profile.web.utils.RandomUtil;
import com.phzc.ubs.common.facade.model.BankCardInfoDto;
import com.phzc.ubs.common.facade.model.IdInfoRequest;
import com.phzc.ubs.common.facade.model.IdInfoResult;
import com.phzc.ubs.common.facade.model.UserInfoDto;
import com.phzc.ubs.common.facade.service.UserInfoFacade;


public class BaseController {
    @Autowired
    protected ApplicationPropertyConfig applicationPropertyConfig;

    @Autowired
    protected UserSessionFacade userSessionFacade;

    @Autowired
    protected WebServiceClientFactory serviceClientFactory;

    @Autowired
    protected ServiceClient serviceClient;
    
    protected static ConsoleSession consoleSession = DefaultConsoleSession.getInstance();
    

    // UBS安全码
    protected String ubsConsumerId = ConsolePropertiesManager.getUbsConsumerCode();

    protected UserInfoFacade getUserInfoWebServiceClient() throws ServiceException {
        return (UserInfoFacade) serviceClientFactory.getWebServiceClient(UserInfoFacade.class, "userInfoFacade", "UBS");
    }

    protected UserProductFacade getUserProductWebServiceClient() throws ServiceException {
        return  serviceClient.getWebServiceClient(UserProductFacade.class, "userProduct", "PMS");
    }

    protected UserRelationFacade getUserRelationWebServiceClient() throws ServiceException {
        return  serviceClient.getWebServiceClient(UserRelationFacade.class, "userRelation", "PMS");
    }

    protected UserInfoDto queryUserInfoByCustIdFromUbs(String custId, String operId) throws ServiceException {
        UserInfoFacade userInfoFacade = getUserInfoWebServiceClient();
        IdInfoRequest idInfoRequest = new IdInfoRequest();
        idInfoRequest.setCustId(custId);
        idInfoRequest.setOperId(operId);
        idInfoRequest.setConsumerId(ubsConsumerId);
        IdInfoResult idInfoResult = userInfoFacade.queryUserByCustId(idInfoRequest);
        if ("000".equals(idInfoResult.getRespCode())) {
            if (null != idInfoResult.getList() && idInfoResult.getList().size() > 0) {
                return idInfoResult.getList().get(0);
            }
        }
        return null;
    }
    
    /**
     * 获取请求流水号
     * @return
     */
    protected String getReqSeqId() {
    	return DateUtils.getCurrentDate()+RandomUtil.rands(22);
    }

    protected String confuseMobile(String mobile) {
		int len = mobile.length();
		String subStr = " **** ";
		String encodedCardId = mobile.substring(0, 3) + subStr + mobile.substring(len - 4, len);
		return encodedCardId;
	}

}