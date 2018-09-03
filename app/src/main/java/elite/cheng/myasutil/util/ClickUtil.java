package elite.cheng.myasutil.util;

import android.app.Activity;
import android.view.MotionEvent;

public class ClickUtil {
    private static long lastClickTime;


    /**
     * 禁止实例化本工具类，因为实例化本工具类没有任何意义
     */
    private ClickUtil() {
        throw new AssertionError();
    }

    /**
     * 判断点击频率过快，即是否双击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 触发屏幕点击事件
     *
     * @param x 点击位置的x值
     * @param y 点击位置的y值
     */
    public static void setMouseClick(Activity activity, int x, int y) {
        MotionEvent evenDown = MotionEvent.obtain(System.currentTimeMillis(),
                System.currentTimeMillis() + 100, MotionEvent.ACTION_DOWN, x, y, 0);
        activity.dispatchTouchEvent(evenDown);
        MotionEvent eventUp = MotionEvent.obtain(System.currentTimeMillis(),
                System.currentTimeMillis() + 100, MotionEvent.ACTION_UP, x, y, 0);
        activity.dispatchTouchEvent(eventUp);
        evenDown.recycle();
        eventUp.recycle();
    }

}
