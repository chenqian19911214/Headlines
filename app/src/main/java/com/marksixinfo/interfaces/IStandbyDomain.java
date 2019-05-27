package com.marksixinfo.interfaces;

/**
 * 备用网址
 *
 * @Auther: Administrator
 * @Date: 2019/5/25 0025 19:24
 * @Description:
 */
public interface IStandbyDomain {


    /**
     * 删除网址
     *
     * @param id
     */
    void delDomain(String id);

    /**
     * 侧滑
     */
    void showDelete(int position);
}
