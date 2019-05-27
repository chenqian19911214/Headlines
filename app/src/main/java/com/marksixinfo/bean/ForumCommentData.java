package com.marksixinfo.bean;

/**
 *  论坛列表回复
 *
 * @Auther: Administrator
 * @Date: 2019/4/1 0001 15:47
 * @Description:
 */
public class ForumCommentData {


    private String Id;
    private String Nickname;
    private String Content;

    public ForumCommentData() {
    }

    public ForumCommentData(String id, String nickname, String content) {
        this.Id = id;
        Nickname = nickname;
        Content = content;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}
