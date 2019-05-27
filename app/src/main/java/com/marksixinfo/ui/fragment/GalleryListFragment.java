package com.marksixinfo.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.marksixinfo.R;
import com.marksixinfo.adapter.GalleryListAdapter;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.bean.GalleryListData;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.bean.MyCollectionData;
import com.marksixinfo.bean.PictureDetailsData;
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.LoginSuccessEvent;
import com.marksixinfo.interfaces.GalleryItemClickListener;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.net.impl.GalleryImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.GalleryListUtils;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.utils.ValueAnimatorUtils;
import com.marksixinfo.widgets.IndexBar;
import com.marksixinfo.widgets.LoadingLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
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
 * 图库列表
 *
 * @Auther: Administrator
 * @Date: 2019/5/8 0008 13:09
 * @Description:
 */
public class GalleryListFragment extends PageBaseFragment implements IndexBar.OnLetterUpdateListener, View.OnClickListener {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.index_bar)
    IndexBar indexBar;
    @BindView(R.id.fl_indexbar_content)
    RelativeLayout flIndexBarContent;


    private GalleryListAdapter adapter;
    List<GalleryListData.ChildBean> list = new ArrayList<>();
    private String period;//期数
    private int type;//分类
    private StaggeredGridLayoutManager layoutManager;
    private GalleryListUtils galleryListUtils = GalleryListUtils.getInstance();
    private int firstPosition;
    private String currentKey;
    private boolean isTouchScroll;//是否点击或滑动索引  引起onScroll
    private int indexBarWidth;//索引宽

    @Override
    public int getViewId() {
        return R.layout.fragment_gallery_list;
    }


    @Override
    protected void afterViews() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            period = bundle.getString(StringConstants.PERIOD);
            type = bundle.getInt(StringConstants.TYPE);
        }


        adapter = new GalleryListAdapter(getContext(), this, list);
        mLoadingLayout.setRetryListener(this);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setEnableAutoLoadMore(false);
        refreshLayout.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4

        adapter.setGalleryItemClickListener(new GalleryItemClickListener() {
            @Override
            public void onItemClickListener(int position) {
                String[] listData = new String[]{ //页面调转
                        StringConstants.ID, list.get(position).getId(),
                        StringConstants.POSITION, position + ""
                };
                HashMap<String, String> hashMap = IntentUtils.getHashObj(listData);
                startClass(R.string.GalleryDetailActivity, hashMap);
            }

            @Override
            public void like(int position, boolean isLike) {
                praise(position, isLike);//
            }

            @Override
            public void collection(int position, boolean isCollection) {
                collect(position, isCollection);
            }
        });
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                getListData();
            }
        });

        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//可防止Item切换
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(null);

        recyclerView.setAdapter(adapter);

        indexBarWidth = UIUtils.dip2px(getContext(), 20);
        UIUtils.setMargins(getContext(), flIndexBarContent, 0, indexBarWidth, 0, indexBarWidth);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int state;
            boolean isHidden;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                firstPosition = CommonUtils.findMax(layoutManager.findFirstVisibleItemPositions(null));
                if (firstPosition < 10) {//防止顶部留白
                    layoutManager.invalidateSpanAssignments();
                }
                if (!isTouchScroll) {
                    if (!CommonUtils.StringNotNull(currentKey) && galleryListUtils.getIndexBarNames().size() > 0) {
                        currentKey = galleryListUtils.getIndexBarNames().get(0);
                    }
                    currentKey = galleryListUtils.finKeyByPosition(firstPosition, currentKey);
                    indexBar.matchingLetter(currentKey, false);
                }
                isTouchScroll = false;
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                state = newState;
                if (state == RecyclerView.SCROLL_STATE_IDLE) {//停止
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (state == RecyclerView.SCROLL_STATE_IDLE) {//停止显示索引
                                ValueAnimatorUtils.getValueShow(500, new SucceedCallBackListener<Float>() {
                                    @Override
                                    public void succeedCallBack(Float o) {
                                        float v = indexBarWidth * o - indexBarWidth;
                                        UIUtils.setMargins(getContext(), flIndexBarContent, 0, indexBarWidth, v, indexBarWidth);
                                    }
                                }).start();
                                isHidden = false;
                            }
                        }
                    }, NumberConstants.GALLERY_INDEX_BAR_DELAYED);
                } else {//滑动隐藏索引
                    if (isHidden) {
                        return;
                    }
                    ValueAnimatorUtils.getValueHidden(500, new SucceedCallBackListener<Float>() {
                        @Override
                        public void succeedCallBack(Float o) {
                            float v = indexBarWidth * o - indexBarWidth;
                            UIUtils.setMargins(getContext(), flIndexBarContent, 0, indexBarWidth, v, indexBarWidth);
                        }
                    }).start();
                    isHidden = true;
                }
            }
        });

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

            getListData();
        }
    }

    /**
     * 获取列表
     */

    private void getListData() {
        new GalleryImpl(new MarkSixNetCallBack<List<GalleryListData>>(this, GalleryListData.class) {
            @Override
            public void onSuccess(List<GalleryListData> response, int id) {
                setData(response);
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
                loadDone();
            }
        }.setNeedDialog(false)).getGalleryMainList(period, String.valueOf(type));
    }

    /**
     * 请求完成
     */
    private void loadDone() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.resetNoMoreData();
        }
    }

    /**
     * 设置数据
     *
     * @param response
     */
    private void setData(List<GalleryListData> response) {
        list = galleryListUtils.handlerList(response);
        indexBar.setIndexName(galleryListUtils.getIndexBarNames());
        indexBar.setListener(this);
        if (adapter != null) {
            adapter.changeDataUi(list);
        }
        if (CommonUtils.ListNotNull(list)) {
            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getUpdateeData(PictureDetailsData pictureDetailsData) {

        if (pictureDetailsData.getPosition()== - 1)
            return;
        GalleryListData.ChildBean childBean = list.get(pictureDetailsData.getPosition());
        GalleryListData.ChildBean.LikeBean likeBean = new GalleryListData.ChildBean.LikeBean();
        likeBean.setNum(pictureDetailsData.getLike().getNum());
        likeBean.setStatus(pictureDetailsData.getLike().getStatus());
        GalleryListData.ChildBean.FavoritesBean favoritesBean = new GalleryListData.ChildBean.FavoritesBean();
        favoritesBean.setStatus(pictureDetailsData.getFavorites().getStatus());
        favoritesBean.setNum(pictureDetailsData.getFavorites().getNum());
        childBean.setLike(likeBean);
        childBean.setFavorites(favoritesBean);
        list.set(pictureDetailsData.getPosition(), childBean);
        adapter.changeDataUi(list);
    }


    /**
     * 收藏点赞，关注后的回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(MyCollectionData event) {
       int p =  selectPoition("4089");
    }

    private int selectPoition(String str) {
        int position =- 0;
        for (int i = 0; i <list.size() ; i++) {
          if(list.get(i).getId().equals(str)){
              position = i;
          }
        }
        return position;
    }

    @Override
    protected void loadData() {
        getListData();
    }

    /**
     * 点击或滑动索引
     *
     * @param letter
     */
    @Override
    public void onLetterUpdate(String letter) {
        setSelect(letter);
    }

    /**
     * 设置点击,如果当前key无数据,选择下一个
     *
     * @param letter
     */
    private void setSelect(String letter) {
        try {
            List<Integer> integer = galleryListUtils.keyIndex.get(letter);
            if (CommonUtils.ListNotNull(integer)) {
                Integer integer1 = integer.get(0);
                Integer integer2 = integer.get(1);
                if (integer1.equals(integer2)) {
                    String nextName = indexBar.getNextName(letter);
                    if (CommonUtils.StringNotNull(nextName)) {
                        setSelect(nextName);
                        return;
                    }
                }
                currentKey = letter;
                if (layoutManager != null) {
                    layoutManager.scrollToPosition(integer1);
                    indexBar.matchingLetter(currentKey, true);
                    isTouchScroll = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 点赞接口
     *
     * @param position
     * @param isPraise
     */
    private void praise(int position, boolean isPraise) {
        if (!CommonUtils.ListNotNull(list))
            return;
        GalleryListData.ChildBean mainHomeData = list.get(position);
        if (mainHomeData != null) {
            if (mainHomeData.getLoadingStatus() == 0 && isLogin()) {
                new GalleryImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                    @Override
                    public void onSuccess(LikeAndFavoriteData response, int id) {
                        if (response != null) {
                            mainHomeData.getLike().setNum(response.getNum() + "");
                            mainHomeData.getLike().setStatus(response.getType());
                            refreshEvent();
                            adapter.notifyItemChanged(position);
                        }
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        mainHomeData.setLoadingStatus(0);
                    }
                }.setNeedDialog(false)).getClickLike(mainHomeData.getId(), period, 1 + "");
            }
            mainHomeData.getLike().setStatus(isPraise ? 1 : 0);
            int like_num = Integer.valueOf(mainHomeData.getLike().getNum());
            mainHomeData.getLike().setNum(isPraise ? ++like_num + "" : --like_num + "");
            mainHomeData.setLoadingStatus(1);
            //adapter.notifyUI(list);
            adapter.notifyItemChanged(position);
        }
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
        GalleryListData.ChildBean mainHomeData = list.get(position);
        if (mainHomeData != null && isLoginToast()) {
            if (mainHomeData.getLoadingStatus() == 0) {
                int status = mainHomeData.getFavorites().getStatus();
                int favorites_num = Integer.valueOf(mainHomeData.getFavorites().getNum());
                new GalleryImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                    @Override
                    public void onSuccess(LikeAndFavoriteData response, int id) {
                        if (response != null) {
                            mainHomeData.getFavorites().setStatus(response.getType());
                            mainHomeData.getFavorites().setNum(response.getNum() + "");
                            refreshEvent();
                            adapter.notifyItemChanged(position);
                        }
                    }

                    @Override
                    public void onError(String msg, String code) {
                        super.onError(msg, code);
                        mainHomeData.getFavorites().setStatus(status);
                        mainHomeData.getFavorites().setNum(favorites_num + "");
                        if (adapter != null) {
                            adapter.notifyItemChanged(position);
                        }
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        mainHomeData.setLoadingStatus(0);
                    }
                }.setNeedDialog(false)).postClickFavorites(mainHomeData.getId(), period);
            }
            mainHomeData.getFavorites().setStatus((isCollect ? 1 : 0));
            int num = Integer.valueOf(mainHomeData.getFavorites().getNum());
            mainHomeData.getFavorites().setNum(isCollect ? ++num + "" : --num + "");
            mainHomeData.setLoadingStatus(2);
            adapter.notifyItemChanged(position);
        }
    }

    private void refreshEvent() {
        PictureDetailsData pictureDetailsData = new PictureDetailsData();
        pictureDetailsData.setPosition(-1);
        EventBusUtil.post(pictureDetailsData);
    }

    @Override
    public void onClick(View v) {
        mLoadingLayout.showLoading();
        getListData();
    }
}
