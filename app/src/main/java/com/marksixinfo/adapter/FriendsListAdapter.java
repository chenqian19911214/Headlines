package com.marksixinfo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.bean.FriendListData;
import com.marksixinfo.interfaces.IFriendsListInterface;
import com.marksixinfo.utils.CommonUtils;

import java.util.List;

/**
 * 好友列表
 *
 * @Auther: Administrator
 * @Date: 2019/4/23 0023 17:12
 * @Description:
 */
public class FriendsListAdapter extends BaseAdapter {


    private List<FriendListData> list;
    private IFriendsListInterface listener;
    private Context context;
    private int emptyHeight;


    public FriendsListAdapter(Context context, List<FriendListData> list, int emptyHeight, IFriendsListInterface listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
        this.emptyHeight = emptyHeight;
    }

    public void notifyUI(List<FriendListData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public FriendListData getItem(int position) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_friends_list, null);
            holder = new ViewHolder(viewGroup.getContext(), convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        LinearLayout mLlRootContent = holder.getView(R.id.ll_root_content);
        RelativeLayout mRlNotList = holder.getView(R.id.rl_not_list);
        TextView mTvRemindFriends = holder.getView(R.id.tv_remind_friends);


        FriendListData listBean = list.get(position);

        int type = listBean.getType();
        String userId = listBean.getUser_Id();
        String face = listBean.getFace();
        String nickname = listBean.getNickname();

        String mainContent = listBean.getTips_1();
        String minorContent = listBean.getTips_2();

        if (type == 0) {//正常条目

            mLlRootContent.setVisibility(View.VISIBLE);
            holder.setVisible(R.id.ll_one_content, position == 0);
            mRlNotList.setVisibility(View.GONE);

            //头像
            holder.setCircleImageView(R.id.iv_user_photo, face);
            holder.setOnClickListener(R.id.iv_user_photo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.clickUser(userId);
                    }
                }
            });


            //昵称
            holder.setText(R.id.tv_nike_name, CommonUtils.StringNotNull(nickname) ? CommonUtils.CommHandleText(nickname) : "");
            holder.setOnClickListener(R.id.tv_nike_name, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.clickUser(userId);
                    }
                }
            });


            //提醒好友
            mTvRemindFriends.setVisibility(listBean.getStatus() == 0 ? View.VISIBLE : View.GONE);
            mTvRemindFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.remindUser(userId, position);
                    }
                }
            });


            holder.setText(R.id.tv_content_one, CommonUtils.StringNotNull(mainContent) ? mainContent : "");
            holder.setVisible(R.id.tv_content_one, CommonUtils.StringNotNull(mainContent));
            holder.setText(R.id.tv_content_two, CommonUtils.StringNotNull(minorContent) ? minorContent : "");
            holder.setVisible(R.id.tv_content_two, CommonUtils.StringNotNull(minorContent));

            holder.getView(R.id.view_line).setVisibility(position != list.size() - 1 ? View.VISIBLE : View.INVISIBLE);

        } else {
            mLlRootContent.setVisibility(View.GONE);
            mRlNotList.getLayoutParams().height = emptyHeight;
            mRlNotList.setVisibility(View.VISIBLE);
//            mRlNotList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, emptyHeight));
        }
        return convertView;
    }
}
