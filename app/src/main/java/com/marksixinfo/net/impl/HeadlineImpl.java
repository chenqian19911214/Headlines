package com.marksixinfo.net.impl;

import com.marksixinfo.base.BaseNetUtil;
import com.marksixinfo.base.CallbackBase;
import com.marksixinfo.base.NetManagerBase;
import com.marksixinfo.net.api.HeadlineApi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import static com.marksixinfo.constants.NetUrlHeadline.*;
import static com.marksixinfo.constants.StringConstants.*;

/**
 * 头条接口实现类
 *
 * @Auther: Administrator
 * @Date: 2019/3/19 0019 19:57
 * @Description:
 */
public class HeadlineImpl extends NetManagerBase implements HeadlineApi {


    public HeadlineImpl(CallbackBase callBack) {
        super(callBack);
    }


    /**
     * 功能描述:  图片上传接口
     *
     * @param: type 文件类型 1,base64   2,base64(带缩略图)
     * @param: imageBase64 base64
     * @auther: Administrator
     * @date: 2019/3/22 0022 20:20
     */
    @Override
    public void imagesUploading(String imageBase64) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, WIDTH, RELEASE_IMAGE_WIDTH);
        BaseNetUtil.putStringParams(params, HEIGHT, RELEASE_IMAGE_HEIGHT);
        BaseNetUtil.putStringParams(params, TYPE, "2");
        BaseNetUtil.putStringParams(params, VALUE, imageBase64);
        BaseNetUtil.post(false, IMAGES_UPLOADING, params, callBack);
    }

    /**
     * 功能描述: 登录
     *
     * @return: 账号, 密码
     * @auther: Administrator
     * @date: 2019/3/19 0019 下午 7:57
     */
    @Override
    public void login(String account, String password) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ACCOUNT, account);
        BaseNetUtil.putStringParams(params, PASSWORD, password);
        BaseNetUtil.post(LOGIN, params, callBack);
    }

    /**
     * 功能描述: 发送验证码
     *
     * @param: 手机号
     * @auther: Administrator
     * @date: 2019/3/20 0020 上午 10:27
     */
    @Override
    public void sendAuthCode(String phone) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, PHONE, phone);
        BaseNetUtil.post(SEND_AUTH_CODE, params, callBack);
    }

    /**
     * 功能描述: 注册
     *
     * @param: 昵称, 手机号, 验证码, 密码,邀请码
     * @auther: Administrator
     * @date: 2019/3/20 0020 上午 10:36
     */
    @Override
    public void register(String mNickname, String mPhone
            , String mAuthCode, String mPassword, String mInviteCode) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, NAME, mNickname);
        BaseNetUtil.putStringParams(params, PHONE, mPhone);
        BaseNetUtil.putStringParams(params, CODE, mAuthCode);
        BaseNetUtil.putStringParams(params, PASSWORD, mPassword);
        BaseNetUtil.putStringParams(params, INVITE_CODE, mInviteCode);
        BaseNetUtil.post(REGISTER, params, callBack);
    }

    /**
     * 功能描述: 获取推荐标识
     *
     * @param: 页码
     * @auther: Administrator
     * @date: 2019/3/20 0020 上午 10:36
     */
    @Override
    public void getRecommendTag() {
        BaseNetUtil.get(GET_RECOMMEND_TAG, null, callBack);
    }


    /**
     * 功能描述: 推荐
     *
     * @param: recommendTag 推荐标识
     * @param: type  0,下拉刷新 1,上拉加载
     * @auther: Administrator
     * @date: 2019/3/20 0020 上午 10:36
     */
    @Override
    public void recommend(String recommendTag, String type) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, SIGNATURE, recommendTag);
        BaseNetUtil.putStringParams(params, TYPE, type);
        BaseNetUtil.get(RECOMMEND, params, callBack);
    }


    /**
     * 功能描述: 关注列表
     *
     * @param: 页码
     * @auther: Administrator
     * @date: 2019/3/20 0020 上午 10:36
     */
    @Override
    public void concern(String page) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, PAGE, page);
        BaseNetUtil.get(CONCERN_LIST, params, callBack);
    }


    /**
     * 功能描述:  点赞
     *
     * @param: 文章id
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:07
     */
    @Override
    public void praise(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.post(PRAISE, params, callBack);
    }


    /**
     * 功能描述:  点赞用户评论
     *
     * @param: 文章id
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:07
     */
    @Override
    public void praiseComment(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.post(PRAISE_COMMENT, params, callBack);
    }

    /**
     * 功能描述:  收藏
     *
     * @param: 文章id
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:07
     */
    @Override
    public void favorites(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.post(FAVORITES, params, callBack);
    }

    /**
     * 功能描述: 关注用户
     *
     * @param: id
     * @auther: Administrator
     * @date: 2019/3/20 0020 上午 10:36
     */
    @Override
    public void concernUser(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.post(CONCERN_USER, params, callBack);
    }

    /**
     * 功能描述: 我的关注-取消关注
     *
     * @param: page 页码
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    @Override
    public void mineConcernCancel(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.post(MINE_CONCERN_CANCEL, params, callBack);
    }


    /**
     * 功能描述:  热门搜索关键词
     *
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:07
     */
    @Override
    public void getSearchHot() {
        BaseNetUtil.get(SEARCH_HOT, null, callBack);
    }


    /**
     * 功能描述: 搜索
     *
     * @param: 搜索关键字, 页码
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:10
     */
    @Override
    public void getSearchResult(String keyword, int page,String period) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, PAGE, String.valueOf(page));
        BaseNetUtil.putStringParams(params, PERIOD, period);
        try {
            String encode = URLEncoder.encode(keyword, "utf-8");
            BaseNetUtil.putStringParams(params, KEYWORD, encode);
            BaseNetUtil.get(GET_SEARCH_USER_RESULT, params, callBack);
//            BaseNetUtil.get(SEARCH_RESULT, params, callBack);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    /**
     * 功能描述: 搜索获取期数
     *
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:10
     */
    @Override
    public void getSearchPeriod() {
        BaseNetUtil.get(GET_SEARCH_PERIOD, null, callBack);
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
        BaseNetUtil.post(COMMENTS, params, callBack);
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
        BaseNetUtil.post(COMMENTS_USER, params, callBack);
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
        BaseNetUtil.get(COMMENTS_LIST_DETAIL, params, callBack);
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
        BaseNetUtil.get(COMMENTS_LIST, params, callBack);
    }


    /**
     * 功能描述: 用户个人中心
     *
     * @param: 用户id
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:13
     */
    @Override
    public void getUserCenter(String userId) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, MEMBER_ID, userId);
        BaseNetUtil.get(USER_CENTER, params, callBack);
    }


    /**
     * 功能描述: 用户个人中心分类
     *
     * @param: 用户id
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:13
     */
    @Override
    public void getUserCenterType(String userId) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, MEMBER_ID, userId);
        BaseNetUtil.get(USER_CENTER_type, params, callBack);
    }

    /**
     * 功能描述: 用户个人中心列表
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
        BaseNetUtil.get(USER_CENTER_LIST, params, callBack);
    }


    /**
     * 功能描述: 发布获取分类及期数
     *
     * @auther: Administrator
     * @date: 2019/3/22 0022 15:38
     */
    @Override
    public void getSelectClassify() {
        BaseNetUtil.post(GET_SELECT_CLASSIFY, null, callBack);
    }

    /**
     * 功能描述:添加分类
     *
     * @param: name 分类名称
     * @auther: Administrator
     * @date: 2019/3/22 0022 16:16
     */
    @Override
    public void addSelectClassify(String name) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, NAME, name);
        BaseNetUtil.post(ADD_SELECT_CLASSIFY, params, callBack);
    }

    /**
     * 功能描述:修改分类
     *
     * @param: name 分类名称
     * @auther: Administrator
     * @date: 2019/3/22 0022 16:16
     */
    @Override
    public void changeSelectClassify(String name, String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.putStringParams(params, NAME, name);
        BaseNetUtil.post(CHANGE_SELECT_CLASSIFY, params, callBack);
    }

    /**
     * 功能描述:删除分类
     *
     * @param: name 分类名称
     * @auther: Administrator
     * @date: 2019/3/22 0022 16:16
     */
    @Override
    public void deleteSelectClassify(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.post(DELETE_SELECT_CLASSIFY, params, callBack);
    }

    /**
     * 功能描述:排序分类
     *
     * @param: sortList 分类id集合
     * @auther: Administrator
     * @date: 2019/3/22 0022 16:16
     */
    @Override
    public void sortSelectClassify(String sortList) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, SEQ, sortList);
        BaseNetUtil.post(SORT_SELECT_CLASSIFY, params, callBack);
    }


    /**
     * 功能描述:分类操作完成刷新
     *
     * @auther: Administrator
     * @date: 2019/3/22 0022 16:16
     */
    @Override
    public void setSelectClassify() {
        BaseNetUtil.get(SET_SELECT_CLASSIFY, null, callBack);
    }


    /**
     * 功能描述: 文章详情
     *
     * @param: 文章id
     * @auther: Administrator
     * @date: 2019/3/23 0023 12:12
     */
    @Override
    public void getDetail(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.get(DETAIL, params, callBack);
    }

    /**
     * 功能描述: 获取下一期
     *
     * @param: 文章id
     * @auther: Administrator
     * @date: 2019/3/23 0023 12:12
     */
    @Override
    public void getDetailNext(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.get(DETAIL_NEXT, params, callBack);
    }


    /**
     * 功能描述: 文章详情 - 回复列表
     *
     * @param: 文章id
     * @param: 页码
     * @auther: Administrator
     * @date: 2019/3/23 0023 12:12
     */
    @Override
    public void getDetailCommentList(String item_id, String page) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ITEM_ID, item_id);
        BaseNetUtil.putStringParams(params, PAGE, page);
        BaseNetUtil.get(DETAIL_CONTENT_LIST, params, callBack);
    }


    /**
     * 我的: 个人中心
     *
     * @auther: Administrator
     * @date: 2019/3/23 0023 12:19
     */
    @Override
    public void mineUserCenter() {
        BaseNetUtil.get(MINE_USER_CENTER, null, callBack);
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
        BaseNetUtil.get(MINE_COMMENT, params, callBack);
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
        BaseNetUtil.post(MINE_DELETE_COMMENT, params, callBack);
    }

    /**
     * 功能描述:  我的关注 / 粉丝
     *
     * @param: page 页码 type 1:我关注的 2:我的粉丝
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    @Override
    public void mineConcern(String page, String type) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, PAGE, page);
        BaseNetUtil.putStringParams(params, TYPE, type);
        BaseNetUtil.get(MINE_CONCERN, params, callBack);
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
        BaseNetUtil.get(MINE_COLLECT, params, callBack);
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
        BaseNetUtil.post(MINE_DELETE_COLLECT, params, callBack);
    }

    /**
     * 功能描述: 我的帖子 分类
     *
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    @Override
    public void mineInvitationCategory() {
        BaseNetUtil.get(MINE_INVITATION_CATEGORY, null, callBack);
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
        BaseNetUtil.get(MINE_INVITATION_LIST, params, callBack);
    }

    /**
     * 我的帖子 编辑分类
     *
     * @param: id 帖子id
     * @param: categoryId 分类id
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    @Override
    public void mineInvitationChange(String id, String categoryId) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.putStringParams(params, TYPE, categoryId);
        BaseNetUtil.post(MINE_INVITATION_CHANGE, params, callBack);
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
        BaseNetUtil.post(MINE_INVITATION_DELETE, params, callBack);
    }

    /**
     * 功能描述: 我的设置
     *
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    @Override
    public void mineSetting() {
        BaseNetUtil.get(MINE_SETTING, null, callBack);
    }

    /**
     * 功能描述: 设置中心 设置 (修改昵称、邮箱)
     *
     * @param: type 修改类型
     * @param: value 值
     * @auther: Administrator
     * @date: 2019/3/27 0027 20:28
     */
    @Override
    public void mineSettingProfile(String type, String value) {
        HashMap<String, String> params = new HashMap<>();
        params.put(TYPE, type);
        params.put(VALUE, value);
        BaseNetUtil.post(MINE_SETTING_SET_PROFILE, params, callBack);
    }

    /**
     * 功能描述:  个人设置,修改密码
     *
     * @param: 原密码 ,新密码
     * @auther: Administrator
     * @date: 2019/3/30 0030 23:06
     */
    @Override
    public void changePassWord(String oldPassWord, String newPassWord) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, OLD_PASSWORD, oldPassWord);
        BaseNetUtil.putStringParams(params, NEW_PASSWORD, newPassWord);
        BaseNetUtil.post(MINE_SETTING_SET_PASSWORD, params, callBack);
    }

    /**
     * 功能描述: 找回密码
     *
     * @param: 手机号, 验证码, 密码
     * @auther: Administrator
     * @date: 2019/4/16 0016 20:16
     */
    @Override
    public void forgetPassWord(String photo, String authCode, String password) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, PHONE, photo);
        BaseNetUtil.putStringParams(params, CODE, authCode);
        BaseNetUtil.putStringParams(params, PASSWORD, password);
        BaseNetUtil.post(FORGET_PASSWORD, params, callBack);
    }


    /**
     * 功能描述:任务中心
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/4/20 0020 16:30
     */
    @Override
    public void getTaskCenter() {
        BaseNetUtil.get(GET_TASK_CENTER, null, callBack);
    }


    /**
     * 功能描述:    自动签到
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/4/20 0020 16:54
     */
    @Override
    public void startSignIn() {
        BaseNetUtil.get(AUTO_SIGN_IN, null, callBack);
    }

    /**
     * 功能描述: 我的签到记录
     *
     * @auther: Administrator
     * @date: 2019/5/23 0023 23:58
     */
    @Override
    public void signInHistory() {
        BaseNetUtil.get(SIGN_IN_HISTORY, null, callBack);
    }

    /**
     * 功能描述: 个人中心(改版)
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/4/20 0020 22:30
     */
    @Override
    public void newUserCenter() {
        BaseNetUtil.get(NEW_USER_CENTER, null, callBack);
    }

    /**
     * 功能描述: 反馈信息
     *
     * @param:
     * @auther: Administrator
     * @date: 2019/4/22 0022 15:08
     */
    @Override
    public void feedbackContent(String content) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, CONTENT, content);
        BaseNetUtil.post(FEEDBACK_CONTENT, params, callBack);
    }

    /**
     * 功能描述: 任务中心金币兑换
     *
     * @param: 金币
     * @auther: Administrator
     * @date: 2019/4/22 0022 15:41
     */
    @Override
    public void taskExchange(String gold) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, GOLD, gold);
        BaseNetUtil.post(TASK_EXCHANGE, params, callBack);
    }


    /**
     * 功能描述: 收支记录 - 显示金额
     *
     * @param: 1：余额 0：金币
     * @auther: Administrator
     * @date: 2019/4/23 0023 14:46
     */
    @Override
    public void displayCredit(String type) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, TYPE, type);
        BaseNetUtil.get(DISPLAY_CREDIT, params, callBack);
    }


    /**
     * 功能描述:  收支记录-记录列表
     *
     * @param: type:1：余额 0：金币 page:页码
     * @auther: Administrator
     * @date: 2019/4/23 0023 14:48
     */
    @Override
    public void earningsList(String type, String page) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, TYPE, type);
        BaseNetUtil.putStringParams(params, PAGE, page);
        BaseNetUtil.get(EARNINGS_LIST, params, callBack);
    }

    /**
     * 功能描述:   我的好友列表详情
     *
     * @auther: Administrator
     * @date: 2019/4/23 0023 15:48
     */
    @Override
    public void myFriendListDetail() {
        BaseNetUtil.get(MY_FRIEND_DETAIL, null, callBack);
    }

    /**
     * 功能描述:   我的好友列表
     *
     * @auther: Administrator
     * @date: 2019/4/23 0023 15:48
     */
    @Override
    public void myFriendList(String page) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, PAGE, page);
        BaseNetUtil.get(MY_FRIEND_LIST, params, callBack);
    }

    /**
     * 功能描述:   我的好友列表 提醒好友
     *
     * @param: 用户id
     * @auther: Administrator
     * @date: 2019/4/24 0024 20:31
     */
    @Override
    public void remindFriends(String userId) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, SEND_TO, userId);
        BaseNetUtil.get(MY_FRIEND_LIST_REMIND, params, callBack);
    }

    /**
     * 功能描述:  任务中心设置邀请码
     *
     * @param: 邀请码
     * @auther: Administrator
     * @date: 2019/4/24 0024 01:09
     */
    @Override
    public void setInvitationCode(String invitationCode) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, CODE, invitationCode);
        BaseNetUtil.post(SET_INVITATION_CODE, params, callBack);
    }

    /**
     * 功能描述: 消息通知
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/4/24 0024 12:43
     */
    @Override
    public void getMessageNotification(String page) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, PAGE, page);
        BaseNetUtil.get(MESSAGE_NOTIFICATION, params, callBack);
    }

    /**
     * 功能描述: 提现兑换 列表
     *
     * @auther: Administrator
     * @date: 2019/4/24 0024 20:08
     */
    @Override
    public void getWithdrawDepositList() {
        BaseNetUtil.get(GET_WITHDRAW_DEPOSIT_LIST, null, callBack);
    }

    /**
     * 功能描述: 提现兑换  提现到平台
     *
     * @param: id 平台id
     * @param: number 数值
     * @param: account 账号
     * @param: type  是  int  类型 [0:网站 1:银行卡 2:支付宝]
     * @auther: Administrator
     * @date: 2019/4/24 0024 20:09
     */
    @Override
    public void withdrawDepositNumber(String id, String number, String account, String type) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, SITE_ID, id);
        BaseNetUtil.putStringParams(params, PRICE, number);
        BaseNetUtil.putStringParams(params, ACCOUNT, account);
        BaseNetUtil.putStringParams(params, TYPE, type);
        BaseNetUtil.post(WITHDRAW_DEPOSIT_NUMBER, params, callBack);
    }

    /**
     * 功能描述: 我的收藏/评论/点赞/历史
     *
     * @param: page 页码
     * @param: type  1:收藏 2:评论 3:点赞 4:历史
     * @auther: Administrator
     * @date: 2019/4/24 0024 20:38
     */
    @Override
    public void concernAndHistoryList(String page, String type) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, PAGE, page);
        BaseNetUtil.putStringParams(params, TYPE, type);
        BaseNetUtil.get(CONCERN_AND_HISTORY_LIST, params, callBack);
    }

    /**
     * 任务中心分享任务
     */
    @Override
    public void taskShare() {
        BaseNetUtil.get(TASK_SHARE, null, callBack);
    }


    /**
     * 好友阅读文章记录
     *
     * @param id
     */
    @Override
    public void taskReadArticleRecords(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.get(TASK_READ_ARTICLE_RECORDS, params, callBack);
    }


    /**
     * 获取备用网址
     */
    @Override
    public void getDomainList() {
        BaseNetUtil.get(GET_DOMAIN_LIST, null, callBack);
    }


    /**
     * 增加备用网址
     *
     * @param url
     */
    @Override
    public void addStandbyDomain(String url) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, URL, url);
        BaseNetUtil.post(ADD_STANDBY_DOMAIN, params, callBack);
    }

    /**
     * 删除备用网址
     *
     * @param id
     */
    @Override
    public void delStandbyDomain(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.get(DEL_STANDBY_DOMAIN, params, callBack);
    }
}
