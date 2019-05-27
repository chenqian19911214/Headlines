package com.marksixinfo.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.marksixinfo.R;
import com.marksixinfo.adapter.PagerBaseAdapter;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.base.ViewPagerHelper;
import com.marksixinfo.bean.LikeAndFavoriteData;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.bean.UserPostCenterCategory;
import com.marksixinfo.bean.UserPostCenterData;
import com.marksixinfo.bean.UserPostCenterMember;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.CommListDataEvent;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.ui.fragment.UserPostCentreFragment;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.EventBusUtil;
import com.marksixinfo.utils.NumberUtils;
import com.marksixinfo.utils.ReleaseUtils;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.CommonNavigator;
import com.marksixinfo.widgets.ConcernTextView;
import com.marksixinfo.widgets.MarkSixNavigatorAdapter;
import com.marksixinfo.widgets.UserPostCategoryView;
import com.marksixinfo.widgets.glide.GlideUtil;
import com.marksixinfo.widgets.umengshare.ShareParams;
import com.marksixinfo.widgets.umengshare.UMShareDialog;
import com.marksixinfo.widgets.umengshare.UMengShareUtils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 查看发贴人
 *
 * @Auther: Administrator
 * @Date: 2019/3/21 0021 19:57
 * @Description:
 */
public class UserPostCenterActivity extends MarkSixActivity implements
        UserPostCategoryView.OnSelectedListener, View.OnClickListener {
    @BindView(R.id.iv_official)
    ImageView ivOfficial;
    @BindView(R.id.tv_concern)
    ConcernTextView tvConcern;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_user_photo)
    ImageView tvUserPhoto;
    @BindView(R.id.tv_official_website)
    TextView tvOfficialWebsite;
    @BindView(R.id.tv_attention)
    TextView tvAttention;
    @BindView(R.id.tv_attention_view)
    TextView tvAttentionView;
    @BindView(R.id.tv_vermicelli)
    TextView tvVermicelli;
    @BindView(R.id.tv_vermicelli_view)
    TextView tvVermicelliView;
    @BindView(R.id.ll_topView)
    LinearLayout topView;
    @BindView(R.id.indicator)
    MagicIndicator indicator;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.view_user_post)
    UserPostCategoryView viewUserPost;


    private TextView tvNameTitle;
    private List<PageBaseFragment> mDataList = new ArrayList<>();
    private List<String> classify = new ArrayList<>();
    private String userId = "";
    private boolean isShowTitle;//是否显示title
    private UMShareDialog umShareDialog;
    private ShareParams shareParams;
    private String shareUrl = "https://app.sskk58.com/";
    private int look_status;
    private UserPostCenterData data;
    private String face;
    private int type = 0;//分类 0,头条  1,论坛  2,图库
    private List<UserPostCenterCategory> category;
    private UserPostCenterCategory allCategory;
    private ReleaseUtils releaseUtils;


    @Override
    public int getViewId() {
        return R.layout.activity_user_post_centre;
    }

    @Override
    public void afterViews() {

        markSixTitle.init("", "");
        tvNameTitle = markSixTitle.getTvTitle();
        tvNameTitle.setAlpha(0);

        allCategory = new UserPostCenterCategory("0", "全部", "", "", "0");

        userId = getStringParam(StringConstants.USER_ID);
        type = NumberUtils.stringToInt(getStringParam(StringConstants.TYPE));


        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int scrollRange = appBarLayout.getTotalScrollRange();
                onHidden(scrollRange == Math.abs(verticalOffset));
            }
        });

        viewUserPost.setListener(this);

        viewUserPost.setIndex(type);
    }


    /**
     * 获取数据
     */
    private void getData() {
        getDetail();
        getCategory();
    }


    /**
     * 获取详情
     */
    private void getDetail() {
        new HeadlineImpl(new MarkSixNetCallBack<UserPostCenterData>(this, UserPostCenterData.class) {
            @Override
            public void onSuccess(UserPostCenterData response, int id) {
                setDetail(response);
            }
        }.setNeedDialog(false)).getUserCenter(userId);
    }


    /**
     * 获取分类
     */
    private void getCategory() {
        new HeadlineImpl(new MarkSixNetCallBack<List<UserPostCenterCategory>>(this, UserPostCenterCategory.class) {
            @Override
            public void onSuccess(List<UserPostCenterCategory> response, int id) {
                setCategory(response);
            }
        }.setNeedDialog(false)).getUserCenterType(userId);
    }

    /**
     * 设置分类
     *
     * @param response
     */
    private void setCategory(List<UserPostCenterCategory> response) {
        category = response;
        setViewPage(category);
    }


    /**
     * 设置数据
     *
     * @param response
     */
    private void setDetail(UserPostCenterData response) {
        if (response != null) {
            data = response;
            UserPostCenterMember member = data.getMember();
            look_status = data.getIs_look();
            tvConcern.setLookStatus(look_status);
            setMember(member);
            List<Integer> look = data.getLook();
            setAttention(look);
        }
    }


    /**
     * 设置viewpage数据
     *
     * @param category
     */
    private void setViewPage(List<UserPostCenterCategory> category) {
        setTitlesAndData(category);
        PagerBaseAdapter pagerBaseAdapter = new PagerBaseAdapter(getSupportFragmentManager(), mDataList);
        viewPager.setAdapter(pagerBaseAdapter);
        viewPager.setCurrentItem(0, false);
        viewPager.setOffscreenPageLimit(mDataList.size());
        MarkSixNavigatorAdapter markSixNavigatorAdapter = new MarkSixNavigatorAdapter(classify, viewPager, 0,
                LinePagerIndicator.MODE_WRAP_CONTENT);
        CommonNavigator commonNavigator = new CommonNavigator(this);
        commonNavigator.setAdapter(markSixNavigatorAdapter);

        indicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(indicator, viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            public void onPageSelected(int position) {
                mSwipeBackLayout.setSwipeFromEdge(position != 0);
                CommonUtils.setInitFragmentData(mDataList, position);
            }
        });

        viewPager.post(new Runnable() {
            @Override
            public void run() {
                CommonUtils.setInitFragmentData(mDataList, 0);
            }
        });
    }

    /**
     * 关注,粉丝数
     *
     * @param look
     */
    private void setAttention(List<Integer> look) {
        if (CommonUtils.ListNotNull(look)) {
            try {
                String attention = CommonUtils.getThousandNumber(look.get(0));
                String vermicelli = CommonUtils.getThousandNumber(look.get(1));
                tvAttentionView.setVisibility(View.VISIBLE);
                tvVermicelliView.setVisibility(View.VISIBLE);
                tvAttention.setText(CommonUtils.StringNotNull(attention) ? attention : "0");
                tvVermicelli.setText(CommonUtils.StringNotNull(vermicelli) ? vermicelli : "0");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置头部个人信息
     *
     * @param member
     */
    private void setMember(UserPostCenterMember member) {
        if (member != null) {
            face = member.getFace();
            String Nickname = member.getNickname();
            int level = member.getLevel();
            String remark = member.getRemark();
            CommonUtils.setUserLevel(ivOfficial, type == 0 ? level : 0);
            tvUserName.setText(CommonUtils.StringNotNull(Nickname) ? CommonUtils.CommHandleText2(Nickname) : "");
//            tvNameTitle.setText(CommonUtils.StringNotNull(Nickname) ? Nickname : "");
            //分享
            markSixTitle.init(CommonUtils.StringNotNull(Nickname) ? Nickname : "", ""
                    , "", R.drawable.icon_share, this);
            GlideUtil.loadCircleImage(face, tvUserPhoto);
            tvUserPhoto.setVisibility(View.VISIBLE);
            if (type == 0) {
                tvOfficialWebsite.setVisibility(View.VISIBLE);
                tvOfficialWebsite.setText(CommonUtils.StringNotNull(remark) ? remark : "官方网址：无");
            } else {
                tvOfficialWebsite.setVisibility(View.GONE);
            }
        }
    }


    /**
     * 获取title及Fragment
     *
     * @param category
     */
    private void setTitlesAndData(List<UserPostCenterCategory> category) {
        if (viewUserPost.getVisibility() != View.VISIBLE) {
            viewUserPost.setVisibility(View.VISIBLE);
        }
        if (!CommonUtils.ListNotNull(category)) {
            category = new ArrayList<>();
        }
        if (type == 0) {
            if (indicator.getVisibility() != View.VISIBLE) {
                indicator.setVisibility(View.VISIBLE);
                topView.setMinimumHeight(UIUtils.dip2px(this, 41));
            }
        } else {
            if (indicator.getVisibility() != View.GONE) {
                indicator.setVisibility(View.GONE);
                topView.setMinimumHeight(UIUtils.dip2px(this, 0));
            }
        }
        classify.clear();
        mDataList.clear();
        //新增一个全部分类
        if (!category.contains(allCategory)) {
            category.add(0, allCategory);
        }
        for (UserPostCenterCategory centerCategory : category) {
            if (centerCategory != null) {
                classify.add(centerCategory.getName());
                UserPostCentreFragment userPostCentreFragment = new UserPostCentreFragment();
                Bundle bundle = new Bundle();
                bundle.putString(StringConstants.CATEGROY_ID, centerCategory.getId());
                bundle.putString(StringConstants.USER_ID, userId);
                bundle.putInt(StringConstants.TYPE, type);
                userPostCentreFragment.setArguments(bundle);
                mDataList.add(userPostCentreFragment);
            }
        }
    }


    @OnClick({R.id.tv_concern, R.id.tv_user_photo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_concern://关注
                if (tvConcern.getLook_status() != -1) {
                    if (tvConcern.startLookStatus()) {
                        concern();
                    }
                }
                break;
            case R.id.tv_user_photo: //查看头像
                CommonUtils.checkImagePreview(getContext(), CommonUtils.getFaceUrls(face), 0);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
        }
    }

    /**
     * 关注接口
     */
    private void concern() {
        new HeadlineImpl(new MarkSixNetCallBack<LikeAndFavoriteData>(this, LikeAndFavoriteData.class) {
            @Override
            public void onSuccess(LikeAndFavoriteData response, int id) {
                setConcern(response);
            }

            @Override
            public void onError(String msg, String code) {
                super.onError(msg, code);
                if (tvConcern != null) {
                    tvConcern.setLookStatus(look_status);
                }
            }
        }.setNeedDialog(false)).concernUser(userId);
    }


    /**
     * 设置关注数量
     *
     * @param response
     */
    private void setConcern(LikeAndFavoriteData response) {
        if (response != null && tvVermicelli != null) {
            data.setIs_look(response.getType());
            String vermicelli = CommonUtils.getThousandNumber(response.getNum());
            tvVermicelli.setText(CommonUtils.StringNotNull(vermicelli) ? vermicelli : "0");
            refreshEvent();
        }
    }


    /**
     * 获取分享
     *
     * @return
     */
    public ShareParams getShareParams() {
        if (shareParams == null) {
            shareParams = new ShareParams("", "", UMengShareUtils.getImageIcon(this, ""), shareUrl, new UMengShareUtils.MyShareListener(getContext()));
        } else {

//            shareParams.setContent(getSecondTitle());//重设副标题

            shareParams.setUmImage(UMengShareUtils.getImageIcon(this, ""));//重设icon

            shareParams.setUrl(shareUrl);
        }

        return shareParams;
    }

    /**
     * 动画隐藏用户名称
     *
     * @param isHidden
     */
    public void onHidden(boolean isHidden) {
        if (isShowTitle != isHidden) {
            isShowTitle = isHidden;
            if (isShowTitle) {
                CommonUtils.getValueShow(tvNameTitle, UIUtils.getViewHeight(tvNameTitle)).start();
            } else {
                CommonUtils.getValueHidden(tvNameTitle, UIUtils.getViewHeight(tvNameTitle)).start();
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
        if (event != null && event.getData() != null && data != null) {
            MainHomeData data = event.getData();
            int look_status = data.getLook_status();
            if (look_status != this.look_status) {
                updateLookNum();
            }
            CommonUtils.commListDataChange(data, this.data);
            this.look_status = this.data.getIs_look();
            tvConcern.setLookStatus(this.look_status);
        }
    }

    /**
     * 更新粉丝数量
     */
    private void updateLookNum() {
        new HeadlineImpl(new MarkSixNetCallBack<UserPostCenterData>(this, UserPostCenterData.class) {
            @Override
            public void onSuccess(UserPostCenterData response, int id) {
                if (response != null) {
                    List<Integer> look = response.getLook();
                    setAttention(look);
                }
            }
        }.setNeedDialog(false)).getUserCenter(userId);
    }

    /**
     * 列表操作全局刷新
     */
    private void refreshEvent() {
        if (data != null) {
            EventBusUtil.post(new CommListDataEvent(CommonUtils.getMainHomeData(data), getClassName()));
        }
    }

    @Override
    public void onSelected(int index) {
        type = index;
        switch (index) {
            case 0://头条
                getData();
                break;
            case 1://论坛
            default://图库
                getDetail();
                setViewPage(null);
                break;

        }
    }
}
