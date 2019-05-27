package com.marksixinfo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.marksixinfo.R;
import com.marksixinfo.interfaces.SucceedCallBackListener;


public class TimeDownView extends RelativeLayout implements Runnable {

    protected TextView timedown_day, timedown_hour, timedown_min,
            timedown_second;
    private int[] times;
    private long mday, mhour, mmin, msecond;// 天，小时，分钟，秒
    private boolean run = false; // 是否启动了

    public TimeDownView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    public TimeDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TimeDownView(Context context) {
        super(context);
        initView(context);
    }

    protected void initView(Context context) {
        View.inflate(context, getView(), TimeDownView.this);
        timedown_hour = this.findViewById(R.id.timedown_hour);
        timedown_min = this.findViewById(R.id.timedown_min);
        timedown_second = this.findViewById(R.id.timedown_second);
    }

    /**
     * 返回lyouut id、
     *
     * @return
     */
    public int getView() {
        return R.layout.timedown_layout;
    }

    public int[] getTimes() {
        return times;
    }

    public void setTimes(int[] times) {
        this.times = times;
        mday = times[0];
        mhour = times[1];
        mmin = times[2];
        msecond = times[3];
    }

    public void setDay(int day) {
        mday = day;
    }

    public void setHour(int hour) {
        mhour = hour;
    }

    public void setMin(int min) {
        mmin = min;
    }

    public void setSec(int sec) {
        msecond = sec;
    }

    /**
     * 倒计时计算
     */
    private void ComputeTime() {
        mCurrentMS = mCurrentMS - 1000;
        msecond--;
        if (msecond < 0) {
            mmin--;
            msecond = 59;
            if (mmin < 0) {
                mmin = 59;
                mhour--;
                if (mhour < 0) {
                    // 倒计时结束
                    mhour = 59;
                    mday--;
                }
            }
        }
    }

    public long getCurrentMs() {
        return mCurrentMS;
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }

    @Override
    public void run() {
        ComputeTime();

        String day = mday + "";
        String hour = mhour + "";
        String min = mmin + "";
        String second = msecond + "";
        if (mday < 10) {
            day = "0" + mday;
        }
        if (mhour < 10) {
            hour = "0" + mhour;
        }
        if (mmin < 10) {
            min = "0" + mmin;
        }
        if (msecond < 10) {
            second = "0" + msecond;
        }
        if (timedown_day != null) {
            timedown_day.setText(day + "");
        }
        timedown_hour.setText(hour + "");
        timedown_min.setText(min + "");
        timedown_second.setText(second + "");
        if (currentListener != null) {
            currentListener.succeedCallBack(getCurrentMs());
        }
        if (mhour <= 0 && mmin <= 0 && msecond <= 0) {
            if (onTimeEndListener != null) {
                onTimeEndListener.onEnd();
            }
            stop();
            return;
        }
        if (!isRun()) {//设置时间归零更新
            setStopTimeZero();
            return;
        }
        postDelayed(this, 1000);
    }

    private long mCurrentMS = 0;

    public void formatTime(long ms) {
        this.removeCallbacks(this);
        if (ms <= 0) {
            mday = 0;
            mhour = 0;
            mmin = 0;
            msecond = 0;
            return;
        }
        mCurrentMS = ms;
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        if (timedown_day == null) {
            hour = day * 24 + hour;
        }

        mday = day;
        mhour = hour;
        mmin = minute;
        msecond = second;
        System.out.println("guanting formatTime mhour = "
                + mhour + " mmin = " + mmin + " msecond = " + msecond);

    }

    public void release() {
        timedown_hour = null;
        timedown_day = null;
        timedown_min = null;
    }

    public void stop() {
        setRun(false);
        mCurrentMS = 0;
        mPauseStartTime = 0;
        mPauseEndTime = 0;
    }

    /**
     * 设置归零
     */
    public void setStopTimeZero() {
        if (timedown_day != null) {
            timedown_day.setText("0");
        }
        if (timedown_hour != null
                && timedown_min != null
                && timedown_second != null) {
            timedown_hour.setText("00");
            timedown_min.setText("00");
            timedown_second.setText("00");
        }
    }

    private long mPauseStartTime = 0;
    private long mPauseEndTime = 0;

    public void pause() {
        mPauseStartTime = System.currentTimeMillis();
        setRun(false);
    }

    public void start() {
        setRun(true);
        run();
    }

    public void calTimeDiffAndStart() {
        mPauseEndTime = System.currentTimeMillis();
        long tempTime = mCurrentMS - (mPauseEndTime - mPauseStartTime);
        start(tempTime);
        mPauseStartTime = 0;
        mPauseEndTime = 0;
    }

    public void start(final long time) {
        if (!isRun()) {
            setRun(true);
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    formatTime(time);
                    TimeDownView.this.run();
                }
            }, 1100);
        }


    }

    public void setOnTimeEndListener(OnTimeEndListener onTimeEndListener) {
        this.onTimeEndListener = onTimeEndListener;
    }

    public void setCurrentListener(SucceedCallBackListener<Long> currentListener) {
        this.currentListener = currentListener;
    }

    OnTimeEndListener onTimeEndListener;
    SucceedCallBackListener<Long> currentListener;

    public interface OnTimeEndListener {
        void onEnd();
    }

    public void showTime(long leftTime) {
        converTimeToDate(leftTime);

        String day = mday + "";
        String hour = mhour + "";
        String min = mmin + "";
        String second = msecond + "";
        if (mday < 10) {
            day = "0" + mday;
        }
        if (mhour < 10) {
            hour = "0" + mhour;
        }
        if (mmin < 10) {
            min = "0" + mmin;
        }
        if (msecond < 10) {
            second = "0" + msecond;
        }
        if (timedown_day != null) {
            timedown_day.setText(day + "");
        }
        timedown_hour.setText(hour + "");
        timedown_min.setText(min + "");
        timedown_second.setText(second + "");
    }

    public void converTimeToDate(long leftTime) {
        if (leftTime <= 0) {
            mday = 0;
            mhour = 0;
            mmin = 0;
            msecond = 0;
            return;
        }
        mCurrentMS = leftTime;
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = leftTime / dd;
        long hour = (leftTime - day * dd) / hh;
        long minute = (leftTime - day * dd - hour * hh) / mi;
        long second = (leftTime - day * dd - hour * hh - minute * mi) / ss;
        if (timedown_day == null) {
            hour = day * 24 + hour;
        }
        mday = day;
        mhour = hour;
        mmin = minute;
        msecond = second;
    }


}
