package com.marksixinfo.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.PagerBaseAdapter;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.base.ViewPagerHelper;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.interfaces.IConcernAndHistory;
import com.marksixinfo.ui.fragment.ConcernAndHistoryFragment;
import com.marksixinfo.ui.fragment.ConcernCommentFragment;
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
 * 功能描述: 我的收藏/评论/点赞/历史
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/4/19 0019 15:13
 */
public class MineConcernAndHistoryActivity extends MarkSixWhiteActivity implements View.OnClickListener {


    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.cvp_view_pager)
    ViewPager mViewPager;

    private static final String[] CHANNELS = new String[]{"收藏", "评论", "点赞", "历史"};
    private List<PageBaseFragment> mDataList = new ArrayList<>();

    private TextView tvFunction;
    private int type;//position 0,收藏 1,评论 2,点赞 3,历史
    private int parentType;//0,头条 1,论坛
    private boolean isEdit;//是否编辑状态


    @Override
    public int getViewId() {
        return R.layout.activity_mine_concern_and_history;
    }

    @Override
    public void afterViews() {
        tvFunction = markSixTitleWhite.getTvFunction();
        markSixTitleWhite.init("", "", "编辑", 0, this);

        parentType = NumberUtils.stringToInt(getStringParam(StringConstants.PARENT_TYPE));
        type = NumberUtils.stringToInt(getStringParam(StringConstants.TYPE));


        mDataList.clear();
        mDataList.add(getContentFragment(1));
        mDataList.add(getCommentFragment(2));
        mDataList.add(getContentFragment(3));
        mDataList.add(getContentFragment(4));

        PagerBaseAdapter pagerBaseAdapter = new PagerBaseAdapter(getSupportFragmentManager(), mDataList);

        mViewPager.setAdapter(pagerBaseAdapter);
        mViewPager.setOffscreenPageLimit(mDataList.size());


        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            int currentPosition;

            @Override
            public void onPageSelected(int position) {
                mSwipeBackLayout.setSwipeFromEdge(position != 0);
                if (currentPosition != position && isEdit) {
                    clickEdit(currentPosition);
                }
                CommonUtils.setInitFragmentData(mDataList, position);
                currentPosition = position;
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_function://编辑
                clickEdit(mViewPager.getCurrentItem());
                break;
        }
    }

    /**
     * 点击编辑
     */
    public void clickEdit(int index) {
        isEdit = !isEdit;
        tvFunction.setText(isEdit ? "取消" : "编辑");
        if (index >= 0) {
            IConcernAndHistory iConcernAndHistory = (IConcernAndHistory) mDataList.get(index);
            if (iConcernAndHistory != null) {
                iConcernAndHistory.isEdit(isEdit);
            }
        }
    }


    /**
     * 设置是否可以编辑
     *
     * @param isEnabled
     */
    public void setEditEnabled(int type, boolean isEnabled) {
        if (mViewPager.getCurrentItem() == type - 1) {
            tvFunction.setTextColor(isEnabled ? 0xff333333 : 0xffdddddd);
            tvFunction.setOnClickListener(isEnabled ? this : null);
        }
    }

    /**
     * 获取收藏/点赞/历史
     *
     * @return
     */
    private ConcernAndHistoryFragment getContentFragment(int type) {
        ConcernAndHistoryFragment concernAndHistoryFragment = new ConcernAndHistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(StringConstants.TYPE, type);
        bundle.putInt(StringConstants.PARENT_TYPE, parentType);
        concernAndHistoryFragment.setArguments(bundle);
        return concernAndHistoryFragment;
    }

    /**
     * 获取评论
     *
     * @return
     */
    private ConcernCommentFragment getCommentFragment(int type) {
        ConcernCommentFragment concernCommentFragment = new ConcernCommentFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(StringConstants.TYPE, type);
        bundle.putInt(StringConstants.PARENT_TYPE, parentType);
        concernCommentFragment.setArguments(bundle);
        return concernCommentFragment;
    }
}
