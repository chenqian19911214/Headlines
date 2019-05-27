package com.marksixinfo.bean;

import java.util.List;

/**
 * 我的帖子 编辑 获取数据
 *
 * @Auther: Administrator
 * @Date: 2019/4/12 0012 21:58
 * @Description:
 */
public class ForumModifyItemData {


    /**
     * Id : 6376
     * Content : 测试发布测试发布测试发布测试发布测试发布测试发布测试发布测试发布测试发布测试发布测试发布
     * Add_Time : 1555084314
     * User_Id : 3796
     * Class_Id : null
     * View : 7
     * Seq : 0
     * Ip : null
     * Title : 测试发布
     * Color : null
     * Reply : 0
     * Favorites : 0
     * Like : 1
     * Pic : ["https://images.34399.com/files/20190412/0347549002603?640_461","https://images.34399.com/files/20190412/0347564453176?640_495"]
     * Period : null
     * Is_Top : 0
     * Is_Predict : 0
     * Status : 0
     */

    private String Id;
    private String Content;
    private String Add_Time;
    private String User_Id;
    private Object Class_Id;
    private String View;
    private String Seq;
    private Object Ip;
    private String Title;
    private Object Color;
    private String Reply;
    private String Favorites;
    private String Like;
    private Object Period;
    private String Is_Top;
    private String Is_Predict;
    private String Status;
    private List<String> Pic;

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

    public void setUser_Id(String User_Id) {
        this.User_Id = User_Id;
    }

    public Object getClass_Id() {
        return Class_Id;
    }

    public void setClass_Id(Object Class_Id) {
        this.Class_Id = Class_Id;
    }

    public String getView() {
        return View;
    }

    public void setView(String View) {
        this.View = View;
    }

    public String getSeq() {
        return Seq;
    }

    public void setSeq(String Seq) {
        this.Seq = Seq;
    }

    public Object getIp() {
        return Ip;
    }

    public void setIp(Object Ip) {
        this.Ip = Ip;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public Object getColor() {
        return Color;
    }

    public void setColor(Object Color) {
        this.Color = Color;
    }

    public String getReply() {
        return Reply;
    }

    public void setReply(String Reply) {
        this.Reply = Reply;
    }

    public String getFavorites() {
        return Favorites;
    }

    public void setFavorites(String Favorites) {
        this.Favorites = Favorites;
    }

    public String getLike() {
        return Like;
    }

    public void setLike(String Like) {
        this.Like = Like;
    }

    public Object getPeriod() {
        return Period;
    }

    public void setPeriod(Object Period) {
        this.Period = Period;
    }

    public String getIs_Top() {
        return Is_Top;
    }

    public void setIs_Top(String Is_Top) {
        this.Is_Top = Is_Top;
    }

    public String getIs_Predict() {
        return Is_Predict;
    }

    public void setIs_Predict(String Is_Predict) {
        this.Is_Predict = Is_Predict;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public List<String> getPic() {
        return Pic;
    }

    public void setPic(List<String> Pic) {
        this.Pic = Pic;
    }
}
