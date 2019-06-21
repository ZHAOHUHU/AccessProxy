package shenzhen.teamway.acsproxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shenzhen.teamway.deviceproxy.TeamWayClass;

import java.util.List;
import java.util.Map;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-06-04 14:24
 **/
@Component
public class AscProxy {

    @Autowired
    private Map<Integer, Map<String, AbstractDeviceProxy>> map;

    public void init() {
        for (Integer integer : map.keySet()) {
            //如果是海康 1
            for (Map<String, AbstractDeviceProxy> maps : map.values()) {
                for (Map.Entry<String, AbstractDeviceProxy> deviceProxyEntry : maps.entrySet()) {
                    deviceProxyEntry.getValue().initDevice(deviceProxyEntry.getKey());
                }
            }

        }
    }


    public void sendDeviceStatus() {
        for (Integer integer : map.keySet()) {
            //如果是海康 1
            for (Map<String, AbstractDeviceProxy> maps : map.values()) {
                for (Map.Entry<String, AbstractDeviceProxy> deviceProxyEntry : maps.entrySet()) {
                    deviceProxyEntry.getValue().schudleGetState(deviceProxyEntry.getKey());
                }
            }

        }
    }

    public void sendDeviceEvent() {
        for (Integer integer : map.keySet()) {
            //如果是海康 1
            for (Map<String, AbstractDeviceProxy> maps : map.values()) {
                for (Map.Entry<String, AbstractDeviceProxy> deviceProxyEntry : maps.entrySet()) {
                    deviceProxyEntry.getValue().schudleGetEvent(deviceProxyEntry.getKey());
                }
            }

        }
    }


}