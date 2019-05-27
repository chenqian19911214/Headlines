package com.marksixinfo.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.MainActivityIndexEvent;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.ui.activity.MainActivity;
import com.marksixinfo.utils.ActivityManager;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.LogUtils;
import com.marksixinfo.utils.SPUtil;

import java.util.HashMap;

import androidx.fragment.app.FragmentActivity;

/**
 * Activity 跳转工具类
 *
 * @Auther: Administrator
 * @Date: 2019/4/4 0004 20:51
 * @Description:
 */
public class ActivityPresent extends NetPresenterBase {
    CommonDialog commonDialog;

    public ActivityIntentInterface getCtrl() {
        return (ActivityIntentInterface) getiBaseView();
    }

    ActivityIntentInterface activityIntentInterface;
    static final String TAG = "ActivityIntentUtils";

    public ActivityPresent(ActivityIntentInterface activityIntentInterface) {
        super(activityIntentInterface);
        this.activityIntentInterface = activityIntentInterface;
    }

    /**
     * 是否可用
     *
     * @return
     */
    public boolean isUseable() {
        return activityIntentInterface != null;
    }

    public FragmentActivity getContext() {
        if (!isUseable())
            return null;
        return activityIntentInterface.getActivity();
    }

    public Intent getIntent() {
        if (!isUseable() || getContext() == null)
            return null;
        return activityIntentInterface.getActivity().getIntent();
    }


    /**
     * 跳转到登录页面
     */
    public void goToLoginActivity() {
        startClass(getString(R.string.LoginActivity), null);
    }

    /**
     * 页面跳转
     *
     * @param activityName
     */
    public void startClass(String activityName) {
        startClass(activityName, null, null);
    }

    /**
     * 页面跳转
     *
     * @param activityName
     * @param params
     */
    public void startClass(String activityName, HashMap params) {
        startClass(activityName, params, null);
    }

    /**
     * 页面跳转 带启动模式的
     *
     * @param activityName
     * @param params
     * @param flags        可多传
     */
    public void startClass(String activityName, HashMap params, int... flags) {
        startClass(activityName, params, true, flags);

    }

    /**
     * 页面跳转 带启动模式的
     *
     * @param activityName
     * @param params
     * @param checkLogin   是否校验登录
     * @param flags        可多传
     */
    public void startClass(String activityName, HashMap params, boolean checkLogin, int... flags) {
        startClass(activityName, params, checkLogin, null, flags);

    }

