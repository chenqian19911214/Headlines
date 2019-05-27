package com.marksixinfo.ui.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.android.material.appbar.AppBarLayout;
import com.marksixinfo.BuildConfig;
import com.marksixinfo.R;
import com.marksixinfo.adapter.LotteryPeriodAdapter;
import com.marksixinfo.adapter.LotteryTitleNavigatorAdapter;
import com.marksixinfo.adapter.PagerBaseAdapter;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.base.ViewPagerHelper;
import com.marksixinfo.bean.LotteryBaseData;
import com.marksixinfo.bean.LotteryCountDownData;
import com.marksixinfo.bean.LotteryRealTimeData;
import com.marksixinfo.bean.LotteryTitleBean;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.LotteryRealTimeEvent;
import com.marksixinfo.interfaces.OnItemClickListener;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.net.impl.LotteryImpl;
import com.marksixinfo.ui.activity.MainActivity;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.CommRedTitle;
import com.marksixinfo.widgets.CommonNavigator;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.LotteryCurrentNumber;
import com.marksixinfo.widgets.LotteryNextTimeDownView;
import com.marksixinfo.widgets.LotteryPeriodLinearLayout;
import com.marksixinfo.widgets.SwitchButton;
import com.marksixinfo.widgets.pagergridlayoutmanager.PagerConfig;
import com.marksixinfo.widgets.pagergridlayoutmanager.PagerGridLayoutManager;
import com.marksixinfo.widgets.pagergridlayoutmanager.PagerGridSnapHelper;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import net.lucode.hackware.magicindicator.MagicIndicator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开奖
 *
 * @Auther: Administrator
 * @Date: 2019/5/10 0010 21:42
 * @Description:
 */
