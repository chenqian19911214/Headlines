package com.marksixinfo.widgets;

import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.base.DialogBase;
import com.marksixinfo.interfaces.ActivityIntentInterface;

/**
 * 开奖提醒弹框
 *
 * @Auther: Administrator
 * @Date: 2019/5/18 0018 19:36
 * @Description:
 */
public class LotteryRemindDialog extends DialogBase implements View.OnClickListener {

    public LotteryRemindDialog(ActivityIntentInterface context) {
        super(context, R.style.SignInSuccessDialog);
    }


    @Override
    public void initView() {
        setView(R.layout.dialog_remind_lottery);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        findViewById(R.id.iv_close).setOnClickListener(this);
        findViewById(R.id.iv_click).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_click:
                getCtrl().gotoMainActivity(2);
                break;
        }
        if (isShowing()) {
            dismiss();
        }
    }
}
