package com.marksixinfo.base;

import android.app.Application;

import com.marksixinfo.utils.MyLogAbleImpl;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.WebSocketManager;
import com.zhangke.websocket.WebSocketSetting;

/**
 * WebSocket
 *
 * @Auther: Administrator
 * @Date: 2019/5/17 0017 19:35
 * @Description:
 */
public class WebSocketUtils {


    /**
     * WebSocket 连接地址
     */
//    private static String connectUrl = "ws://121.40.165.18:8800";
//    private static String connectUrl = "wss://echo.websocket.org/";
    //线上
    private static String connectUrl = "wss://www.sskk58.com:9200?type=lottery";
//    private static String connectUrl = "wss://images.34399.com:9200?type=lottery";

    /**
     * 超时时间 毫秒
     */
    private static int connectTimeout = 15000;

    /**
     * 设置心跳间隔时间
     */
    private static int connectionLostTimeout = 60;

    /**
     * 重连次数，默认为：Integer.MAX_VALUE 次
     */
    private static int reconnectFrequency = Integer.MAX_VALUE;


    public static void init(Application application) {

        WebSocketSetting setting = new WebSocketSetting();
        //连接地址，必填，例如 wss://echo.websocket.org
        setting.setConnectUrl(connectUrl);//必填

        //设置连接超时时间
        setting.setConnectTimeout(connectTimeout);

        //设置心跳间隔时间
        setting.setConnectionLostTimeout(connectionLostTimeout);

        //设置断开后的重连次数，可以设置的很大，不会有什么性能上的影响
        setting.setReconnectFrequency(reconnectFrequency);

        //设置消息分发器，接收到数据后先进入该类中处理，处理完再发送到下游
//        setting.setResponseProcessDispatcher(new AppResponseDispatcher());

        //网络状态发生变化后是否重连，
        //需要调用 WebSocketHandler.registerNetworkChangedReceiver(context) 方法注册网络监听广播
        setting.setReconnectWithNetworkChanged(true);

        //通过 init 方法初始化默认的 WebSocketManager 对象
        WebSocketManager manager = WebSocketHandler.init(setting);

        //重写log,release不打印
        WebSocketHandler.setLogable(new MyLogAbleImpl());

        //启动连接
        manager.start();

        //注意，需要在 AndroidManifest 中配置网络状态获取权限
        //注册网路连接状态变化广播
        WebSocketHandler.registerNetworkChangedReceiver(application.getApplicationContext());

    }

}
