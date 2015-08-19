package com.phzc.profile.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.phzc.console.CertifyLevel;
import com.phzc.console.CertifyRequired;
import com.phzc.console.EnvEnum;
import com.phzc.console.base.exception.ErrorCode;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.console.util.ConsolePropertiesManager;
import com.phzc.profile.api.vo.UserAuthorizeInfo;
import com.phzc.profile.biz.service.UserAuthorizeService;
import com.phzc.profile.web.form.BankCardBindForm;
import com.phzc.ubs.common.facade.model.IdInfoRequest;
import com.phzc.ubs.common.facade.model.IdInfoResult;
import com.phzc.ubs.common.facade.model.ModifyCertiInfoRequest;
import com.phzc.ubs.common.facade.model.ModifyCertiInfoResult;
import com.phzc.ubs.common.facade.model.OpenAccountInfoRequest;
import com.phzc.ubs.common.facade.model.OpenAccountInfoResult;
import com.phzc.ubs.common.facade.model.PwdInfoResult;
import com.phzc.ubs.common.facade.model.SetTransPwdRequest;
import com.phzc.ubs.common.facade.model.UserInfoDto;
import com.phzc.ubs.common.facade.service.AccountInfoFacade;
import com.phzc.ubs.common.facade.service.PwdInfoFacade;
import com.phzc.ubs.common.facade.service.UserInfoFacade;

