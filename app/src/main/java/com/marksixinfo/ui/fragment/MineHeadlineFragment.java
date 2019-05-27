package com.marksixinfo.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.marksixinfo.R;
import com.marksixinfo.adapter.MIneHeadlineAdapter;
import com.marksixinfo.base.CommonDialog;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.bean.UserPostCenterCategory;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.CommListDataEvent;
import com.marksixinfo.evenbus.DeleteInvitationEvent;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.IMainHomeRecommend;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.ui.activity.MineHeadlineActivity;
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
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;

/**
 * 我的头条
 *
 * @Auther: Administrator
 * @Date: 2019/3/27 0027 13:06
 * @Description:
 */
public class MineHeadlineFragment extends PageBaseFragment implements SucceedCallBackListener, View.OnClickListener, IMainHomeRecommend {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;


    private MIneHeadlineAdapter adapter;
    private List<MainHomeData> list = new ArrayList<>();
    private int pageIndex = 0;//页数
    private String categoryId = "";//分类id
    private SimpleDialog simpleDialog;
    private int editPosition;//当前编辑的index
    private OptionsPickerView<UserPostCenterCategory> pvCustomOptions;
    private CommonDialog commonDialog;


    @Override
    public int getViewId() {
        return R.layout.fragmnet_mine_headline;
    }

    @Override
    protected void afterViews() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            categoryId = bundle.getString(StringConstants.CATEGROY_ID);
        }
        assert getContext() != null;
        adapter = new MIneHeadlineAdapter(getActivity(), list, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);//单条刷新无动画
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

    }


    /**
     * 刷新
     *
     * @param ifRefresh
     */
    private void refresh(boolean ifRefresh) {
        if (ifRefresh) {
            pageIndex = 0;
        }
        new HeadlineImpl(new MarkSixNetCallBack<List<MainHomeData>>(this, MainHomeData.class) {
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
     * @param ifRefresh
     * @param response
     */
    private void setData(boolean ifRefresh, List<MainHomeData> response) {
        if (ifRefresh) {
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

    /**
     * 弹框移除当前分类,不让选择
     *
     * @param response
     */
    private void handlerCurrentId(List<UserPostCenterCategory> response) {
        if (CommonUtils.ListNotNull(response)) {
            Iterator<UserPostCenterCategory> iterator = response.iterator();
            while (iterator.hasNext()) {
                UserPostCenterCategory data = iterator.next();
                if (data != null) {
                    if (categoryId.equals(data.getId())) {
                        iterator.remove();
                    }
                }
            }
        }
    }

    public void startData(boolean isInit) {
        if (isInit) {
            refresh(true);
        } else {
            //触发自动刷新
            refreshLayout.autoRefresh();
        }
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
            goToDetailActivity(mMainHomeData.getId());
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
        simpleDialog.show("编辑头条分类", "删除", this);
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


    @Override
    protected void loadData() {
        startData(true);
    }

    @Override
    public void succeedCallBack(@Nullable Object o) {
        if ("1".equals(o)) {//编辑
            getCategoryData();
        } else if ("2".equals(o)) {//删除
            showDeleteDialog();
        }
    }


    /**
     * 编辑dialog
     *
     * @param response
     */
    private void showEditDialog(List<UserPostCenterCategory> response) {
        pvCustomOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                edit(response.get(options1).getId());
            }
        }).setLayoutRes(R.layout.pickerview_custom_options, new CustomListener() {
            @Override
            public void customLayout(View v) {
                final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                tvSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pvCustomOptions.returnData();
                        pvCustomOptions.dismiss();
                    }
                });
            }
        }).setOutSideCancelable(true).build();
        pvCustomOptions.setPicker(response);//添加数据
        pvCustomOptions.show();
    }


    /**
     * 编辑获取分类
     */
    private void getCategoryData() {
        new HeadlineImpl(new MarkSixNetCallBack<List<UserPostCenterCategory>>(this, UserPostCenterCategory.class) {
            @Override
            public void onSuccess(List<UserPostCenterCategory> response, int id) {
//                handlerCurrentId(response);
                if (CommonUtils.ListNotNull(response)) {
                    showEditDialog(response);
                } else {
                    toast("暂无分类");
                }
            }
        }).mineInvitationCategory();
    }


    /**
     * 开始编辑
     *
     * @param categoryId
     */
    private void edit(String categoryId) {
        MainHomeData mainHomeData = CommonUtils.getDataByPosition(list, editPosition);
        if (mainHomeData != null) {
            String id = mainHomeData.getId();
            if (CommonUtils.StringNotNull(categoryId)) {
                if (categoryId.equals(this.categoryId)) {//id相同直接成功
                    toast("编辑成功");
                    return;
                }
                new HeadlineImpl(new MarkSixNetCallBack<Object>(this, Object.class) {
                    @Override
                    public void onSuccess(Object response, int id) {
                        toast("编辑成功");
                        isInit = true;
                        EventBusUtil.post(new DeleteInvitationEvent());
                        selectClassifyId(categoryId);
                        startData(false);
                    }
                }).mineInvitationChange(id, categoryId);
            }
        }
    }

    /**
     * 选中已编辑的类目
     *
     * @param categoryId
     */
    private void selectClassifyId(String categoryId) {
        ((MineHeadlineActivity) getActivity()).selectIndexAndRefresh(categoryId);
    }

    /**
     * 删除确认框
     */
    private void showDeleteDialog() {
        if (commonDialog == null) {
            commonDialog = new CommonDialog((ActivityIntentInterface) getContext());
        }
        commonDialog.show("确认删除此内容?", "", "取消", "确定删除", null, new View.OnClickListener() {
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
            new HeadlineImpl(new MarkSixNetCallBack<Object>(this, Object.class) {
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

    @Override
    public void onClick(View view) {
        mLoadingLayout.showLoading();
        startData(true);
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
            CommonUtils.commListDataChange(event.getData(), list);
            adapter.notifyUI(list);
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
}
