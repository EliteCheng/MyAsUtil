package elite.cheng.myasutil.util;

import java.io.IOException;

/**
 * 修改文件权限工具类。
 * Created by TC on 2018/1/29.
 */
public class ChmodUtil {
    private ChmodUtil(){}
    public static void chmod( String permission, String fullPath) {
        try {
            String cmd = "chmod " + permission + " " + fullPath;
            Runtime runtime=Runtime.getRuntime();
            runtime.exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
