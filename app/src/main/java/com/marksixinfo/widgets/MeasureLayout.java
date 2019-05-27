package com.marksixinfo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.marksixinfo.utils.LogUtils;

/**
 * 详情webview测量top,测量结束回调
 *
 * @Auther: Administrator
 * @Date: 2019/4/5 0005 14:01
 * @Description:
 */
public class MeasureLayout extends LinearLayout {

    public MeasureLayout(Context context) {
        super(context);
    }

    public MeasureLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeasureLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private OnMaxYListener maxYListener;
    private int maxY;
    public final int MEASURE_TIME = 1000;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View childAt = getChildAt(0);
        if (childAt != null) {
            maxY = childAt.getMeasuredHeight();
        }
        int size = MeasureSpec.getSize(heightMeasureSpec);
        LogUtils.d("MeasureListView______" + size + "______" + maxY);
        if (isStart) {
            current = 0;
        } else {
            setTime();
        }
    }

    /**
     * 最大滑动值
     */
    public interface OnMaxYListener {
        void onMaxY(int maxY);
    }

    public void setMaxYListener(OnMaxYListener maxYListener) {
        this.maxYListener = maxYListener;
    }


    int current = 0;
    boolean isStart;

    public void setTime() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isStart = true;
                while (MEASURE_TIME - current >= 0) {
                    try {
                        current += 5;
                        Thread.sleep(5);
//                        LogUtils.d("timer:" + current);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
//                LogUtils.d("timer:" + current + "done");
                if (maxYListener != null) {
                    maxYListener.onMaxY(maxY);
                }
                isStart = false;
                current = 0;
            }
        }).start();
    }
}
