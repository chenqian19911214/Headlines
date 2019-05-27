package com.marksixinfo.base;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.UIUtils;

/**
 * @Auther: Administrator
 * @Date: 2019/3/18 0018 12:03
 * @Description:
 */
public class CommonDialog extends DialogBase implements View.OnClickListener {

    protected TextView title;
    protected TextView msg;
    protected TextView tvCancel;
    protected TextView tvOk;
    private LinearLayout ll_title;

    public CommonDialog(ActivityIntentInterface context) {
        super(context);
    }

    public CommonDialog(ActivityIntentInterface context, int theme) {
        super(context, theme);
    }

    public CommonDialog(ActivityIntentInterface context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    protected int getViewLayoutId() {
        return R.layout.dialog_common;
    }

    @Override
    public void initView() {
        setView(getViewLayoutId());
        ll_title = findViewById(R.id.ll_title);
        title = findViewById(R.id.title);
        msg = findViewById(R.id.msg);
        tvCancel = findViewById(R.id.tv_cancel);
        tvOk = findViewById(R.id.tv_ok);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    public void show(String title, String msg, String leftStr, String rightStr) {
        show(title, msg, leftStr, rightStr, null, null);
    }

    /**
     * @param title    标题
     * @param msg      显示文字
     * @param leftStr  左边文字
     * @param rightStr 右边文字
     * @param left     左按钮点击
     * @param right    右边按钮点击
     */
    public void show(String title, String msg, String leftStr, String rightStr, View.OnClickListener left, View.OnClickListener right) {
        super.show();
        this.msg.setText(String.valueOf(msg));
        showOrHideTitle(!TextUtils.isEmpty(title));
        this.title.setText(String.valueOf(title));
        this.msg.setVisibility(TextUtils.isEmpty(msg) ? View.GONE : View.VISIBLE);
        this.tvCancel.setText(String.valueOf(leftStr));
        this.tvCancel.setVisibility(CommonUtils.StringNotNull(leftStr) ? View.VISIBLE : View.GONE);
        this.tvOk.setVisibility(CommonUtils.StringNotNull(rightStr) ? View.VISIBLE : View.GONE);
        this.tvOk.setText(String.valueOf(rightStr));
        this.tvCancel.setOnClickListener(left != null ? left : new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        this.tvOk.setOnClickListener(right != null ? right : new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Deprecated
    public void show() {
        super.show();
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    public void showOrHideTitle(boolean isShow) {
        if (title != null && msg != null) {

            title.setVisibility(!isShow ? View.GONE : View.VISIBLE);
            msg.setBackgroundColor(!isShow ? 0x00000000 : 0xffffffff);
            UIUtils.setMargins(getContext(), this.ll_title, 0, !isShow ? 20 : 0, 0, 0);
        }
    }

    /**
     * 获取信息view
     *
     * @return
     */
    public TextView getMsgTextView() {
        return msg;
    }

    public CommonDialog mSetCancelable(boolean flag) {
        setCancelable(flag);
        return this;
    }

    public CommonDialog mSetCanceledOnTouchOutside(boolean flag) {
        setCanceledOnTouchOutside(flag);
        return this;
    }

    public CommonDialog setMsgMaxLines(int lines) {
        this.msg.setMaxLines(lines);
        return this;
    }

//    /**
//     * 设置title灰色背景
//     *
//     * @return
//     */
//    public CommonDialog setTitleBackgroundGrayColor() {
//        this.ll_title.setBackgroundResource(R.drawable.shape_cart_dialog_corner);
//        return this;
//    }
//
//    /**
//     * 设置title白色背景
//     *
//     * @return
//     */
//    public CommonDialog setTitleBackgroundWhiteColor() {
//        this.ll_title.setBackgroundResource(R.drawable.shape_cart_dialog_white_corner);
//        return this;
//    }
}

