package com.marksixinfo.bean;

/**
 * @Auther: Administrator
 * @Date: 2019/3/28 0028 21:30
 * @Description:
 */
public class CommentsListData {

    /**
     * Id : 151
     * Content : 968078-9970-
     * Add_Time : 47分钟前
     * User_id : 837
     * Item_Id : null
     * Ip : null
     * Nickname : 测试账号
     * Face : https://images.34399.com/files/20190328/0819458423159?50_50
     * Parent_Id : 143
     * Level : 0
     * Like_Num : 0
     * like_status : 0
     */

    private String Id;
    private String Content;
    private String Add_Time;
    private String User_Id;
    private Object Item_Id;
    private Object Ip;
    private String Nickname;
    private String Face;
    private String Parent_Id;
    private String Level;
    private int Like_Num;
    private int like_status;

    private int loadingStatus;//是否是点赞/收藏 调用接口中  0,正常 1,点赞中 2,收藏中

    private int type;//0,条目  1,emptyFoot

    private String footViewText;

    public String getFootViewText() {
        return footViewText;
    }

    public void setFootViewText(String footViewText) {
        this.footViewText = footViewText;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLoadingStatus() {
        return loadingStatus;
    }

    public void setLoadingStatus(int loadingStatus) {
        this.loadingStatus = loadingStatus;
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

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public Object getItem_Id() {
        return Item_Id;
    }

    public void setItem_Id(Object Item_Id) {
        this.Item_Id = Item_Id;
    }

    public Object getIp() {
        return Ip;
    }

    public void setIp(Object Ip) {
        this.Ip = Ip;
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

    public String getParent_Id() {
        return Parent_Id;
    }

    public void setParent_Id(String Parent_Id) {
        this.Parent_Id = Parent_Id;
    }

    public String getLevel() {
        return Level;
    }

    public void setLevel(String Level) {
        this.Level = Level;
    }

    public int getLike_Num() {
        return Like_Num;
    }

    public void setLike_Num(int Like_Num) {
        this.Like_Num = Like_Num;
    }

    public int getLike_status() {
        return like_status;
    }

    public void setLike_status(int like_status) {
        this.like_status = like_status;
    }
}
