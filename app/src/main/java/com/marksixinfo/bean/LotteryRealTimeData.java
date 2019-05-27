package com.marksixinfo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 *  实时开奖实体
 *
 * @Auther: Administrator
 * @Date: 2019/5/18 0018 22:03
 * @Description:
 */
public class LotteryRealTimeData implements Parcelable {


    /**
     * period : 20190302
     * lottery : ["01","02","12","04","05","06","07"]
     */

    private String period;
    private int open;//0,晚上9点20 后台开始重置  1,准备开奖,弹框提醒  2,开球中   3,开奖结束
    private List<String> lottery;

    public LotteryRealTimeData(String period, List<String> lottery) {
        this.period = period;
        this.lottery = lottery;
    }

    public LotteryRealTimeData(String period, int open, List<String> lottery) {
        this.period = period;
        this.open = open;
        this.lottery = lottery;
    }

    public int getIsOpen() {
        return open;
    }

    public void setIsOpen(int open) {
        this.open = open;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public List<String> getLottery() {
        return lottery;
    }

    public void setLottery(List<String> lottery) {
        this.lottery = lottery;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.period);
        dest.writeInt(this.open);
        dest.writeStringList(this.lottery);
    }

    protected LotteryRealTimeData(Parcel in) {
        this.period = in.readString();
        this.open = in.readInt();
        this.lottery = in.createStringArrayList();
    }

    public static final Parcelable.Creator<LotteryRealTimeData> CREATOR = new Parcelable.Creator<LotteryRealTimeData>() {
        @Override
        public LotteryRealTimeData createFromParcel(Parcel source) {
            return new LotteryRealTimeData(source);
        }

        @Override
        public LotteryRealTimeData[] newArray(int size) {
            return new LotteryRealTimeData[size];
        }
    };
}