package com.marksixinfo.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.FriendsListAdapter;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.bean.FriendListData;
import com.marksixinfo.bean.FriendListDetailData;
import com.marksixinfo.constants.UrlStaticConstants;
import com.marksixinfo.interfaces.IFriendsListInterface;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.ReleaseUtils;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.SpannableStringUtils;
import com.marksixinfo.widgets.umengshare.ShareParams;
import com.marksixinfo.widgets.umengshare.UMShareDialog;
import com.marksixinfo.widgets.umengshare.UMengShareUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能描述: 好友列表
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/4/19 0019 15:07
 */
public class FriendsListActivity extends MarkSixWhiteActivity implements View.OnClickListener, IFriendsListInterface {


    @BindView(R.id.recyclerView)
    ListView recyclerView;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.ll_bottom_content)
    LinearLayout llBottomContent;

    private TextView tvInvitedFriends;
    private TextView tvRevenueReceived;
    private TextView tvBeBeingEarnings;
    private LinearLayout llHeadView;


    private List<FriendListData> list = new ArrayList<>();
    private FriendsListAdapter adapter;
    private int pageIndex = 0;//页数
    private Bitmap mBitmap;
    private UMShareDialog umShareDialog;
    private ShareParams shareParams;
    private ReleaseUtils releaseUtils;
    private boolean isInit = true;

    @Override
    public int getViewId() {
        return R.layout.activity_friends_list;
    }

    @Override
    public void afterViews() {
        markSixTitleWhite.init("好友列表");

        View headView = View.inflate(getContext(), R.layout.item_friends_list_head, null);
        llHeadView = headView.findViewById(R.id.ll_head_view);
        ImageView mIvHeadImage = headView.findViewById(R.id.iv_head_image);
        int imageHeight = setAutoScale(mIvHeadImage);
        tvInvitedFriends = headView.findViewById(R.id.tv_invited_friends);
        tvRevenueReceived = headView.findViewById(R.id.tv_revenue_received);
        tvBeBeingEarnings = headView.findViewById(R.id.tv_be_being_earnings);
        recyclerView.addHeaderView(headView);

        int emptyHeight = UIUtils.getScreenHeight(this) - UIUtils.dip2px(getContext()
                , 221) - UIUtils.getStatusBarHeight(getContext()) - imageHeight;


        adapter = new FriendsListAdapter(this, list, emptyHeight, this);
        recyclerView.setAdapter(adapter);
        mLoadingLayout.setRetryListener(this);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                getDetail();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refresh(false, null);
            }
        });

        getDetail();
    }


    /**
     * 设置自适应缩放
     *
     * @param mIvHeadImage
     */
    private int setAutoScale(ImageView mIvHeadImage) {
        float imageHeight = 0;
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_my_friend_head);
        int maxWidth = UIUtils.getScreenWidth(this);
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
        return (int) imageHeight;
    }


    /**
     * 获取详情
     */
    private void getDetail() {
        //开启加载更多
        refreshLayout.setEnableLoadMore(true);
        refreshLayout.setEnableAutoLoadMore(true);
        refreshLayout.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
        new HeadlineImpl(new MarkSixNetCallBack<FriendListDetailData>(this, FriendListDetailData.class) {
            @Override
            public void onSuccess(FriendListDetailData response, int id) {
                refresh(true, response);
            }

            @Override
            public void onError(String msg, String code) {
                loadDone(true);
                mLoadingLayout.showError();
            }
        }.setNeedDialog(false)).myFriendListDetail();
    }

    /**
     * 请求网络
     *
     * @param isRefresh
     */
    private void refresh(boolean isRefresh, FriendListDetailData response2) {
        if (isRefresh) {
            pageIndex = 0;
        }
        new HeadlineImpl(new MarkSixNetCallBack<List<FriendListData>>(this, FriendListData.class) {
            @Override
            public void onSuccess(List<FriendListData> response, int id) {
                setDetail(response2);
                showBottomButton();
                setData(isRefresh, response);
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
        }.setNeedDialog(false)).myFriendList(String.valueOf(++pageIndex));
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
     * @param isRefresh
     * @param response
     */
    private void setData(boolean isRefresh, List<FriendListData> response) {
        if (isRefresh) {
            list.clear();
        }
        if (CommonUtils.ListNotNull(response)) {
            list.addAll(response);
        } else {
            setEmptyData();
            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
        }
        if (adapter != null) {
            adapter.notifyUI(list);
        }
        if (CommonUtils.ListNotNull(list)) {
            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }
    }

    /**
     * 设置邀请详情
     *
     * @param response
     */
    private void setDetail(FriendListDetailData response) {
        if (response != null) {
            llHeadView.setVisibility(View.VISIBLE);
            String count = response.getCount();
            String completed = response.getCompleted();
            String processing = response.getProcessing();
            setDetailTextView(tvInvitedFriends, count, "个");
            setDetailTextView(tvRevenueReceived, completed, "元");
            setDetailTextView(tvBeBeingEarnings, processing, "元");
        }
    }


    /**
     * 设置邀请详情
     *
     * @param textView
     * @param number
     * @param unit
     */
    private void setDetailTextView(TextView textView, String number, String unit) {
        SpannableStringUtils ssu = new SpannableStringUtils();
        ssu.addText(24, 0xfffc5c66, number);
        ssu.addText(13, 0xff333333, unit);
        textView.setText(ssu.toSpannableString());
    }


    /**
     * 设置无好友空太页
     */
    private void setEmptyData() {
        //添加空页面,禁止加载更多
        if (!CommonUtils.ListNotNull(list)) {
            list.add(new FriendListData(1));
            refreshLayout.setEnableLoadMore(false);
            refreshLayout.setEnableAutoLoadMore(false);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.retry_button://网络错误
                mLoadingLayout.showLoading();
                getDetail();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }

    @OnClick(R.id.tv_invite_friends)
    public void onViewClicked() {
        //邀请好友
//        startClass(R.string.InviteFriendsActivity);
        //分享
        if (umShareDialog == null) {
            umShareDialog = new UMShareDialog(this);
        }
        ShareParams shareParams = getShareParams();
        UMengShareUtils.getInstance()
                .doCustomShare(this, shareParams.getUrl(),
                        shareParams.getTitle(), shareParams.getContent(),
                        shareParams.getUmImage(), shareParams.getListener(),
                        umShareDialog);
        if (releaseUtils == null) {
            releaseUtils = new ReleaseUtils();
        }
        releaseUtils.setPermission(this, null);
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
     * 查看用户
     *
     * @param userId
     */
    @Override
    public void clickUser(String userId) {
        goToUserCenterActivity(userId, 0);
    }

    /**
     * 提醒好友
     *
     * @param userId
     */
    @Override
    public void remindUser(String userId, int position) {
//        toast("提醒");
        new HeadlineImpl(new MarkSixNetCallBack<Object>(this, Object.class) {
            @Override
            public void onSuccess(Object response, int id) {
                toast("已提醒");
                goneButton(position);
            }
        }).remindFriends(userId);
    }


    /**
     * 隐藏提醒好友按钮
     *
     * @param position
     */
    private void goneButton(int position) {
        try {
            list.get(position).setStatus(1);
            adapter.notifyUI(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 动画显示底部按钮
     */
    private void showBottomButton() {
        if (isInit) {
            isInit = false;
            CommonUtils.getValueShow(llBottomContent, UIUtils.dip2px(getContext(), 83), 300).start();
        }
    }
}
