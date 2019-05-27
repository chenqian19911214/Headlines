package com.marksixinfo.ui.activity;

import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.adapter.MineConcernAdapter;
import com.marksixinfo.base.CommonDialog;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.bean.MineConcernData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.CommListDataEvent;
import com.marksixinfo.interfaces.IMineConcernInterface;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.NumberUtils;
import com.marksixinfo.widgets.LoadingLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;


/**
 * 我的关注
 *
 * @auther: Administrator
 * @date: 2019/3/26 0026 21:14
 */
public class MineConcernActivity extends MarkSixActivity implements IMineConcernInterface, View.OnClickListener {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;

    private List<MineConcernData> list = new ArrayList<>();
    private MineConcernAdapter adapter;
    private int pageIndex = 0;//页码
    private CommonDialog commonDialog;
    private int type = 0;//分类 0,头条  1,论坛

    @Override
    public int getViewId() {
        return R.layout.activity_mine_concern;
    }


    @Override
    public void afterViews() {

        type = NumberUtils.stringToInt(getStringParam(StringConstants.TYPE));

        markSixTitle.init("我的关注");

        adapter = new MineConcernAdapter(this, list, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        mLoadingLayout.setRetryListener(this);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refresh(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refresh(false);
            }
        });
        startData();
    }

    /**
     * 刷新
     */
    private void startData() {
//        refreshLayout.autoRefresh()();
        refreshLayout.autoRefresh();
    }

    /**
     * 请求网络
     *
     * @param isRefresh
     */
    private void refresh(boolean isRefresh) {
        if (isRefresh) {
            pageIndex = 0;
        }
        new HeadlineImpl(new MarkSixNetCallBack<List<MineConcernData>>(this, MineConcernData.class) {
            @Override
            public void onSuccess(List<MineConcernData> response, int id) {
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
        }.setNeedDialog(false)).mineConcern(String.valueOf(++pageIndex), "1");

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
    private void setData(boolean isRefresh, List<MineConcernData> response) {
        if (isRefresh) {
            list.clear();
        }
        if (response != null && CommonUtils.ListNotNull(response)) {
            list.addAll(response);
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
        }
        if (adapter != null) {
            adapter.notifyUI(list);
        }
        if (CommonUtils.ListNotNull(list)) {
            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }
    }


    /**
     * 取消关注
     *
     * @param position
     */
    private void cancelConcern(int position) {
        if (CommonUtils.ListNotNull(list)) {
            MineConcernData listBean = list.get(position);
            if (listBean != null) {
                String userId = listBean.getMember_Id();
                new HeadlineImpl(new MarkSixNetCallBack<Object>(this, Object.class) {
                    @Override
                    public void onSuccess(Object response, int id) {
                        toast("取消成功");
                        refreshEventData(userId, 0);
                    }
                }).mineConcernCancel(userId);
            }
        }
    }

    /**
     * 接口刷新实体
     */
    private void refreshEventData(String userId, int is_look) {
        EventBusUtil.post(new CommListDataEvent(CommonUtils.getMainHomeData(userId, is_look)));
    }


    /**
     * 帖子数据变更
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommListDataEvent event) {
        if (event != null && event.getData() != null) {
            MainHomeData data = event.getData();
            if (data.getLook_status() == 0 && CommonUtils.ListNotNull(list)) {
                CommonUtils.mineConcernDataChange(data, list);
                if (adapter != null) {
                    adapter.notifyUI(list);
                }
                if (!CommonUtils.ListNotNull(list)) {
                    startData();
                }
            } else {
                refresh(true);
            }
        }
    }

    /**
     * 点击取消关注
     *
     * @param position
     */
    @Override
    public void deleteConcern(int position) {
        if (commonDialog == null) {
            commonDialog = new CommonDialog(this);
        }
        commonDialog.show("是否取消关注", "", "取消", "确定", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelConcern(position);
                if (commonDialog != null && commonDialog.isShowing()) {
                    commonDialog.dismiss();
                }
            }
        });
    }

    /**
     * 查看用户详情
     *
     * @param id
     */
    @Override
    public void checkUser(String id) {
        goToUserCenterActivity(id, type);
    }

    @Override
    public void onClick(View view) {
        mLoadingLayout.showLoading();
        startData();
    }
}
