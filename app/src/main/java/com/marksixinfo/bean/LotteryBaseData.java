package com.marksixinfo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 *
 *  开奖基础数据
 *
 * @Auther: Administrator
 * @Date: 2019/5/11 0011 19:35
 * @Description:
 */
public class LotteryBaseData {


    /**
     * lottery : {"Id":"10607","Year":"2019","V1":{"num":"44","shengxiao":"龙","style":"#259B24"},"V2":{"num":"12","shengxiao":"鼠","style":"#E51C23"},"V3":{"num":"39","shengxiao":"鸡","style":"#259B24"},"V4":{"num":"06","shengxiao":"马","style":"#259B24"},"V5":{"num":"35","shengxiao":"牛","style":"#E51C23"},"V6":{"num":"42","shengxiao":"马","style":"#3F51B5"},"V7":{"num":"22","shengxiao":"虎","style":"#259B24"},"Open_Time":"1557158400","Add_Time":"1557235032","Period":"2019051"}
     * period : {"year":"2019","list":[["051期","2019051"],["050期","2019050"],["049期","2019049"],["048期","2019048"],["047期","2019047"],["046期","2019046"],["045期","2019045"],["044期","2019044"],["043期","2019043"],["042期","2019042"],["041期","2019041"],["040期","2019040"],["039期","2019039"],["038期","2019038"],["037期","2019037"],["036期","2019036"],["035期","2019035"],["034期","2019034"],["033期","2019033"],["032期","2019032"],["031期","2019031"],["030期","2019030"],["029期","2019029"],["028期","2019028"],["027期","2019027"],["026期","2019026"],["025期","2019025"],["024期","2019024"],["023期","2019023"],["022期","2019022"],["021期","2019021"],["020期","2019020"],["019期","2019019"],["018期","2019018"],["017期","2019017"],["016期","2019016"],["015期","2019015"],["014期","2019014"],["013期","2019013"],["012期","2019012"],["011期","2019011"],["010期","2019010"],["009期","2019009"],["008期","2019008"],["007期","2019007"],["006期","2019006"],["005期","2019005"],["004期","2019004"],["003期","2019003"],["002期","2019002"],["001期","2019001"]]}
     * categroy : [{"name":"号码","child":[{"id":1,"name":"特码热门"},{"id":2,"name":"特码遗漏"},{"id":3,"name":"特码两面遗漏"}]},{"name":"生肖","child":[{"id":4,"name":"生肖特码热门"},{"id":5,"name":"生肖特码遗漏"},{"id":6,"name":"生肖正码热门"}]},{"name":"波色","child":[{"id":7,"name":"波色特码热门"},{"id":8,"name":"特码遗漏"},{"id":9,"name":"波色正码热门"}]},{"name":"路珠","child":[{"id":10,"name":"单双路珠"},{"id":11,"name":"波色路珠"},{"id":12,"name":"大小路珠"}]}]
     */

    private LotteryBean lottery;
    private PeriodBean period;
    private List<CategroyBean> categroy;

    public LotteryBean getLottery() {
        return lottery;
    }

    public void setLottery(LotteryBean lottery) {
        this.lottery = lottery;
    }

    public PeriodBean getPeriod() {
        return period;
    }

    public void setPeriod(PeriodBean period) {
        this.period = period;
    }

    public List<CategroyBean> getCategroy() {
        return categroy;
    }

    public void setCategroy(List<CategroyBean> categroy) {
        this.categroy = categroy;
    }

    public static class LotteryBean {
        /**
         * Id : 10607
         * Year : 2019
         * V1 : {"num":"44","shengxiao":"龙","style":"#259B24"}
         * V2 : {"num":"12","shengxiao":"鼠","style":"#E51C23"}
         * V3 : {"num":"39","shengxiao":"鸡","style":"#259B24"}
         * V4 : {"num":"06","shengxiao":"马","style":"#259B24"}
         * V5 : {"num":"35","shengxiao":"牛","style":"#E51C23"}
         * V6 : {"num":"42","shengxiao":"马","style":"#3F51B5"}
         * V7 : {"num":"22","shengxiao":"虎","style":"#259B24"}
         * Open_Time : 1557158400
         * Add_Time : 1557235032
         * Period : 2019051
         */

        private String Id;
        private String Year;
        private VBean V1;
        private VBean V2;
        private VBean V3;
        private VBean V4;
        private VBean V5;
        private VBean V6;
        private VBean V7;
        private String Open_Time;
        private String Add_Time;
        private String Period;

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getYear() {
            return Year;
        }

        public void setYear(String Year) {
            this.Year = Year;
        }

        public VBean getV1() {
            return V1;
        }

