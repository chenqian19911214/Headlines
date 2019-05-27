package com.marksixinfo.ui.activity;

import android.content.Context;
import android.view.View;
import android.widget.ExpandableListView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.MineCommentAdapter;
import com.marksixinfo.adapter.ViewHolder;
import com.marksixinfo.base.CommonDialog;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.MineCommentData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.CommListDataEvent;
import com.marksixinfo.evenbus.DeleteListCommentEvent;
import com.marksixinfo.interfaces.IMinCommentInterface;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.net.impl.ForumImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.NumberUtils;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.dockingexpandablelistview.adapter.DockingExpandableListViewAdapter;
import com.marksixinfo.widgets.dockingexpandablelistview.controller.IDockingHeaderUpdateListener;
import com.marksixinfo.widgets.dockingexpandablelistview.view.DockingExpandableListView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;

/**
 * 功能描述: 个人中心,我的回复
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/3/31 0031 18:14
 */
public class MineCommentActivity extends MarkSixActivity implements IMinCommentInterface, View.OnClickListener {


    @BindView(R.id.recycler_view)
    DockingExpandableListView mListView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;

    private List<MineCommentData> list = new ArrayList<>();
    private MineCommentAdapter adapter;
    private HeadViewHolder headViewHolder;
    private DockingExpandableListViewAdapter listViewAdapter;
    private int pageIndex = 0;//页码
    private CommonDialog commonDialog;
    private int type = 0;//分类 0,头条  1,论坛
    private boolean isRefresh;
    int groupPosition, childPosition;

    @Override
    public int getViewId() {
        return R.layout.activity_mine_comment;
    }

