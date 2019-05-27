package com.marksixinfo.net.api;

/**
 * 开奖接口
 *
 * @Auther: Administrator
 * @Date: 2019/5/11 0011 19:18
 * @Description:
 */
public interface LotteryApi {


    /**
     * 开奖基础信息
     */
    void getLotteryBaseInfo();


    /**
     * 获取所有年份
     */
    void getAllLotteryYear();


    /**
     * 根据年份获取期数
     *
     * @param year
     */
    void getPeriodByLotteryYear(String year);

    /**
     * 根据期数获取开奖结果
     *
     * @param period 2019050
     */
    void getLotteryResultByPeriod(String period);


    /**
     * 获取统计结果
     *
     * @param id  功能ID
     * @param num 统计期数，默认100
     */
    void getStatisticsResult(String id, String num);


    /**
     * 获取距离开奖秒数
     */
    void getCountdown();

}
