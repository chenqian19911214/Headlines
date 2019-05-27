package com.marksixinfo.ui.activity;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.DetailCommentAdapter;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.CommentsListDetail;
import com.marksixinfo.bean.DetailCommentData;
import com.marksixinfo.bean.DetailCommentListData;
import com.marksixinfo.bean.DetailData;
import com.marksixinfo.bean.DetailNextData;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.constants.UrlStaticConstants;
import com.marksixinfo.evenbus.CommListDataEvent;
import com.marksixinfo.evenbus.CommentListEvent;
import com.marksixinfo.interfaces.IDetailCommentInterface;
import com.marksixinfo.interfaces.NoDoubleClickListener;
import com.marksixinfo.interfaces.OnItemClickListener;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.LogUtils;
import com.marksixinfo.utils.NumberUtils;
import com.marksixinfo.utils.ReleaseUtils;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.ConcernTextView;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.MyClassicsFooter;
import com.marksixinfo.widgets.MySmartRefreshLayout;
import com.marksixinfo.widgets.backedittext.BackInputDialog;
import com.marksixinfo.widgets.glide.GlideUtil;
import com.marksixinfo.widgets.richweb.RichWebView;
import com.marksixinfo.widgets.richweb.RichWebViewUtil;
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
import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * 功能描述: 帖子详情
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/3/31 0031 18:00
 */
public class DetailActivity extends MarkSixActivity implements
        SucceedCallBackListener, BackInputDialog.InputTextListener, IDetailCommentInterface, View.OnClickListener {


    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.tv_past_message_count)
    TextView tvPastMessageCount;
    @BindView(R.id.list_view)
    ListView listView;
    @BindView(R.id.iv_collect)
    SparkButton ivCollect;
    @BindView(R.id.iv_praise)
    SparkButton ivPraise;
    @BindView(R.id.ll_bottom_content)
    LinearLayout llBottomContent;


    private ImageView ivUserPhoto;
    private TextView tvUserName;
    private ImageView ivOfficial;
    private TextView tvUserDomain;
    private TextView tvTime;
    private ConcernTextView tvConcern;
    private TextView tvPeriod;
    private RichWebView webView;
    private TextView tvUpMsg;
    private TextView tvPastBottom;
    private TextView tvNextMsg;
    private RichWebView webViewNext;
    private LinearLayout mLlNextContent;
    private View viewWebFootLine;
