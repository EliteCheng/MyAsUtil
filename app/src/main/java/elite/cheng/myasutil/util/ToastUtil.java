package elite.cheng.myasutil.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by TC on 2018/1/29
 */
public class ToastUtil {
    private ToastUtil(){}
    public static void showShortToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String content) {
        Toast.makeText(context, content, Toast.LENGTH_LONG).show();
    }
}
