package com.marksixinfo.ui.activity;

import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.adapter.MineCollectAdapter;
import com.marksixinfo.base.CommonDialog;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.bean.MineCollectData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.CommListDataEvent;
import com.marksixinfo.interfaces.ISimpleMineInter;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.net.impl.ForumImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.NumberUtils;
import com.marksixinfo.widgets.LoadingLayout;
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
import androidx.recyclerview.widget.SimpleItemAnimator;
import butterknife.BindView;

/**
 * 个人中心,我的收藏
 *
 * @auther: Administrator
 * @date: 2019/3/26 0026 22:03
 */
public class MineCollectActivity extends MarkSixActivity implements ISimpleMineInter, View.OnClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;

    private MineCollectAdapter adapter;
    private List<MainHomeData> list = new ArrayList<>();
    private int pageIndex = 0;//页数
    private CommonDialog commonDialog;
    private int type = 0;//分类 0,头条  1,论坛
    private boolean ifRefresh;
    private MainHomeData dataByPosition;

    @Override
    public int getViewId() {
        return R.layout.activity_mine_collect;
    }

    @Override
    public void afterViews() {

        markSixTitle.init("我的收藏");

        type = NumberUtils.stringToInt(getStringParam(StringConstants.TYPE));

        adapter = new MineCollectAdapter(this, list, type == 1, this);
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

        startData();
    }


    /**
     * 开始加载,下拉动画
     */
    private void startData() {
        refresh(true);
    }


    /**
     * 加载网络
     *
     * @param ifRefresh
     */
    private void refresh(boolean ifRefresh) {
        this.ifRefresh = ifRefresh;
        if (ifRefresh) {
            pageIndex = 0;
        }
        if (type == 0) {
            new HeadlineImpl(callBack.setNeedDialog(false)).mineCollect(String.valueOf(++pageIndex));
        } else {
            new ForumImpl(callBack.setNeedDialog(false)).mineCollect(String.valueOf(++pageIndex));
        }
    }

    MarkSixNetCallBack callBack = (MarkSixNetCallBack) new MarkSixNetCallBack<MineCollectData>(this, MineCollectData.class) {
        @Override
        public void onSuccess(MineCollectData response, int id) {
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
    private void setData(boolean ifRefresh, MineCollectData response) {
        if (ifRefresh) {
            list.clear();
        }
        if (response != null && CommonUtils.ListNotNull(response.getList())) {
            list.addAll(response.getList());
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
     * 条目点击区详情
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
     * 查看头像
     *
     * @param list
     * @param index
     */
    @Override
    public void clickPhoto(List<String> list, int index) {
        CommonUtils.checkImagePreview(getContext(), list, index);
    }

    //编辑分类(我的头条使用)
    @Override
    public void changeInvitation(int position) {

    }

    /**
     * 取消收藏
     *
     * @param position
     */
    @Override
    public void cancelFavorites(int position) {
        if (commonDialog == null) {
            commonDialog = new CommonDialog(this);
        }
        commonDialog.show("是否取消收藏", "", "取消", "确认", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (commonDialog != null && commonDialog.isShowing()) {
                    commonDialog.dismiss();
                }
                deleteCollect(position);
            }
        });
    }


    /**
     * 取消收藏
     *
     * @param position
     */
    private void deleteCollect(int position) {
        dataByPosition = CommonUtils.getDataByPosition(list, position);
        if (dataByPosition != null) {
            if (type == 0) {
                new HeadlineImpl(deleteCallBack).mineDeleteCollect(dataByPosition.getId());
            } else {
                new ForumImpl(deleteCallBack).mineDeleteCollect(dataByPosition.getId());
            }
        }
    }

    MarkSixNetCallBack deleteCallBack = new MarkSixNetCallBack<Object>(this, Object.class) {
        @Override
        public void onSuccess(Object response, int id) {
            toast("取消成功");
            refreshEventData(dataByPosition.getId());
        }
    };


    /**
     * 帖子数据变更
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommListDataEvent event) {
        if (event != null && event.getData() != null) {
            MainHomeData data = event.getData();
            if ((data.isOnlyFavorites() || data.getFav_status() == 0)
                    && CommonUtils.ListNotNull(list)) {
                CommonUtils.mineCollectDataChange(data, list);
                if (adapter != null) {
                    adapter.notifyUI(list);
                }
                if (!CommonUtils.ListNotNull(list)) {
                    if (refreshLayout != null) {
                        refreshLayout.autoRefresh();
                    }
                }
            } else {
                startData();
            }
        }
    }


    /**
     * 接口刷新实体
     */
    private void refreshEventData(String id) {
        EventBusUtil.post(new CommListDataEvent(CommonUtils.getMainHomeData(id)));
    }

    /**
     * 网络错误重试
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        mLoadingLayout.showLoading();
        startData();
    }
}
