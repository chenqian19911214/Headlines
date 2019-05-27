package com.marksixinfo.bean;

/**
 *  路珠开奖单双...
 *
 * @Auther: Administrator
 * @Date: 2019/5/14 0014 22:11
 * @Description:
 */
public class RoadBeadDetailData {

    private String string;
    private int color;

    public RoadBeadDetailData() {
    }

    public RoadBeadDetailData(String string, int color) {
        this.string = string;
        this.color = color;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
