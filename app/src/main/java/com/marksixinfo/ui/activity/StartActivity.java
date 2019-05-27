package com.marksixinfo.ui.activity;

import android.content.Intent;
import android.widget.ImageView;

import com.google.gson.reflect.TypeToken;
import com.marksixinfo.R;
import com.marksixinfo.base.ActivityBase;
import com.marksixinfo.bean.ClientInfo;
import com.marksixinfo.bean.LotteryNumberConfigData;
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.service.WebSocketService;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.JSONUtils;
import com.marksixinfo.utils.LogUtils;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.utils.UIUtils;
import com.zhangke.websocket.SimpleListener;
import com.zhangke.websocket.SocketListener;
import com.zhangke.websocket.WebSocketHandler;
import com.zhangke.websocket.response.ErrorResponse;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 启动页
 *
 * @auther: Administrator
 * @date: 2019/3/14 0014 下午 10:53
 */
public class StartActivity extends ActivityBase {

    @BindView(R.id.iv_background)
    ImageView ivBackground;


    @Override
    public int getViewId() {
        return R.layout.activity_start;
    }

    @Override
    public void afterViews() {
        startWebSocketService();
        SPUtil.setSystemStartTime(getApplication().getApplicationContext());//储存启动时间
        UIUtils.setTranslucentStatus(this, true);//将图片顶到状态栏
        UIUtils.hideBottomUIMenu(this);
        initLotteryConfig();
        WebSocketHandler.getDefault().addListener(socketListener);

//        ivBackground.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (lotteryInitDone) {
//                    startMain();
//                } else {
//                    lotteryInitDone = true;
//                }
//            }
//        }, NumberConstants.START_TIME);

//        initInfo();


        CommonUtils.setCurrentLottery(getApplicationContext(),
//                "{\"period\":\"2019057\",\"open\":1,\"lottery\":[\"43\",\"13\",\"11\",\"17\",\"33\",\"14\",\"27\"]}");
                "{\"period\":\"2019058\",\"open\":1,\"lottery\":[\"\",\"\",\"\",\"\",\"\",\"\",\"\"]}");
    }

    /**
     * WebSocket服务
     */
    private void startWebSocketService() {
        startService(new Intent(this, WebSocketService.class));
    }

    /**
     * 开奖页面,实时开奖配置文件
     */
    private void initLotteryConfig() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String json = JSONUtils.getJson("number_config_2019.json", getApplicationContext());
                    if (CommonUtils.StringNotNull(json)) {
                        ArrayList<LotteryNumberConfigData> config = null;
                        config = JSONUtils.fromJson(json, new TypeToken<ArrayList<LotteryNumberConfigData>>() {
                        });
                        if (CommonUtils.ListNotNull(config)) {
                            for (int i = 0; i < config.size(); i++) {
                                LotteryNumberConfigData data = config.get(i);
                                if (data != null) {
                                    data.setColor(CommonUtils.getLotteryColorByString(data.getBose()));
                                    ClientInfo.lotteryConfig.put(i, data);
                                }
                            }
                        }
                    }
                    Thread.sleep(NumberConstants.START_TIME);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    startMain();
                }
            }
        }).start();
    }

    private void startMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
//
//    private void initInfo() {
//        ClientInfo.getScreenParam(this);
//        initUmengPush();
//        ShortcutBadger.removeCount(getApplicationContext()); //移除外部消息红点
//    }
//
//    private void initUmengPush() {
//        PushUtils.initPush(this, new PushNotificationHandler());
//        if (SPUtil.isLogin(this)) {
//            //设置别名
//            String userId = SPUtil.getUserId(this);
//            PushUtils.pushSetAlias(this, userId);
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
//            StartActivity.this.onMessage(message);
            LogUtils.d("onMessage(String, T):" + message);
//            ToastShow.toastShow(getApplicationContext(), "推送信息:" + message);
//            LotteryRealTimeData data = CommonUtils.getRealTimeDataByMessage(message);
//            if (data != null) {
//                List<String> lottery = data.getLottery();
//                if (lottery != null && lottery.size() < 7) {
//                    LotteryRealTimeEvent event = new LotteryRealTimeEvent(2, data);
//                    SPUtil.setStringValue(getApplicationContext(), SPUtil.LOTTERY_CURRENT, JSONUtils.toJson(event));
//                    return;
//                }
//            }
//            SPUtil.setStringValue(getApplicationContext(), SPUtil.LOTTERY_CURRENT, "");
            CommonUtils.setCurrentLottery(getApplicationContext(), message);
        }
    };
}
