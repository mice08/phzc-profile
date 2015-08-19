package com.phzc.profile.web.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.phzc.console.EnvEnum;
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
import com.phzc.profile.api.constants.ProfileConstants;
import com.phzc.profile.api.utils.CommonUtil;
import com.phzc.profile.api.vo.UserAuthorizeInfo;
import com.phzc.profile.biz.model.IdentityInfo;
import com.phzc.profile.biz.model.IdentityRecord;
import com.phzc.profile.biz.service.SequenceService;
import com.phzc.profile.biz.service.UserAuthorizeService;
import com.phzc.profile.biz.service.impl.IdentityInfoServiceImpl;
import com.phzc.profile.biz.utils.ProfileUtil;
import com.phzc.profile.web.config.ApplicationPropertyConfig;
import com.phzc.profile.web.entity.IdentityUnPassCause;
import com.phzc.profile.web.entity.UserIdentifyStatus;
import com.phzc.profile.web.entity.UserIdentityType;
import com.phzc.profile.web.utils.DateUtils;
import com.phzc.ubs.common.facade.model.IdInfoRequest;
import com.phzc.ubs.common.facade.model.IdInfoResult;
import com.phzc.ubs.common.facade.model.ModifyCertiInfoRequest;
import com.phzc.ubs.common.facade.model.ModifyCertiInfoResult;
import com.phzc.ubs.common.facade.model.UserInfoDto;
import com.phzc.ubs.common.facade.service.UserInfoFacade;
/**
 * 跳转状态控制
 * @author ZHAIRONGYE
 *
 */
@Service
public class RedirectController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(RedirectController.class);
	@Autowired
	private WebServiceClientFactory serviceClientFactory;

	@Autowired
	protected ApplicationPropertyConfig applicationPropertyConfig;

	@Autowired
	private UserSessionFacade userSessionFacade;
	
	// UBS安全码
	private String ubsConsumerId = ConsolePropertiesManager.getUbsConsumerCode();
	/**
	 * 
	 * @param request
	 * @param type  1 股权，2债权  ，3 公益
	 * @param from
	 * @return
	 */
	public String getNextDirect(HttpServletRequest request,String type,String from){
		if("2".equals(type)||"3".equals(type)){
			if (getRealNameStates(request)) {
				//如果实名了 则去设置交易密码  ？？？
				return "redirect:/m/openAccount/openAccount.do?type="+type+"&from="+from;
			}else{
				//没实名去实名
				return "redirect:/m/openAccount/openAccount.do?type="+type+"&from="+from;
			}
		}else if("1".equals(type)){
			//股权类
			if (getRealNameStates(request)) {
				//如果实名了 则去设置交易密码
				return "redirect:/m/openAccount/openAccount.do?type="+type+"&from="+from;
			}else{
				//没实名去实名
				return "redirect:/m/openAccount/openAccount.do?type="+type+"&from="+from;
			}
		}else{
			return from;
		}
		
		
	}
	public boolean getRealNameStates(HttpServletRequest request){
		boolean flag=false;
		//获得的用户
		User userSession=userSessionFacade.getUser(request);
		String userId = userSession.getCustId();
		String operId = userSession.getOperId();
		UserInfoFacade ubsUserService = null;
		try {
			ubsUserService = (UserInfoFacade) serviceClientFactory
					.getWebServiceClient(UserInfoFacade.class, "userInfoFacade", "UBS");
		} catch (ServiceException e) {
			logger.info("调用ubs服务失败");
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
		IdInfoRequest usrBaseReq = new IdInfoRequest();
		usrBaseReq.setCustId(userId);
		usrBaseReq.setOperId(operId);
		usrBaseReq.setConsumerId(ubsConsumerId);
		IdInfoResult usrBaseRslt = ubsUserService.queryUserByCustId(usrBaseReq);
		UserInfoDto usrBaseInfo = null;
		if (null != usrBaseRslt && "000".equals(usrBaseRslt.getRespCode())) {
			System.out.println(usrBaseRslt.getRespCode());
			System.out.println(usrBaseRslt.getRespDesc());
			usrBaseInfo = usrBaseRslt.getList().get(0);
		}
		if (null != usrBaseInfo && !StringUtils.isBlank( usrBaseInfo.getUsrName()) && !StringUtils.isBlank(usrBaseInfo.getCertId())) {
			flag=true;
			return flag;
		}else{
			return flag;
			
		}
		
	}
}
