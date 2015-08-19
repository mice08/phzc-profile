package com.phzc.profile.web.utils;

/**
 * Created by HANRAN177 on 2015/7/28.
 */
public class UserInfoUtils {
    public static String confuseUsrName(String usrName) {
        if (usrName == null || usrName.length() < 1) {
            return usrName;
        }
        String lastName = usrName.substring(usrName.length() - 1);
        return generateStars(usrName.length() - 1) + lastName;
    }

    public static String confuseCertId(String certId) {
        if (certId == null || certId.length() < 2) {
            return certId;
        }
        String firstCertId = certId.substring(0, 1);
        String lastCertId = certId.substring(certId.length() - 1);
        return firstCertId + generateStars(certId.length() - 2) + lastCertId;
    }

    public static String confuseUsrMp(String usrMp) {
        if (usrMp == null || usrMp.length() < 8) {
            return usrMp;
        }
        String firstMp = usrMp.substring(0, 3);
        String lastMp = usrMp.substring(usrMp.length() - 4, usrMp.length());
        return firstMp + generateStars(usrMp.length() - 8) + lastMp;
    }

    public static String confuseUsrEmail(String usrEmail) {
        if (usrEmail == null || usrEmail.indexOf("@") == -1) {
            return usrEmail;
        }
        String[] emails = usrEmail.split("@");
        String emailId = emails[0];
        String firstEmailId = emailId.substring(0, 1);
        String lastEmailId = emailId.substring(emailId.length() - 1);
        return firstEmailId + generateStars(emailId.length() - 2) + lastEmailId + "@" + emails[1];
    }

    public static String convertUsrSex(String usrSex) {
        if ("M".equals(usrSex)) {
            return "男";
        } else if ("F".equals(usrSex)) {
            return "女";
        } else if ("B".equals(usrSex)) {
            return "企业";
        } else {
            return "未知";
        }
    }

    public static String generateStars(int num) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < num; i++) {
            stringBuffer.append("*");
        }
        return stringBuffer.toString();
    }
}
