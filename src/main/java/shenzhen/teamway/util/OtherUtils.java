package shenzhen.teamway.util;

import org.apache.tomcat.util.codec.binary.Base64;

import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-05-30 16:00
 **/
public class OtherUtils {
    private static String localip = null;

    public static String getDate2String(Date date) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        final String s = format.format(date);
        return s;
    }

    public static String getLocalIp() {
        if (null == localip) {
            InetAddress ia = null;
            try {
                ia = ia.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            String localname = ia.getHostName();
            localip = ia.getHostAddress();
        }
        return localip;
    }

    public String byte2String(byte[] b) {
        String imageString = Base64.encodeBase64String(b);
        return imageString;
    }

    public static void main(String[] args) {


    }
}