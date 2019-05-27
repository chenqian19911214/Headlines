package com.marksixinfo.ui.activity;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.PictureDetailsListAdapter;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.CommentsListDetail;
import com.marksixinfo.bean.GalleryExplainTopData;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.bean.PictreCommentData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.CommentListEvent;
import com.marksixinfo.interfaces.IDetailCommentInterface;
import com.marksixinfo.interfaces.NoDoubleClickListener;
import com.marksixinfo.net.impl.GalleryImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.GoodView;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.MyClassicsFooter;
import com.marksixinfo.widgets.ScaleImageView;
import com.marksixinfo.widgets.backedittext.BackInputDialog;
import com.marksixinfo.widgets.glide.GlideUtil;
import com.marksixinfo.widgets.sparkbutton.SparkButton;
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
 * 图解详情页
 *
 * @Auther: Administrator
 * @Date: 2019/5/8 0008 12:34
 * @Description:
 */
public class GalleryExplainDetailActivity extends MarkSixActivity implements BackInputDialog.InputTextListener, IDetailCommentInterface,View.OnClickListener{

    /**
     * 回复的 dialog
     */
    private BackInputDialog backInputDialog;
    private GoodView goodView;

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.list_view)
    ListView listView;

    @BindView(R.id.iv_collect)
    SparkButton ivCollect;
    @BindView(R.id.iv_praise)
    SparkButton ivPraise;

    /**
     * 消息数量
     */
    @BindView(R.id.tv_past_message_count)
    TextView tv_past_message_count;
    private PictureDetailsListAdapter simpleAdapter;
    private List<PictreCommentData> list = new ArrayList();
    private int commentListPosition;
    private List<PictreCommentData> responseComment = new ArrayList<>();

    /**
     * 当前图库id
     */
    private String ID, imageUri;

    private View topListView;
    private int pageIndex = 0;//页数

    @Override
    public int getViewId() {
        return R.layout.activity_gallery_explain_detail;
    }

    @Override
    public void afterViews() {
        //获取到图解id
        markSixTitle.init("图解详情", "", "", 0, this);

        ID = getStringParam(StringConstants.ID);
        imageUri = getStringParam(StringConstants.IMAGE_URL);
        if (!TextUtils.isEmpty(ID)) {
            getPictureDetails(ID);
        }
        simpleAdapter = new PictureDetailsListAdapter(getContext(), this, list, this);
        listView.setAdapter(simpleAdapter);
        mLoadingLayout.setRetryListener(new View.OnClickListener() {
            //点击重试
            @Override
            public void onClick(View view) {
                mLoadingLayout.showLoading();
                getPictureDetails(ID);
            }
        });
        /**
         * 下拉刷新
         * */
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                //  getDetailData();
                getPictureDetails(ID);
            }
        });
        //上拉加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                //   getCommentList();
                getPictureReple(false);
            }
        });

        ivPraise.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {

                if (response.getLike().getStatus()==0){
                    GoodView goodView = new GoodView(getContext());
                    ivPraise.playAnimation();
                    goodView.setTextColor(getContext().getResources().getColor(R.color.colorPrimary)).setText("+1").show(viewPraise);
                    ivPraise.setChecked(true);
                }
                praises(response, response.getLike().getStatus() == 1);
               // praises(response, response.getLike().getStatus() == 1);

            }
        });
    }

    /**
     * 评论的评论页面跟新
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommentListEvent event) {
        if (event != null) {//回复帖子列表页,刷新详情回复数量
            if (event.getType() == 3) {
                CommentsListDetail detail = event.getDetail();
                if (detail != null && CommonUtils.ListNotNull(list)) {
                    commListDataChange(detail, list);
                }
            }
        }
    }

    private void commListDataChange(CommentsListDetail detail, List<PictreCommentData> list) {
        PictreCommentData pictreCommentData = list.get(commentListPosition);
        pictreCommentData.setReply_Num(detail.getReply_Num() + "");
        pictreCommentData.getLike().setStatus(detail.getLike_status());
        pictreCommentData.getLike().setNum(detail.getLike_Num() + "");
        list.set(commentListPosition, pictreCommentData);
        simpleAdapter.notifyUI(list);
    }

    /**
     * 获取图解详情
     *
     * @param id
     */
    private void getPictureDetails(String id) {
        new GalleryImpl(new MarkSixNetCallBack<GalleryExplainTopData>(this, GalleryExplainTopData.class) {
            @Override
            public void onSuccess(GalleryExplainTopData response, int id) {
                if (response != null) {
                    //详情 数据
                    if (!isAddListView)
                        addListViewHead(response);
                    else
                        setLikeAndCollect(response, false, false);

                    getPictureReple(true);
                }
            }

            @Override
            public void onError(String msg, String code) {
                super.onError(msg, code);
                mLoadingLayout.showEmpty();
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                //loadDone(false);
                mLoadingLayout.showContent();
            }
        }.setNeedDialog(false)).getAnswerData(id);
    }


    /**
     * 获取图解详情 评论
     */
    private void getPictureReple(boolean isRefresh) {
        if (isRefresh) {
            pageIndex = 0;
        }
        pageIndex++;
        new GalleryImpl(new MarkSixNetCallBack<List<PictreCommentData>>(this, PictreCommentData.class) {
            @Override
            public void onSuccess(List<PictreCommentData> response, int id) {
                if (response != null) {
                    if (isRefresh) {
                        list.clear();
                    }
                    mLoadingLayout.showContent();
                    setFootView(response, false);
                }
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                loadDone();
            }
        }.setNeedDialog(false)).getAnswerComment(ID, pageIndex);
    }

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

    private GalleryExplainTopData response;

    private TextView PraiseTv;
    private TextView collerTv;
    private View viewPraise;
    private String titleName;

    /**
     * 添加图库详情 消息
     */
    private void addListViewHead(GalleryExplainTopData response) {
        this.response = response;
        isAddListView = true;
        goodView = new GoodView(getContext());
        topListView = LayoutInflater.from(getContext()).inflate(R.layout.head_view_graphic, null, true);
        listView.addHeaderView(topListView);
        TextView titleTv = topListView.findViewById(R.id.picture_title); //
        TextView picture_comment_tv = topListView.findViewById(R.id.picture_comment_tv); //comment
        TextView addTimeTv = topListView.findViewById(R.id.tv_time); //addTime
        TextView tv_user_name = topListView.findViewById(R.id.tv_user_name); //userName
        ImageView iv_user_photo = topListView.findViewById(R.id.iv_user_photo); // userPhoto
        ScaleImageView viewPictrueIv = topListView.findViewById(R.id.picture_image); //da
        PraiseTv = topListView.findViewById(R.id.tv_praise);
        collerTv = topListView.findViewById(R.id.tv_collect);
        viewPraise = topListView.findViewById(R.id.view_praise);

        tv_user_name.setText(response.getNickname());
        picture_comment_tv.setText(response.getContent());
        this.titleName = response.getTitle();
        titleTv.setText(this.titleName);
        addTimeTv.setText(response.getAdd_Time());
        GlideUtil.loadCircleImage(response.getFace(), iv_user_photo);
        //imageUri = response.getFace();
        if (!TextUtils.isEmpty(imageUri)) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    setAutoScale(viewPictrueIv, imageUri);
                    // setAutoScale(viewPictrueIv, CommonUtils.getGalleryUrl(imageUri));
                }
            }).start();
        }

        viewPictrueIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(imageUri))
                    CommonUtils.checkImagePreview(getContext(), CommonUtils.getGalleryUrls(imageUri), 0);
            }
        });

        PraiseTv.setOnClickListener(new NoDoubleClickListener() { //点赞

            @Override
            protected void onNoDoubleClick(View v) {

               // praises(response, response.getLike().getStatus() == 1);

                if (response.getLike().getStatus()==0){
                    GoodView goodView = new GoodView(getContext());
                    ivPraise.playAnimation();
                    goodView.setTextColor(getContext().getResources().getColor(R.color.colorPrimary)).setText("+1").show(viewPraise);
                    ivPraise.setChecked(true);
                }
                praises(response, response.getLike().getStatus() == 1);
            }
        });

        collerTv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) { //踩一下
                collect(response, response.getTread().getStatus() == 1);
            }
        });
        setLikeAndCollect(response, false, false);
        //EventBusUtil.post(new SendGalleryTitle(this.titleName));
    }


    /**
     * 设置下面的view
     * 填充评论数据
     * <p>
     * List<PictreCommentData> response
     */
    private void setFootView(List<PictreCommentData> response, boolean isTopAdd) {
        if (CommonUtils.ListNotNull(response)) {
            if (isTopAdd) {
                list.addAll(0, response);
                handleEmpty(true);
            } else {
                list.addAll(response);
            }
        }
        if (CommonUtils.ListNotNull(list)) { //没有数据
            if (!CommonUtils.ListNotNull(response)) {
                handleEmpty(true);
            }
        } else {
            handleEmpty(false);
        }
        simpleAdapter.notifyUI(list);
        if (isTopAdd) {
            listView.setSelection(1);
        }
    }


    /**
     * @param isText true 加载全部
     *               false 没有数据
     */
    private void handleEmpty(boolean isText) {
        try {
            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
            MyClassicsFooter footer = (MyClassicsFooter) refreshLayout.getRefreshFooter();
            footer.setTextTextNothing("");
            handleList(isText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleList(boolean b) {

        if (CommonUtils.ListNotNull(list)) {
            if (list.get(list.size() - 1).getType() == 2) {
                list.remove(list.size() - 1);
            }
        }
        PictreCommentData detailCommentData = new PictreCommentData();
        detailCommentData.setType(2);
        detailCommentData.setFootViewText(b ? getString(R.string.load_all_comment) : getString(R.string.not_comment));
        list.add(detailCommentData);
    }

    /**
     * 点赞接口
     *
     * @param mainHomeData
     * @param isPraise
     */
    private void praises(GalleryExplainTopData mainHomeData, boolean isPraise) {
        if (mainHomeData.getLoadingStatus() == 0 && isLogin()) {
            mainHomeData.setLoadingStatus(1);

            new GalleryImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                @Override
                public void onSuccess(LikeAndFavoriteData response, int id) {
                    if (response != null) {
                        mainHomeData.getLike().setNum(response.getNum() + "");
                        mainHomeData.getLike().setStatus(response.getType());
                        // setLikeAndCollect(mainHomeData, true, false);
                    }
                }

                @Override
                public void onAfter(int id) {
                    super.onAfter(id);
                    mainHomeData.setLoadingStatus(0);
                    setLikeAndCollect(mainHomeData, true, false);

                }
            }.setNeedDialog(false)).getClickLike(mainHomeData.getId(), "", 2 + "");
        }
        mainHomeData.getLike().setStatus(!isPraise ? 1 : 0);
        int like_num = Integer.valueOf(mainHomeData.getLike().getNum());
        mainHomeData.getLike().setNum(!isPraise ? ++like_num + "" : --like_num + "");
        setLikeAndCollect(mainHomeData, true, false);

    }


    /**
     * 设置点赞数量和踩的数量
     */
    private void setLikeAndCollect(GalleryExplainTopData response, boolean isLikeAnimation, boolean isFavoritesAnimation) {
        this.response = response;
        //赞
        GalleryExplainTopData.LikeBean likeBean = response.getLike();
        if (likeBean != null) {
            String like_nums = likeBean.getNum();
            if (!TextUtils.isEmpty(like_nums)) {
                int like_num = Integer.valueOf(like_nums);
                PraiseTv.setText(like_num > 0 ? CommonUtils.getThousandNumber(like_num) : "");
            }
            if (response.getLike().getStatus() == 1 && isLikeAnimation) {
                GoodView goodView = new GoodView(getContext());
                ivPraise.playAnimation();
                goodView.setTextColor(getContext().getResources().getColor(R.color.colorPrimary)).setText("+1").show(viewPraise);
            }
            CommonUtils.setPraiseStatus(getContext(), PraiseTv, likeBean.getStatus());
            ivPraise.setChecked(likeBean.getStatus() == 1);
        }

        //踩
        GalleryExplainTopData.TreadBean treadBean = response.getTread();
        if (treadBean != null) {
            String tread_nums = treadBean.getNum();
            if (!TextUtils.isEmpty(tread_nums)) {
                int ltread_num = Integer.valueOf(tread_nums);
                collerTv.setText(ltread_num > 0 ? CommonUtils.getThousandNumber(ltread_num) : "");
            }
            CommonUtils.setTrampleStatus(getContext(), collerTv, treadBean.getStatus());
        }
        //收藏
     /*   GalleryExplainTopData.FavoritesBean favoritesBean = response.getFavorites();
        if (favoritesBean != null) {
            if (favoritesBean.getStatus() == 1 && isFavoritesAnimation) {
                ivCollect.playAnimation();
            }
            ivCollect.setChecked(favoritesBean.getStatus() == 1);
        }*/
        //评论数
        int viewNum = Integer.valueOf(response.getReply_Num());
        if (viewNum > 0) {
            if (tv_past_message_count != null) {
                tv_past_message_count.setVisibility(View.VISIBLE);
            }
            tv_past_message_count.setText(response.getReply_Num() + "");
        } else {
            tv_past_message_count.setVisibility(View.GONE);

        }
    }

    /**
     * 踩接口
     *
     * @param mainHomeData
     * @param isCollect
     */
    private void collect(GalleryExplainTopData mainHomeData, boolean isCollect) {
        if (mainHomeData != null) {
            if (mainHomeData.getLoadingStatus() == 0 && isLoginToast()) {
                mainHomeData.setLoadingStatus(2);
                int status = mainHomeData.getTread().getStatus();
                int favorites_num = Integer.valueOf(mainHomeData.getTread().getNum());
                new GalleryImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                    @Override
                    public void onSuccess(LikeAndFavoriteData response, int id) {
                        if (response != null) {
                            mainHomeData.getTread().setStatus(response.getType());
                            mainHomeData.getTread().setNum(response.getNum() + "");
                            // setLikeAndCollect(mainHomeData, false, false);
                        }
                    }

                    @Override
                    public void onError(String msg, String code) {
                        super.onError(msg, code);
                        mainHomeData.getTread().setStatus(status);
                        mainHomeData.getTread().setNum(favorites_num + "");

                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        mainHomeData.setLoadingStatus(0);
                        setLikeAndCollect(mainHomeData, false, false);

                    }
                }.setNeedDialog(false)).postClickTread(mainHomeData.getId(), "", 2 + "");

                mainHomeData.getTread().setStatus(!isCollect ? 1 : 0);
                int like_num = Integer.valueOf(mainHomeData.getTread().getNum());
                mainHomeData.getTread().setNum(!isCollect ? ++like_num + "" : --like_num + "");
                //   mainHomeData.setLoadingStatus(1);
                setLikeAndCollect(mainHomeData, false, false);

            }

         /*   mainHomeData.getTread().setStatus(!isCollect ? 1 : 0);
            int like_num = Integer.valueOf(mainHomeData.getTread().getNum());
            mainHomeData.getTread().setNum(!isCollect ? ++like_num + "" : --like_num + "");
            mainHomeData.setLoadingStatus(1);*/
        }
    }


    /**
     * 回复帖子
     *
     * @param position -1 为评论图库 0 以上为评论评论
     */
    private void commentDetail(int position) {
        if (isLoginToast()) {
            String backDialogHint = "";
            if (backInputDialog == null) {
                backInputDialog = new BackInputDialog(getContext(), this);
            }
            if (position == -1) {
                backDialogHint = backInputDialog.hint;
            } else {
                backDialogHint = list.get(position).getNickname();
            }
            backInputDialog.setCommentPosition(position).setId("").show(backDialogHint);
        }
    }

    private boolean isAddListView = false;

    @Override
    public void onClick(View view) {

    }

    @Override
    public void checkUser(String id) {

    }

    @Override
    public void praise(int position, boolean isPraise) {
        commounPraise(position, isPraise);
    }

    /**
     * 设置自适应缩放
     *
     * @param mIvHeadImage
     */
    private void setAutoScale(ImageView mIvHeadImage, String uri) {
        Bitmap mBitmap = GlideUtil.load(getContext(), uri);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int maxWidth = UIUtils.getScreenWidth(getContext()) - UIUtils.dip2px(getContext(), 30);
                float imageHeight = 0;
                int width = mBitmap.getWidth();
                int height = mBitmap.getHeight();
                if (width != 0 && mBitmap != null) {
                    float scale = maxWidth * 1.0f / width * 1.0f;
                    imageHeight = height * 1.0f * scale;
                    ViewGroup.LayoutParams lp = mIvHeadImage.getLayoutParams();
                    lp.width = maxWidth;
                    lp.height = (int) imageHeight;
                    mIvHeadImage.setLayoutParams(lp);
                    mIvHeadImage.setImageBitmap(mBitmap);
                }
            }
        });
    }

    @Override
    public void commentUser(int position, boolean hasComment) {
        if (!isLoginToast()) return;
        commentListPosition = position;
        if (hasComment) {
            goToCommentListActivity(list.get(position).getId(), 3);
        } else {
            commentDetail(position);
        }
    }

    @Override
    public void onInputText(String text) {
        if (backInputDialog != null) {
            int position = backInputDialog.getCommentPosition();
            if (position < 0) { //图解回复 galleryID,period
                comment(text, "", ID, "2");
            } else { //回复的回复
                comment(text, "", list.get(position).getId(), "4");
            }
        }
    }


    /**
     * 评论点赞
     *
     * @param position
     * @param isPraise
     */
    private void commounPraise(int position, boolean isPraise) {
        if (!CommonUtils.ListNotNull(list))
            return;
        PictreCommentData mainHomeData = list.get(position);
        if (mainHomeData.getLoadingStatus() == 0 && isLogin()) {
            new GalleryImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                @Override
                public void onSuccess(LikeAndFavoriteData response, int id) {
                    if (response != null) {
                        mainHomeData.getLike().setNum(response.getNum() + "");
                        mainHomeData.getLike().setStatus(response.getType());
                        // setLikeAndCollect(mainHomeData,true);
                        simpleAdapter.notifyUI(list);
                    }
                }

                @Override
                public void onAfter(int id) {
                    super.onAfter(id);
                    mainHomeData.setLoadingStatus(0);
                }
            }.setNeedDialog(false)).getClickLike(mainHomeData.getId(), "", 5 + "");
        }
        mainHomeData.getLike().setStatus(isPraise ? 1 : 0);
        int like_num = Integer.valueOf(mainHomeData.getLike().getNum());
        mainHomeData.getLike().setNum(isPraise ? ++like_num + "" : --like_num + "");
        mainHomeData.setLoadingStatus(1);
        simpleAdapter.notifyUI(list);
    }

    /**
     * 评论接口
     */
    private void comment(String text, String periods, String id, String type) {

        new GalleryImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
            @Override
            public void onSuccess(LikeAndFavoriteData response, int id) { //回复成功
                if (type.equals("2")) { //图库
                    responseComment.clear();
                    PictreCommentData pictreCommentData = new PictreCommentData();
                    pictreCommentData.setAdd_Time("刚刚");
                    pictreCommentData.setContent(text);
                    pictreCommentData.setId(response.getId());
                    pictreCommentData.setNickname(SPUtil.getNickName(getContext()));
                    pictreCommentData.setFace(SPUtil.getUserPhoto(getContext()));
                    PictreCommentData.LikeBean likeBean = new PictreCommentData.LikeBean();
                    likeBean.setNum("0");
                    likeBean.setStatus(0);
                    pictreCommentData.setLike(likeBean);
                    if (tv_past_message_count.getVisibility() != View.VISIBLE)
                        tv_past_message_count.setVisibility(View.VISIBLE);
                    int reply = GalleryExplainDetailActivity.this.response.getReply_Num() + 1;
                    GalleryExplainDetailActivity.this.response.setReply_Num(reply);
                    tv_past_message_count.setText(reply + "");
                    responseComment.add(pictreCommentData);

                    EventBusUtil.post(response);//通知评论
                    setFootView(responseComment, true);
                } else if (type.equals("4")) {//评论的评论
                    if (backInputDialog != null) {
                        int position = backInputDialog.getCommentPosition();
                        if (position != -1) {
                            list.get(position).setReply_Num(response.getNum()+"");
                        }
                        simpleAdapter.notifyUI(list);
                    }
                }
            }

        }.setNeedDialog(false)).postPullReply(text, id, periods, type);

    }

    @OnClick({R.id.rl_comment, R.id.rl_write_comment})
    public void onClicks(View view) {
        switch (view.getId()) {
            case R.id.rl_write_comment:
                if (!isLoginToast())
                    break;
                commentDetail(-1);
                break;
            case R.id.rl_comment:
                if (!isLoginToast())
                    break;
                if (Integer.valueOf(response.getReply_Num()) != 0) {
                    listView.setSelection(1);

                } else {
                    commentDetail(-1);
                }
                break;
            default:
        }
    }
}
