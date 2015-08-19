package com.phzc.profile.web.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.phzc.console.ConsoleConstants;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.console.base.remote.WebServiceClientFactory;
import com.phzc.console.base.session.UserSessionFacade;
import com.phzc.console.base.session.impl.DefaultConsoleSession;
import com.phzc.console.sms.SendMessage;
import com.phzc.console.sms.SmsConfig;
import com.phzc.console.util.CommonUtils;
import com.phzc.console.util.ConsolePropertiesManager;
import com.phzc.console.util.Md5Utils;
import com.phzc.profile.api.utils.HttpClientPost;
import com.phzc.profile.biz.service.UserAuthorizeService;
import com.phzc.profile.web.config.ApplicationPropertyConfig;
import com.phzc.profile.web.constants.ProfileConstants;
import com.phzc.profile.web.dto.RechargeResponseDTO;
import com.phzc.profile.web.utils.DateUtils;
import com.phzc.profile.web.utils.HttpClientUtil;
import com.phzc.profile.web.utils.ProfileUtil;
import com.phzc.profile.web.utils.RandomUtil;
import com.phzc.ubs.common.facade.model.AccountInfoDto;
import com.phzc.ubs.common.facade.model.BankCardInfoDto;
import com.phzc.ubs.common.facade.model.CashLogDto;
import com.phzc.ubs.common.facade.model.PwdInfoResult;
import com.phzc.ubs.common.facade.model.QueryAcctInfoRequest;
import com.phzc.ubs.common.facade.model.QueryAcctInfoResult;
import com.phzc.ubs.common.facade.model.QueryBankCardRequest;
import com.phzc.ubs.common.facade.model.QueryBankCardResult;
import com.phzc.ubs.common.facade.model.QueryCashLogResult;
import com.phzc.ubs.common.facade.model.QuerySaveWithdrawLogRequest;
import com.phzc.ubs.common.facade.model.ValidTransPwdRequest;
import com.phzc.ubs.common.facade.model.WithdrawCashRequest;
import com.phzc.ubs.common.facade.model.WithdrawCashResult;
import com.phzc.ubs.common.facade.service.AccountInfoFacade;
import com.phzc.ubs.common.facade.service.BankCardInfoFacade;
import com.phzc.ubs.common.facade.service.CashInfoFacade;
import com.phzc.ubs.common.facade.service.PwdInfoFacade;
/**
 * 
 * @author YOUGUOQIANG225
 *
 */
public abstract class PayChannelClient {

	private  Logger log = LoggerFactory.getLogger(getClass());
	private static ProfileUtil profileUtil;
	
	public PayChannelClient() {
		super();
	}
	
	// UBS安全码
	protected String consumerId = ConsolePropertiesManager.getUbsConsumerCode();
	protected WebServiceClientFactory serviceClientFactory;
	protected UserSessionFacade userSessionFacade;
	protected UserAuthorizeService userAuthorizeService;
	protected SendMessage sendMessage;
    protected ApplicationPropertyConfig applicationPropertyConfig;
	
	private static HttpClientUtil clientUtil;
	
	private String TRADE_RECHARGE_BASE_URL;
	
	//系统来源,PC or M
	private String sysId;
	

