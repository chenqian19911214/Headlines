package com.marksixinfo.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.adapter.PagerBaseAdapter;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.base.ViewPagerHelper;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.constants.UrlStaticConstants;
import com.marksixinfo.ui.fragment.IncomeRecordFragment;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.NumberUtils;
import com.marksixinfo.widgets.CommonNavigator;
import com.marksixinfo.widgets.MarkSixNavigatorAdapter;
import com.marksixinfo.widgets.NumberAnimTextView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * 功能描述: 收益记录
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/4/19 0019 15:03
 */
public class IncomeRecordActivity extends MarkSixWhiteActivity implements View.OnClickListener {


    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.cvp_view_pager)
    ViewPager mViewPager;

    private static final String[] CHANNELS = new String[]{"金币收益", "现金收益"};
    private List<PageBaseFragment> mDataList = new ArrayList<>();
    private int type;//0,金币收益 1,现金收益

    @Override
    public int getViewId() {
        return R.layout.activity_income_record;
    }

    @Override
    public void afterViews() {

        markSixTitleWhite.init("我的收益", "", "", R.drawable.icon_explain_title_right, this);

        type = NumberUtils.stringToInt(getStringParam(StringConstants.TYPE));

        mDataList.clear();
        mDataList.add(getContentFragment(0));
        mDataList.add(getContentFragment(1));

        PagerBaseAdapter pagerBaseAdapter = new PagerBaseAdapter(getSupportFragmentManager(), mDataList);

        mViewPager.setAdapter(pagerBaseAdapter);
        mViewPager.setOffscreenPageLimit(mDataList.size());


        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mSwipeBackLayout.setSwipeFromEdge(position != 0);
                CommonUtils.setInitFragmentData(mDataList, position);
            }
        });


        assert getContext() != null;
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        MarkSixNavigatorAdapter markSixNavigatorAdapter = new MarkSixNavigatorAdapter(Arrays.asList(CHANNELS), mViewPager, 2,
                LinePagerIndicator.MODE_WRAP_CONTENT);
        commonNavigator.setAdapter(markSixNavigatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, mViewPager);


        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(type);
                CommonUtils.setInitFragmentData(mDataList, type);
            }
        });
    }


    /**
     * 获取关注/粉丝
     *
     * @return
     */
    private IncomeRecordFragment getContentFragment(int type) {
        IncomeRecordFragment incomeRecordFragment = new IncomeRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(StringConstants.TYPE, type);
        incomeRecordFragment.setArguments(bundle);
        return incomeRecordFragment;
    }


    @Override
    public void finish() {
        try {
            if (CommonUtils.ListNotNull(mDataList)) {
                IncomeRecordFragment incomeRecordFragment =
                        (IncomeRecordFragment) mDataList.get(mViewPager.getCurrentItem());
                if (incomeRecordFragment != null) {
                    NumberAnimTextView tvCashReserve = incomeRecordFragment.getTvCashReserve();
                    if (tvCashReserve != null && tvCashReserve.getAnimator() != null) {
                        tvCashReserve.getAnimator().cancel();

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            super.finish();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_function://说明
                startClass(R.string.WebViewActivity, IntentUtils.getHashObj(new String[]{
                        StringConstants.URL, UrlStaticConstants.EARNINGS_EXPLAIN}));
                break;
        }
    }
}
