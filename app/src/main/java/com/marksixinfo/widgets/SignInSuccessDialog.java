package com.marksixinfo.widgets;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.DialogBase;
import com.marksixinfo.bean.TaskCenterData;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.utils.CommonUtils;

import java.util.List;

/**
 * 签到成功dialog
 *
 * @Auther: Administrator
 * @Date: 2019/4/20 0020 17:09
 * @Description:
 */
public class SignInSuccessDialog extends DialogBase implements View.OnClickListener {

    public SignInSuccessDialog(ActivityIntentInterface context) {
        super(context, R.style.SignInSuccessDialog);
    }

    private TextView tvSiginTotal;
    private SiginDialogCurrentView siginCurrentView;
    private ImageView ivSiginCurrentDay;
    private int index = 0;


    @Override
    public void initView() {
        setView(R.layout.dialog_signin_success);
        setCancelable(false);
        setCanceledOnTouchOutside(false);

        findViewById(R.id.iv_close).setOnClickListener(this);
        tvSiginTotal = findViewById(R.id.tv_sigin_total);
        siginCurrentView = findViewById(R.id.siginCurrentView);
        ivSiginCurrentDay = findViewById(R.id.iv_sigin_current_day);
        Drawable bgDrawable = OneDrawable.createBgDrawableWithDarkMode(getContext(), R.drawable.icon_sigin_button);
        ivSiginCurrentDay.setImageDrawable(bgDrawable);
        ivSiginCurrentDay.setOnClickListener(this);

    }


    public void setData(List<TaskCenterData.CheckinBean> response) {
        if (CommonUtils.ListNotNull(response)) {
            boolean isToday = false;
            for (TaskCenterData.CheckinBean bean : response) {
                if (bean != null) {
                    int status = bean.getStatus();//签到状态 1为已签
                    int today = bean.getToday();//1,是今天
                    if (status == 1) {//已签
                        index++;
                    }
                    if (today == 1) {//今天是否已签到
                        isToday = status == 1;
                    }
                }
            }
            tvSiginTotal.setText("已连续签到" + index + "天，签到奖励金币");
            tvSiginTotal.postDelayed(new Runnable() {
                @Override
                public void run() {
                    siginCurrentView.setData(response);
                }
            }, 300);

            if (!isToday) {
                ivSiginCurrentDay.setEnabled(true);
            } else {//今天已签到
                ivSiginCurrentDay.setEnabled(false);
            }
            super.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                if (isShowing()) {
                    dismiss();
                }
                break;
            case R.id.iv_sigin_current_day://签到
                getCtrl().startClass(R.string.TaskCenterActivity);
                dismiss();
//                ivSiginCurrentDay.setEnabled(false);
//                siginCurrentView.signIn(new SucceedCallBackListener<Boolean>() {
//                    @Override
//                    public void succeedCallBack(Boolean o) {
//                        if (o) {
//                            tvSiginTotal.setText("已连续签到" + ++index + "天，签到奖励金币");
//                            if (siginCurrentView != null) {
//                                siginCurrentView.postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (isShowing()) {
//                                            dismiss();
//                                        }
//                                    }
//                                }, 1500);//加上500毫秒动画
//                            }
//                        } else {
//                            ivSiginCurrentDay.setEnabled(true);
//                        }
//                    }
//                });
                break;
        }
    }
}