	//判断是否绑定银行卡
	public boolean isBindedBankCard(HttpServletRequest	req) {
		
		//先判断是否绑定银行卡
		BankCardInfoFacade bankCardInfoFacade;
		try {
			bankCardInfoFacade = (BankCardInfoFacade) serviceClientFactory.getWebServiceClient(BankCardInfoFacade.class,
			        "bankCardInfoFacade", "UBS");
		} catch (ServiceException e) {
			log.error("判断绑卡服务异常："+e.getMessage());
			return false;
		}
		QueryBankCardRequest queryBankCardRequest = new QueryBankCardRequest();
		queryBankCardRequest.setCustId(getUser(req).getCustId());
		queryBankCardRequest.setConsumerId(consumerId);
		QueryBankCardResult queryBankCardResult = bankCardInfoFacade.queryBankCard(queryBankCardRequest);
		if(queryBankCardResult != null &&  ProfileConstants.RESPONSE_STATUS_SUCCESS.equals(queryBankCardResult.getRespCode()) 
				&& queryBankCardResult.getBankCardInfoList().size()>0){
			return true;
		}			
		return false;
	}
	
	
	//查询用户账户信息
	public AccountInfoDto getAccountInfo(HttpServletRequest request) {
		AccountInfoFacade acctInfoservice = null;
		try {
			acctInfoservice = (AccountInfoFacade) serviceClientFactory.getWebServiceClient(AccountInfoFacade.class,
			    "accountInfoFacade", "UBS");
		} catch (ServiceException e) {
			log.error("查询账户服务异常："+e.getMessage());
		}
		QueryAcctInfoRequest queryAcctInfoReq = new QueryAcctInfoRequest();
		queryAcctInfoReq.setCustId(getUser(request).getCustId());
		queryAcctInfoReq.setAcctType(ProfileConstants.DEFAULT_ACCOUNT_TYPE);
		queryAcctInfoReq.setDcFlag("D");
		queryAcctInfoReq.setConsumerId(consumerId);
        QueryAcctInfoResult queryAcctInfoRslt = acctInfoservice.queryAcctInfo(queryAcctInfoReq);
        
        if (queryAcctInfoRslt != null && queryAcctInfoRslt.getAccountInfoDtoList() != null && queryAcctInfoRslt.getAccountInfoDtoList().size() > 0
        		&& ProfileConstants.RESPONSE_STATUS_SUCCESS.equals(queryAcctInfoRslt.getRespCode())) {
            return queryAcctInfoRslt.getAccountInfoDtoList().get(0);
        }
        log.info("无该账户信息.");
        return null;
	}
	
	public void initBandCardInfo(HttpServletRequest req) throws ServiceException {
		getProfileUtil().getBandCardInfo(req);
	}
	
	public String getRechargeRecord(HttpServletRequest req) {
		String url = getTradeForRechargeBaseUrl() + getRechargeRecordUrl(req);
		log.info(url);
		String result = null;
		try {
			result = HttpClientPost.doPost("", url);
		} catch (ClientProtocolException e) {
			log.error("查询充值记录异常："+e.getMessage());
		} catch (IOException e) {
			log.error("查询充值记录异常："+e.getMessage());
		}
		
        return result;
	}
	
	//查询取现记录
	public List<CashLogDto> getCashingRecord(HttpServletRequest req,String startDate, String endDate) {
        QuerySaveWithdrawLogRequest queryCashLogReq = new QuerySaveWithdrawLogRequest();
        queryCashLogReq.setConsumerId(consumerId);
        queryCashLogReq.setCustId(getUser(req).getCustId());
        queryCashLogReq.setStartDate(startDate);
        queryCashLogReq.setEndDate(endDate);
        if(!StringUtils.isBlank(req.getParameter("transStat"))) {
			queryCashLogReq.setTransStat(req.getParameter("transStat"));
		}
        queryCashLogReq.setCurPage(req.getParameter("currentPage"));
        String pageSizeS = req.getParameter("pageSize");
        int pageSize = StringUtils.isEmpty(pageSizeS) ? Integer.valueOf(ProfileConstants.DEFAULT_PAGE_SIZE) : Integer.valueOf(pageSizeS);
        queryCashLogReq.setPageSize(pageSize);
       
        CashInfoFacade cashInfoService = null;
		try {
			cashInfoService = (CashInfoFacade) serviceClientFactory.getWebServiceClient(CashInfoFacade.class, "cashInfoFacade", "UBS");
		} catch (ServiceException e) {
			log.error("获取取现服务异常:"+e.getMessage());
			return null;
		}
        QueryCashLogResult queryCashLogRslt = cashInfoService.queryCashLog(queryCashLogReq);
        if (queryCashLogRslt == null || !ProfileConstants.RESPONSE_STATUS_SUCCESS.equals(queryCashLogRslt.getRespCode())) {
            return null;
        }
        List<CashLogDto> cashLogList = queryCashLogRslt.getCashLogList();
        if (cashLogList != null && !cashLogList.isEmpty()) {
            //转换日期格式
            for (CashLogDto cashLogDto : cashLogList) {
                String sysTime = cashLogDto.getSysTime();
                if (sysTime == null) {
                    sysTime = "";
                } else if (sysTime.length() <14) {
                    sysTime = DateUtils.dateStringFormat2(sysTime);
                } else {
                    sysTime = DateUtils.dateStringFormat3(sysTime);
                }
                cashLogDto.setSysTime(sysTime);
            }
        }
        
        req.setAttribute("tabSelectStat", req.getParameter("tabSelectStat"));
        return cashLogList;
	}
	
