package com.marksixinfo.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.marksixinfo.R;
import com.marksixinfo.adapter.GallerySearchAdapter;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.base.MarkSixFragment;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.CarSearchData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.interfaces.GallerySearchItemClickListener;
import com.marksixinfo.net.impl.GalleryImpl;
import com.marksixinfo.ui.activity.GallerySearchActivity;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.MyClassicsHeader;
import com.marksixinfo.widgets.MySmartRefreshLayout;
import com.marksixinfo.widgets.SpannableStringUtils;
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
 * 图库搜索结果
 *
 * @Auther: Administrator
 * @Date: 2019/3/16 0016 17:16
 * @Description:
 */
public class GallerySearchResultFragment extends MarkSixFragment implements View.OnClickListener, GallerySearchItemClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;

    ArrayList<CarSearchData.ListBean> list = new ArrayList<>();
    private GallerySearchAdapter adapter;
    private int pageIndex = 0;//页数
    private String keyword = "";
    private final int FINISH_DURATION = 2000;//下拉完成时间
    private MyClassicsHeader classicsHeader;

    @Override
    public int getViewId() {
        return R.layout.fragment_gallery_search_result;
    }

    @Override
    protected void afterViews() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            keyword = bundle.getString(StringConstants.SEARCH_KEYWORD);
        }
        assert getContext() != null;
        adapter = new GallerySearchAdapter(this, list, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        mLoadingLayout.setRetryListener(this);
        mLoadingLayout.setEmptyText(getContext().getResources().getString(R.string.no_search_gallery));
        setHeadView();

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

        startData(keyword);
    }

    /**
     * 设置下拉
     */
    private void setHeadView() {
        ((MySmartRefreshLayout) refreshLayout).moveSpinner(0);
        classicsHeader = (MyClassicsHeader) refreshLayout.getRefreshHeader();
        classicsHeader.setTextPulling("下拉搜索")
                .setTextRefreshing("正在努力加载")
                .setTextRelease("松开搜索")
                .setTextSizeTitle(14)
                .setFinishDuration(FINISH_DURATION);
    }

    /**
     * 开始加载
     *
     * @param keyword 搜索词
     */
    public void startData(String keyword) {
        GallerySearchActivity activity = (GallerySearchActivity) getActivity();
        if (activity != null) {
            activity.getEditTextView().setCursorVisible(false);
        }
        this.keyword = keyword;
        adapter.setKeyword(keyword);

        if (recyclerView != null) {
            recyclerView.scrollToPosition(0);
        }
        if (refreshLayout != null) {
            mLoadingLayout.showLoading();
            //触发自动刷新
            refreshLayout.autoRefresh();
        }
    }

    /**
     * 请求网络
     *
     * @param isRefresh 是否下拉刷新
     */
    private void refresh(boolean isRefresh) {
        if (isRefresh) {
            pageIndex = 0;
        }
        pageIndex++;
        new GalleryImpl(new MarkSixNetCallBack<CarSearchData>(this, CarSearchData.class) {
            @Override
            public void onSuccess(CarSearchData response, int id) {
                setData(response.getList(), isRefresh);
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
        }.setNeedDialog(false)).getCharSearch(keyword, pageIndex);
    }


    /**
     * 请求完成
     */
    private void loadDone(boolean isRefresh) {
        if (refreshLayout != null) {
            if (isRefresh) {
                refreshLayout.finishRefresh();
                refreshLayout.resetNoMoreData();
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (classicsHeader != null) {
                            LinearLayout contentView = classicsHeader.getContentView();
                            if (contentView != null) {
                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
                                layoutParams.setMargins(0, 0, 0, 0);
                                layoutParams.removeRule(RelativeLayout.CENTER_VERTICAL);
                                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                            }
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
     * @param response
     * @param isRefresh
     */
    private void setData(List<CarSearchData.ListBean> response, boolean isRefresh) {
        if (isRefresh) {
            setSearchText(response.size());
        }
        if (isRefresh) {
            list.clear();
        }
        if (CommonUtils.ListNotNull(response)) {
            list.addAll(response);
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (CommonUtils.ListNotNull(list)) {
            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }
    }

    /**
     * 设置
     *
     * @param size
     */
    private void setSearchText(int size) {
        ((MySmartRefreshLayout) refreshLayout).moveSpinner(40);
        if (classicsHeader != null) {
            SpannableStringUtils sp = new SpannableStringUtils();
            //搜索“刘半仙”的结果，共找到 5 张图纸
            sp.addText(14, 0xff999999, "搜索 ");
            sp.addText(14, 0xfffc5c66, "“" + keyword + "”");
            sp.addText(14, 0xff999999, " 的结果，共找到 ");
            sp.addText(14, 0xfffc5c66, String.valueOf(size));
            sp.addText(14, 0xff999999, " 张图纸");
            classicsHeader.setTextFinish(sp.toSpannableString());

            LinearLayout contentView = classicsHeader.getContentView();
            if (contentView != null) {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) contentView.getLayoutParams();
                layoutParams.setMargins(UIUtils.dip2px(getContext(), 15), 0, 0, 0);
                layoutParams.removeRule(RelativeLayout.CENTER_IN_PARENT);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            }
        }
    }

    /***
     * 网络错误重试
     * @param view
     */
    @Override
    public void onClick(View view) {
        mLoadingLayout.showLoading();
        startData(keyword);
    }

    /**
     * 点击跳转
     *
     * @param id     id
     * @param period 期数
     */
    @Override
    public void periodsSelect(String id, String period) {
        //带参传输
        startClass(R.string.GalleryDetailActivity,
                IntentUtils.getHashObj(new String[]{
                        StringConstants.ID, id,
                        StringConstants.PERIOD, period}));
    }
}


