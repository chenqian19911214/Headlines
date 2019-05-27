package com.marksixinfo.net.impl;

import com.marksixinfo.base.BaseNetUtil;
import com.marksixinfo.base.CallbackBase;
import com.marksixinfo.base.NetManagerBase;
import com.marksixinfo.net.api.LotteryApi;

import java.util.HashMap;

import static com.marksixinfo.constants.NetUrlLottery.*;
import static com.marksixinfo.constants.StringConstants.*;

/**
 * 开奖接口实现
 *
 * @Auther: Administrator
 * @Date: 2019/5/11 0011 19:23
 * @Description:
 */
public class LotteryImpl extends NetManagerBase implements LotteryApi {


    public LotteryImpl(CallbackBase callBack) {
        super(callBack);
    }


    /**
     * 开奖基础信息
     */
    @Override
    public void getLotteryBaseInfo() {
        BaseNetUtil.get(LOTTERY_BASE_INFO, null, callBack);
    }


    /**
     * 获取所有年份
     */
    @Override
    public void getAllLotteryYear() {
        BaseNetUtil.get(GET_ALL_LOTTERY_YEAR, null, callBack);
    }


    /**
     * 根据年份获取期数
     *
     * @param year
     */
    @Override
    public void getPeriodByLotteryYear(String year) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, YEAR, year);
        BaseNetUtil.get(GET_PERIOD_BY_LOTTERY_YEAR, params, callBack);
    }

    /**
     * 根据期数获取开奖结果
     *
     * @param period 2019050
     */
    @Override
    public void getLotteryResultByPeriod(String period) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, PERIOD, period);
        BaseNetUtil.get(GET_LOTTERY_RESULT_BY_PERIOD, params, callBack);
    }


    /**
     * 获取统计结果
     *
     * @param id  功能ID
     * @param num 统计期数，默认100
     */
    @Override
    public void getStatisticsResult(String id, String num) {
        HashMap<String, String> params = new HashMap<>();
        BaseNetUtil.putStringParams(params, ID, id);
        BaseNetUtil.putStringParams(params, NUM, num);
        BaseNetUtil.get(GET_STATISTICS_RESULT, params, callBack);
    }


    /**
     * 获取距离开奖秒数
     */
    @Override
    public void getCountdown() {
        BaseNetUtil.get(GET_COUNT_DOWN, null, callBack);
    }
}
