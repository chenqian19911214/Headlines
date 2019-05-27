package com.marksixinfo.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lenovo on 2019/5/20.
 * 线程管理类
 */

public class ThreadUtils {
    ExecutorService executorService;
    /**
     * 初始化线程池的
     * @param threadSize 线程的长度
     * */
    public static ThreadUtils initThread(int threadSize){
        return new ThreadUtils(threadSize);
    }
    private ThreadUtils(int threadSize) {
        executorService = Executors.newFixedThreadPool(threadSize);
    }

    /**
     * 添加一个线程
     * */
    public void addRunnable(Runnable runnable){
        if (runnable!=null){
            executorService.execute(runnable); //
        }
    }
}
