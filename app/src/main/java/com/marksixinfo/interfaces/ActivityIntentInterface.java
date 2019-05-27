package com.marksixinfo.interfaces;

import android.os.Bundle;

import com.marksixinfo.base.IBaseView;

import java.util.HashMap;

import androidx.fragment.app.FragmentActivity;

/**
 * 通用
 *
 * @Auther: Administrator
 * @Date: 2019/3/18 0018 12:02
 * @Description:
 */
public interface ActivityIntentInterface extends IBaseView {
    /**
     * 获取当前activity
     *
     * @return
     */
    FragmentActivity getActivity();

    /**
     * 页面打开之前判断登录（用于外部跳转）
     */
    void beforeOnCreate();

    /**
     * 跳转到登录页面
     */
    void goToLoginActivity();

    /**
     * 页面跳转
     */
    void startClass(String activityName);

    /**
     * 页面跳转
     */
    void startClass(String activityName, HashMap params);


    /**
     * 页面跳转
     */
    void startClass(int activityName);

    /**
     * 页面跳转
     */
    void startClass(int activityName, HashMap params);

    /**
     * 页面跳转 带flag
     */
    void startClassWithFlag(String activityName, HashMap params, int... flags);

    /**
     * 页面跳转回调
     */
    void startClassForResult(String activityName, HashMap params, int requestCode);

    /**
     * 获取上一个页面参数
     */
    HashMap getUrlParam();

    /**
     * 获取上一个页面复杂参数
     */
    Bundle getBundleParams();

    /**
     * 重新setResult，规范传参
     *
     * @param resultCode
     * @param data
     */
    void setResult(int resultCode, Bundle data);

    /**
     * 获取当前页面host
     */
    String getThisHost();

    /**
     * 获取当前页面host以及参数
     */
    String getThisHostUrl();

    /**
     * 获取上一个页面host
     */
    String getLastHost();

    /**
     * 获取上一个页面host以及参数
     */
    String getLastHostUrl();

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
    void startClassForResult(String activityName, HashMap params, int requestCode, boolean checkLogin, Bundle bundle, int... flags);


    /**
     * 页面跳转 带启动模式的
     *
     * @param activityName
     * @param params
     * @param checkLogin   是否校验登录
     * @param bundle       json 数据
     * @param flags        可多传
     */
    void startClass(String activityName, HashMap params, boolean checkLogin, Bundle bundle, int... flags);

    /**
     * 判断是否登录，未登录跳转登录页面 登录成功后，跳到制定页面
     *
     * @param activityName 登录成功后跳转页面
     * @param params       登录成功后页面传参
     * @return 是否登录
     */
    boolean checkLogin(String activityName, HashMap params);


    /**
     * 判断是否登录，未登录跳转登录页面
     *
     * @return 是否登录
     */
    boolean checkLogin();

    /**
     * 是否登录
     *
     * @return
     */
    boolean isLogin();

    /**
     * 回到首页跳转
     *
     * @param index
     */
    void gotoMainActivity(int index);
}

