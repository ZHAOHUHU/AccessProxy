package shenzhen.teamway.deviceproxy;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import org.apache.tomcat.util.codec.binary.Base64;
import org.fusesource.mqtt.client.BlockingConnection;
import org.slf4j.LoggerFactory;
import shenzhen.AcsproxyApplication;
import shenzhen.teamway.acsproxy.AbstractDeviceProxy;
import shenzhen.teamway.acsproxy.wrapperBean.WrapperAccessMessage;
import shenzhen.teamway.bean.Payload;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.logging.Logger;

//import com.sun.jna.Callback;
//import com.sun.jna.Library;
//import com.sun.jna.Native;
//import com.sun.jna.NativeLong;
//import com.sun.jna.Pointer;
//import com.sun.jna.Structure;
//import com.sun.jna.ptr.IntByReference;

public interface TeamWayInterface extends Library {

    //	TeamWayInterface INSTANCE = (TeamWayInterface) Native.loadLibrary("hcnetsdk",TeamWayInterface.class);//加载库文件
    TeamWayInterface INSTANCE = (TeamWayInterface) Native.loadLibrary("F:\\CH-HCNetSDKV6.0.2.35_build20190411_Win64\\CH-HCNetSDKV6.0.2.35_build20190411_Win64\\库文件\\HCNetSDK.dll", TeamWayInterface.class);//加载库文件
    public static Logger log = Logger.getAnonymousLogger();
    public static org.slf4j.Logger logg = LoggerFactory.getLogger(TeamWayInterface.class);
    public static final int NET_DVR_DEV_ADDRESS_MAX_LEN = 129;
    public static final int NET_DVR_LOGIN_USERNAME_MAX_LEN = 64;
    public static final int NET_DVR_LOGIN_PASSWD_MAX_LEN = 64;
    public static final int MAX_CARD_READER_NUM = 64;
    public static final int SERIALNO_LEN = 48;   //序列号长度
    public static final int MAX_CASE_SENSOR_NUM = 8;
    public static final int MAX_ALARMHOST_ALARMIN_NUM = 512;
    public static final int MAX_ALARMHOST_ALARMOUT_NUM = 512;
    public static final int ACS_CARD_NO_LEN = 32;
    public static final int CARD_PASSWORD_LEN = 8;
    public static final int MAX_CARD_RIGHT_PLAN_NUM = 4;
    public static final int MAX_GROUP_NUM = 128;
    public static final int NAME_LEN = 128;
    public static final int MAX_LOCK_CODE_LEN = 8;
    public static final int MAX_DOOR_CODE_LEN = 8;
    public static final int MACADDR_LEN = 16;
    public static final int MAX_NAMELEN = 16;
    public static final int MAX_CARD_READER_NUM_512 = 512;
    public static final int MAX_DOOR_NUM_256 = 256;
    /**
     * 每周天数
     */
    public static final int MAX_DAYS = 7;
    /**
     * 最大时段
     */
    public static final int MAX_TIMESEGMENT_V30 = 8;
    /**
     * 最大门数量
     */
    public static final int MAX_DOOR_NUM = 32;
    /**
     * dwCommand 门禁主机报警信息
     */
    public static final int COMM_ALARM_ACS = 0x5002;
    /**
     * 最大门数量
     */

    /********************************************* 接口 *******************************************************************************************************/
    //sdk初始化
    boolean NET_DVR_Init();

    //获取错误值
    int NET_DVR_GetLastError();

    //启用日志文件写入接口
    boolean NET_DVR_SetLogToFile(boolean bLogEnable, String strLogDir, boolean bAutoDel);

