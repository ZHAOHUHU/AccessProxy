package shenzhen.teamway.acsproxy.wrapperBean;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shenzhen.teamway.bean.*;
import shenzhen.teamway.util.OtherUtils;

import java.util.Date;
import java.util.Map;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-06-05 14:10
 **/
@Component
public class WrapperAccessMessage {
    static final String DEVICE_TYPE_DOOR = "Door";
    static final String DEVICE_TYPE_BARRIER = "Barrier";
    static final String DATA_TYPE_EVENY = "Event";
    static final String DEVICE_TYPE_POLLING = "Polling";
    static final String VENDER_HAIKANG = "haikang";

    @Autowired
    private Map<Integer, String> mapStatus;

    public String getDoorStatusMessage(Integer status, HostInfo h, ChannelInfo c) {
        final Payload p = new Payload();
        p.setDoorStatus(mapStatus.get(status));
        p.setTimeStamp((new Date()));
        final GatewayInfo g = new GatewayInfo();
        g.setDescription("本机软网关");
        g.setIP(OtherUtils.getLocalIp());
        final AccessMessage a = new AccessMessage();
        a.setChannelInfo(c);
        a.setDataType(WrapperAccessMessage.DEVICE_TYPE_POLLING);
        a.setDeviceType(WrapperAccessMessage.DEVICE_TYPE_DOOR);
        a.setVendor(WrapperAccessMessage.VENDER_HAIKANG);
        a.setGatewayInfo(g);
        a.setHostInfo(h);
        a.setPayload(p);
        String s = JSONObject.toJSONString(a);
        return s;
    }

    public String getEventMessage(Payload p) {
        final ChannelInfo c = new ChannelInfo();
        c.setLocation("linduan");
        c.setIndex(2);
        c.setDescription("channeldescription");
        final GatewayInfo g = new GatewayInfo();
        g.setDescription("getwauinfo");
        g.setIP("192.168.12.188");
        final HostInfo h = new HostInfo();
        h.setDescription("hostid");
        h.setIndex(0);
        h.setLocation("location");
        final AccessMessage a = new AccessMessage();
        a.setChannelInfo(c);
        a.setDataType("Event");
        a.setDeviceType("door");
        a.setGatewayInfo(g);
        a.setHostInfo(h);
        a.setPayload(p);
        a.setVendor("haikang");
        a.setVersion("1.0");
        String s = JSONObject.toJSONString(a);
        return s;
    }


}