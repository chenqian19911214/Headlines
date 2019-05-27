package com.marksixinfo.bean;

/**
 * 消息通知
 *
 * @Auther: Administrator
 * @Date: 2019/4/24 0024 12:34
 * @Description:
 */
public class MessageNotificationData {


    /**
     * Id : 30
     * Content : 发广告
     * Add_Time : 2019-04-29 03:53
     * Type : 2
     * Reply_Id : 25026
     * Reply_Title : 改革ｖｖ
     * Like_Id: "1109",
     * SendBy : {"Id":"3797","Nickname":"测试221","Face":"https://images.34399.com/files/20190411/1012417415275?1920_1080"}
     */

    private String Id;
    private String Content;
    private String Add_Time;
    private int Type; //0:系统消息 1:关注提醒 2:回复提醒 3:帖子点赞通知 4:我的回复点赞通知
    private String Reply_Id; //回复文章ID
    private String Reply_Period; //文章期数
    private String Reply_Title; //回复标题
    private SendByBean SendBy;

    private int Like_Id; //帖子点赞通知 4:我的回复点赞通知 跳转的id

    public int getLike_Id() {
        return Like_Id;
    }

    public void setLike_Id(int like_Id) {
        Like_Id = like_Id;
    }

    public String getReply_Period() {
        return Reply_Period;
    }

    public void setReply_Period(String reply_Period) {
        Reply_Period = reply_Period;
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

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public String getReply_Id() {
        return Reply_Id;
    }

    public void setReply_Id(String Reply_Id) {
        this.Reply_Id = Reply_Id;
    }

    public String getReply_Title() {
        return Reply_Title;
    }

    public void setReply_Title(String Reply_Title) {
        this.Reply_Title = Reply_Title;
    }

    public SendByBean getSendBy() {
        return SendBy;
    }

    public void setSendBy(SendByBean SendBy) {
        this.SendBy = SendBy;
    }

    public static class SendByBean {
        /**
         * Id : 3797
         * Nickname : 测试221
         * Face : https://images.34399.com/files/20190411/1012417415275?1920_1080
         */

        private String Id;
        private String Nickname;
        private String Face;

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

        public String getFace() {
            return Face;
        }

        public void setFace(String Face) {
            this.Face = Face;
        }
    }
}
