package com.phzc.profile.web.services;

import com.phzc.profile.web.config.ApplicationPropertyConfig;
import org.codehaus.xfire.client.XFireProxyFactory;
import org.codehaus.xfire.service.Service;
import org.codehaus.xfire.service.binding.ObjectServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HANRAN177 on 2015/7/17.
 */

@Component
public class ServiceClient {

    private static Logger logger = LoggerFactory.getLogger(ServiceClient.class);

    @Autowired
    private ApplicationPropertyConfig propertyConfig;

    private Map<String, Object> interfaceCache = new HashMap<String, Object>();

    /**
     * @param interfaceClazz 服务接口
     * @param serviceName    服务名
     * @param system         UBS 用户中心
     * @return
     * @throws Exception
     */
    public <T> T getWebServiceClient(Class<T> interfaceClazz, String serviceName, String system) {
        String interfaceName = interfaceClazz.getCanonicalName();
        T interfaceObj = null;
        if (interfaceCache.containsKey(interfaceName)) {// 如果缓存存在服务接口对象，直接返回
            interfaceObj = (T) interfaceCache.get(interfaceName);
        } else {

            try {
                Service service = new ObjectServiceFactory().create(interfaceClazz);
                if (null == service) {
                    throw new RuntimeException("webservice 初始化失败, data: interfaceClazz-" + interfaceClazz +
                            " , serviceName-" + serviceName + " , system-" + system);
                }
                String serviceUrl = buildServiceUrl(serviceName, system);
                if (null == serviceUrl) {
                    throw new RuntimeException("webservice url 初始化失败, data: interfaceClazz-" + interfaceClazz +
                            " , serviceName-" + serviceName + " , system-" + system);
                }
                interfaceObj = (T) new XFireProxyFactory().create(service, serviceUrl);
                interfaceCache.put(interfaceName, interfaceObj);
            } catch (MalformedURLException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e.getCause());
            }
        }
        return interfaceObj;
    }

    private String buildServiceUrl(String serviceName, String system) {
        try {
            String systemProperty = null;
            Field[] declaredFields = propertyConfig.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                if (field.getName().equals(system)) {
                    field.setAccessible(true);
                    systemProperty = field.get(propertyConfig).toString();
                    break;
                }
            }
            if (null != systemProperty) {
                return systemProperty.replace("{0}", serviceName);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param serviceName 服务名
     * @param system      TS 交易中心
     * @param params      参数key1=value1&key2=value2
     * @return
     */
    public URL getRestfulURL(String serviceName, String system, String params) {
        String serviceKey = system + "|" + serviceName + "|" + params;
        URL interfaceObj = null;
        if (interfaceCache.containsKey(serviceKey)) {// 如果缓存存在服务接口对象，直接返回
            interfaceObj = (URL) interfaceCache.get(serviceKey);
        } else {
            String serviceUrl = buildServiceUrl(serviceName, system);
            if (null == serviceUrl) {
                throw new RuntimeException("webservice url 初始化失败, data: serviceName-" + serviceName + " , system-" + system);
            }
            serviceUrl = serviceUrl + "?" + params;
            try {
                interfaceObj = new URL(serviceUrl);
                interfaceCache.put(serviceKey, interfaceObj);
            } catch (MalformedURLException e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e.getCause());
            }
        }
        return interfaceObj;
    }

    /**
     * @param interfaceClazz FacadeXXXX.class
     * @param <T>
     * @return
     */
    public <T> T getDubboServiceClient(Class<T> interfaceClazz) {
        WebApplicationContext webContext = ContextLoader.getCurrentWebApplicationContext();
        T dubboClient = (T) webContext.getBean(interfaceClazz.getSimpleName());
        return dubboClient;
    }
}
