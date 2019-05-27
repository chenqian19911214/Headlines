package com.marksixinfo.adapter;

import android.content.Context;
import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.base.SimpleAdapter3;
import com.marksixinfo.bean.MineConcernData;
import com.marksixinfo.constants.NumberConstants;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.utils.CommonUtils;

import java.util.List;

/**
 * 头条搜索显示横滑用户
 *
 * @Auther: Administrator
 * @Date: 2019/5/23 0023 23:27
 * @Description:
 */
public class SearchUserAdapter extends SimpleAdapter3<MineConcernData> {

    SucceedCallBackListener<Integer> listener;

    public SearchUserAdapter(Context context, List<MineConcernData> mData, SucceedCallBackListener<Integer> listener) {
        super((ActivityIntentInterface) context, mData, R.layout.item_search_user);
        this.listener = listener;
    }

    @Override
    public void convert(ViewHolder holder, MineConcernData item, int position) {
        if (item != null) {
            //头像
            holder.setCircleImageView(R.id.iv_user_photo, item.getFace());
            //最后一个
            boolean isLast = position >= NumberConstants.SEARCH_USER_COUNT;

            if (isLast) {
                //昵称
                holder.setText(R.id.tv_nike_name, "更多");
                //网址
                holder.setText(R.id.tv_user_domain, "");
                holder.setVisible(R.id.iv_user_level, false);
                holder.setVisible(R.id.iv_more_user, true);
                holder.setVisible(R.id.view_line, true);
            } else {
                String nickname = item.getNickname();
                int level = item.getLevel();
                String remark = item.getRemark();
                //昵称
                holder.setText(R.id.tv_nike_name, CommonUtils.StringNotNull(nickname) ? nickname : "");
                //网址
                holder.setText(R.id.tv_user_domain, CommonUtils.StringNotNull(remark) ? remark : "");
                //等级
                CommonUtils.setSearchUserLevel(holder.getView(R.id.iv_user_level), level);

                holder.setVisible(R.id.iv_more_user, false);
                holder.setVisible(R.id.view_line, false);
            }
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.succeedCallBack(position);
                    }
                }
            });
        }
    }
}
