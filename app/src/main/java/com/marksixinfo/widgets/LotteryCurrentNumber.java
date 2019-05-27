package com.marksixinfo.widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.ViewBase;
import com.marksixinfo.bean.LotteryBaseData;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.utils.CommonUtils;

/**
 * 开奖页头部当期开奖
 *
 * @Auther: Administrator
 * @Date: 2019/5/11 0011 14:00
 * @Description:
 */
public class LotteryCurrentNumber extends ViewBase implements View.OnClickListener {


    private View view_v1;
    private View view_v2;
    private View view_v3;
    private View view_v4;
    private View view_v5;
    private View view_v6;
    private View view_v7;
    private View card_scratch;
    private boolean isScratch = true;
    private LotteryBaseData.LotteryBean.VBean v7;
    private SucceedCallBackListener<Boolean> listener;

    public LotteryCurrentNumber(Context context) {
        super(context);
    }

    public LotteryCurrentNumber(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LotteryCurrentNumber(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getViewId() {
        return R.layout.view_lottery_current_number;
    }

    @Override
    public void afterViews(View v) {
        view_v1 = v.findViewById(R.id.view_v1);
        view_v2 = v.findViewById(R.id.view_v2);
        view_v3 = v.findViewById(R.id.view_v3);
        view_v4 = v.findViewById(R.id.view_v4);
        view_v5 = v.findViewById(R.id.view_v5);
        view_v6 = v.findViewById(R.id.view_v6);
        view_v7 = v.findViewById(R.id.view_v7);
        card_scratch = v.findViewById(R.id.card_scratch);

        card_scratch.setOnClickListener(this);
    }

    /**
     * 设置当期开奖数据
     *
     * @param data
     */
    public void setData(LotteryBaseData.LotteryBean data, SucceedCallBackListener<Boolean> listener) {
        this.listener = listener;
        if (data != null) {
            v7 = data.getV7();
            setLotteryData(view_v1, data.getV1());
            setLotteryData(view_v2, data.getV2());
            setLotteryData(view_v3, data.getV3());
            setLotteryData(view_v4, data.getV4());
            setLotteryData(view_v5, data.getV5());
            setLotteryData(view_v6, data.getV6());
            setLotteryData(view_v7, v7);
            setScratch(true);
        }
    }


    /**
     * 设置单个开奖信息
     *
     * @param view_v
     * @param v
     */
    private void setLotteryData(View view_v, LotteryBaseData.LotteryBean.VBean v) {
        View view_cart = view_v.findViewById(R.id.view_cart);
        if (v != null) {
            view_cart.setVisibility(GONE);
            String num = v.getNum();
            String shengxiao = v.getShengxiao();
            String style = v.getStyle();
            if (CommonUtils.StringNotNull(style)) {
                View view = view_v.findViewById(R.id.ll_background);
                view.setBackgroundColor(Color.parseColor(style));
            }
            if (CommonUtils.StringNotNull(num)) {
                TextView tv_number = view_v.findViewById(R.id.tv_number);
                tv_number.setText(num);
            }
            if (CommonUtils.StringNotNull(shengxiao)) {
                TextView tv_sheng_xiao = view_v.findViewById(R.id.tv_sheng_xiao);
                tv_sheng_xiao.setText(shengxiao);
            }
        } else {
            view_cart.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v7 != null) {
            ScratchDialog scratchDialog = new ScratchDialog(this);
            scratchDialog.showDialog(v7, new SucceedCallBackListener<Boolean>() {
                @Override
                public void succeedCallBack(Boolean o) {
                    if (listener != null) {
                        listener.succeedCallBack(true);
                    }
                }
            });
        }
    }

    public boolean isScratch() {
        return isScratch;
    }

    public void setScratch(boolean scratch) {
        isScratch = scratch;
        if (isScratch) {
            card_scratch.setVisibility(VISIBLE);
            view_v7.setVisibility(GONE);
        } else {
            card_scratch.setVisibility(GONE);
            view_v7.setVisibility(VISIBLE);
        }
    }
}
