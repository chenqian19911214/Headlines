package com.marksixinfo.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.adapter.ForumListAdapter;
import com.marksixinfo.adapter.MainHomeAdapter;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.bean.ForumCommentData;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.CommListDataEvent;
import com.marksixinfo.interfaces.IForumListInterface;
import com.marksixinfo.interfaces.IMainHomeRecommend;
import com.marksixinfo.net.impl.ForumImpl;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.ui.activity.UserPostCenterActivity;
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
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;

/**
 * 查看他人详情页
 *
 * @Auther: Administrator
 * @Date: 2019/3/21 0021 20:44
 * @Description:
 */
public class UserPostCentreFragment extends PageBaseFragment implements IMainHomeRecommend, View.OnClickListener, IForumListInterface, BackInputDialog.InputTextListener {


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
//    @BindView(R.id.iv_image_loading)
//    ImageView loading;
//    @BindView(R.id.fl_loading)
//    FrameLayout flLoading;


    List<MainHomeData> list = new ArrayList<>();
    private MainHomeAdapter adapter;
    private ForumListAdapter forumAdapter;
    private int pageIndex = 0;//页数
    private String categoryId = "";//分类id
    private String userId = "";
    private int type;//分类 0,头条  1,论坛  2,图库
    private boolean ifRefresh;
    private MainHomeData mainHomeData;
    private MarkSixNetCallBack callBack;
    private BackInputDialog backInputDialog;
    private int fav_status;
    private int favorites_num;

    @Override
    public int getViewId() {
        return R.layout.fragment_user_post_center;
    }

    @Override
    protected void afterViews() {


        Bundle bundle = getArguments();
        if (bundle != null) {
            categoryId = bundle.getString(StringConstants.CATEGROY_ID);
            userId = bundle.getString(StringConstants.USER_ID);
            type = bundle.getInt(StringConstants.TYPE);
        }


        assert getContext() != null;
        adapter = new MainHomeAdapter(getContext(), type != 0, list, this);
        forumAdapter = new ForumListAdapter(getContext(), list, 1, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);//单条刷新无动画
        recyclerView.setLayoutManager(linearLayoutManager);
        assert getContext() != null;
        if (type == 0) {
            recyclerView.setAdapter(adapter);
        } else {
            recyclerView.setAdapter(forumAdapter);
        }
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

        callBack = new MarkSixNetCallBack<List<MainHomeData>>(this, MainHomeData.class) {
            @Override
            public void onSuccess(List<MainHomeData> response, int id) {
                setData(ifRefresh, response);
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
                loadDone(ifRefresh);
            }
        };
    }


    /**
     * 刷新
     *
     * @param ifRefresh
     */
    private void refresh(boolean ifRefresh) {
        this.ifRefresh = ifRefresh;
        if (ifRefresh) {
            pageIndex = 0;
        }
        if (type == 0) {
            new HeadlineImpl(callBack.setNeedDialog(false)).getUserCenterList(categoryId, userId, String.valueOf(++pageIndex));
        } else if (type == 1) {
            new ForumImpl(callBack.setNeedDialog(false)).getUserCenterList(categoryId, userId, String.valueOf(++pageIndex));
        } else {
            mLoadingLayout.showEmpty();
            refreshLayout.finishRefresh();
            refreshLayout.resetNoMoreData();
            refreshLayout.finishLoadMore();
            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
        }
    }


