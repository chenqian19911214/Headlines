package com.marksixinfo.widgets;

import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.DialogBase;
import com.marksixinfo.bean.LotteryBaseData;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.LogUtils;

/**
 * 开奖刮奖弹框
 *
 * @Auther: Administrator
 * @Date: 2019/5/11 0011 15:34
 * @Description:
 */
public class ScratchDialog extends DialogBase implements View.OnClickListener {


    public ScratchDialog(ActivityIntentInterface context) {
        this(context, R.style.ScratchDialog);
    }

    public ScratchDialog(ActivityIntentInterface context, int theme) {
        super(context, theme);
    }

    protected ScratchDialog(ActivityIntentInterface context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private RelativeLayout rlBackground;
    private TextView rlNumber;
    private TextView tvShengXiao;
    private ScratchView ScratchView;
    private SucceedCallBackListener<Boolean> listener;

    public void setListener(SucceedCallBackListener<Boolean> listener) {
        this.listener = listener;
    }


    /**
     * 显示刮奖弹框
     *
     * @param v
     */
    public void showDialog(LotteryBaseData.LotteryBean.VBean v, SucceedCallBackListener<Boolean> listener) {
        if (v != null) {
            this.listener = listener;
            String num = v.getNum();
            String shengxiao = v.getShengxiao();
            String style = v.getStyle();
            if (CommonUtils.StringNotNull(style)) {
                rlBackground.setBackgroundColor(Color.parseColor(style));
            }
            if (CommonUtils.StringNotNull(num)) {
                rlNumber.setText(num);
            }
            if (CommonUtils.StringNotNull(shengxiao)) {
                tvShengXiao.setText(shengxiao);
            }
            super.show();
        }
    }

    @Override
    public void initView() {
        setView(R.layout.dialog_scratch_view);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        rlBackground = findViewById(R.id.rl_background);
        findViewById(R.id.iv_close).setOnClickListener(this);
        rlNumber = findViewById(R.id.rl_number);
        tvShengXiao = findViewById(R.id.tv_sheng_xiao);
        ScratchView = findViewById(R.id.ScratchView);
        ScratchView.setCanERase(true);
        ScratchView.setEraseStatusListener(new ScratchView.EraseStatusListener() {
            boolean isClear = false;

            @Override
            public void onProgress(int percent) {
                LogUtils.d(percent + "");
                if (percent >= 65 && !isClear) {
                    isClear = true;
                    ScratchView.clear();
                    ScratchView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isClear = false;
                            if (listener != null) {
                                listener.succeedCallBack(true);
                            }
                            if (isShowing()) {
                                dismiss();
                            }
                        }
                    }, 1000);
                }
            }

            @Override
            public void onCompleted(View view) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (isShowing()) {
            dismiss();
        }
    }
}
