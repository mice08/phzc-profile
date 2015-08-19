package com.phzc.profile.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service
public class ContentConfig {
	/**
	 *网站title名称
	 */
	@Value("${content.titleName}")
	private String contentTitleName;
	/**
	 * 文档中的网站名称
	 */
	@Value("${content.textName}")
	private String contentTextName;
	/**
	 * 网站中带样式的名称
	 */
	@Value("${content.titleNameStyle}")
	private String contentTitleNameStyle;
	
	public String getContentTitleName() {
		return contentTitleName;
	}
	public String getContentTextName() {
		return contentTextName;
	}
	public String getContentTitleNameStyle() {
		return contentTitleNameStyle;
	}

}
