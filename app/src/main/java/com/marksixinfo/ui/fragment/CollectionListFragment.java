package com.marksixinfo.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.adapter.GalleryMyCollectionListAdapter;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.bean.GalleryListData;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.bean.MyCollectionData;
import com.marksixinfo.bean.PictureDetailsData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.LoginSuccessEvent;
import com.marksixinfo.evenbus.TokenTimeOutEvent;
import com.marksixinfo.interfaces.GalleryItemClickListener;
import com.marksixinfo.interfaces.OnCollectionItemClickListener;
import com.marksixinfo.net.impl.GalleryImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.widgets.LoadingLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.BindView;

/**
 * 图库 我的收藏
 *
 * @Auther: Administrator
 * @Date: 12点40分
 */
public class CollectionListFragment extends PageBaseFragment implements View.OnClickListener {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    private GalleryMyCollectionListAdapter adapter;
    List<MyCollectionData> list = new ArrayList<>();
    private StaggeredGridLayoutManager layoutManager;
    private String period;//期数
    private int firstPosition;

    /**
     * 当前请求的页码
     */
    private int pageIndex = 0;

    @Override
    public int getViewId() {
        return R.layout.fragment_collection_list;
    }

    @Override
    protected void afterViews() {

        Bundle bundle = getArguments();
        if (bundle != null) {
            period = bundle.getString(StringConstants.PERIOD);
        }
        adapter = new GalleryMyCollectionListAdapter(getContext(), this, list);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//可防止Item切换
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);
        recyclerView.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                if (isLogin())
                    getListData(true);
                else
                    showEmpty();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                if (isLogin())
                    getListData(false);
                else
                    showEmpty();
            }
        });
        /**
         * 设置item点击事件
         * */
        adapter.setOnCollerctonItemClickListener(new GalleryItemClickListener() {
            /**
             * item 点击
             * */
            @Override
            public void onItemClickListener(int position) {
                String[] listData = new String[]{ //页面调转
                        StringConstants.ID, list.get(position).getItem_Id(),
                        StringConstants.POSITION, position + ""
                };
                HashMap<String, String> hashMap = IntentUtils.getHashObj(listData);
                startClass(R.string.GalleryDetailActivity, hashMap);//搜索
            }

            @Override
            public void like(int position, boolean isLike) {
                praise(position, isLike);
            }

            @Override
            public void collection(int position, boolean isCollection) {
                collect(position, isCollection);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                firstPosition = CommonUtils.findMax(layoutManager.findFirstVisibleItemPositions(null));
                if (firstPosition < 10) {//防止顶部留白
                    layoutManager.invalidateSpanAssignments();
                }
            }
        });

        //未登录
        if (!isLogin()) {
            showEmpty();
        } else {
            getListData(true);
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
     * 登录成功,清空关注
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginSuccessEvent event) {
        if (event != null) {
            //先展示内容
            mLoadingLayout.showContent();
            //startData(false);
            getListData(false);
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
            getListData(true);
        }
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateeData(PictureDetailsData pictureDetailsData) {
        if (pictureDetailsData == null)
            return;

        if (pictureDetailsData.getPosition()==-1){
            if (isLogin())
                getListData(true);
            return;
        }
        if(CommonUtils.ListNotNull(list)) {
            if (pictureDetailsData.getFavorites().getStatus() == 0) {
                list.remove(list.get(pictureDetailsData.getPosition()));
                adapter.notifyUI(list);

            } else if (pictureDetailsData.getFavorites().getStatus() == 1) {
                if (isLogin())
                    getListData(true);

            }else if (pictureDetailsData.getTread()!=null){
                MyCollectionData childBean = list.get(pictureDetailsData.getPosition());
                MyCollectionData.LikeBean likeBean = new MyCollectionData.LikeBean();
                likeBean.setNum(pictureDetailsData.getLike().getNum());
                likeBean.setStatus(pictureDetailsData.getLike().getStatus());
                MyCollectionData.FavoritesBean favoritesBean = new MyCollectionData.FavoritesBean();
                // favoritesBean.setStatus(pictureDetailsData.getFavorites().getStatus());
                //  favoritesBean.setNum(Integer.valueOf(pictureDetailsData.getFavorites().getNum()));
                childBean.setLike(likeBean);
                //childBean.setFavorites(favoritesBean);
                list.set(pictureDetailsData.getPosition(), childBean);
                adapter.notifyUI(list);

            }
        } else {
            showEmpty();
            if (pictureDetailsData.getFavorites().getStatus() == 1) {
                if (isLogin())
                    getListData(true);
            }
        }
    }


    /**
     * 点赞
     *
     * @param position
     * @param isPraise
     */
    private void praise(int position, boolean isPraise) {
        if (!CommonUtils.ListNotNull(list))
            return;
        MyCollectionData mainHomeData = list.get(position);
        if (mainHomeData != null) {
            if (mainHomeData.getLoadingStatus() == 0 && isLogin()) {
                mainHomeData.setLoadingStatus(1);

                new GalleryImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                    @Override
                    public void onSuccess(LikeAndFavoriteData response, int id) {
                        if (response != null) {
                            mainHomeData.getLike().setNum(response.getNum() + "");
                            mainHomeData.getLike().setStatus(response.getType());
                          //  refreshEvent(mainHomeData);
                          //  adapter.notifyItemChanged(position);
                        }
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        mainHomeData.setLoadingStatus(0);
                        adapter.notifyItemChanged(position);
                    }
                }.setNeedDialog(false)).getClickLike(mainHomeData.getItem_Id(), period, 1 + "");
            }
            mainHomeData.getLike().setStatus(isPraise ? 1 : 0);
            int like_num = Integer.valueOf(mainHomeData.getLike().getNum());
            mainHomeData.getLike().setNum(isPraise ? ++like_num + "" : --like_num + "");
            //adapter.notifyUI(list);
            adapter.notifyItemChanged(position);
        }
    }

    private void refreshEvent(MyCollectionData mainHomeData) {

        EventBusUtil.post(mainHomeData);
    }

    /**
     * 收藏
     *
     * @param position
     * @param isCollect
     */
    private void collect(int position, boolean isCollect) {
        if (!CommonUtils.ListNotNull(list))
            return;
        MyCollectionData mainHomeData = list.get(position);
        if (mainHomeData != null) {
            if (mainHomeData.getLoadingStatus() == 0 && isLoginToast()) {
                mainHomeData.setLoadingStatus(2);
                int status = mainHomeData.getFavorites().getStatus();
                int favorites_num = Integer.valueOf(mainHomeData.getFavorites().getNum());
                new GalleryImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                    @Override
                    public void onSuccess(LikeAndFavoriteData response, int id) {
                        if (response != null) {
                            mainHomeData.getFavorites().setStatus(response.getType());
                            mainHomeData.getFavorites().setNum(response.getNum());
                            // refreshEvent(mainHomeData);
                            if (response.getType() == 0) {
                                list.remove(list.get(position));
                               // adapter.notifyUI(list);
                            }
                        }
                    }

                    @Override
                    public void onError(String msg, String code) {
                        super.onError(msg, code);
                       // mainHomeData.getFavorites().setStatus(status);
                       // mainHomeData.getFavorites().setNum(favorites_num);

                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        mainHomeData.setLoadingStatus(0);
                        if (adapter != null) {
                            adapter.notifyUI(list);
                        }
                    }
                }.setNeedDialog(false)).postClickFavorites(mainHomeData.getItem_Id(), period);
                mainHomeData.getFavorites().setStatus((isCollect ? 1 : 0));
                int num = Integer.valueOf(mainHomeData.getFavorites().getNum());
                mainHomeData.getFavorites().setNum(isCollect ? ++num : --num);
                adapter.notifyItemChanged(position);
            }
        }

    }

    /**
     * 获取列表
     */
    private void getListData(boolean isRefresh) {
        if (isRefresh) {
            pageIndex = 0;
        }
        pageIndex++;
      new GalleryImpl(new MarkSixNetCallBack<List<MyCollectionData>>(this, MyCollectionData.class) {
            @Override
            public void onSuccess(List<MyCollectionData> response, int id) {
                setData(response, isRefresh);
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
        }.setNeedDialog(false)).getGalleryMyCollection(String.valueOf(pageIndex));
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
     * @param response
     */
    private void setData(List<MyCollectionData> response, boolean isRefresh) {
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

    @Override
    protected void loadData() {
        // getListData(true);
    }
}
