package com.marksixinfo.net.api;

/**
 * 头条接口
 *
 * @Auther: Administrator
 * @Date: 2019/3/19 0019 19:56
 * @Description:
 */
public interface HeadlineApi {


    /**
     * 功能描述:  图片上传接口
     *
     * @param: type 文件类型 1,base64
     * @param: imageBase64 base64
     * @auther: Administrator
     * @date: 2019/3/22 0022 20:20
     */
    void imagesUploading(String imageBase64);


    /**
     * 功能描述: 登录
     *
     * @return: 账号, 密码
     * @auther: Administrator
     * @date: 2019/3/19 0019 下午 7:57
     */
    void login(String account, String password);


    /**
     * 功能描述: 发送验证码
     *
     * @param: 手机号
     * @auther: Administrator
     * @date: 2019/3/20 0020 上午 10:27
     */
    void sendAuthCode(String phone);

    /**
     * 功能描述: 注册
     *
     * @param: 昵称, 手机号, 验证码, 密码,邀请码
     * @auther: Administrator
     * @date: 2019/3/20 0020 上午 10:36
     */
    void register(String mNickname, String mPhone, String mAuthCode, String mPassword, String mInviteCode);


    /**
     * 功能描述: 获取推荐标识
     *
     * @param: 页码
     * @auther: Administrator
     * @date: 2019/3/20 0020 上午 10:36
     */
    void getRecommendTag();


    /**
     * 功能描述: 推荐
     *
     * @param: recommendTag 推荐标识
     * @param: type  0,下拉刷新 1,上拉加载
     * @auther: Administrator
     * @date: 2019/3/20 0020 上午 10:36
     */
    void recommend(String recommendTag, String type);


    /**
     * 功能描述: 关注
     *
     * @param: 页码
     * @auther: Administrator
     * @date: 2019/3/20 0020 上午 10:36
     */
    void concern(String page);


    /**
     * 功能描述:  点赞
     *
     * @param: 文章id
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:07
     */
    void praise(String id);


    /**
     * 功能描述:  点赞用户评论
     *
     * @param: 文章id
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:07
     */
    void praiseComment(String id);


    /**
     * 功能描述:  收藏
     *
     * @param: 文章id
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:07
     */
    void favorites(String id);


    /**
     * 功能描述: 关注用户
     *
     * @param: id
     * @auther: Administrator
     * @date: 2019/3/20 0020 上午 10:36
     */
    void concernUser(String id);


    /**
     * 功能描述:  热门搜索关键词
     *
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:07
     */
    void getSearchHot();


    /**
     * 功能描述: 搜索
     *
     * @param: 搜索关键字, 页码
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:10
     */
    void getSearchResult(String keyword, int page);


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


    /**
     * 功能描述: 用户个人中心
     *
     * @param: 用户id
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:13
     */
    void getUserCenter(String userId);


    /**
     * 功能描述: 用户个人中心分类
     *
     * @param: 用户id
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:13
     */
    void getUserCenterType(String userId);

    /**
     * 功能描述: 用户个人中心列表
     *
     * @param: 分类id
     * @param: 页码
     * @auther: Administrator
     * @date: 2019/3/22 0022 13:13
     */
    void getUserCenterList(String categroyId, String memberId, String page);


    /**
     * 功能描述: 发布获取分类及期数
     *
     * @auther: Administrator
     * @date: 2019/3/22 0022 15:38
     */
    void getSelectClassify();


    /**
     * 功能描述:添加分类
     *
     * @param: name 分类名称
     * @auther: Administrator
     * @date: 2019/3/22 0022 16:16
     */
    void addSelectClassify(String name);

    /**
     * 功能描述:修改分类
     *
     * @param: name 分类名称
     * @auther: Administrator
     * @date: 2019/3/22 0022 16:16
     */
    void changeSelectClassify(String name, String id);

    /**
     * 功能描述:删除分类
     *
     * @param: name 分类名称
     * @auther: Administrator
     * @date: 2019/3/22 0022 16:16
     */
    void deleteSelectClassify(String id);

    /**
     * 功能描述:排序分类
     *
     * @param: sortList 分类id集合
     * @auther: Administrator
     * @date: 2019/3/22 0022 16:16
     */
    void sortSelectClassify(String sortList);

    /**
     * 功能描述:分类操作完成刷新
     *
     * @auther: Administrator
     * @date: 2019/3/22 0022 16:16
     */
    void setSelectClassify();

    /**
     * 功能描述: 文章详情
     *
     * @param: 文章id
     * @auther: Administrator
     * @date: 2019/3/23 0023 12:12
     */
    void getDetail(String id);

    /**
     * 功能描述: 文章详情获取下一期
     *
     * @param: 文章id
     * @auther: Administrator
     * @date: 2019/3/23 0023 12:12
     */
    void getDetailNext(String id);


    /**
     * 功能描述: 文章详情 - 回复列表
     *
     * @param: 文章id
     * @param: 页码
     * @auther: Administrator
     * @date: 2019/3/23 0023 12:12
     */
    void getDetailCommentList(String item_id, String page);

    /**
     * 我的: 个人中心
     *
     * @auther: Administrator
     * @date: 2019/3/23 0023 12:19
     */
    void mineUserCenter();


