package elite.cheng.myasutil.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.lang.reflect.Field;

import elite.cheng.myasutil.constants.APPConstant;

/**
 * Created by guan on 5/19/17.
 */

public class DisplayUtil {

    private static int SCREENWIDTH;
    private static int SCREENHEIGHT;
    private static int DensityDpi;
    private static int StatusBarHeight;

    //但例模式：饿汉式
    private static DisplayUtil displayUtil = new DisplayUtil();

    private Context mContext;


    private static String TAG = "DisplayUtil";

    public static int getScreenWidth() {
        return SCREENWIDTH;
    }

    public static int getScreenHeight() {
        return SCREENHEIGHT;
    }

    public static int getDensityDpi() {
        return DensityDpi;
    }

    public static int getStatusBarHeight() {
        return StatusBarHeight;
    }


    //=================================================
    public static void init(Context mContext) {
        displayUtil.saveScreenInfo(mContext);
        displayUtil.saveStatusBarHeight(mContext);
    }

    private DisplayUtil() {
    }


    /**
     * 获取并保存屏幕大小
     *
     * @param mContext
     */
    public void saveScreenInfo(Context mContext) {

        if (SCREENWIDTH != 0 && SCREENHEIGHT != 0) {
            return;
        }

        //通过Application获取屏幕信息
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(metric);
        //通过Activity获取屏幕信息
        //((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(metric);
        SCREENWIDTH = metric.widthPixels;     // 屏幕宽度（像素）
        SCREENHEIGHT = metric.heightPixels;   // 屏幕高度（像素）
        DensityDpi = metric.densityDpi;

        APPConstant.SCREEN_WIDTH = SCREENWIDTH;
        APPConstant.SCREEN_HEIGHT = SCREENHEIGHT;

        Log.d(TAG, "SCREENWIDTH:\t" + SCREENWIDTH +
                "\tSCREENHEIGHT:\t" + SCREENHEIGHT +
                "\tDensityDpi:\t" + DensityDpi);
    }

    /**
     * 用于获取状态栏的高度。
     *
     * @return 返回状态栏高度的像素值。
     */
    private int saveStatusBarHeight(Context mContext) {
        if (StatusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                StatusBarHeight = mContext.getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return StatusBarHeight;
    }
}
