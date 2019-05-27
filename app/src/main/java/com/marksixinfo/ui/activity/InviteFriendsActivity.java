package com.marksixinfo.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.IntentUtils;
import com.marksixinfo.base.MarkSixNetCallBack;
import com.marksixinfo.base.MarkSixWhiteActivity;
import com.marksixinfo.constants.StringConstants;
import com.marksixinfo.constants.UrlStaticConstants;
import com.marksixinfo.net.impl.HeadlineImpl;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.ReleaseUtils;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.umengshare.ShareParams;
import com.marksixinfo.widgets.umengshare.UMShareDialog;
import com.marksixinfo.widgets.umengshare.UMengShareUtils;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 功能描述: 邀请好友
 *
 * @param:
 * @return:
 * @auther: Administrator
 * @date: 2019/4/19 0019 15:05
 */
public class InviteFriendsActivity extends MarkSixWhiteActivity implements View.OnClickListener {

    @BindView(R.id.tv_my_invitation_code)
    TextView tvMyInvitationCode;
    @BindView(R.id.iv_background)
    ImageView ivBackground;
    @BindView(R.id.iv_background_fix)
    ImageView ivBackgroundFix;
    @BindView(R.id.ll_bottom_content1)
    LinearLayout llBottomContent1;
    @BindView(R.id.tv_immediately_invited)
    TextView tvImmediatelyInvited;
    private UMShareDialog umShareDialog;
    private ShareParams shareParams;
    private ReleaseUtils releaseUtils;
    private String invitationCode;
    private Bitmap mBitmap;


    @Override
    public int getViewId() {
        return R.layout.activity_invite_friends;
    }

    @Override
    public void afterViews() {

        markSixTitleWhite.init("邀请好友", "", "", R.drawable.icon_explain_title_right, this);

        invitationCode = SPUtil.getInvitationCode(this);

        tvMyInvitationCode.setText(CommonUtils.StringNotNull(invitationCode) ? "我的邀请码：" + invitationCode : "");
//        tvMyInvitationCode.setText("我的邀请码：C9ESYRMA");

        setAutoScale();
    }

    /**
     * 设置自适应缩放
     */
    private void setAutoScale() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_invite_friends_bg);
        int maxWidth = UIUtils.getScreenWidth(this);
        int width = mBitmap.getWidth();
        int height = mBitmap.getHeight();
        if (width != 0 && mBitmap != null) {
            float scale = maxWidth * 1.0f / width * 1.0f;
            float imageHeight = height * 1.0f * scale;
            ViewGroup.LayoutParams lp = ivBackground.getLayoutParams();
            lp.width = maxWidth;
            lp.height = (int) imageHeight;
            ivBackground.setLayoutParams(lp);
            ivBackground.setImageBitmap(mBitmap);
        }
        //全面屏位置修改
        ivBackgroundFix.post(new Runnable() {
            @Override
            public void run() {
                int viewHeight = UIUtils.px2dip(getContext(), ivBackgroundFix.getMeasuredHeight());
                if (viewHeight > 0) {
                    UIUtils.setMargins(getActivity(), llBottomContent1, 0, 0, 0, 105 + (viewHeight / 2));
                    UIUtils.setMargins(getActivity(), tvImmediatelyInvited, 0, 0, 0, 20 + (viewHeight / 2));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_function://说明
                startClass(R.string.WebViewActivity, IntentUtils.getHashObj(new String[]{
                        StringConstants.URL, UrlStaticConstants.INVITE_FRIENDS_EXPLAIN}));
                break;
        }
    }

    @OnClick({R.id.tv_copy_invitation_code, R.id.tv_immediately_invited})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_copy_invitation_code://复制
                if (CommonUtils.StringNotNull(invitationCode)) {
                    CommonUtils.copyText(this, invitationCode);
                }
                break;
            case R.id.tv_immediately_invited://立即邀请
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
                break;
        }
    }


    public ShareParams getShareParams() {
        String url = UrlStaticConstants.INVITE_FRIENDS + (CommonUtils.StringNotNull(invitationCode) ? invitationCode : "");
        if (shareParams == null) {
            shareParams = new ShareParams(" ", " ", UMengShareUtils.getImageIcon(this, "")
                    , url, new UMengShareUtils.MyShareListener(getContext()) {

                @Override
                public void onResult(SHARE_MEDIA platform) {
                    super.onResult(platform);
                    taskShare();
                }
            });
        } else {
//            shareParams.setContent(getSecondTitle());//重设副标题
            shareParams.setUmImage(UMengShareUtils.getImageIcon(this, ""));//重设icon

            shareParams.setUrl(url);
        }

        return shareParams;
    }


    /**
     * 分享任务
     */
    private void taskShare() {
        new HeadlineImpl(new MarkSixNetCallBack(this, Object.class) {
            @Override
            public void onSuccess(Object response, int id) {

            }
        }).taskShare();
    }
}
