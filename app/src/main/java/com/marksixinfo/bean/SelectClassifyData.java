package com.marksixinfo.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 分类
 *
 * @Auther: Administrator
 * @Date: 2019/3/18 0018 14:36
 * @Description:
 */
public class SelectClassifyData implements Parcelable {

    /**
     * Id : 38
     * Name : 综合
     * Parent : null
     * Is_Edit=0
     * Seq : 7
     */

    private String Id;
    private String Name;
    private String Parent;
    private String Seq;
    private int Is_Edit;

    private boolean isExpand;//代表当前是否是展开状态
    /**
     * 是否编辑状态
     */
    private boolean isEditState;

    public SelectClassifyData() {
    }

    public SelectClassifyData(String id, String name, String parent, String seq, int is_Edit, boolean isExpand, boolean isEditState) {
        Id = id;
        Name = name;
        Parent = parent;
        Seq = seq;
        Is_Edit = is_Edit;
        this.isExpand = isExpand;
        this.isEditState = isEditState;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public boolean isEditState() {
        return isEditState;
    }

    public void setEditState(boolean editState) {
        isEditState = editState;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getParent() {
        return Parent;
    }

    public void setParent(String parent) {
        Parent = parent;
    }

    public String getSeq() {
        return Seq;
    }

    public void setSeq(String seq) {
        Seq = seq;
    }

    public int getIs_Edit() {
        return Is_Edit;
    }

    public void setIs_Edit(int is_Edit) {
        Is_Edit = is_Edit;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Id);
        dest.writeString(this.Name);
        dest.writeString(this.Parent);
        dest.writeString(this.Seq);
        dest.writeInt(this.Is_Edit);
        dest.writeByte(this.isExpand ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isEditState ? (byte) 1 : (byte) 0);
    }

    protected SelectClassifyData(Parcel in) {
        this.Id = in.readString();
        this.Name = in.readString();
        this.Parent = in.readString();
        this.Seq = in.readString();
        this.Is_Edit = in.readInt();
        this.isExpand = in.readByte() != 0;
        this.isEditState = in.readByte() != 0;
    }

    public static final Creator<SelectClassifyData> CREATOR = new Creator<SelectClassifyData>() {
        @Override
        public SelectClassifyData createFromParcel(Parcel source) {
            return new SelectClassifyData(source);
        }

        @Override
        public SelectClassifyData[] newArray(int size) {
            return new SelectClassifyData[size];
        }
    };
}