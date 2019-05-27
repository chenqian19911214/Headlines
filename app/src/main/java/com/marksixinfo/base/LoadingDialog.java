package com.marksixinfo.base;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.glide.GlideUtil;

/**
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 20:46
 * @Description:
 */
public class LoadingDialog extends Dialog {
    TextView msgText;
    private ImageView iv_loading;

    /**
     * Create a Dialog window that uses the default dialog frame style.
     *
     * @param context The Context the Dialog is to run it.  In particular, it
     *                uses the window manager and theme in this context to
     *                present its UI.
     */
    public LoadingDialog(Context context) {
        this(context, R.style.notitleDialog);
    }

    /**
     * Create a Dialog window that uses a custom dialog style.
     *
     * @param context The Context in which the Dialog should run. In particular, it
     *                uses the window manager and theme from this context to
     *                present its UI.
     * @param theme   A style resource describing the theme to use for the
     *                window. See <a href="{@docRoot}guide/topics/resources/available-resources.html#stylesandthemes">Style
     *                and Theme Resources</a> for more information about defining and using
     *                styles.  This theme is applied on top of the current theme in
     */
    public LoadingDialog(Context context, int theme) {
        super(context, theme);
        initView();
    }

    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }


    protected void init() {
        Window window = getWindow();
//        //设置该dialog与软键盘共存，不会在loading时隐藏软键盘
        window.setFlags(
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //设置无边距
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置dialog的位置在底部
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);

    }


    void initView() {
        View v = View.inflate(getContext(), R.layout.dialog_loading, null);
        setContentView(v);
        init();
        msgText = v.findViewById(R.id.msg);
        msgText.setVisibility(View.GONE);
        iv_loading = v.findViewById(R.id.iv_loading);
    }

    public void show(String msg, boolean isListLoading) {
        if (this.msgText != null) {
            if (!TextUtils.isEmpty(msg)) {
                msgText.setVisibility(View.VISIBLE);
                msgText.setText(msg);
            } else {
                msgText.setVisibility(View.GONE);
            }
        }

        if (iv_loading != null) {
            int drawable;
            int width;
            int height;
            if (isListLoading) {
                drawable = R.drawable.default_list_loading;
                width = UIUtils.dip2px(getContext(), 100);
                height = UIUtils.dip2px(getContext(), 45);
            } else {
                drawable = R.drawable.default_loading;
                width = UIUtils.dip2px(getContext(), 30);
                height = UIUtils.dip2px(getContext(), 30);
            }
            iv_loading.getLayoutParams().width = width;
            iv_loading.getLayoutParams().height = height;
            GlideUtil.loadImage(getContext(), drawable, iv_loading, R.drawable.toumingtu_head);
        }
        super.show();
    }


}
