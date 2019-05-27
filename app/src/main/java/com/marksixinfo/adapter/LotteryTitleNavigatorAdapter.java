package com.marksixinfo.adapter;

import android.content.Context;
import android.view.View;

import com.marksixinfo.bean.LotteryTitleBean;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.TitleScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;

/**
 * 开奖滑动Indicator
 *
 * @Auther: Administrator
 * @Date: 2019/5/13 0013 14:19
 * @Description:
 */
public class LotteryTitleNavigatorAdapter extends CommonNavigatorAdapter {


    private List<LotteryTitleBean> mTitles = new ArrayList<>();
    private ViewPager mViewPager;
    private int normalColor = 0xff999999;//title未选中的颜色
    private int selectedColor = 0xffFC5C66;//title选中的颜色
    private SucceedCallBackListener<Integer> listener;

    public LotteryTitleNavigatorAdapter(List<LotteryTitleBean> mTitles, ViewPager mViewPager, SucceedCallBackListener<Integer> listener) {
        this.mTitles = mTitles;
        this.mViewPager = mViewPager;
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return mTitles == null ? 0 : mTitles.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        TitleScaleTransitionPagerTitleView simplePagerTitleView = new TitleScaleTransitionPagerTitleView(context);
        if (mTitles != null && mTitles.size() > 0) {
            LotteryTitleBean lotteryTitleBean = mTitles.get(index);
            if (null != lotteryTitleBean) {
                simplePagerTitleView.setText(lotteryTitleBean.getTitleName());
                simplePagerTitleView.setmSelectedImg(lotteryTitleBean.getSelectedId());
                simplePagerTitleView.setmNormalImg(lotteryTitleBean.getNorId());
            }
        }
        simplePagerTitleView.setMinScale(0.9f);
        simplePagerTitleView.setTextSize(15);
        int viewWidth = UIUtils.getViewWidth(simplePagerTitleView) + simplePagerTitleView.getContentRight();
        simplePagerTitleView.setPadding((int) (viewWidth / 2f), 0, (int) (viewWidth / 2f), 0);

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

        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index == mViewPager.getCurrentItem() && listener != null) {
                    listener.succeedCallBack(index);
                }
                mViewPager.setCurrentItem(index);
            }
        });

        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        return null;
    }

}