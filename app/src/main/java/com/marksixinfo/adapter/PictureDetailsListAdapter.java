package com.marksixinfo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.SimpleAdapter;
import com.marksixinfo.bean.PictreCommentData;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.IDetailCommentInterface;
import com.marksixinfo.interfaces.NoDoubleClickListener;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.GoodView;
import com.marksixinfo.widgets.glide.GlideUtil;

import java.util.List;

/**
 * 图库详情listView adapter
 * @Description:
 */
public class PictureDetailsListAdapter extends SimpleAdapter<PictreCommentData> {
    private List<PictreCommentData> list;
    private Context context;
    private GoodView goodView;
    private long lastClickTime;
    private IDetailCommentInterface listener;

    public PictureDetailsListAdapter(Context context , ActivityIntentInterface contextIntentInterface, List<PictreCommentData> mData, IDetailCommentInterface iDetailCommentInterface) {
        super(contextIntentInterface, mData, R.layout.item_detail_comment);
        this.list = mData;
        this.context = context;
        this.listener = iDetailCommentInterface;
    }
    public void notifyUI(List<PictreCommentData> list) {
        this.list = list;
        notifyDataSetChanged();
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
        PictreCommentData detailCommentData = list.get(position);
        String add_time = detailCommentData.getAdd_Time();
        String content = detailCommentData.getContent();
        String Nickname = detailCommentData.getNickname();
        String face = detailCommentData.getFace();
        String user_id = detailCommentData.getUser_Id();
        int like_num = 0,like_status=0;
        String num="";
        int reply_num=0;
        PictreCommentData.LikeBean likeBean = detailCommentData.getLike();
        if (likeBean!=null){
             like_num = Integer.valueOf(likeBean.getNum());
             like_status = Integer.valueOf(likeBean.getStatus());
              num = detailCommentData.getReply_Num();
        }
        if (!TextUtils.isEmpty(num)){
             reply_num = Integer.valueOf(num);
        }

        int type = detailCommentData.getType();
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
           int finalLike_status = like_status;
           mTvPraise.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    lastClickTime = getLastClickTime();
                    if (listener != null) {
                        if (finalLike_status != 1) {
                            goodView = new GoodView(context);
                            goodView.setTextColor(context.getResources().getColor(R.color.colorPrimary)).setText("+1").show(viewPraise);
                        }
                        listener.praise(position, finalLike_status != 1);
                    }
                }
            }.setLastClickTime(lastClickTime));

            //回复
           int finalReply_num = reply_num;
           mTvReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.commentUser(position, finalReply_num > 0);
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

    @Override
    public void convert(ViewHolder holder, PictreCommentData item) {
    }
}
