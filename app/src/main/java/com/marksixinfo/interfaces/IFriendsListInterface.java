package com.marksixinfo.interfaces;

/**
 * 好友列表
 *
 * @Auther: Administrator
 * @Date: 2019/4/23 0023 17:20
 * @Description:
 */
public interface IFriendsListInterface {


    /**
     * 功能描述: 查看用户
     *
     * @param: 用户id
     * @auther: Administrator
     * @date: 2019/4/23 0023 17:21
     */
    void clickUser(String userId);


    /**
     * 功能描述:  提醒用户
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/4/23 0023 17:21
     */
    void remindUser(String userId, int position);
}
