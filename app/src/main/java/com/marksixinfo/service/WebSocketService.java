package com.marksixinfo.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

import com.marksixinfo.MarkSixApp;
import com.marksixinfo.evenbus.WebSocketEvent;
import com.marksixinfo.ui.activity.MainActivity;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.LogUtils;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.response.ErrorResponse;

import androidx.annotation.Nullable;

/**
 * WebSocket服务
 *
 * @Auther: Administrator
 * @Date: 2019/5/22 0022 18:54
 * @Description:
 */
public class WebSocketService extends Service {

    private PendingIntent pi;

    @Override
    public void onCreate() {
        LogUtils.d("WebSocketService__onCreate");
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        pi = PendingIntent.getActivity(getBaseContext(), 0, intent, 0);
        WebSocketHandler.getDefault().addListener(socketListener);
    }

    @Override
    public void onDestroy() {
        LogUtils.d("WebSocketService__onDestroy");
        WebSocketHandler.getDefault().removeListener(socketListener);
    }


    private SocketListener socketListener = new SimpleListener() {

        @Override
        public void onSendDataError(ErrorResponse errorResponse) {
            errorResponse.release();
            LogUtils.d("onSendDataError:" + errorResponse.toString());
        }

        @Override
        public <T> void onMessage(String message, T d) {
            LogUtils.d("onMessage(String, T):" + message);
            try {
                if (CommonUtils.StringNotNull(message)) {
                    if (message.contains("from") && MarkSixApp.isDebug) {
                        message = message.substring(message.indexOf("{"), message.length() - 1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            EventBusUtil.post(new WebSocketEvent(message));
            CommonUtils.setCurrentLottery(getApplicationContext(), message);

//            NotificationUtils.showNotification(getApplicationContext(),pi,
//                    "标题1","标题2","主要标题3","标题4",message);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
