package com.marksixinfo.bean;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/3/23 0023 12:25
 * @Description:
 */
public class DetailData {


    /**
     * Id : 5198
     * Title : 118开奖推荐《平特一肖》每期赚钱不是梦
     * Nickname : 戒不掉你
     * Face : https://images.34399.com//files/20190324/0307379085788.png
     * Add_Time : 55分钟前
     * Level : 1
     * Period : 11
     * Content : 035期推荐平特一肖：虎虎虎
     * Reply : 2
     * User_Id : 21
     * Is_Lock : 0
     * Is_Predict : 0
     * Like : 0
     * look_status : 0
     * fav_status : 0
     * like_status : 0
     * reply_num : 2
     * favorites_num : 0
     * like_num : 0
     * Next : 5124
     */

    private String Id;
    private String Title;
    private String Nickname;
    private String Face;
    private String Add_Time;
    private int Level;
    private int Period;
    private String Content;
    private String Reply;
    private String User_Id;
    private String Is_Lock;
    private String Is_Predict;
    private String Like;
    private int look_status;
    private int fav_status;
    private int like_status;
    private int reply_num;
    private int favorites_num;
    private int like_num;
    private String Next;
    private String Remark;
    private List<String> Pic;

    private int loadingStatus;//是否是点赞/收藏 调用接口中  0,正常 1,点赞中 2,收藏中

    public int getLoadingStatus() {
        return loadingStatus;
    }

    public void setLoadingStatus(int loadingStatus) {
        this.loadingStatus = loadingStatus;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String Nickname) {
        Nickname = Nickname;
    }

    public String getFace() {
        return Face;
    }

    public void setFace(String face) {
        Face = face;
    }

    public String getAdd_Time() {
        return Add_Time;
    }

    public void setAdd_Time(String add_Time) {
        Add_Time = add_Time;
    }

    public int getLevel() {
        return Level;
    }

    public void setLevel(int level) {
        Level = level;
    }

    public int getPeriod() {
        return Period;
    }

    public void setPeriod(int period) {
        Period = period;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getReply() {
        return Reply;
    }

    public void setReply(String reply) {
        Reply = reply;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public String getIs_Lock() {
        return Is_Lock;
    }

    public void setIs_Lock(String is_Lock) {
        Is_Lock = is_Lock;
    }

    public String getIs_Predict() {
        return Is_Predict;
    }

    public void setIs_Predict(String is_Predict) {
        Is_Predict = is_Predict;
    }

    public String getLike() {
        return Like;
    }

    public void setLike(String like) {
        Like = like;
    }

    public int getLook_status() {
        return look_status;
    }

    public void setLook_status(int look_status) {
        this.look_status = look_status;
    }

    public int getFav_status() {
        return fav_status;
    }

    public void setFav_status(int fav_status) {
        this.fav_status = fav_status;
    }
    public int getLike_status() {
        return like_status;
    }

    public void setLike_status(int like_status) {
        this.like_status = like_status;
    }

    public int getReply_num() {
        return reply_num;
    }

    public void setReply_num(int reply_num) {
        this.reply_num = reply_num;
    }

    public int getFavorites_num() {
        return favorites_num;
    }

    public void setFavorites_num(int favorites_num) {
        this.favorites_num = favorites_num;
    }

    public int getLike_num() {
        return like_num;
    }

    public void setLike_num(int like_num) {
        this.like_num = like_num;
    }

    public String getNext() {
        return Next;
    }

    public void setNext(String next) {
        Next = next;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public List<String> getPic() {
        return Pic;
    }

    public void setPic(List<String> pic) {
        Pic = pic;
    }

}