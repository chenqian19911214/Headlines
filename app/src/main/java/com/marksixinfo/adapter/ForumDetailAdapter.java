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
import com.marksixinfo.widgets.ninegridimg.ItemImageClickListener;
import com.marksixinfo.widgets.ninegridimg.NineGridImageView;

import java.util.List;

/**
 * 论坛文章详情页面,底部评论列表
 *
 * @Auther: Administrator
 * @Date: 2019/3/19 0019 12:36
 * @Description:
 */
public class ForumDetailAdapter extends BaseAdapter {


    private List<DetailCommentData> list;
    private IDetailCommentInterface listener;
    private ItemImageClickListener listener1;
    private Context context;
    private boolean isAddPic;
    private GoodView goodView;
    private long lastClickTime;

    public ForumDetailAdapter(Context context, List<DetailCommentData> list
            , IDetailCommentInterface listener, ItemImageClickListener listener1) {
        this.list = list;
        this.context = context;
        this.listener = listener;
        this.listener1 = listener1;
        goodView = new GoodView(context);
    }

    public void setAddPic(boolean addPic) {
        isAddPic = addPic;
    }

    public boolean isAddPic() {
        return isAddPic;
    }

    public void notifyUI(List<DetailCommentData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public int getViewTypeCount() {
        return 3;
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

        DetailCommentData detailCommentData = list.get(position);
        int type = detailCommentData.getType();//0,条目  1,九宫格图片  2,emptyFoot

        if (type == 1) {//九宫格图片
            List<String> pic = detailCommentData.getPic();
            NineGridHolder holder1;
            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_detail_comment_head, null);
                holder1 = new NineGridHolder(context, convertView);
                convertView.setTag(holder1);
            } else {
                holder1 = (NineGridHolder) convertView.getTag();
            }

            NineGridImageView mPaLinearLayout = holder1.getView(R.id.pa_LinearLayout);

            if (CommonUtils.ListNotNull(pic) && listener1 != null) {
                mPaLinearLayout.setVisibility(View.VISIBLE);
                mPaLinearLayout.setImagesData(pic, listener1);
            } else {
                mPaLinearLayout.setVisibility(View.GONE);
            }
        } else {
            mHolder holder;
            if (null == convertView) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_detail_comment, null);
                holder = new mHolder(context, convertView);
                convertView.setTag(holder);
            } else {
                holder = (mHolder) convertView.getTag();
            }

            holder.setVisible(R.id.view_line, isAddPic ? position == 1 : position == 0);
            if (type == 2) {
                String footViewText = detailCommentData.getFootViewText();
                holder.setVisible(R.id.view_foot, true);
                holder.setVisible(R.id.ll_data_content, false);
                holder.setText(R.id.tv_foot_text, CommonUtils.StringNotNull(footViewText) ? footViewText : "");

            } else {
                holder.setVisible(R.id.view_foot, false);
                holder.setVisible(R.id.ll_data_content, true);

                String add_time = detailCommentData.getAdd_Time();
                String content = detailCommentData.getContent();
                String Nickname = detailCommentData.getNickname();
                String face = detailCommentData.getFace();
                String user_id = detailCommentData.getUser_Id();
                int like_num = detailCommentData.getLike_Num();//点赞数
                int like_status = detailCommentData.getLike_status();//是否点赞
                int reply_num = detailCommentData.getReply_Num();//回复数

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
        }


        return convertView;
    }


    static class NineGridHolder extends ViewHolder {

        public NineGridHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }


    static class mHolder extends ViewHolder {

        public mHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }

}
