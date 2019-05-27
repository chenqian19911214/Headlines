package com.marksixinfo.interfaces;

import android.view.View;

import java.util.List;

/**
 *  我的收藏
 *
 * @Auther: Administrator
 * @Date: 2019/3/27 0027 14:06
 * @Description:
 */
public interface ISimpleMineInter {


    /**
     * 功能描述:  条目点击
     *
     * @param: view 条目
     * @param: id 位置
     * @auther: Administrator
     * @date: 2019/3/16 0016 下午 2:30
     */
    void onItemClick(View view, int position);


    /**
     * 功能描述:  点击图片查看
     *
     * @param: 图片url集合
     * @param: 选中位置
     * @auther: Administrator
     * @date: 2019/3/16 0016 下午 2:21
     */
    void clickPhoto(List<String> list, int index);

    /**
     *
     * 功能描述: 编辑帖子,编辑分类
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/3/27 0027 14:12
     */
    void changeInvitation(int position);


    /**
     *
     * 功能描述: 取消收藏
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/3/27 0027 21:15
     */
    void cancelFavorites(int position);
}
