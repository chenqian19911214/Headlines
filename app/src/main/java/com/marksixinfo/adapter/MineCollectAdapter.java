package com.marksixinfo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.marksixinfo.R;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.interfaces.ISimpleMineInter;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.ninegridimg.ItemImageClickListener;
import com.marksixinfo.widgets.ninegridimg.NineGridImageView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 个人中心,我的收藏
 *
 * @Auther: Administrator
 * @Date: 2019/3/27 0027 13:59
 * @Description:
 */
public class MineCollectAdapter extends RecyclerView.Adapter<ViewHolder> {


    private List<MainHomeData> list;
    private ISimpleMineInter mISimpleMineInter;
    private Context context;
    private boolean isRounded;

    public MineCollectAdapter(Context context, List<MainHomeData> list, boolean isRounded, ISimpleMineInter mISimpleMineInter) {
        this.context = context;
        this.list = list;
        this.isRounded = isRounded;
        this.mISimpleMineInter = mISimpleMineInter;
    }

    public void notifyUI(List<MainHomeData> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return ViewHolder.createViewHolder(context, parent, R.layout.item_mine_collect);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MainHomeData mainHomeData = list.get(position);

        String face = mainHomeData.getFace();
        String Nickname = mainHomeData.getNickname();
        List<String> pic = mainHomeData.getPic();
        String title = mainHomeData.getTitle();
        String content = mainHomeData.getContent();
        String fav_time = mainHomeData.getFav_time();


        holder.setCircleImageView(R.id.tv_user_photo, face);

        holder.setText(R.id.tv_Nickname, CommonUtils.StringNotNull(Nickname) ? Nickname : "");

        holder.setText(R.id.tv_favorites_time, CommonUtils.StringNotNull(fav_time) ? fav_time : "");

        holder.setVisible(R.id.tv_title, CommonUtils.StringNotNull(title));
        holder.setText(R.id.tv_title, CommonUtils.StringNotNull(title) ? title : "");

        holder.setVisible(R.id.tv_content, CommonUtils.StringNotNull(content));
        holder.setText(R.id.tv_content, CommonUtils.StringNotNull(content) ? content : "");

        NineGridImageView mPaLinearLayout = holder.getView(R.id.pa_LinearLayout);

        //查看图片
        if (CommonUtils.ListNotNull(pic)) {
            mPaLinearLayout.setVisibility(View.VISIBLE);

            //最多3条
            if (pic.size() > 3) {
                pic = pic.subList(0, 3);
            }
            //图片集合
            mPaLinearLayout.setImagesData(isRounded, pic, new ItemImageClickListener<String>() {
                @Override
                public void onItemImageClick(Context context, ImageView imageView, int index, List<String> list) {
                    if (mISimpleMineInter != null) {
                        mISimpleMineInter.clickPhoto(list, index);
                    }
                }
            });
        } else {
            mPaLinearLayout.setVisibility(View.GONE);
        }

        //取消收藏
        holder.setOnClickListener(R.id.iv_cancel_favorites, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mISimpleMineInter != null) {
                    mISimpleMineInter.cancelFavorites(position);
                }
            }
        });

        //帖子详情
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mISimpleMineInter != null) {
                    mISimpleMineInter.onItemClick(view, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
}
