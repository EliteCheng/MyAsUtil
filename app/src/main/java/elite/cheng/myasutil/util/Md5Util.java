package elite.cheng.myasutil.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 使用消息摘要算法对传入的key进行加密并返回。
 * create by TC 2018/1/29
 */
public class Md5Util {

    private static MessageDigest mDigest = null;
    //静态代码区，用于对初始化时需要抛异常的全局变量进行初始化
    static {
        try {
            mDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    /**
     * 对String key进行MD5加密，如果无MD5加密算法，则直接使用key对应的hash值。
     */
    public static String toMD5(String key) {
        StringBuilder cacheKey=new StringBuilder();
        //获取MD5算法失败时，直接使用key对应的hash值
        if (mDigest == null) {
            return String.valueOf(key.hashCode());
        }
        //将key的数据存储到mDigest中,作为信息摘要
        mDigest.update(key.getBytes());
        //取出信息摘要并转换成16进制的字符串
        for (byte b:mDigest.digest() ) {
            // '02'表示不足两位前面补0输出,'X'表示以十六进制形式输出
            cacheKey.append(String.format("%02X",b));
        }
        return cacheKey.toString();
    }
}
