package com.marksixinfo.base;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.marksixinfo.R;
import com.marksixinfo.bean.HashMapEntity;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.LogUtils;
import com.marksixinfo.utils.ToastShow;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 隐式意图跳转工具类
 *
 * @Auther: Administrator
 * @Date: 2019/4/4 0004 20:52
 * @Description:
 */
public class IntentUtils {
    private static final String TAG = "IntentUtils";

    public static Intent getIntent(Context context, String activityName, HashMap params) {
        if (context != null) {
            Intent in = new Intent();
            in.setAction(context.getString(R.string.action));
            in.addCategory(context.getString(R.string.category));
            if (TextUtils.isEmpty(activityName)) {
                return null;
            }
            Uri uri;
            if (activityName.startsWith(getScheme(context) + "://")) {
//                activityName = activityName.replace("#", "%23").replace("!", "%21");
                uri = Uri.parse(addParams(new StringBuffer(activityName), params).toString());

            } else {
                uri = getUri(getUrl(getScheme(context), activityName, params));
            }

            if (uri == null) {
                return null;
            }
            in.setData(uri);
            return in;
        }
        return null;
    }

    public static Intent getIntent(Context context, Uri uri) {
        if (context != null) {
            Intent in = new Intent();
            in.setAction(context.getString(R.string.action));
            in.addCategory(context.getString(R.string.category));
            if (uri == null) {
                return null;
            }
            in.setData(uri);
            return in;
        }
        return null;
    }

    /**
     * 跳转默认浏览器
     *
     * @param context
     * @param url     需要打开的网址
     */
    public static void gotoDefaultWeb(ActivityIntentInterface context, String url) {
        if (context != null && context.getActivity() != null) {
            try {
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.getActivity().startActivity(intent);
            }catch (ActivityNotFoundException exception){
              //  context.getActivity():
                ToastShow.toastShow(context.getContext(),"请安装浏览器");
            }
        }
    }


    /**
     * 获取对应的 Activity信息，多个返回第一个
     *
     * @param context
     * @param in
     * @return
     */
    public static Bundle getActivityInfo(Context context, Intent in) {
        if (context != null && in != null) {
            try {
                PackageManager pm = context.getPackageManager();
                List<ResolveInfo> resolveInfos = pm
                        .queryIntentActivities(in, PackageManager.GET_META_DATA);
                if (resolveInfos != null && resolveInfos.size() > 0) {
                    return resolveInfos.get(0).activityInfo.metaData;
                } else {
                    LogUtils.d(TAG, "找不到host对应Activity");
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 当前版本是否有此页面
     *
     * @param context
     * @param host    页面host
     * @return
     */
    public static boolean hasThisHost(Context context, String host) {
        return getActivityInfo(context, getIntent(context, host, null)) != null;
    }

    public static Uri getUri(String url) {
        try {
            return Uri.parse(url);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public static String getUrl(String scheme, String activityName, HashMap<String, String> params) {
        StringBuffer sb = new StringBuffer();
        if (!TextUtils.isEmpty(scheme) && !TextUtils.isEmpty(activityName)) {
            sb.append(scheme).append("://").
                    append(activityName);
            addParams(sb, params);
        }

        return sb.toString();
    }

    public static StringBuffer addParams(StringBuffer sb, HashMap<String, String> params) {
        if (sb == null) {
            return new StringBuffer();
        }
        if (params != null && params.size() > 0) {
            for (HashMap.Entry<String, String> entry : params.entrySet()) {
                if (!TextUtils.isEmpty(entry.getKey())) {
                    sb.append("&").append(entry.getKey()).append("=");
                    if (!TextUtils.isEmpty(entry.getValue())) {
                        String value = entry.getValue();
                        try {
                            value = URLEncoder.encode(value, "utf-8");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        sb.append(value);
                    }

                }

            }
            while (sb.indexOf("?") > 0) {
                int index = sb.indexOf("?");
                sb.replace(index, index + 1, "&");
            }
            int index = sb.indexOf("&");
            sb.replace(index, index + 1, "?");

        }
        return sb;
    }

    public static String getScheme(Context context) {
        if (context != null) {
            return context.getString(R.string.scheme);
        }
        return null;
    }

    public static HashMap<String, String> getUrlParam(Activity context) {

        if (context != null && context.getIntent() != null) {
            HashMap params = new HashMap();
            Uri uri = context.getIntent().getData();
            if (uri != null && uri.getQueryParameterNames() != null) {
                for (String key : uri.getQueryParameterNames()) {
                    String value = String.valueOf(uri.getQueryParameter(key));
                    try {
                        value = URLDecoder.decode(value, "utf-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    params.put(key, value);
                }
            }
            return params;
        }
        return null;
    }

    /**
     * 获取页面的传进来的bundle数据
     *
     * @param context
     * @return
     */
    public static Bundle getBundleParams(Activity context) {
        if (context != null && context.getIntent() != null) {
            return getBundleParams(context, context.getIntent());
        }
        return null;
    }

    /**
     * 获取Intent中的bundle数据
     *
     * @param context
     * @param intent
     * @return
     */
    public static Bundle getBundleParams(Context context, Intent intent) {

        if (context != null && intent != null) {
            return intent.getBundleExtra(context.getString(R.string.jsonBundle));
        }
        return null;
    }

    public static HashMap<String, String> getUrlParam(String url) {

        if (CommonUtils.StringNotNull(url)) {
            HashMap params = new HashMap();
//            url = url.replace("#", "%23").replace("!", "%21");//针对链接做特殊处理

            Uri uri = getUri(url);
            if (uri != null && uri.getQueryParameterNames() != null) {
                for (String key : uri.getQueryParameterNames()) {
                    String value = String.valueOf(uri.getQueryParameter(key));
                    try {
                        value = URLDecoder.decode(value, "utf-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    params.put(key, value);
                }
            }
            return params;
        }
        return null;
    }

    /**
     * 获取一个保存了Bundle对象的Intent用于setResult 回传bundle对象
     */
    public static Intent getResultBundleIntent(Context context, Bundle bundle) {
        Intent in = new Intent();
        in.putExtra(context.getString(R.string.jsonBundle), bundle);
        return in;
    }

    /**
     * 传入参数为{"key","value"}
     */

    public static HashMap<String, String> getHashObj(String[] params) {
        if (params.length % 2 != 0) {
            LogUtils.d(TAG, "请输入正确的键值对个数");
            return null;
        }
        ArrayList<HashMapEntity> entityList = new ArrayList<>();
        for (int i = 0; i < params.length / 2; i++) {
            HashMapEntity item = new HashMapEntity(params[i * 2],
                    params[i * 2 + 1]);
            entityList.add(item);
        }
        HashMap<String, String> pp = new HashMap<>();
        for (HashMapEntity item : entityList) {
            pp.put(item.getKey(), item.getValue());
        }
        return pp;
    }

    /**
     * 判断应用是否安装
     *
     * @param context
     * @return
     */
    public static boolean isHaveInstallApp(Context context, String packName) {
        if (TextUtils.isEmpty(packName))
            return false;
        PackageManager packageManager = context.getPackageManager();

        List<PackageInfo> list = packageManager
                .getInstalledPackages(PackageManager.GET_PERMISSIONS);
        if (list == null) {
            return false;
        }
        for (int i = 0; i < list.size(); i++) {
            if (packName.equals(list.get(i).packageName)) {
                return true;
            }
        }

        return false;
    }
}

