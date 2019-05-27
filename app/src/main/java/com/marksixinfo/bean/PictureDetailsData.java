package com.marksixinfo.bean;

/**
 * Created by lenovo on 2019/5/14.
 * 图库详情 头 实体类
 */

public class PictureDetailsData{


    /**
     * Id : 6808
     * Title : 白小姐救世民B
     * View_Num : 1
     * Add_Time : 01-01 08:00
     * Pic : https://images.qz022.com/files/tuku/color/45/白小姐救世民B.jpg.w320.jpg
     * Favorites : {"status":0,"num":"0"}
     * Reply_Num : 2
     * Tread : {"status":0,"num":"0"}
     * Like : {"status":0,"num":"0"}
     */

    private String Id;
    private String Title;
    private int View_Num;
    private String Add_Time;
    private String Pic;
    private FavoritesBean Favorites;
    private String Reply_Num;

    /**
     * 用于点赞收藏 图库列表的刷新
     * */
    private int position;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getLoadingStatus() {
        return loadingStatus;
    }

    public void setLoadingStatus(int loadingStatus) {
        this.loadingStatus = loadingStatus;
    }

    private TreadBean Tread;
    private LikeBean Like;

    private int loadingStatus;//是否是点赞/收藏 调用接口中  0,正常 1,点赞中 2,收藏中

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

    public int getView_Num() {
        return View_Num;
    }

    public void setView_Num(int View_Num) {
        this.View_Num = View_Num;
    }

    public String getAdd_Time() {
        return Add_Time;
    }

    public void setAdd_Time(String Add_Time) {
        this.Add_Time = Add_Time;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String Pic) {
        this.Pic = Pic;
    }

    public FavoritesBean getFavorites() {
        return Favorites;
    }

    public void setFavorites(FavoritesBean Favorites) {
        this.Favorites = Favorites;
    }

    public String getReply_Num() {
        return Reply_Num;
    }

    public void setReply_Num(String Reply_Num) {
        this.Reply_Num = Reply_Num;
    }

    public TreadBean getTread() {
        return Tread;
    }

    public void setTread(TreadBean Tread) {
        this.Tread = Tread;
    }

    public LikeBean getLike() {
        return Like;
    }

    public void setLike(LikeBean Like) {
        this.Like = Like;
    }

    public static class FavoritesBean {
        /**
         * status : 0
         * num : 0
         */

        private int status;
        private String num;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }

    public static class TreadBean {
        /**
         * status : 0
         * num : 0
         */

        private int status;
        private String num;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }

    public static class LikeBean {
        /**
         * status : 0
         * num : 0
         */

        private int status;
        private String num;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }
}
