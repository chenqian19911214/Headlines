package com.marksixinfo.constants;

/**
 * 头条url
 *
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 20:18
 * @Description:
 */
public interface NetUrlHeadline {


    /**
     * host
     */
//     String HOST = "http://192.168.1.106/api/?C=";
//    String HOST = "https://api.sskk58.com/?C=";
//    String HOST = "http://api.toutiao.lhtt.co/?C=";
    String HOST = "http://api.toutiao." + StringConstants.Main_Host + "?C=";
//     String HOST = "http://hongkong.sskk58.com/api/?C=";


    /**
     * 图片上传
     */
//    String IMAGES_UPLOADING = "https://images.34399.com/";
    String IMAGES_UPLOADING = "https://images.qz022.com/";

    /**
     * 发送验证码
     */
    String SEND_AUTH_CODE = HOST + "index&A=sms";

    /**
     * 登录
     */
    String LOGIN = HOST + "passport&A=login";

    /**
     * 注册
     */
    String REGISTER = HOST + "passport&A=reg";

    /**
     * 获取推荐标识
     */
    String GET_RECOMMEND_TAG = HOST + "index";

    /**
     * 推荐
     */
    String RECOMMEND = HOST + "index&A=push";

    /**
     * 关注
     */
    String CONCERN_LIST = HOST + "index&A=my_look";


    /**
     * 文章点赞
     */
    String PRAISE = HOST + "bbs&A=like";


    /**
     * 评论点赞
     */
    String PRAISE_COMMENT = HOST + "bbs&A=like_reply";

    /**
     * 收藏
     */
    String FAVORITES = HOST + "bbs&A=add_favorites";

    /**
     * 关注用户
     */
    String CONCERN_USER = HOST + "bbs&A=add_look";


    /**
     * 热门搜索关键词
     */
    String SEARCH_HOT = HOST + "search";

    /**
     * 搜索
     */
    String SEARCH_RESULT = HOST + "search&A=get_list";

    /**
     * 获取搜索期数
     */
    String GET_SEARCH_PERIOD = HOST + "search&A=get_period";

    /**
     * 获取搜索用户/头条
     */
    String GET_SEARCH_USER_RESULT = HOST + "search&A=get_list_user";

    /**
     * 回帖
     */
    String COMMENTS = HOST + "bbs&A=reply";

    /**
     * 回帖 - 回复用户
     */
    String COMMENTS_USER = HOST + "bbs&A=replys";


    /**
     * 评论列表 - 详情
     */
    String COMMENTS_LIST_DETAIL = HOST + "bbs&A=reply_details";

    /**
     * 评论列表 - 评论
     */
    String COMMENTS_LIST = HOST + "bbs&A=get_reply_child";


    /**
     * 用户个人中心
     */
    String USER_CENTER = HOST + "home";


    /**
     * 用户个人中心分类
     */
    String USER_CENTER_type = HOST + "home&A=get_user_type";


    /**
     * 用户个人中心 帖子列表
     */
    String USER_CENTER_LIST = HOST + "home&A=get_list";


    /**
     * 发布获取分类及期数
     */
    String GET_SELECT_CLASSIFY = HOST + "bbs&A=get_pub";

    /**
     * 添加分类
     */
    String ADD_SELECT_CLASSIFY = HOST + "bbs&A=add_type";

    /**
     * 修改分类
     */
    String CHANGE_SELECT_CLASSIFY = HOST + "ucenter&A=edit_categroy";

    /**
     * 删除分类
     */
    String DELETE_SELECT_CLASSIFY = HOST + "ucenter&A=del_categroy";

    /**
     * 分类排序
     */
    String SORT_SELECT_CLASSIFY = HOST + "ucenter&A=seq_categroy";

    /**
     * 分类操作完成刷新
     */
    String SET_SELECT_CLASSIFY = HOST + "ucenter&A=set_categroy";

    /**
     * 发布
     */
    String RELEASE_CONTENT = HOST + "bbs&A=pub";


    /**
     * 文章详情
     */
    String DETAIL = HOST + "bbs&A=details";

    /**
     * 文章详情 获取下一期
     */
    String DETAIL_NEXT = HOST + "bbs&A=get_more";


    /**
     * 文章详情 - 回复列表
     */
    String DETAIL_CONTENT_LIST = HOST + "bbs&A=get_reply_list";

    /**
     * 我的个人中心
     */
    String MINE_USER_CENTER = HOST + "ucenter";


    /**
     * 我的回复
     */
    String MINE_COMMENT = HOST + "ucenter&A=my_reply";

    /**
     * 我的回复-单条删除
     */
    String MINE_DELETE_COMMENT = HOST + "ucenter&A=del_reply";

