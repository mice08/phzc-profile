package com.phzc.profile.web.services.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.phzc.profile.web.services.BindBankCardRequestBuilder;
import com.phzc.ubs.common.facade.model.BindBankCardRequest;

@Service
public class DefaultBindBankCardRequestBuilder implements BindBankCardRequestBuilder {

	@Override
	public BindBankCardRequestBuilding build() {
		return new DefaultBindBankCardRequestBuilding();
	}
	
	class DefaultBindBankCardRequestBuilding implements BindBankCardRequestBuilding {
		
		private BindBankCardRequest bindBankCardRequest = new BindBankCardRequest();

		@Override
		public BindBankCardRequestBuilding withBizValidationInfo(String consumerId) {
			bindBankCardRequest.setConsumerId(consumerId);
			return this;
		}

		@Override
		public BindBankCardRequestBuilding withBankInfo(String bankName, String cardId,
				String cardType, String cardChannel) {
			bindBankCardRequest.setBankNm(bankName);
			bindBankCardRequest.setCardId(cardId);
			bindBankCardRequest.setCardType(cardType);
			bindBankCardRequest.setChkChnl(cardChannel);
			return this;
		}

		@Override
		public BindBankCardRequestBuilding withIdentificationInfo(String custId, String realName,
				String certId, String certType, String mobile) {
			bindBankCardRequest.setCustId(custId);
			bindBankCardRequest.setCardNm(realName);
			bindBankCardRequest.setCertId(certId);
			bindBankCardRequest.setCertType(certType);
			bindBankCardRequest.setTelNo(mobile);
			return this;
		}

		@Override
		public BindBankCardRequest end() {
			if (StringUtils.isEmpty(bindBankCardRequest.getConsumerId())
					|| StringUtils.isEmpty(bindBankCardRequest.getCardId())
					|| StringUtils.isEmpty(bindBankCardRequest.getCardType())
					|| StringUtils.isEmpty(bindBankCardRequest.getChkChnl())
					|| StringUtils.isEmpty(bindBankCardRequest.getCustId())
					|| StringUtils.isEmpty(bindBankCardRequest.getCardNm())
					|| StringUtils.isEmpty(bindBankCardRequest.getCertId())
					|| StringUtils.isEmpty(bindBankCardRequest.getCertType())
					|| StringUtils.isEmpty(bindBankCardRequest.getTelNo())) {
				throw new RuntimeException("Failed to create BindBankCardRequest instance.");
			}
			return bindBankCardRequest;
		}
		
	}

}