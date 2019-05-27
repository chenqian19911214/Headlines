package com.marksixinfo.ui.activity;

import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.adapter.MIneInvitationAdapter;
import com.marksixinfo.base.CommonDialog;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.CommListDataEvent;
import com.marksixinfo.evenbus.DeleteInvitationEvent;
import com.marksixinfo.interfaces.IMainHomeRecommend;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.net.impl.ForumImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.SimpleDialog;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 论坛  - 我的帖子
 *
 * @Auther: Administrator
 * @Date: 2019/4/12 0012 17:44
 * @Description:
 */
public class MineInvitationActivity extends MarkSixActivity implements View.OnClickListener
        , SucceedCallBackListener, IMainHomeRecommend {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    private List<MainHomeData> list = new ArrayList<>();
    private MIneInvitationAdapter adapter;
    private int editPosition;//当前编辑的index
    private int pageIndex = 0;//页数
    private String categoryId = "0";//分类id
    private SimpleDialog simpleDialog;
    private CommonDialog commonDialog;

    @Override
    public int getViewId() {
        return R.layout.activity_mine_invitation;
    }

    @Override
    public void afterViews() {

        markSixTitle.init("我的帖子");

        adapter = new MIneInvitationAdapter(this, list, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getContext().getResources().getDrawable(R.drawable.shape_item_decoration));
        recyclerView.addItemDecoration(decoration);
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
     * 请求网络
     *
     * @param isRefresh
     */
    private void refresh(boolean isRefresh) {
        if (isRefresh) {
            pageIndex = 0;
        }
        new ForumImpl(new MarkSixNetCallBack<List<MainHomeData>>(this, MainHomeData.class) {
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
        }.setNeedDialog(false)).mineInvitationList(categoryId, String.valueOf(++pageIndex));

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
    private void setData(boolean isRefresh, List<MainHomeData> response) {
        if (isRefresh) {
            list.clear();
        }
        if (CommonUtils.ListNotNull(response)) {
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

    @Override
    public void onClick(View v) {
        mLoadingLayout.showLoading();
        startData(true);
    }


    /**
     * 条目点击，查看详情
     *
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        MainHomeData mMainHomeData = CommonUtils.getDataByPosition(list, position);
        if (mMainHomeData != null) {
            goToForumDetailActivity(mMainHomeData.getId());
        }
    }

    /**
     * 编辑帖子
     *
     * @param position
     * @param isConcern
     */
    @Override
    public void isConcern(int position, boolean isConcern) {
        editPosition = position;
        if (simpleDialog == null) {
            simpleDialog = new SimpleDialog(getContext());
        }
        simpleDialog.show("编辑", "删除", this);
    }

    /**
     * 更多(无)
     *
     * @param position
     */
    @Override
    public void moreContent(int position) {

    }

    /**
     * 查看头像
     *
     * @param list
     * @param index
     */
    @Override
    public void clickPhoto(List<String> list, int index) {
        CommonUtils.checkImagePreview(getContext(), list, index);
    }

    /**
     * 点赞
     *
     * @param position
     * @param isPraise
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
     * 查看评论
     *
     * @param position
     */
    @Override
    public void clickComment(int position) {
        MainHomeData mMainHomeData = CommonUtils.getDataByPosition(list, position);
        if (mMainHomeData != null) {
            goToForumDetailComment(mMainHomeData.getId());
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
        if (mainHomeData != null) {
            if (mainHomeData.getLoadingStatus() == 0) {
                int status = mainHomeData.getFav_status();
                int favorites_num = mainHomeData.getFavorites_Num();
                new ForumImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
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
                }.setNeedDialog(false)).forumFavorites(mainHomeData.getId());
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
        goToUserCenterActivity(userId, 1);
    }


    @Override
    public void succeedCallBack(Object o) {
        if ("1".equals(o)) {//编辑
            editData();
        } else if ("2".equals(o)) {//删除
            showDeleteDialog();
        }
    }


    /**
     * 编辑
     */
    private void editData() {
        MainHomeData mMainHomeData = CommonUtils.getDataByPosition(list, editPosition);
        if (mMainHomeData != null) {
            startClass(R.string.ForumReleaseActivity, IntentUtils.getHashObj(new String[]{
                    StringConstants.TYPE, "1", StringConstants.ID, mMainHomeData.getId()}));
        }
    }

    /**
     * 删除确认框
     */
    private void showDeleteDialog() {
        if (commonDialog == null) {
            commonDialog = new CommonDialog(this);
        }
        commonDialog.show("确认删除此内容?", "", "取消",
                "确定删除", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        delete();
                        if (commonDialog != null && commonDialog.isShowing()) {
                            commonDialog.dismiss();
                        }
                    }
                });
    }


    /**
     * 删除帖子
     */
    private void delete() {
        MainHomeData mainHomeData = CommonUtils.getDataByPosition(list, editPosition);
        if (mainHomeData != null) {
            String mainHomeDataId = mainHomeData.getId();
            new ForumImpl(new MarkSixNetCallBack<Object>(this, Object.class) {
                @Override
                public void onSuccess(Object response, int id) {
                    toast("删除成功");
                    list.remove(editPosition);
                    EventBusUtil.post(new DeleteInvitationEvent(mainHomeDataId));
                    adapter.notifyUI(list);
                    if (!CommonUtils.ListNotNull(list)) {
                        startData(false);
                    }
                }
            }).mineInvitationDelete(mainHomeDataId);
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
                    refresh(true);
                }
            } else {
                refresh(true);
            }
        }
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
            if (CommonUtils.ListNotNull(list)) {
                CommonUtils.commListDataChange(data, list);
                adapter.notifyUI(list);
            }
        }
    }
}
