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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.phzc.ubs.common.facade.model.ModifyInvRequest;
import com.phzc.ubs.common.facade.model.ModifyInvResult;
import com.phzc.ubs.common.facade.model.OpenAccountInfoRequest;
import com.phzc.ubs.common.facade.model.OpenAccountInfoResult;
import com.phzc.ubs.common.facade.model.QueryInvByCustRequest;
import com.phzc.ubs.common.facade.model.QueryInvByCustResult;
import com.phzc.ubs.common.facade.model.QueryInvInfoRequest;
import com.phzc.ubs.common.facade.model.QueryInvInfoResult;
import com.phzc.ubs.common.facade.model.UserInfoDto;
import com.phzc.ubs.common.facade.model.UsrIdInfoRequest;
import com.phzc.ubs.common.facade.model.UsrIdInfoResult;
import com.phzc.ubs.common.facade.model.UsrIdentityDto;
import com.phzc.ubs.common.facade.service.AccountInfoFacade;
import com.phzc.ubs.common.facade.service.UserInfoFacade;

@Controller
public class IdentityController extends BaseController {

	private static final Logger logger = LoggerFactory
			.getLogger(IdentityController.class);
	private static int ID_LENGTH = 20;
	@Autowired
	private WebServiceClientFactory serviceClientFactory;

	@Autowired
	protected ApplicationPropertyConfig applicationPropertyConfig;
	@Autowired
	private SendMessage sendMessage;
	@Autowired
	private UserSessionFacade userSessionFacade;
	// 国政通校验服务
	@Autowired
	private UserAuthorizeService userAuthorizeService;
//	// 获得实名认证信息服务
//	@Autowired
//	private IdentityInfoServiceImpl identityInfoService;
	
