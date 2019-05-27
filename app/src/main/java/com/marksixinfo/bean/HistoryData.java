package com.marksixinfo.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 *  搜索历史及热词
 *
 * @Auther: Administrator
 * @Date: 2019/3/16 0016 19:05
 * @Description:
 */
public class HistoryData implements Parcelable {

    private List<String> list;

    private List<String> hotList;


    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public List<String> getHotList() {
        return hotList;
    }

    public void setHotList(List<String> hotList) {
        this.hotList = hotList;
    }

    public HistoryData(List<String> list) {
        this.list = list;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(this.list);
        dest.writeStringList(this.hotList);
    }

    public HistoryData() {
    }

    protected HistoryData(Parcel in) {
        this.list = in.createStringArrayList();
        this.hotList = in.createStringArrayList();
    }

    public static final Creator<HistoryData> CREATOR = new Creator<HistoryData>() {
        @Override
        public HistoryData createFromParcel(Parcel source) {
            return new HistoryData(source);
        }

        @Override
        public HistoryData[] newArray(int size) {
            return new HistoryData[size];
        }
    };
}