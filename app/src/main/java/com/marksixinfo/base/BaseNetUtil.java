package com.marksixinfo.base;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.marksixinfo.bean.ClientInfo;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.HTTPSCerUtils;
import com.marksixinfo.utils.LogUtils;
import com.marksixinfo.utils.XDns;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLPeerUnverifiedException;
import javax.net.ssl.SSLSession;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 20:00
 * @Description:
 */
public class BaseNetUtil {

    public static final String OK_HTTP_LOG_TAG = "marksix_okhttp";
    private static final int TIME_OUT_MILLIS = 15;//超时时间,秒

    private BaseNetUtil() {
    }

    public static void init() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HTTPSCerUtils.setTrustAllCertificate(builder);
        OkHttpClient okHttpClient = builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                try {
                    LogUtils.d(OK_HTTP_LOG_TAG, "hostname=" + hostname + "session=" +
                            session.getPeerHost() + "PeerCertificates" + session.getPeerCertificates().toString());
                } catch (SSLPeerUnverifiedException e) {
                    e.printStackTrace();
                }
                return true;
            }
        })
                .addInterceptor(new LoggerInterceptor(OK_HTTP_LOG_TAG))
                .connectTimeout(TIME_OUT_MILLIS, TimeUnit.SECONDS)
                .readTimeout(TIME_OUT_MILLIS, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT_MILLIS, TimeUnit.SECONDS)
                .dns(new XDns(TIME_OUT_MILLIS))
                .retryOnConnectionFailure(false).
                        cookieJar(new CookieJar() {
                            private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

                            //Tip：這裡key必須是String
                            @Override
                            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                                cookieStore.put(url.host(), cookies);
                            }

                            @Override
                            public List<Cookie> loadForRequest(HttpUrl url) {
                                List<Cookie> cookies = cookieStore.get(url.host());
                                return cookies != null ? cookies : new ArrayList<Cookie>();
                            }
                        })
                .build();
        okHttpClient.dispatcher().setMaxRequestsPerHost(10);
        okHttpClient.dispatcher().setMaxRequests(30);
        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 通用的异步get请求
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 回调接口
     */
    public static void get(boolean isAddHeadParams, String url, Map<String, String> params, final Callback callback) {
        if (isAddHeadParams) {
            url = addHeadParams(url);
        }
        GetBuilder builder = OkHttpUtils.get().url(url);
        if (callback != null && callback instanceof CallbackBase && CommonUtils.StringNotNull(((CallbackBase) callback).getCancelTag())) {

            builder = builder.tag(((CallbackBase) callback).getCancelTag());
        }

        if (params != null) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (CommonUtils.StringNotNull(key, value)) {
                    builder.addParams(key, value);
                }
            }
        }
        builder.build().execute(callback);
    }

    /**
     * 通用的异步get请求
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 回调接口
     */
    public static void get(String url, Map<String, String> params, final Callback callback) {
        get(true, url, params, callback);
    }

    /**
     * post 数组
     *
     * @param url
     * @param params
     * @param arrayKey
     * @param array
     * @param callback
     */
    public static void postArray(String url, Map<String, String> params, String arrayKey,
                                 List<String> array, final ArrayCallback callback) {
        if (callback.isNeedDialog()) {
            callback.onBefore();
        }

        url = BaseNetUtil.addHeadParams(url);

        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (CommonUtils.StringNotNull(key, value)) {
                    builder.add(key, value);
                }
            }
        }

        if (CommonUtils.ListNotNull(array)) {
            for (String s : array) {
                builder.add(arrayKey, s);
            }
        }
        //构建请求
        OkHttpClient client = OkHttpUtils.getInstance().getOkHttpClient();
        okhttp3.RequestBody requestBody = builder.build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }


    /**
     * 通用的异步post请求
     *
     * @param isAddHeadParams 是否添加请求头参数
     * @param url             请求地址
     * @param params          请求参数
     * @param callback        回调接口
     */
    public static void post(boolean isAddHeadParams, String url, Map<String, String> params, final Callback callback) {
        if (CommonUtils.StringNotNull(url)) {
            if (isAddHeadParams) {
                url = addHeadParams(url);
            }
            PostFormBuilder builder = OkHttpUtils.post().url(url);
            boolean isAllowNull = false;
            if (callback != null && callback instanceof CallbackBase && CommonUtils.StringNotNull(((CallbackBase) callback).getCancelTag())) {

                builder = builder.tag(((CallbackBase) callback).getCancelTag());
                isAllowNull = ((CallbackBase) callback).isAllowNullParams();
            }
            if (params != null) {
                Set<Map.Entry<String, String>> entries = params.entrySet();
                for (Map.Entry<String, String> entry : entries) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    if (isAllowNull) {
                        if (CommonUtils.StringNotNull(key)) {
                            builder.addParams(key, value);
                        }

                    } else {
                        if (CommonUtils.StringNotNull(key, value)) {
                            builder.addParams(key, value);
                        }
                    }


                }
            }
            builder.build().execute(callback);
        }
    }

    /**
     * 通用的异步post请求
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 回调接口
     */
    public static void post(String url, Map<String, String> params, final Callback callback) {
        post(true, url, params, callback);
    }

    /**
     * 功能描述:    添加head参数
     *
     * @auther: Administrator
     * @date: 2019/3/19 0019 下午 9:55
     */
    public static String addHeadParams(String url) {
        return url + getParamsByMap(generateCommonParams());
    }


    /**
     * 拼接参数
     *
     * @param map
     * @return
     */
    public static String getParamsByMap(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        if (CommonUtils.MapNotNull(map)) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry != null) {
                    String key = entry.getKey();
                    String value = entry.getValue();
                    sb.append("&").append(key).append("=").append(value);
                }
            }
        }
        return sb.toString();
    }

    /**
     * 通用的异步post请求
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 回调接口
     */
    public static void postUpLoadFile(String url, Map<String, String> params, LinkedHashMap<String, String> fileMap, final Callback callback) {
        url = addHeadParams(url);
        PostFormBuilder builder = OkHttpUtils.post().url(url);
        if (callback != null && callback instanceof CallbackBase && CommonUtils.StringNotNull(((CallbackBase) callback).getCancelTag())) {

            builder = builder.tag(((CallbackBase) callback).getCancelTag());
        }
        String tag = "files";
        if (fileMap != null) {
            Set<Map.Entry<String, String>> entries = fileMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String value = entry.getValue();
                if (CommonUtils.StringNotNull(value)) {
                    builder.addFile(tag, value, new File(value));
                }
            }
        }
        if (params != null) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (CommonUtils.StringNotNull(key, value)) {
                    builder.addParams(key, value);
                }
            }
        }

        builder.build().execute(callback);
    }


    /**
     * 通用的异步post请求
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 回调接口
     */
    public static void postUpLoadSingleFile(String url, Map<String, String> params, String filePath, final Callback callback) {
        url = addHeadParams(url);
        PostFormBuilder builder = OkHttpUtils.post().url(url).params(generateCommonParams());
        String tag = "file";
        if (!TextUtils.isEmpty(filePath)) {
            builder.addFile(tag, filePath, new File(filePath));
        }
        if (params != null) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (CommonUtils.StringNotNull(key, value)) {
                    builder.addParams(key, value);
                }
            }
        }
        builder.build().execute(callback);
    }


    /**
     * 通用的异步get请求(支持tag)
     *
     * @param tag      tag
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 回调接口
     */
    public static void get(Object tag, String url, Map<String, String> params, final Callback callback) {
        url = addHeadParams(url);
        GetBuilder builder = OkHttpUtils.get().tag(tag).url(url);
        if (params != null) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (CommonUtils.StringNotNull(key, value)) {
                    builder.addParams(key, value);
                }
            }
        }
        builder.build().execute(callback);
    }

    /**
     * 通用的异步post请求(支持tag)
     *
     * @param tag      tag
     * @param url      请求地址
     * @param params   请求参数
     * @param callback 回调接口
     */
    public static void post(Object tag, String url, Map<String, String> params, final Callback callback) {
        url = addHeadParams(url);
        PostFormBuilder builder = OkHttpUtils.post().tag(tag).url(url);
        if (params != null) {
            Set<Map.Entry<String, String>> entries = params.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                if (CommonUtils.StringNotNull(key, value)) {
                    builder.addParams(key, value);
                }
            }
        }
        builder.build().execute(callback);
    }


    /**
     * 取消正在请求
     *
     * @param tag tag
     */
    public static void cancel(Object tag) {
        OkHttpUtils.getInstance().cancelTag(tag);
    }

    /**
     * 生成通用的请求参数
     *
     * @return 参数map
     */
    public static Map<String, String> generateCommonParams() {
        HashMap map = new HashMap();
        map.putAll(ClientInfo.getHeadMap());
        return map;
    }

    public static <T> T parseFromJson(String json, Type type) {
        try {
            return !TextUtils.isEmpty(json) && type != null ? (T) new Gson().fromJson(json, type) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 自动填参，加入空判断
     *
     * @param map
     * @param key
     * @param value
     */
    public static void putStringParams(HashMap map, String key, String value) {
        if (map == null) {
            map = new HashMap<>();
        }
        if (CommonUtils.StringNotNull(key, value)) {
            map.put(key, value);
        }
    }
}

