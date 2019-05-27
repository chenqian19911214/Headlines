package com.marksixinfo.ui.activity;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.marksixinfo.R;
import com.marksixinfo.adapter.PagerBaseAdapter;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.base.ViewPagerHelper;
import com.marksixinfo.bean.PeriodData;
import com.marksixinfo.bean.SelectPictureData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.SendGalleryTitle;
import com.marksixinfo.net.impl.GalleryImpl;
import com.marksixinfo.ui.fragment.PictureDetailsFragment;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.CommonNavigator;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.MarkSixNavigatorAdapter;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 图库详情
 *
 * @Auther: Administrator
 * @Date: 2019/5/8 0008 12:27
 * @Description:
 */
public class GalleryDetailActivity extends MarkSixActivity implements View.OnClickListener {

    @BindView(R.id.picture_indicator)
    MagicIndicator picture_indicator;
    @BindView(R.id.picture_view_pager)
    ViewPager picture_view_pager;
    private List<PageBaseFragment> mDataList;

    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    private OptionsPickerView pvCustomOptions;

    private List<String> classify;
    /**
     * 当前期数
     */
    private String period;

    /**
     * 最新期数
     */
    private String newPeriod;

    /**
     * 传进来对比的期数
     */
    private String periodId;
    /**
     * 图库ID
     */
    private String galleryId;
    private PictureDetailsFragment userPostCentreFragment;

    /**
     * 期数下标
     */
    private int topIndex = 0;

    /**
     * 上个界面传过来的
     * 用于点赞后回传 刷新
     * */
    private int listPosition;
    private String titleName;
    private List<SelectPictureData> responseData;


    @Override
    public int getViewId() {
        return R.layout.activity_gallery_detail;
    }

