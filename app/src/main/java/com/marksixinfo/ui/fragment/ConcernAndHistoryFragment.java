package com.marksixinfo.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.ConcernAndHistoryAdapter;
import com.marksixinfo.base.ArrayCallback;
import com.marksixinfo.base.CommonDialog;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.bean.ResultData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.CommListDataEvent;
import com.marksixinfo.evenbus.DeleteLikeAndFavorEvent;
import com.marksixinfo.interfaces.IConcernAndHistory;
import com.marksixinfo.interfaces.IMainHomeRecommend;
import com.marksixinfo.net.impl.CommArrayImpl;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.ui.activity.MineConcernAndHistoryActivity;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.widgets.LoadingLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 收藏/点赞/历史 列表
 *
 * @Auther: Administrator
 * @Date: 2019/4/19 0019 22:55
 * @Description:
 */
public class ConcernAndHistoryFragment extends PageBaseFragment implements IConcernAndHistory, IMainHomeRecommend, View.OnClickListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.tv_delete_select)
    TextView tvDeleteSelect;
    @BindView(R.id.ll_delete_content)
    LinearLayout llDeleteContent;


    private int type;//1,收藏 2,评论 3,点赞 4,历史
    private int parentType;//0,头条 1,论坛
    private ConcernAndHistoryAdapter adapter;
    List<MainHomeData> list = new ArrayList<>();
    private int pageIndex = 0;//页数
    private boolean isEdit;//是否编辑
    private Set<String> selectIds = new HashSet<>();//编辑下需要删除的帖子id
    private CommonDialog commonDialog;


    @Override
    public int getViewId() {
        return R.layout.fragment_concern_and_history;
    }

    @Override
    protected void afterViews() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            parentType = bundle.getInt(StringConstants.PARENT_TYPE);
            type = bundle.getInt(StringConstants.TYPE);
        }

        assert getContext() != null;
        adapter = new ConcernAndHistoryAdapter(getContext(), parentType == 1, list, this, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);//单条刷新无动画
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getContext().getResources().getDrawable(R.drawable.shape_item_decoration));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
        mLoadingLayout.setRetryListener(this);
        commonDialog = new CommonDialog(this);
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
     * 开始加载
     */
    @Override
    protected void loadData() {
        startData(true);
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
        }.setNeedDialog(false)).concernAndHistoryList(String.valueOf(++pageIndex), String.valueOf(type));
    }


    /**
     * 请求完成
     */
    private void loadDone(boolean isRefresh) {
        if (refreshLayout != null) {
            if (isRefresh) {
                refreshLayout.finishRefresh();
                refreshLayout.resetNoMoreData();
                refreshReset();
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
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
        }
        if (pageIndex == 1) {
            ((MineConcernAndHistoryActivity) getActivity()).setEditEnabled(type, CommonUtils.ListNotNull(list));
        }
        if (adapter != null) {
            adapter.notifyUI(list, isEdit);
        }
        handlerEmpty();
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
    private void startData(boolean isInit) {
        if (isInit) {
            refresh(true);
        } else {
            //触发自动刷新
            refreshLayout.autoRefresh();
        }
    }

    /**
     * 是否编辑状态
     *
     * @param isEdit
     */
    @Override
    public void isEdit(boolean isEdit) {
        this.isEdit = isEdit;
        selectIds.clear();
        if (!isEdit) {//取消编辑,清除选中
            if (CommonUtils.ListNotNull(list)) {
                for (MainHomeData data : list) {
                    data.setSelect(false);
                }
            }
            setSelectStatus();
            llDeleteContent.setVisibility(View.GONE);
        } else {//显示编辑选项
            llDeleteContent.setVisibility(View.VISIBLE);
        }
        if (adapter != null) {
            adapter.notifyUI(list, isEdit);
        }
    }

    /**
     * 选中
     *
     * @param position 位置
     * @param isSelect 是否选中
     */
    @Override
    public void selectItem(int position, boolean isSelect) {
        if (CommonUtils.ListNotNull(list)) {
            list.get(position).setSelect(isSelect);
            adapter.notifyUI(list, isEdit);
            selectIds.clear();
            for (MainHomeData data : list) {
                if (data != null && data.isSelect() && CommonUtils.StringNotNull(data.getId())) {
                    selectIds.add(data.getId());
                }
            }
        }
        setSelectStatus();
    }

    /**
     * 下拉刷新重置删除状态
     */
    private void refreshReset() {
        if (isEdit) {
            selectIds.clear();
            setSelectStatus();
        }
    }

    /**
     * 设置已选需要删除的位置
     */
    private void setSelectStatus() {
        if (selectIds.size() > 0) {
            tvDeleteSelect.setText("删除(" + selectIds.size() + ")");
            tvDeleteSelect.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
        } else {
            tvDeleteSelect.setText("删除");
            tvDeleteSelect.setTextColor(getContext().getResources().getColor(R.color.grey_999));
        }
    }

    /**
     * 查看详情
     *
     * @param view
     * @param position
     */
    @Override
    public void onItemClick(View view, int position) {
        MainHomeData data = CommonUtils.getDataByPosition(list, position);
        if (data != null) {
            if (isEdit) {//编辑状态下选中
                selectItem(position, !data.isSelect());
            } else {
                if (parentType == 1) {//论坛
                    goToForumDetailActivity(data.getItem_Id());
                } else {
                    goToDetailActivity(data.getItem_Id());
                }
            }
        }
    }

    /**
     * 关注
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
                }.setNeedDialog(false)).praise(mainHomeData.getItem_Id());
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
            if (parentType == 1) {//论坛
                goToForumDetailComment(mMainHomeData.getItem_Id());
            } else {
                goToDetailActivityComment(mMainHomeData.getItem_Id());
            }
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
                }.setNeedDialog(false)).favorites(mainHomeData.getItem_Id());
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
     * 帖子数据变更
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommListDataEvent event) {
        if (event != null && event.getData() != null) {
            MainHomeData data = event.getData();
            if (CommonUtils.ListNotNull(list)) {
                boolean b = CommonUtils.commListDataChange2(data, list);
                if (b) {
                    if (adapter != null) {
                        adapter.notifyUI(list, isEdit);
                    }
                } else {
                    if (!isInit) {
                        refresh(true);
                    }
                }
            } else {
                if (!isInit) {
                    refresh(true);
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
     * 接口刷新实体
     *
     * @param data
     */
    private void refreshEvent(MainHomeData data) {
        MainHomeData clone = data.clone();
        if (clone != null) {
            clone.setId(clone.getItem_Id());
            EventBusUtil.post(new CommListDataEvent(clone));
        }
    }


    /**
     * 接口刷新实体
     */
    private void refreshEvent(boolean isCleanAll) {
        if (CommonUtils.ListNotNull(list)) {
            List<String> array = new ArrayList<>();
            if (isCleanAll) {
                array.add("-1");
            } else {
                for (MainHomeData data : list) {
                    if (data != null && data.isSelect()) {
                        array.add(data.getItem_Id());
                    }
                }
            }
            EventBusUtil.post(new DeleteLikeAndFavorEvent(array, type));
        }
    }


    @OnClick({R.id.tv_delete_all, R.id.tv_delete_select})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_delete_all://删除所有
                deleteAll();
                break;
            case R.id.tv_delete_select://删除所选
                if (selectIds.size() > 0) {
                    deleteSelect();
                }
                break;
        }
    }

    /**
     * 清除所有
     */
    private void deleteAll() {
        commonDialog.show("", "确认要清空吗？清空后将永久无法找回，请谨慎操作。"
                , "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (commonDialog != null && commonDialog.isShowing()) {
                            commonDialog.dismiss();
                        }
                        deleteHistory(getDeleteIds(true), true);
                    }
                });


    }


    /**
     * 删除提示
     */
    private void deleteSelect() {
        commonDialog.show("", "确认删除" + selectIds.size() + "条" + getDeleteText()
                , "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (commonDialog != null && commonDialog.isShowing()) {
                            commonDialog.dismiss();
                        }
                        deleteHistory(getDeleteIds(false), false);
                    }
                });
    }


    /**
     * 删除记录
     *
     * @param array
     * @param isCleanAll
     */
    private void deleteHistory(List<String> array, boolean isCleanAll) {
        new CommArrayImpl(new ArrayCallback<String>(this) {
            @Override
            public void onSuccess(ResultData<String> response) {
                setDeleteResult(isCleanAll);
            }
        }).concernAndHistoryDelete(array, String.valueOf(type));
    }


    /**
     * 设置删除完成
     *
     * @param isCleanAll
     */
    private void setDeleteResult(boolean isCleanAll) {
        refreshEvent(isCleanAll);
        if (isCleanAll) {
            list.clear();
        } else {
            Iterator<MainHomeData> iterator = list.iterator();
            while (iterator.hasNext()) {
                MainHomeData data = iterator.next();
                if (data != null && data.isSelect()) {
                    iterator.remove();
                }
            }
        }
        isEdit(false);
        resetActivityStatus();
        if (CommonUtils.ListNotNull(list)) {
            adapter.notifyUI(list, isEdit);
        } else {
            startData(false);
        }
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !isInit) {//可见并且已经加载过,判断列表
            ((MineConcernAndHistoryActivity) getActivity()).setEditEnabled(type, CommonUtils.ListNotNull(list));
        }
    }

    /**
     * 重置状态
     */
    private void resetActivityStatus() {
        ((MineConcernAndHistoryActivity) getActivity()).clickEdit(-1);
    }


    /**
     * 获取需要删除的条目的id
     *
     * @return
     */
    private List<String> getDeleteIds(boolean isCleanAll) {
        List<String> list = new ArrayList<>();
        if (isCleanAll) {
            list.add("-1");
        } else {
            if (selectIds.size() > 0) {
                list.addAll(selectIds);
            }
        }
        return list;
    }

    /**
     * 获取文描
     *
     * @return
     */
    private String getDeleteText() {
        switch (type) {//0,收藏 1,评论 2,点赞 3,历史
            case 0:
                return "收藏记录吗?";
            case 2:
                return "点赞记录吗?";
            case 3:
                return "阅读历史吗?";
            default:
                return "收藏记录吗?";
        }
    }

    @Override
    public void onClick(View v) {
        mLoadingLayout.showLoading();
        startData(true);
    }
}