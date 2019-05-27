package com.marksixinfo.constants;

/**
 * number常量
 *
 * @Auther: Administrator
 * @Date: 2019/3/14 0014 20:18
 * @Description:
 */
public interface NumberConstants {

    /**
     * 启动屏延时
     */
    int START_TIME = 500;

    /**
     * Activity StatusViewID 动态改变status的ViewId
     */
    int ACTIVITY_STATUS_VIEW_ID = 0x12345678;

    /**
     * 发布内容选择分类intent code
     */
    int INTENT_CODE_CLASSIFY_SELECT = 101;

    /**
     * 登录页面到注册intent code
     */
    int INTENT_CODE_REGISTER = 102;


    /**
     * 我的帖子 - 新增分类
     */
    int INTENT_CODE_MINE_INVITATION = 103;

    /**
     * 回复列表 返回帖子详情 code
     */
    int INTENT_CODE_DETAIL_RESULT = 104;


    /**
     * 搜索记录最大条数
     */
    int SEARCH_HISTORY_SIZE = 6;


    /**
     * 新增分类名称长度
     */
    int CLASSIFY_NAME_LENGTH = 10;


    /**
     * 密码最少长度
     */
    int PASS_WORD_LENGTH = 6;


    /**
     * 手机号长度
     */
    int PHONE_LENGTH = 11;

    /**
     * 验证码长度
     */
    int AUTH_CODE_LENGTH = 4;


    /**
     * 推荐列表缓存最大条数
     */
    int RECOMMEND_LIST_CACHE_LENGTH = 30;


    /**
     * 任务相关金额金币动画延时
     */
    long ANIM_TEXT_VIEW_TIME = 1000;


    /**
     * 头像查看大图最低分辨率 400x400
     */
    int CHECK_PIC_ORIGIN_SIZE = 400;

    /**
     * 好友阅读文章记录接口触发延时时间
     */
    int TASK_READ_ARTICLE_TIME = 5000;


    /**
     * 图库滑动时,索引隐藏延时
     */
    int GALLERY_INDEX_BAR_DELAYED = 1000;


    /**
     * 准备开奖,时间 10分钟
     */
    long REMIND_LOTTERY_TIME = 1000 * 60 * 10;
//    long REMIND_LOTTERY_TIME = 1000 * 30;


    /**
     * 头条搜索用户显示数量
     */
    int SEARCH_USER_COUNT = 6;
}
