package com.marksixinfo.constants;

/**
 * 开奖url
 *
 * @Auther: Administrator
 * @Date: 2019/5/11 0011 19:14
 * @Description:
 */
public interface NetUrlLottery {


    /**
     * 开奖host
     */
    String LOTTERY_HOST = "http://api.lottery." + StringConstants.Main_Host + "?";


    /**
     * 开奖基础信息
     */
    String LOTTERY_BASE_INFO = LOTTERY_HOST + "C=index";


    /**
     * 获取所有年份
     */
    String GET_ALL_LOTTERY_YEAR = LOTTERY_HOST + "A=get_all_year";


    /**
     * 根据年份获取期数
     */
    String GET_PERIOD_BY_LOTTERY_YEAR = LOTTERY_HOST + "A=get_year_period";

    /**
     * 根据期数获取开奖结果
     */
    String GET_LOTTERY_RESULT_BY_PERIOD = LOTTERY_HOST + "A=get_period_lottery";


    /**
     * 获取统计结果
     */
    String GET_STATISTICS_RESULT = LOTTERY_HOST + "A=get_statis";

    /**
     * 获取距离开奖秒数
     */
    String GET_COUNT_DOWN = LOTTERY_HOST + "A=get_countdown";


}
