package com.marksixinfo.ui.activity;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.ViewHolder;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.base.SimpleAdapter;
import com.marksixinfo.bean.WithdrawDepositData;
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.constants.UrlStaticConstants;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.MoneyUtils;
import com.marksixinfo.utils.MoneyValueFilter;
import com.marksixinfo.utils.NumberUtils;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.NumberAnimTextView;
import com.marksixinfo.widgets.SpannableStringUtils;
import com.marksixinfo.widgets.WithdrawDepositDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;

/**
 * 功能描述: 提现兑换
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/4/19 0019 14:59
 */
public class WithdrawDepositActivity extends MarkSixWhiteActivity implements View.OnClickListener {

    NumberAnimTextView tvCashReserve;
    @BindView(R.id.recyclerView)
    ListView recyclerView;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;


    private List<WithdrawDepositData.ChannelBean.ListBean> list = new ArrayList<>();
    private WithdrawDepositDialog commonInputDialog;
    private SimpleAdapter<WithdrawDepositData.ChannelBean.ListBean> adapter;
    private int withdrawMin;//最小提金额
    private String blance;//当前金额

    @Override
    public int getViewId() {
        return R.layout.activity_withdraw_deposit;
    }

    @Override
    public void afterViews() {

        markSixTitleWhite.init("提现兑换", "", "", R.drawable.icon_explain_title_right, this);

        View headView = View.inflate(this, R.layout.item_withdraw_deposit_head, null);
        tvCashReserve = headView.findViewById(R.id.tv_cash_reserve);
        recyclerView.addHeaderView(headView);

        adapter = new SimpleAdapter<WithdrawDepositData.ChannelBean.ListBean>(this, list, R.layout.item_withdraw_deposit) {
            @Override
            public void convert(ViewHolder holder, WithdrawDepositData.ChannelBean.ListBean item) {
                if (item != null) {
                    String name = item.getName();
                    if (CommonUtils.StringNotNull(name) && name.contains("优7")) {
                        SpannableStringUtils sp = new SpannableStringUtils();
                        sp.addText(17, 0xff333333, name);
                        sp.addText(17, 0xfffc5c66, "(提现1.5倍)");
                        holder.setText(R.id.tv_platform_name, sp.toSpannableString());
                        holder.setVisible(R.id.tv_platform_dom, true);
                    } else {
                        holder.setText(R.id.tv_platform_name, name);
                        holder.getConvertView().setOnClickListener(null);
                        holder.setVisible(R.id.tv_platform_dom, false);
                    }

                    holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            withdrawDepositDialog(item.getType(), item.getId(), item.getName());
                        }
                    });

                    holder.setOnClickListener(R.id.tv_withdraw_deposit, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            withdrawDepositDialog(item.getType(), item.getId(), item.getName());
                        }
                    });
                }
            }
        };
        recyclerView.setAdapter(adapter);
        mLoadingLayout.setRetryListener(this);
        refreshLayout.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refresh(true);
            }
        });

        startData(true);
    }

    @Override
    public void finish() {
        if (tvCashReserve != null && tvCashReserve.getAnimator() != null) {
            tvCashReserve.getAnimator().cancel();
        }
        super.finish();
    }

    private void startData(boolean init) {
        if (init) {
            refresh(true);
        } else {
            if (refreshLayout != null) {
                //触发自动刷新
                refreshLayout.autoRefresh();
            }
        }
    }

    /**
     * 设置余额显示
     *
     * @param response
     * @param isRefresh
     */
    private void setDetailData(WithdrawDepositData response, boolean isRefresh) {
        if (response != null) {
            withdrawMin = response.getWithdraw_Min();
            if (CommonUtils.StringNotNull(response.getBlance())
                    && !response.getBlance().equals(blance)) {
                blance = response.getBlance();
                tvCashReserve.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvCashReserve.setHadThousands(false);
                        tvCashReserve.setFractionDigits(2);
                        tvCashReserve.setNumberString(CommonUtils.StringNotNull(blance)
                                ? MoneyUtils.getIntMoneyText(blance) : "0");
                    }
                }, isRefresh ? NumberConstants.ANIM_TEXT_VIEW_TIME : 0);
            }
        }
    }

    /**
     * 请求网络
     *
     * @param isRefresh
     */

    private void refresh(boolean isRefresh) {
        new HeadlineImpl(new MarkSixNetCallBack<WithdrawDepositData>(this, WithdrawDepositData.class) {
            @Override
            public void onSuccess(WithdrawDepositData response, int id) {
                setDetailData(response, isRefresh);
                setData(response);
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
                loadDone();
            }
        }.setNeedDialog(false)).getWithdrawDepositList();

    }


    /**
     * 请求完成
     */
    private void loadDone() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.resetNoMoreData();
            refreshLayout.finishLoadMore();
        }
    }


    /**
     * 设置数据
     *
     * @param response
     */
    private void setData(WithdrawDepositData response) {
        list.clear();
        if (response != null && CommonUtils.ListNotNull(response.getChannel())) {
            List<WithdrawDepositData.ChannelBean> channel = response.getChannel();
            for (int i = 0; i < channel.size(); i++) {
                WithdrawDepositData.ChannelBean channelBean = channel.get(i);
                if (channelBean != null) {
                    List<WithdrawDepositData.ChannelBean.ListBean> list = channelBean.getList();
                    if (CommonUtils.ListNotNull(list)) {
                        for (WithdrawDepositData.ChannelBean.ListBean listBean : list) {
                            if (listBean != null) {
                                listBean.setType(i);
                                this.list.add(listBean);
                            }
                        }
                    }
                }
            }
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
        }
        if (adapter != null) {
            adapter.changeDataUi(list);
        }
        if (CommonUtils.ListNotNull(list)) {
            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }
    }


    /**
     * 提现弹框
     *
     * @param type
     * @param id
     * @param name
     */
    private void withdrawDepositDialog(int type, String id, String name) {
        if (!CommonUtils.StringNotNull(blance) || "0.00".equals(blance) || "0".equals(blance)) {
            toast("没有现金可以提现,快去做任务赚取吧");
            return;
        }
        if (NumberUtils.stringToDouble(blance) < withdrawMin) {
            toast("最小提现金额为" + withdrawMin + ",快去做任务赚取吧");
            return;
        }
        if (commonInputDialog == null) {
            commonInputDialog = new WithdrawDepositDialog(this);
        }
        EditText editText = commonInputDialog.getEditInputNumber();
        //输入两位小数
        editText.setFilters(new InputFilter[]{new MoneyValueFilter()});
        double blanceNumber = NumberUtils.stringToDouble(blance);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //限制最大输入
                double i = NumberUtils.stringToDouble(editText.getText().toString().trim());
                if (i > blanceNumber) {
                    i = blanceNumber;
                    toast("当前余额为" + blanceNumber);
                    editText.removeTextChangedListener(this);
                    editText.setText(i > 0 ? String.valueOf(i) : "");
                    if (i > 0) {
                        editText.setSelection(String.valueOf(i).length());
                    }
                    editText.addTextChangedListener(this);
                }

            }
        });
        commonInputDialog.show(type, name, "取消", "确定", null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withdrawDeposit(type, id, commonInputDialog.getInputNumber(), commonInputDialog.getInputAccount(), commonInputDialog.getEditInputName());
            }
        });
    }

    /**
     * 提现接口
     *
     * @param id
     * @param number
     */
    private void withdrawDeposit(int type, String id, String number, String account, String name) {

        if (!CommonUtils.StringNotNull(number)) {
            toast("请输入金额");
            return;
        }
        //0,网站平台  1,银行账户   2,支付宝
        if (type != 0 && !CommonUtils.StringNotNull(name)) {
            toast("请输入您的真实姓名");
            return;
        }
        if (!CommonUtils.StringNotNull(account)) {
            toast("请输入账号");
            return;
        }
        if (NumberUtils.stringToDouble(number) < withdrawMin) {
            toast("最小提现金额为" + withdrawMin);
            if (commonInputDialog != null) {
                commonInputDialog.setInputNumber(String.valueOf(withdrawMin));
            }
            return;
        }
        if (commonInputDialog != null && commonInputDialog.isShowing()) {
            commonInputDialog.dismiss();
        }
        new HeadlineImpl(new MarkSixNetCallBack<Object>(this, Object.class) {
            @Override
            public void onSuccess(Object response, int id) {
                toast("提现成功");
                refresh(false);
            }
        }).withdrawDepositNumber(String.valueOf(type), id, number, account);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_function://说明
                startClass(R.string.WebViewActivity, IntentUtils.getHashObj(new String[]{
                        StringConstants.URL, UrlStaticConstants.WITHDRAW_DEPOSIT}));
                break;
            case R.id.retry_button://网络错误
                mLoadingLayout.showLoading();
                startData(true);
                break;
        }
    }
}
