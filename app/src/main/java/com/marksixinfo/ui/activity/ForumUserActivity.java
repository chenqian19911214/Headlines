package com.marksixinfo.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.base.MarkSixActivity;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.bean.ForumUserCenterData;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.net.impl.ForumImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.LoadingLayout;
import com.marksixinfo.widgets.glide.GlideUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能描述: 论坛个人中心
 *
 * @auther: Administrator
 * @date: 2019/4/10 0010 13:57
 */
public class ForumUserActivity extends MarkSixActivity implements View.OnClickListener {

    @BindView(R.id.iv_photo)
    ImageView ivPhoto;
    @BindView(R.id.tv_nike_name)
    TextView tvNikeName;
    @BindView(R.id.iv_official)
    ImageView ivOfficial;
    @BindView(R.id.iv_expert)
    ImageView ivExpert;
    @BindView(R.id.tv_signature)
    TextView tvSignature;
    @BindView(R.id.tv_invitation_num)
    TextView tvInvitationNum;
    @BindView(R.id.tv_concern_num)
    TextView tvConcernNum;
    @BindView(R.id.tv_fans_num)
    TextView tvFansNum;
    @BindView(R.id.tv_praise_num)
    TextView tvPraiseNum;
    @BindView(R.id.tv_invitation_tag)
    TextView tvInvitationTag;
    @BindView(R.id.loading)
    LoadingLayout loading;

    private String face = "";

    @Override
    public int getViewId() {
        return R.layout.activity_forum_user;
    }

    @Override
    public void afterViews() {
        markSixTitle.init("论坛个人中心");
        tvInvitationTag.setText("帖子");
        loading.setRetryListener(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    /**
     * 获取数据
     */
    private void getData() {
        new ForumImpl(new MarkSixNetCallBack<ForumUserCenterData>(this, ForumUserCenterData.class) {
            @Override
            public void onSuccess(ForumUserCenterData response, int id) {
                if (response != null) {
                    setData(response);
                    loading.showContent();
                } else {
                    loading.showError();
                }
            }

            @Override
            public void onError(String msg, String code) {
                loading.showError();
            }
        }.setNeedDialog(false)).forumUserCenter();
    }


    /**
     * 设置数据
     *
     * @param response
     */
    private void setData(ForumUserCenterData response) {

        face = response.getFace();
        String nickname = response.getNickname();
        int item_num = response.getItem_num();
        int like_num = response.getLike_num();
        int look_num = response.getLook_num();
        int lookme_num = response.getLookme_num();
        int level = response.getLevel();
        String remark = response.getRemark();


        GlideUtil.loadCircleImage(face, ivPhoto);

        tvNikeName.setText(CommonUtils.StringNotNull(nickname) ? CommonUtils.CommHandleText2(nickname) : "");
        tvSignature.setText(CommonUtils.StringNotNull(remark) ? remark : "");
        tvSignature.setVisibility(CommonUtils.StringNotNull(remark) ? View.VISIBLE : View.GONE);

        //官方
        CommonUtils.setUserLevel(ivOfficial, 0);//论坛模块不显示

        tvInvitationNum.setText(item_num > 0 ? CommonUtils.getThousandNumber(item_num) : "0");
        tvConcernNum.setText(look_num > 0 ? CommonUtils.getThousandNumber(look_num) : "0");
        tvFansNum.setText(lookme_num > 0 ? CommonUtils.getThousandNumber(lookme_num) : "0");
        tvPraiseNum.setText(like_num > 0 ? CommonUtils.getThousandNumber(like_num) : "0");

    }


    @OnClick({R.id.iv_photo, R.id.rl_invitation, R.id.rl_concern, R.id.rl_fans, R.id.rl_praise, R.id.ll_my_favorites, R.id.ll_my_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_photo:
                CommonUtils.checkImagePreview(this, CommonUtils.getFaceUrls(face), 0);
                break;
            case R.id.rl_invitation://我的帖子
                startClass(R.string.MineInvitationActivity);
                break;
            case R.id.rl_concern://我的关注
//                startClass(R.string.MineConcernActivity, IntentUtils.getHashObj(new String[]{NetUrlHeadline.TYPE, "1"}));
                startClass(R.string.MineConcernAndFansActivity,
                        IntentUtils.getHashObj(new String[]{StringConstants.TYPE, "0", StringConstants.PARENT_TYPE, "1"}));
                break;
            case R.id.rl_fans://我的粉丝
                startClass(R.string.MineConcernAndFansActivity,
                        IntentUtils.getHashObj(new String[]{StringConstants.TYPE, "1", StringConstants.PARENT_TYPE, "1"}));
                break;
            case R.id.rl_praise:
//                toast("点赞");
                break;
            case R.id.ll_my_favorites://我的收藏
                startClass(R.string.MineCollectActivity, IntentUtils.getHashObj(new String[]{StringConstants.TYPE, "1"}));
                break;
            case R.id.ll_my_comment://我的回复
                startClass(R.string.MineCommentActivity, IntentUtils.getHashObj(new String[]{StringConstants.TYPE, "1"}));
                break;
        }
    }

    @Override
    public void onClick(View v) {
        loading.showLoading();
        getData();
    }
}
