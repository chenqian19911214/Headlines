package com.marksixinfo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.marksixinfo.R;
import com.marksixinfo.bean.MineConcernData;
import com.marksixinfo.interfaces.IMineConcernInterface;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.SwipeMenuLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 个人中心,我的关注/粉丝
 *
 * @Auther: Administrator
 * @Date: 2019/3/26 0026 21:19
 * @Description:
 */
public class MineConcernAdapter extends RecyclerView.Adapter<ViewHolder> {


    private List<MineConcernData> list;
    private IMineConcernInterface listener;
    private Context mContext;

    public MineConcernAdapter(Context mContext, List<MineConcernData> list, IMineConcernInterface listener) {
        this.mContext = mContext;
        this.list = list;
        this.listener = listener;
    }

    public void notifyUI(List<MineConcernData> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.createViewHolder(mContext, parent, R.layout.item_mine_concern);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MineConcernData mineConcernData = list.get(position);
        String add_time = mineConcernData.getAdd_Time();
        String Nickname = mineConcernData.getNickname();
        String face = mineConcernData.getFace();
        String id = mineConcernData.getMember_Id();
        SwipeMenuLayout SwipeMenuLayout = holder.getView(R.id.swipe_layout);
        holder.setCircleImageView(R.id.iv_user_photo, face);
        holder.setText(R.id.tv_Nickname, CommonUtils.StringNotNull(Nickname) ? Nickname : "");
        holder.setText(R.id.tv_add_time, CommonUtils.StringNotNull(add_time) ? add_time : "");

        //取消关注
        holder.setOnClickListener(R.id.tv_delete, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    SwipeMenuLayout.quickClose();
                    listener.deleteConcern(position);
                }
            }
        });

        //查看用户
        holder.setOnClickListener(R.id.rl_content, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.checkUser(id);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
