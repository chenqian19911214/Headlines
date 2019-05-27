package com.marksixinfo.bean;

/**
 * 我的好友列表
 *
 * @Auther: Administrator
 * @Date: 2019/4/23 0023 15:57
 * @Description:
 */
public class FriendListDetailData {


    /**
     * "count": "2", //邀请用户数
     * "completed": 100,  //已完成收益
     * "processing": 200  //进行中收益
     */

    private String count;
    private String completed;
    private String processing;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getProcessing() {
        return processing;
    }

    public void setProcessing(String processing) {
        this.processing = processing;
    }

}
