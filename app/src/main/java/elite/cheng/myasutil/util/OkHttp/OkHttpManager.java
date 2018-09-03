package elite.cheng.myasutil.util.OkHttp;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import elite.cheng.myasutil.constants.APPConstant;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;


/**
 * Created by Guan on 2016/8/31.
 * <p>
 * 本方法是基于 okhttp 3.6.0和  okio 1.12.0 进行的封装
 * <p>
 * 总原则:
 * 1.要完成不同的功能,其本质上就是构造不同的Request
 * 2.通过继承重写ResponseBody而实现了上传进度的监听
 * 3.通过获取Response的Stream而实现了下载进度的监听
 */
public class OkHttpManager {

    private static String TAG = "OkHttpManager";

    private OkHttpClient mOkHttpClient;
    private static HttpsUtils.SSLParams sslParams;


    //public static String FAILURE = "failure";

    /**
     * SUCCESS: 网络访问成功
     * FAILURE：网络畅通，但接口404、500等错误
     * NETWORK_FAILURE：由于网络原因引起的“无法访问”的错误
     */
    public enum State {
        SUCCESS, FAILURE, NETWORK_FAILURE
    }

    private static ResultCallback defaultResultCallback = new ResultCallback() {
        @Override
        public void onCallBack(State state, String result) {
            Log.e(TAG, "由于未定义ResultCallback参数,以下信息未被处理" +
                    "\tstate:" + state +
                    "\tresult:" + result);
        }
    };

    private String Network_Failure_Message = "Timeout：请检查网络连接。";


    /**
     * 单例模式：
     * "静态内部类的单例模式"只有当被调用的时候才开始首次被加载，利用此特性，可以实现懒汉式
     * 参考资料：
     * http://mp.weixin.qq.com/s?__biz=MjM5NzMyMjAwMA==&mid=2651477404&idx=2&sn=e7fff60ef79af8f69c33c535ff448b66&scene=1&srcid=0902Q8NkdRTN6z5s8WUwOcO5#wechat_redirect
     * http://www.cnblogs.com/lwbqqyumidi/p/3738059.html
     */
    private static class OkHttpManagerBuilder {
        private static OkHttpManager okHttpManager = new OkHttpManager();
    }

    private OkHttpManager() {
        sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        mOkHttpClient = new OkHttpClient().newBuilder()
                //10000毫秒
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //30秒
                .writeTimeout(30L, TimeUnit.SECONDS)
                //https
                .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                .hostnameVerifier(sslParams.unSafeHostnameVerifier)
                .build();
        Log.e(TAG, "调用了OkHttpManager一次");

    }

    private static OkHttpManager getInstance() {
        return OkHttpManagerBuilder.okHttpManager;
    }
    //==========================以上是单例模式,以下是对外暴露的API=========================


    /**
     * 上传：一次性指定多个文件，按顺序上传，不可修改文件名
     *
     * @param url
     * @param files
     * @param filekeys 对应服务前端的name,并不是文件名
     * @param params
     * @param listener 文件传输进度监听器
     * @param callback
     */
    public static void upload(String url, File[] files, String[] filekeys
            , HashMap<String, String> params
            , ProgressListener listener
            , ResultCallback callback) {

        if (callback == null) {
            callback = defaultResultCallback;
        }

        getInstance().Upload(url, files, filekeys, params, listener, callback);

    }


    /**
     * 下载：执行单一文件下载，可指定保存的文件名
     *
     * @param url
     * @param fileDir  目标文件夹
     * @param saveName 保存的文件名
     * @param listener
     * @param callback
     */
    public static void download(String url, String fileDir, String saveName
            , ProgressListener listener
            , ResultCallback callback) {

        if (callback == null) {
            callback = defaultResultCallback;
        }

        getInstance().Download(url, fileDir, saveName, listener, callback);

    }


    /**
     * 同步Get请求
     *
     * @param url
     * @param params
     */
    public static String CommonGetSync(String url, HashMap<String, String> params) {

        if (params == null) {
            params = new HashMap<String, String>();
        }

        return getInstance().GetSync(url, params);
    }


