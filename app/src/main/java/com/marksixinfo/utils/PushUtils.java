package com.marksixinfo.utils;//package com.marksixinfo.utils;
//
//import android.app.Application;
//import android.content.Context;
//
//import com.umeng.message.IUmengRegisterCallback;
//import com.umeng.message.MsgConstant;
//import com.umeng.message.PushAgent;
//import com.umeng.message.UTrack;
//import com.umeng.message.UmengMessageHandler;
//import com.umeng.message.UmengNotificationClickHandler;
//import com.umeng.message.entity.UMessage;
//
//import me.leolin.shortcutbadger.ShortcutBadger;
//
///**
// *  友盟推送Utils
// *
// * @Auther: Administrator
// * @Date: 2019/4/18 0018 14:49
// * @Description:
// */
//public class PushUtils {
//
//    private static final String TAG = "PushUtils";
//
//    public static void initPush(Context context, UmengNotificationClickHandler notificationClickHandler) {
//        if (context == null || notificationClickHandler == null) {
//            return;
//        }
////        //开启 umeng push
//        PushAgent mPushAgent = PushAgent.getInstance(context);
//        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SERVER); //声音
//        mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);//呼吸灯
//        mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);//振动
//        mPushAgent.setPushCheck(true);
//        mPushAgent.setNotificationClickHandler(notificationClickHandler);
//        mPushAgent.setMessageHandler(new UmengMessageHandler() {
//            @Override
//            public void dealWithNotificationMessage(Context context, UMessage uMessage) {
//                super.dealWithNotificationMessage(context, uMessage);
//                String badge = uMessage.extra.get("badge"); //消息数量
//                LogUtils.d("badge", "badge:" + badge);
//                int count = NumberUtils.stringToInt(badge);
//                LogUtils.d("count", "count:" + count);
//                if (count <= 0) {
//                    count = 0;
//                }
//                if (count > 0) {
//                    ShortcutBadger.applyCount(context.getApplicationContext(), count);
//                }
//            }
//        });
//    }
//
//    /**
//     * 友盟推送单独使用
//     * 在所有的Activity 的onCreate 方法或在应用的BaseActivity的onCreate方法中添加：
//     * 注意：
//     * <p>
//     * 此方法与统计分析sdk中统计日活的方法无关！请务必调用此方法！
//     * 如果不调用此方法，不仅会导致按照"几天不活跃"条件来推送失效，
//     * 还将导致广播发送不成功以及设备描述红色等问题发生。
//     * 可以只在应用的主Activity中调用此方法，
//     * 但是由于SDK的日志发送策略，有可能由于主activity的日志没有发送成功，
//     * 而导致未统计到日活数据。
//     *
//     * @param context
//     */
//    public static void onActivityStart(Context context) {
//        if (context == null) {
//            return;
//        }
//        PushAgent.getInstance(context).onAppStart();
//    }
//
//    public static void initPushWithApplication(Application context) {
//        if (context == null) {
//            return;
//        }
//        PushAgent mPushAgent = PushAgent.getInstance(context);
//        //注册推送服务，每次调用register方法都会回调该接口
//        mPushAgent.register(new IUmengRegisterCallback() {
//
//            @Override
//            public void onSuccess(String deviceToken) {
//                //注册成功会返回device token
//                LogUtils.d(TAG, "deviceToken=" + deviceToken);
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//                LogUtils.d(TAG, "error=" + s + "\n" + s1);
//            }
//        });
//
//    }
//
//    /**
//     * 设置别名 同步操作，需要在子线程中执行 多次设置以最后一次设置为准
//     *
//     * @param context
//     * @param userId
//     */
//    public static void pushSetAlias(final Context context, final String userId) {
//        if (!CommonUtils.StringNotNull(userId) || context == null) {
//            return;
//        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                PushAgent mPushAgent = PushAgent.getInstance(context);
//                try {
//                    mPushAgent.addAlias(userId, "user_id", new UTrack.ICallBack() {
//                        @Override
//                        public void onMessage(boolean isSuccess, String message) {
//                            LogUtils.d(TAG, "pushSetAlias" + isSuccess + "\nmessage=" + message
//                                    + "\nuserId=" + userId);
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    LogUtils.e(TAG, "增加别名失败");
//                }
//            }
//        }).start();
//    }
//
//    /**
//     * 退出登录的时候，清除掉  alias
//     */
//    public static void removeAlias(final Context context, final String userId) {
//        if (!CommonUtils.StringNotNull(userId) || context == null) {
//            return;
//        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                PushAgent instance = PushAgent.getInstance(context);
//                try {
//                    instance.deleteAlias(userId, "user_id", new UTrack.ICallBack() {
//                        @Override
//                        public void onMessage(boolean isSuccess, String message) {
//                            LogUtils.d(TAG, "removeAlias" + isSuccess + "\nmessage=" + message
//                                    + "\nuserId=" + userId);
//                        }
//                    });
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//
//}
//
