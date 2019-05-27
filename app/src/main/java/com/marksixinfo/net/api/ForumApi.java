package com.marksixinfo.net.api;

/**
 * 论坛接口
 *
 * @Auther: Administrator
 * @Date: 2019/4/9 0009 15:46
 * @Description:
 */
public interface ForumApi {


    /**
     * 功能描述: 论坛关注/广场
     *
     * @param: page 页码
     * @param: isSquare isSquare是否是广场页
     * @auther: Administrator
     * @date: 2019/4/9 0009 15:47
     */
    void getForumConcernList(String page, boolean isSquare);


    /**
     * 功能描述: 论坛帖子详情
     *
     * @param: id 帖子id
     * @auther: Administrator
     * @date: 2019/4/9 0009 15:51
     */
    void getForumDetail(String id);


    /**
     * 功能描述: 论坛帖子回复列表
     *
     * @param: id 帖子id
     * @param: page 页码
     * @auther: Administrator
     * @date: 2019/4/9 0009 15:51
     */
    void getForumCommentList(String id, String page);


    /**
     * 功能描述:论坛帖子点赞
     *
     * @param: 文章id
     * @auther: Administrator
     * @date: 2019/4/9 0009 15:54
     */
    void forumPraise(String id);

    /**
     * 功能描述:论坛回复点赞
     *
     * @param: 文章id
     * @auther: Administrator
     * @date: 2019/4/9 0009 15:54
     */
    void forumPraiseComment(String id);


    /**
     * 功能描述: 回帖 - 回复帖子
     *
     * @param: 文章id ,回复内容 是否回复用户
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:11
     */
    void comments(String id, String content);


    /**
     * 功能描述: 回帖 - 回复用户
     *
     * @param: 文章id ,回复内容 是否回复用户
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:11
     */
    void commentsUser(String id, String content);


    /**
     * 功能描述:  收藏
     *
     * @param: 文章id
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:07
     */

    void forumFavorites(String id);


    /**
     * 功能描述:关注用户
     *
     * @param: 用户id
     * @auther: Administrator
     * @date: 2019/4/9 0009 15:55
     */
    void forumConcernUser(String id);


    /**
     * 功能描述: 他人详情页(论坛分类)
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/4/9 0009 16:02
     */
    void forumUserPost(String userId);


    /**
     * 功能描述: 论坛个人中心
     *
     * @auther: Administrator
     * @date: 2019/4/12 0012 13:18
     */
    void forumUserCenter();


    /**
     * 功能描述: 用户个人中心列表(论坛)
     *
     * @param: 用户id
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:13
     */
    void getUserCenterList(String categroyId, String memberId, String page);


    /**
     * 功能描述: 我的收藏
     *
     * @param: page 页码
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    void mineCollect(String page);


    /**
     * 功能描述: 我的回复
     *
     * @param: page 页码
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    void mineComment(String page);


    /**
     * 我的帖子 列表
     *
     * @param: page 页码
     * @param: id 分类id
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    void mineInvitationList(String page, String id);


    /**
     * 我的帖子 列表
     *
     * @param: page 页码
     * @param: id 分类id
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    void mineInvitationDelete(String id);


    /**
     * 编辑帖子获取分类
     *
     * @param id
     */
    void getModifyData(String id);


    /**
     * 功能描述: 我的收藏 取消收藏
     *
     * @param: id 帖子id
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    void mineDeleteCollect(String id);


    /**
     * 功能描述: 我的回复 单条删除
     *
     * @param: page 页码
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    void mineDeleteComment(String id);

    /**
     * 功能描述:  评论列表 - 详情
     *
     * @param: id
     * @auther: Administrator
     * @date: 2019/3/28 0028 21:22
     */
    void commentsListDetail(String id);


    /**
     * 功能描述:  评论列表 - 评论
     *
     * @param: id, 页码
     * @auther: Administrator
     * @date: 2019/3/28 0028 21:22
     */
    void commentsList(String id, String page);


}
