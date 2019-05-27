package com.marksixinfo.interfaces;

import java.util.List;

/**
 *  个人中心,我的回复
 *
 * @Auther: Administrator
 * @Date: 2019/3/27 0027 22:12
 * @Description:
 */
public interface IMinCommentInterface {

    /**
     * 删除回复
     *
     * @param groupPosition
     * @param childPosition
     * @param id
     */
    void deleteComment(int groupPosition, int childPosition, String id);


    /**
     * 查看帖子,回复位置
     *
     * @param id
     */
    void checkComment(String id);


    /**
     * 查看帖子
     *
     * @param id
     */
    void checkDetail(String id);


    /**
     * 功能描述:  点击图片查看
     *
     * @param: 图片url集合
     * @param: 选中位置
     * @auther: Administrator
     * @date: 2019/3/16 0016 下午 2:21
     */
    void clickPhoto(List<String> list, int index);

}