    /**
     * 获取当前门闸状态 获取设备配置信息sdk6.6.3
     *
     * @param lUserID
     * @param dwCommand       设备配置命令，详见表 6.6
     * @param lChannel        通道号，如果命令不需要通道号，该参数无效，置为 0xFFFFFFFF即可
     * @param lpOutBuffer     [out]  接收数据的缓冲指针，详见表 6.6
     * @param dwOutBufferSize 接收数据的缓冲长度(以字节为单位)，不能为 0
     * @param lpBytesReturned [out]  实际收到的数据长度指针，不能为 NULL
     * @return boolean
     */
    boolean NET_DVR_GetDVRConfig(NativeLong lUserID, int dwCommand, NativeLong lChannel, Pointer lpOutBuffer, int dwOutBufferSize, IntByReference lpBytesReturned);///获取设备配置信息

    //错误信息
    String NET_DVR_GetErrorMsg(NativeLongByReference pErrorNo);

    //注销用户
    boolean NET_DVR_Logout(NativeLong lUserID);

    //释放sdk资源
    boolean NET_DVR_Cleanup();

    //对于同步登录，接口返回-1 表示登录失败，其他值表示返回的用户 ID 值
    NativeLong NET_DVR_Login_V40(Pointer pLoginInfo, Pointer lpDeviceInfo);

    NativeLong NET_DVR_Login_V30(String sDVRIP, short wDVRPort, String sUserName, String sPassword, Pointer lpDeviceInfo);

    /**
     * 门禁控制 sdk6.8.1
     *
     * @param lGatewayIndex 门禁序号，从 1 开始，-1 表示对所有门进行操作
     * @param dwStaic       命令值：0- 关闭，1- 打开，2- 常开，3- 常关
     * @author dell
     */
    boolean NET_DVR_ControlGateway(NativeLong lUserID, long lGatewayIndex, int dwStaic);

    /**
     * 启动长链接配置sdk6.6.17
     *
     * @param dwCommand       配置命令，详见表 6.18
     * @param lpInBuffer      详见sdk6.18表 根据dwCommand不同 对应不同的结构体
     * @param dwInBufferLen   输入缓冲大小
     * @param cbStateCallback 卡号下发的回调函数 sdk3.5>sdk8.29
     * @param pUserData       用户数据
     * @return 返回值： -1 表示失败，其他值作为 NET_DVR_SendRemoteConfig 等接口的句柄
     * @author dell
     */
    NativeLong NET_DVR_StartRemoteConfig(NativeLong lUserID, int dwCommand, Pointer lpInBuffer, int dwInBufferLen, FRemoteConfigCallback cbStateCallback, Pointer pUserData);

    /**
     * 发送长链接数据sdk6.6.18
     *
     * @param lHandle    长连接句柄，NET_DVR_StartRemoteConfig 的返回值
     * @param dwDataType 数据类型 详见表 6.9
     * @param pSendBuf   详见表 6.9	保存发送数据的缓冲区，与 dwDataType 有关   长连接中的命令不同 dwDataType与pSendBuf也会不同
     * @return
     * @author dell
     */
    boolean NET_DVR_SendRemoteConfig(NativeLong lHandle, int dwDataType, String pSendBuf, int dwBufSize);

    /****************************************接口 end *******************************************************************************************************/

    /**
     * 回调函数----获取卡号sdk6.6.17  sdk3.5
     *
     * @author dell
     */
    public static interface FRemoteConfigCallback extends Callback {
        public void invoke(int dwType, Pointer lpBuffer, int dwBufLen, Pointer pUserData);

    }

    /**
     * 异步登录回调函数----获取ID
     *
     * @author dell
     */
    public static interface FLoginResultCallBack extends Callback {
        public int invoke(NativeLong lUserID, int dwResult, Pointer lpDeviceinfo, Pointer pUser);
    }
/*********************************************************/
    /**
     * 报警布防参数
     */
    public class NET_DVR_SETUPALARM_PARAM extends Structure {
        public int dwSize;
        /**
         * 布防优先级：0- 一等级（高），1- 二等级（中），2- 三等级（低）
         */
        public byte byLevel;
        public byte byAlarmInfoType;
        public byte byRetAlarmTypeV40;
        public byte byRetDevInfoVersion;
        public byte byRetVQDAlarmType;
        public byte byFaceAlarmDetection;
        public byte bySupport;
        public byte byBrokenNetHttp;
        public short wTaskNo;
        public byte byDeployType;
        public byte byRes1[] = new byte[3];
        public byte byAlarmTypeURL;
        public byte byCustomCtrl;

    }

