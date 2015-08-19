package com.phzc.profile.web.controller.m;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.druid.util.StringUtils;
import com.phzc.console.base.exception.ServiceException;
import com.phzc.console.base.pojo.User;
import com.phzc.profile.web.controller.BaseController;
import com.phzc.profile.web.form.UserInfoVo;
import com.phzc.ubs.common.facade.model.IdInfoRequest;
import com.phzc.ubs.common.facade.model.IdInfoResult;
import com.phzc.ubs.common.facade.model.QueryBankCardRequest;
import com.phzc.ubs.common.facade.model.QueryBankCardResult;
import com.phzc.ubs.common.facade.model.UserInfoDto;
import com.phzc.ubs.common.facade.service.BankCardInfoFacade;
import com.phzc.ubs.common.facade.service.UserInfoFacade;
import com.phzc.wxconsole.api.service.WeixinUserService;

@Controller
@RequestMapping("/m/*")
public class UserInfoController extends BaseController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserInfoController.class);
	
//	@Autowired
//	private WeixinUserService weixinUserServiceDubbo;
	
	@RequestMapping(value = "userInfo.do", method = RequestMethod.GET)
	public String userInfo(Model model, HttpServletRequest request, 
			@RequestParam(required = false) String isFromWeChat) {
		model.addAttribute("config", applicationPropertyConfig);
		
		User user = userSessionFacade.getUser(request);
		
		UserInfoFacade ubsUserService = null;
		try {
			ubsUserService = (UserInfoFacade) serviceClientFactory
					.getWebServiceClient(UserInfoFacade.class, "userInfoFacade", "UBS");
		} catch (ServiceException e) {
			model.addAttribute("msg", "系统异常");
			return "share/m/500.ftl";
		}

		IdInfoRequest idInfoRequest = new IdInfoRequest();
		idInfoRequest.setCustId(user.getCustId());
		idInfoRequest.setOperId(user.getOperId());
		idInfoRequest.setConsumerId(ubsConsumerId);
		IdInfoResult idInfoResult = ubsUserService.queryUserByCustId(idInfoRequest);
		if (null == idInfoResult || !"000".equals(idInfoResult.getRespCode())) {
			model.addAttribute("msg", (null == idInfoResult) ? "查询用户系统异常" : idInfoResult.getRespDesc());
			return "share/m/500.ftl";
		}
		
		UserInfoDto userInfoDto = idInfoResult.getList().get(0);
		
		UserInfoVo userInfoVO = new UserInfoVo();
		userInfoVO.setCustId(userInfoDto.getCustId());
		userInfoVO.setUsrIcon(userInfoDto.getUsrIcon());
		userInfoVO.setUsrMp(this.confuseMobile(userInfoDto.getUsrMp()));
		userInfoVO.setUsrEmail(userInfoDto.getUsrEmail());
		userInfoVO.setIdChkStat(userInfoDto.getIdChkStat());
		userInfoVO.setIsInvStat(userInfoDto.getIsInvStat());
		userInfoVO.setUsrSex(userInfoDto.getUsrSex());
		BankCardInfoFacade cardInfoService = null;
		try {
			cardInfoService = (BankCardInfoFacade) serviceClientFactory
					.getWebServiceClient(BankCardInfoFacade.class, "bankCardInfoFacade", "UBS");
		} catch (ServiceException e) {
			return null;
		}

		QueryBankCardRequest qryCardReq = new QueryBankCardRequest();
		qryCardReq.setConsumerId(ubsConsumerId);
		qryCardReq.setCustId(userInfoDto.getCustId());
		QueryBankCardResult qryCardRslt = cardInfoService.queryBankCard(qryCardReq);
		if (hasCard(qryCardRslt)) {
			userInfoVO.setHasCard(1);
		} else {
			userInfoVO.setHasCard(0);
		}

		if (!StringUtils.isEmpty(isFromWeChat) && "1".equals(isFromWeChat)) {
			String headImgUrl = "";//weixinUserServiceDubbo.getWeixinUserHeadImgUrl(user.getCustId());
			logger.info("headImgUrl: " + headImgUrl);
			if (!StringUtils.isEmpty(headImgUrl)) {
				userInfoVO.setHeadImgUrl(headImgUrl);
			}
		}

		model.addAttribute("userInfo", userInfoVO);
		
		return "information/m/userInfo.ftl";
	}

	private boolean hasCard(QueryBankCardResult qryCardRslt) {
		return null != qryCardRslt 
				&& qryCardRslt.getBankCardInfoList() != null 
				&& !qryCardRslt.getBankCardInfoList().isEmpty();
	}

}