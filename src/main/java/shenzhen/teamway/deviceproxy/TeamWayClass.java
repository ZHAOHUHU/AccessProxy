package shenzhen.teamway.deviceproxy;

import java.util.logging.Logger;

import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;


public class TeamWayClass {
	private static Logger log = Logger.getAnonymousLogger();
	static TeamWayInterface hCNetSDK = TeamWayInterface.INSTANCE; 
	
	/**sdk6.6.3 参数dwCommand=2127 含义：获取门禁主机工作状态 */
	public static final int NET_DVR_GET_ACS_WORK_STATUS = 2127;
	/**启动长连接  sdk6.6.17 参数dwCommand=2116 含义:获取卡参数 */
	public static final int NET_DVR_GET_CARD_CFG = 2116;
	
	static NativeLong lHandlee;//启动长链接的反回值
	
	
	/**
	 * 初始化
	 * @return
	 */
	public static void init() {
		hCNetSDK.NET_DVR_SetLogToFile(true, null, false);// 启用日志文件写入接口
		boolean b = hCNetSDK.NET_DVR_Init();// 初始化
		if (b) {
			log.info("初始化成功");
		} else {
			int numbeFalse = hCNetSDK.NET_DVR_GetLastError();
			log.info("初始化失败，错误代码：" + numbeFalse);
		}
	}
	
	/**
	 * 登录
	 * @return NativeLong id
	 */
	private static NativeLong LoginDevice(String ip,String user,String password, int port) {
		TeamWayInterface.NET_DVR_USER_LOGIN_INFO struLoginInfo = new TeamWayInterface.NET_DVR_USER_LOGIN_INFO();//登录信息结构体
		TeamWayInterface.NET_DVR_DEVICEINFO_V40  struDeviceInfo = new TeamWayInterface.NET_DVR_DEVICEINFO_V40();//设备信息结构体
		
		struLoginInfo.sDeviceAddress = ip.getBytes();
		struLoginInfo.sUserName = user.getBytes();
		struLoginInfo.sPassword = password.getBytes();
		struLoginInfo.wPort = (short)port;
		struLoginInfo.bUseAsynLogin=0;//0同登录  1异登录
		
		NativeLong id = hCNetSDK.NET_DVR_Login_V40(struLoginInfo.getPointer(), struDeviceInfo.getPointer());// getPointer()返回对象指针
		if ("-1".equals(String.valueOf(id).trim())) {
			int numbeFalse = hCNetSDK.NET_DVR_GetLastError();
			log.info("登录失败,错误代码：" + numbeFalse);
		} else {
			log.info("登录成功"+id);
		}
	return id;
	}
	
	/**
	 * 获取当前门闸状态 获取设备配置信息sdk6.6.3
	 * @param lUserID
	 * @param dwCommand  设备配置命令，详见表 6.6
	 * @param lChannel  通道号，如果命令不需要通道号，该参数无效，置为 0xFFFFFFFF即可
	 * @param lpOutBuffer [out]  接收数据的缓冲指针，详见表 6.6
	 * @param dwOutBufferSize [out]  接收数据的缓冲长度(以字节为单位)，不能为 0
	 * @param lpBytesReturned [out]  实际收到的数据长度指针，不能为 NULL
	 * @return String
	 */
	public static String GetDVRConfig(NativeLong lUserID, int dwCommand, NativeLong lChannel, Pointer lpOutBuffer, int dwOutBufferSize, IntByReference lpBytesReturned) {
		
		TeamWayInterface.NET_DVR_ACS_WORK_STATUS ndaw = new TeamWayInterface.NET_DVR_ACS_WORK_STATUS();//dwCommand=2127时, lpOutBuffer对应接构体
		
		boolean bool = hCNetSDK.NET_DVR_GetDVRConfig(lUserID, dwCommand, lChannel,lpOutBuffer, dwOutBufferSize, lpBytesReturned);
		String byDoorStatus = "";
		if (bool) {
			for (int i = 0; i < ndaw.byDoorStatus.length; i++) {
				byDoorStatus = byDoorStatus + ndaw.byDoorStatus[i] + ",";
			}
		} else {
			int numbeFalse = hCNetSDK.NET_DVR_GetLastError();
			log.info("门状态获取失败,错误代码：" + numbeFalse);
		}
		return byDoorStatus;
	}
	
