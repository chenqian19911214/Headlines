package com.marksixinfo.bean;

/**
 * 图库图解 实体类 list<></>
 * Created by lenovo on 2019/5/14.
 */

public class PictureAnswerData {

    /**
     * Nickname : 测试测试测试测试测试测试测试
     * Face : https://images.34399.com/files/20190411/1009457658429?1280_720
     * User_Id : 3796
     * Id : 1
     * Content : 3242424234
     * Add_Time : 01-01 08:00
     * Item_Id : 4075
     * Status : 0
     * Period_Id : 2019045
     * Title : 傻瓜蛋萨嘎啥
     */

    private String Nickname;
    private String Face;
    private String User_Id;
    private String Id;
    private String Content;
    private String Add_Time;
    private String Item_Id;
    private String Status;
    private String Period_Id;
    private String Title;
    private String imageUri;
    /**
     * Reply_Num : 0
     * Period : 045
     */

    private String Reply_Num;
    private String Period;

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getPeriod_Id() {
        return Period_Id;
    }

    public void setPeriod_Id(String Period_Id) {
        this.Period_Id = Period_Id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getReply_Num() {
        return Reply_Num;
    }

    public void setReply_Num(String Reply_Num) {
        this.Reply_Num = Reply_Num;
    }

    public String getPeriod() {
        return Period;
    }

    public void setPeriod(String Period) {
        this.Period = Period;
    }
}
