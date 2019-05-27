package com.marksixinfo.ui.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.PictureAnswerAdapter;
import com.marksixinfo.adapter.PictureDetailsListAdapter;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.bean.CommentsListDetail;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.bean.PeriodData;
import com.marksixinfo.bean.PictreCommentData;
import com.marksixinfo.bean.PictureAnswerData;
import com.marksixinfo.bean.PictureDetailsData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.CommentListEvent;
import com.marksixinfo.evenbus.PullGalleryOkEvent;
import com.marksixinfo.evenbus.SendGalleryTitle;
import com.marksixinfo.interfaces.IDetailCommentInterface;
import com.marksixinfo.interfaces.NoDoubleClickListener;
import com.marksixinfo.net.impl.GalleryImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.utils.ThreadUtils;
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
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 图库详情fragment
 *
 * @Description:
 */
public class PictureDetailsFragment extends PageBaseFragment implements BackInputDialog.InputTextListener, IDetailCommentInterface, View.OnClickListener {

    /**
     * 回复的 dialog
     */
    private BackInputDialog backInputDialog;

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

    private TextView answer_pull_tv;
    /**
     * 图片地址
     */
    private String imageUri;
    private PictureDetailsListAdapter simpleAdapter;
    private List<PictreCommentData> list = new ArrayList();

    /**
     * 当前图库id
     */
    private String galleryID;
    /**
     * 图库的期数
     */
    private String period;

    /***
     *
     * */
    private int listPosition;

    private String newPeriod;

    private View topListView;
    private View topAnswerView;
    private int pageIndex = 0;//页数

    private String titleName;

    /**
     * 图解数据是否为空
     */
    private boolean isAnswerNull = false;



    @Override
    public int getViewId() {
        return R.layout.fragment_picture_details;
    }

    /**
     * 设置参数
     */
    public void setPictureId(PeriodData pictureId) {
        if (pictureId != null) {
            galleryID = pictureId.getGalleryId();
            period = pictureId.getId();
            newPeriod = pictureId.getNewPeriod();
            listPosition = pictureId.getPosition();
        }
    }

    /**
     * 获取图库详情 详情消息
     *
     * @param galleryID
     * @param period    期数
     */
    private void getPictureDetails(String galleryID, String period) {

        new GalleryImpl(new MarkSixNetCallBack<PictureDetailsData>(this, PictureDetailsData.class) {
            @Override
            public void onSuccess(PictureDetailsData response, int id) {
                if (response != null) {
                    //详情 数据
                    if (!isAddListView)
                        addListViewHead(response);
                    else
                        setLikeAndCollect(response, false, false);
                    getPictureReple(galleryID, period, true);
                    getPictureAnswer(galleryID, period);
                }
            }

            @Override
            public void onError(String msg, String code) {
                if (response == null) {
                    mLoadingLayout.showError();
                } else {
                    super.onError(msg, code);
                }
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                mLoadingLayout.showContent();
            }
        }.setNeedDialog(false)).getDetails(galleryID, period);
    }