    /**
     * NET_DVR_ALARMER:报警设备信息
     */
    public class NET_DVR_ALARMER extends Structure {
        public byte byUserIDValid;
        public byte bySerialValid;
        public byte byVersionValid;
        public byte byDeviceNameValid;
        public byte byMacAddrValid;
        public byte byLinkPortValid;
        public byte byDeviceIPValid;
        public byte bySocketIPValid;
        public NativeLong lUserID;
        public byte sSerialNumber[] = new byte[SERIALNO_LEN];
        public int dwDeviceVersion;
        public byte sDeviceName[] = new byte[NAME_LEN];
        public byte byMacAddr[] = new byte[MACADDR_LEN];
        public short wLinkPort;
        public byte sDeviceIP[] = new byte[128];
        public byte sSocketIP[] = new byte[128];
        public byte byIpProtocol;
        public byte byRes2[] = new byte[11];
    }

    /**
     * 门禁主机报警信息
     */
    public class NET_DVR_ACS_ALARM_INFO extends Structure {
        public int dwSize;
        /**
         * 报警主类型
         */
        public int dwMajor;
        /**
         * 报警次类型
         */
        public int dwMinor;
        /**
         * 报警时间
         */
        public NET_DVR_TIME struTime;
        /**
         * 网络操作者的用户名
         */
        public byte sNetUser[] = new byte[MAX_NAMELEN];
        /**
         * 远程主机地址
         */
        public NET_DVR_IPADDR struRemoteHostAddr;
        /**
         * 报警信息详细参数
         */
        public NET_DVR_ACS_EVENT_INFO struAcsEventInfo;
        /**
         * 图片数据大小
         */
        public int dwPicDataLen;
        /**
         * 图片数据缓冲区
         */
        public Pointer pPicData;
        public byte byRes[] = new byte[24];


    }

    /**
     * 报警时间
     */
    public class NET_DVR_TIME extends Structure {
        public int dwYear;
        public int dwMonth;
        public int dwDay;
        public int dwHour;
        public int dwMinute;
        public int dwSecond;
    }

    /**
     * 远程主机地址
     */
    public class NET_DVR_IPADDR extends Structure {
        public byte sIpV4[] = new byte[16];
        public byte sIpV6[] = new byte[128];

    }

    /**
     * 报警信息详细参数
     */
    public class NET_DVR_ACS_EVENT_INFO extends Structure {
        public int dwSize;
        public byte[] byCardNo = new byte[32];
        public byte byCardType;
        public byte byWhiteListNo;
        public byte byReportChannel;
        public byte byCardReaderKind;
        public int dwCardReaderNo;
        public int dwDoorNo;
        public int dwVerifyNo;
        public int dwAlarmInNo;
        public int dwAlarmOutNo;
        public int dwCaseSensorNo;
        public int dwRs485No;
        public int dwMultiCardGroupNo;
        public short wAccessChannel;
        public byte byDeviceNo;
        public byte byDistractControlNo;
        public int dwEmployeeNo;
        public short wLocalControllerID;
        public byte byInternetAccess;
        public byte byType;
        public byte[] byRes = new byte[20];
    }
/*********************************************************/

    /**
     * 注册报警回调函数
     *
     * @author dell
     */
    public static interface MSGCallBack_V31 extends Callback {
        public boolean invoke(NativeLong lCommand, NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser);
    }

    /**
     * 注册报警回调函数的实现
     *
     * @author dell
     */
    public static class MSGCallBackV31 implements MSGCallBack_V31 {
        AbstractDeviceProxy a = null;
        WrapperAccessMessage w = new WrapperAccessMessage();