    /**
     * 请求完成
     */
    private void loadDone(boolean isRefresh) {
//        loading.setImageResource(0);
//        flLoading.setVisibility(View.GONE);
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
     * @param ifRefresh
     * @param response
     */
    private void setData(boolean ifRefresh, List<MainHomeData> response) {
        if (ifRefresh) {
            list.clear();
        }
        if (CommonUtils.ListNotNull(response)) {
            list.addAll(response);
            setCollectGone();
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
        }
        notifyList();
        if (CommonUtils.ListNotNull(list)) {
            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }
    }


    /**
     * 根据type刷新列表
     */
    private void notifyList() {
        if (type == 0) {
            if (adapter != null) {
                adapter.notifyUI(list);
            }
        } else {
            if (forumAdapter != null) {
                forumAdapter.notifyUI(list);
            }
        }
    }

    /**
     * 设置关注按钮不可见
     */
    private void setCollectGone() {
        if (CommonUtils.ListNotNull(list)) {
            for (MainHomeData data : list) {
                data.setLook_status(2);
                if (type == 1) {
                    data.setLevel(0);//论坛模块不显示
                }
            }
        }
    }

    /**
     * 开始加载
     */
    private void startData() {
        mLoadingLayout.setLoadingViewMarginTop(-70).showLoading();
        refresh(true);
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
            if (type == 0) {
                goToDetailActivity(mMainHomeData.getId());
            } else {
                goToForumDetailActivity(mMainHomeData.getId());
            }
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
            if (type == 0) {
                goToDetailActivity(mMainHomeData.getId());
            } else {
                goToForumDetailActivity(mMainHomeData.getId());
            }
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

    @Override
    public void isConcern(int position) {

    }

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

    @Override
    public void onItemClick(int position) {
        MainHomeData mMainHomeData = CommonUtils.getDataByPosition(list, position);
        if (mMainHomeData != null) {
            if (type == 0) {
                goToDetailActivity(mMainHomeData.getId());
            } else {
                goToForumDetailActivity(mMainHomeData.getId());
            }
        }
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
     * @param isPraise
     */
    private void praise(int position, boolean isPraise) {
        mainHomeData = CommonUtils.getDataByPosition(list, position);
        if (mainHomeData != null) {
            if (mainHomeData.getLoadingStatus() == 0 && isLogin()) {
                if (type == 0) {
                    new HeadlineImpl(praiseCallBack.setNeedDialog(false)).praise(mainHomeData.getId());
                } else {
                    new ForumImpl(praiseCallBack.setNeedDialog(false)).forumPraise(mainHomeData.getId());
                }
            }
            mainHomeData.setLike_status(isPraise ? 1 : 0);
            int like_num = mainHomeData.getLike_Num();
            mainHomeData.setLike_Num(isPraise ? ++like_num : --like_num);
            mainHomeData.setLoadingStatus(1);
            notifyList();
        }
    }

    MarkSixNetCallBack praiseCallBack = new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
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
    };


    /**
     * 查看评论
     *
     * @param position
     */
    @Override
    public void clickComment(int position) {
        if (type == 0) {
            MainHomeData mMainHomeData = CommonUtils.getDataByPosition(list, position);
            if (mMainHomeData != null) {
                goToDetailActivityComment(mMainHomeData.getId());
            }
        } else {
            commentDialog(position);
        }
    }

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

    @Override
    public void checkUser(int position) {

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
        mainHomeData = CommonUtils.getDataByPosition(list, position);
        if (mainHomeData != null && isLoginToast()) {
            if (mainHomeData.getLoadingStatus() == 0) {
                fav_status = mainHomeData.getFav_status();
                favorites_num = mainHomeData.getFavorites_Num();
                if (type == 0) {
                    new HeadlineImpl(favoritesCallBack.setNeedDialog(false)).favorites(mainHomeData.getId());
                } else {
                    new ForumImpl(favoritesCallBack.setNeedDialog(false)).forumFavorites(mainHomeData.getId());
                }
            }
            mainHomeData.setFav_status(isCollect ? 1 : 0);
            int num = mainHomeData.getFavorites_Num();
            mainHomeData.setFavorites_Num(isCollect ? ++num : --num);
            mainHomeData.setLoadingStatus(2);
            notifyList();
        }
    }

    MarkSixNetCallBack favoritesCallBack = new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
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
            mainHomeData.setFav_status(fav_status);
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
    };

    /**
     * 查看用户
     *
     * @param userId
     */
    @Override
    public void clickUser(String userId) {
    }

    /**
     * 加载
     */
    @Override
    protected void loadData() {
        startData();
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
                && CommonUtils.ListNotNull(list)) {
            if (!UserPostCenterActivity.class.getSimpleName().equals(event.getClassName())) {
                CommonUtils.commListDataChange(event.getData(), list);
                setCollectGone();
                notifyList();
            }
        }
    }

    /**
     * 接口刷新实体
     *
     * @param data
     */
    private void refreshEvent(MainHomeData data) {
        //设置空id,不更新关注状态
        data.setUser_Id("");
        EventBusUtil.post(new CommListDataEvent(data, getClassName()));
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
                        forumAdapter.notifyUI(list);
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
}
