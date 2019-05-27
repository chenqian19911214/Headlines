package com.marksixinfo.ui.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.PagerBaseAdapter;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.base.ViewPagerHelper;
import com.marksixinfo.bean.GalleryPeriodData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.net.impl.GalleryImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.CommRedTitle;
import com.marksixinfo.widgets.CommonNavigator;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.MarkSixNavigatorAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;

/**
 * 图库
 *
 * @Auther: Administrator
 * @Date: 2019/3/15 0015 12:18
 * @Description:
 */
public class GalleryFragment2 extends PageBaseFragment implements View.OnClickListener {


    @BindView(R.id.magic_indicator)
    MagicIndicator indicator;
    @BindView(R.id.cvp_view_pager)
    ViewPager viewPager;
    @BindView(R.id.marksix_title)
    CommRedTitle markSixTitle;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;

//        private static final String[] CHANNELS = new String[]{"我的收藏", "彩色图库", "黑白图库"};
    private List<String> category = new ArrayList<>();
    private List<PageBaseFragment> mDataList = new ArrayList<>();

    @Override
    public int getViewId() {
        return R.layout.fragment_gallery2;
    }

    @Override
    protected void afterViews() {
        TextView tvTitle = markSixTitle.getTvTitle();
        tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
        markSixTitle.init("火山图库", false, "", "",
                R.drawable.icon_main_search, this);

        mLoadingLayout.setRetryListener(this);

//        mDataList.clear();
//        mDataList.add(getContentFragment(0));
//        mDataList.add(getContentFragment(1));
//        mDataList.add(getContentFragment(2));
//
//        PagerBaseAdapter pagerBaseAdapter = new PagerBaseAdapter(getChildFragmentManager(), mDataList);
//
//        mViewPager.setAdapter(pagerBaseAdapter);
//        mViewPager.setOffscreenPageLimit(mDataList.size());
//
//
//        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
//            @Override
//            public void onPageSelected(int position) {
//                CommonUtils.setInitFragmentData(mDataList, position);
//            }
//        });
//
//
//        assert getContext() != null;
//        CommonNavigator commonNavigator = new CommonNavigator(getContext());
//        MarkSixNavigatorAdapter markSixNavigatorAdapter = new MarkSixNavigatorAdapter(Arrays.asList(CHANNELS),
//                mViewPager, 2, LinePagerIndicator.MODE_WRAP_CONTENT);
//        commonNavigator.setAdapter(markSixNavigatorAdapter);
//        magicIndicator.setNavigator(commonNavigator);
//        ViewPagerHelper.bind(magicIndicator, mViewPager);
//
//
//        mViewPager.post(new Runnable() {
//            @Override
//            public void run() {
//                mViewPager.setCurrentItem(0);
//                CommonUtils.setInitFragmentData(mDataList, 0);
//            }
//        });
    }

    /**
     * 获取分类及期数
     */
    private void getCategory() {
        new GalleryImpl(new MarkSixNetCallBack<GalleryPeriodData>(this, GalleryPeriodData.class) {
            @Override
            public void onSuccess(GalleryPeriodData response, int id) {
                mLoadingLayout.showContent();
                setTitleAndFragment(response);
            }

            @Override
            public void onError(String msg, String code) {
                mLoadingLayout.showError();
            }
        }.setNeedDialog(false)).getGalleryMainPeriod();
    }


    /**
     * 设置分类标题及让Fragment
     *
     * @param response
     */
    private void setTitleAndFragment(GalleryPeriodData response) {
        if (response != null) {
            category.clear();
            mDataList.clear();
            String period = response.getPeriod();
//            List<GalleryPeriodData.TypeBean> type = response.getType();
//            if (!CommonUtils.ListNotNull(type)) {
            List<GalleryPeriodData.TypeBean> type = new ArrayList<>();
//            }
            type.add(0, new GalleryPeriodData.TypeBean(1, "我的收藏"));
            type.add(new GalleryPeriodData.TypeBean(3, "彩色图库"));
            type.add(new GalleryPeriodData.TypeBean(2, "黑白图库"));
            for (GalleryPeriodData.TypeBean typeBean : type) {
                if (typeBean != null) {
                    category.add(typeBean.getName());
                    int id = typeBean.getId();
                    GalleryListFragment galleryListFragment = new GalleryListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString(StringConstants.PERIOD, period);
                    bundle.putInt(StringConstants.TYPE, id);
                    galleryListFragment.setArguments(bundle);
                    mDataList.add(galleryListFragment);
                }
            }
            CollectionListFragment collectionListFragmentv =  new CollectionListFragment();
            Bundle bundle = new Bundle();
            bundle.putString(StringConstants.PERIOD, period);
           // bundle.putInt(StringConstants.TYPE, id);
            collectionListFragmentv.setArguments(bundle);
            mDataList.set(0,collectionListFragmentv);

            PagerBaseAdapter pagerBaseAdapter = new PagerBaseAdapter(getChildFragmentManager(), mDataList);
            viewPager.setAdapter(pagerBaseAdapter);
            viewPager.setOffscreenPageLimit(mDataList.size());
            MarkSixNavigatorAdapter markSixNavigatorAdapter = new MarkSixNavigatorAdapter(category, viewPager, 2,
                    LinePagerIndicator.MODE_WRAP_CONTENT);
            CommonNavigator commonNavigator = new CommonNavigator(getContext());
            commonNavigator.setAdapter(markSixNavigatorAdapter);

            indicator.setNavigator(commonNavigator);
            ViewPagerHelper.bind(indicator, viewPager);

            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

                public void onPageSelected(int position) {
                    CommonUtils.setInitFragmentData(mDataList, position);
                }
            });

            viewPager.post(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(1, false);
                    CommonUtils.setInitFragmentData(mDataList, 1);
                }
            });
        }
    }

    @Override
    protected void loadData() {
        getCategory();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_function://搜索
                startClass(R.string.GallerySearchActivity);
                break;
            case R.id.retry_button://网络错误
                getCategory();
                break;
        }
    }
}
