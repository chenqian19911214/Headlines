package com.marksixinfo.ui.fragment;

import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.adapter.MainHomeAdapter;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.evenbus.CommListDataEvent;
import com.marksixinfo.evenbus.DeleteInvitationEvent;
import com.marksixinfo.evenbus.DeleteLikeAndFavorEvent;
import com.marksixinfo.evenbus.LoginSuccessEvent;
import com.marksixinfo.evenbus.MainClickIndexEvent;
import com.marksixinfo.evenbus.TokenTimeOutEvent;
import com.marksixinfo.interfaces.IMainHomeRecommend;
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
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;

/**
 * 关注
 *
 * @Auther: Administrator
 * @Date: 2019/3/15 0015 12:55
 * @Description:
 */
public class ConcernFragment extends PageBaseFragment implements IMainHomeRecommend, View.OnClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;


    List<MainHomeData> list = new ArrayList<>();
    private MainHomeAdapter adapter;
    private int pageIndex = 0;//页数

    @Override
    public int getViewId() {
        return R.layout.fragment_concern;
    }

    @Override
    protected void afterViews() {

        assert getContext() != null;
        adapter = new MainHomeAdapter(getContext(), list, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);//单条刷新无动画
        recyclerView.setLayoutManager(linearLayoutManager);
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
        if (!isLogin()) {
            showEmpty();
        }
    }


    /**
     * 加载
     *
     * @param isRefresh
     */
    private void refresh(boolean isRefresh) {
        if (!isLogin()) {
            loadDone(isRefresh);
            return;
        }
        if (isRefresh) {
            pageIndex = 0;
        }
        new HeadlineImpl(new MarkSixNetCallBack<List<MainHomeData>>(this, MainHomeData.class) {
            @Override
            public void onSuccess(List<MainHomeData> response, int id) {
                setData(isRefresh, response);
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
                loadDone(isRefresh);
            }
        }.setNeedDialog(false)).concern(String.valueOf(++pageIndex));
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
        if (CommonUtils.ListNotNull(list)) {
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
     * 登录页
     */
    private void showEmpty() {
        assert getContext() != null;
        mLoadingLayout.setErrorText(getContext().getResources().getString(R.string.no_login));
        mLoadingLayout.setErrorImage(R.drawable.no_data);
        mLoadingLayout.setRetryText(getContext().getResources().getString(R.string.right_login));
        mLoadingLayout.setRetryListener(this);
        mLoadingLayout.showError();
    }

    /**
     * 错误页
     */
    private void showError() {
        assert getContext() != null;
        mLoadingLayout.setErrorText(getContext().getResources().getString(R.string.no_network));
        mLoadingLayout.setErrorImage(R.drawable.no_network);
        mLoadingLayout.setRetryText(getContext().getResources().getString(R.string.reload_button));
        mLoadingLayout.setRetryListener(this);
        mLoadingLayout.showError();
    }

    /**
     * 条目点击,去详情
     *
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        MainHomeData mMainHomeData = CommonUtils.getDataByPosition(list, position);
        if (mMainHomeData != null) {
            goToDetailActivity(mMainHomeData.getId());
        }
    }

    /**
     * 点击关注
     *
     * @param position
     * @param isConcern
     */
    @Override
    public void isConcern(int position, boolean isConcern) {
        MainHomeData mainHomeData = CommonUtils.getDataByPosition(list, position);
        if (mainHomeData != null) {
            String userId = mainHomeData.getUser_Id();
            concern(userId, mainHomeData);
        }
    }


    /**
     * 关注接口
     *
     * @param userId
     */
    private void concern(String userId, MainHomeData mainHomeData) {
        int status = mainHomeData.getLook_status();
        mainHomeData.setLook_status(-1);//接口中
        new HeadlineImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
            @Override
            public void onSuccess(LikeAndFavoriteData response, int id) {
                if (response != null) {
                    mainHomeData.setLook_status(response.getType());
                    refreshEvent(mainHomeData);
                }
            }

            @Override
            public void onError(String msg, String code) {
                super.onError(msg, code);
                mainHomeData.setLook_status(status);
                if (adapter != null) {
                    adapter.notifyUI(list);
                }
            }
        }.setNeedDialog(false)).concernUser(userId);
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
     * 查看更多
     *
     * @param position
     */
    @Override
    public void moreContent(int position) {
        MainHomeData mMainHomeData = CommonUtils.getDataByPosition(list, position);
        if (mMainHomeData != null) {
            goToDetailActivity(mMainHomeData.getId());
        }
    }

    /**
     * 查看图片
     *
     * @param list  图片url集合
     * @param index 从第几张开始
     */
    @Override
    public void clickPhoto(List<String> list, int index) {
        CommonUtils.checkImagePreview(getContext(), list, index);
    }

    /**
     * 点赞/取消点赞
     *
     * @param position
     * @param isPraise true,点赞 false 取消
     */
    @Override
    public void checkPraise(int position, boolean isPraise) {
        praise(position, isPraise);
    }


    /**
     * 点赞接口
     *
     * @param position
     */
    private void praise(int position, boolean isPraise) {
        MainHomeData mainHomeData = CommonUtils.getDataByPosition(list, position);
        if (mainHomeData != null) {
            if (mainHomeData.getLoadingStatus() == 0 && isLogin()) {
                new HeadlineImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
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
                }.setNeedDialog(false)).praise(mainHomeData.getId());
            }
            mainHomeData.setLike_status(isPraise ? 1 : 0);
            int like_num = mainHomeData.getLike_Num();
            mainHomeData.setLike_Num(isPraise ? ++like_num : --like_num);
            mainHomeData.setLoadingStatus(1);
            adapter.notifyUI(list);
        }
    }


    /**
     * 查看评论
     *
     * @param position
     */
    @Override
    public void clickComment(int position) {
        MainHomeData mMainHomeData = CommonUtils.getDataByPosition(list, position);
        if (mMainHomeData != null) {
            goToDetailActivityComment(mMainHomeData.getId());
        }
    }

    /**
     * 点击收藏
     *
     * @param position
     * @param isCollect
     */
    @Override
    public void checkCollect(int position, boolean isCollect) {
        collect(position, isCollect);
    }


    /**
     * 收藏
     *
     * @param position
     * @param isCollect
     */
    private void collect(int position, boolean isCollect) {
        MainHomeData mainHomeData = CommonUtils.getDataByPosition(list, position);
        if (mainHomeData != null && isLoginToast()) {
            if (mainHomeData.getLoadingStatus() == 0) {
                int status = mainHomeData.getFav_status();
                int favorites_num = mainHomeData.getFavorites_Num();
                new HeadlineImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                    @Override
                    public void onSuccess(LikeAndFavoriteData response, int id) {
                        if (response != null) {
                            mainHomeData.setFav_status(response.getType());
                            mainHomeData.setFavorites_Num(response.getNum());
                            refreshEvent(mainHomeData);
                        }
                    }

                    @Override
                    public void onError(String msg, String code) {
                        super.onError(msg, code);
                        mainHomeData.setFav_status(status);
                        mainHomeData.setFavorites_Num(favorites_num);
                        if (adapter != null) {
                            adapter.notifyUI(list);
                        }
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        mainHomeData.setLoadingStatus(0);
                    }
                }.setNeedDialog(false)).favorites(mainHomeData.getId());
            }
            mainHomeData.setFav_status(isCollect ? 1 : 0);
            int num = mainHomeData.getFavorites_Num();
            mainHomeData.setFavorites_Num(isCollect ? ++num : --num);
            mainHomeData.setLoadingStatus(2);
            adapter.notifyUI(list);
        }
    }

    /**
     * 查看用户
     *
     * @param userId
     */
    @Override
    public void clickUser(String userId) {
        goToUserCenterActivity(userId, 0);
    }


    /**
     * 点击底部刷新关注
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MainClickIndexEvent event) {
        if (event != null && (event.currentIndex == 0 && isVisible)) {
            if (recyclerView != null) {
                recyclerView.scrollToPosition(0);
            }
            startData(false);
        }
    }

    /**
     * 登录成功,清空关注
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
        if (event != null) {
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
        if (isLogin() && !CommonUtils.ListNotNull(list)) {
            startData(true);
        }
    }

    /**
     * 点击登录/错误重试
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (!isLogin()) {
            goToLoginActivity();
        } else {
            mLoadingLayout.showLoading();
            startData(true);
        }
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
            if (data.getLook_status() == 0 && !data.isOnlyFavorites()) {
                setConcernAllById(data.getUser_Id());
                if (adapter != null) {
                    adapter.notifyUI(list);
                }
                if (!CommonUtils.ListNotNull(list)) {
                    startData(false);
                }
            } else {
                if (CommonUtils.ListNotNull(list)) {
                    CommonUtils.commListDataChange(data, list);
                    if (getClassName().equals(event.getClassName())) {
                        if (adapter != null) {
                            adapter.notifyUI(list);
                        }
                    } else {
                        startData(false);
                    }
                } else {
                    startData(false);
                }
            }
        }
    }


    /**
     * 我的收藏/点赞列表 删除多个或删除所有
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(DeleteLikeAndFavorEvent event) {
        if (event != null && CommonUtils.ListNotNull(event.ids)
                && CommonUtils.ListNotNull(list) && adapter != null) {
            CommonUtils.deleteLikeAndFavorDataChange(event.ids, event.type, list);
            if (adapter != null) {
                adapter.notifyUI(list);
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
     * 接口刷新实体
     *
     * @param data
     */
    private void refreshEvent(MainHomeData data) {
        EventBusUtil.post(new CommListDataEvent(data, getClassName()));
    }
}
