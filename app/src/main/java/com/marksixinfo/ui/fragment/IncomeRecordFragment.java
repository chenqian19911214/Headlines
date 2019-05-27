package com.marksixinfo.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.ViewHolder;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.base.SimpleAdapter;
import com.marksixinfo.bean.IncomeRecordData;
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.MoneyUtils;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.MyClassicsFooter;
import com.marksixinfo.widgets.NumberAnimTextView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;

/**
 * 收益记录
 *
 * @Auther: Administrator
 * @Date: 2019/4/23 0023 14:08
 * @Description:
 */
public class IncomeRecordFragment extends PageBaseFragment implements View.OnClickListener {


    TextView tvDescribe;
    NumberAnimTextView tvCashReserve;
    @BindView(R.id.recyclerView)
    ListView recyclerView;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;


    private List<IncomeRecordData> list = new ArrayList<>();
    private int pageIndex = 0;//页数
    private SimpleAdapter<IncomeRecordData> adapter;
    private int type;//0,金币收益 1,现金收益
    private String number = "";

    @Override
    public int getViewId() {
        return R.layout.fragment_income_record;
    }

    @Override
    protected void afterViews() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt(StringConstants.TYPE);
        }

        mLoadingLayout.setRetryListener(this);

        View headView = View.inflate(getContext(), R.layout.item_income_record_head, null);
        tvDescribe = headView.findViewById(R.id.tv_describe);
        tvCashReserve = headView.findViewById(R.id.tv_cash_reserve);
        tvCashReserve.setText(type == 1 ? "0.00" : "0");
        recyclerView.addHeaderView(headView);

        int emptyHeight = UIUtils.getScreenHeight(getContext()) - UIUtils.dip2px(getContext()
                , 208) - UIUtils.getStatusBarHeight(getContext());

        adapter = new SimpleAdapter<IncomeRecordData>((ActivityIntentInterface) getContext()
                , list, R.layout.item_income_record) {
            @Override
            public void convert(ViewHolder holder, IncomeRecordData item) {
                if (item != null) {
                    if (item.getItemType() == 1) {
                        View empty = holder.getView(R.id.rl_empty);
                        empty.getLayoutParams().height = emptyHeight;
                        holder.setVisible(R.id.ll_data_content, false);
                        holder.setVisible(R.id.rl_empty, true);
                    } else {
                        holder.setVisible(R.id.ll_data_content, true);
                        holder.setVisible(R.id.rl_empty, false);
                        holder.setText(R.id.tv_type_name, CommonUtils.StringNotNull(item.getNote()) ? item.getNote() : "");
                        holder.setText(R.id.tv_time, CommonUtils.StringNotNull(item.getAdd_Time()) ? item.getAdd_Time() : "");
                        int type = item.getType();
                        String price = item.getPrice();
                        holder.setText(R.id.tv_number_record, CommonUtils.StringNotNull(price) ?
                                (type == 1 ? "+" : "-") + (IncomeRecordFragment.this.type == 1 ?
                                        price : MoneyUtils.getIntMoneyText(price)) : "");
                        //1是收入  0是支出
                        holder.setTextColor(R.id.tv_number_record, mContext.getContext()
                                .getResources().getColor(type == 1 ? R.color.colorPrimary : R.color.green_31b));

                    }
                }
            }
        };
        recyclerView.setAdapter(adapter);
        mLoadingLayout.setRetryListener(this);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                getDetail();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refresh(false, "");
            }
        });
    }


    /**
     * 开始加载,下拉动画
     */
    private void startData() {
        getDetail();
    }


    /**
     * 获取余额
     */
    private void getDetail() {
        new HeadlineImpl(new MarkSixNetCallBack<String>(this, String.class) {
            @Override
            public void onSuccess(String response, int id) {
                refresh(true, response);
            }

            @Override
            public void onError(String msg, String code) {
                mLoadingLayout.showError();
            }

        }.setNeedDialog(false)).displayCredit(String.valueOf(type));
    }


    /**
     * 设置余额
     *
     * @param response
     */
    private void setDetailData(String response) {
        tvDescribe.setText(type == 0 ? "金币收益(个)" : "现金收益(元)");
        if (CommonUtils.StringNotNull(response) && !response.equals(number)) {
            number = response;
            tvCashReserve.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvCashReserve.setHadThousands(false);
                    tvCashReserve.setFractionDigits(type == 0 ? 0 : 2);
                    tvCashReserve.setNumberString(CommonUtils.StringNotNull(response) ? response : "0");
                }
            }, NumberConstants.ANIM_TEXT_VIEW_TIME);
        }
    }


    /**
     * 请求网络
     *
     * @param isRefresh
     */
    private void refresh(boolean isRefresh, String response2) {
        if (isRefresh) {
            pageIndex = 0;
        }
        new HeadlineImpl(new MarkSixNetCallBack<List<IncomeRecordData>>(this, IncomeRecordData.class) {
            @Override
            public void onSuccess(List<IncomeRecordData> response, int id) {
                setDetailData(response2);
                setData(isRefresh, response);
            }

            @Override
            public void onError(String msg, String code) {
                if (!CommonUtils.ListNotNull(list)) {
                    mLoadingLayout.showError();
                } else {
                    super.onError(msg, code);
                }
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                loadDone(isRefresh);
            }
        }.setNeedDialog(false)).earningsList(String.valueOf(type), String.valueOf(++pageIndex));
    }


    /**
     * 请求完成
     */
    private void loadDone(boolean isRefresh) {
        if (refreshLayout != null) {
            if (isRefresh) {
                refreshLayout.finishRefresh();
                refreshLayout.resetNoMoreData();
            } else {
                refreshLayout.finishLoadMore();
            }
        }
    }


    /**
     * 设置数据
     *
     * @param isRefresh
     * @param response
     */
    private void setData(boolean isRefresh, List<IncomeRecordData> response) {
        if (isRefresh) {
            list.clear();
        }
        if (CommonUtils.ListNotNull(response)) {
            list.addAll(response);
        } else {
            setEmptyData();
            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
        }
        mLoadingLayout.showContent();
        if (adapter != null) {
            adapter.changeDataUi(list);
        }
    }


    /**
     * 设置空太页
     */
    private void setEmptyData() {
        try {
            MyClassicsFooter footer = (MyClassicsFooter) refreshLayout.getRefreshFooter();
            if (!CommonUtils.ListNotNull(list)) {
                list.add(new IncomeRecordData(1));
                footer.setTextTextNothing("");
            } else {
                footer.setTextTextNothing(getContext().getResources().getString(R.string.load_all_data));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void loadData() {
        startData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_function://说明
                toast("说明");
                break;
            case R.id.retry_button://网络错误
                mLoadingLayout.showLoading();
                startData();
                break;
        }
    }

    public NumberAnimTextView getTvCashReserve() {
        return tvCashReserve;
    }
}
