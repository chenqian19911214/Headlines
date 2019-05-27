package com.marksixinfo.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.tencent.smtt.sdk.WebView;


/**
 * @Auther: Administrator
 * @Date: 2019/3/19 0019 14:41
 * @Description:
 */
public class ScrollWebView extends WebView {

    private OnScrollChangeListener mOnScrollChangeListener;

    public ScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener.onScrollChanged(l, t, oldl, oldt);
        }
    }

    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
        this.mOnScrollChangeListener = listener;
    }

    public interface OnScrollChangeListener {

        void onScrollChanged(int l, int t, int oldl, int oldt);

    }

    public void setChildrenCacheEnabled(boolean enabled) {
        super.setChildrenDrawingCacheEnabled(enabled);
    }

}