    /**
     * 获取图库详情 评论
     *
     * @param id
     * @param period 期数
     */
    private void getPictureReple(String id, String period, boolean isRefresh) {
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
                    setFootView(response, false);
                }
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                loadDone();
            }
        }.setNeedDialog(false)).getComment(id, period, pageIndex);
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
            if (isAnswerNull)
                listView.setSelection(2);
            else
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
     * 发布图解成功回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(PullGalleryOkEvent event) {
        //refreshLayout.autoRefresh();
        getPictureAnswer(galleryID, period);
    }

    /**
     * 获取图库详情 图解
     *
     * @param id
     * @param period 期数
     */
    private void getPictureAnswer(String id, String period) {
        new GalleryImpl(new MarkSixNetCallBack<List<PictureAnswerData>>(this, PictureAnswerData.class) {
            @Override
            public void onSuccess(List<PictureAnswerData> responses, int id) {
                if (responses.size() > 0) {
                    responseList = responses;
                    //详情 数据
                    if (!isAddAnswerView) {
                        addAnswerViewHead();
                    } else {
                        UpdateAnswerData();
                    }
                }
            }
        }.setNeedDialog(false).setNeedToast(false)).getAnswer(id, period, 1);
    }

    /**
     * 更新图解小组ListView 数据
     * */
    private void UpdateAnswerData() {
        if (pictureAnswerAdapter.getListSize()<=3) {
            if (responseList.size() >= 3) {
                pictureAnswerAdapter.setListSize(3);
            } else {
                pictureAnswerAdapter.setListSize(responseList.size());
            }
        }else {
            if (responseList.size() >= 9) {
                pictureAnswerAdapter.setListSize(9);
            } else {
                pictureAnswerAdapter.setListSize(responseList.size());
            }
        }
        if (responseList.size() <= 3) {
            answer_pull_tv.setVisibility(View.GONE);
        } else {
            answer_pull_tv.setVisibility(View.VISIBLE);
        }
        pictureAnswerAdapter.notifyUI(responseList);
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

    @Override
    protected void afterViews() {
        isReload = true;
        simpleAdapter = new PictureDetailsListAdapter(getContext(), this, list, this);
        listView.setAdapter(simpleAdapter);
        mLoadingLayout.setRetryListener(new View.OnClickListener() {
            //点击重试
            @Override
            public void onClick(View view) {
                mLoadingLayout.showLoading();
                getPictureDetails(galleryID, period);
            }
        });
        /**
         * 下拉刷新
         * */
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                getPictureDetails(galleryID, period);
            }
        });
        //上拉加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                //   getCommentList();
                getPictureReple(galleryID, period, false);
            }
        });
        ivPraise.setOnClickListener(new NoDoubleClickListener() { //点赞
            @Override
            protected void onNoDoubleClick(View v) {

                if (response.getLike().getStatus()==0){
                        GoodView goodView = new GoodView(getContext());
                        ivPraise.playAnimation();
                        goodView.setTextColor(getContext().getResources().getColor(R.color.colorPrimary)).setText("+1").show(viewPraise);
                        ivPraise.setChecked(true);
                }
                praise(response, response.getLike().getStatus() == 1);
            }
        });

        ivCollect.setOnClickListener(new NoDoubleClickListener() { //收藏
            @Override
            protected void onNoDoubleClick(View v) {
                if (isLoginToast()) {
                    if (response.getFavorites().getStatus() == 0) {
                        ivCollect.playAnimation();
                        ivCollect.setChecked(true);
                    }
                    setFavorites(response, response.getFavorites().getStatus() == 1);
                }
            }
        });
        threadUtils = ThreadUtils.initThread(3);
    }


    private TextView PraiseTv;
    private TextView collerTv;
    private View viewPraise;
    private PictureDetailsData response;

    private ThreadUtils threadUtils;
    /**
     * 添加图库详情 消息
     */
    private void addListViewHead(PictureDetailsData response) {
        this.response = response;
        isAddListView = true;
        goodView = new GoodView(getContext());
        topListView = LayoutInflater.from(getContext()).inflate(R.layout.head_view_picture, null, true);
        listView.addHeaderView(topListView);
        TextView titleTv = topListView.findViewById(R.id.picture_title);
        TextView addTimeTv = topListView.findViewById(R.id.picture_add_time);
        TextView viewNumTv = topListView.findViewById(R.id.picture_view_num_tv1);
        ScaleImageView viewPictrueIv = topListView.findViewById(R.id.picture_image);
        PraiseTv = topListView.findViewById(R.id.tv_praise);
        collerTv = topListView.findViewById(R.id.tv_collect);
        viewPraise = topListView.findViewById(R.id.view_praise);
        this.titleName = response.getTitle();

       String prriods =  period.substring(period.length()-3,period.length());
        titleTv.setText("第"+prriods+"期"+this.titleName);
        addTimeTv.setText(response.getAdd_Time());
        viewNumTv.setText(response.getView_Num() + "");
        //  GlideUtil.loadImage(response.getPic(), viewPictrueIv);
        imageUri = response.getPic();
        if (!TextUtils.isEmpty(imageUri)) {
           /* new Thread(new Runnable() {
                @Override
                public void run() {
                  if (isAdded())
                      //setAutoScale(viewPictrueIv, CommonUtils.getGalleryUrl(imageUri));
                      setAutoScale(viewPictrueIv, imageUri);

                }
            }).start();*/

            threadUtils.addRunnable(new Runnable() {
                @Override
                public void run() {
                    setAutoScale(viewPictrueIv, imageUri);
                }
            });
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
              //  praise(response, response.getLike().getStatus() == 1);

                if (response.getLike().getStatus()==0){
                    GoodView goodView = new GoodView(getContext());
                    ivPraise.playAnimation();
                    goodView.setTextColor(getContext().getResources().getColor(R.color.colorPrimary)).setText("+1").show(viewPraise);
                    ivPraise.setChecked(true);
                }
                praise(response, response.getLike().getStatus() == 1);
            }
        });

        collerTv.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) { //踩一下
                collect(response, response.getTread().getStatus() == 1);
            }
        });
        setLikeAndCollect(response, false, false);
        EventBusUtil.post(new SendGalleryTitle(this.titleName));
    }

    private GoodView goodView;

    /**
     * 设置点赞数量和踩的数量
     */
    private void setLikeAndCollect(PictureDetailsData response, boolean isLikeAnimation, boolean isFavoritesAnimation) {
        this.response = response;
        //赞
        PictureDetailsData.LikeBean likeBean = response.getLike();
        if (likeBean != null) {
            String like_nums = likeBean.getNum();
            if (!TextUtils.isEmpty(like_nums)) {
                int like_num = Integer.valueOf(like_nums);
                PraiseTv.setText(like_num > 0 ? CommonUtils.getThousandNumber(like_num) : "");
            }
          /*  if (response.getLike().getStatus() == 1 && isLikeAnimation) {
                GoodView goodView = new GoodView(getContext());
                ivPraise.playAnimation();
                goodView.setTextColor(getContext().getResources().getColor(R.color.colorPrimary)).setText("+1").show(viewPraise);
            }*/
            CommonUtils.setPraiseStatus(getContext(), PraiseTv, likeBean.getStatus());
            ivPraise.setChecked(likeBean.getStatus() == 1);

        }

        //踩
        PictureDetailsData.TreadBean treadBean = response.getTread();
        if (treadBean != null) {
            String tread_nums = treadBean.getNum();
            if (!TextUtils.isEmpty(tread_nums)) {
                int ltread_num = Integer.valueOf(tread_nums);
                collerTv.setText(ltread_num > 0 ? CommonUtils.getThousandNumber(ltread_num) : "");
            }
            CommonUtils.setTrampleStatus(getContext(), collerTv, treadBean.getStatus());
        }
        //收藏
        PictureDetailsData.FavoritesBean favoritesBean = response.getFavorites();
        if (favoritesBean != null) {
          /*  if (favoritesBean.getStatus() == 1 && isFavoritesAnimation) {
                ivCollect.playAnimation();
            }*/
            ivCollect.setChecked(favoritesBean.getStatus() == 1);
        }
        //评论数
        int viewNum = Integer.valueOf(response.getReply_Num());
        if (viewNum > 0) {
            if (tv_past_message_count != null) {
                tv_past_message_count.setVisibility(View.VISIBLE);
            }
            tv_past_message_count.setText(response.getReply_Num());
        } else {
            tv_past_message_count.setVisibility(View.GONE);

        }
    }

    /**
     * 收藏接口
     *
     * @param mainHomeData
     * @param isPraise
     */
    private void setFavorites(PictureDetailsData mainHomeData, boolean isPraise) {
        if (mainHomeData.getLoadingStatus() == 0 &&  isLoginToast()) {
            mainHomeData.setLoadingStatus(1);
            new GalleryImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                @Override
                public void onSuccess(LikeAndFavoriteData response, int id) {
                    if (response != null) {
                        mainHomeData.getFavorites().setNum(response.getNum() + "");
                        mainHomeData.getFavorites().setStatus(response.getType());
                      //  setLikeAndCollect(mainHomeData, false, true);
                        mainHomeData.setPosition(listPosition);
                        if (period.equals(newPeriod))
                            EventBusUtil.post(mainHomeData);
                    }
                }

                @Override
                public void onAfter(int id) {
                    super.onAfter(id);
                    mainHomeData.setLoadingStatus(0);
                    setLikeAndCollect(mainHomeData, false, true);

                }
            }.setNeedDialog(false)).postClickFavorites(mainHomeData.getId(), period);
            mainHomeData.getFavorites().setStatus(!isPraise ? 1 : 0);
            int like_num = Integer.valueOf(mainHomeData.getFavorites().getNum());
            mainHomeData.getFavorites().setNum(!isPraise ? ++like_num + "" : --like_num + "");
            setLikeAndCollect(mainHomeData, false, true);
        }

