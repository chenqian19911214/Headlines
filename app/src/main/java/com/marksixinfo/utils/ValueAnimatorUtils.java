package com.marksixinfo.utils;

import android.animation.ValueAnimator;

import com.marksixinfo.interfaces.SucceedCallBackListener;

/**
 * 动画utils
 *
 * @Auther: Administrator
 * @Date: 2019/5/10 0010 16:59
 * @Description:
 */
public class ValueAnimatorUtils {


    /**
     * 属性动画百分比 0~100
     *
     * @param duration
     * @param listener
     * @return
     */
    public static ValueAnimator getValueShow(long duration, SucceedCallBackListener<Float> listener) {
        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                if (listener != null) {
                    listener.succeedCallBack(animatedValue / 100f);
                }
            }
        });
        return animator;
    }

    /**
     * 属性动画百分比 100~0
     *
     * @param duration
     * @param listener
     * @return
     */
    public static ValueAnimator getValueHidden(long duration, SucceedCallBackListener<Float> listener) {
        ValueAnimator animator = ValueAnimator.ofInt(100, 0);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int animatedValue = (int) valueAnimator.getAnimatedValue();
                if (listener != null) {
                    listener.succeedCallBack(animatedValue / 100f);
                }
            }
        });
        return animator;
    }
}
