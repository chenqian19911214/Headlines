package com.marksixinfo.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.ScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.List;

import androidx.viewpager.widget.ViewPager;

/**
 *  推荐/关注 论坛:关注/广场等
 *
 * @Auther: Administrator
 * @Date: 2019/4/3 0003 21:27
 * @Description:
 */
public class ForumNavigatorAdapter extends CommonNavigatorAdapter {


    private List<PageBaseFragment> mDataList;
    private String[] CHANNELS;
    private ViewPager mViewPager;

    public ForumNavigatorAdapter(List<PageBaseFragment> mDataList, String[] CHANNELS, ViewPager mViewPager) {
        this.mDataList = mDataList;
        this.CHANNELS = CHANNELS;
        this.mViewPager = mViewPager;
    }

    @Override
    public int getCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        ScaleTransitionPagerTitleView titleView = new ScaleTransitionPagerTitleView(context);
        titleView.setText(CHANNELS[index]);
        titleView.setTextSize(20);
        titleView.setNormalColor(Color.WHITE);
        titleView.setSelectedColor(Color.WHITE);
        titleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(index);
            }
        });
        return titleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        indicator.setStartInterpolator(new AccelerateInterpolator());
        indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
        indicator.setLineHeight(UIUtil.dip2px(context, 5));
        indicator.setRoundRadius(UIUtil.dip2px(context, 2));
        indicator.setYOffset(UIUtils.dip2px(context, 2));
        indicator.setColors(Color.WHITE);
        return indicator;
    }
}