        @Override
        public boolean invoke(NativeLong lCommand, NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
            final Payload payload = AlarmDataHandle(lCommand, pAlarmer, pAlarmInfo, dwBufLen, pUser);
            final String eventMessage = w.getEventMessage(payload);
            a.publishToIot(eventMessage);
            return true;
        }

        public void setMqt(AbstractDeviceProxy a) {
            this.a = a;
        }
    }

    /**
     * @param fMessageCallBack 注册报警回调函数
     * @param pUser            用户数据
     */
    boolean NET_DVR_SetDVRMessageCallBack_V31(MSGCallBack_V31 fMessageCallBack, Pointer pUser);

    /**
     * 布防 建立报警上传通道获取报警信息
     *
     * @param lUserID
     * @param lpSetupParam
     * @return -1失败，其他值作为 撤销通道的句柄
     */
    NativeLong NET_DVR_SetupAlarmChan_V41(NativeLong lUserID, NET_DVR_SETUPALARM_PARAM lpSetupParam);

    /**
     * 设备在线状态检测
     * @param lUserID
     * @param dwCommand
     * @param lpInBuffer
     * @param dwInBufferSize
     * @return
     */
    boolean NET_DVR_RemoteControl(NativeLong lUserID, int dwCommand, Pointer lpInBuffer, int dwInBufferSize);
    /**
     * 布防 建立报警上传通道，获取报警等信息
     */
    NativeLong NET_DVR_SetupAlarmChan_V30(NativeLong lUserID);

    /**
     * 撤防
     */
    boolean NET_DVR_CloseAlarmChan_V30(NativeLong lAlarmHandle);

    public static Payload AlarmDataHandle(NativeLong lCommand, NET_DVR_ALARMER pAlarmer, Pointer pAlarmInfo, int dwBufLen, Pointer pUser) {
        String sAlarmType = new String();

        NET_DVR_ACS_ALARM_INFO strACSInfo = new NET_DVR_ACS_ALARM_INFO();//pAlarmInfo 对应的结构体 (报警信息)
        strACSInfo.write();
        Pointer pACSInfo = strACSInfo.getPointer();
        pACSInfo.write(0, pAlarmInfo.getByteArray(0, strACSInfo.size()), 0, strACSInfo.size());
        strACSInfo.read();
        sAlarmType = sAlarmType + "：门禁主机报警信息，卡号：" + new String(strACSInfo.struAcsEventInfo.byCardNo).trim() + "，卡类型："
                + strACSInfo.struAcsEventInfo.byCardType + "，报警主类型：" + strACSInfo.dwMajor + "，报警次类型：" + strACSInfo.dwMinor;
        //写入图片
        Payload p = new Payload();
        if (strACSInfo.dwPicDataLen > 0) {
            long offset = 0;
            ByteBuffer buffers = strACSInfo.pPicData.getByteBuffer(offset, strACSInfo.dwPicDataLen);
            byte[] bytes = new byte[strACSInfo.dwPicDataLen];
            buffers.rewind();// 将文件指针重新指向一个流的开头
            buffers.get(bytes);
            String imageString = Base64.encodeBase64String(bytes);
            p.setFaceImage(imageString);
        }
        String cardid = new String(strACSInfo.struAcsEventInfo.byCardNo).trim();
        p.setTimeStamp(new Date());
        p.setCardId(cardid);
        p.setEventCode(strACSInfo.dwMinor);
        //  TeamWayClass.log.info(sAlarmType);
        logg.info(sAlarmType);
        return p;
    }

    /***************************************** 结构体 *******************************************************************************************************/

    public class NET_DVR_USER_LOGIN_INFO extends Structure {
        public byte sDeviceAddress[] = new byte[NET_DVR_DEV_ADDRESS_MAX_LEN];
        public byte byUseTransport;
        public short wPort;
        public byte sUserName[] = new byte[NET_DVR_LOGIN_USERNAME_MAX_LEN];
        public byte sPassword[] = new byte[NET_DVR_LOGIN_PASSWD_MAX_LEN];
        public FLoginResultCallBack cbLoginResult;
        public Pointer pUser;
        public boolean bUseAsynLogin;
        public byte byProxyType;
        public byte byUseUTCTime;
        public byte byLoginMode;
        public byte byHttps;
        public NativeLong iProxyID;
        public byte byRes3[] = new byte[120];

    }

