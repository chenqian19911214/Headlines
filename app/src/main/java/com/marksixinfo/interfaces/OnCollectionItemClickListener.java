package com.marksixinfo.interfaces;

/**
 * Created by Administrator on 2019/5/11.
 */

public interface OnCollectionItemClickListener {

    /**
     * 点击关注
     *
     * @param position
     * @param: 是否关注
     */
    void isConcern(int position, boolean isConcern);
    /**
     * 功能描述: 点赞
     * @param position
     * @param: 是否点赞
     */
    void checkPraise(int position, boolean isPraise);

    /**
     * Item 点击事件
     * */
    void onItemClickListener(int position);
}
