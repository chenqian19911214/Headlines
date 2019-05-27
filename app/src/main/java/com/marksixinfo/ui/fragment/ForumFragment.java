package com.marksixinfo.ui.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.marksixinfo.R;
import com.marksixinfo.adapter.PagerBaseAdapter;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.base.ViewPagerHelper;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.LoginSuccessEvent;
import com.marksixinfo.ui.activity.MainActivity;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.LogUtils;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.CommonNavigator;
import com.marksixinfo.widgets.MarkSixNavigatorAdapter;
import com.marksixinfo.widgets.glide.GlideUtil;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.ArgbEvaluatorHolder;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 论坛
 *
 * @Auther: Administrator
 * @Date: 2019/3/15 0015 12:06
 * @Description:
 */
public class ForumFragment extends PageBaseFragment {


    @BindView(R.id.indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.viewpager)
    ViewPager cvpViewPager;
    @BindView(R.id.iv_user_photo)
    ImageView ivUserPhoto;
    @BindView(R.id.tv_nike_name)
    TextView tvNikeName;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.ll_topView)
    LinearLayout llTopView;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.view_line)
    View view_line;

    private List<PageBaseFragment> mDataList = new ArrayList<>();
    private static final String[] CHANNELS = new String[]{"关注", "广场"/*, "购彩", "专家"*/};
    private boolean isInit = true;
    private boolean isWhiteStatus;


    @Override
    public int getViewId() {
        return R.layout.fragment_forum;
    }

    @Override
    protected void afterViews() {

        //每次更新头像
        isReload = true;

        initUserPhoto();

        mDataList.clear();
        mDataList.add(getContentFragment(0));
        mDataList.add(getContentFragment(1));

        PagerBaseAdapter pagerBaseAdapter = new PagerBaseAdapter(getChildFragmentManager(), mDataList);

        cvpViewPager.setAdapter(pagerBaseAdapter);
        cvpViewPager.setOffscreenPageLimit(mDataList.size());

        cvpViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                CommonUtils.setInitFragmentData(mDataList, position);
            }
        });

        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setNeedSetTitleWidth(false);//分类从左开始
        MarkSixNavigatorAdapter markSixNavigatorAdapter = new MarkSixNavigatorAdapter(Arrays.asList(CHANNELS), cvpViewPager, 3,
                LinePagerIndicator.MODE_EXACTLY);
        markSixNavigatorAdapter.setTextSize(18);
        commonNavigator.setAdapter(markSixNavigatorAdapter);
        magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        if (getContext() != null) {
            titleContainer.setDividerPadding(UIUtil.dip2px(getContext(), 33));
        }

        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, cvpViewPager);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            int totalScrollRange = UIUtils.dip2px(getContext(), 30);

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                int ver = Math.abs(verticalOffset);
                view_line.setVisibility(appBarLayout.getTotalScrollRange() == ver ? View.GONE : View.VISIBLE);
                if (ver > totalScrollRange) {
                    ver = totalScrollRange;
                }

                float v = ver / (totalScrollRange * 1.0f);

                LogUtils.d("AppBarLayout___" + totalScrollRange + "____" + ver + "_______" + v + "____" + verticalOffset);


                setStatus(ver == totalScrollRange);

                int size = UIUtils.dip2px(getContext(), 60) - ver;
                ivUserPhoto.getLayoutParams().width = size;
                ivUserPhoto.getLayoutParams().height = size;

                int i = (int) -(UIUtils.dip2px(getContext(), 14) * v);

//                if (v < 0.7f) {
//                    tvNikeName.setAlpha(1.0f - v);
//                }
                tvNikeName.setTextColor(ArgbEvaluatorHolder.eval(v, 0xff333333, 0xff666666));

                tvNikeName.setTextSize(17 - (2 * v));

                magicIndicator.setTranslationY(i);
                view_line.setTranslationY(i);

                llTopView.getLayoutParams().height = UIUtils.dip2px(getContext(), 121.5f) - ver + i;

                flContent.requestLayout();
            }
        });
    }


    /**
     * 初始头像及昵称
     */
    private void initUserPhoto() {
        GlideUtil.loadCircleImage(SPUtil.getUserPhoto(getContext()), ivUserPhoto);
        String nickName = SPUtil.getNickName(getContext());
        if (isLogin()) {
            tvNikeName.setText(CommonUtils.StringNotNull(nickName)
                    ? CommonUtils.handleText(nickName, 20) : "");
        } else {
            tvNikeName.setText("未登录");
        }
    }


    @Override
    protected void loadData() {
        initUserPhoto();
        if (isInit && isVisible) {
            isInit = false;
            //显示广场
            cvpViewPager.setCurrentItem(1);
        }
    }

    /**
     * 获取关注/广场
     *
     * @param type
     * @return
     */
    private ForumListFragment getContentFragment(int type) {
        ForumListFragment forumListFragment = new ForumListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(StringConstants.TYPE, type);
        forumListFragment.setArguments(bundle);
        return forumListFragment;
    }

    @OnClick({R.id.iv_user_photo, R.id.tv_nike_name, R.id.tv_forum_release/*, R.id.tv_forum_release2*/})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_nike_name://点击昵称
            case R.id.iv_user_photo://点击头像
                if (checkLogin(R.string.ForumUserActivity)) {
                    startClass(R.string.ForumUserActivity);
                }
                break;
            case R.id.tv_forum_release://发布
//            case R.id.tv_forum_release2://发布
                HashMap<String, String> stringHashMap = IntentUtils.getHashObj(new String[]{StringConstants.TYPE, "0"});
                if (checkLogin(R.string.ForumReleaseActivity, stringHashMap)) {
                    startClass(R.string.ForumReleaseActivity, stringHashMap);
                }
                break;
        }
    }

    /**
     * 登录成功,刷新状态
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(LoginSuccessEvent event) {
        if (event != null) {
            loadData();
        }
    }


    /**
     * 动态设置状态栏
     *
     * @param isWhite
     */
    private void setStatus(boolean isWhite) {
        if (this.isWhiteStatus != isWhite) {
            this.isWhiteStatus = isWhite;
//            tvNikeName.setTextColor(isWhite ? 0xff666666 : 0xff333333);
            tvNikeName.setTypeface(Typeface.defaultFromStyle(isWhite ? Typeface.NORMAL : Typeface.BOLD));
            ((MainActivity) getActivity()).setTitleBar(isWhiteStatus);
        }
    }

}
