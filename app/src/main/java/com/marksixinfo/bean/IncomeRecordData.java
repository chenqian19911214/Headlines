package com.marksixinfo.bean;

/**
 *  收益记录
 *
 * @Auther: Administrator
 * @Date: 2019/4/23 0023 14:11
 * @Description:
 */
public class IncomeRecordData {


    /**
     * Id : 1399
     * User_Id : 3797
     * Price : 11.00
     * Add_Time : 1555946291
     * Type : 1  //1是收入  0是支出
     * Note : 金币兑换余额
     * Ip : 103.110.229.33
     * Device : android
     * Cash_Type : 1
     */

    private String Id;
    private String User_Id;
    private String Price;
    private String Add_Time;
    private int Type;
    private String Note;
    private String Ip;
    private String Device;
    private String Cash_Type;

    private int itemType;//0,条目 1,空太页


    public IncomeRecordData() {
    }

    public IncomeRecordData(int itemType) {
        this.itemType = itemType;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String User_Id) {
        this.User_Id = User_Id;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public String getAdd_Time() {
        return Add_Time;
    }

    public void setAdd_Time(String Add_Time) {
        this.Add_Time = Add_Time;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String Note) {
        this.Note = Note;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String Ip) {
        this.Ip = Ip;
    }

    public String getDevice() {
        return Device;
    }

    public void setDevice(String Device) {
        this.Device = Device;
    }

    public String getCash_Type() {
        return Cash_Type;
    }

    public void setCash_Type(String Cash_Type) {
        this.Cash_Type = Cash_Type;
    }
}
