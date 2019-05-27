package com.marksixinfo.ui.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.StandbyRemarkAdapter;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.bean.StandbyRemarkData;
import com.marksixinfo.interfaces.IStandbyRemark;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
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
public class StandbyRemarkActivity extends MarkSixWhiteActivity implements View.OnClickListener, IStandbyRemark, TextWatcher {


    TextView tvEditState;
    TextView tvEditFinish;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.ed_add_remark)
    EditText edAddRemark;
    @BindView(R.id.ll_add_remark)
    LinearLayout llAddRemark;
    @BindView(R.id.tv_save_remark)
    TextView tvSaveRemark;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;

    private boolean isEditState;
    private StandbyRemarkAdapter adapter;
    List<StandbyRemarkData> list = new ArrayList<>();

    @Override
    public int getViewId() {
        return R.layout.activity_standby_remark;
    }

    @Override
    public void afterViews() {
        markSixTitleWhite.init("备用网址", "", "编辑", 0, this);
        tvEditState = markSixTitleWhite.getTvTitle();
        tvEditState.setVisibility(View.GONE);
        tvEditFinish = markSixTitleWhite.getTvFunction();
        tvEditFinish.setVisibility(View.INVISIBLE);

        mLoadingLayout.setRetryListener(this);
        edAddRemark.addTextChangedListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new StandbyRemarkAdapter(this, list, this);
        recyclerView.setAdapter(adapter);

        getDataList();
    }


    /**
     * 获取列表数据
     */
    private void getDataList() {
        new HeadlineImpl(new MarkSixNetCallBack<List<StandbyRemarkData>>(this, StandbyRemarkData.class) {
            @Override
            public void onSuccess(List<StandbyRemarkData> response, int id) {
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
    private void setData(List<StandbyRemarkData> response) {
        list.clear();
        if (CommonUtils.ListNotNull(response)) {
            list.addAll(response);
        }
        if (adapter != null) {
            adapter.changeDataUi(list);
        }
        if (CommonUtils.ListNotNull(list)) {
            tvEditFinish.setVisibility(View.VISIBLE);
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
    private void addRemark(String url) {
        new HeadlineImpl(new MarkSixNetCallBack(this, Object.class) {
            @Override
            public void onSuccess(Object response, int id) {
                toast("添加成功");
                getDataList();
            }
        }.setNeedDialog(false)).addStandbyDomain(url);
    }

    /**
     * 删除备用网址
     *
     * @param id
     */
    private void deleteRemark(String id) {
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
                notifyUI();
                break;
            case R.id.tv_save_remark://添加网址
                String s = edAddRemark.getText().toString().trim();
                if (!CommonUtils.StringNotNull(s)) {
                    return;
                }
                addRemark(s);
                break;
            case R.id.retry_button://网络错误
                mLoadingLayout.showLoading();
                getDataList();
                break;
        }
    }

    /**
     * 刷新数据
     */
    private void notifyUI() {
        isEditState = !isEditState;
        if (CommonUtils.ListNotNull(list)) {
            for (StandbyRemarkData data : list) {
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
        llAddRemark.setVisibility(isEditState ? View.VISIBLE : View.GONE);
        tvEditState.setVisibility(isEditState ? View.VISIBLE : View.GONE);
    }

    @Override
    public void delRemark(String id) {
        deleteRemark(id);
    }

    @Override
    public void showDelete(int position) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence string, int start, int before, int count) {
        String s = edAddRemark.getText().toString().trim();
        if (CommonUtils.StringNotNull(s)) {
            tvSaveRemark.setBackgroundColor(0xff42c056);
        } else {
            tvSaveRemark.setBackgroundColor(0xffdddddd);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
