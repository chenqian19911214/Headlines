package com.marksixinfo.adapter;

import android.content.Context;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.interfaces.IMainHomeRecommend;
import com.marksixinfo.interfaces.NoDoubleClickListener;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.widgets.ConcernTextView;
import com.marksixinfo.widgets.GoodView;
import com.marksixinfo.widgets.ShowAllSpan;
import com.marksixinfo.widgets.ShowAllTextView;
import com.marksixinfo.widgets.glide.GlideUtil;
import com.marksixinfo.widgets.ninegridimg.ItemImageClickListener;
import com.marksixinfo.widgets.ninegridimg.NineGridImageView;

import java.util.List;

/**
 * 头条搜索结果页
 *
 * @Auther: Administrator
 * @Date: 2019/3/15 0015 13:35
 * @Description:
 */
public class SearchResultAdapter extends BaseAdapter {


    private Context context;
    private List<MainHomeData> list;
    private IMainHomeRecommend iMainHomeRecommend;
    private GoodView goodView;
    private long lastClickTime;


    public SearchResultAdapter(Context context, List<MainHomeData> list, IMainHomeRecommend iMainHomeRecommend) {
        this.list = list;
        this.context = context;
        this.iMainHomeRecommend = iMainHomeRecommend;
        goodView = new GoodView(context);
    }

    public void notifyUI(List<MainHomeData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }


