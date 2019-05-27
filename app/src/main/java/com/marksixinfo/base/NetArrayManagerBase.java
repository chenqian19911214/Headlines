package com.marksixinfo.base;

/**
 * 数组callback
 *
 * @Auther: Administrator
 * @Date: 2019/3/19 0019 19:59
 * @Description:
 */
public class NetArrayManagerBase {

    protected ArrayCallback callBack;

    public NetArrayManagerBase(ArrayCallback callBack) {
        this.callBack = callBack;
    }
}
