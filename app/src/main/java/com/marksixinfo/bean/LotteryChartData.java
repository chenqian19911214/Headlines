package com.marksixinfo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *  开奖柱状图实体
 *
 * @Auther: Administrator
 * @Date: 2019/5/13 0013 20:04
 * @Description:
 */
public class LotteryChartData implements Comparable<LotteryChartData>,Parcelable {


    /**
     * name : 25
     * value : 100
     */

    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }


    @Override
    public int compareTo(LotteryChartData o) {
        return value - o.getValue();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.value);
    }

    public LotteryChartData() {
    }

    protected LotteryChartData(Parcel in) {
        this.name = in.readString();
        this.value = in.readInt();
    }

    public static final Parcelable.Creator<LotteryChartData> CREATOR = new Parcelable.Creator<LotteryChartData>() {
        @Override
        public LotteryChartData createFromParcel(Parcel source) {
            return new LotteryChartData(source);
        }

        @Override
        public LotteryChartData[] newArray(int size) {
            return new LotteryChartData[size];
        }
    };
}
