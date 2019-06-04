package shenzhen;

import com.alibaba.fastjson.JSONObject;
import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.QoS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import shenzhen.teamway.bean.*;

import java.util.Date;

@SpringBootApplication
public class AcsproxyApplication implements CommandLineRunner {
    @Autowired
    BlockingConnection mqt;

    public static void main(String[] args) {
        SpringApplication.run(AcsproxyApplication.class, args);

    }

    @Override
    public void run(String... args) throws Exception {
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
            Thread.sleep(5000);
            System.out.println("开");
            mqt.publish("zhaohongyu", s.getBytes(), QoS.AT_LEAST_ONCE, false);
        }
    }

}