    @Override
    public void afterViews() {
        //markSixTitle.init("", "", "发布图解", 0, this);
        markSixTitle.init("图库详情", "", "", 0, this);
        galleryId = getStringParam(StringConstants.ID);
        periodId = getStringParam(StringConstants.PERIOD);
        String position = getStringParam(StringConstants.POSITION);
        if (!TextUtils.isEmpty(position))
            listPosition = Integer.valueOf(position);
        if (!TextUtils.isEmpty(galleryId))
            getAllPeriods(galleryId, 0);

        mLoadingLayout.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoadingLayout.showLoading();
                if (!TextUtils.isEmpty(galleryId))
                    getAllPeriods(galleryId, 0);

            }
        });
        getSelectPicture(false);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(SendGalleryTitle event) {
        titleName = event.getTitleName();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_function://去发布图解
                if (userPostCentreFragment == null)
                    break;
                String[] listData = new String[]{ //页面调转
                        StringConstants.ID, galleryId,
                        StringConstants.PERIOD, period,
                        "StringName", titleName
                };
                HashMap<String, String> hashMap = IntentUtils.getHashObj(listData);
                if (checkLogin(R.string.GalleryExplainReleaseActivity,hashMap)){
                    startClass(R.string.GalleryExplainReleaseActivity, hashMap);
                }
                break;
        }
    }

    private void getAllPeriods(String id, int yera) {

        new GalleryImpl(new MarkSixNetCallBack<List<PeriodData>>(this, PeriodData.class) {
            @Override
            public void onSuccess(List<PeriodData> response, int id) {
                mLoadingLayout.showContent();
                if (response != null) {
                    if (response.size() > 0)
                        setViewPage(response);
                }
            }

            @Override
            public void onError(String msg, String code) {
                //super.onError(msg, code);
                mLoadingLayout.showError();
            }
        }.setNeedDialog(false)).getAllPictrue(id, yera);
    }

    /**
     * 设置viewpage数据
     *
     * @param
     */

    private void setViewPage(List<PeriodData> list) {
        classify = new ArrayList<>();
        mDataList = new ArrayList<>();
        newPeriod = list.get(0).getId();
        for (int i = 0; i < list.size(); i++) {
            classify.add(list.get(i).getPeriod() + "期");
            userPostCentreFragment = new PictureDetailsFragment();
            PeriodData periodData = list.get(i);
            periodData.setGalleryId(galleryId);
            periodData.setPosition(listPosition);
            periodData.setNewPeriod(newPeriod);
            userPostCentreFragment.setPictureId(periodData);
            mDataList.add(userPostCentreFragment);
            if (list.get(i).getPeriod().equals(periodId)) {
                topIndex = i;
            }
        }
        period = list.get(topIndex).getId();
        PagerBaseAdapter pagerBaseAdapter = new PagerBaseAdapter(getSupportFragmentManager(), mDataList);
        picture_view_pager.setAdapter(pagerBaseAdapter);

        picture_view_pager.setOffscreenPageLimit(mDataList.size());

        MarkSixNavigatorAdapter markSixNavigatorAdapter = new MarkSixNavigatorAdapter(classify, picture_view_pager, 0,
                LinePagerIndicator.MODE_WRAP_CONTENT);
        markSixNavigatorAdapter.setTextSize(20);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(markSixNavigatorAdapter);

        picture_indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(picture_indicator, picture_view_pager);
        // picture_view_pager.addOnPageChangeListener(pageChangeListener);
        picture_view_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                periodOpition = position;
                mSwipeBackLayout.setSwipeFromEdge(position != 0);
                CommonUtils.setInitFragmentData(mDataList, position);
                // 获取当前期数
                period = list.get(position).getId();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        picture_view_pager.post(new Runnable() {
            @Override
            public void run() {
                picture_view_pager.setCurrentItem(topIndex, false);
                CommonUtils.setInitFragmentData(mDataList, topIndex);
            }
        });
    }

    private List<SelectPictureData> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();

    @OnClick({R.id.more_years})
    public void onClicks(View view) {
        switch (view.getId()) {
            case R.id.more_years: //选择更多年份
                if (CommonUtils.ListNotNull(responseData)){
                    if (pvCustomOptions!=null){
                        if (!pvCustomOptions.isDialog()){
                            showYearDialog();
                        }else {
                            pvCustomOptions.dismiss();
                        }
                    }else {
                        showYearDialog();
                    }
                }else {
                    getSelectPicture(true);
                }
                break;
            default:
        }
    }

    /**
     * 设置弹框高度
     */
    private void reSetOptionsHeight() {
        if (pvCustomOptions != null) {
            ViewGroup dialogContainerLayout = pvCustomOptions.getDialogContainerLayout();
            ViewParent parent = dialogContainerLayout.getParent();
            if (parent != null && parent instanceof FrameLayout) {
                FrameLayout frameLayout = (FrameLayout) parent;
                frameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                        UIUtils.getScreenHeight(getContext())));
            }
        }
    }

    /**
     * 当前年
     */
    private String currentYear = "2019";
    /**
     * 时间选择期当前的年Opition
     */
    private int yearOpition = 0;
    /**
     * 时间选择器当前的期数Opition
     */
    private int periodOpition = 0;

    /**
     * 选择年dialog
     *
     * @param
     */
    private void showYearDialog() {

        if (options1Items.size() > 0) {
            options1Items.clear();
        }
        if (options2Items.size() > 0) {
            options2Items.clear();
        }
        options1Items.addAll(responseData);

        for (int i = 0; i < responseData.size(); i++) {
            ArrayList<String> cityList = new ArrayList<>();//该省的城市列表（第二级）

            for (int j = 0; j < responseData.get(i).getPeriod().size(); j++) {
                String period = responseData.get(i).getPeriod().get(j).getName() + "期";
                cityList.add(period);
            }
            options2Items.add(cityList);
        }
        pvCustomOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置

                yearOpition = options1;
                periodOpition = option2;
                //  currentYear = options1Items.get(options1).getPickerViewText();
                // String tx = options1Items.get(options1).getPickerViewText() + options2Items.get(options1).get(option2);
                setSelectYear();
            }
        }).setLayoutRes(R.layout.pickerview_picture_year_options, new CustomListener() {
            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = v.findViewById(R.id.tv_finish); //tv_ok
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvCustomOptions.dismiss();
                    }
                });

                final TextView tvOk = v.findViewById(R.id.tv_ok); //
                tvOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvCustomOptions.returnData();
                        pvCustomOptions.dismiss();
                    }
                });
            }
        }).setOutSideCancelable(true)
                .setSelectOptions(yearOpition, periodOpition)
                .build();
        reSetOptionsHeight();
        //pvCustomOptions.setPicker(list,options2Items);//添加数据
        pvCustomOptions.setPicker(options1Items, options2Items);
        pvCustomOptions.show();
    }

    /**
     * 选择对应的年份和期数
     */
    private void setSelectYear() {

        String ysar = responseData.get(yearOpition).getYear();
        topIndex = periodOpition;
        if (ysar.equals(currentYear)) {
            period = responseData.get(yearOpition).getPeriod().get(periodOpition).getId();
            picture_view_pager.post(new Runnable() {
                @Override
                public void run() {
                    picture_view_pager.setCurrentItem(topIndex, false);
                    //  CommonUtils.setInitFragmentData(mDataList, topIndex);
                }
            });
        } else {
            currentYear = ysar;
            getAllPeriods(galleryId, Integer.valueOf(ysar));
        }
    }

    private void getSelectPicture(boolean isShow) {
        new GalleryImpl(new MarkSixNetCallBack<List<SelectPictureData>>(this, SelectPictureData.class) {
            @Override
            public void onSuccess(List<SelectPictureData> response, int id) {
               // mLoadingLayout.showContent();
                if (response != null) {
                    responseData = response;
                    if (isShow)
                        showYearDialog();
                }
            }

            @Override
            public void onError(String ms, String code) {
             //   super.onError(msg, code);

            }
        }.setNeedDialog(isShow)).getSelectPeriod();
    }

}
