package elite.cheng.myasutil.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

/**
 * 运行时权限申请
 * Created by TC on 2018/1/29.
 */
public class PermisssionUtil {

    /**
     * 检测单个权限是否授权
     * @return true 已授权 false 未授权
     */
    public static boolean checkPermission(Context mContext, String permission) {
        if (ContextCompat.checkSelfPermission(mContext, permission) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    /**
     * 检测多个权限是否授权
     * @param permissions 数组模式
     * @return 未授权的权限字符串
     */
    public static List<String> checkMultiPermission(Context mContext, String[] permissions) {
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (!checkPermission(mContext, permission)) {
                permissionList.add(permission);
            }
        }
        return permissionList;
    }

    /**
     * 检测多个权限是否授权
     * @param permissions 列表模式
     * @return 未授权的权限字符串
     */
    public static List<String> checkMultiPermission(Context mContext, List<String> permissions) {
        String[] perArr = permissions.toArray(new String[permissions.size()]);
        return checkMultiPermission(mContext, perArr);
    }

    /**
     * 请求单个权限
     * @param permission  单个权限
     * @param requestCode 请求码
     */
    public static void requestPermission(Context mContext, String permission, int requestCode) {
        requestPermission(mContext, new String[]{permission}, requestCode);
    }

    /**
     * 请求多个权限
     * @param permissions 权限数组
     * @param requestCode 请求码
     */
    public static void requestPermission(Context mContext, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions((Activity) mContext, permissions, requestCode);
    }

    /**
     * 请求多个权限
     * @param permissions 权限列表
     * @param requestCode 请求码
     */
    public static void requestPermission(Context mContext, List<String> permissions, int requestCode) {
        requestPermission(mContext, permissions.toArray(new String[permissions.size()]), requestCode);
    }

    /**
     * 判断用户过去是否禁止了该权限
     * @return 如果用户彻底禁止了该权限，返回true;如果用户只是拒绝了该权限，那么返回false
     */
    public static boolean judgeRefusePermission(Context mContext, String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext, permission)) {
            return false;
        }
        return true;
    }

    /**
     * 检测并请求多个权限
     */
    public static void checkAndRequestPermission(Context mContext, String[] permissions, int requestCode) {
        List<String> perList = checkMultiPermission(mContext, permissions);
        if (!perList.isEmpty()) {
            requestPermission(mContext, perList, requestCode);
        }
    }

    /**
     * 检测并请求单个权限
     */
    public static void checkAndRequestPermission(Context mContext, String permission, int requestCode) {
        checkAndRequestPermission(mContext, new String[]{permission}, requestCode);
    }

    /**
     * 检测并请求多个权限,并且在全部权限都允许之后，调用回调函数
     */
    public static void checkAndRequestPermission(Context mContext, String[] permissions, int requestCode, RequestPermissionSeccessCallBack callBack) {
        List<String> perList = checkMultiPermission(mContext, permissions);
        if (!perList.isEmpty()) {
            requestPermission(mContext, perList, requestCode);
            //权限全部被允许后
            if (checkMultiPermission(mContext, perList).isEmpty()) {
                callBack.onSeccess();
            }
        }
    }
    public static void onRequestPermissionsResult(Context mContext, String[] permissions, int[] grantResults, CheckPermissionsCallBack callBack) {
        List<String> permitList = new ArrayList<>();
        List<String> deniedList = new ArrayList<>();
        //记录是否有权限被彻底禁止
        boolean hasBannedPermission = false;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                permitList.add(permissions[i]);
            } else {
                deniedList.add(permissions[i]);
            }
        }
        //如果有被禁止的权限，判断其中有无完全被禁止的
        if (!deniedList.isEmpty()) {
            for (String denied : deniedList) {
                if (judgeRefusePermission(mContext, denied)) {
                    hasBannedPermission = true;
                    break;
                }
            }
            callBack.onDeniedPermission();
        } else {
            callBack.onHasPermission();
        }
        if (hasBannedPermission) {
            callBack.onDeniedAndRefusedPermission();
        }
    }
    /**
     * 跳转到设置界面
     */
    public static void toAppSetting(Context mContext) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", mContext.getPackageName());
        }
        mContext.startActivity(intent);
    }
    /**
     * 检查权限时的回调接口
     */
    public interface CheckPermissionsCallBack {
        /**
         * 申请权限成功后执行
         */
        void onHasPermission();
        /**
         * 未授权但并没有彻底禁止
         */
        void onDeniedPermission();
        /**
         * 未授权并且已经彻底禁止
         */
        void onDeniedAndRefusedPermission();
    }

    public interface RequestPermissionSeccessCallBack {
        void onSeccess();
    }
}
