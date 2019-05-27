package com.marksixinfo.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.jaeger.library.StatusBarUtil;
import com.marksixinfo.BuildConfig;
import com.marksixinfo.R;
import com.marksixinfo.bean.ClientInfo;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.LoginSuccessEvent;
import com.marksixinfo.evenbus.TokenTimeOutEvent;
import com.marksixinfo.evenbus.WebSocketEvent;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.utils.ActivityManager;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.LogUtils;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.widgets.CommRedTitle;
import com.marksixinfo.widgets.CommWhiteTitle;
import com.marksixinfo.widgets.swipeback.SwipeBackLayout;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;
import androidx.fragment.app.FragmentActivity;


/**
 * Activity base
 *
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 19:47
 * @Description:
 */
public abstract class MarkSixActivity extends ActivityBase implements ActivityIntentInterface {
    private static final String TAG = "MarkSixActivity";
    ActivityPresent activityPresent = new ActivityPresent(this);
    public CommRedTitle markSixTitle;
    public CommWhiteTitle markSixTitleWhite;
    protected boolean isWhiteTitle = false;
    protected SwipeBackLayout mSwipeBackLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ClientInfo.getScreenParam(this);
        LogUtils.d(TAG, "DEBUG=" + BuildConfig.DEBUG);
//        PushUtils.onActivityStart(this);
        initEventBus();
        beforeOnCreate();

