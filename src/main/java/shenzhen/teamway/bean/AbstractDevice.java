package shenzhen.teamway.bean;

import com.sun.jna.NativeLong;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-06-13 11:15
 **/
public class AbstractDevice {
    NativeLong id;

    public NativeLong getId() {
        return id;
    }

    public void setId(NativeLong id) {
        this.id = id;
    }
}