package com.marksixinfo.net.impl;

import android.text.TextUtils;

import com.marksixinfo.base.BaseNetUtil;
import com.marksixinfo.base.CallbackBase;
import com.marksixinfo.base.NetManagerBase;
import com.marksixinfo.net.api.GalleryApi;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import static com.marksixinfo.constants.NetUrlGallery.*;
import static com.marksixinfo.constants.StringConstants.*;

/**
 * 图库接口实现类
 *
 * @Auther: Administrator
 * @Date: 2019/5/9 0009 14:24
 * @Description:
 */
public class GalleryImpl extends NetManagerBase implements GalleryApi {

    public GalleryImpl(CallbackBase callBack) {
        super(callBack);
    }

    @Override
    public void getGalleryMainPeriod() {
        BaseNetUtil.get(GALLERY_MAIN, null, callBack);
    }

    @Override
    public void getGalleryMainList(String period, String type) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, PERIOD, period);
        BaseNetUtil.putStringParams(params, TYPE, type);
        BaseNetUtil.get(GALLERY_MAIN_LIST, params, callBack);
    }

    @Override
    public void getGalleryMyCollection(String p) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, PAGE, p);
        BaseNetUtil.get(GALLERY_MY_COLLECTION, params, callBack);
    }

    @Override
    public void getCharSearch(String keyword, int page) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, PAGE, String.valueOf(page));
        try {
            String encode = URLEncoder.encode(keyword, "utf-8");
            BaseNetUtil.putStringParams(params, KEYWORD, encode);
            BaseNetUtil.get(SEARCH_CHAR, params, callBack);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    /**
     * 图库详情所以期数
     *
     * @param id   图库ID
     * @param year 年份 2019
     */
    @Override
    public void getAllPictrue(String id, int year) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        if (year!=0)
            BaseNetUtil.putStringParams(params, YEAR, year+"");
        BaseNetUtil.get(SEARCH_GET_ALL_PERIOD, params, callBack);
    }

    /**
     * 获取图库详情
     *
     * @param id   图库ID
     * @param period 期数
     */
    @Override
    public void getDetails(String  id, String period) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.putStringParams(params, PERIOD, period);
        BaseNetUtil.get(SEARCH_GET_DETAILS, params, callBack);
    }

    /**
     * 获取图库评论
     *
     * @param id   图库ID
     * @param period 期数
     */
    @Override
    public void getComment(String  id, String period,int p) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.putStringParams(params, PERIOD, period);
        BaseNetUtil.putStringParams(params, PAGE, p+"");
        BaseNetUtil.get(SEARCH_GET_REPLY, params, callBack);
    }

    @Override
    public void getAnswerComment(String id, int p) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.putStringParams(params, PAGE, p+"");
        BaseNetUtil.get(SEARCH_GET_ANSWER_REPLY, params, callBack);
    }

    /**
     * 获取图库图解
     *
     * @param id   图库ID
     * @param period 期数
     */
    @Override
    public void getAnswer(String id, String period,int p) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.putStringParams(params, PERIOD, period);
        BaseNetUtil.putStringParams(params, PAGE, p+"");
        BaseNetUtil.get(SEARCH_GET_ANSWER, params, callBack);
    }

    @Override
    public void getAnswerData(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.get(SEARCH_GET_ANSWER_DATA, params, callBack);
    }

    @Override
    public void getSelectPeriod() {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.get(SEARCH_GET_SELECT_PERIOD, params, callBack);
    }
    // 实体类 LikeAndFavoriteData
    @Override
    public void getClickLike(String id, String period, String type) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        if (!TextUtils.isEmpty(period))
            BaseNetUtil.putStringParams(params, PERIOD, period);
        BaseNetUtil.putStringParams(params, TYPE, type);
        BaseNetUtil.get(SEARCH_GET_CLICK_LIKE,params,callBack);
    }

    @Override
    public void postClickTread(String id, String period, String type) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        if (!TextUtils.isEmpty(period))
            BaseNetUtil.putStringParams(params, PERIOD, period);
        BaseNetUtil.putStringParams(params, TYPE, type);
        BaseNetUtil.get(SEARCH_GET_CLICK_TREAD,params,callBack);
    }

    @Override
    public void postClickFavorites(String id, String period) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.putStringParams(params, PERIOD, period);
        BaseNetUtil.post(SEARCH_GET_CLICK_FAVORITER,params,callBack);
    }

    /**
     * 发布图解
     * */
    @Override
    public void postPullAnswer(String title, String content, String id, String period) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.putStringParams(params, TITLE, title);
        BaseNetUtil.putStringParams(params, CONTENT, content);
        BaseNetUtil.putStringParams(params, PERIOD, period);
        BaseNetUtil.post(SEARCH_POST_ANSWER,params,callBack);
    }

    /**
     * 发布评论
     * @param content 内容 2019045 y
     * @param id    图库ID y
     * @param period 期号 2019045
     * @param type 1:图库 2:图解 3:回帖 4图解回帖
     */
    @Override
    public void postPullReply(String content, String id, String period,String type) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.putStringParams(params, CONTENT, content);
        BaseNetUtil.putStringParams(params, PERIOD, period);
        BaseNetUtil.putStringParams(params, TYPE, type);
        BaseNetUtil.post(SEARCH_POST_PULL_REPLE,params,callBack);
    }

    @Override
    public void getAnswerReplyDetails(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.get(SEARCH_GET_ANSWER_REPLY_DETAILS,params,callBack);
    }

    @Override
    public void getAnswerReplyList(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.get(SEARCH_GET_ANSWER_REPLY_LIST,params,callBack);
    }

    @Override
    public void getDetailsReplyDetails(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.get(SEARCH_GET_DETAILS_REPLY_DETAILS,params,callBack);
    }

    @Override
    public void getDetailsReplyList(String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.get(SEARCH_GET_DETAILS_REPLY_LIST,params,callBack);
    }


    /**
     * 功能描述:  评论列表 - 详情
     *
     * @param: id
     * @param: type, 2,图库图片 3,图库图解
     * @auther: Administrator
     * @date: 2019/3/28 0028 21:22
     */
    @Override
    public void commentsListDetail(String id,int type) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.get(type == 2 ? REPLY_DETAILS_DETAILS : REPLY_DETAILS_ANSWER, params, callBack);
    }


    /**
     * 功能描述:  评论列表 - 评论
     *
     * @param: id, 页码
     * @param: type, 2,图库图片 3,图库图解
     * @auther: Administrator
     * @date: 2019/3/28 0028 21:22
     */
    @Override
    public void commentsList(String id, String page,int type) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.putStringParams(params, PAGE, page);
        BaseNetUtil.get(type == 2 ? REPLY_LIST_DETAILS : REPLY_LIST_ANSWER, params, callBack);
    }

    /**
     * 功能描述: 图库搜索,获取热搜词
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/5/17 0017 00:13
     */
    @Override
    public void getSearchHot() {
        BaseNetUtil.get(GALLERY_SEARCH_HOT, null, callBack);
    }

}
