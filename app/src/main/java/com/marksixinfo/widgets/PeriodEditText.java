package com.marksixinfo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.marksixinfo.interfaces.SucceedCallBackListener;

/**
 *  开奖输入期数
 *
 * @Auther: Administrator
 * @Date: 2019/5/16 0016 16:25
 * @Description:
 */
public class PeriodEditText extends EditText {

    public PeriodEditText(Context context) {
        super(context);
    }

    public PeriodEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PeriodEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    SucceedCallBackListener listener;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && listener != null) {
            listener.succeedCallBack(null);
        }
        return super.onTouchEvent(event);
    }

    public void setListener(SucceedCallBackListener listener) {
        this.listener = listener;
    }
}
