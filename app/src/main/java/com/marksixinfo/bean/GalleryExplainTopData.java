package com.marksixinfo.bean;

/**
 * Created by lenovo on 2019/5/15.
 * 图解详情头布局实体类
 */

public class GalleryExplainTopData {

    /**
     * Id : 1
     * Nickname : 测试测试测试测试测试测试测试
     * User_Id : 3796
     * Face : https://images.34399.com/files/20190411/1009457658429?1280_720
     * Title : 傻瓜蛋萨嘎啥
     * Add_Time : 1970-01-01 08:00
     * Reply_Num : 0
     * Like : {"num":"0","status":0}
     * Tread : {"num":"1","status":0}
     */

    private String Id;
    private String Nickname;
    private String User_Id;
    private String Face;
    private String Title;
    private String Add_Time;
    private String Content;
    private int Reply_Num;
    private LikeBean Like;
    private TreadBean Tread;

    private int loadingStatus;//是否是点赞/收藏 调用接口中  0,正常 1,点赞中 2,收藏中


    public int getLoadingStatus() {
        return loadingStatus;
    }

    public void setLoadingStatus(int loadingStatus) {
        this.loadingStatus = loadingStatus;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String Nickname) {
        this.Nickname = Nickname;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String User_Id) {
        this.User_Id = User_Id;
    }

    public String getFace() {
        return Face;
    }

    public void setFace(String Face) {
        this.Face = Face;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getAdd_Time() {
        return Add_Time;
    }

    public void setAdd_Time(String Add_Time) {
        this.Add_Time = Add_Time;
    }

    public int getReply_Num() {
        return Reply_Num;
    }

    public void setReply_Num(int Reply_Num) {
        this.Reply_Num = Reply_Num;
    }

    public LikeBean getLike() {
        return Like;
    }

    public void setLike(LikeBean Like) {
        this.Like = Like;
    }

    public TreadBean getTread() {
        return Tread;
    }

    public void setTread(TreadBean Tread) {
        this.Tread = Tread;
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

    public static class TreadBean {
        /**
         * num : 1
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
