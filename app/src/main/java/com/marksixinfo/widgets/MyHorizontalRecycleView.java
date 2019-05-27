package com.marksixinfo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.recyclerview.widget.RecyclerView;

/**
 * 搜索结果横滑用户与侧滑关闭冲突
 *
 * @Auther: Administrator
 * @Date: 2019/5/27 0027 20:34
 * @Description:
 */
public class MyHorizontalRecycleView extends RecyclerView {

    public MyHorizontalRecycleView(Context context) {
        super(context);
    }

    public MyHorizontalRecycleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyHorizontalRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}