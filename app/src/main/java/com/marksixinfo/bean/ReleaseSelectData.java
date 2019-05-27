package com.marksixinfo.bean;

/**
 * @Auther: Administrator
 * @Date: 2019/3/20 0020 18:24
 * @Description:
 */
public class ReleaseSelectData {

    private String value;
    private String name;
    private boolean isCheck;

    public ReleaseSelectData() {
    }

    public ReleaseSelectData(String value, String name, boolean isCheck) {
        this.value = value;
        this.name = name;
        this.isCheck = isCheck;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
