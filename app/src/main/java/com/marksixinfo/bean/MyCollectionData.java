package com.marksixinfo.bean;

/**
 * Created by Administrator on 2019/5/11.
 */

public class MyCollectionData {

    /**
     * Id : 1
     * Title : 正宗曾道人
     * Pic : https://images.qz022.com/files/tuku/color/51/001.jpg.w320.jpg
     * favorites : {"status":1,"num":0}
     * like : {"status":1,"num":"0"}
     */

    private String Id;
    private String Title;
    private String Pic;
    private FavoritesBean favorites;
    private LikeBean like;
    private String Item_Id;

    private String Width;
    private String Height;

    public String getWidth() {
        return Width;
    }

    public void setWidth(String width) {
        Width = width;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getItem_Id() {
        return Item_Id;
    }

    public void setItem_Id(String item_Id) {
        Item_Id = item_Id;
    }

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

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getPic() {
        return Pic;
    }

    public void setPic(String Pic) {
        this.Pic = Pic;
    }

    public FavoritesBean getFavorites() {
        return favorites;
    }

    public void setFavorites(FavoritesBean favorites) {
        this.favorites = favorites;
    }

    public LikeBean getLike() {
        return like;
    }

    public void setLike(LikeBean like) {
        this.like = like;
    }

    public static class FavoritesBean {
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

    public static class LikeBean {
        /**
         * status : 1
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