	//调用UBS账户取现服务
	public WithdrawCashResult cashing(HttpServletRequest req, User user, String avlBalReq) throws ServiceException {
		CashInfoFacade cashInfoFacade = (CashInfoFacade)serviceClientFactory.getWebServiceClient(CashInfoFacade.class,
	            "cashInfoFacade", "UBS");
		//封装提现请求对象
        WithdrawCashRequest withdrawCashRequest = new WithdrawCashRequest();
        withdrawCashRequest.setCustId(user.getCustId());//客户号
        withdrawCashRequest.setAcctType(ProfileConstants.DEFAULT_ACCOUNT_TYPE);//账户类型
        withdrawCashRequest.setSubAcctId(ProfileConstants.DEFAULT_ACCOUNT_TYPE);//子账号
        withdrawCashRequest.setBusiType(ProfileConstants.DEFAULT_BUSINESS_TYPE);//业务类型
        withdrawCashRequest.setTransAmt(avlBalReq);//取现金额
        withdrawCashRequest.setPayChnl(getPayChannel());//代付渠道 12-通联支付  13-易联支付            
        withdrawCashRequest.setCardSeqId(getCardSeqId(req));//卡序列号
        withdrawCashRequest.setReqDate(DateUtils.getCurrentDate());//请求日期
        withdrawCashRequest.setReqSeqId(getReqSeqId());//请求流水
        withdrawCashRequest.setSysId(ProfileConstants.SYSTEM_ID);//系统号
        withdrawCashRequest.setBedpId(ProfileConstants.DEPARTMENT_CODE);//业务部门代号
        withdrawCashRequest.setPgRetUrl(getPgRetUrl());//前台返回地址
        withdrawCashRequest.setConsumerId(consumerId);
        WithdrawCashResult withdrawCashResult = cashInfoFacade.withdrawCash(withdrawCashRequest); 
        return withdrawCashResult;
	}
	
	
	protected String getCardSeqId(HttpServletRequest req) {
		String cardSeqId = req.getParameter("cardSeqId") == null ? 
				String.valueOf(DefaultConsoleSession.getInstance().getAttribute(req,getUser(req).getCustId()+"_"+getUser(req).getOperId()+"_cardSeqId"))
				: req.getParameter("cardSeqId");
		if(cardSeqId == null || "".equals(cardSeqId)) {
			BankCardInfoDto bankCardInfo = null;
			bankCardInfo = (BankCardInfoDto) DefaultConsoleSession.getInstance().getAttribute(req, ProfileConstants.SEESION_KEY_CARD,BankCardInfoDto.class);
			if(bankCardInfo == null)
				return null;
			cardSeqId = String.valueOf(bankCardInfo.getCardSeqId());
		}
		log.info("cardSeqId----="+cardSeqId);
		return cardSeqId;
	}
	

	public RechargeResponseDTO recharge(HttpServletRequest req) {

		String url = getTradeForRechargeBaseUrl() + getRechargeUrl(req);
		log.info(url);
		RechargeResponseDTO rechargeResp = null;
		try {
			String result = HttpClientPost.doPost("", url);
			if(!StringUtils.isBlank(result)) {
				rechargeResp = JSON.parseObject(result,RechargeResponseDTO.class);
			}
		} catch (ClientProtocolException e) {
			log.error("充值异常："+e.getMessage());
		} catch (IOException e) {
			log.error("充值异常："+e.getMessage());
		}
        return rechargeResp;
	}
	
