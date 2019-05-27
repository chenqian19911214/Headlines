package com.marksixinfo.bean;

/**
 * 论坛个人中心
 *
 * @Auther: Administrator
 * @Date: 2019/4/12 0012 13:23
 * @Description:
 */
public class ForumUserCenterData {


    /**
     * Id : 3796
     * Nickname : 测试1
     * Face : https://images.34399.com/files/20190411/1009457658429?1280_720
     * item_num : 0
     * look_num : 1
     * lookme_num : 3
     * like_num : 0
     */

    private String Id;
    private String Nickname;
    private String Face;
    private int item_num;//帖子
    private int look_num;//关注
    private int lookme_num;//粉丝
    private int like_num;//获赞
    private String Remark;
    private int Level;

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

    public int getItem_num() {
        return item_num;
    }

    public void setItem_num(int item_num) {
        this.item_num = item_num;
    }

    public int getLook_num() {
        return look_num;
    }

    public void setLook_num(int look_num) {
        this.look_num = look_num;
    }

    public int getLookme_num() {
        return lookme_num;
    }

    public void setLookme_num(int lookme_num) {
        this.lookme_num = lookme_num;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }
}
