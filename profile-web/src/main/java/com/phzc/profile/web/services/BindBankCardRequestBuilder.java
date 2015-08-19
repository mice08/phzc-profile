package com.phzc.profile.web.services;

import com.phzc.ubs.common.facade.model.BindBankCardRequest;

public interface BindBankCardRequestBuilder {
	
	BindBankCardRequestBuilding build();
	
	interface BindBankCardRequestBuilding {
		BindBankCardRequestBuilding withBizValidationInfo(String consumerId);
		BindBankCardRequestBuilding withBankInfo(String bankName, String cardId, String cardType, String cardChannel);
		BindBankCardRequestBuilding withIdentificationInfo(String custId, String realName, String certId, String certType, String mobile);
		BindBankCardRequest end();
	}

}