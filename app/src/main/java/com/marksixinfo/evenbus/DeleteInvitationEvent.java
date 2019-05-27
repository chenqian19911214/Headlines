package com.marksixinfo.evenbus;

/**
 * 全局删除帖子/编辑帖子刷新
 *
 * @Auther: Administrator
 * @Date: 2019/4/13 0013 00:13
 * @Description:
 */
public class DeleteInvitationEvent {


    public String id;//有id需要删除,无id刷新

    public DeleteInvitationEvent() {
    }

    public DeleteInvitationEvent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