	protected String getRechargeUrl(HttpServletRequest req) {
		Map paramMap = new HashMap();
		paramMap.put("merId", req.getParameter("merId") == null ? ProfileConstants.DEFAULT_MERID : req.getParameter("merId") );
		paramMap.put("cmdId", ProfileConstants.CMDID_RECHARGE);
		paramMap.put("version", req.getParameter("version") == null ? ProfileConstants.REQ_VERSION : req.getParameter("version"));
		paramMap.put("custId", getUser(req).getCustId());
		paramMap.put("operId", getUser(req).getOperId());//操作员ID
		paramMap.put("subAcctId", req.getParameter("subAcctId") == null ? ProfileConstants.DEFAULT_ACCOUNT_TYPE : req.getParameter("subAcctId"));//充值入账账户号
		paramMap.put("transAmt", req.getParameter("transAmt"));//充值金额
		paramMap.put("payChnl", getPayChannel());//代收渠道
		paramMap.put("reqDate", DateUtils.getCurrentDate());//请求日期 
		paramMap.put("reqSeqId", getReqSeqId());//请求流水号 
		paramMap.put("sysId", getSysId());//系统来源   ProfileConstants.FORM_PC
		paramMap.put("bedpId", ProfileConstants.DEPARTMENT_CODE);//业务部门代号，默认phzc
		paramMap.put("cardSeqId",  getCardSeqId(req) == null ? "" : getCardSeqId(req));//卡序列号,对应CARD_BASE_INFO
		
		String mobileNo = req.getParameter("mobileNo") == null 
				? getUser(req).getMobileNo() : req.getParameter("mobileNo");
		paramMap.put("mobileNo", mobileNo);//接收短信手机号
		setSmsCode(req, paramMap);//短信验证码
		setSmsSeqId(req, paramMap);
		paramMap.put("chkValue", getMd5ChkValForRecharge(paramMap,req));
		return getHttpClientUtil().getUriByParms(paramMap);
	}
	
	public String getMd5ChkValForRechargeRecord(Map map) {
		String chkVal = getRechargeRecordOfMd5ChkValStr(map);
		try {
			chkVal = Md5Utils.md5(chkVal, "utf-8");
		} catch (NoSuchAlgorithmException e) {
			log.error("getMd5ChkValForRechargeRecord exception:"+e.getMessage());
		} catch (UnsupportedEncodingException e) {
			log.error("getMd5ChkValForRechargeRecord exception:"+e.getMessage());
		}
		return chkVal;
	}
	
	public String getMd5ChkValForRecharge(Map paramMap,HttpServletRequest req){
		if(!StringUtils.isBlank(req.getParameter("chkValue"))) {
			return req.getParameter("chkValue");
		}
		
		String chkVal = paramMap.get("merId").toString()
				+paramMap.get("cmdId").toString()
				+paramMap.get("version").toString()
				+paramMap.get("custId").toString()
				+paramMap.get("operId").toString()
				+paramMap.get("subAcctId").toString()
				+paramMap.get("transAmt").toString()
				+paramMap.get("payChnl").toString()
				+paramMap.get("reqDate").toString()
				+paramMap.get("reqSeqId").toString()
				+paramMap.get("sysId").toString()
				+paramMap.get("bedpId").toString()
				+paramMap.get("cardSeqId").toString()
				+paramMap.get("mobileNo").toString()
				+getSmsCode(paramMap)
				+getSmsSeqId(paramMap)
				+ProfileConstants.MER_KEY;
				log.info("getMd5ChkValForRecharge chkval="+chkVal);
		
		try {
			chkVal = Md5Utils.md5(chkVal, "utf-8");
		} catch (NoSuchAlgorithmException e) {
			log.error("getMd5ChkValForRecharge exception:"+e.getMessage());
		} catch (UnsupportedEncodingException e) {
			log.error("getMd5ChkValForRecharge exception:"+e.getMessage());
		}
		return chkVal;
	}
	
