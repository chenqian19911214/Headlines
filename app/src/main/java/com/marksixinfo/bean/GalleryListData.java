package com.marksixinfo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 图库列表实体
 *
 * @Auther: Administrator
 * @Date: 2019/5/8 0008 18:56
 * @Description:
 */
public class GalleryListData{


    /**
     * keys : A
     * child : [{"Id":"4217","Title":"澳门小神仙第二版 ","Pic":"https://images.qz022.com/files/tuku/color/51/364.jpg.w320.jpg","Width":"320","Height":"228","favorites":{"status":1,"num":"1"},"like":{"status":1,"num":"0"}},{"Id":"4216","Title":"澳门小神仙第一版 ","Pic":"https://images.qz022.com/files/tuku/color/51/363.jpg.w320.jpg","Width":"320","Height":"215","favorites":{"status":1,"num":"0"},"like":{"status":1,"num":"0"}},{"Id":"4195","Title":"A版六合皇 ","Pic":"https://images.qz022.com/files/tuku/color/51/285.jpg.w320.jpg","Width":"320","Height":"494","favorites":{"status":1,"num":"0"},"like":{"status":1,"num":"0"}},{"Id":"4136","Title":"澳门三合图水印原版 ","Pic":"https://images.qz022.com/files/tuku/color/51/066.jpg.w320.jpg","Width":"320","Height":"223","favorites":{"status":1,"num":"1"},"like":{"status":1,"num":"0"}}]
     */

    private String keys;
    private List<ChildBean> child;

    public String getKeys() {
        return keys;
    }

    public void setKeys(String keys) {
        this.keys = keys;
    }

    public List<ChildBean> getChild() {
        return child;
    }

    public void setChild(List<ChildBean> child) {
        this.child = child;
    }

    public static class ChildBean implements Parcelable {
        /**
         * Id : 4217
         * Title : 澳门小神仙第二版
         * Pic : https://images.qz022.com/files/tuku/color/51/364.jpg.w320.jpg
         * Width : 320
         * Height : 228
         * favorites : {"status":1,"num":"1"}
         * like : {"status":1,"num":"0"}
         */

        private String Id;
        private String Title;
        private String Pic;
        private String Width;
        private String Height;
        private FavoritesBean favorites;
        private LikeBean like;

        public int getLoadingStatus() {
            return loadingStatus;
        }

        public void setLoadingStatus(int loadingStatus) {
            this.loadingStatus = loadingStatus;
        }

        private int loadingStatus;//是否是点赞/收藏 调用接口中  0,正常 1,点赞中 2,收藏中

        protected ChildBean(Parcel in) {
            Id = in.readString();
            Title = in.readString();
            Pic = in.readString();
            Width = in.readString();
            Height = in.readString();
        }

        public static final Creator<ChildBean> CREATOR = new Creator<ChildBean>() {
            @Override
            public ChildBean createFromParcel(Parcel in) {
                return new ChildBean(in);
            }

            @Override
            public ChildBean[] newArray(int size) {
                return new ChildBean[size];
            }
        };

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

        public String getWidth() {
            return Width;
        }

        public void setWidth(String Width) {
            this.Width = Width;
        }

        public String getHeight() {
            return Height;
        }

        public void setHeight(String Height) {
            this.Height = Height;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(Id);
            parcel.writeString(Title);
            parcel.writeString(Pic);
            parcel.writeString(Width);
            parcel.writeString(Height);
        }

        public static class FavoritesBean {
            /**
             * status : 1
             * num : 1
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
}


