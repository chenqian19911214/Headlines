package com.marksixinfo.interfaces;

import android.view.View;

import java.util.List;

/**
 * 首页逻辑事件处理
 *
 * @Auther: Administrator
 * @Date: 2019/3/16 0016 12:47
 * @Description:
 */
public interface IMainHomeRecommend {


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
     * 功能描述: 点击关注
     *
     * @param: 是否关注
     * @auther: Administrator
     * @date: 2019/3/16 0016 下午 12:51
     */
    void isConcern(int position, boolean isConcern);


    /**
     * 功能描述: 更多
     *
     * @param: id 位置
     * @auther: Administrator
     * @date: 2019/3/16 0016 下午 12:53
     */
    void moreContent(int position);


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
     * 功能描述: 点赞
     *
     * @param: 是否点赞
     * @auther: Administrator
     * @date: 2019/3/16 0016 下午 2:23
     */
    void checkPraise(int position, boolean isPraise);

    /**
     * 功能描述:  点击评论
     *
     * @auther: Administrator
     * @date: 2019/3/16 0016 下午 2:23
     */
    void clickComment(int position);


    /**
     * 功能描述: 收藏
     *
     * @param: 是否收藏
     * @auther: Administrator
     * @date: 2019/3/16 0016 下午 2:23
     */
    void checkCollect(int position, boolean isCollect);


    /**
     * 功能描述: 查看发帖人
     *
     * @param: 发帖人id
     * @auther: Administrator
     * @date: 2019/3/21 0021 20:20
     */
    void clickUser(String userId);
}


