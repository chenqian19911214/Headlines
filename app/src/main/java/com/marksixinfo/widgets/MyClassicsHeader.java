package com.marksixinfo.widgets;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import java.util.Date;

import androidx.annotation.NonNull;

/**
 * 复写下拉刷新head
 *
 * @Auther: Administrator
 * @Date: 2019/5/7 0007 20:10
 * @Description:
 */
public class MyClassicsHeader extends ClassicsHeader {

    public MyClassicsHeader(Context context) {
        super(context);
    }

    public MyClassicsHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyClassicsHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyClassicsHeader setTextPulling(String text) {
        mTextPulling = text;
        return this;
    }

    public MyClassicsHeader setTextRefreshing(String text) {
        mTextRefreshing = text;
        return this;
    }

    public MyClassicsHeader setTextRelease(String text) {
        mTextRelease = text;
        return this;
    }

    public MyClassicsHeader setTextFinish(String text) {
        mTextFinish = text;
        return this;
    }

    public MyClassicsHeader setTextFinish(CharSequence text) {
        myTextFinish = text;
        return this;
    }

    public TextView getTitleView() {
        return findViewById(ID_TEXT_TITLE);
    }

    public LinearLayout getContentView() {
        return findViewById(android.R.id.widget_frame);
    }

    CharSequence myTextFinish;

    @Override
    public int onFinish(@NonNull RefreshLayout layout, boolean success) {
        if (success) {
            if (myTextFinish != null) {
                mTitleText.setText(myTextFinish);
            } else {
                mTitleText.setText(mTextFinish);
            }
            if (mLastTime != null) {
                setLastUpdateTime(new Date());
            }
        } else {
            mTitleText.setText(mTextFailed);
        }
        final View progressView = mProgressView;
        Drawable drawable = mProgressView.getDrawable();
        if (drawable instanceof Animatable) {
            if (((Animatable) drawable).isRunning()) {
                ((Animatable) drawable).stop();
            }
        } else {
            progressView.animate().rotation(0).setDuration(0);
        }
        progressView.setVisibility(GONE);
        return mFinishDuration;//延迟500毫秒之后再弹回
    }
}
