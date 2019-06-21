package shenzhen.teamway.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-05-30 16:08
 **/
public class AccessMessage {
    @JSONField(name = "Version")
    private String Version = "1.0";
    @JSONField(name = "DeviceType")
    private String DeviceType;
    @JSONField(name = "Vendor")
    private String Vendor;
    @JSONField(name = "DataType")
    private String DataType;
    @JSONField(name = "Payload")
    private Payload Payload;
    @JSONField(name = "GatewayInfo")
    private GatewayInfo GatewayInfo;
    @JSONField(name = "HostInfo")
    private HostInfo HostInfo;
    @JSONField(name = "ChannelInfo")
    private shenzhen.teamway.bean.ChannelInfo ChannelInfo;

    public String getVendor() {
        return Vendor;
    }

    public void setVendor(String vendor) {
        Vendor = vendor;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getDataType() {
        return DataType;
    }

    public void setDataType(String dataType) {
        DataType = dataType;
    }

    public shenzhen.teamway.bean.Payload getPayload() {
        return Payload;
    }

    public void setPayload(shenzhen.teamway.bean.Payload payload) {
        Payload = payload;
    }

    public shenzhen.teamway.bean.GatewayInfo getGatewayInfo() {
        return GatewayInfo;
    }

    public void setGatewayInfo(shenzhen.teamway.bean.GatewayInfo gatewayInfo) {
        GatewayInfo = gatewayInfo;
    }

    public shenzhen.teamway.bean.HostInfo getHostInfo() {
        return HostInfo;
    }

    public void setHostInfo(shenzhen.teamway.bean.HostInfo hostInfo) {
        HostInfo = hostInfo;
    }

    public shenzhen.teamway.bean.ChannelInfo getChannelInfo() {
        return ChannelInfo;
    }

    public void setChannelInfo(shenzhen.teamway.bean.ChannelInfo channelInfo) {
        ChannelInfo = channelInfo;
    }
}