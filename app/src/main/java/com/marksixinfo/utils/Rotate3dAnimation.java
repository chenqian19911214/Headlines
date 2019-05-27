package com.marksixinfo.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;

/**
 * 3D卡片旋转动画
 *
 * @Auther: Administrator
 * @Date: 2019/5/23 0023 13:00
 * @Description:
 */
public class Rotate3dAnimation {

    public Rotate3dAnimation() {
        initAnimatorSet();
    }

    private AnimatorSet inSet, outSet;

    // 改变视角距离, 贴近屏幕,这个必须设置，因为如果不这么做，沿着Y轴旋转的过程中有可能产生超出屏幕的3D效果。
    public void start(Context context, View top, View back) {
        if (context != null && top != null && back != null) {
            int distance = 16000;
            float scale = context.getResources().getDisplayMetrics().density * distance;
            top.setCameraDistance(scale);
            back.setCameraDistance(scale);
            inSet.setTarget(top);
            outSet.setTarget(back);
            inSet.start();
            outSet.start();
        }
    }

    private void initAnimatorSet() {
        inSet = new AnimatorSet();
        ObjectAnimator animator = ObjectAnimator.ofFloat(null, "rotationY", -180f, 0f);
        animator.setDuration(500);
        animator.setStartDelay(0);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(null, "alpha", 0.0f, 1f);
        animator2.setStartDelay(250);
        animator2.setDuration(0);
        inSet.playTogether(animator, animator2);
//        inSet.addListener(listenerAdapter);

        outSet = new AnimatorSet();
        ObjectAnimator animator_ = ObjectAnimator.ofFloat(null, "rotationY", 0, 180f);
        animator_.setDuration(500);
        animator_.setStartDelay(0);
        ObjectAnimator animator2_ = ObjectAnimator.ofFloat(null, "alpha", 1f, 0f);
        animator2_.setStartDelay(250);
        animator2_.setDuration(0);
        outSet.playTogether(animator_, animator2_);
//        outSet.addListener(animatorListenerAdapter);
    }

//
//    private AnimatorListenerAdapter listenerAdapter = new AnimatorListenerAdapter() {
//
//        @Override
//        public void onAnimationEnd(Animator animation) {
//            super.onAnimationEnd(animation);
//            currentIndex++;
//            setArrayView();
//        }
//    };
}