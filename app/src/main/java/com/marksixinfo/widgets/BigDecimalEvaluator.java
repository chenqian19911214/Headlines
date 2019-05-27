package com.marksixinfo.widgets;

import android.animation.TypeEvaluator;

import java.math.BigDecimal;

/**
 * @Auther: Administrator
 * @Date: 2019/5/3 0003 14:47
 * @Description:
 */
public class BigDecimalEvaluator implements TypeEvaluator {
    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        BigDecimal start = (BigDecimal) startValue;
        BigDecimal end = (BigDecimal) endValue;
        BigDecimal result = end.subtract(start);
        return result.multiply(new BigDecimal("" + fraction)).add(start);
    }
}
