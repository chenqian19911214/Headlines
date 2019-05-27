package com.marksixinfo.bean;

import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/3/22 0022 15:31
 * @Description:
 */
public class ReleaseSelectClassify {


    private List<SelectClassifyData> type;
    private List<ReleaseSelectData> period;


    public List<SelectClassifyData> getType() {
        return type;
    }

    public void setType(List<SelectClassifyData> type) {
        this.type = type;
    }


    public List<ReleaseSelectData> getPeriod() {
        return period;
    }

    public void setPeriod(List<ReleaseSelectData> period) {
        this.period = period;
    }
}
