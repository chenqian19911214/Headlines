package com.marksixinfo.ui.fragment;

import android.view.View;
import android.widget.LinearLayout;

import com.marksixinfo.R;
import com.marksixinfo.adapter.ForumNavigatorAdapter;
import com.marksixinfo.adapter.PagerBaseAdapter;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.base.ViewPagerHelper;
import com.marksixinfo.bean.EditionNameDate;
import com.marksixinfo.bean.TaskCenterData;
import com.marksixinfo.bean.TaskSignInData;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.ui.activity.MainActivity;
import com.marksixinfo.utils.ActivityManager;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.utils.VersionUpdateUtils;
import com.marksixinfo.widgets.EditionDialog;
import com.marksixinfo.widgets.SignInSuccessDialog;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页
 *
 * @Auther: Administrator
 * @Date: 2019/3/15 0015 11:20
 * @Description:
 */
public class MainHomeFragment extends PageBaseFragment {

    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.cvp_view_pager)
    ViewPager mViewPager;

    private static final String[] CHANNELS = new String[]{"推荐", "关注"};
    private List<PageBaseFragment> mDataList = new ArrayList<>();
    private List<TaskCenterData.CheckinBean> signIn;

    @Override
    public int getViewId() {
        return R.layout.fragment_main_home;
    }

    @Override
    protected void afterViews() {

        mDataList.add(new RecommendFragment());
        mDataList.add(new ConcernFragment());

        //重复验证签到
        isReload = true;

        PagerBaseAdapter pagerBaseAdapter = new PagerBaseAdapter(getChildFragmentManager(), mDataList);

        mViewPager.setAdapter(pagerBaseAdapter);
        mViewPager.setCurrentItem(0, false);
        mViewPager.setOffscreenPageLimit(mDataList.size());


        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                CommonUtils.setInitFragmentData(mDataList, position);
            }
        });

        CommonUtils.setInitFragmentData(mDataList, 0);

        assert getContext() != null;
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdapter(new ForumNavigatorAdapter(mDataList, CHANNELS, mViewPager));
        magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer();
        if (getContext() != null) {
            titleContainer.setDividerPadding(UIUtil.dip2px(getContext(), 33));
        }
        ViewPagerHelper.bind(magicIndicator, mViewPager);


    }

    /**
     * 签到弹框
     */
    private void startSignIn() {
        SPUtil.setTaskSignInTime(getContext(),
                new TaskSignInData(System.currentTimeMillis(), SPUtil.getUserId(getContext())));//储存签到请求时间
        new HeadlineImpl(new MarkSixNetCallBack<List<TaskCenterData.CheckinBean>>(this, TaskCenterData.CheckinBean.class) {
            @Override
            public void onSuccess(List<TaskCenterData.CheckinBean> response, int id) {
                boolean todayCheckIn = CommonUtils.getTodayCheckIn(response);
                if (!todayCheckIn) {//当天未签到
                    signIn = response;
                    if (isVisible) {//可见时直接弹框
                        showSignInSuccess();
                    }
                }
            }
        }.setNeedDialog(false).setNeedToast(false)).signInHistory();

    }

    /**
     * 弹出签到成功dialog
     */
    private void showSignInSuccess() {
        if (signIn != null) {
          // EditionDialog updateDialog = VersionUpdateUtils.initVersionUpdateUtils().getUpdateDialog();
            if (VersionUpdateUtils.initVersionUpdateUtils().isShowDialog()) {//有更新弹框,不提示签到
                SPUtil.cleanTaskSignInTime(getContext());//更新后继续校验签到
                return;
            }
            new SignInSuccessDialog(this).setData(signIn);
            signIn = null;
        }
    }

    @OnClick({R.id.iv_search, R.id.tv_release, R.id.tv_task})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                 startClass(R.string.SearchActivity);//搜索
                //showEditionDialog();
                break;
            case R.id.tv_release://发布
                if (checkLogin(R.string.ReleaseActivity)) {
                    startClass(R.string.ReleaseActivity);
                }
                break;
            case R.id.tv_task://任务
                if (checkLogin(R.string.TaskCenterActivity)) {
                    startClass(R.string.TaskCenterActivity);
                }
                break;
        }
    }

    /**
     * 可见签到
     */
    @Override
    public void onResume() {
        super.onResume();
        if (isVisible) {
            checkSignIn();
        }
    }


    /**
     * 检查签到
     */
    private void checkSignIn() {
        try {
            if (isLogin()) {//已登录
                if (!CommonUtils.isCurrentDaySign(getContext())) { //一天只请求一次
                    startSignIn();
                } else {//可见且需要弹框
                    if (isVisible) {
                        showSignInSuccess();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    EditionDialog updateDialog;

    /**
     * 跟新弹窗
     */
    private void showEditionDialog() {
        if (!getActivity().isFinishing()) {
            if (updateDialog == null || !updateDialog.isShowing()) {
                EditionNameDate editionNameDate = new EditionNameDate();

                ArrayList<String> list = new ArrayList();
                list.add("1：更新头像");
                list.add("2：更新图库");
                list.add("3：更新东西");
                list.add("4：修改bug");
                list.add("5：修改bug");
                list.add("6：修改bug");
                list.add("7：修改bug");
                list.add("8：修改bug");
                editionNameDate.setState("2");
                editionNameDate.setUrl("http://www.baidu.com");
                editionNameDate.setVersion("2.03");
                editionNameDate.setNews(list);
                updateDialog = new EditionDialog((ActivityIntentInterface) getActivity(),editionNameDate);
                updateDialog.show();
            }
        }
    }

    /**
     * 可见签到
     */
    @Override
    protected void loadData() {
        checkSignIn();
    }
}
