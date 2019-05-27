package com.marksixinfo.evenbus;

import com.marksixinfo.bean.SelectClassifyData;

import java.util.List;

/**
 *  发布选择分类发生变化
 *
 * @Auther: Administrator
 * @Date: 2019/3/22 0022 16:11
 * @Description:
 */
public class SelectClassifyEvent {

    private List<SelectClassifyData> type;

    public SelectClassifyEvent() {
    }

    public SelectClassifyEvent(List<SelectClassifyData> type) {
        this.type = type;
    }

    public List<SelectClassifyData> getType() {
        return type;
    }

    public void setType(List<SelectClassifyData> type) {
        this.type = type;
    }
}
