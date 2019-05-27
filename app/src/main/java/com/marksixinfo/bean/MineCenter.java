package com.marksixinfo.bean;

/**
 * 头条个人中心
 *
 * @Auther: Administrator
 * @Date: 2019/3/26 0026 13:33
 * @Description:
 */
public class MineCenter {


    /**
     * Id : 837
     * Nickname : 测试账号
     * Password : $2y$10$js58pS8nggOOUCLFQw/7quUSnj9XVOj5iog6.kEZrlaAE5Yq.rQaS
     * Tel : 11111111111
     * Email : null
     * Login_Time : 2019/03/26 21:13
     * Reg_Time : 1553523525
     * Face : /static/img/face/default.jpeg
     * Level : 0
     * Safety : 23991e4a4b1486b66e4ce692bba47caa
     * Status : 1
     * Uid : LT000837
     * Look_Last : 0
     * Type : 0
     * Remark : null
     * reply : 5
     * item_num : 3
     */

    private String Id;
    private String Nickname;
    private String Password;
    private String Tel;
    private String Email;
    private String Login_Time;
    private String Reg_Time;
    private String Face;
    private String Remark;
    private int Level;
    private String Safety;
    private String Status;
    private String Uid;
    private String Look_Last;
    private String Type;
    private int reply;
    private int item_num;

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

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
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

    public String getLogin_Time() {
        return Login_Time;
    }

    public void setLogin_Time(String Login_Time) {
        this.Login_Time = Login_Time;
    }

    public String getReg_Time() {
        return Reg_Time;
    }

    public void setReg_Time(String Reg_Time) {
        this.Reg_Time = Reg_Time;
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

    public String getSafety() {
        return Safety;
    }

    public void setSafety(String Safety) {
        this.Safety = Safety;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String Uid) {
        this.Uid = Uid;
    }

    public String getLook_Last() {
        return Look_Last;
    }

    public void setLook_Last(String Look_Last) {
        this.Look_Last = Look_Last;
    }

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public int getReply() {
        return reply;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public int getItem_num() {
        return item_num;
    }

    public void setItem_num(int item_num) {
        this.item_num = item_num;
    }
}
