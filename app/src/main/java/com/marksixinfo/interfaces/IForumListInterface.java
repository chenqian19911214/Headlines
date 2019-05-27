package com.marksixinfo.interfaces;

import java.util.List;

/**
 * 论坛页
 *
 * @Auther: Administrator
 * @Date: 2019/4/2 0002 00:11
 * @Description:
 */
public interface IForumListInterface {


    /**
     * 功能描述:  条目点击
     *
     * @param: view 条目
     * @param: id 位置
     * @auther: Administrator
     * @date: 2019/3/16 0016 下午 2:30
     */
    void onItemClick(int position);


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
     * 功能描述:  查看评论
     *
     * @auther: Administrator
     * @date: 2019/3/16 0016 下午 2:23
     */
    void checkComment(int position);


    /**
     * 功能描述: 查看用户
     *
     * @param: 位置
     * @auther: Administrator
     * @date: 2019/3/16 0016 下午 2:23
     */
    void checkUser(int position);


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
     * 功能描述: 点击关注
     *
     * @auther: Administrator
     * @date: 2019/3/16 0016 下午 12:51
     */
    void isConcern(int position);


    /**
     * 功能描述:回复列表
     *
     * @param: id, name
     * @auther: Administrator
     * @date: 2019/4/13 0013 20:31
     */
    void clickListComment(String id, String nickname);
}
