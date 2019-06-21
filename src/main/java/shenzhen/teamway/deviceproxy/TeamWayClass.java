package shenzhen.teamway.deviceproxy;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import shenzhen.teamway.acsproxy.AbstractDeviceProxy;
import shenzhen.teamway.deviceproxy.TeamWayInterface.FRemoteConfigCallback;

import java.util.logging.Logger;


public class TeamWayClass {
    public static Logger log = Logger.getAnonymousLogger();
    /**
     * 布防返回值
     */
    static NativeLong lAlarmHandlee;
    static TeamWayInterface hCNetSDK = TeamWayInterface.INSTANCE;
    private static volatile boolean isInit = false;
    /**in 参数用.write(), out参数用.read */
    /**
     * dwCommand 设备在线检测
     */
    public static final int NET_DVR_CHECK_USER_STATUS = 20005;
    /**
     * sdk6.6.3 参数dwCommand=2127 含义：获取门禁主机工作状态
     */
    public static final int NET_DVR_GET_ACS_WORK_STATUS = 2123;
    /**
     * 启动长连接  sdk6.6.17 参数dwCommand=2116 含义:获取卡参数
     */
    public static final int NET_DVR_GET_CARD_CFG = 2116;

    static NativeLong lHandlee;//启动长链接的反回值
    static NativeLong userId;


    /**
     * 初始化
     *
     * @return
     */
    public static void init() {
        if (isInit) {
            return;
        }
        hCNetSDK.NET_DVR_SetLogToFile(true, null, false);// 启用日志文件写入接口
        boolean b = hCNetSDK.NET_DVR_Init();// 初始化
        if (b) {
            log.info("初始化成功");
            isInit = b;
        } else {
            int numbeFalse = hCNetSDK.NET_DVR_GetLastError();
            log.info("初始化失败，错误代码：" + numbeFalse);
        }
    }

    /**
     * 登录
     *
     * @return NativeLong id
     */
    public static NativeLong LoginDevice(String ip, String user, String password, int port) {
        TeamWayInterface.NET_DVR_USER_LOGIN_INFO struLoginInfo = new TeamWayInterface.NET_DVR_USER_LOGIN_INFO();//登录信息结构体
        TeamWayInterface.NET_DVR_DEVICEINFO_V40 struDeviceInfo = new TeamWayInterface.NET_DVR_DEVICEINFO_V40();//设备信息结构体

        for (int i = 0; i < ip.length(); i++) {
            struLoginInfo.sDeviceAddress[i] = (byte) ip.charAt(i);
        }

        for (int i = 0; i < user.length(); i++) {
            struLoginInfo.sUserName[i] = (byte) user.charAt(i);
        }

        for (int i = 0; i < password.length(); i++) {
            struLoginInfo.sPassword[i] = (byte) password.charAt(i);
        }
        struLoginInfo.wPort = (short) port;
        struLoginInfo.write();
        NativeLong id = hCNetSDK.NET_DVR_Login_V40(struLoginInfo.getPointer(), struDeviceInfo.getPointer());// getPointer()返回对象指针
        if ("-1".equals(String.valueOf(id).trim())) {
            int numbeFalse = hCNetSDK.NET_DVR_GetLastError();
            log.info("登录失败,错误代码：" + numbeFalse);
        } else {
            int numbeFalse = hCNetSDK.NET_DVR_GetLastError();
            log.info("登录成功");

        }
        userId = id;
        return id;
    }


    /**
     * 检测设备是否在线 sdk6.15.1
     *
     * @returnTRUE 表示在线，FALSE 表示与设备通信失败 或者返回错误状态
     */
    public static boolean remoteControl(NativeLong userId) {
        boolean bool = hCNetSDK.NET_DVR_RemoteControl(userId, NET_DVR_CHECK_USER_STATUS, null, 0);
        if (bool) {
            log.info("设备在线");
        } else {
            int numbeFalse = hCNetSDK.NET_DVR_GetLastError();
            if (numbeFalse != 0) {
                String a = hCNetSDK.NET_DVR_GetErrorMsg(null);
                log.info("检测错误，错误代码：" + numbeFalse + "，错误信息：" + a);
            } else {
                log.info("设备不在线");
            }
        }
        return bool;
    }

