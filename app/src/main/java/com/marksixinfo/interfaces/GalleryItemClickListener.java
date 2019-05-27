package com.marksixinfo.interfaces;

/**
 * Created by Administrator on 2019/5/11.
 * 火山图库瀑布点击事件
 * 包括收藏，彩色和黑白
 */

public interface GalleryItemClickListener {
    /**
     * Item 点击事件
     * */
    void onItemClickListener(int position);
    /**
     * 点赞
     * @param position
     * @param isLike
     */
    void like(int position, boolean isLike);

    /**
     * 点赞
     *
     * @param position
     * @param isCollection
     */
    void collection(int position, boolean isCollection);
}
