package com.marksixinfo.widgets;

import android.content.Context;

import net.lucode.hackware.magicindicator.buildins.ArgbEvaluatorHolder;

/**
 * 开奖 IndicatorTitle
 *
 * @Auther: Administrator
 * @Date: 2019/5/13 0013 14:24
 * @Description:
 */
public class TitleColorTransitionPagerTitleView extends TitleSimplePagerTitleView {

    public TitleColorTransitionPagerTitleView(Context context) {
        super(context);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        int color = ArgbEvaluatorHolder.eval(leavePercent, mSelectedColor, mNormalColor);
        setTextColor(color);
        setImageResource(mNormalImg);
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        int color = ArgbEvaluatorHolder.eval(enterPercent, mNormalColor, mSelectedColor);
        setTextColor(color);
        setImageResource(mSelectedImg);
    }

    @Override
    public void onSelected(int index, int totalCount) {
    }

    @Override
    public void onDeselected(int index, int totalCount) {
    }
}
