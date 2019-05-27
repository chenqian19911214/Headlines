package com.marksixinfo.net.api;

import java.util.List;

/**
 * 数组提交
 *
 * @Auther: Administrator
 * @Date: 2019/4/25 0025 00:09
 * @Description:
 */
public interface CommArrayApi {


    /**
     * 头条发布
     *
     * @param array
     * @param title
     * @param content
     * @param currentId
     * @param periodValue
     */
    void headlineRelease(List<String> array, String title, String content, String currentId, String periodValue);


    /**
     * 论坛发布/编辑
     *
     * @param array
     * @param title
     * @param content
     * @param id      id不为空则为编辑
     */
    void forumRelease(List<String> array, String title, String content, String id);


    /**
     * 我的收藏/评论/点赞/历史  删除记录
     *
     * @param array [1,2,3],正常删除  [-1],删除所有
     * @param type
     */
    void concernAndHistoryDelete(List<String> array, String type);

}
