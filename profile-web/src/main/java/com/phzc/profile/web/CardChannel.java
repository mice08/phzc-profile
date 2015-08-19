package com.phzc.profile.web;

public enum CardChannel {
	
	EASY_LINK("13"),
	YEE_PAY("14");
	
	private String channelNo;
	
	private CardChannel(String channelNo) {
		this.channelNo = channelNo;
	}

	public String getChannelNo() {
		return channelNo;
	}

}