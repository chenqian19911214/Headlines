package com.marksixinfo.bean;

/**
 * 图片详情 评论 实体类
 *
 * @Auther: Administrator
 * 19点56分
 * @Description:
 */
public class PictureDetailsCommentData{

    /**
     * status : 1
     * result : {"Id":"4075","Title":"正宗曾道人\r","Pic":"https://images.qz022.com/files/tuku/color/45/001.jpg.w320.jpg","Favorites_Num":"0","Reply_Num":"1","Tread":{"status":0,"num":"1"},"Like":{"status":0,"num":"0"}}
     */

    private int status;
    private ResultBean result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        /**
         * Id : 4075
         * Title : 正宗曾道人
         * Pic : https://images.qz022.com/files/tuku/color/45/001.jpg.w320.jpg
         * Favorites_Num : 0
         * Reply_Num : 1
         * Tread : {"status":0,"num":"1"}
         * Like : {"status":0,"num":"0"}
         */

        private String Id;
        private String Title;
        private String Pic;
        private String Favorites_Num;
        private String Reply_Num;
        private TreadBean Tread;
        private LikeBean Like;

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

        public String getFavorites_Num() {
            return Favorites_Num;
        }

        public void setFavorites_Num(String Favorites_Num) {
            this.Favorites_Num = Favorites_Num;
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

        public static class TreadBean {
            /**
             * status : 0
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
}
