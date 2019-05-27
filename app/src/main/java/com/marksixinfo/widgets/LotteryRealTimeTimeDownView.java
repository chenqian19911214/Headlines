package com.marksixinfo.widgets;

import android.content.Context;
import android.util.AttributeSet;

import com.marksixinfo.R;

/**
 * 实时开奖倒计时
 *
 * @Auther: Administrator
 * @Date: 2019/5/18 0018 15:51
 * @Description:
 */
public class LotteryRealTimeTimeDownView extends TimeDownView {

    public LotteryRealTimeTimeDownView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LotteryRealTimeTimeDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LotteryRealTimeTimeDownView(Context context) {
        super(context);
    }


    @Override
    public int getView() {
        return R.layout.layout_lottery_timedown;
    }
}
