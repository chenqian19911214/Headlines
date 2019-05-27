package com.marksixinfo.evenbus;

import java.util.List;

/**
 * 我的收藏/点赞列表 删除多个或删除所有,推荐页及关注页数据变化
 *
 * @Auther: Administrator
 * @Date: 2019/4/25 0025 14:05
 * @Description:
 */
public class DeleteLikeAndFavorEvent {

    /**
     * 变更集合 [1,2,3],正常删除  [-1],删除所有
     */
    public List<String> ids;

    /**
     * 1,收藏 2,评论 3,点赞 4,历史
     */
    public int type;

    public DeleteLikeAndFavorEvent() {
    }

    public DeleteLikeAndFavorEvent(List<String> ids, int type) {
        this.ids = ids;
        this.type = type;
    }
}
