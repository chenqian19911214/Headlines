package com.marksixinfo.utils;

import android.util.Log;

import com.zhangke.websocket.util.Logable;

/**
 *  webSocket 日志重写
 *
 * @Auther: Administrator
 * @Date: 2019/5/20 0020 20:19
 * @Description:
 */
public class MyLogAbleImpl implements Logable {

    @Override
    public void v(String tag, String msg) {
        if (!LogUtils.IS_DEBUG) {
            return;
        }
        Log.v(tag, msg);
    }

    @Override
    public void v(String tag, String msg, Throwable tr) {
        if (!LogUtils.IS_DEBUG) {
            return;
        }
        Log.v(tag, msg, tr);
    }

    @Override
    public void d(String tag, String text) {
        if (!LogUtils.IS_DEBUG) {
            return;
        }
        Log.d(tag, text);
    }

    @Override
    public void d(String tag, String text, Throwable tr) {
        if (!LogUtils.IS_DEBUG) {
            return;
        }
        Log.d(tag, text, tr);
    }

    @Override
    public void i(String tag, String text) {
        if (!LogUtils.IS_DEBUG) {
            return;
        }
        Log.i(tag, text);
    }

    @Override
    public void i(String tag, String text, Throwable tr) {
        if (!LogUtils.IS_DEBUG) {
            return;
        }
        Log.i(tag, text, tr);
    }

    @Override
    public void e(String tag, String text) {
        if (!LogUtils.IS_DEBUG) {
            return;
        }
        Log.e(tag, text);
    }

    @Override
    public void e(String tag, String msg, Throwable tr) {
        if (!LogUtils.IS_DEBUG) {
            return;
        }
        Log.e(tag, msg, tr);
    }

    @Override
    public void w(String tag, Throwable tr) {
        if (!LogUtils.IS_DEBUG) {
            return;
        }
        Log.w(tag, tr);
    }

    @Override
    public void wtf(String tag, String msg) {
        if (!LogUtils.IS_DEBUG) {
            return;
        }
        Log.wtf(tag, msg);
    }

    @Override
    public void wtf(String tag, Throwable tr) {
        if (!LogUtils.IS_DEBUG) {
            return;
        }
        Log.wtf(tag, tr);
    }

    @Override
    public void wtf(String tag, String msg, Throwable tr) {
        if (!LogUtils.IS_DEBUG) {
            return;
        }
        Log.wtf(tag, msg, tr);
    }
}

