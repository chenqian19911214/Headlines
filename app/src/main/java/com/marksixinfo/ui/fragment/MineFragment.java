package com.marksixinfo.ui.fragment;

import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.MineUserFunctionAdapter;
import com.marksixinfo.base.CommonDialog;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.PageBaseFragment;
import com.marksixinfo.bean.MineNewCenter;
import com.marksixinfo.bean.MineUserFunction;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.evenbus.TokenTimeOutEvent;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.MineSystemView;
import com.marksixinfo.widgets.glide.GlideUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的
 *
 * @Auther: Administrator
 * @Date: 2019/3/15 0015 12:20
 * @Description:
 */
public class MineFragment extends PageBaseFragment implements View.OnClickListener {

    @BindView(R.id.iv_user_photo)
    ImageView ivUserPhoto;
    @BindView(R.id.tv_Nickname)
    TextView tvNickname;
    @BindView(R.id.iv_official)
    ImageView ivOfficial;
    @BindView(R.id.tv_user_signature)
    TextView tvUserSignature;
    @BindView(R.id.tv_invitation_num)
    TextView tvInvitationNum;
    @BindView(R.id.tv_concern_num)
    TextView tvConcernNum;
    @BindView(R.id.tv_fans_num)
    TextView tvFansNum;
    @BindView(R.id.tv_praise_num)
    TextView tvPraiseNum;
    @BindView(R.id.tv_user_gold)
    TextView tvUserGold;
    @BindView(R.id.tv_user_balance)
    TextView tvUserBalance;
    @BindView(R.id.gridView_function)
    GridView gridViewFunction;
    @BindView(R.id.view_system_message)
    MineSystemView viewSystemMessage;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.ll_login_out)
    LinearLayout llLoginOut;
    @BindView(R.id.view_system_loing_out)
    MineSystemView viewSystemLoingOut;
    @BindView(R.id.view_line)
    View viewLine;

    private String face;
    private MineUserFunctionAdapter adapter;
    private CommonDialog commonDialog;
    private String parentType = "0";//类型 0,头条 1,论坛

    @Override
    public int getViewId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void afterViews() {
        // 设置重复刷新
        isReload = true;

        adapter = new MineUserFunctionAdapter(this, MineUserFunction.getAllListData(getContext(), parentType));
        gridViewFunction.setAdapter(adapter);

        loadData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        new HeadlineImpl(new MarkSixNetCallBack<MineNewCenter>(this, MineNewCenter.class) {
            @Override
            public void onSuccess(MineNewCenter response, int id) {
                setData(response);
            }

            @Override
            public void onError(String msg, String code) {
                showError();
            }
        }.setNeedDialog(false)).newUserCenter();


    }

    private void setData(MineNewCenter response) {
        if (response != null) {
            String blance = response.getBlance();
            face = response.getFace();
            String gold = response.getGold();
            String nickname = response.getNickname();
            int level = response.getLevel();
            int item_num = response.getItem_num();
            int msg_num = response.getMsg_num();
            int like_num = response.getLike_num();
            String remark = response.getRemark();
            int look_num = response.getLook_num();
            int lookme_num = response.getLookme_num();


            GlideUtil.loadCircleImage(face, ivUserPhoto);
            SPUtil.setUserPhoto(getContext(), face);
            SPUtil.setNickName(getContext(), nickname);

            tvNickname.setText(CommonUtils.StringNotNull(nickname) ? CommonUtils.CommHandleText(nickname) : "");
            tvUserSignature.setText(CommonUtils.StringNotNull(remark) ? remark : "官方网址：无");

            //官方
            CommonUtils.setUserLevel(ivOfficial, level);

            //消息通知
            viewSystemMessage.setTvMessageCount(msg_num);

            tvInvitationNum.setText(item_num > 0 ? CommonUtils.getThousandNumber(item_num) : "0");
            tvConcernNum.setText(look_num > 0 ? CommonUtils.getThousandNumber(look_num) : "0");
            tvFansNum.setText(lookme_num > 0 ? CommonUtils.getThousandNumber(lookme_num) : "0");
            tvPraiseNum.setText(like_num > 0 ? CommonUtils.getThousandNumber(like_num) : "0");


            //金币
            tvUserGold.setText(CommonUtils.StringNotNull(gold) ? gold : "0");

            //现金
            tvUserBalance.setText(CommonUtils.StringNotNull(blance) ? blance : "0");

            showContent();
        } else {
            showError();
        }
    }


    /**
     * 显示内容页
     */
    private void showContent() {
        viewLine.getLayoutParams().height = UIUtils.dip2px(getContext(), 10);
        llLogin.setVisibility(View.VISIBLE);
        viewSystemLoingOut.setVisibility(View.VISIBLE);
        llLoginOut.setVisibility(View.GONE);
        if (adapter.getCount() != 8) {
            adapter.changeDataUi(MineUserFunction.getAllListData(getContext(), parentType));
        }
        mLoadingLayout.showContent();
    }


    /**
     * 显示登录页
     */
    private void showEmpty() {
        viewLine.getLayoutParams().height = UIUtils.dip2px(getContext(), 6);
        llLogin.setVisibility(View.GONE);
        viewSystemLoingOut.setVisibility(View.GONE);
        llLoginOut.setVisibility(View.VISIBLE);
        //消息通知
        viewSystemMessage.setTvMessageCount(0);
        if (adapter.getCount() != 4) {
            adapter.changeDataUi(MineUserFunction.getListData(getContext(), 4, 8, parentType));
        }
        mLoadingLayout.showContent();
    }

    /**
     * 显示错误页
     */
    private void showError() {
        assert getContext() != null;
        mLoadingLayout.setErrorText(getContext().getResources().getString(R.string.no_network));
        mLoadingLayout.setErrorImage(R.drawable.no_network);
        mLoadingLayout.setRetryText(getContext().getResources().getString(R.string.reload_button));
        mLoadingLayout.setRetryListener(this);
        mLoadingLayout.showError();
    }


    /**
     * 重复加载
     */
    @Override
    public void onResume() {
        super.onResume();
        if (isVisible) {
            startData();
        }
    }

    @Override
    protected void loadData() {
        startData();
    }

    private void startData() {
        if (!isLogin()) {
            showEmpty();
        } else {
            getData();
        }
    }

    @Override
    public void onClick(View view) {
        if (!isLogin()) {
            goToLoginActivity();
        } else {
            mLoadingLayout.showLoading();
            getData();
        }
    }

    @OnClick({R.id.iv_goto_login, R.id.view_system_message, R.id.view_system_feedback
            , R.id.view_system_setting, R.id.rl_mine_setting
            , R.id.view_system_loing_out, R.id.rl_invitation, R.id.rl_concern, R.id.rl_fans
            , R.id.rl_praise, R.id.iv_user_photo, R.id.ll_user_gold, R.id.ll_user_balance})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_mine_setting://个人设置
                startClass(R.string.SettingActivity);
                break;
            case R.id.iv_goto_login://登录
                goToLoginActivity();
                break;
            case R.id.iv_user_photo://查看头像
                CommonUtils.checkImagePreview(getContext(), CommonUtils.getFaceUrls(face), 0);
                break;
            case R.id.rl_invitation://我的头条
                startClass(R.string.MineHeadlineActivity);
                break;
            case R.id.rl_concern://我的关注
                startClass(R.string.MineConcernAndFansActivity,
                        IntentUtils.getHashObj(new String[]{StringConstants.TYPE, "0", StringConstants.PARENT_TYPE, "0"}));
                break;
            case R.id.rl_fans://我的粉丝
                startClass(R.string.MineConcernAndFansActivity,
                        IntentUtils.getHashObj(new String[]{StringConstants.TYPE, "1", StringConstants.PARENT_TYPE, "0"}));
                break;
            case R.id.rl_praise://获赞
                break;
            case R.id.ll_user_gold://我的金币
                startClass(R.string.IncomeRecordActivity, IntentUtils.getHashObj(new String[]{StringConstants.TYPE, "0"}));
                break;
            case R.id.ll_user_balance://我的现金
                startClass(R.string.IncomeRecordActivity, IntentUtils.getHashObj(new String[]{StringConstants.TYPE, "1"}));
                break;
            case R.id.view_system_message://消息通知
                if (checkLogin(R.string.MessageNotificationActivity)) {
                    startClass(R.string.MessageNotificationActivity);
                }
                break;
            case R.id.view_system_feedback://用户反馈
                if (checkLogin(R.string.FeedbackActivity)) {
                    startClass(R.string.FeedbackActivity);
                }
                break;
            case R.id.view_system_setting://个人设置
                if (checkLogin(R.string.SettingActivity)) {
                    startClass(R.string.SettingActivity);
                }
                break;
            case R.id.view_system_loing_out://退出登录
                if (commonDialog == null) {
                    commonDialog = new CommonDialog(this);
                }
                commonDialog.show("退出确认", "退出当前头条帐号，将不能同步收藏，发布评论和分享等",
                        "取消", "退出确认", null, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MobclickAgent.onProfileSignOff();
                                EventBus.getDefault().post(new TokenTimeOutEvent(false, ""));
                                showEmpty();
                                if (commonDialog != null && commonDialog.isShowing()) {
                                    commonDialog.dismiss();
                                }
                            }
                        });
                break;
        }
    }
}