    public static byte GetDVRConfig(NativeLong userId, int index) {
        TeamWayInterface.NET_DVR_ACS_WORK_STATUS status = new TeamWayInterface.NET_DVR_ACS_WORK_STATUS();//dwCommand=2127时, lpOutBuffer对应接构体
        IntByReference lpBytesReturned = new IntByReference();
        boolean bool = hCNetSDK.NET_DVR_GetDVRConfig(userId, NET_DVR_GET_ACS_WORK_STATUS, new NativeLong(-1), status.getPointer(), status.size(), lpBytesReturned);
        status.read();
        byte byDoorStatus = 99;
        if (index < 32) {
            if (bool) {
                byDoorStatus = status.byDoorStatus[index];
                log.info("门状态获取成功:" + byDoorStatus);
            } else {
                int numbeFalse = hCNetSDK.NET_DVR_GetLastError();
                log.info("门状态获取失败,错误代码：" + numbeFalse);

            }
        }
        return byDoorStatus;
    }

    /**
     * 启动长链接sdk6.6.17
     *
     * @return -1 表示失败，其他值作为 NET_DVR_SendRemoteConfig 等接口的句柄
     */
    public static NativeLong startRemoteConfig(NativeLong userId, int dwCommand, Pointer lpInBuffer, int dwInBufferLen, FRemoteCallback cbStateCallback, Pointer pUserData) {

        lHandlee = hCNetSDK.NET_DVR_StartRemoteConfig(userId, dwCommand, lpInBuffer, dwInBufferLen, cbStateCallback, pUserData);// 启动长连接
        if ("-1".equals(String.valueOf(lHandlee).trim())) {
            int numbeFalse = hCNetSDK.NET_DVR_GetLastError();
            log.info("长连接启动失败,错误代码：" + numbeFalse);
        } else {
            log.info("长连接启动成功" + lHandlee);
        }
        return lHandlee;
    }

    /**
     * 回调函数(启动长链接)
     *
     * @return -1 表示失败，其他值作为 NET_DVR_SendRemoteConfig 等接口的句柄
     */
    public static class FRemoteCallback implements FRemoteConfigCallback {
        TeamWayInterface.NET_DVR_CARD_CFG_V50 ndc = new TeamWayInterface.NET_DVR_CARD_CFG_V50();//卡参数配置结构体
        private String cardID = "";
        private int dwType;
        private Pointer lpBuffer;
        private int dwBufLen;
        private Pointer pUserData;

        public FRemoteCallback(int dwType, Pointer lpBuffer, int dwBufLen, Pointer pUserData) {
            this.dwType = dwType;
            this.lpBuffer = lpBuffer;
            this.dwBufLen = dwBufLen;
            this.pUserData = pUserData;
        }

        public void invoke(int dwType, Pointer lpBuffer, int dwBufLen, Pointer pUserData) {// 取 lpBuffer中的卡号 在NET_DVR_CARD_CFG结构体中
            dwType = this.dwType;
            lpBuffer = this.lpBuffer;
            dwBufLen = this.dwBufLen;
            pUserData = this.pUserData;
            for (int j = 0; j < ndc.byCardNo.length; j++) {
                cardID = cardID + ndc.byCardNo[j];
            }
        }
    }

    /**
     * 注册报警回调函数
     *
     * @return
     */
    public static boolean setDVRMessageCallBack_V31(AbstractDeviceProxy abs) {
        TeamWayInterface.MSGCallBackV31 msgCallBackV31 = new TeamWayInterface.MSGCallBackV31();
        msgCallBackV31.setMqt(abs);
        boolean bool = hCNetSDK.NET_DVR_SetDVRMessageCallBack_V31(msgCallBackV31, null);
        if (bool) {
            log.info("设置报警回调函数成功");
        } else {
            int numbeFalse = hCNetSDK.NET_DVR_GetLastError();
            String a = hCNetSDK.NET_DVR_GetErrorMsg(null);
            log.info("设置报警回调函数,错误代码：" + numbeFalse + "，错误信息：" + a);
        }
        return bool;
    }

