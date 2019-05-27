package com.marksixinfo.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import com.marksixinfo.R;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.os.Build.VERSION.SDK_INT;

/**
 * 消息通知
 *
 * @Auther: Administrator
 * @Date: 2019/5/22 0022 21:25
 * @Description:
 */
public class NotificationUtils {


    private static final int NotificationID = 0x10000;

    /**
     * 生成通知
     *
     * @param context
     * @param pi
     * @param ticker          标题
     * @param contentTitle    标题
     * @param bigContentTitle 主要标题
     * @param summaryText     标题
     * @param message         显示内容（消息体）
     */
    public static void showNotification(Context context, PendingIntent pi,
                                        String ticker, String contentTitle, String bigContentTitle, String summaryText,
                                        String message) {
        //android O后必须传入NotificationChannel
        if (SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "1");
            setNotification(builder, context, pi, ticker, contentTitle, bigContentTitle, summaryText, message);
            assert notificationManager != null;
            notificationManager.notify(NotificationID, builder.build());

            //ChannelId为"1",ChannelName为"Channel1"
            NotificationChannel channel = new NotificationChannel("1",
                    "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.YELLOW); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(channel);
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, null);
            setNotification(builder, context, pi, ticker, contentTitle, bigContentTitle, summaryText, message);
            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
            managerCompat.notify(NotificationID, builder.build());
        }
    }


    /**
     * 设置大布局通知参数
     *
     * @param builder
     * @param context
     * @param pi
     * @param ticker
     * @param contentTitle
     * @param bigContentTitle
     * @param summaryText
     * @param message
     */
    private static void setNotification(NotificationCompat.Builder builder, Context context, PendingIntent pi,
                                        String ticker, String contentTitle, String bigContentTitle, String summaryText,
                                        String message) {
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setTicker(ticker)
                .setContentTitle(contentTitle)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pi)
                .setVibrate(new long[]{500, 500, 500, 500, 500, 500})
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_SOUND);

        //大布局通知在4.1以后才能使用，BigTextStyle
        NotificationCompat.BigTextStyle textStyle = null;
        textStyle = new NotificationCompat.BigTextStyle();
        textStyle.setBigContentTitle(bigContentTitle)
                // 标题
                .setSummaryText(summaryText)
                .bigText(message);// 内容
        builder.setStyle(textStyle);
        builder.setContentText(message);
        builder.setSmallIcon(R.mipmap.ic_launcher);
    }

    /**
     * 关闭通知
     */
    public static void cancelNotification(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        assert manager != null;
        manager.cancel(NotificationID);
    }
}
