package com.marksixinfo.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.CommentListAdapter;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.CommentsListData;
import com.marksixinfo.bean.CommentsListDetail;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.CommentListEvent;
import com.marksixinfo.interfaces.ICommentListInterface;
import com.marksixinfo.interfaces.NoDoubleClickListener;
import com.marksixinfo.net.impl.ForumImpl;
import com.marksixinfo.net.impl.GalleryImpl;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.NumberUtils;
import com.marksixinfo.widgets.ConcernTextView;
import com.marksixinfo.widgets.GoodView;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.MyClassicsFooter;
import com.marksixinfo.widgets.backedittext.BackInputDialog;
import com.marksixinfo.widgets.glide.GlideUtil;
import com.marksixinfo.widgets.sparkbutton.SparkButton;
import com.marksixinfo.widgets.swipeback.SwipeBackLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 回复用户列表
 *
 * @auther: Administrator
 * @date: 2019/3/29 0029 17:55
 */
public class CommentListActivity extends MarkSixActivity implements
        BackInputDialog.InputTextListener, ICommentListInterface, View.OnClickListener {


    @BindView(R.id.tv_title_number)
    TextView tvTitleNumber;
    @BindView(R.id.recyclerView)
    ListView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.iv_praise)
    SparkButton mTvPraise;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.tv_past_message_count)
    TextView tvPastMessageCount;
    @BindView(R.id.ll_bottom_content)
    LinearLayout llBottomContent;


    private ImageView mIvUserPhoto;
    private TextView mTvUserName;
    private ConcernTextView mTvConcern;
    private TextView mTvCommentContent;
    private TextView mTvTime;
    private View viewHeadPraise;
    private TextView mTvHeadPraise;


    private List<CommentsListData> list = new ArrayList<>();
    private CommentListAdapter adapter;
    private int pageIndex = 0;//页数帖子
    private String id = "";//回复id
    private String mId = "";//用户id
    private CommentsListDetail detail = new CommentsListDetail();
    private View mLlContent;//headView
    private BackInputDialog backInputDialog;
    private int type = 0;//分类 0,头条  1,论坛  2,图库图片 3,图库图解
    private String nickname;
    private int like_status;
    private GoodView goodView;
    private CommentsListData data;
    private boolean isRefresh;

    @Override
    public int getViewId() {
        return R.layout.activity_comment_list;
    }

    @Override
    public void afterViews() {

        id = getStringParam(StringConstants.ID);
        type = NumberUtils.stringToInt(getStringParam(StringConstants.TYPE));

        adapter = new CommentListAdapter(this, list, this);
        mLoadingLayout.setRetryListener(this);
        goodView = new GoodView(this);
        refreshLayout.setEnableRefresh(false);//禁止下拉刷新
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                getCommentList(false);
            }
        });


        /**********headView*********/
        View headView = View.inflate(this, R.layout.item_list_comment_head, null);
        mLlContent = headView.findViewById(R.id.ll_content);
        mIvUserPhoto = headView.findViewById(R.id.iv_user_photo);
        mTvUserName = headView.findViewById(R.id.tv_user_name);
        mTvConcern = headView.findViewById(R.id.tv_concern);
        mTvCommentContent = headView.findViewById(R.id.tv_comment_content);
        mTvTime = headView.findViewById(R.id.tv_time);
        viewHeadPraise = headView.findViewById(R.id.view_head_praise);
        mTvHeadPraise = headView.findViewById(R.id.tv_head_praise);

        recyclerView.addHeaderView(headView);
        mLlContent.setVisibility(View.GONE);
        recyclerView.setAdapter(adapter);

        refresh();
    }


    /**
     * 刷新数据
     */
    private void refresh() {
        // 0,头条  1,论坛  2,图库图片 3,图库图解
        if (type == 0) {
            new HeadlineImpl(commentsListDetailCallBack.setNeedDialog(false)).commentsListDetail(id);
        } else if (type == 1) {
            new ForumImpl(commentsListDetailCallBack.setNeedDialog(false)).commentsListDetail(id);
        } else {
            new GalleryImpl(commentsListDetailCallBack.setNeedDialog(false)).commentsListDetail(id, type);
        }
    }

    MarkSixNetCallBack commentsListDetailCallBack = new MarkSixNetCallBack<CommentsListDetail>(this, CommentsListDetail.class) {
        @Override
        public void onSuccess(CommentsListDetail response, int id) {
            if (response != null) {
                CommentsListDetail.LikeBean like = response.getLike();
                if (like != null) {
                    response.setLike_Num(like.getNum());
                    response.setLike_status(like.getStatus());
                }
                mId = response.getId();
                detail = response;
                refreshEventData();
                setDetailData();
                getCommentList(true);
            }
        }

        @Override
        public void onError(String msg, String code) {
            mLoadingLayout.showError();
        }
    };

    /**
     * 设置head数据
     */
    private void setDetailData() {
        String user_id = detail.getUser_id();
        String face = detail.getFace();
        nickname = detail.getNickname();
        String content = detail.getContent();
        String add_time = detail.getAdd_Time();
        int reply_num = detail.getReply_Num();
        like_status = detail.getLike_status();
        int like_num = detail.getLike_Num();
        int look_status = detail.getLook_status();

        tvTitleNumber.setText(reply_num > 0 ? String.valueOf(reply_num) + "条回复" : "");

        //评论数
        tvPastMessageCount.setText(reply_num > 0 ? CommonUtils.getThousandNumber(reply_num) : "");
        tvPastMessageCount.setVisibility(reply_num > 0 ? View.VISIBLE : View.INVISIBLE);

        GlideUtil.loadCircleImage(face, mIvUserPhoto);

        mTvUserName.setText(CommonUtils.StringNotNull(nickname) ? CommonUtils.CommHandleText2(nickname) : "");
        mTvCommentContent.setText(CommonUtils.StringNotNull(content) ? content : "");
        mTvTime.setText(CommonUtils.StringNotNull(add_time) ? add_time : "");
        mTvHeadPraise.setText(like_num > 0 ? CommonUtils.getThousandNumber(like_num) : "");
        CommonUtils.setPraiseStatus(getContext(), mTvHeadPraise, like_status);

//        CommonUtils.setPraise(mTvPraise, like_status);
        mTvPraise.setEnabled(true);
        mTvPraise.setChecked(like_status == 1);

        mTvConcern.setLookStatus(look_status);


        //关注/取消关注
        mTvConcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mTvConcern.getLook_status() != -1) {
                    if (mTvConcern.startLookStatus()) {
                        detail.setLook_status(-1);//接口中
                        concern(user_id);
                    }
                }
            }
        });

        //点赞/取消点赞
        mTvHeadPraise.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                if (like_status != 1) {
                    goodView.setTextColor(getResources().getColor(R.color.colorPrimary)).setText("+1").show(viewHeadPraise);
                }
                praiseComment(mId);
            }
        });

        //查看用户
        mIvUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type < 2) {
                    goToUserCenterActivity(user_id, type);
                }
            }
        });

        //查看用户
        mTvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type < 2) {
                    goToUserCenterActivity(user_id, type);
                }
            }
        });
        mLlContent.setVisibility(View.VISIBLE);

        //点赞自己
        mTvPraise.setOnClickListener(new NoDoubleClickListener() {

            @Override
            protected void onNoDoubleClick(View v) {
                mTvPraise.setChecked(like_status != 1);
                if (like_status != 1) {
                    mTvPraise.playAnimation();
                }
                praiseComment(mId);
            }
        });
        if (llBottomContent.getVisibility() != View.VISIBLE) {
            llBottomContent.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 关注
     */
    private void concern(String user_id) {
        new HeadlineImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
            @Override
            public void onSuccess(LikeAndFavoriteData response, int id) {
                if (response != null) {
                    int type = response.getType();
                    detail.setLook_status(type);
                    mTvConcern.setLookStatus(type);
                    refreshEventData();
                }
            }

            @Override
            public void onError(String msg, String code) {
                super.onError(msg, code);
                int look_status = mTvConcern.getStatus();
                detail.setLook_status(look_status);
                mTvConcern.setLookStatus(look_status);
            }
        }.setNeedDialog(false)).concernUser(user_id);

    }


    /**
     * 获取评论列表
     *
     * @param isRefresh
     */
    private void getCommentList(boolean isRefresh) {
        this.isRefresh = isRefresh;
        if (isRefresh) {
            pageIndex = 0;
        }
        // 0,头条  1,论坛  2,图库图片 3,图库图解
        if (type == 0) {
            new HeadlineImpl(commentListCallBack.setNeedDialog(false)).commentsList(mId, String.valueOf(++pageIndex));
        } else if (type == 1) {
            new ForumImpl(commentListCallBack.setNeedDialog(false)).commentsList(mId, String.valueOf(++pageIndex));
        } else {
            new GalleryImpl(commentListCallBack.setNeedDialog(false)).commentsList(mId, String.valueOf(++pageIndex), type);
        }
    }


    MarkSixNetCallBack commentListCallBack = new MarkSixNetCallBack<List<CommentsListData>>(this, CommentsListData.class) {
        @Override
        public void onSuccess(List<CommentsListData> response, int id) {
            loadDone();
            setData(response, isRefresh);
        }

        @Override
        public void onError(String msg, String code) {
            loadDone();
            if (!CommonUtils.ListNotNull(list)) {
                mLoadingLayout.showError();
            }
        }
    };

    /**
     * 请求完成
     */
    private void loadDone() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.resetNoMoreData();
            refreshLayout.finishLoadMore();
        }
    }


    /**
     * 设置网络数据
     *
     * @param response
     * @param isRefresh
     */
    private void setData(List<CommentsListData> response, boolean isRefresh) {
        if (isRefresh) {
            list.clear();
        }
        if (CommonUtils.ListNotNull(response)) {
            list.addAll(response);
            if (response.size() < 10) {
                handleEmpty();
            }
        } else {
            handleEmpty();
        }
        if (adapter != null) {
            adapter.notifyUI(list);
        }
        if (CommonUtils.ListNotNull(list) || detail != null) {
            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }
    }


    /**
     * 处理空太页
     */
    private void handleEmpty() {
        try {
            MyClassicsFooter footer = (MyClassicsFooter) refreshLayout.getRefreshFooter();
            footer.setTextTextNothing("");

            handleList(CommonUtils.ListNotNull(list));
            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 添加footView条目,只加一次
     */
    private void handleList(boolean b) {
        if (CommonUtils.ListNotNull(list)) {
            if (list.get(list.size() - 1).getType() == 2) {
                list.remove(list.size() - 1);
            }
        }
        CommentsListData detailCommentData = new CommentsListData();
        detailCommentData.setType(2);
        detailCommentData.setFootViewText(b ? getString(R.string.load_all_comment) : getString(R.string.not_comment));
        list.add(detailCommentData);
    }


    /**
     * 点击事件
     *
     * @param view
     */
    @OnClick({R.id.iv_close, R.id.rl_write_comment, R.id.rl_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.rl_write_comment://回复框
            case R.id.rl_comment:
                showEdit();
                break;
        }
    }

    /**
     * 回复框
     */
    private void showEdit() {
        if (isLoginToast()) {
            if (backInputDialog == null) {
                backInputDialog = new BackInputDialog(this, this);
            }
            backInputDialog.show("回复@" + nickname + "：");
        }
    }

    @Override
    public void onInputText(String text) {
        comment(text);
    }


    /**
     * 回复用户
     *
     * @param content
     */
    private void comment(String content) {
        // 0,头条  1,论坛  2,图库图片 3,图库图解
        if (type == 0) {
            new HeadlineImpl(commentCallBack).commentsUser(mId, content);
        } else if (type == 1) {
            new ForumImpl(commentCallBack).commentsUser(mId, content);
        } else {//1:图库 2:图解 3:回帖 4图解回帖
            String mType = "";
            if (type == 2) {
                mType = "3";
            } else {
                mType = "4";
            }
            new GalleryImpl(commentCallBack).postPullReply(content, mId, "", mType);
        }
    }


    MarkSixNetCallBack commentCallBack = new MarkSixNetCallBack<Object>(this, Object.class) {
        @Override
        public void onSuccess(Object reply_num, int id) {
            refresh();
        }
    };

    /**
     * 点赞我的评论
     */
    private void praiseComment(String id) {
        if (detail.getLoadingStatus() == 0 && isLogin()) {
            // 0,头条  1,论坛  2,图库图片 3,图库图解
            if (type == 0) {
                new HeadlineImpl(praiseHeadCallBack.setNeedDialog(false)).praiseComment(id);
            } else if (type == 1) {
                new ForumImpl(praiseHeadCallBack.setNeedDialog(false)).forumPraiseComment(id);
            } else {
                String mType = "";
                if (type == 2) {
                    mType = "3";
                } else {
                    mType = "5";
                }
                new GalleryImpl(praiseHeadCallBack.setNeedDialog(false)).getClickLike(id, "", mType);
            }
        }
        detail.setLoadingStatus(1);
        like_status = like_status == 1 ? 0 : 1;
        int num = detail.getLike_Num() + (like_status == 1 ? +1 : -1);
        mTvHeadPraise.setText(num > 0 ? CommonUtils.getThousandNumber(num) : "");
        mTvPraise.setChecked(like_status == 1);
        CommonUtils.setPraiseStatus(getContext(), mTvHeadPraise, like_status);
        detail.setLike_status(like_status);
        detail.setLike_Num(num);
    }

    MarkSixNetCallBack praiseHeadCallBack = new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
        @Override
        public void onSuccess(LikeAndFavoriteData response, int id) {
            if (response != null) {
                int num = response.getNum();
                like_status = response.getType();
                mTvHeadPraise.setText(num > 0 ? CommonUtils.getThousandNumber(num) : "");
                mTvPraise.setChecked(like_status == 1);
                CommonUtils.setPraiseStatus(getContext(), mTvHeadPraise, like_status);
                detail.setLike_status(like_status);
                detail.setLike_Num(num);
                refreshEventData();
            }
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            if (detail != null) {
                detail.setLoadingStatus(0);
            }
        }
    };


    @Override
    public void checkUser(String id) {
        goToUserCenterActivity(id, type);
    }

    @Override
    public void praise(int position, boolean isPraise) {
        if (CommonUtils.ListNotNull(list)) {
            data = list.get(position);
            if (data != null) {
                String id = data.getId();
                praiseList(id);
            }
        }
    }


    /**
     * 评论点赞
     *
     * @param id
     */
    private void praiseList(String id) {
        if (data.getLoadingStatus() == 0 && isLogin()) {
            // 0,头条  1,论坛  2,图库图片 3,图库图解
            if (type == 0) {
                new HeadlineImpl(praiseListCallBack.setNeedDialog(false)).praiseComment(id);
            } else if (type == 1) {
                new ForumImpl(praiseListCallBack.setNeedDialog(false)).forumPraiseComment(id);
            } else {
                String mType = "";
                if (type == 2) {
                    mType = "4";
                } else {
                    mType = "6";
                }
                new GalleryImpl(praiseListCallBack.setNeedDialog(false)).getClickLike(id, "", mType);
            }
        }
        data.setLoadingStatus(1);
        int like_status = data.getLike_status() == 1 ? 0 : 1;
        int like_num = data.getLike_Num();
        data.setLike_status(like_status);
        data.setLike_Num(like_status == 1 ? ++like_num : --like_num);
        adapter.notifyUI(list);
    }


    MarkSixNetCallBack praiseListCallBack = new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
        @Override
        public void onSuccess(LikeAndFavoriteData response, int id) {
            if (response != null) {
                data.setLike_status(response.getType());
                data.setLike_Num(response.getNum());
                adapter.notifyUI(list);
            }
        }

        @Override
        public void onAfter(int id) {
            super.onAfter(id);
            if (data != null) {
                data.setLoadingStatus(0);
            }
        }
    };

    @Override
    public void onClick(View view) {
        mLoadingLayout.showLoading();
        refresh();
    }


    /**
     * 接口刷新实体
     */
    private void refreshEventData() {
        CommentListEvent commentListEvent = new CommentListEvent();
        commentListEvent.setDetail(detail);
        commentListEvent.setType(type);
        EventBusUtil.post(commentListEvent);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mSwipeBackLayout != null) {
            mSwipeBackLayout.setMaskAlpha(50);
            mSwipeBackLayout.setDirectionMode(SwipeBackLayout.FROM_TOP);
            setStatusBarColor(getResources().getColor(R.color.no_color), 0);
        }
    }
}
