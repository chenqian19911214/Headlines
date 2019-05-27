package com.marksixinfo.bean;

/**
 * @Auther: Administrator
 * @Date: 2019/3/27 0027 18:32
 * @Description:
 */
public class SettingData {


    /**
     * Id : 839
     * Nickname : ceshi3
     * Face : /static/img/face/default.jpeg
     * Tel : 33333333333
     * Email : 未录入
     * Uid : LT000839
     */

    private String Id;
    private String Nickname;
    private String Face;
    private String Tel;
    private String Email;
    private String Uid;
    private String Remark;
    private int Level;
    private String Last_Login;

    public String getLast_Login() {
        return Last_Login;
    }

    public void setLast_Login(String last_Login) {
        Last_Login = last_Login;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
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

    public String getFace() {
        return Face;
    }

    public void setFace(String Face) {
        this.Face = Face;
    }

    public String getTel() {
        return Tel;
    }

    public void setTel(String Tel) {
        this.Tel = Tel;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String Uid) {
        this.Uid = Uid;
    }
}
