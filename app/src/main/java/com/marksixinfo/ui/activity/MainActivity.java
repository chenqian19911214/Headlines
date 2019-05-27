package com.marksixinfo.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;

import com.google.gson.Gson;
import com.marksixinfo.R;
import com.marksixinfo.adapter.MainNavigatorAdapter;
import com.marksixinfo.adapter.PagerBaseAdapter;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.base.ViewPagerHelper;
import com.marksixinfo.bean.ClientInfo;
import com.marksixinfo.bean.EditionNameDate;
import com.marksixinfo.bean.LotteryRealTimeData;
import com.marksixinfo.constants.NetUrlGallery;
import com.marksixinfo.evenbus.LotteryRealTimeEvent;
import com.marksixinfo.evenbus.MainActivityIndexEvent;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.ui.fragment.ForumFragment;
import com.marksixinfo.ui.fragment.GalleryFragment2;
import com.marksixinfo.ui.fragment.LotteryFragment2;
import com.marksixinfo.ui.fragment.MainHomeFragment;
import com.marksixinfo.ui.fragment.MineFragment;
import com.marksixinfo.utils.ActivityManager;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.NotificationUtils;
import com.marksixinfo.widgets.EditionDialog;
import com.marksixinfo.widgets.LotteryRemindDialog;
import com.marksixinfo.widgets.NoAnimationViewPager;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 功能描述: 主页
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/4/3 0003 19:15
 */
public class MainActivity extends MarkSixActivity {

    @BindView(R.id.cvp_view)
    NoAnimationViewPager mViewPager;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;

    private static final String[] CHANNELS = new String[]{
            "首页",
            "悟空论坛",
            "开奖",
            "图库",
            "我"};
    private static final int[] IMAGES = new int[]{
            R.drawable.selector_tab_main,
            R.drawable.selector_tab_forum,
            R.drawable.selector_tab_lottery,
            R.drawable.selector_tab_gallery,
            R.drawable.selector_tab_mine};

    private List<PageBaseFragment> mDataList = new ArrayList<>();
    private CommonNavigator commonNavigator;
    private int position;
    private LotteryRemindDialog remindDialog;
    private EditionDialog updateDialog;


    @Override
    protected void onStart() {
        super.onStart();
        mSwipeBackLayout.setSwipeBackEnable(false); //主 activity 可以调用该方法，禁用滑动退出
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        WebSocketHandler.getDefault().removeListener(socketListener);
    }