    /**
     * 页面跳转 带启动模式的
     *
     * @param activityName
     * @param params
     * @param checkLogin   是否校验登录
     * @param bundle       json 数据
     * @param flags        可多传
     */
    public void startClass(String activityName, HashMap params, boolean checkLogin, Bundle bundle, int... flags) {
        Intent in = initIntent(activityName, params, checkLogin, bundle, flags);
        if (in != null) {
            if (getContext().getPackageManager().resolveActivity(in, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                try {
                    getContext().startActivity(in);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.e(TAG, "传值异常，无法跳转activityName=" + activityName);
                }
            } else {
                LogUtils.e(TAG, "无此页面，无法跳转activityName=" + activityName);
            }

        } else {
            LogUtils.e(TAG, "传值异常，无法跳转activityName=" + activityName);
        }
    }

    /**
     * 生成intent
     *
     * @param activityName
     * @param params
     * @param checkLogin   是否校验登录
     * @param bundle       复杂数据
     * @param flags        启动模式
     * @return
     */
    private Intent initIntent(String activityName, HashMap params, boolean checkLogin, Bundle bundle, int... flags) {
        Intent in = IntentUtils.getIntent(getContext(), activityName, params);
        if (in != null) {
            in = intentFilter(in, checkLogin);
            if (in == null)
                return null;
            String thisHost = getThisHost();
            String thisHostUrl = getThisHostUrl();
            in.putExtra(getString(R.string.thisHost), TextUtils.isEmpty(thisHost) ? getString(R.string.MainActivity) : thisHost);
            in.putExtra(getString(R.string.thisUrl), TextUtils.isEmpty(thisHostUrl) ? getString(R.string.MainActivity) : thisHostUrl);
            if (bundle != null) {
                in.putExtra(getString(R.string.jsonBundle), bundle);
            }
            if (in.getDataString() != null) {
                in.putExtra(getString(R.string.nextHost), in.getData().getHost());
                in.putExtra(getString(R.string.nextUrl), in.getDataString());
            }
            if (flags != null && flags.length > 0) {
                if (flags.length > 1) {
                    for (int flag : flags) {
                        in.addFlags(flag);
                    }
                } else {
                    in.setFlags(flags[0]);
                }
            }
        } else {
            LogUtils.e(TAG, "传值异常，无法跳转activityName=" + activityName);
        }
        return in;
    }


    /**
     * 页面跳转回调
     *
     * @param activityName
     * @param params
     * @param requestCode
     * @param checkLogin   是否校验登录
     * @param bundle       复杂数据
     * @param flags        启动模式
     */
    public void startClassForResult(String activityName, HashMap params, int requestCode, boolean checkLogin, Bundle bundle, int... flags) {
        Intent in = initIntent(activityName, params, checkLogin, bundle, flags);
        if (in != null) {
            try {
                getContext().startActivityForResult(in, requestCode);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(TAG, "传值异常，无法跳转activityName=" + activityName);
            }
        } else {
            LogUtils.e(TAG, "传值异常，无法跳转activityName=" + activityName);
        }

    }

    /**
     * 页面跳转回调
     *
     * @param activityName
     * @param params
     * @param requestCode
     */
    public void startClassForResult(String activityName, HashMap params, int requestCode) {
        startClassForResult(activityName, params, requestCode, true, null, null);
    }

    /**
     * 获取上一个页面参数
     */
    public HashMap getParams() {
        return IntentUtils.getUrlParam(getContext());
    }

    /**
     * 获取上一个页面参数
     */
    public Bundle getBundleParams() {
        return IntentUtils.getBundleParams(getContext());
    }

    /**
     * 获取startClassForResult中回传的bundle数据
     */
    public Bundle getResultBundleParams(Intent in) {
        return IntentUtils.getBundleParams(getContext(), in);
    }

    /**
     * 重新setResult，规范传参
     *
     * @param resultCode
     * @param data
     */
    public void setResult(int resultCode, Bundle data) {
        Intent in = IntentUtils.getResultBundleIntent(getContext(), data);
        if (getContext() != null) {
            getContext().setResult(resultCode, in);
        }

    }

    /**
     * 获取当前页面host
     */
    public String getThisHost() {
        String thisHost = "";
        if (getIntent() != null) {
            if (getIntent().getData() != null) {
                thisHost = getIntent().getData().getHost();
            } else {
                thisHost = getThisHostUrl();
            }
        }
        return thisHost;
    }

    /**
     * 获取当前页面host以及参数
     */
    public String getThisHostUrl() {
        String thisHost = "";
        if (getIntent() != null && getIntent().getDataString() != null) {
            try {
                thisHost = getIntent().getDataString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return thisHost;
    }

    /**
     * 获取上一个页面host
     */
    public String getLastHost() {

        String thisHost = "";
        if (getIntent() != null) {
            thisHost = getIntent().getStringExtra(getString(R.string.thisHost));
        }
        return thisHost;
    }

    /**
     * 获取上一个页面host以及参数
     */
    public String getLastHostUrl() {

        String thisHost = "";
        if (getIntent() != null) {
            thisHost = getIntent().getStringExtra(getString(R.string.thisUrl));
        }
        return thisHost;
    }

    /**
     * 对 intent进行筛选或者一切定制化操作
     * 加之前请跟 master管理者沟通确认，尽量少加
     *
     * @param in
     * @return
     */
    public Intent intentFilter(Intent in, boolean checkLogin) {
        if (in != null) {
            Bundle bundle = IntentUtils.getActivityInfo(getContext(), in);
            if (bundle != null) {
                if (checkLogin && !SPUtil.isLogin(getContext()) && isNeedLogin(bundle))//需要登录，并且没有登录 走登录流程
                {
                    return getLoginIntent();
                }

            }
            Uri uri = in.getData();
            in.setData(uri);
        }
        return in;
    }

    /**
     * 判断是否需要登录
     *
     * @return
     */
    public boolean isNeedLogin(Bundle bundle) {
        return bundle != null && bundle.getBoolean(getString(R.string.loginTag));
    }

    /**
     * 获取登录意图
     *
     * @return
     */
    public Intent getLoginIntent() {
        return IntentUtils.getIntent(getContext(), getString(R.string.LoginActivity), null);
    }

    /**
     * 判断是否登录，未登录跳转登录页面
     *
     * @return 是否登录
     */
    public boolean checkLogin() {
        if (!SPUtil.isLogin(getContext())) {
            goToLoginActivity();
            return false;
        }
        return true;
    }

    /**
     * 判断是否登录
     *
     * @return 是否登录
     */
    public boolean isLogin() {
        return SPUtil.isLogin(getContext());
    }

    /**
     * 判断是否登录，未登录跳转登录页面 登录成功后，跳到指定页面
     *
     * @param activityName 登录成功后跳转页面
     * @param params       登录成功后页面传参
     * @return 是否登录
     */
    public boolean checkLogin(String activityName, HashMap params) {
        if (!SPUtil.isLogin(getContext())) {
            if (params == null) {
                params = new HashMap();
            }
            if (CommonUtils.StringNotNull(activityName)) {
                params.put(StringConstants.CALLBACK, activityName);
            }
            startClass(getString(R.string.LoginActivity), params);
            return false;
        }
        return true;
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
    public void showCommonDialog(String title, String msg, String leftStr, String rightStr, View.OnClickListener left, View.OnClickListener right) {
        if (commonDialog == null) {
            commonDialog = new CommonDialog(getCtrl());
        }
        if (commonDialog.isShowing()) {
            commonDialog.dismiss();
        }
        commonDialog.show(title, msg, leftStr, rightStr, left, right);
    }

    /**
     * 隐藏commondialog
     */
    public void closeCommonDialog() {
        if (commonDialog != null) {
            commonDialog.dismiss();
        }
    }

    /**
     * 回到首页跳转
     *
     * @param index
     */
    public void gotoMainActivity(int index) {
        Activity currentActivity = ActivityManager.getActivityManager().getCurrentActivity();
        EventBusUtil.post(new MainActivityIndexEvent(index));
        if (!MainActivity.class.equals(currentActivity.getClass())) {
            ActivityManager.getActivityManager().popAllActivityExceptOne(MainActivity.class);
        }
    }
}

