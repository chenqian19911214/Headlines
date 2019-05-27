package com.marksixinfo.net.impl;

import com.marksixinfo.base.ArrayCallback;
import com.marksixinfo.base.BaseNetUtil;
import com.marksixinfo.base.NetArrayManagerBase;
import com.marksixinfo.net.api.CommArrayApi;
import com.marksixinfo.utils.CommonUtils;

import java.util.HashMap;
import java.util.List;

import static com.marksixinfo.constants.NetUrlHeadline.*;
import static com.marksixinfo.constants.StringConstants.*;
import static com.marksixinfo.constants.NetUrlForum.*;

/**
 * 数组提交
 *
 * @Auther: Administrator
 * @Date: 2019/4/25 0025 00:10
 * @Description:
 */
public class CommArrayImpl extends NetArrayManagerBase implements CommArrayApi {

    public CommArrayImpl(ArrayCallback callBack) {
        super(callBack);
    }

    /**
     * 头条发布
     *
     * @param array
     * @param title
     * @param content
     * @param currentId
     * @param periodValue
     */
    @Override
    public void headlineRelease(List<String> array, String title, String content, String currentId, String periodValue) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, TITLE, title);
        BaseNetUtil.putStringParams(params, CONTENT, content);
        BaseNetUtil.putStringParams(params, TYPE, currentId);
        BaseNetUtil.putStringParams(params, PERIOD, periodValue);
        BaseNetUtil.postArray(RELEASE_CONTENT, params, IMAGES, array, callBack);
    }

    /**
     * 论坛发布/编辑
     *
     * @param array
     * @param title
     * @param content
     * @param id      id不为空则为编辑
     */
    @Override
    public void forumRelease(List<String> array, String title, String content, String id) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, TITLE, title);
        BaseNetUtil.putStringParams(params, CONTENT, content);
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.postArray(CommonUtils.StringNotNull(id) ? FORUM_EDIT_RELEASE : FORUM_RELEASE_CONTENT
                , params, IMAGES, array, callBack);
    }


    /**
     * 我的收藏/评论/点赞/历史  删除记录
     *
     * @param array [1,2,3],正常删除  [-1],删除所有
     * @param type
     */
    @Override
    public void concernAndHistoryDelete(List<String> array, String type) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, TYPE, type);
        BaseNetUtil.postArray(CONCERN_AND_HISTORY_LIST_DELETE, params, ID_ARRAY, array, callBack);
    }

}