/*
        mainHomeData.getFavorites().setStatus(!isPraise ? 1 : 0);
        int like_num = Integer.valueOf(mainHomeData.getFavorites().getNum());
        mainHomeData.getFavorites().setNum(!isPraise ? ++like_num + "" : --like_num + "");
        mainHomeData.setLoadingStatus(1);
        setLikeAndCollect(mainHomeData, false, true);*/
    }

    /**
     * 点赞接口
     *
     * @param mainHomeData
     * @param isPraise
     */
    private void praise(PictureDetailsData mainHomeData, boolean isPraise) {
        if (mainHomeData.getLoadingStatus() == 0 && isLogin()) {
            mainHomeData.setLoadingStatus(1);
            new GalleryImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
                @Override
                public void onSuccess(LikeAndFavoriteData response, int id) {
                    if (response != null) {
                        mainHomeData.getLike().setNum(response.getNum() + "");
                        mainHomeData.getLike().setStatus(response.getType());
                     //   setLikeAndCollect(mainHomeData, true, false);
                        mainHomeData.setPosition(listPosition);
                        if (period.equals(newPeriod))
                            EventBusUtil.post(mainHomeData);
                    }
                }

                @Override
                public void onError(String msg, String code) {
                    super.onError(msg, code);
                }

                @Override
                public void onAfter(int id) {
                    super.onAfter(id);
                    mainHomeData.setLoadingStatus(0);
                    setLikeAndCollect(mainHomeData, true, false);

                }
            }.setNeedDialog(false)).getClickLike(mainHomeData.getId(), period, 1 + "");
        }
        mainHomeData.getLike().setStatus(!isPraise ? 1 : 0);
        int like_num = Integer.valueOf(mainHomeData.getLike().getNum());
        mainHomeData.getLike().setNum(!isPraise ? ++like_num + "" : --like_num + "");
        setLikeAndCollect(mainHomeData, true, false);

    }

    /**
     * 踩接口
     *
     * @param mainHomeData
     * @param isCollect
     */
    private void collect(PictureDetailsData mainHomeData, boolean isCollect) {
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
                      //  mainHomeData.getTread().setStatus(status);
                       // mainHomeData.getTread().setNum(favorites_num + "");
                       // setLikeAndCollect(mainHomeData, false, false);
                    }

                    @Override
                    public void onAfter(int id) {
                        super.onAfter(id);
                        mainHomeData.setLoadingStatus(0);
                        setLikeAndCollect(mainHomeData, false, false);

                    }
                }.setNeedDialog(false)).postClickTread(mainHomeData.getId(), period, 1 + "");

                mainHomeData.getTread().setStatus(!isCollect ? 1 : 0);
                int like_num = Integer.valueOf(mainHomeData.getTread().getNum());
                mainHomeData.getTread().setNum(!isCollect ? ++like_num + "" : --like_num + "");
              //  mainHomeData.setLoadingStatus(1);
                setLikeAndCollect(mainHomeData, false, false);
            }

          /*  mainHomeData.getTread().setStatus(!isCollect ? 1 : 0);
            int like_num = Integer.valueOf(mainHomeData.getTread().getNum());
            mainHomeData.getTread().setNum(!isCollect ? ++like_num + "" : --like_num + "");
            mainHomeData.setLoadingStatus(1);
            setLikeAndCollect(mainHomeData, false, false);*/

        }
    }


    private boolean isAddAnswerView = false;
    private boolean isAddListView = false;
    PictureAnswerAdapter pictureAnswerAdapter;
    ListView viewNumTv;
    /**
     * 图解小组数据
     * */
    List<PictureAnswerData> responseList;
    /**
     * 添加图库详情 图解
     */
    private void addAnswerViewHead() {

        isAddAnswerView = true;
        isAnswerNull = true;
        topAnswerView = LayoutInflater.from(getContext()).inflate(R.layout.head_answer_view_picture, null, true);
        listView.addHeaderView(topAnswerView);
        //更多
        TextView answer_more_tv = topAnswerView.findViewById(R.id.answer_more_tv);
        //图解列表
        pictureAnswerAdapter = new PictureAnswerAdapter(getContext(), this, responseList);
        if (responseList.size()>=3){
            pictureAnswerAdapter.setListSize(3);
        }else {
            pictureAnswerAdapter.setListSize(responseList.size());
        }
        viewNumTv = topAnswerView.findViewById(R.id.answer_list_item_id);
        viewNumTv.setAdapter(pictureAnswerAdapter);

        viewNumTv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(responseList.get(i));
            }
        });

        answer_more_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity();
            }
        });
        //收起
        answer_pull_tv = topAnswerView.findViewById(R.id.answer_pull_tv);
        if (responseList.size() <= 3) {
            answer_pull_tv.setVisibility(View.GONE);
        } else {
            answer_pull_tv.setVisibility(View.VISIBLE);
        }
        answer_pull_tv.setOnClickListener(new View.OnClickListener() { //查看更多
            @Override
            public void onClick(View view) {
                if (pictureAnswerAdapter.getListSize() == 3) {
                    if (responseList.size()>=9){
                        pictureAnswerAdapter.setListSize(9);
                    }else {
                        pictureAnswerAdapter.setListSize(responseList.size());
                    }
                    pictureAnswerAdapter.notifyUI(responseList);
                    answer_pull_tv.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.icon_pull_grays),null);
                    answer_pull_tv.setText("收起");
                } else {
                    if (responseList.size()>=3){
                        pictureAnswerAdapter.setListSize(3);
                    }else {
                        pictureAnswerAdapter.setListSize(responseList.size());
                    }
                    pictureAnswerAdapter.notifyUI(responseList);
                    answer_pull_tv.setCompoundDrawablesWithIntrinsicBounds(null,null,getResources().getDrawable(R.drawable.icon_pull_gray2),null);
                    answer_pull_tv.setText("展开");

                }
            }
        });
    }

    /**
     * 跳转到图解小组
     */
    private void startActivity() {

        String[] list = new String[]{
                StringConstants.IMAGE_URL, imageUri,
                StringConstants.ID, galleryID,
                StringConstants.PERIOD, period
        };
        HashMap<String, String> hashMap = IntentUtils.getHashObj(list);
        startClass(R.string.GraphicListActivity, hashMap);//搜索
    }

    /**
     * 跳转到图解页面
     * */
    private void startActivity(PictureAnswerData pictureAnswerData){

        String[] list = new String[]{
                StringConstants.ID, pictureAnswerData.getId(),
                StringConstants.IMAGE_URL,imageUri
        };
        HashMap<String, String> hashMap = IntentUtils.getHashObj(list);

        startClass(R.string.GalleryExplainDetailActivity, hashMap);//搜索

    }
    /**
     * 设置自适应缩放
     *
     * @param mIvHeadImage
     */
    private void setAutoScale(ImageView mIvHeadImage, String uri) {

        Context context = getContext() ;
        final float[] imageHeight = {0};
        Bitmap mBitmap = GlideUtil.load(getContext(), uri);
        if (mBitmap == null||context == null||getActivity()==null)
            return;
        int maxWidth = UIUtils.getScreenWidth(context) - UIUtils.dip2px(context, 30);
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        if (width != 0 && mBitmap != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    float scale = maxWidth * 1.0f / width * 1.0f;
                    imageHeight[0] = height * 1.0f * scale;
                    ViewGroup.LayoutParams lp = mIvHeadImage.getLayoutParams();
                    lp.width = maxWidth;
                    lp.height = (int) imageHeight[0];
                    mIvHeadImage.setLayoutParams(lp);
                    mIvHeadImage.setImageBitmap(mBitmap);
                   // GlideUtil.loadCircleImage(mBitmap,mIvHeadImage);
                }
            });
        }
    }

    /**
     * 评论的评论页面跟新
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(CommentListEvent event) {
        if (event != null) {//回复帖子列表页,刷新详情回复数量
            if (event.getType()==2) {
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


    @Override
    protected void loadData() {
        if (!TextUtils.isEmpty(galleryID) && !TextUtils.isEmpty(period)) {
            getPictureDetails(galleryID, period);
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
            backInputDialog.setCommentPosition(position).setId(galleryID).show(backDialogHint);
        }
    }

    /**
     * 回复后的text
     */
    @Override
    public void onInputText(String text) {
        if (backInputDialog != null) {
            int position = backInputDialog.getCommentPosition();
            if (position < 0) { //图解回复 galleryID,period
                comment(text, period, galleryID, "1");
            } else { //回复的回复
                comment(text, "", list.get(position).getId(), "3");
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
            }.setNeedDialog(false)).getClickLike(mainHomeData.getId(), "", 3 + "");
        }
        mainHomeData.getLike().setStatus(isPraise ? 1 : 0);
        int like_num = Integer.valueOf(mainHomeData.getLike().getNum());
        mainHomeData.getLike().setNum(isPraise ? ++like_num + "" : --like_num + "");
        mainHomeData.setLoadingStatus(1);
        simpleAdapter.notifyUI(list);
    }

    /**
     * 调评论接口
     */
    List<PictreCommentData> responseComment = new ArrayList<>();

    private void comment(String text, String periods, String id, String type) {

        new GalleryImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
            @Override
            public void onSuccess(LikeAndFavoriteData response, int id) { //回复成功
                // refreshLayout.autoRefresh();
                if (type.equals("1")) { //图库
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
                    int reply = Integer.valueOf(PictureDetailsFragment.this.response.getReply_Num()) + 1;
                    PictureDetailsFragment.this.response.setReply_Num(reply + "");
                    tv_past_message_count.setText(reply + "");
                    responseComment.add(pictreCommentData);
                    setFootView(responseComment, true);
                } else if (type.equals("3")) {//评论的评论
                    if (backInputDialog != null) {
                        int position = backInputDialog.getCommentPosition();
                        if (position != -1) {
                           // list.get(position).setReply_Num(list.get(position).getReply_Num() + 1);
                            list.get(position).setReply_Num(response.getNum()+"");

                        }
                        simpleAdapter.notifyUI(list);
                    }
                }
            }

            @Override
            public void onError(String msg, String code) {
                super.onError(msg, code);
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
            }
        }.setNeedDialog(false)).postPullReply(text, id, periods, type);

    }

    @OnClick({R.id.rl_comment, R.id.rl_write_comment})
    public void onClicks(View view) {
        switch (view.getId()) {
            case R.id.rl_write_comment:
                if (isLoginToast())
                    commentDetail(-1);
                break;
            case R.id.rl_comment:
                if (!isLoginToast())
                    break;
                if (Integer.valueOf(response.getReply_Num()) != 0) {
                    if (isAnswerNull)
                        listView.setSelection(2);
                    else
                        listView.setSelection(1);

                } else {
                    commentDetail(-1);
                }
                break;
            default:
        }
    }

    /**
     * 查看用户
     */
    @Override
    public void checkUser(String id) {

    }

    /**
     * 评论赞
     */
    @Override
    public void praise(int position, boolean isPraise) {
        commounPraise(position, isPraise);
    }

    private int commentListPosition;

    /**
     * 评论回复
     */
    @Override
    public void commentUser(int position, boolean hasComment) {
        commentListPosition = position;
        if (! isLoginToast())
            return;
        if (hasComment) {
            goToCommentListActivity(list.get(position).getId(), 2);
        } else {
            commentDetail(position);
        }
    }


    @Override
    public void onClick(View view) {


    }
}
