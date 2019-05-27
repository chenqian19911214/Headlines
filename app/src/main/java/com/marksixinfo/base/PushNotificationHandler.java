package com.marksixinfo.base;//package com.marksixinfo.base;
//
//import android.content.Context;
//import android.content.Intent;
//import android.net.Uri;
//
//import com.marksixinfo.R;
//import com.marksixinfo.utils.CommonUtils;
//import com.marksixinfo.utils.LogUtils;
//import com.umeng.message.UmengNotificationClickHandler;
//import com.umeng.message.entity.UMessage;
//
///**
// * @Auther: Administrator
// * @Date: 2019/4/18 0018 14:56
// * @Description:
// */
//public class PushNotificationHandler extends UmengNotificationClickHandler {
//
//    private static final String TAG = "ZallGoPushNotificationHandler";
//
//    @Override
//    public void dismissNotification(Context context, UMessage msg) {
//        LogUtils.d(TAG, "dismissNotification");
//        super.dismissNotification(context, msg);
//    }
//
//    @Override
//    public void launchApp(Context context, UMessage msg) {
//        LogUtils.d(TAG, "launchApp");
//        //获取跳转的路径
//        String path = msg.extra.get("pushPage"); //zallgo://orderDetail?orderId=1364151&orderType=1&role=0
//        LogUtils.d("push", "launchApp:" + path);
//        Intent activityIntent = getActivityIntent(context, path);
//        if (CommonUtils.isAppRunBackground(context)) {
//            Intent mainIntent = IntentUtils.getIntent(context, context.getString(R.string.MainActivity), null);
//            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            if (activityIntent != null) {
//                gotoActivitys(context, new Intent[]{mainIntent, activityIntent});
//            } else {
//                gotoActivity(context, mainIntent);
//            }
//        } else {
//            gotoActivity(context, activityIntent);
//        }
//    }
//
//    @Override
//    public void openActivity(Context context, UMessage msg) {
//        LogUtils.d(TAG, "openActivity");
//        super.openActivity(context, msg);
//    }
//
//    @Override
//    public void openUrl(Context context, UMessage msg) {
//        LogUtils.d(TAG, "openUrl");
//        super.openUrl(context, msg);
//    }
//
//    @Override
//    public void dealWithCustomAction(Context context, UMessage msg) {
//        LogUtils.d(TAG, "dealWithCustomAction");
//        super.dealWithCustomAction(context, msg);
//    }
//
//    @Override
//    public void autoUpdate(Context context, UMessage msg) {
//        LogUtils.d(TAG, "autoUpdate");
//        super.autoUpdate(context, msg);
//    }
//
//
//    private Intent getActivityIntent(Context context, String path) {
//        try {
//            Uri uri = Uri.parse(path);
//            Intent in = IntentUtils.getIntent(context, uri);
//            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            return in;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//
//    private void gotoActivity(Context context, Intent in) {
//        if (in != null) {
//            try {
//                context.startActivity(in);
//            } catch (Exception e) {
//                e.printStackTrace();
//                LogUtils.e(TAG, "传值异常，无法跳转");
//            }
//        } else {
//            LogUtils.e(TAG, "传值异常，无法跳转");
//        }
//    }
//
//
//    private void gotoActivitys(Context context, Intent[] intents) {
//        if (intents != null) {
//            try {
//                context.startActivities(intents);
//            } catch (Exception e) {
//                e.printStackTrace();
//                LogUtils.e(TAG, "传值异常，无法跳转");
//            }
//        } else {
//            LogUtils.e(TAG, "传值异常，无法跳转");
//        }
//    }
//
//}
//
