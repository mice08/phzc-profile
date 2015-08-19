package com.phzc.profile.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.phzc.console.base.remote.WebServiceClientFactory;
import com.phzc.console.base.session.UserSessionFacade;
import com.phzc.console.sms.SendMessage;
import com.phzc.profile.biz.service.UserAuthorizeService;
import com.phzc.profile.web.config.ApplicationPropertyConfig;
import com.phzc.profile.web.services.PayChannelClient;
import com.phzc.profile.web.utils.DateUtils;
import com.phzc.profile.web.utils.PayChannelClientFactory;
/**
 * 
 * @author YOUGUOQIANG225
 *
 */
public class AccountBaseController extends BaseController {
	@Autowired
	protected  WebServiceClientFactory serviceClientFactory;

	@Autowired
	protected  UserSessionFacade userSessionFacade;
	
	@Autowired
	protected  UserAuthorizeService userAuthorizeService;
	
	@Autowired
	protected  SendMessage sendMessage;
	@Autowired
	protected  ApplicationPropertyConfig applicationPropertyConfig;

	protected volatile PayChannelClient payChannelClient;
	
	protected void setDate(HttpServletRequest req) {
        String startDate = req.getParameter("startDate");
        if (startDate != null) {
            startDate = startDate.replace("-", "");
        } else {
            startDate = DateUtils.getAboutDate(-7);
        }
        set_startDate(startDate);
        
        String endDate = req.getParameter("endDate");
        if (endDate != null) {
            endDate = endDate.replace("-", "");
        } else {
            endDate = DateUtils.getCurrentDate();//一周前日期
        }
        set_endDate(endDate);
	}
	
	protected  void initServices() {
		payChannelClient.setApplicationPropertyConfig(applicationPropertyConfig);
		payChannelClient.setSendMessage(sendMessage);
		payChannelClient.setServiceClientFactory(serviceClientFactory);
		payChannelClient.setUserAuthorizeService(userAuthorizeService);
		payChannelClient.setUserSessionFacade(userSessionFacade);
	}
	
	protected  PayChannelClient getInstance(HttpServletRequest request, String from) throws Exception {
		System.out.print("payChannelClient=======before=============================="+payChannelClient);
		payChannelClient = PayChannelClientFactory.getInstance(request,from);
		System.out.print("payChannelClient=======after=============================="+payChannelClient);
		return payChannelClient;
	}
	
	private String _startDate;
	private String _endDate;
	public String get_startDate() {
		return _startDate;
	}


	public void set_startDate(String _startDate) {
		this._startDate = _startDate;
	}


	public String get_endDate() {
		return _endDate;
	}


	public void set_endDate(String _endDate) {
		this._endDate = _endDate;
	}
	
}
