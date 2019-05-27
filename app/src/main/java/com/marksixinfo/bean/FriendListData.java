package com.marksixinfo.bean;

/**
 * 我的好友列表
 *
 * @Auther: Administrator
 * @Date: 2019/4/23 0023 15:57
 * @Description:
 */
public class FriendListData {



    /**
     * Nickname : test999
     * User_Id : 4389
     * Add_Time : 1556353002
     * Parent_Id : 3796
     * Face : /static/img/face/default.jpeg
     * Id : 12
     * Status : 0
     * Today_Read : 0
     * Tips_1 : 今日奖励
     * Tips_2 : 好友认真阅读3篇文章,奖励3元
     */

    private String Nickname;
    private String User_Id;
    private String Add_Time;
    private String Parent_Id;
    private String Face;
    private String Id;
    private int Status;
    private int Today_Read;
    private String Tips_1;
    private String Tips_2;
    private int type;//0,正常条目 1,无条目


    public FriendListData() {
    }

    public FriendListData(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getAdd_Time() {
        return Add_Time;
    }

    public void setAdd_Time(String Add_Time) {
        this.Add_Time = Add_Time;
    }

    public String getParent_Id() {
        return Parent_Id;
    }

    public void setParent_Id(String Parent_Id) {
        this.Parent_Id = Parent_Id;
    }

    public String getFace() {
        return Face;
    }

    public void setFace(String Face) {
        this.Face = Face;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public int getToday_Read() {
        return Today_Read;
    }

    public void setToday_Read(int Today_Read) {
        this.Today_Read = Today_Read;
    }

    public String getTips_1() {
        return Tips_1;
    }

    public void setTips_1(String Tips_1) {
        this.Tips_1 = Tips_1;
    }

    public String getTips_2() {
        return Tips_2;
    }

    public void setTips_2(String Tips_2) {
        this.Tips_2 = Tips_2;
    }
}