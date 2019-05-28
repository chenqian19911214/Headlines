package com.marksixinfo.ui.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.marksixinfo.R;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.bean.LotteryBaseData;
import com.marksixinfo.bean.LotteryChartData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.net.impl.LotteryImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.NumberUtils;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.AutoFlowLayout;
import com.marksixinfo.widgets.LotteryPeriodRelativeLayout;
import com.marksixinfo.widgets.PeriodEditText;
import com.marksixinfo.widgets.chart.AxisValueFormatter;
import com.marksixinfo.widgets.chart.CustomXAxisRendererHorizontalBarChart;
import com.marksixinfo.widgets.easyemoji.KeyBoardUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开奖分类图表
 *
 * @Auther: Administrator
 * @Date: 2019/5/13 0013 17:06
 * @Description:
 */
public class LotteryChartFragment extends PageBaseFragment implements
        com.marksixinfo.widgets.AutoFlowLayout.OnTextViewClickListener
        , TextWatcher, TextView.OnEditorActionListener {


    @BindView(R.id.tv_category_name)
    TextView tvCategoryName;
    @BindView(R.id.edit_period)
    PeriodEditText editPeriod;
    @BindView(R.id.chart)
    HorizontalBarChart mChart;
    @BindView(R.id.AutoFlowLayout)
    AutoFlowLayout AutoFlowLayout;
    @BindView(R.id.rl_category_root)
    RelativeLayout rlCategoryRoot;
    @BindView(R.id.rl_content)
    LotteryPeriodRelativeLayout rl_content;
    private int type;//0:号码  1:生肖 2:波色
    private List<LotteryBaseData.CategroyBean.ChildBean> child;
    private boolean isShowDialog;
    private ViewGroup.MarginLayoutParams params;
    private String categoryName = "";
    private String categoryId = "";
    private String categoryNumber = "100";//输入期数
    private String oldNumber = "100";//当前列表期数
    private int minPeriod = 10;//最小输入期数
    private int maxPeriod = 500;//最大输入期数
    private int margins15;
    private int margins10;

    @Override
    public int getViewId() {
        return R.layout.fragment_lottery_chart;
    }

    @Override
    protected void afterViews() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt(StringConstants.TYPE);
            child = bundle.getParcelableArrayList(StringConstants.CATEGORY);
        }
        params = new ViewGroup.MarginLayoutParams(ViewGroup.MarginLayoutParams.WRAP_CONTENT
                , ViewGroup.MarginLayoutParams.WRAP_CONTENT);
        margins10 = UIUtils.dip2px(getContext(), 10);
        margins15 = UIUtils.dip2px(getContext(), 15);
        params.setMargins(margins15 - 3, margins15, 0, 0);

        editPeriod.setOnEditorActionListener(this);
        editPeriod.addTextChangedListener(this);
        editPeriod.setFocusable(false);
        editPeriod.setFocusableInTouchMode(false);
        editPeriod.setListener(new SucceedCallBackListener() {
            @Override
            public void succeedCallBack(Object o) {
                editPeriod.setFocusableInTouchMode(true);
                editPeriod.setFocusable(true);
                editPeriod.setCursorVisible(true);
                editPeriod.setText("");
            }
        });
        rl_content.setListener(new SucceedCallBackListener() {
            @Override
            public void succeedCallBack(Object o) {
                changeSelect(type, type);
            }
        });
        setCartBase();
    }

    /**
     * 设置图表基础参数
     */
    private void setCartBase() {


        mChart.setNoDataText("");
        mChart.setScaleEnabled(false);//禁止缩放
        mChart.setDrawBarShadow(false);// 是否展示阴影
        mChart.setDrawValueAboveBar(true); //数值展示在柱状图上面还是里面
        mChart.getDescription().setEnabled(false);// 图标右下角的描述信息
        mChart.setDrawGridBackground(false);// 是否显示表格颜色
        mChart.setMaxVisibleValueCount(160);
        mChart.setPinchZoom(false);
        mChart.setMinOffset(0f);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTextSize(13);
        xl.setAxisLineColor(0xffdddddd);
        xl.setTextColor(0xff999999);
        xl.setDrawGridLines(false);
        xl.setXOffset(10);


        YAxis axisLeft = mChart.getAxisLeft();
        axisLeft.setDrawGridLines(true);
        axisLeft.setGridColor(0xffdddddd);
        axisLeft.setGridLineWidth(1);
        axisLeft.setAxisMinimum(0);
        axisLeft.setDrawAxisLine(false);
        axisLeft.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);  // YY方向文字的位置，在线外侧
        axisLeft.setTextColor(0xff999999);
        axisLeft.setTextSize(13f);
        axisLeft.setYOffset(10);

        YAxis axisRight = mChart.getAxisRight();
        axisRight.setEnabled(false);

        mChart.getLegend().setEnabled(false);
        mChart.setExtraOffsets(-15, 5, 10, 15);
    }


    /**
     * 开始加载
     */
    @Override
    protected void loadData() {
        selectCategory(0);
    }


    /**
     * 选择分类
     *
     * @param index
     */
    private void selectCategory(int index) {
        if (CommonUtils.ListNotNull(child)) {
            LotteryBaseData.CategroyBean.ChildBean childBean = null;
            try {
                childBean = child.get(index);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (childBean != null) {
                categoryId = String.valueOf(childBean.getId());
                categoryName = childBean.getName();
                tvCategoryName.setText(CommonUtils.StringNotNull(categoryName) ? categoryName : "");
                getDetailList();
            }
        }
    }

    /**
     * 根据分类id获取柱状图数据
     */
    private void getDetailList() {
        new LotteryImpl(new MarkSixNetCallBack<List<LotteryChartData>>(this, LotteryChartData.class) {
            @Override
            public void onSuccess(List<LotteryChartData> response, int id) {
                if (CommonUtils.ListNotNull(response)) {
                    setChart(response);
                }
            }
        }.setNeedDialog(!isInit)).getStatisticsResult(categoryId, categoryNumber);
        oldNumber = categoryNumber;
    }


    /**
     * 设置图表数据
     *
     * @param response
     */
    private void setChart(List<LotteryChartData> response) {

        Collections.sort(response);
        ArrayList<BarEntry> yVals = new ArrayList<>();
        String[] xVals = new String[response.size()];
        for (int i = 0; i < response.size(); i++) {
            LotteryChartData lotteryChartData = response.get(i);
            if (lotteryChartData != null) {
                xVals[i] = lotteryChartData.getName();
                yVals.add(new BarEntry(i, lotteryChartData.getValue()));
            }
        }
        int height = UIUtils.getScreenHeight(getContext()) - UIUtils.dip2px(getContext()
                , 91) - UIUtils.getStatusBarHeight(getContext());
        height = response.size() > 12 ? (int) ((height / 12f) * response.size()) : height;
        height = type == 2 ? (int) (height * 0.8f) : height;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
        layoutParams.setMargins(0, UIUtils.dip2px(getContext(), 60), 0, 0);
        mChart.setLayoutParams(layoutParams);


        XAxis xl = mChart.getXAxis();
        xl.setLabelCount(xVals.length, false);
        IAxisValueFormatter xAxisFormatter = new AxisValueFormatter(xVals);
        xl.setValueFormatter(xAxisFormatter);

        YAxis axisLeft = mChart.getAxisLeft();
        axisLeft.setLabelCount(7, false);
        float y = yVals.get(yVals.size() - 1).getY();
        if (y < 7) {
            axisLeft.setSpaceTop(10);
            axisLeft.setAxisMaximum(7);
        } else {
            axisLeft.setSpaceTop(30);
            axisLeft.resetAxisMaximum();
        }
        axisLeft.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }
        });

        mChart.setXAxisRenderer(new CustomXAxisRendererHorizontalBarChart(mChart.getViewPortHandler(),
                xl, mChart.getTransformer(YAxis.AxisDependency.LEFT), mChart, xVals.length));

        BarDataSet set1;

        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals, "");
            set1.setDrawValues(true);
            set1.setValueTextColor(0xff333333);
            set1.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return String.valueOf((int) value);
                }
            });
            if (type == 2) {//波色颜色设置
                if (xVals.length > 0) {
                    int[] color = new int[xVals.length];
                    for (int i = xVals.length - 1; i >= 0; i--) {
                        color = CommonUtils.getLotteryChartColorByString(color, i, xVals[i]);
                    }
                    set1.setColors(color, getContext());
                }
            } else {
                set1.setColor(getContext().getResources().getColor(R.color.colorPrimary));
            }
        }

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(13f);
        data.setBarWidth(0.55f);
        data.setHighlightEnabled(false);
        mChart.setData(data);

        mChart.setFitBars(false);
        mChart.animateXY(0, 1500);
    }


    /**
     * 显示分类弹框,本页面取反
     */
    @Override
    public void setShowCategory() {
        isShowDialog = !isShowDialog;
        if (isShowDialog) {
            rlCategoryRoot.setVisibility(View.VISIBLE);
            if (!CommonUtils.ListNotNull(AutoFlowLayout.getAllTextView()) &&
                    CommonUtils.ListNotNull(child)) {
                for (int i = 0; i < child.size(); i++) {
                    LotteryBaseData.CategroyBean.ChildBean childBean = child.get(i);
                    if (childBean != null) {
                        TextView textView = getCategoryTextView(childBean.getName());
                        if (i == 0) {
                            textView.setTextColor(0xfffc5c66);
                        }
                        AutoFlowLayout.addView(textView);
                    }
                }
                AutoFlowLayout.setOnTextViewClickListener(this);
            }
        } else {
            rlCategoryRoot.setVisibility(View.GONE);
        }
    }

    /**
     * 关闭分类弹框
     */
    @Override
    public void setHiddenCategory() {
        isShowDialog = true;
        setShowCategory();
    }

    /**
     * 页面发生切换,重置当前列表期数
     *
     * @param oldPosition
     * @param newPosition
     */
    @Override
    public void changeSelect(int oldPosition, int newPosition) {
        if(editPeriod==null)
            return;
        if (oldPosition == type && editPeriod != null
                && !oldNumber.equals(editPeriod.getText().toString().trim().replace("期", ""))) {
            editPeriod.setText(oldNumber + "期");
        }
        editPeriod.setFocusableInTouchMode(false);
        editPeriod.setCursorVisible(false);
        editPeriod.setFocusable(false);
    }

    /**
     * 获取分类弹框TextView
     *
     * @param s
     * @return
     */
    private TextView getCategoryTextView(String s) {
        TextView textView = new TextView(getContext());
        textView.setText(s);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(margins10, margins15, margins10, margins15);
        textView.setTextSize(15);
        textView.setTextColor(0xff333333);
        textView.setBackgroundResource(R.drawable.shape_release_select);
        textView.setLayoutParams(params);
        return textView;
    }

    @OnClick({R.id.btn_commit, R.id.rl_category_root})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_commit:
                goToPeriod();
                break;
            case R.id.rl_category_root:
                setHiddenCategory();
                break;
        }
    }

    /**
     * 确定输入期数刷新
     */
    private void goToPeriod() {
        String trim = editPeriod.getText().toString().trim();
        if (!CommonUtils.StringNotNull(trim)) {
            toast("请输入期数");
        } else {
            trim = trim.replace("期", "");
            if (!CommonUtils.StringNotNull(trim)) {
                toast("请输入期数");
            } else {
                int i = NumberUtils.stringToInt(trim.replace("期", ""));
                if (i < minPeriod) {
                    toast("最小期数为" + minPeriod + "期");
                    editPeriod.setText(String.valueOf(minPeriod));
                    editPeriod.setSelection(String.valueOf(minPeriod).length());
                    return;
                }
                categoryNumber = trim;
                editPeriod.setCursorVisible(false);
                editPeriod.setText(categoryNumber + "期");
                getDetailList();
            }
        }
    }

    @Override
    public void onGetText(TextView textView, int position) {
        List<TextView> allTextView = AutoFlowLayout.getAllTextView();
        if (CommonUtils.ListNotNull(allTextView)) {
            for (TextView v : allTextView) {
                if (v != null) {
                    v.setTextColor(0xff333333);
                }
            }
            textView.setTextColor(0xfffc5c66);
            setHiddenCategory();
            changeSelect(type, type);
            selectCategory(position);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable editable) {
        //限制最小输入
        String trim = editPeriod.getText().toString().trim();
        if (CommonUtils.StringNotNull(trim)) {
            if (CommonUtils.StringNotNull(trim)) {
                boolean hasUnit = false;
                if (trim.contains("期")) {
                    hasUnit = true;
                    trim = trim.replace("期", "");
                }
                int i = NumberUtils.stringToInt(trim);
                if (i > maxPeriod) {
                    i = maxPeriod;
                    toast("最大期数为" + maxPeriod + "期");
                }
                editPeriod.removeTextChangedListener(this);
                editPeriod.setText(i > 0 ? String.valueOf(i) + (hasUnit ? "期" : "") : "");
                if (i > 0) {
                    editPeriod.setSelection((String.valueOf(i) + (hasUnit ? "期" : "")).length());
                }
                editPeriod.addTextChangedListener(this);
            } else {
                editPeriod.setText("");
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        if (EditorInfo.IME_ACTION_GO == actionId) {
            KeyBoardUtils.hideInput(getActivity(), editPeriod);
            goToPeriod();
        }
        return true;
    }


}
