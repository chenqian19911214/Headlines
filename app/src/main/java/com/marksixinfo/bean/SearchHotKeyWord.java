package com.marksixinfo.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @Auther: Administrator
 * @Date: 2019/3/22 0022 14:30
 * @Description:
 */
public class SearchHotKeyWord {


    /**
     * Id : 19
     * Keyword : 金钥匙攻略
     * Seq : 0
     * Add_Time : null
     * Ip : null
     * Is_Hot : 1
     */

    private String Id;
    @SerializedName(value = "Keyword", alternate = {"Name"})
    private String Keyword;
    private String Seq;
    private Object Add_Time;
    private Object Ip;
    private String Is_Hot;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getKeyword() {
        return Keyword;
    }

    public void setKeyword(String Keyword) {
        this.Keyword = Keyword;
    }

    public String getSeq() {
        return Seq;
    }

    public void setSeq(String Seq) {
        this.Seq = Seq;
    }

    public Object getAdd_Time() {
        return Add_Time;
    }

    public void setAdd_Time(Object Add_Time) {
        this.Add_Time = Add_Time;
    }

    public Object getIp() {
        return Ip;
    }

    public void setIp(Object Ip) {
        this.Ip = Ip;
    }

    public String getIs_Hot() {
        return Is_Hot;
    }

    public void setIs_Hot(String Is_Hot) {
        this.Is_Hot = Is_Hot;
    }
}
