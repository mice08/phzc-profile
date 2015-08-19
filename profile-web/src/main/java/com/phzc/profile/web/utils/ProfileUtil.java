package com.phzc.profile.web.utils;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.console.base.remote.WebServiceClientFactory;
import com.phzc.console.base.session.UserSessionFacade;
import com.phzc.console.base.session.impl.DefaultConsoleSession;
import com.phzc.console.util.ConsolePropertiesManager;
import com.phzc.profile.web.constants.ProfileConstants;
import com.phzc.ubs.common.facade.model.BankCardInfoDto;
import com.phzc.ubs.common.facade.model.QueryBankCardRequest;
import com.phzc.ubs.common.facade.model.QueryBankCardResult;
import com.phzc.ubs.common.facade.service.BankCardInfoFacade;

@Component
public class ProfileUtil {

	// UBS安全码
	protected static String consumerId = ConsolePropertiesManager.getUbsConsumerCode();
	
	public ProfileUtil() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}
	
	@Autowired
	private WebServiceClientFactory serviceClientFactory;
	@Autowired
	private  UserSessionFacade userSessionFacade;
	private static  Logger log = LoggerFactory.getLogger(ProfileUtil.class);
	
	public BankCardInfoDto getBandCardInfo(HttpServletRequest req) {
		BankCardInfoDto bankCardInfo = null;
		if(getUser(req).getCustId() != null && consumerId != null) {
			//初始化银行卡信息
		    BankCardInfoFacade bankCardInfoFacade = null;
			try {
				bankCardInfoFacade = (BankCardInfoFacade)serviceClientFactory.getWebServiceClient(BankCardInfoFacade.class,
				    "bankCardInfoFacade", "UBS");
			} catch (ServiceException e) {
				log.error("getBandCardInfo Exception:"+e.getMessage());
			}
		    //查询银行卡号信息
		    QueryBankCardRequest queryBankCardRequest = new QueryBankCardRequest();
		    queryBankCardRequest.setCustId(getUser(req).getCustId());
		    queryBankCardRequest.setConsumerId(consumerId);
		    QueryBankCardResult queryBankCardResult = bankCardInfoFacade.queryBankCard(queryBankCardRequest);
		    if(null != queryBankCardResult && queryBankCardResult.getBankCardInfoList() != null ) 
		    	log.info("queryBankCardResult="+queryBankCardResult+"--size="+queryBankCardResult.getBankCardInfoList().size());
		    if(queryBankCardResult!=null && queryBankCardResult.getBankCardInfoList()!=null){
		    	bankCardInfo = queryBankCardResult.getBankCardInfoList().get(0);
		    	DefaultConsoleSession.getInstance().setAttribute(req, ProfileConstants.SEESION_KEY_CARD, bankCardInfo);
		        String cardNo = bankCardInfo.getCardId();//银行卡号(目前仅支持绑定一张卡)
		        Integer cardSeqId = bankCardInfo.getCardSeqId();//持卡人序列号
		        String mobileNo = bankCardInfo.getMobileNo();//银行预留手机号
		        String bankId = bankCardInfo.getBankId();
		        log.info("cardNo="+cardNo+"\n"+"--cardSeqId="+cardSeqId+"\n"+"--mobileNo="+mobileNo+"\n"+"--bankId="+bankId);
		        req.setAttribute("cardNoPre", StringUtils.substring(cardNo, 0, 4));//银行卡号前四位
		        req.setAttribute("cardNoEnd", StringUtils.substring(cardNo, cardNo.length()-4, cardNo.length()));//银行卡号后四位
		        req.setAttribute("bankId", bankId);
		        req.setAttribute("mobileNo", mobileNo);
		        //将关键信息放入session
		        DefaultConsoleSession.getInstance().setAttribute(req, getUser(req).getCustId()+"_"+getUser(req).getOperId()+"_cardSeqId", cardSeqId);//银行卡序列号 TODO _myOperId_
		        DefaultConsoleSession.getInstance().setAttribute(req, getUser(req).getCustId()+"_"+getUser(req).getOperId()+"_mobileNo", mobileNo);//银行预留手机号   TODO
		    }
		}
		return bankCardInfo;
	}
	
	protected  User getUser(HttpServletRequest request) {
		System.out.println("userSessionFacade--------------------"+userSessionFacade);
		return userSessionFacade.getUser(request);
	}
	
	public void setServiceClientFactory(WebServiceClientFactory serviceClientFactory) {
		this.serviceClientFactory = serviceClientFactory;
	}

	public void setUserSessionFacade(UserSessionFacade userSessionFacade) {
		this.userSessionFacade = userSessionFacade;
	}
}
