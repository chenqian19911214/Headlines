package com.marksixinfo.adapter;

import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.base.SimpleAdapter3;
import com.marksixinfo.bean.StandbyRemarkData;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.IStandbyRemark;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.SwipeMenuLayout;

import java.util.List;

/**
 * 备用网址编辑
 *
 * @Auther: Administrator
 * @Date: 2019/5/25 0025 14:57
 * @Description:
 */
public class StandbyRemarkAdapter extends SimpleAdapter3<StandbyRemarkData> {


    IStandbyRemark standbyRemark;

    public StandbyRemarkAdapter(ActivityIntentInterface context, List<StandbyRemarkData> mData, IStandbyRemark standbyRemark) {
        super(context, mData, R.layout.item_standby_remark);
        this.standbyRemark = standbyRemark;
    }

    @Override
    public void convert(ViewHolder holder, StandbyRemarkData item, int position) {
        if (item != null) {
            boolean editState = item.isEditState();//是否编辑
            String url = item.getUrl();
            String id = item.getId();
            SwipeMenuLayout swipeMenuLayout = holder.getView(R.id.swipe_layout);

            holder.setText(R.id.tv_remark, CommonUtils.StringNotNull(url) ? url : "");


            if (editState) {
                holder.setVisible(R.id.iv_delete_button, true);
                //调出删除
                holder.setOnClickListener(R.id.iv_delete_button, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swipeMenuLayout.smoothExpand();
                        if (standbyRemark != null) {
                            standbyRemark.showDelete(position);
                        }
                    }
                });
                //点击删除
                holder.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swipeMenuLayout.quickClose();
                        if (standbyRemark != null) {
                            standbyRemark.delRemark(id);
                        }
                    }
                });
            } else {
                holder.setOnClickListener(R.id.iv_delete_button, null);
                holder.setVisible(R.id.iv_delete_button, false);
            }
        }
    }
}
