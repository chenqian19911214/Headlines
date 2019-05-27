package com.marksixinfo.utils;

import android.util.Log;

import com.marksixinfo.BuildConfig;

/**
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 20:59
 * @Description:
 */
public class LogUtils {
    public static boolean IS_DEBUG = BuildConfig.DEBUG;

    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;
    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;
    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;
    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;
    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;
    static final String TAG = "debug";

    public static void setDebugMode(boolean isDebug) {
        IS_DEBUG = isDebug;
    }

    public static void v(Object... msg) {
        print(VERBOSE, msg);
    }

    public static void d(Object... msg) {
        print(DEBUG, msg);
    }

    public static void i(Object... msg) {
        print(INFO, msg);
    }

    public static void w(Object... msg) {
        print(WARN, msg);
    }

    public static void e(Object... msg) {
        print(ERROR, msg);
    }

    public static void mark(String markStr) {
        print(ERROR, markStr, "标记测试代码，上线前必须删除！！！");
    }

    private static void print(int type, Object... msg) {
        if (!IS_DEBUG) {
            return;
        }

        StringBuilder str = new StringBuilder();

        if (msg != null) {
            for (Object obj : msg) {
                str.append("★").append(obj);
            }
            if (str.length() > 0) {
                str.deleteCharAt(0);// 移除第一个五角星
            }
        } else {
            str.append("null");
        }
        try {
            StackTraceElement[] sts = Thread.currentThread().getStackTrace();
            StackTraceElement st = null;
            String tag = null;
            if (sts != null && sts.length > 4) {
                st = sts[4];
                if (st != null) {
                    String fileName = st.getFileName();
                    tag = (fileName == null) ? "Unkown" : fileName.replace(".java", "");
                    str.insert(0, "【" + tag + "." + st.getMethodName() + "() line " + st.getLineNumber() + "】\n>>>[")
                            .append("]");
                }
            }

            // use logcat log
            while (str.length() > 0) {
                switch (type) {
                    case VERBOSE:
                        Log.v(tag == null ? TAG : (TAG + "_" + tag), str.substring(0, Math.min(2000, str.length())));
                        break;
                    case DEBUG:
                        Log.d(tag == null ? TAG : (TAG + "_" + tag), str.substring(0, Math.min(2000, str.length())));
                        break;
                    case INFO:
                        Log.i(tag == null ? TAG : (TAG + "_" + tag), str.substring(0, Math.min(2000, str.length())));
                        break;
                    case WARN:
                        Log.w(tag == null ? TAG : (TAG + "_" + tag), str.substring(0, Math.min(2000, str.length())));
                        break;
                    case ERROR:
                        Log.e(tag == null ? TAG : (TAG + "_" + tag), str.substring(0, Math.min(2000, str.length())));
                        break;
                    default:
                        break;
                }
                str.delete(0, 2000);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void printStackTrace() {
        if (!IS_DEBUG) {
            return;
        }
        try {
            StackTraceElement[] sts = Thread.currentThread().getStackTrace();
            for (StackTraceElement stackTraceElement : sts) {
                Log.e("Log_trace", stackTraceElement.toString());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

