package shenzhen.teamway.deviceproxy;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;

public interface TeamWayInterface extends Library{
	
//	TeamWayInterface INSTANCE = (TeamWayInterface) Native.loadLibrary("hcnetsdk",TeamWayInterface.class);//加载库文件
	TeamWayInterface INSTANCE = (TeamWayInterface) Native.loadLibrary("F:\\CH-HCNetSDKV6.0.2.35_build20190411_Win64\\CH-HCNetSDKV6.0.2.35_build20190411_Win64\\库文件\\HCNetSDK.dll", TeamWayInterface.class);//加载库文件
	
	
	public static final int NET_DVR_DEV_ADDRESS_MAX_LEN = 129;
	public static final int NET_DVR_LOGIN_USERNAME_MAX_LEN = 64;
	public static final int NET_DVR_LOGIN_PASSWD_MAX_LEN = 64;
	public static final int SERIALNO_LEN = 48;   //序列号长度
	/** 最大门数量 */
	public static final int MAX_DOOR_NUM = 255;
	
	/********************************************* 接口 *******************************************************************************************************/
	//sdk初始化
	 boolean  NET_DVR_Init();
	//获取错误值
	 int NET_DVR_GetLastError();
	//启用日志文件写入接口
	 boolean  NET_DVR_SetLogToFile(boolean bLogEnable , String  strLogDir, boolean bAutoDel );
	 /** 获取当前门闸状态 获取设备配置信息sdk6.6.3
		 * @param lUserID
		 * @param dwCommand  设备配置命令，详见表 6.6
		 * @param lChannel  通道号，如果命令不需要通道号，该参数无效，置为 0xFFFFFFFF即可
		 * @param lpOutBuffer [out]  接收数据的缓冲指针，详见表 6.6
		 * @param dwOutBufferSize [out]  接收数据的缓冲长度(以字节为单位)，不能为 0
		 * @param lpBytesReturned [out]  实际收到的数据长度指针，不能为 NULL
		 * @return boolean 
		 */
	 boolean  NET_DVR_GetDVRConfig(NativeLong lUserID, int dwCommand,NativeLong lChannel, Pointer lpOutBuffer, int dwOutBufferSize, IntByReference lpBytesReturned);///获取设备配置信息
	//注销用户
	 boolean  NET_DVR_Logout(NativeLong lUserID);
	//释放sdk资源
	 boolean  NET_DVR_Cleanup();
	//对于同步登录，接口返回-1 表示登录失败，其他值表示返回的用户 ID 值
	 NativeLong NET_DVR_Login_V40(Pointer pLoginInfo,Pointer lpDeviceInfo);
	 
	 /**
	  * 门禁控制 sdk6.8.1
	  * @param lGatewayIndex 门禁序号，从 1 开始，-1 表示对所有门进行操作
	  * @param dwStaic 命令值：0- 关闭，1- 打开，2- 常开，3- 常关
	  * @author dell
	  */
	 boolean NET_DVR_ControlGateway(NativeLong lUserID,long lGatewayIndex, int dwStaic);
	 
	 /**
	  * 启动长链接配置sdk6.6.17
	  * @param dwCommand 配置命令，详见表 6.18
	  * @param lpInBuffer 详见sdk6.18表 根据dwCommand不同 对应不同的结构体
	  * @param dwInBufferLen 输入缓冲大小
	  * @param cbStateCallback 卡号下发的回调函数 sdk3.5>sdk8.29
	  * @param pUserData 用户数据
	  * @return 返回值： -1 表示失败，其他值作为 NET_DVR_SendRemoteConfig 等接口的句柄
	  * @author dell
	  */
//	 NativeLong NET_DVR_StartRemoteConfig(NativeLong lUserID, int dwCommand, Pointer lpInBuffer, int dwInBufferLen, NET_DVR_CARD_CFG_V50 cbStateCallback, Pointer pUserData);
	 
	 /**
	  * 发送长链接数据sdk6.6.18
	  * @param lHandle  长连接句柄，NET_DVR_StartRemoteConfig 的返回值
	  * @param dwDataType  数据类型 详见表 6.9
	  * @param pSendBuf  详见表 6.9	保存发送数据的缓冲区，与 dwDataType 有关   长连接中的命令不同 dwDataType与pSendBuf也会不同
	  * @param dwBufSize发送数据的长度
	  * @return
	  * @author dell
	  */
//	 boolean NET_DVR_SendRemoteConfig(NativeLong lHandle, int dwDataType, String pSendBuf, int dwBufSize);
	 
	 /****************************************接口 end *******************************************************************************************************/
	 