    @Override
    public void afterViews() {

        type = NumberUtils.stringToInt(getStringParam(StringConstants.TYPE));

        markSixTitle.init(type == 0 ? "我的回复" : "我的评论");

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
        mLoadingLayout.setRetryListener(this);
        mListView.setGroupIndicator(null);
        mListView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter = new MineCommentAdapter(getContext(), list, this, type);
        listViewAdapter = new DockingExpandableListViewAdapter(mListView, adapter);
        mListView.setAdapter(listViewAdapter);
        //不能点击收缩
        mListView.setExpandGroupAll(true);
        mListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });


        //悬浮headView
        View headerView = getLayoutInflater().inflate(R.layout.item_mine_comment_group, mListView, false);
        mListView.setDockingHeader(headerView, new IDockingHeaderUpdateListener() {

            /**
             * 点击跳转详情
             */
            private String id;

            @Override
            public void onUpdate(View headerView, int groupPosition, boolean expanded) {
                MineCommentData ListBean = list.get(groupPosition);
                if (ListBean != null) {
                    id = ListBean.getId();
                    String face = ListBean.getFace();
                    String Nickname = ListBean.getNickname();
                    String add_time = ListBean.getAdd_Time();
                    if (headViewHolder == null) {
                        headViewHolder = new HeadViewHolder(getContext(), headerView);
                    }
                    headViewHolder.setText(R.id.tv_Nickname, CommonUtils.StringNotNull(Nickname) ? Nickname : "");
                    headViewHolder.setText(R.id.tv_comment_time, CommonUtils.StringNotNull(add_time) ? add_time : "");
                    headViewHolder.setCircleImageView(R.id.iv_user_photo, face);
                }
            }

            @Override
            public void onClickHead() {
                checkComment(id);
            }
        });
        startData(true);
    }

    /**
     * 开始加载,下拉动画
     */
    private void startData(boolean init) {
        if (init) {
            refresh(true);
        } else {
            //触发自动刷新
            refreshLayout.autoRefresh();
        }
    }



    /**
     * 加载
     *
     * @param isRefresh
     */
    private void refresh(boolean isRefresh) {
        this.isRefresh = isRefresh;
        if (isRefresh) {
            pageIndex = 0;
        }
        if (type == 0) {
            new HeadlineImpl(callBack.setNeedDialog(false)).mineComment(String.valueOf(++pageIndex));
        } else {
            new ForumImpl(callBack.setNeedDialog(false)).mineComment(String.valueOf(++pageIndex));
        }
    }

    MarkSixNetCallBack callBack = (MarkSixNetCallBack) new MarkSixNetCallBack<List<MineCommentData>>(this, MineCommentData.class) {
        @Override
        public void onSuccess(List<MineCommentData> response, int id) {
            setData(response, isRefresh);
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
    };


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
     * @param response
     * @param isRefresh
     */
    private void setData(List<MineCommentData> response, boolean isRefresh) {
        if (isRefresh) {
            list.clear();
        }
        if (CommonUtils.ListNotNull(response)) {
            list.addAll(response);
            //展开所有
            expandAllGroup();
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
        }
        if (adapter != null && listViewAdapter != null) {
            adapter.notifyUI(list);
            listViewAdapter.notifyUI(adapter);
        }
        if (CommonUtils.ListNotNull(list)) {
            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }
    }

    /**
     * 展开所有
     */
    private void expandAllGroup() {
        if (CommonUtils.ListNotNull(list)) {
            for (int i = 0; i < list.size(); i++) {
                mListView.expandGroup(i);
            }
        }
    }

    /**
     * 删除回复
     *
     * @param id
     */
    private void mDeleteComment(String id) {
        if (type == 0) {
            new HeadlineImpl(deleteCallBack).mineDeleteComment(String.valueOf(id));
        } else {
            new ForumImpl(deleteCallBack).mineDeleteComment(String.valueOf(id));
        }
    }

    MarkSixNetCallBack deleteCallBack = new MarkSixNetCallBack<Object>(this, Object.class) {
        @Override
        public void onSuccess(Object response, int id) {
            deleteCommentByPosition();
        }
    };

    /**
     * 删除回复接口
     */
    private void deleteCommentByPosition() {
        if (CommonUtils.ListNotNull(list)) {
            try {
                List<MineCommentData.ReplyBean> reply = list.get(groupPosition).getReply();
                reply.remove(childPosition);
                if (reply.size() <= 0) {
                    list.remove(groupPosition);
                }
                adapter.notifyUI(this.list);
                listViewAdapter.notifyUI(adapter);
                toast("删除成功");
                if (type == 1) {//论坛删除回复通知刷新
                    EventBusUtil.post(new DeleteListCommentEvent());
                }
                if (!CommonUtils.ListNotNull(list)) {//删除完最后一条刷新接口
                    startData(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 点击删除
     *
     * @param groupPosition
     * @param childPosition
     * @param id
     */
    @Override
    public void deleteComment(int groupPosition, int childPosition, String id) {
        this.groupPosition = groupPosition;
        this.childPosition = childPosition;
        if (commonDialog == null) {
            commonDialog = new CommonDialog(this);
        }
        commonDialog.show("是否删除" + (type == 0 ? "回复" : "评论"), "", "取消", "确定", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDeleteComment(id);
                if (commonDialog != null && commonDialog.isShowing()) {
                    commonDialog.dismiss();
                }
            }
        });
    }

    /**
     * 跳转详情页回复位置
     *
     * @param id
     */
    @Override
    public void checkComment(String id) {
        if (type == 0) {
            goToDetailActivityComment(id);
        } else {
            goToForumDetailComment(id);
        }
    }

    /**
     * 详情页
     *
     * @param id
     */
    @Override
    public void checkDetail(String id) {
        if (type == 0) {
            goToDetailActivity(id);
        } else {
            goToForumDetailActivity(id);
        }
    }

    /**
     * 如果有图片,查看图片
     *
     * @param list
     * @param index
     */
    @Override
    public void clickPhoto(List<String> list, int index) {
        CommonUtils.checkImagePreview(getContext(), list, index);
    }

    /**
     * 网络重试
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        mLoadingLayout.showLoading();
        startData(true);
    }


    static class HeadViewHolder extends ViewHolder {

        public HeadViewHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }


    /**
     * 详情页数据变更,直接刷新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommListDataEvent event) {
        if (event != null && event.getData() != null) {
            refresh(true);
        }
    }

}
