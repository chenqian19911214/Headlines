package com.marksixinfo.evenbus;

import com.marksixinfo.bean.ForumConcernListData;

import java.util.List;

/**
 *  一期论坛发送分类(已由广场页请求固定id)
 *
 * @Auther: Administrator
 * @Date: 2019/4/1 0001 22:18
 * @Description:
 */
public class ForumListTypeEvent {

    private List<ForumConcernListData.TypeBean> type;

    public ForumListTypeEvent() {
    }

    public ForumListTypeEvent(List<ForumConcernListData.TypeBean> type) {
        this.type = type;
    }

    public List<ForumConcernListData.TypeBean> getType() {
        return type;
    }

    public void setType(List<ForumConcernListData.TypeBean> type) {
        this.type = type;
    }
}
