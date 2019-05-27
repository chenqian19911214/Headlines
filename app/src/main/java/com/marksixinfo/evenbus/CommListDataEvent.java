package com.marksixinfo.evenbus;

import com.marksixinfo.bean.MainHomeData;

/**
 * 全局列表页面操作刷新
 *
 * @Auther: Administrator
 * @Date: 2019/3/29 0029 21:33
 * @Description:
 */
public class CommListDataEvent {

    private MainHomeData data;
    private String className;

    public CommListDataEvent() {
    }

    public CommListDataEvent(MainHomeData data, String className) {
        this.data = data;
        this.className = className;
    }

    public CommListDataEvent(MainHomeData data) {
        this.data = data;
    }

    public MainHomeData getData() {
        return data;
    }

    public void setData(MainHomeData data) {
        this.data = data;
    }


    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}