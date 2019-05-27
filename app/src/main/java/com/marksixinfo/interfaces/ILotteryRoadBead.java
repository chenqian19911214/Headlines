package com.marksixinfo.interfaces;

/**
 * 开奖路珠选择球
 *
 * @Auther: Administrator
 * @Date: 2019/5/15 0015 12:47
 * @Description:
 */
public interface ILotteryRoadBead {

    /**
     * 选中第几球
     * @param position 位置
     * @param isCheck 是否选中
     */
    void checkSelect(int position, boolean isCheck);
}
