package elite.cheng.myasutil.util;

import java.util.Calendar;

/**
 * 获取系统日期的工具类
 * Created by TC on 2018/1/29.
 */

public class CalendarUtil {
    private static Calendar calendar = Calendar.getInstance();

    public static int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public static int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int getDayOfMonth() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getHour() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinute() {
        return calendar.get(Calendar.MINUTE);
    }

    public static int getSecond() {
        return calendar.get(Calendar.SECOND);
    }

}
