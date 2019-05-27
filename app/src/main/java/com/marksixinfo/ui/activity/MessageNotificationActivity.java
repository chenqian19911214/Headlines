package com.marksixinfo.ui.activity;

import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.adapter.MessageNotificationAdapter;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.base.SimpleAdapter2;
import com.marksixinfo.bean.MessageNotificationData;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.LoadingLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 功能描述:  消息通知
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/4/19 0019 16:07
 */
public class MessageNotificationActivity extends MarkSixWhiteActivity implements View.OnClickListener
        , SucceedCallBackListener<MessageNotificationData> {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;


    private List<MessageNotificationData> list = new ArrayList<>();
    private SimpleAdapter2<MessageNotificationData> adapter;
    private int pageIndex = 0;//页码


    @Override
    public int getViewId() {
        return R.layout.activity_message_notification;
    }

    @Override
    public void afterViews() {

        markSixTitleWhite.init("消息通知");

        adapter = new MessageNotificationAdapter(this, list, this);
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
        new HeadlineImpl(new MarkSixNetCallBack<List<MessageNotificationData>>(this, MessageNotificationData.class) {
            @Override
            public void onSuccess(List<MessageNotificationData> response, int id) {
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
        }.setNeedDialog(false)).getMessageNotification(String.valueOf(++pageIndex));

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
    private void setData(boolean isRefresh, List<MessageNotificationData> response) {
        if (isRefresh) {
            list.clear();
        }
        if (CommonUtils.ListNotNull(response)) {
            list.addAll(response);
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

    @Override
    public void onClick(View view) {
        mLoadingLayout.showLoading();
        startData();
    }


    /**
     * 条目点击
     *
     * @param o
     */
    @Override
    public void succeedCallBack(MessageNotificationData o) {
        if (o != null) {
            int type = o.getType();//0:系统消息 1:关注提醒 2:回复提醒 3:帖子点赞通知 4:我的回复点赞通知
            switch (type) {
                case 1: //去个人中心
                    MessageNotificationData.SendByBean sendBy = o.getSendBy();
                    if (sendBy != null) {
                        goToUserCenterActivity(sendBy.getId(), 0);
                    }
                    break;
                case 2: //去关注
                    goToDetailActivityComment(o.getReply_Id());
                    break;
                case 3: //去帖子
                    goToDetailActivity(o.getLike_Id()+"");
                    break;
                case 4: //去回复的回复
                    goToCommentListActivity(o.getLike_Id()+"", 0);
                    break;
                default:
//                        gotoMainActivity(0);
                    break;
            }
        }
    }
}
