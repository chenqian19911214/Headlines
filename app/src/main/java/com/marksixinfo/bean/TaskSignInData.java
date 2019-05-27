package com.marksixinfo.bean;

/**
 * 自动签到标识
 *
 * @Auther: Administrator
 * @Date: 2019/4/24 0024 13:40
 * @Description:
 */
public class TaskSignInData {


    public long time;//时间戳
    public String userId;//用户id

    public TaskSignInData() {
    }

    public TaskSignInData(long time, String userId) {
        this.time = time;
        this.userId = userId;
    }
}
