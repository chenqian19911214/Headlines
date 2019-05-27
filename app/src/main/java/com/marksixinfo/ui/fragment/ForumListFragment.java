package com.marksixinfo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.adapter.ForumListAdapter;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.bean.ForumCommentData;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.CommListDataEvent;
import com.marksixinfo.evenbus.DeleteInvitationEvent;
import com.marksixinfo.evenbus.DeleteListCommentEvent;
import com.marksixinfo.evenbus.LoginSuccessEvent;
import com.marksixinfo.evenbus.MainClickIndexEvent;
import com.marksixinfo.evenbus.TokenTimeOutEvent;
import com.marksixinfo.interfaces.IForumListInterface;
import com.marksixinfo.net.impl.ForumImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.backedittext.BackInputDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;

/**
 * 论坛列表
 *
 * @Auther: Administrator
 * @Date: 2019/3/20 0020 12:10
 * @Description:
 */
public class ForumListFragment extends PageBaseFragment implements View.OnClickListener,
        IForumListInterface, BackInputDialog.InputTextListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;

    List<MainHomeData> list = new ArrayList<>();
    private ForumListAdapter adapter;
    private int type;//0,关注 1,广场
    private int pageIndex = 0;//页数
    private BackInputDialog backInputDialog;


    @Override
    public int getViewId() {
        return R.layout.fragment_forum_list;
    }

    @Override
    protected void afterViews() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt(StringConstants.TYPE);
        }

        assert getContext() != null;
        adapter = new ForumListAdapter(getContext(), list, type, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);//单条刷新无动画
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getContext().getResources().getDrawable(R.drawable.shape_item_decoration));
        recyclerView.addItemDecoration(decoration);
        mLoadingLayout.setRetryListener(this);
        recyclerView.setAdapter(adapter);
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

        //未登录
        if (type == 0 && !isLogin()) {
            showEmpty();
        }
    }

    /**
     * 获取列表
     *
     * @param ifRefresh
     */
    private void refresh(boolean ifRefresh) {
        if (type == 0 && !isLogin()) {
            loadDone(ifRefresh);
            return;
        }
        if (ifRefresh) {
            pageIndex = 0;
        }
        new ForumImpl(new MarkSixNetCallBack<List<MainHomeData>>(this, MainHomeData.class) {
            @Override
            public void onSuccess(List<MainHomeData> response, int id) {
                setData(ifRefresh, response);
            }

            @Override
            public void onError(String msg, String code) {
                if (!CommonUtils.ListNotNull(list)) {
                    showError();
                } else {
                    super.onError(msg, code);
                }
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                loadDone(ifRefresh);
            }

        }.setNeedDialog(false)).getForumConcernList(String.valueOf(++pageIndex), type == 1);
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
     * @param isRefresh 是否刷新
     * @param response  请求的数据
     */
    private void setData(boolean isRefresh, List<MainHomeData> response) {
        if (isRefresh) {
            list.clear();
        }
        if (CommonUtils.ListNotNull(response)) {
            list.addAll(response);
            setStatusTwo();
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
        }
        if (adapter != null) {
            adapter.notifyUI(list);
        }
        handlerEmpty();
    }

    /**
     * 设置不显示关注
     */
    private void setStatusTwo() {
        if (CommonUtils.ListNotNull(list) && type == 0) {
            for (MainHomeData mainHomeData : list) {
                if (mainHomeData != null) {
                    mainHomeData.setLook_status(2);
                }
            }
        }
    }

    /**
     * 处理空太页
     */
    private void handlerEmpty() {
        if (CommonUtils.ListNotNull(list)) {
            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }
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
     * 回复框
     *
     * @param position
     */
    private void commentDialog(int position) {
        if (isLoginToast()) {
            if (backInputDialog == null) {
                assert getContext() != null;
                backInputDialog = new BackInputDialog(getContext(), this);
            }
            MainHomeData data = CommonUtils.getDataByPosition(list, position);
            if (data != null) {
                backInputDialog.setCommentPosition(position).show("回复@" + data.getNickname() + "：");
            }
        }
    }

    /**
     * 回复dialog
     *
     * @param text
     */
    @Override
    public void onInputText(String text) {
        if (backInputDialog != null) {
            if (backInputDialog.getCommentPosition() != -1) {
                comment(text, backInputDialog.getCommentPosition());
            } else {
                commentUser(text, backInputDialog.getId());
            }
        }
    }

    /**
     * 回复帖子
     *
     * @param position
     */
    private void comment(String text, int position) {
        MainHomeData data = CommonUtils.getDataByPosition(list, position);
        if (data != null) {
            new ForumImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                @Override
                public void onSuccess(LikeAndFavoriteData response, int id) {
                    if (response != null) {
                        toast("回复成功");
                        //评论数
                        data.setReply_Num(response.getNum());
                        handlerCommentList(data, text, response.getId());
                        adapter.notifyUI(list);
                    }
                }
            }).comments(data.getId(), text);
        }
    }

    /**
     * 回复评论
     */
    private void commentUser(String text, String id) {
        new ForumImpl(new MarkSixNetCallBack<Object>(this, Object.class) {
            @Override
            public void onSuccess(Object response, int id) {
                if (response != null) {
                    toast("回复成功");
                }
            }
        }).commentsUser(id, text);
    }


    /**
     * 处理新回复的内容到列表上
     *
     * @param data
     * @param text
     */
    private void handlerCommentList(MainHomeData data, String text, String id) {
        if (data != null) {
            List<ForumCommentData> commentList = data.getCommentList();
            if (commentList == null) {
                commentList = new ArrayList<>();
            }
            commentList.add(0, new ForumCommentData(id, SPUtil.getNickName(getContext()), text));
            if (commentList.size() > 2) {
                commentList = commentList.subList(0, 2);//只需要两条
            }
            data.setCommentList(commentList);
        }
    }

    /**
     * 到详情页
     *
     * @param position
     */
    @Override
    public void onItemClick(int position) {
        MainHomeData data = CommonUtils.getDataByPosition(list, position);
        if (data != null) {
            goToForumDetailActivity(data.getId());
        }
    }

    /**
     * 点赞
     *
     * @param position
     */
    @Override
    public void checkPraise(int position, boolean isPraise) {
        praise(position, isPraise);
    }

    /**
     * 点赞帖子
     */
    private void praise(int position, boolean isPraise) {
        MainHomeData mainHomeData = CommonUtils.getDataByPosition(list, position);
        if (mainHomeData != null) {
            if (mainHomeData.getLoadingStatus() == 0 && isLogin()) {
                new ForumImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                    @Override
                    public void onSuccess(LikeAndFavoriteData response, int id) {
                        if (response != null) {
                            mainHomeData.setLike_status(response.getType());
                            mainHomeData.setLike_Num(response.getNum());
                            refreshEvent(mainHomeData);
                        }
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        mainHomeData.setLoadingStatus(0);
                    }
                }.setNeedDialog(false)).forumPraise(mainHomeData.getId());
            }
            mainHomeData.setLike_status(isPraise ? 1 : 0);
            int like_num = mainHomeData.getLike_Num();
            mainHomeData.setLike_Num(isPraise ? ++like_num : --like_num);
            mainHomeData.setLoadingStatus(1);
            adapter.notifyUI(list);
        }
    }


    /**
     * 登录页
     */
    private void showEmpty() {
        Context context = getContext();
        if (context != null) {
            mLoadingLayout.setErrorText(context.getResources().getString(R.string.no_login));
            mLoadingLayout.setErrorImage(R.drawable.no_data);
            mLoadingLayout.setRetryText(context.getResources().getString(R.string.right_login));
            mLoadingLayout.setRetryListener(this);
            mLoadingLayout.showError();
        }
    }

    /**
     * 错误页
     */
    private void showError() {
        Context context = getContext();
        if (context != null) {
            mLoadingLayout.setErrorText(context.getResources().getString(R.string.no_network));
            mLoadingLayout.setErrorImage(R.drawable.no_network);
            mLoadingLayout.setRetryText(context.getResources().getString(R.string.reload_button));
            mLoadingLayout.setRetryListener(this);
            mLoadingLayout.showError();
        }
    }

    /**
     * 点击首页刷新关注/广场
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MainClickIndexEvent event) {
        if (event != null && (event.currentIndex == 1 && isVisible)) {
            if (recyclerView != null) {
                recyclerView.scrollToPosition(0);
            }
            startData(false);
        }
    }


    /**
     * 登录成功,清空关注/广场
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginSuccessEvent event) {
        if (event != null) {
            //先展示内容
            mLoadingLayout.showContent();
            startData(false);
        }
    }

    /**
     * 退出登录
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTokenLoss(TokenTimeOutEvent event) {
        if (event != null && type == 0) {
            list.clear();
            if (adapter != null) {
                adapter.notifyUI(list);
            }
            showEmpty();
        }
    }

    /**
     * 开始加载
     */
    @Override
    protected void loadData() {
        if (type == 0) {
            if (isLogin()) {
                if (!CommonUtils.ListNotNull(list)) {
                    startData(true);
                }
            } else {
                showEmpty();
            }
        } else {
            if (!CommonUtils.ListNotNull(list)) {
                startData(true);
            }
        }
    }

    /**
     * 点击登录/错误重试
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (type == 0) {
            if (!isLogin()) {
                goToLoginActivity();
            } else {
                mLoadingLayout.showLoading();
                startData(true);
            }
        } else {
            mLoadingLayout.showLoading();
            startData(true);
        }
    }


    /**
     * 点击回复
     *
     * @param position
     */
    @Override
    public void clickComment(int position) {
        commentDialog(position);
    }


    /**
     * 查看回复
     *
     * @param position
     */
    @Override
    public void checkComment(int position) {
        MainHomeData data = CommonUtils.getDataByPosition(list, position);
        if (data != null) {
            if (data.getReply_Num() > 0) {
                goToForumDetailComment(data.getId());//有回复跳详情回复
            } else {//回复框
                commentDialog(position);
            }
        }
    }

    /**
     * 查看用户
     *
     * @param position
     */
    @Override
    public void checkUser(int position) {
        MainHomeData data = CommonUtils.getDataByPosition(list, position);
        if (data != null) {
            goToUserCenterActivity(data.getUser_Id(), 1);
        }
    }

    /**
     * 查看图片
     *
     * @param list
     * @param index
     */
    @Override
    public void clickPhoto(List<String> list, int index) {
        CommonUtils.checkImagePreview(getContext(), list, index);
    }

    /**
     * 是否关注
     *
     * @param position
     */
    @Override
    public void isConcern(int position) {
        MainHomeData data = CommonUtils.getDataByPosition(list, position);
        if (data != null) {
            concern(data.getUser_Id(), data);
        }
    }

    /**
     * 功能描述: 回复评论
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/4/13 0013 20:33
     */
    @Override
    public void clickListComment(String id, String nickname) {
        if (isLoginToast()) {
            if (backInputDialog == null) {
                assert getContext() != null;
                backInputDialog = new BackInputDialog(getContext(), this);
            }
            backInputDialog.setCommentPosition(-1).setId(id).show("回复@" + nickname + "：");
        }
    }


    /**
     * 关注接口
     *
     * @param userId
     */
    private void concern(String userId, MainHomeData data) {
        int status = data.getLook_status();
        data.setLook_status(-1);//接口中
        new ForumImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
            @Override
            public void onSuccess(LikeAndFavoriteData response, int id) {
                if (response != null) {
                    data.setLook_status(response.getType());
                    refreshEvent(data);
                }
            }

            @Override
            public void onError(String msg, String code) {
                super.onError(msg, code);
                data.setLook_status(status);
                if (adapter != null) {
                    adapter.notifyUI(list);
                }
            }
        }.setNeedDialog(false)).forumConcernUser(userId);
    }

    /**
     * 接口刷新实体
     *
     * @param data
     */
    private void refreshEvent(MainHomeData data) {
        EventBusUtil.post(new CommListDataEvent(data));
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
            if (type == 0) {//关注页
                if (data.getLook_status() == 0 && !data.isOnlyFavorites()) {
                    setConcernAllById(data.getUser_Id());
                    if (adapter != null) {
                        adapter.notifyUI(list);
                    }
                    if (!CommonUtils.ListNotNull(list)) {
                        startData(false);
                    }
                } else {
                    startData(false);
                }
            } else {//广场页
                if (CommonUtils.ListNotNull(list)) {
                    CommonUtils.commListDataChange(data, list);
                    adapter.notifyUI(list);
                }
            }
        }
    }

    /**
     * 设置列表相同id取消关注
     *
     * @param id
     */
    private void setConcernAllById(String id) {
        if (CommonUtils.ListNotNull(list)) {
            Iterator<MainHomeData> iterator = list.iterator();
            while (iterator.hasNext()) {
                MainHomeData mainHomeData = iterator.next();
                if (mainHomeData != null) {
                    if (id.equals(mainHomeData.getUser_Id())) {
                        //取消关注
                        iterator.remove();
                    }
                }
            }
        }
    }


    /**
     * 全局删除帖子/编辑帖子刷新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeleteInvitationEvent event) {
        if (event != null) {
            if (CommonUtils.StringNotNull(event.id)) {
                CommonUtils.getDeleteInvitation(list, event.id);
                if (CommonUtils.ListNotNull(list) && adapter != null) {
                    adapter.notifyUI(list);
                } else {
                    startData(false);
                }
            } else {
                startData(false);
            }
        }
    }


    /**
     * 我的回复页面删除回复,刷新广场/关注列表回复
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeleteListCommentEvent event) {
        if (event != null) {
            startData(false);
        }
    }
}