package com.marksixinfo.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.footer.ClassicsFooter;

/**
 * 复写下拉刷新foot
 *
 * @Auther: Administrator
 * @Date: 2019/5/8 0008 17:20
 * @Description:
 */
public class MyClassicsFooter extends ClassicsFooter {

    public MyClassicsFooter(Context context) {
        super(context);
    }

    public MyClassicsFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyClassicsFooter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MyClassicsFooter setTextPulling(String text) {
        mTextPulling = text;
        return this;
    }

    public MyClassicsFooter setTextRefreshing(String text) {
        mTextRefreshing = text;
        return this;
    }

    public MyClassicsFooter setTextRelease(String text) {
        mTextRelease = text;
        return this;
    }

    public MyClassicsFooter setTextFinish(String text) {
        mTextFinish = text;
        return this;
    }

    public MyClassicsFooter setTextTextNothing(String text) {
        mTextNothing = text;
        return this;
    }

}
