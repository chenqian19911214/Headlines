package com.marksixinfo.ui.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.PagerBaseAdapter;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.base.ViewPagerHelper;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.ui.fragment.ConcernAndFansFragment;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.NumberUtils;
import com.marksixinfo.widgets.CommonNavigator;
import com.marksixinfo.widgets.MarkSixNavigatorAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;


/**
 * 功能描述: 我的关注与粉丝
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/4/19 0019 15:10
 */
public class MineConcernAndFansActivity extends MarkSixWhiteActivity {


    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.cvp_view_pager)
    ViewPager mViewPager;

    private static final String[] CHANNELS = new String[]{"关注", "粉丝"};
    private List<PageBaseFragment> mDataList = new ArrayList<>();


    private int parentType;//0,头条 1,论坛
    private int type;//0,关注 1,粉丝
    private TextView tvTitle;

    @Override
    public int getViewId() {
        return R.layout.activity_mine_concern_and_fans;
    }

    @Override
    public void afterViews() {
        markSixTitleWhite.init("");
        tvTitle = markSixTitleWhite.getTvTitle();


        parentType = NumberUtils.stringToInt(getStringParam(StringConstants.PARENT_TYPE));
        type = NumberUtils.stringToInt(getStringParam(StringConstants.TYPE));

        mDataList.clear();
        mDataList.add(getContentFragment(1));
        mDataList.add(getContentFragment(2));

        PagerBaseAdapter pagerBaseAdapter = new PagerBaseAdapter(getSupportFragmentManager(), mDataList);

        mViewPager.setAdapter(pagerBaseAdapter);
        mViewPager.setOffscreenPageLimit(mDataList.size());


        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mSwipeBackLayout.setSwipeFromEdge(position != 0);
                CommonUtils.setInitFragmentData(mDataList, position);
                setTitleText(position);
            }
        });


        assert getContext() != null;
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        MarkSixNavigatorAdapter markSixNavigatorAdapter = new MarkSixNavigatorAdapter(Arrays.asList(CHANNELS),
                mViewPager, 2, LinePagerIndicator.MODE_WRAP_CONTENT);
        commonNavigator.setAdapter(markSixNavigatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);


        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(type);
                CommonUtils.setInitFragmentData(mDataList, type);
                setTitleText(type);
            }
        });
    }


    /**
     * 动态设置标题
     * @param position
     */
    private void setTitleText(int position) {
        if (tvTitle != null) {
            tvTitle.setText("我的" + CHANNELS[position]);
        }
    }


    /**
     * 获取关注/粉丝
     *
     * @return
     */
    private ConcernAndFansFragment getContentFragment(int type) {
        ConcernAndFansFragment concernAndFansFragment = new ConcernAndFansFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(StringConstants.PARENT_TYPE, parentType);
        bundle.putInt(StringConstants.TYPE, type);
        concernAndFansFragment.setArguments(bundle);
        return concernAndFansFragment;
    }


}
