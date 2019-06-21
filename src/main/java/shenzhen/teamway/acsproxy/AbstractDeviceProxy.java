package shenzhen.teamway.acsproxy;

import com.sun.jna.NativeLong;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.QoS;
import org.springframework.beans.factory.annotation.Autowired;
import shenzhen.teamway.bean.AbstractDevice;

import java.util.HashMap;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-06-04 14:15
 **/
public abstract class AbstractDeviceProxy {
    private String topic = "zhaohongyu";
    //一个厂家一个map
    HashMap<String, AbstractDevice> haikangMap = new HashMap<String, AbstractDevice>();

    @Autowired
    BlockingConnection mqt;

    //初始化各个设备的sdk
    abstract void initDevice(String id);

    abstract void schudleGetState(String id);

    abstract void schudleGetEvent(String id);

    abstract void schudleIsOnline(String id);

    public void publishToIot(String s) {
        try {
            mqt.publish(topic, s.getBytes(), QoS.AT_LEAST_ONCE, false);
        } catch (Exception e) {
            System.out.println("dfwaf");
            e.printStackTrace();
        }
    }


}