package com.marksixinfo.bean;

import com.contrarywind.interfaces.IPickerViewData;

/**
 * @Auther: Administrator
 * @Date: 2019/3/24 0024 11:39
 * @Description:
 */
public class UserPostCenterCategory implements IPickerViewData {

    /**
     * Id : 38
     * Name : 综合
     * Parent : 0
     * Seq : 7
     * User_Id : 0
     */

    private String Id;
    private String Name;
    private String Parent;
    private String Seq;
    private String User_Id;

    public UserPostCenterCategory() {
    }

    public UserPostCenterCategory(String id, String name, String parent, String seq, String user_Id) {
        Id = id;
        Name = name;
        Parent = parent;
        Seq = seq;
        User_Id = user_Id;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getParent() {
        return Parent;
    }

    public void setParent(String Parent) {
        this.Parent = Parent;
    }

    public String getSeq() {
        return Seq;
    }

    public void setSeq(String Seq) {
        this.Seq = Seq;
    }

    public String getUser_Id() {
        return User_Id;
    }

    public void setUser_Id(String User_Id) {
        this.User_Id = User_Id;
    }

    @Override
    public String getPickerViewText() {
        return Name;
    }
}