	//序列号的服务
	@Autowired
	private SequenceService sequenceService;
	// UBS安全码
	private String ubsConsumerId = ConsolePropertiesManager.getUbsConsumerCode();
	/**
	 * 获得UserInfoFacade
	 * @return
	 */
	public UserInfoFacade getUserInfoService(){
		try{
			//获取UserInfoFacade
			UserInfoFacade userInfoService = (UserInfoFacade) serviceClientFactory.getWebServiceClient(UserInfoFacade.class, "userInfoFacade", "UBS");
			return userInfoService;
		} catch (Exception e) {
			logger.info("调用ubs服务失败");
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 特定合格投资人认证的入口，返回列表，如果为空则提示去合格认证
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/identity/queryIdentity.do", method = RequestMethod.GET)
	public String queryIndentityList(HttpServletRequest request, Model model) {
		model.addAttribute("config", applicationPropertyConfig);
	
		// User
		User userSession=userSessionFacade.getUser(request);
		String custId=userSession.getCustId();
		//获得认证信息
		QueryInvByCustRequest queryreq=new QueryInvByCustRequest();
		queryreq.setConsumerId(ubsConsumerId);
		queryreq.setCustId(custId);
		QueryInvByCustResult queryreqResult=getUserInfoService().queryInvByCust(queryreq);
		List<UsrIdentityDto> usrList=null;
		
		if(!"000".equals(queryreqResult.getRespCode())){
			return "identity/myIdentityError.ftl";
		}else{
			usrList=queryreqResult.getUsrIdentityInfoLst();
			for(UsrIdentityDto dto:queryreqResult.getUsrIdentityInfoLst()){
				String remarkCode=dto.getRemark();
				dto.setUsrName("*" + star(dto.getUsrName()).substring(1));
				if(null != remarkCode){
					dto.setRemark(IdentityUnPassCause.getCauseMap().get(remarkCode));
				}
			}
//			for(int index=0;index<usrList.size();index++){
//				String remarkCode=usrList.get(index).getRemark();
//				if(null != remarkCode){
//					usrList.get(index).setRemark(IdentityUnPassCause.getCauseMap().get(remarkCode));
//				}
//			}
		}
		// 获取认证信息
		model.addAttribute("userIdentityList", usrList);
		
		return "identity/myidentity1.ftl";
	}
	
	/**
	 * 特定合格投资人认证信息初始化
	 * @param request
	 * @param model
	 * @param identityId
	 * @return
	 * @throws ServiceException 
	 */
	@RequestMapping(value = "/identity/identityInit.do", method = RequestMethod.GET)
	public String indentityInfo(HttpServletRequest request, Model model,String idSid) throws ServiceException {
		model.addAttribute("config", applicationPropertyConfig);
		// User
		User userSession=userSessionFacade.getUser(request);
		String custId = userSession.getCustId();
		//编辑
		QueryInvInfoResult queryIdSidR=new QueryInvInfoResult();
		if (!StringUtils.isEmpty(idSid)){
			QueryInvInfoRequest queryid=new QueryInvInfoRequest();
			queryid.setConsumerId(ubsConsumerId);
			queryid.setIdSid(idSid);
			queryIdSidR=getUserInfoService().queryInvInfo(queryid);
			if("000".equals(queryIdSidR.getRespCode())){
//				if (!queryIdSidR.getCertId().equals(String.valueOf(custId))) {
//					throw new ServiceException(ErrorCode.ILLEGAL_REQUEST,"非法请求参数");
//				}else{
					queryIdSidR.setUsrName("*" + star(queryIdSidR.getUsrName()).substring(1));
					queryIdSidR.setCertId(star(queryIdSidR.getCertId()));
					model.addAttribute("userIdentity", queryIdSidR);
					model.addAttribute("idSid", idSid);
					return "identity/identityInit.ftl";
//				}
			}
		}
		
		/**
		 * isSid为空  防止直接输入地址进入首先检查用户有没有做过合格投资人认证
		 */
		QueryInvByCustRequest querylist=new QueryInvByCustRequest();
		querylist.setConsumerId(ubsConsumerId);
		querylist.setCustId(custId);
		QueryInvByCustResult queryreqResult=getUserInfoService().queryInvByCust(querylist);
		//查到数据 择取详情页面
		if("000".equals(queryreqResult.getRespCode())){
			for(UsrIdentityDto dto:queryreqResult.getUsrIdentityInfoLst()){
				if(dto.getIdType().equals("2")&&!dto.getIdentityStat().equals("3")){
					QueryInvInfoRequest queryid=new QueryInvInfoRequest();
					queryid.setConsumerId(ubsConsumerId);
					queryid.setIdSid(dto.getIdSid());
					queryIdSidR=getUserInfoService().queryInvInfo(queryid);
					model.addAttribute("userIdentity", getSartQueryInvInfoResult(queryIdSidR));
					return "identity/myidentity.ftl";
				}
			}
		}
		
		/**
		 * 第一次认证 如果实名过则显示实名信息
		 */
		UserInfoDto userInfoDto=getuserInfoDto(request);
		if(null!=userInfoDto){
			queryIdSidR.setUsrName(userInfoDto.getUsrName());
			queryIdSidR.setCertId(userInfoDto.getCertId());
			queryIdSidR.setUsrMp(userInfoDto.getUsrMp());
			queryIdSidR.setUsrEmail(userInfoDto.getUsrEmail());
		}
		model.addAttribute("userIdentity", queryIdSidR);
		return "identity/identityInit.ftl";
	}
	
	
	/**
	 * 特定合格投资人认证数据持久化
	 * @param request
	 * @param model
	 * @param identityForm
	 * @return
	 * @throws ServiceException 
	 */
	@RequestMapping(value = "/identity/identity.do", method = RequestMethod.POST)
	public String userInfo(HttpServletRequest request, Model model,IdentityInfo identityForm) throws ServiceException {
		model.addAttribute("config", applicationPropertyConfig);	
		//获得的用户
		User userSession=userSessionFacade.getUser(request);
		String custId = userSession.getCustId();
		String operId = userSession.getOperId();
		String telphone="";
		String respCode = "";
		String respDesc = "";
		String certType = "00";
		// 获得实名认证信息
		String userName = identityForm.getUsrName();
		String certId = identityForm.getCertId();
		// 查询用户实名信息状况
		boolean hasCertVerified = false;
		
		IdInfoRequest usrBaseReq = new IdInfoRequest();
		usrBaseReq.setCustId(custId);
		usrBaseReq.setOperId(operId);
		usrBaseReq.setConsumerId(ubsConsumerId);
		IdInfoResult usrBaseRslt = getUserInfoService().queryUserByCustId(usrBaseReq);
		UserInfoDto usrBaseInfo = null;
		if (null != usrBaseRslt && "000".equals(usrBaseRslt.getRespCode())) {
			System.out.println(usrBaseRslt.getRespCode());
			System.out.println(usrBaseRslt.getRespDesc());
			usrBaseInfo = usrBaseRslt.getList().get(0);
		}
		if(null!=usrBaseInfo){
			telphone=usrBaseInfo.getUsrMp();
		}
		logger.info("判断实名");
		// 新增实名认证状态位，根据此字段来查询是否通过实名认证
		if (null != usrBaseInfo && !StringUtils.isBlank( usrBaseInfo.getUsrName()) && !StringUtils.isBlank(usrBaseInfo.getCertId())) {
			userName = usrBaseInfo.getUsrName();
			certId = usrBaseInfo.getCertId();
			hasCertVerified = true;
		}
		logger.info(" 实名结束");
				
		logger.info("id verify -- before, id:" + certId + ", name: "+ userName);
		//严格进行身份证验证
		if (!hasCertVerified) {
			boolean verified = true;
			// （local环境下不验证）
			if(!applicationPropertyConfig.getEnv().equalsIgnoreCase(EnvEnum.LOCAL.getEnvString())){
				//国政通验证身份证
				UserAuthorizeInfo userAuthrizeInfo = new UserAuthorizeInfo();
				userAuthrizeInfo.setCertId(certId);
				userAuthrizeInfo.setCertName(userName);
				userAuthrizeInfo = userAuthorizeService.authorizeUser(userAuthrizeInfo);
			    respCode = userAuthrizeInfo.getRespCode();
	            respDesc = userAuthrizeInfo.getRespDesc();
				logger.info("id gzt verify data, id:" + certId + ", name: "+ userName + ", code: " + respCode + ", desc:" + respDesc);
				if (!"0".equals(respCode)) {
					verified = false;
					if ("50000".equals(respCode)) {
						identityForm.setErrorCode(String.valueOf(ErrorCode.OPEN_ACCT_FAIL));
						identityForm.setErrorString("实名认证失败");
						logger.info("实名认证出错，报错信息为：" + respDesc);
					} else if ("50001".equals(respCode)) {
						identityForm.setErrorCode(String.valueOf(ErrorCode.OPEN_ACCT_FAIL));
						identityForm.setErrorString("该身份证号已在平台认证，无法进行重复认证。");
						logger.info("实名认证出错，报错信息为：" + respDesc);
					} else if ("50002".equals(respCode)) {
						identityForm.setErrorCode(String.valueOf(ErrorCode.OPEN_ACCT_FAIL));
						identityForm.setErrorString("该身份证号和姓名已在平台完成认证，无法进行重复认证。");
						logger.info("实名认证出错，报错信息为：" + respDesc);
					}
					model.addAttribute("identityForm", identityForm);
					return "identity/smerror.ftl";
				}
			} else{
				logger.info("id local verify data, id:" + certId + ", name: "+ userName + ", code: " + respCode + ", desc:"	+ respDesc);
			}
			if(verified){
				// 更新用户实名信息
				String usrSex = "";
				String birthday = certId.substring(6, 10) + "-"+ certId.substring(10, 12) + "-" + certId.substring(12, 14);
				String sexFlag = certId.substring(certId.length() - 2,certId.length() - 1);
				if ((Integer.valueOf(sexFlag) % 2) != 0) {
					usrSex = "M";
				} else {
					usrSex = "F";
				}
				ModifyCertiInfoRequest certReq = new ModifyCertiInfoRequest();
				certReq.setCustId(custId);
				certReq.setRealName(userName);
				certReq.setCertType(certType);
				certReq.setCertNo(certId);
				certReq.setBirthday(birthday);
				certReq.setUsrSex(usrSex);
				certReq.setConsumerId(ubsConsumerId);
				// 调用ubs服务的更新
				ModifyCertiInfoResult certiRslt = getUserInfoService().modifyCertiInfo(certReq);
				if (null != certiRslt) {
					respCode = certiRslt.getRespCode();
					respDesc = certiRslt.getRespDesc();
					if (!"000".equals(respCode)) {
						identityForm.setErrorCode(String.valueOf(ErrorCode.OPEN_ACCT_FAIL));
						identityForm.setErrorString("开户异常，请稍后重试");
						logger.info("更新用户实名信息出错，报错信息为：" + respDesc);
						model.addAttribute("identityForm", identityForm);
						return "identity/smerror.ftl";
					}
				} else {
					identityForm.setErrorString("开户异常，请稍后重试");
					model.addAttribute("identityForm", identityForm);
					logger.info("更新用户实名信息出错，请稍后重试");
					return "identity/smerror.ftl";
				}
				
				//开户
				AccountInfoFacade ubsAcctService = (AccountInfoFacade) serviceClientFactory.
						getWebServiceClient(AccountInfoFacade.class, "accountInfoFacade", "UBS");
				OpenAccountInfoRequest acctReq = new OpenAccountInfoRequest();
				acctReq.setCustId(custId);
				acctReq.setAcctType("BASEDA");
				acctReq.setDcFlag("D");
				acctReq.setAcctAlias("def");
				acctReq.setConsumerId(ubsConsumerId);
				logger.info("OpenAccountInfoRequest: " + JSON.toJSONString(acctReq));
				OpenAccountInfoResult openAccountInfoResult = ubsAcctService.openAccountInfo(acctReq);
				if (null == openAccountInfoResult || !"000".equals(openAccountInfoResult.getRespCode())) {
					logger.info((null == openAccountInfoResult) ? "开通账户异常，请稍后重试" : openAccountInfoResult.getRespDesc());
					identityForm.setErrorCode(String.valueOf(ErrorCode.OPEN_ACCT_FAIL));
					identityForm.setErrorString((null == openAccountInfoResult) ? "开通账户异常，请稍后重试" : openAccountInfoResult.getRespDesc());
					model.addAttribute("identityForm", identityForm);
					return "identity/smerror.ftl";
				} else {
					logger.info("开户成功: " + custId);
				}
				
			}
		}
		//
		validateParame(identityForm);
		
		IdentityInfo identityInfo=new IdentityInfo();
		identityInfo.setUsrMp(telphone);
		// 获得认证类型 目前为特定合格人S
		String idType =identityForm.getIdType();
		String idSid=identityForm.getIdSid();
		//如果idsid不为空 更新
		if(null!=idSid&&!"".equals(idSid)){
			//update
			ModifyInvRequest req=new ModifyInvRequest();
			identityInfo.setIdSid(idSid);
			req.setConsumerId(ubsConsumerId);
			req.setIdSid(idSid);
			req.setOrgName(identityForm.getOrgName());
			if(null != identityForm.getAssetSal()){
				req.setAssetSal(identityForm.getAssetSal());
			}else{
				req.setAssetSal("0");
			}
			if(null != identityForm.getAssetTot()){
				req.setAssetTot(identityForm.getAssetTot());
			}else{
				req.setAssetTot("0");
			}
			ModifyInvResult result;
			result=getUserInfoService().modifyUsrIdInfo(req);
			if("000".equals(result.getRespCode())){
				//发短信
				String random = CommonUtils.rands(6);
				DefaultConsoleSession.getInstance().setAttribute(request, SmsConstants.SESSION_MESSAGE_CODE, random);
				sendMessage.send(getSmsMsg(random,SmsConstants.BIZ_INVESTOR_CERTIFICATION_SMS), custId, "1",telphone);
				model.addAttribute("identityInfo", identityInfo);

				return "identity/identityRes.ftl";
			}else{
				logger.info(result.getRespCode());
				identityInfo.setErrorString(result.getRespDesc());
				model.addAttribute("identityForm", identityInfo);
				return "identity/smerror.ftl";
			}
		}else{
			
			//insert
			String identiType = idType.toString()+ "C".toString();
			idSid = createId(ProfileConstants.prdtOrderId, identiType);
			String random = CommonUtil.rands(4);
			System.out.println("认证编号是:::::::" + idSid + random);
			identityInfo.setIdSid(idSid);
			UsrIdInfoRequest req=new UsrIdInfoRequest();
			req.setConsumerId(ubsConsumerId);
			req.setIdSid(idSid+random);
			req.setCustId(custId);
			req.setIdType(idType);
			req.setIsAgree(identityForm.getIsAgree());
			req.setOrgName(identityForm.getOrgName());
			if(null != identityForm.getAssetSal()){
				req.setAssetSal(identityForm.getAssetSal());
			}else{
				req.setAssetSal("0");
			}
			if(null != identityForm.getAssetTot()){
				req.setAssetTot(identityForm.getAssetTot());
			}else{
				req.setAssetTot("0");
			}
			UsrIdInfoResult result;
			
			result=getUserInfoService().applyUsrIdCerti(req);
			if("000".equals(result.getRespCode())){
				//发短信
				random = CommonUtils.rands(6);
				DefaultConsoleSession.getInstance().setAttribute(request, SmsConstants.SESSION_MESSAGE_CODE, random);
				sendMessage.send(getSmsMsg(random,SmsConstants.BIZ_INVESTOR_CERTIFICATION_SMS), custId, "1",telphone);
				model.addAttribute("identityInfo", identityInfo);

				return "identity/identityRes.ftl";
			}else{
				logger.info(result.getRespCode());
				identityInfo.setErrorString(result.getRespDesc());
				model.addAttribute("identityForm", identityInfo);
				return "identity/smerror.ftl";
				
			}
		}
		
	}
	
	/**
	 * 特定合格投资人认证信息查看
	 * @param request
	 * @param model
	 * @param identityId
	 * @return
	 */
	@RequestMapping(value = "/identity/myIdentity.do", method = RequestMethod.GET)
	public String queryIndentity(HttpServletRequest request, Model model,String idSid) {
		model.addAttribute("config", applicationPropertyConfig);
		//编辑
		QueryInvInfoResult queryIdSidR=new QueryInvInfoResult();
		QueryInvInfoRequest queryid=new QueryInvInfoRequest();
		queryid.setConsumerId(ubsConsumerId);
		queryid.setIdSid(idSid);
		queryIdSidR=getUserInfoService().queryInvInfo(queryid);
		if("000".equals(queryIdSidR.getRespCode())){
			
			model.addAttribute("userIdentity", getSartQueryInvInfoResult(queryIdSidR));
			return "identity/myidentity.ftl";
		}else{
			return "identity/myIdentityError.ftl";
		}
		
	}
	
	
	
	/**
	 * 微信特定合格投资人认证入口
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/m/identity/queryIdentity.do", method = RequestMethod.GET)
	public String identityInit_M(HttpServletRequest request, Model model,@RequestParam(required = false) String source){
			if (!StringUtils.isEmpty(source)) {
				model.addAttribute("source", source);
			}	
			model.addAttribute("config", applicationPropertyConfig);
			// User
			User userSession=userSessionFacade.getUser(request);
			IdentityInfo userIdentity =new IdentityInfo();
			String usrName="";
			String certId="";
			String custId=userSession.getCustId();
			//获取user信息
			IdInfoRequest idInfoRequest = new IdInfoRequest();
			idInfoRequest.setCustId(userSession.getCustId());
			idInfoRequest.setConsumerId(ubsConsumerId);
			IdInfoResult idInfoResult = getUserInfoService().queryUserByCustId(idInfoRequest);
			//如果没有实名则去实名页面
			if(null!=idInfoResult){
				List<UserInfoDto> userInfoDtoList = idInfoResult.getList();
				if(null!=userInfoDtoList){
					if(null!=userInfoDtoList.get(0)){
						usrName = userInfoDtoList.get(0).getUsrName();
						certId = userInfoDtoList.get(0).getCertId();
						if(null!=usrName&&!"".equals(usrName)&&null!=certId&&!"".equals(certId)){
							userIdentity.setUsrName("*"+star(usrName).substring(1));
							userIdentity.setCertId(star(certId));
						}else{
							return "account/m/openAccount_m.ftl";
						}
					}else{
						//返回到错误页面
						return "identity/m/identity_error_m.ftl";
					}
				}else{
					//返回到错误页面
					return "identity/m/identity_error_m.ftl";
				}
				
			}else{
				//返回到错误页面
				return "identity/m/identity_error_m.ftl";
			}
			
			usrName=userIdentity.getUsrName();
			certId=userIdentity.getCertId();
			model.addAttribute("userIdentity", userIdentity);
			
			//获得认证信息
			QueryInvByCustRequest queryreq=new QueryInvByCustRequest();
			queryreq.setConsumerId(ubsConsumerId);
			queryreq.setCustId(custId);
			queryreq.setIdType(UserIdentityType.SPEC_QUALI_INVESTORS.getCode());
			QueryInvByCustResult queryreqResult=getUserInfoService().queryInvByCust(queryreq);
			
			if("000".equals(queryreqResult.getRespCode())){
				if(queryreqResult.getUsrIdentityInfoLst().size()>0){
					QueryInvInfoResult queryIdSidR=new QueryInvInfoResult();
						QueryInvInfoRequest queryid=new QueryInvInfoRequest();
						queryid.setConsumerId(ubsConsumerId);
						queryid.setIdSid(queryreqResult.getUsrIdentityInfoLst().get(0).getIdSid());
						queryIdSidR=getUserInfoService().queryInvInfo(queryid);
						if("000".equals(queryIdSidR.getRespCode())){
							
							model.addAttribute("userIdentity", getSartQueryInvInfoResult(queryIdSidR));
							//返回到查看界面
							return "identity/m/myidentity_m.ftl";
						}
				}
			}
		
			return "identity/m/identityInit_m.ftl";
	}

	
	/**
	 * 微信版实名认证数据持久化
	 * @param request
	 * @param model
	 * @param identityForm
	 * @return
	 */
	@RequestMapping(value = "/m/identity/identity.do", method = RequestMethod.POST)
	public String userInfo_M(HttpServletRequest request, Model model,IdentityInfo identityForm,String type,String source) {
		model.addAttribute("config", applicationPropertyConfig);
		//获得的用户
		User userSession=userSessionFacade.getUser(request);
		String custId = userSession.getCustId();
		String operId = userSession.getOperId();
		String telphone="";
		String idSid="";
		// 查询用户实名信息状况
		UserInfoFacade ubsUserService = null;
		try {
			ubsUserService = (UserInfoFacade) serviceClientFactory
					.getWebServiceClient(UserInfoFacade.class, "userInfoFacade", "UBS");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}

		IdInfoRequest usrBaseReq = new IdInfoRequest();
		usrBaseReq.setCustId(custId);
		usrBaseReq.setOperId(operId);
		usrBaseReq.setConsumerId(ubsConsumerId);
		IdInfoResult usrBaseRslt = ubsUserService.queryUserByCustId(usrBaseReq);
		UserInfoDto usrBaseInfo = null;
		if (null != usrBaseRslt && "000".equals(usrBaseRslt.getRespCode())) {
			System.out.println(usrBaseRslt.getRespCode());
			System.out.println(usrBaseRslt.getRespDesc());
			usrBaseInfo = usrBaseRslt.getList().get(0);
		}
		if(null!=usrBaseInfo){
			telphone=usrBaseInfo.getUsrMp();
		}
		logger.info("判断实名");
		try{
			
			// 新增实名认证状态位，根据此字段来查询是否通过实名认证
			if (null != usrBaseInfo && !StringUtils.isBlank( usrBaseInfo.getUsrName()) && !StringUtils.isBlank(usrBaseInfo.getCertId())) {
				IdentityInfo identityInfo=new IdentityInfo();
				
				String idType=UserIdentityType.SPEC_QUALI_INVESTORS.getCode();
				//insert
				String identiType = idType.toString()+ "C".toString();
				idSid = createId(ProfileConstants.prdtOrderId, identiType);
				String random = CommonUtil.rands(4);
				idSid=idSid+random;
				System.out.println("认证编号是:::::::" + idSid + random);
				identityInfo.setIdSid(idSid);
				UsrIdInfoRequest req=new UsrIdInfoRequest();
				req.setConsumerId(ubsConsumerId);
				req.setIdSid(idSid);
				req.setCustId(custId);
				req.setIdType(idType);
				req.setIsAgree(identityForm.getIsAgree());
				req.setOrgName(identityForm.getOrgName());
				if(null != identityForm.getAssetSal()&&!"".equals(identityForm.getAssetSal())){
					req.setAssetSal(identityForm.getAssetSal());
				}else{
					req.setAssetSal("0");
				}
				if(null != identityForm.getAssetTot()&&!"".equals(identityForm.getAssetTot())){
					req.setAssetTot(identityForm.getAssetTot());
				}else{
					req.setAssetTot("0");
				}
				UsrIdInfoResult result;
				
				result=getUserInfoService().applyUsrIdCerti(req);
				if("000".equals(result.getRespCode())){
					//发短信
					random = CommonUtils.rands(6);
					DefaultConsoleSession.getInstance().setAttribute(request, SmsConstants.SESSION_MESSAGE_CODE, random);
					sendMessage.send(getSmsMsg(random,SmsConstants.BIZ_INVESTOR_CERTIFICATION_SMS), custId, "1",telphone);
					model.addAttribute("identityInfo", identityInfo);

//					return "identity/identityRes_m.ftl";
				}else{
					logger.info(result.getRespCode());
					identityInfo.setErrorString(result.getRespDesc());
					model.addAttribute("identityForm", identityInfo);
					return "identity/m/identity_error_m.ftl";
					
				}
			}else{
				//没有实名认证
				return "identity/m/identity_error_m.ftl";
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		if(null!=source&&!"".equals(source)){
			return "redirect:"+source;
		}else{
			QueryInvInfoResult queryIdSidR=new QueryInvInfoResult();
			QueryInvInfoRequest queryid=new QueryInvInfoRequest();
			queryid.setConsumerId(ubsConsumerId);
			queryid.setIdSid(idSid);
			queryIdSidR=getUserInfoService().queryInvInfo(queryid);
			if("000".equals(queryIdSidR.getRespCode())){
				model.addAttribute("userIdentity", getSartQueryInvInfoResult(queryIdSidR));
				//返回到查看界面
				return "identity/m/myidentity_m.ftl";
			}
			return "identity/m/myidentity_m.ftl";
		}	
	}
	
	
	/**
	 * 微信我的认证页面
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/m/identity/identityCenter.do", method = RequestMethod.GET)
	public String identityCenter_M(HttpServletRequest request,Model model) {
		
		model.addAttribute("config", applicationPropertyConfig);
		model.addAttribute("realFlag", false);
		User user = userSessionFacade.getUser(request);
		String custId = user.getCustId();
		String operId=user.getOperId();
		
		//查下用户是否实名验证
		IdInfoRequest usrBaseReq = new IdInfoRequest();
		usrBaseReq.setCustId(custId);
		usrBaseReq.setOperId(operId);
		usrBaseReq.setConsumerId(ubsConsumerId);
		IdInfoResult usrBaseRslt = getUserInfoService().queryUserByCustId(usrBaseReq);
		UserInfoDto usrBaseInfo = new UserInfoDto();
		if (null != usrBaseRslt.getList()) {
			usrBaseInfo = usrBaseRslt.getList().get(0);
		}
		if (null != usrBaseInfo&&!StringUtils.isBlank(usrBaseInfo.getUsrName())&& !StringUtils.isBlank(usrBaseInfo.getCertId())) {
			model.addAttribute("realFlag", true);
		} 
		
		return "identity/m/identification_center_m.ftl";
	}
	
	
	/**
	 * 创建id
	 * @param seqName
	 * @param productType
	 * @return
	 * @throws ServiceException
	 */
	public String createId(String seqName, String productType) throws ServiceException {
		int length = 0;
		StringBuilder builder = new StringBuilder(ID_LENGTH);
		if (StringUtils.isNotBlank(productType)) {
			builder.append(productType);
			length = length + productType.length();
		}
		builder.append(DateUtils.getCurrentDate());
		length = length + 8;

		builder.append(ProfileUtil.leftPadding(
				String.valueOf(sequenceService.getSequenceNextval(seqName)), ID_LENGTH - length, '0'));
		return builder.toString();
	}
	
	
	/**
	 * 显示星号数量的方法
	 * @param oldStr
	 * @return
	 */
	public String star(String oldStr) {
		String newStr = "";// 要显示的数字串
		if (oldStr.length() < 3) {
			newStr = oldStr;
		} else {
			String begin = oldStr.substring(0, 1);// 头字符
			String end = oldStr.substring(oldStr.length() - 1);// 尾字符
			String starStr = "";// "*"数量
			for (int i = 0; i < oldStr.length() - 2; i++) {
				starStr += "*";
			}
			newStr = begin + starStr + end;
		}
		return newStr;
	}
	
	
	/**
	 * 发短信随机数
	 * @param random
	 * @param smsBiz
	 * @return
	 */
	private String getSmsMsg(String random, String smsBiz) {
		SmsConfig smsCfg = SmsConfig.getInstance();
		smsCfg.load();
		String smsTemplate = smsCfg.getString(smsBiz);
		String randoms = smsTemplate.replace("#MSGCODE#", random);
		return randoms;
	}
	
	
	/**
	 * 获得实名之后处理信息
	 * @param request
	 * @return
	 */
	public UserInfoDto getuserInfoDto(HttpServletRequest request){
		User userSession=userSessionFacade.getUser(request);
		try{
			UserInfoFacade userInfoService = null;
			userInfoService = (UserInfoFacade) serviceClientFactory.getWebServiceClient(UserInfoFacade.class, "userInfoFacade", "UBS");
			// 获取user信息
			IdInfoRequest idInfoRequest = new IdInfoRequest();
			idInfoRequest.setCustId(userSession.getCustId());
			idInfoRequest.setConsumerId(ubsConsumerId);
			IdInfoResult idInfoResult = userInfoService.queryUserByCustId(idInfoRequest);
			List<UserInfoDto> userInfoDtoList = idInfoResult.getList();
			UserInfoDto userInfoDto=null;
			if (null != userInfoDtoList) {
				userInfoDto = userInfoDtoList.get(0);
				String usrName = userInfoDto.getUsrName();
				String certId = userInfoDto.getCertId();
				String usrMp = userInfoDto.getUsrMp();
				String usrEmail = userInfoDto.getUsrEmail();
				if (null != usrName && !StringUtils.isBlank(usrName)) {
					userInfoDto.setUsrName("*" + star(usrName).substring(1));
				}
				if (null != certId && !StringUtils.isBlank(certId)) {
					userInfoDto.setCertId(star(certId));
				}
				if (null != usrMp && !StringUtils.isBlank(usrMp)&& usrMp.length() > 10) {
					usrMp = usrMp.substring(0, 3) + "****"+ usrMp.substring(7);
					userInfoDto.setUsrMp(usrMp);
				}
				if (null != usrEmail && !StringUtils.isBlank(usrMp)) {
					String begin = star(usrEmail.substring(0,usrEmail.indexOf("@")));
					String end = usrEmail.substring(usrEmail.indexOf("@"));
					userInfoDto.setUsrEmail(begin + end);
				}
				
			}
			return userInfoDto;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 校验isAggree
	 * @param identityInfo
	 * @throws ServiceException
	 */
	private void validateParame(IdentityInfo identityInfo)
			throws ServiceException {
		String isAgree=identityInfo.getIsAgree();
		if (!"3".equals(isAgree)) {
			throw new ServiceException(ErrorCode.VALIDATOR_PARAM_ERROR,
					"请同意并勾选协议");
		}}
	
	/**
	 * 星化数据
	 * @param queryIdSidR
	 * @return
	 */
	public QueryInvInfoResult getSartQueryInvInfoResult(QueryInvInfoResult queryIdSidR){
		
		if(null!=queryIdSidR){
			String userName=queryIdSidR.getUsrName();
			String certId=queryIdSidR.getCertId();
			String tel=queryIdSidR.getUsrMp();
			if(null!=userName&&!"".equals(userName))
				queryIdSidR.setUsrName("*"+star(userName).substring(1));
			if(null!=certId&&!"".equals(certId))
				queryIdSidR.setCertId("*"+star(certId).substring(1));
			if(null!=tel&&!"".equals(tel))
				queryIdSidR.setUsrMp(tel.substring(0, 3) + "****"+ tel.substring(7));
		}
		
		return queryIdSidR;
	}
}