	 /**
	     * 回调函数----获取卡号sdk6.6.17  sdk3.5
	     * @author dell
	     */
//	    public static interface FRemoteConfigCallback extends Callback{
//	    	public void invoke(int dwType, Pointer lpBuffer, int dwBufLen, Pointer pUserData);
//	    	
//	    }	 
	 
	 /**
	     * 异步登录回调函数----获取ID
	     * @author dell
	     */
	    public static interface FLoginResultCallBack extends Callback{
	    	public int invoke(NativeLong lUserID,int dwResult,Pointer lpDeviceinfo,Pointer pUser);
	    }
	 
	 
	 
	 
	 /***************************************** 结构体 *******************************************************************************************************/
	 
	 public static class NET_DVR_USER_LOGIN_INFO  extends Structure {
			public  byte[] sDeviceAddress = new byte[NET_DVR_DEV_ADDRESS_MAX_LEN];
			public  byte byUseTransport;
			public  short wPort;
//			public  FLoginResultCallBack cbLoginResult;
			public  byte[] sUserName = new byte[NET_DVR_LOGIN_USERNAME_MAX_LEN];
			public  byte[] sPassword = new byte[NET_DVR_LOGIN_PASSWD_MAX_LEN];
			public  FLoginResultCallBack cbLoginResult;//回调函数
			Pointer pUser;
			public int bUseAsynLogin;///bUseAsynLogin=0,同步登陆方式， =1异步登陆方式
		  	public  byte[] byRes2 = new byte[128];
		}
	 
	 public static class NET_DVR_DEVICEINFO_V40 extends Structure {
	 		public NET_DVR_DEVICEINFO_V30 struDeviceV30  = new NET_DVR_DEVICEINFO_V30 ();//设备参数
	 		public byte bySupportLock;///设备是否支持锁定功能，bySupportLock 为 1 时，dwSurplusLockTime 和 byRetryLoginTime
	 		public byte byRetryLoginTime;///剩余可尝试登陆的次数，用户名、密码错误时，此参数有效
	 		public byte byPasswordLevel;///密码安全等级：0- 无效，1- 默认密码，2- 有效密码，3- 风险较高的密码
	 		public byte byRes1;
	 		public int dwSurplusLockTime;///剩余锁定时间
	 		public byte[] byRes2 = new byte[256];
	 }
	 
	//NET_DVR_Login_V30()参数结构
	 public static class NET_DVR_DEVICEINFO_V30 extends Structure
	 {
	    public  byte[] sSerialNumber = new byte[SERIALNO_LEN];  //序列号
	    public  byte byAlarmInPortNum;		        //报警输入个数
	    public  byte byAlarmOutPortNum;		        //报警输出个数
	    public  byte byDiskNum;				    //硬盘个数
	    public  byte byDVRType;				    //设备类型, 1:DVR 2:ATM DVR 3:DVS ......
	    public  byte byChanNum;				    //模拟通道个数
	    public  byte byStartChan;			        //起始通道号,例如DVS-1,DVR - 1
	    public  byte byAudioChanNum;                //语音通道数
	    public  byte byIPChanNum;					//最大数字通道个数
	    public  byte[] byRes1 = new byte[24];					//保留
	 }
	 
	 /**
	  * sdk6.6.3 > sdk8.8 dwCommand=2127时   lpOutBuffer【out】对应的结构体  简化
	  * @author dell
	  */
	 public static class NET_DVR_ACS_WORK_STATUS extends Structure {
		 /** 结构体大小*/
		 public int dwSize;
		 /** 门锁状态 门锁状态：0- 关，1- 开*/
		 byte []byDoorLockStatus = new byte[MAX_DOOR_NUM];
		 /** 门状态：0- 闭合，1- 开启*/
		 byte []byDoorStatus = new byte[MAX_DOOR_NUM];	
		 /** 门磁状态：0- 闭合，1- 开启*/
		 byte []byMagneticStatus = new byte[MAX_DOOR_NUM];
		 
		 
	 }
	 
	 /**
	  * sdk6.6.17 > sdk8.30 dwCommand=2116时   lpInBuffer对应的结构体  简化
	  * @author dell
	  */
	 public static class NET_DVR_CARD_CFG_COND extends Structure {
		 /** 结构体大小*/
		 public int dwSize;
		 /** 卡号*/
		 public int dwCardNum;
		 /** 设备是否进行卡号校验：0- 不校验，1- 校验*/
		 public byte byCheckCardNo;
		 /**就地控制器序号，表示往就地控制器下发离线卡参数，0 代表是门禁主机*/
		 short wLocalControllerID;
		 /**保留，置为零*/
		 byte byRes[] = new byte[26];
	 }
	 
	 
	 /****************************************结构体 end *******************************************************************************************************/
}














