package com.marksixinfo.interfaces;

import android.view.View;

import java.util.Calendar;

/**
 * 防止重复点击
 *
 * @Auther: Administrator
 * @Date: 2019/4/29 0029 13:22
 * @Description:
 */
public abstract class NoDoubleClickListener implements View.OnClickListener {

    private int MIN_CLICK_DELAY_TIME = 500;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            onNoDoubleClick(v);
        }
    }

    protected abstract void onNoDoubleClick(View v);

    public NoDoubleClickListener setLastClickTime(long lastClickTime) {
        this.lastClickTime = lastClickTime;
        return this;
    }

    public long getLastClickTime() {
        return lastClickTime;
    }

    public NoDoubleClickListener setMinClickDelayTime(int time) {
        this.MIN_CLICK_DELAY_TIME = time;
        return this;
    }
}
