package com.marksixinfo.bean;

/**
 * @Auther: Administrator
 * @Date: 2019/4/1 0001 16:36
 * @Description:
 */
public class ForumCommListBean {

    /**
     * Id : 4880
     * Title : 038期：香港人【三肖中特】已公开
     * Nickname : 香港人
     * Add_Time : 2019/03/31 13:59
     * Face : https://images.34399.com//files/20190226/074137.png
     * Color : null
     * Is_Top : 0
     * Is_Predict : 0
     * View : 5467
     * Like : 580
     * Reply : 38
     */

    private String Id;
    private String Title;
    private String Nickname;
    private String Add_Time;
    private String Face;
    private Object Color;
    private int Is_Top;
    private String View;
    private int Like;
    private int Reply;

    /**
     * 0,正常列表 1,高手贴 2,分类贴
     */
    private int status;


    /**
     * 分类名称
     */
    private String category;


    /**
     * 设置分类的位置
     */
    private int categoryStatus;


    /**
     * 是否是更新
     */
    private boolean isUpdate = true;


    /**
     * 高手帖是否最后一条 0,不是 1,是
     */
    private int lastAceIndex;


    /**
     * 展开的富文本
     */
    private String htmlText;


    /**
     * 是否展开状态
     */
    private boolean isExpend;


    public boolean isExpend() {
        return isExpend;
    }

    public void setExpend(boolean expend) {
        isExpend = expend;
    }

    public String getHtmlText() {
        return htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    public int getLastAceIndex() {
        return lastAceIndex;
    }

    public void setLastAceIndex(int lastAceIndex) {
        this.lastAceIndex = lastAceIndex;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public int getCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(int categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String Nickname) {
        this.Nickname = Nickname;
    }

    public String getAdd_Time() {
        return Add_Time;
    }

    public void setAdd_Time(String Add_Time) {
        this.Add_Time = Add_Time;
    }

    public String getFace() {
        return Face;
    }

    public void setFace(String Face) {
        this.Face = Face;
    }

    public Object getColor() {
        return Color;
    }

    public void setColor(Object Color) {
        this.Color = Color;
    }

    public int getIs_Top() {
        return Is_Top;
    }

    public void setIs_Top(int Is_Top) {
        this.Is_Top = Is_Top;
    }

    public String getView() {
        return View;
    }

    public void setView(String View) {
        this.View = View;
    }

    public int getLike() {
        return Like;
    }

    public void setLike(int Like) {
        this.Like = Like;
    }

    public int getReply() {
        return Reply;
    }

    public void setReply(int Reply) {
        this.Reply = Reply;
    }


    @Override
    public String toString() {
        return "ForumCommListBean{" +
                "Id='" + Id + '\'' +
                ", Title='" + Title + '\'' +
                ", Nickname='" + Nickname + '\'' +
                ", Add_Time='" + Add_Time + '\'' +
                ", Face='" + Face + '\'' +
                ", Color=" + Color +
                ", Is_Top=" + Is_Top +
                ", View='" + View + '\'' +
                ", Like=" + Like +
                ", Reply=" + Reply +
                ", status=" + status +
                ", category='" + category + '\'' +
                ", categoryStatus='" + categoryStatus + '\'' +
                '}';
    }
}
