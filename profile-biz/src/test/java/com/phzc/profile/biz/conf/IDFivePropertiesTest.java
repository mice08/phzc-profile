package com.phzc.profile.biz.conf;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IDFivePropertiesTest {

	@Test
	public void testProperties() {
		String paths[] = {"classpath:spring-profile-biz.xml"};
		ApplicationContext ctx = new ClassPathXmlApplicationContext(paths);
		IDFiveProperties idFiveProperties = (IDFiveProperties) ctx.getBean("idFiveProperties");
		
		assertNotNull(idFiveProperties.getOrgCode());
		assertNotNull(idFiveProperties.getChnlId());
		assertNotNull(idFiveProperties.getAuthCode());
		assertNotNull(idFiveProperties.getPostUrl());
		assertNotNull(idFiveProperties.getUserName());
		assertNotNull(idFiveProperties.getUserPassword());
		assertNotNull(idFiveProperties.getCheckCode());
		assertNotNull(idFiveProperties.getJksPath());
		assertNotNull(idFiveProperties.getStorePassword());
		assertNotNull(idFiveProperties.getStoreAlias());
	}

}