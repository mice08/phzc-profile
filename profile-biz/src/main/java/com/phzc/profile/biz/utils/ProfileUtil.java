package com.phzc.profile.biz.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

public class ProfileUtil {

	static public final String FORMAT_COMPACT2 = "yyyyMMdd";
	/**
	 * 编号的长度
	 */
	private static int ID_LENGTH = 20;

	public static String createId(String seqName, String productType) {
		int length = 0;
		StringBuilder builder = new StringBuilder(ID_LENGTH);
		if (StringUtils.isNotBlank(productType)) {
			builder.append(productType);
			length = length + productType.length();
		}
		builder.append(format(new Timestamp(System.currentTimeMillis()),
				FORMAT_COMPACT2));
		length = length + 8;
		// 8888去数据库的中的长度
		builder.append(leftPadding("8888", ID_LENGTH - length, '0'));
		
		/*builder.append(StringUtil.leftPadding(
				String.valueOf(BeanFactory.getSequenceManager().createSequence(
						seqName)), ID_LENGTH - length, '0'));*/
		
		return builder.toString();
	}

	public static String format(Timestamp date, String format) {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		sf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
		return sf.format(date);
	}

	/**
	 * 在字符串左端补齐
	 */
	public static String leftPadding(String text, int len, char c) {
		StringBuffer buffer = null;
		int curSize = 0;

		if (text != null) {
			curSize = text.length();
			buffer = new StringBuffer(text);
		} else {
			curSize = 0;
			buffer = new StringBuffer();
		}

		for (; curSize < len; curSize++) {
			buffer.insert(0, c);
		}

		return buffer.toString();
	}
}
