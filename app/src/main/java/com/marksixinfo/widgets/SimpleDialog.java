package com.marksixinfo.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.utils.CommonUtils;

import androidx.annotation.LayoutRes;

/**
 * 简单dialog
 *
 * @Auther: Administrator
 * @Date: 2019/3/27 0027 14:45
 * @Description:
 */
public class SimpleDialog extends Dialog implements View.OnClickListener {


    private SucceedCallBackListener listener;
    private TextView selectOne;
    private TextView selectTwo;
    private boolean selectOneTitle;

    public SimpleDialog(Context context) {
        this(context, R.style.SimpleDialog);
    }

    public SimpleDialog(Context context, int theme) {
        super(context, theme);
        initView();
        init();
    }

    protected SimpleDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public void show(String one, String two, SucceedCallBackListener listener) {
        selectOne.setTextSize(selectOneTitle ? 15 : 20);
        selectOne.setTextColor(selectOneTitle ? 0xff666666 : 0xff333333);
        if (selectOneTitle) {
            selectOne.setBackgroundResource(0);
        }
        selectOne.setText(CommonUtils.StringNotNull(one) ? one : "");
        selectOne.setVisibility(CommonUtils.StringNotNull(one) ? View.VISIBLE : View.GONE);
        selectTwo.setText(CommonUtils.StringNotNull(two) ? two : "");
        selectTwo.setVisibility(CommonUtils.StringNotNull(two) ? View.VISIBLE : View.GONE);
        this.listener = listener;
        show();
    }

    public View setView(@LayoutRes int ResId) {
        //解决有时候布局加载高度不正常情况
        return getLayoutInflater().inflate(ResId, (ViewGroup) getWindow().getDecorView());
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

    public void initView() {
        setView(R.layout.dialog_simple);
        selectOne = findViewById(R.id.tv_one);
        selectOne.setOnClickListener(this);
        selectTwo = findViewById(R.id.tv_two);
        selectTwo.setOnClickListener(this);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }

    protected int getGravity() {
        return Gravity.BOTTOM;
    }

    protected int getWindowManagerWidth() {
        return WindowManager.LayoutParams.MATCH_PARENT;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_one:
                if (listener != null) {
                    listener.succeedCallBack("1");
                }
                if (!selectOneTitle && isShowing()) {
                    dismiss();
                }
                break;
            case R.id.tv_two:
                if (listener != null) {
                    listener.succeedCallBack("2");
                }
                if (isShowing()) {
                    dismiss();
                }
                break;
            case R.id.tv_cancel:
                if (isShowing()) {
                    dismiss();
                }
                break;
        }
    }

    public SimpleDialog setSelectOneTitle(boolean selectOneTitle) {
        this.selectOneTitle = selectOneTitle;
        return this;
    }
}
