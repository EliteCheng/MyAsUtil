package elite.cheng.myasutil.util.crash;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import elite.cheng.myasutil.constants.APPConstant;
import elite.cheng.myasutil.util.OkHttp.OkHttpManager;


public class MyCrashHandler implements Thread.UncaughtExceptionHandler {

    //采集AppCrash信息
    private static final String COLLECT_CRASH = "http://125.216.242.147:8080/bathProject" + "/debug/collectCrash";

    private Thread.UncaughtExceptionHandler originalHandler;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Context mContext;
    private CrashSQLite crashSQLite;

    private static final String TAG = "MyCrashHandler";

    public MyCrashHandler(Context context) {
        originalHandler = Thread.getDefaultUncaughtExceptionHandler();
        mContext = context;
        crashSQLite = CrashSQLite.getInstance(mContext);
        reportLatestCrashInfo();
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        System.out.println("catch it----------------------------------");
        handleException(throwable);
        if (originalHandler != null) {
            originalHandler.uncaughtException(thread, throwable);
        }
    }

    /**
     * 发生异常时做具体的处理
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        try {
            CrashInfoResult.CrashItem item = new CrashInfoResult.CrashItem();
            collectDeviceInfo(mContext, item);
            getDetailExceptionInfo(ex, item);
            getExceptionInfo(ex, item);
            crashSQLite.saveDB(item.toJson());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 异常堆栈信息
     */
    private void getDetailExceptionInfo(Throwable ex, CrashInfoResult.CrashItem item) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        item.setStackInfo(writer.toString());
        Log.i(TAG, "收集堆栈信息");
    }

    /**
     * 采集设备信息
     */
    private void collectDeviceInfo(Context context, CrashInfoResult.CrashItem item) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";

                item.setVersionName(versionName);
                item.setVersionCode(versionCode);
            }
            CrashInfoResult.DeviceInfo deviceInfo = new CrashInfoResult.DeviceInfo();
            deviceInfo.setBrand(Build.BRAND);
            deviceInfo.setDisplay(Build.DISPLAY);
            deviceInfo.setModel(Build.MODEL);
            deviceInfo.setSDKInt(Build.VERSION.SDK_INT);
            deviceInfo.setVersionRelease(Build.VERSION.RELEASE);
            item.setDeviceInfo(deviceInfo);
            Log.i(TAG, "收集设备信息");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 采集Exception其余信息
     */
    private void getExceptionInfo(Throwable ex, CrashInfoResult.CrashItem item) {
        item.setAndroidId(APPConstant.ANDROID_ID);
        item.setTime(format.format(new Date()));
        item.setTimestamp(System.currentTimeMillis());
        Log.i(TAG, "收集Exception信息");
    }

    /**
     * 上报
     */
    private void reportLatestCrashInfo() {
        Log.i(TAG, "开始上报上次记录的崩溃数据");
        CrashInfoResult crashInfoResult = crashSQLite.getSavedCrashInfo();
        if (crashInfoResult != null && crashInfoResult.getDataList() != null && crashInfoResult.getDataList().size() > 0) {
            OkHttpManager.CommonPostJson(COLLECT_CRASH, crashInfoResult.toJson(), new OkHttpManager.ResultCallback() {
                @Override
                public void onCallBack(OkHttpManager.State state, String result) {
                    if (state == OkHttpManager.State.SUCCESS) {
                        Log.i(TAG, "上报成功");
                    }
                }
            });
        }
    }
}
