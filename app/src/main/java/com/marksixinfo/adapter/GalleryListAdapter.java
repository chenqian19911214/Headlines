package com.marksixinfo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.base.SimpleAdapter2;
import com.marksixinfo.bean.GalleryListData;
import com.marksixinfo.interfaces.ActivityIntentInterface;
import com.marksixinfo.interfaces.GalleryItemClickListener;
import com.marksixinfo.interfaces.NoDoubleClickListener;
import com.marksixinfo.utils.CommonUtils;
import com.marksixinfo.utils.SPUtil;
import com.marksixinfo.utils.UIUtils;
import com.marksixinfo.widgets.GoodView;
import com.marksixinfo.widgets.ScaleImageView;
import com.marksixinfo.widgets.glide.GlideUtil;

import java.util.List;

/**
 * 图库列表
 *
 * @Auther: Administrator
 * @Date: 2019/5/8 0008 18:56
 * @Description:
 */
public class GalleryListAdapter extends SimpleAdapter2<GalleryListData.ChildBean> {

    private GalleryItemClickListener galleryItemClickListener;
    private GoodView goodView;
    private Context mContexts;
    private long lastClickTime;

    public GalleryListAdapter(Context mContext, ActivityIntentInterface context, List<GalleryListData.ChildBean> mData) {
        super(context, mData, R.layout.item_gallery_list);
        this.mContexts = mContext;
        goodView = new GoodView(mContexts);
    }

    public void setGalleryItemClickListener(GalleryItemClickListener galleryItemClickListener) {
        this.galleryItemClickListener = galleryItemClickListener;
    }

    @Override
    public void convert(ViewHolder holder, GalleryListData.ChildBean item) {
        if (item != null) {
            String pic = item.getPic();
            String title = item.getTitle();

            String widths = item.getWidth();
            String heights = item.getHeight();

            int width = 0;
            int height = 0;

            int favorites_num = 0, favorites_status = 0;
            int like_num = 0, like_status = 0;
            View view_praise, view_collect;
            float top = currentPosition == 0 || currentPosition == 1 ? 2.5f : 0;
            UIUtils.setMargins(mContext.getContext(), holder.getConvertView(),
                    UIUtils.dip2px(mContext.getContext(), 2.5f),
                    UIUtils.dip2px(mContext.getContext(), top), 0,
                    UIUtils.dip2px(mContext.getContext(), 2.5f));

            ScaleImageView imageView = holder.getView(R.id.siv_gallery_view);

            if (!TextUtils.isEmpty(widths)&& !TextUtils.isEmpty(heights)){
                 width = Integer.valueOf(widths);
                 height = Integer.valueOf(heights);
            }

            imageView.setInitSize(width, height);
            GlideUtil.loadImage(pic, imageView);
//            GlideUtil.loadImage(mContexts, pic, imageView, R.drawable.default_image_grey);
            holder.setText(R.id.tv_content, CommonUtils.StringNotNull(title) ? title : "");

            TextView praiseView = holder.getView(R.id.tv_praise);
            TextView collectView = holder.getView(R.id.tv_collect);
            view_praise = holder.getView(R.id.view_praise);
            view_collect = holder.getView(R.id.view_collect);

            GalleryListData.ChildBean.FavoritesBean favoritesBean = item.getFavorites();
            GalleryListData.ChildBean.LikeBean like = item.getLike();
            if (favoritesBean != null) {
                favorites_num = Integer.valueOf(favoritesBean.getNum());
                favorites_status = favoritesBean.getStatus();
            }
            if (like != null) {
                like_num = Integer.valueOf(like.getNum());
                like_status = like.getStatus();

            }
            //点赞数量
            praiseView.setText(like_num > 0 ? CommonUtils.getThousandNumber(like_num) : "");

            //收藏数量
            collectView.setText(favorites_num > 0 ? CommonUtils.getThousandNumber(favorites_num) : "");

            //点赞
            CommonUtils.setPraiseStatus(mContexts, praiseView, like_status);

            //收藏
            CommonUtils.setCollectStatus(mContexts, collectView, favorites_status);

            holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (galleryItemClickListener != null) {
                        galleryItemClickListener.onItemClickListener(holder.getAdapterPosition());
                    }
                }
            });
            int finalLike_status = like_status;
            praiseView.setOnClickListener(new NoDoubleClickListener() { //点赞

                @Override
                protected void onNoDoubleClick(View v) {
                    lastClickTime = getLastClickTime();
                    if (galleryItemClickListener != null) {
                        if (finalLike_status != 1) {
                            goodView.setTextColor(mContexts.getResources().getColor(R.color.colorPrimary)).setText("+1").show(view_praise);
                        }
                        galleryItemClickListener.like(holder.getAdapterPosition(), finalLike_status != 1);
                    }
                }
            }.setLastClickTime(lastClickTime));

            int finalFavorites_status = favorites_status;
            collectView.setOnClickListener(new NoDoubleClickListener() { //收藏

                @Override
                protected void onNoDoubleClick(View v) {
                    lastClickTime = getLastClickTime();
                    if (galleryItemClickListener != null) {
                        if (finalFavorites_status != 1 && CommonUtils.StringNotNull(SPUtil.getToken(mContexts))) {
                            goodView.setTextInfo("收藏成功", 0xffefa301, 12).show(view_collect);
                        }
                        galleryItemClickListener.collection(holder.getAdapterPosition(), finalFavorites_status != 1);
                    }
                }
            }.setLastClickTime(lastClickTime));
        }
    }
}
