package shenzhen.teamway.acsproxy;

import com.alibaba.fastjson.JSONObject;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import shenzhen.AcsproxyApplication;
import shenzhen.teamway.bean.*;

import java.util.Date;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-06-04 15:46
 **/
@Service
public class ThreadService {
    @Autowired
    BlockingConnection mqt;
    private Logger log = LoggerFactory.getLogger(ThreadService.class);

    @Async("pool")
    public void aa() {
        final Payload p = new Payload();
        p.setCardId("123456789");
        p.setDoorStatus("open");
        p.setEventCode(12);
        p.setFaceImage("tupian");
        p.setTimeStamp((new Date()));
        p.setVehicleImage("vehicieimage");
        p.setVehicleNoImage("noviceimage");
        final GatewayInfo g = new GatewayInfo();
        g.setDescription("getwauinfo");
        g.setIP("192.168.12.188");
        final HostInfo h = new HostInfo();
        h.setDescription("hostid");
        h.setIndex(0);
        h.setLocation("location");
        final AccessMessage a = new AccessMessage();
        final ChannelInfo c = new ChannelInfo();
        a.setChannelInfo(c);
        a.setDataType("Event");
        a.setDeviceType("door");
        a.setGatewayInfo(g);
        a.setHostInfo(h);
        a.setPayload(p);
        a.setVendor("haikang");
        final String s = JSONObject.toJSONString(a);
        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("开");
            try {
                mqt.publish("zhaohongyu", s.getBytes(), QoS.AT_LEAST_ONCE, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}