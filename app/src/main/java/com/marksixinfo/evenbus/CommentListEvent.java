package com.marksixinfo.evenbus;

import com.marksixinfo.bean.CommentsListDetail;

/**
 *  评论列表操作更新详情页
 *
 * @Auther: Administrator
 * @Date: 2019/4/6 0006 22:14
 * @Description:
 */
public class CommentListEvent {

    private CommentsListDetail detail;

    private int  type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CommentListEvent() {
    }

    public CommentListEvent(CommentsListDetail detail) {
        this.detail = detail;
    }

    public CommentsListDetail getDetail() {
        return detail;
    }

    public void setDetail(CommentsListDetail detail) {
        this.detail = detail;
    }
}
