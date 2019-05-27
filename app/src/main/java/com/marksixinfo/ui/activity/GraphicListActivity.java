package com.marksixinfo.ui.activity;

import android.text.TextUtils;
import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.adapter.GraphicListAdapter;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.bean.PictureAnswerData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.net.impl.GalleryImpl;
import com.marksixinfo.utils.CommonUtils;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * 图解小组
 * */
public class GraphicListActivity extends MarkSixActivity implements View.OnClickListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    private GraphicListAdapter adapter;
    private int pageIndex = 0;//页数
    private String id;
    private String period;
    private List<PictureAnswerData> listData = new ArrayList<>();
    private String imageUrl;
    private int position;
    @Override
    public int getViewId() {
        return R.layout.activity_graphic;
    }

    @Override
    public void afterViews() {
        markSixTitle.init("图解小组", "", "", 0,this) ;
        adapter = new GraphicListAdapter(this, listData);
        imageUrl =  getStringParam(StringConstants.IMAGE_URL);
        id =  getStringParam(StringConstants.ID);
        period =  getStringParam(StringConstants.PERIOD);
        getPictureAnswer(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(getContext().getResources().getDrawable(R.drawable.shape_item_decoration));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(adapter);
        mLoadingLayout.setRetryListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mLoadingLayout.setEmptyText(getContext().getResources().getString(R.string.no_search_gallery));


        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
               // refresh(true);
                getPictureAnswer(true);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
              //  refresh(false);
                getPictureAnswer(false);
            }
        });

        adapter.setOnCollerctonItemClickListener(new SucceedCallBackListener() {
            @Override
            public void succeedCallBack(Object o) {
                position = (int) o;
                startActivity(listData.get(position));
            }
        });
    }
    /**
     * 跳转到图解页面
     * */
    private void startActivity(PictureAnswerData pictureAnswerData){

        String[] list = new String[]{
                StringConstants.ID, pictureAnswerData.getId(),
                StringConstants.IMAGE_URL,pictureAnswerData.getImageUri()
        };
        HashMap<String, String> hashMap = IntentUtils.getHashObj(list);

        startClass(R.string.GalleryExplainDetailActivity, hashMap);//搜索

    }



    /**
     * 评论的评论页面跟新
     *
     *
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LikeAndFavoriteData event) {
        if (event != null) {//回复帖子列表页,刷新详情回复数量
           /* CommentsListDetail detail = event.getDetail();
            if (detail != null && CommonUtils.ListNotNull(list)) {
                commListDataChange(detail, list);
            }*/
           int num = event.getNum();
           listData.get(position).setReply_Num(num+"");
           adapter.notifyItemChanged(position);
        }
    }

    /**
     * 获取图库详情 图解
     **/
    private void getPictureAnswer(boolean isRefresh) {
        if (isRefresh) {
            pageIndex = 0;
        }
        pageIndex++;
        new GalleryImpl(new MarkSixNetCallBack<List<PictureAnswerData>>(this, PictureAnswerData.class) {
            @Override
            public void onSuccess(List<PictureAnswerData> response, int id) {
                if (response != null) {
                    //详情 数据
                    if (isRefresh) {
                        listData.clear();
                    }
                    if (!TextUtils.isEmpty(imageUrl)) {
                        for (PictureAnswerData pictureAnswerData : response) {
                            pictureAnswerData.setImageUri(imageUrl);
                        }
                    }
                    if (CommonUtils.ListNotNull(response)) {
                        listData.addAll(response);
                    } else {
                        refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                    }
                    if (CommonUtils.ListNotNull(listData)) {
                        mLoadingLayout.showContent();
                    } else {
                        mLoadingLayout.showEmpty();
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String msg, String code) {
                if (!CommonUtils.ListNotNull(listData)) {
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
        }.setNeedDialog(false)).getAnswer(id, period,pageIndex);
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
    @Override
    public void onClick(View view) {

    }
}
