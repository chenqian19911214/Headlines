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
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.utils.VersionUpdateUtils;
import com.marksixinfo.widgets.EditionDialog;
import com.marksixinfo.widgets.LotteryRemindDialog;
import com.marksixinfo.widgets.NoAnimationViewPager;
import com.zhangke.websocket.WebSocketHandler;

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


    private VersionUpdateUtils versionUpdateUtils;
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
        getVersionUpdate();
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

        setMessage();

//        tezt();
    }

    /**
     * 处理版本更新
     * */
    private void getVersionUpdate() {
        versionUpdateUtils = VersionUpdateUtils.initVersionUpdateUtils();
        if (versionUpdateUtils!=null) {
            versionUpdateUtils.getInformation();//获取当前服务器版本
            versionUpdateUtils.setVersionCallback(new VersionUpdateUtils.VersionCallback() { //当需要更新是回调
                @Override
                public void CallbackData(EditionNameDate editionNameDater) {
                    versionUpdateUtils.showEditionDialog(ActivityManager.getActivityManager().getCurrentActivity(),editionNameDater); //调用更新弹窗
                }
            });
        }
    }

    private void tezt() {
        mViewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (indes >= lsits.size()) {
                    indes = 0;
                }
                String s = lsits.get(indes);
                onMessage(s);
                indes++;
                tezt();
            }
        }, 7000);
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
                setRealLottery();
                finish();
            }
        }
        return false;
    }

    /**
     * 实时开奖中双击退出断开连接,下次启动重连,确保接收到最新数据
     */
    private void setRealLottery() {
        String value = SPUtil.getStringValue(getContext(), SPUtil.LOTTERY_CURRENT);
        if (CommonUtils.StringNotNull(value)) {
            WebSocketHandler.getDefault().disConnect();
        }
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

    @Override
    public void onMessage(String message) {
        LotteryRealTimeData data = CommonUtils.getRealTimeDataByMessage(message);
        if (data != null) {
            int isOpen = data.getIsOpen();//0,晚上9点20 后台开始重置  1,准备开奖,弹框提醒  2,开球中   3,开奖结束
            if (isOpen == 1) {
                setRemindDialog();
            }
            EventBusUtil.post(new LotteryRealTimeEvent(isOpen, data));
        }
    }

    List<String> lsits = new ArrayList<>();
    int indes = 0;

    private void setMessage() {
        String s0 = "{\"period\":\"2019058\",\"open\":0,\"lottery\":[\"16\",\"\",\"\",\"\",\"\",\"\",\"\"]}";
        String s1 = "{\"period\":\"2019058\",\"open\":1,\"lottery\":[\"\",\"\",\"\",\"\",\"\",\"\",\"\"]}";
        String s2 = "{\"period\":\"2019058\",\"open\":2,\"lottery\":[\"16\",\"\",\"\",\"\",\"\",\"\",\"\"]}";
        String s3 = "{\"period\":\"2019058\",\"open\":2,\"lottery\":[\"16\",\"12\",\"\",\"\",\"\",\"\",\"\"]}";
        String s4 = "{\"period\":\"2019058\",\"open\":2,\"lottery\":[\"16\",\"12\",\"35\",\"\",\"\",\"\",\"\"]}";
        String s5 = "{\"period\":\"2019058\",\"open\":2,\"lottery\":[\"16\",\"12\",\"35\",\"09\",\"\",\"\",\"\"]}";
        String s6 = "{\"period\":\"2019058\",\"open\":2,\"lottery\":[\"16\",\"12\",\"35\",\"09\",\"23\",\"\",\"\"]}";
        String s7 = "{\"period\":\"2019058\",\"open\":2,\"lottery\":[\"16\",\"12\",\"35\",\"09\",\"23\",\"44\",\"\"]}";
        String s8 = "{\"period\":\"2019058\",\"open\":3,\"lottery\":[\"16\",\"12\",\"35\",\"09\",\"23\",\"44\",\"02\"]}";

        lsits.add(s0);
        lsits.add(s1);
        lsits.add(s2);
        lsits.add(s3);
        lsits.add(s4);
        lsits.add(s5);
        lsits.add(s6);
        lsits.add(s7);
        lsits.add(s8);

    }

    /**
     * 设置开奖弹框
     */
    public void setRemindDialog() {
        Activity currentActivity = ActivityManager.getActivityManager().getCurrentActivity();
        if (currentActivity != null && !MainActivity.class.equals(currentActivity.getClass())) {
            showRemindDialog(currentActivity);
        } else {
            if (currentActivity != null && ((MainActivity) currentActivity).getPosition() != 2) {
                showRemindDialog(currentActivity);
            }
        }
    }

    /**
     * 即将开奖弹框提醒
     */
    private void showRemindDialog(Activity currentActivity) {
        EditionDialog updateDialog = null;
        if (versionUpdateUtils!=null) {
            updateDialog = versionUpdateUtils.getUpdateDialog();
        }
        if (updateDialog != null && updateDialog.isShowing()) {//有更新弹框,不提示开奖
            return;
        }
        if (!currentActivity.isFinishing()) {
            if (remindDialog == null || !remindDialog.isShowing()) {
                remindDialog = new LotteryRemindDialog((ActivityIntentInterface) currentActivity);
                remindDialog.show();
            }
        }
    }

    public int getPosition() {
        return position;
    }
}
