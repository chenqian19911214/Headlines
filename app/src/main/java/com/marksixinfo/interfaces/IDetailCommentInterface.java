package com.marksixinfo.interfaces;

/**
 * 帖子详情
 *
 * @Auther: Administrator
 * @Date: 2019/3/28 0028 17:52
 * @Description:
 */
public interface IDetailCommentInterface {

    /**
     * 查看用户
     *
     * @param id
     */
    void checkUser(String id);


    /**
     * 点赞
     *
     * @param position
     * @param isPraise 是否点赞
     */
    void praise(int position, boolean isPraise);


    /**
     * 回复用户
     *
     * @param position
     * @param hasComment 是否有回复
     */
    void commentUser(int position, boolean hasComment);

}
