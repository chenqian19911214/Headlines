package com.marksixinfo.interfaces;


import androidx.annotation.Nullable;

/**
 * 通用简单回调结果
 *
 * @author guangleilei
 * @version 1.0 2017-04-27
 */
public interface SucceedCallBackListener<T> {


    void succeedCallBack(@Nullable T o);
}
