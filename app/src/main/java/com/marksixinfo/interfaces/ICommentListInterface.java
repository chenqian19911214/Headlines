package com.marksixinfo.interfaces;

/**
 * 回复用户列表
 *
 * @Auther: Administrator
 * @Date: 2019/3/29 0029 00:06
 * @Description:
 */
public interface ICommentListInterface {

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
     * @param isPraise
     */
    void praise(int position, boolean isPraise);
}