@Controller("openAccountController")
public class OpenAccountController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(OpenAccountController.class);
	
	private static final String SET_PWD_LEGAL_FLAG = "setPwdLegalFlag";
	
	@Autowired
	private UserAuthorizeService userAuthorizeService;

	// UBS安全码
	private String ubsConsumerId = ConsolePropertiesManager.getUbsConsumerCode();
	
	@RequestMapping(value = "/openAccount/openAccount.do")
	public String openAccount(Model model , HttpServletRequest request , BankCardBindForm cardForm){
		model.addAttribute("config", applicationPropertyConfig);
		//BankCardForm cardForm = (BankCardForm) form;
		User user = userSessionFacade.getUser(request);
	    String custId = user.getCustId();
        String operId = user.getOperId();
        String cardNm = cardForm.getRealName();
		String certId = cardForm.getCertId();
        if(StringUtils.isBlank(cardNm))
        	cardNm = request.getParameter("cerName");
        if(StringUtils.isBlank(certId))
        	certId = request.getParameter("cerId");
        
        if(null == certId){
	        
	        try{
	            int certRslt = 0;  //0:实名、交易密码和开户都未做；1：实名认证通过，未设置交易密码，未开户；2：实名认证通过，已设置交易密码，未开户。
	            String certNm = "";
	            String certiId = "";
	            String transPwdSet = "";
	            
	            //查询用户实名信息状况，交易密码是否设置
	            UserInfoFacade ubsUserService = (UserInfoFacade)serviceClientFactory.getWebServiceClient(UserInfoFacade.class,
	                "userInfoFacade", "UBS");
	            IdInfoRequest usrBaseReq = new IdInfoRequest();
	            usrBaseReq.setCustId(custId);
	            usrBaseReq.setOperId(operId);
	            usrBaseReq.setConsumerId(ubsConsumerId);
	            IdInfoResult usrBaseRslt = ubsUserService.queryUserByCustId(usrBaseReq);
	            UserInfoDto usrBaseInfo = new UserInfoDto();
	            if(null != usrBaseRslt.getList()){
	                usrBaseInfo = usrBaseRslt.getList().get(0);
	            }
	            
	            if(null == usrBaseInfo){
	                certRslt = 0;
	            }else{
	            	//新增实名认证状态位，根据此字段来查询是否通过实名认证
	                certNm = usrBaseInfo.getUsrName();
	                certiId = usrBaseInfo.getCertId();
	                
	                transPwdSet = usrBaseInfo.getIsTransPwdSet();
	                
	                if(StringUtils.isBlank(certNm) && StringUtils.isBlank(certiId)){
	                    certRslt = 0;
	                }else{
	                    certNm = "**" + certNm.substring(certNm.length()-1, certNm.length());
	                    certiId = certiId.substring(0, 1) + "****************" + certiId.substring(certiId.length()-1, certiId.length());
	                    
	                    if("0".equals(transPwdSet)){
	                        certRslt = 1;
	                    }
    	                else{
    	                	 certRslt = 2;
    	                	 cardForm.setRealName(certNm);
    	                	 cardForm.setCertId(certiId);
    	                	 model.addAttribute("cardForm", cardForm);
    	             		 return "account/openAccountSuc.ftl";
    	                }
	                }
	            }
	            
	            model.addAttribute("certNm", certNm);
	            model.addAttribute("certId", certiId);
	            model.addAttribute("certRslt", certRslt);
	            
	        }catch (Exception e){
	        	logger.error(e.getMessage(), e);
	        	return "account/openAccount.ftl";//todo  应该跳转到错误页面
	        }
	        return "account/openAccount.ftl";   //跳转到开户界面
	    }
        
	    /*提交开户操作*/
		String certType = "00";
		String newTransPwd = cardForm.getTransPwd();
		String respCode = "";
        String respDesc = "";
        String certFlag = (null==cardForm.getCertStatus())?"0":cardForm.getCertStatus();
        model.addAttribute("certRslt", certFlag);
        model.addAttribute("certNm", cardNm);
        model.addAttribute("certId", certId);
		
		try {
			// 查询用户实名信息状况
			boolean hasCertVerified = false;
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

			// 新增实名认证状态位，根据此字段来查询是否通过实名认证
			if (null != usrBaseInfo && !StringUtils.isBlank( usrBaseInfo.getUsrName()) && !StringUtils.isBlank(usrBaseInfo.getCertId())) {
				cardNm = usrBaseInfo.getUsrName();
				certId = usrBaseInfo.getCertId();
				hasCertVerified = true;
			}
			logger.info("id verify -- before, id:" + certId + ", name: "+ cardNm);
			
			if(!hasCertVerified){
			//1.实名认证及更新信息
			if("0".equals(certFlag) && StringUtils.isNotBlank(cardNm) && StringUtils.isNotBlank(certId)){
				//国政通验证身份证
              //严格进行身份证验证（local环境下不验证）
    			if (!applicationPropertyConfig.getEnv().equalsIgnoreCase(EnvEnum.LOCAL.getEnvString())) {
    				//国政通验证身份证
    				UserAuthorizeInfo userAuthrizeInfo = new UserAuthorizeInfo();
    				userAuthrizeInfo.setCertId(certId);
    				userAuthrizeInfo.setCertName(cardNm);
    				userAuthorizeService.authorizeUser(userAuthrizeInfo);
    			    respCode = userAuthrizeInfo.getRespCode();
                    respDesc = userAuthrizeInfo.getRespDesc();
                    
    				if (!"0".equals(respCode)) {
    					if ("50000".equals(respCode)) {
    						cardForm.setErrorCode(String.valueOf(ErrorCode.OPEN_ACCT_FAIL));
    						cardForm.setErrorString("实名认证失败");
    						logger.info("实名认证出错，报错信息为：" +respCode+":"+ respDesc);
    					} else if ("50001".equals(respCode)) {
    						cardForm.setErrorCode(String.valueOf(ErrorCode.OPEN_ACCT_FAIL));
    						cardForm.setErrorString("身份证号码已经被实名认证，请检查身份证号是否输入正确后重新认证。如有疑问，请与客服联系");
    						logger.info("实名认证出错，报错信息为：" + respCode+":"+ respDesc);
    					} else if ("50002".equals(respCode)) {
    						cardForm.setErrorCode(String.valueOf(ErrorCode.OPEN_ACCT_FAIL));
    						cardForm.setErrorString("身份证号码已经被实名认证，请检查身份证号是否输入正确后重新认证。如有疑问，请与客服联系");
    						logger.info("实名认证出错，报错信息为：" + respCode+":"+ respDesc);
                       }else if("50003".equals(respCode)){
                    	   cardForm.setErrorCode(String.valueOf(ErrorCode.OPEN_ACCT_FAIL));
                           cardForm.setErrorString("身份证号码和姓名不一致，请检查姓名和身份证号后，重新认证");
                           logger.info("实名认证出错，报错信息为：" + respCode+":"+ respDesc);
                       }
    					int certRslt = 4;
    					model.addAttribute("certRslt", certRslt);
    					model.addAttribute("respDesc", respDesc);
                        model.addAttribute("cardForm", cardForm);
                        return "account/openAccount.ftl";
                    }
    			}
    			
    			//更新用户实名信息
    			try {
    				ubsUserService = (UserInfoFacade)serviceClientFactory.getWebServiceClient(UserInfoFacade.class,"userInfoFacade", "UBS");
    				usrBaseReq = new IdInfoRequest();
    		            usrBaseReq.setCustId(custId);
    		            usrBaseReq.setOperId(operId);
    		            usrBaseReq.setConsumerId(ubsConsumerId);
    		            usrBaseRslt = ubsUserService.queryUserByCustId(usrBaseReq);
    		            usrBaseInfo = new UserInfoDto();
    		            if(null != usrBaseRslt.getList()){
    		                usrBaseInfo = usrBaseRslt.getList().get(0);
    		            }
    		            if(null != usrBaseInfo){
    		            	if(StringUtils.isNotBlank(usrBaseInfo.getUsrName()) ){
    		            		cardNm = usrBaseInfo.getUsrName();
    		            	}
    		            	if(StringUtils.isNotBlank(usrBaseInfo.getCertId())){
    		            		certId = usrBaseInfo.getCertId();
    		            	}
    		            }
    			}catch (ServiceException e) {
    				logger.error("用户实名信息查询出错了", e);
    				e.printStackTrace();
    				model.addAttribute("cardForm", cardForm);
    	    		return "account/openAccount.ftl";   
    			}
    			String usrSex = "";
    			
    			String birthday = certId.substring(6, 10) + "-" + certId.substring(10, 12) + "-" + certId.substring(12, 14);
    			String sexFlag = certId.substring(certId.length()-2, certId.length()-1);
    			if ((Integer.valueOf(sexFlag) % 2) != 0) {
    				usrSex = "M";
    			} else {
    				usrSex = "F";
    			}
    			/** 实名存储  */
    			ubsUserService = (UserInfoFacade)serviceClientFactory.getWebServiceClient(UserInfoFacade.class, "userInfoFacade", "UBS");
    			ModifyCertiInfoRequest certReq = new ModifyCertiInfoRequest();
    			certReq.setCustId(custId);
    			certReq.setRealName(cardNm);
    			certReq.setCertType(certType);
    			certReq.setCertNo(certId);
    			certReq.setBirthday(birthday);
    			certReq.setUsrSex(usrSex);
    			certReq.setConsumerId(ubsConsumerId);
    			ModifyCertiInfoResult certiRslt = ubsUserService.modifyCertiInfo(certReq);
    			if(null != certiRslt){
    			    respCode = certiRslt.getRespCode();
                    respDesc = certiRslt.getRespDesc();
                    if(!"000".equals(respCode)){
                        cardForm.setErrorCode(String.valueOf(ErrorCode.OPEN_ACCT_FAIL));
                        cardForm.setErrorString("开户异常，请稍后重试");
                        logger.info("更新用户实名信息出错，报错信息为：" + respCode+":"+ respDesc);
                        int certRslt = 5;
                		model.addAttribute("certRslt", certRslt);
                		model.addAttribute("respDesc", respDesc);
                        model.addAttribute("cardForm", cardForm);
                        return "account/openAccount.ftl";
                    } 
                    
                    
    			}else{
    				cardForm.setErrorCode("099");
                    cardForm.setErrorString("开户异常，请稍后重试");
                    logger.info("实名认证接口异常，请稍后重试");
                    int certRslt = 5;
            		model.addAttribute("certRslt", certRslt);
            		model.addAttribute("respDesc", "实名认证接口异常，请稍后重试");
                    model.addAttribute("cardForm", cardForm);
                    return "account/openAccount.ftl";
    			}
			}
			
		}
			
			//2、开户
			AccountInfoFacade ubsAcctService = (AccountInfoFacade)serviceClientFactory.getWebServiceClient(AccountInfoFacade.class,"accountInfoFacade", "UBS");
			OpenAccountInfoRequest acctReq = new OpenAccountInfoRequest();
			acctReq.setCustId(custId);
			acctReq.setAcctType("BASEDA");
			acctReq.setDcFlag("D");
			acctReq.setAcctAlias("def");
			acctReq.setConsumerId(ubsConsumerId);
			OpenAccountInfoResult acctRslt = ubsAcctService.openAccountInfo(acctReq);
			
			if(null != acctRslt){
			    respCode = acctRslt.getRespCode();
			    respDesc = acctRslt.getRespDesc();
			    if(!"000".equals(respCode) && !"363".equals(respCode)){
			        cardForm.setErrorCode(String.valueOf(ErrorCode.OPEN_ACCT_FAIL));
			        cardForm.setErrorString("开通账户异常，请稍后重试");
			        logger.info("开通账户出错，报错信息为：" + respCode+":"+ respDesc);
                    int certRslt = 6;
            		model.addAttribute("certRslt", certRslt);
            		model.addAttribute("respDesc", respDesc);
                    model.addAttribute("cardForm", cardForm);
                    return "account/openAccount.ftl";
			    }
			}else{
				cardForm.setErrorCode("099");
                cardForm.setErrorString("开户时开通账户异常，请稍后重试");
                logger.info("开户时开通账户异常，请稍后重试");
                int certRslt = 6;
        		model.addAttribute("certRslt", certRslt);
        		model.addAttribute("respDesc", "开户时开通账户异常，请稍后重试");
                model.addAttribute("cardForm", cardForm);
                return "account/openAccount.ftl";
			}
			
			//3.开户完成设置交易密码
			if(!StringUtils.isBlank(newTransPwd) && ("0".equals(certFlag) || "1".equals(certFlag))) {
    			PwdInfoFacade ubsPwdService = (PwdInfoFacade)serviceClientFactory.getWebServiceClient(PwdInfoFacade.class,
                    "pwdInfoFacade", "UBS");
    			SetTransPwdRequest transPwdReq = new SetTransPwdRequest();
    			transPwdReq.setCustId(custId);
    			transPwdReq.setNewTransPwd(newTransPwd);
    			transPwdReq.setOperId(operId);   
    			transPwdReq.setConsumerId(ubsConsumerId);
    			PwdInfoResult transPwdRslt = ubsPwdService.setTransPwd(transPwdReq);
    			if(null != transPwdRslt){
    			    respCode = transPwdRslt.getRespCode();
                    respDesc = transPwdRslt.getRespDesc();
                    if(!"000".equals(respCode)){
                        cardForm.setErrorCode(String.valueOf(ErrorCode.SET_TRANSPWD_FAIL));    
                        cardForm.setErrorString(respDesc);
                        logger.info("设置交易密码出错，报错信息为：" + respCode+":"+ respDesc);
                        int certRslt = 7;
                		model.addAttribute("certRslt", certRslt);
                		model.addAttribute("respDesc", respDesc);
                        model.addAttribute("cardForm", cardForm);
                        return "account/openAccount.ftl";
                    }
    			}else{
    				cardForm.setErrorCode("099");
                    cardForm.setErrorString("开户时设置账户交易密码异常，请稍后重试");
                    logger.info("开户时设置账户交易密码异常，请稍后重试");
                    int certRslt = 7;
            		model.addAttribute("certRslt", certRslt);
            		model.addAttribute("respDesc", "开户时设置账户交易密码异常，请稍后重试");
                    model.addAttribute("cardForm", cardForm);
                    return "account/openAccount.ftl";
    			}
			}   
		} catch (ServiceException e) {
			e.printStackTrace();
			cardForm.setErrorCode(String.valueOf(e.getErrorCode()));
			cardForm.setErrorString("开户失败，请稍后重试");
			logger.error("开户异常", e);
			int certRslt = 5;
			model.addAttribute("certRslt", certRslt);
    		model.addAttribute("respDesc", "开户失败，请稍后重试");
			model.addAttribute("cardForm", cardForm);
            return "account/openAccount.ftl";
		} catch (Exception e) {
			e.printStackTrace();
			cardForm.setErrorCode("099");
			cardForm.setErrorString("开户失败，请稍后重试");
			logger.error("开户异常", e);
			int certRslt = 5;
    		model.addAttribute("certRslt", certRslt);
    		model.addAttribute("respDesc", "开户失败，请稍后重试");
			model.addAttribute("cardForm", cardForm);
            return "account/openAccount.ftl";
		}
		
		try {
			UserInfoFacade ubsUserService = (UserInfoFacade)serviceClientFactory.getWebServiceClient(UserInfoFacade.class,"userInfoFacade", "UBS");
			 IdInfoRequest usrBaseReq = new IdInfoRequest();
	            usrBaseReq.setCustId(custId);
	            usrBaseReq.setOperId(operId);
	            usrBaseReq.setConsumerId(ubsConsumerId);
	            IdInfoResult usrBaseRslt = ubsUserService.queryUserByCustId(usrBaseReq);
	            UserInfoDto usrBaseInfo = new UserInfoDto();
	            if(null != usrBaseRslt.getList()){
	                usrBaseInfo = usrBaseRslt.getList().get(0);
	            }
	            if(null != usrBaseInfo){
	            	String usrName = usrBaseInfo.getUsrName();
	            	usrName = "**" + usrName.substring(usrName.length()-1, usrName.length());
	                cardForm.setRealName(usrName);
	                String certId2 = usrBaseInfo.getCertId();
	                certId2 = certId2.substring(0, 1) + "****************" + certId2.substring(certId2.length()-1, certId2.length());
               	 	cardForm.setCertId(certId2);
	            }
	            //跳转开户成功页面
	    		model.addAttribute("cardForm", cardForm);
	    		return "account/openAccountSuc.ftl";    
			
		} catch (ServiceException e) {
			logger.error("开户异常", e);
			e.printStackTrace();
			model.addAttribute("cardForm", cardForm);
    		return "account/openAccountSuc.ftl";   
		}
	}
	
	
	/**
	 * 实名认证微信入口
	 * @param model
	 * @param request
	 * @param cardForm
	 * @param type
	 * @param from
	 * @return
	 */
	@RequestMapping(value = "/m/openAccount/openAccountInit.do")
	public String OpenAccountInit_m(HttpServletRequest request,HttpServletResponse response,Model model ,BankCardBindForm cardForm, @RequestParam(required = false) String source){
		model.addAttribute("config", applicationPropertyConfig);
		if (!StringUtils.isEmpty(source)) {
			model.addAttribute("source", source);
		}
		User user = userSessionFacade.getUser(request);
		String custId = user.getCustId();
		String operId = user.getOperId();
		String realName = cardForm.getRealName();
		String certNum = cardForm.getCertId();
		// 调用ubs
		UserInfoFacade ubsUserService = null;
		try {
			ubsUserService = (UserInfoFacade) serviceClientFactory
					.getWebServiceClient(UserInfoFacade.class,
							"userInfoFacade", "UBS");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			logger.info("调用ubs服务出错");
			throw new RuntimeException(e);
		}
		//查下用户是否实名验证
		IdInfoRequest usrBaseReq = new IdInfoRequest();
		usrBaseReq.setCustId(custId);
		usrBaseReq.setOperId(operId);
		usrBaseReq.setConsumerId(ubsConsumerId);
		IdInfoResult usrBaseRslt = ubsUserService.queryUserByCustId(usrBaseReq);
		UserInfoDto usrBaseInfo = new UserInfoDto();
		if (null != usrBaseRslt.getList()) {
			usrBaseInfo = usrBaseRslt.getList().get(0);
		}
		if (null == usrBaseInfo) {
			// 查不到信息直接到申请页面
			return "account/m/openAccount_m.ftl";
		} else {
			// 新增实名认证状态位，根据此字段来查询是否通过实名认证
			realName = usrBaseInfo.getUsrName();
			certNum = usrBaseInfo.getCertId();

			if (StringUtils.isBlank(realName)&& StringUtils.isBlank(certNum)) {
				return "account/m/openAccount_m.ftl";
			} else {
				realName = "**" + realName.substring(realName.length() - 1,realName.length());
				certNum = certNum.substring(0, 1) + "****************" + certNum.substring(certNum.length() - 1, certNum.length());
				cardForm.setRealName(realName);
				cardForm.setCertId(certNum);
				model.addAttribute("cardForm", cardForm);
				return "account/m/openAccounted_m.ftl";
			}
		}
	}
	/**
	 * 实名认证微信数据持久化
	 * @param model
	 * @param request
	 * @param cardForm
	 * @return
	 */
	@RequestMapping(value = "/m/openAccount/openAccount.do")
	public String OpenAccount_m(Model model , HttpServletRequest request,BankCardBindForm cardForm,@RequestParam(required = false) String source){
		model.addAttribute("config", applicationPropertyConfig);
		if (!StringUtils.isEmpty(source)) {
			model.addAttribute("source", source);
		}
		User user = userSessionFacade.getUser(request);
		String custId = user.getCustId();
		String realName = cardForm.getRealName();
		String certNum = cardForm.getCertId();
		// 调用ubs
		UserInfoFacade ubsUserService = null;
		try {
			ubsUserService = (UserInfoFacade) serviceClientFactory
					.getWebServiceClient(UserInfoFacade.class,
							"userInfoFacade", "UBS");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			logger.info("调用ubs服务出错");
			throw new RuntimeException(e);
		}
		
			String respCode = "";
			String respDesc = "";
			String certType = "00";
			// 查询用户实名信息状况
			boolean hasCertVerified = false;
			// 严格进行身份证验证
			if (!hasCertVerified) {
				boolean verified = true;
				// （local环境下不验证）
				if (!applicationPropertyConfig.getEnv().equalsIgnoreCase(
						EnvEnum.LOCAL.getEnvString())) {
					// 国政通验证身份证
					UserAuthorizeInfo userAuthrizeInfo = new UserAuthorizeInfo();
					userAuthrizeInfo.setCertId(certNum);
					userAuthrizeInfo.setCertName(realName);
					userAuthrizeInfo = userAuthorizeService.authorizeUser(userAuthrizeInfo);
					respCode = userAuthrizeInfo.getRespCode();
					respDesc = userAuthrizeInfo.getRespDesc();
					if (!"0".equals(respCode)) {
						verified = false;
						if ("50000".equals(respCode)) {
							cardForm.setErrorCode(String
									.valueOf(ErrorCode.OPEN_ACCT_FAIL));
							cardForm.setErrorString("实名认证失败");
							System.out.println("实名认证出错，报错信息为：" + respDesc);
						} else if ("50001".equals(respCode)) {
							cardForm.setErrorCode(String
									.valueOf(ErrorCode.OPEN_ACCT_FAIL));
							cardForm.setErrorString("该身份证号已在平台认证，无法进行重复认证。");
							System.out.println("实名认证出错，报错信息为：" + respDesc);
						} else if ("50002".equals(respCode)) {
							cardForm.setErrorCode(String
									.valueOf(ErrorCode.OPEN_ACCT_FAIL));
							cardForm.setErrorString("该身份证号和姓名已在平台完成认证，无法进行重复认证。");
							System.out.println("实名认证出错，报错信息为：" + respDesc);
						} else if ("50003".equals(respCode)) {
							cardForm.setErrorCode(String
									.valueOf(ErrorCode.OPEN_ACCT_FAIL));
							cardForm.setErrorString("该身份证号和姓名不一致");
							System.out.println("实名认证出错，报错信息为：" + respDesc);
						}
						model.addAttribute("cardForm", cardForm);
						// 跳转到错误页面
						return "account/m/openAccountErr_m.ftl";
					}
				}

				if (verified) {
					try {
						// 更新用户实名信息
						String usrSex = "";
						String birthday = certNum.substring(6, 10) + "-"
								+ certNum.substring(10, 12) + "-"
								+ certNum.substring(12, 14);
						String sexFlag = certNum.substring(
								certNum.length() - 2, certNum.length() - 1);
						if ((Integer.valueOf(sexFlag) % 2) != 0) {
							usrSex = "M";
						} else {
							usrSex = "F";
						}
						ModifyCertiInfoRequest certReq = new ModifyCertiInfoRequest();
						certReq.setCustId(custId);
						certReq.setRealName(realName);
						certReq.setCertType(certType);
						certReq.setCertNo(certNum);
						certReq.setBirthday(birthday);
						certReq.setUsrSex(usrSex);
						certReq.setConsumerId(ubsConsumerId);
						// 调用ubs服务的更新
						ModifyCertiInfoResult certiRslt = ubsUserService.modifyCertiInfo(certReq);
						if (null != certiRslt) {
							respCode = certiRslt.getRespCode();
							respDesc = certiRslt.getRespDesc();
							if (!"000".equals(respCode)) {
								cardForm.setErrorCode(String.valueOf(ErrorCode.OPEN_ACCT_FAIL));
								cardForm.setErrorString("开户异常，请稍后重试");
								logger.info("更新用户实名信息出错，报错信息为：" + respDesc);
								model.addAttribute("respDesc", respDesc);
								model.addAttribute("cardForm", cardForm);
								return "account/m/openAccountErr_m.ftl";
							}
						} else {
							cardForm.setErrorCode("099");
							cardForm.setErrorString("开户异常，请稍后重试");
							model.addAttribute("respDesc", "开户异常，请稍后重试");
							model.addAttribute("cardForm", cardForm);
							return "account/m/openAccountErr_m.ftl";
						}

					} catch (Exception e) {
						logger.error("更新实名数据出错", e);
						logger.info("更新用户实名信息出错，请稍后重试");
						return "account/m/openAccountErr_m.ftl";
					}
				}

			}
			if(null!=source&&!"".equals(source)){
				return "redirect:"+source;
			}else{
				return "account/m/openAccountSuc_m.ftl";
			}
	}
	
	/**
	 * 设置交易密码微信入口
	 * @param model
	 * @param request
	 * @param cardForm
	 * @return
	 */
	@CertifyRequired(minLevel = CertifyLevel.ID)
	@RequestMapping(value = "/m/openAccount/setTransPassWordInit.do")
	public String setTransPassWordInit_M(HttpServletRequest request, HttpServletResponse response, Model model, 
			@RequestParam(required = false) String source) {
		model.addAttribute("config", applicationPropertyConfig);
		if (!StringUtils.isEmpty(source)) {
			model.addAttribute("source", source);
		}
		
		consoleSession.setAttribute(request, SET_PWD_LEGAL_FLAG, SET_PWD_LEGAL_FLAG);
		
		return "account/m/paymentSetPassword_m.ftl";
	}
	
	/**
	 *设置交易密码数据持久化微信
	 * @param transPwd
	 * @param confirmTransPwd
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/m/openAccount/setTransPassWord.do", method = RequestMethod.POST)
	public @ResponseBody JSONObject setPwdForM(@RequestParam String transPwd, @RequestParam String confirmTransPwd, 
			HttpServletRequest request) {
		JSONObject jsonObj = new JSONObject();
		
		String bindCardLegalFlag = consoleSession.getAttribute(request, SET_PWD_LEGAL_FLAG);
		if (StringUtils.isEmpty(bindCardLegalFlag)) {
			jsonObj.put("success", false);
			jsonObj.put("msg", "非法请求");
			return jsonObj;
		}
		consoleSession.deleteAttribute(request, SET_PWD_LEGAL_FLAG);
		
		if(!transPwd.equals(confirmTransPwd)) {
			jsonObj.put("success", false);
			jsonObj.put("msg", "两次密码不一致");
			return jsonObj;
		}
		
		User user = userSessionFacade.getUser(request);
		
		AccountInfoFacade ubsAcctService = null;
		try {
			ubsAcctService = (AccountInfoFacade) serviceClientFactory
					.getWebServiceClient(AccountInfoFacade.class, "accountInfoFacade", "UBS");
		} catch (ServiceException e) {
			jsonObj.put("success", false);
			jsonObj.put("msg", "系统异常");
			return jsonObj;
		}
		
		OpenAccountInfoRequest acctReq = new OpenAccountInfoRequest();
		acctReq.setCustId(user.getCustId());
		acctReq.setAcctType("BASEDA");
		acctReq.setDcFlag("D");
		acctReq.setAcctAlias("def");
		acctReq.setConsumerId(ubsConsumerId);
		logger.info("OpenAccountInfoRequest: " + JSON.toJSONString(acctReq));
		
		OpenAccountInfoResult openAccountInfoResult = ubsAcctService.openAccountInfo(acctReq);
		if (null == openAccountInfoResult || !"000".equals(openAccountInfoResult.getRespCode())) {
	        jsonObj.put("msg", (null == openAccountInfoResult) ? "开通账户异常，请稍后重试" : openAccountInfoResult.getRespDesc());
	        jsonObj.put("success", false);
	        return jsonObj;
		}
		
		PwdInfoFacade ubsPwdService = null;
		try {
			ubsPwdService = (PwdInfoFacade) serviceClientFactory
					.getWebServiceClient(PwdInfoFacade.class, "pwdInfoFacade", "UBS");
		} catch (ServiceException e) {
			jsonObj.put("success", false);
			jsonObj.put("msg", "系统异常");
			return jsonObj;
		}
		
		SetTransPwdRequest transPwdReq = new SetTransPwdRequest();
		transPwdReq.setCustId(user.getCustId());
		transPwdReq.setNewTransPwd(transPwd);
		transPwdReq.setOperId(user.getOperId());
		transPwdReq.setConsumerId(ubsConsumerId);
		PwdInfoResult transPwdRslt = ubsPwdService.setTransPwd(transPwdReq);
		if (null == transPwdRslt || !"000".equals(transPwdRslt.getRespCode())) {
			jsonObj.put("msg", (null == transPwdRslt) ? "设置交易密码系统异常" : transPwdRslt.getRespDesc());
			jsonObj.put("success", false);
		    return jsonObj;
		}
		
		jsonObj.put("success", true);
	    return jsonObj;
	}
	/**
	 * 设置交易密码成功之后页面
	 * @param model
	 * @param source
	 * @return
	 */
	@RequestMapping(value = "/m/openAccount/setTransPassWordSuccess.do")
	public String setTransPwdSuccess(Model model, @RequestParam(required = false) String source) {
		model.addAttribute("config", applicationPropertyConfig);
		if (!StringUtils.isEmpty(source)) {
			model.addAttribute("source", source);
		}
		return "account/m/pamentSetPasswordSuc_m.ftl";
	}

}