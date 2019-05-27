package com.marksixinfo.bean;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.utils.LogUtils;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.utils.UIUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * by Zane, 2016/8/19.
 * 对外提供Core Module的一些接口
 * sy_ui	用户IP
 * sy_um	用户mac地址
 * sy_mc	设备号
 * sy_lo	经度
 * sy_la	纬度
 * sy_ov	操作系统版本
 * sy_mm	手机型号
 * sy_nw	网络模式WIFI/4G
 * sy_an	APP名称
 * sy_av	APP版本
 * sy_hv	APP补丁版本
 * sy_ba	浏览器 user-agent
 * sy_gs	终端全局标识
 * sy_oi	H5微信入口openId
 */
public class ClientInfo {


    private static HashMap<String, String> headMap = new HashMap();
    private static final String CHARSET_NAME = "UTF-8";
    private static final String INVALID_ANDROID_ID = "9774d56d682e549c";
    public static final String INTERFACE_VERSION = "1.0.0"; //version
    public static final double  VERSION_NUMS = 2.01; //更新时的版本比对qian



    /**
     * 屏幕密度
     */
    public static float scale = 1.5f;

    /**
     * 是否有网络
     */
    public static boolean isConnect = true;

    /**
     * 渠道
     */
    public static String SOURCE = "";
    /**
     * 版本号
     */
    public static int VERSION_NUM = 0;
    /**
     * 外部版本
     */
    public static String VERSION = "";
    /**
     * 热修复补丁版本(打补丁包修改)
     */
    public static String HOTFIX_VERSSION = "";

    /**
     * 客服端设备ID
     */
    public static String DEVICE_ID = "";
    /**
     * IP地址
     */
    public static String WAP_IP = "";

    /**
     * 当前手机分辨率
     */
    public static String SCREEN = "0x0";


    /**
     * 版本信息
     */
    public static String SYSTEM_INFO = "android";

    /**
     * 接入点是否是使用wap方式
     */
    public static boolean ISWAP = false;
    /**
     * 网络方式
     */
    public static String NET_TYPE = "";

    /**
     * 机器型号
     */
    public static String MODEL = "0000";

    /**
     * 经度
     */
    public static String LAT = "";
    /**
     * 纬度
     */
    public static String LONG = "";

    /**
     * Ip
     */
    public static String MAC_IP = "";
    /**
     * 站点Id
     */
    public static String PROVINCEID = "";
    /**
     * 是否发布版
     */
    public static boolean IS_RELEASE = true;


    /**
     * 基础参数 用户id
     */
    public static String USER_ID;


    /**
     * 基础参数 token
     */
    public static String KEY;

    public static LinkedHashMap<Integer, LotteryNumberConfigData> lotteryConfig = new LinkedHashMap<>();

    private ClientInfo() {
    }

    static {
//        String sdkRelease = Build.VERSION.RELEASE;
//        SYSTEM_INFO = SYSTEM_INFO + sdkRelease;
        MODEL = Build.MODEL;
    }

    public static void init(Context context) {
        //更新屏幕密度
        UIUtils.dip2px(context, 0);
        if (TextUtils.isEmpty(DEVICE_ID)) {
            DEVICE_ID = obtainDeviceId(context);
        }
        if (TextUtils.isEmpty(VERSION)) {
            getVersionCode(context);
        }
        initHeadParams(context);
//        if (!CommonUtils.StringNotNull(SOURCE)) {
//           updataSource(context);
//        }
//
//        if (TextUtils.isEmpty(VERSION)) {
//            getVersionCode(context);
//        }
//        if (TextUtils.isEmpty(PROVINCEID)) {
//            try {
//                PROVINCEID = SPUtil.getProvinceId(context);//分站省份id
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        updataNetType(context);
//        if (CommonUtils.StringNotNull(DEVICE_ID)) {
//            headMap.put(NumberConstants.DEVICEID, DEVICE_ID);
//        } else {
//            headMap.put(NumberConstants.DEVICEID, "123");
//        }
//        BaseNetUtil.putStringParams(headMap,NumberConstants.SY_SY,NumberConstants.SYSTEM_ANDROID);
//        BaseNetUtil.putStringParams(headMap,NumberConstants.SY_IV,INTERFACE_VERSION);
//        BaseNetUtil.putStringParams(headMap,NetUrlHeadline.SY_UM,MAC_IP);
//        BaseNetUtil.putStringParams(headMap,NetUrlHeadline.SY_MC,DEVICE_ID);
//        BaseNetUtil.putStringParams(headMap,NetUrlHeadline.SY_OV,SYSTEM_INFO);
//        BaseNetUtil.putStringParams(headMap,NetUrlHeadline.SY_DN,MODEL);
//        BaseNetUtil.putStringParams(headMap,NetUrlHeadline.SY_MM,MODEL);
//        BaseNetUtil.putStringParams(headMap,NetUrlHeadline.SY_AV,VERSION);
//        BaseNetUtil.putStringParams(headMap,NetUrlHeadline.SY_HV,HOTFIX_VERSSION);
//        headMap.put(NumberConstants.SY_CV, VERSION + "." + VERSION_NUM);


    }

