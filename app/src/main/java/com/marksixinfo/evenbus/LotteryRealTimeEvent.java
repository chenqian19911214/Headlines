package com.marksixinfo.evenbus;

import android.os.Parcel;
import android.os.Parcelable;

import com.marksixinfo.bean.LotteryRealTimeData;

/**
 *  实时开奖webSocket
 *
 * @Auther: Administrator
 * @Date: 2019/5/18 0018 19:04
 * @Description:
 */
public class LotteryRealTimeEvent implements Parcelable {

    //{"period":"20190302","lottery":["23","23","12","05","05","06","12"]}

    private int type;//0,开奖结束   1,准备开奖    2,开奖中
    private long showTime;//倒计时时间
    private LotteryRealTimeData message;//传输的消息

    public LotteryRealTimeEvent() {
    }

    public LotteryRealTimeEvent(int type, LotteryRealTimeData message) {
        this.type = type;
        this.message = message;
    }

    public LotteryRealTimeEvent(int type, long showTime, LotteryRealTimeData message) {
        this.type = type;
        this.showTime = showTime;
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getShowTime() {
        return showTime;
    }

    public void setShowTime(long showTime) {
        this.showTime = showTime;
    }

    public LotteryRealTimeData getMessage() {
        return message;
    }

    public void setMessage(LotteryRealTimeData message) {
        this.message = message;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeLong(this.showTime);
        dest.writeParcelable(this.message, flags);
    }

    protected LotteryRealTimeEvent(Parcel in) {
        this.type = in.readInt();
        this.showTime = in.readLong();
        this.message = in.readParcelable(LotteryRealTimeData.class.getClassLoader());
    }

    public static final Parcelable.Creator<LotteryRealTimeEvent> CREATOR = new Parcelable.Creator<LotteryRealTimeEvent>() {
        @Override
        public LotteryRealTimeEvent createFromParcel(Parcel source) {
            return new LotteryRealTimeEvent(source);
        }

        @Override
        public LotteryRealTimeEvent[] newArray(int size) {
            return new LotteryRealTimeEvent[size];
        }
    };
}
