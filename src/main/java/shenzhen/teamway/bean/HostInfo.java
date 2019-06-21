package shenzhen.teamway.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-05-30 16:07
 **/
public class HostInfo {
    @JSONField(name = "Description")
    private String Description;
    @JSONField(name = "Location")
    private String Location;
    @JSONField(name = "index")
    private int index;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}