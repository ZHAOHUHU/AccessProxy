package shenzhen.teamway.acsproxy;

import com.sun.jna.NativeLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import shenzhen.teamway.acsproxy.wrapperBean.WrapperAccessMessage;
import shenzhen.teamway.bean.AbstractDevice;
import shenzhen.teamway.bean.ChannelInfo;
import shenzhen.teamway.bean.HostInfo;
import shenzhen.teamway.deviceproxy.TeamWayClass;

/**
 * @program: acsproxy
 * @description:
 * @author: Zhao Hong Ning
 * @create: 2019-06-04 14:19
 **/
@Component
public class HaiKangDoorDevice extends AbstractDeviceProxy {
    @Autowired
    @Lazy
    private HaiKangDoorDevice haiKangDoorDevice;
    @Autowired
    private WrapperAccessMessage w;

    @Override
    public void initDevice(String id) {
        TeamWayClass.init();
        final AbstractDevice a = new AbstractDevice();
        this.haikangMap.put(id, a);
        NativeLong nativeLongid = TeamWayClass.LoginDevice("192.168.12.39", "admin", "TY26811438", 8000);
        a.setId(nativeLongid);
    }

    @Async("pool")
    public void schudleGetState(String id) {
        while (true) {
            final int b = TeamWayClass.GetDVRConfig(this.haikangMap.get(id).getId(), 0);
            final String message = w.getDoorStatusMessage(b, new HostInfo(), new ChannelInfo());
            publishToIot(message);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @Async("pool")
    public void schudleGetEvent(String id) {
        while (true) {

        }
    }

    @Override
    void schudleIsOnline(String id) {
        while (true) {
            final boolean online = TeamWayClass.remoteControl(haikangMap.get(id).getId());

        }
    }


}