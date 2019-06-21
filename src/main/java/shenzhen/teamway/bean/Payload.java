package shenzhen.teamway.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-05-30 16:06
 **/
public class Payload {
    @JSONField(name = "FaceImage")
    private String FaceImage;
    @JSONField(name = "VehicleNoImage")
    private String VehicleNoImage;
    @JSONField(name = "VehicleImage")
    private String VehicleImage;
    @JSONField(name = "CardId")
    private String CardId;
    @JSONField(name = "DoorStatus")
    private String DoorStatus;
    @JSONField(name = "EventCode")
    private int EventCode;
    @JSONField(name = "TimeStamp")
    private Date TimeStamp;

    public String getFaceImage() {
        return FaceImage;
    }

    public void setFaceImage(String faceImage) {
        FaceImage = faceImage;
    }

    public String getVehicleNoImage() {
        return VehicleNoImage;
    }

    public void setVehicleNoImage(String vehicleNoImage) {
        VehicleNoImage = vehicleNoImage;
    }

    public String getVehicleImage() {
        return VehicleImage;
    }

    public void setVehicleImage(String vehicleImage) {
        VehicleImage = vehicleImage;
    }

    public String getCardId() {
        return CardId;
    }

    public void setCardId(String cardId) {
        CardId = cardId;
    }

    public String getDoorStatus() {
        return DoorStatus;
    }

    public void setDoorStatus(String doorStatus) {
        DoorStatus = doorStatus;
    }

    public int getEventCode() {
        return EventCode;
    }

    public void setEventCode(int eventCode) {
        EventCode = eventCode;
    }

    public Date getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        TimeStamp = timeStamp;
    }
}