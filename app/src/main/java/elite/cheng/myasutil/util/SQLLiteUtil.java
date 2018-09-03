package elite.cheng.myasutil.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by TC on 2018/8/8.
 */
public class SQLLiteUtil {

    private static SQLiteDatabase instance = null;
    private static final String dbName = "bathProjectAS.db";

    private SQLLiteUtil(){throw new AssertionError();}

    public static SQLiteDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (SQLLiteUtil.class) {
                if (instance == null) {
                    instance = context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
                }
            }
        }
        return instance;
    }
}
