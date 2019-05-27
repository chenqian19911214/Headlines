package com.marksixinfo.adapter;

import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.base.SimpleAdapter;
import com.marksixinfo.bean.MineUserFunction;
import com.marksixinfo.interfaces.ActivityIntentInterface;

import java.util.List;

/**
 * 个人页面8大功能
 *
 * @Auther: Administrator
 * @Date: 2019/4/20 0020 23:11
 * @Description:
 */
public class MineUserFunctionAdapter extends SimpleAdapter<MineUserFunction> {

    public MineUserFunctionAdapter(ActivityIntentInterface context, List<MineUserFunction> mData) {
        super(context, mData, R.layout.item_function_gridview);
    }

    @Override
    public void convert(ViewHolder holder, MineUserFunction item) {
        if (item != null) {
            holder.setText(R.id.iv_text, item.getText());
            holder.setImageResource(R.id.iv_image, item.getImage());
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext.checkLogin(item.getHost(), null)) {
                        mContext.startClass(item.getHost());
                    }
                }
            });
        }
    }
}
