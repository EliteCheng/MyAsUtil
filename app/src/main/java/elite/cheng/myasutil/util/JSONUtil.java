package elite.cheng.myasutil.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TC
 * @time 2017/12/20
 * @desc ${TODD}
 */
public class JSONUtil {

    //不允许实例化工具类，因为实例化后没有意义
    private JSONUtil() {
        throw new AssertionError();
    }

    /**
     * 对象转JSONObject
     */
    public static JSONObject toJSONObject(Object object) {
        if (object instanceof JSONObject) {
            return (JSONObject) object;
        }
        return JSONObject.parseObject(JSONObject.toJSONString(object));
    }

    /**
     * 对象转JSON字符串
     */
    public static String toJSONString(Object object) {
        return JSONObject.toJSONString(object);
    }

    /**
     * 从JSONObject中获取ArrayList
     *
     * @param jsonArr  需要获取的Json字符串
     * @param classOfT ArrayList的元素类型
     */
    public static <T> List<T> getArrayList(String jsonArr, Class<T> classOfT) {
        List<T> list = new ArrayList<>();
        JSONArray jsonArray = JSONArray.parseArray(jsonArr);
        for (Object object : jsonArray) {
            list.add(new Gson().fromJson(toJSONString(object), classOfT));
        }
        return list;
    }

    /**
     * JSONObject转JavaBean
     */
    public static <T> T toJavaBean(JSONObject jsonObject, Class<T> classOfT) {
        return toJavaBean(jsonObject.toJSONString(), classOfT);
    }

    /**
     * JSON字符串转JavaBean
     */
    public static <T> T toJavaBean(String jsonString, Class<T> classOfT) {
        return new Gson().fromJson(jsonString, classOfT);
    }


}