//    private View viewFootLine;


    private DetailCommentAdapter adapter;
    private String id = "";//帖子id
    private int pageIndex = 0;//页数
    private List<DetailCommentData> list = new ArrayList<>();
    private boolean isShowNext = false;//是否显示下一期
    private UMShareDialog umShareDialog;
    private ShareParams shareParams;
    private String initNext = "";//初始加载的下一期id
    private String next = "";//下期id
    private String prev = "";//上期id
    private ReleaseUtils releaseUtils;
    private String user_id;
    private DetailData data = new DetailData();
    private BackInputDialog backInputDialog;
    private int currentY;
    private int maxY;
    private boolean isScrollComment;
    private boolean isInComment;
    private RichWebViewUtil richWebViewUtil;
    private List<String> pic;//当前图片集合
    private List<String> picNext;//下期图片集合
    private boolean isNeedNext;
    private int scrollY;
    private int reply_num;
    private int fav_status;
    private int like_status;
    private long lastClickTime;
    private boolean isReadArticle;//好友阅读文章记录
    private int look_status;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        richWebViewUtil.setWebDestroy(webView);
        richWebViewUtil.setWebDestroy(webViewNext);
        richWebViewUtil = null;
    }

    @Override
    public int getViewId() {
        return R.layout.activity_detail;
    }


    @Override
    public void afterViews() {

        initParamData();

        markSixTitle.init("", "");

        richWebViewUtil = new RichWebViewUtil(this);

        /**********headView*********/
        View headView = View.inflate(this, R.layout.head_view_detail, null);
        initHeadView(headView);
        richWebViewUtil.setWebSetting(webView, this);
        richWebViewUtil.setWebSetting(webViewNext, this);
        webView.loadUrl("");
        webViewNext.loadUrl("");


        adapter = new DetailCommentAdapter(this, list, this);
        listView.addHeaderView(headView);
        mLoadingLayout.setRetryListener(this);
        listView.setAdapter(adapter);

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


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                int firstVisiblePosition = listView.getFirstVisiblePosition();
                int lastVisiblePosition = listView.getLastVisiblePosition();
                if (firstVisiblePosition == 0) {
                    scrollY = getScrollY();
                    if (scrollY >= maxY && maxY != 0) {
                        scrollY = 0;
                        isInComment = true;
                    } else {
                        isInComment = false;
                    }
                } else {
                    isInComment = true;
                }
                /**
                 * 触发好友阅读文章接口,最后可见位置为评论条目
                 * 1.不足一页,延时5秒请求
                 * 2.超过一页,滑动到底部请求
                 *
                 */
                if (lastVisiblePosition == 1 && !isReadArticle) {
                    isReadArticle = true;
                    if (scrollY == 0) {//不足一页,延时5秒请求
                        markSixTitle.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                taskReadArticleRecords();
                            }
                        }, NumberConstants.TASK_READ_ARTICLE_TIME);
                    } else {
                        taskReadArticleRecords();
                    }
                }
                LogUtils.d("onScroll_____" + scrollY + "___"
                        + firstVisiblePosition + "_____" + lastVisiblePosition);
            }
        });


        richWebViewUtil.addDetailJavaScript(webView, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (CommonUtils.ListNotNull(pic)) {
                    CommonUtils.checkImagePreview(getContext(), pic, position);
                }
            }
        });
        richWebViewUtil.addDetailJavaScript(webViewNext, new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (CommonUtils.ListNotNull(picNext)) {
                    CommonUtils.checkImagePreview(getContext(), picNext, position);
                }
            }
        });
        getDetailData();
    }


    /**
     * webview加载完成
     */
    private void setWebLoadFinish() {
        if (isNeedNext && mLlNextContent.getVisibility() != View.VISIBLE) {
            mLlNextContent.setVisibility(View.VISIBLE);
        }
        mLoadingLayout.showContent();
        if (listView.getVisibility() != View.VISIBLE) {
            listView.setVisibility(View.VISIBLE);
        }
        if (llBottomContent.getVisibility() != View.VISIBLE) {
            llBottomContent.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 获取上滑的距离
     *
     * @return distance
     */
    public int getScrollY() {
        View c = listView.getChildAt(0);
        if (null == c) {
            return 0;
        }
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        int top = c.getTop();
        return firstVisiblePosition * c.getHeight() + Math.abs(top);
    }

    /**
     * 头部View
     *
     * @param view
     */
    private void initHeadView(View view) {
        view.findViewById(R.id.ll_user_content).setOnClickListener(this);
        ivUserPhoto = view.findViewById(R.id.iv_user_photo);
        tvUserName = view.findViewById(R.id.tv_user_name);
        tvPeriod = view.findViewById(R.id.tv_period_number);
        ivOfficial = view.findViewById(R.id.iv_official);
        tvUserDomain = view.findViewById(R.id.tv_user_domain);
        tvTime = view.findViewById(R.id.tv_time);
        tvConcern = view.findViewById(R.id.tv_concern);
        tvConcern.setOnClickListener(this);
        webView = view.findViewById(R.id.web_view);
        mLlNextContent = view.findViewById(R.id.ll_next_content);
        tvUpMsg = view.findViewById(R.id.tv_up_msg);
        tvUpMsg.setOnClickListener(this);
        tvPastBottom = view.findViewById(R.id.tv_past_bottom);
        tvPastBottom.setOnClickListener(this);
        tvNextMsg = view.findViewById(R.id.tv_next_msg);
        tvNextMsg.setOnClickListener(this);
        webViewNext = view.findViewById(R.id.web_view_next);
        viewWebFootLine = view.findViewById(R.id.view_web_foot_line);
//        viewFootLine = view.findViewById(R.id.view_foot_line);
    }


    /**
     * 初始参数
     */
    private void initParamData() {
        id = getStringParam(StringConstants.ID);
        isScrollComment = Boolean.parseBoolean(getStringParam(StringConstants.DETAIL_SCROLL_COMMENT));
    }


    /**
     * 获取评论列表
     */
    private void getCommentList() {
        new HeadlineImpl(new MarkSixNetCallBack<DetailCommentListData>(this, DetailCommentListData.class) {
            @Override
            public void onSuccess(DetailCommentListData response, int id) {
                loadDone();
                setData(response);
            }

            @Override
            public void onError(String msg, String code) {
                mLoadingLayout.showError();
                loadDone();
            }
        }.setNeedDialog(false)).getDetailCommentList(id, String.valueOf(++pageIndex));
    }

    private void loadDone() {
        if (refreshLayout != null) {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadMore();
            refreshLayout.resetNoMoreData();
        }
    }

    /**
     * 设置数据
     *
     * @param response
     */
    private void setData(DetailCommentListData response) {
        if (response != null && CommonUtils.ListNotNull(response.getList())) {
            list.addAll(response.getList());
            if (response.getList().size() < response.getSize()) {
                handleEmpty();
            }
        } else {
            handleEmpty();
        }
        adapter.notifyUI(list);
        if (isScrollComment) {
            listView.setSelection(1);
            isScrollComment = false;
            isInComment = true;
        }
    }

    /**
     * 处理空太页
     */
    private void handleEmpty() {
        try {
            MyClassicsFooter footer = (MyClassicsFooter) refreshLayout.getRefreshFooter();
            footer.setTextTextNothing("");

            handleList(CommonUtils.ListNotNull(list) && list.get(0).getType() != 2);

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
        DetailCommentData detailCommentData = new DetailCommentData();
        detailCommentData.setType(2);
        detailCommentData.setFootViewText(b ? getString(R.string.load_all_comment) : getString(R.string.not_comment));
        list.add(detailCommentData);
    }


    /**
     * 获取详情数据
     */
    private void getDetailData() {
        pageIndex = 0;
        list.clear();
        adapter.notifyUI(list);
        new HeadlineImpl(new MarkSixNetCallBack<DetailData>(this, DetailData.class) {
            @Override
            public void onSuccess(DetailData response, int id) {
                if (listView.getVisibility() == View.VISIBLE) {
                    getCommentList();
                }
                data = response;
                refreshEventData();
            }

            @Override
            public void onError(String msg, String code) {
                mLoadingLayout.showError();
                //todo ErrorCode 文章不存在
                if (CommonUtils.StringNotNull(msg) && msg.contains("文章不存在")) {
                    super.onError(msg, code);
                    if (ivPraise != null) {
                        ivPraise.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 500);
                    }
                }
            }
        }.setNeedDialog(false)).getDetail(id);
    }


    /**
     * 获取下一期
     *
     * @param isNext
     */
    private void getDetailNextData(boolean isNext) {
        //显示webview里面的期数
        webViewNext.loadUrl(UrlStaticConstants.DETAIL_WEB_VIEW + (isNext ? next : prev) + StringConstants.GONE_PERIOD + "1");
        new HeadlineImpl(new MarkSixNetCallBack<DetailNextData>(this, DetailNextData.class) {
            @Override
            public void onSuccess(DetailNextData response, int id) {
                if (response != null) {
                    setNextData(response);
                }
            }
        }).getDetailNext(isNext ? next : prev);
    }


    /**
     * 设置下一期
     *
     * @param response
     */
    private void setNextData(DetailNextData response) {

        picNext = response.getPic();
        next = response.getNext();
        prev = response.getPrev();

        viewWebFootLine.setVisibility(isShowNext ? View.VISIBLE : View.GONE);

        if (CommonUtils.StringNotNull(next)
                && !"0".equals(next)
                && NumberUtils.stringToInt(next) < NumberUtils.stringToInt(this.id)) {
            setNextMsgBottomStatus(true);
        } else {
            setNextMsgBottomStatus(false);
        }

        if (CommonUtils.StringNotNull(prev) && !"0".equals(prev)) {
            setTvUpMsgStatus(true);
        } else {
            setTvUpMsgStatus(false);
        }
        setTvUpMsg(true);
        setNextMsgBottom(true);
    }


    /**
     * 设置帖子明细
     */
    private void setDetail() {

        if (data != null) {

            String id = data.getId();
            String title = data.getTitle();
            String add_time = data.getAdd_Time();
            String face = data.getFace();
            initNext = data.getNext();
            String Nickname = data.getNickname();
            user_id = data.getUser_Id();
            look_status = data.getLook_status();
            String remark = data.getRemark();
            int period = data.getPeriod();
            int level = data.getLevel();
            reply_num = data.getReply_num();
            fav_status = data.getFav_status();
            like_status = data.getLike_status();
            pic = data.getPic();

            //设置关注
            tvConcern.setLookStatus(look_status);
            //隐藏webview里面的期数
            String url = UrlStaticConstants.DETAIL_WEB_VIEW + id + StringConstants.GONE_PERIOD + "0";
            if (!url.equalsIgnoreCase(webView.getUrl())) {
                webView.loadUrl(url);
            }

            if (CommonUtils.StringNotNull(initNext) && !"0".equals(initNext)) {
                tvPastBottom.setClickable(true);
                setPastBottom(true);
            } else {
                tvPastBottom.setClickable(false);
                setPastBottom(false);
            }

            tvTime.setText(CommonUtils.StringNotNull(add_time) ? add_time : "");

            GlideUtil.loadCircleImage(face, ivUserPhoto);
            ivUserPhoto.setVisibility(View.VISIBLE);

            tvUserName.setText(CommonUtils.StringNotNull(Nickname) ? CommonUtils.CommHandleText2(Nickname) : "");
            markSixTitle.init("", CommonUtils.StringNotNull(Nickname) ? Nickname : ""
                    , "", R.drawable.icon_share, this);

            //官方
            CommonUtils.setUserLevel(ivOfficial, level);

            //网址
            tvUserDomain.setText(CommonUtils.StringNotNull(remark) ? CommonUtils.CommHandleText(remark) : "");
            tvUserDomain.setVisibility(CommonUtils.StringNotNull(remark) ? View.VISIBLE : View.GONE);

            //期数
            if (period > 0) {
                tvPeriod.setVisibility(View.VISIBLE);
                tvPeriod.setText(CommonUtils.fixThree(String.valueOf(period)) + "期");
            } else {
                tvPeriod.setVisibility(View.GONE);
                tvPeriod.setText("");
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
        }
    }

    /**
     * 设置是否有下一期
     *
     * @param isClickable
     */
    private void setPastBottom(boolean isClickable) {
        if (isClickable) {
            isNeedNext = true;
            tvPastBottom.setTextColor(0xff333333);
            setPastBottomStatus(true);
        } else {
            isNeedNext = false;
        }
    }

    /**
     * 设置下期开启及收起
     *
     * @param isStatus
     */
    private void setPastBottomStatus(boolean isStatus) {
        Drawable drawable;
        if (isStatus) {
            drawable = getResources().getDrawable(R.drawable.icon_pull_down_black);
        } else {
            drawable = getResources().getDrawable(R.drawable.icon_pull_up_black);
        }
        tvPastBottom.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        tvPastBottom.setCompoundDrawablePadding(UIUtils.dip2px(this, 5));
    }

    /**
     * 下期按钮显示
     *
     * @param isClickable
     */
    private void setNextMsgBottom(boolean isClickable) {
        if (isClickable) {
            tvNextMsg.setVisibility(View.VISIBLE);
        } else {
            tvNextMsg.setVisibility(View.GONE);
        }
    }

    /**
     * 下期按钮颜色
     *
     * @param isClickable
     */
    private void setNextMsgBottomStatus(boolean isClickable) {
        if (isClickable) {
            tvNextMsg.setClickable(true);
            tvNextMsg.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            tvNextMsg.setClickable(false);
            tvNextMsg.setTextColor(getResources().getColor(R.color.grey_ddd));
        }
    }

    /**
     * 上期按钮显示
     *
     * @param isClickable
     */
    private void setTvUpMsg(boolean isClickable) {
        if (isClickable) {
            tvUpMsg.setVisibility(View.VISIBLE);
        } else {
            tvUpMsg.setVisibility(View.GONE);
        }
    }

    /**
     * 上期按钮颜色
     *
     * @param isClickable
     */
    private void setTvUpMsgStatus(boolean isClickable) {
        if (isClickable) {
            tvUpMsg.setClickable(true);
            tvUpMsg.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else {
            tvUpMsg.setClickable(false);
            tvUpMsg.setTextColor(getResources().getColor(R.color.grey_ddd));
        }
    }

    /**
     * webview onPageFinished 回调
     *
     * @param o
     */
    @Override
    public void succeedCallBack(@Nullable Object o) {
        setWebLoadFinish();
        getCommentList();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_user_content://用户中心
                checkUser(user_id);
                break;
            case R.id.tv_concern:
                //关注 0:未关注;1:已关注 2:自己
                if (tvConcern.getLook_status() != -1) {
                    if (tvConcern.startLookStatus()) {
                        concern();
                    }
                }
                break;
            case R.id.tv_up_msg://上一期
                getDetailNextData(false);
                break;
            case R.id.tv_past_bottom://打开/收起往期消息
                isShowNext = !isShowNext;
                webViewNext.setVisibility(isShowNext ? View.VISIBLE : View.GONE);
                setPastBottomStatus(!isShowNext);
                setTvUpMsg(isShowNext);
                setNextMsgBottom(isShowNext);
                if (isShowNext) {
                    next = initNext;
                    getDetailNextData(true);
                }
                break;
            case R.id.tv_next_msg://下一期
                getDetailNextData(true);
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


    @OnClick({R.id.rl_write_comment, R.id.rl_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_write_comment://回复
                commentDetail();
                break;
            case R.id.rl_comment://锚点跳转
                if (reply_num > 0) {//有评论锚点
                    isInComment = !isInComment;
                    if (isInComment) {
                        listView.setSelection(1);
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
     * 点赞帖子
     */
    private void praise() {
        if (data != null) {
            if (data.getLoadingStatus() == 0 && isLogin()) {
                new HeadlineImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
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
                }.setNeedDialog(false)).praise(id);
            }

            data.setLoadingStatus(1);
            like_status = like_status == 1 ? 0 : 1;
            ivPraise.setChecked(like_status == 1);
            data.setLike_status(like_status);
            setDetail();
        }
    }

    /**
     * 点赞评论
     */
    private void praiseComment(DetailCommentData detailCommentData, String id) {
        if (detailCommentData.getLoadingStatus() == 0 && isLogin()) {
            new HeadlineImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                @Override
                public void onSuccess(LikeAndFavoriteData response, int id) {
                    if (response != null) {
                        detailCommentData.setLike_status(response.getType());
                        detailCommentData.setLike_Num(response.getNum());
                        adapter.notifyUI(list);
                        refreshEventData();
                    }
                }

                @Override
                public void onAfter(int id) {
                    super.onAfter(id);
                    detailCommentData.setLoadingStatus(0);
                }
            }.setNeedDialog(false)).praiseComment(id);
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
                new HeadlineImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
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
                }.setNeedDialog(false)).favorites(id);
            }

            data.setLoadingStatus(2);
            fav_status = fav_status == 1 ? 0 : 1;
            ivCollect.setChecked(fav_status == 1);
            data.setFav_status(fav_status);
//            setDetail();
        }
    }


    /**
     * 关注
     */
    private void concern() {
        data.setLook_status(-1);//接口中
        new HeadlineImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
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
                if (tvConcern != null) {
                    tvConcern.setLookStatus(look_status);
                }
            }
        }.setNeedDialog(false)).concernUser(user_id);

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
        new HeadlineImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
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
     * 回复用户
     *
     * @param id
     * @param content
     */
    private void commentUser(String id, String content) {
        new HeadlineImpl(new MarkSixNetCallBack<Integer>(this, Integer.class) {
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
     * 处理回复本地显示,跳转到第一条
     *
     * @param id
     * @param content
     */
    private void handlerCommentList(String id, String content) {
        DetailCommentData detailComment = CommonUtils.getDetailComment(this, id, content);
        if (detailComment != null) {
            list.add(0, detailComment);
            if (((MySmartRefreshLayout) refreshLayout).isFooterNoMoreData()) {
                handleList(true);
            }
            adapter.notifyUI(list);
            listView.setSelection(1);
        }
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
                CommentsListDetail data = new CommentsListDetail();
                data.setId(detailCommentData.getId());
                data.setReply_Num(response);
                data.setOnlyReply_Num(true);
                EventBusUtil.post(new CommentListEvent(data));
            }
        }
    }


    /**
     * 查看用户
     *
     * @param id
     */
    @Override
    public void checkUser(String id) {
        goToUserCenterActivity(id, 0);
    }

    /**
     * 点赞
     *
     * @param position
     * @param isPraise 是否点赞
     */
    @Override
    public void praise(int position, boolean isPraise) {
        if (CommonUtils.ListNotNull(list)) {
            DetailCommentData detailCommentData = list.get(position);
            if (detailCommentData != null) {
                String id = detailCommentData.getId();
                praiseComment(detailCommentData, id);
            }
        }
    }

    /**
     * 回复用户
     *
     * @param position
     * @param hasComment 是否有回复
     */
    @Override
    public void commentUser(int position, boolean hasComment) {
        if (CommonUtils.ListNotNull(list)) {
            DetailCommentData detailCommentData = list.get(position);
            if (detailCommentData != null) {
                String mId = detailCommentData.getId();
                if (hasComment) {
                    goToCommentListActivity(mId, 0);
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
    }


    /**
     * 评论列表操作更新详情页
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommentListEvent event) {
        if (event != null) {//回复帖子列表页,刷新详情回复数量
            if (event.getType()==0) {
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
            setDetail();
        }
    }

    /**
     * 接口刷新实体
     */
    private void refreshEventData() {
        EventBusUtil.post(new CommListDataEvent(CommonUtils.getMainHomeData(data)));
    }


    /**
     * 好友阅读文章记录
     */
    private void taskReadArticleRecords() {
        new HeadlineImpl(new MarkSixNetCallBack<Object>(this, Object.class) {
            @Override
            public void onSuccess(Object response, int id) {

            }
        }.setNeedDialog(false).setNeedToast(false)).taskReadArticleRecords(id);
    }
}
