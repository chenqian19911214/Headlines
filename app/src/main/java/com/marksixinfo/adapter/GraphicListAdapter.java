package com.marksixinfo.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.SimpleAdapter3;
import com.marksixinfo.bean.PictureAnswerData;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.SucceedCallBackListener;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.widgets.glide.GlideUtil;

import java.util.List;

/**
 * 图解小组 list Adapter
 *
 * @Auther: Administrator
 * @Date: 2019/5/8 0008 18:56
 * @Description:
 */
public class GraphicListAdapter extends SimpleAdapter3<PictureAnswerData> {

    private SucceedCallBackListener onCollerctonItemClickListener;
    public GraphicListAdapter( ActivityIntentInterface context, List<PictureAnswerData> mData) {
        super(context, mData, R.layout.item_graphic_list);
    }

    public void setOnCollerctonItemClickListener(SucceedCallBackListener onCollerctonItemClickListener){
        this.onCollerctonItemClickListener = onCollerctonItemClickListener;
    }


    @Override
    public void convert(ViewHolder holder, PictureAnswerData item, int position) {

        ImageView imageView =holder.getView(R.id.iv_image);
        ImageView iv_user_photo =holder.getView(R.id.iv_user_photo);
        GlideUtil.loadImage(item.getImageUri(), imageView);
        GlideUtil.loadCircleImage(item.getFace(), iv_user_photo);

        String reply_Num = item.getReply_Num();
        int Num = 0;
        if (! TextUtils.isEmpty(reply_Num)){
            Num = Integer.valueOf(reply_Num);
        }
        holder.setText(R.id.tv_title,item.getTitle());
        holder.setText(R.id.tv_title_comment,item.getContent());
        holder.setText(R.id.tv_user_name,item.getNickname());
        holder.setText(R.id.tv_period_number,item.getPeriod()+"期");
        holder.setText(R.id.tv_time,item.getAdd_Time());
        TextView domain = holder.getView(R.id.tv_user_domain);
        domain.setText(Num > 0 ? CommonUtils.getThousandNumber(Num)+" 条评论" : "0 条评论");
        holder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onCollerctonItemClickListener!=null){
                    onCollerctonItemClickListener.succeedCallBack(position);
                }
            }
        });
    }
}
