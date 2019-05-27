package com.marksixinfo.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.SearchResultAdapter;
import com.marksixinfo.adapter.SearchUserAdapter;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.base.MarkSixFragment;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.bean.MineConcernData;
import com.marksixinfo.bean.SearchData;
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.CommListDataEvent;
import com.marksixinfo.interfaces.IMainHomeRecommend;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.ui.activity.SearchActivity;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.JSONUtils;
import com.marksixinfo.widgets.LoadingLayout;
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
import butterknife.BindView;

/**
 * 搜索结果
 *
 * @Auther: Administrator
 * @Date: 2019/3/16 0016 17:16
 * @Description:
 */
public class SearchResultFragment extends MarkSixFragment implements IMainHomeRecommend, View.OnClickListener, SucceedCallBackListener<Integer> {

    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;

    ArrayList<MainHomeData> list = new ArrayList<>();
    ArrayList<MineConcernData> userList = new ArrayList<>();
    private SearchResultAdapter adapter;
    private int pageIndex = 0;//页数
    private String keyword = "";
    private String currentPeriod = "0";
    private SearchUserAdapter userAdapter;
    private List<MineConcernData> user;
    private LinearLayout llData;
    private SucceedCallBackListener listener;


    @Override
    public int getViewId() {
        return R.layout.fragment_search_result;
    }

    @Override
    protected void afterViews() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            keyword = bundle.getString(StringConstants.SEARCH_KEYWORD);
            currentPeriod = bundle.getString(StringConstants.PERIOD);
        }

        View headView = View.inflate(getContext(), R.layout.head_view_search_user, null);
        initHeadView(headView);

        assert getContext() != null;
        adapter = new SearchResultAdapter(getContext(), list, this);
        listView.addHeaderView(headView);
        listView.setAdapter(adapter);
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

        startData(keyword, currentPeriod, true);
    }

    /**
     * 初始headView
     *
     * @param headView
     */
    private void initHeadView(View headView) {
        llData = headView.findViewById(R.id.ll_data);
        llData.setVisibility(View.GONE);
        RecyclerView recyclerView = headView.findViewById(R.id.recyclerView);
        recyclerView.getParent().requestDisallowInterceptTouchEvent(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        userAdapter = new SearchUserAdapter(getContext(), userList, this);
        recyclerView.setAdapter(userAdapter);
    }


    /**
     * 开始加载
     *
     * @param keyword       搜索词
     * @param currentPeriod 期数
     */
    public void startData(String keyword, String currentPeriod, boolean isInit) {
        SearchActivity activity = (SearchActivity) getActivity();
        if (activity != null) {
            activity.getEditTextView().setCursorVisible(false);
        }
        this.keyword = keyword;
        this.currentPeriod = currentPeriod;
        if (listView != null) {
            listView.setSelection(0);
        }
        if (isInit) {
            refresh(true);
        } else {
            mLoadingLayout.showLoading();
            refresh(true);
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
        new HeadlineImpl(new MarkSixNetCallBack<SearchData>(this, SearchData.class) {
            @Override
            public void onSuccess(SearchData response, int id) {
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
        }.setNeedDialog(false)).getSearchResult(keyword, ++pageIndex, currentPeriod);
    }

    /**
     * 设置搜索用户
     *
     * @param response
     */
    private void setSearchUser(List<MineConcernData> response) {
        user = response;
        userList.clear();
        if (CommonUtils.ListNotNull(user)) {
            if (user.size() > 6) {
                userList.addAll(user.subList(0, NumberConstants.SEARCH_HISTORY_SIZE));
            } else {
                userList.addAll(user);
            }
            userAdapter.changeDataUi(this.userList);
            llData.setVisibility(View.VISIBLE);
        } else {
            llData.setVisibility(View.GONE);
        }
    }


    /**
     * 请求完成
     */
    private void loadDone(boolean isRefresh) {
        if (listener != null) {
            listener.succeedCallBack(null);
        }
        if (refreshLayout != null) {
            if (isRefresh) {
                refreshLayout.finishRefresh();
                refreshLayout.resetNoMoreData();
            } else {
                refreshLayout.finishLoadMore();
            }
        }
    }


    private void setData(SearchData response, boolean isRefresh) {
        if (isRefresh) {
            list.clear();
        }
        if (response != null && CommonUtils.ListNotNull(response.getList())) {
            list.addAll(response.getList());
        } else {
            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
        }
        if (isRefresh && response != null) {
            List<MineConcernData> member = response.getMember();
            setSearchUser(member);
        }
        if (adapter != null) {
            adapter.notifyUI(list);
        }
        if (CommonUtils.ListNotNull(list) || llData.getVisibility() == View.VISIBLE) {
            mLoadingLayout.showContent();
            if (isRefresh) {
                if (listView != null) {
                    listView.setSelection(0);
                }
            }
        } else {
            mLoadingLayout.showEmpty();
        }
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
    public void onClick(View view) {
        mLoadingLayout.showLoading();
        startData(keyword, currentPeriod, true);
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
            if (CommonUtils.ListNotNull(userList)) {
                CommonUtils.searchConcernDataChange(event.getData(), userList);
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
     * 用户点击
     *
     * @param o
     */
    @Override
    public void succeedCallBack(Integer o) {
        if (o != null && CommonUtils.ListNotNull(userList)) {
            if (o < NumberConstants.SEARCH_USER_COUNT - 1) {//查看用户
                MineConcernData mineConcernData = userList.get(o);
                if (mineConcernData != null) {
                    goToUserCenterActivity(mineConcernData.getId(), 0);
                }
            } else {
                startClass(R.string.SearchUserListActivity, IntentUtils.getHashObj(new String[]{
                        StringConstants.DATA_LIST, JSONUtils.toJson(user)}));
            }
        }
    }

    public void setListener(SucceedCallBackListener listener) {
        this.listener = listener;
    }
}


