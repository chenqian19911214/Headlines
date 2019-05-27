package com.marksixinfo.net.impl;

import com.marksixinfo.base.BaseNetUtil;
import com.marksixinfo.base.CallbackBase;
import com.marksixinfo.base.NetManagerBase;
import com.marksixinfo.net.api.ForumApi;

import java.util.HashMap;

import static com.marksixinfo.constants.NetUrlForum.*;
import static com.marksixinfo.constants.StringConstants.*;

/**
 * 论坛接口实现类
 *
 * @Auther: Administrator
 * @Date: 2019/4/9 0009 16:05
 * @Description:
 */
public class ForumImpl extends NetManagerBase implements ForumApi {


    public ForumImpl(CallbackBase callBack) {
        super(callBack);
    }


    /**
     * 功能描述: 论坛关注/广场
     *
     * @param: page 页码
     * @param: isSquare isSquare是否是广场页
     * @auther: Administrator
     * @date: 2019/4/9 0009 15:47
     */
    @Override
    public void getForumConcernList(String page, boolean isSquare) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, PAGE, page);
        BaseNetUtil.get(isSquare ? FORUM_LIST : FORUM_CONCERN_LIST, params, callBack);
    }


    /**
     * 功能描述: 论坛帖子详情
     *
     * @param: id 帖子id
     * @auther: Administrator
     * @date: 2019/4/9 0009 15:51
     */
    @Override
    public void getForumDetail(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.get(FORUM_DETAIL, params, callBack);
    }


    /**
     * 功能描述: 论坛帖子回复列表
     *
     * @param: id 帖子id
     * @param: page 页码
     * @auther: Administrator
     * @date: 2019/4/9 0009 15:51
     */
    @Override
    public void getForumCommentList(String id, String page) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ITEM_ID, id);
        BaseNetUtil.putStringParams(params, PAGE, page);
        BaseNetUtil.get(FORUM_DETAIL_COMMENT_LIST, params, callBack);
    }


    /**
     * 功能描述:论坛帖子点赞
     *
     * @param: 文章id
     * @auther: Administrator
     * @date: 2019/4/9 0009 15:54
     */
    @Override
    public void forumPraise(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.post(FORUM_PRAISE_ARTICLE, params, callBack);
    }

    /**
     * 功能描述:论坛回复点赞
     *
     * @param: 文章id
     * @auther: Administrator
     * @date: 2019/4/9 0009 15:54
     */
    @Override
    public void forumPraiseComment(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.post(FORUM_PRAISE_USER, params, callBack);
    }


    /**
     * 功能描述:  论坛收藏
     *
     * @param: 文章id
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:07
     */
    @Override
    public void forumFavorites(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.post(FORUM_FAVORITES, params, callBack);
    }


    /**
     * 功能描述: 回帖 - 回复帖子
     *
     * @param: 文章id ,回复内容
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:11
     */
    @Override
    public void comments(String id, String content) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.putStringParams(params, CONTENT, content);
        BaseNetUtil.post(FORUM_COMMENTS_ARTICLE, params, callBack);
    }

    /**
     * 功能描述:  回帖 - 回复用户
     *
     * @param: 文章id ,回复内容
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:11
     */
    @Override
    public void commentsUser(String id, String content) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.putStringParams(params, CONTENT, content);
        BaseNetUtil.post(FORUM_COMMENTS_USER, params, callBack);
    }

    /**
     * 功能描述:关注用户
     *
     * @param: 用户id
     * @auther: Administrator
     * @date: 2019/4/9 0009 15:55
     */
    @Override
    public void forumConcernUser(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.post(FORUM_CONCERN, params, callBack);
    }


    /**
     * 功能描述: 他人详情页(论坛分类)
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/4/9 0009 16:02
     */
    @Override
    public void forumUserPost(String userId) {
    }


    /**
     * 功能描述: 论坛个人中心
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/4/12 0012 13:18
     */
    @Override
    public void forumUserCenter() {
        BaseNetUtil.get(FORUM_USER_CENTER, null, callBack);
    }


    /**
     * 功能描述: 用户个人中心列表(论坛)
     *
     * @param: 用户id
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:13
     */
    @Override
    public void getUserCenterList(String categroyId, String memberId, String page) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, CATEGROY_ID, categroyId);
        BaseNetUtil.putStringParams(params, MEMBER_ID, memberId);
        BaseNetUtil.putStringParams(params, PAGE, page);
        BaseNetUtil.get(FORUM_USER_CENTER_LIST, params, callBack);
    }


    /**
     * 功能描述: 我的收藏
     *
     * @param: page 页码
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    @Override
    public void mineCollect(String page) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, PAGE, page);
        BaseNetUtil.get(FORUM_MINE_COLLECT, params, callBack);
    }

    /**
     * 功能描述: 我的回复
     *
     * @param: page 页码
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    @Override
    public void mineComment(String page) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, PAGE, page);
        BaseNetUtil.get(FORUM_MINE_COMMENT, params, callBack);
    }


    /**
     * 我的帖子 列表
     *
     * @param: id 分类id
     * @param: page 页码
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    @Override
    public void mineInvitationList(String id, String page) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, PAGE, page);
        BaseNetUtil.putStringParams(params, CLASS_ID, id);
        BaseNetUtil.get(FORUM_MINE_INVITATION_LIST, params, callBack);
    }


    /**
     * 我的帖子 删除
     *
     * @param: page 页码
     * @param: id 分类id
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    @Override
    public void mineInvitationDelete(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.post(FORUM_MINE_INVITATION_DELETE, params, callBack);
    }

    /**
     * 编辑帖子获取分类
     *
     * @param id
     */
    @Override
    public void getModifyData(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.get(FORUM_EDIT_GET_DATA, params, callBack);
    }


    /**
     * 功能描述: 我的回复 单条删除
     *
     * @param: page 页码
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    @Override
    public void mineDeleteComment(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.post(FORUM_MINE_DELETE_COMMENT, params, callBack);
    }

    /**
     * 功能描述: 我的收藏 取消收藏
     *
     * @param: id 帖子id
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    @Override
    public void mineDeleteCollect(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.post(FORUM_MINE_DELETE_COLLECT, params, callBack);
    }


    /**
     * 功能描述:  评论列表 - 详情
     *
     * @param: id
     * @auther: Administrator
     * @date: 2019/3/28 0028 21:22
     */
    @Override
    public void commentsListDetail(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.get(FORUM_COMMENTS_LIST_DETAIL, params, callBack);
    }

    /**
     * 功能描述:  评论列表 - 评论
     *
     * @param: id, 页码
     * @auther: Administrator
     * @date: 2019/3/28 0028 21:22
     */
    @Override
    public void commentsList(String id, String page) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.putStringParams(params, PAGE, page);
        BaseNetUtil.get(FORUM_COMMENTS_LIST, params, callBack);
    }
}