	public String getRechargeRecordUrl(HttpServletRequest req) {
		Map paramMap = new HashMap();
		paramMap.put("merId", req.getParameter("merId") == null ?  ProfileConstants.DEFAULT_MERID : req.getParameter("merId"));
		paramMap.put("cmdId", req.getParameter("cmdId") == null ? ProfileConstants.CMDID_QUERY_RECHARGE_LOG : req.getParameter("cmdId"));
		paramMap.put("version", req.getParameter("version") == null ? ProfileConstants.REQ_VERSION : req.getParameter("version"));
		paramMap.put("custId", getUser(req).getCustId());
		paramMap.put("currentPage", req.getParameter("currentPage"));
		paramMap.put("pageSize", req.getParameter("pageSize") == null ? ProfileConstants.DEFAULT_PAGE_SIZE : req.getParameter("pageSize"));
		paramMap.put("startDate", StringUtils.isBlank(DateUtils.formatDate(req.getParameter("startDate"))) ? DateUtils.getAboutDate(-7) : DateUtils.formatDate(req.getParameter("startDate")));
		paramMap.put("endDate", StringUtils.isBlank(DateUtils.formatDate(req.getParameter("endDate"))) ? DateUtils.getCurrentDate() : DateUtils.formatDate(req.getParameter("endDate")));
		/**
		 交易状态
		‘I’ – 初始状态
		‘S’ – 成功
		‘P’ – 处理中
		‘F’ – 充值失败
		‘I’ – 初始状态，用户已经提交
		‘H’ – 经办完成，审核过程中
		‘F’ – 审核失败
		*/
		if(!StringUtils.isBlank(req.getParameter("transStat"))) {
			paramMap.put("transStat", req.getParameter("transStat"));
		}
		paramMap.put("operId", getUser(req).getOperId());
		paramMap.put("payChnl", getPayChannel());
		paramMap.put("chkValue", getMd5ChkValForRechargeRecord(paramMap));//Md5 加签
		String url = getHttpClientUtil().getUriByParms(paramMap);
		log.info(url);
		return url;
	}
	
	public String getRechargeRecordOfMd5ChkValStr(Map paramMap) {
		Object tranS = paramMap.get("transStat");
		String transStat = tranS == null ? "" : tranS.toString();
		String chkVal = paramMap.get("merId").toString()
					  + paramMap.get("cmdId").toString()
					  + paramMap.get("version").toString()
					  //no need merPriv
					  + paramMap.get("custId").toString()
					  + paramMap.get("operId").toString()
					  + getPayChannel()
					  + transStat
					  + paramMap.get("startDate").toString()
					  + paramMap.get("endDate").toString()
					  + paramMap.get("currentPage").toString()
					  + paramMap.get("pageSize").toString() 
					  //no need reqExt
					  + ProfileConstants.MER_KEY;
		return chkVal;
	}
	
	public boolean isCorrectTradePassword(HttpServletRequest request,User user) throws ServiceException {
		String transPwd = request.getParameter("transPwd");
		
		//验证交易密码
		PwdInfoFacade pwdInfoFacade = (PwdInfoFacade)serviceClientFactory.getWebServiceClient(PwdInfoFacade.class,
            "pwdInfoFacade", "UBS");
		ValidTransPwdRequest validTransPwdRequest = new ValidTransPwdRequest();
		validTransPwdRequest.setCustId(getUser(request).getCustId());//客户号 
		validTransPwdRequest.setOperId(getUser(request).getOperId());//操作员id
		validTransPwdRequest.setTransPwd(transPwd);//交易密码
		validTransPwdRequest.setConsumerId(consumerId);
		PwdInfoResult validResult = pwdInfoFacade.validTransPwd(validTransPwdRequest);
		if(validResult==null || !ProfileConstants.RESPONSE_STATUS_SUCCESS.equals(validResult.getRespCode())){
			return false;
		}
		return true;
	}
	
