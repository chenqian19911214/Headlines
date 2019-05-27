package com.marksixinfo.bean;

import java.util.List;

/**
 * 帖子详情评论
 *
 * @Auther: Administrator
 * @Date: 2019/3/19 0019 12:37
 * @Description:
 */
public class DetailCommentData {


    /**
     * Id : 28
     * Content : 高手，就是踏实，值得一顶!！我永远支持你！实力派高手！！
     * Nickname : 龙游四海
     * Add_Time : 2019/03/10 05:53
     * Face : /static/img/face/default.jpeg
     */



    private String Id;
    private String Content;
    private String Nickname;
    private String Add_Time;
    private String Face;
    private int type;//0,条目  1,九宫格图片  2,emptyFoot

    /**
     * Like_Num : 0
     * Reply_Num : 2
     * like_status : 0
     */

    private int Like_Num;
    private int Reply_Num;
    private int like_status;
    private String User_Id;

    private List<String> pic;

    private int loadingStatus;//是否是点赞/收藏 调用接口中  0,正常 1,点赞中 2,收藏中


    private String footViewText;

    public String getFootViewText() {
        return footViewText;
    }

    public void setFootViewText(String footViewText) {
        this.footViewText = footViewText;
    }

    public int getLoadingStatus() {
        return loadingStatus;
    }

    public void setLoadingStatus(int loadingStatus) {
        this.loadingStatus = loadingStatus;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String Content) {
        this.Content = Content;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String Nickname) {
        this.Nickname = Nickname;
    }

    public String getAdd_Time() {
        return Add_Time;
    }

    public void setAdd_Time(String Add_Time) {
        this.Add_Time = Add_Time;
    }

    public String getFace() {
        return Face;
    }

    public void setFace(String Face) {
        this.Face = Face;
    }

    public int getLike_Num() {
        return Like_Num;
    }

    public void setLike_Num(int Like_Num) {
        this.Like_Num = Like_Num;
    }

    public int getReply_Num() {
        return Reply_Num;
    }

    public void setReply_Num(int Reply_Num) {
        this.Reply_Num = Reply_Num;
    }

    public int getLike_status() {
        return like_status;
    }

    public void setLike_status(int like_status) {
        this.like_status = like_status;
    }
}
