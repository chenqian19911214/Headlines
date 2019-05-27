package com.marksixinfo.adapter;

import android.view.View;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.SimpleAdapter3;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.OnItemClickListener;
import com.marksixinfo.utils.CommonUtils;

import java.util.List;

/**
 * 开奖当年所有期数
 *
 * @Auther: Administrator
 * @Date: 2019/5/11 0011 20:51
 * @Description:
 */
public class LotteryPeriodAdapter extends SimpleAdapter3<List<String>> {


    public LotteryPeriodAdapter(ActivityIntentInterface context, List<List<String>> mData, OnItemClickListener listener) {
        super(context, mData, R.layout.item_lottery_period, listener);
    }

    @Override
    public void convert(ViewHolder holder, List<String> item, int position) {
        if (item != null) {
            try {
                String periodString = item.get(0);
                boolean isCheck = Boolean.parseBoolean(item.get(2));

                TextView lotteryPeriod = holder.getView(R.id.tv_lottery_period);

                lotteryPeriod.setTextColor(isCheck ? 0xffffffff : 0xff333333);
                lotteryPeriod.setSelected(isCheck);

                lotteryPeriod.setText(CommonUtils.StringNotNull(periodString) ? periodString : "");

                lotteryPeriod.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isCheck) {
                            if (listener != null) {
                                listener.onItemClick(v, position);
                            }
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
