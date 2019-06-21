package shenzhen.teamway.cconfig;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-05-30 15:46
 **/
@Configuration
public class MqttProvider {
    private static String urlTcp = "tcp://192.168.0.134:1883";
    private final static String CONNECTION_STRING = urlTcp;
    private final static boolean CLEAN_START = true;
    private final static short KEEP_ALIVE = 30;// 低耗网络，但是又需要及时获取数据，心跳30s
    public final static long RECONNECTION_ATTEMPT_MAX = -1;
    public final static long RECONNECTION_DELAY = 2000;
    public final static int SEND_BUFFER_SIZE = 2 * 1024 * 1024;//发送最大缓冲为2M



    @Bean
    @Scope("prototype")
    public BlockingConnection mqtt() throws Exception {
        MQTT mqtt = new MQTT();
        //设置服务端的ip
        mqtt.setHost(CONNECTION_STRING);
        //连接前清空会话信息
        mqtt.setCleanSession(CLEAN_START);
        //设置重新连接的次数
        mqtt.setReconnectAttemptsMax(RECONNECTION_ATTEMPT_MAX);
        //设置重连的间隔时间
        mqtt.setReconnectDelay(RECONNECTION_DELAY);
        //设置心跳时间
        mqtt.setKeepAlive(KEEP_ALIVE);
        //设置缓冲的大小
        mqtt.setSendBufferSize(SEND_BUFFER_SIZE);
        //创建连接
        BlockingConnection connection = mqtt.blockingConnection();
        //开始连接
        connection.connect();
        return connection;

    }
}