    public static void initHeadParams(Context context) {
        USER_ID = SPUtil.getUserId(context);
        KEY = SPUtil.getToken(context);
        DEVICE_ID = SPUtil.getDeviceId(context);
    }


    private static String obtainDeviceId(Context context) {
        String deviceId = SPUtil.getDeviceId(context);
        if (!TextUtils.isEmpty(deviceId)) {
            return deviceId;
        }
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            //step1:正常获取
            if (!TextUtils.equals(INVALID_ANDROID_ID, androidId)) {
                deviceId = UUID.nameUUIDFromBytes(androidId.getBytes(CHARSET_NAME)).toString();
            } else {
                //step2:如果是非法值，再通过TelephonyManager获取
                @SuppressLint("MissingPermission") String id = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).
                        getDeviceId();
                if (!TextUtils.isEmpty(id)) {
                    deviceId = UUID.nameUUIDFromBytes(deviceId.getBytes(CHARSET_NAME)).toString();
                } else {
                    //step3:如果还是未获取到，则通过UUID生成一个
                    deviceId = UUID.randomUUID().toString();
                }
            }
        } catch (Exception e) {
            LogUtils.e("ClientInfo.getDeviceId() error");
        }
//        if (!CommonUtils.StringNotNull(deviceId)) {
//            deviceId = UTDevice.getUtdid(context);
//        }
        SPUtil.setDeviceId(context, deviceId);
        return deviceId;
    }

    public static HashMap<String, String> getHeadMap() {
        updata();
        return headMap;
    }

    public static int getVersionCode(Context context) {
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            VERSION = packInfo.versionName;
            IS_RELEASE = !VERSION.contains("test");
            VERSION_NUM = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return VERSION_NUM;
    }

    public static void updata() {
        headMap.put(StringConstants.USER_ID, USER_ID);
        headMap.put(StringConstants.KEY, KEY);
        headMap.put(StringConstants.DEVICE, SYSTEM_INFO);
        headMap.put(StringConstants.DEVICE_ID, DEVICE_ID);
//        BaseNetUtil.putStringParams(headMap,NetUrlHeadline.SY_NW,NET_TYPE);
//        BaseNetUtil.putStringParams(headMap,NetUrlHeadline.SY_UI,WAP_IP);
//        if (CommonUtils.StringNotNull(PROVINCEID)) {
//            headMap.put(NumberConstants.SY_PR, PROVINCEID);
//        } else {
//            headMap.put(NumberConstants.SY_PR, "");//为空要传空字符  //分站省id
//        }
    }

//    public static void updataNetType(Context context) {
//        obtainMACIP(context);
//        NET_TYPE = GetNetworkType(context);
//        ISWAP = !"WIFI".equals(NET_TYPE);
//        WAP_IP = getHostIP();
//        isConnect= NetWorkUtils.isNetworkConnected(context);
//        BaseNetUtil.putStringParams(headMap,NetUrlHeadline.SY_NW,NET_TYPE);
//        BaseNetUtil.putStringParams(headMap,NetUrlHeadline.SY_UI,WAP_IP);
//    }

