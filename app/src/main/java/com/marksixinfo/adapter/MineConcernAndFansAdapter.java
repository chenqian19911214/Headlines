package com.marksixinfo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.bean.MineConcernData;
import com.marksixinfo.interfaces.IMineConcernInterface;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.ConcernTextView;

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
public class MineConcernAndFansAdapter extends RecyclerView.Adapter<ViewHolder> {


    private List<MineConcernData> list;
    private IMineConcernInterface listener;
    private Context mContext;

    public MineConcernAndFansAdapter(Context mContext, List<MineConcernData> list, IMineConcernInterface listener) {
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
        return ViewHolder.createViewHolder(mContext, parent, R.layout.item_mine_concern_and_fans);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MineConcernData mineConcernData = list.get(position);
        String add_time = mineConcernData.getAdd_Time();
        String Nickname = mineConcernData.getNickname();
        String face = mineConcernData.getFace();
        String id = mineConcernData.getMember_Id();
        int level = mineConcernData.getLevel();
        int look_status = mineConcernData.getLook_status();
        String remark = mineConcernData.getRemark();
        boolean hasViewLine = mineConcernData.isHasViewLine();

        ImageView mIvOfficial = holder.getView(R.id.iv_official);
        ConcernTextView mTvConcern = holder.getView(R.id.tv_concern);
        TextView mTvUserDomain = holder.getView(R.id.tv_user_domain);

        holder.setVisible(R.id.view_line, position == 0 && !hasViewLine);

        holder.setCircleImageView(R.id.iv_user_photo, face);

        holder.setText(R.id.tv_user_name, CommonUtils.StringNotNull(Nickname) ? CommonUtils.CommHandleText(Nickname) : "");
        holder.setText(R.id.tv_time, CommonUtils.StringNotNull(add_time) ? add_time : "");

        //网址
        mTvUserDomain.setText(CommonUtils.StringNotNull(remark) ? CommonUtils.CommHandleText(remark) : "");
        mTvUserDomain.setVisibility(CommonUtils.StringNotNull(remark) ? View.VISIBLE : View.GONE);

        //官方
        CommonUtils.setUserLevel(mIvOfficial, level);

        //设置关注
        mTvConcern.setLookStatus(look_status);

        //取消关注
        mTvConcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null && mTvConcern.getLook_status() != -1) {
                    if (mTvConcern.startLookStatus()) {
                        listener.deleteConcern(position);
                    }
                }
            }
        });

        //查看用户
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
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
