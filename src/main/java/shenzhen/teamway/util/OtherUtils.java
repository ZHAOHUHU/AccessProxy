package shenzhen.teamway.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-05-30 16:00
 **/
public class OtherUtils {
    public static String getDate2String(Date date) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        final String s = format.format(date);
        return s;
    }

    public static void main(String[] args) {



    }
}