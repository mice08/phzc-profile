package com.phzc.profile.web.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {
	
    protected static  String TRADE_SERVICE_HOST_BASE_URI;
    protected static  String TRADE_SERVICE_PROJECT_NAME;
    protected static  String TRADE_SERVICE_NAME;
    protected static  String TRADE_SERVICE_HOST_PORT;
    protected static  String EMPTY_STR = "";
    protected static  String SEPARATOR = "/";
    protected static  String COLON = ":"; 
    private   static  Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
    private static InputStream stream = null;
    
    public HttpClientUtil() {
    	stream = getClass().getResourceAsStream("/services.properties");
    	Properties prop = new Properties();
        try {
			prop.load(stream);
		} catch (IOException e) {
			log.error("get properties file error: "+ e.getStackTrace());
		}
        TRADE_SERVICE_HOST_BASE_URI = prop.getProperty("tradeServiceHostUri");
        TRADE_SERVICE_HOST_PORT = prop.getProperty("tradeServiceHostPort");
        TRADE_SERVICE_PROJECT_NAME = prop.getProperty("tradeServiceProjectName");
        TRADE_SERVICE_NAME = prop.getProperty("tradeServiceName");
    }
    
	public static String getTradeServiceBaseUri() {
		return TRADE_SERVICE_HOST_BASE_URI + COLON + TRADE_SERVICE_HOST_PORT + SEPARATOR + TRADE_SERVICE_PROJECT_NAME 
				+ SEPARATOR + TRADE_SERVICE_NAME;
	}
	
	
    public  String getUriByParms(Map map) {
        StringBuffer sb = new StringBuffer();
        if(map != null && map.size() > 0) {
            sb.append("?"); 
        } else {
            return "";
        }
        Iterator iter = map.entrySet().iterator();
        int i = 0;
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            Object val = entry.getValue();
            if(key != null && val != null) {
                sb.append(key.toString());
                sb.append("=");
                sb.append(val.toString()); 
                if(i < map.size() -1) {
                    sb.append("&");
                }
            }
            i++;
        }
        return sb.toString();
    }
}