    /**
     * 我的关注
     */
    String MINE_CONCERN = HOST + "ucenter&A=my_look";

    /**
     * 我的关注-取消关注
     */
    String MINE_CONCERN_CANCEL = HOST + "ucenter&A=look_cancel";

    /**
     * 我的收藏
     */
    String MINE_COLLECT = HOST + "ucenter&A=my_favorites";

    /**
     * 我的收藏 - 取消收藏
     */
    String MINE_DELETE_COLLECT = HOST + "ucenter&A=del_favorites";

    /**
     * 我的帖子 分类
     */
    String MINE_INVITATION_CATEGORY = HOST + "ucenter&A=get_all_categroy";

    /**
     * 我的帖子 列表
     */
    String MINE_INVITATION_LIST = HOST + "ucenter&A=my_item";

    /**
     * 我的帖子 编辑分类
     */
    String MINE_INVITATION_CHANGE = HOST + "ucenter&A=modify_item";

    /**
     * 我的头条 删除帖子
     */
    String MINE_INVITATION_DELETE = HOST + "ucenter&A=del_item";

    /**
     * 我的设置
     */
    String MINE_SETTING = HOST + "ucenter&A=my_profile";


    /**
     * 我的设置 -(修改昵称、邮箱、密码<临时>)
     */
    String MINE_SETTING_SET_PROFILE = HOST + "ucenter&A=set_profile";

    /**
     * 我的设置 -(修改密码)
     */
    String MINE_SETTING_SET_PASSWORD = HOST + "ucenter&A=set_password";

    /**
     * 找回密码
     */
    String FORGET_PASSWORD = HOST + "passport&A=forget_password";


    /**
     * 免责声明
     */
    String DISCLAIMER = HOST + "passport&A=terms";


    /**
     * 任务中心
     */
    String GET_TASK_CENTER = HOST + "task";


    /**
     * 自动签到
     */
    String AUTO_SIGN_IN = HOST + "task&A=check_in";


    /**
     * 我的签到记录
     */
    String SIGN_IN_HISTORY = HOST + "task&A=my_check_in";

    /**
     * 个人中心(改版)
     */
    String NEW_USER_CENTER = HOST + "Ucenter";

    /**
     * 反馈信息
     */
    String FEEDBACK_CONTENT = HOST + "ucenter&A=post_feedback";

    /**
     * 任务中心金币兑换
     */
    String TASK_EXCHANGE = HOST + "task&A=exchange";


    /**
     * 收支记录-记录列表 显示余额
     */
    String DISPLAY_CREDIT = HOST + "ucenter&A=earnings";


    /**
     * 收支记录-记录列表 列表
     */
    String EARNINGS_LIST = HOST + "ucenter&A=earnings_log";


    /**
     * 好友列表详情
     */
    String MY_FRIEND_DETAIL = HOST + "ucenter&A=my_friend";


    /**
     * 好友列表 列表
     */
    String MY_FRIEND_LIST = HOST + "ucenter&A=my_friend_list";


    /**
     * 好友列表  提醒好友
     */
    String MY_FRIEND_LIST_REMIND = HOST + "ucenter&A=remind";


    /**
     * 任务中心设置邀请码
     */
    String SET_INVITATION_CODE = HOST + "ucenter&A=set_invitation_code";


    /**
     * 消息通知 列表
     */
    String MESSAGE_NOTIFICATION = HOST + "ucenter&A=get_msg";


    /**
     * 提现兑换 列表
     */
    String GET_WITHDRAW_DEPOSIT_LIST = HOST + "task&A=withdraw";


    /**
     * 提现兑换  提现到平台
     */
    String WITHDRAW_DEPOSIT_NUMBER = HOST + "task&A=request_withdraw";


    /**
     * 我的收藏/评论/点赞/历史
     */
    String CONCERN_AND_HISTORY_LIST = HOST + "ucenter&A=my_used_log";


    /**
     * 我的收藏/评论/点赞/历史  删除记录
     */
    String CONCERN_AND_HISTORY_LIST_DELETE = HOST + "ucenter&A=del_used_log";


    /**
     * 分享任务
     */
    String TASK_SHARE = HOST + "task&A=task_share";

    /**
     * 好友阅读文章记录
     */
    String TASK_READ_ARTICLE_RECORDS = HOST + "task&A=task_read";


    /**
     * 获取备用网址
     */
    String GET_DOMAIN_LIST = HOST + "ucenter&A=my_domain";

    /**
     * 增加备用网址
     */
    String ADD_STANDBY_DOMAIN = HOST + "ucenter&A=add_domain";

    /**
     * 删除备用网址
     */
    String DEL_STANDBY_DOMAIN = HOST + "ucenter&A=del_domain";


}
