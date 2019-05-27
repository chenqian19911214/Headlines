package com.marksixinfo.bean;

import android.text.SpannableString;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/3/15 0015 17:51
 * @Description:
 */
public class MainHomeData implements Cloneable {


    /**
     * Id : 5123
     * Face : https://images.34399.com//files/20190312/060914.png
     * Color : null
     * Like : 1
     * Reply : 0
     * User_Id : 21
     * View : 33
     * Nickname : 戒不掉你
     * Add_Time : 1小时前
     * Pic : ["https://images.34399.com//files/20190314/084352.png","https://images.34399.com//files/20190314/084357.png"]
     * Title : 全国政协十三届二次会议闭幕
     * Content=哦困哦哦哦墨迹5爱咯来了扣了热啊大大热啊鹅翅几杯dark mode儿科代打1爱儿乐色卡带了伞咳嗽灸是带了伞客气咯是咯是哦了破了是咯破了搜集是咯带哦了是咯破了是哦了是咯是咯哦了热了算了哦了热了上课睡哦了热了算了哦了是可怕1饿就哦了是可怕哦了是...
     * Is_Look : 0
     * <p>
     * <p>
     * Remark	vachar	用户网址或者标签，如果不为空则显示内容
     * Level	int	用户等级 2:官方 1:V 0不显示
     * Pic	int	列表图片缩略图，前端根据LENGTH 判断显示格式
     * Period	int	期数 前端请 做三位补0 显示格式: 012 001
     * Favorites_Num	int	收藏数
     * Like_Num	int	点赞数
     * Reply_Num	int	回帖数
     * look_status	int	是否关注，0:未关注;1:已关注 2:自己
     * like_status	int	是否点赞，0:未;1:已
     * fav_time=20小时前
     * fav_status	int	是否收藏，0:未;1:已
     */

    private String Id;
    private String Item_Id;//文章id
    private String Face;
    //    private Object Color;
    private String Like;
    private String User_Id;
    private String View;
    private String Nickname;
    private String Content;
    private transient SpannableString OldContent;
    private String Add_Time;
    private String Title;
    private int Is_Look;
    private List<String> Pic;
    private String Remark;
    private int Level;
    private int Period;
    private int Like_Num;
    private int Reply_Num;
    private int Favorites_Num;
    private int look_status;  // 是否关注，0:未关注;1:已关注 2:自己
    private int like_status;
    private int fav_status;
    private String fav_time;
    private boolean isOnlyFavorites;//是否只有收藏需要更新

    private boolean isSelect;//是否选中状态

    private ReplyBean Reply;//评论

    private int loadingStatus;//是否是点赞/收藏 调用接口中  0,正常 1,点赞中 2,收藏中

    private int Is_Top;//1,置顶

    private long lastLookTime;//上次查看时间

    private boolean isGoneDecoration;//隐藏分隔条

    private int itemType;//0,条目

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public boolean isGoneDecoration() {
        return isGoneDecoration;
    }

    public void setGoneDecoration(boolean goneDecoration) {
        isGoneDecoration = goneDecoration;
    }

    public long getLastLookTime() {
        return lastLookTime;
    }

    public void setLastLookTime(long lastLookTime) {
        this.lastLookTime = lastLookTime;
    }

    public int getIs_Top() {
        return Is_Top;
    }

    public void setIs_Top(int is_Top) {
        Is_Top = is_Top;
    }

    public int getLoadingStatus() {
        return loadingStatus;
    }

    public void setLoadingStatus(int loadingStatus) {
        this.loadingStatus = loadingStatus;
    }

    public String getItem_Id() {
        return Item_Id;
    }

    public void setItem_Id(String item_Id) {
        Item_Id = item_Id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    private List<ForumCommentData> Reply_List;//论坛列表回复

    public List<ForumCommentData> getCommentList() {
        return Reply_List;
    }

    public void setCommentList(List<ForumCommentData> commentList) {
        this.Reply_List = commentList;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFace() {
        return Face;
    }

    public void setFace(String face) {
        Face = face;
    }

    public String getLike() {
        return Like;
    }

    public void setLike(String like) {
        Like = like;
    }

    public ReplyBean getReply() {
        return Reply;
    }

    public void setReply(ReplyBean reply) {
        Reply = reply;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String user_Id) {
        User_Id = user_Id;
    }

    public String getView() {
        return View;
    }

    public void setView(String view) {
        View = view;
    }

    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public SpannableString getOldContent() {
        return OldContent;
    }

    public void setOldContent(SpannableString oldContent) {
        OldContent = oldContent;
    }

    public String getAdd_Time() {
        return Add_Time;
    }

    public void setAdd_Time(String add_Time) {
        Add_Time = add_Time;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getIs_Look() {
        return Is_Look;
    }

    public void setIs_Look(int is_Look) {
        Is_Look = is_Look;
    }

    public List<String> getPic() {
        return Pic;
    }

    public void setPic(List<String> pic) {
        Pic = pic;
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

    public int getPeriod() {
        return Period;
    }

    public void setPeriod(int period) {
        Period = period;
    }

    public int getLike_Num() {
        return Like_Num;
    }

    public void setLike_Num(int like_Num) {
        Like_Num = like_Num;
    }

    public int getReply_Num() {
        return Reply_Num;
    }

    public void setReply_Num(int reply_Num) {
        Reply_Num = reply_Num;
    }

    public int getFavorites_Num() {
        return Favorites_Num;
    }

    public void setFavorites_Num(int favorites_Num) {
        Favorites_Num = favorites_Num;
    }

    public int getLook_status() {
        return look_status;
    }

    public void setLook_status(int look_status) {
        this.look_status = look_status;
    }

    public int getLike_status() {
        return like_status;
    }

    public void setLike_status(int like_status) {
        this.like_status = like_status;
    }

    public int getFav_status() {
        return fav_status;
    }

    public void setFav_status(int fav_status) {
        this.fav_status = fav_status;
    }

    public String getFav_time() {
        return fav_time;
    }

    public void setFav_time(String fav_time) {
        this.fav_time = fav_time;
    }

    public boolean isOnlyFavorites() {
        return isOnlyFavorites;
    }

    public void setOnlyFavorites(boolean onlyFavorites) {
        isOnlyFavorites = onlyFavorites;
    }

    public static class ReplyBean {
        /**
         * Content : 123213213
         * Like_Num : 0
         * Reply_Num : 0
         * like_status : 0
         * Add_Time : 6秒前
         */
        private String Id;
        private String Content;
        private int Like_Num;
        private int Reply_Num;
        private int like_status;
        private String Add_Time;

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
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

        public String getAdd_Time() {
            return Add_Time;
        }

        public void setAdd_Time(String Add_Time) {
            this.Add_Time = Add_Time;
        }
    }

    @Override
    public MainHomeData clone() {
        MainHomeData data = null;
        try {
            data = (MainHomeData) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return data;
    }

}