//    /**
//     * 获取版本号
//     *
//     * @return
//     */
//    public static String getVersionName(Context context) {
//        // 获取包管理者
//        PackageManager pm = context.getPackageManager();
//        try {
//            // 获取包信息
//            PackageInfo packageInfo = pm.getPackageInfo(
//                    context.getPackageName(), 0);
//            // 获取versionName
//            return packageInfo.versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return null;
//
//    }

//    /**
//     * 获取渠道
//     *
//     * @return
//     */
//    public static void updataSource(Context context) {
//        if(context==null)
//        {
//            return;
//        }
//        try {
//            SOURCE = WalleChannelReader.getChannel(context.getApplicationContext());
//            if(!CommonUtils.StringNotNull(SOURCE))
//            {
//                SOURCE = AnalyticsConfig.getChannel(context);
//            }
//            BaseNetUtil.putStringParams(headMap,NetUrlHeadline.MO_NA,SOURCE);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    public static String getDeviceId() {
        return DEVICE_ID;
    }

//    private static void obtainMACIP(Context context) {
//        try {
//            //获取wifi服务
//            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
////            if (!wifi.isWifiEnabled()) {
////                wifi.setWifiEnabled(true);
////            }
//            MAC_IP = wifi.getConnectionInfo().getMacAddress();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//    }


    /**
     * 获取ip地址
     *
     * @return
     */
    public static String getHostIP() {

        String hostIp = null;
        try {
            Enumeration nis = NetworkInterface.getNetworkInterfaces();
            InetAddress ia = null;
            while (nis.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) nis.nextElement();
                Enumeration<InetAddress> ias = ni.getInetAddresses();
                while (ias.hasMoreElements()) {
                    ia = ias.nextElement();
                    if (ia instanceof Inet6Address) {
                        continue;// skip ipv6
                    }
                    String ip = ia.getHostAddress();
                    if (!"127.0.0.1".equals(ip)) {
                        hostIp = ia.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
            LogUtils.e("getHostIP error");
        }
        return hostIp;

    }

    public static String GetNetworkType(Context context) {
        String strNetworkType = "";
        try {
            NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    strNetworkType = "WIFI";
                } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    String _strSubTypeName = networkInfo.getSubtypeName();

                    LogUtils.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);

                    // TD-SCDMA   networkType is 17
                    int networkType = networkInfo.getSubtype();
                    switch (networkType) {
                        case TelephonyManager.NETWORK_TYPE_GPRS:
                        case TelephonyManager.NETWORK_TYPE_EDGE:
                        case TelephonyManager.NETWORK_TYPE_CDMA:
                        case TelephonyManager.NETWORK_TYPE_1xRTT:
                        case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                            strNetworkType = "2G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_UMTS:
                        case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        case TelephonyManager.NETWORK_TYPE_HSDPA:
                        case TelephonyManager.NETWORK_TYPE_HSUPA:
                        case TelephonyManager.NETWORK_TYPE_HSPA:
                        case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                        case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                        case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                            strNetworkType = "3G";
                            break;
                        case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                            strNetworkType = "4G";
                            break;
                        default:
                            // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                            if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                                strNetworkType = "3G";
                            } else {
                                strNetworkType = _strSubTypeName;
                            }

                            break;
                    }

                    LogUtils.e("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtils.e("cocos2d-x", "Network Type : " + strNetworkType);

        return strNetworkType;
    }

    /**
     * 获取宽
     *
     * @return
     */
    public static int getScreenWidth() {
        String screen = ClientInfo.SCREEN;
        int screenWidth = 0;
        try {
            String[] xes = screen.split("x");
            String x = xes[0];
            screenWidth = Integer.parseInt(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenWidth;
    }

    /**
     * 获取高
     *
     * @return
     */
    public static int getScreenHeight() {
        String screen = ClientInfo.SCREEN;
        int screenHeight = 0;
        try {
            String[] yes = screen.split("x");
            String y = yes[1];
            screenHeight = Integer.parseInt(y);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenHeight;
    }

    /**
     * 获取屏幕信息
     */
    public static void getScreenParam(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        ClientInfo.SCREEN = dm.widthPixels + "x" + getRealHeight(context);
    }


    public static int getRealHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            display.getRealMetrics(dm);
        } else {
            display.getMetrics(dm);
        }
        int realHeight = dm.heightPixels;
        return realHeight;
    }


    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    public static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
