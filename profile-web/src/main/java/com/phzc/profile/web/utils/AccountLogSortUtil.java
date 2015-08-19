package com.phzc.profile.web.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.phzc.profile.web.dto.AccountLog;
import com.phzc.profile.web.dto.RechargeInfoList;
import com.phzc.ubs.common.facade.model.CashLogDto;

public class AccountLogSortUtil implements Comparator {

	static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmmss");
	
	public static void sortDate(List<AccountLog> accountLogList,List<RechargeInfoList> rechageLogList,
			List<CashLogDto> cashLogList) {
		AccountLogSortUtil.buildAccountLog(accountLogList, rechageLogList, cashLogList);
		Collections.sort(accountLogList, new AccountLogSortUtil());
	}
	
	
	public static void buildAccountLog(List<AccountLog> accountLogList,List<RechargeInfoList> rechageLogList,
			List<CashLogDto> cashLogList) {
		if(rechageLogList != null && !rechageLogList.isEmpty()) {
			for(RechargeInfoList rechargeLog : rechageLogList) {
				AccountLog accountLog = new AccountLog();
				accountLog.setName("充值");
				accountLog.setCustId(rechargeLog.getCustId());
				accountLog.setOperId(rechargeLog.getOperId());
				accountLog.setTransAmt(rechargeLog.getTransAmt());
				accountLog.setTransStat(rechargeLog.getTransStat());
				String acctTime = rechargeLog.getAcctTime();
				if(!StringUtils.isBlank(acctTime)) {
					try {
						acctTime = sdf.format(sdf2.parse(acctTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					accountLog.setAcctTime(acctTime);
				}
				accountLog.setAcctType("充值");
				accountLogList.add(accountLog);
			}
		}
		if(cashLogList != null && !cashLogList.isEmpty()) {
			for(CashLogDto cashLog : cashLogList) {
				AccountLog accountLog = new AccountLog();
				accountLog.setName("提现");
				accountLog.setCustId(cashLog.getCustId());
				accountLog.setOperId(String.valueOf(cashLog.getOperId()));
				accountLog.setTransAmt(cashLog.getTransAmt());
				accountLog.setTransStat(cashLog.getTransStat());
				accountLog.setAcctTime(cashLog.getSysTime());
				accountLog.setAcctType("提现");
				accountLogList.add(accountLog);
			}
		}
	}
	
	@Override
	public int compare(Object o1, Object o2) {
		AccountLog accountLog1 = (AccountLog)o1;
		AccountLog accountLog2 = (AccountLog)o2;
		
		String acctTime1 = accountLog1.getAcctTime();
		String acctTime2 = accountLog2.getAcctTime();
		if(StringUtils.isBlank(acctTime1))
			return 1;
		if(StringUtils.isBlank(acctTime2))
			return -1;
		Date d1 = null;
		Date d2 = null;
		try {
			d1 = sdf.parse(acctTime1);
			d2 = sdf.parse(acctTime2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(d1.before(d2)) {
			return 1;
		}else {
			return -1;
		}
	}


}
