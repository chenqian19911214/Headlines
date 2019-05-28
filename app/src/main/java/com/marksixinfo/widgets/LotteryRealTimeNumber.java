package com.marksixinfo.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.ViewBase;
import com.marksixinfo.bean.ClientInfo;
import com.marksixinfo.bean.LotteryNumberConfigData;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.NumberUtils;
import com.marksixinfo.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 实时开奖页头部
 *
 * @Auther: Administrator
 * @Date: 2019/5/11 0011 14:00
 * @Description:
 */
public class LotteryRealTimeNumber extends ViewBase {


    private List<View> views;
    private List<View> cards;
    private int cardHeight;

    public LotteryRealTimeNumber(Context context) {
        super(context);
    }

    public LotteryRealTimeNumber(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LotteryRealTimeNumber(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getViewId() {
        return R.layout.view_lottery_real_time_number;
    }

    @Override
    public void afterViews(View v) {
        views = new ArrayList<>();
        cards = new ArrayList<>();
        cardHeight = UIUtils.dip2px(getContext(), 50);
        views.add(v.findViewById(R.id.view_v1));
        views.add(v.findViewById(R.id.view_v2));
        views.add(v.findViewById(R.id.view_v3));
        views.add(v.findViewById(R.id.view_v4));
        views.add(v.findViewById(R.id.view_v5));
        views.add(v.findViewById(R.id.view_v6));
        views.add(v.findViewById(R.id.view_v7));

        cards.add(views.get(0).findViewById(R.id.view_cart));
        cards.add(views.get(1).findViewById(R.id.view_cart));
        cards.add(views.get(2).findViewById(R.id.view_cart));
        cards.add(views.get(3).findViewById(R.id.view_cart));
        cards.add(views.get(4).findViewById(R.id.view_cart));
        cards.add(views.get(5).findViewById(R.id.view_cart));
        cards.add(views.get(6).findViewById(R.id.view_cart));
        for (View view : cards) {
            if (view != null) {
                view.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * 设置当期开奖数据
     */
    public void setData(int index, List<String> lottery) {
        if (CommonUtils.ListNotNull(lottery)) {
            try {
                for (int i = 0; i < index + 1; i++) {
                    setLotteryData(views.get(i), ClientInfo.lotteryConfig.get((NumberUtils.stringToInt(lottery.get(i)) - 1)));
                    View cardView = cards.get(i);
                    if (cardView.getVisibility() == VISIBLE) {
                        setGoneView(cardView);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 恢复已开
     */
    public void reSetStatus() {
        if (CommonUtils.ListNotNull(cards)) {
            for (View card : cards) {
                card.getLayoutParams().height = cardHeight;
                card.requestLayout();
                card.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * 开奖动画
     *
     * @param cardView
     */
    private void setGoneView(View cardView) {
        ValueAnimator animator = ValueAnimator.ofInt(100, 0);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (int) valueAnimator.getAnimatedValue();
//                cardView.setAlpha((animatedValue / 100f));
                cardView.getLayoutParams().height = (int) ((animatedValue / 100f) * cardHeight);
                cardView.requestLayout();
                if (animatedValue <= 0) {
                    cardView.setVisibility(GONE);
                }
            }
        });
        animator.start();
    }


    /**
     * 设置单个开奖信息
     *
     * @param view_v
     * @param data
     */
    private void setLotteryData(View view_v, LotteryNumberConfigData data) {
        if (data != null) {
            String num = data.getTema();
            String shengxiao = data.getShengxiao();
            String color = data.getColor();
            if (CommonUtils.StringNotNull(color)) {
                View view = view_v.findViewById(R.id.ll_background);
                view.setBackgroundColor(Color.parseColor(color));
            }
            if (CommonUtils.StringNotNull(num)) {
                TextView tv_number = view_v.findViewById(R.id.tv_number);
                tv_number.setText(num);
            }
            if (CommonUtils.StringNotNull(shengxiao)) {
                TextView tv_sheng_xiao = view_v.findViewById(R.id.tv_sheng_xiao);
                tv_sheng_xiao.setText(shengxiao);
            }
        }
    }


}
