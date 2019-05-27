package com.marksixinfo.evenbus;

/**
 *  首页点击当前已选中的tab
 *
 * @Auther: Administrator
 * @Date: 2019/4/11 0011 13:01
 * @Description:
 */
public class MainClickIndexEvent {


    public int currentIndex;

    public MainClickIndexEvent() {
    }

    public MainClickIndexEvent(int currentIndex) {
        this.currentIndex = currentIndex;
    }

}
