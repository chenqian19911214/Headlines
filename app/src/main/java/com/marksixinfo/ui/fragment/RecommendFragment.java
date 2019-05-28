package com.marksixinfo.ui.fragment;

import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.adapter.MainHomeAdapter;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.evenbus.CleanCacheEvent;
import com.marksixinfo.evenbus.CommListDataEvent;
import com.marksixinfo.evenbus.DeleteInvitationEvent;
import com.marksixinfo.evenbus.DeleteLikeAndFavorEvent;
import com.marksixinfo.evenbus.LoginSuccessEvent;
import com.marksixinfo.evenbus.MainClickIndexEvent;
import com.marksixinfo.evenbus.TokenTimeOutEvent;
import com.marksixinfo.interfaces.IMainHomeRecommend;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.JSONUtils;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.MyClassicsHeader;
import com.marksixinfo.widgets.MySmartRefreshLayout;
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
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;

/**
 * 推荐
 *
 * @Auther: Administrator
 * @Date: 2019/3/15 0015 12:47
 * @Description:
 */
public class RecommendFragment extends PageBaseFragment implements IMainHomeRecommend, View.OnClickListener, SucceedCallBackListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;


    private List<MainHomeData> list = new ArrayList<>(); //列表数据
    private List<MainHomeData> cacheList = new ArrayList<>(); //缓存数据
    private MainHomeAdapter adapter;
    private boolean isClean = false;//是否清空list,重新登录清空推荐
    private final int DELAYED_TIME = 1000;//加载缓存后延时加载网络
    private final int FINISH_DURATION = 700;//下拉推荐完成时间
    private MyClassicsHeader classicsHeader;
    private int lastTimeIndex;//上次查看位置

    @Override
    public int getViewId() {
        return R.layout.fragment_recommend;
    }


    @Override
    protected void afterViews() {
        assert getContext() != null;
        adapter = new MainHomeAdapter(getContext(), list, this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);//单条刷新无动画
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        setRecommendHeader();

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
     * 设置下拉推荐header
     */
    private void setRecommendHeader() {
        ((MySmartRefreshLayout) refreshLayout).moveSpinner(0);
        classicsHeader = (MyClassicsHeader) refreshLayout.getRefreshHeader();
        classicsHeader.setTextPulling("下拉推荐")
                .setTextRefreshing("正在努力加载")
                .setTextRelease("松开推荐")
                .setTextSizeTitle(13)
                .setFinishDuration(FINISH_DURATION);
    }


    /**
     * 开始加载,下拉动画
     */
    private void startData() {
        String recommendTag = SPUtil.getRecommendTag(getContext());
        if (CommonUtils.StringNotNull(recommendTag)) {
            //触发自动刷新
            refreshLayout.autoRefresh();
        } else {
            getSignature();
        }
    }

    /**
     * 加载
     *
     * @param isRefresh
     */
    private void refresh(boolean isRefresh) {
        new HeadlineImpl(new MarkSixNetCallBack<List<MainHomeData>>(this, MainHomeData.class) {
            @Override
            public void onSuccess(List<MainHomeData> response, int id) {
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

        }.setNeedDialog(false)).recommend(getRecommendTag(), String.valueOf(isRefresh ? 0 : 1));
    }


    /**
     * 请求完成
     */
    private void loadDone(boolean isRefresh) {
        if (refreshLayout != null) {
            if (isRefresh) {
                refreshLayout.resetNoMoreData();
                refreshLayout.finishRefresh();
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (classicsHeader != null) {
                            classicsHeader.setAccentColorId(R.color.grey_999);
                            classicsHeader.setPrimaryColorId(R.color.main_bg);
                        }
                    }
                }, FINISH_DURATION + 300);//2300毫秒回弹动画
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
        if (CommonUtils.ListNotNull(response)) {
            if (isClean) {
                isClean = false;
                list.clear();
                cacheList.clear();
            }
            if (isRefresh) {
                handlerTopList();
                handlerCacheList(response);
                handlerLastTime(response);
                list.addAll(0, response);
                setRecommendText("六合头条推荐引擎有" + response.size() + "条更新");
            } else {
                list.addAll(response);
            }
            if (adapter != null) {
                adapter.notifyUI(list);
            }
        } else {
            if (isRefresh) {
                setRecommendText("暂时没有推荐了,看看其他吧");
            }
            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
        }
        if (CommonUtils.ListNotNull(list)) {
            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }
    }

    /**
     * 上次浏览位置
     *
     * @param response
     */
    private void handlerLastTime(List<MainHomeData> response) {
        if (CommonUtils.ListNotNull(list)) {
            CommonUtils.deleteLastTime(list);
            MainHomeData data = new MainHomeData();
            data.setGoneDecoration(true);
            data.setLastLookTime(System.currentTimeMillis());
            response.get(response.size() - 1).setGoneDecoration(true);
            response.add(data);
            lastTimeIndex = response.size() - 1;
        }
    }


    /**
     * 处理置顶数据
     */
    private void handlerTopList() {
        if (CommonUtils.ListNotNull(list)) {
            CommonUtils.setDeleteTop(list);
        }
        if (CommonUtils.ListNotNull(cacheList)) {
            CommonUtils.setDeleteTop(cacheList);
        }
    }

    /**
     * 设置
     *
     * @param text
     */
    private void setRecommendText(String text) {
        ((MySmartRefreshLayout) refreshLayout).moveSpinner(35);
        if (classicsHeader != null) {
            classicsHeader.setAccentColorId(R.color.blue_2a9);
            classicsHeader.setPrimaryColorId(R.color.blue_d5e);
            classicsHeader.setTextFinish(text);
        }
    }

    /**
     * 异步更新缓存集合数据
     *
     * @param response
     */
    private void handlerCacheList(List<MainHomeData> response) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    cacheList.addAll(0, new ArrayList<>(response));
                    if (cacheList.size() > NumberConstants.RECOMMEND_LIST_CACHE_LENGTH) {
                        cacheList = cacheList.subList(0, NumberConstants.RECOMMEND_LIST_CACHE_LENGTH);
                    }
                    SPUtil.setRecommendCache(getContext(), JSONUtils.toJson(cacheList));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
     * 点赞
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
     * 发布成功或推荐页点击首页刷新
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MainClickIndexEvent event) {
        if (event != null && (event.currentIndex == 0 && isVisible)) {
            if (recyclerView != null) {
                recyclerView.scrollToPosition(0);
            }
            startData();
        }
    }

    /**
     * 加载
     */
    @Override
    protected void loadData() {
        //添加缓存
        List<MainHomeData> cache = SPUtil.getRecommendCache(getContext());
        if (CommonUtils.ListNotNull(cache)) {
            setData(true, cache);
        }
        if (recyclerView != null) {
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startData();
                }
            }, DELAYED_TIME);//延迟1000毫秒后自动刷新
        }
    }

    /**
     * 登录成功,清空推荐
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginSuccessEvent event) {
        if (event != null) {
            deleteCache();
        }
    }

    /**
     * 清空推荐
     */
    private void deleteCache() {
        SPUtil.cleanRecommendAndTag(getContext());
        isClean = true;
        cacheList.clear();
        if (recyclerView != null) {
            recyclerView.scrollToPosition(0);
        }
        startData();
    }

    /**
     * 退出登录,清空推荐
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(TokenTimeOutEvent event) {
        if (event != null) {
            deleteCache();
        }
    }

    /**
     * 设置页清除缓存
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CleanCacheEvent event) {
        if (event != null) {
            deleteCache();
        }
    }

    /**
     * 加载
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        startData();
    }


    /**
     * 帖子数据变更
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommListDataEvent event) {
        if (event != null && event.getData() != null
                && CommonUtils.ListNotNull(list) && adapter != null) {
            MainHomeData data = event.getData();
            CommonUtils.commListDataChange(data, list);
            adapter.notifyUI(list);
            notifyCacheList(data);
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
            notifyCacheList(event.ids, event.type);
        }
    }

    /**
     * 更新缓存集合
     */
    private void notifyCacheList(List<String> ids, int type) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (CommonUtils.ListNotNull(cacheList)) {
                        CommonUtils.deleteLikeAndFavorDataChange(ids, type, cacheList);
                        SPUtil.setRecommendCache(getContext(), JSONUtils.toJson(cacheList));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 更新缓存集合
     *
     * @param data
     */
    private void notifyCacheList(MainHomeData data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (data != null && CommonUtils.ListNotNull(cacheList)) {
                        CommonUtils.commListDataChange(data, cacheList);
                        SPUtil.setRecommendCache(getContext(), JSONUtils.toJson(cacheList));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
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
     * 获取推荐列表标识
     */
    private void getSignature() {
        mLoadingLayout.showLoading();
        new HeadlineImpl(new MarkSixNetCallBack<String>(this, String.class) {
            @Override
            public void onSuccess(String response, int id) {
                if (CommonUtils.StringNotNull(response)) {
                    SPUtil.setRecommendTag(getContext(), response);
                    mLoadingLayout.showContent();
                    startData();
                } else {
                    onConnectFail();
                }
            }

            @Override
            public void onError(String msg, String code) {
                if (!CommonUtils.ListNotNull(list)) {
                    mLoadingLayout.showError();
                } else {
                    super.onError(msg, code);
                }
            }
        }.setNeedDialog(false)).getRecommendTag();
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
                    refresh(true);
                }
            } else {
                refresh(true);
            }
        }
    }

    /**
     * 上传浏览位置点击刷新
     *
     * @param o
     */
    @Override
    public void succeedCallBack(Object o) {
        if (recyclerView != null) {
            recyclerView.scrollToPosition(0);
        }
        startData();
    }


    /**
     * 更新上次浏览时间
     */
    @Override
    public void onResume() {
        super.onResume();
        if (isVisible) {
            if (adapter != null && CommonUtils.ListNotNull(list) && lastTimeIndex < list.size()) {
                adapter.notifyItemChanged(lastTimeIndex);
            }
        }
    }
}
