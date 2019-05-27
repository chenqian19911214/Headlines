package com.marksixinfo.ui.activity;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.adapter.MineUserFunctionAdapter;
import com.marksixinfo.adapter.TaskCenterAdapter;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.bean.MineUserFunction;
import com.marksixinfo.bean.TaskCenterData;
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.constants.UrlStaticConstants;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.NumberUtils;
import com.marksixinfo.utils.ReleaseUtils;
import com.marksixinfo.widgets.CommonInputDialog;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.MyGridView;
import com.marksixinfo.widgets.NumberAnimTextView;
import com.marksixinfo.widgets.TaskCenterSignView;
import com.marksixinfo.widgets.umengshare.ShareParams;
import com.marksixinfo.widgets.umengshare.UMShareDialog;
import com.marksixinfo.widgets.umengshare.UMengShareUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能描述: 任务中心
 *
 * @auther: Administrator
 * @date: 2019/4/18 0018 21:41
 */
public class TaskCenterActivity extends MarkSixWhiteActivity implements
        View.OnClickListener, SucceedCallBackListener<TaskCenterData.TaskBean> {


    @BindView(R.id.tv_cash_reserve)
    NumberAnimTextView tvCashReserve;
    @BindView(R.id.tv_task_of_gold)
    NumberAnimTextView tvTaskOfGold;
    @BindView(R.id.tv_Invitation_code)
    TextView tvInvitationCode;
    @BindView(R.id.gridView_function)
    MyGridView gridViewFunction;
    @BindView(R.id.view_SignView)
    TaskCenterSignView viewSignView;
    @BindView(R.id.new_recyclerView)
    ListView newRecyclerView;
    @BindView(R.id.everyday_recyclerView)
    ListView everydayRecyclerView;
    @BindView(R.id.loading)
    LoadingLayout mLoadingLayout;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    private TaskCenterData data;
    private String invitationCode;
    private TaskCenterAdapter newAdapter;
    private TaskCenterAdapter everydayAdapter;
    private CommonInputDialog commonInputDialog;
    private UMShareDialog umShareDialog;
    private ShareParams shareParams;
    private ReleaseUtils releaseUtils;
    private String gold = "";
    private int exchange_ratio = 0;//金币兑换比例
    private String parentType = "0";//类型 0,头条 1,论坛
    private String blance = "";
    private int shareStatus;//分享状态  初始0,点击分享渠道1,不可见2,再次可见3,调用分享接口

    @Override
    public int getViewId() {
        return R.layout.activity_task_center;
    }

    @Override
    public void afterViews() {

        markSixTitleWhite.getTvFunction().setTextColor(getResources().getColor(R.color.colorPrimary));
        markSixTitleWhite.init("任务中心", "", "", R.drawable.icon_explain_title_right, this);

        mLoadingLayout.setRetryListener(this);
        refreshLayout.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
        setFunction();
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refresh(true);
            }
        });
    }


    /**
     * 开始加载
     */
    private void startData() {
        refresh(true);
    }

    @Override
    public void finish() {
        if (tvCashReserve != null && tvCashReserve.getAnimator() != null) {
            tvCashReserve.getAnimator().cancel();
        }
        if (tvTaskOfGold != null && tvTaskOfGold.getAnimator() != null) {
            tvTaskOfGold.getAnimator().cancel();
        }
        super.finish();
    }

    /**
     * 加载
     */
    private void refresh(boolean isRefresh) {
        new HeadlineImpl(new MarkSixNetCallBack<TaskCenterData>(this, TaskCenterData.class) {
            @Override
            public void onSuccess(TaskCenterData response, int id) {
                data = response;
                setData(isRefresh);
            }

            @Override
            public void onError(String msg, String code) {
                if (data == null) {
                    mLoadingLayout.showError();
                } else {
                    super.onError(msg, code);
                }
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                loadDone();
            }
        }.setNeedDialog(false)).getTaskCenter();
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

    /**
     * 设置数据
     *
     * @param isRefresh
     */
    private void setData(boolean isRefresh) {
        if (data != null) {
            setInfo(data.getInfo(), isRefresh);
            setCheckin(data.getCheckin());
            setTaskList(data.getTask());
            mLoadingLayout.showContent();
        } else {
            mLoadingLayout.showEmpty();
        }

    }

    /**
     * 设置任务列表
     *
     * @param task //任务中心 [0] 新手任务 [1] 日常任务
     */
    private void setTaskList(List<List<TaskCenterData.TaskBean>> task) {
        if (CommonUtils.ListNotNull(task)) {
            List<TaskCenterData.TaskBean> taskBeans = task.get(0);
            setNewTaskList(taskBeans);
            if (task.size() >= 2) {
                List<TaskCenterData.TaskBean> taskBeans2 = task.get(1);
                setEverydayTaskList(taskBeans2);
            }
        }
    }


    /**
     * 设置新手任务
     *
     * @param taskBeans
     */
    private void setNewTaskList(List<TaskCenterData.TaskBean> taskBeans) {
        if (CommonUtils.ListNotNull(taskBeans)) {
            if (newAdapter == null) {
                newAdapter = new TaskCenterAdapter(this, taskBeans, this);
                newRecyclerView.setAdapter(newAdapter);
            } else {
                newAdapter.changeDataUi(taskBeans);
            }
        }
    }

    /**
     * 设置日常任务
     *
     * @param taskBeans
     */
    private void setEverydayTaskList(List<TaskCenterData.TaskBean> taskBeans) {
        if (CommonUtils.ListNotNull(taskBeans)) {
            if (everydayAdapter == null) {
                everydayAdapter = new TaskCenterAdapter(this, taskBeans, this);
                everydayRecyclerView.setAdapter(everydayAdapter);
            } else {
                everydayAdapter.changeDataUi(taskBeans);
            }
        }
    }


    /**
     * 设置签到
     *
     * @param checkin
     */
    private void setCheckin(List<TaskCenterData.CheckinBean> checkin) {
        if (CommonUtils.ListNotNull(checkin)) {
            viewSignView.setData(checkin, new SucceedCallBackListener() {
                @Override
                public void succeedCallBack(Object o) {
                    signIn();
                }
            });
        }
    }

    /**
     * 签到
     */
    private void signIn() {
        new HeadlineImpl(new MarkSixNetCallBack<String>(this, String.class) {
            @Override
            public void onSuccess(String response, int id) {
                refresh(false);
            }
        }).startSignIn();
    }


    /**
     * 设置Detail
     *
     * @param info
     * @param isRefresh
     */
    private void setInfo(TaskCenterData.InfoBean info, boolean isRefresh) {
        if (info != null) {
            exchange_ratio = info.getExchange_Ratio();
            if (!blance.equals(info.getBlance())) {
                blance = info.getBlance();
                tvCashReserve.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvCashReserve.setHadThousands(false);
                        tvCashReserve.setFractionDigits(2);
                        tvCashReserve.setNumberString(CommonUtils.StringNotNull(blance) ? blance : "0");
                    }
                }, isRefresh ? NumberConstants.ANIM_TEXT_VIEW_TIME : 0);
            }

            if (!gold.equals(info.getGold())) {
                gold = info.getGold();
                tvTaskOfGold.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tvTaskOfGold.setHadThousands(false);
                        tvTaskOfGold.setNumberString(CommonUtils.StringNotNull(gold) ? gold : "0");
                    }
                }, isRefresh ? NumberConstants.ANIM_TEXT_VIEW_TIME : 0);
            }
            invitationCode = info.getUid();
            tvInvitationCode.setText(CommonUtils.StringNotNull(invitationCode) ? "点击复制邀请码：" + invitationCode : "");
        }
    }


    /**
     * 功能gridview
     */
    private void setFunction() {
        gridViewFunction.setAdapter(new MineUserFunctionAdapter(this
                , MineUserFunction.getListData(getContext(), 0, 4, parentType)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_function://说明
                startClass(R.string.WebViewActivity, IntentUtils.getHashObj(new String[]{
                        StringConstants.URL, UrlStaticConstants.TASK_EXPLAIN}));
                break;
            case R.id.retry_button://网络错误
                mLoadingLayout.showLoading();
                startData();
                break;
        }
    }

    @OnClick({R.id.tv_withdraw_deposit, R.id.tv_conversion, R.id.tv_Invitation_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_withdraw_deposit://提现
                startClass(R.string.WithdrawDepositActivity);
                break;
            case R.id.tv_conversion://兑换
                if (!CommonUtils.StringNotNull(gold) || "0".equals(gold)) {
                    toast("没有金币可以兑换,快去做任务赚取金币吧");
                    return;
                }
                if (NumberUtils.stringToInt(gold) < exchange_ratio) {
                    toast("当前最小兑换金币个数为" + exchange_ratio + "个");
                    return;
                }

                int goldNumber = NumberUtils.stringToInt(gold);
                CommonInputDialog commonInputDialog = new CommonInputDialog(this);
                EditText editText = commonInputDialog.getEditText();
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        //限制最大输入
                        int i = NumberUtils.stringToInt(editText.getText().toString().trim());
                        if (i >= goldNumber) {
                            i = goldNumber;
                            editText.removeTextChangedListener(this);
                            editText.setText(i > 0 ? String.valueOf(i) : "");
                            if (i > 0) {
                                editText.setSelection(String.valueOf(i).length());
                            }
                            editText.addTextChangedListener(this);
                        }
                    }
                });
                commonInputDialog.show("任务金币" + exchange_ratio + "：1兑换现金余额",
                        "请输入兑换金币数量", "取消", "确定", null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String inputContent = commonInputDialog.getInputContent();
                        if (NumberUtils.stringToInt(inputContent) < exchange_ratio) {
                            toast("当前最小兑换金币个数为" + exchange_ratio + "个");
                            return;
                        }
                        if (commonInputDialog.isShowing()) {
                            commonInputDialog.dismiss();
                        }
                        conversionGold(inputContent);
                    }
                });
                break;
            case R.id.tv_Invitation_code://复制邀请码
                CommonUtils.copyText(this, invitationCode);
                break;
        }
    }

    /**
     * 兑换金币
     *
     * @param inputContent
     */
    private void conversionGold(String inputContent) {
        taskExchange(inputContent);
    }

    /**
     * 兑换金币
     */
    private void taskExchange(String gold) {
        new HeadlineImpl(new MarkSixNetCallBack<Object>(this, Object.class) {
            @Override
            public void onSuccess(Object response, int id) {
                toast("兑换成功");
                refresh(false);
            }
        }).taskExchange(gold);
    }

    /**
     * 领取现金操作
     *
     * @param o
     */
    @Override
    public void succeedCallBack(TaskCenterData.TaskBean o) {
        if (o != null) {
            gotoGold(o.getIcon());
        }
    }

    /**
     * 点击领取操作
     *
     * @param type
     */
    private void gotoGold(int type) {
        switch (type) {
            case 1://首次邀请好友
                startClass(R.string.InviteFriendsActivity);
                break;
            case 2://输入邀请码
                showInputDiaolg();
                break;
            case 3://我的点赞
//                startClass(R.string.MineConcernAndHistoryActivity, IntentUtils.getHashObj(new String[]{NetUrlHeadline.TYPE, "2"}));
//                break;
            case 4://优质评论
                gotoMainActivity(0);
//                startClass(R.string.MineConcernAndHistoryActivity, IntentUtils.getHashObj(new String[]{NetUrlHeadline.TYPE, "1"}));
                break;
            case 5://首次分享
                //分享
                showShareDialog();
                break;
            case 6://发布头条
                startClass(R.string.ReleaseActivity);
                break;
            case 7://再次邀请好友
                startClass(R.string.InviteFriendsActivity);
                break;
            default:
                break;
        }
    }


    /**
     * 分享弹框
     */
    private void showShareDialog() {
        if (umShareDialog == null) {
            umShareDialog = new UMShareDialog(this);
        }
        if (shareStatus != 0) {
            shareStatus = 0;
        }
        ShareParams shareParams = getShareParams();
        UMengShareUtils.getInstance()
                .doCustomShare(this, shareParams.getUrl(),
                        shareParams.getTitle(), shareParams.getContent(), shareParams.getUmImage(),
                        shareParams.getListener(), umShareDialog, new SucceedCallBackListener<Integer>() {
                            @Override
                            public void succeedCallBack(Integer o) {
                                shareStatus++;
                            }
                        });
        if (releaseUtils == null) {
            releaseUtils = new ReleaseUtils();
        }
        releaseUtils.setPermission(this, null);
    }

    private void showInputDiaolg() {
        if (commonInputDialog == null) {
            commonInputDialog = new CommonInputDialog(this);
        }
        commonInputDialog.getEditText().setInputType(InputType.TYPE_CLASS_TEXT);
        commonInputDialog.show("输入邀请码", "请输入邀请码", "取消", "确定",
                null, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String inputContent = commonInputDialog.getInputContent();
                        if (!CommonUtils.StringNotNull(inputContent)) {
                            toast("请输入邀请码");
                            return;
                        }
                        if (commonInputDialog != null && commonInputDialog.isShowing()) {
                            commonInputDialog.dismiss();
                        }
                        invitationCode(inputContent);
                    }
                }).setTextViewInput("");
    }

    /**
     * 分享弹框
     *
     * @return
     */
    public ShareParams getShareParams() {
        String url = UrlStaticConstants.SHARE_URL;
        if (shareParams == null) {
            shareParams = new ShareParams(" ", " ", UMengShareUtils.getImageIcon(this, ""),
                    url, new UMengShareUtils.MyShareListener(getContext()));
        } else {
//            shareParams.setContent(getSecondTitle());//重设副标题
            shareParams.setUmImage(UMengShareUtils.getImageIcon(this, ""));//重设icon

            shareParams.setUrl(url);
        }

        return shareParams;
    }


    /**
     * 输入邀请码
     *
     * @param inputContent
     */
    private void invitationCode(String inputContent) {
        new HeadlineImpl(new MarkSixNetCallBack<Object>(this, Object.class) {
            @Override
            public void onSuccess(Object response, int id) {
                toast("设置成功");
                refresh(false);
            }
        }).setInvitationCode(inputContent);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (shareStatus == 2) {
            shareStatus++;
        }
        if (shareStatus == 3) {
            shareStatus = 0;
            taskShare();
        } else {
            startData();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (shareStatus == 1) {
            shareStatus++;
        }
    }


    /**
     * 分享任务
     */
    private void taskShare() {
        new HeadlineImpl(new MarkSixNetCallBack(this, Object.class) {
            @Override
            public void onSuccess(Object response, int id) {
                toast("分享成功");
            }

            @Override
            public void onAfter(int id) {
                super.onAfter(id);
                startData();
            }
        }.setNeedDialog(false).setNeedToast(false)).taskShare();
    }
}
