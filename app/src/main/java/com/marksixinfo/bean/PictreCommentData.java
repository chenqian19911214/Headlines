package com.marksixinfo.bean;

/**
 * 图库评论 实体类
 * Created by lenovo on 2019/5/14.
 */

public class PictreCommentData {


    /**
     * User_Id : 3889
     * Id : 3
     * Content : 242423423424
     * Add_Time : 05-13 19:37
     * Item_Id : 6808
     * Period_Id : 2019045
     * Nickname : 测试009
     * Face : https://images.34399.com/files/20190412/1105499614296?4032_3024
     * Parent_Id : 0
     * Reply_Num : 0
     * Like : {"num":"0","status":0}
     */

    private String User_Id;
    private String Id;
    private String Content;
    private String Add_Time;
    private String Item_Id;
    private String Period_Id;
    private String Nickname;
    private String Face;
    private String Parent_Id;
    private String Reply_Num;
    private LikeBean Like;
    private int loadingStatus;//是否是点赞/收藏 调用接口中  0,正常 1,点赞中 2,收藏中

    public int getLoadingStatus() {
        return loadingStatus;
    }

    public void setLoadingStatus(int loadingStatus) {
        this.loadingStatus = loadingStatus;
    }
    public PictreCommentData(String nickname) {
        Nickname = nickname;
    }
    public PictreCommentData() {
    }
    private int type;
    private String footViewText;

    public int getType() {
        return type;
    }

    public String getFootViewText() {
        return footViewText;
    }

    public void setFootViewText(String footViewText) {
        this.footViewText = footViewText;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String User_Id) {
        this.User_Id = User_Id;
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

    public String getAdd_Time() {
        return Add_Time;
    }

    public void setAdd_Time(String Add_Time) {
        this.Add_Time = Add_Time;
    }

    public String getItem_Id() {
        return Item_Id;
    }

    public void setItem_Id(String Item_Id) {
        this.Item_Id = Item_Id;
    }

    public String getPeriod_Id() {
        return Period_Id;
    }

    public void setPeriod_Id(String Period_Id) {
        this.Period_Id = Period_Id;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String Nickname) {
        this.Nickname = Nickname;
    }

    public String getFace() {
        return Face;
    }

    public void setFace(String Face) {
        this.Face = Face;
    }

    public String getParent_Id() {
        return Parent_Id;
    }

    public void setParent_Id(String Parent_Id) {
        this.Parent_Id = Parent_Id;
    }

    public String getReply_Num() {
        return Reply_Num;
    }

    public void setReply_Num(String Reply_Num) {
        this.Reply_Num = Reply_Num;
    }

    public LikeBean getLike() {
        return Like;
    }

    public void setLike(LikeBean Like) {
        this.Like = Like;
    }

    public static class LikeBean {
        /**
         * num : 0
         * status : 0
         */

        private String num;
        private int status;

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