public class LotteryFragment2 extends PageBaseFragment implements PagerGridLayoutManager.PageListener, OnItemClickListener, View.OnClickListener {

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.marksix_title)
    CommRedTitle markSixTitle;
    @BindView(R.id.tv_period_number_name)
    TextView tvPeriodNumberName;
    @BindView(R.id.SwitchButton_lottery)
    SwitchButton SwitchButtonLottery;
    @BindView(R.id.LotteryCurrentView)
    LotteryCurrentNumber LotteryCurrentView;
    @BindView(R.id.tv_current_year)
    TextView tvCurrentYear;
    @BindView(R.id.btn_up_page)
    Button btnUpPage;
    @BindView(R.id.btn_next_page)
    Button btnNextPage;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.indicator)
    MagicIndicator indicator;
    @BindView(R.id.ll_topView)
    LotteryPeriodLinearLayout llTopView;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.tv_next_lottery_time_down)
    LotteryNextTimeDownView nextTimeDownView;

    private LotteryBaseData data;
    private int mRows = 4;
    private int mColumns = 5;
    private PagerGridLayoutManager mLayoutManager;
    private List<List<String>> periodList = new ArrayList<>();
    private LotteryPeriodAdapter periodAdapter;
    private int mTotalPage = 0;//期数总页数
    private String currentYear = "";//当前年 2019
    private List<PageBaseFragment> mDataList = new ArrayList<>();
    private OptionsPickerView<String> pvCustomOptions;
    private int type = 0;//0,开奖主页 1,实时开奖页
    private LotteryRealTimeFragment realTimeFragment;
    private String nextPeriod;//下期开奖期数

    @Override
    public int getViewId() {
        return R.layout.fragment_lottery2;
    }

    @Override
    protected void afterViews() {
        TextView tvTitle = markSixTitle.getTvTitle();
        tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
        markSixTitle.init("西瓜开奖", false);

        setDefaultSwitch();

        mLayoutManager = new PagerGridLayoutManager(mRows, mColumns, PagerGridLayoutManager
                .HORIZONTAL);

        // 水平分页布局管理器
        mLayoutManager.setPageListener(this);    // 设置页面变化监听器
        recyclerView.setLayoutManager(mLayoutManager);

        // 设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(recyclerView);

        // 如果需要查看调试日志可以设置为true，一般情况忽略即可
        PagerConfig.setShowLog(BuildConfig.DEBUG);

        periodAdapter = new LotteryPeriodAdapter(this, periodList, this);
        recyclerView.setAdapter(periodAdapter);
        mLoadingLayout.setRetryListener(this);

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                getData();
            }
        });

        llTopView.setListener(new SucceedCallBackListener() {
            @Override
            public void succeedCallBack(Object o) {
                int currentItem = viewpager.getCurrentItem();
                setChangeSelect(currentItem, currentItem);
            }
        });
        getData();
    }


    /**
     * 获取数据
     */
    private void getData() {
        getCountDown();
        new LotteryImpl(new MarkSixNetCallBack<LotteryBaseData>(this, LotteryBaseData.class) {
            @Override
            public void onSuccess(LotteryBaseData response, int id) {
                setData(response);
            }

            @Override
            public void onError(String msg, String code) {
                if (data == null) {
                    mLoadingLayout.showError();
                } else {
                    super.onError(msg, code);
                }
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                if (refreshLayout != null) {
                    refreshLayout.finishRefresh();
                    refreshLayout.resetNoMoreData();
                }
            }
        }.setNeedDialog(false)).getLotteryBaseInfo();

    }

    /**
     * 设置数据
     *
     * @param response
     */
    private void setData(LotteryBaseData response) {
        data = response;
        if (data != null) {
            setLottery(data.getLottery());

            setPeriod(data.getPeriod(), true);

            setCategroy(data.getCategroy());

            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }
    }

    /**
     * 设置开奖数据
     *
     * @param lottery
     */
    private void setLottery(LotteryBaseData.LotteryBean lottery) {
        if (lottery != null) {
            LotteryCurrentView.setData(lottery, new SucceedCallBackListener<Boolean>() {
                @Override
                public void succeedCallBack(Boolean o) {
                    LotteryCurrentView.setScratch(false);
                }
            });
            String period = lottery.getPeriod();
            tvPeriodNumberName.setText(CommonUtils.StringNotNull(period) ? "第" + period + "期" : "");

            setDefaultSwitch();

            SwitchButtonLottery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    LotteryCurrentView.setScratch(isChecked);
                    SPUtil.setBooleanValue(getContext(), SPUtil.LOTTERY_SCRATCH, isChecked);
                }
            });
        }
    }

    /**
     * 获取距离开奖秒数
     */
    private void getCountDown() {
        new LotteryImpl(new MarkSixNetCallBack<LotteryCountDownData>(this, LotteryCountDownData.class) {
            @Override
            public void onSuccess(LotteryCountDownData response, int id) {
                setCountDown(response);
            }
        }.setNeedDialog(false).setNeedToast(false)).getCountdown();
    }

    /**
     * 设置倒计时
     *
     * @param countDown
     */
    private void setCountDown(LotteryCountDownData countDown) {
        if (countDown != null) {
            long time = countDown.getTime();
            nextPeriod = countDown.getPeriod();
            long now = countDown.getNow();
            long l = Math.abs(time);
            if (l > 0) {
                nextTimeDownView.stop();
                nextTimeDownView.start(l * 1000);
            }
//            setCurrentLottery(now);
//            setCurrentLottery(1558790399);//21:19:59
//            setCurrentLottery(1558790966);// 21:29:26
            setCurrentLottery(1558870106);// 2019-05-26 19:28:26
        }
    }

    /**
     * 回复开奖主页,刷新
     */
    private void resetPage() {
        type = 0;//开奖主页
        if (realTimeFragment != null) {
            removeFragment(realTimeFragment);
        }
        if (refreshLayout != null) {
            refreshLayout.autoRefresh();
        }
    }


    /**
     * 设置实时开奖
     */
    private void setRealTimeFragment(LotteryRealTimeEvent data) {
        if (type == 0) {//未进入开奖
            type = 1;//开奖中
            realTimeFragment = new LotteryRealTimeFragment();
            realTimeFragment.setListener(new SucceedCallBackListener() {
                @Override
                public void succeedCallBack(Object o) {
                    resetPage();
                }
            });
            if (data != null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(StringConstants.LOTTERY_REAL_TIME, data);
                realTimeFragment.setArguments(bundle);
                if (isAdded()) {
                    replace(R.id.fl_real_time_content, realTimeFragment);
                }
            }
        }
    }

    /**
     * 设置是否可以刮奖
     */
    private void setDefaultSwitch() {
        if (SPUtil.getSharedPreferences(getContext()).contains(SPUtil.LOTTERY_SCRATCH)) {
            boolean booleanValue = SPUtil.getBooleanValue(getContext(), SPUtil.LOTTERY_SCRATCH);
            SwitchButtonLottery.setChecked(booleanValue);
            LotteryCurrentView.setScratch(booleanValue);
        } else {
            SwitchButtonLottery.setChecked(true);
            LotteryCurrentView.setScratch(true);
            SPUtil.setBooleanValue(getContext(), SPUtil.LOTTERY_SCRATCH, true);
        }
    }

    /**
     * 设置当年期数
     *
     * @param period
     */
    private void setPeriod(LotteryBaseData.PeriodBean period, boolean init) {
        if (period != null) {
            currentYear = period.getYear();
            tvCurrentYear.setText(CommonUtils.StringNotNull(currentYear) ? currentYear + "年" : "");
            periodList = handlerLotteryLotteryList(period.getList());
            if (CommonUtils.ListNotNull(periodList)) {
                periodAdapter.changeDataUi(periodList);
            }
            if (mLayoutManager != null) {
                mLayoutManager.smoothScrollToPage(0);
            }
            if (!init) {
                onItemClick(null, 0);
            }
        }
    }

    /**
     * 处理期数显示
     *
     * @param list
     * @return
     */
    private List<List<String>> handlerLotteryLotteryList(List<List<String>> list) {
        if (CommonUtils.ListNotNull(list)) {
            for (int i = 0; i < list.size(); i++) {
                List<String> strings = list.get(i);
                if (CommonUtils.ListNotNull(strings) && strings.size() == 2) {
                    strings.add(String.valueOf(i == 0));
                }
            }
        }
        return list;
    }


    /**
     * 设置分类
     *
     * @param categroy
     */
    private void setCategroy(List<LotteryBaseData.CategroyBean> categroy) {

        if (CommonUtils.ListNotNull(categroy)) {
            mDataList.clear();
            List<LotteryTitleBean> titles = new ArrayList<>();
            for (int i = 0; i < categroy.size(); i++) {
                LotteryBaseData.CategroyBean categroyBean = categroy.get(i);
                if (categroyBean != null) {
                    titles.add(new LotteryTitleBean(categroyBean.getName(),
                            R.drawable.icon_pull_red, R.drawable.icon_pull_gray2));
                    if (i < categroy.size() - 1) {
                        mDataList.add(getLotteryChartFragment(i, categroyBean.getChild()));
                    } else {
                        mDataList.add(getLotteryRoadBeadFragment(i, categroyBean.getChild()));
                    }
                }
            }

            PagerBaseAdapter pagerBaseAdapter = new PagerBaseAdapter(getChildFragmentManager(), mDataList);

            viewpager.setAdapter(pagerBaseAdapter);
            viewpager.setOffscreenPageLimit(mDataList.size());


            viewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    CommonUtils.setInitFragmentData(mDataList, position);
                    setCurrentFragmentCategory(-1);
                }
            });


            assert getContext() != null;
            CommonNavigator commonNavigator = new CommonNavigator(getContext());
            commonNavigator.setAdapter(new LotteryTitleNavigatorAdapter(titles, viewpager,
                    new SucceedCallBackListener<Integer>() {
                        @Override
                        public void succeedCallBack(Integer o) {
                            setCurrentFragmentCategory(o);
                        }
                    }));
            indicator.setNavigator(commonNavigator);
            ViewPagerHelper.bind(indicator, viewpager);

            viewpager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    CommonUtils.setInitFragmentData(mDataList, 0);
                }
            }, 100);
        }
    }


    /**
     * 页面发生选择变化
     *
     * @param oldPosition
     * @param newPosition
     */
    private void setChangeSelect(int oldPosition, int newPosition) {
        if (CommonUtils.ListNotNull(mDataList)) {
            for (PageBaseFragment fragment : mDataList) {
                if (fragment != null) {
                    fragment.changeSelect(oldPosition, newPosition);
                }
            }
        }
    }

    /**
     * 设置分类弹框
     *
     * @param index
     */
    private void setCurrentFragmentCategory(int index) {
        if (CommonUtils.ListNotNull(mDataList)) {
            if (index == -1) {
                for (PageBaseFragment fragment : mDataList) {
                    if (fragment != null) {
                        fragment.setHiddenCategory();
                    }
                }
            } else {
                PageBaseFragment fragment = mDataList.get(index);
                if (fragment != null) {
                    fragment.setShowCategory();
                }
            }
        }
    }


    /**
     * 开始加载
     */
    @Override
    protected void loadData() {

    }

    /**
     * @param pageSize 页面总数
     */
    @Override
    public void onPageSizeChanged(int pageSize) {
        mTotalPage = pageSize;
    }

    /**
     * @param pageIndex 选中的页面
     */
    @Override
    public void onPageSelect(int pageIndex) {
        btnUpPage.setEnabled(pageIndex > 0);
        btnNextPage.setEnabled(pageIndex < mTotalPage - 1);
    }

    /**
     * 期数选择
     *
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        if (CommonUtils.ListNotNull(periodList)) {
            String period = "";
            for (int i = 0; i < periodList.size(); i++) {
                List<String> strings = periodList.get(i);
                try {
                    boolean b = position == i;
                    strings.set(2, String.valueOf(b));
                    if (b) {
                        period = strings.get(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (periodAdapter != null) {
                periodAdapter.changeDataUi(periodList);
            }
            if (CommonUtils.StringNotNull(period)) {
                getLotteryResultByPeriod(period);
            }
        }
    }

    /**
     * 根据期数获取开奖
     */
    private void getLotteryResultByPeriod(String currentPeriod) {
        new LotteryImpl(new MarkSixNetCallBack<LotteryBaseData.LotteryBean>(this, LotteryBaseData.LotteryBean.class) {
            @Override
            public void onSuccess(LotteryBaseData.LotteryBean response, int id) {
                setLottery(response);
            }
        }).getLotteryResultByPeriod(currentPeriod);
    }

    /**
     * 获取年
     */
    private void selectYear() {
        new LotteryImpl(new MarkSixNetCallBack<List<String>>(this, String.class) {
            @Override
            public void onSuccess(List<String> response, int id) {
                if (CommonUtils.ListNotNull(response)) {
                    showYearDialog(response);
                } else {
                    onError("未获取到年份", "");
                }
            }
        }).getAllLotteryYear();
    }


    /**
     * 选择年dialog
     *
     * @param response
     */
    private void showYearDialog(List<String> response) {
        int selectYear = getSelectYear(response);
        pvCustomOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String s = response.get(options1);
                s = s.contains("年") ? s.replace("年", "") : s;
                setSelectYear(s);
            }
        }).setLayoutRes(R.layout.pickerview_custom_lottery_year, new CustomListener() {
            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvCustomOptions.returnData();
                        pvCustomOptions.dismiss();
                    }
                });
            }
        }).setOutSideCancelable(true)
                .setSelectOptions(selectYear)
                .build();
        reSetOptionsHeight();
        pvCustomOptions.setPicker(response);//添加数据
        pvCustomOptions.show();
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
     * 选中当前年
     *
     * @param response
     * @return
     */
    private int getSelectYear(List<String> response) {
        int index = 0;
        if (CommonUtils.ListNotNull(response)) {
            for (int i = 0; i < response.size(); i++) {
                String s = response.get(i);
                response.set(i, s + "年");
                if (s.equals(currentYear)) {
                    index = i;
                }
            }
        }
        return index;
    }

    /**
     * 选择年份
     *
     * @param year
     */
    private void setSelectYear(String year) {
        currentYear = year;
        tvCurrentYear.setText(CommonUtils.StringNotNull(currentYear) ? currentYear + "年" : "");
        getAllPeriodByYear(currentYear);
    }

    /**
     * 根据年份获取期数
     *
     * @param year
     */
    private void getAllPeriodByYear(String year) {
        new LotteryImpl(new MarkSixNetCallBack<LotteryBaseData.PeriodBean>(this, LotteryBaseData.PeriodBean.class) {
            @Override
            public void onSuccess(LotteryBaseData.PeriodBean response, int id) {
                setPeriod(response, false);
            }
        }).getPeriodByLotteryYear(year);
    }


    @OnClick({R.id.btn_up_page, R.id.btn_next_page, R.id.tv_current_year})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_current_year://选择年
                selectYear();
                break;
            case R.id.btn_up_page://上一页期数
                mLayoutManager.smoothPrePage();
                break;
            case R.id.btn_next_page://下一页期数
                mLayoutManager.smoothNextPage();
                break;
        }
    }


    /**
     * 获取图表分类
     *
     * @return
     */
    private LotteryChartFragment getLotteryChartFragment(int type, List<LotteryBaseData.CategroyBean.ChildBean> child) {
        LotteryChartFragment lotteryChartFragment = new LotteryChartFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(StringConstants.TYPE, type);
        bundle.putParcelableArrayList(StringConstants.CATEGORY, new ArrayList<>(child));
        lotteryChartFragment.setArguments(bundle);
        return lotteryChartFragment;


    }

    /**
     * 获取路珠
     *
     * @return
     */
    private LotteryRoadBeadFragment getLotteryRoadBeadFragment(int type, List<LotteryBaseData.CategroyBean.ChildBean> child) {
        LotteryRoadBeadFragment LotteryRoadBeadFragment = new LotteryRoadBeadFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(StringConstants.TYPE, type);
        bundle.putParcelableArrayList(StringConstants.CATEGORY, new ArrayList<>(child));
        LotteryRoadBeadFragment.setArguments(bundle);
        return LotteryRoadBeadFragment;
    }

    @Override
    public void onClick(View v) {
        mLoadingLayout.showLoading();
        getData();
    }

    /**
     * 开奖推送
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LotteryRealTimeEvent event) {
        if (event != null && type == 0) {
            LotteryRealTimeData data = event.getMessage();
            if (data != null) {
                List<String> lottery = data.getLottery();
                if (lottery != null && lottery.size() > 0 && lottery.size() < 7) {//进入开奖
                    setRealTimeFragment(event);
                }
            }
        }
    }


    /**
     * 设置当前开奖
     */
    private void setCurrentLottery(long now) {
        String value = SPUtil.getStringValue(getContext(), SPUtil.LOTTERY_CURRENT);
        if (CommonUtils.StringNotNull(value)) {
            LotteryRealTimeEvent data = CommonUtils.getRealTimeData(value);
            if (data != null) {
                int type = data.getType();//0,晚上9点20 后台开始重置  1,准备开奖,弹框提醒  2,开球中   3,开奖结束
                switch (type) {
                    case 0://开始倒计时
                        long time = CommonUtils.getTodayNineTime(now);
                        if (time > 0) {//开始倒计时
                            data.setType(1);
                            data.setShowTime(time);
                            setRealTimeFragment(data);
                        }
                        break;
                    case 1://弹框提醒
                        ((MainActivity) getActivity()).setRemindDialog();
                        break;
                    case 2://开球中
                        data.setType(2);
                        setRealTimeFragment(data);
                        break;
                    case 3://开奖结束
                        break;
                }
            }
        }
    }
}