    /**
     * 功能描述: 我的回复
     *
     * @param: page 页码
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    void mineComment(String page);

    /**
     * 功能描述: 我的回复 单条删除
     *
     * @param: page 页码
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    void mineDeleteComment(String id);

    /**
     * 功能描述: 我的关注 / 粉丝
     *
     * @param: page 页码  type 1:我关注的 2:我的粉丝
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    void mineConcern(String page, String type);

    /**
     * 功能描述: 我的关注-取消关注
     *
     * @param: page 页码
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    void mineConcernCancel(String id);

    /**
     * 功能描述: 我的收藏
     *
     * @param: page 页码
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    void mineCollect(String page);


    /**
     * 功能描述: 我的收藏 取消收藏
     *
     * @param: id 帖子id
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    void mineDeleteCollect(String id);

    /**
     * 功能描述: 我的帖子 分类
     *
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    void mineInvitationCategory();

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
     * 我的帖子 编辑分类
     *
     * @param: id 帖子id
     * @param: categoryId 分类id
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    void mineInvitationChange(String id, String categoryId);

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
     * 功能描述: 我的设置
     *
     * @auther: Administrator
     * @date: 2019/3/26 0026 22:15
     */
    void mineSetting();


    /**
     * 功能描述: 设置中心 设置 (修改昵称、邮箱)
     *
     * @param: type 修改类型
     * @param: value 值
     * @auther: Administrator
     * @date: 2019/3/27 0027 20:28
     */
    void mineSettingProfile(String type, String value);

    /**
     * 功能描述:  个人设置,修改密码
     *
     * @param: 原密码 ,新密码
     * @auther: Administrator
     * @date: 2019/3/30 0030 23:06
     */
    void changePassWord(String oldPassWord, String newPassWord);


    /**
     * 功能描述: 找回密码
     *
     * @param: 手机号, 验证码, 密码
     * @auther: Administrator
     * @date: 2019/4/16 0016 20:16
     */
    void forgetPassWord(String photo, String authCode, String password);


    /**
     * 功能描述:任务中心
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/4/20 0020 16:30
     */
    void getTaskCenter();


    /**
     * 功能描述:    自动签到
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/4/20 0020 16:54
     */
    void startSignIn();


    /**
     * 功能描述: 我的签到记录
     *
     * @auther: Administrator
     * @date: 2019/5/23 0023 23:58
     */
    void signInHistory();

    /**
     * 功能描述: 个人中心(改版)
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/4/20 0020 22:30
     */
    void newUserCenter();

    /**
     * 功能描述: 反馈信息
     *
     * @param:
     * @auther: Administrator
     * @date: 2019/4/22 0022 15:08
     */
    void feedbackContent(String content);

    /**
     * 功能描述: 任务中心金币兑换
     *
     * @param: 金币
     * @auther: Administrator
     * @date: 2019/4/22 0022 15:41
     */
    void taskExchange(String gold);


    /**
     * 功能描述: 收支记录 - 显示金额
     *
     * @param: 1：余额 0：金币
     * @auther: Administrator
     * @date: 2019/4/23 0023 14:46
     */
    void displayCredit(String type);


    /**
     * 功能描述:  收支记录-记录列表
     *
     * @param: type:1：余额 0：金币 page:页码
     * @auther: Administrator
     * @date: 2019/4/23 0023 14:48
     */
    void earningsList(String type, String page);


    /**
     * 功能描述:   我的好友列表详情
     *
     * @auther: Administrator
     * @date: 2019/4/23 0023 15:48
     */
    void myFriendListDetail();

    /**
     * 功能描述:   我的好友列表
     *
     * @auther: Administrator
     * @date: 2019/4/23 0023 15:48
     */
    void myFriendList(String page);


    /**
     * 功能描述:   我的好友列表 提醒好友
     *
     * @param: 用户id
     * @auther: Administrator
     * @date: 2019/4/24 0024 20:31
     */
    void remindFriends(String userId);


    /**
     * 功能描述:  任务中心设置邀请码
     *
     * @param: 邀请码
     * @auther: Administrator
     * @date: 2019/4/24 0024 01:09
     */
    void setInvitationCode(String invitationCode);


    /**
     * 功能描述: 消息通知
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/4/24 0024 12:43
     */
    void getMessageNotification(String page);


    /**
     * 功能描述: 提现兑换 列表
     *
     * @auther: Administrator
     * @date: 2019/4/24 0024 20:08
     */
    void getWithdrawDepositList();

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
    void withdrawDepositNumber(String id, String number, String account, String type);


    /**
     * 功能描述: 我的收藏/评论/点赞/历史
     *
     * @param: page 页码
     * @param: type  1:收藏 2:评论 3:点赞 4:历史
     * @auther: Administrator
     * @date: 2019/4/24 0024 20:38
     */
    void concernAndHistoryList(String page, String type);


    /**
     * 任务中心分享任务
     */
    void taskShare();


    /**
     * 好友阅读文章记录
     *
     * @param id
     */
    void taskReadArticleRecords(String id);


    /**
     * 获取备用网址
     */
    void getDomainList();


    /**
     * 增加备用网址
     *
     * @param url
     */
    void addStandbyDomain(String url);

    /**
     * 删除备用网址
     *
     * @param id
     */
    void delStandbyDomain(String id);
}
