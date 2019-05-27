package com.marksixinfo.base;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.SPUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

/**
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 21:04
 * @Description:
 */
public abstract class MarkSixFragment extends FragmentBase implements ActivityIntentInterface {


    ActivityPresent activityPresent = new ActivityPresent(this);
    protected FragmentManager mFragmentManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentManager = getChildFragmentManager();
        initEventBus();
    }

    protected void initEventBus() {
        EventBusUtil.register(this);
    }


    @Override
    public void onDestroyView() {
        closeDialog();
        EventBusUtil.unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        return getActivity();
    }

    public void showDialog() {
        showDialog("", false, false);
    }

    public void showDialog(String msg) {
        showDialog(msg, false, false);
    }


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

    @Override
    public void beforeOnCreate() {

    }

    @Override
    public void goToLoginActivity() {
        activityPresent.startClass(getString(R.string.LoginActivity));
    }

    @Override
    public void startClass(String activityName) {
        activityPresent.startClass(activityName, null);
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
    public HashMap getUrlParam() {
        return activityPresent.getParams();
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


    public String getRecommendTag() {
        return SPUtil.getRecommendTag(getContext());
    }


    public String getClassName() {
        return getClass().getSimpleName();
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
        startClass(R.string.DetailActivity, IntentUtils.getHashObj(new String[]{StringConstants.ID, id}));
    }

    /**
     * 他人详情页
     *
     * @param id
     * @param type 0,头条  1,论坛  2,图库图片 3,图库图解
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

    @Override
    public void gotoMainActivity(int index) {
        activityPresent.gotoMainActivity(index);
    }

    public void onResume() {
        super.onResume();
//        ActivityManager.getActivityManager().pushActivity(this);
        MobclickAgent.onResume(getContext());
    }

    public void onPause() {
        super.onPause();
//        ActivityManager.getActivityManager().popActivity(this);
        MobclickAgent.onPause(getContext());

    }
}

