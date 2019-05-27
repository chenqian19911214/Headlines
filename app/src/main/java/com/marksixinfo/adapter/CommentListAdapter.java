package com.marksixinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.bean.CommentsListData;
import com.marksixinfo.interfaces.ICommentListInterface;
import com.marksixinfo.interfaces.NoDoubleClickListener;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.GoodView;
import com.marksixinfo.widgets.glide.GlideUtil;

import java.util.List;

/**
 * 回复评论页面列表
 *
 * @Auther: Administrator
 * @Date: 2019/3/19 0019 12:36
 * @Description:
 */
public class CommentListAdapter extends BaseAdapter {


    private List<CommentsListData> list;
    private ICommentListInterface listInterface;
    private Context context;
    private GoodView goodView;
    private long lastClickTime;

    public CommentListAdapter(Context context, List<CommentsListData> list, ICommentListInterface listInterface) {
        this.context = context;
        this.list = list;
        this.listInterface = listInterface;
        goodView = new GoodView(context);
    }

    public void notifyUI(List<CommentsListData> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public CommentsListData getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_list_comment, null);
            holder = new ViewHolder(context, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        CommentsListData data = list.get(position);
        String userId = data.getUser_Id();
        String face = data.getFace();
        String Nickname = data.getNickname();
        String content = data.getContent();
        String add_time = data.getAdd_Time();
        int like_num = data.getLike_Num();
        int like_status = data.getLike_status();
        int type = data.getType();
        String footViewText = data.getFootViewText();

        if (type == 2) {//footView
            holder.setVisible(R.id.view_foot, true);
            holder.setVisible(R.id.ll_data_content, false);
            holder.mTvAllComment.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            holder.setText(R.id.tv_foot_text, CommonUtils.StringNotNull(footViewText) ? footViewText : "");
        } else {
            holder.setVisible(R.id.view_foot, false);
            holder.setVisible(R.id.ll_data_content, true);

            GlideUtil.loadCircleImage(face, holder.mIvUserPhoto);
            holder.mTvAllComment.setVisibility(position == 0 ? View.VISIBLE : View.GONE);

            holder.mTvUserName.setText(CommonUtils.StringNotNull(Nickname) ? Nickname : "");
            holder.mTvCommentContent.setText(CommonUtils.StringNotNull(content) ? content : "");
            holder.mTvTime.setText(CommonUtils.StringNotNull(add_time) ? add_time : "");
            holder.mTvListPraise.setText(like_num > 0 ? CommonUtils.getThousandNumber(like_num) : "");

            CommonUtils.setPraiseStatus(context, holder.mTvListPraise, like_status);
//        holder.mTvListPraise.setSelected(like_status == 1);


            //查看用户
            holder.mIvUserPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listInterface != null) {
                        listInterface.checkUser(userId);
                    }
                }
            });

            //查看用户
            holder.mTvUserName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listInterface != null) {
                        listInterface.checkUser(userId);
                    }
                }
            });

            //点赞用户评论
            holder.mTvListPraise.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    lastClickTime = getLastClickTime();
                    if (listInterface != null) {
                        if (like_status != 1) {
                            goodView.setTextColor(context.getResources().getColor(R.color.colorPrimary)).setText("+1").show(holder.viewListPraise);
                        }
                        listInterface.praise(position, like_status != 1);
                    }
                }
            }.setLastClickTime(lastClickTime));
        }

        return convertView;
    }

    static class ViewHolder extends com.marksixinfo.adapter.ViewHolder {
        private ImageView mIvUserPhoto;
        private TextView mTvUserName;
        private View viewListPraise;
        private TextView mTvListPraise;
        private TextView mTvCommentContent;
        private TextView mTvTime;
        private TextView mTvAllComment;

        public ViewHolder(Context context, View convertView) {
            super(context, convertView);
            mIvUserPhoto = convertView.findViewById(R.id.iv_user_photo);
            mTvUserName = convertView.findViewById(R.id.tv_user_name);
            viewListPraise = convertView.findViewById(R.id.view_list_praise);
            mTvListPraise = convertView.findViewById(R.id.tv_list_praise);
            mTvCommentContent = convertView.findViewById(R.id.tv_comment_content);
            mTvTime = convertView.findViewById(R.id.tv_time);
            mTvAllComment = convertView.findViewById(R.id.tv_all_comment);
        }
    }
}
