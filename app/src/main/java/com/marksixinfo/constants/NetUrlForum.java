package com.marksixinfo.constants;


/**
 * 论坛url
 *
 * @Auther: Administrator
 * @Date: 2019/5/9 0009 13:54
 * @Description:
 */
public interface NetUrlForum {


    /**
     * 论坛host
     */
//     String FORUM_HOST = "https://www.34399.com/api/?C=";
//    String FORUM_HOST = "http://hongkong.sskk58.com/api/?C=";
    String FORUM_HOST = "http://api.bbs." + StringConstants.Main_Host + "?C=";


    /**
     * 论坛 - 广场列表
     */
    String FORUM_LIST = FORUM_HOST + "index&A=get_list";


    /**
     * 论坛 - 关注列表
     */
    String FORUM_CONCERN_LIST = FORUM_HOST + "index&A=my_look";


    /**
     * 论坛 - 详情页
     */
    String FORUM_DETAIL = FORUM_HOST + "bbs&A=details";


    /**
     * 论坛 - 详情页回复列表
     */
    String FORUM_DETAIL_COMMENT_LIST = FORUM_HOST + "bbs&A=get_reply_list";


    /**
     * 论坛  - 关注用户(同头条一致)
     */
    String FORUM_CONCERN = NetUrlHeadline.HOST + "bbs&A=add_look";


    /**
     * 论坛 - 文章点赞
     */
    String FORUM_PRAISE_ARTICLE = FORUM_HOST + "bbs&A=like";

    /**
     * 论坛 - 回复点赞
     */
    String FORUM_PRAISE_USER = FORUM_HOST + "bbs&A=like_reply";


    /**
     * 论坛  - 文章收藏
     */
    String FORUM_FAVORITES = FORUM_HOST + "bbs&A=add_favorites";


    /**
     * 论坛 - 回复文章
     */
    String FORUM_COMMENTS_ARTICLE = FORUM_HOST + "bbs&A=reply";


    /**
     * 论坛 - 回复用户
     */
    String FORUM_COMMENTS_USER = FORUM_HOST + "bbs&A=replys";

    /**
     * 论坛 - 论坛个人中心
     */
    String FORUM_USER_CENTER = FORUM_HOST + "ucenter";


    /**
     * 用户个人中心 论坛 帖子列表
     */
    String FORUM_USER_CENTER_LIST = FORUM_HOST + "home&A=get_list";


    /**
     * 评论列表 - 详情
     */
    String FORUM_COMMENTS_LIST_DETAIL = FORUM_HOST + "bbs&A=reply_details";

    /**
     * 评论列表 - 评论
     */
    String FORUM_COMMENTS_LIST = FORUM_HOST + "bbs&A=get_reply_child";


    /**
     * 论坛发布
     */
    String FORUM_RELEASE_CONTENT = FORUM_HOST + "bbs&A=pub";

    /**
     * 论坛 -  我的收藏
     */
    String FORUM_MINE_COLLECT = FORUM_HOST + "ucenter&A=my_favorites";


    /**
     * 论坛 -  我的回复
     */
    String FORUM_MINE_COMMENT = FORUM_HOST + "ucenter&A=my_reply";


    /**
     * 论坛 - 我的帖子
     */
    String FORUM_MINE_INVITATION_LIST = FORUM_HOST + "ucenter&A=my_item";


    /**
     * 我的帖子 删除帖子 https://api.bbs.sskk58.com/?C=ucenter&A=modify&id=6382&user_id=3796&key=e21ed2e18ec72dcbdfbe17c03a2d7835&_=1555076854011
     */
    String FORUM_MINE_INVITATION_DELETE = FORUM_HOST + "ucenter&A=del_item";

    /**
     * 我的帖子 编辑 获取数据
     */
    String FORUM_EDIT_GET_DATA = FORUM_HOST + "ucenter&A=modify";


    /**
     * 我的帖子 编辑发布
     */
    String FORUM_EDIT_RELEASE = FORUM_HOST + "ucenter&A=modify_item";


    /**
     * 我的收藏 - 取消收藏
     */
    String FORUM_MINE_DELETE_COLLECT = FORUM_HOST + "ucenter&A=del_favorites";


    /**
     * 我的回复-单条删除
     */
    String FORUM_MINE_DELETE_COMMENT = FORUM_HOST + "ucenter&A=del_reply";


}
