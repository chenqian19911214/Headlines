package com.marksixinfo.adapter;

import android.view.View;

import com.marksixinfo.R;
import com.marksixinfo.base.SimpleAdapter2;
import com.marksixinfo.bean.MessageNotificationData;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.utils.CommonUtils;

import java.util.List;

/**
 * 消息通知
 *
 * @Auther: Administrator
 * @Date: 2019/4/24 0024 12:58
 * @Description:
 */
public class MessageNotificationAdapter extends SimpleAdapter2<MessageNotificationData> {

    public MessageNotificationAdapter(ActivityIntentInterface context, List<MessageNotificationData> mData,
                                      SucceedCallBackListener<MessageNotificationData> listener) {
        super(context, mData, R.layout.item_message_notification);
        this.listener = listener;
    }

    SucceedCallBackListener<MessageNotificationData> listener;

    @Override
    public void convert(ViewHolder holder, MessageNotificationData item) {
        if (item != null) {
            String content = item.getContent();
            String add_time = item.getAdd_Time();

            holder.setText(R.id.tv_time, CommonUtils.StringNotNull(add_time) ? add_time : "");

            MessageNotificationData.SendByBean sendBy = item.getSendBy();
            if (sendBy != null) {
                String face = sendBy.getFace();
                int type = item.getType();//0:系统消息 1:关注提醒 2:回复提醒
                String reply_title = item.getReply_Title();
                String reply_period = item.getReply_Period();
                holder.setCircleImageView(R.id.iv_user_photo, face);

                String nickname = sendBy.getNickname();
                if (type == 1) {//1:关注提醒
                    holder.setVisible(R.id.ll_title, false);
                    holder.setVisible(R.id.ll_concern, true);
                    holder.setText(R.id.tv_nike_name2, CommonUtils.StringNotNull(nickname) ? CommonUtils.CommHandleText2(nickname) : "");
                } else if (type == 2) {//2:回复提醒
                    holder.setVisible(R.id.ll_title, true);
                    holder.setVisible(R.id.ll_concern, false);
                    holder.setText(R.id.tv_nike_name, CommonUtils.StringNotNull(nickname) ?
                            CommonUtils.StringNotNull(reply_title) ? CommonUtils.handleText(nickname, 16)
                                    : CommonUtils.CommHandleText2(nickname) : "");
                } else {//系统消息
                    holder.setVisible(R.id.ll_title, false);
                    holder.setVisible(R.id.ll_concern, false);
//                    content = "快来阅读文章赚钱吧!" ;
                }

                holder.setText(R.id.tv_comment_content, CommonUtils.StringNotNull(content) ? content : "");
                holder.setVisible(R.id.tv_comment_content, CommonUtils.StringNotNull(content));

                holder.setVisible(R.id.tv_period_number, type == 2 && CommonUtils.StringNotNull(reply_period));
                holder.setVisible(R.id.tv_title, type == 2);
                holder.setText(R.id.tv_title, CommonUtils.StringNotNull(reply_title)
                        && type == 2 ? CommonUtils.handleText(reply_title, 14) : "");
                holder.setText(R.id.tv_period_number, CommonUtils.StringNotNull(reply_period) ? "第" + reply_period + "期" : "");

            }

            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (listener != null) {
                        listener.succeedCallBack(item);
                    }
                }
            });
        }
    }
}
