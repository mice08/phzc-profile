package com.phzc.profile.web.utils;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.phzc.console.base.session.impl.DefaultConsoleSession;
import com.phzc.profile.web.constants.ProfileConstants;
import com.phzc.profile.web.services.PayChannelClient;
import com.phzc.profile.web.services.YLPayChannelClient;
import com.phzc.profile.web.services.YeePayChannelClient;
import com.phzc.ubs.common.facade.model.BankCardInfoDto;

public class PayChannelClientFactory {

	private static ProfileUtil profileUtil;
	private static  Logger log = LoggerFactory.getLogger(PayChannelClientFactory.class);
	
	public static PayChannelClient getInstance(HttpServletRequest request,String from) throws Exception {
		BankCardInfoDto bankCardInfo = (BankCardInfoDto) DefaultConsoleSession.getInstance().getAttribute(request, ProfileConstants.SEESION_KEY_CARD,BankCardInfoDto.class);
		if(bankCardInfo == null) {
			bankCardInfo = getProfileUtil().getBandCardInfo(request);
		}
		if(bankCardInfo == null) {
			log.info("bankCardInfo----is null-------未绑卡------");
			System.out.println("bankCardInfo----is null-------未绑卡------");
			return null;
		}
		String chkChannel = bankCardInfo.getChkChnl();
		if(chkChannel == null) {//TODO
			log.info("chkChannel----is null-------default YL------");
			System.out.println("chkChannel----is null------default YL------");
			return new YLPayChannelClient(from);
		}
		log.info("chkChannel-----------------="+chkChannel);
		if(ProfileConstants.YL_PAY_CHANNEL.equals(chkChannel)) {
			return new YLPayChannelClient(from);
		} else if(ProfileConstants.YEE_PAY_CHANNEL.equals(chkChannel)) {
			return new YeePayChannelClient(from);
		} else {
			throw new Exception("Can not get pay channel .");
		}
	}
	
	private static ProfileUtil getProfileUtil() {
		if(profileUtil == null)
			profileUtil =  new ProfileUtil();
		return profileUtil;
	}
}
