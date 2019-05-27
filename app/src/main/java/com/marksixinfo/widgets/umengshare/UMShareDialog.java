package com.marksixinfo.widgets.umengshare;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.marksixinfo.R;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * @Auther: Administrator
 * @Date: 2019/3/23 0023 13:54
 * @Description:
 */
public class UMShareDialog extends Dialog {

    private TextView share_wechat_view, share_friends_view, share_qq_view, custom_share_cancel_view, share_copy_view;

    protected static int defaultStyle = R.style.BottomDialog;
    private Activity mContext;

    public UMShareDialog(Activity mContext) {
        this(mContext, defaultStyle);
    }

    public UMShareDialog(Activity mContext, int theme) {
        super(mContext, theme);
        this.mContext = mContext;
        init();
        initView();
    }

    public UMShareDialog(Activity mContext, SHARE_MEDIA shareMedia) {
        this(mContext);
        showDiffPerformShare(shareMedia);
    }

    public void initView() {
        setContentView(R.layout.custom_share_layout);
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        share_wechat_view = findViewById(R.id.custom_share_wechat_view);
        share_friends_view = findViewById(R.id.custom_share_friends_view);
        share_qq_view = findViewById(R.id.custom_share_qq_view);
        share_copy_view = findViewById(R.id.custom_share_copy_view);
        custom_share_cancel_view = findViewById(R.id.custom_share_cancel_view);
    }

    protected void init() {
        Window window = getWindow();
        //设置无边距
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //设置dialog的位置在底部
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    /**
     * 单独显示不同的平台分享
     */
    public void showDiffPerformShare(SHARE_MEDIA performShare) {
        if (performShare == null) {
            setWChatVisible(View.VISIBLE);
            setQQVisible(View.VISIBLE);
            setSinaVisible(View.VISIBLE);
            setFriendVisible(View.VISIBLE);
        } else {
            if (performShare == SHARE_MEDIA.WEIXIN) {
                setWChatVisible(View.VISIBLE);
                setQQVisible(View.GONE);
                setSinaVisible(View.GONE);
                setFriendVisible(View.GONE);
            } else if (performShare == SHARE_MEDIA.QQ) {
                setWChatVisible(View.GONE);
                setQQVisible(View.VISIBLE);
                setSinaVisible(View.GONE);
                setFriendVisible(View.GONE);
            } else if (performShare == SHARE_MEDIA.SINA) {
                setWChatVisible(View.GONE);
                setQQVisible(View.GONE);
                setSinaVisible(View.VISIBLE);
                setFriendVisible(View.GONE);
            } else if (performShare == SHARE_MEDIA.WEIXIN_CIRCLE) {
                setWChatVisible(View.GONE);
                setQQVisible(View.GONE);
                setSinaVisible(View.GONE);
                setFriendVisible(View.VISIBLE);
            }
        }
    }


    public void setOnListener(View.OnClickListener onClickListener) {
        share_wechat_view.setOnClickListener(onClickListener);
        share_friends_view.setOnClickListener(onClickListener);
        share_qq_view.setOnClickListener(onClickListener);
        share_copy_view.setOnClickListener(onClickListener);
        custom_share_cancel_view.setOnClickListener(onClickListener);
    }

    public void setWChatVisible(int visible) {
        if (share_wechat_view != null) {
            share_wechat_view.setVisibility(visible);
        }
    }

    public void setQQVisible(int visible) {
        if (share_qq_view != null) {
            share_qq_view.setVisibility(visible);
        }
    }

    public void setSinaVisible(int visible) {
        if (share_copy_view != null) {
            share_copy_view.setVisibility(visible);
        }
    }

    public void setFriendVisible(int visible) {
        if (share_friends_view != null) {
            share_friends_view.setVisibility(visible);
        }
    }


    public void showDialog() {
        if (mContext != null && !mContext.isFinishing()) {
            super.show();
        }
    }
}