package com.marksixinfo.constants;

/**
 * 图库url
 *
 * @Auther: Administrator
 * @Date: 2019/5/9 0009 13:55
 * @Description:
 */
public interface NetUrlGallery {

    /**
     * 图库host
     */
    String GALLERY_HOST = "http://api.images." + StringConstants.Main_Host + "?";


    /**
     * 图库首页 请求分类及期数
     */
    String GALLERY_MAIN = GALLERY_HOST + "C=index";


    /**
     * 图库首页 获取列表
     */
    String GALLERY_MAIN_LIST = GALLERY_HOST + "A=get_list";

    /**
     * 图库 我的收藏
     */
    String GALLERY_MY_COLLECTION = GALLERY_HOST + "A=my_favorites";

    /**
     * 通过关键字搜索
     */
    String SEARCH_CHAR = GALLERY_HOST + "C=search&A=get_list";

    /**
     * 过去年度所以期数
     */
    String SEARCH_GET_ALL_PERIOD = GALLERY_HOST + "C=details&A=get_period";

    /**
     * 获取图库详情 C=Answer
     */
    String SEARCH_GET_DETAILS = GALLERY_HOST + "C=details&A=get_details";

    /**
     * 获取图解详情
     */
    String SEARCH_GET_ANSWER_DATA = GALLERY_HOST + "C=Answer";
    /**
     * 获取图库评论
     */
    String SEARCH_GET_REPLY = GALLERY_HOST + "C=details&A=get_reply";
    /**
     * 获取图解评论
     */
    String SEARCH_GET_ANSWER_REPLY = GALLERY_HOST + "C=Answer&A=get_reply";

    /**
     * 获取图库图解
     */
    String SEARCH_GET_ANSWER = GALLERY_HOST + "C=details&A=get_answer";
    /**
     * 选择期数
     */
    String SEARCH_GET_SELECT_PERIOD = GALLERY_HOST + "C=details&A=select_period";

    /**
     * 点赞
     */
    String SEARCH_GET_CLICK_LIKE = GALLERY_HOST + "C=Common&A=like";
    /**
     * 踩
     */
    String SEARCH_GET_CLICK_TREAD = GALLERY_HOST + "C=Common&A=tread";

    /**
     * 收藏
     */
    String SEARCH_GET_CLICK_FAVORITER = GALLERY_HOST + "C=Common&A=favorites";
    /**
     * 发布图解
     */
    String SEARCH_POST_ANSWER = GALLERY_HOST + "C=details&A=pub_answer";
    /**
     * 发布评论
     */
    String SEARCH_POST_PULL_REPLE = GALLERY_HOST + "C=common&A=pub_reply";
    /**
     * 获取图解评论详情
     */
    String SEARCH_GET_ANSWER_REPLY_DETAILS = GALLERY_HOST + "C=Answer&A=get_reply_details";
    /**
     * 获取图解评论列表
     */
    String SEARCH_GET_ANSWER_REPLY_LIST = GALLERY_HOST + "C=Answer&A=get_replys";

    /**
     * 获取图片评论详情
     */
    String SEARCH_GET_DETAILS_REPLY_DETAILS = GALLERY_HOST + "C=details&A=get_reply_details";
    /**
     * 获取图片评论列表
     */
    String SEARCH_GET_DETAILS_REPLY_LIST = GALLERY_HOST + "C=details&A=get_replys";


    /**
     * 图片详情 回复列表页 回复详情
     */
    String REPLY_DETAILS_DETAILS = GALLERY_HOST + "C=details&A=get_reply_details";

    /**
     * 图片详情 回复列表页 回复列表
     */
    String REPLY_LIST_DETAILS = GALLERY_HOST + "C=details&A=get_replys";


    /**
     * 图解详情 回复列表页 回复详情
     */
    String REPLY_DETAILS_ANSWER = GALLERY_HOST + "C=Answer&A=get_reply_details";

    /**
     * 图解详情 回复列表页 回复列表
     */
    String REPLY_LIST_ANSWER = GALLERY_HOST + "C=Answer&A=get_replys";


    /**
     * 图库搜索,热搜词
     */
    String GALLERY_SEARCH_HOT = GALLERY_HOST + "C=search";

    /**
     * 获取版本号 getVersionName
     */
    String GET_VERSION_NAME = "https://app.sskk58.com/download/update/android.php";

}
