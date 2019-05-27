package com.marksixinfo.widgets;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.ViewBase;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.UIUtils;

/**
 * 通用红色背景标题
 *
 * @Auther: Administrator
 * @Date: 2019/4/17 0017 13:46
 * @Description:
 */
public class CommRedTitle extends ViewBase {

    private TextView tvBack;
    private TextView tvTitle;
    private TextView tvFunction;

    protected OnBackListener onBackListener;

    public interface OnBackListener {
        void back();
    }

    public void setOnBackListener(OnBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public CommRedTitle(Context context) {
        super(context);
    }

    public CommRedTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommRedTitle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getViewId() {
        return R.layout.view_title_red;
    }

    @Override
    public void afterViews(View view) {

        tvBack = view.findViewById(R.id.tv_back);
        tvTitle = view.findViewById(R.id.tv_title);
        tvFunction = view.findViewById(R.id.tv_function);

        //返回键的监听
        tvBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onBackListener != null) {
                    onBackListener.back();
                } else {
                    Activity a = (Activity) getContext();
                    if (a != null)
                        a.onBackPressed();
                }
            }
        });
    }

    /**
     * 通用标题
     *
     * @param title 标题文字
     */
    public void init(String title) {
        init(title, true);
    }

    /**
     * 通用标题
     *
     * @param title 标题文字
     * @param back  返回文字
     */
    public void init(String title, String back) {
        init(title, true, back, "", 0, null);
    }

    /**
     * 通用标题
     *
     * @param title  标题文字
     * @param isBack 是否显示返回
     */
    public void init(String title, boolean isBack) {
        init(title, isBack, "", "", 0, null);
    }

    /**
     * 功能描述: 通用标题
     *
     * @auther: Administrator
     * @date: 2019/4/17 0017 14:28
     */
    public void init(String title, String back,
                     String function, int selectResId, OnClickListener functionListener) {
        init(title, true, back, function, selectResId, functionListener);
    }

    /**
     * 功能描述: 通用标题
     *
     * @auther: Administrator
     * @date: 2019/4/17 0017 14:28
     */
    public void init(String title, boolean isBack, String back,
                     String function, int selectResId, OnClickListener functionListener) {
        tvTitle.setText(CommonUtils.StringNotNull(title) ? title : "");
        tvBack.setText(CommonUtils.StringNotNull(back) ? back : "");
        tvBack.setVisibility(isBack ? VISIBLE : GONE);
        tvFunction.setVisibility(functionListener != null && (CommonUtils.StringNotNull(function) || selectResId > 0) ? VISIBLE : GONE);
        tvFunction.setText(CommonUtils.StringNotNull(function) ? function : "");
        if (selectResId > 0) {
            UIUtils.setRightDrawable(getContext(), tvFunction, selectResId);
        }
        if (functionListener != null && tvFunction.getVisibility() == VISIBLE) {
            tvFunction.setOnClickListener(functionListener);
        }
    }

    public TextView getTvTitle() {
        return tvTitle;
    }

    public TextView getTvFunction() {
        return tvFunction;
    }

    public TextView getTvBack() {
        return tvBack;
    }
}
