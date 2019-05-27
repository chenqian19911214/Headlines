package com.marksixinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.evenbus.MainClickIndexEvent;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.NavigationBarUtil;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.CommonPagerTitleView;

import java.util.List;

import androidx.viewpager.widget.ViewPager;

/**
 * 首页tab
 *
 * @Auther: Administrator
 * @Date: 2019/4/3 0003 19:17
 * @Description:
 */
public class MainNavigatorAdapter extends CommonNavigatorAdapter {

    private List<PageBaseFragment> mDataList;
    private int[] IMAGES;
    private String[] CHANNELS;
    private CommonNavigator commonNavigator;
    private ViewPager mViewPager;

    public MainNavigatorAdapter(List<PageBaseFragment> mDataList, int[] IMAGES,
                                String[] CHANNELS, CommonNavigator commonNavigator, ViewPager mViewPager) {
        this.mDataList = mDataList;
        this.IMAGES = IMAGES;
        this.CHANNELS = CHANNELS;
        this.commonNavigator = commonNavigator;
        this.mViewPager = mViewPager;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    /**
     * 设置文字
     *
     * @param context
     * @param index
     * @return
     */
    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        CommonPagerTitleView commonPagerTitleView = new CommonPagerTitleView(context);

        // load custom layout
        View customLayout = LayoutInflater.from(context).inflate(R.layout.simple_pager_title_layout, null);
        final ImageView titleImg = customLayout.findViewById(R.id.title_img);
        final TextView titleText = customLayout.findViewById(R.id.title_text);
        titleImg.setImageDrawable(context.getResources().getDrawable(IMAGES[index]));
        titleText.setText(CHANNELS[index]);
        commonPagerTitleView.setContentView(customLayout);

        commonPagerTitleView.setOnPagerTitleChangeListener(new CommonPagerTitleView.OnPagerTitleChangeListener() {

            @Override
            public void onSelected(int index, int totalCount) {
                titleText.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                selectIndex(index);
            }

            @Override
            public void onDeselected(int index, int totalCount) {
                titleText.setTextColor(0xff999999);
            }

            @Override
            public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
                //缩放效果
//                        titleImg.setScaleX(1.3f + (0.8f - 1.3f) * leavePercent);
//                        titleImg.setScaleY(1.3f + (0.8f - 1.3f) * leavePercent);
            }

            @Override
            public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
                //缩放效果
//                        titleImg.setScaleX(0.8f + (1.3f - 0.8f) * enterPercent);
//                        titleImg.setScaleY(0.8f + (1.3f - 0.8f) * enterPercent);
            }
        });

        commonPagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationBarUtil.usableHeightView = 0;
                if (index == mViewPager.getCurrentItem()) {
                    EventBusUtil.post(new MainClickIndexEvent(index));
                }
                mViewPager.setCurrentItem(index);
            }
        });

        return commonPagerTitleView;
    }

    /**
     * 不需要指示器
     *
     * @param context
     * @return
     */
    @Override
    public IPagerIndicator getIndicator(Context context) {
        return null;
    }

    /**
     * 功能描述: 设置选中
     *
     * @param: 选中index
     * @auther: Administrator
     * @date: 2019/3/15 0015 下午 3:40
     */
    private void selectIndex(int index) {
        if (commonNavigator != null) {
            LinearLayout titleContainer = commonNavigator.getTitleContainer();
            if (titleContainer != null) {
                if (titleContainer.getChildCount() > 0) {
                    for (int i = 0; i < titleContainer.getChildCount(); i++) {
                        View view = titleContainer.getChildAt(i);
                        if (view != null) {
                            view.setSelected(false);
                        }
                    }
                    titleContainer.getChildAt(index).setSelected(true);
                }
            }
        }
    }
}
