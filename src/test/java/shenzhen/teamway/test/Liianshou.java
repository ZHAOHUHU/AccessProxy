package shenzhen.teamway.test;

import com.sun.jna.NativeLong;
import shenzhen.teamway.deviceproxy.TeamWayClass;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-05-31 09:08
 **/
public class Liianshou {
    public static void a(String s) {
        System.out.println(s);
    }

    public static void b(String s) {
        final String s1 = new String();
        a(s1);
    }

    public static void main(String[] args) throws InterruptedException {
        TeamWayClass.init();
        NativeLong id = TeamWayClass.LoginDevice("192.168.12.37", "admin", "ty26811438", 8000);
       TeamWayClass.remoteControl(id);
       /*
        */
    }
}