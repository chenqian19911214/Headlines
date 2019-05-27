package com.marksixinfo.base;

import android.view.View;

/**
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 19:52
 * @Description:
 */
public interface IBaseView extends ContextInterface {
    /**
     * 弹窗提示
     *
     * @param msg       文字显示
     * @param canCancle 是否可以返回键关闭
     */
    void showDialog(String msg, boolean canCancle, boolean isListLoading);

    /**
     * 关闭弹窗
     */
    void closeDialog();

    /**
     * 弱提示
     *
     * @param msg   文字显示
     * @param imgId 图片id -1则不显示图片
     */
    void toast(String msg, int imgId);

    /**
     * 成功类提示
     *
     * @param msg
     */
    void toastSuccess(String msg);

    /**
     * 信息类提示
     *
     * @param msg
     */
    void toastInfo(String msg);

    /**
     * 预警类提示
     *
     * @param msg
     */
    void toastWarning(String msg);

    /**
     * 错误类提示
     *
     * @param msg
     */
    void toastError(String msg);

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
    void showCommonDialog(String title, String msg, String leftStr, String rightStr, View.OnClickListener left, View.OnClickListener right);

    /**
     * 隐藏commondialog
     */
    void closeCommonDialog();


    /**
     * 开始网络请求
     */
    void onBefore();
}
