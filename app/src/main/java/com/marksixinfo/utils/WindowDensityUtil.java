package com.marksixinfo.utils;

import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

/**
 * @Auther: Administrator
 * @Date: 2019/5/4 0004 22:25
 * @Description:
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class WindowDensityUtil {
    private static float mSize = 375F;
    private static float mDensity;
    private static float mScaledDensity;

    public static void setCustomDensity(@NonNull Context activity, @NonNull final Application application) {
        DisplayMetrics appDisplayMetrics = application.getResources().getDisplayMetrics();
        if (mDensity == 0) {
            mDensity = appDisplayMetrics.density;
            mScaledDensity = appDisplayMetrics.scaledDensity;
            //注册下 onConfigurationChanged 监听,觖决再返回应用，字体并没有变化
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        mScaledDensity = application.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {
                }
            });
        }

        final float targetDensity = appDisplayMetrics.widthPixels / mSize;
        final float targetScaledDensity = targetDensity * (mScaledDensity / mDensity);
        final int targetDensityDpi = (int) (160 * targetDensity);
        //更改application
        appDisplayMetrics.density = targetDensity;
        appDisplayMetrics.scaledDensity = targetScaledDensity;
        appDisplayMetrics.densityDpi = targetDensityDpi;
        //更改activity
        DisplayMetrics activityMetrics = activity.getResources().getDisplayMetrics();
        activityMetrics.density = targetDensity;
        activityMetrics.scaledDensity = targetScaledDensity;
        activityMetrics.densityDpi = targetDensityDpi;
    }
}
