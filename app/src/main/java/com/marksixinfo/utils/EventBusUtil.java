package com.marksixinfo.utils;

import org.greenrobot.eventbus.EventBus;

/**
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 21:01
 * @Description:
 */
public class EventBusUtil {
    private EventBusUtil() {
    }

    public static void register(Object subscriber) {
        if (!EventBus.getDefault().isRegistered(subscriber)) {
            EventBus.getDefault().register(subscriber);
        }
    }

    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
        EventBus.getDefault().removeAllStickyEvents();
    }

    public static void post(Object event) {
        EventBus.getDefault().post(event);
    }

    public static void postSticky(Object event) {
        EventBus.getDefault().postSticky(event);
    }


    //订阅者处理事件
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMoonEvent(MessageEvent messageEvent){
//
//    }
//
    //订阅者处理粘性事件
//    @Subscribe(threadMode = ThreadMode.MAIN,sticky = true)
//    public void onMoonStickyEvent(MessageEvent messageEvent){
//
//    }

}

