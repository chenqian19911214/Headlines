package com.marksixinfo.bean;

/**
 * @Auther: Administrator
 * @Date: 2019/3/28 0028 21:29
 * @Description:
 */
public class CommentsListDetail {


    /**
     * Id : 143
     * Content : 4545654467
     * Add_Time : 1小时前
     * User_id : 837
     * Item_Id : 5317
     * Ip : null
     * Nickname : 测试账号
     * Face : https://images.34399.com/files/20190328/0819458423159?50_50
     * Parent_Id : 0
     * Level : 0
     * Like_Num : 1
     * Reply_Num : 4
     * like_status : 1
     */

    private String Id;
    private String Content;
    private String Add_Time;
    private String User_Id;
    private String Item_Id;
    private String Nickname;
    private String Face;
    private String Parent_Id;
    private String Level;
    private int Like_Num;
    private int Reply_Num;
    private int like_status;
    private int look_status;
    private boolean isOnlyReply_Num;

    private int loadingStatus;//是否是点赞/收藏 调用接口中  0,正常 1,点赞中 2,收藏中

    private LikeBean Like;

    public LikeBean getLike() {
        return Like;
    }

    public void setLike(LikeBean like) {
        Like = like;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public int getLoadingStatus() {
        return loadingStatus;
    }

    public void setLoadingStatus(int loadingStatus) {
        this.loadingStatus = loadingStatus;
    }

    public boolean isOnlyReply_Num() {
        return isOnlyReply_Num;
    }

    public void setOnlyReply_Num(boolean onlyReply_Num) {
        isOnlyReply_Num = onlyReply_Num;
    }

    public int getLook_status() {
        return look_status;
    }

    public void setLook_status(int look_status) {
        this.look_status = look_status;
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

    public String getUser_id() {
        return User_Id;
    }

    public void setUser_id(String User_id) {
        this.User_Id = User_id;
    }

    public String getItem_Id() {
        return Item_Id;
    }

    public void setItem_Id(String Item_Id) {
        this.Item_Id = Item_Id;
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

    public int getReply_Num() {
        return Reply_Num;
    }

    public void setReply_Num(int Reply_Num) {
        this.Reply_Num = Reply_Num;
    }

    public int getLike_status() {
        return like_status;
    }

    public void setLike_status(int like_status) {
        this.like_status = like_status;
    }

    public static class LikeBean {
        /**
         * status : 1
         * num : 0
         */

        private int status;
        private int num;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
