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
public class HaiKangFaceDevice extends AbstractDeviceProxy {
    @Autowired
    @Lazy
    private HaiKangFaceDevice haiKangFaceDevice;
    @Autowired
    private WrapperAccessMessage w;

    @Override
    public void initDevice(String id) {
        TeamWayClass.init();
        final AbstractDevice a = new AbstractDevice();
        this.haikangMap.put(id, a);
        NativeLong nativeLongid = TeamWayClass.LoginDevice("192.168.12.37", "admin", "ty26811438", 8000);
        a.setId(nativeLongid);
    }

    public void schudleGetState(String id) {
    }

    @Override
    @Async("pool")
    void schudleGetEvent(String id) {
        TeamWayClass.getAlarm(haiKangFaceDevice);
        System.out.println("sdfsdf");
    }

    @Override
    @Async("pool")
    void schudleIsOnline(String id) {
        while (true){
            final boolean online = TeamWayClass.remoteControl(haikangMap.get(id).getId());

        }

    }


}