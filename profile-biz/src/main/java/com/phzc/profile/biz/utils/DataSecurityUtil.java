package com.phzc.profile.biz.utils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.phzc.profile.api.utils.CommonUtil;
import com.phzc.profile.biz.conf.IDFiveProperties;

public class DataSecurityUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(DataSecurityUtil.class);

	public static String digest(byte[] oriByte) throws Exception {
		MessageDigest md = null;
		String strDes = null;
		try {
			md = MessageDigest.getInstance("SHA1");
			md.update(oriByte);
			strDes = CommonUtil.bytes2Hex(md.digest());
		} catch (Exception e) {

		}
		return strDes;
	}

	public static String signData(String data, IDFiveProperties idFiveProperties) throws Exception {
		try {
			PrivateKey key = getPrivateKey(idFiveProperties);
			Signature sig = Signature.getInstance("SHA1WithRSA");
			sig.initSign(key);
			sig.update(data.getBytes("utf-8"));
			byte[] sigBytes = sig.sign();
			BASE64Encoder encoder = new BASE64Encoder();
			return encoder.encodeBuffer(sigBytes);
		} catch (Exception e) {

		}
		return null;
	}

	public static void verifyData(String data, String signValue)
			throws Exception {
		try {
			PublicKey key = getPublicKey();
			Signature sig = Signature.getInstance("SHA1WithRSA");
			sig.initVerify(key);
			sig.update(data.getBytes("utf-8"));
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] signValueByte = decoder.decodeBuffer(signValue);
			if (!sig.verify(signValueByte)) {

			}
		} catch (Exception e) {

		}
	}

	private static PublicKey getPublicKey() throws Exception {
		InputStream is = null;
		try {
			// is = new FileInputStream("D:\\KeyScript\\test\\bistest_2.cer");
			is = new FileInputStream("D:\\Key\\credoo_test.cer");
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
			return cert.getPublicKey();
		} catch (FileNotFoundException e) {

		} catch (CertificateException e) {

		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private static PrivateKey getPrivateKey(IDFiveProperties idFiveProperties) {
		char[] storePwdArr;
		int i;
		BufferedInputStream bis = null;
		try {
			logger.info("Get JKS before");
			KeyStore ks = KeyStore.getInstance("JKS");
			FileInputStream fis = new FileInputStream(idFiveProperties.getJksPath());
			bis = new BufferedInputStream(fis);
			String storePassword = idFiveProperties.getStorePassword();
			String storeAlias = idFiveProperties.getStoreAlias();
			storePwdArr = new char[storePassword.length()];// store password
			for (i = 0; i < storePassword.length(); i++) {
				storePwdArr[i] = storePassword.charAt(i);
			}
			ks.load(bis, storePwdArr);
			//  返回与给定别名相关联的密钥
			PrivateKey priv = (PrivateKey) ks.getKey(storeAlias, storePwdArr);
			logger.info("Get JKS before"+priv.toString());
			return priv;
		} catch (KeyStoreException e) {
			logger.info("jks Access key exception",e);
			e.printStackTrace();

		} catch (FileNotFoundException e) {
			logger.info("jks The file does not exist in this directory",e);
			e.printStackTrace();

		} catch (NoSuchAlgorithmException e) {
			logger.info("jks Access key exception 2",e);
			e.printStackTrace();

		} catch (CertificateException e) {
			logger.info("jks Access key exception 3",e);
			e.printStackTrace();

		} catch (IOException e) {
			logger.info("jks Access key exception 4",e);
			e.printStackTrace();

		} catch (UnrecoverableKeyException e) {
			logger.info("jks Access key exception 5",e);
			e.printStackTrace();

		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String decrypt(String sealTxt, String keyStr)
			throws Exception {
		try {
			Cipher cipher = null;
			byte[] byteFina = null;
			SecretKey key = getKey(keyStr);
			try {
				cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
				cipher.init(Cipher.DECRYPT_MODE, key);
				BASE64Decoder decoder = new BASE64Decoder();
				byte[] sealByte = decoder.decodeBuffer(sealTxt);
				byteFina = cipher.doFinal(sealByte);
				return new String(byteFina, "utf-8");
			} catch (Exception e) {

			} finally {
				cipher = null;
			}
		} catch (Exception ee) {

		}
		return null;
	}

	public static String encrypt(byte[] oriByte, String keyStr)
			throws Exception {
		try {
			byte[] sealTxt = null;
			SecretKey key = getKey(keyStr);
			Cipher cipher = null;
			try {
				cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, key);
				sealTxt = cipher.doFinal(oriByte);
				BASE64Encoder encoder = new BASE64Encoder();
				String ret = encoder.encode(sealTxt);
				return ret;
			} catch (Exception e) {

			} finally {
				cipher = null;
			}
		} catch (Exception ee) {

		}
		return null;
	}

	private static SecretKey getKey(String key) throws Exception {
		try {
			// 实例化DESede密钥
			DESedeKeySpec dks = new DESedeKeySpec(key.getBytes("utf-8"));
			// 实例化密钥工厂
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
			// 生成密钥
			SecretKey secretKey = keyFactory.generateSecret(dks);
			return secretKey;
		} catch (Exception e) {
			return null;
		}
	}
}