        public void setV1(VBean V1) {
            this.V1 = V1;
        }

        public VBean getV2() {
            return V2;
        }

        public void setV2(VBean V2) {
            this.V2 = V2;
        }

        public VBean getV3() {
            return V3;
        }

        public void setV3(VBean V3) {
            this.V3 = V3;
        }

        public VBean getV4() {
            return V4;
        }

        public void setV4(VBean V4) {
            this.V4 = V4;
        }

        public VBean getV5() {
            return V5;
        }

        public void setV5(VBean V5) {
            this.V5 = V5;
        }

        public VBean getV6() {
            return V6;
        }

        public void setV6(VBean V6) {
            this.V6 = V6;
        }

        public VBean getV7() {
            return V7;
        }

        public void setV7(VBean V7) {
            this.V7 = V7;
        }

        public String getOpen_Time() {
            return Open_Time;
        }

        public void setOpen_Time(String Open_Time) {
            this.Open_Time = Open_Time;
        }

        public String getAdd_Time() {
            return Add_Time;
        }

        public void setAdd_Time(String Add_Time) {
            this.Add_Time = Add_Time;
        }

        public String getPeriod() {
            return Period;
        }

        public void setPeriod(String Period) {
            this.Period = Period;
        }

        public static class VBean implements Parcelable {
            /**
             * num : 44
             * shengxiao : 龙
             * style : #259B24
             */

            private String num;
            private String shengxiao;
            private String style;

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }

            public String getShengxiao() {
                return shengxiao;
            }

            public void setShengxiao(String shengxiao) {
                this.shengxiao = shengxiao;
            }

            public String getStyle() {
                return style;
            }

            public void setStyle(String style) {
                this.style = style;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.num);
                dest.writeString(this.shengxiao);
                dest.writeString(this.style);
            }

            public VBean() {
            }

            protected VBean(Parcel in) {
                this.num = in.readString();
                this.shengxiao = in.readString();
                this.style = in.readString();
            }

            public static final Parcelable.Creator<VBean> CREATOR = new Parcelable.Creator<VBean>() {
                @Override
                public VBean createFromParcel(Parcel source) {
                    return new VBean(source);
                }

                @Override
                public VBean[] newArray(int size) {
                    return new VBean[size];
                }
            };
        }

    }

    public static class PeriodBean {
        /**
         * year : 2019
         * list : [["051期","2019051"],["050期","2019050"],["049期","2019049"],["048期","2019048"],["047期","2019047"],["046期","2019046"],["045期","2019045"],["044期","2019044"],["043期","2019043"],["042期","2019042"],["041期","2019041"],["040期","2019040"],["039期","2019039"],["038期","2019038"],["037期","2019037"],["036期","2019036"],["035期","2019035"],["034期","2019034"],["033期","2019033"],["032期","2019032"],["031期","2019031"],["030期","2019030"],["029期","2019029"],["028期","2019028"],["027期","2019027"],["026期","2019026"],["025期","2019025"],["024期","2019024"],["023期","2019023"],["022期","2019022"],["021期","2019021"],["020期","2019020"],["019期","2019019"],["018期","2019018"],["017期","2019017"],["016期","2019016"],["015期","2019015"],["014期","2019014"],["013期","2019013"],["012期","2019012"],["011期","2019011"],["010期","2019010"],["009期","2019009"],["008期","2019008"],["007期","2019007"],["006期","2019006"],["005期","2019005"],["004期","2019004"],["003期","2019003"],["002期","2019002"],["001期","2019001"]]
         */

        private String year;
        private List<List<String>> list;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public List<List<String>> getList() {
            return list;
        }

        public void setList(List<List<String>> list) {
            this.list = list;
        }
    }

    public static class CategroyBean {
        /**
         * name : 号码
         * child : [{"id":1,"name":"特码热门"},{"id":2,"name":"特码遗漏"},{"id":3,"name":"特码两面遗漏"}]
         */

        private String name;
        private List<ChildBean> child;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<ChildBean> getChild() {
            return child;
        }

        public void setChild(List<ChildBean> child) {
            this.child = child;
        }

        public static class ChildBean implements Parcelable {
            /**
             * id : 1
             * name : 特码热门
             */

            private int id;
            private String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.name);
            }

            public ChildBean() {
            }

            protected ChildBean(Parcel in) {
                this.id = in.readInt();
                this.name = in.readString();
            }

            public static final Creator<ChildBean> CREATOR = new Creator<ChildBean>() {
                @Override
                public ChildBean createFromParcel(Parcel source) {
                    return new ChildBean(source);
                }

                @Override
                public ChildBean[] newArray(int size) {
                    return new ChildBean[size];
                }
            };
        }
    }
}
