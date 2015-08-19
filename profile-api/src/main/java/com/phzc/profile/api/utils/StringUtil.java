package com.phzc.profile.api.utils;

import java.util.Date;
import java.util.Random;

/**
 * 字符串通用类库
 * @author Administrator
 *
 */
public class StringUtil {
	static public Random secureRand = new Random(new Date().getTime());
    /**
    * 清除XSS攻击的字符串
    */
    public static String getSafeStringXSS(String s){
        if (s == null || "".equals(s)) {  
            return s;  
        }  
        StringBuilder sb = new StringBuilder(s.length() + 16);  
        for (int i = 0; i < s.length(); i++) {  
            char c = s.charAt(i);  
            switch (c) {  
	            case '>':
					sb.append('＞');//全角大于号
					break;
				case '<':
					sb.append('＜');//全角小于号
					break;
				case '\'':
					sb.append('‘');//全角单引号
					break;
				case '\"':
					sb.append('“');//全角双引号
					break;
				case '&':
					sb.append('＆');//全角
					break;
				case '\\':
					sb.append('＼');//全角斜线
					break;
				case '#':
					sb.append('＃');//全角井号
					break;
				default:
					sb.append(c);
					break;
            }
        }  
        return sb.toString(); 
    }
}
