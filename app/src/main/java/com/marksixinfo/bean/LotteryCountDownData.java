package com.marksixinfo.bean;

/**
 *  下期开奖倒计时
 *
 * @Auther: Administrator
 * @Date: 2019/5/21 0021 18:19
 * @Description:
 */
public class LotteryCountDownData {


    /**
     * time : 9877
     * period : 2019056
     */

    private long time;
    private long now;
    private String period;

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }
}
