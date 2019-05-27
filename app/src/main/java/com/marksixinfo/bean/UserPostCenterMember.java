package com.marksixinfo.bean;

/**
 *  他人详情页用户信息
 *
 * @Auther: Administrator
 * @Date: 2019/3/24 0024 11:38
 * @Description:
 */
public class UserPostCenterMember {

    /**
     * Id : 21
     * Nickname : 戒不掉你
     * Face : https://images.34399.com//files/20190324/0307379085788.png
     * Remark : 官方网址 http://www.6631.com
     * Level : 1
     */

    private String Id;
    private String Nickname;
    private String Face;
    private String Remark;
    private int Level;

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

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int Level) {
        this.Level = Level;
    }
}
