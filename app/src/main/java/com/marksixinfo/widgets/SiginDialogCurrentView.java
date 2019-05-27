package com.marksixinfo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.ViewBase;
import com.marksixinfo.bean.TaskCenterData;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.Rotate3dAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * 签到弹框View
 *
 * @Auther: Administrator
 * @Date: 2019/5/23 0023 13:23
 * @Description:
 */
public class SiginDialogCurrentView extends ViewBase {

    public SiginDialogCurrentView(Context context) {
        super(context);
    }

    public SiginDialogCurrentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SiginDialogCurrentView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private List<FrameLayout> list;
    private String[] dates;
    private FrameLayout currentFlContent1;
    private FrameLayout currentFlContent2;

    @Override
    public int getViewId() {
        return R.layout.view_sigin_dialog_current;
    }

    @Override
    public void afterViews(View v) {
        list = new ArrayList<>();
        dates = new String[]{"第一天", "第二天", "第三天", "第四天", "第五天", "第六天", "第七天"};
        list.add(v.findViewById(R.id.view_1));
        list.add(v.findViewById(R.id.view_2));
        list.add(v.findViewById(R.id.view_3));
        list.add(v.findViewById(R.id.view_4));
        list.add(v.findViewById(R.id.view_5));
        list.add(v.findViewById(R.id.view_6));
        list.add(v.findViewById(R.id.view_7));
    }

    public void setData(List<TaskCenterData.CheckinBean> response) {
        for (int i = 0; i < response.size(); i++) {
            TaskCenterData.CheckinBean bean = null;
            try {
                bean = response.get(i);
                setViewData(i, dates[i], bean, list.get(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void setViewData(int index, String date, TaskCenterData.CheckinBean bean, FrameLayout view) {
        if (bean != null) {
            String bonus = bean.getBonus();

            int status = bean.getStatus();//签到状态 1为已签
            int today = bean.getToday();//1.当天

            TextView day1 = view.findViewById(R.id.tv_eveny_day1);
            TextView day2 = view.findViewById(R.id.tv_eveny_day2);

            day1.setText(CommonUtils.StringNotNull(date) ? date : "");
            day2.setText(CommonUtils.StringNotNull(date) ? date : "");

            TextView gold1 = view.findViewById(R.id.tv_current_gold1);
            TextView gold2 = view.findViewById(R.id.tv_current_gold2);

            gold1.setText(CommonUtils.StringNotNull(bonus) ? bonus : "");
            gold2.setText(CommonUtils.StringNotNull(bonus) ? bonus : "");

            FrameLayout flContent1 = view.findViewById(R.id.fl_content);
            FrameLayout flContent2 = view.findViewById(R.id.fl_content2);

            if (today == 1) {
                currentFlContent1 = flContent1;
                currentFlContent2 = flContent2;
            }

            if (status == 1) {
                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Rotate3dAnimation().start(getContext(), flContent1, flContent2);
                    }
                }, (index + 1) * 350);
            }
        }
    }

//    /**
//     * 签到
//     */
//    public void signIn(SucceedCallBackListener<Boolean> listener) {
//        new HeadlineImpl(new MarkSixNetCallBack<String>(getCtrl(), String.class) {
//            @Override
//            public void onSuccess(String response, int id) {
//                new Rotate3dAnimation().start(getContext(), currentFlContent1, currentFlContent2);
//                if (listener != null) {
//                    listener.succeedCallBack(true);
//                }
//            }
//
//            @Override
//            public void onError(String msg, String code) {
//                super.onError(msg, code);
//                if (listener != null) {
//                    listener.succeedCallBack(false);
//                }
//            }
//        }).startSignIn();
//    }

}
