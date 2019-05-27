package com.marksixinfo.ui.activity;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.ForumDetailAdapter;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.CommentsListDetail;
import com.marksixinfo.bean.DetailCommentData;
import com.marksixinfo.bean.DetailCommentListData;
import com.marksixinfo.bean.DetailData;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.constants.UrlStaticConstants;
import com.marksixinfo.evenbus.CommListDataEvent;
import com.marksixinfo.evenbus.CommentListEvent;
import com.marksixinfo.interfaces.IDetailCommentInterface;
import com.marksixinfo.interfaces.NoDoubleClickListener;
import com.marksixinfo.net.impl.ForumImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.ReleaseUtils;
import com.marksixinfo.widgets.ConcernTextView;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.MyClassicsFooter;
import com.marksixinfo.widgets.MySmartRefreshLayout;
import com.marksixinfo.widgets.backedittext.BackInputDialog;
import com.marksixinfo.widgets.glide.GlideUtil;
import com.marksixinfo.widgets.ninegridimg.ItemImageClickListener;
import com.marksixinfo.widgets.sparkbutton.SparkButton;
import com.marksixinfo.widgets.umengshare.ShareParams;
import com.marksixinfo.widgets.umengshare.UMShareDialog;
import com.marksixinfo.widgets.umengshare.UMengShareUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * 功能描述:  论坛详情页
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/4/2 0002 12:58
 */
