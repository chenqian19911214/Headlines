package com.marksixinfo.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.bean.LotteryBaseData;
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
public class ScratchDialog extends Dialog implements View.OnClickListener {


    private RelativeLayout rlBackground;
    private TextView rlNumber;
    private TextView tvShengXiao;
    private ScratchView scratchView;
    private SucceedCallBackListener<Boolean> listener;

    public ScratchDialog(Context context) {
        super(context, R.style.ScratchDialog);
        init(context);
    }

    public ScratchDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public ScratchDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

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
            super.show();
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

            scratchView.setEraseStatusListener(new ScratchView.EraseStatusListener() {
                boolean isClear = false;

                @Override
                public void onProgress(int percent) {
                    LogUtils.d(percent + "");
                    if (percent >= 65 && !isClear) {
                        isClear = true;
                        scratchView.clear();
                        scratchView.postDelayed(new Runnable() {
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
    }

    public void init(Context context) {
//        setView();
        View view = View.inflate(context, R.layout.dialog_scratch_view, null);
        setContentView(view);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        rlBackground = view.findViewById(R.id.rl_background);
        view.findViewById(R.id.iv_close).setOnClickListener(this);
        rlNumber = view.findViewById(R.id.rl_number);
        tvShengXiao = view.findViewById(R.id.tv_sheng_xiao);
        scratchView = view.findViewById(R.id.scratchView);
    }

    @Override
    public void onClick(View v) {
        if (isShowing()) {
            dismiss();
        }
    }
}