    public static class NET_DVR_DEVICEINFO_V40 extends Structure {
        public NET_DVR_DEVICEINFO_V30 struDeviceV30;//设备参数
        public byte bySupportLock;///设备是否支持锁定功能，bySupportLock 为 1 时，dwSurplusLockTime 和 byRetryLoginTime
        public byte byRetryLoginTime;///剩余可尝试登陆的次数，用户名、密码错误时，此参数有效
        public byte byPasswordLevel;///密码安全等级：0- 无效，1- 默认密码，2- 有效密码，3- 风险较高的密码
        public byte byProxyType;
        public int dwSurplusLockTime;///剩余锁定时间
        public byte byCharEncodeType;
        public byte bySupportDev5;
        public byte byLoginMode;
        public byte byRes2[] = new byte[253];
    }

    //NET_DVR_Login_V30()参数结构
    public class NET_DVR_DEVICEINFO_V30 extends Structure {
        public byte sSerialNumber[] = new byte[SERIALNO_LEN];
        public byte byAlarmInPortNum;
        public byte byAlarmOutPortNum;
        public byte byDiskNum;
        public byte byDVRType;
        public byte byChanNum;
        public byte byStartChan;
        public byte byAudioChanNum;
        public byte byIPChanNum;
        public byte byZeroChanNum;
        public byte byMainProto;
        public byte bySubProto;
        public byte bySupport;
        public byte bySupport1;
        public byte bySupport2;
        public short wDevType;
        public byte bySupport3;
        public byte byMultiStreamProto;
        public byte byStartDChan;
        public byte byStartDTalkChan;
        public byte byHighDChanNum;
        public byte bySupport4;
        public byte byLanguageType;
        public byte byVoiceInChanNum;
        public byte byStartVoiceInChanNo;
        public byte byRes3[] = new byte[2];
        public byte byMirrorChanNum;
        public short wStartMirrorChanNo;
        public byte byRes2[] = new byte[2];

    }

    /**
     * sdk6.6.3 > sdk8.8 dwCommand=2127时   lpOutBuffer【out】对应的结构体  简化
     *
     * @author dell
     */
    public class NET_DVR_ACS_WORK_STATUS extends Structure {
        public int dwSize;
        public byte byDoorLockStatus[] = new byte[MAX_DOOR_NUM];
        public byte byDoorStatus[] = new byte[MAX_DOOR_NUM];
        public byte byMagneticStatus[] = new byte[MAX_DOOR_NUM];
        public byte byCaseStatus[] = new byte[MAX_CASE_SENSOR_NUM];
        public short wBatteryVoltage;
        public byte byBatteryLowVoltage;
        public byte byPowerSupplyStatus;
        public byte byMultiDoorInterlockStatus;
        public byte byAntiSneakStatus;
        public byte byHostAntiDismantleStatus;
        public byte byIndicatorLightStatus;
        public byte byCardReaderOnlineStatus[] = new byte[MAX_CARD_READER_NUM];
        public byte byCardReaderAntiDismantleStatus[] = new byte[MAX_CARD_READER_NUM];
        public byte byCardReaderVerifyMode[] = new byte[MAX_CARD_READER_NUM];
        public byte bySetupAlarmStatus[] = new byte[MAX_ALARMHOST_ALARMIN_NUM];
        public byte byAlarmInStatus[] = new byte[MAX_ALARMHOST_ALARMIN_NUM];
        public byte byAlarmOutStatus[] = new byte[MAX_ALARMHOST_ALARMOUT_NUM];
        public int dwCardNum;
        public byte byRes2[] = new byte[32];

    }

