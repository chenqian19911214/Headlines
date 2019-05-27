package com.marksixinfo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.marksixinfo.interfaces.SucceedCallBackListener;

/**
 *  开奖期数控制键盘收起输入框
 *
 * @Auther: Administrator
 * @Date: 2019/5/16 0016 16:34
 * @Description:
 */
public class LotteryPeriodLinearLayout extends LinearLayout{
    public LotteryPeriodLinearLayout(Context context) {
        super(context);
    }

    public LotteryPeriodLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LotteryPeriodLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    SucceedCallBackListener listener;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && listener != null) {
            listener.succeedCallBack(null);
        }
        return super.onInterceptTouchEvent(event);
    }

    public void setListener(SucceedCallBackListener listener) {
        this.listener = listener;
    }
}

