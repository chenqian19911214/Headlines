package com.marksixinfo.widgets;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.MoneyUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;


/**
 * 数字增加动画的　TextView
 * Created by bakumon on 16-11-26.
 */
@SuppressLint("AppCompatCustomView")
public class NumberAnimTextView extends TextView {

    private String mNumStart = "0.0";  // 起始值 默认 0
    private String mNumEnd; // 结束值
    private long mDuration = 1000; // 动画总时间 默认 2000 毫秒
    private String mPrefixString = ""; // 前缀
    private String mPostfixString = ""; // 后缀
    private boolean isEnableAnim = true; // 是否开启动画
    private int fractionNumber;
    private boolean setTestSize;
    boolean IsThousands = true;
    String format = "######0.00";
    private ValueAnimator animator;


    public boolean isThousands() {
        return IsThousands;
    }


    private int AbsoluteSizeSpan = 14;

    private SpannableString spannableString;

    public NumberAnimTextView(Context context) {
        super(context);
    }

    public NumberAnimTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumberAnimTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setNumberString(String number) {
        setNumberString("0", number);
    }

    public void setNumberString(String numberStart, String numberEnd) {
        mNumStart = numberStart;
        mNumEnd = numberEnd;
        if (checkNumString(numberStart, numberEnd)) {
            // 数字合法　开始数字动画
            start();
        } else {
            // 数字不合法　直接调用　setText　设置最终值
            setText(mPrefixString + numberEnd + mPostfixString);
        }
    }

    public void setNumberString(String number, ValueAnimator.AnimatorUpdateListener valueAnimator) {
        setNumberString("0", number, valueAnimator);
    }

    public void setNumberString(String numberStart, String numberEnd, ValueAnimator.AnimatorUpdateListener valueAnimator) {
        mNumStart = numberStart;
        mNumEnd = numberEnd;
        if (checkNumString(numberStart, numberEnd)) {
            // 数字合法　开始数字动画
            start(valueAnimator);
        } else {
            // 数字不合法　直接调用　setText　设置最终值
            setText(mPrefixString + numberEnd + mPostfixString);
        }
    }


    public void setEnableAnim(boolean enableAnim) {
        isEnableAnim = enableAnim;
    }

    public void setDuration(long mDuration) {
        this.mDuration = mDuration;
    }

    public void setPrefixString(String mPrefixString) {
        this.mPrefixString = mPrefixString;
    }

    public void setPostfixString(String mPostfixString) {
        this.mPostfixString = mPostfixString;
    }

    public void setTextsize(boolean setTestSize) {
        this.setTestSize = setTestSize;
    }

    public void setFractionDigits(int fractionNumber) {
        this.fractionNumber = fractionNumber;
    }

    public void setHadThousands(boolean IsThousands) {
        this.IsThousands = IsThousands;
    }

    private boolean isInt; // 是否是整数

    /**
     * 校验数字的合法性
     *
     * @param numberStart 　开始的数字
     * @param numberEnd   　结束的数字
     * @return 合法性
     */
    private boolean checkNumString(String numberStart, String numberEnd) {
        if (!CommonUtils.StringNotNull(numberEnd)) {
            return false;
        }
        String regexInteger = "-?\\d*";
        isInt = numberEnd.matches(regexInteger) && numberStart.matches(regexInteger);
        if (isInt) {
            BigInteger start = new BigInteger(numberStart);
            BigInteger end = new BigInteger(numberEnd);
            return end.compareTo(start) >= 0;
        }
        String regexDecimal = "-?[1-9]\\d*.\\d*|-?0.\\d*[1-9]\\d*";
        if ("0".equals(numberStart)) {
            if (numberEnd.matches(regexDecimal)) {
                BigDecimal start = new BigDecimal(numberStart);
                BigDecimal end = new BigDecimal(numberEnd);
                return end.compareTo(start) > 0;
            }
        }
        if (numberEnd.matches(regexDecimal) && numberStart.matches(regexDecimal)) {
            BigDecimal start = new BigDecimal(numberStart);
            BigDecimal end = new BigDecimal(numberEnd);
            return end.compareTo(start) > 0;
        }
        return false;
    }

    private void start() {
        start(null);
    }

    private void start(ValueAnimator.AnimatorUpdateListener valueAnimator) {
        if (!isEnableAnim) { // 禁止动画
            setText(mPrefixString + format(new BigDecimal(mNumEnd)) + mPostfixString);
            return;
        }
        animator = ValueAnimator.ofObject(new BigDecimalEvaluator(), new BigDecimal(mNumStart), new BigDecimal(mNumEnd));
        animator.setDuration(mDuration);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        if (valueAnimator != null) {
            animator.addUpdateListener(valueAnimator);
        } else {
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    BigDecimal value = (BigDecimal) valueAnimator.getAnimatedValue();

                    if (setTestSize) {
                        SpannableString yss = new SpannableString(mPrefixString + format(value) + mPostfixString);
                        yss.setSpan(new AbsoluteSizeSpan(AbsoluteSizeSpan, true), yss.length() - 1, yss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        setText(yss);
                    } else {
                        setText(mPrefixString + format(value) + mPostfixString);
                    }
                }
            });
        }
        animator.start();
    }

    public ValueAnimator getAnimator() {
        return animator;
    }

    public String getRealText(BigDecimal value) {
        return mPrefixString + format(value) + mPostfixString;
    }

    public String getRealTextEco(BigDecimal value) {
        return MoneyUtils.getIntMoneyText(format(value));
    }


    public void setSpannableString(SpannableString spannableString) {
        if (spannableString != null) {
            setText(spannableString);
        }
    }

    public int getAbsoluteSizeSpan() {
        return AbsoluteSizeSpan;
    }

    public void setAbsoluteSizeSpan(int absoluteSizeSpan) {
        AbsoluteSizeSpan = absoluteSizeSpan;
    }

    /**
     * 格式化 BigDecimal ,小数部分时保留两位小数并四舍五入
     *
     * @param bd 　BigDecimal
     * @return 格式化后的 String
     */
    private String format(BigDecimal bd) {
        if (bd == null) {
            return "";
        }
        if (IsThousands) {
            NumberFormat format = NumberFormat.getInstance();
            format.setMinimumFractionDigits(fractionNumber);   // 小数最大的位数
            format.setMaximumFractionDigits(fractionNumber);
            format.setMinimumIntegerDigits(1);

            return format.format(bd);
        } else {
            if (0 == fractionNumber) {
                return bd.longValue() + "";
            } else {
                DecimalFormat df = new DecimalFormat(format);
                return /* bd.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue()*/ df.format(bd.doubleValue()) + "";
            }
        }

    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
