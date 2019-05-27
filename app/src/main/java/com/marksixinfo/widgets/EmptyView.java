package com.marksixinfo.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.ViewBase;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.UIUtils;

/**
 * @Auther: Administrator
 * @Date: 2019/3/25 0025 11:50
 * @Description:
 */
public class EmptyView extends ViewBase {

    TextView label, button;

    public EmptyView(Context context) {
        super(context);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onBefore() {

    }

    @Override
    public int getViewId() {
        return R.layout.view_empty;
    }

    @Override
    public void afterViews(View v) {
        button = findViewById(R.id.button);
        label = findViewById(R.id.label);
    }

    public EmptyView setEmptyImageSource(int id) {
        if (id != -1) {
            label.setCompoundDrawablesWithIntrinsicBounds(0, id, 0, 0);
        }
        return this;
    }

    public EmptyView setEmptyImageSource(int id, int width, int height) {
        if (id != -1) {
            Drawable drawable = getResources().getDrawable(id); //获取图片
            if (drawable != null) {
                drawable.setBounds(0, 0, UIUtils.dip2px(getContext(), width), UIUtils.dip2px(getContext(), height));  //设置图片参数
                label.setCompoundDrawables(null, drawable, null, null);  //设置到topImage
            }
        }
        return this;
    }

    /**
     * @param text            提示文字
     * @param id              提示图片
     * @param bottonText      按钮文字
     * @param onClickListener 按钮点击事件
     * @return
     */
    public EmptyView init(String text, int id, String bottonText, OnClickListener onClickListener) {
        setEmptyText(text).setEmptyImageSource(id).setEmptyButtonText(bottonText, onClickListener);
        return this;
    }

    /**
     * @param text            提示文字
     * @param id              提示图片
     * @param bottonText      按钮文字
     * @param width           图片宽
     * @param height          图片高
     * @param onClickListener 按钮点击事件
     * @return
     */
    public EmptyView init(String text, int id, String bottonText, int width, int height, OnClickListener onClickListener) {
        setEmptyText(text).setEmptyImageSource(id, width, height).setEmptyButtonText(bottonText, onClickListener);
        return this;
    }

    public EmptyView setEmptyText(String text) {
        if (CommonUtils.StringNotNull(text)) {
            label.setText(text);
            label.setVisibility(View.VISIBLE);
        } else {
            label.setVisibility(View.GONE);
        }
        return this;
    }

    public EmptyView setEmptyButtonText(String text, OnClickListener onClickListener) {
        if (CommonUtils.StringNotNull(text)) {
            button.setText(text);
            button.setOnClickListener(onClickListener);
            button.setVisibility(View.VISIBLE);
        } else {
            button.setVisibility(View.GONE);
        }

        return this;
    }


    public void showEmpty(View content, String title, String button, OnClickListener listener) {
        content.setVisibility(GONE);
        init(title, R.drawable.no_data, button, listener).setVisibility(VISIBLE);
    }

    public void showEmpty(View content, String button, OnClickListener listener) {
        content.setVisibility(GONE);
        init(getCtrl().getContext().getResources().getString(R.string.no_content), R.drawable.no_data, button, listener).setVisibility(VISIBLE);
    }

    public void showNetError(View content, String button, OnClickListener listener) {
        content.setVisibility(GONE);
        init(getCtrl().getContext().getResources().getString(R.string.no_network), R.drawable.no_network, button, listener).setVisibility(VISIBLE);
    }


    public void showContent(View content) {
        content.setVisibility(VISIBLE);
        setVisibility(GONE);
    }
}
