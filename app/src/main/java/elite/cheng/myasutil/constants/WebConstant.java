package elite.cheng.myasutil.constants;

import org.apache.commons.lang.StringUtils;

/**
 * Created by TC on 2017/10/23.
 */

public class WebConstant {
    //    public static String WEBHOST = "http://121.199.23.184:8082/bathProject";
    public static String WEBHOST = "";
    public static String MQTT_BROKER = "";

    //    public static String WEB_SOCKET = "ws://121.199.23.184:8082/bathProject/websocket";
    public static String WEB_SOCKET = "";


    //登录接口
    public static String LOGIN = WEBHOST + "/appLogin";//1
    public static String LOGOUT = WEBHOST + "/appLogout";//1
    public static String SINGLE_LOGIN = WEBHOST + "/singleLogin";//2

    public static String GET_USE_TIME = WEBHOST + "/statsRunRecord";//运行记录统计分析
    public static String SEND_CMD = WEBHOST + "/sendCmd";//1、发送开关命令
    public static String SET_SYSTEM_CLOCK = WEBHOST + "/systemClock";//2、发送校准系统时间命令
    public static String SET_THRESHOLD = WEBHOST + "/threshold";//3、发送设定探测器阈值命令
    public static String SET_TIMER = WEBHOST + "/timer";//3、发送设置定时器命令
    public static String GET_CURRENT_STATER = WEBHOST + "/requestState";//4.请求整体设备状态的命令
    public static String INTO_OR_EXIT_REMOTE_CTRL = WEBHOST + "/remoteControl";//6、开关远程控制状态
    public static String GET_TOILET_INFO_FROM_LOCATION = WEBHOST + "/toilet/findToiletsByLocation";//7、通过地址获取厕所信息
    public static String GET_TOILET_INFO_FROM_REGEX = WEBHOST + "/toilet/findToiletsByRegEx";//8、通过地址模糊查询获取厕所信息
    public static String LOCK_TOILET = WEBHOST + "/lock/lockToilet";//9、锁定厕所
    public static String RELEASE_TOILET = WEBHOST + "/lock/releaseToilet";//10、释放厕所
    public static String CHECK_TOILET = WEBHOST + "/lock/checkLock";//11、查询厕所是否已被锁

    public static String SET_DEFAULT_ALL = WEBHOST + "/restoreDefault";//一键恢复默认设置
    //设置水表校正值
    public static String SET_WATER_ADJUSTED_VALUE = WEBHOST + "/info/setWaterAdjustedValue";
    //读取水表校正值
    public static String GET_WATER_ADJUSTED_VALUE = WEBHOST + "/info/findWaterAdjustedValue";

    //获取参数阈值记录
    public static String GET_PARAMS = WEBHOST + "/requestParams";

    //检查更新的地址
    public static String APP_CHECK_UPDATE = WEBHOST + "/resources/app/androidApp.json";

    public static String GET_USER_INFO = WEBHOST + "/user/findUser";

    public static String GET_USER_INFO_BY_NICKNAME = WEBHOST + "/user/findUserByNickname";

    public static String UPDATE_USER_PWD = WEBHOST + "/user/updateUserPassword";

    //通过toiletId获取一组照片的url
    public static String GET_IMGS_URLS_BY_TOILETID = WEBHOST + "/file/getFilesByToiletId";
    //下载图片
    public static String DOWNLOAD_IMG_BY_FILEID = WEBHOST + "/file/download";
    //上传图片
    public static String UPLOAD_IMG_TOILET = WEBHOST + "/file/uploadPicUnderPermission";
    //删除图片
    public static String DELETE_IMG_TOILET = WEBHOST + "/file/deletePicUnderPermission";


    //请求蹲位pit数据
    public static String REQUEST_PIT = WEBHOST + "/cmd/requestPit";

