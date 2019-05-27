package com.marksixinfo.interfaces;

/**
 *  个人中心,我的关注
 *
 * @Auther: Administrator
 * @Date: 2019/3/27 0027 22:46
 * @Description:
 */
public interface IMineConcernInterface {

    /**
     *  取消关注
     * @param position
     */
    void deleteConcern(int position);

    /**
     * 查看用户
     * @param id
     */
    void checkUser(String id);
}
