package com.marksixinfo.bean;

import java.util.List;

/**
 *  图库首页期数
 *
 * @Auther: Administrator
 * @Date: 2019/5/9 0009 14:28
 * @Description:
 */
public class GalleryPeriodData {


    /**
     * type : [{"Id":"2","Name":"黑白图库"},{"Id":"3","Name":"彩色图库"}]
     * period : 2019051
     */

    private String period;
    private List<TypeBean> type;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<TypeBean> getType() {
        return type;
    }

    public void setType(List<TypeBean> type) {
        this.type = type;
    }

    public static class TypeBean {
        /**
         * Id : 2
         * Name : 黑白图库
         */

        private int Id;
        private String Name;

        public TypeBean() {
        }

        public TypeBean(int id, String name) {
            Id = id;
            Name = name;
        }

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }
    }
}
