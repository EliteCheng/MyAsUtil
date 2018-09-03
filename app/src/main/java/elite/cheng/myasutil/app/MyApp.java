package elite.cheng.myasutil.app;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;

import java.util.List;

import elite.cheng.myasutil.constants.APPConstant;
import elite.cheng.myasutil.util.DisplayUtil;
import elite.cheng.myasutil.util.crash.MyCrashHandler;


public class MyApp extends Application {

    private static Context appContext;
    private static MyApp mInstance;
    //主线程处理handler
    private static Handler mHandler;


    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        mInstance = this;
        mHandler = new Handler();
        init();
    }

    private void init() {
        //设置一些APPConstant常量
//        String usage = SharedPreferencesUtil.getString(this, "usage");
//        APPConstant.USAG = StringUtils.isEmpty(usage) ? "点击设置" : usage;
        //设置屏幕的宽度和高度
        DisplayUtil.init(getBaseContext());
        //获取AndroidId，用于标识设备
//        APPConstant.ANDROID_ID = Settings.Secure.getString(MyApp.getAppContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        APPConstant.ANDROID_ID = Build.SERIAL;
        Thread.setDefaultUncaughtExceptionHandler(new MyCrashHandler(getAppContext()));
    }

    /**
     * 获取当前APPlication的实例
     */
    public static MyApp getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return appContext;
    }

    /**
     * 获取版本号
     */
    public static int getVersionCode() {
        PackageInfo packageInfo = null;
        try {
            PackageManager pm = appContext.getPackageManager();
            packageInfo = pm.getPackageInfo(appContext.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取版本名
     */
    public static String getVersionName() {
        try {
            PackageInfo pi = appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "unknown version name";
        }
    }

    /**
     * 判断app是否处于前台
     */
    public boolean isAppForeground() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfoList == null) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo processInfo : runningAppProcessInfoList) {
            if (processInfo.processName.equals(getPackageName()) &&
                    processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 注销的操作
     */
    public static void Logout() {
        //此处表明已经收到 单点登录退出app的遗言，需要发送新的 空消息 删除该遗言
    }
}