    public static void getAlarm(AbstractDeviceProxy a) {
        setDVRMessageCallBack_V31(a);
        setupAlarmChan_V41();
    }

    /**
     * 建立报警上传通道，获取报警等信息
     *
     * @return
     */
    public static NativeLong setupAlarmChan_V41() {
        TeamWayInterface.NET_DVR_SETUPALARM_PARAM nds = new TeamWayInterface.NET_DVR_SETUPALARM_PARAM();
        nds.dwSize = nds.size();
        nds.byLevel = 1;//布防等级
        nds.byAlarmInfoType = 1;//智能交通报警信息上传类型
        nds.byRetAlarmTypeV40 = 0;
        nds.byRetDevInfoVersion = 1;//CVR上传报警信息类型
        nds.byRetVQDAlarmType = 1;//VQD报警上传类型（仅对接VQD诊断功能的设备有效）
        nds.byFaceAlarmDetection = 0;//人脸报警信息类型  抓拍
        nds.bySupport = 1;
        nds.byBrokenNetHttp = 31;//断网传续类型 全部续传
//		nds.wTaskNo= ;//任务号
        nds.byDeployType = 0;//布防类型：0-客户端布防，1-实时布防       测试设备不支持实施布防
        nds.byAlarmTypeURL = 0;//报警图片数据类型  二进制传输
//		nds.byCustomCtrl=1;//是否上传副驾驶图片0- 不上传，1- 上传
        nds.write();

        NativeLong lAlarmHandle = hCNetSDK.NET_DVR_SetupAlarmChan_V41(userId, nds);
        if ("-1".equals(String.valueOf(lAlarmHandle).trim())) {
            int numbeFalse = hCNetSDK.NET_DVR_GetLastError();
            String a = hCNetSDK.NET_DVR_GetErrorMsg(null);
            log.info("布防失败,错误代码：" + numbeFalse + ",错误信息" + a);
        } else {
            log.info("布防成功 ");
        }
        lAlarmHandlee = lAlarmHandle;
        return lAlarmHandle;
    }

    /**
     * 撤防 撤销报警上传通道
     *
     * @return
     */
    public static boolean closeAlarmChan_V30() {
        boolean bool = hCNetSDK.NET_DVR_CloseAlarmChan_V30(lAlarmHandlee);
        int numbeFalse = hCNetSDK.NET_DVR_GetLastError();
        if (bool && numbeFalse == 0) {
            log.info("撤防成功");
        } else {
            String msg = hCNetSDK.NET_DVR_GetErrorMsg(null);
            log.info("撤防失败,错误代码：" + numbeFalse + ",错误信息" + msg);
        }
        return bool;
    }

    /**
     * 门禁控制 sdk6.8.1
     *
     * @param lGatewayIndex 门禁序号，从 1 开始，-1 表示对所有门进行操作
     * @param dwStaic       命令值：0- 关闭，1- 打开，2- 常开，3- 常关
     */
    public static void ControlGateway(NativeLong userId, long lGatewayIndex, int dwStaic) {
        boolean bool = hCNetSDK.NET_DVR_ControlGateway(userId, lGatewayIndex, dwStaic);
        if (bool) {
            log.info("门禁控制成功");
        } else {
            int numbeFalse = hCNetSDK.NET_DVR_GetLastError();
            log.info("门禁控制失败,错误代码：" + numbeFalse);
        }
    }

    /**
     * 注销用户
     *
     * @param userId
     */
    public static void LogOut(NativeLong userId) {
        boolean bool = hCNetSDK.NET_DVR_Logout(userId);
        if (bool) {
            log.info("注销成功");
        } else {
            int numbeFalse = hCNetSDK.NET_DVR_GetLastError();
            log.info("注销失败,错误代码：" + numbeFalse);
        }
    }

    /**
     * 释放sdk资源
     */
    public static void Cleanup() {//注销用户后，释放sdk资源
        boolean bool = hCNetSDK.NET_DVR_Cleanup();
        if (bool) {
            log.info("释放SDK资源成功");
        } else {
            int numbeFalse = hCNetSDK.NET_DVR_GetLastError();
            log.info("释放SDK资源失败,错误代码：" + numbeFalse);
        }
    }


}
