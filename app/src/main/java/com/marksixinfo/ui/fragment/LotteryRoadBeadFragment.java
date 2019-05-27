package com.marksixinfo.ui.fragment;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.roadbeadtreeview.RoadBeadRootItem;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.bean.LotteryBaseData;
import com.marksixinfo.bean.LotteryRoadBeadData;
import com.marksixinfo.bean.RoadBeadDetailData;
import com.marksixinfo.bean.RoadBeadRootData;
import com.marksixinfo.bean.RoadBeadTitleData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.interfaces.ILotteryRoadBead;
import com.marksixinfo.net.impl.LotteryImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.AutoFlowLayout;
import com.marksixinfo.widgets.treerecycler.adpater.TreeRecyclerViewAdapter;
import com.marksixinfo.widgets.treerecycler.adpater.TreeRecyclerViewType;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 开奖分类路珠
 *
 * @Auther: Administrator
 * @Date: 2019/5/13 0013 17:07
 * @Description:
 */
public class LotteryRoadBeadFragment extends PageBaseFragment implements ILotteryRoadBead
        , com.marksixinfo.widgets.AutoFlowLayout.OnTextViewClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.AutoFlowLayout)
    AutoFlowLayout AutoFlowLayout;
    @BindView(R.id.rl_category_root)
    RelativeLayout rlCategoryRoot;

    private int type;//0:号码  1:生肖 2:波色 3,路珠
    private List<LotteryBaseData.CategroyBean.ChildBean> child;
    private boolean isShowDialog;
    private String categoryName = "";//当前选择的分类名称
    private ViewGroup.MarginLayoutParams params;
    private TreeRecyclerViewAdapter<RoadBeadRootItem> adapter;
    private ArrayList<RoadBeadRootItem> rootItems = new ArrayList<>();
    private RoadBeadRootItem rootItem;
    private RoadBeadRootData roadBeadRootData;
    private int margins15;
    private int margins10;
    private String categoryId = "";

    @Override
    public int getViewId() {
        return R.layout.fragment_lottery_road_bead;
    }

    @Override
    protected void afterViews() {
        initData();
    }


    /**
     * 初始化数据
     */
    private void initData() {
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

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new TreeRecyclerViewAdapter<>(this, rootItems, TreeRecyclerViewType.SHOW_ALL);
        recyclerView.setAdapter(adapter);
    }


    /**
     * 获取数据
     */
    private void getData() {
        new LotteryImpl(new MarkSixNetCallBack<LotteryRoadBeadData>(this, LotteryRoadBeadData.class) {
            @Override
            public void onSuccess(LotteryRoadBeadData response, int id) {
                setData(response);
            }
        }).getStatisticsResult(categoryId, "");
    }

    /**
     * 设置数据
     *
     * @param response
     */
    private void setData(LotteryRoadBeadData response) {
        if (response != null) {
            handlerList(response);
            listNotify();
        }
    }

    /**
     * 刷新列表
     */
    private void listNotify() {
        rootItem = new RoadBeadRootItem(roadBeadRootData, this);
        if (rootItems != null) {
            rootItems.clear();
        } else {
            rootItems = new ArrayList<>();
        }
        rootItems.add(rootItem);
        adapter.setDatas(rootItems);
    }

    /**
     * 处理数据
     *
     * @param data
     */
    private void handlerList(LotteryRoadBeadData data) {
        if (data != null) {
            roadBeadRootData = new RoadBeadRootData();
            LinkedHashMap<Integer, Boolean> checked = new LinkedHashMap<>();
            for (int i = 0; i < 7; i++) {
                checked.put(i, true);
            }
            roadBeadRootData.setTitle(categoryName);
            roadBeadRootData.setChecked(checked);

            LinkedHashMap<Integer, RoadBeadTitleData> titles = new LinkedHashMap<>();

            titles.put(0, new RoadBeadTitleData("第一球", getDetailData(data.get_$1())));
            titles.put(1, new RoadBeadTitleData("第二球", getDetailData(data.get_$2())));
            titles.put(2, new RoadBeadTitleData("第三球", getDetailData(data.get_$3())));
            titles.put(3, new RoadBeadTitleData("第四球", getDetailData(data.get_$4())));
            titles.put(4, new RoadBeadTitleData("第五球", getDetailData(data.get_$5())));
            titles.put(5, new RoadBeadTitleData("第六球", getDetailData(data.get_$6())));
            titles.put(6, new RoadBeadTitleData("特码", getDetailData(data.get_$7())));

            roadBeadRootData.setTitles(titles);
            roadBeadRootData.setTitlesTemp(new LinkedHashMap<>(titles));

        }
    }

    /**
     * 处理列表数据
     *
     * @param listList
     * @return
     */
    private List<RoadBeadDetailData> getDetailData(List<List<String>> listList) {
        List<RoadBeadDetailData> datas = new ArrayList<>();
        if (CommonUtils.ListNotNull(listList)) {
            for (int i = 0; i < listList.size(); i++) {
                List<String> strings = listList.get(i);
                if (CommonUtils.ListNotNull(strings)) {
                    int roadBeadColor = CommonUtils.getRoadBeadColor(strings.get(0));
                    StringBuilder s = new StringBuilder();
                    for (int j = 0; j < strings.size(); j++) {
                        s.append(strings.get(j));
                    }
                    datas.add(new RoadBeadDetailData(s.toString(), roadBeadColor));
                }
            }
        }
        return datas;
    }

    /**
     * 开始加载
     */
    @Override
    protected void loadData() {
        selectCategory(0);
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
                getData();
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
            selectCategory(position);
        }
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

    /**
     * 选择第几球
     *
     * @param position 位置
     * @param isCheck  是否选中
     */
    @Override
    public void checkSelect(int position, boolean isCheck) {
        if (roadBeadRootData != null) {
            LinkedHashMap<Integer, RoadBeadTitleData> titles = roadBeadRootData.getTitles();
            LinkedHashMap<Integer, RoadBeadTitleData> titlesTemp = roadBeadRootData.getTitlesTemp();
            LinkedHashMap<Integer, Boolean> checked = roadBeadRootData.getChecked();
            if (CommonUtils.MapNotNull(checked)) {
                try {
                    checked.put(position, isCheck);
                    titles.put(position, isCheck ? titlesTemp.get(position) : null);
                    listNotify();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @OnClick(R.id.rl_category_root)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_category_root:
                setHiddenCategory();
                break;
        }
    }
}
