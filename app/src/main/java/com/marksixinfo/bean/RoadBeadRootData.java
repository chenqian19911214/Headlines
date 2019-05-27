package com.marksixinfo.bean;

import java.util.LinkedHashMap;

/**
 * 开奖路珠最外层
 *
 * @Auther: Administrator
 * @Date: 2019/5/15 0015 13:13
 * @Description:
 */
public class RoadBeadRootData {

    private LinkedHashMap<Integer, Boolean> checked;
    private String title;
    private LinkedHashMap<Integer, RoadBeadTitleData> titles;
    private LinkedHashMap<Integer, RoadBeadTitleData> titlesTemp;

    public LinkedHashMap<Integer, Boolean> getChecked() {
        return checked;
    }

    public void setChecked(LinkedHashMap<Integer, Boolean> checked) {
        this.checked = checked;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LinkedHashMap<Integer, RoadBeadTitleData> getTitles() {
        return titles;
    }

    public void setTitles(LinkedHashMap<Integer, RoadBeadTitleData> titles) {
        this.titles = titles;
    }

    public LinkedHashMap<Integer, RoadBeadTitleData> getTitlesTemp() {
        return titlesTemp;
    }

    public void setTitlesTemp(LinkedHashMap<Integer, RoadBeadTitleData> titlesTemp) {
        this.titlesTemp = titlesTemp;
    }
}
