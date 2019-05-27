package com.marksixinfo.bean;

/**
 * 我的关注
 *
 * @Auther: Administrator
 * @Date: 2019/3/26 0026 21:20
 * @Description:
 */
public class MineConcernData {

    /**
     * Id : 17
     * User_Id : 837
     * Add_Time : 2019/03/25
     * Member_Id : 829
     * Nickname : 大黄瓜
     * Face : https://images.34399.com//files/20190324/0739098977452.png
     * Level : 0
     * Uid : LT000829
     */

    private String Id;
    private String User_Id;
    private String Add_Time;
    private String Member_Id;
    private String Nickname;
    private String Face;
    private int Level;
    private String Uid;
    private int look_status;  // 是否关注，0:未关注;1:已关注 2:自己
    private String Remark;

    public int getLook_status() {
        return look_status;
    }

    public void setLook_status(int look_status) {
        this.look_status = look_status;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
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

    public String getMember_Id() {
        return Member_Id;
    }

    public void setMember_Id(String Member_Id) {
        this.Member_Id = Member_Id;
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

    public int getLevel() {
        return Level;
    }

    public void setLevel(int Level) {
        this.Level = Level;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String Uid) {
        this.Uid = Uid;
    }
}
