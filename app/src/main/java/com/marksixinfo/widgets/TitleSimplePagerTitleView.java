package com.marksixinfo.widgets;

import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marksixinfo.utils.UIUtils;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IMeasurablePagerTitleView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

/**
 * 开奖 IndicatorTitle
 *
 * @Auther: Administrator
 * @Date: 2019/5/13 0013 14:27
 * @Description:
 */
public class TitleSimplePagerTitleView extends LinearLayout implements IMeasurablePagerTitleView {


    protected int mSelectedColor;
    protected int mNormalColor;
    protected int mSelectedImg;
    protected int mNormalImg;
    protected Context context;
    private TextView textView;
    private ImageView imageView;


    public TitleSimplePagerTitleView(Context context) {
        super(context, null);
        this.context = context;
        getTextView(context);
        getImageView(context);
        init();
    }

    private void getImageView(Context context) {
        imageView = new ImageView(context);
        addView(imageView);
        UIUtils.setMargins(context, imageView, 3, 0, 0, 0);
    }

    private void getTextView(Context context) {
        textView = new TextView(context);
        textView.setSingleLine();
        addView(textView);
    }


    public void setText(CharSequence text) {
        if (textView != null) {
            textView.setText(text);
        }
    }

    public void setImageResource(@DrawableRes int resId) {
        if (imageView != null) {
            imageView.setImageResource(resId);
        }
    }

    public void setTextColor(@ColorInt int color) {
        if (textView != null) {
            textView.setTextColor(color);
        }
    }

    public void setTextSize(float size) {
        if (textView != null) {
            textView.setTextSize(size);
        }
    }

    private void init() {
        setGravity(Gravity.CENTER);
    }

    @Override
    public void onSelected(int index, int totalCount) {
        setTextColor(mSelectedColor);
        setImageResource(mSelectedImg);
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        setTextColor(mNormalColor);
        setImageResource(mNormalImg);
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
    }

    @Override
    public int getContentLeft() {
        return getLeft();
    }

    @Override
    public int getContentTop() {
        return getTop();
    }

    @Override
    public int getContentRight() {
        return getRight();
    }

    @Override
    public int getContentBottom() {
        return getBottom();
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        mSelectedColor = selectedColor;
    }

    public int getNormalColor() {
        return mNormalColor;
    }

    public void setNormalColor(int normalColor) {
        mNormalColor = normalColor;
    }

    public void setmSelectedImg(int mSelectedImg) {
        this.mSelectedImg = mSelectedImg;
    }

    public void setmNormalImg(int mNormalImg) {
        this.mNormalImg = mNormalImg;
    }
}