    @Override
    public int getViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void afterViews() {
        // 获取版本号
        getInformation();
//        WebSocketHandler.getDefault().addListener(socketListener);
        NotificationUtils.cancelNotification(getContext());

        //添加页面
        mDataList.add(new MainHomeFragment());
        mDataList.add(new ForumFragment());
        mDataList.add(new LotteryFragment2());
        mDataList.add(new GalleryFragment2());
        mDataList.add(new MineFragment());


        mViewPager.setAdapter(new PagerBaseAdapter(getSupportFragmentManager(), mDataList));
        mViewPager.setOffscreenPageLimit(mDataList.size());

        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                MainActivity.this.position = position;
                CommonUtils.setInitFragmentData(mDataList, position);
                setTitleBar(position);
            }
        });

        initMagicIndicator();

    }

    /**
     * 设置个人页status颜色
     *
     * @param position
     */
    private void setTitleBar(int position) {
        setTitleBar(position == 4);
    }

    /**
     * 设置个人页status颜色
     *
     * @param isWhite
     */
    public void setTitleBar(boolean isWhite) {
        if (isWhite) {
            setWhiteStatus();
        } else {
            setRedStatus();
        }
    }


    /**
     * 功能描述: 初始化指示器及标题
     *
     * @auther: Administrator
     * @date: 2019/3/15 0015 下午 3:41
     */
    private void initMagicIndicator() {
        commonNavigator = new CommonNavigator(this);
        commonNavigator.setBackgroundColor(Color.WHITE);
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new MainNavigatorAdapter(mDataList, IMAGES, CHANNELS
                , commonNavigator, mViewPager));
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (exitBy2Click(this)) {// 调用双击退出函数
                finish();
            }
        }
        return false;
    }

    /**
     * 回到首页
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MainActivityIndexEvent event) {
        if (event != null && mViewPager != null) {
            mViewPager.setCurrentItem(event.index);
        }
    }

//    private SocketListener socketListener = new SimpleListener() {
//        @Override
//        public void onConnected() {
//            LogUtils.d("onConnected");
//        }
//
//        @Override
//        public void onConnectFailed(Throwable e) {
//            if (e != null) {
//                LogUtils.d("onConnectFailed:" + e.toString());
//            } else {
//                LogUtils.d("onConnectFailed:null");
//            }
//        }
//
//        @Override
//        public void onDisconnect() {
//            LogUtils.d("onDisconnect");
//        }
//
//        @Override
//        public void onSendDataError(ErrorResponse errorResponse) {
//            errorResponse.release();
//            LogUtils.d("onSendDataError:" + errorResponse.toString());
//        }
//
//        @Override
//        public <T> void onMessage(String message, T data) {
//            MainActivity.this.onMessage(message);
//            LogUtils.d("onMessage(String, T):" + message);
////            toast("MainActivity推送信息:" + message);
//        }
//
//        @Override
//        public void onPing(Framedata framedata) {
//            //to override
//        }
//
//        @Override
//        public void onPong(Framedata framedata) {
//            //to override
//        }
//    };

    @Override
    public void onMessage(String message) {
        LotteryRealTimeData data = CommonUtils.getRealTimeDataByMessage(message);
        if (data != null) {
            int isOpen = data.getIsOpen();//0,晚上9点20 后台开始重置  1,准备开奖,弹框提醒  2,开球中   3,开奖结束
            if (isOpen == 1) {
                setRemindDialog();
            } else {
                EventBusUtil.post(new LotteryRealTimeEvent(isOpen, data));
            }
        }
    }

    /**
     * 设置开奖弹框
     */
    public void setRemindDialog() {
        Activity currentActivity = ActivityManager.getActivityManager().getCurrentActivity();
        if (!MainActivity.class.equals(currentActivity.getClass())) {
            showRemindDialog(currentActivity);
        } else {
            if (((MainActivity) currentActivity).getPosition() != 2) {
                showRemindDialog(currentActivity);
            }
        }
    }


    /**
     * 即将开奖弹框提醒
     */
    private void showRemindDialog(Activity currentActivity) {
        if (updateDialog != null && updateDialog.isShowing()) {//有更新弹框,不提示开奖
            return;
        }
        if (!currentActivity.isFinishing()) {
            if (remindDialog != null && remindDialog.isShowing()) {
                remindDialog.dismiss();
            }
            remindDialog = new LotteryRemindDialog((ActivityIntentInterface) currentActivity);
            remindDialog.show();
        }
    }

    /**
     * 更新弹窗
     */
    private void showEditionDialog(EditionNameDate editionNameDater) {
        Activity currentActivity = ActivityManager.getActivityManager().getCurrentActivity();
        if (!currentActivity.isFinishing()) {
            if (updateDialog == null || !updateDialog.isShowing()) {
                updateDialog = new EditionDialog((ActivityIntentInterface) currentActivity, editionNameDater);
                updateDialog.show();
            }
        }
    }

    public int getPosition() {
        return position;
    }

    /**
     * 获取当前版本
     */
    private void getInformation() {
        String url = NetUrlGallery.GET_VERSION_NAME;
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder().url(url).build();
        Call call = client.newCall(request);
        client.sslSocketFactory();
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = handler.obtainMessage();
                message.obj = "";
                message.what = 1;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream input = response.body().byteStream();
                BufferedInputStream bufinput = new BufferedInputStream(input);
                byte[] buffer = new byte[10000];
                int bytes = bufinput.read(buffer);
                final String str = new String(buffer, 0, bytes);
                if (TextUtils.isEmpty(str))
                    return;
                Message message = handler.obtainMessage();
                if (str.contains("<html>")) {
                    message.obj = "";
                    message.what = 1;
                } else {
                    message.obj = str;
                    message.what = 0;
                }
                handler.sendMessage(message);
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) { //请求成功
                String str = (String) msg.obj;
                EditionNameDate responsese = new Gson().fromJson(str, EditionNameDate.class);
                if (responsese == null)
                    return;
                String serverVersionD = responsese.getVersion();
                //  String serverVersionD = "2.0.2";
                String localVersionD = ClientInfo.VERSION;
                String serverVersion = serverVersionD.replace(".", "");
                String localVersion = localVersionD.replace(".", "");

                if (TextUtils.isEmpty(serverVersion) || TextUtils.isEmpty(serverVersion))
                    return;
                if ((Integer.valueOf(serverVersion) > Integer.valueOf(localVersion)) && !responsese.getState().equals("0")) {
                    showEditionDialog(responsese);
                }
            } else { //请求失败
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getInformation(); //5 秒后再次请求
                    }
                }, 10000);
            }
        }
    };

    public EditionDialog getUpdateDialog() {
        return updateDialog;
    }
}