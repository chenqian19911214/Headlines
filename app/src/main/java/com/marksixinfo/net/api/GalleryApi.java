package com.marksixinfo.net.api;

/**
 * 图库接口
 *
 * @Auther: Administrator
 * @Date: 2019/5/9 0009 14:21
 * @Description:
 */
public interface GalleryApi {


    /**
     * 获取分类及期数
     */
    void getGalleryMainPeriod();
    /**
     * 获取图库列表
     *
     * @param period 期数 2019045
     * @param type   分类 2:黑白 3:彩色
     */
    void getGalleryMainList(String period, String type);

    /**
     * 获取图库我的收藏
     *
     * @param p 当前页面
     */
    void getGalleryMyCollection(String p);

    /**
     * 通过关键字搜索
     *
     * @param keyword 关键字
     * @param page    当前页面
     */
    void getCharSearch(String keyword, int page);

    /**
     * 图库详情所有期数
     *
     * @param id   图库ID
     * @param year 年份 2019
     */
    void getAllPictrue(String id, int year);

    /**
     * 获取图库详情
     *
     * @param id     图库ID
     * @param period 期数
     */
    void getDetails(String id, String period);

    /**
     * 获取图库评论
     *
     * @param id     图库ID
     * @param period 期数
     * @param p      页面
     */
    void getComment(String id, String period, int p);

    /**
     * 获取图解评论
     *
     * @param id 图解ID
     * @param p  页面
     */
    void getAnswerComment(String id, int p);

    /**
     * 获取图库 图解
     *
     * @param id     图库ID
     * @param period 期数
     * @param p      页面
     */
    void getAnswer(String id, String period, int p);

    /**
     * 获取图解 详情
     *
     * @param id 图库ID
     */
    void getAnswerData(String id);

    /**
     * 获取图库 选择期数
     */
    void getSelectPeriod();

    /**
     * 点赞
     *
     * @param id     根据TYPE值对应（图库ID或图解ID,回帖ID…） y
     * @param period 图库必传（type=1）,其它不用传 f
     * @param type   1:图库 2:图解 3:回帖 4：回帖的回帖 5图解回帖 6图解回帖的回帖 y
     */
    void getClickLike(String id, String period, String type);
    /**
     * 踩
     *
     * @param id     根据TYPE值对应（图库ID或图解ID,回帖ID…） y
     * @param period 图库必传（type=1） 期号 2019045 f
     * @param type   1:图库 2:图解 y
     */
    void postClickTread(String id, String period, String type);
    /**
     * 图库收藏
     *
     * @param id    图库ID y
     * @param period 期号 2019045 y
     */
    void postClickFavorites(String id, String period);

    /**
     * 发布图解
     * @param title     标题 y
     * @param content 内容 2019045 y
     * @param id    图库ID y
     * @param period 期号 2019045
     */
    void postPullAnswer(String title, String content,String id, String period);
    /**
     * 发布评论
     * @param content 内容 2019045 y
     * @param id    图库ID y
     * @param period 期号 2019045
     * @param type 1:图库 2:图解 3:回帖 4图解回帖
     */
    void postPullReply(String content,String id,String period,String type);
    /**
     * 图解评论详情
     * @param id   评论ID y
     */
    void getAnswerReplyDetails(String id);
    /**
      *图解评论list
     * @param id  评论ID y
     */
    void getAnswerReplyList(String id);

    /**
     * 图片评论详情
     * @param id   评论ID y
     */
    void getDetailsReplyDetails(String id);
    /**
     *图片评论list
     * @param id  评论ID y
     */
    void getDetailsReplyList(String id);


    /**
     * 功能描述:  评论列表 - 详情
     *
     * @param: id,
     * @param: type, 2,图库图片 3,图库图解
     * @auther: Administrator
     * @date: 2019/3/28 0028 21:22
     */
    void commentsListDetail(String id,int type);


    /**
     * 功能描述:  评论列表 - 评论
     *
     * @param: id, 页码
     * @param: type, 2,图库图片 3,图库图解
     * @auther: Administrator
     * @date: 2019/3/28 0028 21:22
     */
    void commentsList(String id, String page,int type);

    /**
     * 功能描述: 图库搜索,获取热搜词
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/5/17 0017 00:13
     */
    void getSearchHot();

}