    /**
     * 异步Get请求
     *
     * @param url
     * @param params
     * @param callback
     */
    public static void CommonGetAsyn(String url, HashMap<String, String> params
            , ResultCallback callback) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        try {
            getInstance().GetAsyn(url, params, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 同步post请求
     *
     * @param url
     * @param params
     * @return 直接将String类型的服务器响应数据返回
     */
    public static String CommonPostSync(String url, HashMap<String, String> params) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        return getInstance().PostSync(url, params);
    }


    /**
     * 异步post请求
     *
     * @param url
     * @param params
     * @param callback 通过定义回调函数来接收服务器的返回数据
     */
    public static void CommonPostAsyn(String url, HashMap<String, String> params
            , ResultCallback callback) {
        if (params == null) {
            params = new HashMap<String, String>();
        }
        /**
         * 此处适配具体情况
         */
        params.put("token", APPConstant.ANDROID_ID);
        try {
            getInstance().PostAsyn(url, params, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 异步Post请求，以json格式发送数据
     *
     * @param url
     * @param json
     * @param callback
     */
    public static void CommonPostJson(String url, String json, ResultCallback callback) {
        getInstance().PostJson(url, json, callback);
    }


    //==================以下是实现功能的方法======================

    /**
     * 上传：一次性指定多个文件，按顺序上传
     *
     * @param url
     * @param files
     * @param filekeys 对应服务前端的name,并不是文件名
     * @param params
     * @param listener 文件传输进度监听器
     * @param callback 通过定义回调函数来接收服务器的返回数据
     */
    private void Upload(String url
            , File[] files
            , String[] filekeys
            , HashMap<String, String> params
            , ProgressListener listener
            , ResultCallback callback) {

        //1.由MultipartBody构造RequestBody
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);
        //2.添加参数
        for (String key : params.keySet()) {
            builder.addFormDataPart(key, params.get(key));
        }
        //3.按顺序添加文件
        for (int i = 0; i < files.length; i++) {
            //每一个file又需要自己构造一个RequestBody
            ProgressRequestBody fileRequestBody = new ProgressRequestBody(i
                    , MediaType.parse("application/octet-stream")
                    , files[i]
                    , listener);
            builder.addFormDataPart(filekeys[i], files[i].getName(), fileRequestBody);


        }
        //4.将总的RequestBody加入到Request中去
        RequestBody requestbody = builder.build();
        final Request request = new Request.Builder()
                .url(url)
                .post(requestbody)
                .build();
        //5.执行
        ExecuteAsyn_String(request, callback);

    }

    /**
     * 下载
     *
     * @param url
     * @param fileDir  目标文件夹
     * @param filename 保存的文件名
     * @param listener
     * @param callback 通过定义回调函数来接收服务器的返回数据
     */
    private void Download(String url, String fileDir, String filename, ProgressListener listener, ResultCallback callback) {

        Request request = new Request.Builder()
                .url(url)
                .build();
        //mOkHttpClient.newCall(request).enqueue();
        ExecuteAsyn_DownloadWithListener(request, fileDir, filename, listener, callback);

    }


    /**
     * 异步Get请求
     *
     * @param url
     * @param params
     * @param callback
     */
    private void GetAsyn(String url, HashMap<String, String> params, ResultCallback callback) {
        //i.e: http://www.it315.org/counter.jsp?name=zhangsan&password=123
        StringBuilder new_url = new StringBuilder(url);
        if (null != params) {
            new_url.append("?");
//            for (String key : params.keySet()) {
//                String value = params.get(key);
//                new_url.append(key + "=" + value + "&");
//            }

            for (Map.Entry entry : params.entrySet()) {
                new_url.append(entry.getKey() + "=" + entry.getValue() + "&");
            }
            //删除最后一个&符号
            new_url.deleteCharAt(new_url.length() - 1);
        }
        Log.e(TAG, "Get请求：" + new_url.toString());

        Request request = new Request.Builder()
                .url(new_url.toString())
                .build();

        ExecuteAsyn_String(request, callback);

    }


    /**
     * 同步Get请求
     *
     * @param url
     * @param params
     * @return
     */
    private String GetSync(String url, HashMap<String, String> params) {

        StringBuilder new_url = new StringBuilder(url);
        if (null != params) {
            new_url.append("?");
            for (String key : params.keySet()) {
                String value = params.get(key);
                new_url.append(key + "=" + value + "&");
            }
            new_url.deleteCharAt(new_url.length() - 1);
        }
        Log.e(TAG, "Get请求：" + new_url.toString());

        Request request = new Request.Builder()
                .url(new_url.toString())
                .build();

        try {
            //execute执行同步请求操作
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {

                Log.v(TAG, "the request was successfully received, understood, and accepted.");
                //Log.v(TAG, "Code is "+response.code() +":"+ response.body().toString());
                // 返回字符串,则调用response.body().string()
                // 返回的二进制字节数组，则调用response.body().bytes()
                // 返回的inputStream，则调用response.body().byteStream()

                return response.body().string();

            } else {
                Log.e(TAG, "not successful, Code is : " + response.code() + "");
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 异步post请求
     *
     * @param url
     * @param params   参数
     * @param callback 通过定义回调函数来接收服务器的返回数据
     */
    private void PostAsyn(String url, HashMap<String, String> params, ResultCallback callback) {
        //post的时候,参数是在请求体中
        //application/x-www-form-urlencoded :
        //<form encType=””>中默认的encType，form表单数据被编码为key/value格式发送到服务器（表单默认的提交数据的格式）

        // TODO 访问蒲公英网站，如果header不对，会怎么样。
        // content-type: application/x-www-form-urlencoded

        //1.以表单的形式,由FormBody构建RequestBody
        FormBody.Builder builder = new FormBody.Builder();

        for (Map.Entry entry : params.entrySet()) {
            builder.add((String) entry.getKey(), (String) entry.getValue());
        }

        //3.将RequestBody加入Request中
        RequestBody mRequestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(mRequestBody)//表明这里是Post请求
                .build();
        //4.异步请求
        ExecuteAsyn_String(request, callback);


    }


    /**
     * 同步post请求
     *
     * @param url
     * @param params
     * @return 直接将String类型的服务器响应数据返回
     */
    private String PostSync(String url, HashMap<String, String> params) {
        //结构与PostAsyn类似,只是最后执行的方式不同
        FormBody.Builder builder = new FormBody.Builder();
//        for (String key : params.keySet()) {
//            builder.add(key, params.get(key));
//        }
        for (Map.Entry entry : params.entrySet()) {
            builder.add((String) entry.getKey(), (String) entry.getValue());
        }
        RequestBody mRequestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(mRequestBody)
                .build();

        try {
            //execute执行同步请求操作
            Response response = mOkHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {

                Log.v(TAG, "the request was successfully received, understood, and accepted.");
                //Log.v(TAG, "Code is "+response.code() +":"+ response.body().toString());
                // 返回字符串,则调用response.body().string()
                // 返回的二进制字节数组，则调用response.body().bytes()
                // 返回的inputStream，则调用response.body().byteStream()

                return response.body().string();

            } else {
                Log.e(TAG, "not successful, Code is : " + response.code() + "");
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 异步Post请求，以json格式发送数据
     * <p>
     * 相关知识：https://imququ.com/post/four-ways-to-post-data-in-http.html
     *
     * @param url
     * @param json
     * @param callback
     */
    private void PostJson(String url, String json, ResultCallback callback) {
        try {
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            byte[] postData = new byte[0];
            if (json != null) {
                postData = json.getBytes("UTF-8");
            }
            RequestBody mRequestBody = RequestBody.create(JSON, postData);
            Request request = new Request.Builder()
                    .url(url)
                    .post(mRequestBody)
                    .build();

            ExecuteAsyn_String(request, callback);

        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    //=======================================================================

    /**
     * 开始执行异步请求，获得少量的String类型返回值。
     * 普通的异步post请求、上传文件时调用
     *
     * @param request
     * @param callback 通过定义回调函数来接收服务器的返回数据
     */
    private void ExecuteAsyn_String(Request request, final ResultCallback callback) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                callback.onCallBack(State.NETWORK_FAILURE, Network_Failure_Message);
                //Log.e(TAG, "OkHttp is on Failure:操作取消,文件读取失败或者连接超时");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.v(TAG, "the request was successfully received, understood, and accepted.");
                    String responseBody = response.body().string();
                    //回调,把response传递给调用该OKHttpUtil的线程
                    callback.onCallBack(State.SUCCESS, responseBody);
                } else {

                    Log.e(TAG, "OkHttp response is not successful. Code is: " + response.code());
                    callback.onCallBack(State.FAILURE, Integer.toString(response.code()));
                }
            }
        });
    }

    /**
     * 开始执行带进度监听的异步请求,或得返回的数据流。适用于接收大量的返回数据
     * 下载时调用
     *
     * @param request
     * @param fileDir
     * @param filename
     * @param listener
     */
    private void ExecuteAsyn_DownloadWithListener(Request request, final String fileDir
            , final String filename
            , final ProgressListener listener
            , final ResultCallback callback) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onCallBack(State.NETWORK_FAILURE, Network_Failure_Message);
                //Log.e(TAG, "OkHttp is on Failure:操作取消,文件读取失败或者连接超时");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {

                    Log.v(TAG, "the request was successfully received, understood, and accepted.");
                    InputStream is = null;
                    byte[] buf = new byte[2048];
                    int len = 0;//本次读取的字节数
                    long currSize = 0;//当前已经读取的字节数
                    //总大小
                    long totalSize = Integer.valueOf(response.header("content-Length", "-1"));

                    FileOutputStream fos = null;
                    try {
                        is = response.body().byteStream(); //获取返回的Stream
                        File dir = new File(fileDir);
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        File file = new File(fileDir, filename);

                        fos = new FileOutputStream(file);
                        while ((len = is.read(buf)) != -1) {
                            fos.write(buf, 0, len);
                            currSize += len;
                            //文件总长度从外部获得；
                            //因为是单文件下载，所以id永远为0，表示不起作用
                            listener.onProgress(totalSize, currSize, totalSize == currSize, 0);
                        }
                        fos.flush();
                        //Log.e(TAG, "file has finished downloading.");
                        callback.onCallBack(State.SUCCESS, "file has finished downloading.filename=" +
                                filename + "\tfileDir:" + fileDir);


                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (is != null)
                                is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            if (fos != null)
                                fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


                } else {
                    Log.e(TAG, "OkHttp response is not successful. Code is: " + response.code());
                    callback.onCallBack(State.FAILURE, Integer.toString(response.code()));

                }
            }
        });
    }


    //============================以下是实现上传监听与回调的类和接口==============

    /**
     * 继承RequestBody,实现上传的进度监听
     */
    private class ProgressRequestBody extends RequestBody {
        MediaType contentType;
        File file;
        ProgressListener listener;
        int id;

        /**
         * 构造函数
         *
         * @param id          一次可以上传多个文件,id表示本文件在这一批文件当中的编号
         * @param contentType MIME类型
         * @param file        要上传的文件
         * @param listener    传输进度监听器
         */
        public ProgressRequestBody(int id, MediaType contentType, File file, ProgressListener listener) {
            this.id = id;
            this.contentType = contentType;
            this.file = file;
            this.listener = listener;
        }


        @Override
        public MediaType contentType() {
            return contentType;
        }

        @Override
        public long contentLength() throws IOException {
            return file.length();
        }

        @Override
        public void writeTo(BufferedSink sink) throws IOException {
            Source source;
            long len;//记录本次读了多少数据
            long currSize = 0;//记录目前一共读了多少数据
            long totalSize = contentLength();//一共有多少数据

            try {
                source = Okio.source(file);
                Buffer buffer = new Buffer();

                while ((len = source.read(buffer, 2048)) != -1) {
                    sink.write(buffer, len);
                    sink.flush();
                    currSize += len;

                    //回调,进度监听
                    listener.onProgress(totalSize, currSize, totalSize == currSize, id);
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 传输进度监听器
     */

    public interface ProgressListener {
        /**
         * @param totalSize 总大小
         * @param currSize  当前大小
         * @param done      是否完成
         * @param id        序号
         */
        void onProgress(long totalSize, long currSize, boolean done, int id);
    }

    /**
     * 回调
     */
    public interface ResultCallback {
        /**
         * @param state  状态
         * @param result 返回结果
         */
        void onCallBack(State state, String result);
    }


}
