package com.phzc.profile.api.utils;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientPost{

	public static String doPost(String jsonStr,
			String postUrl) throws ClientProtocolException, IOException {

		StringEntity entityStr = new StringEntity(jsonStr);
	
		CloseableHttpClient httpclient = createSSLClientDefault();
		HttpPost httpPost = new HttpPost(postUrl);
		RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(60000).setConnectTimeout(60000).build();//设置请求和传输超时时间
		
		httpPost.setConfig(requestConfig);
		
		httpPost.setEntity(entityStr);
		httpPost.setHeader("Accept", "application/json");
		httpPost.setHeader("Content-Type","application/json;charset=utf-8");
		CloseableHttpResponse response = httpclient.execute(httpPost);
				
		String res = StringUtils.EMPTY;
		try {
			HttpEntity entity = response.getEntity();
			
			if (response.getStatusLine().getReasonPhrase().equals("OK")
					&& response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				res = EntityUtils.toString(entity, "UTF-8");
			}
			EntityUtils.consume(entity);
		} finally {
			response.close();
			httpclient.close();
		}
		
		return res;
	}
	
	private static CloseableHttpClient createSSLClientDefault() {
		try {
			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(
					null, new TrustStrategy() {
						// 信任所有
						public boolean isTrusted(X509Certificate[] chain,
								String authType) throws CertificateException {
							return true;
						}
					}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslContext);
			return HttpClients.custom().setSSLSocketFactory(sslsf).build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return HttpClients.createDefault();
	}

}