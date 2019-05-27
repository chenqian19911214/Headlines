package com.marksixinfo.widgets;

import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.marksixinfo.utils.UIUtils;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;

/**
 * @Auther: Administrator
 * @Date: 2019/3/21 0021 20:43
 * @Description:
 */
public class MarkSixNavigatorAdapter extends CommonNavigatorAdapter {


    private List<String> mTitles = new ArrayList<>();
    private ViewPager mViewPager;
    private int normalColor = 0xff333333;//title未选中的颜色
    private int selectedColor = 0xfffc5c66;//title选中的颜色
    private int indicatorColor = selectedColor;//指示条颜色
    private int indicatorHeight;//指示条高度
    private int mTextSize = 16;//默认字体大小
    private int YOffset = 0;//底边偏移
    /**
     * 指示器宽度模式
     * {@link LinePagerIndicator}
     */
    private int indicatorWidthMode = LinePagerIndicator.MODE_WRAP_CONTENT;
    //文本显示单行
    private boolean isSingleLine = true;


    public MarkSixNavigatorAdapter(List<String> mTitles, ViewPager mViewPager) {
        this.mTitles = mTitles;
        this.mViewPager = mViewPager;
    }

    public MarkSixNavigatorAdapter(List<String> mTitles, ViewPager mViewPager, int indicatorHeight, int indicatorWidthMode) {
        this.mTitles = mTitles;
        this.mViewPager = mViewPager;
        this.indicatorHeight = indicatorHeight;
        this.indicatorWidthMode = indicatorWidthMode;
    }

    public MarkSixNavigatorAdapter(List<String> mTitles, ViewPager mViewPager, int normalColor,
                                   int selectedColor, int indicatorColor, int indicatorWidthMode) {
        this.mTitles = mTitles;
        this.mViewPager = mViewPager;
        this.indicatorWidthMode = indicatorWidthMode;
        if (normalColor > 0) {
            this.normalColor = normalColor;
        }
        if (selectedColor > 0) {
            this.selectedColor = selectedColor;
        }
        if (indicatorColor > 0) {
            this.indicatorColor = indicatorColor;
        }
    }

    @Override
    public int getCount() {
        return mTitles == null ? 0 : mTitles.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        SimplePagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
        simplePagerTitleView.setText(mTitles != null && mTitles.size() > 0 ? mTitles.get(index) : "");
        simplePagerTitleView.setSingleLine(isSingleLine);
        simplePagerTitleView.setTextSize(mTextSize);
        int padding = UIUtil.dip2px(context, 5);
        simplePagerTitleView.setPadding(padding, 0, padding, 0);
        simplePagerTitleView.setNormalColor(normalColor);
        simplePagerTitleView.setSelectedColor(selectedColor);
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(index);
                }
            }
        });

        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {

        LinePagerIndicator indicator = new LinePagerIndicator(context);
//                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        indicator.setStartInterpolator(new AccelerateInterpolator());
        indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
//        indicator.setLineHeight(UIUtil.dip2px(context, 5));
        indicator.setRoundRadius(UIUtil.dip2px(context, 2));
        indicator.setYOffset(UIUtils.dip2px(context, YOffset));
//                indicator.setColors(Color.WHITE);
//        LinePagerIndicator indicator = new LinePagerIndicator(context);
//        indicator.setStartInterpolator(new AccelerateInterpolator());
//        indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
        indicator.setLineHeight(UIUtil.dip2px(context, indicatorHeight));
        indicator.setMode(indicatorWidthMode);
        indicator.setColors(indicatorColor);
        return indicator;
    }

    public boolean isSingleLine() {
        return isSingleLine;
    }

    public MarkSixNavigatorAdapter setSingleLine(boolean singleLine) {
        isSingleLine = singleLine;
        return this;
    }

    public MarkSixNavigatorAdapter setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
        return this;
    }

    public MarkSixNavigatorAdapter setYOffset(int YOffset) {
        this.YOffset = YOffset;
        return this;
    }
}

