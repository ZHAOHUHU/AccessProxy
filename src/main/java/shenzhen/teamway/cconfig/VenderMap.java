package shenzhen.teamway.cconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import shenzhen.teamway.acsproxy.AbstractDeviceProxy;
import shenzhen.teamway.acsproxy.HaiKangDoorDevice;
import shenzhen.teamway.acsproxy.HaiKangFaceDevice;
import shenzhen.teamway.bean.AbstractDevice;
import shenzhen.teamway.util.ApplicationContextProviderUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-06-04 14:13
 **/
@Configuration
public class VenderMap {
    private static final Integer haikang = 1;
    @Autowired
    private HaiKangFaceDevice haiKangFaceDevice;
    @Autowired
    private HaiKangDoorDevice haiKangDoorDevice;

    @Bean
    public Map<Integer, Map<String, AbstractDeviceProxy>> map() {
        Map<String, AbstractDeviceProxy> m = new HashMap<>();
        //全局唯一主机id
        m.put("aa", haiKangDoorDevice);
        m.put("bb", haiKangDoorDevice);
        m.put("cc", haiKangDoorDevice);
        m.put("dd", haiKangFaceDevice);
        Map<Integer, Map<String, AbstractDeviceProxy>> map = new HashMap<>();

        map.put(haikang, m);
        return map;
    }

    //门状态：1- 休眠，2- 常开状态，3- 常闭状态，4- 普通状态
    private static final Integer open = 1;
    private static final Integer opendefault = 2;
    private static final Integer close = 3;
    private static final Integer closedefault = 4;

    @Bean
    public Map<Integer, String> mapStatus() {
        Map<Integer, String> map = new HashMap<>();
        map.put(open, "Open");
        map.put(opendefault, "OpenDefault");
        map.put(close, "Close");
        map.put(closedefault, "CloseDefault");
        return map;
    }
}