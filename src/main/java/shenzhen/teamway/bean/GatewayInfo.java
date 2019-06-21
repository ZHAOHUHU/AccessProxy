package shenzhen.teamway.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-05-30 16:06
 **/
public class GatewayInfo {
    @JSONField(name = "Description")
    private String Description;
    @JSONField(name = "IP")
    private String IP;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }
}