public class ForumDetailActivity extends MarkSixActivity implements IDetailCommentInterface,
        ItemImageClickListener<String>, BackInputDialog.InputTextListener, View.OnClickListener {


    private LinearLayout mLlUserContent;
    private ImageView mIvUserPhoto;
    private TextView mTvUserName;
    private TextView mTvPeriodNumber;
    private ImageView mIvOfficial;
    private TextView mTvUserDomain;
    private TextView mTvTime;
    private ConcernTextView mTvConcern;
    private TextView mTvTitle;
    private TextView mTvContent;

    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.rl_write_comment)
    RelativeLayout rlWriteComment;
    @BindView(R.id.tv_past_message_count)
    TextView tvPastMessageCount;
    @BindView(R.id.rl_comment)
    RelativeLayout rlComment;
    @BindView(R.id.iv_collect)
    SparkButton ivCollect;
    @BindView(R.id.iv_praise)
    SparkButton ivPraise;
    @BindView(R.id.ll_bottom_content)
    LinearLayout llBottomContent;

    private String id = "";
    private ForumDetailAdapter adapter;
    private int pageIndex = 0;//页数
    private List<DetailCommentData> list = new ArrayList<>();
    private String user_id;
    private BackInputDialog backInputDialog;
    private int reply_num;
    private boolean isScrollComment;
    private boolean isInComment;
    private boolean isLoadDone;
    private List<String> pic;
    private DetailData data = new DetailData();
    private UMShareDialog umShareDialog;
    private ShareParams shareParams;
    private ReleaseUtils releaseUtils;
    private int fav_status;
    private int like_status;
    private long lastClickTime;

    @Override
    public int getViewId() {
        return R.layout.activity_forum_detail;
    }

    @Override
    public void afterViews() {

        markSixTitle.init("");

        id = getStringParam(StringConstants.ID);

        isScrollComment = Boolean.parseBoolean(getStringParam(StringConstants.DETAIL_SCROLL_COMMENT));

        /**********headView*********/
        View headView = View.inflate(this, R.layout.head_view_forum_detail, null);
        initHeadView(headView);

        adapter = new ForumDetailAdapter(this, list, this, this);
        listView.addHeaderView(headView);
        listView.setAdapter(adapter);
        mLoadingLayout.setRetryListener(this);


        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                getDetailData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                getCommentList();
            }
        });

        getDetailData();
    }


    /**
     * 头部View
     *
     * @param view
     */
    private void initHeadView(View view) {
        mLlUserContent = view.findViewById(R.id.ll_user_content);
        mLlUserContent.setOnClickListener(this);
        mIvUserPhoto = view.findViewById(R.id.iv_user_photo);
        mTvUserName = view.findViewById(R.id.tv_user_name);
        mTvPeriodNumber = view.findViewById(R.id.tv_period_number);
        mIvOfficial = view.findViewById(R.id.iv_official);
        mTvUserDomain = view.findViewById(R.id.tv_user_domain);
        mTvTime = view.findViewById(R.id.tv_time);
        mTvConcern = view.findViewById(R.id.tv_concern);
        mTvConcern.setOnClickListener(this);
        mTvTitle = view.findViewById(R.id.tv_title);
        mTvContent = view.findViewById(R.id.tv_content);
    }


    /**
     * 获取详情
     */
    private void getDetailData() {
        isLoadDone = false;
        pageIndex = 0;
        list.clear();
        adapter.notifyUI(list);
        new ForumImpl(new MarkSixNetCallBack<DetailData>(this, DetailData.class) {
            @Override
            public void onSuccess(DetailData response, int id) {
                setDetailData(response);
                getCommentList();
            }

            @Override
            public void onError(String msg, String code) {
                mLoadingLayout.showError();
            }
        }.setNeedDialog(false)).getForumDetail(id);
    }

    private void setDetailData(DetailData response) {
        if (response != null) {
            data = response;

            if (listView.getVisibility() != View.VISIBLE) {
                listView.setVisibility(View.VISIBLE);
                mLoadingLayout.showContent();
            }

            String id = data.getId();
            String title = data.getTitle();
            String content = data.getContent();
            String add_time = data.getAdd_Time();
            String face = data.getFace();
            String Nickname = data.getNickname();
            user_id = data.getUser_Id();
            int look_status = data.getLook_status();
            String remark = data.getRemark();
            int period = data.getPeriod();
            int level = data.getLevel();
            reply_num = data.getReply_num();
            fav_status = data.getFav_status();
            like_status = data.getLike_status();
            pic = data.getPic();


            //设置关注
            mTvConcern.setLookStatus(look_status);

            mTvTime.setText(CommonUtils.StringNotNull(add_time) ? add_time : "");


            mTvTitle.setText(CommonUtils.StringNotNull(title) ? title : "");
            mTvTitle.setVisibility(CommonUtils.StringNotNull(title) ? View.VISIBLE : View.GONE);


            mTvContent.setText(CommonUtils.StringNotNull(content) ? content : "");
            mTvContent.setVisibility(CommonUtils.StringNotNull(content) ? View.VISIBLE : View.GONE);

            GlideUtil.loadCircleImage(face, mIvUserPhoto);
            mIvUserPhoto.setVisibility(View.VISIBLE);

            mTvUserName.setText(CommonUtils.StringNotNull(Nickname) ? CommonUtils.CommHandleText2(Nickname) : "");
//            markSixTitle.init("", CommonUtils.StringNotNull(Nickname) ? Nickname : "");

            //分享
            markSixTitle.init("", CommonUtils.StringNotNull(Nickname) ? Nickname : ""
                    , "", R.drawable.icon_share, this);

            //官方
            CommonUtils.setUserLevel(mIvOfficial, 0);//论坛模块不显示

            //网址
            mTvUserDomain.setText(CommonUtils.StringNotNull(remark) ? remark : "");
            mTvUserDomain.setVisibility(CommonUtils.StringNotNull(remark) ? View.VISIBLE : View.GONE);

            //期数
            if (period > 0) {
                mTvPeriodNumber.setVisibility(View.VISIBLE);
                mTvPeriodNumber.setText(CommonUtils.fixThree(String.valueOf(period)) + "期");
            } else {
                mTvPeriodNumber.setVisibility(View.GONE);
                mTvPeriodNumber.setText("");
            }

            //评论数
            tvPastMessageCount.setText(reply_num > 0 ? CommonUtils.getThousandNumber(reply_num) : "");
            tvPastMessageCount.setVisibility(reply_num > 0 ? View.VISIBLE : View.INVISIBLE);

            ivPraise.setEnabled(true);
            ivPraise.setChecked(like_status == 1);

            ivCollect.setEnabled(true);
            ivCollect.setChecked(fav_status == 1);

            //点赞
            ivPraise.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    lastClickTime = getLastClickTime();
                    ivPraise.setChecked(like_status != 1);
                    if (like_status != 1) {
                        ivPraise.playAnimation();
                    }
                    praise();
                }
            }.setLastClickTime(lastClickTime));

            //收藏
            ivCollect.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    lastClickTime = getLastClickTime();
                    collect();
                }
            }.setLastClickTime(lastClickTime));

            if (llBottomContent.getVisibility() != View.VISIBLE) {
                llBottomContent.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getCommentList() {
        new ForumImpl(new MarkSixNetCallBack<DetailCommentListData>(this, DetailCommentListData.class) {
            @Override
            public void onSuccess(DetailCommentListData response, int id) {
                loadDone();
                setData(response);
            }

            @Override
            public void onError(String msg, String code) {
                super.onError(msg, code);
                loadDone();
            }
        }.setNeedDialog(false)).getForumCommentList(id, String.valueOf(++pageIndex));
    }


    private void loadDone() {
        isLoadDone = true;
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
            refreshLayout.resetNoMoreData();
        }
    }

    private void setData(DetailCommentListData response) {
        handleList();
        if (response != null && CommonUtils.ListNotNull(response.getList())) {
            list.addAll(response.getList());
            if (response.getList().size() < response.getSize()) {
                handleEmpty();
            }
        } else {
            handleEmpty();
        }
        if (adapter != null) {
            adapter.notifyUI(list);
        }
        if (isScrollComment) {
            listView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (adapter != null) {
                        listView.setSelection(adapter.isAddPic() ? 2 : 1);
                    }
                    isScrollComment = false;
                    isInComment = true;
                }
            }, 100);
        }
    }

    /**
     * 处理空太页
     */
    private void handleEmpty() {
        try {
            MyClassicsFooter footer = (MyClassicsFooter) refreshLayout.getRefreshFooter();
            footer.setTextTextNothing("");

            handleList2(idLoadAll());
            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 添加九宫格条目,只加一次
     */
    private void handleList() {
        if (!CommonUtils.ListNotNull(list) && CommonUtils.ListNotNull(pic)) {
            DetailCommentData detailCommentData = new DetailCommentData();
            detailCommentData.setType(1);
            detailCommentData.setPic(pic);
            list.add(detailCommentData);
            if (adapter != null) {
                adapter.setAddPic(true);
            }
        }
    }

    /**
     * 添加footView条目,只加一次
     */
    private void handleList2(boolean b) {
        if (CommonUtils.ListNotNull(list)) {
            if (list.get(list.size() - 1).getType() == 2) {
                list.remove(list.size() - 1);
            }
        }
        DetailCommentData detailCommentData = new DetailCommentData();
        detailCommentData.setType(2);
        detailCommentData.setFootViewText(b ? getString(R.string.load_all_comment) : getString(R.string.not_comment));
        list.add(detailCommentData);
    }


    /**
     * 是否已经加载全部
     *
     * @return
     */
    private boolean idLoadAll() {
        boolean b;
        if (CommonUtils.ListNotNull(list)) {
            if (list.size() == 1 && adapter.isAddPic()) {
                b = false;
            } else {
                b = true;
            }
        } else {
            b = false;
        }
        return b;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_user_content:
                goToUserCenterActivity(user_id, 1);
                break;
            case R.id.tv_concern://关注
                //关注 0:未关注;1:已关注 2:自己
                if (mTvConcern.getLook_status() != -1) {
                    if (mTvConcern.startLookStatus()) {
                        concern();
                    }
                }
                break;
            case R.id.tv_function://分享
                //分享
                if (umShareDialog == null) {
                    umShareDialog = new UMShareDialog(this);
                }
                ShareParams shareParams = getShareParams();
                UMengShareUtils.getInstance()
                        .doCustomShare(this, shareParams.getUrl(),
                                shareParams.getTitle(), shareParams.getContent(), shareParams.getUmImage(),
                                shareParams.getListener(), umShareDialog);
                if (releaseUtils == null) {
                    releaseUtils = new ReleaseUtils();
                }
                releaseUtils.setPermission(this, null);
                break;
            case R.id.retry_button://网络错误
                mLoadingLayout.showLoading();
                getDetailData();
                break;
        }
    }

    public ShareParams getShareParams() {
        if (shareParams == null) {
            shareParams = new ShareParams(" ", " ", UMengShareUtils.getImageIcon(this, ""),
                    UrlStaticConstants.SHARE_URL, new UMengShareUtils.MyShareListener(getContext()));
        } else {
//            shareParams.setContent(getSecondTitle());//重设副标题
            shareParams.setUmImage(UMengShareUtils.getImageIcon(this, ""));//重设icon

            shareParams.setUrl(UrlStaticConstants.SHARE_URL);
        }

        return shareParams;
    }


    @OnClick({R.id.rl_write_comment, R.id.rl_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_write_comment://写回复
                commentDetail();
                break;
            case R.id.rl_comment://回复锚点
                if (!isLoadDone) {//未加载完成
                    return;
                }
                if (reply_num > 0) {//有评论锚点
                    isInComment = !isInComment;
                    if (isInComment) {
                        listView.setSelection(2);
                    } else {
                        listView.setSelection(0);
                    }
                } else {//无评论弹出评论
                    commentDetail();
                }
                break;
        }
    }

    /**
     * 回复帖子
     */
    private void commentDetail() {
        if (isLoginToast()) {
            if (backInputDialog == null) {
                backInputDialog = new BackInputDialog(this, this);
            }
            backInputDialog.setCommentPosition(-1).setId(id).show(backInputDialog.hint);
        }
    }

    /**
     * 收藏
     */
    private void collect() {
        if (data != null && isLoginToast()) {
            ivCollect.setChecked(fav_status != 1);
            if (fav_status != 1) {
                ivCollect.playAnimation();
            }
            if (data.getLoadingStatus() == 0) {
                int status = data.getFav_status();
                new ForumImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                    @Override
                    public void onSuccess(LikeAndFavoriteData response, int id) {
                        if (response != null) {
                            data.setFav_status(response.getType());
                            data.setFavorites_num(response.getNum());
                            refreshEventData();
                        }
                    }

                    @Override
                    public void onError(String msg, String code) {
                        super.onError(msg, code);
                        data.setFav_status(status);
                        if (ivCollect != null) {
                            ivCollect.setChecked(status == 1);
                        }
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        data.setLoadingStatus(0);
                    }
                }.setNeedDialog(false)).forumFavorites(id);
            }

            data.setLoadingStatus(2);
            fav_status = fav_status == 1 ? 0 : 1;
            ivCollect.setChecked(fav_status == 1);
            data.setFav_status(fav_status);
            setDetailData(data);
        }
    }


    /**
     * 查看回复用户
     *
     * @param id
     */
    @Override
    public void checkUser(String id) {
        goToUserCenterActivity(id, 1);
    }

    /**
     * 点赞回复
     *
     * @param position
     * @param isPraise 是否点赞
     */
    @Override
    public void praise(int position, boolean isPraise) {
        DetailCommentData detailCommentData = CommonUtils.getForumData(list, position);
        if (detailCommentData != null) {
            praiseComment(detailCommentData, detailCommentData.getId());
        }
    }


    /**
     * 点赞帖子
     */
    private void praise() {
        if (data != null) {
            if (data.getLoadingStatus() == 0 && isLogin()) {
                new ForumImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                    @Override
                    public void onSuccess(LikeAndFavoriteData response, int id) {
                        if (response != null) {
                            data.setLike_status(response.getType());
                            data.setLike_num(response.getNum());
                            refreshEventData();
                        }
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        data.setLoadingStatus(0);
                    }
                }.setNeedDialog(false)).forumPraise(id);
            }

            data.setLoadingStatus(1);
            like_status = like_status == 1 ? 0 : 1;
            ivPraise.setChecked(like_status == 1);
            data.setLike_status(like_status);
            setDetailData(data);
        }
    }

    /**
     * 点赞评论
     */
    private void praiseComment(DetailCommentData detailCommentData, String id) {
        if (detailCommentData.getLoadingStatus() == 0 && isLogin()) {
            new ForumImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                @Override
                public void onSuccess(LikeAndFavoriteData response, int id) {
                    if (response != null) {
                        detailCommentData.setLike_status(response.getType());
                        detailCommentData.setLike_Num(response.getNum());
                        adapter.notifyUI(list);
                    }
                }

                @Override
                public void onAfter(int id) {
                    super.onAfter(id);
                    detailCommentData.setLoadingStatus(0);
                }
            }.setNeedDialog(false)).forumPraiseComment(id);
        }
        detailCommentData.setLoadingStatus(1);
        int like_status = detailCommentData.getLike_status();
        like_status = like_status == 1 ? 0 : 1;
        int num = detailCommentData.getLike_Num() + (like_status == 1 ? +1 : -1);
        detailCommentData.setLike_status(like_status);
        detailCommentData.setLike_Num(num);
        adapter.notifyUI(list);
    }


    /**
     * 关注
     */
    private void concern() {
        int status = data.getLook_status();
        data.setLook_status(-1);//接口中
        new ForumImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
            @Override
            public void onSuccess(LikeAndFavoriteData response, int id) {
                if (response != null) {
                    data.setLook_status(response.getType());
                    refreshEventData();
                }
            }

            @Override
            public void onError(String msg, String code) {
                super.onError(msg, code);
                if (mTvConcern != null) {
                    mTvConcern.setLookStatus(status);
                }
            }
        }.setNeedDialog(false)).forumConcernUser(user_id);

    }


    /**
     * 回复用户
     *
     * @param position
     * @param hasComment 是否有回复
     */
    @Override
    public void commentUser(int position, boolean hasComment) {
        DetailCommentData detailCommentData = CommonUtils.getForumData(list, position);
        if (detailCommentData != null) {
            String mId = detailCommentData.getId();
            if (hasComment) {
                goToCommentListActivity(mId, 1);
            } else {
                if (isLoginToast()) {
                    if (backInputDialog == null) {
                        backInputDialog = new BackInputDialog(this, this);
                    }
                    backInputDialog
                            .setCommentPosition(position)
                            .setId(mId)
                            .show("回复@" + detailCommentData.getNickname() + "：");
                }
            }
        }
    }

    @Override
    public void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
        CommonUtils.checkImagePreview(this, list, index);
    }

    /**
     * 回复回调
     *
     * @param text
     */
    @Override
    public void onInputText(String text) {
        if (backInputDialog != null) {
            comment(backInputDialog.getCommentPosition() >= 0, backInputDialog.getId(), text);
        }
    }


    /**
     * 回复帖子/回复用户
     *
     * @param content
     */
    private void comment(boolean commentUser, String id, String content) {
        if (commentUser) {//回复用户
            commentUser(id, content);
        } else {
            comment(id, content);
        }
    }

    /**
     * 回复帖子
     *
     * @param id
     * @param content
     */
    private void comment(String id, String content) {
        new ForumImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
            @Override
            public void onSuccess(LikeAndFavoriteData response, int id) {
                if (response != null) {
                    toast("回复成功");
                    int num = response.getNum();
                    //评论数
                    tvPastMessageCount.setText(num > 0 ? CommonUtils.getThousandNumber(num) : "");
                    tvPastMessageCount.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
                    data.setReply_num(num);
                    refreshEventData();
                    handlerCommentList(response.getId(), content);
                }
            }
        }).comments(id, content);
    }

    /**
     * 处理回复本地显示,跳转到第一条
     *
     * @param id
     * @param content
     */
    private void handlerCommentList(String id, String content) {
        DetailCommentData detailComment = CommonUtils.getDetailComment(this, id, content);
        if (detailComment != null) {
            list.add(adapter.isAddPic() ? 1 : 0, detailComment);
            if (((MySmartRefreshLayout) refreshLayout).isFooterNoMoreData()) {
                handleList2(true);
            }
            adapter.notifyUI(list);
            listView.setSelection(adapter.isAddPic() ? 2 : 1);
        }
    }

    /**
     * 回复用户
     *
     * @param id
     * @param content
     */
    private void commentUser(String id, String content) {
        new ForumImpl(new MarkSixNetCallBack<Integer>(this, Integer.class) {
            @Override
            public void onSuccess(Integer response, int id) {
                if (response != null) {
                    toast("回复成功");
                    changeCommentUser(response);
                }
            }
        }).commentsUser(id, content);
    }

    /**
     * 回复用户
     */
    private void changeCommentUser(Integer response) {
        if (backInputDialog != null && CommonUtils.ListNotNull(list)) {
            int commentPosition = backInputDialog.getCommentPosition();
            DetailCommentData detailCommentData = list.get(commentPosition);
            if (detailCommentData != null) {
                detailCommentData.setReply_Num(response);
                adapter.notifyUI(list);
            }
        }
    }


    /**
     * 评论列表操作更新详情页
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommentListEvent event) {
        if (event != null) {//回复帖子列表页,刷新详情回复数量
            if(event.getType()==1) {
                CommentsListDetail detail = event.getDetail();
                if (detail != null && CommonUtils.ListNotNull(list)) {
                    CommonUtils.commListDataChange(detail, list);
                    adapter.notifyUI(list);
                }
            }
        }
    }


    /**
     * 帖子数据变更
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommListDataEvent event) {
        if (event != null && event.getData() != null
                && data != null) {
            CommonUtils.commListDataChange(event.getData(), data);
            setDetailData(data);
        }
    }

    /**
     * 接口刷新实体
     */
    private void refreshEventData() {
        EventBusUtil.post(new CommListDataEvent(CommonUtils.getMainHomeData(data)));
    }
}