    @Override
    public MainHomeData getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_search_result, null);
            holder = new ViewHolder(context, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MainHomeData mainHomeData = list.get(position);

        String add_time = mainHomeData.getAdd_Time();
        String face = mainHomeData.getFace();
        String Nickname = mainHomeData.getNickname();
        List<String> pic = mainHomeData.getPic();
        String title = mainHomeData.getTitle();
        String content = mainHomeData.getContent();
        SpannableString oldContent = mainHomeData.getOldContent();
        String user_id = mainHomeData.getUser_Id();
        String remark = mainHomeData.getRemark();
        int period = mainHomeData.getPeriod();
        int level = mainHomeData.getLevel();
        int favorites_num = mainHomeData.getFavorites_Num();
        int like_num = mainHomeData.getLike_Num();
        int reply_num = mainHomeData.getReply_Num();
        int fav_status = mainHomeData.getFav_status();
        int like_status = mainHomeData.getLike_status();
        int look_status = mainHomeData.getLook_status();


        ImageView mIvUserPhoto = holder.getView(R.id.iv_user_photo);
        TextView mTvUserName = holder.getView(R.id.tv_user_name);
        TextView tvTitle = holder.getView(R.id.tv_title);
        ImageView mIvOfficial = holder.getView(R.id.iv_official);
        TextView mTvPeriodNumber = holder.getView(R.id.tv_period_number);
        TextView mTvUserDomain = holder.getView(R.id.tv_user_domain);
        TextView mTvTime = holder.getView(R.id.tv_time);
        ConcernTextView mTvConcern = holder.getView(R.id.tv_concern);
        ShowAllTextView mShowAllTextView = holder.getView(R.id.showAllTextView);
        NineGridImageView mPaLinearLayout = holder.getView(R.id.pa_LinearLayout);
        View viewPraise = holder.getView(R.id.view_praise);
        View viewCollect = holder.getView(R.id.view_collect);

        TextView mTvPraise = holder.getView(R.id.tv_praise);
        TextView mTvComment = holder.getView(R.id.tv_comment);
        TextView mTvCollect = holder.getView(R.id.tv_collect);

        RelativeLayout mRlPraise = holder.getView(R.id.rl_praise);
        RelativeLayout mRlComment = holder.getView(R.id.rl_comment);
        RelativeLayout mRlCollect = holder.getView(R.id.rl_collect);

        //头像
        GlideUtil.loadCircleImage(face, mIvUserPhoto);


        //标题
        tvTitle.setText(CommonUtils.StringNotNull(title) ? title : "");
        if (CommonUtils.StringNotNull(title)) {
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
        }
        if (oldContent != null) {
            mShowAllTextView.setText(oldContent);
        } else {
            if (CommonUtils.StringNotNull(content)) {
                mShowAllTextView.setMyText(content, mainHomeData);
                mShowAllTextView.setVisibility(View.VISIBLE);
            } else {
                mShowAllTextView.setMyText("", null);
                mShowAllTextView.setVisibility(View.GONE);
            }
        }
        //名称
        mTvUserName.setText(CommonUtils.StringNotNull(Nickname) ? CommonUtils.CommHandleText(Nickname) : "");

        //官方
        CommonUtils.setUserLevel(mIvOfficial, level);

        //时间
        mTvTime.setText(CommonUtils.StringNotNull(add_time) ? add_time : "");

        //期数
        if (period > 0) {
            mTvPeriodNumber.setVisibility(View.VISIBLE);
            mTvPeriodNumber.setText(CommonUtils.fixThree(String.valueOf(period)) + "期");
        } else {
            mTvPeriodNumber.setVisibility(View.GONE);
            mTvPeriodNumber.setText("");
        }

        //点赞数量
        mTvPraise.setText(like_num > 0 ? CommonUtils.getThousandNumber(like_num) : "");

        //评论数量
        mTvComment.setText(reply_num > 0 ? CommonUtils.getThousandNumber(reply_num) : "");

        //收藏数量
        mTvCollect.setText(favorites_num > 0 ? CommonUtils.getThousandNumber(favorites_num) : "");

        //网址
        mTvUserDomain.setText(CommonUtils.StringNotNull(remark) ? CommonUtils.CommHandleText(remark) : "");
        mTvUserDomain.setVisibility(CommonUtils.StringNotNull(remark) ? View.VISIBLE : View.GONE);

        //点赞
        CommonUtils.setPraiseStatus(context, mTvPraise, like_status);

        //收藏
        CommonUtils.setCollectStatus(context, mTvCollect, fav_status);

        //关注状态
//        CommonUtils.setLookStatus(mTvConcern, look_status);
        mTvConcern.setLookStatus(look_status);


        //图片查看
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
                    if (iMainHomeRecommend != null) {
                        iMainHomeRecommend.clickPhoto(list, index);
                    }
                }
            });
        } else {
            mPaLinearLayout.setVisibility(View.GONE);
        }

        //查看发帖人
        mIvUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iMainHomeRecommend != null) {
                    iMainHomeRecommend.clickUser(user_id);
                }
            }
        });
        //查看发帖人
        mTvUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iMainHomeRecommend != null) {
                    iMainHomeRecommend.clickUser(user_id);
                }
            }
        });

        //更多
        mShowAllTextView.setOnAllSpanClickListener(new ShowAllSpan.OnAllSpanClickListener() {
            @Override
            public void onClick(View widget) {
                if (iMainHomeRecommend != null) {
                    iMainHomeRecommend.moreContent(position);
                }
            }
        });


        //关注
        mTvConcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iMainHomeRecommend != null && mTvConcern.getLook_status() != -1) {
                    if (mTvConcern.startLookStatus()) {
                        iMainHomeRecommend.isConcern(position, look_status != 1);
                    }
                }
            }
        });

        //点赞
        mRlPraise.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                lastClickTime = getLastClickTime();
                if (iMainHomeRecommend != null) {
                    if (like_status != 1) {
                        goodView.setTextColor(context.getResources().getColor(R.color.colorPrimary)).setText("+1").show(viewPraise);
                    }
                    iMainHomeRecommend.checkPraise(position, like_status != 1);
                }
            }
        }.setLastClickTime(lastClickTime));

        //评论
        mRlComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iMainHomeRecommend != null) {
                    iMainHomeRecommend.clickComment(position);
                }
            }
        });

        //收藏
        mRlCollect.setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View v) {
                lastClickTime = getLastClickTime();
                if (iMainHomeRecommend != null) {
                    if (fav_status != 1 && CommonUtils.StringNotNull(SPUtil.getToken(context))) {
                        goodView.setTextInfo("收藏成功", 0xffefa301, 12).show(viewCollect);
                    }
                    iMainHomeRecommend.checkCollect(position, fav_status != 1);
                }
            }
        }.setLastClickTime(lastClickTime));

        //条目
        if (iMainHomeRecommend != null) {
            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    iMainHomeRecommend.onItemClick(view, position);
                }
            });
        }


        return convertView;
    }
}
