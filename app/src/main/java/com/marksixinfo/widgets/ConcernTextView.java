package com.marksixinfo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.ViewBase;
import com.marksixinfo.utils.CommonUtils;

/**
 * 关注按钮
 *
 * @Auther: Administrator
 * @Date: 2019/5/2 0002 12:03
 * @Description:
 */
public class ConcernTextView extends ViewBase {


    private TextView tvConcern;
    private ProgressBar pbProgressBar;
    private ProgressBar pbProgressBarWhite;
    private int status;
    private int look_status;

    public ConcernTextView(Context context) {
        super(context);
    }

    public ConcernTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ConcernTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getViewId() {
        return R.layout.view_concern;
    }

    @Override
    public void afterViews(View view) {
        tvConcern = view.findViewById(R.id.tv_concern);
        pbProgressBar = view.findViewById(R.id.pb_progress_bar);
        pbProgressBarWhite = view.findViewById(R.id.pb_progress_bar_white);
    }

    /**
     * 开始
     *
     * @return
     */
    public boolean startLookStatus() {
        return setLookStatus(-1);
    }


    /**
     * 是否关注，0:未关注;1:已关注 2:自己 -1:调接口中
     *
     * @param look_status
     */
    public boolean setLookStatus(int look_status) {
        if (look_status == 2) {
            this.setVisibility(INVISIBLE);
            return false;
        }
        this.setVisibility(VISIBLE);
        if (look_status == -1) {
            if (!isLogin()) {
                toast("请登录", -1);
                return false;
            }
            pbProgressBar.setVisibility(status == 0 ? GONE : VISIBLE);
            pbProgressBarWhite.setVisibility(status == 0 ? VISIBLE : GONE);
        } else {
            this.status = look_status;
            pbProgressBar.setVisibility(GONE);
            pbProgressBarWhite.setVisibility(GONE);
        }
        this.look_status = look_status;
        CommonUtils.setLookStatus(tvConcern, look_status);
        return true;
    }

    public int getStatus() {
        return status;
    }

    public int getLook_status() {
        return look_status;
    }
}
