package com.marksixinfo.bean;

import com.contrarywind.interfaces.IPickerViewData;

import java.util.List;

/**
 * Created by lenovo on 2019/5/15.
 * 选择期数 实体类
 */

public class SelectPictureData implements IPickerViewData {


    /**
     * year : 2019
     * period : [{"id":"2019051","name":"051"},{"id":"2019050","name":"050"},{"id":"2019049","name":"049"},{"id":"2019048","name":"048"},{"id":"2019047","name":"047"},{"id":"2019046","name":"046"},{"id":"2019045","name":"045"},{"id":"2019044","name":"044"},{"id":"2019043","name":"043"},{"id":"2019042","name":"042"},{"id":"2019041","name":"041"},{"id":"2019040","name":"040"},{"id":"2019039","name":"039"},{"id":"2019038","name":"038"},{"id":"2019037","name":"037"},{"id":"2019036","name":"036"},{"id":"2019035","name":"035"},{"id":"2019034","name":"034"},{"id":"2019033","name":"033"},{"id":"2019032","name":"032"},{"id":"2019031","name":"031"}]
     */

    private int year;
    private List<PeriodBean> period;

    public String getYear() {
        return String.valueOf(year);
    }

    public void setYear(int year) {
        this.year = year;
    }

    public List<PeriodBean> getPeriod() {
        return period;
    }

    public void setPeriod(List<PeriodBean> period) {
        this.period = period;
    }

    @Override
    public String getPickerViewText() {
        return String.valueOf(year)+"年";
    }

    public static class PeriodBean {
        /**
         * id : 2019051
         * name : 051
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
