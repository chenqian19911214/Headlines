package com.marksixinfo.bean;

/**
 *  开奖分类
 *
 * @Auther: Administrator
 * @Date: 2019/5/13 0013 14:21
 * @Description:
 */
public class LotteryTitleBean {

    String titleName;
    int selectedId;
    int norId;

    public LotteryTitleBean() {
    }

    public LotteryTitleBean(String titleName, int selectedId, int norId) {
        this.titleName = titleName;
        this.selectedId = selectedId;
        this.norId = norId;
    }

    public String getTitleName() {
        return titleName;
    }

    public void setTitleName(String titleName) {
        this.titleName = titleName;
    }

    public int getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(int selectedId) {
        this.selectedId = selectedId;
    }

    public int getNorId() {
        return norId;
    }

    public void setNorId(int norId) {
        this.norId = norId;
    }
}