    /**
     * 接口全部更新
     */
    public static void update() {

        LOGIN = WEBHOST + "/appLogin";
        LOGOUT = WEBHOST + "/appLogout";
        SINGLE_LOGIN = WEBHOST + "/singleLogin";

        GET_USE_TIME = WEBHOST + "/stats/statsRunRecord";//运行记录统计分析
        SEND_CMD = WEBHOST + "/cmd/sendCmd";//1、发送开关命令
        SET_SYSTEM_CLOCK = WEBHOST + "/cmd/systemClock";//2、发送校准系统时间命令
        SET_THRESHOLD = WEBHOST + "/cmd/threshold";//3、发送设定探测器阈值命令
        SET_TIMER = WEBHOST + "/cmd/timer";//3、发送设置定时器命令
        GET_CURRENT_STATER = WEBHOST + "/cmd/requestState";//4.请求整体设备状态的命令
        INTO_OR_EXIT_REMOTE_CTRL = WEBHOST + "/cmd/remoteControl";//6、开关远程控制状态
        GET_TOILET_INFO_FROM_LOCATION = WEBHOST + "/toilet/findToiletsByLocation";//7、通过地址获取厕所信息
        GET_TOILET_INFO_FROM_REGEX = WEBHOST + "/toilet/findToiletsByRegEx";//8、通过地址模糊查询获取厕所信息
        LOCK_TOILET = WEBHOST + "/lock/lockToilet";//9、锁定厕所
        RELEASE_TOILET = WEBHOST + "/lock/releaseToilet";//10、释放厕所
        CHECK_TOILET = WEBHOST + "/lock/checkLock";//11、查询厕所是否已被锁

        SET_DEFAULT_ALL = WEBHOST + "/cmd/restoreDefault";//一键恢复默认设置
        //设置水表校正值
        SET_WATER_ADJUSTED_VALUE = WEBHOST + "/info/setWaterAdjustedValue";
        GET_WATER_ADJUSTED_VALUE = WEBHOST + "/info/findWaterAdjustedValue";

        GET_PARAMS = WEBHOST + "/cmd/requestParams";
        APP_CHECK_UPDATE = WEBHOST + "/resources/app/androidApp.json";

        GET_USER_INFO = WEBHOST + "/user/findUser";
        GET_USER_INFO_BY_NICKNAME = WEBHOST + "/user/findUserByNickname";
        UPDATE_USER_PWD = WEBHOST + "/user/updateUserPassword";

        GET_IMGS_URLS_BY_TOILETID = WEBHOST + "/file/getFilesByToiletId";
        DOWNLOAD_IMG_BY_FILEID = WEBHOST + "/file/download";
        UPLOAD_IMG_TOILET = WEBHOST + "/file/uploadPicUnderPermission";
        DELETE_IMG_TOILET = WEBHOST + "/file/deletePicUnderPermission";

        REQUEST_PIT = WEBHOST + "/cmd/requestPit";


    }


    /**
     * 判断是否已经指定WebHost
     *
     * @return
     */
    public static boolean isHostAvailable() {
        return StringUtils.isNotEmpty(WebConstant.WEBHOST)
                && StringUtils.isNotEmpty(WebConstant.WEB_SOCKET)
                && StringUtils.isNotEmpty(WebConstant.MQTT_BROKER);
    }


    /**
     * 记录所有服务器的地址
     */
    public static final class ServerSet {

        public static final String testWebHost = "http://125.216.242.147:8080/bathProject";
        public static final String testWebSocket = "ws://125.216.242.147:8080/bathProject/websocket";
        //        public static final String testMqttBroker = "tcp://139.199.180.158:1883";
        public static final String testMqttBroker = "tcp://47.97.211.221:61613";

        public static final String aliWebHost = "http://121.199.23.184:8082/bathProject";
        public static final String aliWebSocket = "ws://121.199.23.184:8082/bathProject/websocket";
        //        public static final String aliMqttBroker = "tcp://139.199.180.158:1883";
        public static final String aliMqttBroker = "tcp://47.97.211.221:61613";

//        public static final String tencentWebHost = "http://139.199.180.158:8080/claudyServer";
//        public static final String tencentWebSocket = "ws://139.199.180.158:8080/claudyServer/websocket";
//        public static final String tencentMqttBroker = "tcp://139.199.180.158:1883";

        public static final String claudyWebHost = "http://120.79.53.205:80/bathProject";
        public static final String claudyWebSocket = "ws://120.79.53.205:80/bathProject/websocket";
        public static final String claudyMqttBroker = "tcp://120.79.53.205:1883";

//        public static final String chengbinWebHost = "http://125.216.242.147:8080/bathProject";
//        public static final String chengbinWebSocket = "ws://125.216.242.147:8080/bathProject/websocket";
//        public static final String chengbinMqttBroker = "tcp://139.199.180.158:1883";


    }
}


