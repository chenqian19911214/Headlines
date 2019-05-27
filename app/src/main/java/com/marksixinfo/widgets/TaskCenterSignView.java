package com.marksixinfo.widgets;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.ViewBase;
import com.marksixinfo.bean.TaskCenterData;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 任务中心签到View
 *
 * @Auther: Administrator
 * @Date: 2019/4/20 0020 12:37
 * @Description:
 */
public class TaskCenterSignView extends ViewBase implements View.OnClickListener {


    public TaskCenterSignView(Context context) {
        super(context);
    }

    public TaskCenterSignView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TaskCenterSignView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getViewId() {
        return R.layout.view_task_center_sign;
    }

    @Override
    public void afterViews(View v) {
        init(v);
    }

    private TextView sigintitle;
    private Button siginIn;
    private List<TextView> goldList;
    private List<TextView> dateList;
    private SucceedCallBackListener listener;

    private void init(View view) {
        goldList = new ArrayList<>();
        dateList = new ArrayList<>();


        sigintitle = view.findViewById(R.id.tv_sigin_title);
        siginIn = view.findViewById(R.id.tv_sigin_in);
        siginIn.setOnClickListener(this);

        goldList.add(view.findViewById(R.id.tv_gold1));
        goldList.add(view.findViewById(R.id.tv_gold2));
        goldList.add(view.findViewById(R.id.tv_gold3));
        goldList.add(view.findViewById(R.id.tv_gold4));
        goldList.add(view.findViewById(R.id.tv_gold5));
        goldList.add(view.findViewById(R.id.tv_gold6));
        goldList.add(view.findViewById(R.id.tv_gold7));


        dateList.add(view.findViewById(R.id.tv_date1));
        dateList.add(view.findViewById(R.id.tv_date2));
        dateList.add(view.findViewById(R.id.tv_date3));
        dateList.add(view.findViewById(R.id.tv_date4));
        dateList.add(view.findViewById(R.id.tv_date5));
        dateList.add(view.findViewById(R.id.tv_date6));
        dateList.add(view.findViewById(R.id.tv_date7));

    }


    /**
     * 设置数据
     *
     * @param list
     */
    public void setData(List<TaskCenterData.CheckinBean> list, SucceedCallBackListener listener) {
        if (CommonUtils.ListNotNull(list)) {
            this.listener = listener;
            int index = 0;
            boolean isToday = false;
            for (int i = 0; i < list.size(); i++) {
                TaskCenterData.CheckinBean bean = null;
                try {
                    bean = list.get(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (bean != null) {
                    int today = bean.getToday();//1,是今天
                    int status = bean.getStatus();//签到状态 1为已签
                    String bonus = bean.getBonus();
                    String date = bean.getDate();
                    TextView goldView = goldList.get(i);
                    TextView dateView = dateList.get(i);
                    if (today == 1) {//今天是否已签到
                        isToday = status == 1;
                    }
                    if (status == 1) {//已签
                        index++;
                        goldView.setBackgroundResource(R.drawable.icon_task_signin);
                        goldView.setText("");
                        dateView.setText("已签");
                        dateView.setTextColor(0xffff9600);
                    } else {
                        goldView.setBackgroundResource(R.drawable.icon_task_unsign);
                        goldView.setText(CommonUtils.StringNotNull(bonus) ? bonus : "");
                        dateView.setText(date);
                        dateView.setTextColor(0xff333333);
                    }
                }
            }

//            if (index > 0) {
                SpannableStringUtils sp = new SpannableStringUtils();
                sp.addText(17, 0xff333333, "签到任务");
                sp.addText(12, 0xffff9600, "(已连续签到" + index + "天)");
                sigintitle.setText(sp.toSpannableString());
//            }

            if (isToday) {
                setSiginInSuccess();
            } else {
                siginIn.setText("立即签到");
                siginIn.setEnabled(true);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_sigin_in: //签到
                if (listener != null) {
                    listener.succeedCallBack(null);
                }
                break;
        }
    }


    /**
     * 签到已完成
     */
    private void setSiginInSuccess() {
        GradientDrawable myGrad = (GradientDrawable) siginIn.getBackground();
        Resources resources = siginIn.getContext().getResources();
        if (resources == null) {
            return;
        }
        myGrad.setColor(resources.getColor(R.color.yellow_ffd));
        siginIn.setEnabled(false);
        siginIn.setText("今日已签到");
    }


}
