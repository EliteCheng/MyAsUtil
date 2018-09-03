package elite.cheng.myasutil.util.crash;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.commons.lang.StringUtils;

import elite.cheng.myasutil.util.SQLLiteUtil;


public class CrashSQLite {

    private static final String TAG = "CrashSQLite";
    private static final String tableName = "crashTable";
    @SuppressLint("StaticFieldLeak")
    private static CrashSQLite instance = null;
    private Context mContext;

    private CrashSQLite(Context mContext) {
        this.mContext = mContext;
        createCrashTable();
    }

    public static CrashSQLite getInstance(Context mContext) {
        if (instance == null) {
            synchronized (CrashSQLite.class) {
                if (instance == null) {
                    instance = new CrashSQLite(mContext);
                }
            }
        }
        return instance;
    }


    /**
     * 创建保存崩溃信息的Table
     */
    public void createCrashTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (_id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , crashJson TEXT)";
        SQLiteDatabase db = SQLLiteUtil.getInstance(mContext);
        db.execSQL(sql);
        Log.i(TAG, "创建SQLight成功。。。。表：" + tableName);
    }

    /**
     * 存入SQLLight
     */
    public boolean saveDB(String json) {
        Log.d(TAG, "saveDB===" + json);
        if (StringUtils.isEmpty(json)) {
            return false;
        }
        SQLiteDatabase db = SQLLiteUtil.getInstance(mContext);
        //实例化一个ContentValues用来装载待插入的数据
        // cv.put("username","Jack Johnson");
        ContentValues cv = new ContentValues();
        cv.put("crashJson", json);
        long rowId = db.insert(tableName, null, cv);
        if (rowId == -1) {
            Log.e(TAG, "插入SQLight失败");
            return false;
        }
        Log.i(TAG, "插入SQLight成功==" + rowId);
        return true;
    }

    public CrashInfoResult getSavedCrashInfo() {
        CrashInfoResult result = new CrashInfoResult();
        try {
            SQLiteDatabase db = SQLLiteUtil.getInstance(mContext);
            //查询并获得游标
            Cursor c = db.query(tableName, null, null, null, null, null, null);
            int len = c.getCount();
            Log.i(TAG, "查询上次崩溃信息数目:" + len);
            String deletesql = "";
            if (len != 0) {
                while (c.moveToNext()) {
                    int id = c.getInt(c.getColumnIndex("_id"));
                    deletesql += id + ",";
                    System.out.println("sendPostFileData==" + id);
                    String crashJson = c.getString(c.getColumnIndex("crashJson"));
                    CrashInfoResult.CrashItem item = new Gson().fromJson(crashJson, CrashInfoResult.CrashItem.class);
                    result.getDataList().add(item);
                }
                deletesql = deletesql.substring(0, deletesql.length() - 1);
                db.execSQL("delete from crashTable where _id in (" + deletesql + ")");
            }
            result.setResult(true);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            result.setResult(false);
            return result;
        }
    }
}
