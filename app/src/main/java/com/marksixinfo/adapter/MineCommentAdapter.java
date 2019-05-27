package com.marksixinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.marksixinfo.R;
import com.marksixinfo.bean.MineCommentData;
import com.marksixinfo.interfaces.IMinCommentInterface;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.SwipeMenuLayout;
import com.marksixinfo.widgets.dockingexpandablelistview.adapter.IDockingAdapterDataSource;
import com.marksixinfo.widgets.ninegridimg.ItemImageClickListener;
import com.marksixinfo.widgets.ninegridimg.NineGridImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 个人中心,我的回复
 *
 * @auther: Administrator
 * @date: 2019/3/26 0026 20:51
 */
public class MineCommentAdapter implements IDockingAdapterDataSource {

    private Context mContext;
    private List<MineCommentData> mGroupData = new ArrayList<>();
    private IMinCommentInterface listener;
    private int type = 0;//分类 0,头条  1,论坛

    public MineCommentAdapter(Context context, List<MineCommentData> mGroupData
            , IMinCommentInterface listener, int type) {
        mContext = context;
        this.mGroupData = mGroupData;
        this.listener = listener;
        this.type = type;
    }


    public void notifyUI(List<MineCommentData> mGroupData) {
        this.mGroupData = mGroupData;
    }


    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mine_comment_group, parent, false);
            groupViewHolder = new GroupViewHolder(mContext, convertView);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        MineCommentData ListBean = mGroupData.get(groupPosition);
        if (ListBean != null) {

            String id = ListBean.getId();
            String face = ListBean.getFace();
            String Nickname = ListBean.getNickname();
            String add_time = ListBean.getAdd_Time();
            groupViewHolder.setText(R.id.tv_Nickname, CommonUtils.StringNotNull(Nickname) ? Nickname : "");
            groupViewHolder.setText(R.id.tv_comment_time, CommonUtils.StringNotNull(add_time) ? add_time : "");
            groupViewHolder.setCircleImageView(R.id.iv_user_photo, face);

            //查看回复
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.checkComment(id);
                    }
                }
            });
        }
        return convertView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mine_comment_children, parent, false);
            childViewHolder = new ChildViewHolder(mContext, convertView);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }


        MineCommentData group = getGroup(groupPosition);
        MineCommentData.ReplyBean replyBean = group.getReply().get(childPosition);
        if (replyBean != null) {

            if (childPosition == 0) {//第一条回复带帖子
                childViewHolder.setVisible(R.id.ll_content, true);
                NineGridImageView mPaLinearLayout = childViewHolder.getView(R.id.pa_LinearLayout);
                String id = group.getId();
                String title = group.getTitle();
                String content = group.getContent();
                List<String> pic = group.getPic();

                childViewHolder.setVisible(R.id.tv_title, CommonUtils.StringNotNull(title));
                childViewHolder.setText(R.id.tv_title, CommonUtils.StringNotNull(title) ? title : "");

                childViewHolder.setVisible(R.id.tv_content, CommonUtils.StringNotNull(content));
                childViewHolder.setText(R.id.tv_content, CommonUtils.StringNotNull(content) ? content : "");

                //查看图片
                if (CommonUtils.ListNotNull(pic)) {
                    mPaLinearLayout.setVisibility(View.VISIBLE);
                    //最多3条
                    if (pic.size() > 3) {
                        pic = pic.subList(0, 3);
                    }
                    //图片集合
                    mPaLinearLayout.setImagesData(type == 1, pic, new ItemImageClickListener<String>() {
                        @Override
                        public void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
                            if (listener != null) {
                                listener.clickPhoto(list, index);
                            }
                        }
                    });
                } else {
                    mPaLinearLayout.setVisibility(View.GONE);
                }
                //查看帖子
                childViewHolder.setOnClickListener(R.id.ll_content, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (listener != null) {
                            listener.checkDetail(id);
                        }
                    }
                });
            } else {
                childViewHolder.setVisible(R.id.ll_content, false);
            }
            //正常回复条目
            String id = replyBean.getId();
            String content = replyBean.getContent();
            SwipeMenuLayout swipe = childViewHolder.getView(R.id.swipe_layout);
            childViewHolder.setText(R.id.tv_my_comment, type == 0 ? "我回复的：" : "我评论的：");
            childViewHolder.setText(R.id.tv_comment_content, CommonUtils.StringNotNull(content) ? content : "");
            childViewHolder.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        swipe.quickClose();
                        listener.deleteComment(groupPosition, childPosition, id);
                    }
                }
            });

        }
        return convertView;
    }

    public int getGroupCount() {
        return mGroupData.size();
    }

    public int getChildCount(int groupPosition) {
        if (mGroupData.get(groupPosition) != null) {
            return mGroupData.get(groupPosition).getReply().size();
        }

        return 0;
    }

    public MineCommentData getGroup(int groupPosition) {
        if (mGroupData.get(groupPosition) != null) {
            return mGroupData.get(groupPosition);
        }
        return null;
    }

    public MineCommentData.ReplyBean getChild(int groupPosition, int childPosition) {
        MineCommentData listBean = mGroupData.get(groupPosition);
        if (listBean != null) {
            return listBean.getReply().get(childPosition);
        }
        return null;
    }

    static class GroupViewHolder extends ViewHolder {

        public GroupViewHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }


    static class ChildViewHolder extends ViewHolder {

        public ChildViewHolder(Context context, View itemView) {
            super(context, itemView);
        }
    }

}
