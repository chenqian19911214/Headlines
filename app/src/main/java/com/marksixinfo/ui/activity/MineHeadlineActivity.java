package com.marksixinfo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.adapter.PagerBaseAdapter;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.base.ViewPagerHelper;
import com.marksixinfo.bean.UserPostCenterCategory;
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.ui.fragment.MineHeadlineFragment;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.CommonNavigator;
import com.marksixinfo.widgets.MarkSixNavigatorAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 个人中心,我的头条
 *
 * @auther: Administrator
 * @date: 2019/3/27 0027 12:56
 */
public class MineHeadlineActivity extends MarkSixWhiteActivity {

    @BindView(R.id.indicator)
    MagicIndicator indicator;
    @BindView(R.id.viewpager)
    ViewPager viewpager;


    private List<PageBaseFragment> mDataList = new ArrayList<>();
    private List<String> classify = new ArrayList<>();
    private List<UserPostCenterCategory> category;

    @Override
    public int getViewId() {
        return R.layout.activity_mine_headline;
    }

    @Override
    public void afterViews() {

        markSixTitleWhite.init("我的头条");

        getCategoryData("");
    }

    /**
     * 获取分类
     */
    private void getCategoryData(String classifyId) {
        new HeadlineImpl(new MarkSixNetCallBack<List<UserPostCenterCategory>>(this, UserPostCenterCategory.class) {
            @Override
            public void onSuccess(List<UserPostCenterCategory> response, int id) {
                setData(response, classifyId);
            }
        }.setNeedDialog(false)).mineInvitationCategory();
    }

    /**
     * 设置分类及Fragment
     *
     * @param response
     */
    private void setData(List<UserPostCenterCategory> response, String classifyId) {
        if (!CommonUtils.ListNotNull(response)) {
            response = new ArrayList<>();
        }
        int index = setTitlesAndData(response, classifyId);
        PagerBaseAdapter pagerBaseAdapter = new PagerBaseAdapter(getSupportFragmentManager(), mDataList);
        viewpager.setAdapter(pagerBaseAdapter);
        viewpager.setOffscreenPageLimit(mDataList.size());
        MarkSixNavigatorAdapter markSixNavigatorAdapter = new MarkSixNavigatorAdapter(classify, viewpager, 0,
                LinePagerIndicator.MODE_WRAP_CONTENT);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(markSixNavigatorAdapter);

        indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicator, viewpager);

        viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mSwipeBackLayout.setSwipeFromEdge(position != 0);
                CommonUtils.setInitFragmentData(mDataList, position);
            }
        });


        viewpager.post(new Runnable() {
            @Override
            public void run() {
                viewpager.setCurrentItem(index, false);
                CommonUtils.setInitFragmentData(mDataList, index);
            }
        });

    }


    /**
     * 获取title及Fragment
     *
     * @param category
     */
    private int setTitlesAndData(List<UserPostCenterCategory> category, String classifyId) {
        classify.clear();
        mDataList.clear();
        int index = 0;
        //新增一个全部分类
        category.add(0, new UserPostCenterCategory("0", "全部", "", "", "0"));
        for (int i = 0; i < category.size(); i++) {
            UserPostCenterCategory centerCategory = category.get(i);
            if (centerCategory != null) {
                String id = centerCategory.getId();
                if (CommonUtils.StringNotNull(classifyId)) {
                    if (classifyId.equals(id)) {
                        index = i;
                    }
                }
                classify.add(centerCategory.getName());
                MineHeadlineFragment mineHeadlineFragment = new MineHeadlineFragment();
                Bundle bundle = new Bundle();
                bundle.putString(StringConstants.CATEGROY_ID, id);
                mineHeadlineFragment.setArguments(bundle);
                mDataList.add(mineHeadlineFragment);
            }
        }
        this.category = category;
        return index;
    }

    @OnClick(R.id.iv_add_category)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add_category:
                startActivityForResult(new Intent(this, SelectClassifyActivity.class),
                        NumberConstants.INTENT_CODE_CLASSIFY_SELECT);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case NumberConstants.INTENT_CODE_CLASSIFY_SELECT:
                    if (data != null) {
                        String classifyId = data.getStringExtra(StringConstants.RELEASE_CLASSIFY_SELECT);
                        if (data.getBooleanExtra(StringConstants.INTENT_PARAMS_BOOLEAN, false)) {
                            getCategoryData(classifyId);
                        } else {
                            selectIndex(classifyId);
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 根据类目id获取Fragment位置
     *
     * @param classifyId
     * @return
     */
    private int getIndex(String classifyId) {
        if (CommonUtils.StringNotNull(classifyId) && CommonUtils.ListNotNull(category)) {
            for (int i = 0; i < category.size(); i++) {
                UserPostCenterCategory centerCategory = category.get(i);
                if (centerCategory != null) {
                    String id = centerCategory.getId();
                    if (classifyId.equals(id)) {
                        return i;
                    }
                }
            }
        }
        return -1;
    }


    /**
     * 选择类目选中
     *
     * @param classifyId
     */
    private void selectIndex(String classifyId) {
        int index = getIndex(classifyId);
        if (index >= 0) {
            if (viewpager != null) {
                viewpager.setCurrentItem(index, false);
            }
        }
    }

    /**
     * 选择类目选中,刷新当前
     *
     * @param classifyId
     */
    public void selectIndexAndRefresh(String classifyId) {
        if (viewpager != null && viewpager.getCurrentItem() == 0) {//全部分类进行编辑,需要额外刷新
            if (CommonUtils.ListNotNull(mDataList)) {
                for (PageBaseFragment fragment : mDataList) {
                    if (fragment != null) {
                        fragment.setInit(true);
                    }
                }
            }
        } else {
            try {
                int index = getIndex(classifyId);
                if (index >= 0) {
                    if (CommonUtils.ListNotNull(mDataList)) {
                        PageBaseFragment pageBaseFragment = mDataList.get(index);
                        pageBaseFragment.setInit(true);
                        if (viewpager != null) {
                            viewpager.setCurrentItem(index, false);
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