        mSwipeBackLayout = new SwipeBackLayout(this);
        mSwipeBackLayout.attachToActivity(this);
        mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_LEFT);
        setRedStatus();

    }

    @Override
    public void superViews() {
        super.superViews();
        try {
            if (isWhiteTitle) {
                markSixTitleWhite = findViewById(R.id.marksix_title_white);
            } else {
                markSixTitle = findViewById(R.id.marksix_title);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 处理 登录token 失效
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTokenLoss(TokenTimeOutEvent event) {
        try {
            if (lastTime == null) {
                lastTime = new Long(0);
            }
            if (this.getComponentName().getClassName().equals(CommonUtils.getRunningActivityName(this))) {
                synchronized (lastTime) {
                    long currentTime = Calendar.getInstance().getTimeInMillis();
                    if (currentTime - lastTime > 1000) {
                        LogUtils.e("loginOut", String.valueOf(event.errorMsg) + "name=" + this.getComponentName().getClassName() + "time=" + currentTime);
                        tokenTimeOut(event);
                        lastTime = currentTime;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void tokenTimeOut(TokenTimeOutEvent event) {
        if (event == null)
            return;
        SPUtil.setLoginOut(this);
        ClientInfo.initHeadParams(this);
        if (event.isGoToLogin) {
            toast(CommonUtils.StringNotNull(event.errorMsg) ? event.errorMsg : "请登录");
            goToLoginActivity();
        }
    }


    @Override
    public FragmentActivity getActivity() {
        return this;
    }

    public void beforeOnCreate() {

    }

    @Override
    public void goToLoginActivity() {
        startClass(R.string.LoginActivity);
    }

    @Override
    public void startClass(String activityName) {
        activityPresent.startClass(activityName);
    }

    @Override
    public void startClass(int activityName) {
        activityPresent.startClass(getString(activityName), null);
    }

    @Override
    public void startClass(String activityName, HashMap params) {
        activityPresent.startClass(activityName, params);
    }

    @Override
    public void startClass(int activityName, HashMap params) {
        activityPresent.startClass(getString(activityName), params);
    }

    @Override
    public void startClassWithFlag(String activityName, HashMap params, int... flags) {
        activityPresent.startClass(activityName, params, flags);
    }

    @Override
    public void startClassForResult(String activityName, HashMap params, int requestCode) {
        activityPresent.startClassForResult(activityName, params, requestCode);
    }

    @Override
    public HashMap<String, String> getUrlParam() {
        return activityPresent.getParams();
    }

    /**
     * 获取上个页面的数据
     *
     * @param s
     * @return
     */
    public String getStringParam(String s) {
        HashMap<String, String> urlParam = getUrlParam();
        if (CommonUtils.MapNotNull(urlParam)) {
            if (urlParam.containsKey(s)) {
                return urlParam.get(s);
            }
        }
        return "";
    }

    @Override
    public Bundle getBundleParams() {
        return activityPresent.getBundleParams();
    }

    @Override
    public void setResult(int resultCode, Bundle data) {
        activityPresent.setResult(resultCode, data);
    }

    @Override
    public String getThisHost() {
        return activityPresent.getThisHost();
    }

    @Override
    public String getThisHostUrl() {
        return activityPresent.getThisHostUrl();
    }

    @Override
    public String getLastHost() {
        return activityPresent.getLastHost();
    }

    @Override
    public String getLastHostUrl() {
        return activityPresent.getLastHostUrl();
    }

    @Override
    public void startClassForResult(String activityName, HashMap params, int requestCode, boolean checkLogin, Bundle bundle, int... flags) {
        activityPresent.startClassForResult(activityName, params, requestCode, checkLogin, bundle, flags);
    }

    @Override
    public void startClass(String activityName, HashMap params, boolean checkLogin, Bundle bundle, int... flags) {
        activityPresent.startClass(activityName, params, checkLogin, bundle, flags);
    }

    @Override
    public boolean checkLogin(String activityName, HashMap params) {
        return activityPresent.checkLogin(activityName, params);
    }

    public boolean checkLogin(int activityName) {
        return activityPresent.checkLogin(getString(activityName), null);
    }

    public boolean checkLogin(int activityName, HashMap params) {
        return activityPresent.checkLogin(getString(activityName), params);
    }

    @Override
    public boolean checkLogin() {
        return activityPresent.checkLogin();
    }

    @Override
    public boolean isLogin() {
        return activityPresent.isLogin();
    }

    public boolean isLoginToast() {
        boolean login = activityPresent.isLogin();
        if (!login) {
            activityPresent.toast("请登录", -1);
        }
        return login;
    }

    protected void initEventBus() {
        EventBusUtil.register(this);
    }


    @Override
    protected void onDestroy() {
        closeDialog();
        EventBusUtil.unregister(this);
        ActivityManager.getActivityManager().destoryActivity(this);
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ActivityManager.getActivityManager().pushActivity(this);
    }

    public void onResume() {
        super.onResume();
//        ActivityManager.getActivityManager().pushActivity(this);
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
//        ActivityManager.getActivityManager().popActivity(this);
        MobclickAgent.onPause(this);

    }

    /**
     * 弹窗提示
     *
     * @param msg       文字显示
     * @param canCancle 是否可以返回键关闭
     */
    @Override
    public void showDialog(String msg, boolean canCancle, boolean isListLoading) {
        activityPresent.showDialog(msg, canCancle, isListLoading);
    }

    /**
     * 关闭弹窗
     */
    @Override
    public void closeDialog() {
        activityPresent.closeDialog();
    }

    /**
     * 弱提示
     *
     * @param msg   文字显示
     * @param imgId 图片id -1则不显示图片
     */
    @Override
    public void toast(String msg, int imgId) {
        activityPresent.toast(msg, imgId);
    }

    /**
     * 弱提示
     *
     * @param msg 文字显示
     */
    public void toast(String msg) {
        toast(msg, -1);
    }

    /**
     * 成功类提示
     *
     * @param msg
     */
    @Override
    public void toastSuccess(String msg) {
        activityPresent.toastSuccess(msg);
    }

    /**
     * 信息类提示
     *
     * @param msg
     */
    @Override
    public void toastInfo(String msg) {
        activityPresent.toastInfo(msg);
    }

    /**
     * 预警类提示
     *
     * @param msg
     */
    @Override
    public void toastWarning(String msg) {
        activityPresent.toastWarning(msg);
    }

    /**
     * 错误类提示
     *
     * @param msg
     */
    @Override
    public void toastError(String msg) {
        activityPresent.toastError(msg);
    }

    @Override
    public Context getContext() {
        return this;
    }

    public void showDialog() {
        showDialog("", false, false);
    }

    public void showDialog(String msg) {
        showDialog(msg, false, false);
    }


    /**
     * 上次失效时间，避免并发启动login，singleTop并不能有效处理并发
     * 只能防止间隔时间小于20毫秒的
     * singleInstance则没必要
     */
    private Long lastTime = new Long(0);

    /**
     * 显示commondialog
     *
     * @param title    标题
     * @param msg      显示文字
     * @param leftStr  左边文字
     * @param rightStr 右边文字
     * @param left     左按钮点击
     * @param right    右边按钮点击
     */
    @Override
    public void showCommonDialog(String title, String msg, String leftStr, String rightStr, View.OnClickListener left, View.OnClickListener right) {
        activityPresent.showCommonDialog(title, msg, leftStr, rightStr, left, right);
    }

    /**
     * 隐藏commondialog
     */
    @Override
    public void closeCommonDialog() {
        activityPresent.closeCommonDialog();
    }


    @Override
    public void onBefore() {

    }

    /**
     * 设置白色标题,黑色文字
     */
    protected void setWhiteStatus() {
        setStatusBarColor(0xffffffff, 1);
        StatusBarUtil.setLightMode(this);
    }

    /**
     * 设置红色标题,白色文字
     */
    protected void setRedStatus() {
        setStatusBarColor(getResources().getColor(R.color.colorPrimary), 1);
    }


    /**
     * 设置状态栏颜色
     *
     * @param color
     */
    protected void setStatusBarColor(@ColorInt int color) {
        StatusBarUtil.setColor(this, color);
    }


    /**
     * 设置状态栏颜色
     *
     * @param color
     * @param statusBarAlpha 透明度
     */
    public void setStatusBarColor(@ColorInt int color, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        StatusBarUtil.setColorForSwipeBack(this, color, statusBarAlpha);
    }

    /**
     * 登录成功
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginSuccessEvent event) {
        if (event != null) {
            //中间页面也也需要刷新数据,有可能更换帐号登录,所以需要刷新中间页面
            loginSuccessRefresh();
        }
    }

    /**
     * WebSocket服务消息接收
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WebSocketEvent event) {
        if (event != null) {
            onMessage(event.message);
        }
    }

    /**
     * WebSocket 推送消息
     *
     * @param message
     */
    public void onMessage(String message) {

    }

    /**
     * 登录成功刷新
     */
    public void loginSuccessRefresh() {

    }

    public String getRecommendTag() {
        return SPUtil.getRecommendTag(getContext());
    }


    /**
     * 到详情页评论区
     *
     * @param id
     */
    protected void goToDetailActivityComment(String id) {
        startClass(R.string.DetailActivity, IntentUtils.getHashObj(new String[]{StringConstants.ID, id
                , StringConstants.DETAIL_SCROLL_COMMENT, "true"}));
    }

    /**
     * 帖子详情页
     *
     * @param id
     */
    protected void goToDetailActivity(String id) {
        startClass(R.string.DetailActivity,
                IntentUtils.getHashObj(new String[]{StringConstants.ID, id}));
    }

    /**
     * 论坛帖子详情页
     *
     * @param id
     */
    protected void goToForumDetailActivity(String id) {
        startClass(R.string.ForumDetailActivity,
                IntentUtils.getHashObj(new String[]{StringConstants.ID, id}));
    }

    /**
     * 到论坛帖子详情页评论区
     *
     * @param id
     */
    protected void goToForumDetailComment(String id) {
        startClass(R.string.ForumDetailActivity, IntentUtils.getHashObj(new String[]{StringConstants.ID, id
                , StringConstants.DETAIL_SCROLL_COMMENT, "true"}));
    }

    /**
     * 他人详情页
     *
     * @param id
     * @param type 0,头条  1,论坛   2,图库图片 3,图库图解
     */
    protected void goToUserCenterActivity(String id, int type) {
        startClass(R.string.UserPostCenterActivity, IntentUtils.getHashObj(new String[]{StringConstants.USER_ID, id,
                StringConstants.TYPE, String.valueOf(type)}));
    }

    /**
     * 回复列表页
     *
     * @param id
     * @param type 0,头条  1,论坛  2,图库图片 3,图库图解
     */
    protected void goToCommentListActivity(String id, int type) {
        startClass(R.string.CommentListActivity,
                IntentUtils.getHashObj(new String[]{StringConstants.ID, id,
                        StringConstants.TYPE, String.valueOf(type)}));
//        overridePendingTransition(R.anim.dialog_bottom_in, R.anim.bottom_silent);
    }


    public String getClassName() {
        return getClass().getSimpleName();
    }


    /**
     * 回到首页跳转
     *
     * @param index
     */
    public void gotoMainActivity(int index) {
        activityPresent.gotoMainActivity(index);
    }


    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    /**
     * 双击退出函数
     */
    public boolean exitBy2Click(Context context) {
        if (System.currentTimeMillis() - firstTime >= 2000 && context != null) {
            toast(context.getString(R.string.toast_next_press_exit));
            firstTime = System.currentTimeMillis();
            return false;
        } else {
            return true;
        }
    }

}
