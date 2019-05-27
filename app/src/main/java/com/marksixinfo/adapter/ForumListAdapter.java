package com.marksixinfo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.bean.ForumCommentData;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.interfaces.IForumListInterface;
import com.marksixinfo.interfaces.NoDoubleClickListener;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.ConcernTextView;
import com.marksixinfo.widgets.GoodView;
import com.marksixinfo.widgets.SpannableStringUtils;
import com.marksixinfo.widgets.ninegridimg.ItemImageClickListener;
import com.marksixinfo.widgets.ninegridimg.NineGridImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 论坛页面,广场与关注
 *
 * @Auther: Administrator
 * @Date: 2019/3/20 0020 12:47
 * @Description:
 */
public class ForumListAdapter extends RecyclerView.Adapter<ViewHolder> {


    private Context context;
    private List<MainHomeData> list;
    private IForumListInterface listInterface;
    private int type;//0,关注 1,广场
    private GoodView goodView;
    private long lastClickTime;


    public ForumListAdapter(Context context, List<MainHomeData> list, int type, IForumListInterface listInterface) {
        this.context = context;
        this.list = list;
        this.type = type;
        this.listInterface = listInterface;
        goodView = new GoodView(context);
    }

    public void notifyUI(List<MainHomeData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.createViewHolder(context, parent, R.layout.item_forum_list);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MainHomeData data = list.get(position);

        String id = data.getId();
        String face = data.getFace();
        String nickname = data.getNickname();
        String add_time = data.getAdd_Time();
        String title = data.getTitle();
        String content = data.getContent();
        List<String> pic = data.getPic();
        int like_status = data.getLike_status();
        int like_num = data.getLike_Num();
        int look_status = data.getLook_status();
        int reply_num = data.getReply_Num();
        int level = data.getLevel();
        List<ForumCommentData> commentList = data.getCommentList();

        TextView mTvPraise = holder.getView(R.id.tv_praise);
        View viewPraise = holder.getView(R.id.view_praise);
        ConcernTextView mTvConcern = holder.getView(R.id.tv_concern);
        ImageView mIvOfficial = holder.getView(R.id.iv_official);
        NineGridImageView mPaLinearLayout = holder.getView(R.id.pa_LinearLayout);
        LinearLayout ll_comment_content = holder.getView(R.id.ll_comment_content);
        View view_line = holder.getView(R.id.view_line);
//        view_line.getLayoutParams().height = UIUtils.getViewHeight(ll_comment_content);

        UIUtils.setMargins(context,view_line,0,3,0,23);

        holder.setCircleImageView(R.id.iv_user_photo, face);

        holder.setText(R.id.tv_Nickname, CommonUtils.StringNotNull(nickname) ? CommonUtils.CommHandleText2(nickname) : "");

        holder.setVisible(R.id.tv_title, CommonUtils.StringNotNull(title));
        holder.setText(R.id.tv_title, CommonUtils.StringNotNull(title) ? title : "");

//        holder.setVisible(R.id.tv_time, type == 0);//广场不显示时间
        holder.setText(R.id.tv_time, CommonUtils.StringNotNull(add_time) ? add_time : "");

        holder.setVisible(R.id.tv_content, CommonUtils.StringNotNull(content));
        holder.setText(R.id.tv_content, CommonUtils.StringNotNull(content) ? content : "");


        //查看图片
        if (CommonUtils.ListNotNull(pic)) {
            mPaLinearLayout.setVisibility(View.VISIBLE);

            //最多3条
            if (pic.size() > 3) {
                pic = pic.subList(0, 3);
            }
            //图片集合
            mPaLinearLayout.setImagesData(pic, new ItemImageClickListener<String>() {
                @Override
                public void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
                    if (listInterface != null) {
                        listInterface.clickPhoto(list, index);
                    }
                }
            });
        } else {
            mPaLinearLayout.setVisibility(View.GONE);
        }


        //点赞数量
        mTvPraise.setText(like_num > 0 ? CommonUtils.getThousandNumber(like_num) : "");

        //评论数量
        holder.setText(R.id.tv_comment, reply_num > 0 ? CommonUtils.getThousandNumber(reply_num) : "");

        //点赞状态
        CommonUtils.setForumPraiseStatus(context, mTvPraise, like_status);

        //关注状态
        mTvConcern.setLookStatus(look_status);

        //官方
        CommonUtils.setUserLevel(mIvOfficial, 0);//论坛模块不显示

        //评论
        if (CommonUtils.ListNotNull(commentList)) {
            ForumCommentData forumCommentData = commentList.get(0);
            if (forumCommentData != null) {
                SpannableStringUtils stringUtils = new SpannableStringUtils();
                stringUtils.addText(13, 0xff333333, forumCommentData.getNickname() + "：");
                stringUtils.addText(13, 0xff666666, forumCommentData.getContent());
                holder.setText(R.id.tv_comment_one, stringUtils.toSpannableString());
                holder.setVisible(R.id.tv_comment_one, true);

                holder.setOnClickListener(R.id.tv_comment_one, new
                        View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (listInterface != null) {
                                    listInterface.clickListComment(forumCommentData.getId(), forumCommentData.getNickname());
                                }
                            }
                        });
            } else {
                holder.setVisible(R.id.tv_comment_one, false);
            }
            if (commentList.size() > 1) {
                ForumCommentData forumCommentData2 = commentList.get(1);
                if (forumCommentData2 != null) {
                    SpannableStringUtils stringUtils2 = new SpannableStringUtils();
                    stringUtils2.addText(13, 0xff333333, forumCommentData2.getNickname() + "：");
                    stringUtils2.addText(13, 0xff666666, forumCommentData2.getContent());
                    holder.setText(R.id.tv_comment_two, stringUtils2.toSpannableString());
                    holder.setVisible(R.id.tv_comment_two, true);
                    holder.setOnClickListener(R.id.tv_comment_two, new
                            View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (listInterface != null) {
                                        listInterface.clickListComment(forumCommentData2.getId(), forumCommentData2.getNickname());
                                    }
                                }
                            });

                } else {
                    holder.setVisible(R.id.tv_comment_two, false);
                }
            } else {
                holder.setVisible(R.id.tv_comment_two, false);
            }
        } else {
            holder.setVisible(R.id.tv_comment_one, false);
            holder.setVisible(R.id.tv_comment_two, false);
        }

        //点赞
        mTvPraise.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                lastClickTime = getLastClickTime();
                if (listInterface != null) {
                    if (like_status != 1) {
                        goodView.setTextColor(context.getResources().getColor(R.color.colorPrimary)).setText("+1").show(viewPraise);
                    }
                    listInterface.checkPraise(position, like_status != 1);
                }
            }
        }.setLastClickTime(lastClickTime));

        //回复
        holder.setOnClickListener(R.id.tv_comment, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listInterface != null) {
                    listInterface.checkComment(position);
                }
            }
        });

        //用户
        holder.setOnClickListener(R.id.iv_user_photo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listInterface != null) {
                    listInterface.checkUser(position);
                }
            }
        });

        //用户
        holder.setOnClickListener(R.id.tv_Nickname, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listInterface != null) {
                    listInterface.checkUser(position);
                }
            }
        });

        //条目
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listInterface != null) {
                    listInterface.onItemClick(position);
                }
            }
        });

        //评论
        holder.setOnClickListener(R.id.tv_start_comment, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listInterface != null) {
                    listInterface.clickComment(position);
                }
            }
        });

        //关注
        mTvConcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listInterface != null && mTvConcern.getLook_status() != -1) {
                    if (mTvConcern.startLookStatus()) {
                        listInterface.isConcern(position);
                    }
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}