	/**
	 * 启动长链接sdk6.6.17
	 * @return -1 表示失败，其他值作为 NET_DVR_SendRemoteConfig 等接口的句柄
	 */
//	public static NativeLong startRemoteConfig(NativeLong userId, int dwCommand, Pointer lpInBuffer, int dwInBufferLen, NET_DVR_CARD_CFG_V50 cbStateCallback, Pointer pUserData) {                   
//
//		lHandlee = hCNetSDK.NET_DVR_StartRemoteConfig(userId, dwCommand, lpInBuffer, dwInBufferLen, cbStateCallback, pUserData);// 启动长连接
//		if ("-1".equals(String.valueOf(lHandlee).trim())) {
//			int numbeFalse = hCNetSDK.NET_DVR_GetLastError();
//			log.info("长连接启动失败,错误代码：" + numbeFalse);
//		} else {
//			log.info("长连接启动成功"+lHandlee);
//		}
//		return lHandlee;
//	}
	
	/**
	 * 长链接回调函数----获取卡号
	 * @return -1 表示失败，其他值作为 NET_DVR_SendRemoteConfig 等接口的句柄
	 */
//	public static class FRemoteCallback implements FRemoteConfigCallback {
//		NET_DVR_CARD_CFG_V50 ndc = new NET_DVR_CARD_CFG_V50();//卡号等信息结构体
//		private String cardID = "";
//		private int dwType;
//		private Pointer lpBuffer;
//		private int dwBufLen;
//		private Pointer pUserData;
//		
//		public FRemoteCallback(int dwType, Pointer lpBuffer, int dwBufLen, Pointer pUserData){
//					this.dwType=dwType;
//					this.lpBuffer=lpBuffer;
//					this.dwBufLen=dwBufLen;
//					this.pUserData=pUserData;
//		}
//		@Override
//		public void invoke(int dwType, Pointer lpBuffer, int dwBufLen, Pointer pUserData) {// 取 lpBuffer中的卡号 在NET_DVR_CARD_CFG_V50结构体中
//			dwType = this.dwType;
//			lpBuffer = this.lpBuffer;
//			dwBufLen = this.dwBufLen;
//			pUserData = this.pUserData;
//			for (int j = 0; j < ndc.byCardNo.length; j++) {
//				cardID = cardID + ndc.byCardNo[j];
//			}
//		}
//	}
	
	
	/**
	 * 门禁控制 sdk6.8.1
	 * @param lGatewayIndex  门禁序号，从 1 开始，-1 表示对所有门进行操作
	 * @param dwStaic  命令值：0- 关闭，1- 打开，2- 常开，3- 常关
	 */
	public void ControlGateway(NativeLong userId, long lGatewayIndex, int dwStaic) {
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
	
	
	public static void main(String[] args) {
		
		// 初始化
		init();
		//登录
		NativeLong id = LoginDevice("192.168.12.39","admin","TY26811438",8000);
		//获取门状态
		TeamWayInterface.NET_DVR_ACS_WORK_STATUS ndaw = new TeamWayInterface.NET_DVR_ACS_WORK_STATUS();//dwCommand=2127时, lpOutBuffer对应接构体
		IntByReference intByReference = new IntByReference();
		NativeLong lChannel = new NativeLong(0xFFFFFFFF);
		String doorStatus = GetDVRConfig(id, NET_DVR_GET_ACS_WORK_STATUS, lChannel, ndaw.getPointer(), 200, intByReference);
		System.out.println("门状态为"+doorStatus);
		
		//启动长连接
		TeamWayInterface.NET_DVR_CARD_CFG_COND ndc = new TeamWayInterface.NET_DVR_CARD_CFG_COND();//dwCommand=2116时   lpInBuffer对应的结构体  简化
		TeamWayInterface.NET_DVR_USER_LOGIN_INFO struLoginInfo = new TeamWayInterface.NET_DVR_USER_LOGIN_INFO();//登录信息结构体
		/** 错误码:17  参数错误。SDK 接口中给入的输入或输出参数为空。*/
//		startRemoteConfig(lChannel, NET_DVR_GET_CARD_CFG, ndc.getPointer(), 50, null, struLoginInfo.getPointer());
		
		//注销
		LogOut(id);
		//释放sdk资源
		Cleanup();

	}

}
