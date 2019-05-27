package com.marksixinfo.bean;

/**
 * 登录
 *
 * @Auther: Administrator
 * @Date: 2019/3/19 0019 20:09
 * @Description:
 */
public class LoginData {
    /**
     * Id : 21
     * Safety : 528d1ccf4429981cbc64a0e2db8ff392
     * Password : $2y$10$Q6iWbl/uUZynnsurAkak8Oeoq5g97IL1VSvzYhhjAHiki2JrRfzsO
     * Status : 1
     * <p>
     * {
     * //        "Id": "21", //对应接口地址中的[user_id]
     * //            "Safety": "528d1ccf4429981cbc64a0e2db8ff392", //对应接口地址中的[key]
     * //            "Password": "$2y$10$Q6iWbl/uUZynnsurAkak8Oeoq5g97IL1VSvzYhhjAHiki2JrRfzsO",
     * //            "Status": "1"
     * //    }
     * Uid=LT003797
     */

    private String Id;
    private String Safety;
    private String Password;
    private String Status;
    private String Face;
    private String Nickname;
    private String Phone;
    private String Uid;//邀请码

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getFace() {
        return Face;
    }

    public void setFace(String face) {
        this.Face = face;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getSafety() {
        return Safety;
    }

    public void setSafety(String Safety) {
        this.Safety = Safety;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }


}
