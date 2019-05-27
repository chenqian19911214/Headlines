package com.marksixinfo.base;

import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.marksixinfo.R;
import com.marksixinfo.interfaces.ActivityIntentInterface;

import androidx.annotation.LayoutRes;

/**
 *  底部dialog
 *
 * @Auther: Administrator
 * @Date: 2019/3/18 0018 11:50
 * @Description:
 */
public abstract class DialogBase extends Dialog {

    protected static int defaultStyle = R.style.BottomDialog;

    public ActivityIntentInterface getCtrl() {
        return ctrl;
    }

    ActivityIntentInterface ctrl;

    public DialogBase(ActivityIntentInterface context) {
        this(context, defaultStyle);
        this.ctrl = context;
    }

    public DialogBase(ActivityIntentInterface context, int theme) {
        super(context.getContext(), theme);
        this.ctrl = context;
        initView();
        init();
    }

    protected DialogBase(ActivityIntentInterface context, boolean cancelable, OnCancelListener cancelListener) {
        super(context.getContext(), cancelable, cancelListener);
        this.ctrl = context;
    }

    protected int getGravity() {
        return Gravity.CENTER;
    }

    protected void init() {
        Window window = getWindow();
        //设置无边距
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = getWindowManagerWidth();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置dialog的位置在底部
        params.gravity = getGravity();
        window.setAttributes(params);

    }

    public View setView(@LayoutRes int ResId) {
        //解决有时候布局加载高度不正常情况
        return getLayoutInflater().inflate(ResId, (ViewGroup) getWindow().getDecorView());
    }

    public abstract void initView();

    public String getString(int id) {
        return getContext().getString(id);
    }

    protected int getWindowManagerWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }
}
