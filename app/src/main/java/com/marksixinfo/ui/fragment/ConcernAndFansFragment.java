package com.marksixinfo.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.adapter.MineConcernAndFansAdapter;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.bean.MineConcernData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.CommListDataEvent;
import com.marksixinfo.interfaces.IMineConcernInterface;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
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
 * 我的关注与粉丝列表
 *
 * @Auther: Administrator
 * @Date: 2019/4/19 0019 22:33
 * @Description:
 */
public class ConcernAndFansFragment extends PageBaseFragment implements IMineConcernInterface, View.OnClickListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;


    private List<MineConcernData> list = new ArrayList<>();
    private MineConcernAndFansAdapter adapter;
    private int pageIndex = 0;//页码
    private int parentType;//0,头条 1,论坛
    private int type;//1,关注 2,粉丝


    @Override
    public int getViewId() {
        return R.layout.fragment_concern_and_fans;
    }

    @Override
    protected void afterViews() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            parentType = bundle.getInt(StringConstants.PARENT_TYPE);
            type = bundle.getInt(StringConstants.TYPE);
        }

        adapter = new MineConcernAndFansAdapter(getContext(), list, this);
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
    }

    /**
     * 刷新
     */
    private void startData() {
        refresh(true);
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
        }.setNeedDialog(false)).mineConcern(String.valueOf(++pageIndex)
                , String.valueOf(type));

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
        setCollectGone();
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
     * 粉丝页面关注按钮不可见
     */
    private void setCollectGone() {
        if (CommonUtils.ListNotNull(list)) {
            for (MineConcernData data : list) {
                data.setLook_status(type == 2 ? 2 : 1);
            }
        }
    }

    /**
     * 关注接口
     *
     * @param position
     */
    private void concern(int position) {
        if (CommonUtils.ListNotNull(list)) {
            MineConcernData listBean = list.get(position);
            if (listBean != null) {
                int status = listBean.getLook_status();
                listBean.setLook_status(-1);//接口中
                String userId = listBean.getMember_Id();
                new HeadlineImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                    @Override
                    public void onSuccess(LikeAndFavoriteData response, int id) {
                        if (response != null) {
                            refreshEventData(userId, response.getType());
                        }
                    }

                    @Override
                    public void onError(String msg, String code) {
                        super.onError(msg, code);
                        listBean.setLook_status(status);
                        if (adapter != null) {
                            adapter.notifyUI(list);
                        }
                    }
                }.setNeedDialog(false)).concernUser(userId);
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
        if (event != null && event.getData() != null && type == 1) {
            MainHomeData data = event.getData();
            if (CommonUtils.ListNotNull(list)) {
                CommonUtils.mineConcernDataChange(data, list);
                if (adapter != null) {
                    adapter.notifyUI(list);
                }
            } else {
                refresh(true);
            }
        }
    }

    /**
     * 点击取消/关注
     *
     * @param position
     */
    @Override
    public void deleteConcern(int position) {
//        cancelConcern(position);
        concern(position);
    }

    /**
     * 查看用户详情
     *
     * @param id
     */
    @Override
    public void checkUser(String id) {
        goToUserCenterActivity(id, parentType);
    }

    @Override
    public void onClick(View view) {
        mLoadingLayout.showLoading();
        startData();
    }

    @Override
    protected void loadData() {
        startData();
    }
}