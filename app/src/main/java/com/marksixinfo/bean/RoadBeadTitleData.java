package com.marksixinfo.bean;

import java.util.List;

/**
 * 开奖路珠实体,第几球
 *
 * @Auther: Administrator
 * @Date: 2019/5/14 0014 19:21
 * @Description:
 */
public class RoadBeadTitleData {

    private String numberTitle;
    private List<RoadBeadDetailData> details;

    public RoadBeadTitleData() {
    }

    public RoadBeadTitleData(String numberTitle, List<RoadBeadDetailData> details) {
        this.numberTitle = numberTitle;
        this.details = details;
    }

    public String getNumberTitle() {
        return numberTitle;
    }

    public void setNumberTitle(String numberTitle) {
        this.numberTitle = numberTitle;
    }

    public List<RoadBeadDetailData> getDetails() {
        return details;
    }

    public void setDetails(List<RoadBeadDetailData> details) {
        this.details = details;
    }
}

