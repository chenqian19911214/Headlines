package com.marksixinfo.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/5/11.
 */

public class CarSearchData {

    /**
     * total : 16
     * list : [{"Id":"7003","Title":"牛派系列10\r","Label":"牛派系列10","Class_Id":"3","Seq":"0","Keys":"N","Width":"341","Height":"488","Pic":"https://images.qz022.com/files/tuku/color/51/牛派系列10.jpg.w320.jpg","Period":["51","50","49","48","47","46"]},{"Id":"6994","Title":"牛派系列1\r","Label":"牛派系列1","Class_Id":"3","Seq":"0","Keys":"N","Width":"341","Height":"488","Pic":"https://images.qz022.com/files/tuku/color/51/牛派系列1.jpg.w320.jpg","Period":["51","50","49","48","47","46"]},{"Id":"6991","Title":"牛派1494救世报\r","Label":"牛派1494救世报","Class_Id":"3","Seq":"0","Keys":"N","Width":"776","Height":"1203","Pic":"https://images.qz022.com/files/tuku/color/51/牛派1494救世报.jpg.w320.jpg","Period":["51","50","49","48","47","46"]},{"Id":"6879","Title":"挂牌系列11\r","Label":"挂牌系列11","Class_Id":"3","Seq":"0","Keys":"G","Width":"1100","Height":"741","Pic":"https://images.qz022.com/files/tuku/color/51/挂牌系列11.jpg.w320.jpg","Period":["51","50","49","48","47","46"]},{"Id":"6878","Title":"挂牌系列10\r","Label":"挂牌系列10","Class_Id":"3","Seq":"0","Keys":"G","Width":"1100","Height":"805","Pic":"https://images.qz022.com/files/tuku/color/51/挂牌系列10.jpg.w320.jpg","Period":["51","50","49","48","47","46"]},{"Id":"6869","Title":"挂牌系列1\r","Label":"挂牌系列1","Class_Id":"3","Seq":"0","Keys":"G","Width":"1100","Height":"726","Pic":"https://images.qz022.com/files/tuku/color/51/挂牌系列1.jpg.w320.jpg","Period":["51","50","49","48","47","46"]},{"Id":"6851","Title":"赌王1\r","Label":"赌王1","Class_Id":"3","Seq":"0","Keys":"D","Width":"828","Height":"1170","Pic":"https://images.qz022.com/files/tuku/color/51/赌王1.jpg.w320.jpg","Period":["51","50","49","48","47","46"]},{"Id":"6798","Title":"2011B\r","Label":"2011B","Class_Id":"3","Seq":"0","Keys":"#","Width":"972","Height":"1391","Pic":"https://images.qz022.com/files/tuku/color/51/2011B.jpg.w320.jpg","Period":["51","50","49","48","47","46"]},{"Id":"6797","Title":"2011A\r","Label":"2011A","Class_Id":"3","Seq":"0","Keys":"#","Width":"972","Height":"1391","Pic":"https://images.qz022.com/files/tuku/color/51/2011A.jpg.w320.jpg","Period":["51","50","49","48","47","46"]},{"Id":"6720","Title":"通天报1\r","Label":"通天报1","Class_Id":"3","Seq":"0","Keys":"T","Width":"1221","Height":"1686","Pic":"https://images.qz022.com/files/tuku/color/51/通天报1.jpg.w320.jpg","Period":["51","50","49","48","47","46"]},{"Id":"6717","Title":"通缉令1\r","Label":"通缉令1","Class_Id":"3","Seq":"0","Keys":"T","Width":"889","Height":"1226","Pic":"https://images.qz022.com/files/tuku/color/51/通缉令1.jpg.w320.jpg","Period":["51","50","49","48","47","46"]},{"Id":"4929","Title":"特码诗168\r","Label":"a719","Class_Id":"2","Seq":"0","Keys":"T","Width":"536","Height":"363","Pic":"https://images.qz022.com/files/tuku/black/51/a719.jpg.w320.jpg","Period":["51","50","49","48","47","46"]},{"Id":"4877","Title":"六合168\r","Label":"a567","Class_Id":"2","Seq":"0","Keys":"L","Width":"1222","Height":"878","Pic":"https://images.qz022.com/files/tuku/black/51/a567.jpg.w320.jpg","Period":["51","50","49","48","47","46"]},{"Id":"4335","Title":"白姐内幕传真-1\r","Label":"j153","Class_Id":"2","Seq":"0","Keys":"B","Width":"320","Height":"210","Pic":"https://images.qz022.com/files/tuku/black/51/j153.jpg.w320.jpg","Period":["51","50","49","48","47","46"]},{"Id":"4296","Title":"1494救世报B\r","Label":"h516","Class_Id":"2","Seq":"0","Keys":"#","Width":"320","Height":"496","Pic":"https://images.qz022.com/files/tuku/black/51/h516.jpg.w320.jpg","Period":["51","50","49","48","47","46"]},{"Id":"4293","Title":"1494救世报\r","Label":"h513","Class_Id":"2","Seq":"0","Keys":"#","Width":"320","Height":"496","Pic":"https://images.qz022.com/files/tuku/black/51/h513.jpg.w320.jpg","Period":["51","50","49","48","47","46"]}]
     */

    private String total;
    private List<ListBean> list;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * Id : 7003
         * Title : 牛派系列10
         * Label : 牛派系列10
         * Class_Id : 3
         * Seq : 0
         * Keys : N
         * Width : 341
         * Height : 488
         * Pic : https://images.qz022.com/files/tuku/color/51/牛派系列10.jpg.w320.jpg
         * Period : ["51","50","49","48","47","46"]
         */

        private String Id;
        private String Title;
        private String Label;
        private String Class_Id;
        private String Seq;
        private String Keys;
        private String Width;
        private String Height;
        private String Pic;
        private List<String> Period;

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

        public String getLabel() {
            return Label;
        }

        public void setLabel(String Label) {
            this.Label = Label;
        }

        public String getClass_Id() {
            return Class_Id;
        }

        public void setClass_Id(String Class_Id) {
            this.Class_Id = Class_Id;
        }

        public String getSeq() {
            return Seq;
        }

        public void setSeq(String Seq) {
            this.Seq = Seq;
        }

        public String getKeys() {
            return Keys;
        }

        public void setKeys(String Keys) {
            this.Keys = Keys;
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

        public String getPic() {
            return Pic;
        }

        public void setPic(String Pic) {
            this.Pic = Pic;
        }

        public List<String> getPeriod() {
            return Period;
        }

        public void setPeriod(List<String> Period) {
            this.Period = Period;
        }
    }
}
