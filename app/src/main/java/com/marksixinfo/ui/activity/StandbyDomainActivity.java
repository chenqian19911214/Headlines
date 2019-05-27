package com.marksixinfo.ui.activity;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.StandbyDomainAdapter;
import com.marksixinfo.base.CommonDialog;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.bean.StandbyDomainData;
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.interfaces.IStandbyDomain;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EmojiFilter;
import com.marksixinfo.utils.TextChangedListener;
import com.marksixinfo.widgets.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 功能描述: 备用网址
 *
 * @auther: Administrator
 * @date: 2019/5/25 0025 14:44
 */
public class StandbyDomainActivity extends MarkSixWhiteActivity implements View.OnClickListener, IStandbyDomain, TextWatcher {


    TextView tvEditState;
    TextView tvEditFinish;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ed_add_domain)
    EditText edAddDomain;
    @BindView(R.id.ll_add_domain)
    LinearLayout llAddDomain;
    @BindView(R.id.tv_save_domain)
    TextView tvSaveDomain;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;

    private boolean isEditState;
    private StandbyDomainAdapter adapter;
    List<StandbyDomainData> list = new ArrayList<>();
    private CommonDialog commonDialog;

    @Override
    public int getViewId() {
        return R.layout.activity_standby_domain;
    }

    @Override
    public void afterViews() {
        markSixTitleWhite.init("备用网址", "", "编辑", 0, this);
        tvEditState = markSixTitleWhite.getTvTitle();
        tvEditFinish = markSixTitleWhite.getTvFunction();
        tvEditFinish.setVisibility(View.INVISIBLE);

        mLoadingLayout.setRetryListener(this);
        tvSaveDomain.setOnClickListener(this);
        edAddDomain.addTextChangedListener(this);
        //限制长度
        edAddDomain.setFilters(new InputFilter[]{new InputFilter.LengthFilter(NumberConstants.DOMAIN_LENGTH), new EmojiFilter()});
        //不能输入中文
        TextChangedListener.StringWatcher(edAddDomain);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new StandbyDomainAdapter(this, list, this);
        recyclerView.setAdapter(adapter);

        getDataList();
    }


    /**
     * 获取列表数据
     */
    private void getDataList() {
        new HeadlineImpl(new MarkSixNetCallBack<List<StandbyDomainData>>(this, StandbyDomainData.class) {
            @Override
            public void onSuccess(List<StandbyDomainData> response, int id) {
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
        }.setNeedDialog(false)).getDomainList();
    }

    /**
     * 设置列表
     *
     * @param response
     */
    private void setData(List<StandbyDomainData> response) {
        list.clear();
        if (CommonUtils.ListNotNull(response)) {
            list.addAll(response);
        }
        notifyData();
        tvEditFinish.setVisibility(View.VISIBLE);
        if (CommonUtils.ListNotNull(list)) {
            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }
    }


    /**
     * 添加备用网址
     *
     * @param url
     */
    private void addDomain(String url) {
        new HeadlineImpl(new MarkSixNetCallBack(this, Object.class) {
            @Override
            public void onSuccess(Object response, int id) {
                toast("添加成功");
                getDataList();
                if (edAddDomain != null) {
                    edAddDomain.setText("");
                }
            }
        }.setNeedDialog(false)).addStandbyDomain(url);
    }

    /**
     * 删除备用网址
     *
     * @param id
     */
    private void deleteDomain(String id) {
        new HeadlineImpl(new MarkSixNetCallBack(this, Object.class) {
            @Override
            public void onSuccess(Object response, int id) {
                toast("删除成功");
                getDataList();
            }
        }.setNeedDialog(false)).delStandbyDomain(id);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_function://编辑/完成编辑
                isEditState = !isEditState;
                notifyUI();
                break;
            case R.id.tv_save_domain://添加网址
                String s = edAddDomain.getText().toString().trim();
                if (!CommonUtils.StringNotNull(s)) {
                    return;
                }
                if (noneRepeatDomain(s)) {
                    addDomain(s);
                }
                break;
            case R.id.retry_button://网络错误
                mLoadingLayout.showLoading();
                getDataList();
                break;
        }
    }


    /**
     * 设置初始值
     */
    private void notifyData() {
        isEditState = false;
        notifyUI();
    }

    /**
     * 刷新数据
     */
    private void notifyUI() {
        if (CommonUtils.ListNotNull(list)) {
            for (StandbyDomainData data : list) {
                if (data != null) {
                    data.setEditState(isEditState);
                    data.setExpand(false);
                }
            }
        }
        if (adapter != null) {
            adapter.changeDataUi(list);
        }
        tvEditFinish.setText(isEditState ? "完成" : "编辑");
        llAddDomain.setVisibility(isEditState ? View.VISIBLE : View.GONE);
        tvEditState.setText(isEditState ? "网址编辑" : "备用网址");
    }

    @Override
    public void delDomain(String id) {
        if (commonDialog == null) {
            commonDialog = new CommonDialog(this);
        }
        commonDialog.show("是否删除此网址？", "", "取消", "确定", null, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commonDialog != null && commonDialog.isShowing()) {
                    commonDialog.dismiss();
                }
                deleteDomain(id);
            }
        });
    }

    @Override
    public void showDelete(int position) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence string, int start, int before, int count) {
        String s = edAddDomain.getText().toString().trim();
        if (CommonUtils.StringNotNull(s)) {
            tvSaveDomain.setBackgroundColor(0xff42c056);
        } else {
            tvSaveDomain.setBackgroundColor(0xffdddddd);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    /**
     * 重复网址不能添加
     *
     * @param s
     */
    private boolean noneRepeatDomain(String s) {
        if (CommonUtils.ListNotNull(list)) {
            for (StandbyDomainData data : list) {
                if (data != null) {
                    if (s.equals(data.getUrl())) {
                        toast("网址已存在");
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