	public JSONObject sendSmsForCashing(HttpServletRequest req) {
		JSONObject jsonObj = new JSONObject();

		String type = "1";// 1 公共短信服务 
		String random = CommonUtils.rands(6);
		DefaultConsoleSession.getInstance().setAttribute(req, ProfileConstants.CASHING_SMS_CODE, random);
		//SmsConstants.CASH_WITHDRAWAL; 
		String randoms = getSmsMsg("CASHWITHDRAWAL", random, getUser(req).getLoginId(), req.getParameter("transAmt"));//TODO SmsConstants.CASH_WITHDRAWAL
		log.info(randoms);
		sendMessage.send(randoms, null, type, getMobileNo(req));
		log.info("手机验证码：" + random);

		jsonObj.put(ConsoleConstants.ERROR_CODE, ConsoleConstants.ERROR_CODE_000);
		return jsonObj;
	}
	
	protected String getSmsMsg(String smsBiz,String smsCode, String loginId, String transAmt) {
		SmsConfig smsCfg = SmsConfig.getInstance();
		smsCfg.load();
		String smsTemplate = smsCfg.getString(smsBiz);
		String randoms = smsTemplate.replace("#MSGCODE#", smsCode).replace("#LOGINID#",loginId).replace("#TRANSAMT#", transAmt);
		return randoms;
	}
	
    /**
     * 获取请求流水号
     * @return
     */
    protected String getReqSeqId() {
    	return DateUtils.getCurrentDate()+RandomUtil.rands(22);
    }
	
	protected  User getUser(HttpServletRequest request) {
		return userSessionFacade.getUser(request);
	}
	
	public static HttpClientUtil getHttpClientUtil() {
		if(clientUtil == null) {
			clientUtil = new HttpClientUtil();
		}
		return clientUtil;
	}
	
	protected String getMobileNo(HttpServletRequest req) {
		return req.getParameter("mobileNo") == null 
				? DefaultConsoleSession.getInstance().getAttribute(req, getUser(req).getCustId()+"_"+getUser(req).getOperId()+"_mobileNo") : req.getParameter("mobileNo");
				
	}
	
	protected String getTradeForRechargeBaseUrl() {
		if(TRADE_RECHARGE_BASE_URL == null) {
			TRADE_RECHARGE_BASE_URL = applicationPropertyConfig.getDomainTradeForRecharge();
		}
		return TRADE_RECHARGE_BASE_URL;
	}
	
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	
	public String getSysId() {
		return sysId;
	}
	
	abstract String getPayChannel();
	abstract String getPgRetUrl();
	abstract void setSmsSeqId(HttpServletRequest req, Map paramMap);
	abstract void setSmsCode(HttpServletRequest req, Map paramMap);
	abstract String getSmsSeqId(Map paramMap);
	abstract String getSmsCode(Map paramMap);
	
	public abstract JSONObject sendSmsForRecharge(HttpServletRequest request) throws ServiceException, IOException;
	
	public void setServiceClientFactory(WebServiceClientFactory serviceClientFactory) {
		this.serviceClientFactory = serviceClientFactory;
	}


	public void setUserSessionFacade(UserSessionFacade userSessionFacade) {
		this.userSessionFacade = userSessionFacade;
	}


	public void setUserAuthorizeService(UserAuthorizeService userAuthorizeService) {
		this.userAuthorizeService = userAuthorizeService;
	}


	public void setSendMessage(SendMessage sendMessage) {
		this.sendMessage = sendMessage;
	}


	public void setApplicationPropertyConfig(
			ApplicationPropertyConfig applicationPropertyConfig) {
		this.applicationPropertyConfig = applicationPropertyConfig;
	}
	
	private static ProfileUtil getProfileUtil() {
		if(profileUtil == null)
			profileUtil =  new ProfileUtil();
		return profileUtil;
	}
}
