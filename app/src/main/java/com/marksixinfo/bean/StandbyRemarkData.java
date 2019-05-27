package com.marksixinfo.bean;

/**
 *  网址编辑
 *
 * @Auther: Administrator
 * @Date: 2019/5/25 0025 14:59
 * @Description:
 */
public class StandbyRemarkData {


    private String Id;
    private String Url;
    /**
     * 是否编辑状态
     */
    private boolean isEditState;

    private boolean isExpand;//代表当前是否是展开状态

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public boolean isEditState() {
        return isEditState;
    }

    public void setEditState(boolean editState) {
        isEditState = editState;
    }
}
