package com.marksixinfo.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.SimpleAdapter2;
import com.marksixinfo.bean.MainHomeData;
import com.marksixinfo.bean.MyCollectionData;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.GalleryItemClickListener;
import com.marksixinfo.interfaces.NoDoubleClickListener;
import com.marksixinfo.interfaces.OnCollectionItemClickListener;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.GoodView;
import com.marksixinfo.widgets.ScaleImageView;
import com.marksixinfo.widgets.glide.GlideUtil;

import java.util.List;

/**
 * 图库我的收藏 Adapter
 *
 * @Auther: Administrator
 * @Date: 2019/5/8 0008 18:56
 * @Description:
 */
public class GalleryMyCollectionListAdapter extends SimpleAdapter2<MyCollectionData> {

    private Context context;
    private long lastClickTime;
    private GoodView goodView;

    private GalleryItemClickListener onCollerctonItemClickListener;

    public GalleryMyCollectionListAdapter(Context mcontext,ActivityIntentInterface context, List<MyCollectionData> mData) {
        super(context, mData, R.layout.item_gallery_list);
        this.context = mcontext;
        goodView = new GoodView(mcontext);
    }

    public void notifyUI(List<MyCollectionData> list) {
        this.mData = list;
        notifyDataSetChanged();
    }
    public void setOnCollerctonItemClickListener(GalleryItemClickListener onCollerctonItemClickListener){
        this.onCollerctonItemClickListener = onCollerctonItemClickListener;
    }
    @Override
    public void convert(ViewHolder holder, MyCollectionData item) {
        if (item != null) {
            String pic = item.getPic();
            String title = item.getTitle();
            int like_num = Integer.parseInt(item.getLike().getNum());
            int favorites_num = item.getFavorites().getNum();
            int favorites =item.getFavorites().getStatus();
            int flikes =item.getLike().getStatus();
            float top = currentPosition == 0 || currentPosition == 1 ? 2.5f : 0;
            UIUtils.setMargins(mContext.getContext(), holder.getConvertView(),
                    UIUtils.dip2px(mContext.getContext(), 2.5f),
                    UIUtils.dip2px(mContext.getContext(), top), 0,
                    UIUtils.dip2px(mContext.getContext(), 2.5f));
            ScaleImageView imageView = holder.getView(R.id.siv_gallery_view);
            imageView.setInitSize(Integer.valueOf(item.getWidth()),Integer.valueOf(item.getHeight()));
            GlideUtil.loadImage(pic, imageView);
            holder.setText(R.id.tv_content, CommonUtils.StringNotNull(title) ? title : "");
            //点赞数量
            TextView likeText = holder.getView(R.id.tv_praise);
            TextView praiseText = holder.getView(R.id.tv_collect);
            View  viewPraise = holder.getView(R.id.view_praise);
            holder.setText(R.id.tv_praise, like_num > 0 ? CommonUtils.getThousandNumber(like_num) : "");
            //收藏数量
            holder.setText(R.id.tv_collect, favorites_num > 0 ? CommonUtils.getThousandNumber(favorites_num) : "");

            CommonUtils.setPraiseStatus(context, likeText, flikes);
            CommonUtils.setCollectStatus(context, praiseText, favorites);
            //点赞
            likeText.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    lastClickTime = getLastClickTime();
                    if (onCollerctonItemClickListener != null) {
                        if (flikes != 1) {
                            goodView.setTextColor(context.getResources().getColor(R.color.colorPrimary)).setText("+1").show(viewPraise);
                        }
                        onCollerctonItemClickListener.like(holder.getAdapterPosition(), flikes != 1);
                    }
                }
            }.setLastClickTime(lastClickTime));
            //收藏
            praiseText.setOnClickListener(new NoDoubleClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    lastClickTime = getLastClickTime();
                    if (onCollerctonItemClickListener != null) {
                        if (favorites != 1 && CommonUtils.StringNotNull(SPUtil.getToken(context))) {
                            goodView.setTextInfo("收藏成功", 0xffefa301, 12).show(praiseText);
                        }
                        onCollerctonItemClickListener.collection(holder.getAdapterPosition(),favorites!=1);
                    }
                }
            }.setLastClickTime(lastClickTime));

            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onCollerctonItemClickListener!=null){
                        onCollerctonItemClickListener.onItemClickListener(holder.getAdapterPosition());
                    }
                }
            });
        }
    }
}