    /**
     * sdk6.6.17 > sdk8.30 dwCommand=2116时   lpInBuffer对应的结构体  简化
     *
     * @author dell
     */
    public static class NET_DVR_CARD_CFG_COND extends Structure {
        /**
         * 结构体大小
         */
        public int dwSize;
        /**
         * 卡号
         */
        public int dwCardNum;
        /**
         * 设备是否进行卡号校验：0- 不校验，1- 校验
         */
        public byte byCheckCardNo;
        /**
         * 就地控制器序号，表示往就地控制器下发离线卡参数，0 代表是门禁主机
         */
        short wLocalControllerID;
        /**
         * 保留，置为零
         */
        byte byRes[] = new byte[26];
    }

    /**
     * NET_DVR_CARD_CFG_V50
     * 卡参数配置结构体 sdk8.29
     *
     * @author dell
     */
    public static class NET_DVR_CARD_CFG_V50 extends Structure {
        //结构体大小
        public int dwSize;
        //需要修改的卡参数（设置卡参数时有效
        public int dwModifyParamType;
        //卡号    包括特殊卡号
        public byte[] byCardNo = new byte[20];
        //卡是否有效：0- 无效，1- 有效（用于删除卡，设置时置为 0 进行删除，获取时此字段始终为 1）
        public byte byCardValid;
        /*卡类型：1- 普通卡（默认），2- 残疾人卡，3- 黑名单卡，4- 巡更卡，5- 胁迫卡，6- 超级卡，7- 来
        宾卡，8- 解除卡*/
        public byte byCardType;
        //是否为首卡：1- 是，0- 否
        public byte byLeaderCard;
        //门权限，按字节表示，1-为有权限，0-为无权限，从低位到高位依次表示对门 1-N 是否有权限 [MAX_DOOR_NUM_256]
        public byte[] byDoorRight = new byte[256];
        //有效期参数 sdk8.143
        public NET_DVR_VALID_PERIOD_CFG struValid;
        //所属群组，按字节表示，1-属于，0-不属于，从低位到高位表示是否从属群组 1~N [MAX_GROUP_NUM_128]
        public byte[] BelongGroup = new byte[128];
        //卡密码
        public byte[] byCardPassword = new byte[20];
        //卡权限计划，取值为计划模板编号，同个门不同计划模板采用权限或的方式处理 [MAX_DOOR_NUM_256][MAX_CARD_RIGHT_PLAN_NUM];
        public short[][] wCardRightPlan = new short[256][128];
        //最大刷卡次数，0 为无次数限制
        public int dwMaxSwipeTime;
        //已刷卡次数
        public int dwSwipeTime;
        //房间号
        public short wRoomNumber;
        //层号
        public short wFloorNumber;
        //工号
        public int dwEmployeeNo;
        //姓名
        public byte[] byName = new byte[20];
        //部门编号
        public short wDepartmentNo;
        //排班计划编号
        public short wSchedulePlanNo;
        //排班计划类型：0-无意义、1-个人、2-部门
        public byte bySchedulePlanType;

        public byte[] byRes1 = new byte[3];
        public byte[] byRes2 = new byte[119];
    }

    /**
     * 有效期参数结构体 sdk8.143   结构体NET_DVR_CARD_CFG中的结构体
     *
     * @author dell
     */
    public static class NET_DVR_VALID_PERIOD_CFG extends Structure {
        public byte byEnable;  //是否启用该有效期：0- 不启用，1- 启用
        public NET_DVR_TIME_EX struBeginTime;  //有效期起始时间
        public NET_DVR_TIME_EX struEndTime;    //有效期结束时间
        public byte[] byRes1 = new byte[3];    //保留，置为0
        public byte[] byRes2 = new byte[32];    //保留，置为0
    }

    /**
     * s时间
     */
    public static class NET_DVR_TIME_EX extends Structure {
        public short wYear;
        public byte byMonth;
        public byte byDay;
        public byte byHour;
        public byte byMinute;
        public byte bySecond;
        public byte byRes;
    }

    /****************************************结构体 end *******************************************************************************************************/

}














