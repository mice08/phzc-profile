package com.phzc.profile.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author yuanwei
 */
@Service
public class ApplicationPropertyConfig {
	//
	// domain
	//
	/**
	 * 网站协议
	 */
	@Value("${domain.protocol}")
	private String domainProtocol;
	/**
	 * 网站主域名
	 */
	@Value("${domain.host}")
	private String domainHost;
	/**
	 * www站子域名
	 */
	@Value("${domain.protocol}${domain.www}${domain.host}")
	private String domainWww;
	/**
	 * item站子域名
	 */
	@Value("${domain.protocol}${domain.item}${domain.host}")
	private String domainItem;
	/**
	 * profile站子域名
	 */
	@Value("${domain.protocol}${domain.profile}${domain.host}")
	private String domainProfile;
	
	@Value("${domain.protocol}${domain.trade}${domain.host}")
	private String domainTrade;
	
	@Value("${domain.protocol}${domain.m}${domain.host}")
	private String domainM;
	
	public String getTs() {
		return ts;
	}
	public void setTS(String ts) {
		this.ts = ts;
	}

	public String getDomainM() {
		return domainM;
	}

	/**
	 * 交易充值域名
	 */
	@Value("${tradeServiceHostUri}${tradeServiceProjectName}${tradeServiceName}")
	private String domainTradeForRecharge;
	
	/**
	 * passport站子域名
	 */
	@Value("${domain.protocol}${domain.passport}${domain.host}")
	private String domainPassport;
	/**
	 * 静态文件域名
	 */
	@Value("${domain.protocol}${domain.pcstatic}")
	private String domainPcstatic;
	
	/**
	 * 静态文件域名
	 */
	@Value("${domain.protocol}${domain.mstatic}")
	private String domainMstatic;
	
	/**
	 * 上传资料路径配置文件
	 * @return
	 */
	@Value("${upload.path}")
	private String uploadPath;
	/**
	 * 
	 * @return
	 */
	@Value("${domain.protocol}${domain.file}")
	private String domainFile;
	
	/**
	 * 
	 * @return
	 */
	@Value("${TS}")
	private String ts;
	/**
	 * 获得pc端css、js的版本号
	 * @return
	 */
	@Value("${domain.pcstatic.version}")
	private String domainPcstaticVersion;
	/**
	 * 获得m端css、js的版本号
	 * @return
	 */
	@Value("${domain.mstatic.version}")
	private String domainMstaticVersion;
	
	public String getDomainMstaticVersion() {
		return domainMstaticVersion;
	}

	public String getDomainPcstaticVersion() {
		return domainPcstaticVersion;
	}
	public String getDomainFile(){
		return domainFile;
	}
	public String getUploadPath(){
		return uploadPath;
	}
	
	public String getDomainProtocol() {
		return domainProtocol;
	}

	public String getDomainHost() {
		return domainHost;
	}

	public String getDomainWww() {
		return domainWww;
	}

	public String getDomainItem() {
		return domainItem;
	}

	public String getDomainProfile() {
		return domainProfile;
	}

	public String getDomainPassport() {
		return domainPassport;
	}

	public String getDomainPcstatic() {
		return domainPcstatic;
	}

	public String getDomainMstatic() {
		return domainMstatic;
	}
	
	public String getDomainTradeForRecharge() {
		return domainTradeForRecharge;
	}
	
	@Value("${env}")
	private String env;

	public String getEnv() {
		return env;
	}

    /**
     * PMS restful config
     */
    @Value("${PMS}")
    private String PMS;

    public String getPMS() {
        return PMS;
    }
	public String getDomainTrade() {
		return domainTrade;
	}
	public String changeToSSL(String url){
		return url.replace("http://", "https://");
	}
	
	@Value(value = "${card.channel}")
	private String cardChannel;

	public String getCardChannel() {
		return cardChannel;
	}
	/**
	 * 获得文本配置的内容
	 */
	@Autowired
	public ContentConfig contentConfig;
	public void setContentConfig(ContentConfig contentConfig) {
		this.contentConfig = contentConfig;
	}

	public ContentConfig getContentConfig() {
		return contentConfig;
	}
	
}