package com.marksixinfo.bean;

import java.util.List;

/**
 * 论坛二期分页列表
 *
 * @Auther: Administrator
 * @Date: 2019/3/21 0021 23:58
 * @Description:
 */
public class ForumListData {


    private String Id;
    private String Nickname;
    private String User_Id;
    private String Face;
    private List<String> Pic;
    private String Title;
    private String Add_Time;
    private String Content;
    private List<ForumCommentData> commentList;
    private int Reply_Num;
    private int Like_Num;
    private int like_status;
    private int look_status;  // 是否关注，0:未关注;1:已关注 2:自己
    private int Level;

    public int getLevel() {
        return Level;
    }

    public void setLevel(int Level) {
        this.Level = Level;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public String getFace() {
        return Face;
    }

    public void setFace(String face) {
        Face = face;
    }

    public List<String> getPic() {
        return Pic;
    }

    public void setPic(List<String> pic) {
        Pic = pic;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAdd_Time() {
        return Add_Time;
    }

    public void setAdd_Time(String add_Time) {
        Add_Time = add_Time;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public List<ForumCommentData> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<ForumCommentData> commentList) {
        this.commentList = commentList;
    }

    public int getReply_Num() {
        return Reply_Num;
    }

    public void setReply_Num(int reply_Num) {
        Reply_Num = reply_Num;
    }

    public int getLike_Num() {
        return Like_Num;
    }

    public void setLike_Num(int like_Num) {
        Like_Num = like_Num;
    }

    public int getLike_status() {
        return like_status;
    }

    public void setLike_status(int like_status) {
        this.like_status = like_status;
    }

    public int getLook_status() {
        return look_status;
    }

    public void setLook_status(int look_status) {
        this.look_status = look_status;
    }
}
