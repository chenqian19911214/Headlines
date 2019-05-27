package com.marksixinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.bean.DetailCommentData;
import com.marksixinfo.interfaces.IDetailCommentInterface;
import com.marksixinfo.interfaces.NoDoubleClickListener;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.GoodView;
import com.marksixinfo.widgets.glide.GlideUtil;

import java.util.List;

/**
 * 头条文章详情页面,底部评论列表
 *
 * @Auther: Administrator
 * @Date: 2019/3/19 0019 12:36
 * @Description:
 */
public class DetailCommentAdapter extends BaseAdapter {


    private List<DetailCommentData> list;
    private IDetailCommentInterface listener;
    private Context context;
    private GoodView goodView;
    private long lastClickTime;


    public DetailCommentAdapter(Context context, List<DetailCommentData> list, IDetailCommentInterface listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
        goodView = new GoodView(context);
    }

    public void notifyUI(List<DetailCommentData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public DetailCommentData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_detail_comment, null);
            holder = new ViewHolder(viewGroup.getContext(), convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DetailCommentData detailCommentData = list.get(position);
        String add_time = detailCommentData.getAdd_Time();
        String content = detailCommentData.getContent();
        String Nickname = detailCommentData.getNickname();
        String face = detailCommentData.getFace();
        String user_id = detailCommentData.getUser_Id();
        int like_num = detailCommentData.getLike_Num();//点赞数
        int like_status = detailCommentData.getLike_status();//是否点赞
        int reply_num = detailCommentData.getReply_Num();//回复数
        int type = detailCommentData.getType();//0,条目  1,九宫格图片  2,emptyFoot
        String footViewText = detailCommentData.getFootViewText();


        if (type == 2) {
            holder.setVisible(R.id.view_foot, true);
            holder.setVisible(R.id.ll_data_content, false);
            holder.setText(R.id.tv_foot_text, CommonUtils.StringNotNull(footViewText) ? footViewText : "");
            holder.setVisible(R.id.view_line, position == 0);
        } else {
            holder.setVisible(R.id.view_foot, false);
            holder.setVisible(R.id.ll_data_content, true);

            holder.setVisible(R.id.view_line, position == 0);

            ImageView mIvUserPhoto = holder.getView(R.id.iv_user_photo);
            TextView mTvUserName = holder.getView(R.id.tv_user_name);
            TextView mTvPraise = holder.getView(R.id.tv_praise);
            View viewPraise = holder.getView(R.id.view_praise);
            TextView mTvCommentContent = holder.getView(R.id.tv_comment_content);
            TextView mTvTime = holder.getView(R.id.tv_time);
            TextView mTvReply = holder.getView(R.id.tv_reply);

            mTvPraise.setText(like_num > 0 ? CommonUtils.getThousandNumber(like_num) : "");
            CommonUtils.setPraiseStatus(context, mTvPraise, like_status);

            mTvReply.setText(reply_num > 0 ? CommonUtils.getThousandNumber(reply_num) + "回复" : "回复");

            //查看用户
            mTvUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.checkUser(user_id);
                    }
                }
            });

            //查看用户
            mIvUserPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.checkUser(user_id);
                    }
                }
            });

            //点赞
            mTvPraise.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    lastClickTime = getLastClickTime();
                    if (listener != null) {
                        if (like_status != 1) {
                            goodView.setTextColor(context.getResources().getColor(R.color.colorPrimary)).setText("+1").show(viewPraise);
                        }
                        listener.praise(position, like_status != 1);
                    }
                }
            }.setLastClickTime(lastClickTime));

            //回复
            mTvReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.commentUser(position, reply_num > 0);
                    }
                }
            });


            GlideUtil.loadCircleImage(face, mIvUserPhoto);
            mTvUserName.setText(CommonUtils.StringNotNull(Nickname) ? Nickname : "");
            mTvCommentContent.setText(CommonUtils.StringNotNull(content) ? content : "");
            mTvTime.setText(CommonUtils.StringNotNull(add_time) ? add_time : "");
        }
        return convertView;
    }


}
