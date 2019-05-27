package com.marksixinfo.adapter;

import android.view.View;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.SimpleAdapter;
import com.marksixinfo.bean.TaskCenterData;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.utils.MoneyUtils;

import java.util.List;

/**
 * 任务中心任务列表
 *
 * @Auther: Administrator
 * @Date: 2019/4/20 0020 15:59
 * @Description:
 */
public class TaskCenterAdapter extends SimpleAdapter<TaskCenterData.TaskBean> {


    private SucceedCallBackListener<TaskCenterData.TaskBean> listener;
    private ActivityIntentInterface context;


    public TaskCenterAdapter(ActivityIntentInterface context, List<TaskCenterData.TaskBean> mData
            , SucceedCallBackListener<TaskCenterData.TaskBean> listener) {
        super(context, mData, R.layout.item_task_center_list);
        this.listener = listener;
        this.context = context;
    }

    @Override
    public void convert(ViewHolder holder, TaskCenterData.TaskBean item) {
        if (item != null) {
            int type = item.getIcon();
            holder.setImageResource(R.id.iv_icon_type, getIcon(type));
            holder.setText(R.id.tv_item_title, item.getName());
            holder.setText(R.id.tv_item_content, item.getNote());
            TextView mTvGetGold = holder.getView(R.id.tv_get_gold);
            List<Integer> num = item.getNum();
            if (num != null && num.size() >= 2) {
                Integer integer1 = num.get(0);
                Integer integer2 = num.get(1);
                if (type == 1) {//邀请任务
                    holder.setText(R.id.tv_current_num, "(已邀请" + integer1 + "人)");
                } else {
                    holder.setText(R.id.tv_current_num, "(" + integer1 + "/" + integer2 + ")");
                }
                holder.setVisible(R.id.tv_current_num, true);
            } else {
                holder.setVisible(R.id.tv_current_num, false);
            }
            if (item.getStatus() == 1) {//已完成
                mTvGetGold.setText("已完成");
                mTvGetGold.setTextColor(0xffffffff);
                mTvGetGold.setBackgroundResource(R.drawable.shape_task_gold_done);
            } else {
                // 1:余额 0:金币
                String fix = item.getBonus_Type() == 1 ? "元" : "金币";
                mTvGetGold.setText("领" + MoneyUtils.getIntMoneyText(item.getTip_Bonus()) + fix);
                mTvGetGold.setTextColor(0xffbc4a51);
                mTvGetGold.setBackgroundResource(R.drawable.shape_task_get_gold);
            }

            holder.setOnClickListener(R.id.tv_get_gold, item.getStatus() == 0 ? new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.succeedCallBack(item);
                    }
                }
            } : null);
        }
    }


    /**
     * 获取图标
     *
     * @param type
     * @return
     */
    private int getIcon(int type) {
        switch (type) {
            case 1://首次邀请好友
                return R.drawable.icon_task_type1;
            case 2://输入邀请码
                return R.drawable.icon_task_type2;
            case 3://我的点赞
                return R.drawable.icon_task_type3;
            case 4://优质评论
                return R.drawable.icon_task_type4;
            case 5://首次分享
                return R.drawable.icon_task_type5;
            case 6://发布头条
                return R.drawable.icon_task_type6;
            case 7://再次邀请好友
                return R.drawable.icon_task_type7;
            default:
                return R.drawable.icon_task_type1;
        }
    }
}
