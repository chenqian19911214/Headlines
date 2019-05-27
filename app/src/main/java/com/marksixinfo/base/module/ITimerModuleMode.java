package com.marksixinfo.base.module;

/**
 * RecyclerView 中需要定时刷新MODE时所需接口
 */
public interface ITimerModuleMode {
    /**
     * 定时刷新mode中的时间
     * @param time
     */
    void onTimeChange(long time);

    /**
     * 计时是否结束
     */
    boolean onTimeEnd();


    void setIsSend(boolean isSend);
}
